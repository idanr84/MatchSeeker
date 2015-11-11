package com.example.idanr.tinderforjavaclass.StorageManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.idanr.tinderforjavaclass.Configuration.ConfigurationManager;
import com.example.idanr.tinderforjavaclass.Model.CurrentUser;
import com.example.idanr.tinderforjavaclass.Model.PotentialMatch;
import com.example.idanr.tinderforjavaclass.R;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/**
 * Created by idanr on 10/28/15.
 */
public class StorageManager {

    static StorageManager mSharedInstace;
    private Context mContext;
    private CurrentUser mCurrentUser;
    private PropertyChangeSupport mPcs = new PropertyChangeSupport(this);

    static public final String CURRENT_USER = "CURRENT_USER";

    public CurrentUser getCurrentUser() {
        return mCurrentUser;
    }

    public void setCurrentUser(CurrentUser user){
        mPcs.firePropertyChange(CURRENT_USER, mCurrentUser, user);
        ConfigurationManager.sharedInstance().setCurrentUser(user);
        mCurrentUser = user;
    }

//    private ArrayList<PotentialMatch> mPotentialMatches;

    public static StorageManager sharedInstance() {
        if (mSharedInstace == null) {
            mSharedInstace = new StorageManager();
        }
        return mSharedInstace;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public void initWithContext(Context context){
        mContext = context;
        refreshCurrentUser();
    }

    public void refreshCurrentUser(){
        mCurrentUser = ConfigurationManager.sharedInstance().getCurrentUser();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener){
        mPcs.addPropertyChangeListener(CURRENT_USER, listener);
    }

    public void removePropertyChangedListener(PropertyChangeListener listener){
        mPcs.removePropertyChangeListener(CURRENT_USER, listener);
    }

}
