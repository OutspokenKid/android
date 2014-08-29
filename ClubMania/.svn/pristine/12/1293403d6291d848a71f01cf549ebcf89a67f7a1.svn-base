package com.zonecomms.common.utils;

import android.graphics.Bitmap;
//import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.outspoken_kid.downloader.bitmapdownloader.BitmapDownloader;

public class ImageDownloadUtils {
	
	public static void downloadImage(String _url, String key, ImageView _imageView, int _size) {
		
		if(_url == null || _imageView == null) {
			return;
		}
		
		//Set _url to ImageView.
		_imageView.setTag(_url);
		BitmapDownloader.OnCompletedListener ocl = new BitmapDownloader.OnCompletedListener() {
			
			@Override
			public void onErrorRaised(String _url, Exception e) {
			}
			
			@Override
			public void onCompleted(String _url, Bitmap bitmap, ImageView view) {
				
				if(_url == null || bitmap == null || bitmap.isRecycled() || view == null 
						|| view.getTag() == null || !_url.equals(view.getTag().toString())) {
					return;
				}
				
				view.setImageBitmap(bitmap);
				view.setVisibility(View.VISIBLE);
			}
		};
		BitmapDownloader.download(_url, key, ocl, null, _imageView, true);
	}
	
//////// Don't use queue.

	public static void downloadImageImmediately(String _url, String key, ImageView _imageView, int _size, boolean useMemoryCache) {
		
		if(_url == null || _imageView == null) {
			return;
		}

		//Set url to ImageView.
		_imageView.setTag(_url);
		BitmapDownloader.OnCompletedListener ocl = new BitmapDownloader.OnCompletedListener() {
			
			@Override
			public void onErrorRaised(String _url, Exception e) {
				
//				ToastUtils.showToast(R.string.failToDownloadImage);
			}
			
			@Override
			public void onCompleted(String _url, Bitmap bitmap, ImageView view) {
			
				//Check bitmap.
				if(bitmap == null || bitmap.isRecycled()) {
					return;
				}
				
				//Check ImageView.
				if(view == null) {
					return;
				}
				
				//Check tag.
				if(view.getTag() == null || !_url.equals(view.getTag().toString())) {
					return;
				}
				
				view.setImageBitmap(bitmap);
				
				if(view.getVisibility() != View.VISIBLE) {
					view.setVisibility(View.VISIBLE);
				}
			}
		};
		BitmapDownloader.downloadImmediately(_url, key, ocl, null, _imageView, useMemoryCache);
	}
}
