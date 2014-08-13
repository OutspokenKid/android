package com.cmons.cph.classes;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.outspoken_kid.activities.BaseFragmentActivity;

public abstract class CmonsFragmentActivity extends BaseFragmentActivity {
	
	public abstract void onRefreshPage();
	public abstract void setTitleText(String title);
	
	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		//Init application.
		CphApplication.initWithActivity(this);
	}
	
	@Override
	public void setCustomAnimations(FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public int getCustomFontResId() {
		// TODO Auto-generated method stub
		return 0;
	}
	
//////////////////////////// Interfaces.
	
	public interface OnPositiveClickedListener {
		
		public void onPositiveClicked();
	}
}
