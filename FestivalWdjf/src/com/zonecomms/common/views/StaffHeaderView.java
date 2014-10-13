package com.zonecomms.common.views;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.zonecomms.common.models.Staff;
import com.zonecomms.festivalwdjf.IntentHandlerActivity;
import com.zonecomms.festivalwdjf.R;

public class StaffHeaderView extends RelativeLayout {

	private static int madeCount = 11100;

	private Staff staff;
	
	private TextView tvType;
	private TextView tvName;
	private View web;
	private View facebook;
	private View twitter;
	private View soundCloud;
	private View youtube;
	
	public StaffHeaderView(Context context) {
		this(context, null);
	}
	
	public StaffHeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public void init() {

		madeCount += 10;
		ResizeUtils.viewResize(624, 150, this, 1, Gravity.CENTER_HORIZONTAL, new int[]{0, 8, 0, 8});
		RelativeLayout.LayoutParams rp = null;
		int textWidth = ResizeUtils.getSpecificLength(210);
		int textHeight = ResizeUtils.getSpecificLength(40);
		int buttonLength = ResizeUtils.getSpecificLength(80);
		int p = ResizeUtils.getSpecificLength(8);
		
		//Type.			id : 0
		tvType = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(textWidth, textHeight);
		rp.topMargin = ResizeUtils.getSpecificLength(35);
		rp.addRule(ALIGN_PARENT_LEFT);
		rp.addRule(ALIGN_PARENT_TOP);
		tvType.setLayoutParams(rp);
		tvType.setId(madeCount);
		tvType.setGravity(Gravity.CENTER);
		tvType.setSingleLine();
		tvType.setEllipsize(TruncateAt.END);
		tvType.setTextColor(Color.WHITE);
		FontInfo.setFontSize(tvType, 26);
		this.addView(tvType);
		
		//Name.			id : 1
		tvName = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(textWidth, LayoutParams.WRAP_CONTENT);
		rp.addRule(ALIGN_LEFT, madeCount);
		rp.addRule(BELOW, madeCount);
		tvName.setLayoutParams(rp);
		tvName.setId(madeCount + 1);
		tvName.setGravity(Gravity.CENTER);
		tvName.setMaxLines(2);
		tvName.setEllipsize(TruncateAt.END);
		tvName.setTextColor(Color.WHITE);
		FontInfo.setFontSize(tvName, 22);
		this.addView(tvName);
		
		LinearLayout linear = new LinearLayout(getContext());
		rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, buttonLength);
		rp.leftMargin = p;
		rp.addRule(ALIGN_TOP, madeCount);
		rp.addRule(RIGHT_OF, madeCount);
		linear.setLayoutParams(rp);
		this.addView(linear);

		LinearLayout.LayoutParams lp;
		lp = new LinearLayout.LayoutParams(buttonLength, buttonLength);
		
		//Web.
		web = new View(getContext());
		web.setLayoutParams(lp);
		web.setBackgroundResource(R.drawable.btn_staff_web);
		web.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(staff != null && !StringUtils.isEmpty(staff.getUrl_webpage())) {
					Uri uri = Uri.parse(staff.getUrl_webpage());
					IntentHandlerActivity.actionByUri(uri);
				}
			}
		});
		linear.addView(web);
		
		//Facebook.
		facebook = new View(getContext());
		lp = new LinearLayout.LayoutParams(buttonLength, buttonLength);
		facebook.setLayoutParams(lp);
		facebook.setBackgroundResource(R.drawable.btn_staff_facebook);
		facebook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(staff != null && !StringUtils.isEmpty(staff.getUrl_facebook())) {
					Uri uri = Uri.parse(staff.getUrl_facebook());
					IntentHandlerActivity.actionByUri(uri);
				}
			}
		});
		linear.addView(facebook);
		
		//Twitter.
		twitter = new View(getContext());
		twitter.setLayoutParams(lp);
		twitter.setBackgroundResource(R.drawable.btn_staff_twitter);
		twitter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(staff != null && !StringUtils.isEmpty(staff.getUrl_twitter())) {
					Uri uri = Uri.parse(staff.getUrl_twitter());
					IntentHandlerActivity.actionByUri(uri);
				}
			}
		});
		linear.addView(twitter);
		
		//SoundCloud.
		soundCloud = new View(getContext());
		soundCloud.setLayoutParams(lp);
		soundCloud.setBackgroundResource(R.drawable.btn_staff_soundcloud);
		soundCloud.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(staff != null && !StringUtils.isEmpty(staff.getUrl_soundCloud())) {
					Uri uri = Uri.parse(staff.getUrl_soundCloud());
					IntentHandlerActivity.actionByUri(uri);
				}
			}
		});
		linear.addView(soundCloud);
		
		//Youtube.
		youtube = new View(getContext());
		youtube.setLayoutParams(lp);
		youtube.setBackgroundResource(R.drawable.btn_staff_youtube);
		youtube.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(staff != null && !StringUtils.isEmpty(staff.getUrl_youtube())) {
					Uri uri = Uri.parse(staff.getUrl_youtube());
					IntentHandlerActivity.actionByUri(uri);
				}
			}
		});
		linear.addView(youtube);
		
		View blank = new View(getContext());
		blank.setLayoutParams(new LinearLayout.LayoutParams(0, 1, 1));
		linear.addView(blank);
	}
	
	public void setStaff(Staff staff) {
		
		if(staff == null) {
			return;
		}
		
		this.staff = staff;
		
		try {
			tvType.setText(staff.getStaff_type());
			tvName.setText(staff.getMember_name());

			if(!StringUtils.isEmpty(staff.getUrl_webpage())) {
				web.setVisibility(View.VISIBLE);
			} else{
				web.setVisibility(View.GONE);
			}
			
			if(!StringUtils.isEmpty(staff.getUrl_facebook())) {
				facebook.setVisibility(View.VISIBLE);
			} else {
				facebook.setVisibility(View.GONE);
			}
			
			if(!StringUtils.isEmpty(staff.getUrl_twitter())) {
				twitter.setVisibility(View.VISIBLE);
			} else{
				twitter.setVisibility(View.GONE);
			}

			if(!StringUtils.isEmpty(staff.getUrl_soundCloud())) {
				soundCloud.setVisibility(View.VISIBLE);
			} else {
				soundCloud.setVisibility(View.GONE);
			}

			if(!StringUtils.isEmpty(staff.getUrl_youtube())) {
				youtube.setVisibility(View.VISIBLE);
			} else {
				youtube.setVisibility(View.GONE);
			}
		} catch(Exception e) {
		}
	}
}
