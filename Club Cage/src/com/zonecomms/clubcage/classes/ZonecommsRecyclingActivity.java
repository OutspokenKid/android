package com.zonecomms.clubcage.classes;

import com.outspoken_kid.activities.BaseRecyclingActivity;
import com.zonecomms.clubcage.R;
import com.zonecomms.clubcage.fragments.MainPage;

public abstract class ZonecommsRecyclingActivity extends BaseRecyclingActivity {

	@Override
	protected int getCustomFontResId() {

		return R.string.customFont;
	}
	
	@Override
	public void finish() {
		
		super.finish();
		
		if(ZonecommsApplication.getFragmentsSize() != 0
				&& ZonecommsApplication.getTopFragment() instanceof MainPage) {
			overridePendingTransition(R.anim.slide_in_from_top, R.anim.slide_out_to_bottom);
		} else {
			overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
		}
	}
}
