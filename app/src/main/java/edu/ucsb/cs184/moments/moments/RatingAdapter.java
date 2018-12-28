package edu.ucsb.cs184.moments.moments;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class RatingAdapter extends CustomAdapter<Rating> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_rating, parent, false));
    }

    class ViewHolder extends CustomViewHolder {
        TextView time, content, name;
        ImageView icon;

        ViewHolder(View view) {
            super(view);
            time = view.findViewById(R.id.rating_time);
            content = view.findViewById(R.id.rating_content);
            name = view.findViewById(R.id.rating_name);
            icon = view.findViewById(R.id.rating_icon);
        }

        @Override
        void setData() {
            User user = User.findUser(data.getRaterId());
            FirebaseHelper.setIcon(user.GetIcon(), context, icon);
            name.setText(user.getName());
            view.setClickable(true);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, FullPostActivity.class);
                    intent.putExtra(FullPostActivity.POST, Post.findPost(data.getPostKey()));
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
