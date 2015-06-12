package com.outspoken_kid.utils;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;

public class BitmapUtils {

	public static String getRealPathFromUri(Context context, Uri uri) {

		String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
	
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
	
	public static int getBitmapInSampleSize(String filePath, int standardLength) {
		
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 4;
			BitmapFactory.decodeFile(filePath, options);

			int length = 0;

			if(BitmapUtils.GetExifOrientation(filePath) % 180 == 0) {
				length = options.outWidth;
			} else {
				length = options.outHeight;
			}
			
			//option에서 얻어오는 길이는 4배 작아진 가로 길이니까 * 4 해서 원래 길이 구함..
			int calculateSize = length * options.inSampleSize;

			int sampleSize = 1;
			float scale = (float)calculateSize / (float)standardLength;
			
			if(scale > 1) {
				double logBased2 = Math.log(scale) / Math.log(2);
				int index = (int) Math.floor(logBased2) + (logBased2%1==0?0:1);
				sampleSize = (int)Math.pow(2, index);
			}
			
			LogUtils.log("###ImageUploadUtils.getBitmapInSampleSize.  " +
					"\n calculateSize : " + calculateSize +
					"\n standardLength : " + standardLength +
					"\n scale : " + scale +
					"\n sampleSize : " + sampleSize);
			
			return sampleSize;
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		return 1;
	}
	
	public static int getBitmapInSampleSize(File file, int standardLength) {
		
		return getBitmapInSampleSize(file.getPath(), standardLength);
	}
	
	public synchronized static Bitmap GetRotatedBitmap(Bitmap bitmap, int degrees) {

		if (degrees%360 != 0 && bitmap != null) {
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

	public static Bitmap getBitmapFromSdCardPath(String sdCardPath, int inSampleSize) {
		
		try {
			int degree = BitmapUtils.GetExifOrientation(sdCardPath);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = inSampleSize;
			Bitmap thumbnail = BitmapFactory.decodeFile(sdCardPath, options);
			return BitmapUtils.GetRotatedBitmap(thumbnail, degree);
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		return null;
	}
}