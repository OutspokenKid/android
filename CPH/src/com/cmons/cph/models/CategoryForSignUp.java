package com.cmons.cph.models;

public class CategoryForSignUp {

	private String id;
	private String name;
	private boolean selected;
	
	public CategoryForSignUp(String id, String name) {
		
		selected = false;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
