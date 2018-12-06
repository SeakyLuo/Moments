package edu.ucsb.cs184.moments.moments;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchUsersAdapter extends CustomAdapter{
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_search_user, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public class ViewHolder extends CustomAdapter.CustomViewHolder{
        TextView name, number;
        ImageButton follow;
        ImageView icon;
        User data;
        public ViewHolder(@NonNull View view) {
            super(view);
            name = view.findViewById(R.id.su_name);
            number = view.findViewById(R.id.su_number);
            follow = view.findViewById(R.id.su_follow);
            icon = view.findViewById(R.id.su_icon);
        }

        @Override
        public void setData(Object object){
            data = (User) object;
            name.setText(data.getName());
            number.setText(data.getNumber());
            follow.setVisibility(User.user.getId().equals(data.getId()) ? View.INVISIBLE : View.VISIBLE);
            follow.setImageResource(User.user.isFollowing(data.getId()) ? R.drawable.ic_unfollow : R.drawable.ic_follow);
            follow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = data.getId();
                    if (User.user.isFollowing(id)){
                        User.user.unfollow(id);
                        follow.setImageResource(R.drawable.ic_follow);
                    }
                    else{
                        User.user.follow(id);
                        follow.setImageResource(R.drawable.ic_unfollow);
                    }
                }
            });
            icon.setImageBitmap(data.getIcon());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(view.getContext(), UserProfileActivity.class);
                    intent.putExtra(UserProfileActivity.USERID, data.getId());
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
