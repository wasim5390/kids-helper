package com.uiu.helper.KidsHelper.mvp.model.response;

import com.google.gson.annotations.SerializedName;
import com.uiu.helper.KidsHelper.mvp.model.User;

public class GetAccountResponse extends BaseResponse {
    @SerializedName("user")
    private User user;

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
