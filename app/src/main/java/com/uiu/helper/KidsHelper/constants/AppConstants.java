package com.uiu.helper.KidsHelper.constants;

/**
 * Created by Awais on 4/04/18.
 */
public class AppConstants {

    public final static int SLIDE_TYPE_HOME = 0;
    public final static int SLIDE_TYPE_PEOPLE = 1;
    public final static int SLIDE_TYPE_APPLICATION = 2;
    public final static int SLIDE_TYPE_SETTINGS = 5;
    public final static int SLIDE_TYPE_NOTICE_BOARD = -2;
    public final static int SLIDE_TYPE_REGISTER = -1;
    public final static int SLIDE_TYPE_DIRECTIONS = 8;
    public final static int SLIDE_TYPE_SOS = 9;
    public final static int SLIDE_TYPE_REMINDER = 11;
    public final static int SLIDE_TYPE_COMPANION = -3;
    public final static int SLIDE_TYPE_CLOCK = -4;

    public final static String UPDATED_SLIDE_ID = "updatedSlideId";
    public final static String PUSH_CHANNEL = "wiser_channel";
    public final static String SUCCESS = "success";
    public final static String ERROR = "error";
    public final static String CONTACT_EXISTS = "contactsExists";
    public final static int GO_TO_WISER_VIEW = 10;
    public final static int PICK_CONTACT_REQUEST = 100;

    public static final int WISER_MIRROR_REQUEST = 100;
    public final static String IS_FROM_DEVICE = "isFromDevice";
    public static final int PLACE_PICKER_REQUEST = 1234;
    public final static int SINGLE_CONTACT_REQUEST = 99;
    public static int OVERLAY_PERMISSION_REQ_CODE = 1111;

    public static final String CONTACT_EMAIL = "contact@uiumobile.com";
    public static final String SEEN_HELP_BEFORE = "seenHelpBefore";
    public static final String INVITE_EMAIL = "invite_email";
    public static final String INVITE_PASSWORD = "invite_password";
    public static final String SHOW_BATTERY_ALERT = "battery_level";

    public static class ParseConstants{

        final public static String IS_SUBSCRIBED = "is_subscribed";
        public final static String USER_TYPE = "userType";
        public final static int TYPE_KIDS = 2;
        public final static int TYPE_COMPANION = 1;
        final public static String WISERS = "wisers";
        final public static String CONTACT = "contact";
        final public static String CONTACTS = "contacts";
        final public static String APP = "app";
        final public static String SLIDE = "slide";
        final public static String OBJECT_ID = "objectId";
        final public static String MY_WISER_ID = "myWiserId";
        final public static String IS_LOCAL_DATA = "isFirstRun";
        final public static String IS_FIRST_TIME = "isFirsTime";
        final public static String FIRST_EVER_LOGIN = "firstEverLogin";
        final public static String CHOSEN_EMAIL = "chosenEmail";
        final public static String INVITATION = "invitation";
        final public static String SETTING = "setting";
        final public static String REMINDER = "reminder";
        final public static String LOCAL_CHANGES_MADE = "localChangesMade";
        final public static String CONTACT_CHANGES_MADE = "contactChangesMade";
        final public static String IMAGE = "image";

        final public static String IS_PEOPLE_ALERT = "people_alert";
        final public static String IS_APPLICATION_ALERT = "app_alert";
        final public static String IS_SETTING_ALERT = "setting_alert";

        final public static String FULL_MESSAGE = "full_message";
        final public static String PKG_NAME = "pkg_name";

        final public static String SUB_PRICE = "sub_price";
        final public static String INVITED_EMAIL = "invited_email";
    }

    public static class PUSH{
        final public static String SEND_APP_NO_SLIDE = "sendAppNoSlide";
        final public static String WISER_REPLY = "com.wiser.home.WISER_REPLY";
        final public static String WISER_DECLINE = "com.wiser.home.WISER_DECLINE";
        final public static String NOTIFICATION_TYPE = "notificationType";
        final public static String SLIDE_UPDTAED = "com.wiser.home.SLIDE_UPDATED";
        final public static String RECEIVED_INVITE = "com.wiser.home.RECEIVED_INVITE";
        final public static String APP_INSTALL_REQUEST = "com.wiser.home.APP_INSTALL_REQUEST";
        final public static String CONTACT_ADDED = "com.wiser.home.CONTACT_ADDED";
        final public static String APP_ADDED = "com.wiser.home.APP_ADDED";
        final public static String DIRECTION_ADDED= "com.wiser.home.DIRECTION_ADDED";
        final public static String ITEM_UPDATED= "com.wiser.home.ITEM_UPDATED";

