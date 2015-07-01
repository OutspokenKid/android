package com.byecar.byecarplus;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPDownloadUtils;
import com.byecar.classes.BCPFragment;
import com.byecar.classes.BCPFragmentActivity;
import com.byecar.classes.SocketDataHandler;
import com.byecar.fragments.AskPage;
import com.byecar.fragments.CarHistoryPage;
import com.byecar.fragments.CarRegistrationPage;
import com.byecar.fragments.CertifyPhoneNumberPage;
import com.byecar.fragments.DealerPage;
import com.byecar.fragments.NotificationPage;
import com.byecar.fragments.OpenablePostListPage;
import com.byecar.fragments.PhoneInfoPage;
import com.byecar.fragments.ProfilePage;
import com.byecar.fragments.SearchAreaPage;
import com.byecar.fragments.SearchCarPage;
import com.byecar.fragments.SettingPage;
import com.byecar.fragments.TermOfUsePage;
import com.byecar.fragments.TypeSearchCarPage;
import com.byecar.fragments.WebBrowserPage;
import com.byecar.fragments.user.CarDetailPage;
import com.byecar.fragments.user.CarListPage;
import com.byecar.fragments.user.ForumDetailPage;
import com.byecar.fragments.user.ForumListPage;
import com.byecar.fragments.user.MainPage;
import com.byecar.fragments.user.MyForumListPage;
import com.byecar.fragments.user.MyPage;
import com.byecar.fragments.user.ReviewListPage;
import com.byecar.fragments.user.VideoListPage;
import com.byecar.fragments.user.WriteForumPage;
import com.byecar.fragments.user.WriteReplyPage;
import com.byecar.fragments.user.WriteReviewPage;
import com.byecar.models.Area;
import com.byecar.models.Car;
import com.byecar.models.CompanyInfo;
import com.byecar.models.PushObject;
import com.byecar.models.User;
import com.google.android.gcm.GCMRegistrar;
import com.outspoken_kid.classes.RequestManager;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.TimerUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.GestureSlidingLayout;
import com.outspoken_kid.views.GestureSlidingLayout.OnAfterOpenListener;

public class MainActivity extends BCPFragmentActivity {
	
	public static MainActivity activity;

	public static final int POPUP_REGISTRATION_AUCTION = 2;
	public static final int POPUP_REGISTRATION_DEALER = 3;
	public static final int POPUP_REGISTRATION_DIRECT = 4;

	public static User user;
	public static CompanyInfo companyInfo;
	public static Area area;

	private GestureSlidingLayout gestureSlidingLayout;
	private RelativeLayout leftView;
	private LinearLayout leftViewInner;
	private TextView tvMenuTitle;
	
	private ImageView ivProfile;
	private ImageView ivBg;
	private TextView tvNickname;
	private Button[] menuButtons;
	
	private RelativeLayout popup;
	private View popupBg;
	private View popupImage;
	private Button btnHome;

	private View loadingView;
	
	private View cover;
	
	private RelativeLayout noticePopup;
	private ImageView ivNotice;
	private View buttonBg;
	private Button btnDoNotSeeAgain;
	private Button btnClose;

	public static String dealerPhoneNumber;
	
