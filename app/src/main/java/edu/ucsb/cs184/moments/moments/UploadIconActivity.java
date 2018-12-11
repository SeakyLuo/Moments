package edu.ucsb.cs184.moments.moments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.jude.swipbackhelper.SwipeBackHelper;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadIconActivity extends AppCompatActivity {

    public static final int CAMERA_CODE = 0;
    public static final int GALLERY_CODE = 1;
    public static final int ICON_CODE = 2;
    public static final String imagePath = "Moments";
    public static final String ICON = "Icon", GROUP = "Group", USER = "User", CALLER = "Caller";

    private Context context;
    private Intent callerIntent;
    private ImageButton back;
    private ImageView icon;
    private ImageButton camera;
    private String imageFileName;
    public static Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_usericon);
        SwipeBackHelper.onCreate(this);
        context = getApplicationContext();
        callerIntent = getIntent();

        back = findViewById(R.id.uu_back);
        icon = findViewById(R.id.uu_usericon);
        camera = findViewById(R.id.uu_camera);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            }
        });
//        group = callerIntent.getParcelableExtra(GROUP);
        if (group == null)
            icon.setImageBitmap((Bitmap) callerIntent.getParcelableExtra(ICON));
        else
            FirebaseHelper.setIcon(group.GetIcon(), this, icon);
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
    }

    private void openGallery(){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_CODE);
        }else{
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, GALLERY_CODE);
        }
    }

    private void openCamera(){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, CAMERA_CODE);
        }else{
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, getPhotoFileUri(newImageFileName()));
            // Ensure that there's a camera activity to handle the intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, CAMERA_CODE);
            }
        }
    }

    private String newImageFileName(){
        // Create an image file name
        imageFileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".jpg";
        return imageFileName;
    }

    // Returns the Uri for a photo stored on disk given the fileName
    private Uri getPhotoFileUri(String fileName) {
        // Only continue if the SD Card is mounted
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            // Get safe storage directory for photos
            // Use `getExternalFilesDir` on Context to access package-specific directories.
            // This way, we don't need to request external read/write runtime permissions.
            File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), imagePath);
            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
                Log.d("fuck", "failed to create directory");
            }
            // Return the file target for the photo based on filename
            File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
            // wrap File object into a content provider, required for API >= 24
            return FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", file);
        }
        return null;
    }

    public static int followImage(String id){
        return User.user.mutualFollow(id) ? R.drawable.ic_mutual :
                User.user.isFollowing(id) ? R.drawable.ic_unfollow : R.drawable.ic_follow;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length == 0 || grantResults[0] == PackageManager.PERMISSION_DENIED)
            return;
        switch (requestCode){
            case CAMERA_CODE:
                openCamera();
                break;
            case GALLERY_CODE:
                openGallery();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        Uri imageUri = null;
        String path = "";
        Bitmap image= null;
        switch (requestCode){
            case CAMERA_CODE:
                imageUri = getPhotoFileUri(imageFileName);
                path = imageUri.getPath();
                path = path.substring(path.indexOf("/storage"));
                image = BitmapFactory.decodeFile(path);
                break;
            case GALLERY_CODE:
                imageUri = data.getData();
                path = getPath(context, imageUri);
                try {
                    image = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        icon.setImageBitmap(image);
        switch (callerIntent.getStringExtra(CALLER)){
            case USER:
                User.user.modifyIcon(image);
                break;
            case GROUP:
                group.modifyIcon(image);
                break;
        }
        Intent intent = new Intent();
        intent.putExtra(ICON, image);
        setResult(RESULT_OK, intent);
    }

    public static String getPath(Context context, Uri uri) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null){
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }
}
