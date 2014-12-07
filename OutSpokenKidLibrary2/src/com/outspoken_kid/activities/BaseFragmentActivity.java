package com.outspoken_kid.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.cookie.Cookie;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.outspoken_kid.R;
import com.outspoken_kid.activities.MultiSelectGalleryActivity.OnAfterPickImageListener;
import com.outspoken_kid.classes.BaseFragment;
import com.outspoken_kid.classes.OutSpokenConstants;
import com.outspoken_kid.classes.RequestManager;
import com.outspoken_kid.interfaces.OutspokenActivityInterface;
import com.outspoken_kid.model.MultiSelectImageInfo;
import com.outspoken_kid.utils.BitmapUtils;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ImageUploadUtils.OnAfterUploadImage;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.ToastUtils;

public abstract class BaseFragmentActivity extends FragmentActivity 
		implements OutspokenActivityInterface {
	
	//For uploadImage.
	public static OnAfterPickImageListener onAfterPickImageListener;
	
	protected Context context;
	
	public abstract int getFragmentFrameResId();
	public abstract void setCustomAnimations(FragmentTransaction ft);
	public abstract String getCookieName_D1();
	public abstract String getCookieName_S();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getContentViewId());
		context = this;
		
		LogUtils.log("###BaseFragmentActivity.onCreate.  ");
		
		bindViews();
		setVariables();
		createPage();
		setListeners();
		setSizes();
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		LogUtils.log("###BaseFragmentActivity.onStart.  ");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		LogUtils.log("###BaseFragmentActivity.onResume.  ");
		
		downloadInfo();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		LogUtils.log("###BaseFragmentActivity.onPause.  ");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		LogUtils.log("###BaseFragmentActivity.onStop.  ");
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogUtils.log("###BaseFragmentActivity.onDestroy.  ");
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		LogUtils.log("###############################BaseFragmentActivity.onActivityResult.  " +
				"\nresultCode : " + resultCode +
				"\nrequestCode : " + requestCode +
				"\ndata : " + data);
		
		switch(requestCode) {
		
		case OutSpokenConstants.REQUEST_CAMERA:
			
			SharedPrefsUtils.removeVariableFromPrefs(OutSpokenConstants.PREFS_IMAGE_UPLOAD, "uploading");
			if(resultCode == RESULT_OK) {

				String[] sdCardPaths = null;
				int[] inSampleSizes = null;
				
				try {
					File file = null;
					LogUtils.log("###BaseFragmentActivity.onActivityResult.  ");
					
					String filePath = SharedPrefsUtils.getStringFromPrefs(OutSpokenConstants.PREFS_IMAGE_UPLOAD, "filePath");
					String fileName = SharedPrefsUtils.getStringFromPrefs(OutSpokenConstants.PREFS_IMAGE_UPLOAD, "fileName");
					
					file = new File(filePath, fileName);
					sdCardPaths = new String[]{file.getPath()};
					LogUtils.log("###BaseFragmentActivity.onActivityResult.  sdCardPath : " + sdCardPaths[0]);

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
			
		case OutSpokenConstants.REQUEST_ALBUM:
			
			if(resultCode == RESULT_OK) {
				
				try {
					Bundle bundle = data.getBundleExtra("infos");
					
					if(bundle == null) {
						break;
					}

					ArrayList<MultiSelectImageInfo> infos = new ArrayList<MultiSelectImageInfo>();
					int size = bundle.getInt("size");
					LogUtils.log("###BaseFragmentActivity.onActivityResult.  "
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
	
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		
		try {
			if(getCustomFontResId() != 0) {
				FontUtils.setGlobalFont(this, layoutResID, getString(getCustomFontResId()));
			}			
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	@Override
	public void showAlertDialog(int title, int message, int positive,
			OnClickListener onPositive) {

		showAlertDialog(title, message, positive, 0, 
				onPositive, null);
	}
	
	@Override
	public void showAlertDialog(String title, String message, String positive,
			OnClickListener onPositive) {

		showAlertDialog(title, message, positive, null, 
				onPositive, null);
	}

	@Override
	public void showAlertDialog(int title, int message, int positive,
			int negative, OnClickListener onPositive, OnClickListener onNegative) {

		String titleString = null;
		String messageString = null;
		String positiveString = null;
		String negativeString = null;
		
		if(title != 0) {
			titleString = getString(title);
		}
		
		if(message != 0) {
			messageString = getString(message);
		}
		
		if(positive != 0) {
			positiveString = getString(positive);
		}
		
		if(negative != 0) {
			negativeString = getString(negative);
		}
		
		showAlertDialog(titleString, messageString, positiveString, negativeString, 
				onPositive, null);
	}
	
	@Override
	public void showAlertDialog(String title, String message, String positive,
			String negative, OnClickListener onPositive, OnClickListener onNegative) {

		try {
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setTitle(title);
			adb.setPositiveButton(positive, onPositive);
			
			if(negative != null) {
				adb.setNegativeButton(negative, onNegative);
			}
			adb.setCancelable(true);
			adb.setOnCancelListener(null);
			adb.setMessage(message);
			adb.show();
		} catch(Exception e) {
			LogUtils.trace(e);
		}
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
		
		showSelectDialog("사진 업로드", 
				new String[]{"앨범", "카메라"}, 
				new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				Intent intent = null;
				int requestCode = 0;
				
				//앨범.
				if(which == 0) {
					intent = new Intent(BaseFragmentActivity.this, MultiSelectGalleryActivity.class);
					intent.putExtra("maxImageCount", maxImageCount);
					requestCode = OutSpokenConstants.REQUEST_ALBUM;
					
				//카메라.
				} else if(which == 1) {
					intent = new Intent();
					intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

				    File fileDerectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
				    String fileName = System.currentTimeMillis() + ".jpg";
				    String filePath = fileDerectory.getPath();
				    File file = new File(fileDerectory, fileName);
				    
				    SharedPrefsUtils.addDataToPrefs(OutSpokenConstants.PREFS_IMAGE_UPLOAD, "fileName", fileName);
				    SharedPrefsUtils.addDataToPrefs(OutSpokenConstants.PREFS_IMAGE_UPLOAD, "filePath", filePath);
				    
				    if(!fileDerectory.exists()) {
				    	fileDerectory.mkdirs();
				    }
				    
				    Uri uri = Uri.fromFile(file);
				    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
				    requestCode = OutSpokenConstants.REQUEST_CAMERA;
				}
				
				if(intent != null) {
					startActivityForResult(intent, requestCode);
				}
			}
		});
	}

	public void showUploadPhotoPopup(final int maxImageCount, final int color) {
		
		showSelectDialog("사진 업로드", 
				new String[]{"앨범", "카메라"}, 
				new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				Intent intent = null;
				int requestCode = 0;
				
				//앨범.
				if(which == 0) {
					intent = new Intent(BaseFragmentActivity.this, MultiSelectGalleryActivity.class);
					intent.putExtra("maxImageCount", maxImageCount);
					intent.putExtra("titleBgColor", Color.BLACK);
					
					requestCode = OutSpokenConstants.REQUEST_ALBUM;
					
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
				    SharedPrefsUtils.addDataToPrefs(OutSpokenConstants.PREFS_IMAGE_UPLOAD, "fileName", fileName);
				    SharedPrefsUtils.addDataToPrefs(OutSpokenConstants.PREFS_IMAGE_UPLOAD, "filePath", filePath);
				    
				    if(!fileDerectory.exists()) {
				    	fileDerectory.mkdirs();
				    }
				    
				    Uri uri = Uri.fromFile(file);
				    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
				    requestCode = OutSpokenConstants.REQUEST_CAMERA;
				}
				
				if(intent != null) {
					startActivityForResult(intent, requestCode);
				}
			}
		});
	}
	
	public int getFragmentsSize() {
		
		try {
			if(getSupportFragmentManager().getFragments() == null) {
				return 0;
				
			//메인 실행 후.
			} else {
				int entrySize = getSupportFragmentManager().getBackStackEntryCount();
				return entrySize + 1;
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		return 0;
	}
	
	public BaseFragment getTopFragment() {
		
		try {
			//메인 페이지 시작 전.
			if(getFragmentsSize() == 0) {
				return null;
				
			//메인 페이지.
			} else if(getFragmentsSize() == 1) {
				return (BaseFragment) getSupportFragmentManager().getFragments().get(0);
				
			//다른 최상단 페이지.
			} else {
				String fragmentTag = getSupportFragmentManager()
			    		.getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1)
			    		.getName();

			    return (BaseFragment) getSupportFragmentManager().findFragmentByTag(fragmentTag);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {	
			LogUtils.trace(e);
		}
		
		return null;
	}
	
	public BaseFragment getFragmentAt(int index) {
		
		try {
			//Fragment가 없는 경우.
			if(getFragmentsSize() == 0) {
				return null;
				
			//Fragment가 하나 밖에 없는 경우.
			} else if(getFragmentsSize() == 1) {
				return (BaseFragment) getSupportFragmentManager().getFragments().get(0);
				
			//Fragment가 여러개 있는 경우.
			} else {
				
				if(index == 0) {
					return (BaseFragment) getSupportFragmentManager().getFragments().get(0);
				} else {
					String fragmentTag = getSupportFragmentManager().getBackStackEntryAt(index - 1).getName();
					LogUtils.log("###BaseFragmentActivity.getFragmentAt.  index : " + index + ", fragmentTag : " + fragmentTag);
				    return (BaseFragment) getSupportFragmentManager().findFragmentByTag(fragmentTag);
				}
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		return null;
	}
	
	public void startPage(BaseFragment fragment, Bundle bundle) {
		
		try {
			String logString = "#################################BaseFragmentActivity.startPage.  " +
					"\nstartPage. fragment : " + fragment;
			
			if(bundle != null) {
				fragment.setArguments(bundle);
				
				for(String key : bundle.keySet()) {
					logString += "\nkey : " + key + ", value : " + bundle.get(key);
				}
			}
			
			LogUtils.log(logString);

			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

			//Set Animation.
			setCustomAnimations(ft);
			
			if(getFragmentsSize() == 0) {
				ft.add(getFragmentFrameResId(), fragment);
			} else {
				ft.replace(getFragmentFrameResId(), fragment, fragment.getFragmentTag());
				ft.addToBackStack(fragment.getFragmentTag());
			}
			
			ft.commitAllowingStateLoss();
			
			SoftKeyboardUtils.hideKeyboard(this, findViewById(android.R.id.content));
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void clearFragments(boolean needAnim) {

		int size = getFragmentsSize();
		for(int i=0; i<size; i++) {
			try {
				
				if(needAnim) {
					
					if(i == 0) {
						//Do nothing.
					} else if(i == size - 1) {
						((BaseFragment)getSupportFragmentManager().getFragments().get(i)).disableExitAnim(true);
					} else {
						((BaseFragment)getSupportFragmentManager().getFragments().get(i)).disableExitAnim(false);
					}
					
				} else {
					((BaseFragment)getSupportFragmentManager().getFragments().get(i)).disableExitAnim(false);
				}
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
		}
		
		getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

	public void closeTopPage() {

		LogUtils.log("###BaseFragmentActivity.closeTopPage.  ");
		getSupportFragmentManager().popBackStack();
	}

	public void closePageWithRefreshPreviousPage() {
		
		closeTopPage();
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				
				getTopFragment().refreshPage();
			}
		}, 500);
	}
	
	public void closePages(int size) {

		if(size == 1) {
			closeTopPage();
			return;
		}
		
		try {
			int fragmentSize = getFragmentsSize();
			int closeCount = size;
			
			if(closeCount >= fragmentSize) {
				closeCount--;
			}
			
			if(closeCount <= 0) {
				return;
			}
			
			//예.
			//fragment size : 5 - 0, 1, 2, 3, 4
			//close count : 2
			//4부터 3 이상이면 실행.
			for(int i=fragmentSize-1; i>=fragmentSize - closeCount; i--) {

				//마지막으로 닫힐 페이지, 3
				if(i == fragmentSize - closeCount) {
					//Do nothing.
					LogUtils.log("###BaseFragmentPage.closePages.  i : " + i + ", 마지막으로 닫힐 페이지.");
					
				//처음으로 닫힐 페이지, 4
				} else if(i == fragmentSize - 1) {
					LogUtils.log("###BaseFragmentPage.closePages.  i : " + i + ", 처음으로 닫힐 페이지.");
					getFragmentAt(i).disableExitAnim(true);
					
				//중간 페이지.
				} else {
					LogUtils.log("###BaseFragmentPage.closePages.  i : " + i + ", 중간 페이지(에니메이션 무시).");
					getFragmentAt(i).disableExitAnim(false);
				}
				
				getSupportFragmentManager().popBackStackImmediate();
			}
		} catch (Exception e) {
			LogUtils.trace(e);

			for(int i=0; i<size; i++) {
				getSupportFragmentManager().popBackStack();
			}
		}
	}
	
	public void closePagesWithRefreshPreviousPage(int size) {

		closePages(size);

		if(getTopFragment() != null) {
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					
					getTopFragment().refreshPage();
				}
			}, 1000);
		}
	}

	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        
        LogUtils.log("###BaseFragmentActivity.onConfigurationChanged.  " +
        		"newConfig : " + newConfig.toString());
    }

	public void saveCookies() {
		
		LogUtils.log("###BaseFragmentActivity.saveCookies =====================");
		List<Cookie> cookies = RequestManager.getCookieStore().getCookies();
		
		int size = cookies.size();
		for(int i=0; i<size; i++) {
			String prefsName = null;
			
			if(getCookieName_D1().equals(cookies.get(i).getName())) {
				prefsName = getCookieName_D1();
			} else if(getCookieName_S().equals(cookies.get(i).getName())) {
				prefsName = getCookieName_S();
			} else {
				continue;
			}

			if(prefsName != null) {
				SharedPrefsUtils.saveCookie(prefsName, cookies.get(i));
				LogUtils.log("		key : " + cookies.get(i).getName());
			}
		}
	}
	
////////////////////////////Interfaces.
	
	public class AsyncGetThumbnailAndReturnTask extends
			AsyncTask<Void, Void, Void> {

		private String[] sdCardPaths;
		int[] inSampleSizes;

		private ArrayList<MultiSelectImageInfo> infos;
		private Bitmap[] thumbnails;

		public AsyncGetThumbnailAndReturnTask(String[] sdCardPaths,
				int[] inSampleSizes) {

			this.sdCardPaths = sdCardPaths;
			this.inSampleSizes = inSampleSizes;
		}

		public AsyncGetThumbnailAndReturnTask(
				ArrayList<MultiSelectImageInfo> infos) {

			this.infos = infos;
		}

		@Override
		protected Void doInBackground(Void... params) {

			// 카메라로부터.
			if (sdCardPaths != null) {
				int size = sdCardPaths.length;
				thumbnails = new Bitmap[sdCardPaths.length];

				for (int i = 0; i < size; i++) {
					// 썸네일은 샘플링 사이즈보다 2배 작게.
					thumbnails[i] = BitmapUtils.getBitmapFromSdCardPath(
							sdCardPaths[i], inSampleSizes[i] * 2);
				}

				// 앨범으로부터.
			} else {
				int size = infos.size();
				thumbnails = new Bitmap[size];

				LogUtils.log("###AsyncGetThumbnailAndReturnTask.doInBackground.  size : "
						+ size);

				for (int i = 0; i < size; i++) {
					thumbnails[i] = MediaStore.Images.Thumbnails.getThumbnail(
							context.getContentResolver(), infos.get(i).id,
							MediaStore.Images.Thumbnails.MINI_KIND, null);
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			if (onAfterPickImageListener != null) {

				// 카메라로부터.
				if (sdCardPaths != null) {
					onAfterPickImageListener.onAfterPickImage(sdCardPaths,
							thumbnails);

					// 앨범으로부터.
				} else {
					int size = infos.size();
					sdCardPaths = new String[size];

					for (int i = 0; i < size; i++) {
						sdCardPaths[i] = infos.get(i).sdcardPath;
					}

					onAfterPickImageListener.onAfterPickImage(sdCardPaths,
							thumbnails);
				}

				LogUtils.log("###AsyncGetThumbnailAndReturnTask.onPostExecute.  "
						+ "\npath[0] : " + sdCardPaths[0]);
			} else {
				LogUtils.log("###AsyncGetThumbnailAndReturnTask.onPostExecute.  "
						+ "\nListener is null.");
			}
		}
	}
}
