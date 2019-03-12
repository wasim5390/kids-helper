package com.uiu.helper.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.uiu.helper.BuildConfig;
import com.uiu.helper.CompanionApp;

import com.uiu.helper.KidsHelper.mvp.model.User;
import com.uiu.helper.KidsHelper.mvp.ui.c_me.CmeeAudioActivity;
import com.uiu.helper.KidsHelper.mvp.ui.camera.CustomCameraActivity;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;
import com.uiu.helper.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by dors on 8/31/15.
 *
 */
public class Utils {
    public static final String DATE_FORMAT_1 = "MM/dd/yyyy hh:mm a";
    public static final String DATE_FORMAT_2 = "hh:mm a";

    private static Dialog dialog;
    private static  Activity activity;
    private static final String TAG = Utils.class.getSimpleName();

    public static String getAppVersion() {

        String appVersion = String.format("MyBazar %s %s", BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE);
        return appVersion;
    }
    public static String getDeviceInfo() {

        String deviceInfo = String.format("Android; Android OS %s", Build.VERSION.RELEASE);
        return deviceInfo;
    }
    public static String encodeBase64(String s) throws UnsupportedEncodingException {
        byte[] data = s.getBytes("UTF-8");
        String encoded = Base64.encodeToString(data, Base64.NO_WRAP);
        return encoded;
    }
    public static String getAuthorizationHeader() throws UnsupportedEncodingException {
        User account = PreferenceUtil.getInstance(CompanionApp.getContext()).getAccount();
        String username = account.getEmail();
        String password =  username;

        if (username!=null&&!username.isEmpty()&&password!=null&&!password.isEmpty()) {
            return encodeBase64(username+":"+password);
        } else {
            return null;
        }
    }
    public static boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
    /**
     * Show dialog in the top of the screen
     *
     * @param activity    the activity which raising the dialog
     * @param contentView view for the dialog
     */
    public static Dialog showHeaderDialog(Activity activity, View contentView) {
        Log.i(TAG, "showFooterDialog");
        Dialog dialog = new Dialog(activity, R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(contentView);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP;
        wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wmlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        return dialog;
    }
    public static void showWiserPLUSDirectionDialog(Activity getActivity){
        activity = getActivity;
        View dialogView = LayoutInflater.from(getActivity).inflate(R.layout.dialog_subscription, null);
        dialog = Utils.showHeaderDialog(getActivity,dialogView);

        ((RelativeLayout)dialog.findViewById(R.id.topFrame)).
                setBackgroundColor(getActivity.getResources().getColor(R.color.PrimaryColor));
        ((Button)dialog.findViewById(R.id.btnSubscribe)).setOnClickListener(subscription);
        ((Button)dialog.findViewById(R.id.btnCancel)).setOnClickListener(cancel);
    }
    private  static View.OnClickListener subscription =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();
           // activity.startActivity(new Intent(activity, SubscriptionActivity.class));
        }
    };
    private  static View.OnClickListener cancel =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();
        }
    };
    public static int dpToPx(float dp) {

        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int)(dp * (metrics.densityDpi / 160f));

    }

    public static float textSizeFromDp(Context context){
        float textSize =  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                context.getResources().getDimension(R.dimen.numpad_size), context.getResources().getDisplayMetrics());
        return textSize;
    }
    public static void showkeyPad(Context context,View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * Pass a view to method and it will hide the keyboard if opened.
     *
     * @param view
     */
    public static void hidekeyPad(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(),
                InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
    public static Bitmap addTransparentBorder(Bitmap bmp, int borderSize) {
        Bitmap bmpWithBorder = Bitmap.createBitmap(bmp.getWidth() + borderSize * 2, bmp.getHeight() + borderSize * 2, bmp.getConfig());
        Canvas canvas = new Canvas(bmpWithBorder);
        canvas.drawColor(Color.TRANSPARENT);
        canvas.drawBitmap(bmp, borderSize, borderSize, null);
        return bmpWithBorder;
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        encoded = "data:image/png;base64,"+encoded;
        return encoded;
    }
    public static String audioFileToBase64(File file) {
        byte[] byteArray = new byte[(int) file.length()];
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        encoded = "data:audio/mp3;base64,"+encoded;
        return encoded;
    }

    public static String saveImageToFile(Bitmap bmp, String appPkgName){
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/Companion_images";
        File dir = new File(file_path);
        if(!dir.exists())
            dir.mkdirs();
        File file = new File(dir, appPkgName + ".png");
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            Crashlytics.logException(e);
            e.printStackTrace();
        }

        bmp.compress(Bitmap.CompressFormat.PNG, 85, fOut);
        try {
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();Crashlytics.logException(e);
        } catch (NullPointerException e){Crashlytics.logException(e);}

        return  file.getAbsolutePath();

    }

    public static String createAudioFile()
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String suffix = "AUDIO_" + timeStamp;
        String filepath = Environment.getExternalStorageDirectory().getPath()+"/KidsHelper/Audio";
        File file = new File(filepath);

        if(!file.exists()){
            file.mkdirs();
        }

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path = file.getAbsolutePath() + "/"+suffix+".mp3";

        Log.e("file",path);
        return (path);
    }

    public static Intent enterPlayStore()
    {
        String PLAY_STORE_PACKAGE = "com.android.vending";
        String PLAY_STORE_ACTIVITY = PLAY_STORE_PACKAGE + ".AssetBrowserActivity";
        Intent launchIntent = new Intent(Intent.ACTION_MAIN);
        launchIntent.setClassName(PLAY_STORE_PACKAGE, PLAY_STORE_ACTIVITY);
        return launchIntent;
    }
    public static Bitmap getBitmapFromFile(String photoPath){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return BitmapFactory.decodeFile(photoPath, options);

    }

    /**
     * Show the user a dialog where he can resend an invitation, or remove the contact from the list
     */
    public static void showNoInternetConnectionDialog(Activity getActivity,int color){
        View dialogView = LayoutInflater.from(getActivity).inflate(R.layout.no_internet_connection_dialog, null);
        final Dialog dialog = Utils.showHeaderDialog(getActivity, dialogView);
        dialogView.findViewById(R.id.no_internet_connection_button_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.topFrame).setBackgroundColor(color);
        dialogView.findViewById(R.id.no_internet_connection_button_cancel).setBackgroundColor(color);
        dialog.show();
    }
    public static void showAlert(Activity getActivity,String message,int color){
        LayoutInflater inflater = getActivity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_empty_field_check, null);
        final Dialog dialog = Utils.showHeaderDialog(getActivity,dialogView);
        ((TextView) dialog.findViewById(R.id.dialog_invitation_actions_button_resend))
                .setText(message);
        ((FrameLayout)dialog.findViewById(R.id.topFrame)).
                setBackgroundColor(color);
        ((Button) dialog.findViewById(R.id.no_internet_connection_button_cancel)).
                setBackgroundColor(color);
        ((Button) dialog.findViewById(R.id.no_internet_connection_button_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }
    public static void setImageURI(Uri uri, SimpleDraweeView mSimpleDraweeView){
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(500, 500))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(false)
                .build();
        mSimpleDraweeView.setController(controller);

        RoundingParams roundingParams = new RoundingParams();
        roundingParams.setRoundAsCircle(true);
        mSimpleDraweeView.getHierarchy().setRoundingParams(roundingParams);
        mSimpleDraweeView.getHierarchy().setPlaceholderImage(R.drawable.ic_loader);
        mSimpleDraweeView.invalidate();

        mSimpleDraweeView.setController(controller);
    }
    public static void setImageURINoCircle(Uri uri, SimpleDraweeView mSimpleDraweeView){
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(500, 500))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(false)
                .build();
        mSimpleDraweeView.setController(controller);

        mSimpleDraweeView.getHierarchy().setPlaceholderImage(R.drawable.ic_loader);
        mSimpleDraweeView.invalidate();

        mSimpleDraweeView.setController(controller);
    }
    public static void setImageURI(int imgRes, SimpleDraweeView mSimpleDraweeView){
        ImageRequest request = ImageRequestBuilder.newBuilderWithResourceId(imgRes)
                .setResizeOptions(new ResizeOptions(500, 500))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(false)
                .build();
        mSimpleDraweeView.setController(controller);

        RoundingParams roundingParams = new RoundingParams();
        roundingParams.setRoundAsCircle(true);
        mSimpleDraweeView.getHierarchy().setRoundingParams(roundingParams);
        mSimpleDraweeView.getHierarchy().setPlaceholderImage(R.drawable.ic_loader);
        mSimpleDraweeView.invalidate();

        mSimpleDraweeView.setController(controller);
    }
    public static void setImageURINoCircle(int imgRes, SimpleDraweeView mSimpleDraweeView){
        ImageRequest request = ImageRequestBuilder.newBuilderWithResourceId(imgRes)
                .setResizeOptions(new ResizeOptions(500, 500))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setAutoPlayAnimations(false)
                .build();
        mSimpleDraweeView.setController(controller);

        mSimpleDraweeView.getHierarchy().setPlaceholderImage(R.drawable.ic_loader);
        mSimpleDraweeView.invalidate();

        mSimpleDraweeView.setController(controller);
    }
    /**
     * Invite user by email
     */
    public static void inviteUser(final  Activity activity,final String receiverEmail, final String senderEmail,
                                  String receiverName, String senderName){

        final String shareBody = "<html lang=\"en\">\n" +
                "<head>\n" +
                "  <meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\" />\n" +
                "  <title>Wiser Helper</title>\n" +
                "  <style type=\"text/css\">\n" +
                "      @import url(https://fonts.googleapis.com/css?family=Lato);\n" +
                "      b a,\n" +
                "      span a{\n" +
                "          color: #566277 !important;\n" +
                "          text-decoration: none;\n" +
                "      }\n" +
                "  </style>\n" +
                "</head>\n" +
                "<body style=\"margin:0; padding:0; text-align:left;\">\n" +
                "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#f5f6f9\">\n" +
                "  <tbody>\n" +
                "  <tr>\n" +
                "    <td class=\"wrapper\" style=\"padding:53px 10px 35px;\">\n" +
                "      <table class=\"flexible\" width=\"600\"  cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"margin:0 auto !important; border-radius:3px; background:#FEFEFE;\">\n" +
                "        <tbody>\n" +
                "        <tr>\n" +
                "          <td>\n" +
                "            <table>\n" +
                "              <tbody>\n" +
                "              <tr>\n" +
                "                <td style=\"padding:50px 50px 50px;\">\n" +
                "                  <table>\n" +
                "                    <tbody>\n" +
                "                    <tr>\n" +
                "                      <td style=\"padding-bottom:40px;\">\n" +
                "                        <img src=\"https://parsefiles.back4app.com/lGpiIUCEzmh4ujitQSZiiAHD4X58zD7iFCgGgNoD/7642d51526caddced29c224c9b92dc2e_image.jpg\" width=\"100% \"  alt=\"image\">\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                      <td style=\"color:#566277; text-align:left; font-size:20px; letter-spacing:0.5px; font-family: 'Lato', sans-serif; margin:0 0 0.01em!important; padding-bottom:20px;\">\n" +
                "                        <b style=\"color:#566277;\">"+activity.getString(R.string.email_hello)+" "+receiverName+",</b>\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                      <td style=\"color:#797E88; text-align:left; font-size:15px; letter-spacing:0.5px; font-family: 'Lato', sans-serif; margin:0 !important; padding:0 0 30px;\">\n" +
                "                       "+senderName+" "+activity.getString(R.string.email_wants_help)+" \n" +
                "                      </td>\n" +
                "                    </tr>\n" +

                "                    <tr>\n" +
                "                      <td style=\"color:#797E88; font-size:15px; text-align:left; letter-spacing:0.5px; font-family: 'Lato', sans-serif; margin:0 !important; padding:0 0 20px;\">\n" +
                "                        "+senderName+" "+activity.getString(R.string.email_is_using)+" "+senderName+" "+activity.getString(R.string.email_can_see)+"\n" +
                "                      </td>\n" +
                "                    </tr>\n" +

                "                    <tr>\n" +
                "                      <td style=\"color:#797E88; font-size:15px; text-align:left; letter-spacing:0.5px; font-family: 'Lato', sans-serif; margin:0 !important; padding:0 0 20px;\">\n" +
                "                        "+activity.getString(R.string.email_imagine_your)+" "+senderName+activity.getString(R.string.email_hands_on)+" "+senderName+" "+activity.getString(R.string.email_can_help)+"\n" +
                "                      </td>\n" +
                "                    </tr>\n" +

                "                    <tr>\n" +
                "                      <td style=\"color:#797E88; font-size:15px; text-align:left; letter-spacing:0.5px; font-family: 'Lato', sans-serif; margin:0 !important; padding:0 0 20px;\">\n" +
                "                        "+senderName+" "+activity.getString(R.string.email_can)+"\n" +
                "                      </td>\n" +
                "                    </tr>\n" +

                "                    <tr>\n" +
                "                      <td style=\"color:#797E88; font-size:15px; text-align:left; letter-spacing:0.5px; font-family: 'Lato', sans-serif; margin:0 !important; padding:0 0 3px;\">\n" +
                "                        &bull; "+activity.getString(R.string.email_add_contacts)+"\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                      <td style=\"color:#797E88; font-size:15px; text-align:left; letter-spacing:0.5px; font-family: 'Lato', sans-serif; margin:0 !important; padding:0 0 3px;\">\n" +
                "                       &bull; "+activity.getString(R.string.email_add_reminder)+"\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                      <td style=\"color:#797E88; font-size:15px; text-align:left; letter-spacing:0.5px; font-family: 'Lato', sans-serif; margin:0 !important; padding:0 0 3px;\">\n" +
                "                        &bull; "+activity.getString(R.string.email_add_directions)+"\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                      <td style=\"color:#797E88; font-size:15px; text-align:left; letter-spacing:0.5px; font-family: 'Lato', sans-serif; margin:0 !important; padding:0 0 3px;\">\n" +
                "                        &bull; "+activity.getString(R.string.email_add_sos)+"\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                      <td style=\"color:#797E88; font-size:15px; text-align:left; letter-spacing:0.5px; font-family: 'Lato', sans-serif; margin:0 !important; padding:0 0 20px;\">\n" +
                "                        &bull; "+activity.getString(R.string.email_much_more)+"\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                      <td style=\"color:#797E88; font-size:15px; text-align:left; letter-spacing:0.5px; font-family: 'Lato', sans-serif; margin:0 !important; padding:0 0 20px;\">\n" +
                "                        "+activity.getString(R.string.email_to_allow)+" "+senderName+" "+activity.getString(R.string.email_to_do_this)+"\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                      <td style=\"color:#797E88; font-size:15px; text-align:left; letter-spacing:0.5px; font-family: 'Lato', sans-serif; margin:0 !important; padding:0 0 20px;\">\n" +
                "                        "+activity.getString(R.string.email_please_call)+" "+senderName+" "+activity.getString(R.string.email_to_confirm)+"\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                      <td style=\"color:#797E88; font-size:15px; text-align:left; letter-spacing:0.5px; font-family: 'Lato', sans-serif; margin:0 !important; padding:15px 0 3px;\">\n" +
                "                        <small>"+activity.getString(R.string.email_follow_instructions)+"</small>\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                      <td style=\"color:##0000FF; font-size:15px; text-align:left; letter-spacing:0.5px; font-family: 'Lato', sans-serif; margin:0 !important; padding:0 0 10px;\">\n" +
                "                        "+activity.getString(R.string.email_wiser_link)+"\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                      <td style=\"color:#797E88; font-size:15px; text-align:left; letter-spacing:0.5px; font-family: 'Lato', sans-serif; margin:0 !important; padding:15px 0 3px;\">\n" +
                "                        <small>"+activity.getString(R.string.email_thanks)+"</small>\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                      <td style=\"color:#797E88; font-size:15px; text-align:left; letter-spacing:0.5px; font-family: 'Lato', sans-serif; margin:0 !important; padding:15px 0 3px;\">\n" +
                "                        <small>"+activity.getString(R.string.email_wiser_helper)+"</small>\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                    <tr>\n" +
                "                      <td style=\"color:#797E88; font-size:15px; text-align:left; letter-spacing:0.5px; font-family: 'Lato', sans-serif; margin:0 !important; padding: 0 3px;\">\n" +
                "                        <small>"+activity.getString(R.string.email_contact_email)+"</small>\n" +
                "                      </td>\n" +
                "                    </tr>\n" +
                "                    </tbody>\n" +
                "                  </table>\n" +
                "                </td>\n" +
                "              </tr>\n" +
                "              </tbody>\n" +
                "            </table>\n" +
                "          </td>\n" +
                "        </tr>\n" +
                "\n" +
                "        </tbody>\n" +
                "      </table>\n" +
                "    </td>\n" +
                "  </tr>\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";
//
//        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(activity);
//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                try {
//                    GMailSender sender = new GMailSender(sp.getString(Constants.INVITE_EMAIL,null),
//                            sp.getString(Constants.INVITE_PASSWORD,null));
//                    sender.sendMail(senderEmail+" "+R.string.email_invites_you, shareBody,
//                            senderEmail, receiverEmail);
//                } catch (Exception e) {
//                    Log.e("SendMail", e.getMessage(), e);
//                }
//            }
//
//        }).start();
    }
    /**
     * Check  internet connection
     */
    public static boolean isNetworkAvailable(Context context) {
        if(context!=null) {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        Log.w("INTERNET:", String.valueOf(i));
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            Log.w("INTERNET:", "connected!");
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    public static boolean currentVersionSupportBigNotification() {
        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        return sdkVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN;
    }


    public static boolean isSystemPackage(ResolveInfo resolveInfo) {
        return ((resolveInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    public static String formatDate(String dateInMilli) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT_1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(dateInMilli));
        return simpleDateFormat.format(calendar.getTime());
    }

         /* Function to convert milliseconds time to
     * Timer Format
     * Hours:Minutes:Seconds
     * */
    public static String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int)( milliseconds / (1000*60*60));
        int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
        // Add hours if there
        if(hours > 0){
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if(seconds < 10){
            secondsString = "0" + seconds;
        }else{
            secondsString = "" + seconds;}

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }


    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
    public static boolean isValidMobileNumber(String phone) {
        if (TextUtils.isEmpty(phone)|| phone.length()<8)
            return false;
      /*  final PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(phone, Locale.getDefault().getCountry());
           return phoneNumberUtil.isValidNumber(phoneNumber);
        } catch (final Exception e) {
        }*/
        return true;
    }

    public static void saveOnGallery(Context context, File image) {

        /*Intent galleryIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri picUri = Uri.fromFile(image);
        galleryIntent.setData(picUri);
        context.sendBroadcast(galleryIntent);*/


        MediaScannerConnection.scanFile(context,
                new String[]{image.getAbsolutePath()}, null, null);
    }

    public static File createAudioFile(Context context) {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "AUDIO_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
        File file = null;
        try {
            file = File.createTempFile(
                    "audio",  /* prefix */
                    ".mp3",         /* suffix */
                    storageDir      /* directory */
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static File createVideoFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "VIDEO_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DCIM);
        File file = File.createTempFile(
                "video",  /* prefix */
                ".mp4",         /* suffix */
                storageDir      /* directory */
        );
        return file;
    }

    /**
     * copy stream upto 3 mb
     * @param input
     * @param output
     * @throws IOException
     */
    public static void copyStream(InputStream input, OutputStream output) throws IOException {
        byte[] buffer = new byte[3072];
        int bytesRead;
        while ((bytesRead = input.read(buffer)) != -1) {
            output.write(buffer, 0, bytesRead);
        }
    }

    public static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }
}
