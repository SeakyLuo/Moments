package edu.ucsb.cs184.moments.moments;

import android.content.Context;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;

public abstract class NotificationFragment extends Fragment {
    protected Context context;
    protected RecyclerViewFragment fragment;
    protected BottomNavigationView nav;
    public abstract void refresh();
    public void setBottomNav(BottomNavigationView nav) { this.nav = nav; };
}
