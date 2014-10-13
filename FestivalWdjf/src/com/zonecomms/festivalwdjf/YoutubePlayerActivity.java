package com.zonecomms.festivalwdjf;

import com.outspoken_kid.activities.YoutubeActivity;

public class YoutubePlayerActivity extends YoutubeActivity {

	@Override
	public String getAPIKey() {

		return getString(R.string.googleMapApiKey);
	}
}
