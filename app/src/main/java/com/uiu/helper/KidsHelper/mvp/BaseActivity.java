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
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.iid.FirebaseInstanceId;

import com.squareup.picasso.Picasso;
import com.uiu.helper.KidsHelper.mvp.events.ShareEvent;
import com.uiu.helper.KidsHelper.mvp.model.Invitation;
import com.uiu.helper.KidsHelper.mvp.model.User;
import com.uiu.helper.KidsHelper.mvp.ui.ConfirmationCallback;
import com.uiu.helper.KidsHelper.mvp.ui.InvitationConfirmationCallback;
import com.uiu.helper.KidsHelper.mvp.ui.WelcomeFragment;
import com.uiu.helper.KidsHelper.mvp.ui.home.HomeListFragment;
import com.uiu.helper.KidsHelper.mvp.ui.home.HomeListPresenter;
import com.uiu.helper.KidsHelper.mvp.ui.invite.InviteFragment;
import com.uiu.helper.KidsHelper.mvp.ui.media_notification.AudioPlayBackView;
import com.uiu.helper.KidsHelper.mvp.ui.media_notification.UploadFileService;
import com.uiu.helper.KidsHelper.mvp.ui.media_notification.VideoPlaybackActivity;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;
import com.uiu.helper.R;

import com.uiu.helper.util.Utils;

import java.io.IOException;


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
        registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(onDownloadComplete);
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


    protected void updateBottomNavigationViewIconSize(BottomNavigationView bottomNavigationView){
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }
    }

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


    public void showMediaNotification( ShareEvent shareEvent){
        int type = shareEvent.getMediaType();

        int notificationHeader = R.drawable.notification_header_bg;
        int btnColor = R.color.notification_accepted;
        Dialog dialog = new Dialog(this,R.style.Theme_Dialog);
        dialog.setContentView(R.layout.notification_view_media);
        dialog.setCancelable(false);
        dialog.findViewById(R.id.header).setBackgroundResource(notificationHeader);
        if(type==Constant.MEDIA_IMAGE) {
            btnColor = R.color.notification_accepted;
            dialog.findViewById(R.id.image).setVisibility(View.VISIBLE);
            dialog.findViewById(R.id.btnDownloadVideo).setVisibility(View.GONE);
            dialog.findViewById(R.id.btnDownloadAudio).setVisibility(View.GONE);
        }
        else if(type == Constant.MEDIA_AUDIO) {
            dialog.findViewById(R.id.image).setVisibility(View.GONE);
            dialog.findViewById(R.id.btnDownloadVideo).setVisibility(View.GONE);
            dialog.findViewById(R.id.btnDownloadAudio).setVisibility(View.VISIBLE);

        }else if(type == Constant.MEDIA_VIDEO){
            dialog.findViewById(R.id.image).setVisibility(View.VISIBLE);
            dialog.findViewById(R.id.btnDownloadVideo).setVisibility(View.VISIBLE);
            dialog.findViewById(R.id.btnDownloadAudio).setVisibility(View.GONE);
        }

        TextView tvMessage;
        SimpleDraweeView ivSender;
        ImageView image;
        Button okBtn;
        ivSender =  dialog.findViewById(R.id.ivSender);
        tvMessage =  dialog.findViewById(R.id.tvNotificationTitle);
        image = dialog.findViewById(R.id.image);
        okBtn =  dialog.findViewById(R.id.btnOk);

        okBtn.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),btnColor));
        tvMessage.setText(shareEvent.getTitle());
        ivSender.setImageURI(shareEvent.getSender().getSenderImage());
       // Picasso.with(getApplicationContext()).load(shareEvent.getSender().getSenderImage()).placeholder(R.drawable.avatar_male2).into(ivSender);
        if(type==MEDIA_IMAGE)
            Picasso.with(getApplicationContext()).load(shareEvent.getFileUrl()).placeholder(R.drawable.placeholder_sqr).into(image);
        if(type==MEDIA_VIDEO) {
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.with(getApplicationContext()).load(shareEvent.getThumbnailUrl()).placeholder(R.drawable.placeholder_sqr).into(image);
            okBtn.setText("Download");
        }
        if(type==MEDIA_AUDIO) {
            okBtn.setText("Download");
        }

        dialog.show();

        okBtn.setOnClickListener(v -> {
            if(type==MEDIA_AUDIO | type==MEDIA_VIDEO | type==MEDIA_IMAGE)
                UploadFileService.downloadMedia(getApplicationContext(), type, shareEvent.getFileUrl(), shareEvent.getCreatedAt());
            dialog.dismiss();
        });


    }




    public void showAudioNotification(String uri){

        Dialog dialog = new Dialog(this,R.style.Theme_Dialog);
        LayoutInflater inflater = getLayoutInflater();
        AudioPlayBackView view = (AudioPlayBackView) inflater.inflate(R.layout.audio_playback_view,null);
        view.setDataSource(Uri.parse(uri));
        dialog.setContentView(view);
        dialog.setCancelable(true);
        dialog.show();
        dialog.findViewById(R.id.btnCancel).setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.setOnDismissListener(dialog1 -> {
            view.stopMediaPlayer();
        });
    }
    BroadcastReceiver onDownloadComplete=new BroadcastReceiver() {
        public void onReceive(Context ctxt, Intent intent) {
            Bundle extras = intent.getExtras();
            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            DownloadManager.Query q = new DownloadManager.Query();
            q.setFilterById(extras.getLong(DownloadManager.EXTRA_DOWNLOAD_ID));
            Cursor c = manager.query(q);

            if (c.moveToFirst()) {
                int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    // process download
                    final String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    final String mimeStr = c.getString(c.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));

                    String type = getMimeType(Uri.parse(uriString).getPath());

                    Log.i("Download URI: ",uriString);
                    if(type!=null){
                        Log.i("Download MIME: ",type);
                        if(type.toLowerCase().startsWith("audio")){
                            showAudioNotification(uriString);
                        }

                        if(type.toLowerCase().startsWith("video")){
                            intent = new Intent(getApplicationContext(),VideoPlaybackActivity.class);
                            intent.putExtra("uri",uriString);
                            startActivity(intent);
                        }
                    }

                }
            }


        }
    };

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
