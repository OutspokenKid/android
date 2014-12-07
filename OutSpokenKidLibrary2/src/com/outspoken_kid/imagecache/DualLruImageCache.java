package com.outspoken_kid.imagecache;

import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.StringUtils;

/**
 * Bitmap LRU Memory cache + Disk LRU cache cache.
 * 
 * @author Hyung Gun Kim
 *
 */
public class DualLruImageCache implements ImageCache {

	private BitmapLruImageCache memoryCache;
	private DiskLruImageCache diskCache;
	
	public DualLruImageCache(Context context, String uniqueName,
			int memoryCacheSize, int diskCacheSize, 
			CompressFormat compressFormat, int quality) {
		
		memoryCache = new BitmapLruImageCache(memoryCacheSize);
		diskCache = new DiskLruImageCache(context, uniqueName, diskCacheSize * 10,
				compressFormat, quality);
	}
	
	protected int sizeOf(String key, Bitmap value) {
		return value.getRowBytes() * value.getHeight();
	}
	
	@Override
	public Bitmap getBitmap(String url) {
		
		url = StringUtils.stringReplace(url).toLowerCase(Locale.getDefault());
		Bitmap bitmap = memoryCache.getBitmap(url);
		
		if(bitmap == null || bitmap.isRecycled()) {
			bitmap = diskCache.getBitmap(url);
			
			if(bitmap != null && !bitmap.isRecycled()) {
				memoryCache.putBitmap(url, bitmap);
			}
		}
		
		return bitmap;
	}
 
	@Override
	public void putBitmap(String url, final Bitmap bitmap) {

		if(bitmap != null && !bitmap.isRecycled()) {

			try {
				//Put bitmap to memory cache.
				memoryCache.putBitmap(url, bitmap);
				
				//Put bitmap to disk cache.
				(new BackgroundPutBitmapThread(
						StringUtils.stringReplace(url).toLowerCase(Locale.getDefault()), 
						bitmap)).run();
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
		}
	}
	
////////////////// Classes.
	
	public class BackgroundPutBitmapThread extends Thread {
		
		private String url;
		private Bitmap bitmap;
		
		public BackgroundPutBitmapThread(String url, Bitmap bitmap) {
			
			this.url = url;
			this.bitmap = bitmap;
		}
		
		@Override
		public void run() {
			diskCache.putBitmap(url, bitmap);
		}
	}
}
