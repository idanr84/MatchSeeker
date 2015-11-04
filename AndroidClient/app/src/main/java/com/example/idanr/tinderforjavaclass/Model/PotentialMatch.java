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

    public int getCurrentImagePage() {
        return currentImagePage;
    }

    public void setCurrentImagePage(int currentImagePage) {
        this.currentImagePage = currentImagePage;
    }

    private int currentImagePage;

    public PotentialMatch (String name, String age,String userID, ArrayList<String> imageUrls, String state){
        super(name,age,userID,imageUrls);
        this.state = state;
    }

    public PotentialMatch (BaseUser baseUser,String state){
        super(baseUser);
        this.state = state;
    }

    public static PotentialMatch fromJson(JSONObject object){

        try {
            String state = object.getString("like_state");
            BaseUser baseUser =  BaseUser.fromJson(object);
            PotentialMatch potentialMatch = new PotentialMatch(baseUser,state);
            return potentialMatch;
        }
        catch (JSONException e){
            return null;
        }
    }
}
