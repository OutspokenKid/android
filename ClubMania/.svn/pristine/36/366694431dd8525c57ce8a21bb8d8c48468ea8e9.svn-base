package com.zonecomms.clubmania.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.outspoken_kid.classes.BaseFragment;
import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.utils.ResizeUtils;
import com.zonecomms.clubmania.IntentHandlerActivity;
import com.zonecomms.clubmania.MainActivity;
import com.zonecomms.clubmania.R;
import com.zonecomms.clubmania.classes.ZoneConstants;
import com.zonecomms.common.utils.ImageDownloadUtils;

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
		
		setListener();
		setSize();
		
		setPage(true);
	}
	
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
		
		//Box 'l'ength.
		int l = ResizeUtils.getSpecificLength(150);
		
		//Box 's'pace.
		int s = ResizeUtils.getSpecificLength(8);
		
		//Length of big size menus.
		int b = l*2 + s;
		
		//Color of top menus.
		int topMenuColor = Color.rgb(62, 58, 57);
		
		madeCount += 27;
		
		///////////////////////////////////////////////////
		
		View bgForCafe = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.topMargin = s;
		rp.leftMargin = s;
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		bgForCafe.setLayoutParams(rp);
		bgForCafe.setBackgroundColor(topMenuColor);
		mainRelative.addView(bgForCafe);
		
		//Cafe.			id : 0
		View cafe = new View(mContext);
		cafe.setLayoutParams(rp);
		cafe.setId(madeCount);
		cafe.setBackgroundResource(R.drawable.img_cafe);
		cafe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/cafe";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(cafe);
		
		View bgForFacebook = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.leftMargin = s;
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount);
		bgForFacebook.setLayoutParams(rp);
		bgForFacebook.setBackgroundColor(topMenuColor);
		mainRelative.addView(bgForFacebook);
		
		//Facebook.			id : 1
		View facebook = new View(mContext);
		facebook.setLayoutParams(rp);
		facebook.setId(madeCount + 1);
		facebook.setBackgroundResource(R.drawable.img_facebook);
		facebook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/facebook";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(facebook);
		
		View bgForTwitter = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.leftMargin = s;
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 1);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 1);
		bgForTwitter.setLayoutParams(rp);
		bgForTwitter.setBackgroundColor(topMenuColor);
		mainRelative.addView(bgForTwitter);
		
		//Twitter.			id : 2
		View twitter = new View(mContext);
		twitter.setLayoutParams(rp);
		twitter.setId(madeCount + 2);
		twitter.setBackgroundResource(R.drawable.img_twitter);
		twitter.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/twitter";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(twitter);
		
		View bgForContact = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.leftMargin = s;
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 2);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 2);
		bgForContact.setLayoutParams(rp);
		bgForContact.setBackgroundColor(topMenuColor);
		mainRelative.addView(bgForContact);
		
		//Contact.
		View contact = new View(mContext);
		contact.setLayoutParams(rp);
		contact.setBackgroundResource(R.drawable.img_contact);
		contact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/contact";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(contact);
		
		///////////////////////////////////////////////////
		
		View bgForNotice = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.topMargin = s;
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount);
		rp.addRule(RelativeLayout.BELOW, madeCount);
		bgForNotice.setLayoutParams(rp);
		bgForNotice.setBackgroundColor(ZoneConstants.COLOR_MAIN_SMALL);
		mainRelative.addView(bgForNotice);
		
		//Notice.			id : 3
		View notice = new View(mContext);
		notice.setLayoutParams(rp);
		notice.setId(madeCount + 3);
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
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 3);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 3);
		bgForEvent.setLayoutParams(rp);
		bgForEvent.setBackgroundColor(ZoneConstants.COLOR_MAIN_SMALL);
		mainRelative.addView(bgForEvent);
		
		//Event.			id : 4
		View event = new View(mContext);
		event.setLayoutParams(rp);
		event.setId(madeCount + 4);
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
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 3);
		rp.addRule(RelativeLayout.BELOW, madeCount + 3);
		bgForHome.setLayoutParams(rp);
		bgForHome.setBackgroundColor(ZoneConstants.COLOR_MAIN_SMALL);
		mainRelative.addView(bgForHome);
		
		//Home.			id : 5
		View home = new View(mContext);
		home.setLayoutParams(rp);
		home.setId(madeCount + 5);
		home.setBackgroundResource(R.drawable.img_home);
		home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(MainActivity.myInfo != null && !TextUtils.isEmpty(MainActivity.myInfo.getMember_id())) {
					String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/userhome?member_id=" 
										+ MainActivity.myInfo.getMember_id();
					IntentHandlerActivity.actionByUri(Uri.parse(uriString));
				}
			}
		});
		mainRelative.addView(home);
		
		View bgForSetting = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 5);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 5);
		bgForSetting.setLayoutParams(rp);
		bgForSetting.setBackgroundColor(ZoneConstants.COLOR_MAIN_SMALL);
		mainRelative.addView(bgForSetting);
		
		View setting = new View(mContext);
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
		rp = new RelativeLayout.LayoutParams(b, b);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 4);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 4);
		bgForTalk.setLayoutParams(rp);
		bgForTalk.setBackgroundColor(ZoneConstants.COLOR_MAIN_BIG);
		mainRelative.addView(bgForTalk);

		//Story.				id : 6
		View talk = new View(mContext);
		talk.setLayoutParams(rp);
		talk.setId(madeCount + 6);
		talk.setBackgroundResource(R.drawable.img_story);
		talk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				showBoardMenu(false);
			}
		});
		mainRelative.addView(talk);
		
		View bgForSmartpass = new View(mContext);
		rp = new RelativeLayout.LayoutParams(b, b);
		rp.topMargin = s;
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 5);
		rp.addRule(RelativeLayout.BELOW, madeCount + 5);
		bgForSmartpass.setLayoutParams(rp);
		bgForSmartpass.setBackgroundColor(ZoneConstants.COLOR_MAIN_BIG);
		mainRelative.addView(bgForSmartpass);
		
		//Smartpass.			id : 7
		View smartpass = new View(mContext);
		smartpass.setLayoutParams(rp);
		smartpass.setId(madeCount + 7);
		smartpass.setBackgroundResource(R.drawable.img_smartpass);
		smartpass.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/smartpass";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(smartpass);
		
		View bgForMember = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 7);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 7);
		bgForMember.setLayoutParams(rp);
		bgForMember.setBackgroundColor(ZoneConstants.COLOR_MAIN_SMALL);
		mainRelative.addView(bgForMember);
		
		//member.				id : 8
		View member = new View(mContext);
		member.setLayoutParams(rp);
		member.setId(madeCount + 8);
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
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 8);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 8);
		bgForPhoto.setLayoutParams(rp);
		bgForPhoto.setBackgroundColor(ZoneConstants.COLOR_MAIN_SMALL);
		mainRelative.addView(bgForPhoto);
		
		//Photo
		View photo = new View(mContext);
		photo.setLayoutParams(rp);
		photo.setBackgroundResource(R.drawable.img_photo);
		photo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/appsforphoto";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(photo);
		
		View bgForMusic = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.topMargin = s;
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 8);
		rp.addRule(RelativeLayout.BELOW, madeCount + 8);
		bgForMusic.setLayoutParams(rp);
		bgForMusic.setBackgroundColor(ZoneConstants.COLOR_MAIN_SMALL);
		mainRelative.addView(bgForMusic);
		
		//Music.			id : 9
		View music = new View(mContext);
		music.setLayoutParams(rp);
		music.setId(madeCount + 9);
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
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 9);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 9);
		bgForVideo.setLayoutParams(rp);
		bgForVideo.setBackgroundColor(ZoneConstants.COLOR_MAIN_SMALL);
		mainRelative.addView(bgForVideo);
		
		//Video.
		View video = new View(mContext);
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
		
		//Schedule for club.	id : 10
		View scheduleForClub = new View(mContext);
		rp = new RelativeLayout.LayoutParams(b, b);
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 7);
		rp.addRule(RelativeLayout.BELOW, madeCount + 7);
		rp.topMargin = s;
		scheduleForClub.setLayoutParams(rp);
		scheduleForClub.setId(madeCount + 10);
		scheduleForClub.setBackgroundResource(R.drawable.img_schedule_club);
		scheduleForClub.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/appsforschedule?type=club";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(scheduleForClub);
		
		View scheduleForLounge = new View(mContext);
		rp = new RelativeLayout.LayoutParams(b, b);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 10);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 10);
		rp.leftMargin = s;
		scheduleForLounge.setLayoutParams(rp);
		scheduleForLounge.setBackgroundResource(R.drawable.img_schedule_lounge);
		scheduleForLounge.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/appsforschedule?type=lounge";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(scheduleForLounge);
		
		
