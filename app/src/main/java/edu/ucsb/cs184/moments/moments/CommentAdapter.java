package edu.ucsb.cs184.moments.moments;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class CommentAdapter extends CustomAdapter {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_comment, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public static String TimeText(Long time){
        return PostAdapter.TimeText(time);
    }

    public class ViewHolder extends CustomAdapter.CustomViewHolder {
        public ImageView usericon;
        public TextView username;
        public TextView time;
        public TextView content;
        public TextView replies;
        private Comment data;

        public ViewHolder(View view) {
            super(view);
            usericon = view.findViewById(R.id.comment_usericon);
            username = view.findViewById(R.id.comment_username);
            time = view.findViewById(R.id.comment_time);
            content = view.findViewById(R.id.comment_content);
            replies = view.findViewById(R.id.comment_replies);
            usericon.setClickable(true);
            usericon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserProfileActivity.class);
                    intent.putExtra(UserProfileActivity.USERID, data.getUserid());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
                }
            });
            view.setClickable(true);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Context context = v.getContext();
//                    Intent intent = new Intent(context, FullCommentActivity.class);
//                    intent.putExtra("Comment", comment.toString());
//                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void setData(Object obj) {
            data = (Comment) obj;
            User user = User.findUser(data.getUserid());
            Glide.with(context).load(user.GetIcon()).into(usericon);
            username.setText(user.getName());
            time.setText(TimeText(data.getTime()));
            content.setText(data.getContent());
            int number = data.getComments().size();
            replies.setText(number + " Repl" + ((number == 1) ? "y" : "ies"));
        }
    }
}
