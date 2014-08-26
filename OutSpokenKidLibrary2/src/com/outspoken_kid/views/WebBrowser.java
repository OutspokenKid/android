package com.outspoken_kid.views;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.webkit.JsResult;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.outspoken_kid.R;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

/**
 * v1.0.3
 * 
 * @author HyungGunKim
 *
 * v1.0.3 - Add keyword and url in onActionWithKeyword.
 * v1.0.2 - Add getCurrentUrl().
 * v1.0.1 - Add Navigation bar.
 */
public class WebBrowser extends FrameLayout {

	private ArrayList<KeywordAction> actions = new ArrayList<KeywordAction>();
	private WebView webView;
	private Button btnBack;
	private Button btnForward;
	private Button btnRefresh;
	private Button btnClose;
	private ProgressBar progress;
	
	public WebBrowser(Context context) {
		this(context, null, 0);
	}
	
	public WebBrowser(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public WebBrowser(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	@SuppressLint({ "SetJavaScriptEnabled", "ClickableViewAccessibility" })
	public void init() {
		
		setBackgroundColor(Color.WHITE);
		setVisibility(View.INVISIBLE);
		setClickable(true);
		
		webView = new WebView(getContext());
		webView.setLayoutParams(new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		webView.setWebViewClient(new WebClientForBrowser());
		webView.setWebChromeClient(new WebChromeClientForBrowser());
		webView.setBackgroundColor(Color.rgb(230, 230, 230));
		webView.requestFocus(); 
		webView.setFocusable(true); 
		webView.setFocusableInTouchMode(true);
		webView.setOnTouchListener(new View.OnTouchListener() { 

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				switch (event.getAction()) { 
				case MotionEvent.ACTION_DOWN: 
				case MotionEvent.ACTION_UP: 
					if(!v.hasFocus()) { 
						v.requestFocus(); 
					} 
					break; 
				} 
				return false;
			} 
		});
		WebSettings webSettings = webView.getSettings();
		webSettings.setUseWideViewPort(true);
		webSettings.setSupportZoom(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setJavaScriptEnabled(true);
		this.addView(webView);
		
		View bg = new View(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 70, bg, 2, Gravity.BOTTOM, null);
		bg.setBackgroundResource(R.drawable.img_browser_bg);
		this.addView(bg);
		
		btnBack = new Button(getContext());
		ResizeUtils.viewResize(60, 60, btnBack, 2, Gravity.BOTTOM|Gravity.RIGHT, new int[]{0, 0, 300, 0});
		btnBack.setBackgroundResource(R.drawable.img_browser_back_off);
		btnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				goBack();
			}
		});
		this.addView(btnBack);
		
		btnForward = new Button(getContext());
		ResizeUtils.viewResize(60, 60, btnForward, 2, Gravity.BOTTOM|Gravity.RIGHT, new int[]{0, 0, 210, 0});
		btnForward.setBackgroundResource(R.drawable.img_browser_forward_off);
		btnForward.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goForward();
			}
		});
		this.addView(btnForward);
		
		btnRefresh = new Button(getContext());
		ResizeUtils.viewResize(60, 60, btnRefresh, 2, Gravity.BOTTOM|Gravity.RIGHT, new int[]{0, 0, 120, 0});
		btnRefresh.setBackgroundResource(R.drawable.img_browser_refresh);
		btnRefresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				refresh();
			}
		});
		this.addView(btnRefresh);
		
		btnClose = new Button(getContext());
		ResizeUtils.viewResize(60, 60, btnClose, 2, Gravity.BOTTOM|Gravity.RIGHT, new int[]{0, 0, 30, 0});
		btnClose.setBackgroundResource(R.drawable.img_browser_close);
		btnClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				close();
			}
		});
		this.addView(btnClose);
		
		progress = new ProgressBar(getContext());
		ResizeUtils.viewResize(50, 50, progress, 2, Gravity.CENTER, null);
		this.addView(progress);
	}
	
	public void open(final String url, final byte[] postData) {

		GestureSlidingLayout.setScrollLock(true);
		
		TranslateAnimation aaIn = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT, 0,
				TranslateAnimation.RELATIVE_TO_PARENT, 0,
				TranslateAnimation.RELATIVE_TO_PARENT, 1,
				TranslateAnimation.RELATIVE_TO_PARENT, 0);
		aaIn.setDuration(500);
		aaIn.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
			
				webView.postUrl(url, postData);
			}
		});
		setVisibility(View.VISIBLE);
		startAnimation(aaIn);
	}
	
	public void setButtonBg() {
		
		if(canGoBack()) {
			btnBack.setBackgroundResource(R.drawable.img_browser_back_on);
		} else {
			btnBack.setBackgroundResource(R.drawable.img_browser_back_off);
		}
		
		if(webView.canGoForward()) {
			btnForward.setBackgroundResource(R.drawable.img_browser_forward_on);
		} else {
			btnForward.setBackgroundResource(R.drawable.img_browser_forward_off);
		}
	}
	
	public void close() {

		TranslateAnimation aaOut = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT, 0,
				TranslateAnimation.RELATIVE_TO_PARENT, 0,
				TranslateAnimation.RELATIVE_TO_PARENT, 0,
				TranslateAnimation.RELATIVE_TO_PARENT, 1);
		aaOut.setDuration(500);
		aaOut.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				
				webView.loadUrl("about:blank");
				webView.clearCache(true);
				webView.clearHistory();
				GestureSlidingLayout.setScrollLock(false);
			}
		});
		setVisibility(View.INVISIBLE);
		startAnimation(aaOut);
	}
	
	public void goBack() {
		
		if(canGoBack()) {
			webView.goBack();
		}
	}
	
	public void goForward() {
		
		if(webView.canGoForward()) {
			webView.goForward();
		}
	}
	
	public void refresh() {
		
		webView.reload();
	}
	
	public boolean canGoBack() {
		
		try {
			if(webView.canGoBack()) {
				WebBackForwardList list = webView.copyBackForwardList();
				int currentIndex = list.getCurrentIndex();
				
				if(currentIndex != 1 || !list.getItemAtIndex(0).getUrl().equals("about:blank")) {
					return true;
				}
			}
		} catch(Exception e) {
		}
		
		return false;
	}
	
	public void handleBackKey() {
		
		if(canGoBack()) {
			webView.goBack();
		} else {
			close();
		}
	}
	
	public void putAction(String keyword, OnActionWithKeywordListener onActionWithKeywordListener) {
		
		if(!StringUtils.isEmpty(keyword) && onActionWithKeywordListener != null) {
			actions.add(new KeywordAction(keyword, onActionWithKeywordListener));
		}
	}

	public String getCurrentUrl() {
		
		return webView.getUrl();
	}
	
