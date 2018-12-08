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
    public static final String POST = "Posts", FOLLOWER = "Followers", FOLLOWING = "Following";

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
                try {
                    fragment.setData(data);
                } catch (RecyclerViewFragment.UnsupportedDataException e) {
                    e.printStackTrace();
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
        try {
            switch (adapter){
                case FOLLOWER:
                    fragment.setAdapter(new UserAdapter());
                    for (String id: intent.getStringArrayListExtra(DATA))
                        data.add(User.findUser(id));
                    fragment.setData(data);
                    break;
                case FOLLOWING:
                    fragment.setAdapter(new UserAdapter());
                    for (String id: intent.getStringArrayListExtra(DATA))
                        data.add(User.findUser(id));
                    fragment.setData(data);
                    break;
                case POST:
                    fragment.setAdapter(new PostAdapter());
                    data = intent.getParcelableArrayListExtra(DATA);
                    fragment.setData(new ArrayList<Post>());
                    break;
            }
        } catch (RecyclerViewFragment.UnsupportedDataException e) {
            e.printStackTrace();
        }
        fragment.show(getSupportFragmentManager(), R.id.sl_content);
    }

    private void search(String keyword){
        ArrayList list = new ArrayList<>();
        switch (adapter){
            case FOLLOWER:
                list.add(searchUser(keyword));
                list.add(searchUser(keyword.toLowerCase()));
                break;
            case FOLLOWING:
                list.add(searchUser(keyword));
                list.add(searchUser(keyword.toLowerCase()));
                break;
            case POST:
                list.add(searchPost(keyword));
                list.add(searchPost(keyword.toLowerCase()));
                break;
        }
        try {
            fragment.setData(list);
        } catch (RecyclerViewFragment.UnsupportedDataException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<User> searchUser(String keyword){
        ArrayList<User> list = new ArrayList<>();
        for (User obj: (ArrayList<User>) data)
            if ((obj.getName().contains(keyword) || Integer.toString(obj.getNumber()).contains(keyword)))
                list.add(obj);
        return list;
    }

    private ArrayList<Post> searchPost(String keyword){
        ArrayList<Post> list = new ArrayList<>();
        for (Post obj: (ArrayList<Post>) data)
            if (obj.getContent().contains(keyword))
                list.add(obj);
        return list;
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
