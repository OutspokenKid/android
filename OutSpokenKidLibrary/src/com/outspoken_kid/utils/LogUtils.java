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
	private static final boolean NEED_TRACE = true;
	
	public static void log(String logString) {
		
		if(NEED_LOG && logString != null) {
			try {
				Log.i("notice", logString);
			} catch(Exception e) {
				
				if(NEED_TRACE) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void trace(Throwable e) {
		
		if(NEED_TRACE && e != null) {
			e.printStackTrace();
		}
	}
}
