package com.outspoken_kid.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Environment;

import com.outspoken_kid.R;

public class ImageUploadUtils {
	
	/**
	 * 이미지 업로드.
	 * 
	 * @param uploadUrl
	 * @param onAfterUploadImage
	 * @param filePath
	 */
	public static void uploadImage(String uploadUrl,
			OnAfterUploadImage onAfterUploadImage,
			String filePath){
		(new AsyncUploadImage(uploadUrl, onAfterUploadImage, filePath)).execute();
	}

//////////////////// Classes.
	
	public static class AsyncUploadImage extends AsyncTask<Void, Void, Void> {

		private String uploadUrl;
		private OnAfterUploadImage onAfterUploadImage;
		private String filePath;
		private String resultString;
		
		public AsyncUploadImage(String uploadUrl, OnAfterUploadImage onAfterUploadImage,
				String filePath) {
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

			Bitmap tempSamplingBitmap = null;
			File tempFile = null;
			OutputStream out = null;
			
			try {
			//샘플링 사이즈 계산.
				int inSampleSize = 1;
				
				if(ResizeUtils.getScreenWidth() >= 720) {
					inSampleSize = BitmapUtils.getBitmapInSampleSize(filePath, 720);
				} else {
					inSampleSize = BitmapUtils.getBitmapInSampleSize(filePath, 640);
				}
				
			//샘플링 비트맵 생성.
				tempSamplingBitmap = BitmapUtils.getBitmapFromSdCardPath(filePath, inSampleSize);
				
				if(tempSamplingBitmap == null) {
					LogUtils.log("###ImageUploadUtils.doInBackground.  "
							+ "\ntempSamplingBitmap is null."
							+ "\nfilePath : " + filePath
							+ "\ninSampleSize : " + inSampleSize);
				}
				
			//샘플링 비트맵 파일로 변환.
				String tempFilePath = null;
				
				if(filePath.contains(".jpg")) {
					tempFilePath = Environment.getExternalStorageDirectory() + "/temp.jpg";
				} else if(filePath.contains("jpeg")) {
					tempFilePath = Environment.getExternalStorageDirectory() + "/temp.jpeg";
				} else if(filePath.contains("png")) {
					tempFilePath = Environment.getExternalStorageDirectory() + "/temp.png";
				}
				
				tempFile = new File(tempFilePath);
	            tempFile.createNewFile();
				out = new FileOutputStream(tempFile);
	            
	            if(tempFilePath.contains(".png")) {
	            	tempSamplingBitmap.compress(CompressFormat.PNG, 100, out);
	            } else {
	            	tempSamplingBitmap.compress(CompressFormat.JPEG, 100, out);
	            }
	            
			//파일 업로드.
	            LogUtils.log("###AsyncUploadImage.doInBackground.  \nfileSize : " + tempFile.length());
	            resultString = HttpUtils.httpPost(uploadUrl, "userfile", tempFile);
	        } catch(OutOfMemoryError oom) {
				oom.printStackTrace();
				ToastUtils.showToast(R.string.failToLoadBitmap_OutOfMemory);
	        } catch (Error e) {
	        	ToastUtils.showToast(R.string.failToLoadBitmap);
	            LogUtils.trace(e);
	        } catch (Exception e) {
	        	ToastUtils.showToast(R.string.failToLoadBitmap);
	            LogUtils.trace(e);
	        } finally {
				
				if(tempSamplingBitmap != null) {
					
					if(!tempSamplingBitmap.isRecycled()) {
						tempSamplingBitmap.recycle();
					}
					
					tempSamplingBitmap = null;
				}
				
				if(tempFile != null) {
					tempFile.delete();
					tempFile = null;
				}
				
				if(out != null) {
					
					try {
						out.close();
					} catch (IOException e) {
					}
					
					out = null;
				}
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
