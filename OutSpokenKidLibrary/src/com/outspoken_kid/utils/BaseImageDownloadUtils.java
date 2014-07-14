package com.outspoken_kid.utils;

import android.widget.ImageView;

import com.outspoken_kid.downloader.bitmapdownloader.BitmapDownloader;

/**
 * v.1.0.0
 * 
 * Use this class when you want to modulate url for each size of device.
 * set UrlModulator.
 * @author HyungGunKim
 * 
 */
public abstract class BaseImageDownloadUtils {

	protected static UrlModulator urlModulator;

	public static void downloadImage(String _url, String key, ImageView _imageView, int _size) {

		
		if(_url == null || _imageView == null) {
			return;
		}
		
		String url = _url;
		
		if(urlModulator != null && _size != 0) {
			url = urlModulator.getSizeSetUrl(_url, _size);
		}
		
		BitmapDownloader.downloadImage(url, key, _imageView, false, false);
	}
	
//////// Don't use queue.

	public static void downloadImageImmediately(String _url, String key, ImageView _imageView, int _size, boolean useMemoryCache) {
		
		if(_url == null || _imageView == null) {
			return;
		}
		
		String url = _url;
		
		if(urlModulator != null && _size != 0) {
			url = urlModulator.getSizeSetUrl(_url, _size);
		}
		
		BitmapDownloader.downloadImage(url, key, _imageView, true, useMemoryCache);
	}

	public static abstract class UrlModulator {
		public abstract String getSizeSetUrl(String url, int size);
	}
}
