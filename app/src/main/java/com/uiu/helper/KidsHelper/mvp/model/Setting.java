package com.uiu.helper.KidsHelper.mvp.model;


import android.content.Intent;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Setting implements Serializable {

    @SerializedName("location")
    boolean isLocationEnable=false;
    @SerializedName("wifi")
    boolean isWifiEnable=true;
    @SerializedName("bluetooth")
    boolean isBlueTooth=false;

    @SerializedName("sleep")
    boolean isSleepMode = false;

    @SerializedName("timer_enabled")
    boolean isTimedSleepEnable=false;

    @SerializedName("sound_state")
    int soundState=0;
    @SerializedName("brightness")
    int brightnessLevel=50;
    @SerializedName("volume")
    int volumeLevel=50;
    @SerializedName("battery")
    Integer batteryLevel=50;

    public boolean isTimedSleepEnable() {
        return isTimedSleepEnable;
    }

    public void setTimedSleepEnable(boolean timedSleepEnable) {
        isTimedSleepEnable = timedSleepEnable;
    }

    public long getWakeUpTime() {
        return wakeUpTime==null?System.currentTimeMillis():Long.valueOf(wakeUpTime);
    }

    public void setWakeUpTime(String wakeUpTime) {
        this.wakeUpTime = wakeUpTime;
    }

    @SerializedName("sleep_time")
    String wakeUpTime;

    boolean isVibrate;

    public boolean isSleepMode() {
        return isSleepMode;
    }

    public void setSleepMode(boolean sleepMode) {
        isSleepMode = sleepMode;
    }

    public boolean isLocationEnable() {
        return isLocationEnable;
    }

    public void setLocationEnable(boolean locationEnable) {
        isLocationEnable = locationEnable;
    }

    public boolean isWifiEnable() {
        return isWifiEnable;
    }

    public void setWifiEnable(boolean wifiEnable) {
        isWifiEnable = wifiEnable;
    }

    public boolean isBlueToothOn() {
        return isBlueTooth;
    }

    public void setBlueToothOn(boolean blueTooth) {
        isBlueTooth = blueTooth;
    }

    public boolean isVibrate() {
        return isVibrate;
    }

    public void setVibrate(boolean vibrate) {
        isVibrate = vibrate;
    }

    public int getBrightnessLevel() {
        return brightnessLevel;
    }

    public void setBrightnessLevel(int brightnessLevel) {
        this.brightnessLevel = brightnessLevel;
    }

    public int getVolumeLevel() {
        return volumeLevel;
    }

    public void setVolumeLevel(int volumeLevel) {
        this.volumeLevel = volumeLevel;
    }

    public int getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(Integer batteryLevel) {
        this.batteryLevel = batteryLevel;
    }
    public int getSoundState() {
        return soundState;
    }

    public void setSoundState(int soundState) {
        this.soundState = soundState;
    }

}
