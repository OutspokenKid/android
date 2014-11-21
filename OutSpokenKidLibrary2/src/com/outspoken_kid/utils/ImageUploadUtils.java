package com.outspoken_kid.utils;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;

import com.outspoken_kid.R;

public class ImageUploadUtils {
	
	public static String getRealPathFromUri(Context context, Uri uri) {

		String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
	
	public static void uploadImage(String uploadUrl,
			OnAfterUploadImage onAfterUploadImage,
			String filePath, int inSampleSize){
		(new AsyncUploadImage(uploadUrl, onAfterUploadImage, filePath, inSampleSize)).execute();
	}

	public static int getBitmapInSampleSize(File file, int standardLength) {
		
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 4;
			BitmapFactory.decodeFile(file.getPath(), options);

			int width = 0;
			int height = 0;

			if (BitmapUtils.GetExifOrientation(file.getPath()) % 180 == 0) {
				width = options.outWidth;
				height = options.outHeight;
			} else {
				width = options.outHeight;
				height = options.outWidth;
			}

			//가로, 세로 중 긴 값으로 최대치 제한.
//			int length = Math.max(width, height);
			//이걸로 하면 가로 기준.
			int length = width;
			
			int sampleSize = 1;
			
			LogUtils.log("###ImageUploadUtils.getBitmapInSampleSize.  " +
					"\nstandardLength : " + standardLength +
					"\nwidth : " + width +
					"\nheight : " + height +
					"\nlength : " + length);
			
			int calculateSize = length * options.inSampleSize;
			
			if (calculateSize <= standardLength) {
				sampleSize = 1;
			} else if (calculateSize <= standardLength * 2) {
				sampleSize = 2;
			} else if (calculateSize <= standardLength * 4) {
				sampleSize = 4;
			} else if (calculateSize <= standardLength * 8) {
				sampleSize = 8;
			} else {
				sampleSize = 16;
			}
			
			return sampleSize;
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		return 8;
	}
	
//////////////////// Classes.
	
	public static class AsyncUploadImage extends AsyncTask<Void, Void, Void> {

		private String uploadUrl;
		private OnAfterUploadImage onAfterUploadImage;
		private String filePath;
		private String resultString;
		
		public AsyncUploadImage(String uploadUrl, OnAfterUploadImage onAfterUploadImage,
				String filePath, int inSampleSize) {
			this.uploadUrl = uploadUrl;
			this.onAfterUploadImage = onAfterUploadImage;
			this.filePath = filePath;
		}
		
		@Override
		protected Void doInBackground(Void... paramss) {

			LogUtils.log("ImageUploadUtils.doInBackground. ### filePath : " + filePath);

			if(StringUtils.isEmpty(filePath)) {
				ToastUtils.showToast(R.string.failToLoadBitmap);
				return null;
			}

			String tempFilePath = null;
			
	        try {
				tempFilePath = filePath;
				
				if(filePath.contains(".jpg")) {
					tempFilePath = Environment.getExternalStorageDirectory() + "/temp.jpg";
				} else if(filePath.contains("jpeg")) {
					tempFilePath = Environment.getExternalStorageDirectory() + "/temp.jpeg";
				} else if(filePath.contains("png")) {
					tempFilePath = Environment.getExternalStorageDirectory() + "/temp.png";
				}
				
				File tempFile = new File(tempFilePath);
	            tempFile.createNewFile();
	            LogUtils.log("###AsyncUploadImage.doInBackground.  \nfileSize : " + tempFile.length());
	            resultString = HttpUtils.httpPost(uploadUrl, "userfile", tempFile);
				tempFile.delete();
	        } catch(OutOfMemoryError oom) {
				oom.printStackTrace();
				ToastUtils.showToast(R.string.failToLoadBitmap_OutOfMemory);
	        } catch (Error e) {
	        	ToastUtils.showToast(R.string.failToLoadBitmap);
	            LogUtils.trace(e);
	        } catch (Exception e) {
	        	ToastUtils.showToast(R.string.failToLoadBitmap);
	            LogUtils.trace(e);
	        }
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {

			if(onAfterUploadImage != null) {
				LogUtils.log("###ImageUploadUtils.onPostExecute."
						+ "\nonAfterUploadImage is not null"
						+ "\nresultString : " + resultString);
				onAfterUploadImage.onAfterUploadImage(resultString);
			} else {
				LogUtils.log("###ImageUploadUtils.onPostExecute."
						+ "\nonAfterUploadImage is null"
						+ "\nresultString : " + resultString);
			}
		}
	}
	
//////////////////// Interfaces.
	
	public interface OnAfterUploadImage {
		
		public void onAfterUploadImage(String resultString);
	}
}
