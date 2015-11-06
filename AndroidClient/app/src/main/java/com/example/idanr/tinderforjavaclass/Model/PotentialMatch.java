package com.example.idanr.tinderforjavaclass.Model;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by idanr on 10/31/15.
 */
public class PotentialMatch extends Match {

    public enum State {
        TRUE,
        NONE
    }

    State state;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int getCurrentImagePage() {
        return currentImagePage;
    }

    public void setCurrentImagePage(int currentImagePage) {
        this.currentImagePage = currentImagePage;
    }

    private int currentImagePage;

    public PotentialMatch (String name, String age,String userID, ArrayList<String> imageUrls, String state){
        super(name,age,userID,imageUrls);
        this.state = State.valueOf(state.toUpperCase()) ;
    }

    public PotentialMatch (BaseUser baseUser,State state){
        super(baseUser);
        this.state = state;
    }

    public String toJson(){
        try {
            JSONObject json = new JSONObject(super.toJson());
            json.put("like_state",state.toString().toLowerCase());
            return json.toString();
        }
        catch (JSONException e){
            return null;
        }
    }

    public static PotentialMatch fromJson(JSONObject object){

        try {
            String stateStr = object.getString("like_state");
            State state = State.valueOf(stateStr.toUpperCase());
            BaseUser baseUser =  BaseUser.fromJson(object);
            PotentialMatch potentialMatch = new PotentialMatch(baseUser,state);
            return potentialMatch;
        }
        catch (JSONException e){
            return null;
        }
    }
}
