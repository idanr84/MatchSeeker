package com.example.idanr.tinderforjavaclass.Model;

import android.graphics.Bitmap;

import com.example.idanr.tinderforjavaclass.StorageManager.StorageManager;

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

    public CurrentUser(String name, String age,String userID, ArrayList<String> imageUrls){
        super(name,age,userID,imageUrls);
    }

    public CurrentUser(BaseUser user,ArrayList<PotentialMatch> potentialMatches,ArrayList<Match> matches){
        super(user);
//        this.likes = likes;
//        this.dislikes = dislikes;
        this.potentialMatches = potentialMatches;
        this.matches = matches;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    public ArrayList<String> getDislikes() {
        return dislikes;
    }

    public void setDislikes(ArrayList<String> dislikes) {
        this.dislikes = dislikes;
    }

    public void addDislikedUserID(String userID){
        this.dislikes.add(userID);
    }

    public void addLikedUserID(String userID){
        this.likes.add(userID);
    }

    public ArrayList<PotentialMatch> getPotentialMatches() {
        return potentialMatches;
    }

    public void setPotentialMatches(ArrayList<PotentialMatch> potentialMatches) {
        this.potentialMatches = potentialMatches;
    }

    public ArrayList<Match> getMatches() {
        return matches;
    }

    public void setMatches(ArrayList<Match> matches) {
        this.matches = matches;
    }


    public static CurrentUser fromJson(JSONObject object){
        try {
            JSONObject data = object.getJSONObject("data");
            ArrayList<PotentialMatch> potentialMatches = parsePotentialMatches(data);
            ArrayList<Match> matches = parseMatches(data);
//            ArrayList<String> likes = parseString(object.getJSONArray("likes"));
//            ArrayList<String> dislikes = parseString(object.getJSONArray("dislikes"));
            BaseUser user = BaseUser.fromJson(data);
            return new CurrentUser(user,potentialMatches,matches);
        }
        catch (JSONException e){
            return null;
        }
    }

    private static ArrayList<PotentialMatch>  parsePotentialMatches(JSONObject object) throws JSONException {


//        ArrayList<PotentialMatch> potentialMatches = new ArrayList<>();
//        JSONArray potentialMatchesJsonArray = object.getJSONArray("potential_matches");
//        for (int index=0;index<potentialMatchesJsonArray.length();index++){
//            JSONObject potentialMatch = potentialMatchesJsonArray.getJSONObject(index);
//            potentialMatches.add(PotentialMatch.fromJson(potentialMatch));
//        }
//        return potentialMatches;

        return StorageManager.sharedInstance().returnTestPotentialMatches();
    }

    private static ArrayList<Match>  parseMatches(JSONObject object) throws JSONException {
        ArrayList<Match> matches = new ArrayList<>();
        JSONArray matchesJsonArray = object.getJSONArray("matched_users");
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
