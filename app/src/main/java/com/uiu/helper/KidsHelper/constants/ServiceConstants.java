package com.uiu.helper.KidsHelper.constants;

public class ServiceConstants {
    public static final int SERVICE_TIME_OUT = 1000 * 30;
    /**
     * Base Url
     */
    public static final String SERVICE_BASE_URL = "https://kidslauncherapi.herokuapp.com/api/";

    /**
     * User Registration Apis
     */
    public static final String SERVICE_REGISTER = "users/register";

    /**
     * Contacts Apis
     */
    public static final String SERVICE_UPLOAD_CONTACTS = "contacts";
    public static final String SERVICE_ADD_CONTACT_TO_SLIDE = "contacts/add_to_slide";
    public static final String SERVICE_GET_CONTACT = "contacts/get_contact";
    public static final String SERVICE_ADD_CONTACT = "contacts/add_contact";
    public static final String SERVICE_SLIDE_CONTACTS = "contacts/slide_contacts";
    public static final String SERVICE_UPDATE_OBJECT_STATUS = "slides/update_request_status";

    /**
     * Slide Apis
     */
    public static final String SERVICE_ADD_SLIDE = "slides/slides";
    public static final String SERVICE_CREATE_SLIDE = "slides/create_slides";
    public static final String SERVICE_ALL_SLIDES = "slides/user_slides";

    /**
     * Applications Apis
     */
    public static final String SERVICE_UPDATE_APP_STATUS="applications/update_request_status";

    public static final String SERVICE_KID_ACCEPT_STATUS="users/add_primary_helper_response";

}
