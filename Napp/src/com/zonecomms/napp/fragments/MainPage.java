package com.zonecomms.napp.fragments;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import com.outspoken_kid.utils.StringUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.outspoken_kid.utils.LogUtils;
import com.zonecomms.napp.classes.BaseFragment;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.zonecomms.napp.IntentHandlerActivity;
import com.zonecomms.napp.MainActivity;
import com.zonecomms.napp.R;
import com.zonecomms.napp.classes.ZoneConstants;
import com.zonecomms.common.utils.ImageDownloadUtils;
import com.zonecomms.common.views.SideView;

/**
 * @author HyungGunKim
 *
 */
public class MainPage extends BaseFragment {

	private static ImageView ivProfileImage;
	
	private ScrollView scrollView;
	private RelativeLayout mainRelative;
	private FrameLayout colorFrame;
	private View[] menuBgs;
	private View logo;
	
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
		
		//Set menus.
		RelativeLayout.LayoutParams rp;
		
		//Box 'l'ength.
		int l = ResizeUtils.getSpecificLength(150);
		
		//Box 's'pace.
		int s = ResizeUtils.getSpecificLength(8);
		
		int l2 = l*2 + s;

		madeCount += 27;
		menuBgs = new View[23];
		
		//Notice.			id : 0
		View bgForNotice = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.leftMargin = s;
		rp.topMargin = s;
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		bgForNotice.setLayoutParams(rp);
		mainRelative.addView(bgForNotice);
		menuBgs[0] = bgForNotice;
		
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

