package edu.ucsb.cs184.moments.moments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jude.swipbackhelper.SwipeBackHelper;

public class TestActivity extends AppCompatActivity {

    private static final String SETTINGS = "Settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.onCreate(this);
        setContentView(R.layout.activity_settings);

        // our settings activity must come here

        Fragment fragment = new SettingsFragment();
        // this fragment must be from android.app.Fragment,
        // if you use support fragment, it will not work

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (savedInstanceState == null) {
            // when saved instance state is null, that means
            // activity is created for the first time, so basically
            // add the fragment to activity if and only if activity is new
            // when activity rotates, do nothing

            transaction.add(R.id.settings_placeholder, fragment, SETTINGS);
        }

        transaction.commit();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // here we should call settings ui
            addPreferencesFromResource(R.xml.user_preferences);

        }
    }
}
