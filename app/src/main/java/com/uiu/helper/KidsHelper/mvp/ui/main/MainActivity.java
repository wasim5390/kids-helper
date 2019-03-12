package com.uiu.helper.KidsHelper.mvp.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.uiu.helper.KidsHelper.mvp.BaseActivity;

import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.Injection;
import com.uiu.helper.KidsHelper.mvp.events.GoogleLoginEvent;
import com.uiu.helper.KidsHelper.mvp.events.LogoutEvent;
import com.uiu.helper.KidsHelper.mvp.events.MainUIActionEvent;
import com.uiu.helper.KidsHelper.mvp.model.User;
import com.uiu.helper.KidsHelper.mvp.model.response.GetAccountResponse;
import com.uiu.helper.KidsHelper.mvp.source.DataSource;
import com.uiu.helper.KidsHelper.mvp.source.Repository;
import com.uiu.helper.KidsHelper.mvp.ui.invite.InviteFragment;
import com.uiu.helper.KidsHelper.mvp.ui.invite.InvitePresenter;
import com.uiu.helper.KidsHelper.mvp.util.PermissionUtil;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;
import com.uiu.helper.R;
import com.uiu.helper.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.drawerlayout.widget.DrawerLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.uiu.helper.CompanionApp.getContext;

public class MainActivity extends BaseActivity implements PermissionUtil.PermissionCallback {

    private static final int RC_SIGN_IN = 0x006;
    private static final String TAG = MainActivity.class.getSimpleName();

    private GoogleSignInAccount account;
    private int count = 0;


