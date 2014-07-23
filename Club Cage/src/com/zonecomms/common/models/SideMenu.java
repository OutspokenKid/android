package com.zonecomms.common.models;

public class SideMenu {

	private int titleResId;
	private int iconResId;
	private String uri;
	
	public SideMenu() {}
	
	public int getTitleResId() {
		return titleResId;
	}
	public void setTitleResId(int titleResId) {
		this.titleResId = titleResId;
	}
	public int getIconResId() {
		return iconResId;
	}
	public void setIconResId(int iconResId) {
		this.iconResId = iconResId;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
}
