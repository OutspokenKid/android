package com.byecar.fragments.user;

import org.json.JSONObject;

import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.byecarplus.SignActivity;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPFragment;
import com.byecar.views.TitleBar;
import com.outspoken_kid.fragment.sns.FacebookFragment;
import com.outspoken_kid.fragment.sns.FacebookFragment.FBUserInfo;
import com.outspoken_kid.fragment.sns.KakaoFragment;
import com.outspoken_kid.fragment.sns.KakaoFragment.KakaoUserInfo;
import com.outspoken_kid.fragment.sns.SNSFragment.OnAfterSignInListener;
import com.outspoken_kid.model.SNSUserInfo;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo.holo_light.HoloStyleEditText;

public class SignInPage extends BCPFragment {

	private TextView tvSNS;
	private FrameLayout fbFrame;
	private FrameLayout kkFrame;
	private TextView tvEmail;
	private HoloStyleEditText etEmail;
	private HoloStyleEditText etPw;
	private Button btnSignIn;
	private Button btnFindPw;
	
	private FacebookFragment ff;
	private KakaoFragment kf;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.signInPage_titleBar);
		
		tvSNS = (TextView) mThisView.findViewById(R.id.signInPage_tvSNS);
		fbFrame = (FrameLayout) mThisView.findViewById(R.id.signInPage_fbFrame);
		kkFrame = (FrameLayout) mThisView.findViewById(R.id.signInPage_kkFrame);
		tvEmail = (TextView) mThisView.findViewById(R.id.signInPage_tvEmail);
		etEmail = (HoloStyleEditText) mThisView.findViewById(R.id.signInPage_etEmail);
		etPw = (HoloStyleEditText) mThisView.findViewById(R.id.signInPage_etPw);
		btnSignIn = (Button) mThisView.findViewById(R.id.signInPage_btnSignIn);
		btnFindPw = (Button) mThisView.findViewById(R.id.signInPage_btnFindPw);
	}

	@Override
	public void setVariables() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createPage() {
		
		etEmail.setHint(R.string.hintForEmailSignIn);
		etEmail.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		
		etPw.setHint(R.string.hintForPassword);
		etPw.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
		
		initFacebookFragment();
		initKakaoFragment();
	}

	@Override
	public void setListeners() {

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
		
		int width = ResizeUtils.getSpecificLength(586);
		int textViewHeight = ResizeUtils.getSpecificLength(60);
		int buttonHeight = ResizeUtils.getSpecificLength(82);

		//tvSNS.
		rp = (RelativeLayout.LayoutParams) tvSNS.getLayoutParams();
		rp.height = buttonHeight;
		rp.leftMargin = ResizeUtils.getSpecificLength(28);
		rp.topMargin = ResizeUtils.getSpecificLength(4);

		//btnFb.
		rp = (RelativeLayout.LayoutParams) fbFrame.getLayoutParams();
		rp.width = width;
		rp.height = buttonHeight;
		
		//btnKakao.
		rp = (RelativeLayout.LayoutParams) kkFrame.getLayoutParams();
		rp.width = width;
		rp.height = buttonHeight;
		rp.topMargin = ResizeUtils.getSpecificLength(22);
		rp.bottomMargin = ResizeUtils.getSpecificLength(40);
		
		//tvSNS.
		rp = (RelativeLayout.LayoutParams) tvEmail.getLayoutParams();
		rp.height = buttonHeight;
		rp.topMargin = ResizeUtils.getSpecificLength(10);
		
		//etEmail.
		rp = (RelativeLayout.LayoutParams) etEmail.getLayoutParams();
		rp.width = width;
		rp.height = textViewHeight;
		rp.topMargin = ResizeUtils.getSpecificLength(20);
		
		//etPw.
		rp = (RelativeLayout.LayoutParams) etPw.getLayoutParams();
		rp.width = width;
		rp.height = textViewHeight;
		rp.topMargin = ResizeUtils.getSpecificLength(30);
		
		//btnSignIn.
		rp = (RelativeLayout.LayoutParams) btnSignIn.getLayoutParams();
		rp.width = width;
		rp.height = buttonHeight;
		rp.topMargin = ResizeUtils.getSpecificLength(30);
		
		//btnFindPw.
		rp = (RelativeLayout.LayoutParams) btnFindPw.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(300);
		rp.height = ResizeUtils.getSpecificLength(30);
		rp.topMargin = ResizeUtils.getSpecificLength(20);
		rp.bottomMargin = ResizeUtils.getSpecificLength(40);
		
		FontUtils.setFontSize(tvSNS, 30);
		FontUtils.setFontSize(tvEmail, 30);
		FontUtils.setFontAndHintSize(etEmail.getEditText(), 30, 24);
		FontUtils.setFontAndHintSize(etPw.getEditText(), 30, 24);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_sign_in;
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

		return R.drawable.login_back;
	}

	@Override
	public int getBackButtonWidth() {

		return 160;
	}

	@Override
	public int getBackButtonHeight() {

		return 60;
	}
	
	@Override
	public int getRootViewResId() {

		return R.id.signInPage_mainLayout;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		
		fbFrame.removeAllViews();
		kkFrame.removeAllViews();
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		
		if(ff != null) {
			ff.logout();
		}
		
		if(kf != null) {
			kf.logout();
		}
	}
	
