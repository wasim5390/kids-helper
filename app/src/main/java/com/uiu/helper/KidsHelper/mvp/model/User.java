package com.uiu.helper.KidsHelper.mvp.model;

import com.google.gson.annotations.SerializedName;
import com.uiu.helper.KidsHelper.entities.UserEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable{


    @SerializedName("user_type")
    private int userType;

    @SerializedName("image_link")
    private String imageLink;

    @SerializedName("fcm_key")
    private String fcmKey;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("id")
    private String id;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("email")
    private String email;

    @SerializedName("name")
    private String username;

    @SerializedName("kids")
    private List<User> kids;

    @SerializedName("phone_number")
    private String phoneNumber;

    private boolean isPrimary=false;




    public boolean isPrimary() {
        return isPrimary;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public List<User> getKids() {
        return  kids==null?new ArrayList<>():kids;
    }

    public void setKids(List<User> kids) {
        this.kids = kids;
    }

    public void setUserType(int userType){
        this.userType = userType;
    }

    public int getUserType(){
        return userType;
    }

    public void setImageLink(String imageLink){
        this.imageLink = imageLink;
    }

    public String getImageLink(){
        return imageLink;
    }

    public void setFcmKey(String fcmKey){
        this.fcmKey = fcmKey;
    }

    public String getFcmKey(){
        return fcmKey;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return email;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getUsername(){
        return username;
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


    @Override
    public String toString(){
        return
                "User{" +
                        "user_type = '" + userType + '\'' +
                        ",image_link = '" + imageLink + '\'' +
                        ",fcm_key = '" + fcmKey + '\'' +
                        ",last_name = '" + lastName + '\'' +
                        ",id = '" + id + '\'' +
                        ",first_name = '" + firstName + '\'' +
                        ",email = '" + email + '\'' +
                        ",username = '" + username + '\'' +
                        "}";
    }
}
