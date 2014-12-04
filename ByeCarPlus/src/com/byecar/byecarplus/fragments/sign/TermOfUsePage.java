package com.byecar.byecarplus.fragments.sign;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.byecar.byecarplus.R;
import com.byecar.byecarplus.classes.BCPAPIs;
import com.byecar.byecarplus.classes.BCPFragmentForSign;
import com.byecar.byecarplus.views.TitleBar;

public class TermOfUsePage extends BCPFragmentForSign {

	private WebView webView;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.termOfUsePage_titleBar);
		webView = (WebView) mThisView.findViewById(R.id.termOfUsePage_webView);
	}

	@Override
	public void setVariables() {
		// TODO Auto-generated method stub

	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void createPage() {

		webView.setWebViewClient(new WebViewClient());
		webView.setWebChromeClient(new WebChromeClient());
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.loadUrl(BCPAPIs.TERM_OF_USE_URL);
	}

	@Override
	public void setListeners() {

	}

	@Override
	public void setSizes() {

	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_term_of_use;
	}

	@Override
	public boolean parseJSON(JSONObject objJSON) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getBackButtonResId() {

		return R.drawable.term_back_btn;
	}

	@Override
	public int getBackButtonWidth() {

		return 187;
	}

	@Override
	public int getBackButtonHeight() {

		return 60;
	}
}
