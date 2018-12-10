package edu.ucsb.cs184.moments.moments;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class RatingAdapter extends CustomAdapter {
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_rating, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public class ViewHolder extends CustomAdapter.CustomViewHolder {
        public TextView time, content, name;
        public ImageView icon;
        private Rating data;

        public ViewHolder(final View view) {
            super(view);
            time = view.findViewById(R.id.rating_time);
            content = view.findViewById(R.id.rating_content);
            name = view.findViewById(R.id.rating_username);
            icon = view.findViewById(R.id.rating_usericon);
        }

        public void setData(Object object) {
            data = (Rating) object;
            User user = User.findUser(data.getRaterId());
            FirebaseHelper.setIcon(user.GetIcon(), activity, icon);
            name.setText(user.getName());
            view.setClickable(true);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FullPostActivity.class);
                    intent.putExtra(FullPostActivity.POST, Post.findPost(data.GetPostKey()));
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
                }
            });
            time.setText(PostAdapter.TimeText(data.getTime()));
            content.setText(user.getName() + " gave you " + data.getRating() + "-star.");
//            content.setText(data.getContent());
        }
    }

}
