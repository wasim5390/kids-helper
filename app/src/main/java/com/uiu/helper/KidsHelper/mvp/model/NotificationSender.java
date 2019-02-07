package com.uiu.helper.KidsHelper.mvp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NotificationSender implements Serializable{
    @SerializedName("name")
    String senderName;
    @SerializedName("image")
    String senderImage;

    @SerializedName("id")
    String id;

    @SerializedName("user_type")
    String userType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderImage() {
        return senderImage==null||senderImage.isEmpty()?"www.empty":senderImage;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
    }
}
