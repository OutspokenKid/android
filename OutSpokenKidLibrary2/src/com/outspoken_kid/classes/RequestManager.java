package com.outspoken_kid.classes;

import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.Volley;

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
		
		BasicHttpParams mHttpParams = new BasicHttpParams();

	    // Set the timeout in milliseconds until a connection is established.
	    // The default value is zero, that means the timeout is not used.
	    int timeoutConnection = 15000;
	    HttpConnectionParams.setConnectionTimeout(mHttpParams, timeoutConnection);
	    // Set the default socket timeout (SO_TIMEOUT)
	    // in milliseconds which is the timeout for waiting for data.
	    int timeoutSocket = 20000;
	    HttpConnectionParams.setSoTimeout(mHttpParams, timeoutSocket);

	    SchemeRegistry registry = new SchemeRegistry();
	    registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	    
	    /*ClientConnectionManager cm = new ThreadSafeClientConnManager(mHttpParams, registry);*/
	    DefaultHttpClient defaultHttpClient = new DefaultHttpClient(/*cm,*/ mHttpParams);

	    mRequestQueue = Volley.newRequestQueue(context.getApplicationContext(),new HttpClientStack(defaultHttpClient));
		
		///
//		mRequestQueue = Volley.newRequestQueue(context);
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
}
