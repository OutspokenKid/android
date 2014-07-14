package com.outspoken_kid.utils;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;

public class BitmapUtils {

	public synchronized static int GetExifOrientation(String filepath) {
		int degree = 0;
		ExifInterface exif = null;
	
		try {
			exif = new ExifInterface(filepath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (exif != null) {
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
			
			if (orientation != -1) {
				// We only recognize a subset of orientation tag values.
				switch(orientation) {
				case ExifInterface.ORIENTATION_ROTATE_90:
					degree = 90;
					break;
				case ExifInterface.ORIENTATION_ROTATE_180:
					degree = 180;
					break;
				case ExifInterface.ORIENTATION_ROTATE_270:
					degree = 270;
					break;
				}
			}
		}
		return degree;
	}
	
	public synchronized static Bitmap GetRotatedBitmap(Bitmap bitmap, int degrees) {

		if (degrees != 0 && bitmap != null) {
			Matrix m = new Matrix();
			
			m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2 );
			
			try {
				Bitmap b2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);
				
				if (bitmap != b2) {
					bitmap.recycle();
					bitmap = b2;
				}
			} catch(Exception e) {
			} catch(OutOfMemoryError ex) {
			}
		}
		
		return bitmap;
	}
}
