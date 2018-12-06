package edu.ucsb.cs184.moments.moments;

import android.content.Context;
import android.content.Intent;
import android.support.v7.view.menu.MenuBuilder;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class GroupsAdapter extends CustomAdapter {

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_group, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public static String TimeText(Date date){
        Date now = new Date();
        long delta_day = now.getDay() - date.getDay();
        if (delta_day == 0) return new SimpleDateFormat("HH:mm").format(date);
        else if (delta_day == 1) return "Yesterday";
        Calendar c1 = Calendar.getInstance(), c2 = Calendar.getInstance();
        c1.setTime(date);
        c2.setTime(now);
        if (c1.get(Calendar.DAY_OF_WEEK) == c2.get(Calendar.DAY_OF_WEEK)) return c1.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.US);;
        long delta_year = now.getYear() - date.getYear();
        if (delta_year == 0) return new SimpleDateFormat("MM-dd").format(date);
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public static class ViewHolder extends CustomAdapter.CustomViewHolder {
        public ImageView group_icon;
        public TextView group_name;
        public TextView time;
        public TextView content;
        public ImageView quiet;
        private Group data;

        public ViewHolder(final View view) {
            super(view);
            group_icon = view.findViewById(R.id.view_group_icon);
            group_name = view.findViewById(R.id.view_group_name);
            time = view.findViewById(R.id.view_group_time);
            content = view.findViewById(R.id.view_group_content);
            quiet = view.findViewById(R.id.view_group_quiet);
            view.setClickable(true);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, GroupPostsActivity.class);
                    intent.putExtra(GroupsFragment.GROUP, data);
                    context.startActivity(intent);
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
        public void setData(Object object) {
            data = (Group) object;
            group_name.setText(data.getName());
            group_icon.setImageBitmap(data.getIcon());
            ArrayList<Post> posts = data.getPosts();
            if (posts.size() == 0){
                time.setText(TimeText(new Date()));
                String managerid = data.getManagerid();
                String name = User.findUser(managerid).getName();
                content.setText(managerid.equals(User.user.getId()) ? "You" : name + " created a new group.");
            }else{
                Post post = posts.get(posts.size() - 1);
                time.setText(TimeText(new Date(post.getTime())));
                content.setText(User.findUser(post.getUserid()).getName() + ": " + post.getContent());
            }
        }
        public void setQuiet(boolean isQuiet){
            quiet.setVisibility(isQuiet ? View.VISIBLE : View.INVISIBLE);
        }
    } 
}
