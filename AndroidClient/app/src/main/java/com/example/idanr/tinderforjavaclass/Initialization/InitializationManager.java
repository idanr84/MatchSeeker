package com.example.idanr.tinderforjavaclass.Initialization;

import android.content.Context;

import com.example.idanr.tinderforjavaclass.Configuration.ConfigurationManager;

/**
 * Created by idanr on 10/24/15.
 */
public class InitializationManager {

    public static void initSystemWithContext(Context context) {
        ConfigurationManager.init(context);
    }
}