package edu.ucsb.cs184.moments.moments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class FullPostCommentsAdapter extends CustomAdapter {

    private Context context;
    private ArrayList<Comment> comments = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_comment, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }
    public static String TimeText(Long time){
        return PostsAdapter.TimeText(time);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class ViewHolder extends CustomAdapter.CustomViewHolder {
        public ImageView usericon;
        public TextView username;
        public TextView time;
        public TextView content;
        public RatingBar ratingBar;
        public ImageButton comment;
        private Comment data;
        public ViewHolder(View view) {
            super(view);
            usericon = view.findViewById(R.id.post_usericon);
            username = view.findViewById(R.id.post_username);
            time = view.findViewById(R.id.post_time);
            content = view.findViewById(R.id.post_content);
            ratingBar = view.findViewById(R.id.post_ratingBar);
            comment = view.findViewById(R.id.post_comment);
            usericon.setClickable(true);
        }

        @Override
        public void setData(Object object) {
            data = (Comment) object;
            User user = User.findUser(data.getUserid());
//        usericon.setImageBitmap(user.getIcon());
            username.setText(user.getName());
            time.setText(TimeText(data.getTime()));
            content.setText(data.getContent());
            ratingBar.setRating(data.ratings_avg());
        }
    }
}
