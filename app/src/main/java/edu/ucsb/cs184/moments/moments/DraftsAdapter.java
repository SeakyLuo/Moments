package edu.ucsb.cs184.moments.moments;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class DraftsAdapter extends CustomAdapter{
    public static final int FULL_POST = 1;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_post, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public static String TimeText(Long time){
        return new SimpleDateFormat("HH:ss yyyy/MM/dd").format(time);
    }

    public class ViewHolder extends CustomAdapter.CustomViewHolder {
        public TextView time, content;
        private Post data;

        public ViewHolder(final View view) {
            super(view);
            time = view.findViewById(R.id.draft_time);
            content = view.findViewById(R.id.draft_content);
        }

        public void setData(Object object) {
            data = (Post) object;
            view.setClickable(true);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FullPostActivity.class);
                    intent.putExtra(FullPostActivity.POST, data);
                    ((Activity) context).startActivityForResult(intent, FULL_POST);
                    ((Activity) context).overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
                }
            });
            time.setText(TimeText(data.getTime()));
            content.setText(data.getContent());
        }
    }
}
