package com.byecar.byecarplus;

import android.net.Uri;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;

import com.byecar.byecarplus.classes.BCPAPIs;
import com.byecar.byecarplus.classes.BCPConstants;
import com.byecar.byecarplus.classes.BCPFragment;
import com.byecar.byecarplus.classes.BCPFragmentActivity;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.views.GestureSlidingLayout;
import com.outspoken_kid.views.GestureSlidingLayout.OnAfterOpenListener;

public class MainForUserActivity extends BCPFragmentActivity {

	private GestureSlidingLayout gestureSlidingLayout;
	private ScrollView leftView;
	private LinearLayout leftViewInner;
	private FrameLayout fragmentFrame;
	
	@Override
	public void bindViews() {

		gestureSlidingLayout = (GestureSlidingLayout) findViewById(R.id.mainForUserActivity_gestureSlidingLayout);
		leftView = (ScrollView) findViewById(R.id.mainForUserActivity_leftView);
		leftViewInner = (LinearLayout) findViewById(R.id.mainForUserActivity_leftViewInner);
		fragmentFrame = (FrameLayout) findViewById(R.id.mainForUserActivity_fragmentFrame);
	}

	@Override
	public void setVariables() {

		gestureSlidingLayout.setTopView(fragmentFrame);
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

		try {
			addSideViewsToSideMenu();
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

		ResizeUtils.viewResize(200, LayoutParams.MATCH_PARENT, leftView, 2, 0, null);
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
//			return new SignInPage();
			
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
						gestureSlidingLayout.close(true, null);
					} else {
						gestureSlidingLayout.open(true, null);
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
	
//////////////////// Custom methods.

	public void addSideViewsToSideMenu() {
		
	}
}
