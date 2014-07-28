package com.example.androidvolleytest;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.outspoken_kid.classes.OutSpokenApplication;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ToastUtils;

public class IntroActivity extends Activity {

	private BgInfos bgInfos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		OutSpokenApplication.initWithActivity(this);
		setContentView(R.layout.activity_intro);
		
		String url = "http://112.169.61.103/externalapi/public/common/common_data?image_size=640&sb_id=clubmass";
		downloadString(url);
	}
	
	public void downloadString(String url) {
		
		DownloadUtils.downloadString(url, new OnJSONDownloadListener() {
			
			@Override
			public void onError(String url) {
				ToastUtils.showToast("Fail to download info.");
				finish();
			}
			
			@Override
			public void onCompleted(String url, JSONObject objJSON) {
				
				bgInfos = new BgInfos(objJSON);
				downloadBitmap(bgInfos.urls.get(0));
			}
		});
	}
	
	public void downloadBitmap(String url) {
		
		DownloadUtils.downloadBitmap(url, null, new OnBitmapDownloadListener() {
			
			@Override
			public void onError(String url, ImageView ivImage) {
				LogUtils.log("###IntroActivity.onError.  url : " + url);
				ToastUtils.showToast("Fail to download image.");
				finish();
			}
			
			@Override
			public void onCompleted(String url, ImageView ivImage, Bitmap bitmap) {
				launchMainActivity();
			}
		});
	}
	
	public void launchMainActivity() {
		
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("bgInfos", bgInfos);
		startActivity(intent);
		finish();
	}
}
