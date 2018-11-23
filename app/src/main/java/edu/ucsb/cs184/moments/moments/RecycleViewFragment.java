package edu.ucsb.cs184.moments.moments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private boolean showDivider = false;
    private ArrayList<View> hideViews = new ArrayList<>();
    private ArrayList<OnRefreshListener> listeners = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setAdapter(adapter);
        if (showDivider) recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
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
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                for (OnRefreshListener listener : listeners){
                    listener.onRefresh();
                }
            }
        });
//        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
//                android.R.color.holo_green_dark,
//                android.R.color.holo_orange_dark,
//                android.R.color.holo_blue_dark);

        /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {

            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                for (OnRefreshListener listener : listeners){
                    listener.onRefresh();
                }
            }
        });

        return view;
    }

    public void setShowDivider(Boolean bool){
        showDivider = bool;
    }

    public void setAdapter(CustomAdapter adapter){
        this.adapter = adapter;
    }

    public void addHiddenView(View view) { hideViews.add(view); }

    public void addElement(Object obj) throws Exception {
        if (isValidType(obj)) adapter.addElement(obj);
        else throw new Exception("Unsupport Element!");
    }
    public void addElements(List data) throws Exception {
        if (data.size() == 0) return;
        if (isValidType(data.get(0))) adapter.addElements(data);
        else throw new Exception("Unsupport data!");
    }
    public boolean hasData(){
        return adapter.hasData();
    }
    private boolean isValidType(Object obj){
        return obj instanceof Post || obj instanceof Comment || obj instanceof User || obj instanceof Group || obj instanceof String;
    }

    public void clear(){
        adapter.clear();
    }

    public void gotoTop(){
        recyclerView.smoothScrollToPosition(0);
    }

    public void show(FragmentManager manager, int viewId){
        manager.beginTransaction()
               .add(viewId, this)
               .commit();
    }

    public void addOnRefreshListener(OnRefreshListener listener) { listeners.add(listener); }

    public interface OnRefreshListener{
        void onRefresh();
    }
}
