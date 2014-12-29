package com.zonecomms.clubvera;

import java.net.URLEncoder;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.PasswordTransformationMethod;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
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
import com.outspoken_kid.views.holo.holo_light.HoloStyleEditText;
import com.zonecomms.clubvera.classes.ZoneConstants;
import com.zonecomms.clubvera.classes.ZonecommsApplication;
import com.zonecomms.clubvera.classes.ZonecommsFragmentActivity;
import com.zonecomms.common.models.MyInfo;
import com.zonecomms.common.utils.AppInfoUtils;

public class SignInActivity extends ZonecommsFragmentActivity {
	
	private static SignInActivity signInActivity;

	private TextView tvNSeries;
	private Button btnSignUp;
	private HoloStyleEditText etId;
	private HoloStyleEditText etPw;
	private Button btnSignIn;
	private Button btnFindIdAndPw;
//	private TextView tvSNSLogin;
//	private FrameLayout fbFrame;
//	private FrameLayout kkFrame;
//	private FrameLayout twFrame;
//	private FrameLayout nvFrame;
//	private TextView tvSNSWarning;
	private WebBrowser webBrowser;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		signInActivity = this;
	}

	@Override
	public void bindViews() {
		
		tvNSeries = (TextView) findViewById(R.id.signInActivity_tvNSeries);
		btnSignUp = (Button) findViewById(R.id.signInActivity_btnSignUp);
		etId = (HoloStyleEditText) findViewById(R.id.signInActivity_etId);
		etPw = (HoloStyleEditText) findViewById(R.id.signInActivity_etPw);
		btnSignIn = (Button) findViewById(R.id.signInActivity_btnSignIn);
		btnFindIdAndPw = (Button) findViewById(R.id.signInActivity_btnFindIdAndPw);
//		tvSNSLogin = (TextView) findViewById(R.id.signInActivity_tvSNSLogin);
//		fbFrame = (FrameLayout) findViewById(R.id.signInActivity_fbFrame);
//		kkFrame = (FrameLayout) findViewById(R.id.signInActivity_kkFrame);
//		twFrame = (FrameLayout) findViewById(R.id.signInActivity_twFrame);
//		nvFrame = (FrameLayout) findViewById(R.id.signInActivity_nvFrame);
//		tvSNSWarning = (TextView) findViewById(R.id.signInActivity_tvSNSWarning);
		
		webBrowser = (WebBrowser) findViewById(R.id.signInActivity_webBrowser);
	}
	
	@Override
	public void setVariables() {
	}

	@Override
	public void createPage() {
		
		SpannableStringBuilder sp1 = new SpannableStringBuilder("N Series");
		sp1.setSpan(new RelativeSizeSpan(1.4f), 0, sp1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvNSeries.append(sp1); 
		
		SpannableStringBuilder sp2 = new SpannableStringBuilder("\n하나의 아이디로 사용가능합니다.");
		tvNSeries.append(sp2); 
		
		etId.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		etId.setHint(R.string.id);
		
		etPw.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		etPw.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
		etPw.setHint(R.string.password);
		
//		final FacebookFragment ff = new FacebookFragment() {
//			
//			@Override
//			protected int getLoginImageResId() {
//				
//				return R.drawable.btn_loginf_01;
//			}
//		};
//	    getSupportFragmentManager().beginTransaction().add(fbFrame.getId(), ff, "fbFragment").commit();
//	    ff.setOnAfterSignInListener(new OnAfterSignInListener() {
//			
//			@Override
//			public void OnAfterSignIn(SNSUserInfo userInfo) {
//
//				try {
//					FBUserInfo fbUserInfo = (FBUserInfo) userInfo;
//					fbUserInfo.printSNSUserInfo();
//					
//					String nickname = "";
//					
//					if(fbUserInfo.userName != null) {
//						nickname = fbUserInfo.userName;
//					} else {
//						
//						if(fbUserInfo.firstName != null) {
//							nickname += fbUserInfo.firstName;
//						}
//						
//						if(fbUserInfo.lastName != null) {
//							nickname += fbUserInfo.lastName;
//						}
//					}
//					
//					signUpWithSNS(fbUserInfo.id, "facebook", nickname);
//				} catch (Exception e) {
//					LogUtils.trace(e);
//					ff.logout();
//				}
//			}
//		});
//	    
//		final KakaoFragment kf = new KakaoFragment() {
//			
//			@Override
//			protected int getLoginImageResId() {
//				
//				return R.drawable.btn_logink_01;
//			}
//		};
//	    getSupportFragmentManager().beginTransaction().add(kkFrame.getId(), kf, "kkFragment").commit();
//		kf.setOnAfterSignInListener(new OnAfterSignInListener() {
//			
//			@Override
//			public void OnAfterSignIn(SNSUserInfo userInfo) {
//
//				try {
//					KakaoUserInfo kakaoUserInfo = (KakaoUserInfo) userInfo;
//					kakaoUserInfo.printSNSUserInfo();
//					
//					signUpWithSNS("" + kakaoUserInfo.id, "kakao", kakaoUserInfo.nickname);
//				} catch (Exception e) {
//					LogUtils.trace(e);
//					kf.logout();
//				}
//			}
//		});
//	    
//		final TwitterFragment tf = new TwitterFragment() {
//			
//			@Override
//			protected int getLoginImageResId() {
//				
//				return R.drawable.btn_logint_01;
//			}
//
//			@Override
//			public WebBrowser getWebBrowser() {
//
//				return webBrowser;
//			}
//
//			@Override
//			public String getTwitterConsumerKey() {
//
//				return getString(R.string.twitter_consumerKey);
//			}
//
//			@Override
//			public String getTwitterConsumerSecret() {
//
//				return getString(R.string.twitter_consumerSecret);
//			}
//
//			@Override
//			public String getTwitterCallBackUrl() {
//
//				return getString(R.string.twitter_callback_url);
//			}
//		};
//	    getSupportFragmentManager().beginTransaction().add(twFrame.getId(), tf, "twFragment").commit();
//	    tf.setOnAfterSignInListener(new OnAfterSignInListener() {
//			
//			@Override
//			public void OnAfterSignIn(SNSUserInfo userInfo) {
//
//				try {
//					TwitterUserInfo twitterUserInfo = (TwitterUserInfo) userInfo;
//					twitterUserInfo.printSNSUserInfo();
//					
//					signUpWithSNS("" + twitterUserInfo.id, "twitter", twitterUserInfo.screenName);
//				} catch (Exception e) {
//					LogUtils.trace(e);
//					tf.logout();
//				}
//			}
//		});
//	    
//		NaverFragment nf = new NaverFragment() {
//			
//			@Override
//			protected int getLoginImageResId() {
//				
//				return R.drawable.btn_loginn_01;
//			}
//
//			@Override
//			public String getOAuthClientId() {
//
//				return getString(R.string.naver_client_id);
//			}
//
//			@Override
//			public String getOAuthClientSecret() {
//
//				return getString(R.string.naver_client_secret);
//			}
//
//			@Override
//			public String getOAuthClientName() {
//				
//				return getString(R.string.app_name);
//			}
//
//			@Override
//			public String getOAuthCallbackUrl() {
//
//				return null;
//			}
//		};
//	    getSupportFragmentManager().beginTransaction().add(nvFrame.getId(), nf, "nvFragment").commit();
//		nf.setOnAfterSignInListener(new OnAfterSignInListener() {
//			
//			@Override
//			public void OnAfterSignIn(SNSUserInfo userInfo) {
//
//				try {
//					NaverUserInfo naverUserInfo = (NaverUserInfo) userInfo;
//					naverUserInfo.printSNSUserInfo();
//				} catch (Exception e) {
//					LogUtils.trace(e);
//				}
//			}
//		});
	}

	@Override
	public void setSizes() {
		
		RelativeLayout.LayoutParams rp = null;
		int width = ResizeUtils.getSpecificLength(308);
		int height = ResizeUtils.getSpecificLength(70);
		int margin = ResizeUtils.getSpecificLength(8);
		int length = ResizeUtils.getSpecificLength(150);
		
		//tvNSeries.
		rp = (RelativeLayout.LayoutParams) tvNSeries.getLayoutParams();
		rp.height = height;
		rp.topMargin = height;
		
		//btnSignUp.
		rp = (RelativeLayout.LayoutParams) btnSignUp.getLayoutParams();
		rp.width = width;
		rp.height = height;
		
		//etId.
		rp = (RelativeLayout.LayoutParams) etId.getLayoutParams();
		rp.width = width;
		rp.height = height;
		
		//etPw.
		rp = (RelativeLayout.LayoutParams) etPw.getLayoutParams();
		rp.width = width;
		rp.height = height;
		rp.topMargin = margin;
		
		//btnSignIn.
		rp = (RelativeLayout.LayoutParams) btnSignIn.getLayoutParams();
		rp.width = width;
		rp.height = length;
		rp.topMargin = margin;
		
		//btnFindIdAndPw.
		rp = (RelativeLayout.LayoutParams) btnFindIdAndPw.getLayoutParams();
		rp.width = width;
		rp.height = height;
		rp.topMargin = margin;
		
//		//tvSNSLogin.
//		rp = (RelativeLayout.LayoutParams) tvSNSLogin.getLayoutParams();
//		rp.height = height;
//		rp.topMargin = margin;
//		
//		//fbFrame.
//		rp = (RelativeLayout.LayoutParams) fbFrame.getLayoutParams();
//		rp.width = length;
//		rp.height = length;
//		rp.topMargin = margin;
//		
//		//kkFrame.
//		rp = (RelativeLayout.LayoutParams) kkFrame.getLayoutParams();
//		rp.width = length;
//		rp.height = length;
//		rp.leftMargin = margin;
//		
//		//twFrame.
//		rp = (RelativeLayout.LayoutParams) twFrame.getLayoutParams();
//		rp.width = length;
//		rp.height = length;
//		rp.topMargin = margin;
//		
//		//nvFrame.
//		rp = (RelativeLayout.LayoutParams) nvFrame.getLayoutParams();
//		rp.width = length;
//		rp.height = length;
//		rp.leftMargin = margin;
//		
//		//tvSNSWarning.
//		rp = (RelativeLayout.LayoutParams) tvSNSWarning.getLayoutParams();
//		rp.height = height;
		
		FontUtils.setFontSize(tvNSeries, 24);
		FontUtils.setFontSize(btnSignUp, 20);
		FontUtils.setFontSize(etId.getEditText(), 22);
		FontUtils.setFontSize(etPw.getEditText(), 22);
		FontUtils.setFontSize(btnFindIdAndPw, 20);
//		FontUtils.setFontSize(tvSNSLogin, 22);
//		FontUtils.setFontSize(tvSNSWarning, 20);
	}

	@Override
	public void setListeners() {
		
		btnSignUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				launchToSignUpActivity();
			}
		});
		
		btnSignIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

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
						
						LogUtils.log("###oasl.OnAfterSigningIn.  s? : " + successSignIn);
						
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

		btnFindIdAndPw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				webBrowser.open(ZoneConstants.URL_FOR_FIND_ID_AND_PW, null);
			}
		});
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
		
		SoftKeyboardUtils.hideKeyboard(this, tvNSeries);
		super.finish();
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

	@Override
	public String getCookieName_D1() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getCookieName_S() {
		// TODO Auto-generated method stub
		return null;
	}
	