		//NewPost.			id : 1
		View bgForNewPost = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount);
		bgForNewPost.setLayoutParams(rp);
		mainRelative.addView(bgForNewPost);
		menuBgs[1] = bgForNewPost;
		
		View newPost = new View(mContext);
		newPost.setLayoutParams(rp);
		newPost.setId(madeCount + 1);
		newPost.setBackgroundResource(R.drawable.img_newpost);
		newPost.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/newpost";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(newPost);

		//NewPost_thema.			id : 2
		View bgForNewPost_thema = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 1);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 1);
		bgForNewPost_thema.setLayoutParams(rp);
		mainRelative.addView(bgForNewPost_thema);
		menuBgs[2] = bgForNewPost_thema;
		
		View newPost_thema = new View(mContext);
		newPost_thema.setLayoutParams(rp);
		newPost_thema.setId(madeCount + 2);
		newPost_thema.setBackgroundResource(R.drawable.img_newpost_thema);
		newPost_thema.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/category?forpost=true&order=theme";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(newPost_thema);

		//NewPost_region.
		View bgForNewPost_Region = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 2);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 2);
		bgForNewPost_Region.setLayoutParams(rp);
		mainRelative.addView(bgForNewPost_Region);
		menuBgs[3] = bgForNewPost_Region;
		
		View newPost_region = new View(mContext);
		newPost_region.setLayoutParams(rp);
		newPost_region.setBackgroundResource(R.drawable.img_newpost_region);
		newPost_region.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/category?forpost=true&order=region";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(newPost_region);

		//Profile.		id : 3
		View bgForProfile = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l2, l2);
		rp.topMargin = s;
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount);
		rp.addRule(RelativeLayout.BELOW, madeCount);
		bgForProfile.setLayoutParams(rp);
		mainRelative.addView(bgForProfile);
		menuBgs[4] = bgForProfile;
		
		View profile = new View(mContext);
		profile.setLayoutParams(rp);
		profile.setId(madeCount + 3);
		profile.setBackgroundResource(R.drawable.img_profile);
		profile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/userhome" +
						"?member_id=" + MainActivity.myInfo.getMember_id() +
						"&menuindex=" + ZoneConstants.MENU_PROFILE;
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(profile);
		
		ivProfileImage = new ImageView(mContext);
		ivProfileImage.setLayoutParams(rp);
		ivProfileImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/userhome" +
						"?member_id=" + MainActivity.myInfo.getMember_id() +
						"&menuindex=" + ZoneConstants.MENU_PROFILE;
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		ivProfileImage.setBackgroundColor(Color.YELLOW);
		ivProfileImage.setScaleType(ScaleType.CENTER_CROP);
		ivProfileImage.setVisibility(View.INVISIBLE);
		mainRelative.addView(ivProfileImage);

		//MyPost.		id : 4
		View bgForMyPost = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.topMargin = s;
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 2);
		rp.addRule(RelativeLayout.BELOW, madeCount + 2);
		bgForMyPost.setLayoutParams(rp);
		mainRelative.addView(bgForMyPost);
		menuBgs[5] = bgForMyPost;
		
		View myPost = new View(mContext);
		myPost.setLayoutParams(rp);
		myPost.setId(madeCount + 4);
		myPost.setBackgroundResource(R.drawable.img_mypost);
		myPost.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/userhome" +
						"?member_id=" + MainActivity.myInfo.getMember_id() +
						"&menuindex=" + ZoneConstants.MENU_STORY;
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(myPost);

		//Message.
		View bgForMessage = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 4);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 4);
		bgForMessage.setLayoutParams(rp);
		mainRelative.addView(bgForMessage);
		menuBgs[6] = bgForMessage;
		
		View message = new View(mContext);
		message.setLayoutParams(rp);
		message.setBackgroundResource(R.drawable.img_message);
		message.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/userhome" +
						"?member_id=" + MainActivity.myInfo.getMember_id() +
						"&menuindex=" + ZoneConstants.MENU_MESSAGE;
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(message);

		//Scrap.		id : 5.
		View bgForScrap = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.topMargin = s;
		rp.rightMargin = s;
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 4);
		rp.addRule(RelativeLayout.BELOW, madeCount + 4);
		bgForScrap.setLayoutParams(rp);
		mainRelative.addView(bgForScrap);
		menuBgs[7] = bgForScrap;
		
		View scrap = new View(mContext);
		scrap.setLayoutParams(rp);
		scrap.setId(madeCount + 5);
		scrap.setBackgroundResource(R.drawable.img_scrap);
		scrap.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/userhome" +
						"?member_id=" + MainActivity.myInfo.getMember_id() +
						"&menuindex=" + ZoneConstants.MENU_SCRAP;
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(scrap);
		
		//Setting.
		View bgForSetting = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 5);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 5);
		bgForSetting.setLayoutParams(rp);
		mainRelative.addView(bgForSetting);
		menuBgs[8] = bgForSetting;
		
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

		//Friend_newPost.		id : 6
		View bgForFriend_newPost = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 3);
		rp.addRule(RelativeLayout.BELOW, madeCount + 3);
		rp.topMargin = s;
		bgForFriend_newPost.setLayoutParams(rp);
		mainRelative.addView(bgForFriend_newPost);
		menuBgs[9] = bgForFriend_newPost;
		
		View friend_newPost = new View(mContext);
		friend_newPost.setLayoutParams(rp);
		friend_newPost.setId(madeCount + 6);
		friend_newPost.setBackgroundResource(R.drawable.img_friend_newpost);
		friend_newPost.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/friendnewpost";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(friend_newPost);

		//Friend_management.		id : 7
		View bgForFriend_management = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 6);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 6);
		rp.leftMargin = s;
		bgForFriend_management.setLayoutParams(rp);
		mainRelative.addView(bgForFriend_management);
		menuBgs[10] = bgForFriend_management;
		
		View friend_management = new View(mContext);
		friend_management.setLayoutParams(rp);
		friend_management.setId(madeCount + 7);
		friend_management.setBackgroundResource(R.drawable.img_friend_management);
		friend_management.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/friend?userId="
										+ MainActivity.myInfo.getMember_id();
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(friend_management);

		//Friendship.				id : 8
		View bgForFriendship = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 6);
		rp.addRule(RelativeLayout.BELOW, madeCount + 6);
		rp.topMargin = s;
		bgForFriendship.setLayoutParams(rp);
		mainRelative.addView(bgForFriendship);
		menuBgs[11] = bgForFriendship;
		
		View friendship = new View(mContext);
		friendship.setLayoutParams(rp);
		friendship.setId(madeCount + 8);
		friendship.setBackgroundResource(R.drawable.img_friendship);
		friendship.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/friendto";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(friendship);

		//Friend_search.
		View bgForFriend_search = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 7);
		rp.addRule(RelativeLayout.BELOW, madeCount + 7);
		rp.topMargin = s;
		bgForFriend_search.setLayoutParams(rp);
		mainRelative.addView(bgForFriend_search);
		menuBgs[12] = bgForFriend_search;
		
		View friend_search = new View(mContext);
		friend_search.setLayoutParams(rp);
		friend_search.setBackgroundResource(R.drawable.img_friend_search);
		friend_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/friendsearch";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(friend_search);

		//Friend_list.
		View bgForFriend_list = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l2, l2);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 7);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 7);
		rp.leftMargin = s;
		bgForFriend_list.setLayoutParams(rp);
		mainRelative.addView(bgForFriend_list);
		menuBgs[13] = bgForFriend_list;
		
		View friend_list = new View(mContext);
		friend_list.setLayoutParams(rp);
		friend_list.setBackgroundResource(R.drawable.img_friend_list);
		friend_list.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/friend?userId="
						+ MainActivity.myInfo.getMember_id();
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(friend_list);

