package com.uiu.helper.KidsHelper.mvp.events;


import com.uiu.helper.KidsHelper.mvp.Constant;

import org.json.JSONObject;

public class NotificationReceiveEvent implements Constant {
    JSONObject object;
    int notificationType;
    int status=3;
    private String title;
    private String message;


    public NotificationReceiveEvent(int notificationType) {
        this.notificationType = notificationType;
    }

    public NotificationReceiveEvent(String title, String message, JSONObject object, int notificationType, int status) {
        this.title = title;
        this.message = message;
        this.notificationType = notificationType;
        this.object = object;
        this.status=status;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public JSONObject getNotificationResponse(){
        return object;
    }
    public int getNotificationForSlideType() {
        return notificationType;
    }
    public int getStatus() {
        return status;
    }

}
