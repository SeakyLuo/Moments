package edu.ucsb.cs184.moments.moments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuBuilder;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FullPostCommentsFragment extends Fragment {

    private Context context;
    private Button sortby_button;
    private TextView sortby_text;
    private RecyclerViewFragment fragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_full_post_comments, container, false);
        context = getContext();
        sortby_button = view.findViewById(R.id.fpc_sortby_button);
        sortby_text = view.findViewById(R.id.fpc_sortby_text);
        fragment = new RecyclerViewFragment();

        fragment.setAdapter(new CommentsAdapter());
        fragment.setShowDivider(true);
        fragment.show(getFragmentManager(), R.id.fpc_content);
        sortby_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenuHelper helper = new PopupMenuHelper(R.menu.sort_by_menu, context, sortby_button);
                helper.setOnItemSelectedListener(new PopupMenuHelper.onItemSelectListener() {
                    @Override
                    public boolean onItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.sb_highest_rating:
                                sortby_text.setText("Sort By " + getString(R.string.highest_rating));
                                return true;
                            case R.id.sb_lowest_rating:
                                sortby_text.setText("Sort By " + getString(R.string.lowest_rating));
                                return true;
                            case R.id.sb_lastest:
                                sortby_text.setText("Sort By " + getString(R.string.most_recent));
                                return true;
                            case R.id.sb_oldest:
                                sortby_text.setText("Sort By " + getString(R.string.least_recent));
                                return true;
                        }
                        return false;
                    }
                });
                helper.show();
            }
        });

        return view;
    }

    public void addComment(Comment comment){
        try {
            fragment.addElement(comment);
        } catch (RecyclerViewFragment.UnsupportedDataException e) {
            e.printStackTrace();
        }
    }

}
