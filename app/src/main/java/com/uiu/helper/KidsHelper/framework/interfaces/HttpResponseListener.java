/*
 * Awais - Android Framework v1.0
 */
package com.uiu.helper.KidsHelper.framework.interfaces;


import com.uiu.helper.KidsHelper.framework.network.CustomHttpResponse;

/**
 * Interface definition for callbacks to be invoked in response to a network
 * operation. Typically originated from a subclass of
 * {@link HttpResponseListener} using {@code fireSuccessListener} or
 * {@code fireErrorListener} methods.
 * 
 * @author Awais
 * 
 */
public interface HttpResponseListener {

	/**
	 * Called when the network operation was completed successfully.
	 * 
	 * @param object An object of type {@link CustomHttpResponse} representing
	 *            server response.
	 */
	public void onHttpSuccess(CustomHttpResponse object);

	/**
	 * Called when there was an error contacting/processing the server response.
	 *
	 * @param exception An object of type {@link CustomHttpError} representing
	 *            server/client error
	 */
	public void onHttpError(CustomHttpResponse error);
}
