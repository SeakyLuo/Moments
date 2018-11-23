package edu.ucsb.cs184.moments.moments;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchGroupsAdapter extends CustomAdapter{
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_search_group, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public static class ViewHolder extends CustomAdapter.CustomViewHolder {
        TextView name, number;
        ImageButton join;
        ImageView icon;
        Group data;
        public ViewHolder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.sg_name);
            number = view.findViewById(R.id.sg_number);
            join = view.findViewById(R.id.sg_join);
            icon = view.findViewById(R.id.sg_icon);
        }

        @Override
        public void setData(Object object){
            data = (Group) object;
            name.setText(data.getName());
            number.setText(data.getNumber());
            if (User.user.inGroup(data.getId())) join.setVisibility(View.INVISIBLE);
            else join.setVisibility(View.VISIBLE);
            join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User.user.joinGroup(data.getId());
                    join.setVisibility(View.INVISIBLE);
                }
            });
            icon.setImageBitmap(data.getIcon());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Show Group File
                }
            });
        }
    }
}
