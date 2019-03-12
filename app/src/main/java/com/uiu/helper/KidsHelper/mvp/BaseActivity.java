package com.uiu.helper.KidsHelper.mvp;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;

import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;

import com.squareup.picasso.Picasso;
import com.uiu.helper.KidsHelper.mvp.events.ShareEvent;
import com.uiu.helper.KidsHelper.mvp.model.Invitation;
import com.uiu.helper.KidsHelper.mvp.model.User;
import com.uiu.helper.KidsHelper.mvp.ui.ConfirmationCallback;
import com.uiu.helper.KidsHelper.mvp.ui.InvitationConfirmationCallback;
import com.uiu.helper.KidsHelper.mvp.ui.WelcomeFragment;
import com.uiu.helper.KidsHelper.mvp.ui.c_me.AudioPlayBackView;
import com.uiu.helper.KidsHelper.mvp.ui.c_me.VideoPlaybackActivity;
import com.uiu.helper.KidsHelper.mvp.ui.home.HomeListFragment;
import com.uiu.helper.KidsHelper.mvp.ui.home.HomeListPresenter;
import com.uiu.helper.KidsHelper.mvp.ui.invite.InviteFragment;
import com.uiu.helper.KidsHelper.mvp.ui.media_notification.UploadFileService;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;
import com.uiu.helper.R;

import com.uiu.helper.util.Utils;

import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


/**
 * Created by sidhu on 4/12/2018.
 */

public abstract class BaseActivity extends AppCompatActivity implements Constant{

    public abstract int getID();

    public abstract void created(Bundle savedInstanceState);

    private ProgressFragmentDialog pd;
    protected GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getID());
        created(savedInstanceState);
    }


    @Override
    protected void onStart() {

        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    public void setToolBar(Toolbar toolbar, CharSequence title, boolean showToolbar) {
        setSupportActionBar(toolbar);
        showToolbar(showToolbar);
        if(showToolbar)
        {
            setToolBarTitle(toolbar,title);
        }
    }

    public void setToolBarTitle(Toolbar toolbar, CharSequence title){
        TextView tvTitle = toolbar.findViewById(R.id.toolbar_title);
        tvTitle.setText(title);
    }
    public void makeFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public void showToolbar(boolean show){
        if(getSupportActionBar()!=null){
            if(show)
                getSupportActionBar().show();
            else
                getSupportActionBar().hide();
        }
    }

    public GoogleSignInClient googleSignInClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        return mGoogleSignInClient;
    }


    // navigating user to app settings
    public void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    /**** show progress *******************/

    public void showProgress(){
        try {

            if (pd == null) {
                pd = ProgressFragmentDialog.newInstance();
            }
            if (!pd.isAdded())
                pd.show(getSupportFragmentManager(), "TAG");
            //  pd.getView().findViewById(R.id.loadingText);
        }catch(IllegalStateException e){
            Log.e("BaseActivity", e.getMessage());
        }
    }

    /******************* hide progress ***********************/

    public void hideProgress() {
        try {
            if (pd != null) {
                pd.dismiss();
            }
        }catch (IllegalStateException e){
            Log.e("BaseActivity",e.getMessage());
        }

    }


/*    protected void updateBottomNavigationViewIconSize(BottomNavigationView bottomNavigationView){
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }
    }*/

    public void showConfirmationDialog(String title, String body, String slideId, String itemId, ConfirmationCallback confirmationCallback){

        final YesNoDialog emailDialog = new YesNoDialog(this, title, body, YesNoDialog.ALERT_DIALOG);
        emailDialog.setPositiveButtonListener(v -> {
            confirmationCallback.onDeleteConfirmClick(slideId,itemId);
            emailDialog.dismiss();
        });

        emailDialog.setNegativeButtonListener(v -> {
            emailDialog.dismiss();
        });

        emailDialog.show();
    }
    /**
     * Show the user a dialog where he can resend an invitation, or remove the contact from the list
     */
    public void showInvitationActionsDialog(Context context, Invitation invitation, InvitationConfirmationCallback callback){
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_invitation_receiver, null);
        Button resend = dialogView.findViewById(R.id.dialog_invitation_actions_button_resend);
        View switchRole = dialogView.findViewById(R.id.switchRoleView);
        if(invitation.getStatus()==INVITE.CONNECTED)
            resend.setText(getString(R.string.disconnect_kid));
        else
            resend.setText(getString(R.string.invitation_actions_resend));
        final Dialog dialog = Utils.showHeaderDialog(this, dialogView);
        switchRole.setVisibility(invitation.isPrimary()?View.VISIBLE:View.GONE);
        dialogView.findViewById(R.id.remove_from_list_view)
                .setVisibility(invitation.getStatus()!= INVITE.CONNECTED?View.VISIBLE:View.GONE);
        dialogView.findViewById(R.id.dialog_invitation_actions_button_resend).setOnClickListener(v -> {
            dialog.dismiss();
            if(invitation.getStatus()==INVITE.CONNECTED)
                callback.onRejectInvitation(invitation);
                else
                callback.onResendInvitation(invitation);
        });

        dialogView.findViewById(R.id.dialog_invitation_actions_button_remove).setOnClickListener(v -> {
            dialog.dismiss();
            callback.onDeleteInvitation(invitation);

        });
        dialogView.findViewById(R.id.dialog_invitation_actions_button_switch).setOnClickListener(v -> {
            dialog.dismiss();
            callback.onInvitationSwitchClick(invitation);
        });

        dialogView.findViewById(R.id.dialog_invitation_actions_button_cancel).setOnClickListener(v -> dialog.dismiss());
    }

    public void loadWelcomeFragment(){
        Fragment welcomeFragment = WelcomeFragment.newInstance();
        replaceFragment(welcomeFragment);
    }

    public void loadHomeFragment() {
        User entity = PreferenceUtil.getInstance(this).getAccount();
        HomeListFragment fragment = HomeListFragment.newInstance();
        HomeListPresenter presenter = new HomeListPresenter(fragment, Injection.provideRepository(this), entity);
        replaceFragment(fragment);
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(fragment instanceof WelcomeFragment)
            fragmentTransaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.fade_out_fast);
        if(fragment instanceof InviteFragment)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.main_container, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }


    @SuppressLint("StaticFieldLeak")
    public void deleteFCMToken() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    FirebaseInstanceId.getInstance().deleteInstanceId();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                //call your activity where you want to land after log out
            }
        }.execute();
    }




    public void showAudioNotification(String uri, Dialog dialog, LinearLayout container){

        LayoutInflater inflater = getLayoutInflater();
        AudioPlayBackView view = (AudioPlayBackView) inflater.inflate(R.layout.audio_playback_view,null);
        view.setDataSource(Uri.parse(uri),false);
        view.findViewById(R.id.btnCancel).setVisibility(View.GONE);
        container.removeAllViews();
        container.addView(view);

        dialog.setOnDismissListener(dialog1 -> {
            view.stopMediaPlayer();
        });
    }



    // url = file path or whatever suitable URL you want.
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

}
