package com.example.idanr.tinderforjavaclass.Facebook;

import com.example.idanr.tinderforjavaclass.Configuration.ConfigurationManager;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by idanr on 10/25/15.
 */
public class FacebookManager {
    private static FacebookManager mSharedInstace;
    private Boolean mIsFetchingUserInfo = false;
    public static FacebookManager  sharedInstance() {
        if (mSharedInstace == null) {
            mSharedInstace = new FacebookManager ();
        }
        return mSharedInstace;

    }

    public void fetchUserInfo() {
        if (mIsFetchingUserInfo) return;;

        mIsFetchingUserInfo = true;
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken, new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject me, GraphResponse response) {
                            mIsFetchingUserInfo = false;
                            try {
                                String id = me.getString("id");
                                ConfigurationManager.sharedInstance().setFacebookID(id);
                            }
                            catch (JSONException e){

                            }
                        }
                    });
//            Bundle parameters = new Bundle();
//            parameters.putString(FIELDS, REQUEST_FIELDS);
//            request.setParameters(parameters);
            GraphRequest.executeBatchAsync(request);
        } else {
//            user = null;
        }
    }

}
