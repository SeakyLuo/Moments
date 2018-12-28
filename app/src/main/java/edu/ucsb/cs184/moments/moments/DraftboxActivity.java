package edu.ucsb.cs184.moments.moments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.jude.swipbackhelper.SwipeBackHelper;

import java.util.ArrayList;

public class DraftboxActivity extends AppCompatActivity {

    private ImageButton back;
    private RecyclerViewFragment fragment;
    private Button clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draftbox);
        SwipeBackHelper.onCreate(this);

        back = findViewById(R.id.draftbox_back);
        clear = findViewById(R.id.draftbox_clear);
        fragment = new RecyclerViewFragment();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AskYesNoDialog dialog = new AskYesNoDialog();
                dialog.showNow(getSupportFragmentManager(), "Clear");
                dialog.setMessage(getString(R.string.clear_draft));
                dialog.setOnYesListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clear.setTextColor(getColor(R.color.aluminum));
                        User.user.clearDrafts();
                        fragment.clear();
                    }
                });
            }
        });
        fragment.setAdapter(new DraftAdapter());
        refresh();
        fragment.addOnRefreshListener(new RecyclerViewFragment.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        fragment.setShowDivider(true);
        fragment.show(getSupportFragmentManager(), R.id.draftbox_content);
    }
    private void refresh(){
        ArrayList<Post> data = User.user.getDrafts();
        fragment.setData(data);
        clear.setTextColor(getColor((data.size() == 0) ? R.color.aluminum : R.color.BlackGray));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;;
        switch (requestCode){
            case EditPostActivity.EDIT_POST:
                refresh();
                break;
        }
    }
}
