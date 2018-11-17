package edu.ucsb.cs184.moments.moments;

import android.content.Context;
import android.content.Intent;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Group> groups = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.view_group, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    private void add_group(Group group){
        groups.add(0, group);
        notifyDataSetChanged();
    }
    public void addGroups(ArrayList<Group> newGroups){
        for (int i = 0; i < newGroups.size(); i++) add_group(newGroups.get(i));
        notifyDataSetChanged();
    }
    public void addGroup(Group group){
        add_group(group);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Group group = groups.get(position);
        holder.setGroup(group);
    }

    public static String TimeText(Date date){
        Date now = new Date();
        long delta_day = now.getDay() - date.getDay();
        if (delta_day == 0) return new SimpleDateFormat("HH:mm").format(date);
        else if (delta_day == 1) return "Yesterday";
        // return Mon Tue Wed Thu Fri Sat Sun
        long delta_year = now.getYear() - date.getYear();
        if (delta_year == 0) return new SimpleDateFormat("MM-dd").format(date);
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
//        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView group_icon;
        public TextView group_name;
        public TextView time;
        public TextView content;
        public ImageView quiet;
        private Group group;

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
                    intent.putExtra(GroupsFragment.GROUP, group.toString());
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

        public void setGroup(Group group) {
            this.group = group;
            group_name.setText(group.getName());
            group_icon.setImageBitmap(group.getIcon());
            ArrayList<Post> posts = group.getPosts();
            if (posts.size() == 0){
                time.setText(TimeText(new Date()));
                content.setText("You have created a new group.");
            }else{
                Post post = posts.get(posts.size() - 1);
                time.setText(TimeText(post.getDate()));
                content.setText(post.getUserid() +": " + post.getContent());
            }
        }

        public void setQuiet(boolean isQuiet){
            quiet.setVisibility(isQuiet ? View.VISIBLE : View.INVISIBLE);
        }
    } 
}
