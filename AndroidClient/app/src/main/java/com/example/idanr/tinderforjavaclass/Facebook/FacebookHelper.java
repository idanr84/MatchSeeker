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
public class FacebookHelper {

    private FacebookLoginListener mListener;
    private Boolean mIsFetchingUserInfo = false;

    public interface FacebookLoginListener{
        public void fetchedFacebookInfoSuccess(String facebookToken, String facebookID);
        public void fetchedFacebookInfoFailure(String error);
    }

    public FacebookHelper(FacebookLoginListener listener){
        mListener = listener;
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
                                mListener.fetchedFacebookInfoSuccess(accessToken.getToken(),id);
                            }
                            catch (JSONException e){
                                mListener.fetchedFacebookInfoFailure(e.getLocalizedMessage());
                            }
                        }
                    });
            GraphRequest.executeBatchAsync(request);
        } else {

        }
    }

}
