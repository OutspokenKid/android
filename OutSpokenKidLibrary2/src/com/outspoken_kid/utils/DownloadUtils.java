package com.outspoken_kid.utils;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Build;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.outspoken_kid.classes.RequestManager;
import com.outspoken_kid.imagecache.ImageCacheManager;

public class DownloadUtils {

	public static DownloadUtils instance;
	
	public static DownloadUtils getInstance() {
		
		if(instance == null) {
			instance = new DownloadUtils();
		}
		
		return instance;
	}
	
	public void executeDownloadTask(String url, final OnBitmapDownloadListener onBitmapDownloadListener) {
		
		(new AsyncGetBitmap(url, onBitmapDownloadListener)).execute();
	}
	
	public void executeCacheTask(String url, Bitmap bitmap) {

		(new AsyncPutBitmap(url, bitmap)).execute();
	}
	
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
					
					arg0.printStackTrace();
					
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
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
	public static void downloadBitmap(final String url, 
			final OnBitmapDownloadListener onBitmapDownloadListener) {
		
		try {
			getInstance().executeDownloadTask(url, onBitmapDownloadListener);
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
	
/////////////////////////// Classes.
	
	public class AsyncGetBitmap extends AsyncTask<Void, Void, Bitmap> {

		private String url;
		private OnBitmapDownloadListener onBitmapDownloadListener;
		
		public AsyncGetBitmap(String url, OnBitmapDownloadListener onBitmapDownloadListener) {
			this.url = url;
			this.onBitmapDownloadListener = onBitmapDownloadListener;
		}
		
		@Override
		protected Bitmap doInBackground(Void... v) {
			
			try {
				return ImageCacheManager.getInstance().getBitmap(url);
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Bitmap bitmap) {
			
			if(bitmap != null && !bitmap.isRecycled()) {
				onBitmapDownloadListener.onCompleted(url, bitmap);
			} else {
				downloadBitmap();
			}
		}
		
		public void downloadBitmap() {
			
			Response.Listener<Bitmap> onResponseListener = new Response.Listener<Bitmap>() {

				@Override
				public void onResponse(Bitmap bitmap) {
					
					if(bitmap != null && !bitmap.isRecycled()) {
						
						if(onBitmapDownloadListener != null) {
							onBitmapDownloadListener.onCompleted(url, bitmap);
						}
						
						getInstance().executeCacheTask(url, bitmap);
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
			
			try {
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
	}
	
	public class AsyncPutBitmap extends AsyncTask<Void, Void, Void> {

		public String url;
		public Bitmap bitmap;
		
		public AsyncPutBitmap(String url, Bitmap bitmap) {

			this.url = url;
			this.bitmap = bitmap;
		}
		
		@Override
		protected Void doInBackground(Void... params) {

			ImageCacheManager.getInstance().putBitmap(url, bitmap);
			return null;
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
