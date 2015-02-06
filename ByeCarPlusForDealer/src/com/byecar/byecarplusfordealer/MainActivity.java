package com.byecar.byecarplusfordealer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import com.byecar.fragments.AskPage;
import com.byecar.fragments.CarDetailPage;
import com.byecar.fragments.CarRegistrationPage;
import com.byecar.fragments.CertifyPhoneNumberPage;
import com.byecar.fragments.DealerCertifierPage;
import com.byecar.fragments.NotificationPage;
import com.byecar.fragments.OpenablePostListPage;
import com.byecar.fragments.SearchCarPage;
import com.byecar.fragments.SearchResultPage;
import com.byecar.fragments.SettingPage;
import com.byecar.fragments.TermOfUsePage;
import com.byecar.fragments.TypeSearchCarPage;
import com.byecar.fragments.dealer.EditDealerInfoPage;
import com.byecar.fragments.dealer.MainPage;
import com.byecar.fragments.dealer.MyCompletedListPage;
import com.byecar.fragments.dealer.MyGradePage;
import com.byecar.fragments.dealer.MyPage;
import com.byecar.fragments.dealer.MyReviewPage;
import com.byecar.fragments.dealer.MyTicketPage;
import com.byecar.models.Dealer;
import com.byecar.models.User;
import com.google.android.gcm.GCMRegistrar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.GestureSlidingLayout;
import com.outspoken_kid.views.GestureSlidingLayout.OnAfterOpenListener;
import com.outspoken_kid.views.OffsetScrollView;

public class MainActivity extends BCPFragmentActivity {

	public static final int POPUP_REQUEST_REGISTRATION = 0;
	public static final int POPUP_REQUEST_BID = 1;
	public static final int POPUP_NOT_ENOUGH = 2;

	public static User user;
	public static Dealer dealer;

	private GestureSlidingLayout gestureSlidingLayout;
	private RelativeLayout leftView;
	private OffsetScrollView scrollView;
	private LinearLayout leftViewInner;
	private TextView tvTitle;
	
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
	private Button btnMore;
	private Button btnRecharge;
	private Button btnHome;
	private Button btnClose;
	
	private Handler mHandler;
	private Socket socket;
    private BufferedReader networkReader;
	private Thread checkSocket;
	private Runnable showUpdate;
	private String socketString;
	
	private boolean animating;
	
	@Override
	public void bindViews() {

		gestureSlidingLayout = (GestureSlidingLayout) findViewById(R.id.mainForDealerActivity_gestureSlidingLayout);
		leftView = (RelativeLayout) findViewById(R.id.mainForDealerActivity_leftView);
		
		ivProfile = (ImageView) findViewById(R.id.mainForDealerActivity_ivProfile);
		ivBg = (ImageView) findViewById(R.id.mainForDealerActivity_ivBg);
		tvNickname = (TextView) findViewById(R.id.mainForDealerActivity_tvNickname);
		tvInfo = (TextView) findViewById(R.id.mainForDealerActivity_tvInfo);
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
	
//		setSocket(BCPConstants.SOCKET_IP, BCPConstants.SOCKET_PORT);
		
		mHandler = new Handler();
		checkSocket = new Thread() {
			 
	        public void run() {
	            try {
	                LogUtils.log("###MainForDealerActivity.run.  StartThread.");
	                
	                while (true) {
	                    LogUtils.log("###MainForDealerActivity.run.  thread is running.");
	                    socketString = networkReader.readLine();
	                    mHandler.post(showUpdate);
	                }
	            } catch (Exception e) {
	 
	            }
	        }
	    };
	    showUpdate = new Runnable() {
	    	 
	        public void run() {
	        	ToastUtils.showToast("Coming word : " + socketString);
	        }
	    };
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
	}

