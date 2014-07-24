package com.zonecomms.clubcage.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.zonecomms.clubcage.IntentHandlerActivity;
import com.zonecomms.clubcage.MainActivity;
import com.zonecomms.clubcage.R;
import com.zonecomms.clubcage.classes.BaseFragment;
import com.zonecomms.clubcage.classes.ZoneConstants;
import com.zonecomms.common.models.Notice;

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
	private FrameLayout boardMenu;
	private ImageView ivPoster1, ivPoster2;
	
	private View coverForMenu;

	private boolean animating;
	private boolean isWriting;
	private AlphaAnimation aaIn, aaOut, aaIn2, aaOut2;
	private int madeCount = 870901;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if(container == null) {
			return null;
		}
	
		mThisView = inflater.inflate(R.layout.page_main, null);
		return mThisView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		bindViews();
		setVariables();
		createPage();
		
		setListeners();
		setSizes();
		
		setPage(true);
	}
	
	@Override
	protected void bindViews() {
		
		scrollView = (ScrollView) mThisView.findViewById(R.id.mainPage_scrollView);
		mainRelative = (RelativeLayout) mThisView.findViewById(R.id.mainPage_mainRelative);
	}

	@Override
	protected void setVariables() {
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
		
		bgForNotice.setBackgroundColor(getResources().getColor(R.color.color_main_small));
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
		bgForEvent.setBackgroundColor(getResources().getColor(R.color.color_main_small));
		mainRelative.addView(bgForEvent);
		
		//Event.			id : 2
		View event = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 1);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 1);
		event.setLayoutParams(rp);
		event.setId(madeCount + 2);
		event.setBackgroundResource(R.drawable.img_event);
		event.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/event";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(event);
		
		View bgForHome = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.topMargin = s;
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 1);
		rp.addRule(RelativeLayout.BELOW, madeCount + 1);
		bgForHome.setLayoutParams(rp);
		bgForHome.setBackgroundColor(getResources().getColor(R.color.color_main_small));
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
				
				if(MainActivity.myInfo != null && !StringUtils.isEmpty(MainActivity.myInfo.getMember_id())) {
					String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/userhome?member_id=" 
										+ MainActivity.myInfo.getMember_id();
					IntentHandlerActivity.actionByUri(Uri.parse(uriString));
				}
			}
		});
		mainRelative.addView(home);
		
		View bgForSetting = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 3);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 3);
		bgForSetting.setLayoutParams(rp);
		bgForSetting.setBackgroundColor(getResources().getColor(R.color.color_main_small));
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
		bgForTalk.setBackgroundColor(getResources().getColor(R.color.color_main_big));
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

				showBoardMenu(false);
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
		bgForSchedule.setBackgroundColor(getResources().getColor(R.color.color_main_big));
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
		bgForMember.setBackgroundColor(getResources().getColor(R.color.color_main_small));
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
		bgForPhoto.setBackgroundColor(getResources().getColor(R.color.color_main_small));
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
		bgForMusic.setBackgroundColor(getResources().getColor(R.color.color_main_small));
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
		bgForVideo.setBackgroundColor(getResources().getColor(R.color.color_main_small));
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
		ivPoster1 = new ImageView(mContext);
		ivPoster1.setId(madeCount + 7); 
		ivPoster1.setScaleType(ScaleType.CENTER_CROP);
		ivPoster1.setBackgroundResource(R.drawable.bg_poster);
		rp = new RelativeLayout.LayoutParams(l*2 + s, l*3 + s*2);
		rp.topMargin = s;
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 4);
		rp.addRule(RelativeLayout.BELOW, madeCount + 4);
		ivPoster1.setLayoutParams(rp);
		
		if(MainActivity.startupInfo != null && MainActivity.startupInfo.getSchedules() != null
				&& MainActivity.startupInfo.getSchedules().length >= 1) {
			
			final Notice mainSchedule = MainActivity.startupInfo.getSchedules()[0];
			
			if(mainSchedule != null) {
				ivPoster1.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						mActivity.showImageViewerActivity(mainSchedule.getNotice_title(), mainSchedule.getMedias(), 0);
					}
				});
				
				ivPoster1.post(new Runnable() {
					
					@Override
					public void run() {
						String url = mainSchedule.getMedias()[0].getMedia_src();
						ImageDownloadUtils.downloadImageImmediately(url, "mainPoster1", ivPoster1, 308, true);
					}
				});
			}
		}
		mainRelative.addView(ivPoster1);
		
		//Poster2.
		ivPoster2 = new ImageView(mContext);
		ivPoster2.setScaleType(ScaleType.CENTER_CROP);
		ivPoster2.setBackgroundResource(R.drawable.bg_poster);
		rp = new RelativeLayout.LayoutParams(l*2 + s, l*3 + s*2);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 7);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 7);
		ivPoster2.setLayoutParams(rp);
		if(MainActivity.startupInfo != null && MainActivity.startupInfo.getSchedules() != null
				&& MainActivity.startupInfo.getSchedules().length >= 2) {
			
			final Notice mainSchedule = MainActivity.startupInfo.getSchedules()[1];

			if(mainSchedule != null) {
				ivPoster2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						mActivity.showImageViewerActivity(mainSchedule.getNotice_title(), mainSchedule.getMedias(), 0);
					}
				});
				
				ivPoster2.post(new Runnable() {
					
					@Override
					public void run() {
						String url = mainSchedule.getMedias()[0].getMedia_src();
						ImageDownloadUtils.downloadImageImmediately(url, "mainPoster2", ivPoster2, 308, true);
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
		coverForMenu = new View(mContext);
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getScreenWidth(), ResizeUtils.getSpecificLength(1422));
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
	}

	@Override
	protected String getTitleText() {
		
		return getString(R.string.app_name);
	}

	@Override
	protected int getContentViewId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean onBackKeyPressed() {
		
		if(boardMenu.getVisibility() == View.VISIBLE) {
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
			mActivity.getTitleBar().hideHomeButton();
			mActivity.getTitleBar().showWriteButton();
			
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

////////////////////// Custom methdos.
	
	public void addBoardMenu() {

		int l = ResizeUtils.getSpecificLength(150);
		int s = ResizeUtils.getSpecificLength(8);

		boardMenu = new FrameLayout(mContext);
		RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(l*2 + s, l*2 + s);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 2);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 2);
		boardMenu.setLayoutParams(rp);
		boardMenu.setBackgroundColor(Color.BLACK);
		boardMenu.setVisibility(View.INVISIBLE);
		mainRelative.addView(boardMenu);

		int[] imgResIds = new int[] {
			R.drawable.img_popup_story,
			R.drawable.img_popup_review,
			R.drawable.img_popup_with,
			R.drawable.img_popup_find,
		};
		
		FrameLayout.LayoutParams fp = null;
		for(int i=0; i<4; i++) {
			fp = new FrameLayout.LayoutParams(l, l, Gravity.LEFT);
			fp.leftMargin = (i%2) * (l + s);
			fp.topMargin = i<2? 0 : (l + s);
			
			View bg = new View(mContext);
			bg.setLayoutParams(fp);
			bg.setBackgroundColor(getResources().getColor(R.color.color_main_big));
			boardMenu.addView(bg);
			
			final int I = i;
			
			View menu = new View(mContext);
			menu.setLayoutParams(fp);
			menu.setBackgroundResource(imgResIds[i]);
			menu.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if(isWriting) {
						isWriting = false;
						mActivity.showWriteActivity(I+1);
					} else {
						String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/";
						
						switch(I) {
						
						case 0:
							uriString += "freetalk";
							break;
						case 1:
							uriString += "review";
							break;
						case 2:
							uriString += "with";
							break;
						case 3:
							uriString += "find";
							break;
						}
						
						IntentHandlerActivity.actionByUri(Uri.parse(uriString));
					}
					
					hideBoardMenu(false);
				}
			});
			boardMenu.addView(menu);
		}
	}
	
	public void showBoardMenu(boolean isWriting) {

		if(animating || boardMenu.getVisibility() == View.VISIBLE) {
			return;
		}
		
		this.isWriting = isWriting;

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
		
		boardMenu.setVisibility(View.VISIBLE);
		boardMenu.startAnimation(aaIn);
		coverForMenu.setVisibility(View.VISIBLE);
		coverForMenu.startAnimation(aaIn2);
	}
	
	public void hideBoardMenu(boolean needAnim) {
		
		if(animating || boardMenu.getVisibility() != View.VISIBLE) {
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
		
		boardMenu.setVisibility(View.INVISIBLE);
		coverForMenu.setVisibility(View.INVISIBLE);
		if(needAnim) {
			boardMenu.startAnimation(aaOut);
			coverForMenu.startAnimation(aaOut2);
		}
	}

	public void setScrollToTop() {
		
		scrollView.scrollTo(0, 0);
	}
}