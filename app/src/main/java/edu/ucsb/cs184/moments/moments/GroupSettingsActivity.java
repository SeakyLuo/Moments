package edu.ucsb.cs184.moments.moments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.jude.swipbackhelper.SwipeBackHelper;

public class GroupSettingsActivity extends AppCompatActivity {

    private static final String SETTINGS = "Settings";
    private static Group group;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.onCreate(this);
        setContentView(R.layout.activity_group_settings);
        group = (Group) getIntent().getSerializableExtra(GroupsFragment.GROUP);

        back = findViewById(R.id.settings_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Fragment fragment = new SettingsFragment();
        ((SettingsFragment) fragment).setActivity(this);
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
        private AppCompatActivity activity;
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // here we should call settings ui
            addPreferencesFromResource(R.xml.group_preference);
            Preference quit = findPreference(getString(R.string.quit_group));
            quit.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
//                    User.user.quitGroup(group.getGroupid());
//                    activity.finish();
                    // TODO: needs another finish
                    return true;
                }
            });
        }

        public void setActivity(AppCompatActivity activity){
            this.activity = activity;
        }
    }
}
