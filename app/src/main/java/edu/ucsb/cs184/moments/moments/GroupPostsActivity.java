package edu.ucsb.cs184.moments.moments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.jude.swipbackhelper.SwipeBackHelper;

public class GroupPostsActivity extends AppCompatActivity {

    public static final int REQUEST_POST = 0;

    private Context context;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private BottomNavigationView nav;
    private TextView title;
    private ImageView icon;
    private ImageButton back;
    private ImageButton more;
    private FloatingActionButton fab;
    private RecyclerViewFragment fragment;
    private Intent data;
    private Group group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_posts);
        SwipeBackHelper.onCreate(this);
        data = getIntent();
        group = FirebaseHelper.findGroup(data.getStringExtra(GroupsFragment.GROUP));

        fab = findViewById(R.id.gp_fab);
        back = findViewById(R.id.gp_back);
        more = findViewById(R.id.gp_more);
        title = findViewById(R.id.gp_title);
        icon = findViewById(R.id.gp_icon);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditPostActivity.class);
                startActivityForResult(intent, REQUEST_POST);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupPostsActivity.this, GroupSettingsActivity.class);
                intent.putExtra("", group);
                startActivity(intent);
            }
        });
        title.setText(group.getName());
        icon.setImageBitmap(group.getIcon());
        fragment = new RecyclerViewFragment();
        fragment.setAdapter(new PostsAdapter());
        fragment.addHiddenView(fab);
        fragment.show(getSupportFragmentManager(), R.id.gp_content);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's state here
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == REQUEST_POST){
            try {
                fragment.addElement(data.getSerializableExtra(EditPostActivity.POST));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
