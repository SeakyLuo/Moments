package edu.ucsb.cs184.moments.moments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jude.swipbackhelper.SwipeBackHelper;

import java.util.ArrayList;

public class SearchListActivity extends AppCompatActivity {

    public static final String ADAPTER = "adapter", DATA = "data";
    public static final String POST = "Posts", FOLLOWER = "Followers", FOLLOWING = "Following", MEMBER = "Members";

    private EditText searchBar;
    private ImageButton backButton, clearButton;
    private Intent intent;
    private RecyclerViewFragment fragment;
    private String adapter;
    private ArrayList data = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_list);
        SwipeBackHelper.onCreate(this);
        intent = getIntent();
        adapter = intent.getStringExtra(ADAPTER);

        backButton = findViewById(R.id.sl_back);
        clearButton = findViewById(R.id.sl_clear);
        searchBar = findViewById(R.id.sl_text);
        fragment = new RecyclerViewFragment();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            }
        });
        clearButton.setVisibility(View.GONE);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.setText("");
                clearButton.setVisibility(View.GONE);
                switch (adapter) {
                    case MEMBER:
                        fragment.setData(data);
                        break;
                    case FOLLOWER:
                        fragment.setData(data);
                        break;
                    case FOLLOWING:
                        fragment.setData(data);
                        break;
                    case POST:
                        fragment.setData(new ArrayList<Post>());
                        break;
                }
            }
        });
        searchBar.setHint("Search " + adapter);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                clearButton.setVisibility((s.toString().isEmpty()) ? View.GONE : View.VISIBLE);
            }
        });
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search(v.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });
        switch (adapter){
            case MEMBER:
                setUsers();
                break;
            case FOLLOWER:
                setUsers();
                break;
            case FOLLOWING:
                setUsers();
                break;
            case POST:
                setPosts();
                break;
        }
        fragment.show(getSupportFragmentManager(), R.id.sl_content);
    }

    private void setUsers() {
        fragment.setAdapter(new UserAdapter());
        for (String id: intent.getStringArrayListExtra(DATA))
            data.add(User.findUser(id));
        fragment.setData(data);
    }

    private void setPosts() {
        fragment.setAdapter(new PostAdapter());
        data = intent.getParcelableArrayListExtra(DATA);
        fragment.setData(new ArrayList<Post>());
    }

    private void search(String keyword){
        switch (adapter){
            case MEMBER:
                fragment.setData(SearchActivity.searchUsers(keyword));
                break;
            case FOLLOWER:
                fragment.setData(SearchActivity.searchUsers(keyword));
                break;
            case FOLLOWING:
                fragment.setData(SearchActivity.searchUsers(keyword));
                break;
            case POST:
                fragment.setData(SearchActivity.searchPosts(keyword));
                break;
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
