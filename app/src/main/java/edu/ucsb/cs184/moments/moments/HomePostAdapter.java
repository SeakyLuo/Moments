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

import java.util.ArrayList;

public class HomePostAdapter extends RecyclerView.Adapter<HomePostAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Post> posts;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.post_view, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Post post = posts.get(position);
        User user = User.findUser(post.getUserid());
        holder.usericon.setImageBitmap(user.getIcon());
        holder.username.setText(user.getUsername());
        holder.time.setText(post.getTime().toString());
        holder.content.setText(post.getContent());
        holder.ratingBar.setRating(post.ratings_avg());
        // userid needs to be replaced
//        holder.collect.setImageIcon((post.isCollected(userid)) ? (R.drawable.ic_collected) : (R.drawable.ic_collection));
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
            usericon.setClickable(true);
        }
    }
}
