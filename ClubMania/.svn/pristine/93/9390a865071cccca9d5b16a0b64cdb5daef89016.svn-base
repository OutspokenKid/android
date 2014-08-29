package com.outspoken_kid.downloader.bitmapdownloader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import com.outspoken_kid.downloader.BaseAsyncDownloader;
import com.outspoken_kid.imagecache.FileCacheFactory;
import com.outspoken_kid.imagecache.ImageCache;
import com.outspoken_kid.imagecache.ImageCacheFactory;
import com.outspoken_kid.imagefilter.ImageFilter;
import com.outspoken_kid.utils.StringUtils;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.widget.ImageView;

public class BitmapDownloader extends BaseAsyncDownloader {
	
	protected static BitmapDownloader mBitmapDownloader;
	private ImageCache imageCache;
	protected ArrayList<BaseDownloadTask> immediateTasks = new ArrayList<BaseDownloadTask>();
	
	public static BitmapDownloader getInstance() {
		if( mBitmapDownloader == null ) {
			mBitmapDownloader = new BitmapDownloader();
		}
		
		return mBitmapDownloader;
	}
	
	public void setCacheFactory(Context context) {
		
		if(imageCache == null) {
			final int memClass = ((ActivityManager) context.getSystemService(
		            Context.ACTIVITY_SERVICE)).getMemoryClass();
			
			// Use 1/2 of the available KB memory for this memory cache.
		    final int cacheSize = (1024 * memClass) / 2;
			final int memoryImageMaxCounts = 200;
			final String cacheName = "ImageCacheFactory";
		    
			FileCacheFactory.initialize(context);
			FileCacheFactory.getInstance().create(cacheName, cacheSize);
			ImageCacheFactory.getInstance().createTwoLevelCache(cacheName, memoryImageMaxCounts);
			imageCache = ImageCacheFactory.getInstance().get(cacheName);
		}
	}
	
	public void addBitmapToCache(String key, Bitmap bitmap) {
		
		if(imageCache != null
				&& !TextUtils.isEmpty(key)
				&& bitmap != null 
				&& !bitmap.isRecycled()) {
			imageCache.addBitmap(key, bitmap);
		}
	}

	public Bitmap getBitmapFromCache(String key) {

		if(imageCache != null && !TextUtils.isEmpty(key)) {
			Bitmap bitmap = imageCache.getBitmap(key);
			
			if(bitmap != null && !bitmap.isRecycled()) {
				return bitmap;
			}
		}
		
		return null;
	}
	
	protected void addTask(String url, String key, OnCompletedListener listener, ArrayList<ImageFilter> arImageFilter, 
			ImageView targetView, boolean useMemoryCache) {
		
		Bitmap bitmap = null;
		
		if(useMemoryCache) {
			bitmap = getBitmapFromCache(url + key);
		}
		
		if(bitmap != null && listener != null) {
			listener.onCompleted(url, bitmap, targetView);
		} else {
			AsyncDownloadTask task = new AsyncDownloadTask(url, key, listener, arImageFilter, targetView, useMemoryCache);
			addTaskToQueue(task);
		}
	}
	
	protected void executeTask(String url, String key, OnCompletedListener listener, ArrayList<ImageFilter> arImageFilter,
			ImageView targetView, boolean useMemoryCache) {
		
		Bitmap bitmap = null;
		
		if(useMemoryCache) {
			bitmap = getBitmapFromCache(url + key);
		}
		
		if(bitmap != null && listener != null) {
			listener.onCompleted(url, bitmap, targetView);
		} else {
			AsyncDownloadTask task = new AsyncDownloadTask(url, key, listener, arImageFilter, targetView, useMemoryCache);
			immediateTasks.add(task);
			task.execute();
		}
	}
	
	public static void download(String url, String key, OnCompletedListener listener, 
			ArrayList<ImageFilter> arImageFilter, ImageView targetView, boolean useMemoryCache) {
		BitmapDownloader.getInstance().addTask(url, key, listener, arImageFilter, targetView, useMemoryCache);
	}
	
	public static void downloadImmediately(String url, String key, OnCompletedListener listener, 
			ArrayList<ImageFilter> arImageFilter, ImageView targetView, boolean useMemoryCache) {
		BitmapDownloader.getInstance().executeTask(url, key, listener, arImageFilter, targetView, useMemoryCache);
	}
	
