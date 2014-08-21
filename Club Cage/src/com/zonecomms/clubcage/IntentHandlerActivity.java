package com.zonecomms.clubcage;

import java.net.URLDecoder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.StringUtils;
import com.zonecomms.clubcage.MainActivity.OnAfterLoginListener;
import com.zonecomms.clubcage.classes.ZoneConstants;
import com.zonecomms.clubcage.classes.ZonecommsApplication;

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
			if(ZonecommsApplication.getActivity() != null
					&& ZonecommsApplication.getActivity().getFragmentsSize() != 0) {

				LogUtils.log("###IntentHandlerActivity.handlingIntent.  On mainActivity");
				
				Intent mainActivityIntent = new Intent(this, MainActivity.class);
				mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
				startActivity(mainActivityIntent);
				
				if(intent.getData() != null) {
					LogUtils.log("###IntentHandlerActivity.handlingIntent.  On mainActivity, has data");
					actionByUri(intent.getData());
				}
				
			} else  if(ZonecommsApplication.getCircleActivity() != null) {
				
				LogUtils.log("###IntentHandlerActivity.handlingIntent.  On cirleMainActivity.");
				
				Intent mainActivityIntent = new Intent(this, CircleMainActivity.class);
				mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
				startActivity(mainActivityIntent);
				
				if(intent.getData() != null) {
					LogUtils.log("###IntentHandlerActivity.handlingIntent.  On cirleMainActivity, has data.");
					actionByUri(intent.getData());
				}
			} else {
				LogUtils.log("###IntentHandlerActivity.handlingIntent.  app is not running.");
				
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
	
	public static void actionByUri(final Uri uri) {

		try {
			String scheme = uri.getScheme();
			String host = uri.getHost();
			String url = host + uri.getPath();
			
			LogUtils.log("IntentHandlerActivity.actionByUri. ========" +
					"\nuri : " + uri + "\nscheme : "+ scheme
					+ "\nhost : " + host + "\nurl : " + url + "\nPAppId :" + ZoneConstants.PAPP_ID + "\n=========");
			
			if(scheme.equals("http")||scheme.equals("https")) {
				ZonecommsApplication.getActivity().showDeviceBrowser(uri.toString());
			} else if(scheme.equals("market") || scheme.equals("tstore")) {
				ZonecommsApplication.getActivity().showDownloadPage(uri.toString(), null);
			} else if(scheme.equals("popup")) {
				String message = uri.getQueryParameter("message");
				message = URLDecoder.decode(message, "utf-8");
				
				if(ZonecommsApplication.getCircleActivity() != null) {
					ZonecommsApplication.getCircleActivity().showAlertDialog(null, message, 
							ZonecommsApplication.getActivity().getString(R.string.confirm), null, null, null);
				} else {
					ZonecommsApplication.getActivity().showAlertDialog(null, message, 
							ZonecommsApplication.getActivity().getString(R.string.confirm), null);
				}
			} else if(scheme.equals(ZoneConstants.PAPP_ID)) {

				final MainActivity mActivity = ZonecommsApplication.getActivity();
				
				
				if(mActivity == null) {
					
					if(ZonecommsApplication.getCircleActivity() != null) {
						ZonecommsApplication.getCircleActivity().launchToMainActivity(uri);
					}
					
					return;
				}
				
				//인포메이션.
				if(url.equals("android.zonecomms.com/information")) {
					mActivity.showInformationPage();
				
				//마이 홈.
				} else if(url.equals("android.zonecomms.com/home")) {
					mActivity.checkLoginAndExecute(new OnAfterLoginListener() {
						
						@Override
						public void onAfterLogin() {

							String member_id = ZonecommsApplication.myInfo.getMember_id();
							String index = uri.getQueryParameter("index");
							
							int menuIndex = 0;
							
							if(!StringUtils.isEmpty(index)) {
								try {
									menuIndex = Integer.parseInt(index);
								} catch(Exception e) {
									menuIndex = 0;
								}
							} else{
								menuIndex = 0;
							}
							
							mActivity.showUserPage(member_id, menuIndex);
						}
					});
					
				//유저 홈.
				} else if(url.equals("android.zonecomms.com/userhome")) {
					
					mActivity.checkLoginAndExecute(new OnAfterLoginListener() {
						
						@Override
						public void onAfterLogin() {

							String member_id = uri.getQueryParameter("member_id");
							String index = uri.getQueryParameter("index");
							
							int menuIndex = 0;
							
							if(!StringUtils.isEmpty(index)) {
								try {
									menuIndex = Integer.parseInt(index);
								} catch(Exception e) {
									menuIndex = 0;
								}
							} else{
								menuIndex = 0;
							}
							
							mActivity.showUserPage(member_id, menuIndex);
						}
					});

				//공지사항.
				} else if(url.equals("android.zonecomms.com/notice")) {
					mActivity.showListPage("NOTICE", ZoneConstants.TYPE_NOTICE);
					
				//스케쥴.
				} else if(url.equals("android.zonecomms.com/schedule")) {
//					mActivity.showGridPage(2, "SCHEDULE", 0);
					mActivity.showGridPage("SCHEDULE", ZoneConstants.TYPE_SCHEDULE);
					
				//이벤트.
				} else if(url.equals("android.zonecomms.com/event")) {
					mActivity.showListPage("EVENT", ZoneConstants.TYPE_EVENT);
					
				//왁자지껄.
				} else if(url.equals("android.zonecomms.com/freetalk")) {
//					mActivity.showGridPage(2, "TALK", 1);
					mActivity.showGridPage("STORY", ZoneConstants.TYPE_STORY);
					
//				//생생후기.
//				} else if(url.equals("android.zonecomms.com/review")) {
//					mActivity.showGridPage(2, "TALK", 2);
//					
//				//함께가기.
//				} else if(url.equals("android.zonecomms.com/with")) {
//					mActivity.showGridPage(2, "TALK", 3);
//					
//				//공개수배.
//				} else if(url.equals("android.zonecomms.com/find")) {
//					mActivity.showGridPage(2, "TALK", 4);
					
				//이미지.
				} else if(url.equals("android.zonecomms.com/image")) {
//					mActivity.showGridPage(2, "PHOTO", 0);
					mActivity.showGridPage("PHOTO", ZoneConstants.TYPE_PHOTO);
					
				//뮤직.
				} else if(url.equals("android.zonecomms.com/music")) {
//					mActivity.showListPage("MUSIC");
					mActivity.showListPage("MUSIC", ZoneConstants.TYPE_MUSIC);
					
				//비디오.
				} else if(url.equals("android.zonecomms.com/video")) {
//					mActivity.showListPage("VIDEO");
					mActivity.showListPage("VIDEO", ZoneConstants.TYPE_VIDEO);
					
				//멤버 보기.
				} else if(url.equals("android.zonecomms.com/member")) {
					
					mActivity.checkLoginAndExecute(new OnAfterLoginListener() {
						
						@Override
						public void onAfterLogin() {
							
//							mActivity.showGridPage(4, "MEMBER", 0);
							mActivity.showGridPage("MEMBER", ZoneConstants.TYPE_MEMBER);
						}
					});
					
				//환경설정.
				} else if(url.equals("android.zonecomms.com/setting")) {
					mActivity.checkLoginAndExecute(new OnAfterLoginListener() {
						
						@Override
						public void onAfterLogin() {
							
							mActivity.showSettingPage();
						}
					});
					
				//글 상세보기.
				} else if(url.equals("android.zonecomms.com/post")) {
					int spot_nid = Integer.parseInt(uri.getQueryParameter("spot_nid"));
					mActivity.showPostPage(spot_nid);
					
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
	}
}
