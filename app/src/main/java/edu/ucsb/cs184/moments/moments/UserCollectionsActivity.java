package edu.ucsb.cs184.moments.moments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class UserCollectionsActivity extends AppCompatActivity {

    private ImageButton back;
    private RecycleViewFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_collections);

        back = findViewById(R.id.uc_back);
        fragment = new RecycleViewFragment();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        fragment.setAdapter(new PostsAdapter());
        try {
            fragment.addElements(User.user.getCollections());
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragment.show(getSupportFragmentManager(), R.id.content_collections);
    }
}
