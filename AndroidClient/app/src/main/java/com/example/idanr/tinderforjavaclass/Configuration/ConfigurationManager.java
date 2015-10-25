package com.example.idanr.tinderforjavaclass.Configuration;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by idanr on 10/24/15.
 */
public class ConfigurationManager {


    static public final String PREFERENCES_KEY = "tinder_preferences";
    static public final String FACEBOOK_TOKEN = "facebook_token";
    static public final String FACEBOOK_IS_CONNECTED = "facebook_is_connected";


    static private ConfigurationManager mSharedInstace;
    static private Context mContext;

    public static ConfigurationManager sharedInstance() {
        if (mSharedInstace == null) {
            mSharedInstace = new ConfigurationManager();
        }
        return mSharedInstace;
    }

    public static void init (Context context){
        mContext = context;
    }

    public String getFacebookToken (){
        return mContext.getSharedPreferences(PREFERENCES_KEY,Context.MODE_PRIVATE).getString(FACEBOOK_TOKEN, null);
    }

    public void setFacebookToken(String facebookToken) {
        mContext.getSharedPreferences(PREFERENCES_KEY,Context.MODE_PRIVATE).edit().putString(FACEBOOK_TOKEN,facebookToken).apply();
    }


    public boolean isConnectedToFacebook() {
        return mContext.getSharedPreferences(PREFERENCES_KEY,Context.MODE_PRIVATE).getBoolean(FACEBOOK_IS_CONNECTED, false);
    }

    public void setIsConnectedToFacebook(boolean isConnectedToFacebook) {
        mContext.getSharedPreferences(PREFERENCES_KEY,Context.MODE_PRIVATE).edit().putBoolean(FACEBOOK_IS_CONNECTED,isConnectedToFacebook).apply();
    }
}
