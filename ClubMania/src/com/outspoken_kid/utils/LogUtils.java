package com.outspoken_kid.utils;

import android.util.Log;

/**
 * v1.0.1
 * 
 * @author HyungGunKim
 *
 * ========================
 * v1.0.1 - 130914 Add try/catch.
 */
public class LogUtils {
	
	private static final boolean NEED_LOG = true;
	
	@SuppressWarnings("unused")
	public static void log(String logString) {
		
		if(NEED_LOG && logString != null) {
			try {
				Log.i("notice", logString);
			} catch(Exception e) {
			}
		}
	}
}
