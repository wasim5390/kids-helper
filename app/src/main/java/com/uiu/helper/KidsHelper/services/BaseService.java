package com.uiu.helper.KidsHelper.services;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.uiu.helper.CompanionApp;
import com.uiu.helper.KidsHelper.constants.ServiceConstants;
import com.uiu.helper.KidsHelper.entities.BaseEntity;
import com.uiu.helper.KidsHelper.framework.models.BaseWebServiceModel;
import com.uiu.helper.KidsHelper.framework.network.CustomHttpResponse;
import com.uiu.helper.KidsHelper.framework.network.VolleyManager;
import com.uiu.helper.KidsHelper.framework.utilities.DeviceUtility;
import com.uiu.helper.KidsHelper.framework.utilities.JsonUtility;
import com.uiu.helper.KidsHelper.framework.utilities.StringUtility;
import com.uiu.helper.KidsHelper.network.ApplicationJsonDeleteRequest;
import com.uiu.helper.KidsHelper.network.ApplicationJsonGetRequest;
import com.uiu.helper.KidsHelper.network.ApplicationJsonPostRequest;
import com.uiu.helper.KidsHelper.network.ApplicationJsonPutRequest;
import com.uiu.helper.KidsHelper.services.response.BaseResponse;
import com.uiu.helper.R;

public abstract class BaseService extends BaseWebServiceModel {

    public Context mContext;
    private String baseUrl;

    public BaseService(Context context) {

        mContext = context;
        baseUrl = ServiceConstants.SERVICE_BASE_URL;
    }

    private VolleyError extractError(NetworkResponse response) {

        String responseString = new String(response.data);
        JsonObject jsonObject;
        try {
            JsonObject successJsonObject = JsonUtility.parseToJsonObject(responseString);
            jsonObject = JsonUtility.getJsonObject(successJsonObject, "meta");
        } catch (Exception e) {
            return new VolleyError(getContext().getString(R.string.unknown_error));
        }

        String message = "";

        if (jsonObject == null) {
            return null;
        }
        if (!jsonObject.has("message")) {
            return null;
        }

        if (jsonObject.has("message")) {
            JsonArray jsonArray = JsonUtility.getJsonArray(jsonObject, "message");
            for (JsonElement jsonElement : jsonArray) {

                try {
                    message += jsonElement.getAsString() + "\n";
                } catch (Exception e) {
                    message = null;
                }
            }
        }

        if (StringUtility.isEmptyOrNull(message))
            message = getContext().getString(R.string.unknown_error);

        VolleyError volleyError = new VolleyError(message);
        volleyError.setStatusCode(response.statusCode);
        return volleyError;
    }

    final protected <T> CustomHttpResponse getResponse(NetworkResponse networkResponse, BaseResponse<T> output) {

        String responseString = new String(networkResponse.data);
        VolleyError error = extractError(networkResponse);
        if (error != null)
            return getResponse(error);

        output.set(responseString);
        CustomHttpResponse customResponse = new CustomHttpResponse(output, null, networkResponse.statusCode);
        return customResponse;
    }

    final protected <T> CustomHttpResponse getResponse(NetworkResponse networkResponse, BaseEntity output) {

        String responseString = new String(networkResponse.data);
        VolleyError error = extractError(networkResponse);
        if (error != null)
            return getResponse(error);

        output.set(responseString);
        CustomHttpResponse customResponse = new CustomHttpResponse(output, null, networkResponse.statusCode);
        return customResponse;
    }

    final protected CustomHttpResponse getResponse(VolleyError error) {

        if (error != null && error.networkResponse != null)
            error = extractError(error.networkResponse);

        String errorMessage = error.getMessage();

        if (errorMessage == null)
            errorMessage = "There was a problem connecting to server";
        CustomHttpResponse customHttpError = new CustomHttpResponse(errorMessage, null, error.getStatusCode());
        return customHttpError;
    }

    final protected String getServiceURL(String method) {

        return baseUrl + method;
    }