	public static void removeTasksByKey(String key) {
		
		try {
			BitmapDownloader bd = BitmapDownloader.getInstance();
			bd.removeTasks(bd.getDownloadQueue(), key);
			
			if(bd.immediateTasks != null && bd.immediateTasks.size() != 0) {
				
				for(int i=0; i<bd.immediateTasks.size(); i++) {
					
					if(bd.immediateTasks.get(i).getKey().equals(key)) {
						bd.immediateTasks.get(i).cancel(true);
					}
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void removeTasks(ArrayList<BaseDownloadTask> arList, String key) {
		super.removeTasks(arList, key);

		try {
			if(immediateTasks == null || immediateTasks.size() == 0 || StringUtils.checkNullOrDefault(key, "")) {
				return;
			}
			
			int size = immediateTasks.size();
			
			for(int i=size - 1; i>= 0; i--) {
				if(immediateTasks.get(i).getKey() != null && key.equals(immediateTasks.get(i).getKey())) {
					BaseDownloadTask bdt = immediateTasks.get(i);
					immediateTasks.remove(i);
					bdt.cancel(true);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void downloadComplete(AsyncTask<Void, Void, Void> task) {
		
		if(task != null) {

			if(immediateTasks.contains(task)) {
				immediateTasks.remove(task);
			} else {
				super.downloadComplete(task);
			}
		}
	}
	
/////////////////// Classes.
	
	public class AsyncDownloadTask extends BaseDownloadTask {
		OnCompletedListener mListener;
		Bitmap bitmap;
		Bitmap filteredBitmap;
		String strImageFilter = "";
		ArrayList<ImageFilter> arImageFilter;
		ImageView targetView;
		boolean useMemoryCache;
		boolean isDownloaded;
		
		public AsyncDownloadTask(String url, String key, OnCompletedListener onCompletedListener, 
				ArrayList<ImageFilter> arImageFilter, ImageView targetView, boolean useMemoryCache) {
			super(url, key);
			this.mListener = onCompletedListener;
			this.arImageFilter = arImageFilter;
			this.targetView = targetView;
			this.useMemoryCache = useMemoryCache;
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			
			if(TextUtils.isEmpty(url)) {
				return null;
			}
			
			//Check DB.
			if(useMemoryCache) {
				bitmap = getBitmapFromCache(url + key);
				
				if(bitmap != null && !bitmap.isRecycled()) {
					return null;
				}
			}
			
			isDownloaded = true;
			
            //Download from server.
			HttpClient client = null;
	        HttpGet getRequest = null;
	        
	        try {
	        	client = AndroidHttpClient.newInstance("Android");
	        	getRequest = new HttpGet(url);
                
                HttpResponse response = client.execute(getRequest);
	            final int statusCode = response.getStatusLine().getStatusCode();

	            if (statusCode != HttpStatus.SC_OK) {
	                return null;
	            }

	            final HttpEntity entity = response.getEntity();
	            
	            if (entity != null) {
	                InputStream inputStream = null;
	                
                	try {
	                    inputStream = entity.getContent();
	                    bitmap = BitmapFactory.decodeStream(new FlushedInputStream(inputStream));

	                    //Don't use filter in this application.
//	                    if(bitmap != null) {
//		                    if(arImageFilter != null && arImageFilter.size() != 0) {
//		                    	for(int i=0; i<arImageFilter.size(); i++) {
//			                    	ImageFilter imageFilter = arImageFilter.get(i);
//			                    	
//			                    	if(imageFilter != null) {
//			                    		strImageFilter += (i==0? "" : "&") + imageFilter.getFilterText();
//			                    		filteredBitmap = imageFilter.getFilteredBitmap(bitmap);
//			                    		
//			                    		if(filteredBitmap != null && !filteredBitmap.isRecycled()) {
//		                    				Bitmap tempBitmap = bitmap;
//		                    				bitmap = filteredBitmap;
//		                    				filteredBitmap = null;
//			                    			tempBitmap.recycle();
//			                    		}
//			                    	}
//			                    }
//		                    }
//	                    }
	                    
	                    if(bitmap != null && !bitmap.isRecycled() && useMemoryCache) {
	        				addBitmapToCache(url + key, bitmap);
	        			}
	                } catch(OutOfMemoryError e) {
	                	e.printStackTrace();
	                } catch(Exception e) {
	                	e.printStackTrace();
	                } finally {
	                	if (inputStream != null) {
	                        inputStream.close();
	                    }
	                    entity.consumeContent();
	                }
	            }
	        } catch (IOException e) {
	        	e.printStackTrace();
	        	
	        } catch (IllegalStateException e) {
	        	e.printStackTrace();
	        
	        } catch (Exception e) {
	        	e.printStackTrace();
	        	
	        } finally {
	        	if(getRequest != null) {
	        		getRequest.abort();
	        	}
	        	
	            if ((client instanceof AndroidHttpClient)) {
	                ((AndroidHttpClient) client).close();
	            }
	        }
	        return null;
		}
		
		protected void onPostExecute(Void v) {
			
			if( mListener != null ) {
				if( bitmap == null ) {
					mListener.onErrorRaised(url, null);
				} else {
					mListener.onCompleted(url, bitmap, targetView);
				}
			}
			
			downloadComplete(this);
		};
		
		protected HttpURLConnection getRequest() throws IOException {
			return (HttpURLConnection) new URL(url).openConnection();
		}
	}

/////////////////// Interfaces.
	
	/**
	 * Called when the task completed.
	 * 
	 * @author HyungGunKim
	 */
	public interface OnCompletedListener {
		
		/**
		 * Called when the task success to download bitmap.
		 * @param url		url of bitmap.
		 * @param bitmap	bitmap.
		 * @param view 		target imageView.
		 */
		public void onCompleted(String url, Bitmap bitmap, ImageView view);
		
		/**
		 * Called when the task fail to download bitmap.
		 * @param url		url of bitmap.
		 * @param e			exception.
		 */
		public void onErrorRaised(String url, Exception e);
	}
}