	private boolean doNotSeeAgain;
	private boolean animating;
	private long last_connected_at;
	private SocketIO socketIO;
	private SocketDataHandler socketDataHandler;
	
	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		activity = this;
	}
	
	@Override
 	public void bindViews() {

		gestureSlidingLayout = (GestureSlidingLayout) findViewById(R.id.mainForUserActivity_gestureSlidingLayout);
		leftView = (RelativeLayout) findViewById(R.id.mainForUserActivity_leftView);
		
		ivProfile = (ImageView) findViewById(R.id.mainForUserActivity_ivProfile);
		ivBg = (ImageView) findViewById(R.id.mainForUserActivity_ivBg);
		tvNickname = (TextView) findViewById(R.id.mainForUserActivity_tvNickname);
		
		leftViewInner = (LinearLayout) findViewById(R.id.mainForUserActivity_leftViewInner);
		
		tvMenuTitle = (TextView) findViewById(R.id.mainForUserActivity_tvMenuTitle);
		
		popup = (RelativeLayout) findViewById(R.id.mainForUserActivity_popup);
		popupBg = findViewById(R.id.mainForUserActivity_popupBg);
		popupImage = findViewById(R.id.mainForUserActivity_popupImage);
		btnHome = (Button) findViewById(R.id.mainForUserActivity_btnHome);
		
		loadingView = findViewById(R.id.mainForUserActivity_loadingView);
		
		cover = findViewById(R.id.mainForUserActivity_cover);
		
		noticePopup = (RelativeLayout) findViewById(R.id.mainForUserActivity_noticePopup);
		ivNotice = (ImageView) findViewById(R.id.mainForUserActivity_ivNotice);
		buttonBg = findViewById(R.id.mainForUserActivity_buttonBg);
		btnDoNotSeeAgain = (Button) findViewById(R.id.mainForUserActivity_btnDoNotSeeAgain);
		btnClose = (Button) findViewById(R.id.mainForUserActivity_btnClose);
	}

	@Override
	public void setVariables() {

		gestureSlidingLayout.setTopView(findViewById(R.id.mainForUserActivity_topView));
		gestureSlidingLayout.setLeftView(leftView);
		gestureSlidingLayout.setOnAfterOpenToLeftListener(new OnAfterOpenListener() {
			
			@Override
			public void onAfterOpen() {
				SoftKeyboardUtils.hideKeyboard(context, gestureSlidingLayout);
			}
		});
		
		GestureSlidingLayout.setScrollLock(true);
		
		socketDataHandler = new SocketDataHandler(this);
	}

	@Override
	public void createPage() {
		
		try {
			setLoadingView(loadingView);
			setLeftView();
		} catch(Exception e) {
			LogUtils.trace(e);
			finish();
		}
	}

	@Override
	public void setListeners() {

		btnHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(animating) {
					return;
				}
				
				animating = true;
				
				hidePopup();
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {

						clearFragments(true);
						animating = false;
					}
				}, 300);
			}
		});
		
		ivNotice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				hideNoticePopup();
				Bundle bundle = new Bundle();
				bundle.putInt("type", OpenablePostListPage.TYPE_NOTICE);
				showPage(BCPConstants.PAGE_OPENABLE_POST_LIST, bundle);
			}
		});
		
		btnDoNotSeeAgain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				doNotSeeAgain = !doNotSeeAgain;
				
				if(doNotSeeAgain) {
					btnDoNotSeeAgain.setBackgroundResource(R.drawable.notice_deny_b);
				} else {
					btnDoNotSeeAgain.setBackgroundResource(R.drawable.notice_deny_a);
				}
			}
		});
		
		btnClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				hideNoticePopup();
			}
		});

		loadingView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				//Do nothing.
			}
		});
		
		cover.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				//Do nothing.
			}
		});
		
		popup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				//Do nothing.
			}
		});
	}

	@Override
	public void setSizes() {

		ResizeUtils.viewResize(476, LayoutParams.MATCH_PARENT, leftView, 2, 0, null);

		RelativeLayout.LayoutParams rp = null;
		
		float profileImageScale = (float)ResizeUtils.getScreenHeight() / 1136f;
		int profileHeight = (int)(profileImageScale * 137f);
		
		rp = (RelativeLayout.LayoutParams) tvNickname.getLayoutParams();
		rp.height = profileHeight;
		rp.leftMargin = (int)(profileImageScale * 20f);
		rp.rightMargin = (int)(profileImageScale * 20f);
		FontUtils.setFontSize(tvNickname, 28);
		FontUtils.setFontStyle(tvNickname, FontUtils.BOLD);
		
		rp = (RelativeLayout.LayoutParams) tvMenuTitle.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(32);
		rp.topMargin = ResizeUtils.getSpecificLength(96);
		tvMenuTitle.setPadding(ResizeUtils.getSpecificLength(17), 0, 0, 0);
		FontUtils.setFontSize(tvMenuTitle, 18);
		FontUtils.setFontStyle(tvMenuTitle, FontUtils.BOLD);
		
		ResizeUtils.viewResizeForRelative(304, 314, popupImage, null, null, new int[]{0, 110, 0, 0});
		ResizeUtils.viewResizeForRelative(488, 82, btnHome, null, null, new int[]{0, 0, 0, 40});

		ResizeUtils.viewResize(587, 787, noticePopup, 2, Gravity.CENTER, null);
		ResizeUtils.viewResizeForRelative(578, 778, ivNotice, null, null, new int[]{0, 0, 0, 0});
		ResizeUtils.viewResizeForRelative(558, 73, buttonBg, null, null, new int[]{0, 0, 0, 14});
		ResizeUtils.viewResizeForRelative(210, 73, btnDoNotSeeAgain, null, null, new int[]{21, 0, 0, 0});
		ResizeUtils.viewResizeForRelative(131, 52, btnClose, null, null, new int[]{0, 11, 11, 0});
	}

	@Override
	public void downloadInfo() {

		setPage(true);
	}

	@Override
	public void setPage(boolean successDownload) {

		if(getFragmentsSize() == 0) {
			showPage(BCPConstants.PAGE_MAIN, null);
			
			if(getIntent() != null && getIntent().hasExtra("pushObject")) {
				
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {

						PushObject po = (PushObject) getIntent().getSerializableExtra("pushObject");
						handleUri(Uri.parse(po.uri.toString()));
					}
				}, 500);
			}
		}
	}

	@Override
	public int getContentViewId() {
		return R.layout.activity_main_for_user;
	}

	@Override
	public int getFragmentFrameResId() {
		
		return R.id.mainForUserActivity_fragmentFrame;
	}

	@Override
	public BCPFragment getFragmentByPageCode(int pageCode) {
		
		switch(pageCode) {
		
		case BCPConstants.PAGE_TERM_OF_USE:
			return new TermOfUsePage();
		
		case BCPConstants.PAGE_MAIN:
			return new MainPage();
		
		case BCPConstants.PAGE_PHONE_INFO:
			return new PhoneInfoPage();
			
		case BCPConstants.PAGE_CERTIFY_PHONE_NUMBER:
			return new CertifyPhoneNumberPage();
			
		case BCPConstants.PAGE_DEALER:
			return new DealerPage();
			
		case BCPConstants.PAGE_MY:
			return new MyPage();
			
		case BCPConstants.PAGE_WRITE_REVIEW:
			return new WriteReviewPage();
			
		case BCPConstants.PAGE_OPENABLE_POST_LIST:
			return new OpenablePostListPage();
			
		case BCPConstants.PAGE_ASK:
			return new AskPage();
			
		case BCPConstants.PAGE_NOTIFICATION:
			return new NotificationPage();
			
		case BCPConstants.PAGE_SETTING:
			return new SettingPage();
		
		case BCPConstants.PAGE_CAR_DETAIL:
			return new CarDetailPage();
			
		case BCPConstants.PAGE_CAR_LIST:
			return new CarListPage();
			
		case BCPConstants.PAGE_CAR_REGISTRATION:
			return new CarRegistrationPage();
		
		case BCPConstants.PAGE_SEARCH_CAR:
			return new SearchCarPage();
			
		case BCPConstants.PAGE_TYPE_SEARCH_CAR:
			return new TypeSearchCarPage();
		
		case BCPConstants.PAGE_FORUM_LIST:
			return new ForumListPage();
			
		case BCPConstants.PAGE_MY_FORUM_DETAIL:
			return new MyForumListPage();
			
		case BCPConstants.PAGE_WRITE_FORUM:
			return new WriteForumPage();
			
		case BCPConstants.PAGE_FORUM_DETAIL:
			return new ForumDetailPage();
			
		case BCPConstants.PAGE_FORUM_WRITE_REPLY:
			return new WriteReplyPage();
			
		case BCPConstants.PAGE_VIDEO_LIST:
			return new VideoListPage();
			
		case BCPConstants.PAGE_WEB_BROWSER:
			return new WebBrowserPage();
			
		case BCPConstants.PAGE_SEARCH_AREA:
			return new SearchAreaPage();
			
		case BCPConstants.PAGE_BID_REVIEW_LIST:
			return new ReviewListPage();
		
		case BCPConstants.PAGE_PROFILE:
			return new ProfilePage();
		
		case BCPConstants.PAGE_CAR_HISTORY:
			return new CarHistoryPage();
		}
		
		return null;
	}

	@Override
	public void handleUri(Uri uri) {

		try {
			//byecar://notices
			//byecar://notices?post_id=41
			
			//byecar://posts/forum?post_id=73&reply_id=29


			if(uri == null) {
				return;
			} else if(uri.getScheme().toString().equals("byecar")){

				String host = uri.getHost().toString();
				String path = uri.getPath().toString();
				Bundle bundle = new Bundle();
				
				LogUtils.log("MainActivity.actionByUri. ========" +
						"\n uri : " + uri 
						+ "\n scheme : "+ uri.getScheme()
						+ "\n host : " + host 
						+ "\n path : " + path 
						+ "\n=========");
				
				//공지.
				if(host.equals("notices")) {
					bundle.putInt("type", OpenablePostListPage.TYPE_NOTICE);
					
					try {
						int post_id = Integer.parseInt(uri.getQueryParameter("post_id"));
						bundle.putInt("id", post_id);
					} catch (Exception e) {
						LogUtils.trace(e);
					}
					
					showPage(BCPConstants.PAGE_OPENABLE_POST_LIST, bundle);
				
				//차량 관련.
				} else if(host.equals("onsalecars")) {

					int onsalecar_id = Integer.parseInt(uri.getQueryParameter("onsalecar_id"));
					
					if(path.equals("/bids/show")) {
						showCarDetailPage(onsalecar_id, null, Car.TYPE_BID);
					}
					
					//byecar://users/disable

				//블럭.
				} else if(host.equals("users") && path.equals("/disable")) {

					checkSession(new OnAfterCheckSessionListener() {
						
						@Override
						public void onAfterCheckSession(boolean isSuccess, JSONObject objJSON) {

							try {
								if(!isSuccess) {
									ToastUtils.showToast(objJSON.getString("message"));
									launchSignActivity();
								}
							} catch (Exception e) {
								LogUtils.trace(e);
							} catch (Error e) {
								LogUtils.trace(e);
							}
						}
					});
				
					//byecar://posts/forum?post_id=73&reply_id=29
					
				//자유게시판.
				} else if(host.equals("posts") && path.equals("/forum")) {
					
					int post_id = Integer.parseInt(uri.getQueryParameter("post_id"));

					bundle = new Bundle();
					bundle.putInt("post_id", post_id);
					showPage(BCPConstants.PAGE_FORUM_DETAIL, bundle);
				}
				
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if(event.getAction() == KeyEvent.ACTION_DOWN) {
			
			switch(keyCode) {
			
			case KeyEvent.KEYCODE_MENU :
				
				try {
					if(getTopFragment().onMenuPressed()) {
						//Do nothing.
					} else if(GestureSlidingLayout.isOpenToLeft()) {
						closeMenu();
					} else {
						openMenu();
					}
				} catch(Exception e) {
					LogUtils.trace(e);
				}
				break;
			
			case KeyEvent.KEYCODE_BACK :
				
				try {
					
					if(noticePopup.getVisibility() == View.VISIBLE) {
						hideNoticePopup();
					} else if(GestureSlidingLayout.isOpenToLeft()) {
						gestureSlidingLayout.close(true, null);
					} else if(popup.getVisibility() == View.VISIBLE) {
						//Do nothing.
					} else if(getTopFragment() != null && getTopFragment().onBackPressed()) {
						//Do nothing.
					} else if(getFragmentsSize() > 1){
						closeTopPage();
						hideLoadingView();
					} else {
						finish();
					}
				} catch(Exception e) {
					LogUtils.trace(e);
				}
				break;
				
				default:
					return super.onKeyDown(keyCode, event);
			}
		}
		
		return true;
	}

	@Override
	public void finish() {

		DownloadUtils.downloadJSONString(BCPAPIs.BASE_API_URL + "/users/logout.json", null);
		activity = null;
		TimerUtils.clear();
		
		super.finish();
	}

	public boolean isOpen() {

		return GestureSlidingLayout.isOpenToLeft();
	}
	
	public void openMenu() {
	
		if(gestureSlidingLayout != null) {
			gestureSlidingLayout.open(true, null);
		}
	}
	
	public void closeMenu() {
		
		if(gestureSlidingLayout != null) {
			gestureSlidingLayout.close(true, null);
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
//        checkSocket.start();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	
		TimerUtils.startTimer(100, 100);
		checkSession();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		try {
			socketIO.disconnect();
			TimerUtils.cancel();
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		clearLeftViewUserInfo();
	}
	
//////////////////// Custom methods.
	
	public void launchSignActivity() {
		
		Intent intent = new Intent(this, SignActivity.class);
		startActivity(intent);
		finish();
	}
	
	public void launchTutorialActivity() {
		
		Intent intent = new Intent(this, TutorialActivity.class);
		startActivity(intent);
	}
	
	public void setLeftView() {

		setImageViews();
		addButtons();
	}
	
	public void setImageViews() {
		
		Matrix matrix = new Matrix();
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.menu_bg);
		float scale = (float)ResizeUtils.getScreenHeight() / (float) bitmap.getHeight();
		matrix.postScale(scale, scale);
		ivBg.setImageMatrix(matrix);
		ivBg.setImageBitmap(bitmap);

		float profileImageScale = (float)ResizeUtils.getScreenHeight() / 1136f;
		
		RelativeLayout.LayoutParams rp = (RelativeLayout.LayoutParams) ivProfile.getLayoutParams();
		rp.width = (int)(profileImageScale * 137f);
		rp.height = (int)(profileImageScale * 137f);
		rp.leftMargin = (int)(profileImageScale * 113f);
		rp.topMargin = (int)(profileImageScale * 125f);
		
//		04-03 16:17:12.884: I/notice(7821):  width : 115
//		04-03 16:17:12.884: I/notice(7821):  height : 115
//		04-03 16:17:12.884: I/notice(7821):  leftMargin : 95
//		04-03 16:17:12.884: I/notice(7821):  topMargin : 105
	}

	public void addButtons() {
		
		int[] bgResIds = new int[] {
				R.drawable.menu_2_btn,
				R.drawable.menu_3_btn,
				R.drawable.menu_10_btn,
				R.drawable.menu_11_btn,
				R.drawable.menu_4_btn,
				R.drawable.menu_5_btn,
				R.drawable.menu_6_btn,
				R.drawable.menu_7_btn,
				R.drawable.menu_8_btn,
				R.drawable.menu_9_btn,
		};

		menuButtons = new Button[bgResIds.length];
		int size = bgResIds.length;
		for(int i=0; i<size; i++) {
			
			//버튼 추가.
			menuButtons[i] = new Button(this);
			ResizeUtils.viewResize(471, 88, menuButtons[i], 1, Gravity.LEFT, new int[]{0, 10, 0, 10});
			menuButtons[i].setBackgroundResource(bgResIds[i]);
			leftViewInner.addView(menuButtons[i]);
			
			//선 추가.
			if(i != size -1) {
				View line = new View(this);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ResizeUtils.getSpecificLength(430), 1);
				lp.gravity = Gravity.LEFT;
				line.setLayoutParams(lp);
				line.setBackgroundColor(Color.argb(99, 173, 173, 173));
				leftViewInner.addView(line);
			}
			
			final int I = i;
			menuButtons[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					closeMenu();
					
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {

							Bundle bundle = new Bundle();
							
							switch(I) {
							
//								마이페이지
							case 0:
								showPage(BCPConstants.PAGE_MY, null);
								break;
								
//								공지사항
							case 1:
								bundle.putInt("type", OpenablePostListPage.TYPE_NOTICE);
								showPage(BCPConstants.PAGE_OPENABLE_POST_LIST, bundle);
								break;
								
//								동영상게시판
							case 2:
								showPage(BCPConstants.PAGE_VIDEO_LIST, null);
								break;
								
//								자유게시판
							case 3:
								showPage(BCPConstants.PAGE_FORUM_LIST, null);
								break;
								
//								자주 묻는 질문
							case 4:
								bundle.putInt("type", OpenablePostListPage.TYPE_FAQ);
								showPage(BCPConstants.PAGE_OPENABLE_POST_LIST, bundle);
								break;
								
//								Q&A
							case 5:
								showPage(BCPConstants.PAGE_ASK, null);
								break;
								
//								설정
							case 6:
								showPage(BCPConstants.PAGE_SETTING, null);
								break;
								
//								바이카 홈페이지
							case 7:
								if(companyInfo != null) {
									bundle.putString("url", companyInfo.getHomepage());
								}
								
								showPage(BCPConstants.PAGE_WEB_BROWSER, bundle);
								break;
								
//								블로그
							case 8:
								if(companyInfo != null) {
									bundle.putString("url", companyInfo.getBlog_url());
								}
								
								showPage(BCPConstants.PAGE_WEB_BROWSER, bundle);
								break;
								
//								이용약관
							case 9:
								showPage(BCPConstants.PAGE_TERM_OF_USE, null);
								break;
							}
						}
					}, 500);
				}
			});
		}
	}

	public void showPopup(int type) {

		switch(type) {
			
		case POPUP_REGISTRATION_AUCTION:
			popupImage.setBackgroundResource(R.drawable.complete_cartoon);
			ResizeUtils.viewResizeForRelative(564, 614, popupBg, null, null, null);
			break;
			
		case POPUP_REGISTRATION_DIRECT:
			popupImage.setBackgroundResource(R.drawable.complete_direct2);
			ResizeUtils.viewResizeForRelative(564, 614, popupBg, null, null, null);
			break;
		}
		
		AlphaAnimation aaIn = new AlphaAnimation(0, 1);
		aaIn.setDuration(300);
		popup.setVisibility(View.VISIBLE);
		popup.startAnimation(aaIn);
	}
	
	public void hidePopup() {
		
		AlphaAnimation aaOut = new AlphaAnimation(1, 0);
		aaOut.setDuration(300);
		popup.setVisibility(View.INVISIBLE);
		popup.startAnimation(aaOut);
	}
	
	public void signOut() {
		
		String url = BCPAPIs.SIGN_OUT_URL;
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("MainActivity.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("MainForUserActivity.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					MainActivity.user = null;
					
					clearCookies();
					
					SharedPrefsUtils.clearPrefs(BCPConstants.PREFS_PUSH);
					SharedPrefsUtils.clearPrefs(BCPConstants.PREFS_NOTICE);
					SharedPrefsUtils.clearPrefs(BCPConstants.PREFS_REG);
					SharedPrefsUtils.clearPrefs(BCPConstants.PREFS_CERTIFY);
					
					launchSignActivity();
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void withdraw() {
		
		String url = BCPAPIs.WITHDRAW_URL;
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("MainActivity.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("MainForUserActivity.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);
					
					clearCookies();
					launchSignActivity();
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void clearCookies() {
		
		SharedPrefsUtils.clearPrefs(getCookieName_D1());
		SharedPrefsUtils.clearPrefs(getCookieName_S());
		RequestManager.getCookieStore().clear();
	}
	
	public void checkSession() {
		
		checkSession(new OnAfterCheckSessionListener() {
			
			@Override
			public void onAfterCheckSession(boolean isSuccess, JSONObject objJSON) {

				try {
					if(isSuccess) {
						saveCookies();
						
						LogUtils.log("###MainActivity.onAfterCheckSession.  "
								+ "\nresult : " + objJSON);
						
						user = new User(objJSON.getJSONObject("user"));
						
						if(socketIO == null || !socketIO.isConnected()) {
							setSocket();
						}
						
						setLeftViewUserInfo();
						checkGCM();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
						launchSignActivity();
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}
			}
		});
	}
	
	public void setSocket() {
		
		try {
			socketIO = new SocketIO(BCPAPIs.BASE_API_URL + ":" + BCPConstants.SOCKET_PORT);
			socketIO.connect(new IOCallback() {
				
				@Override
				public void onMessage(JSONObject json, IOAcknowledge ack) {
				}
				
				@Override
				public void onMessage(String data, IOAcknowledge ack) {
				}
				
				@Override
				public void onError(SocketIOException socketIOException) {

					LogUtils.trace(socketIOException);
				}
				
				@Override
				public void onDisconnect() {
				}
				
				@Override
				public void onConnect() {

					try {
						JSONObject objMessage = new JSONObject();
						objMessage.put("user_id", user.getId());
						objMessage.put("last_connected_at", last_connected_at);
						
						socketIO.emit("join_as_user", null, objMessage);
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (Error e) {
						LogUtils.trace(e);
					}
				}
				
				@Override
				public void on(String event, IOAcknowledge ack, Object... args) {

					if(args != null 
							&& args.length > 0) {
						/*
						처음 접속, 데이터 없음.
						{"name":"joined_as_user","args":[{"message_at":1424554954}]}
						
						재접속, 데이터 없음.
						{"name":"join_as_user","args":[{"last_connected_at":1424554954,"user_id":5}]}

						재접속, 데이터 있음.
						{
							"name":"last_messages",
							"args":
							[
								arg[0] : 밀린 정보로 이루어진 배열.
								[
									arg[0][0] : 첫번째 아이템.
									{
										...
									}
								]
							]
						}
						
						접속중, 데이터 있음.
						{
							"name":"bid_price_updated",
							"args":
							[
								args[0] : 추가된 정보.
								{
									...
									"message_at":1424555885
								}
							]
						}
						*/
						
						try {
							//last_messages인 경우 arg[0]은 JSONArray.
							if("last_messages".equals(event)) {
								socketDataHandler.onLastData(event, new JSONArray(args[0].toString()));
								
							//그 외의 경우 args[0]은 JSONObject.
							} else {
								JSONObject objJSON = new JSONObject(args[0].toString());
								last_connected_at = objJSON.getLong("message_at");
								socketDataHandler.onData(event, objJSON);
							}
						} catch (Exception e) {
							Log.w("socket", "###MainActivity.socketIO.on.  parseError"
									+ "\n event : " + event);
						}
					}
				}
			});
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
    }
	
	public void setLeftViewUserInfo() {

		try {
			ivProfile.setImageDrawable(null);
			tvNickname.setText(null);
			
			if(user == null) {
				return;
			}
			
			if(!StringUtils.isEmpty(user.getProfile_img_url())) {
				
				ivProfile.setTag(user.getProfile_img_url());
				BCPDownloadUtils.downloadBitmap(user.getProfile_img_url(), new OnBitmapDownloadListener() {

					@Override
					public void onError(String url) {

						LogUtils.log("MainForUserActivity.setLeftViewUserInfo.onError." + "\nurl : " + url);
						ivProfile.setImageDrawable(null);
					}

					@Override
					public void onCompleted(String url, Bitmap bitmap) {

						try {
							LogUtils.log("MainForUserActivity.setLeftViewUserInfo.onCompleted." + "\nurl : " + url);
							ivProfile.setImageBitmap(bitmap);;
						} catch (Exception e) {
							LogUtils.trace(e);
							ivProfile.setImageDrawable(null);
						} catch (OutOfMemoryError oom) {
							LogUtils.trace(oom);
							ivProfile.setImageDrawable(null);
						}
					}
				}, 190);
			
				ivProfile.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {

						showSelectDialog(getString(R.string.profileImage), 
								new String[]{
									getString(R.string.editProfile),
									getString(R.string.showProfile)
								}, 
								new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {

								if(which == 0) {
									closeMenu();
									
									new Handler().postDelayed(new Runnable() {

										@Override
										public void run() {

											showPage(BCPConstants.PAGE_PROFILE, null);
										}
									}, 500);
								} else {
									showImageViewer(0, getString(R.string.profileImage), new String[]{user.getProfile_img_url()}, null);
								}
							}
						});
					}
				});
			}
			
			if(!StringUtils.isEmpty(user.getNickname())) {
				tvNickname.setText(user.getNickname());
				
				tvNickname.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {

						closeMenu();
						
						new Handler().postDelayed(new Runnable() {

							@Override
							public void run() {

								showPage(BCPConstants.PAGE_PROFILE, null);
							}
						}, 500);
					}
				});
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void checkGCM() {

		Log.i("gcm", "checkInfo");
		
		try {
			GCMRegistrar.checkDevice(this);
			GCMRegistrar.checkManifest(this);
			
			final String regId = GCMRegistrar.getRegistrationId(this);
			
			if(regId == null || regId.equals("")) {
				GCMRegistrar.register(this, BCPConstants.GCM_SENDER_ID);
			} else {
				updateInfo(regId);
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void updateInfo(String regId) {
		
		if(user == null || regId == null) {
			return;
		}
		
		try {
			String url = BCPAPIs.REGISTER_URL
					+ "?user_id=" + user.getId()
					+ "&device_token=" + regId;
			
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {
				
				@Override
				public void onError(String url) {

					LogUtils.log("###BCPFragmentActivity.updateInfo.onError.  \nurl : " + url);
				}
				
				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					LogUtils.log("###BCPFragmentActivity.updateInfo.onCompleted.  "
							+ "\n url : " + url
							+ "\n result : " + objJSON);
				}
			});
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	public void clearLeftViewUserInfo() {

		try {
			Drawable d = ivProfile.getDrawable();
	        ivProfile.setImageDrawable(null);
	        
	        if (d != null) {
	            d.setCallback(null);
	        }
			
			tvNickname.setText(null);
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	public void showCarDetailPage(int id, Car car, int type) {
		
		try {
			if(id == 0 && car == null) {
				return;
			}
			
			if(car != null
					&& car.getStatus() == -1) {
				ToastUtils.showToast(R.string.holdOffByAdmin);
				return;
			}
			
			Bundle bundle = new Bundle();
			
			if(car != null) {
				bundle.putSerializable("car", car);
			}
			
			if(id != 0) {
				bundle.putInt("id", id);
			}
			
			bundle.putInt("type", type);
			showPage(BCPConstants.PAGE_CAR_DETAIL, bundle);
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	public void showNoticePopup(final String imageUrl) {
		
		AlphaAnimation aaIn = new AlphaAnimation(0, 1);
		aaIn.setDuration(300);
		noticePopup.setVisibility(View.VISIBLE);
		noticePopup.startAnimation(aaIn);
		cover.setVisibility(View.VISIBLE);
		cover.startAnimation(aaIn);
		
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				BCPDownloadUtils.downloadBitmap(imageUrl, new OnBitmapDownloadListener() {
					
					@Override
					public void onError(String url) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onCompleted(String url, Bitmap bitmap) {

						if(ivNotice != null && !bitmap.isRecycled()) {
							ivNotice.setImageBitmap(bitmap);
						}
					}
				}, 578);
			}
		}, 500);
	}
	
	public void hideNoticePopup() {
		
		AlphaAnimation aaOut = new AlphaAnimation(1, 0);
		aaOut.setDuration(300);
		noticePopup.setVisibility(View.INVISIBLE);
		noticePopup.startAnimation(aaOut);
		cover.setVisibility(View.INVISIBLE);
		cover.startAnimation(aaOut);
		
		if(doNotSeeAgain) {
			SharedPrefsUtils.addDataToPrefs(BCPConstants.PREFS_NOTICE, "time", System.currentTimeMillis());
		}
	}
}
