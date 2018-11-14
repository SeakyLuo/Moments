package edu.ucsb.cs184.moments.moments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class UserCollectionsActivity extends AppCompatActivity {
    private ImageButton back;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_collections);

        back = findViewById(R.id.uc_back);

        intent = getIntent();
        setUserCollections(intent.getIntExtra("userid", 0));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setUserCollections(int userid){

    }
}
