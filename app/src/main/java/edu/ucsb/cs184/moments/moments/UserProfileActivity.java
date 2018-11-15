package edu.ucsb.cs184.moments.moments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.jude.swipbackhelper.SwipeBackHelper;

public class UserProfileActivity extends AppCompatActivity {

    private ImageButton back;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private Intent intent;
    private PostsTimelineFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        SwipeBackHelper.onCreate(this);
        intent = getIntent();

        back = findViewById(R.id.up_back);
        toolbar = findViewById(R.id.up_toolbar);
        collapsingToolbarLayout = findViewById(R.id.up_ctoolbar);
        fragment = new PostsTimelineFragment();
        fragment.show(getSupportFragmentManager(), R.id.up_timeline);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setExpandedTitleColor(getColor(android.R.color.transparent));
        setSupportActionBar(toolbar);
        setUserProfile(intent.getIntExtra("userid", 0));
    }

    private void setUserProfile(int userid){
        collapsingToolbarLayout.setTitle("Username");
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
