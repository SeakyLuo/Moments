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

import java.util.ArrayList;

public class FollowListActivity extends AppCompatActivity {

    public static final String TITLE = "title", LIST = "list";
    public static final String FOLLOWER = "Followers", FOLLOWING = "Following";

    private EditText searchBar;
    private TabPagerAdapter adapter;
    private ImageButton backButton, clearButton;
    private Intent intent;
    private RecyclerViewFragment fragment;
    private String title;
    private ArrayList<User> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_list);
        intent = getIntent();
        title = intent.getStringExtra(TITLE);

        backButton = findViewById(R.id.search_back);
        clearButton = findViewById(R.id.search_clear);
        searchBar = findViewById(R.id.search_text);
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
                    fragment.setData(list);
                } catch (RecyclerViewFragment.UnsupportedDataException e) {
                    e.printStackTrace();
                }
            }
        });
        searchBar.setHint("Search " + title);
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
        fragment.setAdapter(new UserAdapter());
        for (String id: intent.getStringArrayListExtra(LIST))
            list.add(User.findUser(id));
        try {
            fragment.setData(list);
        } catch (RecyclerViewFragment.UnsupportedDataException e) {
            e.printStackTrace();
        }
        fragment.show(getSupportFragmentManager(), R.id.fl_content);
    }

    private void search(String keyword){
        ArrayList<User> data = new ArrayList<>();
        for (User user: list)
            if ((user.getName().contains(keyword) || Integer.toString(user.getNumber()).contains(keyword)))
                data.add(user);
        try {
            fragment.setData(data);
        } catch (RecyclerViewFragment.UnsupportedDataException e) {
            e.printStackTrace();
        }
    }
}
