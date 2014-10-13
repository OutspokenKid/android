package com.zonecomms.festivalwdjf.fragments;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.zonecomms.common.utils.ImageDownloadUtils;
import com.zonecomms.festivalwdjf.IntentHandlerActivity;
import com.zonecomms.festivalwdjf.MainActivity;
import com.zonecomms.festivalwdjf.R;
import com.zonecomms.festivalwdjf.classes.BaseFragment;
import com.zonecomms.festivalwdjf.classes.ZoneConstants;

/**
 * 217 27 92
 * d9 1b 5c
 * 
 * @author HyungGunKim
 *
 */
public class MainPage extends BaseFragment {

	private ScrollView scrollView;
	private RelativeLayout mainRelative;
	
	@Override
	protected void bindViews() {
		
		scrollView = (ScrollView) mThisView.findViewById(R.id.mainPage_scrollView);
		mainRelative = (RelativeLayout) mThisView.findViewById(R.id.mainPage_mainRelative);
	}

	@Override
	protected void setVariables() {
		
		title = getString(R.string.app_name);
	}

	@Override
	protected void createPage() {

		//Set TitleBar.
		mActivity.getTitleBar().hideHomeButton();
		
		//Set menus.
		RelativeLayout.LayoutParams rp;

		//Box 's'pace.
		int s = ResizeUtils.getSpecificLength(8);
		
		//Box 'l'ength.
		int l = ResizeUtils.getSpecificLength(150);
		
		//Length for 2x2 box.
		int l2 = l*2 + s;
		
		int bgColor1 = getResources().getColor(R.color.color_main_bg1);
		int bgColor2 = getResources().getColor(R.color.color_main_bg2);
		int bgColor3 = getResources().getColor(R.color.color_main_bg3);
		int bgColor4 = getResources().getColor(R.color.color_main_bg4);
		
		/////////////////////////////////////////

		//Notice.			id : 0
		View bgForNotice = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.leftMargin = s;
		rp.topMargin = s;
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		bgForNotice.setLayoutParams(rp);
		bgForNotice.setBackgroundColor(bgColor1);
		mainRelative.addView(bgForNotice);
		
		View notice = new View(mContext);
		notice.setLayoutParams(rp);
		notice.setId(madeCount);
		notice.setBackgroundResource(R.drawable.img_notice);
		notice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/notice";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(notice);
		
		//Event.			id : 1
		View bgForEvent = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount);
		bgForEvent.setLayoutParams(rp);
		bgForEvent.setBackgroundColor(bgColor1);
		mainRelative.addView(bgForEvent);
		
		View event = new View(mContext);
		event.setLayoutParams(rp);
		event.setId(madeCount + 1);
		event.setBackgroundResource(R.drawable.img_event);
		event.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/event";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(event);

