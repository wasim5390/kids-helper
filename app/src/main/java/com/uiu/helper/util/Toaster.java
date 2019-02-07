package com.uiu.helper.util;

import android.content.Context;
import android.widget.Toast;

import com.uiu.helper.BuildConfig;


/**
 * Created by dors on 9/1/15.
 */
public class Toaster {

    /**
     * Calls {@link #toast(Context, int, int, boolean)} with hideInProduction = true
     */
    public static void toast(Context context, int message, int length){
        toast(context, message, length, true);

    }

    /**
     * Calls {@link #toast(Context, int, int, boolean)} with hideInProduction = true
     */
    public static void toast(Context context, String message, int length){
        toast(context, message, length, true);
    }

    /**
     * Simply show a toast
     * @param context the context
     * @param message the message id
     * @param length the length LONG/SHORT
     * @param showInProduction true if should be shown in production version also, otherwise false
     */
    public static void toast(Context context, int message, int length, boolean showInProduction){

        if (context == null || (!BuildConfig.DEBUG && !showInProduction)){
            return;
        }

        Toast.makeText(context, message, length).show();
    }

    /**
     * Simply show a toast
     * @param context the context
     * @param message the message text
     * @param length the length LONG/SHORT
     * @param showInProduction true if should be shown in production version also, otherwise false
     */
    public static void toast(Context context, String message, int length, boolean showInProduction){

        if (context == null || (!BuildConfig.DEBUG && !showInProduction)){
            return;
        }

        Toast.makeText(context, message, length).show();
    }
}
