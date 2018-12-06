package edu.ucsb.cs184.moments.moments;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {
    protected List<Object> data = new ArrayList<>();
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {
        holder.setData(data.get(position));
    }

    public void setData(List data){
        this.data = data;
        notifyDataSetChanged();
    }
    private void add_element(Object object){
        data.add(0, object);
    }
    public void addElements(List data){
        for (int i = 0; i < data.size(); i++) add_element(data.get(i));
        notifyDataSetChanged();
    }
    public void addElement(Object object){
        add_element(object);
        notifyItemInserted(0);
    }
    public void removeElement(Object object){
        int index = data.indexOf(object);
        removeElementAt(index);
    }
    public void removeElementAt(int index){
        data.remove(index);
        notifyItemRemoved(index);
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
        public CustomViewHolder(@NonNull View view) {
            super(view);
            this.view = view;
        }
        public abstract void setData(Object object);
    }
}