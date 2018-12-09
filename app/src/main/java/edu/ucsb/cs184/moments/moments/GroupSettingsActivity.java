package edu.ucsb.cs184.moments.moments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.jude.swipbackhelper.SwipeBackHelper;

public class GroupSettingsActivity extends AppCompatActivity {

    public static final String GROUP = "Group", QUIT = "Quit";
    private static final String SETTINGS = "Settings";
    private Group group;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.onCreate(this);
        setContentView(R.layout.activity_group_settings);
        Intent intent = getIntent();
        group = intent.getParcelableExtra(GroupsFragment.GROUP);

        back = findViewById(R.id.gs_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
            }
        });

        Fragment fragment = new SettingsFragment();
        ((SettingsFragment) fragment).setData(group);
        // this fragment must be from android.app.Fragment,
        // if you use support fragment, it will not work

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (savedInstanceState == null) {
            // when saved instance state is null, that means
            // activity is created for the first time, so basically
            // add the fragment to activity if and only if activity is new
            // when activity rotates, do nothing
            transaction.add(R.id.settings_content, fragment, SETTINGS);
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
        private Preference name, number, sortby, quit;
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // here we should call settings ui
            addPreferencesFromResource(R.xml.group_preference);
            name = findPreference("Group Name");
            number = findPreference("Group Number");
            sortby = findPreference(getString(R.string.sort_by));
            quit = findPreference(getString(R.string.quit_group));
        }

        public void setData(final Group group){
            name.setSummary(group.getName());
            name.setDefaultValue(group.getName());
            number.setSummary(group.getNumber() + "");
            // TODO: save group settings
            // Default
            sortby.setSummary("Most Recent");
            quit.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    User.user.quitGroup(group.getId());
                    getActivity().finish();
                    getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
                    Intent intent = new Intent();
                    intent.putExtra(QUIT, QUIT);
                    return true;
                }
            });
        }
    }
}
