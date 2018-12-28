package edu.ucsb.cs184.moments.moments;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageAdapter extends CustomAdapter {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_message, parent, false));
    }

    public class ViewHolder extends CustomViewHolder {
        public TextView time, content, name;
        public ImageView icon;

        ViewHolder(final View view) {
            super(view);
            time = view.findViewById(R.id.message_time);
            content = view.findViewById(R.id.message_content);
            name = view.findViewById(R.id.message_name);
            icon = view.findViewById(R.id.message_icon);
        }

        @Override
        void setData() {

        }
    }
}
