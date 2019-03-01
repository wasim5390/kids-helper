package com.uiu.helper.KidsHelper.mvp.source;

import android.support.annotation.NonNull;

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

public class Repository implements DataSource {

    private static Repository instance;
    private final RemoteDataSource mRemoteDataSource;

    private Repository(@NonNull RemoteDataSource remoteDataSource) {
        mRemoteDataSource =  remoteDataSource;
    }

    public static Repository getInstance() {

        if (instance==null) {
            instance = new Repository(RemoteDataSource.getInstance());
        }
        return instance;
    }

    @Override
    public void createAccount(HashMap<String, Object> params, GetResponseCallback<GetAccountResponse> callback) {
        mRemoteDataSource.createAccount(params, new GetResponseCallback<GetAccountResponse>() {
            @Override
            public void onSuccess(GetAccountResponse response) {
                callback.onSuccess(response);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void addFirebaseToken(String userId, String fcmKey, GetResponseCallback<BaseResponse> callback) {
        mRemoteDataSource.addFirebaseToken(userId,fcmKey, new GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                callback.onSuccess(response);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void getInvites(String id, GetDataCallback<InvitationResponse> callback) {
        mRemoteDataSource.getInvites(id, new GetDataCallback<InvitationResponse>() {
            @Override
            public void onDataReceived(InvitationResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void sendInvite(String inviteeEmail, String helperId, GetResponseCallback<InvitationResponse> callback) {
        mRemoteDataSource.sendInvite(inviteeEmail,helperId, new GetResponseCallback<InvitationResponse>() {
            @Override
            public void onSuccess(InvitationResponse response) {
                if(response.isSuccess())
                    callback.onSuccess(response);
                else
                    callback.onFailed(response.getHttpCode(), response.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void resendInvite(String inviteId, GetResponseCallback<InvitationResponse> callback) {
        mRemoteDataSource.resendInvite(inviteId, new GetResponseCallback<InvitationResponse>() {
            @Override
            public void onSuccess(InvitationResponse response) {
                if(response.isSuccess())
                    callback.onSuccess(response);
                else
                    callback.onFailed(response.getHttpCode(), response.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void disconnect(String inviteId, GetResponseCallback<BaseResponse> callback) {
        mRemoteDataSource.disconnect(inviteId, new GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                if(response.isSuccess())
                    callback.onSuccess(response);
                else
                    callback.onFailed(response.getHttpCode(), response.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void updateInvite(String inviteId,String userId,String secondaryHelper,int status, GetResponseCallback<InvitationResponse> callback) {
        mRemoteDataSource.updateInvite(inviteId, userId,secondaryHelper,status,new GetResponseCallback<InvitationResponse>() {
            @Override
            public void onSuccess(InvitationResponse response) {
                if(response.isSuccess())
                    callback.onSuccess(response);
                else
                    callback.onFailed(response.getHttpCode(), response.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void getSecondaryHelper(String kidId, GetDataCallback<GetSecondaryHelperResponse> callback) {
        mRemoteDataSource.getSecondaryHelper(kidId, new GetDataCallback<GetSecondaryHelperResponse>() {
            @Override
            public void onDataReceived(GetSecondaryHelperResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void getUserSlides(String userId, GetDataCallback<GetAllSlidesResponse> callback) {
        mRemoteDataSource.getUserSlides(userId, new GetDataCallback<GetAllSlidesResponse>() {
            @Override
            public void onDataReceived(GetAllSlidesResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });

    }

    @Override
    public void addNewSlide(HashMap<String, Object> params, GetDataCallback<GetAllSlidesResponse> callback) {
        mRemoteDataSource.addNewSlide(params, new GetDataCallback<GetAllSlidesResponse>() {
            @Override
            public void onDataReceived(GetAllSlidesResponse data) {
                if(data.isSuccess())
                    callback.onDataReceived(data);
                else
                    callback.onFailed(data.getHttpCode(),data.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void removeSlide(String slideId,String helperId, GetResponseCallback<BaseResponse> callback) {
        mRemoteDataSource.removeSlide(slideId,helperId, new GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                if(response.isSuccess())
                    callback.onSuccess(response);
                else
                    callback.onFailed(response.getHttpCode(),response.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }


    @Override
    public void getFavApps(String userId, GetDataCallback<GetFavAppsResponse> callback) {
        mRemoteDataSource.getFavApps(userId, new GetDataCallback<GetFavAppsResponse>() {
            @Override
            public void onDataReceived(GetFavAppsResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }
    @Override
    public void getContactsFromSlide(String id, final GetDataCallback<GetFavContactResponse> callback) {
        mRemoteDataSource.getContactsFromSlide(id,new GetDataCallback<GetFavContactResponse>() {
            @Override
            public void onDataReceived(GetFavContactResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code, message);
            }
        });
    }

    @Override
    public void removeFavContactFromSlide(String contactId, String helper_id, GetResponseCallback<BaseResponse> callback) {
        mRemoteDataSource.removeFavContactFromSlide(contactId, helper_id, new GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                if(response.isSuccess())
                    callback.onSuccess(response);
                else
                    callback.onFailed(0,response.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void getRegdContacts(String userId, GetDataCallback<GetFavContactResponse> callback) {
        mRemoteDataSource.getRegdContacts(userId,new GetDataCallback<GetFavContactResponse>() {
            @Override
            public void onDataReceived(GetFavContactResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code, message);
            }
        });
    }

    @Override
    public void addFavPeopleToSlide(String id, ContactEntity cont, GetDataCallback<GetFavContactResponse> callback) {
        mRemoteDataSource.addFavPeopleToSlide(id,cont,new GetDataCallback<GetFavContactResponse>() {
            @Override
            public void onDataReceived(GetFavContactResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code, message);
            }
        });
    }

    @Override
    public void addFavLinkToSlide(HashMap<String,Object> linkRequest, GetDataCallback<GetFavLinkResponse> callback) {
        mRemoteDataSource.addFavLinkToSlide(linkRequest, new GetDataCallback<GetFavLinkResponse>() {
            @Override
            public void onDataReceived(GetFavLinkResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void getFavLinks(String userId, GetDataCallback<GetFavLinkResponse> callback) {
        mRemoteDataSource.getFavLinks(userId, new GetDataCallback<GetFavLinkResponse>() {
            @Override
            public void onDataReceived(GetFavLinkResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });

    }

    @Override
    public void getFavLinkIcon(String url, GetDataCallback<GetFavLinkIconResponce> callback) {
        mRemoteDataSource.getFavLinkIcon(url, new GetDataCallback<GetFavLinkIconResponce>() {
            @Override
            public void onDataReceived(GetFavLinkIconResponce data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });


    }

    @Override
    public void removeFavLinkFromSlide(String linkId, String helper_id, GetResponseCallback<BaseResponse> callback) {
        mRemoteDataSource.removeFavLinkFromSlide(linkId, helper_id, new GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                if(response.isSuccess())
                    callback.onSuccess(response);
                else
                    callback.onFailed(0,response.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void addSosOnSlide(HashMap<String, Object> sosEntity, GetDataCallback<GetSosResponse> callback) {
        mRemoteDataSource.addSosOnSlide(sosEntity, new GetDataCallback<GetSosResponse>() {
            @Override
            public void onDataReceived(GetSosResponse data) {
                if(data.isSuccess())
                    callback.onDataReceived(data);
                else
                    callback.onFailed(0,data.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void getSosFromSlide(String slideId, GetDataCallback<GetSosResponse> callback) {
        mRemoteDataSource.getSosFromSlide(slideId, new GetDataCallback<GetSosResponse>() {
            @Override
            public void onDataReceived(GetSosResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void removeSosFromSlide(String sosId, String helper_id, GetResponseCallback<BaseResponse> callback) {
        mRemoteDataSource.removeSosFromSlide(sosId, helper_id, new GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                if(response.isSuccess())
                    callback.onSuccess(response);
                else
                    callback.onFailed(0,response.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void getReminderList(String user_id, GetDataCallback<GetReminderResponse> callback) {
        mRemoteDataSource.getReminderList(user_id, new GetDataCallback<GetReminderResponse>() {
            @Override
            public void onDataReceived(GetReminderResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void addReminderToSlide(@Part MultipartBody.Part file, ReminderEntity reminderEntity, GetDataCallback<GetReminderResponse> callback) {
        mRemoteDataSource.addReminderToSlide(file,reminderEntity, new GetDataCallback<GetReminderResponse>() {
            @Override
            public void onDataReceived(GetReminderResponse response) {
                callback.onDataReceived(response);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void deleteReminderFromSlide(String reminderId,String helperId, GetResponseCallback<BaseResponse> callback) {
        mRemoteDataSource.deleteReminderFromSlide(reminderId,helperId, new GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                callback.onSuccess(response);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void addFavAppToSlide(HashMap<String, Object> params, GetDataCallback<GetFavAppsResponse> callback) {
        mRemoteDataSource.addFavAppToSlide(params, new GetDataCallback<GetFavAppsResponse>() {
            @Override
            public void onDataReceived(GetFavAppsResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void removeFavAppFromSlide(String appId, String helperId, GetResponseCallback<BaseResponse> callback) {
        mRemoteDataSource.removeFavAppFromSlide(appId, helperId, new GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                if( response.isSuccess())
                    callback.onSuccess(response);
                else
                    callback.onFailed(0,response.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void getDirections(String userId, GetDataCallback<GetDirectionsResponse> callback) {
        mRemoteDataSource.getDirections(userId, new GetDataCallback<GetDirectionsResponse>() {
            @Override
            public void onDataReceived(GetDirectionsResponse data) {
                if(data.isSuccess())
                    callback.onDataReceived(data);
                else
                    callback.onFailed(0,data.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void getDirectionsSlide(String slideId, GetDataCallback<GetDirectionsResponse> callback) {
        mRemoteDataSource.getDirectionsSlide(slideId, new GetDataCallback<GetDirectionsResponse>() {
            @Override
            public void onDataReceived(GetDirectionsResponse data) {
                if(data.isSuccess())
                    callback.onDataReceived(data);
                else
                    callback.onFailed(0,data.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void addDirectionToSlide(HashMap<String, Object> params, GetDataCallback<GetDirectionsResponse> callback) {
        mRemoteDataSource.addDirectionToSlide(params, new GetDataCallback<GetDirectionsResponse>() {
            @Override
            public void onDataReceived(GetDirectionsResponse data) {
                if(data.isSuccess())
                    callback.onDataReceived(data);
                else
                    callback.onFailed(0,data.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void removeDirectionFromSlide(String directionId, String helperId, GetResponseCallback<BaseResponse> callback) {
        mRemoteDataSource.removeDirectionFromSlide(directionId, helperId, new GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                if( response.isSuccess())
                    callback.onSuccess(response);
                else
                    callback.onFailed(0,response.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(0,message);
            }
        });
    }

    @Override
    public void getKidLocation(String userId, GetDataCallback<GetKidTrackerResponse> callback) {
        mRemoteDataSource.getKidLocation(userId, new GetDataCallback<GetKidTrackerResponse>() {
            @Override
            public void onDataReceived(GetKidTrackerResponse data) {
                if(data.isSuccess())
                    callback.onDataReceived(data);
                else
                    callback.onFailed(0,data.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void getKidDeviceSettings(String kidId, GetDataCallback<GetSettingsResponse> callback) {
        mRemoteDataSource.getKidDeviceSettings(kidId, new GetDataCallback<GetSettingsResponse>() {
            @Override
            public void onDataReceived(GetSettingsResponse data) {
                if(data.isSuccess())
                    callback.onDataReceived(data);
                else
                    callback.onFailed(0,data.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void updateKidSettings(HashMap<String, Object> params, GetDataCallback<GetSettingsResponse> callback) {
        mRemoteDataSource.updateKidSettings(params, new GetDataCallback<GetSettingsResponse>() {
            @Override
            public void onDataReceived(GetSettingsResponse data) {
                if(data.isSuccess())
                    callback.onDataReceived(data);
                else
                    callback.onFailed(data.getHttpCode(),data.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void beepKidsDevice(String kidId, String helperId, GetResponseCallback<BaseResponse> callback) {
        mRemoteDataSource.beepKidsDevice(kidId, helperId, new GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                callback.onSuccess(response);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void getNotifications(boolean pending,String kidId,String userId, int pageNumber, GetResponseCallback<NotificationsListResponse> callback) {
        mRemoteDataSource.getNotifications(pending,kidId,userId, pageNumber, new GetResponseCallback<NotificationsListResponse>() {
            @Override
            public void onSuccess(NotificationsListResponse response) {
                callback.onSuccess(response);
            }

            @Override
            public void onFailed(int code, String message) {
            callback.onFailed(code,message);
            }
        });
    }

    @Override
    public void updateSlideObjStatus(HashMap<String, Object> params, GetResponseCallback<BaseResponse> callback) {
        mRemoteDataSource.updateSlideObjStatus(params, new GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                if( response.isSuccess())
                    callback.onSuccess(response);
                else
                    callback.onFailed(0,response.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(0,message);
            }
        });
    }

    @Override
    public void removeNotification(String notificationId, GetResponseCallback<BaseResponse> callback) {
        mRemoteDataSource.removeNotification(notificationId, new GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {
                if( response.isSuccess())
                    callback.onSuccess(response);
                else
                    callback.onFailed(0,response.getResponseMsg());
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(0,message);
            }
        });
    }

    @Override
    public void shareMediaFile(HashMap<String, RequestBody> params, MultipartBody.Part body, GetDataCallback<BaseResponse> callback) {
        mRemoteDataSource.shareMediaFile(params,body, new GetDataCallback<BaseResponse>() {
            @Override
            public void onDataReceived(BaseResponse data) {
                callback.onDataReceived(data);
            }

            @Override
            public void onFailed(int code, String message) {
                callback.onFailed(code,message);
            }
        });
    }


}
