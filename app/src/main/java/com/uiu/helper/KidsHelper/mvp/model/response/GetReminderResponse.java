package com.uiu.helper.KidsHelper.mvp.model.response;

import com.google.gson.annotations.SerializedName;
import com.uiu.helper.KidsHelper.mvp.ui.slides.reminder.ReminderEntity;

import java.io.Serializable;
import java.util.List;

public class GetReminderResponse extends BaseResponse {


    @SerializedName("reminders")
    public List<ReminderEntity> reminders;

    public ReminderEntity getReminder() {
        return mReminder;
    }

    @SerializedName("reminder")
    public ReminderEntity mReminder;

    public List<ReminderEntity> getReminders() {
        return reminders;
    }


    @Override
    public String toString(){
        return
                "GetReminderResponse{" +
                        "reminder = '" + reminders + '\'' +
                        "}";
    }
}
