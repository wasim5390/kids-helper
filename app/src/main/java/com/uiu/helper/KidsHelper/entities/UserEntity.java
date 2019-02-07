package com.uiu.helper.KidsHelper.entities;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.uiu.helper.KidsHelper.constants.AppConstants;
import com.uiu.helper.KidsHelper.constants.KeyConstants;
import com.uiu.helper.KidsHelper.framework.utilities.JsonUtility;
import com.uiu.helper.KidsHelper.mvp.model.Invitation;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserEntity extends BaseEntity implements Serializable{


    @SerializedName(KeyConstants.KEY_FIRST_NAME)
    private String firstName;
    @SerializedName(KeyConstants.KEY_LAST_NAME)
    private String lastName;
    @SerializedName(KeyConstants.KEY_EMAIL)
    private String email;
    @SerializedName(KeyConstants.KEY_PASSWORD)
    private String password;
    @SerializedName(KeyConstants.KEY_ID)
    private String userId;
    @SerializedName(KeyConstants.KEY_USER_TYPE)
    private int userType;
    @SerializedName(KeyConstants.KEY_PICTURE)
    private String profilePicture;
    @SerializedName(KeyConstants.KEY_FCM_KEY)
    private String fcmKey;
    @SerializedName(KeyConstants.KEY_INVITES)
    private List<Invitation> invitations;


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setFcmKey(String fcmKey) {
        this.fcmKey = fcmKey;
    }

    public String getFcmKey() {
        return fcmKey;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public List<Invitation> getInvitations() {
        return invitations==null?new ArrayList<>():invitations;
    }

    public void set(String jsonString) {

        JsonObject userObject = JsonUtility.parseToJsonObject(jsonString);
        this.email = JsonUtility.getString(userObject, KeyConstants.KEY_EMAIL);
        this.firstName = JsonUtility.getString(userObject, KeyConstants.KEY_FIRST_NAME);
        this.lastName = JsonUtility.getString(userObject, KeyConstants.KEY_LAST_NAME);
        this.profilePicture = JsonUtility.getString(userObject, KeyConstants.KEY_PICTURE);
        this.userId = JsonUtility.getString(userObject, KeyConstants.KEY_ID);
        this.fcmKey = JsonUtility.getString(userObject, KeyConstants.KEY_FCM_KEY);
        this.userType = JsonUtility.getInt(userObject, KeyConstants.KEY_USER_TYPE);
        invitations = new Gson().fromJson(userObject.getAsJsonArray(KeyConstants.KEY_INVITES), new TypeToken<List<Invitation>>(){}.getType());


    }

    public String getRegistrationRequest() {

        JsonObject requestJson = new JsonObject();

        requestJson.addProperty(KeyConstants.KEY_EMAIL, email);
        requestJson.addProperty(KeyConstants.KEY_PASSWORD, email);
        requestJson.addProperty(KeyConstants.KEY_FIRST_NAME, firstName);
        requestJson.addProperty(KeyConstants.KEY_LAST_NAME, lastName);
        requestJson.addProperty(KeyConstants.KEY_FCM_KEY, fcmKey);
        requestJson.addProperty(KeyConstants.KEY_PICTURE, profilePicture);
        requestJson.addProperty(KeyConstants.KEY_USER_TYPE, AppConstants.ParseConstants.TYPE_COMPANION);
        return requestJson.toString();
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
    public String getName() {
        String name="";
        if(email!=null && !email.isEmpty())
            name = email;
        if(firstName!=null && !firstName.isEmpty())
            name = firstName;
        if(lastName!=null && !lastName.isEmpty())
            name = name.concat(" "+lastName);

        return name;
    }


}
