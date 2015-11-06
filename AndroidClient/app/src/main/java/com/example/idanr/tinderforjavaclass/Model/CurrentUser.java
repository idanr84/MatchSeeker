package com.example.idanr.tinderforjavaclass.Model;

import android.graphics.Bitmap;
import android.support.v4.app.NavUtils;

import com.example.idanr.tinderforjavaclass.Configuration.ConfigurationManager;
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

    public void addMatch(Match match){
        matches.add(match);
    }

    public ArrayList<PotentialMatch> getPotentialMatches() {
        return potentialMatches;
    }

    public void setPotentialMatches(ArrayList<PotentialMatch> potentialMatches) {
        this.potentialMatches = potentialMatches;
    }

    public PotentialMatch getPotentialMatchAtIndex(int position){
        return potentialMatches.get(position);
    }


    public void remomoveTopPotentialMatches(int topToRemove){
        getPotentialMatches().subList(0, topToRemove).clear();
    }

    public ArrayList<Match> getMatches() {
        return matches;
    }

    public void setMatches(ArrayList<Match> matches) {
        this.matches = matches;
    }

    public String toJson(){
        try {
            JSONObject json = new JSONObject();
            json.put("liked_users", new JSONArray(likes));
            json.put("disliked_users", new JSONArray(dislikes));
            //json.put("user",super.toJson());

            JSONArray matchesArr = new JSONArray();
            for (Match match : matches){
                matchesArr.put(match.getUserID());
            }
            json.put("matched_users",matchesArr);


//            JSONArray potentialArr = new JSONArray();
//            for (PotentialMatch potentialMatch : potentialMatches){
//                potentialArr.put(potentialMatch.toJson());
//            }
//            json.put("potential_matches",potentialArr);
            return json.toString();
        }
        catch (JSONException e){
            return null;
        }
    }

    public static CurrentUser fromJson(JSONObject object){
        try {
            JSONObject data = object.getJSONObject("data");
            ArrayList<PotentialMatch> potentialMatches = parsePotentialMatches(data);
            ArrayList<Match> matches = parseMatches(data);
            BaseUser user = parseBaseUser(data);
            return new CurrentUser(user,potentialMatches,matches);
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
        JSONArray matchesJsonArray = object.getJSONArray("matched_users");
        for (int index=0;index<matchesJsonArray.length();index++){
            JSONObject match = matchesJsonArray.getJSONObject(index);
            matches.add(Match.fromJson(match));
        }
        return matches ;
    }

    private static BaseUser  parseBaseUser(JSONObject object) throws JSONException {
        JSONObject data = (JSONObject)object.get("user");
        return BaseUser.fromJson(data);
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
