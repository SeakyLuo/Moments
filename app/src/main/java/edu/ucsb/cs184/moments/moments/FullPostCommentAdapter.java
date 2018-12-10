package edu.ucsb.cs184.moments.moments;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class FullPostCommentAdapter extends CustomAdapter {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_fullpost_comment, parent, false);
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
            usericon = view.findViewById(R.id.fpcomment_usericon);
            username = view.findViewById(R.id.fpcomment_username);
            time = view.findViewById(R.id.fpcomment_time);
            content = view.findViewById(R.id.fpcomment_content);
            replies = view.findViewById(R.id.fpcomment_replies);
            usericon.setClickable(true);
            usericon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UserProfileActivity.class);
                    intent.putExtra(UserProfileActivity.USERID, data.getUserid());
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
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
            view.setLongClickable(true);
        }

        @Override
        public void setData(Object obj) {
            data = (Comment) obj;
            User user = User.findUser(data.getUserid());
            Glide.with(context).load(user.GetIcon()).into(usericon);
            username.setText(user.getName());
            time.setText(TimeText(data.getTime()));
            PostAdapter.setContent(context, content, data.getContent());
            replies.setText("Replies: " + data.getComments().size());
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    CommentLongClickDialog dialog = new CommentLongClickDialog();
                    dialog.showNow(((AppCompatActivity) activity).getSupportFragmentManager(), "CommentLongClick");
                    dialog.setComment(data);
                    // TODO: add support for comment parent
                    dialog.setParent(Post.findPost(data.getPostKey()));
                    return false;
                }
            });
        }
    }
}
