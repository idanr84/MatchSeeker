package com.example.idanr.MatchSeeker.Model;

import com.example.idanr.MatchSeeker.StorageManager.StorageManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by idanr on 10/31/15.
 */
public class CurrentUser extends BaseUser {

    ArrayList<Integer> mLikes = new ArrayList<>();
    ArrayList<Integer> mDislikes = new ArrayList<>();
    ArrayList<PotentialMatch> mPotentialMatches = new ArrayList<>();
    ArrayList<Match> mMatches = new ArrayList<>();

    public CurrentUser(String name, String age,int userID,String location, ArrayList<String> imageUrls){
        super(name,age,userID,location,imageUrls);
    }

    public CurrentUser(BaseUser user,ArrayList<PotentialMatch> potentialMatches,ArrayList<Match> matches){
        super(user);
        this.mPotentialMatches = potentialMatches;
        this.mMatches = matches;
    }

    public void addDislikedUserID(int userID){
        this.mDislikes.add(userID);
    }

    public void addLikedUserID(int userID){
        this.mLikes.add(userID);
    }

    public void addMatch(Match match){
        mMatches.add(match);
    }

    public ArrayList<PotentialMatch> getPotentialMatches() {
        return mPotentialMatches;
    }

    public void setPotentialMatches(ArrayList<PotentialMatch> potentialMatches) {
        this.mPotentialMatches = potentialMatches;
    }

    public PotentialMatch getPotentialMatchAtIndex(int position){
        return mPotentialMatches.get(position);
    }

    public Match getMatchAtIndex(int position){
        return mMatches.get(position);
    }


    public void remomoveTopPotentialMatches(int topToRemove){
        getPotentialMatches().subList(0, topToRemove).clear();
    }

    public ArrayList<Match> getMatches() {
        return mMatches;
    }

    public void setMatches(ArrayList<Match> matches) {
        this.mMatches = matches;
    }

    public String toJson(){
        try {
            JSONObject json = new JSONObject();
            json.put("liked_users", new JSONArray(mLikes));
            json.put("disliked_users", new JSONArray(mDislikes));
            //json.put("user",super.toJson());

            JSONArray matchesArr = new JSONArray();
            for (Match match : mMatches){
                matchesArr.put(new JSONObject(match.toJson()));
            }
            json.put("matched_users",matchesArr);


//            JSONArray potentialArr = new JSONArray();
//            for (PotentialMatch potentialMatch : mPotentialMatches){
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

    public int getNewUnnotifiedMatchesCount(){
        int counter=0;
        for (Match match : mMatches){
            if (!match.getWasNotified()){
                counter++;
            }
        }
        return counter;
    }

    public void markAllAsNotified(){
        for (Match match : mMatches){
            if (!match.getWasNotified()){
                match.setWasNotified(true);
            }
        }
        StorageManager.sharedInstance().setCurrentUser(this);
    }
}
