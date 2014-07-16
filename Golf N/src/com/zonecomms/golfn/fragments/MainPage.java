package com.zonecomms.golfn.fragments;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader.OnCompletedListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.zonecomms.common.utils.ImageDownloadUtils;
import com.zonecomms.golfn.IntentHandlerActivity;
import com.zonecomms.golfn.MainActivity;
import com.zonecomms.golfn.R;
import com.zonecomms.golfn.classes.BaseFragment;
import com.zonecomms.golfn.classes.ZoneConstants;

/**
 * 217 27 92
 * d9 1b 5c
 * 
 * @author HyungGunKim
 *
 */
public class MainPage extends BaseFragment {

	public static String bannerLinkUrl;
	
	private ScrollView scrollView;
	private RelativeLayout mainRelative;
	private FrameLayout boardMenuForMarket;
	private FrameLayout boardMenuForBanner;
	private FrameLayout boardMenuForGolfcourse;
	
	private ImageView[] customViews;
	
	private View coverForMenu;

	private boolean animating;
	private AlphaAnimation aaIn, aaOut, aaIn2, aaOut2;
	
	@Override
	protected void bindViews() {
		
		scrollView = (ScrollView) mThisView.findViewById(R.id.mainPage_scrollView);
		mainRelative = (RelativeLayout) mThisView.findViewById(R.id.mainPage_mainRelative);
	}

	@Override
	protected void setVariables() {
		
		title = null;
		customViews = new ImageView[5];
	}

	@Override
	protected void createPage() {

		//Set TitleBar.
		mActivity.getTitleBar().hideHomeButton();
		
		//Set menus.
		RelativeLayout.LayoutParams rp;
		
		//Box 'l'ength.
		int l = ResizeUtils.getSpecificLength(150);
		
		//Box 's'pace.
		int s = ResizeUtils.getSpecificLength(8);
		
		//Length of big size menus.
		int b = l*2 + s;
		
		//Color of top menus.
		int topMenuColor = Color.WHITE;
		
		madeCount += 27;
		
		//Logo_maniaReport.			id : 0
		View bgForLogo_maniaReport = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.topMargin = s;
		rp.leftMargin = s;
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		bgForLogo_maniaReport.setLayoutParams(rp);
		bgForLogo_maniaReport.setBackgroundColor(topMenuColor);
		mainRelative.addView(bgForLogo_maniaReport);
		
		View logo_maniaReport = new View(mContext);
		logo_maniaReport.setLayoutParams(rp);
		logo_maniaReport.setId(madeCount);
		logo_maniaReport.setBackgroundResource(R.drawable.img_main_logo_maniareport);
		logo_maniaReport.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/logo_maniaReport";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(logo_maniaReport);
		
		//MyHome.			id : 1
		View bgForMyHome = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.leftMargin = s;
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount);
		bgForMyHome.setLayoutParams(rp);
		bgForMyHome.setBackgroundColor(topMenuColor);
		mainRelative.addView(bgForMyHome);
		
		View myHome = new View(mContext);
		myHome.setLayoutParams(rp);
		myHome.setId(madeCount + 1);
		myHome.setBackgroundResource(R.drawable.img_main_myhome);
		myHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/userhome" +
						"?member_id=" + MainActivity.myInfo.getMember_id() +
						"&menuIndex=0";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(myHome);
		
