/*
 * Awais - Android Framework v1.0
 */
package com.uiu.helper.KidsHelper.framework.utilities;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * This class serves as a gateway to fonts in assets directory. It also
 * maintains an internal font cache to avoid heavy memory allocation each time
 * same font is requested.
 * 
 * @author Awais
 * 
 */
public class FontUtility {

	/** The font cache. */
	private static Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface> ();

	/**
	 * Gets font {@link Typeface} from assets directory.
	 * 
	 * @param fontPathFromAssets The path to font file, relative to assets
	 *            directory
	 * @param context A valid context
	 * @return Typeface object
	 */
	public static Typeface getFontFromAssets (String fontPathFromAssets, Context context) {

		Typeface tf = fontCache.get (fontPathFromAssets);

		if (tf == null) {

			try {
				tf = Typeface.createFromAsset (context.getAssets (), fontPathFromAssets);
			}
			catch (Exception e) {
				return null;
			}

			fontCache.put (fontPathFromAssets, tf);
		}

		return tf;
	}
}
