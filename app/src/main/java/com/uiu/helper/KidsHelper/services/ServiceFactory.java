package com.uiu.helper.KidsHelper.services;

import android.content.Context;


public class ServiceFactory {


    public SlideService slide;
    public KidConnectionService kidConnectionService;


    public ServiceFactory(Context context) {

        slide = new SlideService(context);
        kidConnectionService = new KidConnectionService(context);
    }
}
