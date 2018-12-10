package edu.ucsb.cs184.moments.moments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.Date;

public class AddCommentDialog extends DialogFragment {

    private EditText edit_comment;
    private EditText parent_comment;
    private FullPostCommentsFragment fragment;
    private ImageButton camera, gallery, at, send;
    private Post post;
    private Comment comment;
    private View.OnClickListener onSendListener;

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_comment,container,false);
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        edit_comment = view.findViewById(R.id.ac_editcomment);
        camera = view.findViewById(R.id.ac_camera);
        gallery = view.findViewById(R.id.ac_gallery);
        at = view.findViewById(R.id.ac_at);
        send = view.findViewById(R.id.ac_send);

        edit_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Boolean hasText = s.toString().trim().length() != 0;
                send.setClickable(hasText);
                send.setImageResource(hasText ? R.drawable.ic_add_comment : R.drawable.ic_add_comment_unclickable);
                if (parent_comment != null) parent_comment.setText(s);
            }
        });
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = edit_comment.getText().toString();
                if (content.trim().length() == 0) return;
                if (post != null) comment = new Comment(User.user.getId(), content, new Date().getTime(), post.GetKey());
                else if (comment != null) comment = new Comment(User.user.getId(), content, new Date().getTime(), comment.GetKey());
                Post.findPost(comment.getPostKey()).addComment(comment);
                User.findUser(comment.getUserid()).CommentNotification(comment, false);
                fragment.addElement(comment);
                if (onSendListener != null) onSendListener.onClick(v);
                edit_comment.setText("");
                dismiss();
            }
        });
        return view;
    }

    public void setPost(Post post) { this.post = post; }
    public void setComment(Comment comment) { this.comment = comment; }

    public void setCaller(EditText editText, FullPostCommentsFragment fragment){
        parent_comment = editText;
        this.fragment = fragment;
    }

    public void setOnSendListener(View.OnClickListener onSendListener){
        this.onSendListener = onSendListener;
    }
}
