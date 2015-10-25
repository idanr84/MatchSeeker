package com.example.idanr.tinderforjavaclass.BusinessLogic.PotentialMatches;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;


import com.example.idanr.tinderforjavaclass.Configuration.ConfigurationManager;
import com.example.idanr.tinderforjavaclass.Facebook.FacebookManager;
import com.example.idanr.tinderforjavaclass.Initialization.InitializationManager;
import com.example.idanr.tinderforjavaclass.BusinessLogic.Login.LoginActivity;
import com.example.idanr.tinderforjavaclass.R;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.ProfilePictureView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by idanr on 10/19/15.
 */
public class PotentialMatchesActivity extends Activity {

    @Bind(R.id.content_frame)
    FrameLayout mContentContainer;

    @Bind(R.id.left_drawer)
    LinearLayout mLeftDrawer;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.userProfile)
    ProfilePictureView mProfilePic;

    @Bind(R.id.settingList)
    ListView mListView;

    private ActionBarDrawerToggle mDrawerToggle;
    private ArrayList<String> mSettingsList = new ArrayList<String>() {{
        add("Matches");
        add("Setting");

    }};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        InitializationManager.initSystemWithContext(getApplicationContext());

        if (!ConfigurationManager.sharedInstance().isConnectedToFacebook()){
            Intent startLogin = new Intent(PotentialMatchesActivity.this, LoginActivity.class);
            PotentialMatchesActivity.this.startActivity(startLogin);
            finish();
            return;
        }

        setContentView(R.layout.user_matches_activity);
        ButterKnife.bind(this);

        String profileID = ConfigurationManager.sharedInstance().getFacebookID();
        if (profileID != null){
            mProfilePic.setProfileId(profileID);
        } else {
            FacebookManager.sharedInstance().fetchUserInfo();
            ConfigurationManager.sharedInstance().addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent event) {
                    mProfilePic.setProfileId((String)event.getNewValue());
                }
            });
        }

        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, mToolbar,R.string.drawer_opened, R.string.drawer_closed);

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (mContentContainer != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new Fragment to be placed in the activity layout
            PotentialMatchesFragment potentialMatchesFragment = new PotentialMatchesFragment();

            // Add the fragment to the 'fragment_container' FrameLayout
            getFragmentManager().beginTransaction()
                    .add(R.id.content_frame, potentialMatchesFragment).commit();
        }

        ArrayAdapter<String> settingAdapter = new ArrayAdapter<String>(this,R.layout.setting_drawer_item,R.id.settingName,mSettingsList );
        mListView.setAdapter(settingAdapter);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }



}
