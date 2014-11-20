package com.outspoken_kid.model;

public class MultiSelectImageInfo {

	public int id;
	public String sdcardPath;
	public boolean isSelected = false;

	/*
	id로 썸네일 구해오기.
	MediaStore.Images.Thumbnails.getThumbnail(
			getApplicationContext().getContentResolver(), 
			id, 
			MediaStore.Images.Thumbnails.MICRO_KIND, 
			null);
	*/
}
