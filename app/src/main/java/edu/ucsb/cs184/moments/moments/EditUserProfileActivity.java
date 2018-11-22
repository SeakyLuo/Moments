package edu.ucsb.cs184.moments.moments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.swipbackhelper.SwipeBackHelper;

public class EditUserProfileActivity extends AppCompatActivity {

    public static final int FINISH = 0;

    private ImageButton back;
    private ImageView icon;
    private TextView name, gender, intro;

    private NameFragment fname;
    private GenderFragment fgender;
    private IntroFragment fintro;

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

        fname = new NameFragment();
        fgender = new GenderFragment();
        fintro = new IntroFragment();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ui = new Intent(EditUserProfileActivity.this, UploadIconActivity.class);
                startActivityForResult(ui, UploadIconActivity.galleryCode);
            }
        });
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;

    }

    public static class NameFragment extends DialogFragment{

    }

    public static class GenderFragment extends DialogFragment{

    }

    public static class IntroFragment extends DialogFragment{

    }
}
