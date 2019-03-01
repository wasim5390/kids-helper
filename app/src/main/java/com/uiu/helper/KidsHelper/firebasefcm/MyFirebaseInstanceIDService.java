package com.uiu.helper.KidsHelper.firebasefcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.model.User;
import com.uiu.helper.KidsHelper.mvp.model.response.BaseResponse;
import com.uiu.helper.KidsHelper.mvp.source.DataSource;
import com.uiu.helper.KidsHelper.mvp.source.Repository;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        PreferenceUtil.getInstance(getApplicationContext()).savePreference(Constant.PREF_NOTIFICATION_TOKEN,refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {

        User user = PreferenceUtil.getInstance(this).getAccount();
        if(user.getId() == null)
            return;

        Repository.getInstance().addFirebaseToken(user.getId(), token, new DataSource.GetResponseCallback<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse response) {

            }

            @Override
            public void onFailed(int code, String message) {

            }
        });
    }
}