///////////////////////////////////////////////////////
		
		//Bottom blank.
		View bottomBlank = new View(mContext);
		rp = new RelativeLayout.LayoutParams(s, s);
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 10);
		rp.addRule(RelativeLayout.BELOW, madeCount + 10);
		bottomBlank.setLayoutParams(rp);
		mainRelative.addView(bottomBlank);

		//150 * 7 = 1050
		//8 * 7 = 56
		//1050 + 56 = 1106
		coverForMenu = new View(mContext);
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getScreenWidth(), ResizeUtils.getSpecificLength(1106));
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
	protected void setListener() {
	}

	@Override
	protected void setSize() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void downloadInfo() {
	}

	@Override
	protected void setPage(boolean downloadSuccess) {
		
		if(MainActivity.myInfo != null && !TextUtils.isEmpty(MainActivity.myInfo.getMember_media_src())) {
			String url = MainActivity.myInfo.getMember_media_src();
			
			if(mActivity.getProfileView() != null) {
				if(!TextUtils.isEmpty(url) && mActivity.getProfileView().getIcon() != null) {
					ImageDownloadUtils.downloadImageImmediately(url, "", mActivity.getProfileView().getIcon(), 80, true);
				}
				
				String nickname = MainActivity.myInfo.getMember_nickname();
				if(!TextUtils.isEmpty(nickname)) {
					mActivity.getProfileView().setTitle(nickname);
				}
			}
		}
	}

	@Override
	protected String getTitleText() {
		
		return title;
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
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 6);
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 6);
		boardMenu.setLayoutParams(rp);
		boardMenu.setBackgroundColor(Color.BLACK);
		boardMenu.setVisibility(View.INVISIBLE);
		mainRelative.addView(boardMenu);

		FrameLayout.LayoutParams fp = null;
		for(int i=0; i<4; i++) {
			fp = new FrameLayout.LayoutParams(l, l, Gravity.LEFT);
			fp.leftMargin = (i%2) * (l + s);
			fp.topMargin = i<2? 0 : (l + s);

			final int I = i;
			
			TextView menu = new TextView(mContext);
			menu.setLayoutParams(fp);
			menu.setId(madeCount + i + 1);
			menu.setBackgroundColor(ZoneConstants.COLOR_MAIN_SMALL);
			menu.setGravity(Gravity.CENTER);
			menu.setMaxLines(3);
			menu.setEllipsize(TruncateAt.END);
			menu.setTextColor(Color.WHITE);
			FontInfo.setFontSize(menu, 32);
			
			menu.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if(isWriting) {
						isWriting = false;

						int type = 0;
						String title = "";
						
						switch(I) {
						
						case 0:
							type = 1;
							title = getString(R.string.club);
							break;
						case 1:
							type = 5;
							title = getString(R.string.party);
							break;
						case 2:
							type = 7;
							title = getString(R.string.festival);
							break;
						case 3:
							type = 2;
							title = getString(R.string.lounge);
							break;
						}
						
						mActivity.showWriteActivity(type, title);
					} else {
						String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/talk?type=";
						
						switch(I) {
						
						case 0:
							uriString += "club";
							break;
						case 1:
							uriString += "party";
							break;
						case 2:
							uriString += "festival";
							break;
						case 3:
							uriString += "lounge";
							break;
						}
						
						IntentHandlerActivity.actionByUri(Uri.parse(uriString));
					}
					
					hideBoardMenu(false);
				}
			});
			
			switch(i) {
			
			case 0:
				menu.setText(R.string.club);
				break;
			case 1:
				menu.setText(R.string.party);
				break;
			case 2:
				menu.setText(R.string.festival);
				break;
			case 3:
				menu.setText(R.string.lounge);
				break;
			}
			
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