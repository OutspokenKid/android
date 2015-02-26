package com.byecar.byecarplus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.byecar.models.PushObject;
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
			if(MainActivity.activity != null
					&& MainActivity.activity.getFragmentsSize() != 0) {
				LogUtils.log("###IntentHandlerActivity.handlingPushObject.  On MainActivity"
						+ "\nmessage : " + pushObject.message
						+ "\nuriString : " + pushObject.uri);
				
				MainActivity.activity.handlePushObject(pushObject);
			} else {
				LogUtils.log("###IntentHandlerActivity.handlingPushObject.  app is not running.");
				showSignActivity(pushObject);
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}

		finish();
	}

	public void showSignActivity(PushObject pushObject) {

		Intent i = new Intent(this, SignActivity.class);
		
		if(pushObject != null) {
			i.putExtra("pushObject", pushObject);
		}
		
		i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(i);
	}
	
	public static void handleInvalidUri() {
	}
}
