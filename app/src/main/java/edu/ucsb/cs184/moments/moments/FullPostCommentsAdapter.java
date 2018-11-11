package edu.ucsb.cs184.moments.moments;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FullPostCommentsAdapter extends RecyclerView.Adapter<FullPostCommentsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Comment> comments = new ArrayList<>();

    @Override
    public FullPostCommentsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.post_view, parent, false);
        FullPostCommentsAdapter.ViewHolder holder = new FullPostCommentsAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Comment comment = comments.get(position);
        User user = User.findUser(comment.getUserid());
//        holder.usericon.setImageBitmap(user.getIcon());
        holder.username.setText(user.getUsername());
        // Can be changed to xxx ago.
        holder.time.setText(TimeText(comment.getDate()));
        holder.content.setText(comment.getContent());
        holder.ratingBar.setRating(comment.ratings_avg());
    }

    private void add_comment(Comment comment){
        comments.add(0, comment);
        notifyDataSetChanged();
    }
    public void addComments(ArrayList<Comment> newComments){
        for (int i = 0; i < newComments.size(); i++) add_comment(newComments.get(i));
        notifyDataSetChanged();
    }
    public void addComment(Comment comment){
        add_comment(comment);
        notifyDataSetChanged();
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
//        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView usericon;
        public TextView username;
        public TextView time;
        public TextView content;
        public RatingBar ratingBar;
        public ImageButton comment;
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
    }
}
