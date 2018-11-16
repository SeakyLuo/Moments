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
import android.support.v7.view.menu.MenuPopupHelper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import java.lang.reflect.Field;

public class GroupsFragment extends Fragment {

    private View view;
    private Context context;
    private ImageButton menu;
    private ImageButton add;
    private DrawerLayout drawer;
    private BottomNavigationView nav;

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
                switch (menuItem.getItemId()){
                    case R.id.create_a_group:
                        return true;
                    case R.id.join_a_group:
                        Intent intent = new Intent(context, SearchActivity.class);
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
        return view;
    }

    public void setWidgets(DrawerLayout drawer, BottomNavigationView nav){
        this.drawer = drawer;
        this.nav = nav;
    }
}