	@Override
	public void setSizes() {

		ResizeUtils.viewResize(492, LayoutParams.MATCH_PARENT, leftView, 2, 0, null);

		RelativeLayout.LayoutParams rp = null;
		
		rp = (RelativeLayout.LayoutParams) tvTitle.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(32);
		tvTitle.setPadding(ResizeUtils.getSpecificLength(4), 0, 0, 0);
		FontUtils.setFontSize(tvTitle, 16);
		
		ResizeUtils.viewResizeForRelative(304, 314, popupImage, null, null, new int[]{0, 40, 0, 0});
		ResizeUtils.setMargin(tvPopupText, new int[]{0, 40, 0, 0});
		ResizeUtils.viewResizeForRelative(488, 82, btnMore, null, null, new int[]{0, 0, 0, 32});
		ResizeUtils.viewResizeForRelative(488, 82, btnRecharge, null, null, new int[]{0, 0, 0, 32});
		ResizeUtils.viewResizeForRelative(488, 82, btnHome, null, null, new int[]{0, 0, 0, 40});
		ResizeUtils.viewResizeForRelative(488, 82, btnClose, null, null, new int[]{0, 0, 0, 40});
		
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
			return new DealerCertifierPage();
			
		case BCPConstants.PAGE_MY:
			return new MyPage();
			
		case BCPConstants.PAGE_MY_GRADE:
			return new MyGradePage();
			
		case BCPConstants.PAGE_MY_TICKET:
			return new MyTicketPage();
			
		case BCPConstants.PAGE_MY_COMPLETED_LIST:
			return new MyCompletedListPage();
			
		case BCPConstants.PAGE_MY_REVIEW:
			return new MyReviewPage();
			
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
			
		case BCPConstants.PAGE_SEARCH_RESULT:
			return new SearchResultPage();
			
		case BCPConstants.PAGE_TYPE_SEARCH_CAR:
			return new TypeSearchCarPage();
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
		
		checkSession();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
//		try {
//			socket.close();
//		} catch (IOException e) {
//			LogUtils.trace(e);
//		}
	}
	
//////////////////// Custom methods.

