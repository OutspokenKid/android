package com.outspoken_kid.classes;

import android.app.Activity;
import android.app.Application;
import android.graphics.PixelFormat;
import android.graphics.Bitmap.CompressFormat;

import com.outspoken_kid.imagecache.ImageCacheManager;
import com.outspoken_kid.imagecache.ImageCacheManager.CacheType;
import com.outspoken_kid.utils.AppInfoUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.ToastUtils;

/**
 * Example application for adding an L1 image cache to Volley. 
 * 
 * @author Trey Robinson
 *
 */
public class OutSpokenApplication extends Application {

	private static int DISK_IMAGECACHE_SIZE = 1024*1024*10;
	private static CompressFormat DISK_IMAGECACHE_COMPRESS_FORMAT = CompressFormat.PNG;
	private static int DISK_IMAGECACHE_QUALITY = 100;  //PNG is lossless so quality is ignored but must be provided
	
	@Override
	public void onCreate() {
		super.onCreate();
		init();
	}
	
	/**
	 * Intialize the request manager and the image cache 
	 */
	private void init() {
		
		RequestManager.init(this);
		createImageCache();
		
		SharedPrefsUtils.setContext(this);
		ToastUtils.setContext(this);
        AppInfoUtils.setAllInfos(this);
	}
	
	public static void initWithActivity(Activity activity) {

		ResizeUtils.setBasicValues(activity, 640);
		activity.getWindow().setFormat(PixelFormat.RGBA_8888);
	}
	
	/**
	 * Create the image cache. Uses Memory Cache by default. Change to Disk for a Disk based LRU implementation.  
	 */
	private void createImageCache(){

//		ImageCacheManager.getInstance().init(this,
//				this.getPackageCodePath()
//				, DISK_IMAGECACHE_SIZE
//				, DISK_IMAGECACHE_COMPRESS_FORMAT
//				, DISK_IMAGECACHE_QUALITY
//				, CacheType.MEMORY);
		
		ImageCacheManager.getInstance().init(this,
				this.getPackageCodePath()
				, DISK_IMAGECACHE_SIZE
				, DISK_IMAGECACHE_COMPRESS_FORMAT
				, DISK_IMAGECACHE_QUALITY
				, CacheType.DUAL);
	}
}