        final public static String COMPANION_REPLY = "com.wiser.home.COMPANION_REPLY";
        final public static String UPDATE_SETTING= "com.wiser.home.UPDATE_SETTING";
        final public static String GET_SETTING= "com.wiser.home.GET_SETTING";
        final public static String UNIQUE_ID = "userUniqueIdentifier";
        final public static String UPDATE_REQUEST_TYPE = "updateRequestType";
        final public static String ALERT = "alert";
        final public static String PACKAGE_NAME = "packageName";
        final public static String SCREEN_NAME = "screenName";
        final public static String SLIDE_CHANGED_POSITION = "slideChangedPosition";
        final public static String ADDED_APPS_LIST = "addedAppsList";
        final public static String SENT_BY = "sentBy";
        final public static int REQUEST_TYPE_SETTINGS = 0;
        final public static int REQUEST_TYPE_ITEM = 1;
        final public static int REQUEST_TYPE_SLIDE = 2;
        final public static int REQUEST_TYPE_APP_INSTALL= 6;
        final public static int REQUEST_TYPE_INVITAION_SENT = 3;
        final public static int REQUEST_TYPE_INVITAION_APPROVED = 4;
        final public static int REQUEST_TYPE_INVITAION_REJECTED = 5;
        final public static int REQUEST_GET_TYPE_SETTINGS = 7;


        final public static String REQUEST_TYPE_SLIDE_ITEM = "com.uiu.helper.action_slide_item";


    }

    public static class User{

        final public static String USER_TYPE = "userType";
        final public static int TYPE_COMPANION = 0;
        final public static int TYPE_WISER = 1;
        final public static String OBJECT_ID = "objectId";
        final public static String IMAGE = "image";
        final public static String PHONE_NUMBER = "phoneNumber";
        final public static String PLAYSTORE_TOKEN = "playstore_token";
        final public static String USER_OBJ = "logged_in_user";

    }


    public static class Contact {

        final public static String EMAIL = "email";
        final public static String LOOKUP_ID = "lookupId";
        final public static String PHONE_JSON = "phoneJson";
        final public static String PHONE_ARRAY = "phoneArray";
        final public static String NAME = "name";
        final public static String OBJECT_ID = "objectId";
        final public static String WISER_ID = "wiserId";
        final public static String SLIDE = "slide";
        final public static String IMAGE_FILE = "imageFile";
        final public static String LAST_EDITED = "setLastEdited";
        final public static String PHOTO_URI = "photoUri";
        final public static String ANDROID_ID = "androidId";


    }

    public static class Invitation {

        final public static String STATUS = "status";
        final public static String OBJECT_ID = "objectId";
        final public static String EMAIL = "email";
        final public static String COMPANION_ID = "companion_id";
        final public static String WISER_ID = "wiser_id";
        final public static String WISER_NAME = "wiser_name";
        final public static String COMPANION_NAME = "companion_name";
        final public static int APPROVED= 0;
        final public static int SENT_FROM_COMPANION = 1;
        final public static int SENT_FROM_WISER = 3;
        final public static int CONNECTED= 0;
        final public static int REJECTED= 2;
        final public static int INVITED= 4;
    }


    public static class WISERS{

        final public static String EMAIL = "primery_mail";
        final public static String CONTACT_ID = "contact_id";
        final public static String CONTACTS = "contacts";
        final public static String APPS = "apps";
        final public static String OBJECT_ID = "objectId";
    }

    public static class Apps{

        final public static String SLIDE_ID = "slide_id";
        final public static String SLIDE_TYPE = "slide_type";
        final public static String PACKAGE= "package_name";
        final public static String LABEL = "label";
        final public static String OBJECT_ID = "objectId";
        final public static String WISER_ID = "wiser_id";
        final public static String APP_ICON = "appIcon";
        final public static String APP_CLASS = "class";
    }


    public static class SLIDE{

        final public static String NAME = "slide_name";
        final public static String TYPE= "slide_type";
        final public static String ORDER= "slide_order";

        final public static String ID= "local_id";
        final public static String WISER_ID = "wiser_id";
        final public static String CONTACTS = "contacts";
        final public static String APPS = "apps";
        final public static String DIRECTIONS = "directions";
        final public static String SOS = "sos";
        final public static String SETTINGS = "settings";
        final public static String USERSETTINGS = "user_settings";
        final public static String REMINDERS = "reminders";
        final public static String SLIDE_ID = "slide_id";
        final public static String SERIAL = "serial";
        final public static String LOCAL_ID = "localSlideId";
        final public static String CREATED_AT = "createdAt";
        final public static String UPDATED_AT = "updatedAt";

    }

