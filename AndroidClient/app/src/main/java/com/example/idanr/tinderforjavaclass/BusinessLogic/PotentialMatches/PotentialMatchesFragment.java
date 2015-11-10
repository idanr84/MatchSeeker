package com.example.idanr.tinderforjavaclass.BusinessLogic.PotentialMatches;


import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.idanr.tinderforjavaclass.BusinessLogic.UserDetails.UserDetailsActivity;
import com.example.idanr.tinderforjavaclass.CustomUI.CardLayout.cardstack.CardStack;
import com.example.idanr.tinderforjavaclass.CustomUI.CardLayout.cardstack.CardUtils;
import com.example.idanr.tinderforjavaclass.CustomUI.MatchAlert;
import com.example.idanr.tinderforjavaclass.Model.CurrentUser;
import com.example.idanr.tinderforjavaclass.Model.Match;
import com.example.idanr.tinderforjavaclass.Model.PotentialMatch;
import com.example.idanr.tinderforjavaclass.NetworkManager.NetworkClient;
import com.example.idanr.tinderforjavaclass.R;
import com.example.idanr.tinderforjavaclass.StorageManager.StorageManager;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by idanr on 10/24/15.
 */
public class PotentialMatchesFragment extends Fragment implements PropertyChangeListener {

    @Bind(R.id.cardLayout)
    CardStack mCardLayout;

    @Bind(R.id.likeButton)
    ImageButton mLikeButton;

    @Bind(R.id.dislikeButton)
    ImageButton mDislikeButton;

    @Bind(R.id.container)
    RelativeLayout mContainer;

    @Bind(R.id.noMatchesView)
    TextView mNoMatchesView;

    PotentialMatchesDataAdapter mAdapter;

    private int mCurrentIndex;

    CurrentUser mCurrentUser;

    NetworkClient mNetworkHelper = new NetworkClient();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.user_matches_fragment,container,false);

        ButterKnife.bind(this, contentView);

        mCurrentUser = StorageManager.sharedInstance().getCurrentUser();

        if (mCurrentUser.getNewUnnotifiedMatchesCount() > 0){
            MatchAlert.showAlert(getActivity(),"YAY!!","NEW MATCHES ARE WAITING, GO CHECK THEM OUT");
            mCurrentUser = StorageManager.sharedInstance().getCurrentUser();
        }

        mCardLayout.setTransitionGroup(true);
        Fade fadeTransition = new Fade();
        fadeTransition.setDuration(1000);
        fadeTransition.excludeTarget(R.id.cardLayout, true);
        fadeTransition.excludeTarget(android.R.id.navigationBarBackground, true);
        fadeTransition.excludeTarget(android.R.id.statusBarBackground, true);
        getActivity().getWindow().setReenterTransition(fadeTransition);

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
                mCurrentIndex = mIndex;
                PotentialMatch potentialMatch = mCurrentUser.getPotentialMatchAtIndex(mIndex-1);
                if ((direction == CardUtils.DIRECTION_TOP_LEFT) || (direction == CardUtils.DIRECTION_BOTTOM_LEFT) ){ // dislike
                    mCurrentUser.addDislikedUserID(potentialMatch.getUserID());
                }
                else if (direction == CardUtils.DIRECTION_TOP_RIGHT || direction == CardUtils.DIRECTION_BOTTOM_RIGHT ) { // like{
                    if (potentialMatch.getState() == PotentialMatch.State.TRUE){
                        mCurrentUser.addMatch(potentialMatch);
                        MatchAlert.showAlert(getActivity(),"MATCH!","WAY TO GO, KEEP ROLLING!");

                    } else {
                        mCurrentUser.addLikedUserID(potentialMatch.getUserID());
                    }
                }

                if (!areMatchesAvailable()){
                    mNoMatchesView.setVisibility(View.VISIBLE);
                    mCardLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void topCardTapped() {
                Intent i = new Intent(getActivity(),
                        UserDetailsActivity.class);

                i.putExtra("is_match",false);
                i.putExtra("user_id",mCurrentIndex);
                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(
                        getActivity(), mCardLayout.topCardImageView(), "user_image");

                startActivity(i, transitionActivityOptions.toBundle());
            }
        });


        setupCardAdapter();

        mLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (areMatchesAvailable()){
                    mCardLayout.discardTop(CardUtils.DIRECTION_TOP_RIGHT,500);
                }
            }
        });

        mDislikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (areMatchesAvailable()){
                    mCardLayout.discardTop(CardUtils.DIRECTION_TOP_LEFT,500);
                }
            }
        });

        return contentView;
    }

    private boolean areMatchesAvailable() {
        return mCurrentIndex < mCurrentUser.getPotentialMatches().size();
    }

    private void setupCardAdapter() {
        mAdapter = new PotentialMatchesDataAdapter(this.getActivity(), mCurrentUser.getPotentialMatches());

        mCardLayout.setAdapter(mAdapter);
    }


    @Override
    public void onStart() {
        super.onStart();
        StorageManager.sharedInstance().addPropertyChangeListener(this);

        mCardLayout.reset(false);
        if (areMatchesAvailable()){
            mNoMatchesView.setVisibility(View.GONE);
            mCardLayout.setVisibility(View.VISIBLE);
        }else {
            mNoMatchesView.setVisibility(View.VISIBLE);
            mCardLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public void onStop() {
        super.onStop();
        StorageManager.sharedInstance().removePropertyChangedListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName() == StorageManager.CURRENT_USER){
            mCurrentUser = (CurrentUser)event.getNewValue();
            setupCardAdapter();
            mCardLayout.reset(true);
            mCurrentIndex = 0;

            if (areMatchesAvailable()){
                mNoMatchesView.setVisibility(View.GONE);
                mCardLayout.setVisibility(View.VISIBLE);
            }
        }
    }
}
