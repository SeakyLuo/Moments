package edu.ucsb.cs184.moments.moments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.jude.swipbackhelper.SwipeBackHelper;

public class UserDraftboxActivity extends AppCompatActivity {

    private ImageButton back;
    private RecyclerViewFragment fragment;
    private Button clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_draftbox);
        SwipeBackHelper.onCreate(this);

        back = findViewById(R.id.ud_back);
        clear = findViewById(R.id.ud_clear);
        fragment = new RecyclerViewFragment();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            }
        });
        setClear();
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
        // TODO: to be implemented
        fragment.setAdapter(new DraftAdapter());
        try {
            fragment.setData(User.user.getDrafts());
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragment.addOnRefreshListener(new RecyclerViewFragment.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fragment.refresh();
            }
        });
        fragment.setShowDivider(true);
        fragment.show(getSupportFragmentManager(), R.id.content_draftbox);
    }

    private void setClear(){
        clear.setTextColor(getColor((User.user.getDrafts().size() == 0) ? R.color.aluminum : R.color.BlackGray));
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
                fragment.refresh();
                setClear();
                break;
        }
    }
}
