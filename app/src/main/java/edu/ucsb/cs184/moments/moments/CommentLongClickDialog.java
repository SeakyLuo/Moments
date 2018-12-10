package edu.ucsb.cs184.moments.moments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CommentLongClickDialog extends DialogFragment {

    private TextView copy, reply, delete, report;
    private Post parentPost; // Comment Parent;
    private Comment parentComment; // Comment Parent;
    private Comment comment;

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_comment_long_click, container, false);
        copy = view.findViewById(R.id.clc_copy);
        reply = view.findViewById(R.id.clc_reply);
        delete = view.findViewById(R.id.clc_delete);
        report = view.findViewById(R.id.clc_report);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copy", comment.getContent());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(),"Copied!",Toast. LENGTH_SHORT).show();
                dismiss();
            }
        });
        reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (parentPost != null) parentPost.removeComment(comment);
                else if (parentComment != null) parentComment.removeReply(comment);
                dismiss();
            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    public void setComment(Comment comment) { this.comment = comment; }
    public void setParent(Post parent) { parentPost = parent; }
    public void setParent(Comment parent) { parentComment = parent; }
}
