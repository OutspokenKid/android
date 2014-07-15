package com.zonecomms.golfn;

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
import com.zonecomms.golfn.classes.ApplicationManager;
import com.zonecomms.golfn.classes.ZoneConstants;
import com.zonecomms.golfn.fragments.MainPage;

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
			
			if(scheme.equals("http")||scheme.equals("https")) {
				ApplicationManager.getInstance().getActivity().showDeviceBrowser(uri.toString());
			} else if(scheme.equals("market") || scheme.equals("tstore")) {
				ApplicationManager.getInstance().getActivity().showDownloadPage(uri.toString(), null);
			} else if(scheme.equals("popup")) {
				String message = uri.getQueryParameter("message");
				message = URLDecoder.decode(message, "utf-8");
				ApplicationManager.getInstance().getActivity().showAlertDialog(null, message, null, false);
			} else if(scheme.equals(ZoneConstants.PAPP_ID)) {

				MainActivity mActivity = ApplicationManager.getInstance().getActivity();
				String title = null;
				
				//마니아리포트 로고.
				if(url.equals("android.zonecomms.com/logo_maniaReport")) {
					String urlString = "http://www.maniareport.com";
					ApplicationManager.getInstance().getActivity().showWebBrowser(urlString, null);
					
				//유저 홈.
				} else if(url.equals("android.zonecomms.com/userhome")) {
					String member_id = uri.getQueryParameter("member_id");
					int menuIndex = 0;
					
					try {
						menuIndex = Integer.parseInt(uri.getQueryParameter("menuIndex"));
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (Error e) {
						LogUtils.trace(e);
					}
					
					mActivity.showUserPage(member_id, menuIndex);
				
				//제휴 문의.
				} else if(url.equals("android.zonecomms.com/cooperation")) {
					mActivity.showCooperateActivity();
					
				//이벤트.
				} else if(url.equals("android.zonecomms.com/event")) {
					title = mActivity.getString(R.string.title_event);
					mActivity.showListPage(title, ZoneConstants.TYPE_EVENT);
				
				//뉴스.
				} else if(url.equals("android.zonecomms.com/news")) {
					title = mActivity.getString(R.string.title_news);
					mActivity.showArticleListPage(title, 3);
				//레슨.
				} else if(url.equals("android.zonecomms.com/lesson")) {
					title = mActivity.getString(R.string.title_lesson);
					mActivity.showArticleListPage(title, 4);
					
				//마니아 TV.
				} else if(url.equals("android.zonecomms.com/maniatv")) {
					title = mActivity.getString(R.string.title_maniatv);
					mActivity.showArticleListPage(title, 5);
					
				//마니아 마켓.
				} else if(url.equals("android.zonecomms.com/market")) {
					int type = Integer.parseInt(uri.getQueryParameter("type"));
					
					switch(type) {
					case ZoneConstants.TYPE_MARKET1:
						title = mActivity.getString(R.string.title_market1);
						mActivity.showGridPage(2, title, ZoneConstants.TYPE_MARKET1, null);
						break;
					case ZoneConstants.TYPE_MARKET2:
						title = mActivity.getString(R.string.title_market2);
						mActivity.showGridPage(2, title, ZoneConstants.TYPE_MARKET2, null);
						break;
					case ZoneConstants.TYPE_MARKET3:
						title = mActivity.getString(R.string.title_market3);
						mActivity.showGridPage(2, title, ZoneConstants.TYPE_MARKET3, null);
						break;
					case ZoneConstants.TYPE_MARKET4:
						title = mActivity.getString(R.string.title_market4);
						mActivity.showGridPage(2, title, ZoneConstants.TYPE_MARKET4, null);
						break;
					}
					
				//출석.
				} else if(url.equals("android.zonecomms.com/attendance")) {
					title = mActivity.getString(R.string.title_attendance);
					mActivity.showGridPage(2, title, ZoneConstants.TYPE_ATTENDANCE, null);
					
				//리뷰.
				} else if(url.equals("android.zonecomms.com/review")) {
					title = mActivity.getString(R.string.title_review);
					mActivity.showGridPage(2, title, ZoneConstants.TYPE_REVIEW, null);
					
				//QnA.
				} else if(url.equals("android.zonecomms.com/qna")) {
					title = mActivity.getString(R.string.title_qna);
					mActivity.showGridPage(2, title, ZoneConstants.TYPE_QNA, null);

				//포토.
				} else if(url.equals("android.zonecomms.com/photo")) {
					title = mActivity.getString(R.string.title_photo);
					mActivity.showGridPage(2, title, ZoneConstants.TYPE_PHOTO, null);
					
				//광고배너.
				} else if(url.equals("android.zonecomms.com/banner")) {
					int type = Integer.parseInt(uri.getQueryParameter("type"));
					
					switch(type) {
					case ZoneConstants.TYPE_BANNER1:
						title = mActivity.getString(R.string.title_banner1);
						mActivity.showArticleListPage(title, 23, 0);
						break;
					case ZoneConstants.TYPE_BANNER2:
						title = mActivity.getString(R.string.title_banner2);
						mActivity.showArticleListPage(title, 23, 1);
						break;
					case ZoneConstants.TYPE_BANNER3:
						title = mActivity.getString(R.string.title_banner3);
						mActivity.showGridPage(2, title, ZoneConstants.TYPE_BANNER3, null);
						break;
					case ZoneConstants.TYPE_BANNER4:

						if(!StringUtils.isEmpty(MainPage.bannerLinkUrl)) {
							ApplicationManager.getInstance().getActivity()
								.showWebBrowser(MainPage.bannerLinkUrl, null);
						}
						break;
					}

				//투어스케쥴.
				} else if(url.equals("android.zonecomms.com/schedule")) {
					title = mActivity.getString(R.string.title_schedule);
					mActivity.showArticleListPage(title, 20);
					
				//골프장.
				} else if(url.equals("android.zonecomms.com/golfcourse")) {
					int type = Integer.parseInt(uri.getQueryParameter("type"));
					
					switch(type) {
					case ZoneConstants.TYPE_GOLFCOURSE1:
						title = mActivity.getString(R.string.title_golfcourse1);
						mActivity.showArticleListPage(title, 16);
						break;
					case ZoneConstants.TYPE_GOLFCOURSE2:
						title = mActivity.getString(R.string.title_golfcourse2);
						mActivity.showArticleListPage(title, 17);
						break;
					case ZoneConstants.TYPE_GOLFCOURSE3:
						title = mActivity.getString(R.string.title_golfcourse3);
						mActivity.showArticleListPage(title, 18);
						break;
					case ZoneConstants.TYPE_GOLFCOURSE4:
						title = mActivity.getString(R.string.title_golfcourse4);
						mActivity.showArticleListPage(title, 19);
						break;
					}
				
				//공지사항
				} else if(url.equals("android.zonecomms.com/notice")) {
					title = mActivity.getString(R.string.notice);
					mActivity.showListPage(title, ZoneConstants.TYPE_NOTICE);
					
				//멤버 보기.
				} else if(url.equals("android.zonecomms.com/member")) {
					mActivity.showGridPage(4, "MEMBER", ZoneConstants.TYPE_MEMBER, null);
					
				//환경설정.
				} else if(url.equals("android.zonecomms.com/setting")) {
					mActivity.showSettingPage();
				
				//핫세일.
				} else if(url.equals("android.zonecomms.com/hotsale")) {
					title = mActivity.getString(R.string.title_hotsale);
					mActivity.showArticleListPage(title, 15);
					
				//마니아몰.
				} else if(url.equals("android.zonecomms.com/maniamall")) {
					title = mActivity.getString(R.string.title_maniamall);
					mActivity.showArticleListPage(title, 13);
					
				//렌탈서비스.
				} else if(url.equals("android.zonecomms.com/retalservice")) {
					title = mActivity.getString(R.string.title_rentalservice);
					mActivity.showArticleListPage(title, 14);
					
				//글 상세보기.
				} else if(url.equals("android.zonecomms.com/post")) {
					title = URLDecoder.decode(uri.getQueryParameter("title"), "utf-8");
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
					
				//모임 상세 페이지.
				} else if(url.equals("android.zonecomms.com/gethering")) {
					String sb_id = URLDecoder.decode(uri.getQueryParameter("sb_id"), "utf-8");
					mActivity.showGetheringPage(sb_id);
					
				//모임 리스트.
				} else if(url.equals("android.zonecomms.com/getheringlist")) {
					title = mActivity.getString(R.string.titleText_gethering);
					mActivity.showGetheringListPage(title, 0);
					
				//모임 새글.
				} else if(url.equals("android.zonecomms.com/getheringnewpost")) {
					title = mActivity.getString(R.string.titleText_getheringNewPost);
					mActivity.showGridPage(2, title, ZoneConstants.TYPE_POST_GETHERING, null);
					
				//모임 관리.
				} else if(url.equals("android.zonecomms.com/getheringmanagement")) {
					title = mActivity.getString(R.string.titleText_gethering);
					mActivity.showGetheringListPage(title, 1);
					
				//모임 소개.
				} else if(url.equals("android.zonecomms.com/getheringintro")) {
					title = mActivity.getString(R.string.titleText_getheringIntro);
					mActivity.showGridPage(2, title, ZoneConstants.TYPE_POST_GETHERING_INTRO, null);
					
				//모임 검색.
				} else if(url.equals("android.zonecomms.com/getheringsearch")) {
					title = mActivity.getString(R.string.titleText_getheringSearch);
					mActivity.showGetheringListPage(title, -1);
					
				//기사 상세보기.
				} else if(url.equals("android.zonecomms.com/article")) {
					title = uri.getQueryParameter("title");
					int spot_nid = Integer.parseInt(uri.getQueryParameter("spot_nid"));
					mActivity.showArticlePage(title, spot_nid);
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
//		ApplicationManager.getInstance().getActivity().showMainPage();
	}
}
