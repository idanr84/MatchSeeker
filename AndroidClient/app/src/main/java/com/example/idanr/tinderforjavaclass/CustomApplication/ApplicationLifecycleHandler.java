package com.example.idanr.tinderforjavaclass.CustomApplication;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.Bundle;

import android.util.Log;

import com.example.idanr.tinderforjavaclass.Model.CurrentUser;
import com.example.idanr.tinderforjavaclass.NetworkManager.NetworkClient;
import com.example.idanr.tinderforjavaclass.StorageManager.StorageManager;

/**
 * Created by idanr on 11/5/15.
 */

public class ApplicationLifecycleHandler implements Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {

    private static final String TAG = ApplicationLifecycleHandler.class.getSimpleName();

    private static boolean isInBackground = false;
    private NetworkClient client = new NetworkClient();

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if(isInBackground){
            Log.d(TAG, "app went to foreground");
            isInBackground = false;

            client.getUserInfo(new NetworkClient.UserInfoListener() {
                @Override
                public void userInfoFetched(CurrentUser currentUser) {
                    StorageManager.sharedInstance().setCurrentUser(currentUser);
                }
            });
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
    }

    @Override
    public void onLowMemory() {
    }

    @Override
    public void onTrimMemory(int i) {
        if(i == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN){
            Log.d(TAG, "app went to background");

            client.updateServer(StorageManager.sharedInstance().getCurrentUser(),null);
            isInBackground = true;
        }
    }
}
