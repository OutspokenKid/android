package com.cmons.cph;

import java.util.List;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.cmons.cph.classes.CphApplication;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.User;
import com.outspoken_kid.classes.RequestManager;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;

public class IntroActivity extends Activity {

	public static boolean isInIntro;
	
	private ImageView ivBg;
	private ImageView ivImage;
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);
		CphApplication.initWithActivity(this);
		
		bindViews();
		setSizes();
		
		if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			 ivBg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
			 ivImage.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		if(ivBg != null) {
			
			try {
				ivBg.setImageResource(R.drawable.bg);
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
		}
		
		if(ivImage != null) {
			
			try {
				ivImage.setImageResource(R.drawable.splash_screen);
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		isInIntro = true;
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				
				checkSession();
			}
		}, 1500);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		isInIntro = false;
		
		if(ivBg != null) {
			ivBg.setImageDrawable(null);
		}
		
		if(ivImage != null) {
			ivImage.setImageDrawable(null);
		}
	}
	
	public void bindViews() {
		
		ivBg = (ImageView) findViewById(R.id.introActivity_ivBg);
		ivImage = (ImageView) findViewById(R.id.introActivity_ivImage);
	}
	
	public void setSizes() {
		
		FrameLayout.LayoutParams fp = (FrameLayout.LayoutParams) ivImage.getLayoutParams();
		fp.width = ResizeUtils.getSpecificLength(483);
		fp.height = ResizeUtils.getSpecificLength(937);
		fp.topMargin = ResizeUtils.getSpecificLength(150);
	}

	public void checkSession() {

		LogUtils.log("###IntroActivity.checkSession.  Get Cookies from prefs. =====================");
		
		try {
			BasicClientCookie bcc1 = SharedPrefsUtils.getCookie(ShopActivity.getInstance().getCookieName_D1());
			BasicClientCookie bcc2 = SharedPrefsUtils.getCookie(ShopActivity.getInstance().getCookieName_S());
			
			if(bcc1 != null) {
				RequestManager.getCookieStore().addCookie(bcc1);
				LogUtils.log("		key : " + bcc1.getName() + ", value : " + bcc1.getValue());
			}
			
			if(bcc2 != null) {
				RequestManager.getCookieStore().addCookie(bcc2);
				LogUtils.log("		key : " + bcc2.getName() + ", value : " + bcc2.getValue());
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		LogUtils.log("###IntroActivity.checkSession.  Print Cookies from cookieStore. =====================");
		
		try {
			List<Cookie> cookies = RequestManager.getCookieStore().getCookies();
			
			for(Cookie cookie : cookies) {
				LogUtils.log("		key : " + cookie.getName() + ", value : " + cookie.getValue());
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}

		String url = CphConstants.BASE_API_URL + "users/login_check";
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("IntroActivity.checkSession.onError." + "\nurl : " + url);
				launchSignInActivity();
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("IntroActivity.checkSession.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);
					
					if(objJSON.getInt("result") == 1) {

						User user = new User(objJSON.getJSONObject("user"));
						
						//도매.
						if(user.getRole() < 200){
							launchWholesaleActivity(user);
							
						//소매.
						} else {
							launchRetailActivity(user);
						}
					} else {
						launchSignInActivity();
					}
					
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void launchSignInActivity() {
		
		Intent intent = new Intent(this, SignInActivity.class);
		
		if(getIntent() != null && getIntent().hasExtra("pushObject")) {
			intent.putExtra("pushObject", getIntent().getSerializableExtra("pushObject"));
		}
		
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		finish();
	}
	
	public void launchWholesaleActivity(User user) {

		Intent intent = new Intent(this, WholesaleActivity.class);
		intent.putExtra("user", user);
		
		if(getIntent() != null && getIntent().hasExtra("pushObject")) {
			intent.putExtra("pushObject", getIntent().getSerializableExtra("pushObject"));
		}
		
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		finish();
	}
	
	public void launchRetailActivity(User user) {

		Intent intent = new Intent(this, RetailActivity.class);
		intent.putExtra("user", user);
		
		if(getIntent() != null && getIntent().hasExtra("pushObject")) {
			intent.putExtra("pushObject", getIntent().getSerializableExtra("pushObject"));
		}
		
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		finish();
	}
}
