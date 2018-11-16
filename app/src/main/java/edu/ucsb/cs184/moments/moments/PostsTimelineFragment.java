package edu.ucsb.cs184.moments.moments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class PostsTimelineFragment extends Fragment {

    private Context context;
    private RecyclerView recyclerView;
    private PostsAdapter adapter;
    private ArrayList<View> hideViews = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.posts_timeline, container, false);
        context = getContext();
        recyclerView = view.findViewById(R.id.posts_recyclerview);
        adapter = new PostsAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);
                for (View view : hideViews){
                    if (view instanceof FloatingActionButton){
                        if (dy > 0 && view.isShown()) ((FloatingActionButton) view).hide();
                        else if (dy < 0) ((FloatingActionButton) view).show();
                    }else{
                        if (dy > 0 && view.isShown()) view.setVisibility(View.GONE);
                        else if (dy < 0) view.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        return view;
    }

    public void addHiddenView(View view) { hideViews.add(view); }

    public void setPost(String json){
        Gson gson = new Gson();
        adapter.addPost(gson.fromJson(json, Post.class));
    }

    public void show(FragmentManager manager, int replaceId){
        manager.beginTransaction()
               .add(replaceId, this)
               .commit();
    }
}
