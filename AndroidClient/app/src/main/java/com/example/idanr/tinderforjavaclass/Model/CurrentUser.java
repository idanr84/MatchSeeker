package com.example.idanr.tinderforjavaclass.Model;

import android.graphics.Bitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by idanr on 10/31/15.
 */
public class CurrentUser extends BaseUser {

    ArrayList<String> likes = new ArrayList<>();
    ArrayList<String> dislikes = new ArrayList<>();
    ArrayList<PotentialMatch> potentialMatches = new ArrayList<>();
    ArrayList<Match> matches = new ArrayList<>();

    public CurrentUser(String name, String age,String userID, ArrayList<String> imageUrls, ArrayList<Bitmap> images){
        super(name,age,userID,imageUrls,images);
    }

    public CurrentUser(BaseUser user,ArrayList<String> likes,ArrayList<String> dislikes,ArrayList<PotentialMatch> potentialMatches,ArrayList<Match> matches){
        super(user);
        this.likes = likes;
        this.dislikes = dislikes;
        this.potentialMatches = potentialMatches;
        this.matches = matches;
    }

    public static CurrentUser fromJson(JSONObject object){
        try {
            ArrayList<PotentialMatch> potentialMatches = parsePotentialMatches(object);
            ArrayList<Match> matches = parseMatches(object);
            ArrayList<String> likes = parseString(object.getJSONArray("likes"));
            ArrayList<String> dislikes = parseString(object.getJSONArray("dislikes"));
            BaseUser user = BaseUser.fromJson(object);
            return new CurrentUser(user,likes,dislikes,potentialMatches,matches);
        }
        catch (JSONException e){
            return null;
        }
    }

    private static ArrayList<PotentialMatch>  parsePotentialMatches(JSONObject object) throws JSONException {
        ArrayList<PotentialMatch> potentialMatches = new ArrayList<>();
        JSONArray potentialMatchesJsonArray = object.getJSONArray("potential_matches");
        for (int index=0;index<potentialMatchesJsonArray.length();index++){
            JSONObject potentialMatch = potentialMatchesJsonArray.getJSONObject(index);
            potentialMatches.add(PotentialMatch.fromJson(potentialMatch));
        }
        return potentialMatches;
    }

    private static ArrayList<Match>  parseMatches(JSONObject object) throws JSONException {
        ArrayList<Match> matches = new ArrayList<>();
        JSONArray matchesJsonArray = object.getJSONArray("matches");
        for (int index=0;index<matchesJsonArray.length();index++){
            JSONObject match = matchesJsonArray.getJSONObject(index);
            matches.add(Match.fromJson(match));
        }
        return matches ;
    }

    private static ArrayList<String> parseString(JSONArray jArray) throws JSONException{
        ArrayList<String> listdata = new ArrayList<String>();
        if (jArray != null) {
            for (int i=0;i<jArray.length();i++){
                listdata.add(jArray.get(i).toString());
            }
        }
        return listdata;
    }
}
