package edu.ucsb.cs184.moments.moments;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;

public abstract class NotificationFragment extends Fragment {
    protected RecyclerViewFragment fragment = new RecyclerViewFragment();
    protected BottomNavigationView nav;
    public void setBottomNavigationView(BottomNavigationView bottomNavigationView) { nav = bottomNavigationView; }
    protected void init(){
        fragment.addOnRefreshListener(new RecyclerViewFragment.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        fragment.addHiddenView(nav);
    }
    public abstract void refresh();
}