		//Message.					id : 2
		View bgFormessage = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.leftMargin = s;
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 1);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 1);
		bgFormessage.setLayoutParams(rp);
		bgFormessage.setBackgroundColor(topMenuColor);
		mainRelative.addView(bgFormessage);
		
		View message = new View(mContext);
		message.setLayoutParams(rp);
		message.setId(madeCount + 2);
		message.setBackgroundResource(R.drawable.img_main_message);
		message.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/userhome" +
						"?member_id=" + MainActivity.myInfo.getMember_id() +
						"&menuIndex=3";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(message);
		
		//Cooperation.					id : 3
		View bgForCooperation = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.leftMargin = s;
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 2);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 2);
		bgForCooperation.setLayoutParams(rp);
		bgForCooperation.setBackgroundColor(topMenuColor);
		mainRelative.addView(bgForCooperation);
		
		View cooperation = new View(mContext);
		cooperation.setLayoutParams(rp);
		cooperation.setId(madeCount + 3);
		cooperation.setBackgroundResource(R.drawable.img_main_cooperation);
		cooperation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/cooperation";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(cooperation);
		
		//Event.					id : 4
		View bgForEvent = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.topMargin = s;
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount);
		rp.addRule(RelativeLayout.BELOW, madeCount);
		bgForEvent.setLayoutParams(rp);
		bgForEvent.setBackgroundColor(topMenuColor);
		mainRelative.addView(bgForEvent);
		
		View event = new View(mContext);
		event.setLayoutParams(rp);
		event.setId(madeCount + 4);
		event.setBackgroundResource(R.drawable.img_main_event);
		event.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/event";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(event);

		//News.						id : 5
		View bgForNews = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.leftMargin = s;
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 4);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 4);
		bgForNews.setLayoutParams(rp);
		bgForNews.setBackgroundColor(topMenuColor);
		mainRelative.addView(bgForNews);
		
		View news = new View(mContext);
		news.setLayoutParams(rp);
		news.setId(madeCount + 5);
		news.setBackgroundResource(R.drawable.img_main_news);
		news.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/news";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(news);
		
		//Lesson. 					id : 6
		View bgForLesson = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.topMargin = s;
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 4);
		rp.addRule(RelativeLayout.BELOW, madeCount + 4);
		bgForLesson.setLayoutParams(rp);
		bgForLesson.setBackgroundColor(topMenuColor);
		mainRelative.addView(bgForLesson);
		
		View lesson = new View(mContext);
		lesson.setLayoutParams(rp);
		lesson.setId(madeCount + 6);
		lesson.setBackgroundResource(R.drawable.img_main_lesson);
		lesson.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/lesson";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(lesson);
		
		//ManiaTV.
		View bgForManiaTV = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.leftMargin = s;
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 6);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 6);
		bgForManiaTV.setLayoutParams(rp);
		bgForManiaTV.setBackgroundColor(topMenuColor);
		mainRelative.addView(bgForManiaTV);
		
		View maniaTV = new View(mContext);
		maniaTV.setLayoutParams(rp);
		maniaTV.setBackgroundResource(R.drawable.img_main_maniatv);
		maniaTV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/maniatv";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(maniaTV);
		
		//ManiaMarket.				id : 7
		View bgForManiaMarket = new View(mContext);
		rp = new RelativeLayout.LayoutParams(b, b);
		rp.leftMargin = s;
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 5);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 5);
		bgForManiaMarket.setLayoutParams(rp);
		bgForManiaMarket.setBackgroundColor(topMenuColor);
		mainRelative.addView(bgForManiaMarket);
		
		View maniaMarket = new View(mContext);
		maniaMarket.setLayoutParams(rp);
		maniaMarket.setId(madeCount + 7);
		maniaMarket.setBackgroundResource(R.drawable.img_main_maniamarket);
		maniaMarket.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showBoardMenu(boardMenuForMarket);
			}
		});
		mainRelative.addView(maniaMarket);
	
		//Banner.					id : 8
		View bgForBanner = new View(mContext);
		rp = new RelativeLayout.LayoutParams(b, b);
		rp.topMargin = s;
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 6);
		rp.addRule(RelativeLayout.BELOW, madeCount + 6);
		bgForBanner.setLayoutParams(rp);
		bgForBanner.setBackgroundColor(topMenuColor);
		mainRelative.addView(bgForBanner);
		
		ImageView banner = new ImageView(mContext);
		banner.setLayoutParams(rp);
		banner.setId(madeCount + 8);
		banner.setBackgroundResource(R.drawable.img_main_banner);
		banner.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showBoardMenu(boardMenuForBanner);
			}
		});
		banner.setScaleType(ScaleType.FIT_XY);
		customViews[0] = banner;
		mainRelative.addView(banner);
		
		//Attendance.					id : 9
		View bgForAttendance = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.leftMargin = s;
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 8);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 8);
		bgForAttendance.setLayoutParams(rp);
		bgForAttendance.setBackgroundColor(topMenuColor);
		mainRelative.addView(bgForAttendance);
		
		View attendance = new View(mContext);
		attendance.setLayoutParams(rp);
		attendance.setId(madeCount + 9);
		attendance.setBackgroundResource(R.drawable.img_main_attendance);
		attendance.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/attendance";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(attendance);
		
		//Review.
		View bgForReview = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.leftMargin = s;
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 9);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 9);
		bgForReview.setLayoutParams(rp);
		bgForReview.setBackgroundColor(topMenuColor);
		mainRelative.addView(bgForReview);
		
		View review = new View(mContext);
		review.setLayoutParams(rp);
		review.setBackgroundResource(R.drawable.img_main_review);
		review.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/review";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(review);
		
		//QnA.						id : 10
		View bgForQnA = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.leftMargin = s;
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 8);
		rp.addRule(RelativeLayout.ALIGN_BOTTOM, madeCount + 8);
		bgForQnA.setLayoutParams(rp);
		bgForQnA.setBackgroundColor(topMenuColor);
		mainRelative.addView(bgForQnA);
		
		View qnA = new View(mContext);
		qnA.setLayoutParams(rp);
		qnA.setId(madeCount + 10);
		qnA.setBackgroundResource(R.drawable.img_main_qna);
		qnA.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/qna";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(qnA);
		
		//Photo.
		View bgForPhoto = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.leftMargin = s;
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 10);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 10);
		bgForPhoto.setLayoutParams(rp);
		bgForPhoto.setBackgroundColor(topMenuColor);
		mainRelative.addView(bgForPhoto);
		
		View photo = new View(mContext);
		photo.setLayoutParams(rp);
		photo.setBackgroundResource(R.drawable.img_main_photo);
		photo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/photo";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(photo);
		
		//Gethering_newPost.		id : 11
		View bgForGethering_newPost = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.topMargin = s;
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 8);
		rp.addRule(RelativeLayout.BELOW, madeCount + 8);
		bgForGethering_newPost.setLayoutParams(rp);
		bgForGethering_newPost.setBackgroundColor(topMenuColor);
		mainRelative.addView(bgForGethering_newPost);
		
		View gethering_newPost = new View(mContext);
		gethering_newPost.setLayoutParams(rp);
		gethering_newPost.setId(madeCount + 11);
		gethering_newPost.setBackgroundResource(R.drawable.img_main_gethering_newpost);
		gethering_newPost.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/getheringnewpost";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(gethering_newPost);
		
		//Gethering_management.		id : 12
		View bgForGethering_management = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.leftMargin = s;
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 11);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 11);
		bgForGethering_management.setLayoutParams(rp);
		bgForGethering_management.setBackgroundColor(topMenuColor);
		mainRelative.addView(bgForGethering_management);
		
		View gethering_management = new View(mContext);
		gethering_management.setLayoutParams(rp);
		gethering_management.setId(madeCount + 12);
		gethering_management.setBackgroundResource(R.drawable.img_main_gethering_management);
		gethering_management.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/getheringmanagement";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(gethering_management);
		
		//Gethering_intro.			id : 13
		View bgForGethering_intro = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		
		rp.topMargin = s;
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 11);
		rp.addRule(RelativeLayout.BELOW, madeCount + 11);
		bgForGethering_intro.setLayoutParams(rp);
		bgForGethering_intro.setBackgroundColor(topMenuColor);
		mainRelative.addView(bgForGethering_intro);
		
		View gethering_intro = new View(mContext);
		gethering_intro.setLayoutParams(rp);
		gethering_intro.setId(madeCount + 13);
		gethering_intro.setBackgroundResource(R.drawable.img_main_gethering_intro);
		gethering_intro.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/getheringintro";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(gethering_intro);
		
		//Gethering_search.
		View bgForGethering_search = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.leftMargin = s;
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 13);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 13);
		bgForGethering_search.setLayoutParams(rp);
		bgForGethering_search.setBackgroundColor(topMenuColor);
		mainRelative.addView(bgForGethering_search);
		
		View gethering_search = new View(mContext);
		gethering_search.setLayoutParams(rp);
		gethering_search.setBackgroundResource(R.drawable.img_main_gethering_search);
		gethering_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/getheringsearch";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(gethering_search);

		//Gethering.				id : 14
		View bgForGethering = new View(mContext);
		rp = new RelativeLayout.LayoutParams(b, b);
		rp.leftMargin = s;
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 12);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 12);
		bgForGethering.setLayoutParams(rp);
		bgForGethering.setBackgroundColor(topMenuColor);
		mainRelative.addView(bgForGethering);
		
		View gethering = new View(mContext);
		gethering.setLayoutParams(rp);
		gethering.setId(madeCount + 14);
		gethering.setBackgroundResource(R.drawable.img_main_gethering);
		gethering.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/getheringlist";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(gethering);
		
		//Schedule.					id : 15
		View bgForSchedule = new View(mContext);
		rp = new RelativeLayout.LayoutParams(b, b);
		rp.topMargin = s;
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 13);
		rp.addRule(RelativeLayout.BELOW, madeCount + 13);
		bgForSchedule.setLayoutParams(rp);
		bgForSchedule.setBackgroundColor(topMenuColor);
		mainRelative.addView(bgForSchedule);
		
		View schedule = new View(mContext);
		schedule.setLayoutParams(rp);
		schedule.setId(madeCount + 15);
		schedule.setBackgroundResource(R.drawable.img_main_schedule);
		schedule.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/schedule";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(schedule);
		
		//Golfcourse.					id : 16
		View bgForGolfcourse = new View(mContext);
		rp = new RelativeLayout.LayoutParams(b, b);
		rp.leftMargin = s;
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 15);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 15);
		bgForGolfcourse.setLayoutParams(rp);
		bgForGolfcourse.setBackgroundColor(topMenuColor);
		mainRelative.addView(bgForGolfcourse);
		
		View golfcourse = new View(mContext);
		golfcourse.setLayoutParams(rp);
		golfcourse.setId(madeCount + 16);
		golfcourse.setBackgroundResource(R.drawable.img_main_golfcourse);
		golfcourse.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showBoardMenu(boardMenuForGolfcourse);
			}
		});
		mainRelative.addView(golfcourse);
		
		//Bottom logo.					id : 17
		View bottomLogo = new View(mContext);
		rp = new RelativeLayout.LayoutParams(
				ResizeUtils.getSpecificLength(416), 
				ResizeUtils.getSpecificLength(44));
		rp.topMargin = s*2;
		rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		rp.addRule(RelativeLayout.BELOW, madeCount + 16);
		bottomLogo.setBackgroundResource(R.drawable.img_bottom_logo);
		bottomLogo.setLayoutParams(rp);
		
