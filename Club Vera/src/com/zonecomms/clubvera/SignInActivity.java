package com.zonecomms.clubvera;

import java.net.URLEncoder;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.outspoken_kid.fragment.sns.FacebookFragment;
import com.outspoken_kid.fragment.sns.FacebookFragment.FBUserInfo;
import com.outspoken_kid.fragment.sns.KakaoFragment;
import com.outspoken_kid.fragment.sns.KakaoFragment.KakaoUserInfo;
import com.outspoken_kid.fragment.sns.NaverFragment;
import com.outspoken_kid.fragment.sns.NaverFragment.NaverUserInfo;
import com.outspoken_kid.fragment.sns.SNSFragment.OnAfterSignInListener;
import com.outspoken_kid.fragment.sns.TwitterFragment;
import com.outspoken_kid.fragment.sns.TwitterFragment.TwitterUserInfo;
import com.outspoken_kid.model.SNSUserInfo;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.WebBrowser;
import com.outspoken_kid.views.WebBrowser.OnActionWithKeywordListener;
import com.outspoken_kid.views.holo.holo_light.HoloConstants;
import com.outspoken_kid.views.holo.holo_light.HoloStyleButton;
import com.outspoken_kid.views.holo.holo_light.HoloStyleEditText;
import com.zonecomms.clubvera.classes.ZoneConstants;
import com.zonecomms.clubvera.classes.ZonecommsApplication;
import com.zonecomms.clubvera.classes.ZonecommsFragmentActivity;
import com.zonecomms.common.models.MyInfo;
import com.zonecomms.common.utils.AppInfoUtils;

public class SignInActivity extends ZonecommsFragmentActivity {
	
	private static SignInActivity signInActivity;

