package edu.ucsb.cs184.moments.moments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CommentsFragment extends NotificationFragment {

    private Context context;
    private RecyclerViewFragment fragment;
    private BottomNavigationView nav;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comments, container, false);
        context = getContext();
        fragment = new RecyclerViewFragment();
        fragment.setAdapter(new CommentAdapter());
        refresh();
        fragment.addOnRefreshListener(new RecyclerViewFragment.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        if (nav != null) fragment.addHiddenView(nav);
        fragment.show(getFragmentManager(), R.id.comments_content);
        return view;
    }

    @Override
    public void refresh(){
        try {
            User.user.refreshCommentsRecv();
            fragment.setData(User.user.getCommentsRecv());
        } catch (RecyclerViewFragment.UnsupportedDataException e) {
            e.printStackTrace();
        }
    }
}
