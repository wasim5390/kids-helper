package com.uiu.helper.KidsHelper.mvp.source;


import android.util.Log;


import com.uiu.helper.KidsHelper.mvp.Constant;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Part;


/**
 * Created by sidhu on 4/11/2018.
 */

public class RemoteDataSource implements DataSource, Constant {

    private static RemoteDataSource instance;
    private static final String ERROR_MESSAGE = "Fail";

    private RemoteDataSource() {

    }

    public static RemoteDataSource getInstance() {
        if (instance == null) {
            instance = new RemoteDataSource();
        }
        return instance;
    }

    @Override
    public void createAccount(HashMap<String, Object> params, GetResponseCallback<GetAccountResponse> callback) {
        Call<GetAccountResponse> call = RetrofitHelper.getInstance().getApi().createAccount(params);
        call.enqueue(new Callback<GetAccountResponse>() {
            @Override
            public void onResponse(Call<GetAccountResponse> call, Response<GetAccountResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    String errorMsg=response.message();

                    callback.onFailed(response.code(), errorMsg);
                }
            }

            @Override
            public void onFailure(Call<GetAccountResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void addFirebaseToken(String userId, String fcmKey, GetResponseCallback<BaseResponse> callback) {
        Call<BaseResponse> call = RetrofitHelper.getInstance().getApi().updateFirebaseToken(userId,fcmKey);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    String errorMsg=response.message();

                    callback.onFailed(response.code(), errorMsg);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void updateInvite(String inviteId,String userId,String secondaryHelperId, int status,GetResponseCallback<InvitationResponse> callback) {
        Call<InvitationResponse> call = RetrofitHelper.getInstance().getApi().updateInvite(inviteId,userId,secondaryHelperId,status);
        call.enqueue(new Callback<InvitationResponse>() {
            @Override
            public void onResponse(Call<InvitationResponse> call, Response<InvitationResponse> response) {
                if(response.isSuccessful())
                    callback.onSuccess(response.body());
                else
                    callback.onFailed(response.code(),response.message());
            }

            @Override
            public void onFailure(Call<InvitationResponse> call, Throwable t) {
                callback.onFailed(0,t.getMessage());
            }
        });
    }

    @Override
    public void getSecondaryHelper(String kidId, GetDataCallback<GetSecondaryHelperResponse> callback) {
        Call<GetSecondaryHelperResponse> call = RetrofitHelper.getInstance().getApi().getSecondaryHelpers(kidId);
        call.enqueue(new Callback<GetSecondaryHelperResponse>() {
            @Override
            public void onResponse(Call<GetSecondaryHelperResponse> call, Response<GetSecondaryHelperResponse> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else
                    callback.onFailed(response.code(),response.message());
            }

            @Override
            public void onFailure(Call<GetSecondaryHelperResponse> call, Throwable t) {
                callback.onFailed(0,t.getMessage());
            }
        });
    }

    @Override
    public void getInvites(String id, GetDataCallback<InvitationResponse> callback) {
        Call<InvitationResponse> call = RetrofitHelper.getInstance().getApi().getInvites(id);
        call.enqueue(new Callback<InvitationResponse>() {
            @Override
            public void onResponse(Call<InvitationResponse> call, Response<InvitationResponse> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else
                    callback.onFailed(response.code(),response.message());
            }

            @Override
            public void onFailure(Call<InvitationResponse> call, Throwable t) {
                callback.onFailed(0,t.getMessage());
            }
        });
    }

    @Override
    public void sendInvite(String inviteeEmail, String helperId, GetResponseCallback<InvitationResponse> callback) {
        Call<InvitationResponse> call = RetrofitHelper.getInstance().getApi().sendInvite(inviteeEmail,helperId);
        call.enqueue(new Callback<InvitationResponse>() {
            @Override
            public void onResponse(Call<InvitationResponse> call, Response<InvitationResponse> response) {
                if(response.isSuccessful())
                    callback.onSuccess(response.body());
                else
                    callback.onFailed(response.code(),response.message());
            }

            @Override
            public void onFailure(Call<InvitationResponse> call, Throwable t) {
                callback.onFailed(0,t.getMessage());
            }
        });
    }

    @Override
    public void resendInvite(String inviteId, GetResponseCallback<InvitationResponse> callback) {
        Call<InvitationResponse> call = RetrofitHelper.getInstance().getApi().resendInvite(inviteId);
        call.enqueue(new Callback<InvitationResponse>() {
            @Override
            public void onResponse(Call<InvitationResponse> call, Response<InvitationResponse> response) {
                if(response.isSuccessful())
                    callback.onSuccess(response.body());
                else
                    callback.onFailed(response.code(),response.message());
            }

            @Override
            public void onFailure(Call<InvitationResponse> call, Throwable t) {
                callback.onFailed(0,t.getMessage());
            }
        });
    }

    @Override
    public void disconnect(String userId, GetResponseCallback<BaseResponse> callback) {
        Call<BaseResponse> call = RetrofitHelper.getInstance().getApi().disconnectKid(userId);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.isSuccessful())
                    callback.onSuccess(response.body());
                else
                    callback.onFailed(response.code(),response.message());
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                callback.onFailed(0,t.getMessage());
            }
        });
    }

