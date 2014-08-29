package com.outspoken_kid.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import com.zonecomms.clubmania.R;

public class ImageCacheUtils {
	
	public static Bitmap SafeDecodeBitmapFile(String _strFilePath) {
    	File file = new File(_strFilePath);
    	
    	if(!file.exists()) {
    		return null;
    	}
    	
    	try {
	    	BitmapFactory.Options bfo = new BitmapFactory.Options();
	    	bfo.inJustDecodeBounds = true;
	    	
	    	BitmapFactory.decodeFile(_strFilePath, bfo);
	    	
	    	bfo.inSampleSize = 1;
	    	bfo.inJustDecodeBounds = false;
	    	bfo.inPurgeable = true;
	    	bfo.inDither = true;
	    	
	    	if(bfo.outWidth > 2000 || bfo.outHeight > 10000) {			//저 사이즈 이상이면 4배 축소.
	    		bfo.inSampleSize = 4;
	    	} else if(bfo.outWidth > 1000 || bfo.outHeight > 3000) {	//저 사이즈 이상이면 2배 축소.
	    		bfo.inSampleSize = 2;
	    	}
	    	
	    	final Bitmap bitmap = BitmapFactory.decodeFile(_strFilePath, bfo);
	    	
	    	if(bitmap != null)
	    		return bitmap;
	    	else 
	    		return null;
    	
    	} catch (OutOfMemoryError e) {
	    	return null;
    	} catch (Exception e) {
    		return null;
    	}
    }

	@TargetApi(Build.VERSION_CODES.HONEYCOMB) public static void saveImage(Context context, Bitmap bitmap, String fileName, boolean needRecycle) {

		if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
			(new BackgroundSaveImage(context, bitmap, fileName, needRecycle))
				.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			(new BackgroundSaveImage(context, bitmap, fileName, needRecycle)).execute();
		}
	}
	
