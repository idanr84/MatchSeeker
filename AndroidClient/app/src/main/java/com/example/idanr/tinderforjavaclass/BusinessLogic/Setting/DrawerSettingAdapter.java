package com.example.idanr.tinderforjavaclass.BusinessLogic.Setting;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.idanr.tinderforjavaclass.Model.PotentialMatch;
import com.example.idanr.tinderforjavaclass.NetworkManager.NetworkManager;
import com.example.idanr.tinderforjavaclass.R;

import java.util.ArrayList;

/**
 * Created by idanr on 11/10/15.
 */
public class DrawerSettingAdapter extends ArrayAdapter<Pair<String,Integer>>{

    public DrawerSettingAdapter(Context context,ArrayList<Pair<String,Integer>> users){
        super(context,0,users);
    }

    @Override
    public View getView(int position, final View contentView, ViewGroup parent){

        Pair<String,Integer> pair = getItem(position);

        View v = contentView;
        if (contentView==null){
            v = LayoutInflater.from(getContext()).inflate(R.layout.setting_drawer_item, parent, false);
        }

        ImageView icon = (ImageView)(v.findViewById(R.id.icon));
        TextView textView = (TextView)(v.findViewById(R.id.settingName));

        icon.setImageResource(pair.second);
        textView.setText(pair.first);
        return v;
    }
}
