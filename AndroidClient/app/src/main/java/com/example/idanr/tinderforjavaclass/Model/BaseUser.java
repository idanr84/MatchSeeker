package com.example.idanr.tinderforjavaclass.Model;

import android.graphics.Bitmap;

import com.facebook.internal.CollectionMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by idanr on 10/23/15.
 */
public class BaseUser {

    private String name;
    private String age;
    private int userID;
    private ArrayList<String> mImageUrls;

    public BaseUser(String name, String age, int userID, ArrayList<String> imageUrl){
        this.name = name;
        this.age = age;
        this.userID = userID;

        if (imageUrl != null){
            this.mImageUrls = imageUrl;
        }
    }

    public BaseUser(BaseUser user){
        this.name = user.name;
        this.age = user.age;
        this.userID = user.userID;

        if (user.mImageUrls != null){
            this.mImageUrls = user.mImageUrls ;
        }
    }

    public BaseUser(String name, String age,int userID, ArrayList<String> imageUrls, ArrayList<Bitmap> images){
        this.name = name;
        this.age = age;
        this.userID = userID;

        if (imageUrls != null){
            this.mImageUrls = imageUrls;
        }
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
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
            json.put("user_id",userID);
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
