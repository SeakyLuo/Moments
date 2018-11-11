package edu.ucsb.cs184.moments.moments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Date;

public class EditPostActivity extends AppCompatActivity {

    private ImageButton back;
    private ImageButton send;
    private EditText edit_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_post);

        edit_content = findViewById(R.id.edit_content);
        back = findViewById(R.id.edit_back);
        send = findViewById(R.id.edit_send);

        edit_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Boolean hasText = s.length() != 0;
                send.setClickable(hasText);
                send.setImageResource(hasText ? R.drawable.ic_send : R.drawable.ic_send_unclickable);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edit_content.getText().toString();
                if (content.length() != 0){
                    // ask save to draft box
                }
                finish();
            }
        });
        send.setClickable(false);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomeFragment.class);
                intent.putExtra("Post", new Post(0, edit_content.getText().toString(), new Date()).toString());
                setResult(EditPostActivity.RESULT_OK, intent);
                finish();
            }
        });
    }
}
