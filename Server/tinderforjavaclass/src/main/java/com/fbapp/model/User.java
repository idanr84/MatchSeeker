/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fbapp.model;

import com.fbapp.FbHelper;
import com.fbapp.Utils;
import com.restfb.types.Album;
import com.restfb.types.Photo;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.Vector;

/**
 * User Object holds the data model for user's details fetched from facebook
 *
 * @author Yaara Shoham
 */
public class User {
    public long id;
    public String facebook_user_id;
    public String accessToken;
    public String fbAccessToken;
    public Integer age;
    public String name;
    public String location;
    public String profile_id;
    public String profile_url;
    public String profile_pic_url;
    public Date birthday_as_date;
    public List<UserImage> images;
    public String gender;
    public String gender_interested;
    public Integer min_age_interested;
    public Integer max_age_interested;

    /*
    * User constructor
    * */

    public User() {
        max_age_interested = null;
        min_age_interested = null;
    }

    /*
    * Initialize user details
    *
    * @param fbHelper facebook client for user
    * @param facebookUserId fetched from login servlet
    * @param facebookAccessToken fetched from login servlet to login into facebook account
    * */
    public void initUserDetails(FbHelper fbHelper, String facebookUserId, String facebookAccessToken){

        birthday_as_date = fbHelper.getMe().birthday_as_date;
        gender = fbHelper.getMe().gender;
        profile_url = fbHelper.getMe().profile_url;
        profile_pic_url = fbHelper.getMe().profile_pic_url;
        profile_id = fbHelper.getMe().profile_id;
        location = fbHelper.getMe().location;
        name = fbHelper.getMe().name;
        facebook_user_id = facebookUserId;
        fbAccessToken = facebookAccessToken;
        age = Utils.getAge(birthday_as_date);
    }

    /*
    * Initialize user interests (gender &amp; age)
    *
    * default <code>gender_interested</code> for male would be female, for female would be male.
    * if gender could not be fetched from facebook (permissions issue) than is set to both.
    *
    * default <code>min_age_interested</code> is set to 10 years younger than user's age
    * default <code>max_age_interested</code> is set to 10 years older than user's age
    * if user could not be fetched from facebook (permissions issue) than <code>min_age_interested</code>
    * should be set to 18 and <code>max_age_interested</code> should be set to 35
    * */
    public void initUserInterests(){
        if (gender.equalsIgnoreCase("m") || gender.equalsIgnoreCase("male")) {
            gender_interested = "female";
        } else if (gender.equalsIgnoreCase("f") || gender.equalsIgnoreCase("female")) {
            gender_interested = "male";
        } else {
            gender_interested = "both";
        }
        if (age > 0) {
            min_age_interested = age - 10;
            max_age_interested = age + 10;
        } else {
            min_age_interested = 18;
            max_age_interested = 35;
        }
    }

    /*
    * This method populates <code>AccessToken</code> for each user
    * */
    public String populateAccessToken() throws NoSuchAlgorithmException {
        String tempAccessToken = null;
        if (id == 0) {
            // algorithm can be "MD5", "SHA-1", "SHA-256"
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] inputBytes = (facebook_user_id + String.valueOf((new Date()).getTime())).getBytes();// get bytes array from message
            byte[] hashBytes = digest.digest(inputBytes); // convert hash bytes to string (usually in hexadecimal form)
            String encodedStr = String.format("%x", new BigInteger(1, hashBytes));
            tempAccessToken = encodedStr;
            accessToken = encodedStr;
        } else {
            tempAccessToken = accessToken;
        }
        return tempAccessToken;
    }

    /*
    * This method fetches profile photo for each user from facebook
    * */
    public void fetchProfilePhotos(FbHelper fbHelper){
        List<Album> albums = fbHelper.getAlbums();
        for (Album album : albums) {
            if (album.getType().contains("profile")) {
                images = new Vector<UserImage>();
                List<Photo> photos = fbHelper.getPictures(album.getId());
                int photoCount = 0;
                for (Photo photo : photos) {
                    UserImage userImage = new UserImage();
                    userImage.picture_url = photo.getPicture();
                    images.add(userImage);
                    photoCount++;
                    if (photoCount == 5)
                        break;
                }
                photos.clear();
                break;
            }
        }
        albums.clear();
    }
}
