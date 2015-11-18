package com.example.idanr.MatchSeeker.CustomApplication;

import android.app.Application;

/**
 * Created by idanr on 11/5/15.
 */
public class TinderApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationLifecycleHandler handler = new ApplicationLifecycleHandler();
        registerActivityLifecycleCallbacks(handler);
        registerComponentCallbacks(handler);

    }
}
