package com.example.idanr.tinderforjavaclass.Model;

import android.graphics.Bitmap;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by idanr on 10/31/15.
 */
public class Match extends BaseUser {
    public Match (String name, String age,String userID, ArrayList<String> imageUrls){
        super(name,age,userID,imageUrls);
    }

    public Match (BaseUser baseUser){
        super(baseUser);
    }

    public static Match fromJson(JSONObject object){
        return  new Match(BaseUser.fromJson(object));
    }
}
