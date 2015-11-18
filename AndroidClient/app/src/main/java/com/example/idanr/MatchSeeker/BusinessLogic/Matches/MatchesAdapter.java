package com.example.idanr.MatchSeeker.BusinessLogic.Matches;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.idanr.MatchSeeker.Model.Match;
import com.example.idanr.MatchSeeker.NetworkManager.NetworkManager;
import com.example.idanr.MatchSeeker.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by idanr on 11/10/15.
 */

public class MatchesAdapter extends RecyclerView.Adapter<MatchesAdapter.MatchViewHolder> {
    private ArrayList<Match> mMatches;
    MatchListener mMatchListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MatchViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.userImage)
        NetworkImageView mUserImage;

        @Bind(R.id.userName)
        TextView mUserName;

        @Bind(R.id.cardViewContainer)
        CardView mCardView;

        public MatchViewHolder(View v) {
            super(v);
            ButterKnife.bind(this,v);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MatchesAdapter(ArrayList<Match> matches, MatchListener listener) {
        mMatches = matches;
        mMatchListener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MatchViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.match, parent, false);
        // set the view's size, margins, paddings and layout parameters

        MatchViewHolder vh = new MatchViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MatchViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        Match match = mMatches.get(position);
        holder.mUserImage.setImageUrl(match.getImageUrlAtIndex(match.getCurrentImagePage()), NetworkManager.sharedInstance().getImageLoader());
        holder.mUserName.setText(match.getName());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMatchListener.matchClicked(position,holder.mUserImage);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mMatches.size();
    }

    public static interface MatchListener {
        public void matchClicked(int position,View userImageView);

    }
}