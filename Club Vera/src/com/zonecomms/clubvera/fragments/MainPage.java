package com.zonecomms.clubvera.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.views.OutspokenImageView;
import com.zonecomms.clubvera.IntentHandlerActivity;
import com.zonecomms.clubvera.R;
import com.zonecomms.clubvera.classes.ZoneConstants;
import com.zonecomms.clubvera.classes.ZonecommsApplication;
import com.zonecomms.clubvera.classes.ZonecommsFragment;
import com.zonecomms.common.models.Notice;

/**
 * 217 27 92
 * d9 1b 5c
 * 
 * @author HyungGunKim
 *
 */
public class MainPage extends ZonecommsFragment {

	public static int colorBig;
	public static int colorSmall;

	private ScrollView scrollView;
	private RelativeLayout mainRelative;
//	private FrameLayout boardMenu;
	private OutspokenImageView ivPoster1, ivPoster2;
	
//	private View coverForMenu;
//
//	private boolean animating;
//	private boolean isWriting;
//	private AlphaAnimation aaIn, aaOut, aaIn2, aaOut2;
	private int madeCount = 870901;
	
	@Override
	public void bindViews() {
		
		scrollView = (ScrollView) mThisView.findViewById(R.id.mainPage_scrollView);
		mainRelative = (RelativeLayout) mThisView.findViewById(R.id.mainPage_mainRelative);
	}

	@Override
	public void setVariables() {
		super.setVariables();
	}

	@Override
	public void createPage() {

		//Set TitleBar.
		mainActivity.getTitleBar().hideHomeButton();
		
		//Set menus.
		RelativeLayout.LayoutParams rp;
		
		//Box 'l'ength.
		int l = ResizeUtils.getSpecificLength(150);
		
		//Box 's'pace.
		int s = ResizeUtils.getSpecificLength(8);
		
		madeCount += 27;
		
		//MainImage.		id : 0
		View mainImage = new View(mContext);
		mainImage.setId(madeCount);
		rp = new RelativeLayout.LayoutParams(l*4 + s*3, l*2 + s);
		rp.topMargin = s;
		rp.addRule(RelativeLayout.CENTER_HORIZONTAL | RelativeLayout.ALIGN_PARENT_TOP);
		mainImage.setLayoutParams(rp);
		mainImage.setBackgroundResource(R.drawable.img_information);
		mainImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/information";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(mainImage);
		
		View bgForNotice = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.topMargin = s;
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount);
		rp.addRule(RelativeLayout.BELOW, madeCount);
		bgForNotice.setLayoutParams(rp);
		
		bgForNotice.setBackgroundColor(colorSmall);
		mainRelative.addView(bgForNotice);
		
		//Notice.			id : 1
		View notice = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.topMargin = s;
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount);
		rp.addRule(RelativeLayout.BELOW, madeCount);
		notice.setLayoutParams(rp);
		notice.setId(madeCount + 1);
		notice.setBackgroundResource(R.drawable.img_notice);
		notice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/notice";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(notice);
		
		View bgForEvent = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 1);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 1);
		bgForEvent.setLayoutParams(rp);
		bgForEvent.setBackgroundColor(colorSmall);
		mainRelative.addView(bgForEvent);
		
		//Guest.			id : 2
		View guest = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 1);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 1);
		guest.setLayoutParams(rp);
		guest.setId(madeCount + 2);
		guest.setBackgroundResource(R.drawable.img_guest);
		guest.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/guest";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(guest);
		
		View bgForHome = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.topMargin = s;
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 1);
		rp.addRule(RelativeLayout.BELOW, madeCount + 1);
		bgForHome.setLayoutParams(rp);
		bgForHome.setBackgroundColor(colorSmall);
		mainRelative.addView(bgForHome);
		
		//Home.			id : 3
		View home = new View(mContext);
		home.setId(madeCount + 3);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.topMargin = s;
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 1);
		rp.addRule(RelativeLayout.BELOW, madeCount + 1);
		home.setLayoutParams(rp);
		home.setId(madeCount + 3);
		home.setBackgroundResource(R.drawable.img_home);
		home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/home";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(home);
		
		View bgForSetting = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 3);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 3);
		bgForSetting.setLayoutParams(rp);
		bgForSetting.setBackgroundColor(colorSmall);
		mainRelative.addView(bgForSetting);
		
		View setting = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 3);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 3);
		setting.setLayoutParams(rp);
		setting.setBackgroundResource(R.drawable.img_setting);
		setting.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/setting";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(setting);

		View bgForTalk = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l*2 + s, l*2 + s);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 2);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 2);
		bgForTalk.setLayoutParams(rp);
		bgForTalk.setBackgroundColor(colorBig);
		mainRelative.addView(bgForTalk);

		View talk = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l*2 + s, l*2 + s);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 2);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 2);
		talk.setLayoutParams(rp);
		talk.setBackgroundResource(R.drawable.img_story);
		talk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/freetalk";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
