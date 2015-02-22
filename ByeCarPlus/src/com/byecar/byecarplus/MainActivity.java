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
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPFragment;
import com.byecar.classes.BCPFragmentActivity;
import com.byecar.classes.SocketDataHandler;
import com.byecar.fragments.AskPage;
import com.byecar.fragments.CertifyPhoneNumberPage;
import com.byecar.fragments.DealerCertifierPage;
import com.byecar.fragments.NotificationPage;
import com.byecar.fragments.OpenablePostListPage;
import com.byecar.fragments.SearchCarPage;
import com.byecar.fragments.SearchResultPage;
import com.byecar.fragments.SettingPage;
import com.byecar.fragments.TermOfUsePage;
import com.byecar.fragments.TypeSearchCarPage;
import com.byecar.fragments.WebBrowserPage;
import com.byecar.fragments.user.CarDetailPage;
import com.byecar.fragments.user.CarListPage;
import com.byecar.fragments.user.CarRegistrationPage;
import com.byecar.fragments.user.DirectMarketPage;
import com.byecar.fragments.user.EditUserInfoPage;
import com.byecar.fragments.user.MainPage;
import com.byecar.fragments.user.MyPage;
import com.byecar.fragments.user.SelectBidPage;
import com.byecar.fragments.user.WriteReviewPage;
import com.byecar.models.Car;
import com.byecar.models.CompanyInfo;
import com.byecar.models.User;
import com.google.android.gcm.GCMRegistrar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.IntentUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.TimerUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.GestureSlidingLayout;
import com.outspoken_kid.views.GestureSlidingLayout.OnAfterOpenListener;
import com.outspoken_kid.views.OffsetScrollView;
import com.outspoken_kid.views.OffsetScrollView.OnScrollChangedListener;

public class MainActivity extends BCPFragmentActivity {

	public static final int POPUP_REQUEST_CERTIFICATION = 0;
	public static final int POPUP_REQUEST_BUY = 1;
	public static final int POPUP_REGISTRATION = 2;
	public static final int POPUP_CHOICE_DEALER = 3;

	public static User user;
	public static CompanyInfo companyInfo;

	private GestureSlidingLayout gestureSlidingLayout;
	private RelativeLayout leftView;
	private OffsetScrollView scrollView;
	private LinearLayout leftViewInner;
	private TextView tvTitle1;
	private TextView tvTitle2;
	private TextView tvTitleIn1;
	private TextView tvTitleIn2;
	
	private ImageView ivProfile;
	private ImageView ivBg;
	private TextView tvNickname;
	private TextView tvInfo;
	private Button btnEdit;
	private Button[] menuButtons;
	
	private RelativeLayout popup;
	private View popupBg;
	private View popupImage;
	private TextView tvPopupText;
	private Button btnHome;
	private Button btnCall;

	public static String dealerPhoneNumber;
	
	private boolean animating;
	private long last_connected_at;
	private SocketIO socketIO;
	private SocketDataHandler socketDataHandler;
	
