package com.zonecomms.common.models;

public class GridMenu {

	protected String imageUrl;
	protected int iconResId;
	protected String iconUrl;
	protected String text;
	protected int[] baseColor;
	protected int colorLevel;
	protected boolean isGroup;
	protected String uri;
	
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public int getIconResId() {
		return iconResId;
	}
	public void setIconResId(int iconResId) {
		this.iconResId = iconResId;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int[] getBaseColor() {
		return baseColor;
	}
	public void setBaseColor(int[] baseColor) {
		this.baseColor = baseColor;
	}
	public int getColorLevel() {
		return colorLevel;
	}
	public void setColorLevel(int colorLevel) {
		this.colorLevel = colorLevel;
	}
	public boolean isGroup() {
		return isGroup;
	}
	public void setGroup(boolean isGroup) {
		this.isGroup = isGroup;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	
}
