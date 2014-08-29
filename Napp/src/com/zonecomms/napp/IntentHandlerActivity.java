package com.zonecomms.napp;

import java.net.URLDecoder;

import com.zonecomms.napp.classes.ApplicationManager;
import com.outspoken_kid.utils.IntentUtils;
import com.outspoken_kid.utils.LogUtils;
import com.zonecomms.napp.classes.ZoneConstants;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import com.outspoken_kid.utils.StringUtils;

import android.view.View;
import android.view.Window;

public class IntentHandlerActivity extends Activity {
	
	private View bg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		bg = new View(this);
		bg.setBackgroundResource(android.R.color.transparent);
		bg.setBackgroundColor(Color.argb(100, 100, 0, 0));
		setContentView(bg);

		handlingIntent(getIntent());
	}
	
	public void handlingIntent(final Intent intent) {

		try {
			if(ApplicationManager.getFragmentsSize() != 0) {
				Intent mainActivityIntent = new Intent(this, MainActivity.class);
				mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
				startActivity(mainActivityIntent);
				
				if(intent.getData() != null) {
					actionByUri(intent.getData());
				}
			} else {
				if(IntroActivity.isInIntro) {
					bg.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									handlingIntent(intent);
								}
							});
						}
					}, 5000);
				} else {
					showIntroActivity(intent);
				}
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}

		finish();
	}

	public void showIntroActivity(Intent intent) {
		
		if(intent != null) {
			Intent i = new Intent(this, IntroActivity.class);
			
			if(intent.getData() != null) {
				i.setData(intent.getData());
			}
			
			i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(i);
		}
	}
	
	public static void actionByUri(Uri uri) {

		try {
			String scheme = uri.getScheme();
			String host = uri.getHost();
			String url = host + uri.getPath();
			
			LogUtils.log("IntentHandlerActivity.actionByUri. ========" +
					"\nuri : " + uri + "\nscheme : "+ scheme
					+ "\nhost : " + host + "\nurl : " + url + "\nPAppId :" + ZoneConstants.PAPP_ID + "\n=========");
			MainActivity mActivity = ApplicationManager.getInstance().getActivity();
			
			if(scheme.equals("http")||scheme.equals("https")) {
				IntentUtils.showDeviceBrowser(mActivity, uri.toString());
			} else if(scheme.equals("market") || scheme.equals("tstore")) {
				IntentUtils.showMarket(mActivity, uri.toString());
			} else if(scheme.equals("popup")) {
				String message = uri.getQueryParameter("message");
				message = URLDecoder.decode(message, "utf-8");
				mActivity.showAlertDialog(null, message, null, false);
			} else if(scheme.equals(ZoneConstants.PAPP_ID)) {
				String title = "";
				
				//유저 홈.
				if(url.equals("android.zonecomms.com/userhome")) {
					String member_id = uri.getQueryParameter("member_id");
					
					int menuIndex = 0;
					if(!StringUtils.isEmpty(uri.getQueryParameter("menuindex"))) {
						menuIndex = Integer.parseInt(uri.getQueryParameter("menuindex"));
					}
					
					mActivity.showUserPage(member_id, menuIndex);
					
				//카테고리.
				} else if(url.equals("android.zonecomms.com/category")) {
					String forPost = uri.getQueryParameter("forpost");
					String order = uri.getQueryParameter("order");
					
					int type = ZoneConstants.TYPE_CATEGORY_THEMA;
					
					if(!StringUtils.isEmpty(order) && order.equals("region")) {
						type = ZoneConstants.TYPE_CATEGORY_REGION;
					}
					
					mActivity.showCategoryPage(type, (!StringUtils.isEmpty(forPost) && forPost.equals("true")));
					
				//공지사항.
				} else if(url.equals("android.zonecomms.com/notice")) {
					title = mActivity.getString(R.string.titleText_notice);
					mActivity.showListPage(title, ZoneConstants.TYPE_LIST_NOTICE, null);
					
				//카테고리별 Papp.
				} else if(url.equals("android.zonecomms.com/categorypaaps")) {
					title = uri.getQueryParameter("s_cate_nm");
					String s_cate_id = uri.getQueryParameter("s_cate_id");
					mActivity.showListPage(title, ZoneConstants.TYPE_LIST_PAPP, s_cate_id);
					
				//최신글.
				} else if(url.equals("android.zonecomms.com/newpost")) {
					title = mActivity.getString(R.string.titleText_allPost);
					mActivity.showGridPage(2, title, ZoneConstants.TYPE_GRID_POST, "010");

				//카테고리별 새글.
				} else if(url.equals("android.zonecomms.com/categoryposts")) {
					title = uri.getQueryParameter("s_cate_nm");
					String s_cate_id = uri.getQueryParameter("s_cate_id");
					mActivity.showGridPage(2, title, ZoneConstants.TYPE_GRID_POST_CATEGORY, s_cate_id);
					
				//친구 새글.
				} else if(url.equals("android.zonecomms.com/friendnewpost")) {
					title = mActivity.getString(R.string.titleText_friendNewPost);
					mActivity.showGridPage(2, title, ZoneConstants.TYPE_GRID_POST_FRIENDS, null);
				
				//친구 하기.
				} else if(url.equals("android.zonecomms.com/friendto")) {
					title = mActivity.getString(R.string.titleText_friendFind);
					mActivity.showGridPage(2, title, ZoneConstants.TYPE_GRID_POST, "040");
					
				//친구 검색.
				} else if(url.equals("android.zonecomms.com/friendsearch")) {
					title = mActivity.getString(R.string.titleText_friendSearch);
					mActivity.showGridPage(4, title, ZoneConstants.TYPE_GRID_MEMBER_SEARCH, "");
					
				//친구 목록.
				} else if(url.equals("android.zonecomms.com/friend")) {
					String userId = uri.getQueryParameter("userId");
					title = mActivity.getString(R.string.titleText_friendList);
					mActivity.showGridPage(4, title, ZoneConstants.TYPE_GRID_MEMBER, userId);
					
				//환경설정.
				} else if(url.equals("android.zonecomms.com/setting")) {
					mActivity.showSettingPage();
					
				//글 상세보기.
				} else if(url.equals("android.zonecomms.com/post")) {
					title = uri.getQueryParameter("title");
					int spot_nid = Integer.parseInt(uri.getQueryParameter("spot_nid"));
					String gethering = uri.getQueryParameter("isGethering");
					boolean isGethering = false;
					
					if(!StringUtils.isEmpty(gethering) && gethering.equals("true")) {
						isGethering = true;
					}
					
					mActivity.showPostPage(title, spot_nid, isGethering);
					
				//메세지 페이지.
				} else if(url.equals("android.zonecomms.com/message")) {
					String member_id = uri.getQueryParameter("member_id");
					mActivity.showMessagePage(member_id);
					
				//기본 프로필 설정.
				} else if(url.equals("android.zonecomms.com/baseprofile")) {
					mActivity.showBaseProfilePage();
					
				//추가 프로필 설정.
				} else if(url.equals("android.zonecomms.com/addedprofile")) {
					mActivity.showAddedProfilePage();

				//이용 안내.
				} else if(url.equals("android.zonecomms.com/guide")) {
					title = mActivity.getString(R.string.titleText_guide);
					mActivity.showListPage(title, ZoneConstants.TYPE_LIST_GUIDE, null);
					
				//Q&A.
				} else if(url.equals("android.zonecomms.com/qna")) {
					title = mActivity.getString(R.string.titleText_qna);
					mActivity.showListPage(title, ZoneConstants.TYPE_LIST_QNA, null);
					
				//제휴.
				} else if(url.equals("android.zonecomms.com/cooperate")) {
					mActivity.showCooperateActivity();

				//모임 상세 페이지.
				} else if(url.equals("android.zonecomms.com/gethering")) {
					String sb_id = uri.getQueryParameter("sb_id");
					mActivity.showGetheringPage(sb_id);
					
				//모임 리스트.
				} else if(url.equals("android.zonecomms.com/getheringlist")) {
					title = mActivity.getString(R.string.titleText_gethering);
					mActivity.showGetheringListPage(title, 0);
					
				//모임 새글.
				} else if(url.equals("android.zonecomms.com/getheringnewpost")) {
					title = mActivity.getString(R.string.titleText_getheringNewPost);
					mActivity.showGridPage(2, title, ZoneConstants.TYPE_GRID_POST_GETHERING, null);
					
				//모임 관리.
				} else if(url.equals("android.zonecomms.com/getheringmanagement")) {
					title = mActivity.getString(R.string.titleText_gethering);
					mActivity.showGetheringListPage(title, 1);
					
				//모임 소개.
				} else if(url.equals("android.zonecomms.com/getheringintro")) {
					title = mActivity.getString(R.string.titleText_getheringIntro);
					mActivity.showGridPage(2, title, ZoneConstants.TYPE_GRID_POST_GETHERING_INTRO, null);
					
				//모임 검색.
				} else if(url.equals("android.zonecomms.com/getheringsearch")) {
					title = mActivity.getString(R.string.titleText_getheringSearch);
					mActivity.showGetheringListPage(title, -1);
					
				} else {
					handleInvalidUri();
				}
			} else {
				handleInvalidUri();
			}
		} catch(Exception e) {
			LogUtils.trace(e);
			handleInvalidUri();
		}
	}
	
	public static void handleInvalidUri() {
//		ToastUtils.showToast(R.string.invalidUri);
//		ApplicationManager.getInstance().getMain().showMainPage();
	}
}
