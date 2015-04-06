package com.byecar.classes;

import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.ResizeUtils;

public class BCPDownloadUtils {

	/**
	 * @param url
	 * @param onBitmapDownloadListener
	 * @param imageWidth
	 */
	public static void downloadBitmap(String url, 
			OnBitmapDownloadListener onBitmapDownloadListener,
			int imageWidth) {

		if(imageWidth != 0 && url.contains(BCPAPIs.IMAGE_SERVER_URL + "/src/")) {
			url = url.replace("/src/", "/thumb/");
			
			if(url.contains("?")) {
				url = new StringBuffer(url).insert(url.indexOf("?"), "=w" + ResizeUtils.getSpecificLength(imageWidth)).toString();
			} else {
				url += "=w" + ResizeUtils.getSpecificLength(imageWidth);
			}
		}
		
		DownloadUtils.downloadBitmap(url, onBitmapDownloadListener);
	}
}
