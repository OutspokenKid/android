package com.outspoken_kid.imagecache;

import java.util.Locale;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import com.android.volley.toolbox.ImageLoader.ImageCache;
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
			int diskCacheSize, CompressFormat compressFormat, int quality) {
		
		memoryCache = new BitmapLruImageCache(diskCacheSize);
		diskCache = new DiskLruImageCache(context, uniqueName, diskCacheSize,
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
	public void putBitmap(String url, Bitmap bitmap) {

		if(bitmap != null && !bitmap.isRecycled()) {
			url = StringUtils.stringReplace(url).toLowerCase(Locale.getDefault());
			memoryCache.putBitmap(url, bitmap);
			diskCache.putBitmap(url, bitmap);
		}
	}
}