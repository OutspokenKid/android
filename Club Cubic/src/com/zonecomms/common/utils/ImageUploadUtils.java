package com.zonecomms.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;

import com.outspoken_kid.utils.BitmapUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.zonecomms.clubcubic.R;
import com.zonecomms.clubcubic.classes.ZoneConstants;
import com.zonecomms.common.models.UploadImageInfo;

public class ImageUploadUtils {
	
	public static String getRealPathFromUri(Context context, Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
	
	public static void uploadImage(Context context, OnAfterUploadImage onAfterUploadImage,
			String filePath, int inSampleSize){
		(new AsyncUploadImage(context, onAfterUploadImage, filePath, inSampleSize)).execute();
	}
	
//////////////////// Classes.
	
	public static class AsyncUploadImage extends AsyncTask<Void, Void, Void> {

		private Context context;
		private OnAfterUploadImage onAfterUploadImage;
		private String filePath;
		private int inSampleSize;
		private String resultString;
		private Bitmap thumbnail;
		
		public AsyncUploadImage(Context context, OnAfterUploadImage onAfterUploadImage, String filePath,
				int inSampleSize) {
			this.context = context;
			this.onAfterUploadImage = onAfterUploadImage;
			this.filePath = filePath;
			this.inSampleSize = inSampleSize;
		}
		
		@Override
		protected Void doInBackground(Void... paramss) {

			LogUtils.log("ImageUploadUtils.doInBackground. ### filePath : " + filePath);

			if(StringUtils.isEmpty(filePath)) {
				ToastUtils.showToast(R.string.failToLoadBitmap);
				return null;
			}
			
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = inSampleSize;
			int degree = BitmapUtils.GetExifOrientation(filePath);
			Bitmap tempBitmap = BitmapFactory.decodeFile(filePath, options);
			tempBitmap = BitmapUtils.GetRotatedBitmap(tempBitmap, degree);
			String tempFilePath = filePath;
			
			if(filePath.contains(".jpg")) {
				tempFilePath = Environment.getExternalStorageDirectory() + "/temp.jpg";
			} else if(filePath.contains("jpeg")) {
				tempFilePath = Environment.getExternalStorageDirectory() + "/temp.jpeg";
			} else if(filePath.contains("png")) {
				tempFilePath = Environment.getExternalStorageDirectory() + "/temp.png";
			}
			
			File tempFile = new File(tempFilePath);
	        OutputStream out = null;
	 
	        try {
	            tempFile.createNewFile();
	            out = new FileOutputStream(tempFile);
	            
	            if(tempFilePath.contains(".png")) {
	            	tempBitmap.compress(CompressFormat.PNG, 100, out);
	            } else {
	            	tempBitmap.compress(CompressFormat.JPEG, 100, out);
	            }
	            
	            resultString = HttpUtil.post(ZoneConstants.IMG_BASE_URL, tempFile, context);
				tempFile.delete();
	        } catch(OutOfMemoryError oom) {
				oom.printStackTrace();
				ToastUtils.showToast(R.string.failToLoadBitmap_OutOfMemory);
	        } catch (Exception e) {
	        	ToastUtils.showToast(R.string.failToLoadBitmap);
	        	
	            LogUtils.trace(e);
	        } finally {
	        	try {
	                out.close();
	            }
	            catch (Exception e) {}
	        	
	        	if(filePath != null && tempFilePath != null && !filePath.equals(tempFilePath) 
	        			&& tempBitmap != null && !tempBitmap.isRecycled()) {
	        		tempBitmap.recycle();
	        		tempBitmap = null;
	        	}
	        }
	        
	        try {
				options = new BitmapFactory.Options();
				options.inSampleSize = inSampleSize / 2;
				
				thumbnail = BitmapFactory.decodeFile(filePath, options);
				thumbnail = BitmapUtils.GetRotatedBitmap(thumbnail, degree);
			} catch(Exception e) {
				ToastUtils.showToast(R.string.failToLoadBitmap);
			} catch(OutOfMemoryError oom) {	
				ToastUtils.showToast(R.string.failToLoadBitmap);
			} catch(Error e) {
				ToastUtils.showToast(R.string.failToLoadBitmap);
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {

			if(onAfterUploadImage != null) {
				try {
					onAfterUploadImage.onAfterUploadImage(new UploadImageInfo(resultString), thumbnail);
				} catch(Exception e) {
					ToastUtils.showToast(R.string.failToLoadBitmap);
					LogUtils.trace(e);
				}
			}
		}
	}
	
//////////////////// Interfaces.
	
	public interface OnAfterUploadImage {
		
		public void onAfterUploadImage(UploadImageInfo uploadImageInfo, Bitmap thumbnail);
	}
}
