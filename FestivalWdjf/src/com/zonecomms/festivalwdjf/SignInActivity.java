package com.zonecomms.festivalwdjf;

import java.net.URLEncoder;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.outspoken_kid.activities.RecyclingActivity;
import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader.OnCompletedListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.WebBrowser;
import com.outspoken_kid.views.holo_dark.HoloStyleButton;
import com.outspoken_kid.views.holo_dark.HoloStyleEditText;
import com.zonecomms.common.models.MyInfo;
import com.zonecomms.common.utils.AppInfoUtils;
import com.zonecomms.festivalwdjf.classes.ZoneConstants;

public class SignInActivity extends RecyclingActivity {
	
	private static SignInActivity signInActivity;

	private LinearLayout innerLinear;
	private View logo;
	private HoloStyleEditText etId;
	private HoloStyleEditText etPw;
	private WebBrowser webBrowser;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
		
		bindViews();
		setVariables();
		createPage();
		setSizes();
		setListeners();
		
		signInActivity = this;
	}

	@Override
	protected void bindViews() {
		innerLinear = (LinearLayout) findViewById(R.id.signInActivity_innerLinear);
		webBrowser = (WebBrowser) findViewById(R.id.signInActivity_webBrowser);
	}
	
	@Override
	protected void setVariables() {
	}

	@Override
	protected void createPage() {
		
		addViewsForInnerLinear();
	}

	@Override
	protected void setSizes() {
	}

	@Override
	protected void setListeners() {

	}
	
	@Override
	protected void downloadInfo() {
	}

	@Override
	protected void setPage() {
	}
	
	@Override
	protected int getContentViewId() {
		return R.id.signInActivity_mainLayout;
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
		
		SoftKeyboardUtils.hideKeyboard(this, etId);
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
		FontInfo.setFontSize(etId.getEditText(), 20);
		etId.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		innerLinear.addView(etId);
		
		etPw = new HoloStyleEditText(this);
		ResizeUtils.viewResize(500, 70, etPw, 1, Gravity.CENTER_HORIZONTAL, new int[]{0, 20, 0, 30});
		etPw.setHint(R.string.hintForPw);
		FontInfo.setFontSize(etPw.getEditText(), 20);
		etPw.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
		etPw.getEditText().setTransformationMethod(PasswordTransformationMethod.getInstance());
		innerLinear.addView(etPw);
		
		TextView tvNId = new TextView(this);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, tvNId, 1, 0, null);
		tvNId.setGravity(Gravity.CENTER_HORIZONTAL);
		tvNId.setText(R.string.introduceNId);
		tvNId.setTextColor(Color.WHITE);
		FontInfo.setFontSize(tvNId, 32);
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
		FontInfo.setFontSize(btnSignIn.getTextView(), 25);
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
		FontInfo.setFontSize(btnSignUp.getTextView(), 25);
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
		FontInfo.setFontSize(btnFind.getTextView(), 25);
		innerLinear.addView(btnFind);
		
		View bottomBlank = new View(this);
		bottomBlank.setLayoutParams(new LinearLayout.LayoutParams(1, 0, 1));
		innerLinear.addView(bottomBlank);
	}

	public static void signIn(final String id, final String pw, final OnAfterSigningInListener onAfterSigningInListener) {
		
		AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
			
			@Override
			public void onErrorRaised(String url, Exception e) {

				LogUtils.log("SignInActivity.SignIn.onError.  url : " + url);
				
				if(onAfterSigningInListener != null) {
					onAfterSigningInListener.OnAfterSigningIn(false);
				}
			}
			
			@Override
			public void onCompleted(String url, String result) {

				LogUtils.log("SignInActivity.SignIn.onCompleted.  url : " + url + "\nresult : " + result);
				
				try {
					JSONObject objResult = new JSONObject(result);
					
					if(!objResult.has("data")
							|| !objResult.has("errorMsg") 
							|| StringUtils.isEmpty(objResult.getString("errorMsg"))
							|| objResult.getInt("errorCode") != 1) {
						
						if(onAfterSigningInListener != null) {
							onAfterSigningInListener.OnAfterSigningIn(false);
						}
						return;
					}
					
					MainActivity.myInfo = new MyInfo();
					MainActivity.myInfo.setUserInfo(objResult);
					
					SharedPrefsUtils.addDataToPrefs(ZoneConstants.PREFS_SIGN, "id", id);
					SharedPrefsUtils.addDataToPrefs(ZoneConstants.PREFS_SIGN, "pw", pw);

					if(onAfterSigningInListener != null) {
						onAfterSigningInListener.OnAfterSigningIn(true);	
					}
				} catch(Exception e) {
					e.printStackTrace();
					
					if(onAfterSigningInListener != null) {
						onAfterSigningInListener.OnAfterSigningIn(false);
					}
				}
			}
		};
		
		try {
			ToastUtils.showToast(R.string.signingIn);
			
			String url = ZoneConstants.BASE_URL + "auth/login" +
					"?id=" + URLEncoder.encode(id, "UTF-8") + 
					"&password=" + URLEncoder.encode(pw, "UTF-8") + 
					"&image_size=308" +
					"&" + AppInfoUtils.getAppInfo(AppInfoUtils.WITHOUT_MEMBER_ID);
			AsyncStringDownloader.download(url, null, ocl);
		} catch(Exception e) {
			e.printStackTrace();
			
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
	
	public void launchToMainActivity() {
		
		Intent intent = new Intent(this, MainActivity.class);
		Intent i = getIntent();				//'i' is intent that passed intent from before.
		
		if(i!= null) {
			if(i.getData() != null) {
				intent.setData(i.getData());
			}
		}
		
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		finish();
	}

///////////////// Interfaces.

	/**
	 * Using at Auto sign in.
	 * @author HyungGunKim
	 */
	public interface OnAfterSigningInListener {

		public void OnAfterSigningIn(boolean successSignIn);
	}
}