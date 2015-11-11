package com.example.idanr.tinderforjavaclass.Model;

import android.graphics.Bitmap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

/**
 * Created by idanr on 10/23/15.
 */
public class BaseUser {

    private String mName;
    private String mAge;
    private int mUserID;
    private ArrayList<String> mImageUrls;
    private int mCurrentImagePage;

    public BaseUser(String name, String age, int userID, ArrayList<String> imageUrl){
        this.mName = name;
        this.mAge = age;
        this.mUserID = userID;

        if (imageUrl != null){
            this.mImageUrls = imageUrl;
        }
    }

    public BaseUser(BaseUser user){
        this.mName = user.mName;
        this.mAge = user.mAge;
        this.mUserID = user.mUserID;

        if (user.mImageUrls != null){
            this.mImageUrls = user.mImageUrls ;
        }
    }

    public BaseUser(String name, String age,int userID, ArrayList<String> imageUrls, ArrayList<Bitmap> images){
        this.mName = name;
        this.mAge = age;
        this.mUserID = userID;

        if (imageUrls != null){
            this.mImageUrls = imageUrls;
        }
    }

    public int getCurrentImagePage() {
        return mCurrentImagePage;
    }

    public void setCurrentImagePage(int currentImagePage) {
        this.mCurrentImagePage = currentImagePage;
    }

    public int getUserID() {
        return mUserID;
    }

    public void setUserID(int userID) {
        this.mUserID = userID;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getAge() {
        return mAge;
    }

    public void setAge(String age) {
        this.mAge = age;
    }

    public String getImageUrlAtIndex(int pos) {
        return mImageUrls.get(pos);
    }

    public void setImageUrl(int pos, String imageUrl) {
        mImageUrls.set(pos, imageUrl);
    }

    public int getNumOfImages(){
        return mImageUrls.size();
    }

    public String toJson(){

        try {
            JSONObject json = new JSONObject();
            json.put("user_id", mUserID);
            return json.toString();

        } catch (JSONException e){
            return null;
        }
    }

    public static BaseUser fromJson(JSONObject data){
        try {
            int userID = data.getInt("id");
            String name = data.getString("name");
            String age = data.getString("age");
            //String location = data.getString("location");
            ArrayList<String> imageUrls = new ArrayList<>();


            JSONArray images = data.getJSONArray("images");
            for (int index=0;index < images.length();index++){
                JSONObject image = images.getJSONObject(index);
                String imageUrl =  image.getString("picture_url");
                String decodedStringImageUrl = null;
                try {
                    decodedStringImageUrl = URLDecoder.decode(imageUrl, "UTF-8");
                }
                catch (UnsupportedEncodingException e){

                }

                if (decodedStringImageUrl != null) {
                    imageUrls.add(decodedStringImageUrl);
                }
            }

            return new BaseUser(name,age,userID,imageUrls);

        }
        catch (JSONException e){
            return null;
        }
    }
}
