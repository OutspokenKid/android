package com.byecar.byecarplus;

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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.classes.BCPAPIs;
import com.byecar.byecarplus.classes.BCPConstants;
import com.byecar.byecarplus.classes.BCPFragment;
import com.byecar.byecarplus.classes.BCPFragmentActivity;
import com.byecar.byecarplus.fragments.main_for_user.CarDetailPage;
import com.byecar.byecarplus.fragments.main_for_user.AuctionListPage;
import com.byecar.byecarplus.fragments.main_for_user.AuctionRegistrationPage;
import com.byecar.byecarplus.fragments.main_for_user.CertifyPhoneNumberPage;
import com.byecar.byecarplus.fragments.main_for_user.DealerCarListPage;
import com.byecar.byecarplus.fragments.main_for_user.EditUserInfoPage;
import com.byecar.byecarplus.fragments.main_for_user.MainForUserPage;
import com.byecar.byecarplus.models.User;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.GestureSlidingLayout;
import com.outspoken_kid.views.GestureSlidingLayout.OnAfterOpenListener;
import com.outspoken_kid.views.OffsetScrollView;
import com.outspoken_kid.views.OffsetScrollView.OnScrollChangedListener;

public class MainForUserActivity extends BCPFragmentActivity {

	private User user;
	
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

	private Handler mHandler;
	private Socket socket;
    private BufferedReader networkReader;
	private Thread checkSocket;
	private Runnable showUpdate;
	private String socketString;
	
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
	
//		setSocket(BCPConstants.SOCKET_IP, BCPConstants.SOCKET_PORT);
		
		mHandler = new Handler();
		checkSocket = new Thread() {
			 
	        public void run() {
	            try {
	                LogUtils.log("###MainForUserActivity.run.  StartThread.");
	                
	                while (true) {
	                    LogUtils.log("###MainForUserActivity.run.  thread is running.");
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
	}

	@Override
	public void downloadInfo() {

		setPage(true);
	}

	@Override
	public void setPage(boolean successDownload) {

		if(getFragmentsSize() == 0) {
			showPage(BCPConstants.PAGE_MAIN_FOR_USER, null);
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
		
		case BCPConstants.PAGE_MAIN_FOR_USER:
			return new MainForUserPage();
			
		case BCPConstants.PAGE_AUCTION_LIST:
			return new AuctionListPage();
			
		case BCPConstants.PAGE_CAR_DETAIL:
			return new CarDetailPage();
			
		case BCPConstants.PAGE_AUCTION_REGISTRATION:
			return new AuctionRegistrationPage();
		
		case BCPConstants.PAGE_EDIT_USER_INFO:
			return new EditUserInfoPage();
			
		case BCPConstants.PAGE_CERTIFY_PHONE_NUMBER:
			return new CertifyPhoneNumberPage();
			
		case BCPConstants.PAGE_DEALER_LIST:
			return new DealerCarListPage();
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
	
	public void checkSession() {
		
		checkSession(new OnAfterCheckSessionListener() {
			
			@Override
			public void onAfterCheckSession(boolean isSuccess, JSONObject objJSON) {

				if(isSuccess) {
					saveCookies();
					
					try {
						user = new User(objJSON.getJSONObject("user"));
						setLeftViewUserInfo();
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (Error e) {
						LogUtils.trace(e);
					}
				} else {
					
					try {
						ToastUtils.showToast(objJSON.getString("message"));
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (Error e) {
						LogUtils.trace(e);
					}
		
					launchSignActivity();
				}
			}
		});
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
		rp.topMargin = ResizeUtils.getSpecificLength(146);
		rp.rightMargin = ResizeUtils.getSpecificLength(16);

		btnEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putInt("from", EditUserInfoPage.FROM_MENU);
				showPage(BCPConstants.PAGE_EDIT_USER_INFO, bundle);
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

	public void setLeftViewUserInfo() {
		
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
		
		tvInfo.setText(null);

		if(!StringUtils.isEmpty(user.getPhone_number())) {
			FontUtils.addSpan(tvInfo, user.getPhone_number() + "\n\n", 0, 1);
		}
		
		if(!StringUtils.isEmpty(user.getAddress())) {
			FontUtils.addSpan(tvInfo, user.getAddress(), 0, 1);
		}
	}
	
	public User getUser() {
		
		return user;
	}
}