    @BindView(R.id.navigation_view)
    NavigationView navigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressBar)
    ContentLoadingProgressBar progressBar;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    private GoogleSignInClient mGoogleSignInClient;
    SimpleDraweeView image;
    TextView name, email;
    TextView navUpgradeView;
    User user;
    Repository repository;


    @Override
    public int getID() {
        return R.layout.helper_activity_main;
    }

    @Override
    public void created(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        View header = navigationView.getHeaderView(0);

        name = header.findViewById(R.id.navigation_view_profile_name);
        email = header.findViewById(R.id.navigation_view_profile_email);
        image = header.findViewById(R.id.navigation_view_profile_image);
        navUpgradeView = header.findViewById(R.id.navigation_view_upgrade);


        repository = Injection.provideRepository(this);
        setDrawerToggle();
        user = PreferenceUtil.getInstance(MainActivity.this).getAccount();
        if(user.getId()!=null){
            PermissionUtil.requestPermissions(this,this);
            setProfileInfo(user.getName(),user.getEmail(),user.getImageLink());
            return;
        }
        googleSignInClient();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account == null) {
            loadWelcomeFragment();
        } else {
            getProfileInformation(account);
        }

    }

    private void setProfileInfo(String name,String email,String image){
        this.name.setText(name);
        this.email.setText(email);
        this.image.setImageURI(image);
        toolbar.setVisibility(View.VISIBLE);
    }

    private void setDrawerToggle() {
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
             /*   if(tinyDB.getBoolean(Constants.ParseConstants.IS_SUBSCRIBED,false)) {
                    navUpgradeView.setText(getString(R.string.helper_plus_menu));
                }else{
                    navUpgradeView.setText("");
                }*/
                super.onDrawerOpened(drawerView);
            }
        };
        actionBarDrawerToggle.syncState();
    }

    private void getProfileInformation(GoogleSignInAccount acct) {
        if (acct != null) {
            showProgress();
            account = acct;
            setProfileInfo(acct.getDisplayName(),acct.getEmail(),acct.getPhotoUrl()!=null?acct.getPhotoUrl().toString():null);
            toolbar.setVisibility(View.VISIBLE);
            PermissionUtil.requestPermissions(this, this);
        }
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.show();
    }

    @Override
    public void hideProgress() {
        progressBar.hide();
    }

    private void createAccount(HashMap<String, Object> params) {

        repository.createAccount(params, new DataSource.GetResponseCallback<GetAccountResponse>() {
            @Override
            public void onSuccess(GetAccountResponse response) {
                hideProgress();
                if(response.isSuccess()) {
                    PreferenceUtil.getInstance(MainActivity.this).saveAccount(response.getUser());
                    loadHomeFragment();
                }  else{
                    if(response.getResponseMsg().toLowerCase().contains("mobile")){
                        params.remove("mobile_number");
                        phoneNumberExist(params);
                    }
                }
            }

            @Override
            public void onFailed(int code, String message) {
                count++;
                hideProgress();
                if (count < 3)
                    createAccount(params);
            }
        });
    }

    public void phoneNumberExist(HashMap<String, Object> params) {
        getMobileNumberFromUser(params,true);
    }

    private void signIn() {
        Intent signInIntent = googleSignInClient().getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        hideProgress();
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                getProfileInformation(account);
            } catch (ApiException e) {
                Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
                Log.w("TAG", "signInResult:failed code=" + e.getMessage());
            }
        }
    }


    public void onNavigationItemClick(View view) {
        drawerLayout.closeDrawers();
        switch (view.getId()) {
            case R.id.nav_view_logout:
                onLogoutEvent(null);
                break;
            case R.id.nav_view_home:
                loadHomeFragment();
                break;
            case R.id.nav_view_invite:
                InviteFragment fragment = InviteFragment.newInstance();
                InvitePresenter presenter = new InvitePresenter(fragment, Injection.provideRepository(getContext()), PreferenceUtil.getInstance(getContext()).getAccount());
                replaceFragment(fragment);
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(GoogleLoginEvent loginEvent) {
        signIn();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLogoutEvent(LogoutEvent event) {
        googleSignInClient().signOut();
        PreferenceUtil.getInstance(this).clearAllPreferences();
        user=null;
        deleteFCMToken();
        loadWelcomeFragment();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onActionBar(MainUIActionEvent event) {
        toolbar.setVisibility(event.getShowActionBar());
    }

    @Override
    public void onPermissionsGranted(String permission) {


    }

    @Override
    public void onPermissionsGranted() {
        if (user == null || user.getId()==null) {
            String photoUri = account.getPhotoUrl()!=null?account.getPhotoUrl().toString():null;
            HashMap<String, Object> params = new HashMap<>();
            params.put("email", account.getEmail());
            params.put("password", account.getEmail());
            params.put("first_name", account.getGivenName());
            params.put("last_name", account.getFamilyName());
            params.put("user_type", Constant.KIDS_HELPER); // 2 means kids Helper.
            if(photoUri!=null)
            params.put("image_link",  photoUri);
            params.put("fcm_key", PreferenceUtil.getInstance(this).getPreference(PREF_NOTIFICATION_TOKEN));
            extractPhoneNumber(params);
        }
        else{
            hideProgress();
            loadHomeFragment();
        }
    }


    @SuppressLint("MissingPermission")
    private void extractPhoneNumber(HashMap<String,Object> params){
        String mPhoneNumber=null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<SubscriptionInfo> subscription = SubscriptionManager.from(getApplicationContext()).getActiveSubscriptionInfoList();
            if(subscription==null || subscription.isEmpty()){
                getMobileNumberFromUser(params,false);
                return;
            }
            for (int i = 0; i < subscription.size(); i++) {
                SubscriptionInfo info = subscription.get(i);
                Log.d(TAG, "number " + info.getNumber());
                Log.d(TAG, "network name : " + info.getCarrierName());
                Log.d(TAG, "country iso " + info.getCountryIso());
                mPhoneNumber = info.getNumber();
                break;
            }
        }else {
            TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            mPhoneNumber = tMgr.getLine1Number();
            if (mPhoneNumber == null || mPhoneNumber.isEmpty()) {
                getMobileNumberFromUser(params,false);
                return;
            }
        }
        params.put("mobile_number", mPhoneNumber);
        createAccount(params);
    }


    private void getMobileNumberFromUser(HashMap<String,Object> params, boolean phoneExist) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View mView = layoutInflaterAndroid.inflate(R.layout.user_input_dialog_box, null);
        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this);
        alertDialogBuilderUserInput.setView(mView);
        TextView title = mView.findViewById(R.id.dialogTitle);
        title.setText(!phoneExist?R.string.enter_mobile_number:R.string.enter_other_mobile_number);
        final EditText userInputDialogEditText = mView.findViewById(R.id.userInputDialog);
        userInputDialogEditText.setInputType(InputType.TYPE_CLASS_PHONE);
        userInputDialogEditText.setHint("Mobile Number");
        alertDialogBuilderUserInput.setCancelable(false)
                .setPositiveButton("OK", (dialogBox, id) -> {
                    String phoneNumber = userInputDialogEditText.getText().toString();
                    if(Utils.isValidMobileNumber(phoneNumber)) {
                        params.put("mobile_number",phoneNumber);
                        createAccount(params);
                    }
                    else
                    {
                        Toast.makeText(getContext(), R.string.enter_mobile_number, Toast.LENGTH_SHORT).show();
                        getMobileNumberFromUser(params,phoneExist);

                    }

                });

        AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();

        alertDialogAndroid.show();
    }
    @Override
    public void onPermissionDenied() {
        Toast.makeText(this, "All permissions required!", Toast.LENGTH_SHORT).show();
        openSettings();
    }
}
