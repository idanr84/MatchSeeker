package com.example.idanr.MatchSeeker.BusinessLogic.UserDetails;

import android.app.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;


import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.transition.ChangeBounds;
import android.transition.Slide;
import android.transition.TransitionSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.idanr.MatchSeeker.Model.BaseUser;
import com.example.idanr.MatchSeeker.Model.CurrentUser;
import com.example.idanr.MatchSeeker.NetworkManager.NetworkManager;
import com.example.idanr.MatchSeeker.R;
import com.example.idanr.MatchSeeker.StorageManager.StorageManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by idanr on 10/26/15.
 */
public class UserDetailsActivity extends Activity{

    static final int NUM_ITEMS = 10;

    @Bind(R.id.userImages)
    ViewPager mUserImages;

    @Bind(R.id.userName)
    TextView mUserName;

    @Bind(R.id.userAge)
    TextView mUserAge;

    @Bind(R.id.userLocation)
    TextView mUserLocation;

    MyAdapter mAdapter;
    BaseUser mBaseUser;

    CurrentUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_details_activity);
        ButterKnife.bind(this);

        mCurrentUser = StorageManager.sharedInstance().getCurrentUser();
        final int index = getIntent().getExtras().getInt("user_id");
        boolean isMatch = getIntent().getExtras().getBoolean("is_match");
        if (isMatch){
            mBaseUser = mCurrentUser.getMatchAtIndex(index);
        } else {
            mBaseUser = mCurrentUser.getPotentialMatchAtIndex(index);
        }


        mUserName.setText(mBaseUser.getName());
        mUserAge.setText(mBaseUser.getAge());
        mUserLocation.setText(mBaseUser.getLocation());


        mAdapter = new MyAdapter(getFragmentManager(), mBaseUser);
        mUserImages.setAdapter(mAdapter);
        mUserImages.setCurrentItem(mBaseUser.getCurrentImagePage());

        mUserImages.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBaseUser.setCurrentImagePage(position);
//                StorageManager.sharedInstance().setPotentialMatch(mBaseUser,index);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TransitionSet animationSet = new TransitionSet();
        animationSet.addTransition(new ChangeBounds());
        animationSet.setDuration(500);
        getWindow().setSharedElementEnterTransition(animationSet);
        getWindow().setSharedElementExitTransition(animationSet);

        Slide slideEnterTransition = new Slide();
        slideEnterTransition.excludeChildren(R.id.userImages, true);
        slideEnterTransition.excludeChildren(R.id.container, true);
        slideEnterTransition.excludeTarget(android.R.id.navigationBarBackground, true);
        slideEnterTransition.excludeTarget(android.R.id.statusBarBackground, true);
        OvershootInterpolator overshoot = new OvershootInterpolator((float)0.5);
        slideEnterTransition.setInterpolator(overshoot);
        slideEnterTransition.setDuration(800);

        //slideEnterTransition.setStartDelay(200);
        getWindow().setEnterTransition(slideEnterTransition);

        Slide slideTransition = new Slide();
        slideTransition .excludeChildren(R.id.userImages, true);
        slideTransition .excludeChildren(R.id.container, true);
        slideTransition.excludeTarget(android.R.id.navigationBarBackground, true);
        slideTransition.excludeTarget(android.R.id.statusBarBackground, true);
        slideTransition .setDuration(800);
        getWindow().setReturnTransition(slideTransition);

        getWindow().setSharedElementsUseOverlay(false);
    }

    public static class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm, BaseUser potentialMatch){
            super(fm);
            mPtentialMatch = potentialMatch;
        }
        private BaseUser mPtentialMatch;

        @Override
        public int getCount() {
            return mPtentialMatch.getNumOfImages();
        }

        @Override
        public Fragment getItem(int position) {
            return UserImageFragment.newInstance(position,mPtentialMatch.getImageUrlAtIndex(position));
        }
    }

    public static class UserImageFragment extends Fragment {
        int mNum;
        String mImageUrl;

        /**
         * Create a new instance of UserImageFragment, providing "num"
         * as an argument.
         */
        static UserImageFragment newInstance(int num,String imageUrl) {
            UserImageFragment f = new UserImageFragment();

            // Supply num input as an argument.
            Bundle args = new Bundle();
            args.putInt("num", num);
            args.putString("imageUrl", imageUrl);
            f.setArguments(args);

            return f;
        }

        /**
         * When creating, retrieve this instance's number from its arguments.
         */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mNum = getArguments() != null ? getArguments().getInt("num") : 1;
            mImageUrl = getArguments() != null ? (String)getArguments().getString("imageUrl") : null;
        }

        /**
         * The Fragment's UI is just a simple text view showing its
         * instance number.
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.user_images_pager, container, false);
            NetworkImageView userImage = (NetworkImageView)v.findViewById(R.id.image);
            userImage.setImageUrl(mImageUrl, NetworkManager.sharedInstance().getImageLoader());
            return v;
        }

    }

}