	public void setSocket(String ip, int port) {
		 
        try {
            socket = new Socket(ip, port);
            networkReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
	
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
		rp.topMargin = ResizeUtils.getSpecificLength(250);
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
	}

	public void addButtons() {
		
		int[] bgResIds = new int[] {
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
							
							case 0:
								showPage(BCPConstants.PAGE_MY, null);
								break;
								
//								R.drawable.menu_notice_btn,
							case 1:
								bundle.putInt("type", OpenablePostListPage.TYPE_NOTICE);
								showPage(BCPConstants.PAGE_OPENABLE_POST_LIST, bundle);
								break;
								
//								R.drawable.menu_faq_btn,
							case 2:
								bundle.putInt("type", OpenablePostListPage.TYPE_FAQ);
								showPage(BCPConstants.PAGE_OPENABLE_POST_LIST, bundle);
								break;
								
//								R.drawable.menu_service_btn,
							case 3:
								showPage(BCPConstants.PAGE_ASK, null);
								break;
								
//								R.drawable.menu_setting_btn,
							case 4:
								showPage(BCPConstants.PAGE_SETTING, null);
								break;
								
//								R.drawable.menu_homepage_btn,
							case 5:
//								showPage(BCPConstants.PAGE_SETTING, null);
								break;
								
//								R.drawable.menu_term_btn,
							case 6:
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
		
//		private Button btnMore;
//		private Button btnRecharge;
//		private Button btnHome;
//		private Button btnClose;
		switch(type) {
		
		case POPUP_REQUEST_REGISTRATION:
			popupImage.setBackgroundResource(R.drawable.complete_cartoon);
			tvPopupText.setText(R.string.popup_registration);
			ResizeUtils.viewResizeForRelative(564, 722, popupBg, null, null, null);
//			private Button btnMore;
//			private Button btnHome;
			btnMore.setVisibility(View.VISIBLE);
			btnRecharge.setVisibility(View.INVISIBLE);
			btnHome.setVisibility(View.VISIBLE);
			btnClose.setVisibility(View.INVISIBLE);
			
			break;
			
		case POPUP_REQUEST_BID:
			popupImage.setBackgroundResource(R.drawable.buy_cartoon);
			tvPopupText.setText(R.string.popup_bid);
			ResizeUtils.viewResizeForRelative(564, 614, popupBg, null, null, null);
//			private Button btnHome;
			btnMore.setVisibility(View.INVISIBLE);
			btnRecharge.setVisibility(View.INVISIBLE);
			btnHome.setVisibility(View.VISIBLE);
			btnClose.setVisibility(View.INVISIBLE);
			break;
			
		case POPUP_NOT_ENOUGH:
			popupImage.setBackgroundResource(R.drawable.empty_cartoon);
			tvPopupText.setText(R.string.popup_not_enough);
			ResizeUtils.viewResizeForRelative(564, 722, popupBg, null, null, null);
//			private Button btnRecharge;
//			private Button btnClose;
			btnMore.setVisibility(View.INVISIBLE);
			btnRecharge.setVisibility(View.VISIBLE);
			btnHome.setVisibility(View.INVISIBLE);
			btnClose.setVisibility(View.VISIBLE);
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

				LogUtils.log("WholesaleForSettingPage.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("MainForDealerActivity.onCompleted." + "\nurl : " + url
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

				LogUtils.log("WholesaleForSettingPage.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("MainForDealerActivity.onCompleted." + "\nurl : " + url
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
				});
			}
			
			if(!StringUtils.isEmpty(user.getNickname())) {
				tvNickname.setText(user.getNickname());
			}
			
			tvInfo.setLineSpacing(0, 0.8f);
			
			//딜러 레벨
			switch(dealer.getLevel()) {

			case Dealer.LEVEL_FRESH_MAN:
				FontUtils.addSpan(tvInfo, getString(R.string.dealerLevel1) + "\n\n", 
						getResources().getColor(R.color.color_dealer_level1), 1.3f, true);
				break;

			case Dealer.LEVEL_NORAML_DEALER:
				FontUtils.addSpan(tvInfo, getString(R.string.dealerLevel2) + "\n\n", 
						getResources().getColor(R.color.color_dealer_level2), 1.3f, true);
				break;
				
			case Dealer.LEVEL_SUPERB_DEALER:
				FontUtils.addSpan(tvInfo, getString(R.string.dealerLevel3) + "\n\n", 
						getResources().getColor(R.color.color_dealer_level3), 1.3f, true);
				break;
				
			case Dealer.LEVEL_POWER_DEALER:
				FontUtils.addSpan(tvInfo, getString(R.string.dealerLevel4) + "\n\n", 
						getResources().getColor(R.color.color_dealer_level4), 1.3f, true);
				break;
			}
			
			if(!StringUtils.isEmpty(user.getPhone_number())) {
				FontUtils.addSpan(tvInfo, user.getPhone_number() + "\n\n", 0, 1);
			}
			
			//딜러 회사
			
			if(!StringUtils.isEmpty(dealer.getCompany())) {
				FontUtils.addSpan(tvInfo, dealer.getCompany() + "\n\n", 0, 1);
			}
			
			if(!StringUtils.isEmpty(user.getAddress())) {
				FontUtils.addSpan(tvInfo, user.getAddress() + "\n", 0, 1);
			}

			ivProfile.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					showMyDealerPage();
				}
			});
			
			tvNickname.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					showMyDealerPage();
				}
			});
			
			tvInfo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					
					showMyDealerPage();
				}
			});
			
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
				bundle.putInt("dealer_id", dealer.getId());
				showPage(BCPConstants.PAGE_DEALER, bundle);
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

					LogUtils.log("###ShopActivity.updateInfo.onError.  \nurl : " + url);
				}
				
				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					LogUtils.log("###ShopActivity.updateInfo.onCompleted.  \nresult : " + objJSON);
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