/////////////////////// Classes.
	
	public class WebClientForBrowser extends WebViewClient {
	
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			
			int size = actions.size();
			for(int i=0; i<size; i++) {
				
				if(url.contains(actions.get(i).getKeyword())) {
					actions.get(i).getListener().onActionWithKeyword(
							actions.get(i).getKeyword(), url);
				}
			}
			
			super.onPageStarted(view, url, favicon);
			
			if(!url.equals("about:blank")) {
				progress.setVisibility(View.VISIBLE);
			}
			
			setButtonBg();
		}
		
		@Override
		public void onPageFinished(WebView view, String url) {
			
			super.onPageFinished(view, url);
			
			if(!url.equals("about:blank")) {
				progress.setVisibility(View.INVISIBLE);
			} else if(getVisibility() == View.VISIBLE) {
				close();
			}
			
			setButtonBg();
		}
	}
	
	public class WebChromeClientForBrowser extends WebChromeClient {
		
		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			return super.onJsAlert(view, url, message, result);
		}
		
		@Override
		public boolean onJsConfirm(WebView view, String url, String message,
				JsResult result) {
			return super.onJsConfirm(view, url, message, result);
		}
	}

	public class KeywordAction {
		
		private String keyword;
		private OnActionWithKeywordListener listener;
		
		public KeywordAction(){}
		
		public KeywordAction(String keyword, OnActionWithKeywordListener listener) {
			this.keyword = keyword;
			this.listener = listener;
		}
		
		public String getKeyword() {
			return keyword;
		}
		public void setKeyword(String keyword) {
			this.keyword = keyword;
		}
		public OnActionWithKeywordListener getListener() {
			return listener;
		}
		public void setListeners(OnActionWithKeywordListener listener) {
			this.listener = listener;
		}
	}
	
/////////////////////// Interfaces.
	
	public interface OnActionWithKeywordListener {
		
		public void onActionWithKeyword(String keyword, String url);
	}
}