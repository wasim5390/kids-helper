package com.uiu.helper.KidsHelper.mvp.source;

import com.uiu.helper.KidsHelper.mvp.model.NotificationsListResponse;
import com.uiu.helper.KidsHelper.mvp.model.response.BaseResponse;
import com.uiu.helper.KidsHelper.mvp.model.response.GetAccountResponse;
import com.uiu.helper.KidsHelper.mvp.model.response.GetAllSlidesResponse;
import com.uiu.helper.KidsHelper.mvp.model.response.GetDirectionsResponse;
import com.uiu.helper.KidsHelper.mvp.model.response.GetFavAppsResponse;
import com.uiu.helper.KidsHelper.mvp.model.response.GetFavContactResponse;
import com.uiu.helper.KidsHelper.mvp.model.response.GetFavLinkIconResponce;
import com.uiu.helper.KidsHelper.mvp.model.response.GetFavLinkResponse;
import com.uiu.helper.KidsHelper.mvp.model.response.GetKidTrackerResponse;
import com.uiu.helper.KidsHelper.mvp.model.response.GetReminderResponse;
import com.uiu.helper.KidsHelper.mvp.model.response.GetSecondaryHelperResponse;
import com.uiu.helper.KidsHelper.mvp.model.response.GetSettingsResponse;
import com.uiu.helper.KidsHelper.mvp.model.response.GetSosResponse;
import com.uiu.helper.KidsHelper.mvp.model.response.InvitationResponse;
import com.uiu.helper.KidsHelper.mvp.ui.slides.reminder.ReminderEntity;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sidhu on 4/11/2018.
 */

public interface API {

    @POST("users/register")
    Call<GetAccountResponse> createAccount(@Body HashMap<String, Object> params);

    @POST("users/update_fcm_key")
    Call<BaseResponse> updateFirebaseToken(@Query("user_id") String userId, @Query("fcm_key") String fcmKey);

    @POST("invitations/send")
    Call<InvitationResponse> sendInvite(@Query("email") String email,@Query("helper_id") String helperId);

    @POST("invitations/resend")
    Call<InvitationResponse> resendInvite(@Query("id") String inviteId);

    @PATCH("invitations/{id}")
    Call<InvitationResponse> updateInvite(@Path("id") String inviteId,@Query("user_id") String userId,@Query("secondary_helper_id") String secondaryHelperId, @Query("request_status") int status);

    @GET("invitations")
    Call<InvitationResponse> getInvites(@Query("id") String id);

    @DELETE("invitations/{id}")
    Call<BaseResponse> disconnectKid(@Path("id") String inviteId);

    @GET("users/registered_users")
    Call<GetFavContactResponse> getRegisteredContacts(@Query("user_id") String userId);

    @GET("users/secondary_helpers")
    Call<GetSecondaryHelperResponse> getSecondaryHelpers(@Query("kid_id") String kidId);

    // ==================== Slides ================== /
    @GET("slides/user_slides")
    Call<GetAllSlidesResponse> getUserSlides(@Query ("user_id") String userId);

    @POST("slides")
    Call<GetAllSlidesResponse> addNewSlide(@Body HashMap<String, Object> params);

    @DELETE("slides/{id}")
    Call<BaseResponse> removeSlide(@Path("id") String slideId, @Query("helper_id") String helperId);

    @POST("contacts/add_to_slide")
    Call<GetFavContactResponse> saveOnSlide(@Body HashMap<String, Object> params);

    // ================= Favorite Links ========= /
    @POST("links/add_to_slide")
    Call<GetFavLinkResponse> addLinkOnSlide(@Body HashMap<String,Object> params);

    @GET("allicons.json")
    Call<GetFavLinkIconResponce> getLinkIcon(@Query ("url") String url);

    @GET("links/slide_links")
    Call<GetFavLinkResponse> getFavLinks(@Query ("slide_id") String slideId);

    @DELETE("links/{id}")
    Call<BaseResponse> removeLinkFromSlide(@Path("id") String linkId, @Query("helper_id") String helperId);

    // ============ Favorite SOS ============ /

    @POST("sos/add_to_slide")
    Call<GetSosResponse> saveSosOnSlide(@Body HashMap<String, Object> params);

    @GET("sos/slide_sos")
    Call<GetSosResponse> getSOS(@Query ("slide_id") String slideId);

    @DELETE("sos/{id}")
    Call<BaseResponse> removeSosFromSlide(@Path("id") String id, @Query("helper_id") String helperId);

    // ============ Favorite Peoples ============= /

    @GET("contacts/slide_contacts")
    Call<GetFavContactResponse> getContactSlide(@Query ("slide_id") String var);

    @DELETE("contacts/{id}")
    Call<BaseResponse> removeContactFromSlide(@Path("id") String id, @Query("helper_id") String helperId);


    // ============ Applications ================= /
    @GET("applications/slide_applications")
    Call<GetFavAppsResponse> getFavApps(@Query ("slide_id") String slideId);

    @POST("applications/add_to_slide")
    Call<GetFavAppsResponse> saveAppOnSlide(@Body HashMap<String, Object> params);

    @DELETE("applications/{id}")
    Call<BaseResponse> removeAppFromSlide(@Path("id") String appId, @Query("helper_id") String helperId);


    // ================ Reminders ====================//

    @GET("reminders")
    Call<GetReminderResponse> getReminderList(@Query ("slide_id") String slideId);

    @Multipart
    @POST("reminders/add_to_slide")
    Call<GetReminderResponse> addReminderToSlide( @Part MultipartBody.Part file, @Part("reminder") ReminderEntity params);

    @DELETE("reminders/{id}")
    Call<BaseResponse> deleteReminderFromSlide(@Path("id") String reminderId, @Query("helper_id") String helperId);

    // ================== Directions ==================//

    @GET("directions")
    Call<GetDirectionsResponse> getDirections(@Query ("user_id") String userId);

    @POST("directions")
    Call<GetDirectionsResponse> addDirection(@Body() HashMap<String,Object> params);

    @GET("directions/slide_directions")
    Call<GetDirectionsResponse> getDirectionSlide(@Query ("slide_id") String var);

    @DELETE("directions/{id}")
    Call<BaseResponse> removeDirectionFromSlide(@Path("id") String id, @Query("helper_id") String helperId);

    @GET("trackers/get_location")
    Call<GetKidTrackerResponse> getKidLocation(@Query ("user_id") String userId);

    // ================= Settings =====================//
    @GET("settings")
    Call<GetSettingsResponse> getKidsDeviceSettings(@Query ("user_id") String userId);

    @POST("actions/beep")
    Call<BaseResponse> findKidDevice(@Query("kid_id") String userId, @Query("helper_id") String helperId);

    @PATCH("settings/update")
    Call<GetSettingsResponse> updateSettings(@Body HashMap<String,Object> params);

    // ================ Notifications ================//
    @GET("notifications")
    Call<NotificationsListResponse> getNotifications(@Query ("pending") boolean pendingNotifications,@Query("kid_id") String kidId,@Query ("user_id") String userId, @Query("page") int page);

    @DELETE("notifications/{id}")
    Call<BaseResponse> removeNotification(@Path("id") String notificationId);

    @Multipart
    @POST("contacts/share_file")
    Call<BaseResponse> shareMediaFile(@PartMap HashMap<String, RequestBody> params, @Part MultipartBody.Part file);

    // ================ Status Update ================//
    @POST("slides/update_request_status")
    Call<BaseResponse> updateSlideObjectStatus(@Body HashMap<String,Object> params);


}

