package com.uiu.helper.KidsHelper.mvp.model.response;

import com.google.gson.annotations.SerializedName;
import com.uiu.helper.KidsHelper.mvp.model.Setting;

import java.util.List;

public class GetSettingsResponse extends BaseResponse {
    @SerializedName("setting")
    public Setting settings;

    public Setting getSettings() {
        return settings;
    }


    @Override
    public String toString(){
        return
                "GetSettingsResponse{" +
                        "settings = '" + settings + '\'' +
                        "}";
    }
}
