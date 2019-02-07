package com.uiu.helper.KidsHelper.config;

import com.google.gson.Gson;
import com.uiu.helper.CompanionApp;
import com.uiu.helper.KidsHelper.constants.AppConstants;
import com.uiu.helper.KidsHelper.entities.UserEntity;
import com.uiu.helper.KidsHelper.helpers.EncryptionHelper;
import com.uiu.helper.KidsHelper.helpers.PreferenceUtility;

public class AppConfig {

    static AppConfig instance;
    private static UserEntity user;

    public static AppConfig getInstance() {

        if (instance == null)
            instance = new AppConfig();

        return instance;
    }

    public UserEntity getUser() {
        if(user!=null)
            return user;
        String json = PreferenceUtility.getString(CompanionApp.getContext(),AppConstants.User.USER_OBJ,"");
        return new Gson().fromJson(json, UserEntity.class);
    }

    public void setUser(UserEntity user) {
        this.user = user;
        PreferenceUtility.setString(CompanionApp.getContext(),AppConstants.User.USER_OBJ,new Gson().toJson(user));
        setUserId(user.getUserId());
    }

    public String getUserId() {

        return EncryptionHelper.decrypt(PreferenceUtility.getString(CompanionApp.getInstance(), AppConstants.User.OBJECT_ID, ""));
    }

    public void setUserId(String userId) {

        PreferenceUtility.setString(CompanionApp.getInstance(), AppConstants.User.OBJECT_ID, EncryptionHelper.encrypt(userId));
    }
}
