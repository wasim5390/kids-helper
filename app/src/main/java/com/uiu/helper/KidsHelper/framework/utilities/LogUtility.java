/*
 * Awais - Android Framework v1.0
 */
package com.uiu.helper.KidsHelper.framework.utilities;

import android.util.Log;

import com.uiu.helper.KidsHelper.framework.managers.ConfigurationManager;

/**
 * Serves as a logging layer. You must never write logs directly to console.
 * Instead, use this class to give a single point of change for your logging
 * code.
 * 
 * @author Awais
 * 
 */
public class LogUtility {

	private static final String CONFIGURATION_VALUE_ENABLE_LOGGING = "enable_logging";

	/**
	 * Writes log with the given log level and class.
	 * 
	 * @param logLevel The log level, e.g. Log.DEBUG
	 * @param tag Log tag
	 * @param message Log message
	 * @param loggingClass The class calling this method
	 */
	public static void log (int logLevel, String tag, String message, Class<?> loggingClass) {

		if (!isLoggingEnabledInConfigurationManager ())
			return;
		
		logByLevel (logLevel, tag, message, loggingClass);
	}

	/**
	 * Writes log with the given log level.
	 * 
	 * @param logLevel The log level, e.g. Log.DEBUG
	 * @param tag Log tag
	 * @param message Log message
	 */
	public static void log (int logLevel, String tag, String message) {

		if (!isLoggingEnabledInConfigurationManager ())
			return;

		logByLevel (logLevel, tag, message, null);
	}

	private static void logByLevel (int logLevel, String tag, String message, Class<?> loggingClass) {
		if(message == null)
			message = "unknown error";
		
		switch (logLevel) {

			case Log.DEBUG:

				if (loggingClass != null)
					Log.d ("[" + loggingClass.getSimpleName () + "->" + tag + "]", message);
				else
					Log.d ("[" + tag + "]", message);
				break;

			case Log.INFO:

				if (loggingClass != null)
					Log.i ("[" + loggingClass.getSimpleName () + "->" + tag + "]", message);
				else
					Log.i ("[" + tag + "]", message);
				break;

			case Log.ERROR:

				if (loggingClass != null)
					Log.e ("[" + loggingClass.getSimpleName () + "->" + tag + "]", message);
				else
					Log.e ("[" + tag + "]", message);
				break;

			case Log.WARN:

				if (loggingClass != null)
					Log.w ("[" + loggingClass.getSimpleName () + "->" + tag + "]", message);
				else
					Log.w ("[" + tag + "]", message);
				break;

			default:
				break;
		}
	}

	private static boolean isLoggingEnabledInConfigurationManager () {

		String loggingConfig = ConfigurationManager.getInstance ().getValueForKey (CONFIGURATION_VALUE_ENABLE_LOGGING);

		if (StringUtility.isEmptyOrNull (loggingConfig)) {
			throw new IllegalStateException ("LogUtility >> Make sure you define value for " + CONFIGURATION_VALUE_ENABLE_LOGGING + " key");
		}

		return Boolean.parseBoolean (loggingConfig);
	}
}
