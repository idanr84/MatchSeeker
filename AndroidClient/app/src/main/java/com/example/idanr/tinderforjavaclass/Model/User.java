package com.example.idanr.tinderforjavaclass.Model;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by idanr on 10/23/15.
 */
public class User {

    public User(String name, String age, ArrayList<String> imageUrl){
        this.name = name;
        this.age = age;

        if (imageUrl != null){
            this.mImageUrls = imageUrl;
        }
    }

    public User(String name, String age,ArrayList<String> imageUrls, ArrayList<Bitmap> images){
        this.name = name;
        this.age = age;

        if (imageUrls != null){
            this.mImageUrls = imageUrls;
        }

        if (images != null){
            this.mImageBitmaps = images;
        }
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

    public Bitmap getImageAtIndex(int pos) {
        return mImageBitmaps.get(pos);
    }

    public void setImageBitmap(int pos, Bitmap imageUrl) {
        mImageBitmaps.set(pos, imageUrl);
    }

    public int getNumOfImages(){
        return mImageUrls.size();
    }

    private String name;
    private String age;
    private ArrayList<String> mImageUrls;
    private ArrayList<Bitmap > mImageBitmaps;
}