    @Override
    protected Context getContext() {

        return CompanionApp.getInstance();
    }

    @Override
    protected boolean shouldShowInternetNotWorkingView() {

        return true;
    }

    @Override
    protected void executeRequest(Request<?> request) {

        if (DeviceUtility.isInternetConnectionAvailable(CompanionApp.getInstance())) {
            VolleyManager.getInstance().getRequestQueue().add(request);
        } else {

        }
    }

    protected <T> void get(String url,  final IService listener, final BaseEntity response) {

        ApplicationJsonGetRequest request = new ApplicationJsonGetRequest(url,  getSuccessListener(listener, response), getErrorListener(listener));
        executeRequest(request);
    }

    protected <T> void get(String url,  final IService listener, final BaseResponse<T> response) {

        ApplicationJsonGetRequest request = new ApplicationJsonGetRequest(url,  getSuccessListener(listener, response), getErrorListener(listener));
        executeRequest(request);
    }

    protected <T> void post(String url, String requestBody, IService listener, BaseResponse<T> response) {

        ApplicationJsonPostRequest request = new ApplicationJsonPostRequest(url, requestBody, getSuccessListener(listener, response), getErrorListener(listener));
        executeRequest(request);
    }

    protected <T> void post(String url, String requestBody, IService listener, BaseEntity response) {

        ApplicationJsonPostRequest request = new ApplicationJsonPostRequest(url, requestBody, getSuccessListener(listener, response), getErrorListener(listener));
        executeRequest(request);
    }

    protected <T> void put(String url,  String requestBody, IService listener, BaseResponse<T> response) {

        ApplicationJsonPutRequest request = new ApplicationJsonPutRequest(url,  requestBody, getSuccessListener(listener, response), getErrorListener(listener));
        executeRequest(request);
    }

    protected <T> void put(String url,  String requestBody, IService listener, BaseEntity response) {

        ApplicationJsonPutRequest request = new ApplicationJsonPutRequest(url,  requestBody, getSuccessListener(listener, response), getErrorListener(listener));
        executeRequest(request);
    }

    protected <T> void delete (String url,  IService listener, final BaseEntity response) {

        ApplicationJsonDeleteRequest request = new ApplicationJsonDeleteRequest (url,  "", getSuccessListener (listener, response), getErrorListener (listener));
        executeRequest (request);
    }

    protected <T> void delete (String url,  IService listener, final BaseResponse response) {

        ApplicationJsonDeleteRequest request = new ApplicationJsonDeleteRequest(url,  "", getSuccessListener (listener, response), getErrorListener (listener));
        executeRequest (request);
    }

    protected <T> void delete (String url,  String requestBody, IService listener, final BaseResponse response) {

        ApplicationJsonDeleteRequest request = new ApplicationJsonDeleteRequest (url,  requestBody, getSuccessListener (listener, response), getErrorListener (listener));
        executeRequest (request);
    }

    private <T> Response.Listener<NetworkResponse> getSuccessListener(final IService listener, final BaseEntity response) {

        return new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse arg0) {

                if (listener == null)
                    return;

                CustomHttpResponse customResponse = getResponse(arg0, response);
                if (customResponse.getStatusCode() == 200)
                    listener.onSuccess(customResponse);
                else
                    listener.onFailure(customResponse);
            }
        };
    }

    private <T> Response.Listener<NetworkResponse> getSuccessListener(final IService listener, final BaseResponse<T> response) {

        return new Response.Listener<NetworkResponse>() {

            @Override
            public void onResponse(NetworkResponse arg0) {

                if (listener == null)
                    return;

                CustomHttpResponse customResponse = getResponse(arg0, response);
                if (customResponse.getStatusCode() == 200)
                    listener.onSuccess(customResponse);
                else
                    listener.onFailure(customResponse);
            }
        };
    }

    private Response.ErrorListener getErrorListener(final IService listener) {

        return new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError arg0) {

                if (listener == null)
                    return;
                listener.onFailure(getResponse(arg0));
            }
        };
    }


}
