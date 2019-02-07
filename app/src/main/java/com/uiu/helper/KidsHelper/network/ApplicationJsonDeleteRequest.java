package com.uiu.helper.KidsHelper.network;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

public class ApplicationJsonDeleteRequest extends ApplicationJsonRequest {

	public ApplicationJsonDeleteRequest (String url, String requestBody, Listener<NetworkResponse> listener, ErrorListener errorListener) {

		super (Request.Method.DELETE, url, requestBody, listener, errorListener);
	}
}