    @Override
    public void getUserSlides(String userId, GetDataCallback<GetAllSlidesResponse> callback) {
        Call<GetAllSlidesResponse> call = RetrofitHelper.getInstance().getApi().getUserSlides(userId);
        call.enqueue(new Callback<GetAllSlidesResponse>() {
            @Override
            public void onResponse(Call<GetAllSlidesResponse> call, Response<GetAllSlidesResponse> response) {
                if (response.isSuccessful()) {
                    callback.onDataReceived(response.body());
                } else {
                    String errorMsg=response.message();
                    callback.onFailed(response.code(), errorMsg);
                }
            }

            @Override
            public void onFailure(Call<GetAllSlidesResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void addNewSlide(HashMap<String,Object> params, GetDataCallback<GetAllSlidesResponse> callback) {
        Call<GetAllSlidesResponse> call = RetrofitHelper.getInstance().getApi().addNewSlide(params);
        call.enqueue(new Callback<GetAllSlidesResponse>() {
            @Override
            public void onResponse(Call<GetAllSlidesResponse> call, Response<GetAllSlidesResponse> response) {
                if (response.isSuccessful()) {
                    callback.onDataReceived(response.body());
                } else {
                    String errorMsg=response.message();
                    callback.onFailed(response.code(), errorMsg);
                }
            }

            @Override
            public void onFailure(Call<GetAllSlidesResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void removeSlide(String slideId,String helperId, GetResponseCallback<BaseResponse> callback) {
        Call<BaseResponse> call = RetrofitHelper.getInstance().getApi().removeSlide(slideId,helperId);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.isSuccessful())
                    callback.onSuccess(response.body());
                else
                    callback.onFailed(response.code(),response.message());
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                callback.onFailed(0,t.getMessage());
            }
        });
    }

    @Override
    public void getRegdContacts(String userId, GetDataCallback<GetFavContactResponse> callback) {
        Call<GetFavContactResponse> call = RetrofitHelper.getInstance().getApi().getRegisteredContacts(userId);
        call.enqueue(new Callback<GetFavContactResponse>() {
            @Override
            public void onResponse(Call<GetFavContactResponse> call, Response<GetFavContactResponse> response) {
                if (response.isSuccessful()) {
                    callback.onDataReceived(response.body());
                } else {
                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<GetFavContactResponse> call, Throwable t) {
                Log.i("ContactEntity", "Error response--> " + t.getMessage());
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void addFavPeopleToSlide(String id, ContactEntity data, final GetDataCallback<GetFavContactResponse> callback) {

        HashMap<String, Object> params = new HashMap<>();
        data.setSlide_id(id);
        data.setRequestStatus(ACCEPTED);
        params.put("contact", data);

        Call<GetFavContactResponse> call = RetrofitHelper.getInstance().getApi().saveOnSlide(params);
        call.enqueue(new Callback<GetFavContactResponse>() {
            @Override
            public void onResponse(Call<GetFavContactResponse> call, Response<GetFavContactResponse> response) {
                if (response.isSuccessful()) {
                    callback.onDataReceived(response.body());
                } else {

                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<GetFavContactResponse> call, Throwable t) {
                Log.i("ContactEntity", "Error response--> " + t.getMessage());
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }


    @Override
    public void addFavLinkToSlide(HashMap<String, Object> linkParam, GetDataCallback<GetFavLinkResponse> callback) {
        Call<GetFavLinkResponse> call = RetrofitHelper.getInstance().getApi().addLinkOnSlide(linkParam);
        call.enqueue(new Callback<GetFavLinkResponse>() {
            @Override
            public void onResponse(Call<GetFavLinkResponse> call, Response<GetFavLinkResponse> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else {

                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<GetFavLinkResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }


    @Override
    public void getFavApps(String userId, GetDataCallback<GetFavAppsResponse> callback) {
        Call<GetFavAppsResponse> call = RetrofitHelper.getInstance().getApi().getFavApps(userId);
        call.enqueue(new Callback<GetFavAppsResponse>() {
            @Override
            public void onResponse(Call<GetFavAppsResponse> call, Response<GetFavAppsResponse> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else {

                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<GetFavAppsResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void getContactsFromSlide(String id, GetDataCallback<GetFavContactResponse> callback) {
        Call<GetFavContactResponse> call = RetrofitHelper.getInstance().getApi().getContactSlide(id);
        call.enqueue(new Callback<GetFavContactResponse>() {
            @Override
            public void onResponse(Call<GetFavContactResponse> call, Response<GetFavContactResponse> response) {
                if (response.isSuccessful()) {
                    callback.onDataReceived(response.body());
                } else {

                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<GetFavContactResponse> call, Throwable t) {
                Log.i("ContactEntity", "Error response--> " + t.getMessage());
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void removeFavContactFromSlide(String contactId, String helperId, GetResponseCallback<BaseResponse> callback) {
        Call<BaseResponse> call = RetrofitHelper.getInstance().getApi().removeContactFromSlide(contactId,helperId);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.isSuccessful())
                    callback.onSuccess(response.body());
                else
                    callback.onFailed(response.code(),response.message());
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                callback.onFailed(0,t.getMessage());
            }
        });
    }
    @Override
    public void getFavLinkIcon(String url, GetDataCallback<GetFavLinkIconResponce> callback) {

        Call<GetFavLinkIconResponce> call =RetrofitHelper.getIconApi().getLinkIcon(url);
        call.enqueue(new Callback<GetFavLinkIconResponce>() {
            @Override
            public void onResponse(Call<GetFavLinkIconResponce> call, Response<GetFavLinkIconResponce> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else {

                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<GetFavLinkIconResponce> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });

    }
    @Override
    public void getFavLinks(String userId, GetDataCallback<GetFavLinkResponse> callback) {

        Call<GetFavLinkResponse> call = RetrofitHelper.getInstance().getApi().getFavLinks(userId);
        call.enqueue(new Callback<GetFavLinkResponse>() {
            @Override
            public void onResponse(Call<GetFavLinkResponse> call, Response<GetFavLinkResponse> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else {

                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<GetFavLinkResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });

    }

    @Override
    public void removeFavLinkFromSlide(String linkId, String helperId, GetResponseCallback<BaseResponse> callback) {
        Call<BaseResponse> call = RetrofitHelper.getInstance().getApi().removeLinkFromSlide(linkId,helperId);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.isSuccessful())
                    callback.onSuccess(response.body());
                else
                    callback.onFailed(response.code(),response.message());
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                callback.onFailed(0,t.getMessage());
            }
        });
    }

    @Override
    public void addSosOnSlide(HashMap<String, Object> sosEntity, GetDataCallback<GetSosResponse> callback) {
        Call<GetSosResponse> call = RetrofitHelper.getInstance().getApi().saveSosOnSlide(sosEntity);
        call.enqueue(new Callback<GetSosResponse>() {
            @Override
            public void onResponse(Call<GetSosResponse> call, Response<GetSosResponse> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else {
                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<GetSosResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void getSosFromSlide(String slideId, GetDataCallback<GetSosResponse> callback) {
        Call<GetSosResponse> call = RetrofitHelper.getInstance().getApi().getSOS(slideId);
        call.enqueue(new Callback<GetSosResponse>() {
            @Override
            public void onResponse(Call<GetSosResponse> call, Response<GetSosResponse> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else {

                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<GetSosResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void removeSosFromSlide(String sosId, String helperId, GetResponseCallback<BaseResponse> callback) {
        Call<BaseResponse> call = RetrofitHelper.getInstance().getApi().removeSosFromSlide(sosId,helperId);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.isSuccessful())
                    callback.onSuccess(response.body());
                else
                    callback.onFailed(response.code(),response.message());
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                callback.onFailed(0,t.getMessage());
            }
        });
    }

    @Override
    public void getReminderList(String user_id, GetDataCallback<GetReminderResponse> callback) {

        Call<GetReminderResponse> call = RetrofitHelper.getInstance().getApi().getReminderList(user_id);
        call.enqueue(new Callback<GetReminderResponse>() {
            @Override
            public void onResponse(Call<GetReminderResponse> call, Response<GetReminderResponse> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else {

                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<GetReminderResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });

    }

    @Override
    public void addReminderToSlide(@Part MultipartBody.Part file, ReminderEntity reminderEntity, GetDataCallback<GetReminderResponse> callback) {
        Call<GetReminderResponse> call = RetrofitHelper.getInstance().getApi().addReminderToSlide(file,reminderEntity);
        call.enqueue(new Callback<GetReminderResponse>() {
            @Override
            public void onResponse(Call<GetReminderResponse> call, Response<GetReminderResponse> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else
                    callback.onFailed(response.code(),response.message());

            }

            @Override
            public void onFailure(Call<GetReminderResponse> call, Throwable t) {
                callback.onFailed(0,t.getMessage());
            }
        });
    }

    @Override
    public void deleteReminderFromSlide(String reminderId,String helperId, GetResponseCallback<BaseResponse> callback) {
        Call<BaseResponse> call = RetrofitHelper.getInstance().getApi().deleteReminderFromSlide(reminderId,helperId);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.isSuccessful())
                    callback.onSuccess(response.body());
                else
                    callback.onFailed(response.code(),response.message());
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                callback.onFailed(0,t.getMessage());
            }
        });

    }

    @Override
    public void addFavAppToSlide(HashMap<String, Object> params, GetDataCallback<GetFavAppsResponse> callback) {
        Call<GetFavAppsResponse> call = RetrofitHelper.getInstance().getApi().saveAppOnSlide(params);
        call.enqueue(new Callback<GetFavAppsResponse>() {
            @Override
            public void onResponse(Call<GetFavAppsResponse> call, Response<GetFavAppsResponse> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else {
                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<GetFavAppsResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void removeFavAppFromSlide(String appId, String helperId, GetResponseCallback<BaseResponse> callback) {
        Call<BaseResponse> call = RetrofitHelper.getInstance().getApi().removeAppFromSlide(appId, helperId);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.isSuccessful())
                    callback.onSuccess(response.body());
                else
                    callback.onFailed(response.code(),response.message());
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                callback.onFailed(0,t.getMessage());
            }
        });
    }

    @Override
    public void getDirections(String userId, GetDataCallback<GetDirectionsResponse> callback) {
        Call<GetDirectionsResponse> call = RetrofitHelper.getInstance().getApi().getDirections(userId);
        call.enqueue(new Callback<GetDirectionsResponse>() {
            @Override
            public void onResponse(Call<GetDirectionsResponse> call, Response<GetDirectionsResponse> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else {
                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<GetDirectionsResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void getDirectionsSlide(String slideId, GetDataCallback<GetDirectionsResponse> callback) {
        Call<GetDirectionsResponse> call = RetrofitHelper.getInstance().getApi().getDirectionSlide(slideId);
        call.enqueue(new Callback<GetDirectionsResponse>() {
            @Override
            public void onResponse(Call<GetDirectionsResponse> call, Response<GetDirectionsResponse> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else {
                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<GetDirectionsResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }


    @Override
    public void addDirectionToSlide(HashMap<String, Object> params, GetDataCallback<GetDirectionsResponse> callback) {
        Call<GetDirectionsResponse> call = RetrofitHelper.getInstance().getApi().addDirection(params);
        call.enqueue(new Callback<GetDirectionsResponse>() {
            @Override
            public void onResponse(Call<GetDirectionsResponse> call, Response<GetDirectionsResponse> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else {
                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<GetDirectionsResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void removeDirectionFromSlide(String directionId, String helperId, GetResponseCallback<BaseResponse> callback) {
        Call<BaseResponse> call = RetrofitHelper.getInstance().getApi().removeDirectionFromSlide(directionId, helperId);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.isSuccessful())
                    callback.onSuccess(response.body());
                else
                    callback.onFailed(response.code(),response.message());
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                callback.onFailed(0,t.getMessage());
            }
        });
    }

    @Override
    public void getKidLocation(String userId, GetDataCallback<GetKidTrackerResponse> callback) {
        Call<GetKidTrackerResponse> call = RetrofitHelper.getInstance().getApi().getKidLocation(userId);
        call.enqueue(new Callback<GetKidTrackerResponse>() {
            @Override
            public void onResponse(Call<GetKidTrackerResponse> call, Response<GetKidTrackerResponse> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else {
                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<GetKidTrackerResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void getKidDeviceSettings(String kidId, GetDataCallback<GetSettingsResponse> callback) {
        Call<GetSettingsResponse> call = RetrofitHelper.getInstance().getApi().getKidsDeviceSettings(kidId);
        call.enqueue(new Callback<GetSettingsResponse>() {
            @Override
            public void onResponse(Call<GetSettingsResponse> call, Response<GetSettingsResponse> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else {
                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<GetSettingsResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void updateKidSettings(HashMap<String, Object> params, GetDataCallback<GetSettingsResponse> callback) {
        Call<GetSettingsResponse> call = RetrofitHelper.getInstance().getApi().updateSettings(params);
        call.enqueue(new Callback<GetSettingsResponse>() {
            @Override
            public void onResponse(Call<GetSettingsResponse> call, Response<GetSettingsResponse> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else {
                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<GetSettingsResponse> call, Throwable t) {

                    callback.onFailed(0, t.getMessage());

            }
        });
    }

    @Override
    public void beepKidsDevice(String kidId, String helperId, GetResponseCallback<BaseResponse> callback) {
        Call<BaseResponse> call = RetrofitHelper.getInstance().getApi().findKidDevice(kidId,helperId);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.isSuccessful())
                    callback.onSuccess(response.body());
                else {
                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void getNotifications(boolean pending,String kidId,String userId, int pageNumber, GetResponseCallback<NotificationsListResponse> callback) {
        Call<NotificationsListResponse> call = RetrofitHelper.getInstance().getApi().getNotifications(pending,kidId,userId,pageNumber);
        call.enqueue(new Callback<NotificationsListResponse>() {
            @Override
            public void onResponse(Call<NotificationsListResponse> call, Response<NotificationsListResponse> response) {
                if(response.isSuccessful())
                    callback.onSuccess(response.body());
                else {
                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<NotificationsListResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }

    @Override
    public void updateSlideObjStatus(HashMap<String, Object> params, GetResponseCallback<BaseResponse> callback) {
        Call<BaseResponse> call = RetrofitHelper.getInstance().getApi().updateSlideObjectStatus(params);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.isSuccessful())
                    callback.onSuccess(response.body());
                else {
                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {

                callback.onFailed(0, t.getMessage());

            }
        });
    }

    @Override
    public void removeNotification(String notificationId, GetResponseCallback<BaseResponse> callback) {
        Call<BaseResponse> call = RetrofitHelper.getInstance().getApi().removeNotification(notificationId);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.isSuccessful())
                    callback.onSuccess(response.body());
                else
                    callback.onFailed(response.code(),response.message());
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                callback.onFailed(0,t.getMessage());
            }
        });
    }

    @Override
    public void shareMediaFile(HashMap<String, RequestBody> params, MultipartBody.Part file, GetDataCallback<BaseResponse> callback) {
        Call<BaseResponse> call = RetrofitHelper.getInstance().getApi().shareMediaFile(params,file);
        call.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if(response.isSuccessful())
                    callback.onDataReceived(response.body());
                else {

                    callback.onFailed(response.code(), response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                callback.onFailed(0, ERROR_MESSAGE);
            }
        });
    }
}
