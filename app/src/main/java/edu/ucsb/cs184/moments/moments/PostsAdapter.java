package edu.ucsb.cs184.moments.moments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PostsAdapter extends CustomAdapter {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_post, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public static String TimeText(Date date){
        Date now = new Date();
        long delta_sec = (now.getTime() - date.getTime()) / 1000;
        if (delta_sec == 0) return "Just now";
        else if (delta_sec < 60) return delta_sec + " second"+ ((delta_sec == 1) ? "" : "s") +" ago";
        long delta_min = delta_sec / 60;
        if (delta_min < 60) return delta_min + " minute"+ ((delta_min == 1) ? "" : "s") +" ago";
        long delta_hour = delta_min / 60;
        if (delta_hour < 24) return delta_hour + " hour"+ ((delta_hour == 1) ? "" : "s") +" ago";
        long delta_day = delta_hour / 24;
        if (delta_day < 7) return delta_day + " day"+ ((delta_hour == 1) ? "" : "s") +" ago";
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static class ViewHolder extends CustomAdapter.CustomViewHolder {
        public ImageView usericon;
        public TextView username;
        public TextView time;
        public TextView content;
        public RatingBar ratingBar;
        public ImageButton comment;
        public ImageButton collect;
        public ImageButton share;
        private Post data;

        public ViewHolder(final View view) {
            super(view);
            usericon = view.findViewById(R.id.post_usericon);
            username = view.findViewById(R.id.post_username);
            time = view.findViewById(R.id.post_time);
            content = view.findViewById(R.id.post_content);
            ratingBar = view.findViewById(R.id.post_ratingBar);
            comment = view.findViewById(R.id.post_comment);
            collect = view.findViewById(R.id.post_collect);
            share = view.findViewById(R.id.post_share);
            usericon.setClickable(true);
            usericon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            collect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    collect(!data.isCollected());
                }
            });
            view.setClickable(true);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, FullPostActivity.class);
                    intent.putExtra(FullPostActivity.POST, data);
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
                }
            });
            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, FullPostActivity.class);
                    intent.putExtra(FullPostActivity.POST, data);
                    intent.putExtra(FullPostActivity.ADD_COMMENT, FullPostActivity.ADD_COMMENT);
                    context.startActivity(intent);
                }
            });
            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if (!fromUser) return;
                    if (data.getUserid() == User.user.getId()){
                        Toast.makeText(view.getContext(), "You can't rate your own post!", Toast.LENGTH_SHORT).show();
                        ratingBar.setRating(data.ratings_avg());
                    }else{
                        User.user.rate(new Rating(User.user.getId(), (int) rating, new Date().getTime(), data.getKey()));
                        if (rating == 0) ratingBar.setRating(data.ratings_avg());
                    }
                }
            });
        }

        public void setData(Object object) {
            data = (Post) object;
            User user = User.findUser(data.getUserid());
            usericon.setImageBitmap(user.getIcon());
            username.setText(user.getName());
            time.setText(TimeText(new Date(data.getTime())));
            content.setText(data.getContent());
            ratingBar.setRating(data.ratings_avg());
            setCollect(data.isCollected());
        }

        public void setCollect(boolean Collect){
            collect.setImageResource(Collect ? R.drawable.ic_heart_filled : R.drawable.ic_heart);
        }

        public void collect(boolean Collect){
            if (Collect) User.user.collect(data);
            else User.user.uncollect(data);
            setCollect(Collect);
        }
    }
}
