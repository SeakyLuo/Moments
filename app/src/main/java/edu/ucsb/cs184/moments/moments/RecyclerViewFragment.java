package edu.ucsb.cs184.moments.moments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RecyclerViewFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private boolean showDivider = false, swipeEnabled = true, nestedScrolling = true;
    private ArrayList<View> hideViews = new ArrayList<>();
    private ArrayList<OnRefreshListener> listeners = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        recyclerView = view.findViewById(R.id.recyclerview);
        swipeRefreshLayout.setEnabled(swipeEnabled);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                for (OnRefreshListener listener: listeners)
                    listener.onRefresh();
                gotoTop();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        SetRecyclerView(recyclerView);
        return view;
    }

    public RecyclerView getRecyclerView() { return recyclerView; }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        SetRecyclerView(recyclerView);
    }
    private void SetRecyclerView(RecyclerView recyclerView) {
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setNestedScrollingEnabled(nestedScrolling);
        adapter.setActivity(getActivity());
        recyclerView.setAdapter(adapter);
        if (showDivider) recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);
                for (final View view: hideViews){
                    if (view instanceof FloatingActionButton){
                        FloatingActionButton fab = (FloatingActionButton) view;
                        if (dy > 0 && fab.isShown()) fab.hide();
                        else if (dy <= 0 && !fab.isShown()) fab.show();
                    }else{
                        if (dy > 0 && view.isShown()) view.setVisibility(View.GONE);
                        else if (dy <= 0 && !view.isShown()) view.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    public void setNestedScrollingEnabled(boolean enabled){ nestedScrolling = enabled; }
    public void setShowDivider(boolean showDivider){ this.showDivider = showDivider; }
    public void setAdapter(CustomAdapter adapter){ this.adapter = adapter; }
    public void setSwipeEnabled(boolean swipeEnabled) { this.swipeEnabled = swipeEnabled; }

    public void addHiddenView(View view) { if (view != null) hideViews.add(view); }
    public void addElement(Object obj) { adapter.add(obj); }
    public void addElements(List data) { adapter.addAll(data); }
    public void addElements(int index, List data) { adapter.addAll(index, data); }
    public void removeElement(Object obj) { adapter.remove(obj); }
    public void sort(Comparator comparator) { adapter.sort(comparator); }
    public void setData(List data) { adapter.setData(data); }
    public List getData() { return adapter.getData(); }
    public boolean hasData(){ return adapter.hasData(); }
    public void clear(){ adapter.clear(); }

    public void gotoTop(){ if (recyclerView != null) recyclerView.smoothScrollToPosition(0); }

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
