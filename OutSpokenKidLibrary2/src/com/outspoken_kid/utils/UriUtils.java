package com.outspoken_kid.utils;

import java.io.ByteArrayOutputStream;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;

public class UriUtils {

	public static String getRealPathFromUri(Context context, Uri uri) {
		
		try {
			Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
			cursor.moveToFirst();
			int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			return cursor.getString(idx);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static Uri getImageUri(Context context, Bitmap bitmap) {
		
		try {
			ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
			String path = Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
			return Uri.parse(path);
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		} catch(Error e) {
			e.printStackTrace();
			return null;
		}
	}
}
