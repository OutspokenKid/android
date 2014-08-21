package com.zonecomms.clubvera.classes;

import android.os.Bundle;
import android.view.View;

import com.outspoken_kid.activities.BaseActivity;
import com.zonecomms.clubvera.R;
import com.zonecomms.clubvera.fragments.MainPage;

public abstract class ZonecommsActivity extends BaseActivity {

	protected View loadingView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public int getCustomFontResId() {

		return R.string.customFont;
	}
	
	@Override
	public void finish() {
		
		super.finish();
		
		if(ZonecommsApplication.getActivity() != null) {
			if(ZonecommsApplication.getActivity().getFragmentsSize() != 0
					&& ZonecommsApplication.getActivity().getTopFragment() instanceof MainPage) {
				overridePendingTransition(R.anim.slide_in_from_top, R.anim.slide_out_to_bottom);
			} else {
				overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
			}
		}
	}
	
	@Override
	public void onBackPressed() {

		finish();
	}
	
	@Override
	public void onMenuPressed() {
		// TODO Auto-generated method stub
		
	}
}
