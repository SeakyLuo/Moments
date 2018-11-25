package edu.ucsb.cs184.moments.moments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.jude.swipbackhelper.SwipeBackHelper;

public class SettingsActivity extends AppCompatActivity {

    private static final String SETTINGS = "Settings";
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SwipeBackHelper.onCreate(this);
        setContentView(R.layout.activity_settings);

        back = findViewById(R.id.settings_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Fragment fragment = new SettingsFragment();
        ((SettingsFragment) fragment).setContext(getApplicationContext());
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
        private Context context;
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // here we should call settings ui
            addPreferencesFromResource(R.xml.user_preferences);
            Preference logout = findPreference("logout");
            logout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    FirebaseAuth.getInstance().signOut();
                    User.firebaseUser = null;
                    User.user = null;
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                    return true;
                }
            });

        }

        public void setContext(Context context){
            this.context = context;
        }
    }
}
