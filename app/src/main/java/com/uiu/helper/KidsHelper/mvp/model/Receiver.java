
package com.uiu.helper.KidsHelper.mvp.model;

import com.google.gson.annotations.SerializedName;

public class Receiver {

    @SerializedName("email")
    private String mEmail;
    @SerializedName("first_name")
    private String mFirstName;
    @SerializedName("id")
    private String mId;
    @SerializedName("image_link")
    private String mImageLink;
    @SerializedName("last_name")
    private String mLastName;
    @SerializedName("mobile_number")
    private String mMobileNumber;
    @SerializedName("user_type")
    private Long mUserType;
    @SerializedName("username")
    private String mUsername;

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getFirstName() {
        return mFirstName!=null?mFirstName:"";
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getImageLink() {
        return mImageLink!=null?mImageLink:"www.empty";
    }

    public void setImageLink(String imageLink) {
        mImageLink = imageLink;
    }

    public String getLastName() {
        return mLastName!=null?mLastName:"";
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getMobileNumber() {
        return mMobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        mMobileNumber = mobileNumber;
    }

    public Long getUserType() {
        return mUserType;
    }

    public void setUserType(Long userType) {
        mUserType = userType;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }
    public String getName() {
        return getFirstName()+" "+getLastName() ;
    }

}
