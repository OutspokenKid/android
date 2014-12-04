package com.byecar.byecarplus;

import java.util.List;

import org.apache.http.cookie.Cookie;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.byecar.byecarplus.classes.BCPAPIs;
import com.byecar.byecarplus.classes.BCPConstants;
import com.byecar.byecarplus.classes.BCPFragment;
import com.byecar.byecarplus.classes.BCPFragmentActivity;
import com.byecar.byecarplus.fragments.MainForUserPage;
import com.byecar.byecarplus.models.User;
import com.outspoken_kid.classes.RequestManager;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.GestureSlidingLayout;
import com.outspoken_kid.views.GestureSlidingLayout.OnAfterOpenListener;

public class MainForUserActivity extends BCPFragmentActivity {

	private User user;
	
	private GestureSlidingLayout gestureSlidingLayout;
	private RelativeLayout leftView;
	private ScrollView scrollView;
	private LinearLayout leftViewInner;
	
	private ImageView ivProfile;
	private ImageView ivBg;
	private TextView tvNickname;
	private TextView tvInfo;
	private Button btnEdit;
	private Button[] menuButtons;
	
	@Override
	public void bindViews() {

		gestureSlidingLayout = (GestureSlidingLayout) findViewById(R.id.mainForUserActivity_gestureSlidingLayout);
		leftView = (RelativeLayout) findViewById(R.id.mainForUserActivity_leftView);
		
		ivProfile = (ImageView) findViewById(R.id.mainForUserActivity_ivProfile);
		ivBg = (ImageView) findViewById(R.id.mainForUserActivity_ivBg);
		tvNickname = (TextView) findViewById(R.id.mainForUserActivity_tvNickname);
		tvInfo = (TextView) findViewById(R.id.mainForUserActivity_tvInfo);
		btnEdit = (Button) findViewById(R.id.mainForUserActivity_btnEdit);
		
		scrollView = (ScrollView) findViewById(R.id.mainForUserActivity_scrollView);
		leftViewInner = (LinearLayout) findViewById(R.id.mainForUserActivity_leftViewInner);
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
	}

	@Override
	public void createPage() {

		LogUtils.log("###MainForUserActivity.checkSession.  Print Cookies from cookieStore. =====================");
		
		try {
			List<Cookie> cookies = RequestManager.getCookieStore().getCookies();
			
			for(Cookie cookie : cookies) {
				LogUtils.log("		key : " + cookie.getName() + ", value : " + cookie.getValue());
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		try {
			setLeftView();
		} catch(Exception e) {
			LogUtils.trace(e);
			finish();
		}
	}

	@Override
	public void setListeners() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSizes() {

		ResizeUtils.viewResize(492, LayoutParams.MATCH_PARENT, leftView, 2, 0, null);
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
	protected void onResume() {
		super.onResume();
		
		checkSession();
	}
	
//////////////////// Custom methods.

	public void checkSession() {
		
		checkSession(new OnAfterCheckSessionListener() {
			
			@Override
			public void onAfterCheckSession(boolean isSuccess, JSONObject objJSON) {

				if(isSuccess) {
					saveCookies();
					user = new User(objJSON);
					
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

		setImageViews();
		setOtherViews();
		addButtons();
		
		ivProfile.setImageResource(R.drawable.c1);
		
		tvNickname.setText("수지짱");
		
		tvInfo.setText(null);
		FontUtils.addSpan(tvInfo, "010 2626 4636", 0, 1);
		FontUtils.addSpan(tvInfo, "\n서울특별시 관악구 봉천동", 0, 1);
	}
	
	public void setImageViews() {
		
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
		
		rp = (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(20);
		
		FontUtils.setFontStyle(tvNickname, FontUtils.BOLD);
		FontUtils.setFontSize(tvNickname, 30);
		FontUtils.setFontSize(tvInfo, 20);
	}

	public void addButtons() {
		
		menuButtons = new Button[11];
		
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
		
		int size = 11;
		for(int i=0; i<size; i++) {
	
			//타이틀 추가 : adadad 60%, text color 393939 
			if(i == 0 || i == 4) {
				TextView tvTitle = new TextView(this);
				ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 32, tvTitle, 1, 0, null);
				tvTitle.setBackgroundColor(Color.argb(99, 173, 173, 173));
				tvTitle.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
				tvTitle.setPadding(ResizeUtils.getSpecificLength(4), 0, 0, 0);
				FontUtils.setFontSize(tvTitle, 16);
				tvTitle.setTextColor(Color.rgb(57, 57, 57));
				tvTitle.setText(i==0? "거래" : "서비스");
				leftViewInner.addView(tvTitle);
			}
			
			//버튼 추가.
			menuButtons[i] = new Button(this);
			ResizeUtils.viewResize(460, 70, menuButtons[i], 1, Gravity.LEFT, new int[]{0, 10, 0, 10});
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
	}

	public User getUser() {
		
		return user;
	}
}
