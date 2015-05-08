package com.byecar.models;

import org.json.JSONObject;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

public class Board extends BaseModel {

//	"id": "1",
//	"type": "20",
//	"name": "테스트1",
//	"orderno": "1"
	
	private int id;
	private int type;
	private String name;
	private int orderno;
	
	public Board() {
	}
	
	public Board(JSONObject objJSON) {
		
		try {
			if(objJSON.has("id")) {
				this.id = objJSON.getInt("id");
			}
			
			if(objJSON.has("type")) {
				this.type = objJSON.getInt("type");
			}
			
			if (objJSON.has("name")) {
				this.name = objJSON.getString("name");
			}
			
			if(objJSON.has("orderno")) {
				this.orderno = objJSON.getInt("orderno");
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getOrderno() {
		return orderno;
	}
	public void setOrderno(int orderno) {
		this.orderno = orderno;
	}
}
