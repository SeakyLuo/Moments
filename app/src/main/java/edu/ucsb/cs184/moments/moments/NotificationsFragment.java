package edu.ucsb.cs184.moments.moments;

import android.content.Context;
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

public class NotificationsFragment extends Fragment {

    private Context context;
    private DrawerLayout drawer;
    private BottomNavigationView nav;
    private ImageButton menu;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TabPagerAdapter adapter;
    private int curr_tab;
    private AtMeFragment atMeFragment;
    private CommentsFragment commentsFragment;
    private RatingsFragment ratingsFragment;
    private MessagesFragment messagesFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications,container,false);
        context = getContext();
        menu = view.findViewById(R.id.menu_notifications);
        mViewPager = view.findViewById(R.id.n_viewpager);
        mTabLayout = view.findViewById(R.id.nTablayout);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawer != null) drawer.openDrawer(Gravity.START);
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

        atMeFragment = new AtMeFragment();
        commentsFragment = new CommentsFragment();
        ratingsFragment = new RatingsFragment();
        messagesFragment = new MessagesFragment();
        adapter = new TabPagerAdapter(getFragmentManager());
        adapter.addFragment(atMeFragment, getString(R.string.atme));
        adapter.addFragment(commentsFragment, getString(R.string.comments));
        adapter.addFragment(ratingsFragment, getString(R.string.ratings));
        adapter.addFragment(messagesFragment, getString(R.string.messages));
        mViewPager.setAdapter(adapter);
        return view;
    }

    public void refresh(){
//        final NotificationTabFragment tabFragment = (NotificationTabFragment) adapter.getItem(curr_tab);
//        tabFragment.gotoTop();
//        (new Thread(new Runnable() {
//            @Override
//            public void run() {
//                tabFragment.refresh();
//                tabFragment.gotoTop();
//            }
//        })).start();
    }

    public void setWidgets(DrawerLayout drawer, BottomNavigationView nav){
        this.drawer = drawer;
        this.nav = nav;
    }

    public static class NotificationTabFragment extends Fragment{
        public void gotoTop(){}
        public void refresh(){}
    }

}
