package edu.ucsb.cs184.moments.moments;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class CustomAdapter<T> extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
    List<T> data = new ArrayList<>();
    Activity activity;

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.CustomViewHolder holder, int position) {
        holder.data = data.get(position);
        holder.setData();
    }

    public void setActivity(Activity activity) { this.activity = activity; }

    public void setData(List<T> data){
        this.data = data;
        notifyDataSetChanged();
    }
    private void addOne(T object){
        data.add(0, object);
    }
    public void addAll(List<T> data){
        for (int i = 0; i < data.size(); i++) addOne(data.get(i));
        notifyItemRangeInserted(0, data.size());
    }
    public void addAll(int index, List<T> data){
        this.data.addAll(index, data);
        notifyItemRangeInserted(index, getItemCount());
    }
    public void add(T object){
        addOne(object);
        notifyItemInserted(0);
    }
    public void remove(T object){
        int index = data.indexOf(object);
        if (index > -1){
            data.remove(object);
            notifyItemRemoved(index);
        }
    }
    public void sort(Comparator<? super T> comparator) {
        Collections.sort(data, comparator);
        notifyItemRangeChanged(0, getItemCount());
    }
    // Shouldn't be called
    public List<T> getData(){ return data; }
    public boolean hasData(){ return data.size() > 0; }
    public void clear(){
        final int size = data.size();
        data.clear();
        notifyItemRangeRemoved(0, size);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    abstract class CustomViewHolder extends RecyclerView.ViewHolder{
        protected View view;
        protected Context context;
        protected T data;
        CustomViewHolder(@NonNull View view) {
            super(view);
            this.view = view;
            this.context = view.getContext();
        }
        abstract void setData();
    }
}