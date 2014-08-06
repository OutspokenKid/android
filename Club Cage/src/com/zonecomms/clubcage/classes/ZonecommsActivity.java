package com.zonecomms.clubcage.classes;

import android.view.animation.Animation;

import com.outspoken_kid.activities.BaseActivity;
import com.zonecomms.clubcage.R;
import com.zonecomms.clubcage.fragments.MainPage;

public abstract class ZonecommsActivity extends BaseActivity {
	
	@Override
	public int getCustomFontResId() {

		return R.string.customFont;
	}
	
	@Override
	public void finish() {
		
		super.finish();
		
		if(ZonecommsApplication.getActivity().getFragmentsSize() != 0
				&& ZonecommsApplication.getActivity().getTopFragment() instanceof MainPage) {
			overridePendingTransition(R.anim.slide_in_from_top, R.anim.slide_out_to_bottom);
		} else {
			overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
		}
	}
	
	@Override
	public Animation getLoadingViewAnimIn() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Animation getLoadingViewAnimOut() {
		// TODO Auto-generated method stub
		return null;
	}
}