//		//Gethering.				id : 9
//		View bgForGethering = new View(mContext);
//		rp = new RelativeLayout.LayoutParams(l2, l2);
//		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 8);
//		rp.addRule(RelativeLayout.BELOW, madeCount + 8);
//		rp.topMargin = s;
//		bgForGethering.setLayoutParams(rp);
//		mainRelative.addView(bgForGethering);
//		menuBgs[14] = bgForGethering;
//		
//		View gethering = new View(mContext);
//		gethering.setLayoutParams(rp);
//		gethering.setId(madeCount + 9);
//		gethering.setBackgroundResource(R.drawable.img_gethering);
//		gethering.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/getheringlist";
//				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
//			}
//		});
//		mainRelative.addView(gethering);
//		
//		//Gethering_newpost.
//		View bgForGethering_newpost = new View(mContext);
//		rp = new RelativeLayout.LayoutParams(l, l);
//		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 9);
//		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 9);
//		rp.leftMargin = s;
//		bgForGethering_newpost.setLayoutParams(rp);
//		mainRelative.addView(bgForGethering_newpost);
//		menuBgs[15] = bgForGethering_newpost;
//		
//		View gethering_newpost = new View(mContext);
//		gethering_newpost.setLayoutParams(rp);
//		gethering_newpost.setBackgroundResource(R.drawable.img_gethering_newpost);
//		gethering_newpost.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/getheringnewpost";
//				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
//			}
//		});
//		mainRelative.addView(gethering_newpost);
//		
//		//Gethering_management.
//		View bgForGethering_management = new View(mContext);
//		rp = new RelativeLayout.LayoutParams(l, l);
//		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 9);
//		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 9);
//		rp.leftMargin = l + 2*s;
//		bgForGethering_management.setLayoutParams(rp);
//		mainRelative.addView(bgForGethering_management);
//		menuBgs[16] = bgForGethering_management;
//		
//		View gethering_management = new View(mContext);
//		gethering_management.setLayoutParams(rp);
//		gethering_management.setBackgroundResource(R.drawable.img_gethering_management);
//		gethering_management.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/getheringmanagement";
//				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
//			}
//		});
//		mainRelative.addView(gethering_management);
//		
//		//Gethering_intro.
//		View bgForGethering_intro = new View(mContext);
//		rp = new RelativeLayout.LayoutParams(l, l);
//		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 9);
//		rp.addRule(RelativeLayout.ALIGN_BOTTOM, madeCount + 9);
//		rp.leftMargin = s;
//		bgForGethering_intro.setLayoutParams(rp);
//		mainRelative.addView(bgForGethering_intro);
//		menuBgs[17] = bgForGethering_intro;
//		
//		View gethering_intro = new View(mContext);
//		gethering_intro.setLayoutParams(rp);
//		gethering_intro.setBackgroundResource(R.drawable.img_gethering_intro);
//		gethering_intro.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/getheringintro";
//				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
//			}
//		});
//		mainRelative.addView(gethering_intro);
//		
//		//Gethering_search.
//		View bgForGethering_search = new View(mContext);
//		rp = new RelativeLayout.LayoutParams(l, l);
//		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 9);
//		rp.addRule(RelativeLayout.ALIGN_BOTTOM, madeCount + 9);
//		rp.leftMargin = l + 2*s;
//		bgForGethering_search.setLayoutParams(rp);
//		mainRelative.addView(bgForGethering_search);
//		menuBgs[18] = bgForGethering_search;
//		
//		View gethering_search = new View(mContext);
//		gethering_search.setLayoutParams(rp);
//		gethering_search.setBackgroundResource(R.drawable.img_gethering_search);
//		gethering_search.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/getheringsearch";
//				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
//			}
//		});
//		mainRelative.addView(gethering_search);
		
		//napp.						id : 10
		View bgForGuide = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
