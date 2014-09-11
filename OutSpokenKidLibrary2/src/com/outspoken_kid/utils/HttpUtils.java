package com.outspoken_kid.utils;

import java.io.File;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpUtils {

	public static String httpPost(String uploadUrl, String paramName, File file) {
		
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(uploadUrl);
			HttpResponse response;
			
			FileBody fileBody = new FileBody(file);
			
			MultipartEntity multiPartEntity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			multiPartEntity.addPart(paramName, fileBody);
			
			post.setEntity(multiPartEntity);
			response = client.execute(post);
			
			return EntityUtils.toString(response.getEntity(), "utf-8");
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		return null;
	}
}
