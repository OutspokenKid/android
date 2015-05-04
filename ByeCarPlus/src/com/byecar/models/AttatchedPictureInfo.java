package com.byecar.models;

import android.graphics.Bitmap;

public class AttatchedPictureInfo {

	private Bitmap thumbnail;
	private String url;
	private String sdCardPath;
	
	public Bitmap getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(Bitmap thumbnail) {
		this.thumbnail = thumbnail;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getSdCardPath() {
		return sdCardPath;
	}
	public void setSdCardPath(String sdCardPath) {
		this.sdCardPath = sdCardPath;
	}
}