//////////////////////////// Classes.
	
	public static class BackgroundFileCaching extends AsyncTask<Void, Void, Void> {
     	
		private Context context;
     	private Bitmap bitmap;
     	private String fileName;
     	private boolean needRecycle;

     	public BackgroundFileCaching(Context context, Bitmap bitmap, String fileName, boolean needRecycle) {

     		this.context = context;
     		this.bitmap = bitmap;
     		this.fileName = fileName;
     		this.needRecycle = needRecycle;
     	}
     	
 		@Override
 		protected Void doInBackground(Void... arg0) {
 			
 			ByteArrayOutputStream stream;
 			byte[] byteArray;
 			File file, removeFile;
 			FileOutputStream fileOutput = null;
 			
 			try{
 				stream = new ByteArrayOutputStream();
 				bitmap.compress(CompressFormat.JPEG, 100, stream);
 				byteArray = stream.toByteArray();
 	
 				final String strImageFilePath = context.getCacheDir() + fileName + ".cache";
 				
 		        removeFile = new File(strImageFilePath);

 		        if(removeFile.isFile()) {
 		        	removeFile.delete();
 		        }
 		        
 		        file = new File(strImageFilePath);
 				fileOutput = new FileOutputStream(file);
 				fileOutput.write(byteArray);
 				return null; 
 			} catch (OutOfMemoryError e) {
 				e.printStackTrace();
 				return null;
 			} catch (Exception e) {
 				e.printStackTrace();
 				return null;
 			} finally {
 				try {
 					if(fileOutput != null) {
 						fileOutput.close();
 					}
 				} catch(Exception e) {
 					e.printStackTrace();
 				}
 			}
 		}
 		
 		@Override
     	public void onPostExecute(Void v) {
 			
 			if(bitmap != null && needRecycle) {
				bitmap.recycle();
				bitmap = null;
			}
     	}
	}
	
	public static class BackgroundLoadFileCache extends AsyncTask<Void, Void, Void> {

		private Context context;
		private Bitmap bitmap;
     	private String fileName;
     	private OnAfterLoadBitmap onAfterLoadBitmap;

     	public BackgroundLoadFileCache(Context context, String fileName, OnAfterLoadBitmap onAfterLoadBitmap) {
     		
     		this.context = context;
     		this.fileName = fileName;
     		this.onAfterLoadBitmap = onAfterLoadBitmap;
     	}
     	
 		@Override
 		protected Void doInBackground(Void... arg0) {
 			
	    	final String strImageFilePath = context.getCacheDir() + fileName + ".cache";	//이미지의 경로와 파일명.
	    	
	    	File cacheFile = new File(strImageFilePath);
	    	if(!cacheFile.isFile()) {
	    		return null;
	    	}
	    	
	    	bitmap = SafeDecodeBitmapFile(strImageFilePath);
 			return null;
 		}
 		
 		@Override
     	public void onPostExecute(Void v) {
 			
 			if(onAfterLoadBitmap != null) {
 				if(bitmap != null 
 	 					&& !bitmap.isRecycled()) {
 	 				onAfterLoadBitmap.onAfterLoadBitmap(bitmap);
 		    	} else {
 		    		onAfterLoadBitmap.onAfterLoadBitmap(null);
 		    	}
 			}
     	}
	}

	public static class BackgroundSaveImage extends AsyncTask<Void, Void, Void> {
     	
		private Context context;
     	private Bitmap bitmap;
     	private String fileName;
     	private boolean needRecycle;
     	private String url;

     	public BackgroundSaveImage(Context context, Bitmap bitmap, String fileName, boolean needRecycle) {

     		this.context = context;
     		this.bitmap = bitmap;
     		this.fileName = fileName;
     		this.needRecycle = needRecycle;
     	}
     	
 		@Override
 		protected Void doInBackground(Void... arg0) {
 			
 			url = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, fileName, "");

 			if(url == null) {
 				ToastUtils.showToast(R.string.failToSaveImage);
 			} else {
 				ToastUtils.showToast(R.string.saveImageCompleted);
 			}
 			return null;
 		}
 		
 		@Override
     	public void onPostExecute(Void v) {

 			LogUtils.log("###ImageCacheUtil.onPostExecute.  filename : " + fileName + ", url :" + url);
 			
// 			context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://"+ Environment.getExternalStorageDirectory())));
// 			MediaScannerConnection.scanFile(context,
// 	                new String[] {url}, null,
// 	                new MediaScannerConnection.OnScanCompletedListener() {
//
// 	                    public void onScanCompleted(String path, Uri uri) {
// 	                        LogUtils.log("###ImageCacheUtil.onScanCompleted.  ");
// 	                    }
// 	                });
// 			context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse(uriString)));
// 			SingleMediaScanner sms = (new SingleMediaScanner(context, url));
 			
 			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { 
// 				 Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE); 
// 				 Uri contentUri = Uri.parse(url); 
// 				 mediaScanIntent.setData(contentUri); 
// 				 context.sendBroadcast(mediaScanIntent); 
 				
 	 			MediaScannerConnection.scanFile(context,
	                new String[] {url}, null,
	                new MediaScannerConnection.OnScanCompletedListener() {

	                    public void onScanCompleted(String path, Uri uri) {
	                    	String printString = "###ImageCacheUtil.onScanCompleted.  \npath : " + path;
	                    	
	                    	if(uri != null) {
	                    		printString += "\nuri : " + uri.toString();
	                    	}
	                    	
	                        LogUtils.log(printString);
	                    }
	                });
			
 			} else { 
 				context.sendBroadcast(
 						new Intent(Intent.ACTION_MEDIA_MOUNTED, 
 						Uri.parse("file://"+ Environment.getExternalStorageDirectory()))); 
 			}
 			
 			if(bitmap != null && needRecycle) {
				bitmap.recycle();
				bitmap = null;
			}
     	}
	}
	
//////////////////////////// Interfaces.
	
	public interface OnAfterLoadBitmap {
		
		public void onAfterLoadBitmap(Bitmap bitmap);
	}
}
