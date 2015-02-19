package com.outspoken_kid.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
	private static Context context;
	private static Toast toast;
	private static int gravity;
	private static int xOffset;
	private static int yOffset;
	
	public static void setContext(Context _context) {
		if(context == null) {
			context = _context;
		}
	}
	
	public static void showToast(String _str) {

		try {
			if(toast == null) {
				toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
				toast.setGravity(gravity, xOffset, yOffset);
			}
			
			toast.setText(_str);
			toast.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void showToast(int resId) {
		
		try {
			if(toast == null) {
				toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
				toast.setGravity(gravity, xOffset, yOffset);
			}
			
			toast.setText(resId);
			toast.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void setGravity(int gravity, int xOffset, int yOffset) {

		ToastUtils.gravity = gravity;
		ToastUtils.xOffset = xOffset;
		ToastUtils.yOffset = yOffset;
	}
}
