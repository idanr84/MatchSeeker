package com.example.idanr.tinderforjavaclass.BusinessLogic.Setting;

import android.app.Activity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.RadioButton;

import com.example.idanr.tinderforjavaclass.Configuration.ConfigurationManager;
import com.example.idanr.tinderforjavaclass.Model.CurrentUser;
import com.example.idanr.tinderforjavaclass.NetworkManager.NetworkClient;
import com.example.idanr.tinderforjavaclass.R;
import com.example.idanr.tinderforjavaclass.StorageManager.StorageManager;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by idanr on 11/8/15.
 */
public class SettingActivity extends Activity {

    @Bind(R.id.rangeBar)
    RangeSeekBar mRangeBar;

    @Bind(R.id.segmentedGender)
    info.hoang8f.android.segmented.SegmentedGroup mSegmentGender;


    //ConfigurationManager.GENDER mRequestGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.setting_activity);
        ButterKnife.bind(this);

//        mSegmentGender.setOnCheckedChangeListener(this);

        //container.setTransitionGroup(true);
        Slide animation = new Slide(Gravity.LEFT);
        animation.setDuration(500);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.excludeTarget(android.R.id.navigationBarBackground, true);
        animation.excludeTarget(android.R.id.statusBarBackground, true);

        //container.setTransitionGroup(true);
        Slide animation1 = new Slide(Gravity.LEFT);
        animation1.setDuration(500);
        animation1.setInterpolator(new DecelerateInterpolator());
        animation1.excludeTarget(android.R.id.navigationBarBackground, true);
        animation1.excludeTarget(android.R.id.statusBarBackground, true);


        getWindow().setEnterTransition(animation);
        getWindow().setReturnTransition(animation1);

        mRangeBar.setRangeValues(18, 40);
        mRangeBar.setSelectedMinValue(ConfigurationManager.sharedInstance().getMinAge());
        mRangeBar.setSelectedMaxValue(ConfigurationManager.sharedInstance().getMaxAge());
        mRangeBar.setSelected(true);

        ConfigurationManager.GENDER requesedtGender = ConfigurationManager.sharedInstance().getGender();
//        mSegmentGender.check(requesedtGender .ordinal());
        ((RadioButton) mSegmentGender.getChildAt(requesedtGender .ordinal())).setChecked(true);

    }

    @Override
    protected void onStop() {
        super.onStop();

        int radioButtonID = mSegmentGender.getCheckedRadioButtonId();
        View radioButton = mSegmentGender.findViewById(radioButtonID);
        int index = mSegmentGender.indexOfChild(radioButton);
        final ConfigurationManager.GENDER gender = ConfigurationManager.GENDER.values()[index];

        final int minAge = mRangeBar.getSelectedMinValue().intValue();
        final int maxAge = mRangeBar.getSelectedMaxValue().intValue();

        final NetworkClient client = new NetworkClient();
        client.updateUserSetting(minAge ,maxAge ,gender.toString(),new NetworkClient.UserSettingUpdateListener() {
            @Override
            public void settingUploadSucceded() {
                client.getUserInfo(new NetworkClient.UserInfoListener() {
                    @Override
                    public void userInfoFetched(CurrentUser currentUser) {
                        ConfigurationManager.sharedInstance().setMinAge(minAge);
                        ConfigurationManager.sharedInstance().setMaxAge(maxAge);
                        ConfigurationManager.sharedInstance().setGender(gender);
                        StorageManager.sharedInstance().setCurrentUser(currentUser);
                    }
                });
            }
        });

    }
}