	@Override
 	public void bindViews() {

		gestureSlidingLayout = (GestureSlidingLayout) findViewById(R.id.mainForUserActivity_gestureSlidingLayout);
		leftView = (RelativeLayout) findViewById(R.id.mainForUserActivity_leftView);
		
		ivProfile = (ImageView) findViewById(R.id.mainForUserActivity_ivProfile);
		ivBg = (ImageView) findViewById(R.id.mainForUserActivity_ivBg);
		tvNickname = (TextView) findViewById(R.id.mainForUserActivity_tvNickname);
		tvInfo = (TextView) findViewById(R.id.mainForUserActivity_tvInfo);
		btnEdit = (Button) findViewById(R.id.mainForUserActivity_btnEdit);
		
		scrollView = (OffsetScrollView) findViewById(R.id.mainForUserActivity_scrollView);
		leftViewInner = (LinearLayout) findViewById(R.id.mainForUserActivity_leftViewInner);
		
		tvTitle1 = (TextView) findViewById(R.id.mainForUserActivity_tvTitle1);
		tvTitle2 = (TextView) findViewById(R.id.mainForUserActivity_tvTitle2);
		
		popup = (RelativeLayout) findViewById(R.id.mainForUserActivity_popup);
		popupBg = findViewById(R.id.mainForUserActivity_popupBg);
		popupImage = findViewById(R.id.mainForUserActivity_popupImage);
		tvPopupText = (TextView) findViewById(R.id.mainForUserActivity_tvPopupText);
		btnHome = (Button) findViewById(R.id.mainForUserActivity_btnHome);
		btnCall = (Button) findViewById(R.id.mainForUserActivity_btnCall);
		
		setLoadingView(findViewById(R.id.mainForUserActivity_loadingView));
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
			setLeftView();
		} catch(Exception e) {
			LogUtils.trace(e);
			finish();
		}
	}

	@Override
	public void setListeners() {

		scrollView.setOnScrollChangedListener(new OnScrollChangedListener() {

			int firstOffset = ResizeUtils.getSpecificLength(70) * 4		//buttons.
							+ ResizeUtils.getSpecificLength(10) * 8		//margins.
							+ 3;										//lines.
			int secondOffset = firstOffset + ResizeUtils.getSpecificLength(32);	//title.
			
			@Override
			public void onScrollChanged(int offset) {

				//Show tvTitle1, tvTitleIn2 and hide others.
				if(offset <= firstOffset) {
					
					if(tvTitle1.getVisibility() != View.VISIBLE) {
						tvTitle1.setVisibility(View.VISIBLE);
					}
					
					if(tvTitle2.getVisibility() == View.VISIBLE) {
						tvTitle2.setVisibility(View.INVISIBLE);
					}
					
					if(tvTitleIn1.getVisibility() == View.VISIBLE) {
						tvTitleIn1.setVisibility(View.INVISIBLE);
					}
					
					if(tvTitleIn2.getVisibility() != View.VISIBLE) {
						tvTitleIn2.setVisibility(View.VISIBLE);
					}
					
				//Show tvTitleIn1, tvTitleIn2 and hide others.
				} else if(offset <= secondOffset) {
					
					if(tvTitle1.getVisibility() == View.VISIBLE) {
						tvTitle1.setVisibility(View.INVISIBLE);
					}
					
					if(tvTitle2.getVisibility() == View.VISIBLE) {
						tvTitle2.setVisibility(View.INVISIBLE);
					}

					if(tvTitleIn1.getVisibility() != View.VISIBLE) {
						tvTitleIn1.setVisibility(View.VISIBLE);
					}
					
					if(tvTitleIn2.getVisibility() != View.VISIBLE) {
						tvTitleIn2.setVisibility(View.VISIBLE);
					}
					
				//Show tvTitle2 and hide others.
				} else {
					
					if(tvTitle1.getVisibility() == View.VISIBLE) {
						tvTitle1.setVisibility(View.INVISIBLE);
					}
					
					if(tvTitle2.getVisibility() != View.VISIBLE) {
						tvTitle2.setVisibility(View.VISIBLE);
					}
					
					if(tvTitleIn1.getVisibility() == View.VISIBLE) {
						tvTitleIn1.setVisibility(View.INVISIBLE);
					}
					
					if(tvTitleIn2.getVisibility() == View.VISIBLE) {
						tvTitleIn2.setVisibility(View.INVISIBLE);
					}
				}
			}
		});
	
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
		
		btnCall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				showAlertDialog(R.string.call, R.string.wannaCallToDealer, 
						R.string.confirm, R.string.cancel,
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								
								if(dealerPhoneNumber != null) {
									IntentUtils.call(MainActivity.this, dealerPhoneNumber);
								}
							}
						}, null);
			}
		});
	}

	@Override
	public void setSizes() {

		ResizeUtils.viewResize(492, LayoutParams.MATCH_PARENT, leftView, 2, 0, null);

		RelativeLayout.LayoutParams rp = null;
		
		rp = (RelativeLayout.LayoutParams) tvTitle1.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(32);
		tvTitle1.setPadding(ResizeUtils.getSpecificLength(4), 0, 0, 0);
		FontUtils.setFontSize(tvTitle1, 16);
		
		rp = (RelativeLayout.LayoutParams) tvTitle2.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(32);
		tvTitle2.setPadding(ResizeUtils.getSpecificLength(4), 0, 0, 0);
		FontUtils.setFontSize(tvTitle2, 16);
		
		ResizeUtils.viewResizeForRelative(304, 314, popupImage, null, null, new int[]{0, 40, 0, 0});
		ResizeUtils.setMargin(tvPopupText, new int[]{0, 40, 0, 0});
		ResizeUtils.viewResizeForRelative(488, 82, btnHome, null, null, new int[]{0, 0, 0, 40});
		ResizeUtils.viewResizeForRelative(488, 82, btnCall, null, null, new int[]{0, 0, 0, 32});
		
		FontUtils.setFontSize(tvPopupText, 30);
		FontUtils.setFontStyle(tvPopupText, FontUtils.BOLD);
	}

	@Override
	public void downloadInfo() {

		setPage(true);
	}

	@Override
	public void setPage(boolean successDownload) {

		if(getFragmentsSize() == 0) {
			showPage(BCPConstants.PAGE_MAIN, null);
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
			
		case BCPConstants.PAGE_EDIT_USER_INFO:
			return new EditUserInfoPage();
			
		case BCPConstants.PAGE_CERTIFY_PHONE_NUMBER:
			return new CertifyPhoneNumberPage();
			
		case BCPConstants.PAGE_DEALER_CERTIFIER:
			return new DealerCertifierPage();
			
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
			
		case BCPConstants.PAGE_SELECT_BID:
			return new SelectBidPage();
			
		case BCPConstants.PAGE_DIRECT_MARKET:
			return new DirectMarketPage();
			
		case BCPConstants.PAGE_DIRECT_NORMAL_LIST:
			return new OpenablePostListPage();
		
		case BCPConstants.PAGE_SEARCH_CAR:
			return new SearchCarPage();
			
		case BCPConstants.PAGE_SEARCH_RESULT:
			return new SearchResultPage();
			
		case BCPConstants.PAGE_TYPE_SEARCH_CAR:
			return new TypeSearchCarPage();
			
		case BCPConstants.PAGE_WEB_BROWSER:
			return new WebBrowserPage();
		}
		
		return null;
	}

	@Override
	public void handleUri(Uri uri) {
		// TODO Auto-generated method stub
		
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
					if(GestureSlidingLayout.isOpenToLeft()) {
						gestureSlidingLayout.close(true, null);
					} else if(popup.getVisibility() == View.VISIBLE) {
						//Do nothing.
					} else if(getTopFragment() != null && getTopFragment().onBackPressed()) {
						//Do nothing.
					} else if(getFragmentsSize() > 1){
						closeTopPage();
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
		
		socketIO.disconnect();
		TimerUtils.cancel();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		TimerUtils.clear();
	}
	
//////////////////// Custom methods.
	
	public void launchSignActivity() {
		
		Intent intent = new Intent(this, SignActivity.class);
		startActivity(intent);
		finish();
	}
	
	public void setLeftView() {

		setImageView();
		setOtherViews();
		addButtons();
	}
	
	public void setImageView() {
		
		RelativeLayout.LayoutParams rp = (RelativeLayout.LayoutParams) ivProfile.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(190);
		rp.height = ResizeUtils.getSpecificLength(190);
		rp.topMargin = ResizeUtils.getSpecificLength(84);
		ivProfile.setScaleType(ScaleType.CENTER_CROP);

		ivBg.setScaleType(ScaleType.MATRIX);
		
		Matrix matrix = new Matrix();
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.menu_bg);
		float scale = (float)ResizeUtils.getSpecificLength(492) / (float) bitmap.getWidth();
		matrix.postScale(scale, scale);
		ivBg.setImageMatrix(matrix);
		ivBg.setImageBitmap(bitmap);
	}

	public void setOtherViews() {
	
		RelativeLayout.LayoutParams rp = null;

		rp = (RelativeLayout.LayoutParams) tvNickname.getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(18);
		
		rp = (RelativeLayout.LayoutParams) tvInfo.getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(14);
		
		rp = (RelativeLayout.LayoutParams) btnEdit.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(160);
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.topMargin = ResizeUtils.getSpecificLength(146);
		rp.rightMargin = ResizeUtils.getSpecificLength(16);

		btnEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				closeMenu();
				
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {

						Bundle bundle = new Bundle();
						bundle.putInt("type", EditUserInfoPage.TYPE_EDIT_PROFILE);
						showPage(BCPConstants.PAGE_EDIT_USER_INFO, bundle);
					}
				}, 500);
			}
		});
		
		rp = (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(20);
		
		FontUtils.setFontStyle(tvNickname, FontUtils.BOLD);
		FontUtils.setFontSize(tvNickname, 30);
		FontUtils.setFontSize(tvInfo, 20);
	}

	public void addButtons() {
		
		int[] bgResIds = new int[] {
				R.drawable.menu_auction_btn,
				R.drawable.menu_used_btn,
				R.drawable.menu_my_car_btn,
				R.drawable.menu_direct_btn,
				R.drawable.menu_mypage_btn,
				R.drawable.menu_notice_btn,
				R.drawable.menu_faq_btn,
				R.drawable.menu_service_btn,
				R.drawable.menu_setting_btn,
				R.drawable.menu_homepage_btn,
				R.drawable.menu_term_btn,
		};

		menuButtons = new Button[bgResIds.length];
		int size = bgResIds.length;
		for(int i=0; i<size; i++) {
	
			//타이틀 추가 : adadad 60%, text color 393939 
			if(i == 4) {
				tvTitleIn2 = new TextView(this);
				ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 32, tvTitleIn2, 1, 0, null);
				tvTitleIn2.setBackgroundColor(Color.argb(99, 173, 173, 173));
				tvTitleIn2.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
				tvTitleIn2.setPadding(ResizeUtils.getSpecificLength(4), 0, 0, 0);
				FontUtils.setFontSize(tvTitleIn2, 16);
				tvTitleIn2.setTextColor(Color.rgb(57, 57, 57));
				tvTitleIn2.setText(getString(R.string.sideMenuTitle2));
				leftViewInner.addView(tvTitleIn2);
			}
			
			//버튼 추가.
			menuButtons[i] = new Button(this);
			ResizeUtils.viewResize(460, 70, menuButtons[i], 1, Gravity.LEFT, 
					new int[]{0, i==0?42:10, 0, 10});
			menuButtons[i].setBackgroundResource(bgResIds[i]);
			leftViewInner.addView(menuButtons[i]);
			
			//선 추가.
			
			if(i != size -1 && i != 3) {
				View line = new View(this);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1);
				lp.leftMargin = ResizeUtils.getSpecificLength(10);
				lp.rightMargin = ResizeUtils.getSpecificLength(10);
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
							
//								R.drawable.menu_auction_btn,
							case 0:
								bundle.putInt("type", Car.TYPE_BID);
								showPage(BCPConstants.PAGE_CAR_LIST, bundle);
								break;
								
//								R.drawable.menu_used_btn,
							case 1:
								bundle.putInt("type", Car.TYPE_DEALER);
								showPage(BCPConstants.PAGE_CAR_LIST, bundle);
								break;
								
//								R.drawable.menu_my_car_btn,
							case 2:
								showPage(BCPConstants.PAGE_CAR_REGISTRATION, null);
								break;
								
//								R.drawable.menu_direct_btn,
							case 3:
								showPage(BCPConstants.PAGE_DIRECT_MARKET, null);
								break;
								
//								R.drawable.menu_mypage_btn,
							case 4:
								showPage(BCPConstants.PAGE_MY, null);
								break;
								
//								R.drawable.menu_notice_btn,
							case 5:
								bundle.putInt("type", OpenablePostListPage.TYPE_NOTICE);
								showPage(BCPConstants.PAGE_OPENABLE_POST_LIST, bundle);
								break;
								
//								R.drawable.menu_faq_btn,
							case 6:
								bundle.putInt("type", OpenablePostListPage.TYPE_FAQ);
								showPage(BCPConstants.PAGE_OPENABLE_POST_LIST, bundle);
								break;
								
//								R.drawable.menu_service_btn,
							case 7:
								showPage(BCPConstants.PAGE_ASK, null);
								break;
								
//								R.drawable.menu_setting_btn,
							case 8:
								showPage(BCPConstants.PAGE_SETTING, null);
								break;
								
//								R.drawable.menu_homepage_btn,
							case 9:
								if(companyInfo != null) {
									bundle.putString("url", companyInfo.getHomepage());
								}
								
								showPage(BCPConstants.PAGE_WEB_BROWSER, bundle);
								break;
								
//								R.drawable.menu_term_btn,
							case 10:
								showPage(BCPConstants.PAGE_TERM_OF_USE, null);
								break;
							}
						}
					}, 500);
				}
			});
		}
		
		tvTitleIn1 = new TextView(this);
		RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, ResizeUtils.getSpecificLength(32));
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		rp.topMargin = ResizeUtils.getSpecificLength(70) * 4		//buttons.
				+ ResizeUtils.getSpecificLength(10) * 8			//margins.
				+ 3;											//lines.
		tvTitleIn1.setLayoutParams(rp);
		tvTitleIn1.setBackgroundColor(Color.argb(99, 173, 173, 173));
		tvTitleIn1.setVisibility(View.INVISIBLE);
		tvTitleIn1.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
		tvTitleIn1.setPadding(ResizeUtils.getSpecificLength(4), 0, 0, 0);
		FontUtils.setFontSize(tvTitleIn1, 16);
		tvTitleIn1.setTextColor(Color.rgb(57, 57, 57));
		tvTitleIn1.setText(getString(R.string.sideMenuTitle1));
		((RelativeLayout)findViewById(R.id.mainForUserActivity_innerRelative)).addView(tvTitleIn1);
	}

	public void showPopup(int type) {

		switch(type) {
		
		case POPUP_REQUEST_CERTIFICATION:
			popupImage.setBackgroundResource(R.drawable.buy_cartoon);
			tvPopupText.setText(R.string.popup_request_certification);
			ResizeUtils.viewResizeForRelative(564, 614, popupBg, null, null, null);
			btnCall.setVisibility(View.INVISIBLE);
			break;
			
		case POPUP_REQUEST_BUY:
			popupImage.setBackgroundResource(R.drawable.buy_cartoon);
			tvPopupText.setText(R.string.popup_request_buy);
			ResizeUtils.viewResizeForRelative(564, 722, popupBg, null, null, null);
			btnCall.setVisibility(View.VISIBLE);
			break;
			
		case POPUP_REGISTRATION:
			popupImage.setBackgroundResource(R.drawable.complete_cartoon);
			tvPopupText.setText(R.string.popup_registration);
			ResizeUtils.viewResizeForRelative(564, 614, popupBg, null, null, null);
			btnCall.setVisibility(View.INVISIBLE);
			break;
			
		case POPUP_CHOICE_DEALER:
			popupImage.setBackgroundResource(R.drawable.dealer_sel_cartoon);
			tvPopupText.setText(R.string.popup_choice_dealer);
			ResizeUtils.viewResizeForRelative(564, 614, popupBg, null, null, null);
			btnCall.setVisibility(View.INVISIBLE);
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

					SharedPrefsUtils.clearCookie(getCookieName_D1());
					SharedPrefsUtils.clearCookie(getCookieName_S());
					
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

					SharedPrefsUtils.clearCookie(getCookieName_D1());
					SharedPrefsUtils.clearCookie(getCookieName_S());
					
					launchSignActivity();
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
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
			tvInfo.setText(null);
			
			if(user == null) {
				return;
			}
			
			if(!StringUtils.isEmpty(user.getProfile_img_url())) {
				
				ivProfile.setTag(user.getProfile_img_url());
				DownloadUtils.downloadBitmap(user.getProfile_img_url(), new OnBitmapDownloadListener() {

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
				});
			}
			
			if(!StringUtils.isEmpty(user.getNickname())) {
				tvNickname.setText(user.getNickname());
			}
			
			if(!StringUtils.isEmpty(user.getPhone_number())) {
				FontUtils.addSpan(tvInfo, user.getPhone_number() + "\n\n", 0, 1);
			}
			
			if(!StringUtils.isEmpty(user.getAddress())) {
				FontUtils.addSpan(tvInfo, user.getAddress(), 0, 1);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void checkGCM() {

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

					LogUtils.log("###BCPFragmentActivity.updateInfo.onCompleted.  \nresult : " + objJSON);
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
			tvInfo.setText(null);
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
}
