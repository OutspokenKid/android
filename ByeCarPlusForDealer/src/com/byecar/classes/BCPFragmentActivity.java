package com.byecar.classes;

import java.io.File;
import java.util.Set;

import org.json.JSONObject;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.byecar.byecarplusfordealer.ImageViewer;
import com.byecar.byecarplusfordealer.MultiSelectGalleryActivity;
import com.byecar.byecarplusfordealer.MultiSelectGalleryActivity.OnAfterPickImagesListener;
import com.byecar.models.PushObject;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.analytics.tracking.android.Tracker;
import com.outspoken_kid.R;
import com.outspoken_kid.activities.BaseFragmentActivity;
import com.outspoken_kid.classes.OutSpokenConstants;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;

public abstract class BCPFragmentActivity extends BaseFragmentActivity {
	
	public Bundle bundle;
	
	public abstract BCPFragment getFragmentByPageCode(int pageCode);
	public abstract void handleUri(Uri uri);
	
	public static OnAfterPickImagesListener onAfterPickImagesListener; 
	
	private View loadingView;
	
	private Tracker mTracker;
	
	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		//Init application.
		BCPApplication.initWithActivity(this);
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

	@Override
	public String getCookieName_D1() {

		return BCPConstants.COOKIE_NAME_D1;
	}
	
