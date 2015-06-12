package com.outspoken_kid.utils;

import java.io.File;

import android.os.AsyncTask;

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
			
			try {
				
			//파일 업로드.
				File tempFile = new File(filePath);
	            LogUtils.log("###AsyncUploadImage.doInBackground.  \nfileSize : " + tempFile.length());
	            resultString = HttpUtils.httpPost(uploadUrl, "userfile", tempFile);
	        } catch(OutOfMemoryError oom) {
				oom.printStackTrace();
//				try {ToastUtils.showToast(R.string.failToLoadBitmap_OutOfMemory);} catch (Exception e) {}
	        } catch (Error e) {
//	        	try {ToastUtils.showToast(R.string.failToLoadBitmap);} catch (Exception e2) {}
	            LogUtils.trace(e);
	        } catch (Exception e) {
//	        	try {ToastUtils.showToast(R.string.failToLoadBitmap);} catch (Exception e2) {}
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
