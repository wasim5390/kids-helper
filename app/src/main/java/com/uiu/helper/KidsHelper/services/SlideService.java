package com.uiu.helper.KidsHelper.services;

import android.content.Context;

import com.uiu.helper.KidsHelper.constants.ServiceConstants;
import com.uiu.helper.KidsHelper.services.response.SlideResponse;
import com.uiu.helper.util.Utils;


public class SlideService extends BaseService {

    private Context context;

    public SlideService(Context context) {

        super(context);
        this.context = context;
    }


    public void updateStatus(String request, IService listener) {

        String methodURL = super.getServiceURL(ServiceConstants.SERVICE_UPDATE_OBJECT_STATUS);

        if (!Utils.isConnected(this.context)) {
            return;
        }

        super.post(methodURL, request, listener, new SlideResponse());
    }

}

