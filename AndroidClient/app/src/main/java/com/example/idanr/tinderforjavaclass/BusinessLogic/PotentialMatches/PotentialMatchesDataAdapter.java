package com.example.idanr.tinderforjavaclass.BusinessLogic.PotentialMatches;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.idanr.tinderforjavaclass.Model.User;
import com.example.idanr.tinderforjavaclass.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PotentialMatchesDataAdapter extends ArrayAdapter<User> {

    public PotentialMatchesDataAdapter(Context context, ArrayList<User> users) {
        super(context,0, users);
    }

    @Override
    public View getView(int position, final View contentView, ViewGroup parent){

        User user = getItem(position);

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
        holder.mName.setText(user.getName());;
        holder.mAge.setText(user.getAge());;

        // Return the completed view to render on screen
        return v;


    }


    static class CardViewHolder {
        @Bind(R.id.name)
        TextView mName;
        @Bind(R.id.age)
        TextView mAge;

        public CardViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

