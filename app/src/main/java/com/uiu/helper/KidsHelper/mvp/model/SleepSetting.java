package com.uiu.helper.KidsHelper.mvp.model;

import com.google.gson.annotations.SerializedName;

public class SleepSetting {

    @SerializedName("sleep_time")
    String wakeUpTime;

    @SerializedName("sleep")
    boolean isSleepMode = false;

    public long getWakeUpTime() {
        return wakeUpTime==null?System.currentTimeMillis():Long.valueOf(wakeUpTime);
    }

    public void setWakeUpTime(String wakeUpTime) {
        this.wakeUpTime = wakeUpTime;
    }

    public boolean isSleepMode() {
        return isSleepMode;
    }

    public void setSleepMode(boolean sleepMode) {
        isSleepMode = sleepMode;
    }


}
