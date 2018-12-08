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

public class UserAdapter extends CustomAdapter{
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_listuser, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public class ViewHolder extends CustomAdapter.CustomViewHolder{
        TextView number, name, intro, followers;
        ImageView icon;
        ImageButton follow;
        User data;
        public ViewHolder(@NonNull View view) {
            super(view);
            number = view.findViewById(R.id.su_number);
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
            number.setText("#" + data.getNumber());
            follow.setVisibility(User.user.getId().equals(id) ? View.GONE : View.VISIBLE);
            follow.setImageResource(IconHelper.followImage(id));
            follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (User.user.isFollowing(id)) User.user.unfollow(id);
                    else  User.user.follow(id);
                    follow.setImageResource(IconHelper.followImage(id));
                }
            });
            if (data.GetIcon() == null) icon.setImageResource(R.drawable.user_icon);
            else icon.setImageBitmap(data.GetIcon());
            name.setText(data.getName());
            intro.setText(data.getIntro());
            followers.setText("Followers: " + data.getFollowers());
        }
    }
}