    public static class DIRECTIONS{


        final public static String LAT= "latitude";
        final public static String LON = "longitude";
        final public static String NAME = "name";
        final public static String OBJECT_ID = "objectId";
        final public static String ID = "Id";
        final public static String ADDRESS = "address";
        final public static String SLIDE_ID= "slideId";
    }
    public static class REMINDER{

        final public static String OBJECT_ID = "objectId";
        final public static String ID = "Id";
        final public static String TITLE = "title";
        final public static String DATE = "date";
        final public static String TIME = "time";
        final public static String REPEAT= "repeat";
        final public static String NOTE= "note";
        final public static String SLIDE_ID= "slideId";
    }

    public static class USERSETTING{

        final public static String OBJECT_ID = "objectId";
        final public static String SETTINGTYPE = "setting_type";
    }
    public static class SETTING{

        final public static String BATTERY_PERCENT = "batteryPercent";
        final public static String SOUND= "sound";
        final public static String WIFI= "wifi";
        final public static String GPS = "gps";
        final public static String BLUETOOTH = "bluetooth";
        final public static String UPDATED_AT = "updatedAt";
        final public static String BRIGHTNESS = "brightness";
        final public static String AUTO_BRIGHTNESS = "auto_brightness";
        final public static String FONT_SIZE = "fontSize";
        final public static String DEFAULT_FONT_SIZE = "defaultFontSize";
        final public static String VOLUME = "volume";
        final public static String OBJECT_ID = "objectId";
        final public static String WISER_ID = "wiser_id";
        final public static String CHARGING = "charging";
        final public static String DEFAULT_SAVED = "default_saved";
    }

    public static class SLIDE_NUM{

        final public static String FIRST_SLIDE = "0";
        final public static String SECONED_SLIDE = "1";
        final public static String HOME_SLIDE = "4";
    }

    public static class WISER_APPS {
        final public static String WISER_PREFIX = "com.wiser.";

        final public static String GENERAL_CONTACT = "0";
        final public static String GENERAL_DIALER = "1";
        final public static String GENERAL_SOS = "2";
        final public static String GENERAL_CAMERA = "3";
        final public static String GENERAL_GALLERY = "4";
        final public static String GENERAL_APPS = "5";

    }

    public static class MAIN_CONTROLLER {
        public static final long FULL_DAY = 1000 * 60 * 60 * 24;
    }
    public static final int NOTIFICATION_INTERVAL = 5000;
    /**
     * Intent Extra Constant
     */
    public static class EXTRA {
        public static String EXTRA_SHOW_CALLLOG;
        public static final String EXTRA_LOOKUP_ID = "lookup_key";
        public static final String EXTRA_ANDROID_ID = "android_id";
        public static final String HOME_SCREEN_ICON = "homeScreenIcon";
        public static final String EXTRA_PICK_MODE = "pick_mode";
        public static final String EXTRA_DETAIL_STATE = "detail_state";
        public static final String EXTRA_NEW_CONTACT = "new_contact";
        public static final String EXTRA_USER_ID = "user_id";
        public static final String EXTRA_ID = "id";

        public static final String EXTRA_OBJECT_ID = "object_id";
        public static final String EXTRA_PERMISSION = "request_status";
        public static final String EXTRA_HELPER_ID = "helper_id";
        public static final String EXTRA_KID_ID = "kid_id";
        public static final String EXTRA_NOTIFICATION_ID = "notification_id";
        public static final String EXTRA_REQUEST_STATUS= "accepted";
    }

    /**
     * Intent Intent Constant
     */
    public static class INTENT {
        public static final int PEOPLE_PICK_MODE = 0;

        public static final String PUSH_TITLE = "push_title";
        public static final String PUSH_MESSAGE = "push_message";
        public static final String PUSH_OBJECT = "push_object";
    }
    /**
     * Permission Constant
     */
    public static class PERMISSION {
        public static final int Requested = 1;
        public static final int Rejected = 2;
        public static final int Approved = 3;
    }
    /**
     * Notification Constant
     */
    public static class NOTIFICATION {
        public static final String APPROVE = "com.uiu.helper.approve";
        public static final String REJECT = "com.uiu.helper.reject";
    }
}
