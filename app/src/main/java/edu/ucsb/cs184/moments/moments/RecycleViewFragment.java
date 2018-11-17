package edu.ucsb.cs184.moments.moments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.ArrayList;

public class RecycleViewFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private boolean showDivider = false;
    private ArrayList<View> hideViews = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setAdapter(adapter);
        if (showDivider) recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);
                for (View view : hideViews){
                    if (view instanceof FloatingActionButton){
                        android.util.Log.d("fuck","hey");
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

    public void setShowDivider(Boolean bool){
        showDivider = bool;
    }

    public void setAdapter(RecyclerView.Adapter adapter){
        this.adapter = adapter;
    }

    public void addHiddenView(View view) { hideViews.add(view); }

    public void setPost(String json){
        ((PostsAdapter) adapter).addPost((new Gson()).fromJson(json, Post.class));
    }

    public void show(FragmentManager manager, int viewId){
        manager.beginTransaction()
               .add(viewId, this)
               .commit();
    }
}
