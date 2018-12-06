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

    private Context context;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private BottomNavigationView nav;
    private ImageButton menu;
    private ImageButton search;
    private FloatingActionButton fab;
    private RecyclerViewFragment fragment;

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
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });
        toolbar = view.findViewById(R.id.home_toolbar);
        fab = view.findViewById(R.id.home_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditPostActivity.class);
                startActivityForResult(intent, EditPostActivity.MAKE_POST);
                getActivity().overridePendingTransition(R.anim.push_down_in, R.anim.push_up_out);
            }
        });
        menu = view.findViewById(R.id.menu_home);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer != null) drawer.openDrawer(Gravity.START);
            }
        });
        fragment = new RecyclerViewFragment();
        fragment.setAdapter(new PostsAdapter());
        fragment.addOnRefreshListener(new RecyclerViewFragment.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        try {
            fragment.addElements(User.user.getTimeline());
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragment.addHiddenView(fab);
        fragment.addHiddenView(toolbar);
        fragment.addHiddenView(nav);
        fragment.show(getFragmentManager(), R.id.home_content);
        fragment.addOnRefreshListener(new RecyclerViewFragment.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    // TODO: see below
                    // Not a good algorithm
                    // Should add new posts only
                    User.user.refreshTimeline();
                    fragment.setData(User.user.getTimeline());
                } catch (RecyclerViewFragment.UnsupportedDataException e) {
                    e.printStackTrace();
                }
                fragment.refresh();
            }
        });
        return view;
    }

    public void refresh(){
        fragment.gotoTop();
        (new Thread(new Runnable() {
            @Override
            public void run() {
                User.user.refreshTimeline();
                fragment.gotoTop();
            }
        })).start();
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
        switch (requestCode){
            case EditPostActivity.MAKE_POST:
                try {
                    fragment.addElement(data.getParcelableExtra(EditPostActivity.POST));
                }catch (RecyclerViewFragment.UnsupportedDataException e) {
                    e.printStackTrace();
                }finally {
                    break;
                }
            case FullPostActivity.DELETE_POST:
                try {
                    fragment.removeElement(data.getParcelableExtra(FullPostActivity.POST));
                } catch (RecyclerViewFragment.UnsupportedDataException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
