package com.outspoken_kid.classes;

import org.apache.http.client.CookieStore;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.Volley;
import com.outspoken_kid.utils.LogUtils;

/**
 * Manager for the queue
 * 
 * @author Trey Robinson
 *
 */
public class RequestManager {
	
	/**
	 * the queue :-)
	 */
	private static RequestQueue mRequestQueue;

	private static CookieStore cookieStore;
	
	/**
	 * Nothing to see here.
	 */
	private RequestManager() {
	 // no instances
	} 

	/**
	 * @param context
	 * 			application context
	 */
	public static void init(Context context) {

		LogUtils.log("###RequestManager.init.  ");
		
		if(mRequestQueue != null) {
			return;
		}
		
		mRequestQueue = Volley.newRequestQueue(context);
	}
	
	public static void initForUsingCookie(Context context) {

		if(mRequestQueue != null) {
			return;
		}
	    
	    DefaultHttpClient mDefaultHttpClient = new DefaultHttpClient();
	    
        final ClientConnectionManager mClientConnectionManager = mDefaultHttpClient.getConnectionManager();
        final HttpParams mHttpParams = mDefaultHttpClient.getParams();
        final ThreadSafeClientConnManager mThreadSafeClientConnManager = new ThreadSafeClientConnManager( mHttpParams, mClientConnectionManager.getSchemeRegistry() );
        mDefaultHttpClient = new DefaultHttpClient( mThreadSafeClientConnManager, mHttpParams );
        cookieStore = new BasicCookieStore();
	    mDefaultHttpClient.setCookieStore( cookieStore );
        
        final HttpStack httpStack = new HttpClientStack( mDefaultHttpClient );
        mRequestQueue = Volley.newRequestQueue(context, httpStack );
	}

	/**
	 * @return
	 * 		instance of the queue
	 * @throws
	 * 		IllegalStatException if init has not yet been called
	 */
	public static RequestQueue getRequestQueue() {
	    if (mRequestQueue != null) {
	        return mRequestQueue;
	    } else {
	        throw new IllegalStateException("Not initialized");
	    }
	}
	
	public static CookieStore getCookieStore() {
		
		return cookieStore;
	}
}
