package edu.ucsb.cs184.moments.moments;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class DraftAdapter extends CustomAdapter{

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_draft, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public static String TimeText(Long time){
        return new SimpleDateFormat("HH:mm yyyy/MM/dd").format(time);
    }

    public class ViewHolder extends CustomAdapter.CustomViewHolder {
        public TextView time, content;
        private Post data;

        public ViewHolder(final View view) {
            super(view);
            time = view.findViewById(R.id.draft_time);
            content = view.findViewById(R.id.draft_content);
        }

        public void setData(Object object) {
            data = (Post) object;
            view.setClickable(true);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EditPostActivity.class);
                    intent.putExtra(EditPostActivity.DRAFT, data);
                    intent.putExtra(EditPostActivity.POST, data);
                    activity.startActivityForResult(intent, EditPostActivity.EDIT_POST);
                    activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
                }
            });
            view.setLongClickable(true);
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AskYesNoDialog dialog = new AskYesNoDialog();
                    dialog.showNow(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), "Delete");
                    dialog.setMessage("Do you want to delete this draft?");
                    dialog.setOnYesListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            User.user.removeDraft(data);
                            notifyDataSetChanged();
                        }
                    });
                    return false;
                }
            });
            time.setText(TimeText(data.getTime()));
            content.setText(data.getContent());
        }
    }
}
