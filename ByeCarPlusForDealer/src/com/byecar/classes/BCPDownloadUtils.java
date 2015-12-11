package com.byecar.classes;

import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.ResizeUtils;

public class BCPDownloadUtils {

	public static float scale = 0;
	
	/**
	 * @param url
	 * @param onBitmapDownloadListener
	 * @param imageWidth
	 */
	public static void downloadBitmap(String url, 
			OnBitmapDownloadListener onBitmapDownloadListener,
			int imageWidth) {

		if(url == "null") {
			return;
		}
		
		if(imageWidth != 0 && url.contains(BCPAPIs.IMAGE_SERVER_URL + "/src/")) {
			url = url.replace("/src/", "/thumb/");

			if(scale == 0) {
				int screenWidth = ResizeUtils.getScreenWidth();
				
				//~720, 1080, 1440
				if(screenWidth <= 720) {
					scale = 1;
					
					
				//721 ~ 1080
				} else if(screenWidth <= 1080) {
					scale = 0.8f;
					
				//1081 ~ 1440
				} else if(screenWidth <= 1440){
					scale = 0.6f;
					
				//1440 ~
				} else {
					scale = 0.4f;
				}
			}
			
			if(url.contains("?")) {
				url = new StringBuffer(url).insert(url.indexOf("?"), "=w" 
						+ (int)((float)ResizeUtils.getSpecificLength(imageWidth) * scale)).toString();
			} else {
				url += "=w" + (int)((float)ResizeUtils.getSpecificLength(imageWidth) * scale);
			}
		}
		
		DownloadUtils.downloadBitmap(url, onBitmapDownloadListener);
	}
}
