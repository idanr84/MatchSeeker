package com.example.idanr.tinderforjavaclass.PotentialMatches;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.idanr.tinderforjavaclass.CustomUI.CardLayout.cardstack.CardStack;
import com.example.idanr.tinderforjavaclass.CustomUI.CardLayout.cardstack.CardUtils;
import com.example.idanr.tinderforjavaclass.Model.User;
import com.example.idanr.tinderforjavaclass.R;

import java.util.ArrayList;

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

    PotentialMatchesDataAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.user_matches_fragment,container,false);

        ButterKnife.bind(this,contentView);

        //add the view via xml or programmatically
        mCardLayout.setStackMargin(20);

        ArrayList<User> al = new ArrayList<User>();
        al.add(new User("idan","31"));
        al.add(new User("idan_1","31"));
        al.add(new User("idan_2","31"));
        al.add(new User("idan_3", "31"));

        mAdapter = new PotentialMatchesDataAdapter(this.getActivity(),al);

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
