package unittests.util;

/**
 * @author android
 */


public class Constants {

    /**
     * Set to true to Enable Debugging in the API false to disable. This should be false when
     * releasing the app.
     */
    public static final boolean DEBUG = true;
    /**
     * API Endpoint
     */
    public static final String PARSE_SERVER_URL = "https://parseapi.back4app.com";
    /**
     * Back4App_KEY
     */
    public static final String Back4App_ID = "RY6fGLNlf02fP2q6n71PycOTFrDSSLIlWHKSp0up"; // Test DB
    /**
     * Back4Client_KEY
     */
    public static final String Back4App_REST_API = "Z14DSInisi72vOyBBnTEcTDBl4wTkAnxVUoaHcuS"; // Test DB
    /**
     * Back4Client_KEY
     */
    public static final String Back4App_MASTER_API = "zGk1lWDqjlcCH5HQniy24FOpogwq61fmTyaDt8ma"; // Test DB
    /**
     * Connection timeout duration
     */
    public static final int CONNECT_TIMEOUT = 60 * 1000;
    /**
     * Connection Read timeout duration
     */
    public static final int READ_TIMEOUT = 60 * 1000;
    /**
     * Connection write timeout duration
     */
    public static final int WRITE_TIMEOUT = 60 * 1000;
    /**
     * Test Database _User Url
     */
    public static final String TEST_USER_URL = "/classes/_User";
    /**
     * Test Database Contact Url
     */
    public static final String TEST_CONTACT_URL = "/classes/contact";
    /**
     * Test Database App Url
     */
    public static final String TEST_APP_URL = "/classes/app";
    /**
     * Test Database App Url
     */
    public static final String TEST_USER_SETTING_URL = "/classes/usersetting";
    /**
     * Test Database App Url
     */
    public static final String TEST_DIRECTION_URL = "/classes/direction";
    /**
     * Test Database App Url
     */
    public static final String TEST_REMINDER_URL = "/classes/reminder";
    /**
     * Test Database App Url
     */
    public static final String TEST_INVITATION_URL = "/classes/invitation";
    /**
     * Test Database App Url
     */
    public static final String TEST_INSTALLATION_URL = "/installations";
    /**
     * Test Database App Url
     */
    public static final String TEST_PUSH_URL = "/push";


    /**
     * Test Database Rows Limit
     */
    public static final String TEST_ROWS_LIMIT = "1000";
    /**
     * These Object Ids will changes on each Test-run
     */
    public  static String App_Id = "";
    public  static String Contact_Id = "";
    public  static String Setting_Id = "";
    public  static String Direction_Id = "";
    public  static String Reminder_Id = "";
    public  static String Invitation_Id = "";
    public  static String Installation_Id = "";

    public  static String app_package = "com.unit.test";
    public  static String label = "Unit Test";
    public  static String location = "location";
    public  static String latitude = "32.181322500000014";
    public  static String longitude = "34.884886718749996";
    public  static String time = "05:48 p.m.";
    public  static String date = "05:48 p.m.";
    public  static String wiser_id = "wiser_test_id";
    public  static String companion_id = "companion_test_id";
    public static int APPROVED= 0;
    public static int REJECTED= 2;
    public static int INVITED= 4;

    public  static String APP_OPERATION = "Add";
    public  static String CONTACT_OPERATION = "Add";
    public  static String SETTING_OPERATION = "Add";
    public  static String DIRECTION_OPERATION = "Add";
    public  static String REMINDER_OPERATION = "Add";
    public  static String INVITATION_OPERATION = "Add";

    public static String email = "awais.ali@algorepublic.com";
    public  static String objectId = "D441pzN8rv";

    public static int TOTAL_COUNT_IN_DB;
    public static String File_NAME;

    public static String PARSE_CHANNEL = "Companion";
    public static String DEVICE_NAME = "android";
    public static String DEVICE_TOKEN = "APA91bFRDFBKmOTr1JhkKWpGKt01ve2zGLRyC4gjloPIVJMcypE4GVdcyFGvUwiu9VyxxNW-E_Jm2pybcG5ziWizpdnymCBOFJD7o7IZz1ratSLONz324sc";
    public static String GCM_SENDER_ID = "862504332103";
}
