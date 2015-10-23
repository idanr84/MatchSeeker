package com.example.idanr.tinderforjavaclass.PotentialMatches;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.idanr.tinderforjavaclass.CustomUI.CardLayout.cardstack.CardStack;
import com.example.idanr.tinderforjavaclass.Model.User;
import com.example.idanr.tinderforjavaclass.R;

import android.util.Log;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by idanr on 10/19/15.
 */
public class PotentialMatchesActivity extends Activity {

    @Bind(R.id.cardLayout) CardStack cardLayout;
    PotentialMatchesDataAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_matches_activity);

        ButterKnife.bind(this);

        //add the view via xml or programmatically
        cardLayout.setStackMargin(20);

        ArrayList<User> al = new ArrayList<User>();
        al.add(new User("idan","31"));
        al.add(new User("idan_1","31"));
        al.add(new User("idan_2","31"));
        al.add(new User("idan_3","31"));

        mAdapter = new PotentialMatchesDataAdapter(this,al);

        cardLayout.setAdapter(mAdapter);
    }
}
