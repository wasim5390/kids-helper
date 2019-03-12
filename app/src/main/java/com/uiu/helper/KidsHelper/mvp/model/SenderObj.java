package com.uiu.helper.KidsHelper.mvp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SenderObj implements Serializable {


    @SerializedName("id")
    String id;
    @SerializedName("name")
    String name;
    @SerializedName("image")
    String image;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }
}
