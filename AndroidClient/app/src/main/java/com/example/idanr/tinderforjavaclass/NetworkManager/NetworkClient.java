package com.example.idanr.tinderforjavaclass.NetworkManager;

import android.net.Uri;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.idanr.tinderforjavaclass.Configuration.ConfigurationManager;
import com.example.idanr.tinderforjavaclass.Model.CurrentUser;
import com.example.idanr.tinderforjavaclass.StorageManager.StorageManager;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by idanr on 10/31/15.
 */
public class NetworkClient {

    private final String BASE_URL = "http://tinderforjavaclass.herokuapp.com/";
    private final String AUTHORIZATION = "Authorization";

    private final String TAG = getClass().getSimpleName();

    public interface LoginListener {
        void loginSuccessed(String accessToken,String userID);
        void loginFailed(String error);
    }

    public interface UserInfoListener{
        void userInfoFetched(CurrentUser currentUser);
    }

    public interface UserSettingUpdateListener{
        void settingUploadSucceded();
    }

    public void login(String facebookToken,String facebookID, final LoginListener listener) {

        final String LOGIN_METHOD = "login";
        final String FB_ID = "facebook_user_id";
        final String ACCESS_TOKEN = "access_token";

        Uri builtUri = Uri.parse(BASE_URL + LOGIN_METHOD).buildUpon()
                .appendQueryParameter(FB_ID, facebookID)
                .appendQueryParameter(ACCESS_TOKEN, facebookToken)
                .build();


        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, builtUri.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject data = response.getJSONObject("data");
                            String accessToken = data.getString("accessToken");
                            String userID = data.getString("id");
                            ConfigurationManager.sharedInstance().setUserID(userID);
                            ConfigurationManager.sharedInstance().setAccessToken(accessToken);
                            listener.loginSuccessed(accessToken,userID);
                        } catch (JSONException e) {
                            listener.loginFailed(e.getLocalizedMessage());
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.loginFailed(error.getLocalizedMessage());
            }
        });


        NetworkManager.sharedInstance().addToRequestQueue(jsonRequest);
    }

    public void getUserInfo(final UserInfoListener listener){
        final String USER_INFO = "me";
        final String USER_ID = "user_id";

        Uri builtUri = Uri.parse(BASE_URL + USER_INFO).buildUpon()
                .appendQueryParameter(USER_ID, ConfigurationManager.sharedInstance().getUserID())
                .build();


        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, builtUri.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        CurrentUser currentUser =  CurrentUser.fromJson(response);
                        listener.userInfoFetched(currentUser);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            public Map<String, String> getHeaders ()throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(AUTHORIZATION, ConfigurationManager.sharedInstance().getAccessToken());
                return map;
            }
        };

        NetworkManager.sharedInstance().addToRequestQueue(jsonRequest);
    }

    public void updateServer(CurrentUser currentUser){
        final String USER_INFO = "submit";

        Uri builtUri = Uri.parse(BASE_URL + USER_INFO).buildUpon().build();

        JSONObject body = null;
        try {
            body = new JSONObject(currentUser.toJson());
        } catch (JSONException e){

        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, builtUri.toString(),body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "success syncing changes");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"failure syncing changes");
            }
        }){
            @Override
            public Map<String, String> getHeaders ()throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(AUTHORIZATION, ConfigurationManager.sharedInstance().getAccessToken());
                return map;
            }
        };

        NetworkManager.sharedInstance().addToRequestQueue(jsonRequest);

    }

    public void updateUserSetting(int minAge,int maxAge,String gender,final UserSettingUpdateListener listener){
        final String USER_INFO = "interested";

        Uri builtUri = Uri.parse(BASE_URL + USER_INFO).buildUpon().build();

        JSONObject body = null;
        try {
            body = new JSONObject();
            body.put("min_age",minAge);
            body.put("max_age",maxAge);
            body.put("gender",gender.toLowerCase());
        } catch (JSONException e){

        }

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, builtUri.toString(),body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "success syncing setting configuration");
                        listener.settingUploadSucceded();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"failure syncing setting changes");
            }
        }){
            @Override
            public Map<String, String> getHeaders ()throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(AUTHORIZATION, ConfigurationManager.sharedInstance().getAccessToken());
                return map;
            }
        };

        NetworkManager.sharedInstance().addToRequestQueue(jsonRequest);
    }

}



