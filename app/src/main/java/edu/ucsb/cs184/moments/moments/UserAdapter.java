package edu.ucsb.cs184.moments.moments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class UserAdapter extends CustomAdapter{
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_listuser, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public class ViewHolder extends CustomAdapter.CustomViewHolder{
        TextView name, intro, followers;
        ImageView icon;
        ImageButton follow;
        User data;
        public ViewHolder(@NonNull View view) {
            super(view);
            icon = view.findViewById(R.id.su_icon);
            name = view.findViewById(R.id.su_name);
            intro = view.findViewById(R.id.su_intro);
            followers = view.findViewById(R.id.su_followers);
            follow = view.findViewById(R.id.su_follow);
        }

        @Override
        public void setData(Object object){
            data = (User) object;
            final String id = data.getId();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, UserProfileActivity.class);
                    intent.putExtra(UserProfileActivity.USERID, data.getId());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
                }
            });
            follow.setVisibility(User.user.getId().equals(id) ? View.GONE : View.VISIBLE);
            follow.setImageResource(UploadIconActivity.followImage(id));
            follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (User.user.isFollowing(id)) User.user.unfollow(id);
                    else  User.user.follow(id);
                    follow.setImageResource(UploadIconActivity.followImage(id));
                }
            });
            Glide.with(context).load(data.GetIcon()).into(icon);
            name.setText(data.getName());
            intro.setText(data.getIntro());
            followers.setText("Followers: " + data.getFollowers().size());
        }
    }
}
