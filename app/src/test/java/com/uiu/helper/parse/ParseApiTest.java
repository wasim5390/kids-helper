package com.uiu.helper.parse;

import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;
import unittests.model.ParseUserModel;
import unittests.model.ResultModel;
import unittests.util.Constants;


/**
 * Created by Android on 2/14/2018.
 */

public class ParseApiTest extends BaseTestCase {

    // Applications getUserApps/UploadNewApp/DeleteApp //Section//Start point
    @Test
    public void testgetAllApplicationsCount() throws IOException {
        String url = Constants.PARSE_SERVER_URL+Constants.TEST_APP_URL+
                "?limit="+Constants.TEST_ROWS_LIMIT+"&where={\"wiser_id\":\""+Constants.objectId+"\"}";
        Call<ResultModel> application = getParseApiClient().applicationInterface().
                getAllApplications(url);
        Response<ResultModel> response = application.execute();

        assertEquals(response.isSuccessful(),true);
        if (response.isSuccessful()){
            switch (Constants.APP_OPERATION){
                case "Add":
                      /* To Set the Total Count Of Records In DB*/
                    Constants.TOTAL_COUNT_IN_DB = getRowEntries(response);
                    System.out.print("Total Apps Counts: "+getRowEntries(response)+"\n");
                    uploadAppIconFileInDB();
                    break;
                case "Del":
                     /* To Check Count After Adding One Row In The DB*/
                    assertEquals(getRowEntries(response),Constants.TOTAL_COUNT_IN_DB + 1);
                    System.out.print("Total Apps Counts After Add Operation: "+getRowEntries(response)+"\n");
                    TestDeleteOneApplicationRowFromDB();
                    break;
                case "Operation":
                      /* To Check Count After deleting One Row In The DB*/
                    assertEquals(getRowEntries(response),Constants.TOTAL_COUNT_IN_DB);
                    System.out.print("Total Apps Counts After Delete Operation: "+getRowEntries(response)+"\n");
                    TestDeleteOneFileRowFromDB();
                    break;
            }
        }
    }
    private void TestAddOneApplicationRowInDB(HashMap<String, Object> object) throws IOException {
        Call<ResultModel> application = getParseApiClient().applicationInterface().
                addApplication(object);
        Response<ResultModel> response = application.execute();

        assertEquals(response.code(),201);
        assertEquals(response.isSuccessful(),true);
        if (response.isSuccessful()){
            String spilt = response.headers().get(Constants.location);
            String[] divided = spilt.split("/");
            Constants.App_Id = divided[divided.length-1];
            Constants.APP_OPERATION = "Del";
            testgetAllApplicationsCount();
        }
    }
    private void TestDeleteOneApplicationRowFromDB() throws IOException {
        Call<ResultModel> application = getParseApiClient().applicationInterface().
                deleteApplication(Constants.App_Id);
        Response<ResultModel> response = application.execute();

        assertEquals(response.code(),200);
        assertEquals(response.isSuccessful(),true);
        if (response.isSuccessful()){
            Constants.APP_OPERATION = "Operation";
            testgetAllApplicationsCount();
        }
    }
    private void TestDeleteOneFileRowFromDB() throws IOException {
        Call<Void> application = getParseApiClient().applicationInterface().
                deleteFile(Constants.File_NAME);
        Response<Void> response = application.execute();

        assertEquals(response.code(),200);
        assertEquals(response.isSuccessful(),true);
    }

