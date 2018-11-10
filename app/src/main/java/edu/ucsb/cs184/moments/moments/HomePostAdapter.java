package edu.ucsb.cs184.moments.moments;

import android.content.Context;
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

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class HomePostAdapter extends RecyclerView.Adapter<HomePostAdapter.ViewHolder> {

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
    public void addPosts(ArrayList<Post> newposts){
        for (int i = 0; i < newposts.size(); i++) add_post(newposts.get(i));
        notifyDataSetChanged();
    }
    public void addPost(Post post){
        add_post(post);
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        android.util.Log.d("fuck",position+"");
        Post post = posts.get(position);
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

    private String TimeText(Date date){
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
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
            collect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    setCollect(post.isCollected(userid));
                    setCollect(true);
                }
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        public void setCollect(boolean collected){
            collect.setImageResource(collected ? R.drawable.ic_collected : R.drawable.ic_collection);
        }
    }
}
