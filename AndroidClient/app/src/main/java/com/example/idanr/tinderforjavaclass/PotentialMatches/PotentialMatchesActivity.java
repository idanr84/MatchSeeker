package com.example.idanr.tinderforjavaclass.PotentialMatches;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;


import com.example.idanr.tinderforjavaclass.Configuration.ConfigurationManager;
import com.example.idanr.tinderforjavaclass.CustomUI.CardLayout.cardstack.CardStack;
import com.example.idanr.tinderforjavaclass.CustomUI.CardLayout.cardstack.CardUtils;
import com.example.idanr.tinderforjavaclass.Initialization.InitializationManager;
import com.example.idanr.tinderforjavaclass.LoginActivity;
import com.example.idanr.tinderforjavaclass.Model.User;
import com.example.idanr.tinderforjavaclass.R;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.FacebookSdk;

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
    ListView mLeftDrawer;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private ActionBarDrawerToggle mDrawerToggle;
    private PotentialMatchesDataAdapter mAdapter;

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

        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, mToolbar,R.string.drawer_opened, R.string.drawer_closed);

//        mDrawerLayout.setStatusBarBackgroundColor(android.graphics.Color.rgb(100, 100, 100));

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
