package com.cmons.cph;

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
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

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
		checkSession();
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

		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {

				launchSignInActivity();
			}
		}, 2000);
	}
	
	public void launchSignInActivity() {
		
		Intent intent = new Intent(this, SignInActivity.class);
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		finish();
	}
}
