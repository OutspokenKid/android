package com.zonecomms.festivalwdjf;

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
import com.zonecomms.festivalwdjf.MainActivity.OnAfterLoginListener;
import com.zonecomms.festivalwdjf.classes.ApplicationManager;
import com.zonecomms.festivalwdjf.classes.ZoneConstants;

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
	
	public static void actionByUri(final Uri uri) {

		try {
			String scheme = uri.getScheme();
			String host = uri.getHost();
			String url = host + uri.getPath();
			
			LogUtils.log("IntentHandlerActivity.actionByUri. ========" +
					"\nuri : " + uri + "\nscheme : "+ scheme
					+ "\nhost : " + host + "\nurl : " + url + "\nPAppId :" + ZoneConstants.PAPP_ID + "\n=========");
			
			if(scheme.equals("http")||scheme.equals("https")) {
				ApplicationManager.getInstance().getActivity().showDeviceBrowser(uri.toString());
			} else if(scheme.equals("market") || scheme.equals("tstore")) {
				ApplicationManager.getInstance().getActivity().showDownloadPage(uri.toString(), null);
			} else if(scheme.equals("popup")) {
				String message = uri.getQueryParameter("message");
				message = URLDecoder.decode(message, "utf-8");
				ApplicationManager.getInstance().getActivity().showAlertDialog(null, message, null, false);
			} else if(scheme.equals(ZoneConstants.PAPP_ID)) {

				final MainActivity mActivity = ApplicationManager.getInstance().getActivity();
				String title = null;
				
				//인포메이션.
				if(url.equals("android.zonecomms.com/information")) {
					mActivity.showInformationPage();
					
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
					title = "NOTICE";
					mActivity.showListPage(title, ZoneConstants.TYPE_NOTICE);
				
				//이벤트.
				} else if(url.equals("android.zonecomms.com/event")) {
					title = "EVENT";
					mActivity.showListPage(title, ZoneConstants.TYPE_EVENT);
				
				//스토리.
				} else if(url.equals("android.zonecomms.com/story")) {
					title = "STORY";
					mActivity.showGridPage(2, title, ZoneConstants.TYPE_STORY, null);
				
				//티켓.
				} else if(url.equals("android.zonecomms.com/ticket")) {
					String urlString = "http://m.ticket.interpark.com/contents/goods_info.html" +
							"?GOODS_CODE=13011732";
					mActivity.showWebBrowser(urlString, null);
					
				//라인업.
				} else if(url.equals("android.zonecomms.com/lineup")) {
					mActivity.showLineupPage();
				
				//뮤직.
				} else if(url.equals("android.zonecomms.com/music")) {
					title = "MUSIC";
					mActivity.showListPage(title, ZoneConstants.TYPE_MUSIC);
					
				//비디오.
				} else if(url.equals("android.zonecomms.com/video")) {
					title = "VIDEO";
					mActivity.showListPage(title, ZoneConstants.TYPE_VIDEO);
					
				//포토.
				} else if(url.equals("android.zonecomms.com/photo")) {
					title = "PHOTO";
					mActivity.showGridPage(2, title, ZoneConstants.TYPE_PHOTO, null);
					
				//멤버 보기.
				} else if(url.equals("android.zonecomms.com/member")) {
					
					mActivity.checkLoginAndExecute(new OnAfterLoginListener() {
						
						@Override
						public void onAfterLogin() {
							
							String title = "MEMBER";
							mActivity.showGridPage(4, title, ZoneConstants.TYPE_MEMBER, null);
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
					
				//VVIP.
				} else if(url.equals("android.zonecomms.com/vvip")) {
					String urlString = "http://www.maxoutkorea.com/sub/vvip.php";
					mActivity.showWebBrowser(urlString, null);
					
				//스케쥴.
				} else if(url.equals("android.zonecomms.com/schedule")) {
					title = "SCHEDULE";
					mActivity.showGridPage(2, title, ZoneConstants.TYPE_SCHEDULE, null);
					
				//로케이션.
				} else if(url.equals("android.zonecomms.com/location")) {
					String urlString = "http://www.naver.com";
					mActivity.showWebBrowser(urlString, null);
					
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
					
				//스태프.
				} else if(url.equals("android.zonecomms.com/staff")) {
					mActivity.showStaffPage();
					
				//Google map.	
				} else if(url.equals("android.zonecomms.com/map")) {
					String latitude = uri.getQueryParameter("latitude");
					String longitude = uri.getQueryParameter("longitude");
					mActivity.showLocationWithGoogleMap(
							Double.parseDouble(latitude), 
							Double.parseDouble(longitude));
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
//		ApplicationManager.getInstance().getMain().showMainPage();
	}
}
