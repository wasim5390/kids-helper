package com.uiu.helper.KidsHelper.network;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

public class ApplicationJsonGetRequest extends ApplicationJsonRequest {

    public ApplicationJsonGetRequest(String url, Listener<NetworkResponse> listener, ErrorListener errorListener) {
        super(Request.Method.GET, url, null, listener, errorListener);
    }
}
