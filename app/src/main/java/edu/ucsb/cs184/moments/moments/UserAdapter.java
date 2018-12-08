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
        TextView name, number;
        ImageButton follow;
        ImageView icon, gender;
        User data;
        public ViewHolder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.su_name);
            number = view.findViewById(R.id.su_number);
            follow = view.findViewById(R.id.su_follow);
            icon = view.findViewById(R.id.su_icon);
            gender = view.findViewById(R.id.su_gender);
        }

        @Override
        public void setData(Object object){
            data = (User) object;
            final String id = data.getId();
            name.setText(data.getName());
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
            String user_gender = data.getGender();
            gender.setVisibility(user_gender.equals(User.UNKNOWN) ? View.GONE : View.VISIBLE);
            gender.setImageResource(data.getGender().equals(User.MALE) ? R.drawable.ic_male : R.drawable.ic_female);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, UserProfileActivity.class);
                    intent.putExtra(UserProfileActivity.USERID, data.getId());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
                }
            });
        }
    }
}
