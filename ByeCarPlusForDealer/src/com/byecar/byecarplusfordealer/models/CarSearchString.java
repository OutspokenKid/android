package com.byecar.byecarplusfordealer.models;

import com.outspoken_kid.model.BaseModel;

public class CarSearchString extends BaseModel {

	private String text;
	private int type;

	public CarSearchString() {
		
	}
	
	public CarSearchString(int type, String text) {
		
		this.type = type;
		this.text = text;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
