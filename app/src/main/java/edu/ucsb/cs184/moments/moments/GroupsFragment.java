package edu.ucsb.cs184.moments.moments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.view.menu.MenuBuilder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class GroupsFragment extends Fragment {

    public static final String GROUP = "Group";

    private static final int CREATE_GROUP = 0;

    private View view;
    private Context context;
    private ImageButton menu;
    private ImageButton add;
    private DrawerLayout drawer;
    private BottomNavigationView nav;
    private GroupsAdapter adapter;
    private RecyclerViewFragment fragment;
    private ArrayList<String> groups = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_groups, container, false);
        context = getContext();
        menu = view.findViewById(R.id.menu_groups);
        add = view.findViewById(R.id.groups_add);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer != null) drawer.openDrawer(Gravity.START);
            }
        });
        final PopupMenuHelper helper = new PopupMenuHelper(R.menu.groups_adder_menu, context, add);
        helper.setOnItemSelectedListener(new PopupMenuHelper.onItemSelectListener() {
            @Override
            public boolean onItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
                Intent intent;
                switch (menuItem.getItemId()){
                    case R.id.create_a_group:
                        intent = new Intent(context, CreateGroupActivity.class);
                        startActivityForResult(intent, CREATE_GROUP);
                        return true;
                    case R.id.join_a_group:
                        intent = new Intent(context, SearchActivity.class);
                        intent.putExtra(SearchActivity.TAB, SearchActivity.GROUPS);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                helper.show();
            }
        });
        fragment = new RecyclerViewFragment();
        fragment.setShowDivider(true);
        adapter = new GroupsAdapter();
        setGroups(User.user.getGroups());
        fragment.setAdapter(adapter);
        fragment.addOnRefreshListener(new RecyclerViewFragment.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        fragment.addHiddenView(nav);
        fragment.show(getFragmentManager(), R.id.groups_content);
        return view;
    }

    public void refresh(){
        fragment.gotoTop();
        new Thread(new Runnable() {
            @Override
            public void run() {
                User.user.refreshGroups();
                fragment.gotoTop();
            }
        }).start();
    }

    public void setWidgets(DrawerLayout drawer, BottomNavigationView nav){
        this.drawer = drawer;
        this.nav = nav;
    }

    public void setGroups(ArrayList<String> groups){
        this.groups = groups;
        for(String group : groups){
            setGroup(Group.findGroup(group));
        }
    }

    public void setGroup(Group group){
        adapter.addElement(group);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == CREATE_GROUP){
            setGroup((Group) data.getSerializableExtra(GROUP));
        }
    }
}
