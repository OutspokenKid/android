package com.byecar.byecarplusfordealer;

import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.net.Uri;
import android.os.Build;
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
import com.byecar.fragments.CertifyPhoneNumberPage;
import com.byecar.fragments.DealerPage;
import com.byecar.fragments.NotificationPage;
import com.byecar.fragments.OpenablePostListPage;
import com.byecar.fragments.SearchAreaPage;
import com.byecar.fragments.SearchCarPage;
import com.byecar.fragments.SettingPage;
import com.byecar.fragments.TermOfUsePage;
import com.byecar.fragments.TypeSearchCarPage;
import com.byecar.fragments.WebBrowserPage;
import com.byecar.fragments.dealer.CarDetailPage;
import com.byecar.fragments.dealer.CarRegistrationPage;
import com.byecar.fragments.dealer.EditDealerInfoPage;
import com.byecar.fragments.dealer.MainPage;
import com.byecar.fragments.dealer.MyCarPage;
import com.byecar.fragments.dealer.MyCompletedListPage;
import com.byecar.fragments.dealer.MyInfoReviewPage;
import com.byecar.fragments.dealer.MyPage;
import com.byecar.fragments.dealer.MyPointPage;
import com.byecar.fragments.dealer.MyTicketPage;
import com.byecar.fragments.dealer.WriteReplyPage;
import com.byecar.models.Area;
import com.byecar.models.Car;
import com.byecar.models.CompanyInfo;
import com.byecar.models.Dealer;
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
import com.outspoken_kid.views.OffsetScrollView;

public class MainActivity extends BCPFragmentActivity {

	public static MainActivity activity;
	
	public static final int POPUP_REQUEST_REGISTRATION = 0;
	public static final int POPUP_REQUEST_BID = 1;
	public static final int POPUP_NOT_ENOUGH = 2;
	public static final int POPUP_COMPLETE_SELLING = 3;

	public static User user;
	public static Dealer dealer;
	public static CompanyInfo companyInfo;
	public static Area area;

	private GestureSlidingLayout gestureSlidingLayout;
	private RelativeLayout leftView;
	private OffsetScrollView scrollView;
	private LinearLayout leftViewInner;
	private TextView tvTitle;
	
	private ImageView ivProfile;
	private ImageView ivBg;
	private TextView tvNickname;
	private TextView tvInfo;
	private View grade;
	private TextView tvGrade;
	private Button btnNotification;
	private TextView tvNotification;
	private Button btnEdit;
	private Button[] menuButtons;
	
	private View loadingView;
	
	private RelativeLayout popup;
	private View popupBg;
	private View popupImage;
	private TextView tvPopupText;
	private Button btnMore;
	private Button btnRecharge;
	private Button btnHome;
	private Button btnClose;
	private Button btnClose2;
	
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

		gestureSlidingLayout = (GestureSlidingLayout) findViewById(R.id.mainForDealerActivity_gestureSlidingLayout);
		leftView = (RelativeLayout) findViewById(R.id.mainForDealerActivity_leftView);
		
		ivProfile = (ImageView) findViewById(R.id.mainForDealerActivity_ivProfile);
		ivBg = (ImageView) findViewById(R.id.mainForDealerActivity_ivBg);
		tvNickname = (TextView) findViewById(R.id.mainForDealerActivity_tvNickname);
		tvInfo = (TextView) findViewById(R.id.mainForDealerActivity_tvInfo);
		grade = findViewById(R.id.mainForDealerActivity_grade);
		tvGrade = (TextView) findViewById(R.id.mainForDealerActivity_tvGrade);
		btnNotification = (Button) findViewById(R.id.mainForDealerActivity_btnNotification);
		tvNotification = (TextView) findViewById(R.id.mainForDealerActivity_tvNotification);
		btnEdit = (Button) findViewById(R.id.mainForDealerActivity_btnEdit);
		
		scrollView = (OffsetScrollView) findViewById(R.id.mainForDealerActivity_scrollView);
		leftViewInner = (LinearLayout) findViewById(R.id.mainForDealerActivity_leftViewInner);
		
		tvTitle = (TextView) findViewById(R.id.mainForDealerActivity_tvTitle);
		
