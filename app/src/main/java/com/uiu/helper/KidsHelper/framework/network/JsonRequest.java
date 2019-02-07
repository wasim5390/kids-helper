/*
 * Awais - Android Framework v1.0
 */
package com.uiu.helper.KidsHelper.framework.network;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.JsonObject;
import com.uiu.helper.KidsHelper.framework.utilities.JsonUtility;
import com.uiu.helper.KidsHelper.framework.utilities.StringUtility;

/**
 * Subclass of {@link com.android.volley.toolbox.JsonRequest} which serves as a
 * layer to aid in processing web service errors and some other stuff.
 *
 * @author Awais
 */
public class JsonRequest extends com.android.volley.toolbox.JsonRequest<NetworkResponse> {

    /**
     * Creates a new instance based on the provided parameters.
     *
     * @param method        The HTTP method to use.
     * @param url           The HTTP Url to hit.
     * @param requestBody   Optional request body to accompany the network call.
     * @param listener      Success listener.
     * @param errorListener Error listener.
     */
    public JsonRequest(int method, String url, String requestBody, Listener<NetworkResponse> listener, ErrorListener errorListener) {

        super(method, url, requestBody, listener, errorListener);
    }

    /**
     * Scrutinizes the server's response and classifies it as success/error
     * response based on the status code.
     *
     * @param response the response
     * @return the response
     */
    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {

        int statusCode = response.statusCode;

        if (statusCode == 400 || statusCode == 401 || statusCode == 403 || statusCode == 404 || statusCode == 405 || statusCode == 500) {
            return Response.error(new VolleyError(response));
        } else {
            return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
        }
    }

    /**
     * Override this method to return the json key used in the web service to
     * represent errors.
     *
     * @return JSON key which is used to represent errors in the Web Service
     * response.
     */
    protected String getJsonResponseErrorKey() {

        throw new UnsupportedOperationException(this.getClass().getName() + " >> You must override getJsonResponseErrorKey()");
    }

    /**
     * Parses error returned from the server based on the json key returned
     * from.
     *
     * @param volleyError the volley error
     * @return Error returned from web service based on the key returned from
     * {@code getJsonResponseErrorKey}. {@code getJsonResponseErrorKey}.
     */
    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {

        String jsonObjectErrorKey = getJsonResponseErrorKey();

        if (StringUtility.isEmptyOrNull(jsonObjectErrorKey))
            return volleyError;

        try {
            String errorJsonString = new String(volleyError.networkResponse.data);
            JsonObject errorJsonObject = JsonUtility.parseToJsonObject(errorJsonString);
            String errorString = "";

            if (errorJsonObject != null) {
                if (errorJsonObject.has(jsonObjectErrorKey)) {
                    JsonObject messageObj = (JsonObject) errorJsonObject.get(jsonObjectErrorKey);
                    errorString = JsonUtility.getString(messageObj, "message");
                }
            }
            return new VolleyError(errorString);
        } catch (Exception e) {
            return volleyError;
        }
    }

}
