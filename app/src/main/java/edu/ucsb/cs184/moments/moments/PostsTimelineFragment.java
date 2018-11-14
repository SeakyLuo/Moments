package edu.ucsb.cs184.moments.moments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;

public class PostsTimelineFragment extends Fragment {

    private Context context;
    private RecyclerView recyclerView;
    private PostsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.posts_timeline, container, false);
        context = getContext();
        recyclerView = view.findViewById(R.id.posts_recyclerview);
        adapter = new PostsAdapter();
        recyclerView.setAdapter(adapter);
        return view;
    }

    public void setPost(String json){
        Gson gson = new Gson();
        adapter.addPost(gson.fromJson(json, Post.class));
    }

    public void show(FragmentManager manager, int replaceId){
        manager.beginTransaction()
               .replace(replaceId, this)
               .commit();
    }
}
