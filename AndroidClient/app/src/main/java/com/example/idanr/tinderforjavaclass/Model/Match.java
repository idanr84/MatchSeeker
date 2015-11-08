package com.example.idanr.tinderforjavaclass.Model;

import android.graphics.Bitmap;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by idanr on 10/31/15.
 */
public class Match extends BaseUser {

    private boolean mWasNotified = true;
    private boolean mWasViewed = false;

    public Match (String name, String age,int userID, ArrayList<String> imageUrls){
        super(name,age,userID,imageUrls);
    }

    public Match (BaseUser baseUser){
        super(baseUser);
    }

    public Match(BaseUser baseUser,boolean wasNotified,boolean wasViewed){
        super(baseUser);
        mWasNotified = wasNotified;
        mWasViewed = wasViewed;
    }

    public String toJson(){
        try {
            JSONObject json = new JSONObject(super.toJson());
            json.put("match_announced",mWasNotified);
            json.put("match_viewed",mWasViewed);
            return json.toString();
        } catch (JSONException e){
            return null;
        }
    }

    public static Match fromJson(JSONObject object){
        try {
            boolean wasNotified = object.getBoolean("match_announced");
            boolean wasViewed = object.getBoolean("match_viewed");
            return  new Match(BaseUser.fromJson(object.getJSONObject("user")),wasNotified,wasViewed);

        } catch (JSONException e){
            return null;
        }
    }
}
