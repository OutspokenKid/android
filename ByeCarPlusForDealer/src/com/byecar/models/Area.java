package com.byecar.models;

import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import com.outspoken_kid.utils.LogUtils;

public class Area {
	
	private String name;
	private ArrayList<Area> areaSet = new ArrayList<Area>();
	
	public Area(JSONObject objJSON) {
		
		try {
			@SuppressWarnings("unchecked")
			Iterator<String> it = objJSON.keys();
			
			while(it.hasNext()) {
				String key = it.next();
				JSONArray arJSON = objJSON.getJSONArray(key);
				
				Area area = new Area(key);
				getAreaSet().add(area);

				int size = arJSON.length();
				for(int i=0; i<size; i++) {
					area.getAreaSet().add(new Area(arJSON.get(i).toString()));
				}
			}
			
		} catch (Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public Area(String name) {
		
		this.name = name;
	}
	
	public String getName() {
		
		return name;
	}

	public ArrayList<Area> getAreaSet() {
		return areaSet;
	}
}
