package edu.ucsb.cs184.moments.moments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Arrays;

public class NotificationsFragment extends Fragment {

    private DrawerLayout drawer;
    private BottomNavigationView nav;
    private ImageButton menu, message;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabPagerAdapter adapter;
    private int curr_tab;
    private String[] tabs = { "AtMe", "Comments", "Ratings", "Messages" };
    private ArrayList<NotificationFragment> fragments = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications,container,false);
        menu = view.findViewById(R.id.notifications_menu);
        message = view.findViewById(R.id.notifications_message);
        mViewPager = view.findViewById(R.id.nViewpager);
        mTabLayout = view.findViewById(R.id.nTablayout);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer != null) drawer.openDrawer(Gravity.START);
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                curr_tab = tab.getPosition();
                mViewPager.setCurrentItem(curr_tab, true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        fragments.add(new AtMeFragment());
        fragments.add(new CommentsFragment());
        fragments.add(new RatingsFragment());
        fragments.add(new MessagesFragment());
        for (NotificationFragment fragment: fragments)
            fragment.setBottomNavigationView(nav);
        adapter = new TabPagerAdapter(getFragmentManager());
        adapter.addFragments(fragments, Arrays.asList(tabs));
        mViewPager.setAdapter(adapter);
        return view;
    }

    public void setWidgets(DrawerLayout drawer, BottomNavigationView nav){
        this.drawer = drawer;
        this.nav = nav;
    }

    public void refresh(){
        fragments.get(curr_tab).refresh();
    }

}