//				showBoardMenu(false);
			}
		});
		mainRelative.addView(talk);
		
		View bgForSchedule = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l*2 + s, l*2 + s);
		rp.topMargin = s;
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 3);
		rp.addRule(RelativeLayout.BELOW, madeCount + 3);
		bgForSchedule.setLayoutParams(rp);
		bgForSchedule.setBackgroundColor(colorBig);
		mainRelative.addView(bgForSchedule);
		
		//Schedule.			id : 4
		View schedule = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l*2 + s, l*2 + s);
		rp.topMargin = s;
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 3);
		rp.addRule(RelativeLayout.BELOW, madeCount + 3);
		schedule.setLayoutParams(rp);
		schedule.setId(madeCount + 4);
		schedule.setBackgroundResource(R.drawable.img_schedule);
		schedule.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/schedule";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(schedule);
		
		View bgForMember = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 4);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 4);
		bgForMember.setLayoutParams(rp);
		bgForMember.setBackgroundColor(colorSmall);
		mainRelative.addView(bgForMember);
		
		//photo.				id : 5
		View member = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 4);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 4);
		member.setLayoutParams(rp);
		member.setId(madeCount + 5);
		member.setBackgroundResource(R.drawable.img_member);
		member.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/member";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(member);
		
		View bgForPhoto = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 5);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 5);
		bgForPhoto.setLayoutParams(rp);
		bgForPhoto.setBackgroundColor(colorSmall);
		mainRelative.addView(bgForPhoto);
		
		View photo = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 5);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 5);
		photo.setLayoutParams(rp);
		photo.setBackgroundResource(R.drawable.img_photo);
		photo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/image";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(photo);
		
		View bgForMusic = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.topMargin = s;
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 5);
		rp.addRule(RelativeLayout.BELOW, madeCount + 5);
		bgForMusic.setLayoutParams(rp);
		bgForMusic.setBackgroundColor(colorSmall);
		mainRelative.addView(bgForMusic);
		
		//Music.			id : 6
		View music = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.topMargin = s;
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 5);
		rp.addRule(RelativeLayout.BELOW, madeCount + 5);
		music.setLayoutParams(rp);
		music.setId(madeCount + 6);
		music.setBackgroundResource(R.drawable.img_music);
		music.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/music";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(music);
		
		View bgForVideo = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 6);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 6);
		bgForVideo.setLayoutParams(rp);
		bgForVideo.setBackgroundColor(colorSmall);
		mainRelative.addView(bgForVideo);
		
		//Video.
		View video = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 6);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 6);
		video.setLayoutParams(rp);
		video.setBackgroundResource(R.drawable.img_video);
		video.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/video";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(video);
		
