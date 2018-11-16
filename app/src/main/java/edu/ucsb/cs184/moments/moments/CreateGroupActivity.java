package edu.ucsb.cs184.moments.moments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class CreateGroupActivity extends AppCompatActivity {

    private ImageView icon;
    private ImageButton camera;
    private ImageButton cancel;
    private ImageButton finish;
    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        icon = findViewById(R.id.cg_group_icon);
        camera = findViewById(R.id.cg_camera);
        cancel = findViewById(R.id.cg_cancel);
        finish = findViewById(R.id.cg_finish);
        name = findViewById(R.id.cg_groupname);

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IconHelper.Gallery();
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IconHelper.Camera();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = name.getText().toString();
                if (content.trim().length() == 0) return;
                Group group = new Group(name.getText().toString(), UserInfo.user.getUserid());
                Intent intent = new Intent(CreateGroupActivity.this, GroupsFragment.class);
                intent.putExtra(GroupsFragment.GROUP, group.toString());
                clear();
            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Boolean hasText = s.toString().trim().length() != 0;
                finish.setClickable(hasText);
                finish.setImageResource(hasText ? R.drawable.ic_tick : R.drawable.ic_tick_unclickable);
            }
        });
    }
    private void clear(){
        icon.setImageBitmap(null);
        name.setText("");
        finish();
    }
}
