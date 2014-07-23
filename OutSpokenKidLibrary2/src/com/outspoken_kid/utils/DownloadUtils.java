package com.outspoken_kid.utils;

import org.json.JSONObject;

import android.graphics.Bitmap;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.outspoken_kid.classes.RequestManager;
import com.outspoken_kid.imagecache.ImageCacheManager;

public class DownloadUtils {

	/**
	 * Download image to NetworkImageView.
	 * 
	 * @param ivImage
	 * @param url
	 */
	public static void downloadImage(NetworkImageView ivImage, String url) {
		
		ivImage.setImageUrl(url,ImageCacheManager.getInstance().getImageLoader());
	}
	
	/**
	 * JSON string downloader.
	 * 
	 * @param url
	 * @param onJSONDownloadListener
	 */
	public static void downloadString(final String url, final OnJSONDownloadListener onJSONDownloadListener) {
		
		try {
			Response.Listener<JSONObject> onResponseListener = new Response.Listener<JSONObject>() {

				@Override
				public void onResponse(final JSONObject objJSON) {
					
					if(onJSONDownloadListener != null) {
						onJSONDownloadListener.onCompleted(url, objJSON);
					}
				}
			};
			
			Response.ErrorListener onErrorListener = new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError arg0) {
					
					if(onJSONDownloadListener != null) {
						onJSONDownloadListener.onError(url);
					}
				}
			};
			
			JsonObjectRequest jsObjRequest = new JsonObjectRequest(
					Request.Method.GET, url, null,
					onResponseListener, 
					onErrorListener);
			RequestManager.getRequestQueue().add(jsObjRequest);
		} catch (Exception e) {
			LogUtils.trace(e);
			
			if(onJSONDownloadListener != null) {
				onJSONDownloadListener.onError(url);
			}
		} catch (Error e) {
			LogUtils.trace(e);
			
			if(onJSONDownloadListener != null) {
				onJSONDownloadListener.onError(url);
			}
		}
	}

	/**
	 * Bitmap downloader.
	 * 
	 * @param url
	 * @param onBitmapDownloadListener
	 */
	public static void downloadBitmap(final String url, 
			final OnBitmapDownloadListener onBitmapDownloadListener) {
		
		try {
			Bitmap bitmap = ImageCacheManager.getInstance().getBitmap(url);
			
			if(bitmap != null && !bitmap.isRecycled()) {
				
				if(onBitmapDownloadListener != null) {
					onBitmapDownloadListener.onCompleted(url, bitmap);
				} 
				
				return;
			}
			
			Response.Listener<Bitmap> onResponseListener = new Response.Listener<Bitmap>() {

				@Override
				public void onResponse(Bitmap bitmap) {

					if(bitmap != null && !bitmap.isRecycled()) {
						ImageCacheManager.getInstance().putBitmap(url, bitmap);
						
						if(onBitmapDownloadListener != null) {
							onBitmapDownloadListener.onCompleted(url, bitmap);
						}
					} else{
						if(onBitmapDownloadListener != null) {
							onBitmapDownloadListener.onError(url);
						}
					}
				}
			};
			
			Response.ErrorListener onErrorListener = new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError arg0) {
					
					if(onBitmapDownloadListener != null) {
						onBitmapDownloadListener.onError(url);
					}
				}
			};
			
			ImageRequest imageRequest = new ImageRequest(url, onResponseListener, 0, 0,
					Bitmap.Config.ARGB_8888, onErrorListener);
			RequestManager.getRequestQueue().add(imageRequest);
		} catch (Exception e) {
			LogUtils.trace(e);
			
			if(onBitmapDownloadListener != null) {
				onBitmapDownloadListener.onError(url);
			}
		} catch (Error e) {
			LogUtils.trace(e);
			
			if(onBitmapDownloadListener != null) {
				onBitmapDownloadListener.onError(url);
			}
		}
	}

/////////////////////////// Interfaces.
	
	public interface OnJSONDownloadListener {

		public void onCompleted(String url, JSONObject objJSON);
		public void onError(String url);
	}
	
	public interface OnBitmapDownloadListener {
		
		public void onCompleted(String url, Bitmap bitmap);
		public void onError(String url);
	}
}