//		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 9);
//		rp.addRule(RelativeLayout.BELOW, madeCount + 9);
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 8);
		rp.addRule(RelativeLayout.BELOW, madeCount + 8);
		rp.topMargin = s;
		bgForGuide.setLayoutParams(rp);
		mainRelative.addView(bgForGuide);
		menuBgs[19] = bgForGuide;

		View guide = new View(mContext);
		guide.setLayoutParams(rp);
		guide.setId(madeCount + 10);
		guide.setBackgroundResource(R.drawable.img_guide);
		guide.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/guide";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(guide);

		//qna.						id : 11
		View bgForQna = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 10);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 10);
		rp.leftMargin = s;
		bgForQna.setLayoutParams(rp);
		mainRelative.addView(bgForQna);
		menuBgs[20] = bgForQna;

		View qna = new View(mContext);
		qna.setLayoutParams(rp);
		qna.setId(madeCount + 11);
		qna.setBackgroundResource(R.drawable.img_qna);
		qna.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/qna";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(qna);

		//cooperate.					id : 12
		View bgForCooperate = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 11);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 11);
		rp.leftMargin = s;
		bgForCooperate.setLayoutParams(rp);
		mainRelative.addView(bgForCooperate);
		menuBgs[21] = bgForCooperate;

		View cooperate = new View(mContext);
		cooperate.setLayoutParams(rp);
		cooperate.setId(madeCount + 12);
		cooperate.setBackgroundResource(R.drawable.img_cooperate);
		cooperate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/cooperate";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		mainRelative.addView(cooperate);

		//skin.						id : 13
		View bgForSkin = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 12);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 12);
		rp.leftMargin = s;
		bgForSkin.setLayoutParams(rp);
		mainRelative.addView(bgForSkin);
		menuBgs[22] = bgForSkin;

		View skin = new View(mContext);
		skin.setLayoutParams(rp);
		skin.setId(madeCount + 13);
		skin.setBackgroundResource(R.drawable.img_skin);
		skin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(colorFrame.getVisibility() != View.VISIBLE) {
					showColorFrame();
				} else {
					hideColorFrame();
				}
			}
		});
		mainRelative.addView(skin);
		
