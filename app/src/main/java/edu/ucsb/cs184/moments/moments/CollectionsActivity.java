package edu.ucsb.cs184.moments.moments;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;

import com.jude.swipbackhelper.SwipeBackHelper;

public class CollectionsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageButton back;
    private RecyclerViewFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);
        SwipeBackHelper.onCreate(this);

        toolbar = findViewById(R.id.collections_toolbar);
        back = findViewById(R.id.collections_back);
        fragment = new RecyclerViewFragment();

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.gotoTop();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            }
        });
        fragment.setAdapter(new PostAdapter());
        try {
            fragment.setData(User.user.getCollections());
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragment.addOnRefreshListener(new RecyclerViewFragment.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    fragment.setData(User.user.getCollections());
                } catch (RecyclerViewFragment.UnsupportedDataException e) {
                    e.printStackTrace();
                }
            }
        });
        fragment.show(getSupportFragmentManager(), R.id.collections_content);
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
