package com.uiu.helper;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.uiu.helper.KidsHelper.framework.network.VolleyManager;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Ziv Kesten on 16/07/2015.
 */
public class CompanionApp extends MultiDexApplication {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.

    public static String PARSE_CHANNEL = "Companion";
    private static final String TWITTER_KEY = "zYtdb8neQYt8ph1XoyPmwNWIR";
    private static final String TWITTER_SECRET = "a5Jo7xEwsNh4BhuHodycFOILfV0OEBkNOHNikt5Cf3VhABSxnp";
    public static final String Back4App_ID = "lGpiIUCEzmh4ujitQSZiiAHD4X58zD7iFCgGgNoD";
    public static final String Back4App_ClientID = "j9e5bweIwEwenTvXeZMEKyO6MyecRDA9yX7m0U41";
    private Tracker mTracker;
    GoogleApiClient mGoogleApiClient;
    public static Context mContext;

    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

    public void setmGoogleApiClient(GoogleApiClient mGoogleApiClient) {
        this.mGoogleApiClient = mGoogleApiClient;
    }

    public static CompanionApp getInstance() {
        return instance;
    }

    public static void setInstance(CompanionApp instance) {
        CompanionApp.instance = instance;
    }

    private static CompanionApp instance = new CompanionApp();
    public CompanionApp() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig), new Crashlytics(), new CrashlyticsNdk());

        if (BuildConfig.DEBUG) {
            Parse.initialize(new Parse.Configuration.Builder(this)
                    .applicationId(Back4App_ID)
                    .clientKey(Back4App_ClientID)
                    .server("https://parseapi.back4app.com/").build()
            );
        } else {
            // UIU parse account
            Parse.initialize(new Parse.Configuration.Builder(this)
                    .applicationId(Back4App_ID)
                    .clientKey(Back4App_ClientID)
                    .server("https://parseapi.back4app.com/").build()
            );
        }
        ParseUser.enableRevocableSessionInBackground();

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("GCMSenderId", "368058953589");
        installation.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    e.printStackTrace();
                }
            }
        });
        ParsePush.subscribeInBackground(PARSE_CHANNEL, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Crashlytics.logException(e);
                    e.printStackTrace();
                } else {
                    Log.d("TAG", "Successfully subscribed to Parse!");
                }
            }
        });
        Fresco.initialize(this);
        MultiDex.install(this);
        initializeVolley();
        setUpFireBase();
    }
    private void setUpFireBase(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.app_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }
        // [START subscribe_topics]
        FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.app_name_fcm))
                .addOnCompleteListener(task -> {
                });
        // [END subscribe_topics]
    }
    private void initializeVolley() {

        int memoryClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        int cacheSize = memoryClass * 1024 / 4;

        VolleyManager.initialize(getApplicationContext(), cacheSize);
    }

}
