package com.cmons.cph;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.cmons.cph.models.PushObject;
import com.outspoken_kid.utils.LogUtils;

public class IntentHandlerActivity extends Activity {
	
	private View bg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		bg = new View(this);
		bg.setBackgroundResource(android.R.color.transparent);
		bg.setBackgroundColor(Color.argb(100, 100, 0, 0));
		setContentView(bg);

		if(getIntent() != null && getIntent().hasExtra("pushObject")) {
			PushObject pushObject = (PushObject) getIntent().getSerializableExtra("pushObject");
			handlingPushObject(pushObject);
		}
	}
	
	public void handlingPushObject(final PushObject pushObject) {

		try {
			if(ShopActivity.getInstance() != null
					&& ShopActivity.getInstance().getFragmentsSize() != 0) {
				LogUtils.log("###IntentHandlerActivity.handlingPushObject.  On ShopActivity"
						+ "\nmessage : " + pushObject.message
						+ "\nuriString : " + pushObject.uri);
				
				ShopActivity.getInstance().handlePushObject(pushObject);
			} else {
				LogUtils.log("###IntentHandlerActivity.handlingPushObject.  app is not running.");
				
				if(IntroActivity.isInIntro) {
					bg.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									handlingPushObject(pushObject);
								}
							});
						}
					}, 5000);
				} else {
					showIntroActivity(pushObject);
				}
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}

		finish();
	}

	public void showIntroActivity(PushObject pushObject) {

		Intent i = new Intent(this, IntroActivity.class);
		
		if(pushObject != null) {
			i.putExtra("pushObject", pushObject);
		}
		
		i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(i);
	}
	
	public static void handleInvalidUri() {
	}
}
