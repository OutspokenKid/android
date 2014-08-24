package com.cmons.cph.models;

import org.json.JSONArray;
import org.json.JSONObject;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

public class Category extends BaseModel {
	
	private int id;
	private String name;
	private long created_at;
	private Category[] categories; 
	
	private boolean selected;
	
	public Category(JSONObject objJSON) {
		
		try {
			if(objJSON.has("id")) {
				this.id = objJSON.getInt("id");
			}
			
			if(objJSON.has("name")) {
				this.name = objJSON.getString("name");
			}
			
			if(objJSON.has("created_at")) {
				this.created_at = objJSON.getLong("created_at");
			}
			
			if(objJSON.has("subcategories")) {
				
				JSONArray arJSON = objJSON.getJSONArray("subcategories");
				int size = arJSON.length();
				
				if(size > 0) {
					categories = new Category[size];
					for(int i=0; i<size; i++) {
						categories[i] = new Category(arJSON.getJSONObject(i));
					}
				}
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		selected = false;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getCreated_at() {
		return created_at;
	}
	public void setCreated_at(long created_at) {
		this.created_at = created_at;
	}
	
	public Category[] getCategories() {
		
		return categories;
	}
	
	public void setCategories(Category[] categories) {
		
		this.categories = categories;
	}
}
