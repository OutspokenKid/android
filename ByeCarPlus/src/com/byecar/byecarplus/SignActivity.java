package com.byecar.byecarplus;

import java.util.List;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

import android.content.Intent;
import android.net.Uri;

import com.byecar.byecarplus.classes.BCPConstants;
import com.byecar.byecarplus.classes.BCPFragment;
import com.byecar.byecarplus.classes.BCPFragmentActivity;
import com.byecar.byecarplus.common.FindPwPage;
import com.byecar.byecarplus.common.TermOfUsePage;
import com.byecar.byecarplus.fragments.user.SignInPage;
import com.byecar.byecarplus.fragments.user.SignPage;
import com.byecar.byecarplus.fragments.user.SignUpPage;
import com.outspoken_kid.classes.RequestManager;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;

public class SignActivity extends BCPFragmentActivity {

	private boolean isMainLaunched;
	
	@Override
	public void bindViews() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setVariables() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setListeners() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSizes() {
		// TODO Auto-generated method stub

	}

	@Override
	public void downloadInfo() {

		setPage(true);
	}

	@Override
	public void setPage(boolean successDownload) {

		loadCookies();
		
		if(getFragmentsSize() == 0) {
			showPage(BCPConstants.PAGE_SIGN, null);
//			showPage(BCPConstants.PAGE_LIGHT, null);
		}
	}

	@Override
	public int getContentViewId() {
		return R.layout.activity_sign;
	}

	@Override
	public int getFragmentFrameResId() {
		
		return R.id.signActivity_fragmentFrame;
	}

	@Override
	public BCPFragment getFragmentByPageCode(int pageCode) {
		
		switch(pageCode) {
		
		case BCPConstants.PAGE_SIGN:
			return new SignPage();
		
		case BCPConstants.PAGE_SIGN_IN:
			return new SignInPage();
		
		case BCPConstants.PAGE_SIGN_UP_FOR_COMMON:
			return new SignUpPage();
		
		case BCPConstants.PAGE_FIND_PW:
			return new FindPwPage();
			
		case BCPConstants.PAGE_TERM_OF_USE:
			return new TermOfUsePage();
		}
		return null;
	}

	@Override
	public void handleUri(Uri uri) {
		// TODO Auto-generated method stub
		
	}
	
//////////////////// Custom methods.

	public void loadCookies() {
		
		LogUtils.log("###SignActivity.checkSession.  Get Cookies from prefs. =====================");
		
		try {
			BasicClientCookie bcc1 = SharedPrefsUtils.getCookie(getCookieName_D1());
			BasicClientCookie bcc2 = SharedPrefsUtils.getCookie(getCookieName_S());
			
			if(bcc1 != null) {
				RequestManager.getCookieStore().addCookie(bcc1);
				LogUtils.log("		key : " + bcc1.getName() + ", value : " + bcc1.getValue());
			}
			
			if(bcc2 != null) {
				RequestManager.getCookieStore().addCookie(bcc2);
				LogUtils.log("		key : " + bcc2.getName() + ", value : " + bcc2.getValue());
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		LogUtils.log("###SignActivity.checkSession.  Print Cookies from cookieStore. =====================");
		
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
	}
	
	public void launchMainForUserActivity() {

		if(isMainLaunched) {
			return;
		}
		
		isMainLaunched = true;
		
		Intent intent = new Intent(this, MainActivity.class);
		
		if(getIntent() != null && getIntent().getData() != null) {
			intent.setData(getIntent().getData());
		}
		
		startActivity(intent);
		finish();
	}
}
