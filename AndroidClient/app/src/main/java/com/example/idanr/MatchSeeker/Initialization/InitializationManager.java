package com.example.idanr.MatchSeeker.Initialization;

import android.content.Context;

import com.example.idanr.MatchSeeker.Configuration.ConfigurationManager;
import com.example.idanr.MatchSeeker.NetworkManager.NetworkManager;
import com.example.idanr.MatchSeeker.StorageManager.StorageManager;

/**
 * Created by idanr on 10/24/15.
 */
public class InitializationManager {

    public static void initSystemWithContext(Context context) {
        ConfigurationManager.init(context);
        StorageManager.sharedInstance().initWithContext(context);
        NetworkManager.sharedInstance().initWithContext(context);
    }
}