///////////////////////////////////////////////////////
		
		//Colorframe.					id : 14
		colorFrame = new FrameLayout(mContext);
		rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, l2);
		rp.addRule(RelativeLayout.ALIGN_RIGHT, madeCount + 13);
		rp.addRule(RelativeLayout.BELOW, madeCount + 13);
		rp.leftMargin = s;
		colorFrame.setLayoutParams(rp);
		colorFrame.setId(madeCount + 14);
		colorFrame.setBackgroundColor(Color.BLACK);
		colorFrame.setVisibility(View.GONE);
		mainRelative.addView(colorFrame);
		
		FrameLayout.LayoutParams fp = null;
		int topMargin = s;
		
		OnClickListener ocl = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int index = Integer.parseInt(v.getTag().toString());
				changeMenuColor(index);
				hideColorFrame();
			}
		};
		
		for(int i=0; i<8; i++) {
			
			if(i==4) {
				topMargin = l + s*2;
			}
			
			try {
				View v = new View(mContext);
				fp = new FrameLayout.LayoutParams(l, l);
				fp.leftMargin = (i%4) *	(l+s);
				fp.topMargin = topMargin;
				fp.gravity = Gravity.LEFT|Gravity.TOP;
				v.setLayoutParams(fp);
				v.setBackgroundColor(MainActivity.startupInfo.getMenuColorSets()[i].getColors()[0]);
				v.setTag("" + i);
				v.setOnClickListener(ocl);
				colorFrame.addView(v);
			} catch(Exception e) {
				LogUtils.trace(e);
			}
		}
		
		int colorIndex = SharedPrefsUtils.getIntegerFromPrefs(ZoneConstants.PREFS_COLOR, "colorIndex");
		changeMenuColor(colorIndex);
		
		//logo.							id : 15
		logo = new View(mContext);
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(328), ResizeUtils.getSpecificLength(32));
		rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		rp.addRule(RelativeLayout.BELOW, madeCount + 14);
		rp.topMargin = s*3;
		rp.rightMargin = s*3;
		logo.setLayoutParams(rp);
		logo.setId(madeCount + 15);
		logo.setBackgroundColor(Color.MAGENTA);
		logo.setBackgroundResource(R.drawable.img_company);
		mainRelative.addView(logo);

		//Bottom blank.
		View bottomBlank = new View(mContext);
		rp = new RelativeLayout.LayoutParams(s, s);
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 15);
		rp.addRule(RelativeLayout.BELOW, madeCount + 15);
		bottomBlank.setLayoutParams(rp);
		mainRelative.addView(bottomBlank);
		
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
			
			if(ivProfileImage != null) {
				ImageDownloadUtils.downloadImageImmediately(url, "", ivProfileImage, 308, true);
			}
			
			SideView profileView = mActivity.getProfileView();
			
			if(profileView != null) {
				if(!StringUtils.isEmpty(url) && profileView.getIcon() != null) {
					ImageDownloadUtils.downloadImageImmediately(url, "", profileView.getIcon(), 80, true);
				}
				
				String nickname = MainActivity.myInfo.getMember_nickname();
				if(!StringUtils.isEmpty(nickname)) {
					profileView.setTitle(nickname);
				}
			}
		}
	}

	@Override
	protected int getContentViewId() {
		
		return R.id.mainPage_mainRelative;
	}

	@Override
	public boolean onBackKeyPressed() {
		
		if(colorFrame.getVisibility() == View.VISIBLE) {
			hideColorFrame();
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
			mActivity.getTitleBar().showPlusAppButton();
			mActivity.getTitleBar().hideThemaButton();
			mActivity.getTitleBar().hideRegionButton();
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
	
	public void showColorFrame() {
		
		if(colorFrame.getVisibility() != View.VISIBLE) {
			colorFrame.setVisibility(View.VISIBLE);
			scrollView.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					
					scrollView.fullScroll(View.FOCUS_DOWN);
				}
			}, 100);
		}
	}
	
	public void hideColorFrame() {
		
		if(colorFrame.getVisibility() == View.VISIBLE) {
			colorFrame.setVisibility(View.GONE);
		}
	}

	public void changeMenuColor(int index) {
		
		int[] colorSet;
		
		if(MainActivity.startupInfo == null
				|| MainActivity.startupInfo.getMenuColorSets() == null
				|| MainActivity.startupInfo.getMenuColorSets().length == 0
				|| MainActivity.startupInfo.getMenuColorSets().length <= index
				|| MainActivity.startupInfo.getMenuColorSets()[index].getColors() == null
				|| MainActivity.startupInfo.getMenuColorSets()[index].getColors().length != 4) {
			colorSet = new int[] {
				Color.rgb(125, 125, 125),
				Color.rgb(100, 100, 100),
				Color.rgb(75, 75, 75),
				Color.rgb(50, 50, 50)
			};
		} else {
			colorSet = MainActivity.startupInfo.getMenuColorSets()[index].getColors();
		}
		
		int color = colorSet[0];
		int size = menuBgs.length;
		for(int i=0; i<size; i++) {
			
			if(i == 4) {
				color = colorSet[1];
			} else if(i == 9) {
				color = colorSet[2];
			} else if(i == 14) {
				color = colorSet[3];
			} else if(i == 19) {
				color = colorSet[2];
			}
			
			if(menuBgs[i] != null) {
				menuBgs[i].setBackgroundColor(color);
			}
		}
	
		SharedPrefsUtils.addDataToPrefs(ZoneConstants.PREFS_COLOR, "colorIndex", index);
	}
	
	public void setScrollToTop() {
		
		scrollView.scrollTo(0, 0);
	}

	public static ImageView getProfileImageView() {
		
		return ivProfileImage;
	}

	@Override
	protected String generateDownloadKey() {
		return "MAINPAGE" + madeCount;
	}

	@Override
	protected int getXmlResId() {
		return R.layout.page_main;
	}
}