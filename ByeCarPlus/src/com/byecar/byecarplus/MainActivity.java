package com.byecar.byecarplus;

import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.classes.BCPAPIs;
import com.byecar.byecarplus.classes.BCPConstants;
import com.byecar.byecarplus.classes.BCPFragmentActivity;
import com.byecar.byecarplus.models.User;
import com.google.android.gcm.GCMRegistrar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.GestureSlidingLayout;
import com.outspoken_kid.views.OffsetScrollView;

public abstract class MainActivity extends BCPFragmentActivity {

	public static User user;

	protected GestureSlidingLayout gestureSlidingLayout;
	protected RelativeLayout leftView;
	protected OffsetScrollView scrollView;
	protected LinearLayout leftViewInner;
	protected TextView tvTitle1;
	protected TextView tvTitle2;
	protected TextView tvTitleIn1;
	protected TextView tvTitleIn2;
	
	protected ImageView ivProfile;
	protected ImageView ivBg;
	protected TextView tvNickname;
	protected TextView tvInfo;
	protected Button btnEdit;
	protected Button[] menuButtons;
	
	protected RelativeLayout popup;
	protected View popupBg;
	protected View popupImage;
	protected TextView tvPopupText;
	protected Button btnHome;

	public abstract void launchSignActivity();
	
//////////////////// Custom methods.
	
	public void checkSession() {
		
		checkSession(new OnAfterCheckSessionListener() {
			
			@Override
			public void onAfterCheckSession(boolean isSuccess, JSONObject objJSON) {

				try {
					if(isSuccess) {
						saveCookies();
						user = new User(objJSON.getJSONObject("user"));
						setLeftViewUserInfo();
						checkGCM();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
						launchSignActivity();
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}
			}
		});
	}
	
	public void setLeftViewUserInfo() {

		try {
			if(!StringUtils.isEmpty(user.getProfile_img_url())) {
				
				ivProfile.setTag(user.getProfile_img_url());
				DownloadUtils.downloadBitmap(user.getProfile_img_url(), new OnBitmapDownloadListener() {

					@Override
					public void onError(String url) {

						LogUtils.log("MainForUserActivity.setLeftViewUserInfo.onError." + "\nurl : " + url);
						ivProfile.setImageDrawable(null);
					}

					@Override
					public void onCompleted(String url, Bitmap bitmap) {

						try {
							LogUtils.log("MainForUserActivity.setLeftViewUserInfo.onCompleted." + "\nurl : " + url);
							ivProfile.setImageBitmap(bitmap);;
						} catch (Exception e) {
							LogUtils.trace(e);
							ivProfile.setImageDrawable(null);
						} catch (OutOfMemoryError oom) {
							LogUtils.trace(oom);
							ivProfile.setImageDrawable(null);
						}
					}
				});
			}
			
			if(!StringUtils.isEmpty(user.getNickname())) {
				tvNickname.setText(user.getNickname());
			}
			
			tvInfo.setText(null);

			if(!StringUtils.isEmpty(user.getPhone_number())) {
				FontUtils.addSpan(tvInfo, user.getPhone_number() + "\n\n", 0, 1);
			}
			
			if(!StringUtils.isEmpty(user.getAddress())) {
				FontUtils.addSpan(tvInfo, user.getAddress(), 0, 1);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void checkGCM() {

		try {
			GCMRegistrar.checkDevice(this);
			GCMRegistrar.checkManifest(this);
			
			final String regId = GCMRegistrar.getRegistrationId(this);
			
			if(regId == null || regId.equals("")) {
				GCMRegistrar.register(this, BCPConstants.GCM_SENDER_ID);
			} else {
				updateInfo(regId);
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void updateInfo(String regId) {
		
		if(user == null || regId == null) {
			return;
		}
		
		try {
			String url = BCPAPIs.REGISTER_URL
					+ "?user_id=" + user.getId()
					+ "&device_token=" + regId;
			
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {
				
				@Override
				public void onError(String url) {

					LogUtils.log("###ShopActivity.updateInfo.onError.  \nurl : " + url);
				}
				
				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					LogUtils.log("###ShopActivity.updateInfo.onCompleted.  \nresult : " + objJSON);
				}
			});
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	public void clearLeftViewUserInfo() {

		try {
			Drawable d = ivProfile.getDrawable();
	        ivProfile.setImageDrawable(null);
	        ivProfile.setImageBitmap(null);
	        
	        if (d != null) {
	            d.setCallback(null);
	        }
	         
	        if (d instanceof BitmapDrawable) {
	            Bitmap bm = ((BitmapDrawable)d).getBitmap();
	            
	            if(bm != null && !bm.isRecycled()) {
	            	bm.recycle();
	            }
	        }
			
			tvNickname.setText(null);
			tvInfo.setText(null);
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
}
