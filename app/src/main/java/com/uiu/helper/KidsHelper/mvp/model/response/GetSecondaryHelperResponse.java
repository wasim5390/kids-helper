package com.uiu.helper.KidsHelper.mvp.model.response;

import com.google.gson.annotations.SerializedName;
import com.uiu.helper.KidsHelper.mvp.model.User;

import java.util.ArrayList;
import java.util.List;

public class GetSecondaryHelperResponse extends BaseResponse {


    @SerializedName("secondary_helpers")
    List<User> mSecondaryHelpers;

    public List<User> getSecondaryHelpers() {
        return mSecondaryHelpers==null?new ArrayList<>():mSecondaryHelpers;
    }
}