///////////////////////////////////////////////////////
		
		//Poster1.			id : 7
		ivPoster1 = new OutspokenImageView(mContext);
		ivPoster1.setId(madeCount + 7); 
		ivPoster1.setScaleType(ScaleType.CENTER_CROP);
		ivPoster1.setBackgroundResource(R.drawable.bg_poster);
		rp = new RelativeLayout.LayoutParams(l*2 + s, l*3 + s*2);
		rp.topMargin = s;
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 4);
		rp.addRule(RelativeLayout.BELOW, madeCount + 4);
		ivPoster1.setLayoutParams(rp);
		
		if(ZonecommsApplication.startupInfo != null && ZonecommsApplication.startupInfo.getSchedules() != null
				&& ZonecommsApplication.startupInfo.getSchedules().length >= 1) {
			
			final Notice mainSchedule = ZonecommsApplication.startupInfo.getSchedules()[0];
			
			if(mainSchedule != null) {
				ivPoster1.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						mainActivity.showImageViewerActivity(mainSchedule.getNotice_title(), mainSchedule.getMedias(), 0);
					}
				});
				
				ivPoster1.post(new Runnable() {
					
					@Override
					public void run() {
						String url = mainSchedule.getMedias()[0].getMedia_src();
						ivPoster1.setImageUrl(url);
						ivPoster1.setTag(url);
						DownloadUtils.downloadBitmap(url,
								new OnBitmapDownloadListener() {

									@Override
									public void onError(String url) {
										
										LogUtils.log("MainPage.onError."
												+ "\nurl : " + url);
									}

									@Override
									public void onCompleted(String url, Bitmap bitmap) {

										try {
											LogUtils.log("MainPage.onCompleted."
													+ "\nurl : " + url);

											if (ivPoster1 != null
													&& ivPoster1.getTag() != null
													&& ivPoster1.getTag()
															.toString()
															.equals(url)) {
												ivPoster1.setImageBitmap(bitmap);
											}
										} catch (Exception e) {
											LogUtils.trace(e);
										} catch (OutOfMemoryError oom) {
											LogUtils.trace(oom);
										}
									}
								});
					}
				});
			}
		}
		mainRelative.addView(ivPoster1);
		
		//Poster2.
		ivPoster2 = new OutspokenImageView(mContext);
		ivPoster2.setScaleType(ScaleType.CENTER_CROP);
		ivPoster2.setBackgroundResource(R.drawable.bg_poster);
		rp = new RelativeLayout.LayoutParams(l*2 + s, l*3 + s*2);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 7);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 7);
		ivPoster2.setLayoutParams(rp);
		if(ZonecommsApplication.startupInfo != null && ZonecommsApplication.startupInfo.getSchedules() != null
				&& ZonecommsApplication.startupInfo.getSchedules().length >= 2) {
			
			final Notice mainSchedule = ZonecommsApplication.startupInfo.getSchedules()[1];

			if(mainSchedule != null) {
				ivPoster2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						mainActivity.showImageViewerActivity(mainSchedule.getNotice_title(), mainSchedule.getMedias(), 0);
					}
				});
				
				ivPoster2.post(new Runnable() {
					
					@Override
					public void run() {
						String url = mainSchedule.getMedias()[0].getMedia_src();
						ivPoster2.setImageUrl(url);
						ivPoster2.setTag(url);
						DownloadUtils.downloadBitmap(url,
								new OnBitmapDownloadListener() {

									@Override
									public void onError(String url) {
										// TODO Auto-generated method stub		
									}

									@Override
									public void onCompleted(String url, Bitmap bitmap) {

										try {
											LogUtils.log("MainPage.onCompleted."
													+ "\nurl : " + url);

											if (ivPoster2 != null
													&& ivPoster2.getTag() != null
													&& ivPoster2.getTag()
															.toString()
															.equals(url)) {
												ivPoster2.setImageBitmap(bitmap);
											}
										} catch (Exception e) {
											LogUtils.trace(e);
										} catch (OutOfMemoryError oom) {
											LogUtils.trace(oom);
										}
									}
								});

					}
				});
			}
		}
		mainRelative.addView(ivPoster2);
		
		//Bottom blank.
		View bottomBlank = new View(mContext);
		rp = new RelativeLayout.LayoutParams(s, s);
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 7);
		rp.addRule(RelativeLayout.BELOW, madeCount + 7);
		bottomBlank.setLayoutParams(rp);
		mainRelative.addView(bottomBlank);

		//150 * 9 = 1350
		//8 * 9 = 72
		//1350 + 72 = 1422
//		coverForMenu = new View(mContext);
//		rp = new RelativeLayout.LayoutParams(ResizeUtils.getScreenWidth(), ResizeUtils.getSpecificLength(1422));
//		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//		coverForMenu.setLayoutParams(rp);
//		coverForMenu.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				hideBoardMenu(true);
//			}
//		});
//		coverForMenu.setVisibility(View.INVISIBLE);
//		coverForMenu.setBackgroundColor(Color.argb(100, 0, 0, 0));
//		mainRelative.addView(coverForMenu);
//		
//		addBoardMenu();
	}

	@Override
	public void setListeners() {
	}

	@Override
	public void setSizes() {
		// TODO Auto-generated method stub

	}

	@Override
	public void downloadInfo() {
	}

	@Override
	public void setPage(boolean downloadSuccess) {
	}

	@Override
	public String getTitleText() {
		
		return getString(R.string.app_name);
	}

	@Override
	public int getContentViewId() {

		return R.layout.page_main;
	}
	
	@Override
	public void hideLoadingView() {

		mainActivity.hideLoadingView();
	}

	@Override
	public void showLoadingView() {

		mainActivity.showLoadingView();
	}
	
	@Override
	public boolean onBackPressed() {
		
//		if(boardMenu.getVisibility() == View.VISIBLE) {
//			hideBoardMenu(true);
//			return true;
//		}
		
		return false;
	}
	
	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refreshPage() {
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		mThisView.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				try {
					InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(mThisView.getWindowToken(), 0);
				} catch (Exception e) {
				}
			}
		}, 1000);
		
		mainActivity.showTitleBar();
		mainActivity.getTitleBar().showCircleButton();
		mainActivity.getTitleBar().hideHomeButton();
		mainActivity.getTitleBar().showWriteButton();
		
		if(mainActivity.getSponserBanner() != null) {
			mainActivity.getSponserBanner().hideBanner();
		}
	}

////////////////////// Custom methdos.
	
	public void setScrollToTop() {
		
		scrollView.scrollTo(0, 0);
	}
}