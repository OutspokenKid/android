package com.cmons.cph.classes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.cookie.Cookie;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;

import com.cmons.cph.R;
import com.outspoken_kid.activities.BaseFragmentActivity;
import com.outspoken_kid.activities.MultiSelectGalleryActivity;
import com.outspoken_kid.activities.MultiSelectGalleryActivity.OnAfterPickImageListener;
import com.outspoken_kid.classes.RequestManager;
import com.outspoken_kid.model.MultiSelectImageInfo;
import com.outspoken_kid.utils.BitmapUtils;
import com.outspoken_kid.utils.ImageUploadUtils.OnAfterUploadImage;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.ToastUtils;

public abstract class CmonsFragmentActivity extends BaseFragmentActivity {

	//For uploadImage.
	public static OnAfterPickImageListener onAfterPickImageListener;
	
	public abstract void onRefreshPage();
	public abstract void setTitleText(String title);
	public abstract void showLoadingView();
	public abstract void hideLoadingView();
	
	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		//Init application.
		CphApplication.initWithActivity(this);
	}
	
	@Override
	public void setCustomAnimations(FragmentTransaction ft) {

		if(getFragmentsSize() == 0) {
			//MainPage.
		} else if(getFragmentsSize() == 1) {
			ft.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out, 
					R.anim.abc_fade_in, R.anim.abc_fade_out);
		} else {
			ft.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left,
					R.anim.slide_in_from_left, R.anim.slide_out_to_right);
		}
	}
	
	@Override
	public int getCustomFontResId() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
	public void showSelectDialog(String title, String[] strings, 
			DialogInterface.OnClickListener onClickListener) {
		
		android.app.AlertDialog.Builder builder = null;
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
		} else {
			builder = new AlertDialog.Builder(this);
		}
		
		builder.setTitle(title); 
		builder.setItems(strings, onClickListener);
		AlertDialog alert = builder.create(); 
		alert.show(); 
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void showSelectDialog(String title, String[] strings, 
			DialogInterface.OnClickListener onPositiveClickListener,
			DialogInterface.OnCancelListener onCancelListener) {
		
		android.app.AlertDialog.Builder builder = null;
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
		} else {
			builder = new AlertDialog.Builder(this);
		}
		
		builder.setTitle(title); 
		builder.setItems(strings, onPositiveClickListener);
		builder.setOnCancelListener(onCancelListener);
		AlertDialog alert = builder.create(); 
		alert.show(); 
	}
	
	public void showUploadPhotoPopup(OnAfterUploadImage onAfterUploadImage, final int maxImageCount) {
		
//		CmonsFragmentActivity.onAfterUploadImage = onAfterUploadImage;
		
		showSelectDialog("사진 업로드", 
				new String[]{"앨범", "카메라"}, 
				new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				Intent intent = null;
				int requestCode = 0;
				
				//앨범.
				if(which == 0) {
					intent = new Intent(CmonsFragmentActivity.this, MultiSelectGalleryActivity.class);
					intent.putExtra("maxImageCount", maxImageCount);
					requestCode = CphConstants.REQUEST_ALBUM;
					
				//카메라.
				} else if(which == 1) {
					intent = new Intent();
					intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

				    File fileDerectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
				    String fileName = System.currentTimeMillis() + ".jpg";
				    String filePath = fileDerectory.getPath();
				    File file = new File(fileDerectory, fileName);
				    
				    LogUtils.log("###CmonsFramentActivity.onClick.  Set fileName, filePath." +
				    		"\nfileName : " + fileName+
				    		"\nfilePath : " + filePath);
				    SharedPrefsUtils.addDataToPrefs(CphConstants.PREFS_IMAGE_UPLOAD, "fileName", fileName);
				    SharedPrefsUtils.addDataToPrefs(CphConstants.PREFS_IMAGE_UPLOAD, "filePath", filePath);
				    
				    if(!fileDerectory.exists()) {
				    	fileDerectory.mkdirs();
				    }
				    
				    Uri uri = Uri.fromFile(file);
				    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
				    requestCode = CphConstants.REQUEST_CAMERA;
				}
				
				if(intent != null) {
					startActivityForResult(intent, requestCode);
				}
			}
		});
	}

	public void showUploadPhotoPopup(final int maxImageCount) {
		
		showSelectDialog("사진 업로드", 
				new String[]{"앨범", "카메라"}, 
				new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				Intent intent = null;
				int requestCode = 0;
				
				//앨범.
				if(which == 0) {
					intent = new Intent(CmonsFragmentActivity.this, MultiSelectGalleryActivity.class);
					intent.putExtra("maxImageCount", maxImageCount);
					requestCode = CphConstants.REQUEST_ALBUM;
					
				//카메라.
				} else if(which == 1) {
					intent = new Intent();
					intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

				    File fileDerectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
				    String fileName = System.currentTimeMillis() + ".jpg";
				    String filePath = fileDerectory.getPath();
				    File file = new File(fileDerectory, fileName);
				    
				    LogUtils.log("###CmonsFramentActivity.onClick.  Set fileName, filePath." +
				    		"\nfileName : " + fileName+
				    		"\nfilePath : " + filePath);
				    SharedPrefsUtils.addDataToPrefs(CphConstants.PREFS_IMAGE_UPLOAD, "fileName", fileName);
				    SharedPrefsUtils.addDataToPrefs(CphConstants.PREFS_IMAGE_UPLOAD, "filePath", filePath);
				    
				    if(!fileDerectory.exists()) {
				    	fileDerectory.mkdirs();
				    }
				    
				    Uri uri = Uri.fromFile(file);
				    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
				    requestCode = CphConstants.REQUEST_CAMERA;
				}
				
				if(intent != null) {
					startActivityForResult(intent, requestCode);
				}
			}
		});
	}
	
	public Bitmap getThumbnail(int id) {
		
		try {
			return MediaStore.Images.Thumbnails.getThumbnail(
					context.getContentResolver(), 
					id, 
					MediaStore.Images.Thumbnails.MINI_KIND, 
					null);

		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		return null;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		LogUtils.log("###############################.onActivityResult.  " +
				"\nresultCode : " + resultCode +
				"\nrequestCode : " + requestCode +
				"\ndata : " + data);
		
		switch(requestCode) {
		
		case CphConstants.REQUEST_CAMERA:
			
			SharedPrefsUtils.removeVariableFromPrefs(CphConstants.PREFS_IMAGE_UPLOAD, "uploading");
			if(resultCode == RESULT_OK) {

				String[] sdCardPaths = null;
				int[] inSampleSizes = null;
				
				try {
					File file = null;
					LogUtils.log("###CmonsFragmentActivity.onActivityResult.  ");
					
					String filePath = SharedPrefsUtils.getStringFromPrefs(CphConstants.PREFS_IMAGE_UPLOAD, "filePath");
					String fileName = SharedPrefsUtils.getStringFromPrefs(CphConstants.PREFS_IMAGE_UPLOAD, "fileName");
					
					file = new File(filePath, fileName);
					sdCardPaths = new String[]{file.getPath()};
					LogUtils.log("###CmonsFragmentActivity.onActivityResult.  sdCardPath : " + sdCardPaths[0]);

					int inSampleSize = 1;
					
					if(ResizeUtils.getScreenWidth() >= 720) {
						inSampleSize = BitmapUtils.getBitmapInSampleSize(file, 720);
					} else {
						inSampleSize = BitmapUtils.getBitmapInSampleSize(file, 640);
					}
					
					inSampleSizes = new int[]{inSampleSize};
					
					if(onAfterPickImageListener != null) {
						(new AsyncGetThumbnailAndReturnTask(sdCardPaths, inSampleSizes)).execute();
						return;
					}
				} catch (OutOfMemoryError oom) {
					oom.printStackTrace();
					ToastUtils.showToast(R.string.failToLoadBitmap_OutOfMemory);
				} catch (Error e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToLoadBitmap);
				} catch (Exception e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToLoadBitmap);
				}
			} else {
				ToastUtils.showToast(R.string.canceled);
			}
			break;
			
		case CphConstants.REQUEST_ALBUM:
			
			SharedPrefsUtils.removeVariableFromPrefs(CphConstants.PREFS_IMAGE_UPLOAD, "uploading");
			
			if(resultCode == RESULT_OK) {
				
				try {
					Bundle bundle = data.getBundleExtra("infos");
					
					if(bundle == null) {
						break;
					}

					ArrayList<MultiSelectImageInfo> infos = new ArrayList<MultiSelectImageInfo>();
					int size = bundle.getInt("size");
					LogUtils.log("###CmonsFragmentActivity.onActivityResult.  "
							+ "size : " + size);
					
					for(int i=0; i<size; i++) {
						infos.add((MultiSelectImageInfo)bundle.getSerializable("" + i));
						LogUtils.log(i + " : " + infos.get(i).sdcardPath);
					}
					
					if(onAfterPickImageListener != null) {
						(new AsyncGetThumbnailAndReturnTask(infos)).execute();
						return;
					}
				} catch (OutOfMemoryError oom) {
					oom.printStackTrace();
					ToastUtils.showToast(R.string.failToLoadBitmap_OutOfMemory);
				} catch (Error e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToLoadBitmap);
				} catch (Exception e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToLoadBitmap);
				}
			} else {
				ToastUtils.showToast(R.string.canceled);
			}

			break;
		}
		
		if(onAfterPickImageListener != null) {
			onAfterPickImageListener.onAfterPickImage(null, null);
		}
	}
	
	public void returnOnAfterPickImageListener(ArrayList<MultiSelectImageInfo> infos) {
		
	}
	
	public void saveCookies() {
		
		LogUtils.log("###CmonsFragmentActivity.saveCookies =====================");
		List<Cookie> cookies = RequestManager.getCookieStore().getCookies();
		
		int size = cookies.size();
		for(int i=0; i<size; i++) {

			String prefsName = null;
			
			if("CPH_D1".equals(cookies.get(i).getName())) {
				prefsName = CphConstants.PREFS_COOKIE_CPH_D1;
			} else if("CPH_S".equals(cookies.get(i).getName())) {
				prefsName = CphConstants.PREFS_COOKIE_CPH_S;
			} else {
				continue;
			}
			
			SharedPrefsUtils.saveCookie(prefsName, cookies.get(i));
			LogUtils.log("		key : " + cookies.get(i).getName());
		}
	}
	
