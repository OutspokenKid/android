package com.zonecomms.common.models;

import org.json.JSONArray;
import org.json.JSONObject;

import com.outspoken_kid.utils.LogUtils;
import com.zonecomms.napp.classes.ApplicationManager;
import com.outspoken_kid.downloader.bitmapdownloader.BitmapDownloader;
import com.outspoken_kid.downloader.bitmapdownloader.BitmapDownloader.OnCompletedListener;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class LoadingImageSet {

	private int time;
	private String[] images;
	private Drawable[] drawables;
	
	public LoadingImageSet(){}
	
	public LoadingImageSet(JSONObject objJSON) {
		
		try {
			if(objJSON.has("loading")) {
				
				JSONArray arJSON = objJSON.getJSONArray("loading");
				int size = arJSON.length();
				images = new String[size];
				drawables = new Drawable[size];
				for(int i=0; i<size; i++) {
					images[i] = arJSON.getString(i);
					final int I = i;
					BitmapDownloader.download(images[i], null, new OnCompletedListener() {

						@Override
						public void onErrorRaised(String url, Exception e) {
						}

						@Override
						public void onCompleted(String url, Bitmap bitmap,
								ImageView view) {
							
							try {
								Resources res = ApplicationManager.getInstance().getActivity().getResources();
								drawables[I] = new BitmapDrawable(res, bitmap);
							} catch(Exception e) {
							}
						}
						
					}, null, null, true);
				}
			}
			
			if(objJSON.has("timer")) {
				try {
					String timerString = objJSON.getString("timer");
					float timer = Float.parseFloat(timerString);
					time = (int)(timer / ((float)images.length) * 1000);
				} catch (Exception e) {
					LogUtils.trace(e);
					time = 0;
				}
			}
			
		} catch(Exception e) {}
	}
	
	public int getTime() {
		return time;
	}
	
	public void setTime(int time) {
		this.time = time;
	}
	
	public String[] getImages() {
		return images;
	}
	
	public void setImages(String[] images) {
		this.images = images;
	}

	public Drawable[] getDrawables() {
		return drawables;
	}

	public void setDrawables(Drawable[] drawables) {
		this.drawables = drawables;
	}
}
