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
import java.util.List;

public class RecyclerViewFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private boolean showDivider = false;
    private boolean swipeEnabled = true;
    private ArrayList<View> hideViews = new ArrayList<>();
    private ArrayList<OnRefreshListener> listeners = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view, container, false);
        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        recyclerView = view.findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setAdapter(adapter);
        if (showDivider) recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy){
                super.onScrolled(recyclerView, dx, dy);
                for (final View view: hideViews){
                    if (view instanceof FloatingActionButton){
                        if (dy > 0 && view.isShown()) ((FloatingActionButton) view).hide();
                        else if (dy <= 0 && !view.isShown()) ((FloatingActionButton) view).show();
                    }else{
                        if (dy > 0 && view.isShown()) view.setVisibility(View.GONE);
                        else if (dy <= 0 && !view.isShown()) view.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        swipeRefreshLayout.setEnabled(swipeEnabled);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                for (OnRefreshListener listener: listeners)
                    listener.onRefresh();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return view;
    }

    public void setShowDivider(boolean showDivider){
        this.showDivider = showDivider;
    }
    public void setAdapter(CustomAdapter adapter){
        this.adapter = adapter;
    }
    public void setSwipeEnabled(boolean swipeEnabled) {
        this.swipeEnabled = swipeEnabled;
    }

    public void addHiddenView(View view) { if (view != null) hideViews.add(view); }

    public void addElement(Object obj) throws UnsupportedDataException {
        if (isValidType(obj)) adapter.addElement(obj);
        else throw new UnsupportedDataException();
    }
    public void addElements(List data) throws UnsupportedDataException {
        if (data.size() == 0 || isValidType(data.get(0))) adapter.addElements(data);
        else throw new UnsupportedDataException();
    }
    public void addElements(int index, List data) throws UnsupportedDataException{
        if (data.size() == 0 || isValidType(data.get(0))) adapter.addElements(index, data);
        else throw new UnsupportedDataException();
    }
    public void removeElement(Object obj) throws UnsupportedDataException{
        if (isValidType(obj)) adapter.removeElement(obj);
        else throw new UnsupportedDataException();
    }
    public void setData(List data) throws UnsupportedDataException{
        if (data.size() == 0 || isValidType(data.get(0))) adapter.setData(data);
        else throw new UnsupportedDataException();
    }
    public void refresh(){
        adapter.notifyDataSetChanged();
        gotoTop();
    }
    public List getData() { return adapter.getData(); }
    public boolean hasData(){
        return adapter.hasData();
    }
    private boolean isValidType(Object obj){
        return obj instanceof Post || obj instanceof Comment || obj instanceof User ||
                obj instanceof Group || obj instanceof String || obj instanceof SearchPair;
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

    public class UnsupportedDataException extends Exception{
        public UnsupportedDataException(){
            super("Unsupported data!");
        }
    }
}
