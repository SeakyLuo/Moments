package edu.ucsb.cs184.moments.moments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.swipbackhelper.SwipeBackHelper;

public class EditUserProfileActivity extends AppCompatActivity {

    public static final int FINISH = 0;
    public static final String NAME = "name";
    public static final String GENDER = "gender";
    public static final String INTRO = "intro";

    private ImageButton back;
    private ImageView icon;
    private TextView name, gender, intro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        SwipeBackHelper.onCreate(this);

        back = findViewById(R.id.eup_back);
        icon = findViewById(R.id.eup_icon);
        name = findViewById(R.id.eup_name_input);
        gender = findViewById(R.id.eup_gender_input);
        intro = findViewById(R.id.eup_intro_input);

        name.setText(User.user.getName());
        gender.setText(User.user.getGender());
        intro.setText(User.user.getIntro());
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            }
        });
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ui = new Intent(EditUserProfileActivity.this, UploadIconActivity.class);
                ui.putExtra(UploadIconActivity.CALLER, UploadIconActivity.USER);
                ui.putExtra(UploadIconActivity.ICON, ((BitmapDrawable) icon.getDrawable()).getBitmap());
                startActivityForResult(ui, UploadIconActivity.GALLERY_CODE);
            }
        });
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputDialog dialog = new InputDialog();
                dialog.showNow(getSupportFragmentManager(), NAME);
                dialog.setTitle("Edit Name");
                dialog.setContent(User.user.getName());
                dialog.setOnSaveListener(new InputDialog.OnClickListener() {
                    @Override
                    public void onClick(EditText editText) {
                        String name = editText.getText().toString();
                        if (SignupActivity.checkName(editText, name)){
                            setName(name);
                            User.user.modifyName(name);
                        }
                    }
                });
            }
        });
        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GenderDialog dialog = new GenderDialog();
                dialog.showNow(getSupportFragmentManager(), GENDER);
                dialog.setOnConfirmListener(new GenderDialog.OnConfirmListener() {
                    @Override
                    public void onConfirm(String selection) {
                        setGender(selection);
                        User.user.setGender(selection);
                    }
                });
            }
        });
        intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputDialog dialog = new InputDialog();
                dialog.showNow(getSupportFragmentManager(), INTRO);
                dialog.setTitle("Edit Intro");
                dialog.setContent(User.user.getIntro());
                dialog.setOnSaveListener(new InputDialog.OnClickListener() {
                    @Override
                    public void onClick(EditText editText) {
                        String text = editText.getText().toString();
                        setIntro(text);
                        User.user.modifyIntro(text);
                    }
                });
            }
        });
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

    public void setName(String name){
        this.name.setText(name);
    }
    public void setGender(String gender){
        this.gender.setText(gender);
    }
    public void setIntro(String intro){
        this.intro.setText(intro);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        icon.setImageBitmap((Bitmap) data.getParcelableExtra(UploadIconActivity.ICON));
    }

}
