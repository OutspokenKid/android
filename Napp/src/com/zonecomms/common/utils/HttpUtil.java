package com.zonecomms.common.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.outspoken_kid.utils.LogUtils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.outspoken_kid.utils.StringUtils;

public class HttpUtil {
	public static boolean use_time_out=false;
	public static int timeoutSocket;
	public static int timeoutConnection;
	public static String get(String url, Context context) {
		try {
			return streamToString(executeGet(url, context));
		} catch (Exception e) {
			return null;
		}
	}

	public static String get(String url, HashMap<String, String> params, Context context) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(url);
		stringBuilder.append("?");

		for (String name : params.keySet()) {
			stringBuilder.append(name);
			stringBuilder.append("=");
			stringBuilder.append(params.get(name));
			stringBuilder.append("&");
		}

		return get(stringBuilder.toString(), context);
	}

	public static Bitmap getImage(String url, Context context) {
		
		if (StringUtils.isEmpty(url)) {
			return null;
		}

		try {
			return streamToBitmap(executeGet(url, context));
		} catch (Exception e) {
			return null;
		}
	}

	public static void resetCache(Context context) {
		File cacheBase = null;
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			cacheBase = new File(android.os.Environment.getExternalStorageDirectory(), "itgling");
			if (!cacheBase.exists()) {
				cacheBase.mkdirs();
			}
		} else {
			cacheBase = context.getFilesDir();
		}

		File[] fileList = cacheBase.listFiles();

		for (File file : fileList) {
			file.delete();
		}
	}

	public static String getCacheInfo(Context context) {

		File cacheBase = null;
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			cacheBase = new File(android.os.Environment.getExternalStorageDirectory(), "itgling");
			if (!cacheBase.exists()) {
				cacheBase.mkdirs();
			}
		} else {
			cacheBase = context.getFilesDir();
		}

		File[] fileList = cacheBase.listFiles();

		Long totalSize = 0L;
		for (File file : fileList) {
			totalSize = totalSize + file.length();
		}

		String cacheInfo = null;
		if (totalSize > 1048575) {
			cacheInfo = (totalSize / 1048575) + "MB (" + fileList.length + ")";
		} else {
			cacheInfo = (totalSize / 1024) + "KB (" + fileList.length + ")";
		}
		return cacheInfo;
	}
	
	public static String post(String url, File uploadFile, Context context) {
		
		HttpPost httpPost = new HttpPost(url);
		InputStream inputStream = null;
		
		try {
			MultipartEntity multipartEntity = new MultipartEntity();
			multipartEntity.addPart("attachFile", new FileBody(uploadFile));
			multipartEntity.addPart("filename", new StringBody(""));
			multipartEntity.addPart("basepath", new StringBody(""));
			multipartEntity.addPart("folder", new StringBody("service/temp"));
			multipartEntity.addPart("ext", new StringBody("image"));
			multipartEntity.addPart("stillimage", new StringBody("false"));
			multipartEntity.addPart("allsizeResize", new StringBody(""));
			multipartEntity.addPart("quality", new StringBody("100"));
			multipartEntity.addPart("result_flag", new StringBody("STRING"));

			httpPost.setEntity(multipartEntity);
			
			inputStream = execute(httpPost, context);
		} catch (Exception e) {
			LogUtils.trace(e);
		}
		return streamToString(inputStream);
	}

	private static InputStream executeGet(String url, Context context) {
		return execute(new HttpGet(url), context);
	}

	private static InputStream execute(HttpUriRequest httpUriRequest, Context context) {
		
		DefaultHttpClient defaultHttpClient=null;
		
		if(use_time_out==true){
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
			
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
		
			defaultHttpClient = new DefaultHttpClient(httpParameters);
		}
		else{
			defaultHttpClient = new DefaultHttpClient();
		}
		
		HttpResponse httpResponse = null;
		httpUriRequest.setHeader("User-Agent", "itgling_android_" + com.outspoken_kid.utils.AppInfoUtils.getVersionCode() + " ("
				+ android.os.Build.MODEL + " " + android.os.Build.VERSION.RELEASE + ")");
		try {
			httpResponse = defaultHttpClient.execute(httpUriRequest);
		} catch (IOException e) {
			LogUtils.trace(e);
			
			try {
				httpResponse = defaultHttpClient.execute(httpUriRequest);
			} catch (IOException e1) {
				LogUtils.trace(e);
			}
		}
		
		if (200 == httpResponse.getStatusLine().getStatusCode()) {
			HttpEntity httpEntity = httpResponse.getEntity();

			if (context instanceof Activity) {
				((Activity) context).getIntent().putExtra("httpResponseStatusCode", 200);
			}

			try {
				return httpEntity.getContent();
			} catch (IllegalStateException e) {
				LogUtils.trace(e);
			} catch (IOException e) {
				LogUtils.trace(e);
			}

		} else {
			if (context instanceof Activity) {
				((Activity) context).getIntent().putExtra("httpResponseStatusCode",
						httpResponse.getStatusLine().getStatusCode());
			}
		}
		return null;
	}

	private static String streamToString(InputStream inputStream) {
		
		if(inputStream == null) {
			return null;
		}
		
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 8192);
		StringBuilder stringBuilder = new StringBuilder();

		String line = null;

		try {
			while (null != (line = bufferedReader.readLine())) {
				stringBuilder.append(line);
			}
		} catch (IOException e) {
			LogUtils.trace(e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				LogUtils.trace(e);
			}
		}
		
		return stringBuilder.toString();
	}

	private static Bitmap streamToBitmap(InputStream inputStream) {
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inTempStorage=new byte[512*16];
	
		
		BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 512*16);
		Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream, null, o);

		try {
			bufferedInputStream.close();
		} catch (IOException e) {
			LogUtils.trace(e);
		}

		return bitmap;
	}
}
