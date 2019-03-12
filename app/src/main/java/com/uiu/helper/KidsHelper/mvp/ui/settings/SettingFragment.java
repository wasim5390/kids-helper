package com.uiu.helper.KidsHelper.mvp.ui.settings;

import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;
import com.uiu.helper.R;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.SwitchCompat;
import butterknife.BindView;
import butterknife.OnClick;

public class SettingFragment extends BaseFragment implements SettingContract.View {

    private static final int NORMAL = 2;
    private static final int VIBRATE = 1;

    private static final String TAG =  SettingFragment.class.getSimpleName();
    //private static final double SCALE_VOLUME = 14.285;//10;// 100/10
    // private static final int SCALE_FONT = 1;//50;// 100/2
    private static final float SCALE_BRIGTNESS =  2.5f;//(float)100/255;//0.3921568627451;
    boolean mWifiOn;
    boolean mLocationOn;
    boolean mBluetooth;
    int mBatteryPrecentage;
    int mSoundState;
    int mBrightness;
    int mVolume;

    @BindView(R.id.llSetting)
    public LinearLayout llSetting;
    @BindView(R.id.swLocation)
    SwitchCompat mLocationIcon;
    @BindView(R.id.swWifi)
    SwitchCompat mWifiIcon;

    @BindView(R.id.swBluetooth)
    SwitchCompat mBluetoothIcon;
    @BindView(R.id.sbBrightness)
    AppCompatSeekBar mBrightnessBar;
    @BindView(R.id.sbVolume)
    AppCompatSeekBar mVolumeBar;
    @BindView(R.id.tvBrightnessPercent)
    TextView tvBrightnessPercent;
    @BindView(R.id.tvVolumePercent)
    TextView tvVolumePercent;
    @BindView(R.id.tvBatteryPercent)
    TextView tvBatteryPercent;


    @BindView(R.id.btnChange)
    Button btnChange;
    String mKidId;
    @BindView(R.id.action_bar_battery_icon)
    public ImageView ivBattery;
    private Setting setting;
    private SettingContract.Presenter presenter;


    @Nullable
    public static SettingFragment newInstance(String kidId){
        SettingFragment settignsFragment= new SettingFragment();
        Bundle args = new Bundle();
        args.putString(KEY_SELECTED_KID, kidId);
        settignsFragment.setArguments(args);
        return settignsFragment;
    }



    @Override
    public int getID() {
        return R.layout.fragment_helper_settings;
    }

    @Override
    public void initUI(View view) {
        EventBus.getDefault().register(this);
        mKidId = getArguments().getString(KEY_SELECTED_KID);
        setting = PreferenceUtil.getInstance(getContext()).getKidDeviceSettings(mKidId);
        initFields();
        presenter.getSettings(mKidId);
    }


    @Override
    public void onPause() {
        super.onPause();
        //updateSettings(mVolume, mBrightness, mFontScale);
    }


