package com.cmons.cph.models;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONObject;

import com.cmons.cph.classes.CphConstants;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

public class WholesaleWish extends BaseModel implements Serializable {

	private static final long serialVersionUID = 8648688228004281184L;
	
	private int id;
	private String name;
	private String location;
	private int sum;
	private Wish[] items;
	
	public WholesaleWish(JSONObject objJSON) {
	
		try {
			if(objJSON.has("id")){
				this.id = objJSON.getInt("id");
			}
			
			if(objJSON.has("name")) {
				this.name = objJSON.getString("name");
			}
			
			if(objJSON.has("location")) {
				this.location = objJSON.getString("location");
			}
			
			if(objJSON.has("sum")){
				this.sum = objJSON.getInt("sum");
			}
			
			if(objJSON.has("items")) {
				JSONArray arJSON = objJSON.getJSONArray("items");
				
				int size = arJSON.length();
				items = new Wish[size];
				for(int i=0; i<size; i++) {
					Wish wish = new Wish(arJSON.getJSONObject(i));
					wish.setItemCode(CphConstants.ITEM_WISH);
					items[i] = wish;
				}
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}

	public Wish[] getItems() {
		return items;
	}

	public void setItems(Wish[] items) {
		this.items = items;
	}
}
