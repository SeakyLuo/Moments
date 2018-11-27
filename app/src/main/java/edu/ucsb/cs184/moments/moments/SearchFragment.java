package edu.ucsb.cs184.moments.moments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SearchFragment extends Fragment {

    private Context context;
    private RecyclerViewFragment fragment;
    private String adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);
        context = getContext();
        fragment = new RecyclerViewFragment();
        return view;
    }

    public SearchFragment setAdapter(String adapter){
        this.adapter = adapter;
        switch (adapter){
            case SearchActivity.POSTS:
                fragment.setAdapter(new PostsAdapter());
            case SearchActivity.USERS:
                fragment.setAdapter(new SearchUsersAdapter());
            case SearchActivity.GROUPS:
                fragment.setAdapter(new SearchGroupsAdapter());
            case SearchActivity.HISTORY:
                fragment.setAdapter(new SearchHistoryAdapter());
        }
        fragment.show(getFragmentManager(), R.id.search_result_content);
        return this;
    }

    public void search(final String keyword){
        fragment.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    switch (adapter){
                        case SearchActivity.POSTS:
                            fragment.addElements(FirebaseHelper.searchPosts(keyword));
                        case SearchActivity.USERS:
                            fragment.addElements(FirebaseHelper.searchUsers(keyword));
                        case SearchActivity.GROUPS:
                            fragment.addElements(FirebaseHelper.searchGroups(keyword));
                        case SearchActivity.HISTORY:
                            if (User.user.getSearchHistory().contains(keyword)){
                                User.user.removeHistory(keyword);
                                User.user.addHistory(keyword);
                            }
                            fragment.addElements(User.user.getSearchHistory());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