    private void switchColor(SwitchCompat switchCompat) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            switchCompat.getThumbDrawable().setColorFilter(switchCompat.isChecked() ?
                    getContext().getResources().getColor(R.color.Green) : getContext().getResources().getColor(R.color.Red), PorterDuff.Mode.MULTIPLY);
            switchCompat.getTrackDrawable().setColorFilter(switchCompat.isChecked() ?
                    getContext().getResources().getColor(R.color.SeaGreen) :  getContext().getResources().getColor(R.color.app_bg), PorterDuff.Mode.MULTIPLY);
        }
    }

    public void updateUi(){
        try {
            long mDate = setting.getWakeUpTime();
            mLocationOn = setting.isLocationEnable();
            Log.d("settingsTest", "locationOn: " + mLocationOn);
            mBatteryPrecentage = setting.getBatteryLevel();
            mBluetooth = setting.isBlueToothOn();

            mSoundState = setting.getSoundState();
            mBrightness = (int) setting.getBrightnessLevel();
            mVolume = (int) setting.getVolumeLevel();

            mWifiOn = setting.isWifiEnable();
            mWifiIcon.setChecked(mWifiOn);
         //   mLocationIcon.setChecked(mLocationOn);
            mBluetoothIcon.setChecked(mBluetooth);

        //    switchColor(mLocationIcon);
            switchColor(mBluetoothIcon);
            switchColor(mWifiIcon);


      /*      mLocationIcon.setOnCheckedChangeListener((buttonView, isChecked) -> {
                mLocationOn = isChecked;
                Log.d("settingsTest", "locationOn: " + mLocationOn);
                //if condition
                switchColor(mLocationIcon);
                mLocationIcon.setChecked(mLocationOn);
                setting.setLocationEnable(isChecked);

            });*/

            mWifiIcon.setOnCheckedChangeListener((buttonView, isChecked) -> {
                mWifiOn = isChecked;
                Log.d("settingsTest", "wifiOn: " + mWifiIcon);
                //if condition
                switchColor(mWifiIcon);
                mWifiIcon.setChecked(mWifiOn);
                setting.setWifiEnable(isChecked);

            });

            mBluetoothIcon.setOnCheckedChangeListener((buttonView, isChecked) -> {
                mBluetooth = isChecked;
                //if condition
                switchColor(mBluetoothIcon);
                mBluetoothIcon.setChecked(isChecked);
                setting.setBlueToothOn(isChecked);
            });
            tvBatteryPercent.setText(mBatteryPrecentage + " %");

            tvBrightnessPercent.setText((int) (mBrightness / SCALE_BRIGTNESS) + "%");
            mBrightnessBar.setProgress((int) (mBrightness / SCALE_BRIGTNESS));

            mVolumeBar.setProgress(mVolume);
            if (mVolume > 95) {
                mVolume = 100;
            }
            tvVolumePercent.setText(mVolume + "%");

            setBatteryLevel(mBatteryPrecentage);
        }catch (Exception e){
            Log.e(TAG,e.getMessage());
        }
    }



    private void setBatteryLevel(int level){
        if (level >= 0 && level <= 25){
            ivBattery.setBackgroundResource(R.drawable.ic_setting_battery_1);
        }else if (level >= 26 && level <= 50){
            ivBattery.setBackgroundResource(R.drawable.ic_setting_battery_2);
        }else if (level >= 51 && level <= 75){
            ivBattery.setBackgroundResource(R.drawable.ic_setting_battery_3);
        }else if (level >= 76 && level <= 100){
            ivBattery.setBackgroundResource(R.drawable.ic_setting_battery_4);
        }
    }

    private void initFields(){

        mBrightnessBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //send push
                //0-100
                tvBrightnessPercent.setText(progress+"%");
                mBrightness = (int) (progress * SCALE_BRIGTNESS);
             //   setting.setBrightnessLevel(mBrightness);
                //updateSettings(mVolume, mBrightness, mFontScale);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mVolumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //send push
                mVolume = (int) (progress);
                tvVolumePercent.setText(mVolume+"%");
                setting.setVolumeLevel(mVolume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        setting = PreferenceUtil.getInstance(getContext()).getKidDeviceSettings(mKidId);
        updateUi();
    }

    private void updateSettings() {

        if(mKidId !=null){
            presenter.updateSettings(setting);
        }
    }

    @OnClick(R.id.btnChange)
    public void onChangeSettingClick() {
        updateSettings();
    }

    @OnClick(R.id.btnFindPhone)
    public void findPhone(){
        presenter.findPhone(mKidId, PreferenceUtil.getInstance(getContext()).getAccount().getId());
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
        if(getActivity()!=null)
        getActivity().onBackPressed();
    }

    @Override
    public void setPresenter(SettingContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showNoInternet() {

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
            JSONObject jsonObject = receiveEvent.getNotificationResponse();
            Setting setting =  new Gson().fromJson(jsonObject.toString(),Setting.class);
            onSettingsLoaded(setting);
        }

    }
}
