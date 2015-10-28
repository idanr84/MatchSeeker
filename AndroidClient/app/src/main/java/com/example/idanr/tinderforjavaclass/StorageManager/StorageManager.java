package com.example.idanr.tinderforjavaclass.StorageManager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ArrayAdapter;

import com.example.idanr.tinderforjavaclass.Model.User;
import com.example.idanr.tinderforjavaclass.R;

import java.util.ArrayList;

/**
 * Created by idanr on 10/28/15.
 */
public class StorageManager {

    static StorageManager mSharedInstace;
    private Context mContext;
    private ArrayList<User> mPotentialMatches;

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
        createTestPotentialMatches();
    }

    private void createTestPotentialMatches() {
        Bitmap testBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.linda);
        ArrayList<String> urls = new ArrayList<>();
        urls.add("1231");
        urls.add("1231");
        urls.add("1231");
        urls.add("1231");
        urls.add("1231");

        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        bitmaps.add(testBitmap);
        bitmaps.add(testBitmap);
        bitmaps.add(testBitmap);
        bitmaps.add(testBitmap);
        bitmaps.add(testBitmap);

        ArrayList<User> al = new ArrayList<User>();
        al.add(new User("idan 0","31",urls,bitmaps));
        al.add(new User("idan 1 ","31",urls,bitmaps));
        al.add(new User("idan 2 ","31",urls,bitmaps));
        al.add(new User("idan 3","31",urls,bitmaps));
        mPotentialMatches = al;
    }

    public int getNumberOfPotentialMatches(){
        return mPotentialMatches.size();
    }

    public User getPotentialMatchAtIndex(int position){
        return mPotentialMatches.get(position);
    }

    public ArrayList<User> getPotentialMatches(){
        return mPotentialMatches;
    }


}
