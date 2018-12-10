package edu.ucsb.cs184.moments.moments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment {

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

        search = view.findViewById(R.id.home_search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SearchActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
            }
        });
        toolbar = view.findViewById(R.id.home_toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.gotoTop();
            }
        });
        fab = view.findViewById(R.id.home_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditPostActivity.class);
                startActivityForResult(intent, EditPostActivity.EDIT_POST);
                getActivity().overridePendingTransition(R.anim.push_down_in, R.anim.push_up_out);
            }
        });
        menu = view.findViewById(R.id.home_menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer != null) drawer.openDrawer(Gravity.START);
            }
        });
        fragment = new RecyclerViewFragment();
        fragment.setAdapter(new PostAdapter());
        try {
            fragment.setData(User.user.getTimeline());
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragment.addHiddenView(fab);
        fragment.addHiddenView(nav);
        fragment.show(getFragmentManager(), R.id.home_content);
        fragment.addOnRefreshListener(new RecyclerViewFragment.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        return view;
    }

    public void refresh(){
        fragment.gotoTop();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    // TODO: see below
                    // Not a good algorithm
                    // Should add new posts only
                    int count = User.user.refreshTimeline();
                    if (count > 0)
                        Toast.makeText(getContext(),  count+ " new post" + (count > 1 ? "s" : "") + "!", Toast.LENGTH_SHORT).show();
                    fragment.setData(User.user.getTimeline());
                    fragment.refresh();
                } catch (RecyclerViewFragment.UnsupportedDataException e) {
                    e.printStackTrace();
                }
            }
        });
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
            case EditPostActivity.EDIT_POST:
                try {
                    fragment.addElement(data.getParcelableExtra(EditPostActivity.POST));
                    fragment.gotoTop();
                }catch (RecyclerViewFragment.UnsupportedDataException e) {
                    e.printStackTrace();
                }
                break;
            case PostAdapter.FULL_POST:
                try {
                    if (data.getBooleanExtra(FullPostActivity.DELETE_POST, false))
                        fragment.removeElement(data.getParcelableExtra(FullPostActivity.POST));
                }catch (RecyclerViewFragment.UnsupportedDataException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