	@Override
	public String getCookieName_S() {

		return BCPConstants.COOKIE_NAME_S;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		try {
			switch(requestCode) {
			
			case OutSpokenConstants.REQUEST_ALBUM_MULTI:
				
				if(resultCode == RESULT_OK) {

					try {
						String[] selectedImageUrls = data.getStringArrayExtra("selectedImageUrls");
						boolean isEssential = data.getBooleanExtra("isEssential", true);
						
						if(onAfterPickImagesListener != null) {
							onAfterPickImagesListener.onAfterPickImage(selectedImageUrls, isEssential);
							return;
						}
					} catch (Exception e) {
						LogUtils.trace(e);

						if(onAfterPickImagesListener != null) {
							onAfterPickImagesListener.onAfterPickImage(null, true);
						}
						
					} catch (Error e) {
						LogUtils.trace(e);
						
						if(onAfterPickImagesListener != null) {
							onAfterPickImagesListener.onAfterPickImage(null, true);
						}
					}
				}
				
				break;
			}

		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
//////////////////// Custom methods.

	public void checkSession(final OnAfterCheckSessionListener onAfterCheckSessionListener) {

		String url = BCPAPIs.SIGN_CHECK_URL;
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("BCPFragmentActivity.checkSession.onError." + "\nurl : " + url);

				if(onAfterCheckSessionListener != null) {
					onAfterCheckSessionListener.onAfterCheckSession(false, null);
				}
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("BCPFragmentActivity.checkSession.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						onAfterCheckSessionListener.onAfterCheckSession(true, objJSON);
					} else {
						onAfterCheckSessionListener.onAfterCheckSession(false, objJSON);
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void showPage(int pageCode, Bundle bundle) {

		String logString = "###BCPFragmentActivity.showPage.  ====================" +
				"\npageCode : " + pageCode;
		
		if(bundle != null) {
			logString += "\nhas bundle.";
			
			Set<String> keySet = bundle.keySet();
			
			for(String key : keySet) {
				logString += "\n    " + key + " : " + bundle.get(key);
			}
		} else {
			logString += "\nhas not bundle.";
		}
		
		BCPFragment cf = getFragmentByPageCode(pageCode);
		
		if(cf != null) {
			logString += "\n===============================================";
			LogUtils.log(logString);
			startPage(cf, bundle);
		} else {
			logString += "\nFail to get fragment from activity" +
					"\n===============================================";
			LogUtils.log(logString);
		}
	}

	public void showImageViewer(int imageIndex, String title, 
			String[] imageUrls, String[] thumbnailUrls) {

		Intent intent = new Intent(this, ImageViewer.class);
		
		intent.putExtra("index", imageIndex);
		
		if(title != null) {
			intent.putExtra("title", title);
		}
		
		if(imageUrls != null) {
			
			//원본을 불러오는 경우, 이미지 사이즈가 4096이 넘으면 에러나므로 리사이징.
			for(int i=0; i<imageUrls.length; i++) {
				
				if(imageUrls[i].contains(BCPAPIs.IMAGE_SERVER_URL + "/src/")) {
					imageUrls[i] = imageUrls[i].replace("/src/", "/thumb/");

					if(!imageUrls[i].matches("=w[0-9]+$")) {
						
						if(imageUrls[i].contains("?")) {
							imageUrls[i] = new StringBuffer(imageUrls[i]).insert(imageUrls[i].indexOf("?"), "=w1080").toString();
						} else {
							imageUrls[i] += "=w1080";
						}						
					}
				}
			}
			
			intent.putExtra("imageUrls", imageUrls);
		}
		
		if(thumbnailUrls != null) {
			intent.putExtra("thumbnailUrls", thumbnailUrls);
		}
		
		startActivity(intent);
	}
	
	public void showImageViewer(String title, int imageResId) {
		
		Intent intent = new Intent(this, ImageViewer.class);
		
		if(title != null) {
			intent.putExtra("title", title);
		}
		
		intent.putExtra("imageResId", imageResId);
		startActivity(intent);
	}

	public void showUploadPhotoPopup(final String[] selectedImageUrls, 
			final Bitmap[] thumbnails, 
			final boolean isEssential, 
			final int targetIndex) {
		
		showSelectDialog("사진 업로드", 
				new String[]{"앨범", "카메라"}, 
				new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				Intent intent = null;
				int requestCode = 0;
				
				//앨범.
				if(which == 0) {
					showMultiImageSelectActivity(selectedImageUrls, thumbnails, isEssential, targetIndex);
					
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
	
	public void showMultiImageSelectActivity(String[] selectedImageUrls, 
			Bitmap[] thumbnails, 
			boolean isEssential,
			int targetIndex) {
		
		Intent intent = new Intent(this, MultiSelectGalleryActivity.class);
		
		intent.putExtra("titleText", getString(com.byecar.byecarplusfordealer.R.string.selectPhoto));
		intent.putExtra("titleBgColor", getResources().getColor(com.byecar.byecarplusfordealer.R.color.titlebar_bg_brown));
		intent.putExtra("selectedImageUrls", selectedImageUrls);
		intent.putExtra("thumbnails", thumbnails);
		intent.putExtra("isEssential", isEssential);
		intent.putExtra("targetIndex", targetIndex);

		startActivityForResult(intent, OutSpokenConstants.REQUEST_ALBUM_MULTI);
	}
	
	public void setLoadingView(View view) {
		
		this.loadingView = view;
	}
	
	public View getLoadingView() {
		
		return loadingView;
	}
	
	public void showLoadingView() {
		
		if(loadingView != null) {
			loadingView.setVisibility(View.VISIBLE);
		}
		
	}
	
	public void hideLoadingView() {
		
		if(loadingView != null) {
			loadingView.setVisibility(View.INVISIBLE);
		}
	}
	
	/**
	 * id : push id. 
	 * receiver_id : 받는 사람의 id.
	 * message : 유저에게 보여줄 메세지.
	 * uri : 연결할 uri.
	 * created_at : 생성된 시간.
	 * pushed_at : 보내진 시간.
	 * read_at : 유저가 읽은 시간.
	 * 
	 */
	public void handlePushObject(final PushObject pushObject) {
		
		LogUtils.log("###WholesaleActivity.handleIntent.  ");
		
		try {
			String message = pushObject.message;
			String uriString = pushObject.uri;
			
			LogUtils.log("message : " + message);
			LogUtils.log("uriString : " + uriString);

			//팝업을 보여준 후 확인을 누르면 처리.
			showAlertDialog("알림", message, "확인", "닫기", 
					new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {

					try {
						setPushRead(pushObject.id);
						handleUri(Uri.parse(pushObject.uri));
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (Error e) {
						LogUtils.trace(e);
					}
				}
			}, null);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void setPushRead(int id) {
		
		String url = BCPAPIs.NOTIFICATION_READ_URL
				+ "notification_id=" + id;
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("BCPFragmentActivity.setPushRead.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("BCPFragmentActivity.setPushRead.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);
					
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void showVideo(String youtubeId) {
		
		try{
	         Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + youtubeId));
	         startActivity(intent);                 
         }catch (ActivityNotFoundException ex){
             Intent intent=new Intent(Intent.ACTION_VIEW, 
             Uri.parse("http://www.youtube.com/watch?v="+youtubeId));
             startActivity(intent);
         }
	}
	
	public void tracking(String name) {

		LogUtils.log("######GA.tracking.  name : " + name);
		
		if(mTracker == null) {
			mTracker = GoogleAnalytics.getInstance(this).getTracker("UA-52516912-3");
		}
		
		mTracker.set(Fields.SCREEN_NAME, name);
		mTracker.send(MapBuilder.createAppView().build());
	}
	
//////////////////// Interfaces.
	
	public interface OnAfterCheckSessionListener {
		
		public void onAfterCheckSession(boolean isSuccess, JSONObject objJSON);
	}
}
