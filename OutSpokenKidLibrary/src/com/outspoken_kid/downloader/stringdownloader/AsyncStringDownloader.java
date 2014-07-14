package com.outspoken_kid.downloader.stringdownloader;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.outspoken_kid.downloader.BaseAsyncDownloader;

public class AsyncStringDownloader extends BaseAsyncDownloader {
	
	public interface OnCompletedListener {
		public void onCompleted(String url, String result);
		public void onErrorRaised(String url, Exception e);
	}
	
	public class AsyncDownloadTask extends BaseDownloadTask {
		String postValue;
		String result;
		OnCompletedListener mListener;
		MultipartEntity mpEntity;
		
		public AsyncDownloadTask(String url, String key, OnCompletedListener onCompletedListener) {
			super(url, key);
			this.mListener = onCompletedListener;
		}
		
		public AsyncDownloadTask(String url, String key, OnCompletedListener onCompletedListener, String postValue) {
			super(url, key);
			this.mListener = onCompletedListener;
			this.postValue = postValue;
		}
		
		public AsyncDownloadTask(String url, String key, OnCompletedListener onCompletedListener, MultipartEntity mpEntity) {
			super(url, key);
			this.mListener = onCompletedListener;
			this.mpEntity = mpEntity;
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			
			DefaultHttpClient client = new DefaultHttpClient();
			
			HttpPost httpPost = null;
			
			try {
				httpPost = new HttpPost(url);
				httpPost.setHeader("Connection", "Keep-Alive");
				httpPost.setHeader("Accept-Charset", "UTF-8");
				httpPost.setHeader("ENCTYPE", "multipart/form-data");
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
	        
	        //Add postData.
			if(mpEntity != null) {
				httpPost.setEntity(mpEntity);
			} else {
				if(postValue != null && !postValue.equals("")) {
		        	try {
			        	UrlEncodedFormEntity entity;
			        	ArrayList<BasicNameValuePair> paramsList = new ArrayList<BasicNameValuePair>();
			        	
			        	for(String line : postValue.split("&") ) {
							String[] line_parts = line.split("=");
							paramsList.add(new BasicNameValuePair(line_parts[0], line_parts[1]));
						}
			        	
						entity = new UrlEncodedFormEntity(paramsList, "UTF-8");
						httpPost.setEntity(entity);
		        	} catch(Exception e) {
		        		e.printStackTrace();
		        	}
		        }
			}

	        try {
	        	int retryCount = 0;

	        	while(result == null && retryCount < RETRY_LIMIT) {
	        		HttpResponse response = client.execute(httpPost);
	        		
		            final int statusCode = response.getStatusLine().getStatusCode();

		            if (statusCode != HttpStatus.SC_OK) {
		                return null;
		            }

		            final HttpEntity entity = response.getEntity();
		            
		            if (entity != null) {
		            	String result = EntityUtils.toString(entity);
		            	this.result = result;
		            }
	        		retryCount++;
	        	}
	        } catch (Exception e) {
	            httpPost.abort();
	            e.printStackTrace();
	        }
	        return null;
		}
		
		protected void onPostExecute(Void v) {
			
			if(mListener != null) {
				if(result == null) {
					mListener.onErrorRaised(url, null);
				} else {
					mListener.onCompleted(url, result);
				}
			}

			downloadComplete(this);
		};
		
		protected HttpURLConnection getRequest() throws IOException {
			return (HttpURLConnection) new URL(url).openConnection();
		}
	}
	
	protected static AsyncStringDownloader mAsyncStringDownloader;
	protected AsyncStringDownloader() {}
	
	public static AsyncStringDownloader getInstance() {
		if( mAsyncStringDownloader == null ) {
			mAsyncStringDownloader = new AsyncStringDownloader();
		}
		
		return mAsyncStringDownloader;
	}
	
	protected void addTask(String url, String key, OnCompletedListener listener) {
		AsyncDownloadTask task = new AsyncDownloadTask(url, key, listener);
		addTaskToQueue(task);
	}
	
	protected void addTask(String url, String key, OnCompletedListener listener, String postValue) {
		AsyncDownloadTask task = new AsyncDownloadTask(url, key, listener, postValue);
		addTaskToQueue(task);
	}
	
	protected void addTask(String url, String key, OnCompletedListener listener, MultipartEntity mpEntity) {
		AsyncDownloadTask task = new AsyncDownloadTask(url, key, listener, mpEntity);
		addTaskToQueue(task);
	}
	
	public static void download(String url, String key, OnCompletedListener listener, String postValue) {
		AsyncStringDownloader.getInstance().addTask(url, key, listener, postValue);
	}
	
	public static void download(String url, String key, OnCompletedListener listener) {
		AsyncStringDownloader.getInstance().addTask(url, key, listener);
	}
	
	public static void download(String url, String key, OnCompletedListener listener, MultipartEntity mpEntity) {
		AsyncStringDownloader.getInstance().addTask(url, key, listener, mpEntity);
	}
	
	public static void cancelAllTasksByKey(String key) {
		getInstance().cancelAllStringTasks(getInstance().getDownloadQueue(), key);
	}
	
	public static void cancelCurrentDownload(String key) {
	}
}