//////////////////////////// Interfaces.
	
	public class AsyncGetThumbnailAndReturnTask extends AsyncTask<Void, Void, Void> {

		private String[] sdCardPaths;
		int[] inSampleSizes;
		
		private ArrayList<MultiSelectImageInfo> infos;
		private Bitmap[] thumbnails;
		
		public AsyncGetThumbnailAndReturnTask(String[] sdCardPaths, int[] inSampleSizes) {

			this.sdCardPaths = sdCardPaths;
			this.inSampleSizes = inSampleSizes;
		}
		
		public AsyncGetThumbnailAndReturnTask(ArrayList<MultiSelectImageInfo> infos) {

			this.infos = infos;
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			
			//카메라로부터.
			if(sdCardPaths != null) {
				int size = sdCardPaths.length;
				thumbnails = new Bitmap[sdCardPaths.length];
				
				for(int i=0; i<size; i++) {
					//썸네일은 샘플링 사이즈보다 2배 작게.
					thumbnails[i] = BitmapUtils.getBitmapFromSdCardPath(sdCardPaths[i], inSampleSizes[i]*2);
				}
				
			//앨범으로부터.
			} else {
				int size = infos.size();
				thumbnails = new Bitmap[size];

				LogUtils.log("###AsyncGetThumbnailAndReturnTask.doInBackground.  size : " + size);
				
				for(int i=0; i<size; i++) {
					thumbnails[i] = MediaStore.Images.Thumbnails.getThumbnail(
							context.getContentResolver(), 
							infos.get(i).id, 
							MediaStore.Images.Thumbnails.MINI_KIND, 
							null);
				}
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			
			if(onAfterPickImageListener != null) {
				
				//카메라로부터.
				if(sdCardPaths != null) {
					onAfterPickImageListener.onAfterPickImage(sdCardPaths, thumbnails);
					
				//앨범으로부터.
				} else {
					int size = infos.size();
					sdCardPaths = new String[size];
					
					for(int i=0; i<size; i++) {
						sdCardPaths[i] = infos.get(i).sdcardPath;
					}
					
					onAfterPickImageListener.onAfterPickImage(sdCardPaths, thumbnails);
				}
				
				LogUtils.log("###AsyncGetThumbnailAndReturnTask.onPostExecute.  "
						+ "\npath[0] : " + sdCardPaths[0]);
			} else {
				LogUtils.log("###AsyncGetThumbnailAndReturnTask.onPostExecute.  "
						+ "\nListener is null.");
			}
		}
	}
	
	public interface OnPositiveClickedListener {
		
		public void onPositiveClicked();
	}
}