///////////////////////////////////////////////////////
		
		//Bottom blank.
		View bottomBlank = new View(mContext);
		rp = new RelativeLayout.LayoutParams(s, s);
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 17);
		rp.addRule(RelativeLayout.BELOW, madeCount + 17);
		bottomBlank.setLayoutParams(rp);
		mainRelative.addView(bottomBlank);

		//150 * 9 = 1350
		//8 * 10 = 80
		//1350 + 80 = 1430
		coverForMenu = new View(mContext);
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getScreenWidth(), ResizeUtils.getSpecificLength(1430));
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		coverForMenu.setLayoutParams(rp);
		coverForMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				hideBoardMenu(true);
			}
		});
		coverForMenu.setVisibility(View.INVISIBLE);
		coverForMenu.setBackgroundColor(Color.argb(100, 0, 0, 0));
		mainRelative.addView(coverForMenu);
		
		addBoardMenu();
		setPage(true);
	}

	@Override
	protected void setListeners() {
	}

	@Override
	protected void setSizes() {
	}

	@Override
	protected void downloadInfo() {
	}

	@Override
	protected void setPage(boolean downloadSuccess) {
		
		if(MainActivity.myInfo != null && !StringUtils.isEmpty(MainActivity.myInfo.getMember_media_src())) {
			String url = MainActivity.myInfo.getMember_media_src();
			
			if(mActivity.getProfileView() != null) {
				if(!StringUtils.isEmpty(url) && mActivity.getProfileView().getIcon() != null) {
					ImageDownloadUtils.downloadImageImmediately(url, "", mActivity.getProfileView().getIcon(), 80, true);
				}
				
				String nickname = MainActivity.myInfo.getMember_nickname();
				if(!StringUtils.isEmpty(nickname)) {
					mActivity.getProfileView().setTitle(nickname);
				}
			}
		}
		
		if(customViews == null
				|| customViews[0] == null
				|| customViews[0].getDrawable() == null) {
			downloadIcons();
		}
	}

	@Override
	protected int getContentViewId() {

		return R.id.mainPage_mainRelative;
	}

	@Override
	public boolean onBackKeyPressed() {
		
		if(boardMenuForMarket.getVisibility() == View.VISIBLE
				|| boardMenuForBanner.getVisibility() == View.VISIBLE
				|| boardMenuForGolfcourse.getVisibility() == View.VISIBLE) {
			hideBoardMenu(true);
			return true;
		}
		
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

			if(mActivity.getSponserBanner() != null) {
				mActivity.getSponserBanner().hideBanner();
			}
			
			mActivity.getTitleBar().hideHomeButton();
			mActivity.getTitleBar().hideWriteButton();
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
	
	public void addBoardMenu() {
		
		int l = ResizeUtils.getSpecificLength(150);
		int s = ResizeUtils.getSpecificLength(8);

		FrameLayout targetFrame = null;
		int targetMadeCount = 0;
		int[] menuImageResIds = null;
		int defaultTypeNumber = 0;
		String defaultString = null;
		
		for(int j=0; j<3; j++) {
			
			switch(j) {
			case 0:
				targetFrame = (boardMenuForMarket = new FrameLayout(mContext));
				targetMadeCount = madeCount + 7;
				menuImageResIds = new int[] {
						R.drawable.img_main_maniamarket1,
						R.drawable.img_main_maniamarket2,
						R.drawable.img_main_maniamarket3,
						R.drawable.img_main_maniamarket4,
				};
				defaultTypeNumber = ZoneConstants.TYPE_MARKET1;
				defaultString = "market";
				break;
				
			case 1:
				targetFrame = (boardMenuForBanner = new FrameLayout(mContext));
				targetMadeCount = madeCount + 8;
				menuImageResIds = new int[] {
						R.drawable.img_main_banner1,
						R.drawable.img_main_banner2,
						R.drawable.img_main_banner3,
						R.drawable.img_main_banner4,
				};
				defaultTypeNumber = ZoneConstants.TYPE_BANNER1;
				defaultString = "banner";
				break;
				
			case 2:
				targetFrame = (boardMenuForGolfcourse = new FrameLayout(mContext));
				targetMadeCount = madeCount + 16;
				menuImageResIds = new int[] {
						R.drawable.img_main_golfcourse1,
						R.drawable.img_main_golfcourse2,
						R.drawable.img_main_golfcourse3,
						R.drawable.img_main_golfcourse4,
				};
				defaultTypeNumber = ZoneConstants.TYPE_GOLFCOURSE1;
				defaultString = "golfcourse";
				break;
			}
			
			RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(l*2 + s, l*2 + s);
			rp.addRule(RelativeLayout.ALIGN_TOP, targetMadeCount);
			rp.addRule(RelativeLayout.ALIGN_LEFT, targetMadeCount);
			targetFrame.setLayoutParams(rp);
			targetFrame.setBackgroundColor(Color.WHITE);
			targetFrame.setVisibility(View.INVISIBLE);
			mainRelative.addView(targetFrame);

			FrameLayout.LayoutParams fp = null;
			for(int i=0; i<4; i++) {
				fp = new FrameLayout.LayoutParams(l, l, Gravity.LEFT);
				fp.leftMargin = (i%2) * (l + s);
				fp.topMargin = i<2? 0 : (l + s);

				final int TYPE = defaultTypeNumber + i;
				final String DEFAULTSTRING = defaultString;

				View bg = new View(mContext);
				bg.setLayoutParams(fp);
				bg.setBackgroundColor(Color.WHITE);
				targetFrame.addView(bg);
				
				if(j==1) {
					ImageView menu = new ImageView(mContext);
					menu.setLayoutParams(fp);
					menu.setBackgroundResource(menuImageResIds[i]);
					menu.setScaleType(ScaleType.FIT_XY);
					menu.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/" + DEFAULTSTRING + "?type=" + TYPE;
							IntentHandlerActivity.actionByUri(Uri.parse(uriString));
							hideBoardMenu(false);
						}
					});
					customViews[i+1] = menu;
					targetFrame.addView(menu);
				} else {
					View menu = new View(mContext);
					menu.setLayoutParams(fp);
					menu.setBackgroundResource(menuImageResIds[i]);
					menu.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/" + DEFAULTSTRING + "?type=" + TYPE;
							IntentHandlerActivity.actionByUri(Uri.parse(uriString));
							hideBoardMenu(false);
						}
					});
					targetFrame.addView(menu);
				}
			}
		}
	}
	
	public void showBoardMenu(View target) {

		if(animating || target.getVisibility() == View.VISIBLE) {
			return;
		}
		
		if(aaIn == null || aaIn2 == null) {
			aaIn = new AlphaAnimation(0, 1);
			aaIn.setDuration(200);
			aaIn.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					animating = true;
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {}

				@Override
				public void onAnimationEnd(Animation animation) {
					animating = false;
				}
			});
		}
		
		if(aaIn2 == null) {
			aaIn2 = new AlphaAnimation(0, 1);
			aaIn2.setDuration(200);
		}
		
		target.setVisibility(View.VISIBLE);
		target.startAnimation(aaIn);
		coverForMenu.setVisibility(View.VISIBLE);
		coverForMenu.startAnimation(aaIn2);
	}
	
	public void hideBoardMenu(boolean needAnim) {
		
		if(animating) {
			return;
		}
		
		if(aaOut == null) {
			aaOut = new AlphaAnimation(1, 0);
			aaOut.setDuration(200);
			aaOut.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
					animating = true;
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					animating = false;
				}
			});
		}
		
		if(aaOut2 == null) {
			aaOut2 = new AlphaAnimation(1, 0);
			aaOut2.setDuration(200);
		}
		
		View targetView = null;
		
		if(boardMenuForMarket.getVisibility() == View.VISIBLE) {
			targetView = boardMenuForMarket; 
		} else if(boardMenuForBanner.getVisibility() == View.VISIBLE) {
			targetView = boardMenuForBanner;
		} else if(boardMenuForGolfcourse.getVisibility() == View.VISIBLE) {
			targetView = boardMenuForGolfcourse;
		}
		
		if(targetView != null) {
			targetView.setVisibility(View.INVISIBLE);
			coverForMenu.setVisibility(View.INVISIBLE);
			if(needAnim) {
				targetView.startAnimation(aaOut);
				coverForMenu.startAnimation(aaOut2);
			}
		}
	}

	public void setScrollToTop() {
		
		scrollView.scrollTo(0, 0);
	}

	public void downloadIcons() {

		String url = ZoneConstants.BASE_URL + "common/main/image" +
				"?sb_id=" + ZoneConstants.PAPP_ID +
				"&image_size=640";

		AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {

			@Override
			public void onErrorRaised(String url, Exception e) {

				LogUtils.log("###MainPage.downloadIcons.onError." + "\nurl : "
						+ url);
			}

			@Override
			public void onCompleted(String url, String result) {

				LogUtils.log("###MainPage.downloadIcons.onCompleted." + "\nurl : "
						+ url + "\nresult : " + result);
				
				try {
					JSONObject objJSON = (new JSONObject(result)).getJSONObject("data");
					JSONArray arJSON = objJSON.getJSONArray("icon_urls");
					
					for(int i=0; i<5; i++) {
						String iconUrl = arJSON.getString(i);
						
						if (!StringUtils.isEmpty(iconUrl)) {
							ImageDownloadUtils.downloadImageImmediately(iconUrl, getDownloadKey(), 
									customViews[i], i==0?308:150, true);
						}
					}
					
					if(objJSON.has("link_url")) {
						bannerLinkUrl = objJSON.getString("link_url");
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}
			}
		};
		AsyncStringDownloader.download(url, getDownloadKey(), ocl);
	}
}