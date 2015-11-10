package com.example.idanr.tinderforjavaclass.BusinessLogic.Matches;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toolbar;

import com.example.idanr.tinderforjavaclass.BusinessLogic.UserDetails.UserDetailsActivity;
import com.example.idanr.tinderforjavaclass.Model.CurrentUser;
import com.example.idanr.tinderforjavaclass.R;
import com.example.idanr.tinderforjavaclass.StorageManager.StorageManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by idanr on 11/9/15.
 */
public class MatchesActivity extends Activity {

    @Bind(R.id.matchesRecycleView)
    RecyclerView mMatchesRecycleView;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;


    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private CurrentUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matches_activity);

        ButterKnife.bind(this);

        setActionBar(mToolbar);
        setTitle("MATCHES");

        mCurrentUser = StorageManager.sharedInstance().getCurrentUser();
        mMatchesRecycleView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mMatchesRecycleView.setLayoutManager(mLayoutManager);

        mAdapter = new MatchesAdapter(mCurrentUser.getMatches(), new MatchesAdapter.MatchListener() {
            @Override
            public void matchClicked(int position, View userImageView) {
                Intent i = new Intent(MatchesActivity.this,
                        UserDetailsActivity.class);

                i.putExtra("user_id",position);
                i.putExtra("is_match",true);
                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                        MatchesActivity.this, userImageView, "user_image");

                startActivity(i, transitionActivityOptions.toBundle());
            }
        });
        mMatchesRecycleView.setAdapter(mAdapter);

        Slide animation = new Slide(Gravity.LEFT);
        animation.setDuration(500);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.excludeTarget(android.R.id.navigationBarBackground, true);
        animation.excludeTarget(android.R.id.statusBarBackground, true);

        Slide animation1 = new Slide(Gravity.LEFT);
        animation1.setDuration(500);
        animation1.setInterpolator(new DecelerateInterpolator());
        animation1.excludeTarget(android.R.id.navigationBarBackground, true);
        animation1.excludeTarget(android.R.id.statusBarBackground, true);

        getWindow().setEnterTransition(animation);
        getWindow().setReturnTransition(animation1);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAdapter.notifyDataSetChanged();
    }
}
