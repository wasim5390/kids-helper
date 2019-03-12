package com.uiu.helper.KidsHelper.mvp;

import android.content.Context;


import com.uiu.helper.KidsHelper.mvp.source.Repository;

import androidx.annotation.NonNull;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created on 24/10/2017.
 */

public class Injection {

    public static Repository provideRepository(@NonNull Context context) {
        checkNotNull(context);
        return Repository.getInstance();
    }
}
