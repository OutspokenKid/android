package com.outspoken_kid.imagefilter;

import android.graphics.Bitmap;

public class SquareFilter extends ImageFilter {

	@Override
	public Bitmap getFilteredBitmap(Bitmap bitmap) {
		
		if(bitmap == null || bitmap.getWidth() == bitmap.getHeight()) {
			return null;
		}
		
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Bitmap squareBitmap = null;
		
		try {
			if(width > height) {
				squareBitmap = Bitmap.createBitmap(bitmap, (width - height) / 2, 0, height, height);
			} else {
				squareBitmap = Bitmap.createBitmap(bitmap, 0, (height - width) / 2, width, width);
			}
			
			if(squareBitmap != null) {
				return squareBitmap;
			} else {
				return null;
			}
		} catch(OutOfMemoryError e) {
			return null;
		} catch(Exception e) {
			return null;
		}
	}

	@Override
	public String getFilterText() {
		return "square";
	}
}
