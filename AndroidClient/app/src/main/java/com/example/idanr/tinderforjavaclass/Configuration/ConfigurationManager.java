package com.example.idanr.tinderforjavaclass.Configuration;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.idanr.tinderforjavaclass.Model.CurrentUser;
import com.google.gson.Gson;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Created by idanr on 10/24/15.
 */
public class ConfigurationManager {

    static public final String PREFERENCES_KEY = "tinder_preferences";
    static public final String FACEBOOK_TOKEN = "facebook_token";
    static public final String ACCESS_TOKEN = "access_token";
    static public final String FACEBOOK_IS_CONNECTED = "facebook_is_connected";
    static public final String FACEBOOK_ID = "facebook_id";
    static public final String USER_ID = "user_id";


    static public final String CURRENT_USER = "current_user";

    static private ConfigurationManager mSharedInstace;
    static private Context mContext;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public static ConfigurationManager sharedInstance() {
        if (mSharedInstace == null) {
            mSharedInstace = new ConfigurationManager();
        }
        return mSharedInstace;
    }

    public static void init (Context context){
        mContext = context;
    }

    //To let classes subscribe for property changed listener
    public void addPropertyChangeListener(PropertyChangeListener listener){
        pcs.addPropertyChangeListener(FACEBOOK_ID, listener);
    }

    public String getFacebookToken (){
        return mContext.getSharedPreferences(PREFERENCES_KEY,Context.MODE_PRIVATE).getString(FACEBOOK_TOKEN, null);
    }

    public void setFacebookToken(String facebookToken) {
        mContext.getSharedPreferences(PREFERENCES_KEY,Context.MODE_PRIVATE).edit().putString(FACEBOOK_TOKEN,facebookToken).apply();
    }

    public String getAccessToken(){
        return mContext.getSharedPreferences(PREFERENCES_KEY,Context.MODE_PRIVATE).getString(ACCESS_TOKEN, null);
    }

    public void setAccessToken(String accessToken) {
        mContext.getSharedPreferences(PREFERENCES_KEY,Context.MODE_PRIVATE).edit().putString(ACCESS_TOKEN,accessToken).apply();
    }


    public String getFacebookID (){
        return mContext.getSharedPreferences(PREFERENCES_KEY,Context.MODE_PRIVATE).getString(FACEBOOK_ID, null);
    }

    public void setFacebookID(String facebookID) {
        pcs.firePropertyChange(FACEBOOK_ID, getFacebookID(), facebookID);
        mContext.getSharedPreferences(PREFERENCES_KEY,Context.MODE_PRIVATE).edit().putString(FACEBOOK_ID,facebookID).apply();
    }

    public String getUserID(){
        return mContext.getSharedPreferences(PREFERENCES_KEY,Context.MODE_PRIVATE).getString(USER_ID, null);
    }

    public void setUserID(String facebookID) {
        pcs.firePropertyChange(USER_ID, getFacebookID(), facebookID);
        mContext.getSharedPreferences(PREFERENCES_KEY,Context.MODE_PRIVATE).edit().putString(USER_ID,facebookID).apply();
    }

    public boolean isConnectedToFacebook() {
        return mContext.getSharedPreferences(PREFERENCES_KEY,Context.MODE_PRIVATE).getBoolean(FACEBOOK_IS_CONNECTED, false);
    }

    public void setIsConnectedToFacebook(boolean isConnectedToFacebook) {
        mContext.getSharedPreferences(PREFERENCES_KEY,Context.MODE_PRIVATE).edit().putBoolean(FACEBOOK_IS_CONNECTED,isConnectedToFacebook).apply();
    }

    public CurrentUser getCurrentUser(){
        Gson gson = new Gson();
        String json = mContext.getSharedPreferences(PREFERENCES_KEY,Context.MODE_PRIVATE).getString(CURRENT_USER, "");
        CurrentUser currentUser = gson.fromJson(json, CurrentUser.class);
        return currentUser;
    }

    public void setCurrentUser(CurrentUser currentUser){
        Gson gson = new Gson();
        String json = gson.toJson(currentUser);
        mContext.getSharedPreferences(PREFERENCES_KEY,Context.MODE_PRIVATE).edit().putString(CURRENT_USER,json).apply();
    }
}
