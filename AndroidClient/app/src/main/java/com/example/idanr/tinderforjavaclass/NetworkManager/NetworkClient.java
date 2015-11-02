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
    private LoginListener mListener;

    public interface LoginListener {
        void loginSuccessed(String accessToken,String userID);
        void loginFailed(String error);
        void userInfoFetched(CurrentUser currentUser);
//        void potentialMatches(ArrayList<BaseUser> potentialMatches);
//        void matches(ArrayList<BaseUser> potentialMatches);
    }

    public NetworkClient(LoginListener listener){
        mListener = listener;
    }

    public void login(String facebookToken,String facebookID) {

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
                            mListener.loginSuccessed(accessToken,userID);
                        } catch (JSONException e) {
                            mListener.loginFailed(e.getLocalizedMessage());
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mListener.loginFailed(error.getLocalizedMessage());
            }
        });


        NetworkManager.sharedInstance().addToRequestQueue(jsonRequest);
    }

    public void getUserInfo(){
        final String USER_INFO = "me";
        final String USER_ID = "user_id";
        final String AUTHORIZATION = "AUTHORIZATION";

        Uri builtUri = Uri.parse(BASE_URL + USER_INFO).buildUpon()
                .appendQueryParameter(USER_ID, ConfigurationManager.sharedInstance().getUserID())
                .build();


        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, builtUri.toString(), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        CurrentUser currentUser =  CurrentUser.fromJson(response);
                        mListener.userInfoFetched(currentUser);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }){
            @Override
            public Map<String, String> getHeaders ()throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", ConfigurationManager.sharedInstance().getAccessToken());
                return map;
            }
        };

        NetworkManager.sharedInstance().addToRequestQueue(jsonRequest);
    }

}



