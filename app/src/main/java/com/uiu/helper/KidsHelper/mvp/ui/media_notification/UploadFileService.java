package com.uiu.helper.KidsHelper.mvp.ui.media_notification;

import android.app.DownloadManager;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;


import com.uiu.helper.KidsHelper.mvp.BaseActivity;
import com.uiu.helper.KidsHelper.mvp.Constant;
import com.uiu.helper.KidsHelper.mvp.events.ShareEvent;
import com.uiu.helper.KidsHelper.mvp.model.response.BaseResponse;
import com.uiu.helper.KidsHelper.mvp.source.DataSource;
import com.uiu.helper.KidsHelper.mvp.source.Repository;
import com.uiu.helper.KidsHelper.mvp.util.PreferenceUtil;
import com.uiu.helper.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class UploadFileService extends IntentService implements Constant {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_UPLOAD_MEDIA = "com.uiu.helper.KidsHelper.mvp.ui.action.UPLOAD_MEDIA";
    private static final String ACTION_DOWNLOAD_MEDIA = "com.uiu.helper.KidsHelper.mvp.ui.action.DOWNLOAD_MEDIA";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM_FILE_PATH = "com.uiu.helper.KidsHelper.mvp.ui.extra.PARAM_FILE_PATH";
    private static final String EXTRA_PARAM_FILE_TYPE = "com.uiu.helper.KidsHelper.mvp.ui.extra.PARAM_FILE_TYPE";
    private static final String EXTRA_PARAM_FILE_NAME = "com.uiu.helper.KidsHelper.mvp.ui.extra.PARAM_FILE_NAME";
    private static final String EXTRA_PARAM_ID = "com.uiu.helper.KidsHelper.mvp.ui.extra.PARAM_ID";

    public UploadFileService() {
        super("UploadFileService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void uploadMedia(Context context, String filePath, int type, String userId) {
        Intent intent = new Intent(context, UploadFileService.class);

        intent.setAction(ACTION_UPLOAD_MEDIA);
        intent.putExtra(EXTRA_PARAM_FILE_PATH, filePath);
        intent.putExtra(EXTRA_PARAM_FILE_TYPE, type);
        intent.putExtra(EXTRA_PARAM_ID, userId);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void downloadMedia(Context context, int fileType, String path,String name) {
        Intent intent = new Intent(context, UploadFileService.class);
        intent.setAction(ACTION_DOWNLOAD_MEDIA);
        intent.putExtra(EXTRA_PARAM_FILE_TYPE, fileType);
        intent.putExtra(EXTRA_PARAM_FILE_PATH, path);
        intent.putExtra(EXTRA_PARAM_FILE_NAME, name);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPLOAD_MEDIA.equals(action)) {
                final String path = intent.getStringExtra(EXTRA_PARAM_FILE_PATH);
                final int type = intent.getIntExtra(EXTRA_PARAM_FILE_TYPE,4);
                final String contactId = intent.getStringExtra(EXTRA_PARAM_ID);
                handleUploadMedia(path, type,contactId);
            } else if (ACTION_DOWNLOAD_MEDIA.equals(action)) {
                // TODO { Need to implement other type of upload or download}
                final String path = intent.getStringExtra(EXTRA_PARAM_FILE_PATH);
                final int type = intent.getIntExtra(EXTRA_PARAM_FILE_TYPE,4);
                final String name = intent.getStringExtra(EXTRA_PARAM_FILE_NAME);
               // final String contactId = intent.getStringExtra(EXTRA_PARAM_ID);
                handleDownloadMedia(path,name, type);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleUploadMedia(String path, int type, String receiverId) {
        if(path==null)
            return;

        File file = new File(path);
        HashMap<String, RequestBody> params = new HashMap<>();
        RequestBody fBody = null;
        Log.e("receiverId",receiverId);
        if (type == MEDIA_VIDEO) {
            fBody = RequestBody.create(MediaType.parse("video/*"), file);
            Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(path, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
            String thumbString = Utils.bitmapToBase64(thumbnail);
            params.put("thumbnail", RequestBody.create(MediaType.parse("text/plain"), String.valueOf(thumbString)));
        } else if (type == MEDIA_AUDIO) {
            fBody = RequestBody.create(MediaType.parse("audio/*"), file);
        }else if(type == MEDIA_IMAGE)
            fBody = RequestBody.create(MediaType.parse("image/*"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), fBody);

        RequestBody mediaType = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(type));
        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(PreferenceUtil.getInstance(this).getAccount().getId()));
        RequestBody receiver_Id = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(receiverId));


        params.put("type", mediaType);
        params.put("user_id", user_id);
        params.put("receiver_id",receiver_Id);

        Repository.getInstance().shareMediaFile(params, body, new DataSource.GetDataCallback<BaseResponse>() {
            @Override
            public void onDataReceived(BaseResponse data) {
                if(data.isSuccess())
                    EventBus.getDefault().post(new ShareEvent(true,type));
              //  Toast.makeText(UploadFileService.this, "Uploaded!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(UploadFileService.this, data.getResponseMsg(), Toast.LENGTH_SHORT).show();
                UploadFileService.this.stopSelf();
            }

            @Override
            public void onFailed(int code, String message) {
                Toast.makeText(UploadFileService.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */
    private void handleDownloadMedia(String path, String name,int type) {
        String extension = BaseActivity.getMimeType(path);
        String subDirectory="Files";
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri Download_Uri = Uri.parse(path);

        DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle("Downloading...");

        request.setVisibleInDownloadsUi(true);
        if(type==Constant.MEDIA_AUDIO) {
            subDirectory = "Audio";
            name =name.concat(".mp3");
        }
        if(type == Constant.MEDIA_IMAGE) {
            subDirectory = "Images";
            String ex = extensionFromMime(extension);
            if(ex!=null)
               name= name.concat(ex);
            else
              name=  name.concat(".png");
        }
        if(type == Constant.MEDIA_VIDEO) {
            subDirectory = "Video";
            String ex = extensionFromMime(extension);
            if(ex!=null)
               name= name.concat(ex);
            else
              name=  name.concat(".mp4");
        }

        request.setDestinationInExternalPublicDir( "/KidsHelper/"+subDirectory,  name);
        downloadManager.enqueue(request);
        //refid = downloadManager.enqueue(request);
    }

    public String extensionFromMime(String mime){
        String[] ex = mime.split("/");
        if(ex.length<2)
            return null;
        String lastOne = ex[ex.length-1];
        return ".".concat(lastOne);
    }

}
