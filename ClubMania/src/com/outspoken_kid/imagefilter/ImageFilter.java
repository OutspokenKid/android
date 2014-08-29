package com.outspoken_kid.imagefilter;

import android.graphics.Bitmap;

public abstract class ImageFilter {
	public abstract Bitmap getFilteredBitmap(Bitmap bitmap);
	public abstract String getFilterText();
}
