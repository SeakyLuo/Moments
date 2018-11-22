package edu.ucsb.cs184.moments.moments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.Date;

public class EditPostActivity extends AppCompatActivity {

    public static final String POST = "Post";

    private ImageButton back;
    private ImageButton send;
    private EditText edit_content;
    private SaveAsDraftFragment saveAsDraftFragment;
    private Class caller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);
        Intent intent = getIntent();
        caller = intent.getClass();

        edit_content = findViewById(R.id.edit_content);
        back = findViewById(R.id.edit_cancel);
        send = findViewById(R.id.edit_send);
        // TODO: if this activity is initiated by UserDraftbox and the post is published, remember to delete from user draftbox
        saveAsDraftFragment = new SaveAsDraftFragment();
        saveAsDraftFragment.setActivity(this);

        edit_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Boolean hasText = s.toString().trim().length() != 0;
                send.setClickable(hasText);
                send.setImageResource(hasText ? R.drawable.ic_send : R.drawable.ic_send_unclickable);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edit_content.getText().toString();
                if (content.length() == 0) finish();
                else {
                    // ask save to draft box
                    saveAsDraftFragment.setPost(getPost());
                    saveAsDraftFragment.show(getSupportFragmentManager(), SaveAsDraftFragment.SAVE_AS_DRAFT);
                }
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edit_content.getText().toString();
                if (content.trim().length() == 0) return;
                Intent intent = new Intent(getApplicationContext(), caller);
                Post post = getPost();
                intent.putExtra(POST, post);
                User.user.make_post(post);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private Post getPost(){
        return new Post(User.user.getUserid(), edit_content.getText().toString(), new Date());
    }
}
