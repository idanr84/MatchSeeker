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
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    static public final String CURRENT_USER = "CURRENT_USER";

    public CurrentUser getCurrentUser() {
        return mCurrentUser;
    }

    public void setCurrentUser(CurrentUser user){
        pcs.firePropertyChange(CURRENT_USER,mCurrentUser,user);
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
//        createTestPotentialMatches();
    }

    public void refreshCurrentUser(){
        mCurrentUser = ConfigurationManager.sharedInstance().getCurrentUser();
    }

    public ArrayList<PotentialMatch> getPotentialMatches(){
        return mCurrentUser.getPotentialMatches();
    }

    public ArrayList<PotentialMatch> returnTestPotentialMatches() {
        Bitmap testBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.linda);
        ArrayList<String> urls = new ArrayList<>();
        urls.add("https://scontent.xx.fbcdn.net/hphotos-xpa1/v/t1.0-0/p130x130/10393773_10153213180429219_3744986111755366759_n.jpg?oh=8761c45c5f18d30150c201663cfe0634&oe=56B2AC15");
        urls.add("https://scontent.xx.fbcdn.net/hphotos-xpa1/v/t1.0-0/p130x130/10393773_10153213180429219_3744986111755366759_n.jpg?oh=8761c45c5f18d30150c201663cfe0634&oe=56B2AC15");
        urls.add("https://scontent.xx.fbcdn.net/hphotos-xpa1/v/t1.0-0/p130x130/10393773_10153213180429219_3744986111755366759_n.jpg?oh=8761c45c5f18d30150c201663cfe0634&oe=56B2AC15");
        urls.add("https://scontent.xx.fbcdn.net/hphotos-xpa1/v/t1.0-0/p130x130/10393773_10153213180429219_3744986111755366759_n.jpg?oh=8761c45c5f18d30150c201663cfe0634&oe=56B2AC15");
        urls.add("https://scontent.xx.fbcdn.net/hphotos-xpa1/v/t1.0-0/p130x130/10393773_10153213180429219_3744986111755366759_n.jpg?oh=8761c45c5f18d30150c201663cfe0634&oe=56B2AC15");

        ArrayList<PotentialMatch> al = new ArrayList<PotentialMatch>();
        al.add(new PotentialMatch("idan 0","31",4,urls,"liked"));
        al.add(new PotentialMatch("idan 1 ","31",5,urls,"liked"));
        al.add(new PotentialMatch("idan 2 ","31",6,urls,"liked"));
        al.add(new PotentialMatch("idan 3","31",7,urls,"liked"));
        return  al;
    }

    public int getNumberOfPotentialMatches(){
        return getPotentialMatches().size();
    }


    public void setPotentialMatch(PotentialMatch potential, int index){
        getPotentialMatches().set(index, potential);
        ConfigurationManager.sharedInstance().setCurrentUser(mCurrentUser);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener){
        pcs.addPropertyChangeListener(CURRENT_USER, listener);
    }

    public void removePropertyChangedListener(PropertyChangeListener listener){
        pcs.removePropertyChangeListener(CURRENT_USER, listener);
    }


//    public ArrayList<PotentialMatch> getPotentialMatches(){
//        return mPotentialMatches;
//    }




}
