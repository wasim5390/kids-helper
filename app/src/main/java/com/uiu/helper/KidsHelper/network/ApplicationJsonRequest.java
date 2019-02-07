
package com.uiu.helper.KidsHelper.network;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.uiu.helper.KidsHelper.constants.ServiceConstants;
import com.uiu.helper.KidsHelper.framework.network.JsonRequest;
import com.uiu.helper.util.Utils;

import java.util.HashMap;
import java.util.Map;

public class ApplicationJsonRequest extends JsonRequest {

    static String REQUEST_TAG = "kids-api-request";
    String url;

    public ApplicationJsonRequest(int method, String url, String requestBody, Listener<NetworkResponse> listener, ErrorListener errorListener) {

        super(method, url, requestBody, listener, errorListener);

        int noOfRetries = 0;
        if(method == Request.Method.GET) {
            noOfRetries = 1;
        }

        this.setRetryPolicy(new DefaultRetryPolicy(ServiceConstants.SERVICE_TIME_OUT, noOfRetries, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        this.setShouldCache(false);
        this.url = url;
    }

    @Override
    public Object getTag() {

        return REQUEST_TAG;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Accept-Encoding", "gzip");
        headers.put("user-agent", String.format("%s (%s)", Utils.getAppVersion(), Utils.getDeviceInfo()));
        headers.put("Cache-Control", "no-cache, no-store, must-revalidate");

        return headers;
    }

    public static void setRequestTag(String tag) {

        REQUEST_TAG = tag;
    }

    @Override
    public Priority getPriority() {

        return Priority.IMMEDIATE;
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {

        REQUEST_TAG = "kids-api-request";
        return super.parseNetworkResponse(response);
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {

        // Check access token expiration error, and logout the user
//        if (isAuthenticationError(volleyError)) {
//            KidsApp.getInstance().onAuthenticationFailed();
//        }

        return super.parseNetworkError(volleyError);
    }

    private boolean isAuthenticationError(VolleyError volleyError) {

        return volleyError.getStatusCode() == 401 ? true : false;
    }

    @Override
    protected String getJsonResponseErrorKey() {

        return "meta";
    }
}