		popup = (RelativeLayout) findViewById(R.id.mainForDealerActivity_popup);
		popupBg = findViewById(R.id.mainForDealerActivity_popupBg);
		popupImage = findViewById(R.id.mainForDealerActivity_popupImage);
		tvPopupText = (TextView) findViewById(R.id.mainForDealerActivity_tvPopupText);
		btnMore = (Button) findViewById(R.id.mainForDealerActivity_btnMore);
		btnRecharge = (Button) findViewById(R.id.mainForDealerActivity_btnRecharge);
		btnHome = (Button) findViewById(R.id.mainForDealerActivity_btnHome);
		btnClose = (Button) findViewById(R.id.mainForDealerActivity_btnClose);
		btnClose2 = (Button) findViewById(R.id.mainForDealerActivity_btnClose2);
		
		loadingView = findViewById(R.id.mainForDealerActivity_loadingView);
	}

	@Override
	public void setVariables() {

		gestureSlidingLayout.setTopView(findViewById(R.id.mainForDealerActivity_topView));
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
		
		btnNotification.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				closeMenu();
				
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {

						showPage(BCPConstants.PAGE_NOTIFICATION, null);
					}
				}, 500);
			}
		});
		
		btnMore.setOnClickListener(new OnClickListener() {

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
					}
				}, 300);
				
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {

						showPage(BCPConstants.PAGE_CAR_REGISTRATION, null);
						animating = false;
					}
				}, 600);
			}
		});
		
		btnRecharge.setOnClickListener(new OnClickListener() {

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

						ToastUtils.showToast("충전하기");
						animating = false;
					}
				}, 300);
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

		btnClose.setOnClickListener(new OnClickListener() {

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
						animating = false;
					}
				}, 300);
			}
		});
		
		btnClose2.setOnClickListener(new OnClickListener() {

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
						animating = false;
					}
				}, 300);
			}
		});
	
		loadingView.setOnClickListener(new OnClickListener() {

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

		ResizeUtils.viewResize(492, LayoutParams.MATCH_PARENT, leftView, 2, 0, null);

		RelativeLayout.LayoutParams rp = null;
		
		rp = (RelativeLayout.LayoutParams) tvTitle.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(32);
		tvTitle.setPadding(ResizeUtils.getSpecificLength(16), 0, 0, 0);
		FontUtils.setFontSize(tvTitle, 16);
		
		ResizeUtils.viewResizeForRelative(304, 314, popupImage, null, null, new int[]{0, 40, 0, 0});
		ResizeUtils.setMargin(tvPopupText, new int[]{0, 40, 0, 0});
		ResizeUtils.viewResizeForRelative(488, 82, btnMore, null, null, new int[]{0, 0, 0, 32});
		ResizeUtils.viewResizeForRelative(488, 82, btnRecharge, null, null, new int[]{0, 0, 0, 32});
		ResizeUtils.viewResizeForRelative(488, 82, btnHome, null, null, new int[]{0, 0, 0, 40});
		ResizeUtils.viewResizeForRelative(488, 82, btnClose, null, null, new int[]{0, 0, 0, 40});
		ResizeUtils.viewResizeForRelative(488, 82, btnClose2, null, null, new int[]{0, 0, 0, 32});
		
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
		return R.layout.activity_main_for_dealer;
	}

	@Override
	public int getFragmentFrameResId() {
		
		return R.id.mainForDealerActivity_fragmentFrame;
	}

	@Override
	public BCPFragment getFragmentByPageCode(int pageCode) {
		
		switch(pageCode) {
		
		case BCPConstants.PAGE_TERM_OF_USE:
			return new TermOfUsePage();
		
		case BCPConstants.PAGE_MAIN:
			return new MainPage();
		
		case BCPConstants.PAGE_CERTIFY_PHONE_NUMBER:
			return new CertifyPhoneNumberPage();

		case BCPConstants.PAGE_NOTIFICATION:
			return new NotificationPage();
			
		case BCPConstants.PAGE_EDIT_DEALER_INFO:
			return new EditDealerInfoPage();
			
		case BCPConstants.PAGE_DEALER:
			return new DealerPage();
			
		case BCPConstants.PAGE_CAR_MY_DEALER:
			return new MyCarPage();
			
		case BCPConstants.PAGE_MY:
			return new MyPage();
		
		case BCPConstants.PAGE_MY_INFO_REVIEW:
			return new MyInfoReviewPage();
			
		case BCPConstants.PAGE_MY_TICKET:
			return new MyTicketPage();
			
		case BCPConstants.PAGE_MY_COMPLETED_LIST:
			return new MyCompletedListPage();
			
		case BCPConstants.PAGE_MY_POINT:
			return new MyPointPage();
			
		case BCPConstants.PAGE_OPENABLE_POST_LIST:
			return new OpenablePostListPage();
			
		case BCPConstants.PAGE_ASK:
			return new AskPage();
			
		case BCPConstants.PAGE_SETTING:
			return new SettingPage();
		
		case BCPConstants.PAGE_CAR_DETAIL:
			return new CarDetailPage();
			
		case BCPConstants.PAGE_CAR_REGISTRATION:
			return new CarRegistrationPage();
		
		case BCPConstants.PAGE_SEARCH_CAR:
			return new SearchCarPage();
			
		case BCPConstants.PAGE_TYPE_SEARCH_CAR:
			return new TypeSearchCarPage();
			
		case BCPConstants.PAGE_SEARCH_AREA:
			return new SearchAreaPage();
			
		case BCPConstants.PAGE_WRITE_REVIEW:
			return new WriteReplyPage();
			
		case BCPConstants.PAGE_WEB_BROWSER:
			return new WebBrowserPage();
			
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
						bundle.putInt("type", Car.TYPE_BID);
						bundle.putInt("id", onsalecar_id);
						showPage(BCPConstants.PAGE_CAR_DETAIL, bundle);
					}
					
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
	
	public void setLeftView() {

		setImageView();
		setOtherViews();
		addButtons();
	}
	
	public void setImageView() {
		
		Matrix matrix = new Matrix();
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.menu_bg);
		float scale = (float)ResizeUtils.getScreenHeight() / (float) bitmap.getHeight();
		matrix.postScale(scale, scale);
		ivBg.setImageMatrix(matrix);
		ivBg.setImageBitmap(bitmap);

		float profileImageScale = (float)ResizeUtils.getScreenHeight() / 1136f;
		
		RelativeLayout.LayoutParams rp = (RelativeLayout.LayoutParams) ivProfile.getLayoutParams();
		rp.width = (int)(profileImageScale * 120f);
		rp.height = (int)(profileImageScale * 120f);
		rp.leftMargin = (int)(profileImageScale * 34f);
		rp.topMargin = (int)(profileImageScale * 111f);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressWarnings("deprecation")
	public void setOtherViews() {
	
		RelativeLayout.LayoutParams rp = null;

		rp = (RelativeLayout.LayoutParams) tvNickname.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(188);
		rp.topMargin = ResizeUtils.getSpecificLength(18);
		
		rp = (RelativeLayout.LayoutParams) tvInfo.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(188);
		rp.topMargin = ResizeUtils.getSpecificLength(8);
		
		rp = (RelativeLayout.LayoutParams) grade.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(120);
		rp.height = ResizeUtils.getSpecificLength(120);
		rp.leftMargin = ResizeUtils.getSpecificLength(45);
		
		rp = (RelativeLayout.LayoutParams) tvGrade.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(120);
		
		rp = (RelativeLayout.LayoutParams) btnNotification.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(64);
		rp.height = ResizeUtils.getSpecificLength(64);
		rp.leftMargin = ResizeUtils.getSpecificLength(50);
		rp.topMargin = ResizeUtils.getSpecificLength(29);
		
		rp = (RelativeLayout.LayoutParams) tvNotification.getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(8);
		
		int pv = ResizeUtils.getSpecificLength(2);
		int ph = ResizeUtils.getSpecificLength(6);
		tvNotification.setPadding(ph, pv, ph, pv);
		
		PaintDrawable pd = new PaintDrawable(getResources().getColor(R.color.progress_guage_red));
        pd.setCornerRadius(ResizeUtils.getSpecificLength(10));
        
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        	tvNotification.setBackground(pd);
		} else {
			tvNotification.setBackgroundDrawable(pd);
		}
		
		
		rp = (RelativeLayout.LayoutParams) btnEdit.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(211);
		rp.height = ResizeUtils.getSpecificLength(44);
		rp.topMargin = ResizeUtils.getSpecificLength(44);
		rp.rightMargin = ResizeUtils.getSpecificLength(16);

		btnEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				closeMenu();
				
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {

						showPage(BCPConstants.PAGE_EDIT_DEALER_INFO, null);
					}
				}, 500);
			}
		});
		
		rp = (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(30);
		
		FontUtils.setFontStyle(tvNickname, FontUtils.BOLD);
		FontUtils.setFontSize(tvNickname, 30);
		FontUtils.setFontSize(tvInfo, 20);
		FontUtils.setFontSize(tvGrade, 24);
		FontUtils.setFontSize(tvNotification, 20);
	}

	public void addButtons() {
		
		int[] bgResIds = new int[] {
				R.drawable.menu_2_btn,
				R.drawable.menu_3_btn,
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
			ResizeUtils.viewResize(471, 88, menuButtons[i], 1, Gravity.LEFT, 
					new int[]{0, i==0?42:10, 0, 10});
			menuButtons[i].setBackgroundResource(bgResIds[i]);
			leftViewInner.addView(menuButtons[i]);
			
			//선 추가.
			
			if(i != size -1) {
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
							
							case 0:
								showPage(BCPConstants.PAGE_MY, null);
								break;
								
							case 1:
								bundle.putInt("type", OpenablePostListPage.TYPE_NOTICE);
								showPage(BCPConstants.PAGE_OPENABLE_POST_LIST, bundle);
								break;
								
							case 2:
								bundle.putInt("type", OpenablePostListPage.TYPE_FAQ);
								showPage(BCPConstants.PAGE_OPENABLE_POST_LIST, bundle);
								break;
								
							case 3:
								showPage(BCPConstants.PAGE_ASK, null);
								break;
								
							case 4:
								showPage(BCPConstants.PAGE_SETTING, null);
								break;
								
							case 5:
								if(companyInfo != null) {
									bundle.putString("url", companyInfo.getHomepage());
								}
								
								showPage(BCPConstants.PAGE_WEB_BROWSER, bundle);
								break;
								
							case 6:
								if(companyInfo != null) {
									bundle.putString("url", companyInfo.getBlog_url());
								}
								
								showPage(BCPConstants.PAGE_WEB_BROWSER, bundle);
								break;
								
							case 7:
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
		
		case POPUP_REQUEST_REGISTRATION:
			popupImage.setBackgroundResource(R.drawable.complete_cartoon);
			tvPopupText.setText(R.string.popup_registration);
			ResizeUtils.viewResizeForRelative(564, 722, popupBg, null, null, null);
			btnMore.setVisibility(View.VISIBLE);
			btnRecharge.setVisibility(View.INVISIBLE);
			btnHome.setVisibility(View.VISIBLE);
			btnClose.setVisibility(View.INVISIBLE);
			btnClose2.setVisibility(View.INVISIBLE);
			break;
			
		case POPUP_REQUEST_BID:
			popupImage.setBackgroundResource(R.drawable.buy_cartoon);
			tvPopupText.setText(R.string.popup_bid);
			ResizeUtils.viewResizeForRelative(564, 614, popupBg, null, null, null);
			btnMore.setVisibility(View.INVISIBLE);
			btnRecharge.setVisibility(View.INVISIBLE);
			btnHome.setVisibility(View.VISIBLE);
			btnClose.setVisibility(View.INVISIBLE);
			btnClose2.setVisibility(View.INVISIBLE);
			break;
			
		case POPUP_NOT_ENOUGH:
			popupImage.setBackgroundResource(R.drawable.empty_cartoon);
			tvPopupText.setText(R.string.popup_not_enough);
			ResizeUtils.viewResizeForRelative(564, 722, popupBg, null, null, null);
			btnMore.setVisibility(View.INVISIBLE);
			btnRecharge.setVisibility(View.VISIBLE);
			btnHome.setVisibility(View.INVISIBLE);
			btnClose.setVisibility(View.VISIBLE);
			btnClose2.setVisibility(View.INVISIBLE);
			break;
			
		case POPUP_COMPLETE_SELLING:
			popupImage.setBackgroundResource(R.drawable.dealer_sel_cartoon);
			tvPopupText.setText(R.string.popup_completeSelling);
			ResizeUtils.viewResizeForRelative(564, 614, popupBg, null, null, null);
			btnMore.setVisibility(View.INVISIBLE);
			btnRecharge.setVisibility(View.INVISIBLE);
			btnHome.setVisibility(View.INVISIBLE);
			btnClose.setVisibility(View.INVISIBLE);
			btnClose2.setVisibility(View.VISIBLE);
			break;
			
			default:
				return;
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
					LogUtils.log("MainForDealerActivity.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					MainActivity.user = null;
					MainActivity.dealer = null;
					
					clearCookies();
					
					SharedPrefsUtils.clearPrefs(BCPConstants.PREFS_PUSH);
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
					LogUtils.log("MainForDealerActivity.onCompleted." + "\nurl : " + url
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
						
						downloadDealerInfo();
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
	
	public void downloadDealerInfo() {

		String url = BCPAPIs.DEALER_INFO_URL + "?dealer_id=" + user.getDealer_id();
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("MainActivity.downloadDealerInfo.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("MainActivity.downloadDealerInfo.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					dealer = new Dealer(objJSON.getJSONObject("dealer"));
					setLeftViewUserInfo();
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void setLeftViewUserInfo() {

		try {
			clearLeftViewUserInfo();
			
			if(user == null) {
				return;
			}
			
			if(!StringUtils.isEmpty(user.getProfile_img_url())) {
				
				ivProfile.setTag(user.getProfile_img_url());
				BCPDownloadUtils.downloadBitmap(user.getProfile_img_url(), new OnBitmapDownloadListener() {

					@Override
					public void onError(String url) {

						LogUtils.log("MainForDealerActivity.setLeftViewUserInfo.onError." + "\nurl : " + url);
						ivProfile.setImageDrawable(null);
					}

					@Override
					public void onCompleted(String url, Bitmap bitmap) {

						try {
							LogUtils.log("MainForDealerActivity.setLeftViewUserInfo.onCompleted." + "\nurl : " + url);
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
			}
			
			if(!StringUtils.isEmpty(user.getName())) {
				tvNickname.setText(user.getName());
			}
			
			grade.setVisibility(View.VISIBLE);
			
			//딜러 레벨
			switch(dealer.getLevel()) {

			case Dealer.LEVEL_FRESH_MAN:
				grade.setBackgroundResource(R.drawable.grade_icon4);
				tvGrade.setText(R.string.dealerLevel1);
				tvGrade.setTextColor(getResources().getColor(R.color.color_dealer_level1));
				break;

			case Dealer.LEVEL_NORAML_DEALER:
				grade.setBackgroundResource(R.drawable.grade_icon3);
				tvGrade.setText(R.string.dealerLevel2);
				tvGrade.setTextColor(getResources().getColor(R.color.color_dealer_level2));
				break;
				
			case Dealer.LEVEL_SUPERB_DEALER:
				grade.setBackgroundResource(R.drawable.grade_icon2);
				tvGrade.setText(R.string.dealerLevel3);
				tvGrade.setTextColor(getResources().getColor(R.color.color_dealer_level3));
				break;
				
			case Dealer.LEVEL_POWER_DEALER:
				grade.setBackgroundResource(R.drawable.grade_icon1);
				tvGrade.setText(R.string.dealerLevel4);
				tvGrade.setTextColor(getResources().getColor(R.color.color_dealer_level4));
				break;

				default:
					grade.setVisibility(View.INVISIBLE);
			}
			
			if(!StringUtils.isEmpty(dealer.getCompany())) {
				FontUtils.addSpan(tvInfo, dealer.getCompany(), 0, 1);
			}
			
			if(!StringUtils.isEmpty(user.getPhone_number())) {
				FontUtils.addSpan(tvInfo, "\n" + user.getPhone_number(), 0, 1);
			}

			OnClickListener ocl = new OnClickListener() {

				@Override
				public void onClick(View view) {

					showMyDealerPage();
				}
			};
			
			ivProfile.setOnClickListener(ocl);
			tvNickname.setOnClickListener(ocl);
			tvInfo.setOnClickListener(ocl);
			grade.setOnClickListener(ocl);
			tvGrade.setOnClickListener(ocl);
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void showMyDealerPage() {
		
		closeMenu();
		
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				Bundle bundle = new Bundle();
				showPage(BCPConstants.PAGE_MY_INFO_REVIEW, bundle);
			}
		}, 500);
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
			tvInfo.setText(null);
			tvGrade.setText(null);
			grade.setVisibility(View.INVISIBLE);
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
			
			if(car != null && car.getStatus() == -1) {
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

	public void setNotificationCount(int count) {

		if(count > 100) {
			tvNotification.setText("+99");
			tvNotification.setVisibility(View.VISIBLE);
		} else if(count <= 0){
			tvNotification.setVisibility(View.INVISIBLE);
		} else {
			tvNotification.setText("" + count);
			tvNotification.setVisibility(View.VISIBLE);
		}
	}
}
