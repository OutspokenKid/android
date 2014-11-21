package com.outspoken_kid.model;

import java.io.Serializable;

public class MultiSelectImageInfo implements Serializable {

	private static final long serialVersionUID = 3848889334753897341L;
	
	public int id;
	public String sdcardPath;
	public boolean isSelected = false;

	/*
	id로 썸네일 구해오기.
	MediaStore.Images.Thumbnails.getThumbnail(
			getApplicationContext().getContentResolver(), 
			id, 
			MediaStore.Images.Thumbnails.MINI_KIND, 
			null);
	*/
}
