package com.uiu.helper.KidsHelper.mvp.ui.slides.sleep;

import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.uiu.helper.KidsHelper.mvp.BaseFragment;
import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.events.NotificationReceiveEvent;
import com.uiu.helper.KidsHelper.mvp.model.Setting;
import com.uiu.helper.KidsHelper.mvp.source.Repository;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;
import com.uiu.helper.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import butterknife.BindView;
import butterknife.OnClick;

public class SleepFragment extends BaseFragment implements SleepContract.View {


    private static final String TAG =  SleepFragment.class.getSimpleName();

    boolean mSleepMode,mSleepWithTimer;

    @BindView(R.id.swSleep)
    SwitchCompat mSleepIcon;

    @BindView(R.id.cbTimer)
    CheckBox cbTimer;

    @BindView(R.id.timePicker)
    TimePicker timePicker;
    String mKidId;

    String timeString;
    private Setting setting;
    private SleepContract.Presenter presenter;
    SimpleDateFormat sdft= new SimpleDateFormat("hh:mm aa");
    Calendar myCalendar = Calendar.getInstance();
    @Nullable
    public static SleepFragment newInstance(String kidId){
        SleepFragment settignsFragment= new SleepFragment();
        Bundle args = new Bundle();
        args.putString(KEY_SELECTED_KID, kidId);
        settignsFragment.setArguments(args);
        return settignsFragment;
    }



    @Override
    public int getID() {
        return R.layout.fragment_sleep_setting;
    }

    @Override
    public void initUI(View view) {
        EventBus.getDefault().register(this);
        mKidId = getArguments().getString(KEY_SELECTED_KID);

        initFields();
        if(presenter==null)
            presenter =  new SleepPresenter(this,mKidId,PreferenceUtil.getInstance(getActivity()), Repository.getInstance());
        presenter.getSettings();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    private void switchColor(SwitchCompat switchCompat) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                switchCompat.getThumbDrawable().setColorFilter(switchCompat.isChecked() ?
                        getContext().getResources().getColor(R.color.Green) : getContext().getResources().getColor(R.color.Red), PorterDuff.Mode.MULTIPLY);
                switchCompat.getTrackDrawable().setColorFilter(switchCompat.isChecked() ?
                        getContext().getResources().getColor(R.color.SeaGreen) : getContext().getResources().getColor(R.color.app_bg), PorterDuff.Mode.MULTIPLY);
            }
        }catch (NullPointerException ex){
            Log.e(TAG,ex.getMessage());
        }
    }

    public void updateUi(){
        long mDate = setting.getWakeUpTime();

        mSleepMode = setting.isSleepMode();
        mSleepWithTimer = setting.isTimedSleepEnable();
        timeString = sdft.format(mDate);
        myCalendar.setTimeInMillis(mDate);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.setHour(myCalendar.get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(myCalendar.get(Calendar.MINUTE));
        }else {
            timePicker.setCurrentHour(myCalendar.get(Calendar.HOUR_OF_DAY));
            timePicker.setCurrentMinute(myCalendar.get(Calendar.MINUTE));
        }

        mSleepIcon.setChecked(mSleepMode);
        cbTimer.setChecked(mSleepWithTimer);
        timePicker.setVisibility(mSleepWithTimer?View.VISIBLE:View.INVISIBLE);
        switchColor(mSleepIcon);

        mSleepIcon.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mSleepMode = isChecked;
            //if condition
            switchColor(mSleepIcon);
            mSleepIcon.setChecked(isChecked);
            setting.setSleepMode(isChecked);


        });

        cbTimer.setOnCheckedChangeListener((buttonView, isChecked) -> {
            mSleepWithTimer = isChecked;
            cbTimer.setChecked(isChecked);
            setting.setTimedSleepEnable(isChecked);
            timePicker.setVisibility(mSleepWithTimer?View.VISIBLE:View.INVISIBLE);
        });

    }




    private void initFields(){

        setting = PreferenceUtil.getInstance(getContext()).getKidDeviceSettings(mKidId);
        updateUi();
    }

    private void updateSettings() {
            selectTime();
        if(mKidId !=null){
            HashMap<String, Object> params = new HashMap<>();
            params.put("sleep_time",setting.getWakeUpTime());
            params.put("sleep", setting.isSleepMode());
            params.put("timer_enabled",setting.isTimedSleepEnable());
            presenter.updateSettings(params);
        }
    }

    @OnClick(R.id.btnChange)
    public void onChangeSettingClick() {
        updateSettings();
    }


    @Override
    public void showMessage(String message) {

    }

    @Override
    public void onSettingsLoaded(Setting settings) {
        this.setting = settings;
        PreferenceUtil.getInstance(getContext()).saveKidsDeviceSettings(mKidId,settings);
        updateUi();
    }

    @Override
    public void onSettingsUpdated(Setting settings) {
        this.setting=settings;
        updateUi();
    }

    @Override
    public void setPresenter(SleepContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

    }

    private void selectTime(){

        int hour = 0;
        int min = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            hour = timePicker.getHour();
            min = timePicker.getMinute();
        }else{
            min = timePicker.getCurrentMinute();
            hour = timePicker.getCurrentHour();
        }

        myCalendar.set(Calendar.HOUR_OF_DAY,hour);
        myCalendar.set(Calendar.MINUTE,min);
        setting.setWakeUpTime(String.valueOf(myCalendar.getTimeInMillis()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Unregister");
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(NotificationReceiveEvent receiveEvent) {
        if(receiveEvent.getNotificationForSlideType()== Constant.REQ_SETTINGS) {
         //   JSONObject jsonObject = receiveEvent.getNotificationResponse();
         //   Setting setting =  new Gson().fromJson(jsonObject.toString(),Setting.class);
         //   onSettingsLoaded(setting);
        }

    }
}
