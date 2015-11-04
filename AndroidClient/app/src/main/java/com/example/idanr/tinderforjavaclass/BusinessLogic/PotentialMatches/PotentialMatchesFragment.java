package com.example.idanr.tinderforjavaclass.BusinessLogic.PotentialMatches;


import android.app.ActivityOptions;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.idanr.tinderforjavaclass.BusinessLogic.UserDetails.UserDetailsActivity;
import com.example.idanr.tinderforjavaclass.CustomUI.CardLayout.cardstack.CardStack;
import com.example.idanr.tinderforjavaclass.CustomUI.CardLayout.cardstack.CardUtils;
import com.example.idanr.tinderforjavaclass.R;
import com.example.idanr.tinderforjavaclass.StorageManager.StorageManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by idanr on 10/24/15.
 */
public class PotentialMatchesFragment extends Fragment {

    @Bind(R.id.cardLayout)
    CardStack mCardLayout;

    @Bind(R.id.likeButton)
    ImageButton mLikeButton;

    @Bind(R.id.dislikeButton)
    ImageButton mDislikeButton;

    @Bind(R.id.container)
    RelativeLayout mContainer;

    PotentialMatchesDataAdapter mAdapter;

    private int mCurrentIndex;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.user_matches_fragment,container,false);

        ButterKnife.bind(this, contentView);

        mCardLayout.setTransitionGroup(true);
        Fade fadeTransition = new Fade();
        fadeTransition.setDuration(1000);
        fadeTransition.excludeTarget(R.id.cardLayout, true);
        fadeTransition.excludeTarget(android.R.id.navigationBarBackground, true);
        fadeTransition.excludeTarget(android.R.id.statusBarBackground, true);
        getActivity().getWindow().setReenterTransition(fadeTransition);

        //add the view via xml or programmatically
        mCardLayout.setStackMargin(20);
        mCardLayout.setListener(new CardStack.CardEventListener() {
            @Override
            public boolean swipeEnd(int section, float distance) {
                if (distance > 300) {
                    return true;
                } else {
                    return false;
                }
            }

            @Override
            public boolean swipeStart(int section, float distance) {
                return false;
            }

            @Override
            public boolean swipeContinue(int section, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void discarded(int mIndex, int direction) {
//                StorageManager.sharedInstance().remomoveTopPotentialMatch();
                mCurrentIndex = mIndex;
            }

            @Override
            public void topCardTapped() {
                Intent i = new Intent(getActivity(),
                        UserDetailsActivity.class);

                i.putExtra("user_id",mCurrentIndex);
                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                        getActivity(), mCardLayout.topCardImageView(), "user_image");

                startActivity(i, transitionActivityOptions.toBundle());
            }
        });


        mAdapter = new PotentialMatchesDataAdapter(this.getActivity(), StorageManager.sharedInstance().getPotentialMatches());

        mCardLayout.setAdapter(mAdapter);

        mLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCardLayout.discardTop(CardUtils.DIRECTION_TOP_RIGHT,500);
            }
        });

        mDislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCardLayout.discardTop(CardUtils.DIRECTION_TOP_LEFT,500);
            }
        });

        return contentView;
    }
}
