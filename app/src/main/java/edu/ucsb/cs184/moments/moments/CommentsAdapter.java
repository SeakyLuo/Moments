package edu.ucsb.cs184.moments.moments;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Comment> comments = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.view_comment, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    private void add_element(Comment comment){
        comments.add(0, comment);
        notifyDataSetChanged();
    }
    public void addElements(ArrayList<Comment> newComments){
        for (int i = 0; i < newComments.size(); i++) add_element(newComments.get(i));
        notifyDataSetChanged();
    }
    public void addElement(Comment comment){
        add_element(comment);
        notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Comment comment = comments.get(position);
        holder.setComment(comment);
    }

    public static String TimeText(Date date){
        return PostsAdapter.TimeText(date);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView usericon;
        public TextView username;
        public TextView time;
        public TextView content;
        public TextView replies;
        private Comment comment;

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

        public void setComment(Comment comment) {
            this.comment = comment;
            User user = User.findUser(comment.getUserid());
            usericon.setImageBitmap(user.getIcon());
            username.setText(user.getUsername());
            time.setText(TimeText(comment.getDate()));
            content.setText(comment.getContent());
            int number = comment.getComments().size();
            replies.setText(number + " Repl" + ((number == 1) ? "y" : "ies"));
        }
    }
}