//////////////////// Custom methods.
	
	public void initFacebookFragment() {
		
		ff = new FacebookFragment() {
			
			@Override
			protected int getLoginImageResId() {
				
				return R.drawable.login_facebook2_btn;
			}
		};
	    mActivity.getSupportFragmentManager().beginTransaction().add(fbFrame.getId(), ff, "fbFragment").commit();
	    ff.setOnAfterSignInListener(new OnAfterSignInListener() {
			
			@Override
			public void OnAfterSignIn(SNSUserInfo userInfo) {

				try {
					FBUserInfo fbUserInfo = (FBUserInfo) userInfo;
					fbUserInfo.printSNSUserInfo();
					
					signInWithSNS("facebook", fbUserInfo.id, 
							fbUserInfo.userName, fbUserInfo.profileImage);
				} catch (Exception e) {
					LogUtils.trace(e);
					ff.logout();
				}
			}
		});
	}
	
	public void initKakaoFragment() {

		kf = new KakaoFragment() {
			
			@Override
			protected int getLoginImageResId() {
				
				return R.drawable.login_kakao2_btn;
			}
		};
		
		mActivity.getSupportFragmentManager().beginTransaction().add(kkFrame.getId(), kf, "kkFragment").commit();
		kf.setOnAfterSignInListener(new OnAfterSignInListener() {
			
			@Override
			public void OnAfterSignIn(SNSUserInfo userInfo) {

				try {
					KakaoUserInfo kakaoUserInfo = (KakaoUserInfo) userInfo;
					kakaoUserInfo.printSNSUserInfo();
					
					signInWithSNS("kakaotalk", "" + kakaoUserInfo.id, 
							kakaoUserInfo.nickname, kakaoUserInfo.profile_image);
				} catch (Exception e) {
					LogUtils.trace(e);
					kf.logout();
				}
			}
		});
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
		});
	}
	
	public void signInWithSNS(String sns_key, String sns_user_key, 
			String nickname, String profileUrl) {
		/*
		http://byecar.minsangk.com/users/sns_join.json
		?sns_key=facebook
		&sns_user_key=2342829
		&user[nickname]=%EB%AF%BC%EC%83%81k
		&user[profile_img_url]=/images/20141106/da2eb7df5113d10b79571f4754b2add5.png
		 */
		String url = BCPAPIs.SIGN_IN_WITH_SNS_URL 
				+ "?sns_key=" + sns_key
				+ "&sns_user_key=" + sns_user_key
				+ "&user[nickname]=" + StringUtils.getUrlEncodedString(nickname)
				+ "&user[profile_img_url]=" + StringUtils.getUrlEncodedString(profileUrl);
		
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
						ff.logout();
						kf.logout();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
}
