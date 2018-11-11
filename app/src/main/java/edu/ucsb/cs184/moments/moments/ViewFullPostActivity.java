package edu.ucsb.cs184.moments.moments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewFullPostActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private FullPostViewPager fullPostViewPager;
    private ImageButton backButton;
    private ImageView usericon;
    private TextView username;
    private TextView time;
    private TextView content;
    private ImageButton comment;
    private ImageButton collect;
    private ImageButton share;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullpost_view);
        intent = getIntent();
        Post post = Post.toPost(intent.getStringExtra("Post"));
        setPost(post);

        backButton = findViewById(R.id.fullpost_back);
        mViewPager = findViewById(R.id.fullpost_viewpager);
        mTabLayout = findViewById(R.id.fullpost_tablayout);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent();
                setResult(RESULT_OK, backIntent);
                finish();
            }
        });
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        fullPostViewPager = new FullPostViewPager(getSupportFragmentManager());
        fullPostViewPager.addFragment(new FullPostCommetsFragment(), "Comments");
        fullPostViewPager.addFragment(new FullPostRatingsFragment(), "Ratings");
        mViewPager.setAdapter(fullPostViewPager);
    }

    private void setPost(Post post){
        usericon = findViewById(R.id.fullpost_usericon);
        username = findViewById(R.id.fullpost_username);
        time = findViewById(R.id.fullpost_time);
        content = findViewById(R.id.fullpost_content);
        comment = findViewById(R.id.fullpost_comment);
        collect = findViewById(R.id.fullpost_collect);
        share = findViewById(R.id.fullpost_share);
        usericon.setClickable(true);
        usericon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        username.setText("Seaky");
        time.setText(TimeText(post.getDate()));
        content.setText(post.getContent());
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    setCollect(post.isCollected(userid));
                setCollect(true);
            }
        });
    }

    public static String TimeText(Date date){
        Date now = new Date();
        long delta_sec = (now.getTime() - date.getTime()) / 1000;
        if (delta_sec == 0) return "Just now";
        long delta_min = delta_sec / 60;
        long delta_hour = delta_min / 60;;
        long delta_day = delta_hour / 24;
        long delta_month = delta_day / 30;
        long delta_year = delta_day / 365;
        if (delta_year >= 1) return new SimpleDateFormat("yyyy-MM-dd").format(date);
        else if (delta_month >= 1) return new SimpleDateFormat("MM-dd").format(date);
        return new SimpleDateFormat("MM-dd HH:mm").format(date);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setCollect(boolean collected){
        collect.setImageResource(collected ? R.drawable.ic_collected : R.drawable.ic_collection);
    }
}
