package edu.ucsb.cs184.moments.moments;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CommentAdapter extends CustomAdapter<Comment> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_comment, parent, false));
    }

    class ViewHolder extends CustomViewHolder {
        TextView time, content, username;
        ImageView usericon;

        ViewHolder(View view) {
            super(view);
            time = view.findViewById(R.id.comment_time);
            content = view.findViewById(R.id.comment_content);
            username = view.findViewById(R.id.comment_username);
            usericon = view.findViewById(R.id.comment_usericon);
        }

        @Override
        void setData() {
            User user = User.findUser(data.getUserid());
            FirebaseHelper.setIcon(user.GetIcon(), context, usericon);
            username.setText(user.getName());
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
            PostAdapter.setContent(context, content, data.getContent());
        }
    }
}
