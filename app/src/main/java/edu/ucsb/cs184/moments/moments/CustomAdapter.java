package edu.ucsb.cs184.moments.moments;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
    protected List<Object> data = new ArrayList<>();
    protected Activity activity;
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.setData(data.get(position));
        if (activity != null) holder.setActivity(activity);
    }

    public void setActivity(Activity activity) { this.activity = activity; }

    public void setData(List data){
        this.data = data;
        notifyDataSetChanged();
    }
    private void add_element(Object object){
        data.add(0, object);
    }
    public void addElements(List data){
        for (int i = 0; i < data.size(); i++) add_element(data.get(i));
        notifyItemRangeInserted(0, data.size());
//        notifyDataSetChanged();
    }
    public void addElements(int index, List data){
        this.data.addAll(index, data);
        notifyItemRangeInserted(index, data.size());
//        notifyDataSetChanged();
    }
    public void addElement(Object object){
        add_element(object);
        notifyItemInserted(0);
    }
    public void removeElement(Object object){
        data.remove(object);
        notifyDataSetChanged();
    }
    // Shouldn't be called
    public List<Object> getData(){
        return data;
    }
    public boolean hasData(){
        return data.size() > 0;
    }
    public void clear(){
//        int size = data.size();
//        data.clear();
//        notifyItemRangeRemoved(0, size);
        data.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static abstract class CustomViewHolder extends RecyclerView.ViewHolder{
        protected View view;
        protected Context context;
        protected Activity activity;
        public CustomViewHolder(@NonNull View view) {
            super(view);
            this.view = view;
            this.context = view.getContext();
            this.activity = (Activity) this.context;
        }
        public void setActivity(Activity activity) { this.activity = activity; }
        public abstract void setData(Object object);
    }
}