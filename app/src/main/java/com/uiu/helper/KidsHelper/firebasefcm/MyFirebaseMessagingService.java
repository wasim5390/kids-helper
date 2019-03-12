package com.uiu.helper.KidsHelper.firebasefcm;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.uiu.helper.KidsHelper.constants.AppConstants;
import com.uiu.helper.KidsHelper.entities.ContactEntity;
import com.uiu.helper.KidsHelper.helpers.NotificationID;
import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.events.NotificationReceiveEvent;
import com.uiu.helper.KidsHelper.mvp.events.ShareEvent;
import com.uiu.helper.KidsHelper.mvp.model.NotificationSender;
import com.uiu.helper.R;
import com.uiu.helper.receivers.NotificationActionReceiver;
import com.uiu.helper.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import androidx.core.app.NotificationCompat;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    String profileImage="https://raw.githubusercontent.com/Infernus101/ProfileUI/0690f5e61a9f7af02c30342d4d6414a630de47fc/icon.png";
    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }
        // Check if message contains a data payload.
        // Handle message within 10 seconds
        handleNotification(remoteMessage);



        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNotification(RemoteMessage remoteMessage) {
        int notificationType=0;
        Log.d(TAG, "Short lived task is done.");
        Map<String, String> map = remoteMessage.getData();
        JSONObject object = new JSONObject(map);
        try {
            String message="";JSONObject sender =null;
            NotificationSender notificationSender=null;
            String title = object.getString("title");
            if(object.has("message"))
                message = object.getString("message");
            if(object.has("image"))
                profileImage = object.getString("image");
            if(remoteMessage.getData().containsKey("sender")) {
                sender = new JSONObject(remoteMessage.getData().get("sender"));
                notificationSender = new Gson().fromJson(sender.toString(), NotificationSender.class);
            }
            if(remoteMessage.getData().containsKey("file_type")){

                int fileType = Integer.valueOf(remoteMessage.getData().get("file_type"));
                String fileUrl = remoteMessage.getData().get("file_url");
                String created_at = remoteMessage.getData().get("created_at");
                ShareEvent event = new ShareEvent(fileType,fileUrl,title,created_at);
                event.setSender(notificationSender);
                if(remoteMessage.getData().containsKey("thumbnail"))
                    event.setThumbnailUrl(remoteMessage.getData().get("thumbnail"));
                EventBus.getDefault().postSticky(event);
                //  sendFileNotification(event.getTitle(),event.getTitle(),event.getMediaType(),event.getFileUrl(),event.getSender().getSenderName(),event.getThumbnailUrl());
                return;
            }

            if(remoteMessage.getData().containsKey("object")) {
                JSONObject jsonObject = new JSONObject(remoteMessage.getData().get("object"));
                int status = Integer.valueOf(jsonObject.getInt("request_status"));
                notificationType = Integer.valueOf(remoteMessage.getData().get("notification_type"));

                EventBus.getDefault().postSticky(new NotificationReceiveEvent(title,message,jsonObject, notificationType,status));
                if(notificationType== Constant.INVITE_CODE || notificationType ==Constant.SLIDE_CREATE_INDEX
                        || notificationType==Constant.SLIDE_REMOVE_INDEX || notificationType ==Constant.REQ_BATTERY_ALERT
                        || notificationType ==Constant.REQ_SETTINGS || notificationType == Constant.REQ_LOCATION
                )
                    return;
                if(notificationType==Constant.REQ_KID_OUT_OF_FENCE) {
                    showNotification(this, R.drawable.companion_app_title_logo_small, title, message);
                    return;
                }
            }

            /*if(object.has("incoming")) {
                int incoming = object.getInt("incoming"); // 1 means notification is coming for Primary_Parent

                if (incoming == 0) {
                    showNotification(this, R.drawable.companion_icon, title, message);
                    return;
                }
            }*/
            if(object.has("kid_id"))
            {
                String kid= object.getString("kid_id");
                String helper = object.getString("helper_id");
                sendNotification(title,message,notificationType,null,null,kid,helper);
            }
            else {
                String itemObj = object.getString("object");

                sendNotification(title, message, notificationType,itemObj,notificationSender,null,null);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static final String PRIMARY_CHANNEL = "default";
    private static boolean currentVersionSupportBigNotification = false;
    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param title FCM message body received.
     */
    public void sendNotification(String title, String message,int notificationType, String contact,NotificationSender sender, String kidId,String helperId) {


        int nId = NotificationID.getID();

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.companion_icon);
        RemoteViews expandedView = new RemoteViews(getPackageName(), R.layout.dialog_fcm_notification);
        expandedView.setTextViewText(R.id.textViewMessage, message);
        currentVersionSupportBigNotification = Utils.currentVersionSupportBigNotification();
        NotificationCompat.Builder builder  = new NotificationCompat.Builder(this, PRIMARY_CHANNEL)
                .setSmallIcon(R.drawable.companion_icon)
                .setLargeIcon(icon)
                .setOngoing(false)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setContentTitle(title)
                .setContentText(message);
        if(contact!=null) {
            ContactEntity contactEntity = new ContactEntity();
            contactEntity = contactEntity.setFCMContact(contact);
            if(Integer.parseInt(sender.getUserType())!=(Constant.KIDS_PRIMARY_HELPER)
            || contactEntity.getRequestStatus()==Constant.ACCEPTED
            )
                    setListeners(expandedView,builder,getApplicationContext(),notificationType,nId, contactEntity, null ,sender,sender.getId());
        }else {
            setListeners(expandedView,builder,getApplicationContext(),notificationType,nId, null, kidId,sender,helperId);
        }

        NotificationManager notificationManager = getNotificationManager(this);
        createChannel(notificationManager);
        new Handler(Looper.getMainLooper()).post(() -> {

            if(profileImage==null || profileImage.isEmpty())
                profileImage="t.com";
            new generatePictureNotification(this,builder,notificationManager,nId,profileImage).execute();


        });

    }




    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param title FCM message body received.
     */
    public void sendFileNotification(String title, String message,int notificationType, String url,String sender,String thumbnail) {


        int nId = NotificationID.getID();

        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.companion_icon);
        RemoteViews expandedView = new RemoteViews(getPackageName(), R.layout.dialog_fcm_notification);
        expandedView.setTextViewText(R.id.textViewMessage, message);
        currentVersionSupportBigNotification = Utils.currentVersionSupportBigNotification();
        NotificationCompat.Builder builder  = new NotificationCompat.Builder(this, PRIMARY_CHANNEL)
                .setSmallIcon(R.drawable.companion_icon)
                .setLargeIcon(icon)
                .setOngoing(false)
                .setAutoCancel(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(Notification.PRIORITY_MAX)
                .setDefaults(Notification.DEFAULT_ALL)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setContentTitle(title)
                .setContentText(message);




        NotificationManager notificationManager = getNotificationManager(getApplicationContext());
        createChannel(notificationManager);
        new Handler(Looper.getMainLooper()).post(() -> {

            /*if(thumbnail==null || thumbnail.isEmpty())
                profileImage="t.com";
            else
                profileImage = thumbnail;*/

            Picasso.with(getApplicationContext()).load(profileImage).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    bitmap = Bitmap.createScaledBitmap(bitmap, 70, 70, false);
                    builder.setLargeIcon(bitmap);
                    Notification notification = builder.build();
                    notificationManager.notify(nId, notification);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.placeholder_sqr);
                    builder.setLargeIcon(bitmap);
                    Notification notification = builder.build();
                    notificationManager.notify(nId, notification);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            });
        });


    }

    //get a NotificationManager
    static NotificationManager getNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }
    public static void cancelNotification(Context context, int id){
        getNotificationManager(context).cancel(id);
    }
    private void createChannel(NotificationManager manager){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chan1 = new NotificationChannel(PRIMARY_CHANNEL,
                    "KidsHelper", NotificationManager.IMPORTANCE_HIGH);
            chan1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            manager.createNotificationChannel(chan1);
        }

    }
    public void setListeners(RemoteViews view,NotificationCompat.Builder builder,Context context,int notificationType, int nId, ContactEntity contactEntity, String kidId,NotificationSender sender,String helperId) {
       if(contactEntity!=null
               && contactEntity.getRequestStatus()==Constant.ACCEPTED
               && Integer.parseInt(sender.getUserType())==Constant.KIDS_PRIMARY_HELPER)
        return;
        Intent intentApprove = new Intent(AppConstants.NOTIFICATION.APPROVE);
        Intent intentReject = new Intent(AppConstants.NOTIFICATION.REJECT);

        Bundle b = new Bundle();
        if(contactEntity!=null) {

            b.putString(AppConstants.EXTRA.EXTRA_OBJECT_ID, contactEntity.getObjectId());
            b.putInt(AppConstants.EXTRA.EXTRA_REQUEST_STATUS, contactEntity.getRequestStatus());
            b.putString(AppConstants.SLIDE.SLIDE_ID, contactEntity.getSlideId());
            b.putString(AppConstants.EXTRA.EXTRA_HELPER_ID, helperId);

        }else{
            b.putString(AppConstants.EXTRA.EXTRA_HELPER_ID, helperId);
            b.putString(AppConstants.EXTRA.EXTRA_USER_ID, kidId);
        }
        b.putInt(AppConstants.EXTRA.EXTRA_NOTIFICATION_ID,nId);
        b.putInt(Constant.EXTRA_NOTIFICATION_TYPE,notificationType);
        intentApprove.putExtras(b);
        intentReject.putExtras(b);

        intentApprove.setClass(this, NotificationActionReceiver.class);
        intentReject.setClass(this, NotificationActionReceiver.class);

        PendingIntent pendingApprove = PendingIntent.getBroadcast(context.getApplicationContext(), nId, intentApprove, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingReject = PendingIntent.getBroadcast(context.getApplicationContext(), nId, intentReject, PendingIntent.FLAG_UPDATE_CURRENT);


        view.setOnClickPendingIntent(R.id.btnLeft, pendingApprove);
        view.setOnClickPendingIntent(R.id.btnRight, pendingReject);

        builder.addAction(R.drawable.ic_check_black_24dp,"Approve",pendingApprove);
        builder.addAction(R.drawable.ic_close_black_24dp,"Decline",pendingReject);
    }

    private void showNotification(Context context, int smallIcon, String contentTitle, String contentText) {
        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // create a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,PRIMARY_CHANNEL)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setSmallIcon(smallIcon)
                .setAutoCancel(true);
        //createChannel(manager);
        // Show a notification
        Notification n = builder.build();
        manager.notify(NotificationID.getID(), n);

    }





    public class generatePictureNotification extends AsyncTask<String, Void, Bitmap> {

        private Context mContext;
        private String  imageUrl;
        private NotificationCompat.Builder builder;
        private NotificationManager manager;
        private int notificationId;
        public generatePictureNotification(Context context, NotificationCompat.Builder builder,NotificationManager manager, int id,String imageUrl) {
            super();
            this.mContext = context;
            this.builder = builder;
            this.notificationId = id;
            this.imageUrl=imageUrl;
            this.manager=manager;
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
            try {
                URL url = new URL(this.imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                if(myBitmap==null)
                    return null;
                return Bitmap.createScaledBitmap(myBitmap, 70, 70, false);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            builder.setLargeIcon(result!=null?result:BitmapFactory.decodeResource(getResources(),R.drawable.placeholder_sqr));
            Notification notification = builder.build();
            manager.notify(notificationId, notification);

        }
    }
}