    public void uploadAppIconFileInDB() throws IOException {

        Class myClass = getClass();
        File file = new File(myClass.getClassLoader()
                .getResource("ic_plus.png").getPath());

        RequestBody fbody = RequestBody.create(MediaType.parse("*/*"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("appIcon", file.getName(), fbody);
        RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        Call<ResultModel> allApplications = getParseApiClient().applicationInterface().
                uploadFile(body,descBody);
        Response<ResultModel> response = allApplications.execute();

        assertEquals(response.code(),201);
        assertEquals(response.isSuccessful(),true);
        if (response.isSuccessful()){
            String spilt = response.headers().get(Constants.location);
            String[] divided = spilt.split("/");
            Constants.File_NAME = divided[divided.length-1];
            TestAddOneApplicationRowInDB(getAppJson(response));
        }
    }
    private HashMap<String, Object> getAppJson( Response<ResultModel> response){
        HashMap<String, Object> parentObg = new HashMap<>();
        parentObg.put("wiser_id",Constants.objectId);
        parentObg.put("package_name",Constants.app_package);
        parentObg.put("label",Constants.label);

        HashMap<String, String> childObg = new HashMap<>();
        childObg.put("name","appIcon.png");
        childObg.put("url",response.headers().get(Constants.location));
        childObg.put("__type","File");

        parentObg.put("appIcon",childObg);
        return parentObg;
    }
    // Applications getUserApps/UploadNewApp/DeleteApp //Section//End point

    // Contacts getContacts/UploadContact/DeleteContact //Section//Start point
    @Test
    public void testgetAllContactsCount() throws IOException {
        String url = Constants.PARSE_SERVER_URL+Constants.TEST_CONTACT_URL+
                "?limit="+Constants.TEST_ROWS_LIMIT+"&where={\"wiserId\":\""+Constants.objectId+"\"}";
        Call<ResultModel> contacts = getParseApiClient().contactInterface().
                getAllContacts(url);
        Response<ResultModel> response = contacts.execute();

        assertEquals(response.isSuccessful(),true);
        if (response.isSuccessful()){
            switch (Constants.CONTACT_OPERATION){
                case "Add":
                      /* To Set the Total Count Of Records In DB*/
                    Constants.TOTAL_COUNT_IN_DB = getRowEntries(response);
                    System.out.print("Total Contacts Counts: "+getRowEntries(response)+"\n");
                    TestAddOneContactRowInDB();
                    break;
                case "Del":
                     /* To Check Count After Adding One Row In The DB*/
                    assertEquals(getRowEntries(response),Constants.TOTAL_COUNT_IN_DB + 1);
                    System.out.print("Total Contacts Counts After Add Operation: "+getRowEntries(response)+"\n");
                    TestDeleteOneContactRowFromDB();
                    break;
                case "Operation":
                      /* To Check Count After deleting One Row In The DB*/
                    assertEquals(getRowEntries(response),Constants.TOTAL_COUNT_IN_DB);
                    System.out.print("Total Contacts Counts After Delete Operation: "+getRowEntries(response)+"\n");
                    break;
            }
        }
    }
    private void TestAddOneContactRowInDB() throws IOException {
        Call<ResultModel> contacts = getParseApiClient().contactInterface().
                addContact(Constants.objectId,Constants.label);
        Response<ResultModel> response = contacts.execute();

        assertEquals(response.code(),201);
        assertEquals(response.isSuccessful(),true);
        if (response.isSuccessful()){
            String spilt = response.headers().get(Constants.location);
            String[] divided = spilt.split("/");
            Constants.Contact_Id = divided[divided.length-1];
            Constants.CONTACT_OPERATION = "Del";
            testgetAllContactsCount();
        }
    }
    private void TestDeleteOneContactRowFromDB() throws IOException {
        Call<ResultModel> contacts = getParseApiClient().contactInterface().
                deleteContact( Constants.Contact_Id);
        Response<ResultModel> response = contacts.execute();

        assertEquals(response.code(),200);
        assertEquals(response.isSuccessful(),true);
        if (response.isSuccessful()){
            Constants.CONTACT_OPERATION = "Operation";
            testgetAllContactsCount();
        }
    }
    // Contacts getContacts/UploadContact/DeleteContact //Section//End point

    // Settings getSettings/AddSettings/DeleteSettings //Section//Start point
    @Test
    public void testgetAllSettingsCount() throws IOException {
        String url = Constants.PARSE_SERVER_URL+Constants.TEST_USER_SETTING_URL+
                "?limit="+Constants.TEST_ROWS_LIMIT;
        Call<ResultModel> settings = getParseApiClient().settingInterface().
                getAllSettings(url);
        Response<ResultModel> response = settings.execute();

        assertEquals(response.isSuccessful(),true);
        if (response.isSuccessful()){
            switch (Constants.SETTING_OPERATION){
                case "Add":
                      /* To Set the Total Count Of Records In DB*/
                    Constants.TOTAL_COUNT_IN_DB = getRowEntries(response);
                    System.out.print("Total Settings Counts: "+getRowEntries(response)+"\n");
                    TestAddOneSettingRowInDB();
                    break;
                case "Del":
                     /* To Check Count After Adding One Row In The DB*/
                    assertEquals(getRowEntries(response),Constants.TOTAL_COUNT_IN_DB + 1);
                    System.out.print("Total Settings Counts After Add Operation: "+getRowEntries(response)+"\n");
                    TestDeleteOneSettingRowFromDB();
                    break;
                case "Operation":
                      /* To Check Count After deleting One Row In The DB*/
                    assertEquals(getRowEntries(response),Constants.TOTAL_COUNT_IN_DB);
                    System.out.print("Total Settings Counts After Delete Operation: "+getRowEntries(response)+"\n");
                    break;
            }
        }
    }
    private void TestAddOneSettingRowInDB() throws IOException {
        Call<ResultModel> settings = getParseApiClient().settingInterface().
                addSetting(Constants.label);
        Response<ResultModel> response = settings.execute();

        assertEquals(response.code(),201);
        assertEquals(response.isSuccessful(),true);
        if (response.isSuccessful()){
            String spilt = response.headers().get(Constants.location);
            String[] divided = spilt.split("/");
            Constants.Setting_Id = divided[divided.length-1];
            Constants.SETTING_OPERATION = "Del";
            testgetAllSettingsCount();
        }
    }
    private void TestDeleteOneSettingRowFromDB() throws IOException {
        Call<ResultModel> settings = getParseApiClient().settingInterface().
                deleteSetting(Constants.Setting_Id);
        Response<ResultModel> response = settings.execute();

        assertEquals(response.code(),200);
        assertEquals(response.isSuccessful(),true);
        if (response.isSuccessful()){
            Constants.SETTING_OPERATION = "Operation";
            testgetAllSettingsCount();
        }
    }
    // Settings getSettings/AddSettings/DeleteSettings //Section//End point

    // Direction getDirection/AddDirection/DeleteDirection //Section//Start point
    @Test
    public void testgetAllDirectionsCount() throws IOException {
        String url = Constants.PARSE_SERVER_URL+Constants.TEST_DIRECTION_URL+
                "?limit="+Constants.TEST_ROWS_LIMIT;
        Call<ResultModel> settings = getParseApiClient().directionInterface().
                getAllDirections(url);
        Response<ResultModel> response = settings.execute();

        assertEquals(response.isSuccessful(),true);
        if (response.isSuccessful()){
            switch (Constants.DIRECTION_OPERATION){
                case "Add":
                      /* To Set the Total Count Of Records In DB*/
                    Constants.TOTAL_COUNT_IN_DB = getRowEntries(response);
                    System.out.print("Total Direction Counts: "+getRowEntries(response)+"\n");
                    TestAddOneDirectionRowInDB();
                    break;
                case "Del":
                     /* To Check Count After Adding One Row In The DB*/
                    assertEquals(getRowEntries(response),Constants.TOTAL_COUNT_IN_DB + 1);
                    System.out.print("Total Direction Counts After Add Operation: "+getRowEntries(response)+"\n");
                    TestDeleteOneDirectionRowFromDB();
                    break;
                case "Operation":
                      /* To Check Count After deleting One Row In The DB*/
                    assertEquals(getRowEntries(response),Constants.TOTAL_COUNT_IN_DB);
                    System.out.print("Total Direction Counts After Delete Operation: "+getRowEntries(response)+"\n");
                    break;
            }
        }
    }
    private void TestAddOneDirectionRowInDB() throws IOException {
        Call<ResultModel> settings = getParseApiClient().directionInterface().
                addDirection(Constants.latitude,Constants.longitude,Constants.label,Constants.label);
        Response<ResultModel> response = settings.execute();

        assertEquals(response.code(),201);
        assertEquals(response.isSuccessful(),true);
        if (response.isSuccessful()){
            String spilt = response.headers().get(Constants.location);
            String[] divided = spilt.split("/");
            Constants.Direction_Id = divided[divided.length-1];
            Constants.DIRECTION_OPERATION = "Del";
            testgetAllDirectionsCount();
        }
    }
    private void TestDeleteOneDirectionRowFromDB() throws IOException {
        Call<ResultModel> settings = getParseApiClient().directionInterface().
                deleteDirection(Constants.Direction_Id);
        Response<ResultModel> response = settings.execute();

        assertEquals(response.code(),200);
        assertEquals(response.isSuccessful(),true);
        if (response.isSuccessful()){
            Constants.DIRECTION_OPERATION = "Operation";
            testgetAllDirectionsCount();
        }
    }
    // Direction getDirection/AddDirection/DeleteDirection //Section//End point

    // Reminder getReminder/AddReminder/DeleteReminder //Section//Start point
    @Test
    public void testgetAllRemindersCount() throws IOException {
        String url = Constants.PARSE_SERVER_URL+Constants.TEST_REMINDER_URL+
                "?limit="+Constants.TEST_ROWS_LIMIT;
        Call<ResultModel> reminders = getParseApiClient().reminderInterface().
                getAllReminders(url);
        Response<ResultModel> response = reminders.execute();

        assertEquals(response.isSuccessful(),true);
        if (response.isSuccessful()){
            switch (Constants.REMINDER_OPERATION){
                case "Add":
                    /* To Set the Total Count Of Records In DB*/
                    Constants.TOTAL_COUNT_IN_DB = getRowEntries(response);
                    System.out.print("Total Reminder Counts: "+getRowEntries(response)+"\n");
                    TestAddOneReminderRowInDB();
                    break;
                case "Del":
                     /* To Check Count After Adding One Row In The DB*/
                    assertEquals(getRowEntries(response),Constants.TOTAL_COUNT_IN_DB + 1);
                    System.out.print("Total Reminder Counts After Add Operation: "+getRowEntries(response)+"\n");
                    TestDeleteOneReminderRowFromDB();
                    break;
                case "Operation":
                      /* To Check Count After deleting One Row In The DB*/
                    assertEquals(getRowEntries(response),Constants.TOTAL_COUNT_IN_DB);
                    System.out.print("Total Reminder Counts After Delete Operation: "+getRowEntries(response)+"\n");
                    break;
            }
        }
    }
    private void TestAddOneReminderRowInDB() throws IOException {
        Call<ResultModel> reminders = getParseApiClient().reminderInterface().
                addReminder(Constants.time,Constants.date,Constants.label);
        Response<ResultModel> response = reminders.execute();

        assertEquals(response.code(),201);
        assertEquals(response.isSuccessful(),true);
        if (response.isSuccessful()){
            String spilt = response.headers().get(Constants.location);
            String[] divided = spilt.split("/");
            Constants.Reminder_Id = divided[divided.length-1];
            Constants.REMINDER_OPERATION = "Del";
            testgetAllRemindersCount();
        }
    }
    private void TestDeleteOneReminderRowFromDB() throws IOException {
        Call<ResultModel> reminders = getParseApiClient().reminderInterface().
                deleteReminder(Constants.Reminder_Id);
        Response<ResultModel> response = reminders.execute();

        assertEquals(response.code(),200);
        assertEquals(response.isSuccessful(),true);
        if (response.isSuccessful()){
            Constants.REMINDER_OPERATION = "Operation";
            testgetAllRemindersCount();
        }
    }
    // Reminder getReminder/AddReminder/DeleteReminder //Section//End point

    // Invitation getInvitation/Send Invitation/DeleteInvitation/ApproveInvitation/ //Section//Start point
    @Test
    public void testgetAllInvitationsCount() throws IOException {
        String url = Constants.PARSE_SERVER_URL+Constants.TEST_INVITATION_URL+
                "?limit="+Constants.TEST_ROWS_LIMIT;
        Call<ResultModel> reminders = getParseApiClient().reminderInterface().
                getAllReminders(url);
        Response<ResultModel> response = reminders.execute();

        assertEquals(response.isSuccessful(),true);
        if (response.isSuccessful()){
            switch (Constants.INVITATION_OPERATION){
                case "Add":
                    /* To Set the Total Count Of Records In DB*/
                    Constants.TOTAL_COUNT_IN_DB = getRowEntries(response);
                    System.out.print("Total Invitation Counts: "+getRowEntries(response)+"\n");
                    TestAddOneInvitationRowInDB();
                    break;
                case "Approve":
                     /* To Check Count After Setting Approve Status In The DB*/
                    assertEquals(getRowEntries(response),Constants.TOTAL_COUNT_IN_DB + 1);
                    System.out.print("After Invitation Approve Status Operation: "+getRowEntries(response)+"\n");
                    System.out.print("Total Invitation Counts After Approve Status Operation: "+getRowEntries(response)+"\n");
                    TestChangeStatusToApproveInDB();
                    break;
                case "Reject":
                     /* To Check Count After Setting Reject Status In The DB*/
                    assertEquals(getRowEntries(response),Constants.TOTAL_COUNT_IN_DB + 1);
                    System.out.print("After Invitation Reject Status: "+getRowEntries(response)+"\n");
                    System.out.print("Total Invitation Counts After Reject Status Operation: "+getRowEntries(response)+"\n");
                    TestChangeStatusToRejectInDB();
                    break;
                case "Del":
                     /* To Check Count After Adding One Row In The DB*/
                    assertEquals(getRowEntries(response),Constants.TOTAL_COUNT_IN_DB + 1);
                    System.out.print("Total Invitation Counts After Add Operation: "+getRowEntries(response)+"\n");
                    TestDeleteOneInvitationRowFromDB();
                    break;
                case "Operation":
                      /* To Check Count After deleting One Row In The DB*/
                    assertEquals(getRowEntries(response),Constants.TOTAL_COUNT_IN_DB);
                    System.out.print("Total Invitation Counts After Delete Operation: "+getRowEntries(response)+"\n");
                    break;
            }
        }
    }
    private void TestAddOneInvitationRowInDB() throws IOException {
        Call<ResultModel> reminders = getParseApiClient().invitationInterface().
                addInvitation(Constants.wiser_id,Constants.companion_id,Constants.INVITED);
        Response<ResultModel> response = reminders.execute();

        assertEquals(response.code(),201);
        assertEquals(response.isSuccessful(),true);
        if (response.isSuccessful()){
            String spilt = response.headers().get(Constants.location);
            String[] divided = spilt.split("/");
            Constants.Invitation_Id = divided[divided.length-1];
            Constants.INVITATION_OPERATION = "Approve";
            testgetAllInvitationsCount();
        }
    }
    private void TestDeleteOneInvitationRowFromDB() throws IOException {
        Call<ResultModel> reminders = getParseApiClient().invitationInterface().
                deleteInvitation(Constants.Invitation_Id);
        Response<ResultModel> response = reminders.execute();

        assertEquals(response.code(),200);
        assertEquals(response.isSuccessful(),true);
        if (response.isSuccessful()){
            Constants.INVITATION_OPERATION = "Operation";
            testgetAllInvitationsCount();
        }
    }
    private void TestChangeStatusToApproveInDB() throws IOException {
        Call<ResultModel> reminders = getParseApiClient().invitationInterface().
                changeInvitationStatus(Constants.Invitation_Id,Constants.APPROVED);
        Response<ResultModel> response = reminders.execute();

        assertEquals(response.code(),200);
        assertEquals(response.isSuccessful(),true);
        if (response.isSuccessful()){
            Constants.INVITATION_OPERATION = "Reject";
            testgetAllInvitationsCount();
        }
    }
    private void TestChangeStatusToRejectInDB() throws IOException {
        Call<ResultModel> reminders = getParseApiClient().invitationInterface().
                changeInvitationStatus(Constants.Invitation_Id,Constants.REJECTED);
        Response<ResultModel> response = reminders.execute();

        assertEquals(response.code(),200);
        assertEquals(response.isSuccessful(),true);
        if (response.isSuccessful()){
            Constants.INVITATION_OPERATION = "Del";
            testgetAllInvitationsCount();
        }
    }
    // Invitation getInvitation/Send Invitation/DeleteInvitation/ApproveInvitation //Section//End point

    // ParseUser Login/isExistParseUser/SignUp //Section//Start point
    @Test
    public void testIsExistParseUser() throws IOException {
        String url = Constants.PARSE_SERVER_URL+Constants.TEST_USER_URL+"?where={\"username\":\""+Constants.email+"\"}";
        Call<ParseUserModel> loginParseUser = getParseApiClient().parseUserInterface().isExistParseUser(url);
        Response<ParseUserModel> response = loginParseUser.execute();

        assertEquals(response.code(),200);
        assertEquals(response.isSuccessful(),true);

        // If login user does not exist, call sign up method
        if (!response.isSuccessful() || response.body().getDetails().size() == 0){
            TestSignupParseUser();
        }else{
            TestLogInParseUser();
        }
    }
    private void TestLogInParseUser() throws IOException {
        String url = Constants.PARSE_SERVER_URL+"/login?username="+Constants.email+"&password="+Constants.email;
        Call<ParseUserModel> loginParseUser = getParseApiClient().parseUserInterface().loginParseUser(url);
        Response<ParseUserModel> response = loginParseUser.execute();

        assertEquals(response.code(),200);
        assertEquals(response.isSuccessful(),true);

        // If login user does not exist, call sign up method
        if (!response.isSuccessful()){
            TestSignupParseUser();
        }else{
            Constants.objectId = "D441pzN8rv";
        }
    }

    private void TestSignupParseUser() throws IOException {

        Call<ParseUserModel> signupParseUser = getParseApiClient().parseUserInterface().signupParseUser(Constants.email,Constants.email);
        Response<ParseUserModel> response = signupParseUser.execute();

        assertEquals(response.code(), 201);
        assertEquals(response.isSuccessful(),true );
    }
    // ParseUser Login/isExistParseUser/SignUp //Section//End point

    // PushInstallation RetrieveInstallation/RegisterDevice/SendPush //Section//End point
    @Test
    public void testRegisterDevice() throws IOException {

        Call<ResultModel> registerDevice = getParseApiClient().pushInterface()
                .registerDevice(getRegisterObject());
        Response<ResultModel> response = registerDevice.execute();

        assertEquals(response.code(), 200);
        assertEquals(response.isSuccessful(),true );
    }
    private void TestSendPush() throws IOException {

        Call<ResultModel> sendPush = getParseApiClient().pushInterface()
                .sendPush(getPushObject());
        Response<ResultModel> response = sendPush.execute();

        assertEquals(response.code(),200);
        assertEquals(response.isSuccessful(),true );
        if (response.isSuccessful()){
            TestDeleteInstallation();
        }
    }
    private void TestDeleteInstallation() throws IOException {

        Call<ResultModel> signupParseUser = getParseApiClient().pushInterface()
                .deleteInvitation(Constants.Installation_Id);
        Response<ResultModel> response = signupParseUser.execute();

        assertEquals(response.code(),200);
        assertEquals(response.isSuccessful(),true );
    }
    private HashMap<String, Object> getRegisterObject(){
        HashMap<String, Object> parentObg = new HashMap<>();
        parentObg.put("deviceType",Constants.DEVICE_NAME);
        parentObg.put("deviceToken",Constants.DEVICE_TOKEN);
        parentObg.put("GCMSenderId",Constants.GCM_SENDER_ID);
        // set channel here
        List<String> channel = new ArrayList<>();
        channel.add(Constants.PARSE_CHANNEL);
        parentObg.put("channels",channel);

        return parentObg;
    }
    private HashMap<String, Object> getPushObject(){
        HashMap<String, Object> parentObg = new HashMap<>();

        // set push message here
        HashMap<String, String> childObg = new HashMap<>();
        childObg.put("alert","Test Push To Device");
        parentObg.put("data",childObg);
        // set channel here
        List<String> channel = new ArrayList<>();
        channel.add(Constants.PARSE_CHANNEL);
        parentObg.put("channels",channel);

        return parentObg;
    }
    // PushInstallation RetrieveInstallation/RegisterDevice/SendPush //Section//End point
    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }
    /* Get Total Size of Records*/
    private int getRowEntries(Response<ResultModel> response){
        return response.body().getTotalRecords().size();
    }
}
