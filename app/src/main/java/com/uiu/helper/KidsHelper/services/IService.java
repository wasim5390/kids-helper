package com.uiu.helper.KidsHelper.services;


import com.uiu.helper.KidsHelper.framework.network.CustomHttpResponse;

public interface IService {
    void onSuccess(CustomHttpResponse response);

    void onFailure(CustomHttpResponse response);
}
