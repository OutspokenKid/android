package com.cmons.byecarplus.classes;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.outspoken_kid.R;
import com.outspoken_kid.activities.BaseFragmentActivity;

public abstract class BCPFragmentActivity extends BaseFragmentActivity {
	
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

		return "bcpd1";
	}
	
	@Override
	public String getCookieName_S() {

		return "bcps";
	}
}