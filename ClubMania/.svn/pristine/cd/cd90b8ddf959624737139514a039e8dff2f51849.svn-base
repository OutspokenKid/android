package com.zonecomms.common.models;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.drawable.Drawable;

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
				}
			}
			
			if(objJSON.has("timer")) {
				String timerString = objJSON.getString("timer");
				float timer = Float.parseFloat(timerString);
				time = (int)(timer / ((float)images.length) * 1000);
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
