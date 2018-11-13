package edu.ucsb.cs184.moments.moments;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
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

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Post> posts = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.post_view, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    private void add_post(Post post){
        posts.add(0, post);
        notifyDataSetChanged();
    }
    public void addPosts(ArrayList<Post> newPosts){
        for (int i = 0; i < newPosts.size(); i++) add_post(newPosts.get(i));
        notifyDataSetChanged();
    }
    public void addPost(Post post){
        add_post(post);
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Post post = posts.get(position);
        holder.setPost(post);
        User user = User.findUser(post.getUserid());
//        holder.usericon.setImageBitmap(user.getIcon());
        holder.username.setText(user.getUsername());
        // Can be changed to xxx ago.
        holder.time.setText(TimeText(post.getDate()));
        holder.content.setText(post.getContent());
        holder.ratingBar.setRating(post.ratings_avg());
        // userid needs to be replaced
//        holder.setCollect(post.isCollected(userid));
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
        return posts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView usericon;
        public TextView username;
        public TextView time;
        public TextView content;
        public RatingBar ratingBar;
        public ImageButton comment;
        public ImageButton collect;
        public ImageButton share;
        private Post post;

        public ViewHolder(View view) {
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
//                    setCollect(post.isCollected(userid));
                    setCollect(true);
                }
            });
            view.setClickable(true);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, FullPostActivity.class);
                    intent.putExtra("Post", post.toString());
                    context.startActivity(intent);
                }
            });
        }

        public void setPost(Post post) { this.post = post;}

        @RequiresApi(api = Build.VERSION_CODES.M)
        public void setCollect(boolean collected){
            collect.setImageResource(collected ? R.drawable.ic_collected : R.drawable.ic_collection);
        }
    }
}
