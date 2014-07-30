package com.outspoken_kid.classes;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.outspoken_kid.imagecache.ImageCacheManager;
import com.outspoken_kid.utils.LogUtils;

public class CacheCheckThread extends Thread {
	
	private String url;
	private Handler cacheHandler;
	
	public CacheCheckThread(String url, 
		Handler cacheHandler) {
		
		this.url = url;
		this.cacheHandler = cacheHandler;
	}
	
	@Override
	public void run() {

		try {
			Bitmap bitmap = ImageCacheManager.getInstance().getBitmap(url);
			
			Bundle bundle = new Bundle();
			bundle.putString("url", url);
			bundle.putParcelable("bitmap", bitmap);
			
			Message msg = Message.obtain();
			msg.setData(bundle);
			cacheHandler.sendMessage(msg);
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
}