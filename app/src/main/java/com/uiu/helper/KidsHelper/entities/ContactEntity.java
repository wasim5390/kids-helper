package com.uiu.helper.KidsHelper.entities;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.uiu.helper.KidsHelper.constants.KeyConstants;
import com.uiu.helper.KidsHelper.framework.utilities.JsonUtility;

import java.util.ArrayList;

public class ContactEntity extends BaseEntity {

    private String name, firstName, lastName;
    String email;
    private String photoUri;
    private String lookupId;
    private String androidId;
    private String userId, objectId, slideId;
    private String mPhoneNumber, mHomeNumber;
    private int requestStatus;

    public void set(String jsonString) {

        JsonObject userObject = JsonUtility.parseToJsonObject(jsonString);
        this.name = JsonUtility.getString(userObject, KeyConstants.KEY_NAME);
        this.firstName = JsonUtility.getString(userObject, KeyConstants.KEY_FIRST_NAME);
        this.lastName = JsonUtility.getString(userObject, KeyConstants.KEY_LAST_NAME);
        this.lastName = JsonUtility.getString(userObject, KeyConstants.KEY_EMAIL);
        this.photoUri = JsonUtility.getString(userObject, KeyConstants.KEY_PHOTO_URI);
        this.objectId = JsonUtility.getString(userObject, KeyConstants.KEY_ID);
        this.userId = JsonUtility.getString(userObject, KeyConstants.KEY_USER_ID);
        this.requestStatus = JsonUtility.getInt(userObject, KeyConstants.KEY_REQUEST_STATUS);
        this.lookupId = JsonUtility.getString(userObject, KeyConstants.KEY_LOOKUP_ID);
        this.androidId = JsonUtility.getString(userObject, KeyConstants.KEY_ANDROID_ID);
        this.mHomeNumber = JsonUtility.getString(userObject, KeyConstants.KEY_HOME_NUMBER);
        this.mPhoneNumber = JsonUtility.getString(userObject, KeyConstants.KEY_PHONE_NUMBER);

    }

    public ContactEntity setContact(String jsonString) {

        JsonObject object = JsonUtility.parseToJsonObject(jsonString);
        ContactEntity contactEntity = new ContactEntity();
        contactEntity.setName(JsonUtility.getString(object, KeyConstants.KEY_NAME));
        contactEntity.setFirstName(JsonUtility.getString(object, KeyConstants.KEY_FIRST_NAME));
        contactEntity.setLastName(JsonUtility.getString(object, KeyConstants.KEY_LAST_NAME));
        contactEntity.setEmail(JsonUtility.getString(object, KeyConstants.KEY_EMAIL));
        contactEntity.setPhotoUri(JsonUtility.getString(object, KeyConstants.KEY_PHOTO_URI));
        contactEntity.setUserId(JsonUtility.getString(object, KeyConstants.KEY_USER_ID));
        contactEntity.setObjectId(JsonUtility.getString(object, KeyConstants.KEY_ID));
        contactEntity.setRequestStatus(JsonUtility.getInt(object, KeyConstants.KEY_REQUEST_STATUS));
        contactEntity.setLookupId(JsonUtility.getString(object, KeyConstants.KEY_LOOKUP_ID));
        contactEntity.setAndroidId(JsonUtility.getString(object, KeyConstants.KEY_ANDROID_ID));
        contactEntity.setHomeNumber(JsonUtility.getString(object, KeyConstants.KEY_HOME_NUMBER));
        contactEntity.setMobileNumber(JsonUtility.getString(object, KeyConstants.KEY_PHONE_NUMBER));

        return contactEntity;
    }
    public ContactEntity setFCMContact(String jsonString) {

        JsonObject object = JsonUtility.parseToJsonObject(jsonString);
        ContactEntity contactEntity = new ContactEntity();
        contactEntity.setObjectId(JsonUtility.getString(object, KeyConstants.KEY_ID));
        contactEntity.setAndroidId(JsonUtility.getString(object, KeyConstants.KEY_ANDROID_ID));
        contactEntity.setSlideId(JsonUtility.getString(object, KeyConstants.KEY_SLIDE_ID));
        contactEntity.setRequestStatus(JsonUtility.getInt(object, KeyConstants.KEY_REQUEST_STATUS));
        return contactEntity;
    }