	private LinearLayout innerLinear;
	private View logo;
	private HoloStyleEditText etId;
	private HoloStyleEditText etPw;
	private WebBrowser webBrowser;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		signInActivity = this;
	}

	@Override
	public void bindViews() {
		innerLinear = (LinearLayout) findViewById(R.id.signInActivity_innerLinear);
		webBrowser = (WebBrowser) findViewById(R.id.signInActivity_webBrowser);
	}
	
	@Override
	public void setVariables() {
	}

	@Override
	public void createPage() {

		addViewsForInnerLinear();
	}

	@Override
	public void setSizes() {
	}

	@Override
	public void setListeners() {
	}
	
	@Override
	public void downloadInfo() {
	}

	@Override
	public void setPage(boolean successDownload) {
	}
	
	@Override
	public int getContentViewId() {
	
		return R.layout.activity_sign_in;
	}
	
	@Override
	public void onBackPressed() {

		if(webBrowser.getVisibility() == View.VISIBLE) {
			webBrowser.handleBackKey();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void finish() {
		
		SoftKeyboardUtils.hideKeyboard(this, logo);
		
		super.finish();
	}
	
///////////// Custom methods.
	
	public void addViewsForInnerLinear() {

		View topBlank = new View(this);
		topBlank.setLayoutParams(new LinearLayout.LayoutParams(1, 0, 1));
		innerLinear.addView(topBlank);
		
		logo = new View(this);
		ResizeUtils.viewResize(624, 247, logo, 1, Gravity.CENTER_HORIZONTAL, null);
		logo.setBackgroundResource(R.drawable.img_nlogo);
		innerLinear.addView(logo);
		
		etId = new HoloStyleEditText(this);
		ResizeUtils.viewResize(500, 70, etId, 1, Gravity.CENTER_HORIZONTAL, new int[]{0, 40, 0, 0});
		etId.setHint(R.string.hintForId);
		FontUtils.setFontSize(etId.getEditText(), 20);
		etId.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		innerLinear.addView(etId);
		
		etPw = new HoloStyleEditText(this);
		ResizeUtils.viewResize(500, 70, etPw, 1, Gravity.CENTER_HORIZONTAL, new int[]{0, 20, 0, 30});
		etPw.setHint(R.string.hintForPw);
		FontUtils.setFontSize(etPw.getEditText(), 20);
		etPw.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		etPw.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
		innerLinear.addView(etPw);
		
		TextView tvNId = new TextView(this);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, tvNId, 1, 0, null);
		tvNId.setGravity(Gravity.CENTER_HORIZONTAL);
		tvNId.setText(R.string.introduceNId);
		tvNId.setTextColor(HoloConstants.COLOR_HOLO_TEXT);
		FontUtils.setFontSize(tvNId, 32);
		innerLinear.addView(tvNId);
		
		HoloStyleButton btnSignIn = new HoloStyleButton(this);
		ResizeUtils.viewResize(400, 80, btnSignIn, 1, Gravity.CENTER_HORIZONTAL, new int[]{0, 30, 0, 0});
		btnSignIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				String id = etId.getEditText().getText().toString();
				String pw = etPw.getEditText().getText().toString();
				
				if(id == null || id.length() < 4 || id.length() > 20
						|| id.contains(" ")) {
					
					ToastUtils.showToast(R.string.invalidId);
					return;
				}
				
				if(pw == null || pw.length() < 4 || pw.length() > 20
						|| pw.contains(" ")) {
					
					ToastUtils.showToast(R.string.invalidPw);
					return;
				}

				OnAfterSigningInListener oasl = new OnAfterSigningInListener() {

					@Override
					public void OnAfterSigningIn(boolean successSignIn) {
						
						if(successSignIn) {
							setResult(RESULT_OK);
							finish();
						} else {
							ToastUtils.showToast(R.string.failToSignIn);
						}
					}
				};
				signIn(id, pw, oasl);
			}
		});
		btnSignIn.setText(R.string.signIn);
		FontUtils.setFontSize(btnSignIn.getTextView(), 25);
		innerLinear.addView(btnSignIn);
		
		HoloStyleButton btnSignUp = new HoloStyleButton(this);
		ResizeUtils.viewResize(400, 80, btnSignUp, 1, Gravity.CENTER_HORIZONTAL, new int[]{0, 30, 0, 0});
		btnSignUp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				launchToSignUpActivity();
			}
		});
		btnSignUp.setText(R.string.signUp);
		FontUtils.setFontSize(btnSignUp.getTextView(), 25);
		innerLinear.addView(btnSignUp);
		
		HoloStyleButton btnFind = new HoloStyleButton(this);
		ResizeUtils.viewResize(400, 80, btnFind, 1, Gravity.CENTER_HORIZONTAL, new int[]{0, 30, 0, 0});
		btnFind.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				webBrowser.open(ZoneConstants.URL_FOR_FIND_ID_AND_PW, null);
			}
		});
		btnFind.setText(R.string.findIdAndPw);
		FontUtils.setFontSize(btnFind.getTextView(), 25);
		innerLinear.addView(btnFind);

		FrameLayout fbFrame = new FrameLayout(this);
		ResizeUtils.viewResize(150, 150, fbFrame, 1, Gravity.CENTER_HORIZONTAL, null);
		fbFrame.setId(10001);
		innerLinear.addView(fbFrame);
		
		FacebookFragment ff = new FacebookFragment() {
			
			@Override
			protected int getLoginImageResId() {
				
				return R.drawable.btn_loginf_01;
			}
		};
	    getSupportFragmentManager().beginTransaction().add(fbFrame.getId(), ff, "fbFragment").commit();
	    ff.setOnAfterSignInListener(new OnAfterSignInListener() {
			
			@Override
			public void OnAfterSignIn(SNSUserInfo userInfo) {

				FBUserInfo fbUserInfo = (FBUserInfo) userInfo;
				fbUserInfo.printSNSUserInfo();
			}
		});
	    
	    FrameLayout nvFrame = new FrameLayout(this);
		ResizeUtils.viewResize(150, 150, nvFrame, 1, Gravity.CENTER_HORIZONTAL, null);
		nvFrame.setId(10002);
		innerLinear.addView(nvFrame);
		
		NaverFragment nf = new NaverFragment() {
			
			@Override
			protected int getLoginImageResId() {
				
				return R.drawable.btn_loginf_01;
			}
		};
	    getSupportFragmentManager().beginTransaction().add(nvFrame.getId(), nf, "nvFragment").commit();
		nf.setOnAfterSignInListener(new OnAfterSignInListener() {
			
			@Override
			public void OnAfterSignIn(SNSUserInfo userInfo) {

				NaverUserInfo naverUserInfo = (NaverUserInfo) userInfo;
				naverUserInfo.printSNSUserInfo();
			}
		});
	    
	    FrameLayout kkFrame = new FrameLayout(this);
		ResizeUtils.viewResize(150, 150, kkFrame, 1, Gravity.CENTER_HORIZONTAL, null);
		kkFrame.setId(10003);
		innerLinear.addView(kkFrame);
	    
		KakaoFragment kf = new KakaoFragment() {
			
			@Override
			protected int getLoginImageResId() {
				
				return R.drawable.btn_loginf_01;
			}
		};
	    getSupportFragmentManager().beginTransaction().add(kkFrame.getId(), kf, "kkFragment").commit();
		kf.setOnAfterSignInListener(new OnAfterSignInListener() {
			
			@Override
			public void OnAfterSignIn(SNSUserInfo userInfo) {

				KakaoUserInfo kakaoUserInfo = (KakaoUserInfo) userInfo;
				kakaoUserInfo.printSNSUserInfo();
			}
		});
		
		FrameLayout twFrame = new FrameLayout(this);
		ResizeUtils.viewResize(150, 150, twFrame, 1, Gravity.CENTER_HORIZONTAL, null);
		twFrame.setId(10004);
		innerLinear.addView(twFrame);
	    
		TwitterFragment tf = new TwitterFragment() {
			
			@Override
			protected int getLoginImageResId() {
				
				return R.drawable.btn_loginf_01;
			}

			@Override
			public WebBrowser getWebBrowser() {

				return webBrowser;
			}
		};
	    getSupportFragmentManager().beginTransaction().add(twFrame.getId(), tf, "twFragment").commit();
	    tf.setOnAfterSignInListener(new OnAfterSignInListener() {
			
			@Override
			public void OnAfterSignIn(SNSUserInfo userInfo) {

				TwitterUserInfo twitterUserInfo = (TwitterUserInfo) userInfo;
				twitterUserInfo.printSNSUserInfo();
			}
		});
	    
		View bottomBlank = new View(this);
		bottomBlank.setLayoutParams(new LinearLayout.LayoutParams(1, 0, 1));
		innerLinear.addView(bottomBlank);
	}

	public static void signIn(final String id, final String pw, final OnAfterSigningInListener onAfterSigningInListener) {
		
		try {
			ToastUtils.showToast(R.string.signingIn);
			
			String url = ZoneConstants.BASE_URL + "auth/login" +
					"?id=" + URLEncoder.encode(id, "UTF-8") + 
					"&password=" + URLEncoder.encode(pw, "UTF-8") + 
					"&image_size=308" +
					"&" + AppInfoUtils.getAppInfo(AppInfoUtils.WITHOUT_MEMBER_ID);
			
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {
				
				@Override
				public void onError(String url) {

					LogUtils.log("SignInActivity.SignIn.onError.  url : " + url);
					
					if(onAfterSigningInListener != null) {
						onAfterSigningInListener.OnAfterSigningIn(false);
					}
				}
				
				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					LogUtils.log("SignInActivity.SignIn.onCompleted.  url : " + url + "\nresult : " + objJSON);
					
					try {
						if(!objJSON.has("data")
								|| !objJSON.has("errorMsg") 
								|| StringUtils.isEmpty(objJSON.getString("errorMsg"))
								|| objJSON.getInt("errorCode") != 1) {
							
							if(onAfterSigningInListener != null) {
								onAfterSigningInListener.OnAfterSigningIn(false);
							}
							return;
						}
						
						ZonecommsApplication.myInfo = new MyInfo();
						ZonecommsApplication.myInfo.setUserInfo(objJSON);
						
						SharedPrefsUtils.addDataToPrefs(ZoneConstants.PREFS_SIGN, "id", id);
						SharedPrefsUtils.addDataToPrefs(ZoneConstants.PREFS_SIGN, "pw", pw);

						if(onAfterSigningInListener != null) {
							onAfterSigningInListener.OnAfterSigningIn(true);	
						}
					} catch(Exception e) {
						LogUtils.trace(e);
						
						if(onAfterSigningInListener != null) {
							onAfterSigningInListener.OnAfterSigningIn(false);
						}
					}
				}
			});
		} catch(Exception e) {
			LogUtils.trace(e);
			
			if(onAfterSigningInListener != null) {
				onAfterSigningInListener.OnAfterSigningIn(false);
			}
		}
	}
	
	public void signInByFacebook() {
		ToastUtils.showToast(R.string.signBySNSwillBeUpdatedLater);
	}
	
	public void signInByTwitter() {
		ToastUtils.showToast(R.string.signBySNSwillBeUpdatedLater);
	}
	
	public void signInByMe2day() {
		ToastUtils.showToast(R.string.signBySNSwillBeUpdatedLater);
	}

	public static SignInActivity getInstance() {
		
		return signInActivity;
	}
	
	public void launchToSignUpActivity() {
		
		SignUpActivity.signInActivity = this;
		Intent intent = new Intent(this, SignUpActivity.class);
		Intent i = getIntent();				//'i' is intent that passed intent from before.
		
		if(i!= null) {
			if(i.getData() != null) {
				intent.setData(i.getData());
			}
		}
		
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

///////////////// Interfaces.

	/**
	 * Using at Auto sign in.
	 * @author HyungGunKim
	 */
	public interface OnAfterSigningInListener {

		public void OnAfterSigningIn(boolean successSignIn);
	}

	@Override
	public int getFragmentFrameResId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setCustomAnimations(FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}
}