package edu.ucsb.cs184.moments.moments;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class SearchHistoryAdapter extends CustomAdapter{
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_search_history, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public static class ViewHolder extends CustomAdapter.CustomViewHolder{
        TextView item;
        ImageButton delete;
        public ViewHolder(@NonNull View view) {
            super(view);
            item = view.findViewById(R.id.sh_text);
            delete = view.findViewById(R.id.sh_delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    User.user.removeHistory(item.getText().toString());
                }
            });
        }

        @Override
        public void setData(Object object){
            item.setText((String) object);
        }
    }
}
