package com.example.idanr.tinderforjavaclass.PotentialMatches;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.idanr.tinderforjavaclass.CustomUI.CardLayout.cardstack.CardStack;
import com.example.idanr.tinderforjavaclass.CustomUI.CardLayout.cardstack.CardUtils;
import com.example.idanr.tinderforjavaclass.Model.User;
import com.example.idanr.tinderforjavaclass.R;

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

    PotentialMatchesDataAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_matches_activity);

        ButterKnife.bind(this);

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
}
