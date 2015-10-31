package com.example.idanr.tinderforjavaclass.Model;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by idanr on 10/31/15.
 */
public class PotentialMatch extends Match {

    private String state;

    public PotentialMatch (String name, String age,String userID, ArrayList<String> imageUrls, ArrayList<Bitmap> images, String state){
        super(name,age,userID,imageUrls,images);
        this.state = state;
    }

    public PotentialMatch (BaseUser baseUser,String state){
        super(baseUser);
        this.state = state;
    }

    public static PotentialMatch fromJson(JSONObject object){

        try {
            String state = object.getString("state");
            BaseUser baseUser =  BaseUser.fromJson(object);
            PotentialMatch potentialMatch = new PotentialMatch(baseUser,state);
            return potentialMatch;
        }
        catch (JSONException e){
            return null;
        }
    }
}
