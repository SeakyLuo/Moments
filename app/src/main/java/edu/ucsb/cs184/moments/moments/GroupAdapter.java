package edu.ucsb.cs184.moments.moments;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuBuilder;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class GroupAdapter extends CustomAdapter<Group> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_group, parent, false));
    }

    public static String TimeText(Long time){
        Calendar date = Calendar.getInstance(), now = Calendar.getInstance();
        date.setTimeInMillis(time);
        int delta_year = now.get(Calendar.YEAR) - date.get(Calendar.YEAR);
        if (delta_year != 0) return new SimpleDateFormat("yyyy-MM-dd").format(time);
        int delta_day = now.get(Calendar.DAY_OF_YEAR) - date.get(Calendar.DAY_OF_YEAR);
        if (delta_day == 0) return new SimpleDateFormat("HH:mm").format(time);
        else if (delta_day == 1) return "Yesterday";
        if (date.get(Calendar.WEEK_OF_YEAR) == now.get(Calendar.WEEK_OF_YEAR)) return date.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US);;
        return new SimpleDateFormat("MM-dd").format(time);
    }

    class ViewHolder extends CustomViewHolder {
        ImageView group_icon;
        TextView group_name;
        TextView time;
        TextView content;
        ImageView quiet;

        ViewHolder(final View view) {
            super(view);
            group_icon = view.findViewById(R.id.group_icon);
            group_name = view.findViewById(R.id.group_name);
            time = view.findViewById(R.id.group_time);
            content = view.findViewById(R.id.group_content);
            quiet = view.findViewById(R.id.group_quiet);
            view.setClickable(true);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, GroupPostsActivity.class);
//                    intent.putExtra(GroupPostsActivity.GROUP, data);
                    GroupPostsActivity.group = data;
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    PopupMenuHelper helper = new PopupMenuHelper(R.menu.group_popup_menu, view.getContext(), view);
                    helper.setOnItemSelectedListener(new PopupMenuHelper.onItemSelectListener() {
                        @Override
                        public boolean onItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
                            switch (menuItem.getItemId()){
                                case R.id.menuitem_test:
                                    return true;
                            }
                            return false;
                        }
                    });
                    helper.show();
                    return true;
                }
            });
        }

        @Override
        void setData() {
            group_name.setText(data.getName());
            FirebaseHelper.setIcon(data.GetIcon(), context, group_icon);
            Message message = data.latestActivity();
            time.setText(TimeText(message.getTime()));
            content.setText(message.getContent());
            quiet.setVisibility(data.isSilent() ? View.VISIBLE : View.GONE);
        }
    } 
}