		//Story.			id : 2
		View bgForStory = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 1);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 1);
		bgForStory.setLayoutParams(rp);
		bgForStory.setBackgroundColor(bgColor1);
		mainRelative.addView(bgForStory);
		
		View story = new View(mContext);
		story.setLayoutParams(rp);
		story.setId(madeCount + 2);
		story.setBackgroundResource(R.drawable.img_story);
		story.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/story";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(story);

		//Ticket.
		View bgForTicket = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 2);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 2);
		bgForTicket.setLayoutParams(rp);
		bgForTicket.setBackgroundColor(bgColor1);
		mainRelative.addView(bgForTicket);
		
		View ticket = new View(mContext);
		ticket.setLayoutParams(rp);
		ticket.setBackgroundResource(R.drawable.img_ticket);
		ticket.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/ticket";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(ticket);
		
		//Lineup.			id : 3
		View bgForLineup = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l2, l2);
		rp.topMargin = s;
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount);
		rp.addRule(RelativeLayout.BELOW, madeCount);
		bgForLineup.setLayoutParams(rp);
		bgForLineup.setBackgroundColor(bgColor2);
		mainRelative.addView(bgForLineup);
		
		View lineup = new View(mContext);
		lineup.setLayoutParams(rp);
		lineup.setId(madeCount + 3);
		lineup.setBackgroundResource(R.drawable.img_lineup);
		lineup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/lineup";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(lineup);
		
		//Music.
		View bgForMusic = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l2, l2);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 3);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 3);
		bgForMusic.setLayoutParams(rp);
		bgForMusic.setBackgroundColor(bgColor2);
		mainRelative.addView(bgForMusic);
		
		View music = new View(mContext);
		music.setLayoutParams(rp);
		music.setBackgroundResource(R.drawable.img_music);
		music.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/music";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(music);
		
		//Imformation.		id : 4
		View information = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l*4 + s*3, l*2 + s);
		rp.topMargin = s;
		rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		rp.addRule(RelativeLayout.BELOW, madeCount + 3);
		information.setLayoutParams(rp);
		information.setId(madeCount + 4);
		information.setBackgroundResource(R.drawable.img_information);
		information.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/information";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(information);
		
		//Video.			id : 5
		View bgForVideo = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l2, l2);
		rp.topMargin = s;
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 4);
		rp.addRule(RelativeLayout.BELOW, madeCount + 4);
		bgForVideo.setLayoutParams(rp);
		bgForVideo.setBackgroundColor(bgColor3);
		mainRelative.addView(bgForVideo);
		
		View video = new View(mContext);
		video.setLayoutParams(rp);
		video.setId(madeCount + 5);
		video.setBackgroundResource(R.drawable.img_video);
		video.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/video";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(video);
		
		//Photo.
		View bgForPhoto = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l2, l2);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 5);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 5);
		bgForPhoto.setLayoutParams(rp);
		bgForPhoto.setBackgroundColor(bgColor3);
		mainRelative.addView(bgForPhoto);
		
		View photo = new View(mContext);
		photo.setLayoutParams(rp);
		photo.setBackgroundResource(R.drawable.img_photo);
		photo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/photo";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(photo);

		//Home.			id : 6
		View bgForHome = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.topMargin = s;
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 5);
		rp.addRule(RelativeLayout.BELOW, madeCount + 5);
		bgForHome.setLayoutParams(rp);
		bgForHome.setBackgroundColor(bgColor4);
		mainRelative.addView(bgForHome);
		
		View home = new View(mContext);
		home.setLayoutParams(rp);
		home.setId(madeCount + 6);
		home.setBackgroundResource(R.drawable.img_home);
		home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(MainActivity.myInfo != null && !StringUtils.isEmpty(MainActivity.myInfo.getMember_id())) {
					String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/userhome?member_id=" 
										+ MainActivity.myInfo.getMember_id();
					IntentHandlerActivity.actionByUri(Uri.parse(uriString));
				}
			}
		});
		mainRelative.addView(home);
		
		//Member.				id : 7
		View bgForMember = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 6);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 6);
		bgForMember.setLayoutParams(rp);
		bgForMember.setBackgroundColor(bgColor4);
		mainRelative.addView(bgForMember);
		
		View member = new View(mContext);
		member.setLayoutParams(rp);
		member.setId(madeCount + 7);
		member.setBackgroundResource(R.drawable.img_member);
		member.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/member";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(member);

		//Setting.				id : 8
		View bgForSetting = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 7);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 7);
		bgForSetting.setLayoutParams(rp);
		bgForSetting.setBackgroundColor(bgColor4);
		mainRelative.addView(bgForSetting);
		
		View setting = new View(mContext);
		setting.setLayoutParams(rp);
		setting.setId(madeCount + 8);
		setting.setBackgroundResource(R.drawable.img_setting);
		setting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/setting";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(setting);

		//Vvip.
		View bgForVvip = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 8);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 8);
		bgForVvip.setLayoutParams(rp);
		bgForVvip.setBackgroundColor(bgColor4);
		mainRelative.addView(bgForVvip);

		View vvip = new View(mContext);
		vvip.setLayoutParams(rp);
		vvip.setBackgroundResource(R.drawable.img_vvip);
		vvip.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/vvip";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(vvip);
		
		//Schedule.				id : 9
		View bgForSchedule = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l2, l2);
		rp.topMargin = s;
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 6);
		rp.addRule(RelativeLayout.BELOW, madeCount + 6);
		bgForSchedule.setLayoutParams(rp);
		bgForSchedule.setBackgroundColor(bgColor3);
		mainRelative.addView(bgForSchedule);
		
		View schedule = new View(mContext);
		schedule.setLayoutParams(rp);
		schedule.setId(madeCount + 9);
		schedule.setBackgroundResource(R.drawable.img_schedule);
		schedule.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/schedule";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(schedule);
		
		//Location.
		View bgForLocation = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l2, l2);
		rp.leftMargin = s;
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 9);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 9);
		bgForLocation.setLayoutParams(rp);
		bgForLocation.setBackgroundColor(bgColor3);
		mainRelative.addView(bgForLocation);
		
		View location = new View(mContext);
		location.setLayoutParams(rp);
		location.setBackgroundResource(R.drawable.img_location);
		location.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/location";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(location);
		
		setPage(true);
	}

	@Override
	protected void setListeners() {
	}

	@Override
	protected void setSizes() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void downloadInfo() {
		setPage(true);
	}

	@Override
	protected void setPage(boolean downloadSuccess) {
		
		if(MainActivity.myInfo != null && !StringUtils.isEmpty(MainActivity.myInfo.getMember_media_src())) {
			String url = MainActivity.myInfo.getMember_media_src();
			
			LogUtils.log("###MainPage.setPage.  url : " + url);
			
			if(mActivity.getProfileView() != null) {
				if(!StringUtils.isEmpty(url) && mActivity.getProfileView().getIcon() != null) {
					ImageDownloadUtils.downloadImageImmediately(url, "", mActivity.getProfileView().getIcon(), 80, true);
				}
				
				String nickname = MainActivity.myInfo.getMember_nickname();
				if(!StringUtils.isEmpty(nickname)) {
					mActivity.getProfileView().setTitle(nickname);
				}
			}
		} else {
			LogUtils.log("###MainPage.setPage.  no");
		}
	}

	@Override
	protected int getContentViewId() {

		return R.id.mainPage_scrollView;
	}

	@Override
	public boolean onBackKeyPressed() {
		
		return false;
	}

	@Override
	public void onRefreshPage() {
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		
		if(!hidden) {
			
			mThisView.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(mThisView.getWindowToken(), 0);
				}
			}, 1000);
			mActivity.getTitleBar().hideHomeButton();
			mActivity.getTitleBar().hideWriteButton();
			
			if(mActivity.getSponserBanner() != null) {
				mActivity.getSponserBanner().hideBanner();
			}
		}
	}
	
	@Override
	public void onSoftKeyboardShown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSoftKeyboardHidden() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String generateDownloadKey() {

		return "MAINPAGE" + madeCount;
	}

	@Override
	protected int getXmlResId() {

		return R.layout.page_main;
	}
	
////////////////////// Custom methdos.

	public void setScrollToTop() {
		
		scrollView.scrollTo(0, 0);
	}
}