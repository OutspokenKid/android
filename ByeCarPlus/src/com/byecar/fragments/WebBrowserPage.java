package com.byecar.fragments;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.byecar.byecarplus.R;
import com.byecar.classes.BCPFragment;
import com.outspoken_kid.utils.ResizeUtils;

public class WebBrowserPage extends BCPFragment {

	private Button btnBack;
	private Button btnForward;
	private Button btnRefresh;
	private Button btnClose;
	
	private WebView webView;
	
	@Override
	public void bindViews() {

		btnBack = (Button) mThisView.findViewById(R.id.webBrowserPage_btnBack);
		btnForward = (Button) mThisView.findViewById(R.id.webBrowserPage_btnForward);
		btnRefresh = (Button) mThisView.findViewById(R.id.webBrowserPage_btnRefresh);
		btnClose = (Button) mThisView.findViewById(R.id.webBrowserPage_btnClose);
		
		webView = (WebView) mThisView.findViewById(R.id.webBrowserPage_webView);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {
			url = getArguments().getString("url");
		}
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void createPage() {

		webView.setWebViewClient(new WebViewClient());
		webView.setWebChromeClient(new WebChromeClient());
		webView.requestFocus(); 
		webView.setFocusable(true); 
		webView.setFocusableInTouchMode(true);

		WebSettings webSettings = webView.getSettings();
		webSettings.setUseWideViewPort(true);
		webSettings.setSupportZoom(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setJavaScriptEnabled(true);
	}

	@Override
	public void setListeners() {

		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(webView.canGoBack()) {
					webView.goBack();
				}
			}
		});
		
		btnForward.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(webView.canGoForward()) {
					webView.goForward();
				}
			}
		});
		
		btnRefresh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				webView.reload();
			}
		});
		
		btnClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.closeTopPage();
			}
		});
	}

	@Override
	public void setSizes() {

		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, 88, 
				mThisView.findViewById(R.id.webBrowserPage_titleBg), null, null, null);
		
		ResizeUtils.viewResizeForRelative(60, 60, btnBack, null, null, new int[]{7, 14, 0, 0});
		ResizeUtils.viewResizeForRelative(60, 60, btnForward, null, null, new int[]{14, 14, 0, 0});
		ResizeUtils.viewResizeForRelative(60, 60, btnClose, null, null, new int[]{0, 14, 7, 0});
		ResizeUtils.viewResizeForRelative(60, 60, btnRefresh, null, null, new int[]{0, 14, 14, 0});
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_common_webbrowser;
	}

	@Override
	public int getPageTitleTextResId() {

		return 0;
	}

	@Override
	public int getRootViewResId() {

		return R.id.webBrowserPage_mainLayout;
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

		if(webView.canGoBack()) {
			webView.goBack();
			return true;
		}
		
		return false;
	}

	@Override
	public void onResume() {
		super.onResume();
		
		if(url != null && webView.getUrl() == null) {
			webView.loadUrl(url);
		}
	}
}
