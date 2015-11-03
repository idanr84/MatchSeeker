package com.example.idanr.tinderforjavaclass.BusinessLogic.PotentialMatches;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.idanr.tinderforjavaclass.Model.BaseUser;
import com.example.idanr.tinderforjavaclass.Model.PotentialMatch;
import com.example.idanr.tinderforjavaclass.NetworkManager.NetworkManager;
import com.example.idanr.tinderforjavaclass.R;
import com.example.idanr.tinderforjavaclass.StorageManager.StorageManager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PotentialMatchesDataAdapter extends ArrayAdapter<PotentialMatch> {

    public PotentialMatchesDataAdapter(Context context, ArrayList<PotentialMatch> users) {
        super(context,0, users);
    }

    @Override
    public View getView(int position, final View contentView, ViewGroup parent){

        PotentialMatch user = getItem(position);

        View v = contentView;
        CardViewHolder holder;
        // Check if an existing view is being reused, otherwise inflate the view
        if (v== null) {
            v= LayoutInflater.from(getContext()).inflate(R.layout.potential_match, parent, false);
            holder = new CardViewHolder(v);
            v.setTag(holder);
        } else {
            holder = (CardViewHolder)v.getTag();
        }

        // Lookup view for data population
        holder.mName.setText(user.getName());
        holder.mAge.setText(user.getAge());
        holder.mUserImage.setImageUrl(user.getImageUrlAtIndex(0), NetworkManager.sharedInstance().getImageLoader());

        // Return the completed view to render on screen
        return v;


    }


    public static class CardViewHolder {
        @Bind(R.id.name)
        public TextView mName;

        @Bind(R.id.age)
        public TextView mAge;

        @Bind(R.id.userImage)
        public NetworkImageView mUserImage;

        public CardViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

