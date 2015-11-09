package com.example.idanr.tinderforjavaclass.BusinessLogic.Setting;

import android.app.Activity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

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
    RangeSeekBar rangeBar;

    @Bind(R.id.container)
    LinearLayout container;

    @Bind(R.id.segmentedGender)
    info.hoang8f.android.segmented.SegmentedGroup segmentGender;


    //ConfigurationManager.GENDER mRequestGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.setting_activity);
        ButterKnife.bind(this);

//        segmentGender.setOnCheckedChangeListener(this);

        container.setTransitionGroup(true);
        Slide animation = new Slide(Gravity.LEFT);
        animation.setDuration(500);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.excludeTarget(android.R.id.navigationBarBackground, true);
        animation.excludeTarget(android.R.id.statusBarBackground, true);

        container.setTransitionGroup(true);
        Slide animation1 = new Slide(Gravity.LEFT);
        animation1.setDuration(500);
        animation1.setInterpolator(new DecelerateInterpolator());
        animation1.excludeTarget(android.R.id.navigationBarBackground, true);
        animation1.excludeTarget(android.R.id.statusBarBackground, true);


        getWindow().setEnterTransition(animation);
        getWindow().setReturnTransition(animation1);

        rangeBar.setRangeValues(18, 40);
        rangeBar.setSelectedMinValue(ConfigurationManager.sharedInstance().getMinAge());
        rangeBar.setSelectedMaxValue(ConfigurationManager.sharedInstance().getMaxAge());
        rangeBar.setSelected(true);

        ConfigurationManager.GENDER requesedtGender = ConfigurationManager.sharedInstance().getGender();
//        segmentGender.check(requesedtGender .ordinal());
        ((RadioButton)segmentGender.getChildAt(requesedtGender .ordinal())).setChecked(true);

    }

    @Override
    protected void onStop() {
        super.onStop();

        int radioButtonID = segmentGender.getCheckedRadioButtonId();
        View radioButton = segmentGender.findViewById(radioButtonID);
        int index = segmentGender.indexOfChild(radioButton);
        final ConfigurationManager.GENDER gender = ConfigurationManager.GENDER.values()[index];

        final int minAge = rangeBar.getSelectedMinValue().intValue();
        final int maxAge = rangeBar.getSelectedMaxValue().intValue();

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
