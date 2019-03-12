package com.uiu.helper.KidsHelper.mvp;

import com.uiu.helper.R;

/**
 * Created by sidhu on 4/18/2018.
 */

public interface Constant {


    /** App Modes*/
    String DEVELOPMENT="Development";
    String PRODUCTION="Production";
    String DEBUG="Debugging";

    int KIDS_PRIMARY_HELPER=1;
    int KIDS_SECONDARY_HELPER=2;
    int KIDS_HELPER=2;
    int KIDS=3;

    //Status Codes
    int SUCCESS = 200;
    int BAD_REQUEST = 400;

    // Slide

    String CONTACTS="Contacts";
    String DIALER="Dialer";
    String MESSAGING="Messaging";
    String CAMERA = "Camera";
    String C_ME="C Me";
    String APPLICATIONS= "Applications";
    String CALL_FROM_INFO="CallFromContactInfo";


    String SELECTED_CONTACT="selected_contact";

    int TAB_MODE_MOBILE = 0;
    int TAB_MODE_HOME = 1;
    int TAB_MODE_EMAIL = 2;

    String KEY_SELECTED_CONTACT="add_selected_contact";
    String KEY_SELECTED_KID="add_selected_kid";
    String KEY_SELECTED_APP="add_selected_apps";
    String KEY_SELECTED_APP_NAME="add_selected_apps";
    String KEY_SELECTED_APP_ICON="add_selected_apps";
    String KEY_SELECTED_APP_PKGNAME="add_selected_apps";

    String FAV_APP_SLIDE_NAME="Favorite Applications";
    String FAV_PEOPLE_SLIDE_NAME="Favorite Peoples";
    String FAV_LINK_SLIDE_NAME="Favorite Links";
    String DIRECTION_SLIDE_NAME="Directions";
    String PREF_NOTIFICATION_TOKEN="notification_token";
    String NO_SPACE_ON_SLIDE="No space available on this slide, add on other slide";

    String EXTRA_NOTIFICATION_TYPE = "notification_type";
    String RECORDED_FILE_PATH ="recorded_file_path" ;
    String RECORDED_FILE_TYPE ="recorded_file_type" ;

    int REQ_REQUESTED=1;
    int REJECTED=2;
    int ACCEPTED=3;

    int SLIDE_INDEX_NOTIFICATIONS=-1;
    int SLIDE_INDEX_HOME=1;
    int SLIDE_INDEX_FAV_PEOPLE=2;
    int SLIDE_INDEX_FAV_APP=3;
    int SLIDE_INDEX_FAV_GAMES=4;
    int SLIDE_INDEX_FAV_LINKS=5;
    int SLIDE_INDEX_SOS=6;
    int SLIDE_INDEX_REMINDERS=7;
    int SLIDE_INDEX_SAFE_PLACES =8;
    int SLIDE_CREATE_INDEX=9;
    int SLIDE_REMOVE_INDEX=10;
    int INVITE_CODE = 11;
    int PRIMARY_PARENT_REMOVE=12;
    int SLIDE_INDEX_MAP=13;
    int SLIDE_INDEX_SLEEP_SETTING=14;
    int REQ_BEEP=13;
    int REQ_BATTERY_ALERT=15;
    int REQ_SETTINGS=16;
    int REQ_LOCATION=17;
    int REQ_KID_OUT_OF_FENCE=18;

    int MEDIA_IMAGE=1;
    int MEDIA_VIDEO=2;
    int MEDIA_AUDIO=3;
    int MEDIA_FILE=4;

    interface INVITE {

        int ACCEPTED = 0;
        int CONNECTED= 1;
        int REJECTED= 2;
        int INVITED= 3;
    }
    String CAMERA_IMG_PATH ="camera_img_path" ;
    String key_camera_type="CAMERA_TYPE";
    String key_camera_for_result="CAMERA_FOR_RESULT";

    interface IntentExtras {
        String ACTION_CAMERA = "action-camera";
        String ACTION_GALLERY = "action-gallery";
        String IMAGE_PATH = "image-path";
        String IMAGE_PATH_ID = "image-path-identity";
    }
}
