package com.outspoken_kid.classes;

import java.util.HashMap;
import java.util.Set;

import android.graphics.Bitmap;

import com.outspoken_kid.utils.LogUtils;

public class BitmapContainer {

	public static HashMap<String, BitmapContainer> containers = new HashMap<String, BitmapContainer>(200);

	public String url;
	public Bitmap bitmap;
	public int reference;
	
	public BitmapContainer(String url, Bitmap bitmap) {
		
		this.url = url;
		this.bitmap = bitmap;
	}
	
	public static BitmapContainer getContainer(String url, Bitmap bitmap) {

		try {
			if(containers.containsKey(url)) {
				return containers.get(url);
			} else {
				BitmapContainer bc = new BitmapContainer(url, bitmap);
				
				if(!containers.containsKey(url)) {
					containers.put(url, bc);
				}
				
				return bc;
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		return null;
	}
	
	public static void clearContainer() {
		
		Set<String> urlSet = containers.keySet();
		
		if(urlSet != null && urlSet.size() != 0) {

			while(urlSet.iterator().hasNext()) {
				String key = urlSet.iterator().next();
				containers.get(key).clear();
			}
		}
		
		containers.clear();
		containers = null;
	}
	
	public void reference() {
		
		reference++;
	}
	
	public void unreference() {
		
		reference--;
		
		if(reference == 0) {
			
			if(containers.containsKey(url)) {
				containers.remove(url);
			}
			
			clear();
		}
	}
	
	public void clear() {
		
		if(bitmap != null && !bitmap.isRecycled()) {
			bitmap.recycle();
		}
		
		bitmap = null;
	}
}