///////////// Custom methods.

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

//	public void signUpWithSNS(String id, String type, String nickname) {
//
//		//?sns_id=snsid22&sns_type=facebook&sns_nickname=snsNick22&sb_id=golfn&image_size=
//		String url = ZoneConstants.BASE_URL + "auth/login/sns" +
//				"?sns_id=" + id +
//				"&sns_type=" + type + 
//				"&sns_nickname=" + StringUtils.getUrlEncodedString(nickname) +
//				"&sb_id=" + ZoneConstants.PAPP_ID +
//				"&image_size=640";
//		
//		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {
//
//			@Override
//			public void onError(String url) {
//
//				LogUtils.log("SignInActivity.signUpWithSNS.onError." + "\nurl : " + url);
//			}
//
//			@Override
//			public void onCompleted(String url, JSONObject objJSON) {
//
//				try {
//					LogUtils.log("SignInActivity.signUpWithSNS.onCompleted." + "\nurl : " + url
//							+ "\nresult : " + objJSON);
//					
//					try {
//						if(!objJSON.has("data")
//								|| !objJSON.has("errorMsg") 
//								|| StringUtils.isEmpty(objJSON.getString("errorMsg"))
//								|| objJSON.getInt("errorCode") != 1) {
//							return;
//						}
//						
//						ZonecommsApplication.myInfo = new MyInfo();
//						ZonecommsApplication.myInfo.setUserInfo(objJSON);
//						
//						String id = ZonecommsApplication.myInfo.getMember_id();
//						SharedPrefsUtils.addDataToPrefs(ZoneConstants.PREFS_SIGN, "id", id);
//						SharedPrefsUtils.addDataToPrefs(ZoneConstants.PREFS_SIGN, "pw", id);
//						
//						setResult(RESULT_OK);
//						finish();
//					} catch(Exception e) {
//						LogUtils.trace(e);
//					}
//				} catch (Exception e) {
//					LogUtils.trace(e);
//				} catch (OutOfMemoryError oom) {
//					LogUtils.trace(oom);
//				}
//			}
//		});
//	}
	
///////////////// Interfaces.

	/**
	 * Using at Auto sign in.
	 * @author HyungGunKim
	 */
	public interface OnAfterSigningInListener {

		public void OnAfterSigningIn(boolean successSignIn);
	}
}