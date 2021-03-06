package com.uiu.helper.KidsHelper.mvp.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.widget.Toast;


import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

import androidx.core.content.ContextCompat;

/**
 * Created by sidhu on 6/8/2018.
 */

public class PermissionUtil {

    /**
     * Requesting location permission
     * This uses multiple permission model from dexter
     * Once the permission granted, acts as required
     * On permanent denial opens settings dialog
     */
    public static void requestPermissions(Activity activity,PermissionCallback permissionCallback) {
        Dexter.withActivity(activity)
                .withPermissions(
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {


                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            permissionCallback.onPermissionsGranted();
                            return;
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            permissionCallback.onPermissionDenied();
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(error -> Toast.makeText(activity, "Error occurred!", Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }

    public static void requestPermissions(Activity activity,String permissions[],PermissionCallback permissionCallback) {
        Dexter.withActivity(activity)
                .withPermissions(
                       permissions)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            permissionCallback.onPermissionsGranted();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            permissionCallback.onPermissionDenied();
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(error -> Toast.makeText(activity, "Error occurred!", Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }

    /**
     * Requesting location permission
     * This uses single permission model from dexter
     * Once the permission granted, acts as required
     * On permanent denial opens settings dialog
     */
    public static void requestPermission(Activity activity,String permission,PermissionCallback permissionCallback) {
        Dexter.withActivity(activity)
                .withPermission(permission)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        permissionCallback.onPermissionsGranted(response.getPermissionName());
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        permissionCallback.onPermissionDenied();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();

                    }
                }).withErrorListener(error -> Toast.makeText(activity, error.name(), Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }

    public static boolean isPermissionGranted(Context context,String permission){
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }


    public interface PermissionCallback{
        void onPermissionsGranted(String permission);
        void onPermissionsGranted();
        void onPermissionDenied();
    }
}
