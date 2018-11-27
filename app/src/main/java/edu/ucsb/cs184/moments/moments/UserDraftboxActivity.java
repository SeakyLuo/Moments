package edu.ucsb.cs184.moments.moments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class UserDraftboxActivity extends AppCompatActivity {

    private ImageButton back;
    private RecyclerViewFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_draftbox);

        back = findViewById(R.id.ud_back);
        fragment = new RecyclerViewFragment();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //Adapter to be implemented
        fragment.setAdapter(new PostsAdapter());
        try {
            fragment.addElements(User.user.getDrafts());
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragment.show(getSupportFragmentManager(), R.id.content_draftbox);
    }
}