    public void setSlideContacts(String jsonString) {
        list.clear();
        JsonArray slideObject = JsonUtility.parseToJsonArray(jsonString);

        for (JsonElement entity: slideObject){
            JsonObject object = entity.getAsJsonObject();
            ContactEntity contactEntity = new ContactEntity();
            contactEntity.setName(JsonUtility.getString(object, KeyConstants.KEY_NAME));
            contactEntity.setFirstName(JsonUtility.getString(object, KeyConstants.KEY_FIRST_NAME));
            contactEntity.setLastName(JsonUtility.getString(object, KeyConstants.KEY_LAST_NAME));
            contactEntity.setEmail(JsonUtility.getString(object, KeyConstants.KEY_EMAIL));
            contactEntity.setPhotoUri(JsonUtility.getString(object, KeyConstants.KEY_PHOTO_URI));
            contactEntity.setUserId(JsonUtility.getString(object, KeyConstants.KEY_USER_ID));
            contactEntity.setObjectId(JsonUtility.getString(object, KeyConstants.KEY_ID));
            contactEntity.setRequestStatus(JsonUtility.getInt(object, KeyConstants.KEY_REQUEST_STATUS));
            contactEntity.setLookupId(JsonUtility.getString(object, KeyConstants.KEY_LOOKUP_ID));
            contactEntity.setAndroidId(JsonUtility.getString(object, KeyConstants.KEY_ANDROID_ID));
            contactEntity.setHomeNumber(JsonUtility.getString(object, KeyConstants.KEY_HOME_NUMBER));
            contactEntity.setMobileNumber(JsonUtility.getString(object, KeyConstants.KEY_PHONE_NUMBER));
            list.add(contactEntity);
        }
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setRequestStatus(int requestStatus) {
        this.requestStatus = requestStatus;
    }

    public int getRequestStatus() {
        return requestStatus;
    }

    public String getLookupId() {
        return lookupId;
    }

    public void setLookupId(String lookupId) {
        this.lookupId = lookupId;
    }

    public String getAndroidId() {
        return androidId;
    }

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getSlideId() {
        return slideId;
    }

    public void setSlideId(String slideId) {
        this.slideId = slideId;
    }
    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String profilePicture) {
        this.photoUri = profilePicture;
    }

    public void setMobileNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }
    public String getMobileNumber() {
        return mPhoneNumber;
    }
    public String getHomeNumber(){
        return mHomeNumber;
    }

    public void setHomeNumber(String homeNumber){
        mHomeNumber = homeNumber;
    }

    public void addAllContacts(ArrayList<ContactEntity> entities){
        list = entities;
    }

    public ArrayList<ContactEntity> getAllContacts(){
        return list;
    }

    public String getUploadContactsRequest(){
        JsonObject requestJson = new JsonObject();

        JsonArray jsonArray = new JsonArray();
        for (ContactEntity entity: getAllContacts()){
            JsonObject object = new JsonObject();
            object.addProperty(KeyConstants.KEY_NAME, entity.getName());
            object.addProperty(KeyConstants.KEY_FIRST_NAME, entity.getFirstName());
            object.addProperty(KeyConstants.KEY_LAST_NAME, entity.getLastName());
            object.addProperty(KeyConstants.KEY_EMAIL, entity.getEmail());
            object.addProperty(KeyConstants.KEY_PHOTO_URI, entity.getPhotoUri());
            object.addProperty(KeyConstants.KEY_USER_ID, entity.getUserId());
            object.addProperty(KeyConstants.KEY_LOOKUP_ID, entity.getLookupId());
            object.addProperty(KeyConstants.KEY_ANDROID_ID, entity.getAndroidId());
            object.addProperty(KeyConstants.KEY_HOME_NUMBER, entity.getHomeNumber());
            object.addProperty(KeyConstants.KEY_PHONE_NUMBER, entity.getMobileNumber());
            jsonArray.add(object);
        }
        requestJson.add(KeyConstants.KEY_CONTACT, jsonArray);

        return requestJson.toString();
    }

    public String getContactRequest( ContactEntity entity){
        JsonObject requestJson = new JsonObject();

        requestJson.addProperty(KeyConstants.KEY_NAME, entity.getName());
        requestJson.addProperty(KeyConstants.KEY_FIRST_NAME, entity.getFirstName());
        requestJson.addProperty(KeyConstants.KEY_LAST_NAME, entity.getLastName());
        requestJson.addProperty(KeyConstants.KEY_EMAIL, entity.getEmail());
        requestJson.addProperty(KeyConstants.KEY_PHOTO_URI, entity.getPhotoUri());
        requestJson.addProperty(KeyConstants.KEY_USER_ID, entity.getUserId());
        requestJson.addProperty(KeyConstants.KEY_LOOKUP_ID, entity.getLookupId());
        requestJson.addProperty(KeyConstants.KEY_ANDROID_ID, entity.getAndroidId());
        requestJson.addProperty(KeyConstants.KEY_HOME_NUMBER, entity.getHomeNumber());
        requestJson.addProperty(KeyConstants.KEY_PHONE_NUMBER, entity.getMobileNumber());

        return requestJson.toString();
    }
    /**
     * For remove ContactEntity object out of Collection type like ArrayList, HashSet, Vector,....
     */
    public boolean equals(Object o) {
        if (!(o instanceof ContactEntity)) {
            return false;
        }

        ContactEntity other = (ContactEntity) o;
        return (new Gson()).toJson(this).equals((new Gson()).toJson(other));
    }
}
