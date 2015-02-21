package com.byecar.fragments.dealer;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplusfordealer.R;
import com.byecar.byecarplusfordealer.SignActivity;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPFragment;
import com.byecar.classes.BCPFragmentActivity.OnAfterCheckSessionListener;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo.holo_light.HoloStyleEditText;

public class SignPage extends BCPFragment {

	private ImageView ivBg;
	private TextView tvSignInText;
	private HoloStyleEditText etEmail;
	private HoloStyleEditText etPw;
	private Button btnSignUp;
	private Button btnSignIn;
	private Button btnFindPw;
	
	@Override
	public void bindViews() {
		
		ivBg = (ImageView) mThisView.findViewById(R.id.signPage_ivBg);
		
		tvSignInText = (TextView) mThisView.findViewById(R.id.signPage_tvSignInText);
		
		etEmail = (HoloStyleEditText) mThisView.findViewById(R.id.signPage_etEmail);
		etPw = (HoloStyleEditText) mThisView.findViewById(R.id.signPage_etPw);
		
		btnSignUp = (Button) mThisView.findViewById(R.id.signPage_btnSignUp);
		btnSignIn = (Button) mThisView.findViewById(R.id.signPage_btnSignIn);
		btnFindPw = (Button) mThisView.findViewById(R.id.signPage_btnFindPw);
	}

	@Override
	public void setVariables() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createPage() {

		etEmail.getEditText().setTextColor(Color.WHITE);
		etEmail.getEditText().setHintTextColor(Color.rgb(238, 229, 224));
		etEmail.getEditText().setHint(R.string.hintForEmailSignIn);
		etEmail.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		
		etPw.getEditText().setTextColor(Color.WHITE);
		etPw.getEditText().setHintTextColor(Color.rgb(238, 229, 224));
		etPw.getEditText().setHint(R.string.hintForPassword);
		etPw.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
		
		tvSignInText.requestFocus();
	}

	@Override
	public void setListeners() {
		
		btnSignUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putBoolean("forDealer", true);
				mActivity.showPage(BCPConstants.PAGE_SIGN_UP_FOR_COMMON, bundle);
			}
		});
		
		btnSignIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				signIn();
			}
		});
		
		btnFindPw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(BCPConstants.PAGE_FIND_PW, null);
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		
		//logo.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.signPage_logo).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(299);
		rp.height = ResizeUtils.getSpecificLength(339);
		rp.topMargin = ResizeUtils.getSpecificLength(134);
		
		//tvSignInText
		rp = (RelativeLayout.LayoutParams) tvSignInText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.topMargin = ResizeUtils.getSpecificLength(136);
		
		//etEmail
		rp = (RelativeLayout.LayoutParams) etEmail.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(60);
		
		//etPw
		rp = (RelativeLayout.LayoutParams) etPw.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.topMargin = ResizeUtils.getSpecificLength(26);
		
		//btnSignUp.
		rp = (RelativeLayout.LayoutParams) btnSignUp.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(286);
		rp.height = ResizeUtils.getSpecificLength(83);
		rp.leftMargin = ResizeUtils.getSpecificLength(28);
		rp.topMargin = ResizeUtils.getSpecificLength(50);
		
		//btnSignIn.
		rp = (RelativeLayout.LayoutParams) btnSignIn.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(286);
		rp.height = ResizeUtils.getSpecificLength(83);
		rp.leftMargin = ResizeUtils.getSpecificLength(14);
		
		//btnFindPw.
		rp = (RelativeLayout.LayoutParams) btnFindPw.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(298);
		rp.height = ResizeUtils.getSpecificLength(30);
		rp.topMargin = ResizeUtils.getSpecificLength(40);
		
		FontUtils.setFontSize(tvSignInText, 30);
		FontUtils.setFontStyle(tvSignInText, FontUtils.BOLD);
		FontUtils.setFontAndHintSize(etEmail.getEditText(), 30, 20);
		FontUtils.setFontAndHintSize(etPw.getEditText(), 30, 20);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_sign;
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
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int getBackButtonWidth() {

		return 0;
	}

	@Override
	public int getBackButtonHeight() {

		return 0;
	}

	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		if(Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB
				&& Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT && ivBg != null) {
			 ivBg.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		ivBg.setImageResource(R.drawable.d_splash_bg);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				checkSession();
			}
		}, 1000);
	}
	
	@Override
	public int getRootViewResId() {

		return R.id.signPage_mainLayout;
	}
	
//////////////////// Custom methods.

	public void checkSession() {

		if(mActivity != null) {
			mActivity.checkSession(new OnAfterCheckSessionListener() {
				
				@Override
				public void onAfterCheckSession(boolean isSuccess, JSONObject objJSON) {

					if(isSuccess) {
						
						if(mActivity != null) {
							((SignActivity)mActivity).launchMainForUserActivity();
						}
					} else {
						showButtons();
					}
				}
			});
		}
	}
	
	public void showButtons() {

		if(tvSignInText.getVisibility() != View.VISIBLE) {
			AlphaAnimation aaIn = new AlphaAnimation(0, 1);
			aaIn.setDuration(500);
			
			tvSignInText.setVisibility(View.VISIBLE);
			etEmail.setVisibility(View.VISIBLE);
			etPw.setVisibility(View.VISIBLE);
			btnSignUp.setVisibility(View.VISIBLE);
			btnSignIn.setVisibility(View.VISIBLE);
			btnFindPw.setVisibility(View.VISIBLE);
			
			tvSignInText.startAnimation(aaIn);
			etEmail.startAnimation(aaIn);
			etPw.startAnimation(aaIn);
			btnSignUp.startAnimation(aaIn);
			btnSignIn.startAnimation(aaIn);
			btnFindPw.startAnimation(aaIn);
		}
	}

	public void signIn() {
		
		//http://byecar.minsangk.com/users/login.json?user[email]=minsangk@me.com&user[pw]=1234
		String url = BCPAPIs.SIGN_IN_URL
				+ "?user[email]=" + StringUtils.getUrlEncodedString(etEmail.getEditText())
				+ "&user[pw]=" + StringUtils.getUrlEncodedString(etPw.getEditText());
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("SignInPage.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToSignIn);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("SignInPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);
					
					if(objJSON.getInt("result") == 1) {
						((SignActivity)mActivity).launchMainForUserActivity();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		}, mActivity.getLoadingView());
	}
}
