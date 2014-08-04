package com.outspoken_kid.utils;

import java.util.ArrayList;

import org.json.JSONObject;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.outspoken_kid.classes.CacheCheckThread;
import com.outspoken_kid.classes.RequestManager;
import com.outspoken_kid.imagecache.ImageCacheManager;
import com.outspoken_kid.model.ImageLoadTask;

public class DownloadUtils {

	private static ArrayList<ImageLoadTask> tasks = new ArrayList<ImageLoadTask>();
	private static Handler cacheHandler = new Handler() {
		
		public void handleMessage(Message msg) {

			Bundle bundle = msg.getData();
			String url = bundle.getString("url");
			Bitmap bitmap = null;
			ImageLoadTask task = getImageLoadTask(url);

			if(bundle.containsKey("bitmap")) {
				bitmap = (Bitmap) bundle.get("bitmap");
			}
			
			if(bitmap != null && !bitmap.isRecycled()) {
				task.onBitmapDownloadListener.onCompleted(url, bitmap);
			} else {
				downloadBitmap(task);
			}
		};
		
		public synchronized ImageLoadTask getImageLoadTask(String url) {
			
			if(url == null) {
				return null;
			}
			
			for(int i=0; i<tasks.size(); i++) {
				
				if(url.equals(tasks.get(i).url)) {
					
					ImageLoadTask ilt = tasks.get(i);
					tasks.remove(ilt);
					return ilt;
				}
			}
			
			return null;
		}
	
		public void downloadBitmap(final ImageLoadTask ilt) {
			
			Response.Listener<Bitmap> onResponseListener = new Response.Listener<Bitmap>() {

				@Override
				public void onResponse(Bitmap bitmap) {
					
					if(bitmap != null && !bitmap.isRecycled()) {
						ImageCacheManager.getInstance().putBitmap(ilt.url, bitmap);
						
						if(ilt.onBitmapDownloadListener != null) {
							ilt.onBitmapDownloadListener.onCompleted(ilt.url, bitmap);
						}
					} else{
						if(ilt.onBitmapDownloadListener != null) {
							ilt.onBitmapDownloadListener.onError(ilt.url);
						}
					}
				}
			};
			Response.ErrorListener onErrorListener = new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError arg0) {
					
					if(ilt.onBitmapDownloadListener != null) {
						ilt.onBitmapDownloadListener.onError(ilt.url);
					}
				}
			};
			
			try {
				ImageRequest imageRequest = new ImageRequest(ilt.url, onResponseListener, 0, 0,
						Bitmap.Config.ARGB_8888, onErrorListener);
				RequestManager.getRequestQueue().add(imageRequest);
			} catch (Exception e) {
				LogUtils.trace(e);
				
				if(ilt.onBitmapDownloadListener != null) {
					ilt.onBitmapDownloadListener.onError(ilt.url);
				}
			} catch (Error e) {
				LogUtils.trace(e);
				
				if(ilt.onBitmapDownloadListener != null) {
					ilt.onBitmapDownloadListener.onError(ilt.url);
				}
			}
		}
	};
	
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
	public static void downloadJSONString(final String url, final OnJSONDownloadListener onJSONDownloadListener) {
		
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
			tasks.add(new ImageLoadTask(url, onBitmapDownloadListener));
			(new CacheCheckThread(url, cacheHandler)).run();
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

	/**
	 * String downloader.
	 * 
	 * @param url
	 * @param onStringDownloadListener
	 */
	public static void downloadString(final String url, 
			final OnStringDownloadListener onStringDownloadListener) {
		
		try {
			StringRequest request = new StringRequest(url, new Listener<String>() {

				@Override
				public void onResponse(String string) {

					if(onStringDownloadListener != null) {
						onStringDownloadListener.onCompleted(url, string);
					}
				}
			}, new ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError arg0) {
					
					if(onStringDownloadListener != null) {
						onStringDownloadListener.onError(url);
					}
				}
			});
			RequestManager.getRequestQueue().add(request);
		} catch (Exception e) {
			LogUtils.trace(e);
			
			if(onStringDownloadListener != null) {
				onStringDownloadListener.onError(url);
			}
		} catch (Error e) {
			LogUtils.trace(e);
			
			if(onStringDownloadListener != null) {
				onStringDownloadListener.onError(url);
			}
		}
	}
	
/////////////////////////// Interfaces.
	
	public interface OnJSONDownloadListener {

		public void onCompleted(String url, JSONObject objJSON);
		public void onError(String url);
	}

	public interface OnStringDownloadListener {
		
		public void onCompleted(String url, String result);
		public void onError(String url);
	}
	
	public interface OnBitmapDownloadListener {
		
		public void onCompleted(String url, Bitmap bitmap);
		public void onError(String url);
	}
}
