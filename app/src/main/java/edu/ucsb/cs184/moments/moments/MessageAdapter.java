package edu.ucsb.cs184.moments.moments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageAdapter extends CustomAdapter {
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_message, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public class ViewHolder extends CustomAdapter.CustomViewHolder {
        public TextView time, content, name;
        public ImageView icon;
        private Message data;

        public ViewHolder(final View view) {
            super(view);
            time = view.findViewById(R.id.message_time);
            content = view.findViewById(R.id.message_content);
            name = view.findViewById(R.id.message_name);
            icon = view.findViewById(R.id.message_icon);
        }

        public void setData(Object object) {
            data = (Message) object;
            
        }
    }

}
