package com.zonecomms.common.models;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;

public class MenuColorSet {

	private int color_type;
	private int[] colors;
	
	public MenuColorSet(){}
	
	public MenuColorSet(JSONObject objJSON) {

		try {
			if(objJSON.has("color_type")) {
				color_type = objJSON.getInt("color_type");
			}
			
			if(objJSON.has("color")) {
				JSONArray arJSON = objJSON.getJSONArray("color");
				
				int size = arJSON.length();
				colors = new int[size];
				for(int i=0; i<size; i++) {
					JSONObject objColor = arJSON.getJSONObject(i);
					
					int r = objColor.getInt("r");
					int g = objColor.getInt("g");
					int b = objColor.getInt("b");
					
					colors[i] = Color.rgb(r, g, b);
				}
			}
		} catch(Exception e) {}
	}
	
	public int getColor_type() {
		return color_type;
	}
	public void setColor_type(int color_type) {
		this.color_type = color_type;
	}
	public int[] getColors() {
		return colors;
	}
	public void setColors(int[] colors) {
		this.colors = colors;
	}
}
