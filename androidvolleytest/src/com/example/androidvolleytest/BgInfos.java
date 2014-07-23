package com.example.androidvolleytest;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.outspoken_kid.utils.LogUtils;

public class BgInfos implements Serializable {

	private static final long serialVersionUID = 3763861772345778886L;
	
	ArrayList<String> colors = new ArrayList<String>();
	ArrayList<String> urls = new ArrayList<String>();
	
	public BgInfos(JSONObject objJSON) {
		
		try {
			JSONArray arJSON = objJSON.getJSONArray("bgInfo");
			
			int size = arJSON.length();
			
			for(int i=0; i<size; i++) {
				JSONObject objInfo = arJSON.getJSONObject(i);
				colors.add(objInfo.getString("colorBG"));
				urls.add(objInfo.getJSONArray("link_datas").getString(0));
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
}