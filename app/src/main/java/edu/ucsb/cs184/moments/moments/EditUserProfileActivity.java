package edu.ucsb.cs184.moments.moments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jude.swipbackhelper.SwipeBackHelper;

public class EditUserProfileActivity extends AppCompatActivity {

    public static final int FINISH = 0;
    public static final String NAME = "name";
    public static final String GENDER = "gender";
    public static final String INTRO = "intro";

    private ImageButton back;
    private ImageView icon;
    private static TextView name, gender, intro;

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
                TextFragment fragment = new TextFragment();
                fragment.setTitle("Edit Name");
                fragment.setContent(User.user.getName());
                fragment.show(getSupportFragmentManager(), NAME);
            }
        });
        gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GenderFragment fragment = new GenderFragment();
                fragment.show(getSupportFragmentManager(), GENDER);
            }
        });
        intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextFragment fragment = new TextFragment();
                fragment.setTitle("Edit Intro");
                fragment.setContent(User.user.getIntro());
                fragment.show(getSupportFragmentManager(), NAME);
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

    public static void setName(String name){
        EditUserProfileActivity.name.setText(name);
    }
    public static void setGender(String gender){
        EditUserProfileActivity.gender.setText(gender);
    }
    public static void setIntro(String intro){
        EditUserProfileActivity.intro.setText(intro);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        Intent intent = getIntent();
        icon.setImageBitmap((Bitmap) intent.getParcelableExtra(UploadIconActivity.ICON));
    }

    public static class TextFragment extends DialogFragment{
        private TextView title;
        private EditText text;
        private Button cancel, save;
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_edit_text,container,false);
            cancel = view.findViewById(R.id.et_cancel);
            save = view.findViewById(R.id.et_save);
            title = view.findViewById(R.id.et_title);
            text = view.findViewById(R.id.et_editText);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String title_text = title.getText().toString();
                    String content = text.getText().toString();
                    if (title_text.contains("Name")){
                        EditUserProfileActivity.setName(content);
                        User.user.setName(content);
                    }else if (title_text.contains("Intro")){
                        EditUserProfileActivity.setIntro(content);
                        User.user.setIntro(content);
                    }
                    dismiss();
                }
            });
            return view;
        }

        public void setTitle(String text){
            title.setText(text);
        }

        public void setContent(String content){
            text.setText(content);
        }
    }

    public static class GenderFragment extends DialogFragment{
        public final String MALE = getString(R.string.male);
        public final String FEMALE = getString(R.string.female);
        public final String UNKNOWN = getString(R.string.unknown);
        private RadioGroup radioGroup;
        private Button cancel, confirm;
        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.dialog_select_gender,container,false);
            radioGroup = view.findViewById(R.id.sg_radio_group);
            cancel = view.findViewById(R.id.sg_cancel);
            confirm = view.findViewById(R.id.sg_confirm);
            if (User.user.getGender().equals(MALE)){
                ((RadioButton) view.findViewById(R.id.sg_rb_male)).setChecked(true);
            }else if (User.user.getGender().equals(FEMALE)){
                ((RadioButton) view.findViewById(R.id.sg_rb_female)).setChecked(true);
            }else if (User.user.getGender().equals(UNKNOWN)){
                ((RadioButton) view.findViewById(R.id.sg_rb_unknown)).setChecked(true);
            }
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (radioGroup.getCheckedRadioButtonId()){
                        case R.id.sg_rb_male:
                            EditUserProfileActivity.setGender(MALE);
                            User.user.setGender(MALE);
                        case R.id.sg_rb_female:
                            EditUserProfileActivity.setGender(FEMALE);
                            User.user.setGender(FEMALE);
                        case R.id.sg_rb_unknown:
                            EditUserProfileActivity.setGender(UNKNOWN);
                            User.user.setGender(UNKNOWN);
                    }
                }
            });
            return view;
        }
    }
}
