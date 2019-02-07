package com.uiu.helper.KidsHelper.mvp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Location implements Serializable{

    @SerializedName("latitude")
    String latitude;

    @SerializedName("longitude")
    String longitude;

    @SerializedName("user_id")
    String userId;
    @SerializedName("helper_id")
    String helperId;

    public Double getLatitude() {
        return Double.parseDouble(latitude);
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return Double.parseDouble(longitude);
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setHelperId(String helperId) {
        this.helperId = helperId;
    }

    @Override
    public String toString(){
        return
                "Location{" +
                        "latitude = '" + latitude + '\'' +
                        ",longitude = '" + longitude + '\'' +
                        "}";
    }
}
