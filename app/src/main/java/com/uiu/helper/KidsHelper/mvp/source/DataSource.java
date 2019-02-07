package com.uiu.helper.KidsHelper.mvp.source;


import com.uiu.helper.KidsHelper.mvp.model.ContactEntity;
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
import retrofit2.http.Part;

/**
 * Created by sidhu on 4/11/2018.
 */

public interface DataSource {

    void createAccount(HashMap<String, Object> params, GetResponseCallback<GetAccountResponse> callback);

    void getInvites(String userEmail, GetDataCallback<InvitationResponse> callback);
    void sendInvite(String inviteeEmail,String helperId, GetResponseCallback<InvitationResponse> callback);
    void resendInvite(String inviteId, GetResponseCallback<InvitationResponse> callback);
    void disconnect(String userId, GetResponseCallback<BaseResponse> callback);
    void updateInvite(String inviteId,String userId,String secondaryHelperId,int status, GetResponseCallback<InvitationResponse> callback);
    void getSecondaryHelper(String kidId, GetDataCallback<GetSecondaryHelperResponse> callback);

    void getUserSlides(String userId, GetDataCallback<GetAllSlidesResponse> callback);
    void addNewSlide(HashMap<String,Object> params, GetDataCallback<GetAllSlidesResponse> callback);
    void removeSlide(String slideId,String helperId, GetResponseCallback<BaseResponse> callback);

    void getRegdContacts(String id, GetDataCallback<GetFavContactResponse> callback);
    void addFavPeopleToSlide(String id, ContactEntity cont, GetDataCallback<GetFavContactResponse> callback);

    void addFavLinkToSlide(HashMap<String,Object> linkParam, GetDataCallback<GetFavLinkResponse> callback);
    void getFavLinkIcon(String url, GetDataCallback<GetFavLinkIconResponce> callback);
    void getFavLinks(String userId, GetDataCallback<GetFavLinkResponse> callback);
    void removeFavLinkFromSlide(String linkId,String helper_id, GetResponseCallback<BaseResponse> callback);

    void addSosOnSlide(HashMap<String,Object> sosEntity, GetDataCallback<GetSosResponse> callback);
    void getSosFromSlide(String userId, GetDataCallback<GetSosResponse> callback);
    void removeSosFromSlide(String sosId,String helper_id, GetResponseCallback<BaseResponse> callback);

    void getContactsFromSlide(String id, GetDataCallback<GetFavContactResponse> callback);
    void removeFavContactFromSlide(String contactId,String helper_id, GetResponseCallback<BaseResponse> callback);

    void getReminderList(String userId, GetDataCallback<GetReminderResponse> callback);
    void addReminderToSlide(@Part MultipartBody.Part file, ReminderEntity entity, GetDataCallback<GetReminderResponse> callback);
    void deleteReminderFromSlide(String reminderId, String helper_id, GetResponseCallback<BaseResponse> callback);

    void getFavApps(String userId, GetDataCallback<GetFavAppsResponse> callback);
    void addFavAppToSlide(HashMap<String, Object> params, GetDataCallback<GetFavAppsResponse> callback);
    void removeFavAppFromSlide(String appId,String helper_id, GetResponseCallback<BaseResponse> callback);

    void getDirections(String userId, GetDataCallback<GetDirectionsResponse> callback);
    void getDirectionsSlide(String slideId, GetDataCallback<GetDirectionsResponse> callback);
    void addDirectionToSlide(HashMap<String,Object> params, GetDataCallback<GetDirectionsResponse> callback);
    void removeDirectionFromSlide(String directionId,String helperId, GetResponseCallback<BaseResponse> callback);

    void getKidLocation(String userId, GetDataCallback<GetKidTrackerResponse> callback);
    void getKidDeviceSettings(String kidId, GetDataCallback<GetSettingsResponse> callback);
    void updateKidSettings(HashMap<String, Object> params, GetDataCallback<GetSettingsResponse> callback);
    void beepKidsDevice(String kidId,String helperId, GetResponseCallback<BaseResponse> callback);

    void getNotifications(boolean pending,String kidId,String userId, int pageNumber, GetResponseCallback<NotificationsListResponse> callback);

    void updateSlideObjStatus(HashMap<String, Object> params, GetResponseCallback<BaseResponse> callback);
    void removeNotification(String notificationId, GetResponseCallback<BaseResponse> callback);
    void shareMediaFile(HashMap<String, RequestBody> params, MultipartBody.Part body, GetDataCallback<BaseResponse> callback);


    interface GetDataCallback<M> {
        void onDataReceived(M data);

        void onFailed(int code, String message);
    }

    interface GetResponseCallback<M> {
        void onSuccess(M response);

        void onFailed(int code, String message);
    }
}
