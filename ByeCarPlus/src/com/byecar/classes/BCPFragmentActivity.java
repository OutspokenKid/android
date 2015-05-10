package com.byecar.classes;

import java.util.Set;

import org.json.JSONObject;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

import com.byecar.byecarplus.ImageViewer;
import com.byecar.models.PushObject;
import com.outspoken_kid.R;
import com.outspoken_kid.activities.BaseFragmentActivity;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;

public abstract class BCPFragmentActivity extends BaseFragmentActivity {
	
	public Bundle bundle;
	
	public abstract BCPFragment getFragmentByPageCode(int pageCode);
	public abstract void handleUri(Uri uri);
	
	private View loadingView;
	
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

		return "BYECAR_D1";
	}
	
	@Override
	public String getCookieName_S() {

		return "BYECAR_S";
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
	
	public void setLoadingView(View view) {
		
		this.loadingView = view;
		
		if(view != null) {
			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					
				}
			});
		}
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
	
//////////////////// Interfaces.
	
	public interface OnAfterCheckSessionListener {
		
		public void onAfterCheckSession(boolean isSuccess, JSONObject objJSON);
	}
}
