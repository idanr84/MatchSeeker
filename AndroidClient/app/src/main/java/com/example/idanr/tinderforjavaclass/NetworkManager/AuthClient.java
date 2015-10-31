package com.example.idanr.tinderforjavaclass.NetworkManager;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

/**
 * Created by idanr on 10/31/15.
 */
public class AuthClient {

    private final String BASE_URL = "http://tinderforjavaclass.herokuapp.com/";
    private LoginListener mListener;

    public interface LoginListener {
        void loginSuccessed(String accessToken);
        void loginFailed(String error);
//        void potentialMatches(ArrayList<BaseUser> potentialMatches);
//        void matches(ArrayList<BaseUser> potentialMatches);
    }

    public AuthClient(LoginListener listener){
        mListener = listener;
    }

    public void login(String facebookToken,String facebookID) {

        final String LOGIN_METHOD = "login";
        final String FB_ID = "user_id";
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
                            String token = response.getString("token");
                            mListener.loginSuccessed(token);
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
}
