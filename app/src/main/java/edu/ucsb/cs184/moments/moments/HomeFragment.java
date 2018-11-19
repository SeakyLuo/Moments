package edu.ucsb.cs184.moments.moments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.support.v7.widget.Toolbar;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

    public static final int REQUEST_POST = 0;

    private Context context;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private BottomNavigationView nav;
    private ImageButton menu;
    private ImageButton search;
    private FloatingActionButton fab;
    private RecycleViewFragment fragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        context = getContext();

        search = view.findViewById(R.id.search_home);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
        fab = view.findViewById(R.id.home_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditPostActivity.class);
                startActivityForResult(intent, REQUEST_POST);
            }
        });
        menu = view.findViewById(R.id.menu_home);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer != null) drawer.openDrawer(Gravity.START);
            }
        });
        fragment = new RecycleViewFragment();
        fragment.setAdapter(new PostsAdapter());
        fragment.addHiddenView(toolbar);
        fragment.addHiddenView(fab);
        fragment.addHiddenView(nav);
        try {
            fragment.addElements(User.user.Timeline(-1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragment.show(getFragmentManager(), R.id.home_content);
        return view;
    }

    public void setWidgets(DrawerLayout drawer, BottomNavigationView nav){
        this.drawer = drawer;
        this.nav = nav;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            //Restore the fragment's state here
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's state here
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == REQUEST_POST){
            try {
                fragment.addElement(data.getSerializableExtra(EditPostActivity.POST));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
