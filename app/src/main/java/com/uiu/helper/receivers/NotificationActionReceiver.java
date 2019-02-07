package com.uiu.helper.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.util.Log;

import com.google.gson.JsonObject;
import com.uiu.helper.KidsHelper.constants.AppConstants;
import com.uiu.helper.KidsHelper.firebasefcm.MyFirebaseMessagingService;
import com.uiu.helper.KidsHelper.framework.network.CustomHttpResponse;
import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.events.NotificationReceiveEvent;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;
import com.uiu.helper.KidsHelper.services.IService;
import com.uiu.helper.KidsHelper.services.ServiceFactory;

import org.greenrobot.eventbus.EventBus;

public class NotificationActionReceiver extends BroadcastReceiver {

    ServiceFactory service;
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        int notificationId = intent.getIntExtra(AppConstants.EXTRA.EXTRA_NOTIFICATION_ID, 0);
        MyFirebaseMessagingService.cancelNotification(context, notificationId);
        service = new ServiceFactory(context);
        String action = intent.getAction();
        int notificationType = intent.getIntExtra(Constant.EXTRA_NOTIFICATION_TYPE,0);

        if(intent.hasExtra(AppConstants.EXTRA.EXTRA_OBJECT_ID)) {
           String objectId = intent.getStringExtra(AppConstants.EXTRA.EXTRA_OBJECT_ID);
           String slideId = intent.getStringExtra(AppConstants.SLIDE.SLIDE_ID);
            int requestStatus = intent.getIntExtra(AppConstants.EXTRA.EXTRA_REQUEST_STATUS,1);
            String senderId = intent.getStringExtra(AppConstants.EXTRA.EXTRA_HELPER_ID);

            String request = getObjectStatusResponse(objectId, slideId,
                    action.equalsIgnoreCase(AppConstants.NOTIFICATION.APPROVE)?AppConstants.PERMISSION.Approved:
                            AppConstants.PERMISSION.Rejected,requestStatus,senderId);
            Log.e("Approval",request);
            service.slide.updateStatus(request, new IService() {
                @Override
                public void onSuccess(CustomHttpResponse response) {
                    EventBus.getDefault().post(new NotificationReceiveEvent(notificationType));
                }

                @Override
                public void onFailure(CustomHttpResponse response) {

                }
            });
        }else{
           String helperId = intent.getStringExtra(AppConstants.EXTRA.EXTRA_HELPER_ID);
           String kidId = intent.getStringExtra(AppConstants.EXTRA.EXTRA_USER_ID);
            String request = getKidConnStatusRequest(helperId, kidId,
                    action.equalsIgnoreCase(AppConstants.NOTIFICATION.APPROVE)?true:false);


            service.kidConnectionService.updateStatus(request, new IService() {
                @Override
                public void onSuccess(CustomHttpResponse response) {
                    EventBus.getDefault().post(new NotificationReceiveEvent(notificationType));
                }

                @Override
                public void onFailure(CustomHttpResponse response) {

                }
            });
        }
    }

    public String getObjectStatusResponse(String contactId, String slideId, int requestApproval,int requestStatus,String senderId) {
        String loggedUser = PreferenceUtil.getInstance(context).getAccount().getId();

        // Below one is critical check don't change it
        if(!senderId.equals(loggedUser) && requestStatus==Constant.REJECTED){
            requestApproval = (requestApproval== AppConstants.PERMISSION.Approved)?AppConstants.PERMISSION.Rejected:AppConstants.PERMISSION.Approved;
        }
        JsonObject object = new JsonObject();
        object.addProperty(AppConstants.EXTRA.EXTRA_OBJECT_ID, contactId);
        object.addProperty(AppConstants.SLIDE.SLIDE_ID, slideId);
        object.addProperty(AppConstants.EXTRA.EXTRA_PERMISSION, requestApproval);
        object.addProperty(AppConstants.EXTRA.EXTRA_HELPER_ID, loggedUser);

        return object.toString();
    }

    public String getKidConnStatusRequest(String helperId, String kidId, boolean requestStatus) {

        JsonObject object = new JsonObject();
        object.addProperty(AppConstants.EXTRA.EXTRA_HELPER_ID, helperId);
        object.addProperty(AppConstants.EXTRA.EXTRA_USER_ID, kidId);
        object.addProperty(AppConstants.EXTRA.EXTRA_REQUEST_STATUS, requestStatus);

        return object.toString();
    }
}
