package com.zonecomms.clubmania;

import java.net.URLDecoder;

import com.outspoken_kid.classes.ApplicationManager;
import com.outspoken_kid.utils.LogUtils;
import com.zonecomms.clubmania.classes.ZoneConstants;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
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
			e.printStackTrace();
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
			
			if(scheme.equals("http")||scheme.equals("https")) {
				ApplicationManager.getInstance().getMainActivity().showDeviceBrowser(uri.toString());
			} else if(scheme.equals("market") || scheme.equals("tstore")) {
				ApplicationManager.getInstance().getMainActivity().showDownloadPage(uri.toString(), null);
			} else if(scheme.equals("popup")) {
				String message = uri.getQueryParameter("message");
				message = URLDecoder.decode(message, "utf-8");
				ApplicationManager.getInstance().getMainActivity().showAlertDialog(null, message, null, false);
			} else if(scheme.equals(ZoneConstants.PAPP_ID)) {

				MainActivity mActivity = ApplicationManager.getInstance().getMainActivity();
				
				//유저 홈.
				if(url.equals("android.zonecomms.com/userhome")) {
					String member_id = uri.getQueryParameter("member_id");
					mActivity.showUserPage(member_id, 0);
					
				//공지사항.
				} else if(url.equals("android.zonecomms.com/notice")) {
					mActivity.showListPage("NOTICE", ZoneConstants.TYPE_NOTICE);
					
				//스케쥴.
				} else if(url.equals("android.zonecomms.com/schedule")) {
					String sb_id = uri.getQueryParameter("sb_id");
					String title = uri.getQueryParameter("title");
					mActivity.showGridPage(2, ZoneConstants.TYPE_SCHEDULE, !TextUtils.isEmpty(title)? title : "SCHEDULE", sb_id);
					
				//이벤트.
				} else if(url.equals("android.zonecomms.com/event")) {
					mActivity.showListPage("EVENT", ZoneConstants.TYPE_EVENT);
				
				//스마트 패스.
				} else if(url.equals("android.zonecomms.com/smartpass")) {
					mActivity.showListPage("SMART PASS", ZoneConstants.TYPE_SMARTPASS);
					
				//글.
				} else if(url.equals("android.zonecomms.com/talk")) {
					
					String type = uri.getQueryParameter("type");
					if(TextUtils.isEmpty(type)) {
						handleInvalidUri();
					} else if(type.equals("club")) {
						mActivity.showGridPage(2, ZoneConstants.TYPE_POST, "CLUB", "1");
					} else if(type.equals("party")) {
						mActivity.showGridPage(2, ZoneConstants.TYPE_POST, "PARTY", "5");
					} else if(type.equals("festival")) {
						mActivity.showGridPage(2, ZoneConstants.TYPE_POST, "FESTIVAL", "7");
					} else if(type.equals("lounge")) {
						mActivity.showGridPage(2, ZoneConstants.TYPE_POST, "LOUNGE", "2");
					} else {
						handleInvalidUri();
					}
					
				//이미지.
				} else if(url.equals("android.zonecomms.com/image")) {
					String sb_id = uri.getQueryParameter("sb_id");
					String title = uri.getQueryParameter("title");
					
					if(!TextUtils.isEmpty(sb_id)) {
						mActivity.showGridPage(2, ZoneConstants.TYPE_PHOTO, !TextUtils.isEmpty(title)? title : "PHOTO", sb_id);
					}
					
				//뮤직.
				} else if(url.equals("android.zonecomms.com/music")) {
					mActivity.showListPage("MUSIC", ZoneConstants.TYPE_MUSIC);
					
				//비디오.
				} else if(url.equals("android.zonecomms.com/video")) {
					mActivity.showListPage("VIDEO", ZoneConstants.TYPE_VIDEO);
					
				//멤버 보기.
				} else if(url.equals("android.zonecomms.com/member")) {
					mActivity.showGridPage(4, ZoneConstants.TYPE_MEMBER, "MEMBER", null);
					
				//환경설정.
				} else if(url.equals("android.zonecomms.com/setting")) {
					mActivity.showSettingPage();
					
				//글 상세보기.
				} else if(url.equals("android.zonecomms.com/post")) {
					String title = uri.getQueryParameter("title");
					int spot_nid = Integer.parseInt(uri.getQueryParameter("spot_nid"));
					mActivity.showPostPage(title, spot_nid);
					
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
					
				} else if(url.equals("android.zonecomms.com/contact")) {
					mActivity.showCooperateActivity();

				} else if(url.equals("android.zonecomms.com/cafe")) {
					String urlString = "http://cafe.naver.com/sdhpro";
					ApplicationManager.getInstance().getMainActivity().showWebBrowser(urlString, null);
				} else if(url.equals("android.zonecomms.com/facebook")) {
					String urlString = "https://www.facebook.com/sdhpro";
					ApplicationManager.getInstance().getMainActivity().showWebBrowser(urlString, null);
				} else if(url.equals("android.zonecomms.com/twitter")) {
					String urlString = "https://twitter.com/clubmania_";
					ApplicationManager.getInstance().getMainActivity().showWebBrowser(urlString, null);
					
				} else if(url.equals("android.zonecomms.com/appsforphoto")) {
					mActivity.showGridPage(4, ZoneConstants.TYPE_PAPP_PHOTO, "PHOTO", null);
					
				} else if(url.equals("android.zonecomms.com/appsforschedule")) {

					String type = uri.getQueryParameter("type");
					
					if(TextUtils.isEmpty(type)) {
						handleInvalidUri();
					} else if(type.equals("club")) {
						mActivity.showGridPage(4, ZoneConstants.TYPE_PAPP_SCHEDULE, mActivity.getString(R.string.selectClub), "1");
					} else if(type.equals("lounge")) {
						mActivity.showGridPage(4, ZoneConstants.TYPE_PAPP_SCHEDULE, mActivity.getString(R.string.selectLounge), "2");
					} else {
						handleInvalidUri();
					}
				} else {
					handleInvalidUri();
				}
			} else {
				handleInvalidUri();
			}
		} catch(Exception e) {
			e.printStackTrace();
			handleInvalidUri();
		}
	}
	
	public static void handleInvalidUri() {
//		ToastUtils.showToast(R.string.invalidUri);
//		ApplicationManager.getInstance().getMainActivity().showMainPage();
	}
}
