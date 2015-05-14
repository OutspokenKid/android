package com.byecar.fragments;

import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplusfordealer.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPFragment;
import com.byecar.views.TitleBar;
import com.outspoken_kid.activities.BaseFragmentActivity;
import com.outspoken_kid.activities.MultiSelectGalleryActivity.OnAfterPickImageListener;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ImageUploadUtils;
import com.outspoken_kid.utils.ImageUploadUtils.OnAfterUploadImage;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo.holo_light.HoloStyleEditText;

public class SignUpPage extends BCPFragment {

	private static final int PASSWORD_MIN = 8;
	private static final int PASSWORD_MAX = 15;
	
	private TextView tvProfileTitle;
	private Button btnProfile;
	private ImageView ivProfile;
	private TextView tvProfile;
	private TextView tvCommonInfo;
	private HoloStyleEditText etEmail;
	private TextView tvCheckEmail;
	private HoloStyleEditText etPw;
	private TextView tvCheckPw;
	private HoloStyleEditText etPwConfirm;
	private TextView tvCheckPwConfirm;
	private Button btnSignUp;
	
	private boolean focusOnEmail, focusOnPw;
	private boolean passEmail, passPw, passPwConfirm;
	
	private String selectedSdCardPath;
	private String selectedImageUrl;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		BaseFragmentActivity.onAfterPickImageListener = new OnAfterPickImageListener() {
			
			@Override
			public void onAfterPickImage(String[] sdCardPaths, Bitmap[] thumbnails) {
				
				if(thumbnails != null && thumbnails.length > 0) {
					ivProfile.setImageBitmap(thumbnails[0]);
					selectedSdCardPath = sdCardPaths[0];
				}
			}
		};
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.signUpForCommonPage_titleBar);
		
		tvProfileTitle = (TextView) mThisView.findViewById(R.id.signUpForCommonPage_tvProfileTitle);
		btnProfile = (Button) mThisView.findViewById(R.id.signUpForCommonPage_btnProfile);
		ivProfile = (ImageView) mThisView.findViewById(R.id.signUpForCommonPage_ivProfile);
		tvProfile = (TextView) mThisView.findViewById(R.id.signUpForCommonPage_tvProfile);
		tvCommonInfo = (TextView) mThisView.findViewById(R.id.signUpForCommonPage_tvCommonInfo);
		etEmail = (HoloStyleEditText) mThisView.findViewById(R.id.signUpForCommonPage_etEmail);
		tvCheckEmail = (TextView) mThisView.findViewById(R.id.signUpForCommonPage_tvCheckEmail);
		etPw = (HoloStyleEditText) mThisView.findViewById(R.id.signUpForCommonPage_etPw);
		tvCheckPw = (TextView) mThisView.findViewById(R.id.signUpForCommonPage_tvCheckPw);
		etPwConfirm = (HoloStyleEditText) mThisView.findViewById(R.id.signUpForCommonPage_etPwConfirm);
		tvCheckPwConfirm = (TextView) mThisView.findViewById(R.id.signUpForCommonPage_tvCheckPwConfirm);
		btnSignUp = (Button) mThisView.findViewById(R.id.signUpForCommonPage_btnSignUp);
	}

	@Override
	public void setVariables() {
	}

	@Override
	public void createPage() {

		tvProfileTitle.setText("1. " + getString(R.string.profileImage));
		tvCommonInfo.setText("2. " + getString(R.string.commonInfo));
		
		etEmail.setHint(R.string.hintForEmailSignIn);
		etEmail.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		
		etPw.setHint(R.string.hintForPassword2);
		etPw.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
		
		etPwConfirm.setHint(R.string.hintForPasswordConfirm);
		etPwConfirm.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
	}

	@Override
	public void setListeners() {

		btnProfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showUploadPhotoPopup(1, Color.rgb(254, 188, 42));
			}
		});

		etEmail.getEditText().setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				//포커스를 갖고 있다가 잃은 시점.
				if(focusOnEmail && !hasFocus) {
					checkEmail();
				}
				
				checkEmailDuplication();
				focusOnEmail = hasFocus;
			}
		});
		
		etPw.getEditText().setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				//포커스를 갖고 있다가 잃은 시점.
				if(focusOnPw && !hasFocus) {
					checkPw();
				}
				
				focusOnPw = hasFocus;
			}
		});

		etPwConfirm.getEditText().addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				checkPwConfirm();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {}
		});
		
		btnSignUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(checkInfos()) {
					
					if(!StringUtils.isEmpty(selectedSdCardPath)) {
						uploadImage();
					} else {
						next();
					}
				}
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		
		int width = ResizeUtils.getSpecificLength(586);
		int textViewHeight = ResizeUtils.getSpecificLength(60);
		int buttonHeight = ResizeUtils.getSpecificLength(82);

		//tvProfileTitle.
		rp = (RelativeLayout.LayoutParams) tvProfileTitle.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(41);
		tvProfileTitle.setPadding(ResizeUtils.getSpecificLength(20), 0, 0, 0);
		
		//profileFrame
		rp = (RelativeLayout.LayoutParams) (mThisView.findViewById(R.id.signUpForCommonPage_profileFrame)).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(219);
		rp.height = ResizeUtils.getSpecificLength(219);
		rp.topMargin = ResizeUtils.getSpecificLength(60);
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);

		//tvCommonInfo.
		rp = (RelativeLayout.LayoutParams) tvCommonInfo.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(41);
		rp.topMargin = ResizeUtils.getSpecificLength(60);
		rp.bottomMargin = ResizeUtils.getSpecificLength(30);
		tvCommonInfo.setPadding(ResizeUtils.getSpecificLength(20), 0, 0, 0);
		
		//etEmail.
		rp = (RelativeLayout.LayoutParams) etEmail.getLayoutParams();
		rp.width = width;
		rp.height = textViewHeight;
		rp.bottomMargin = ResizeUtils.getSpecificLength(30);
		
		//etPw.
		rp = (RelativeLayout.LayoutParams) etPw.getLayoutParams();
		rp.width = width;
		rp.height = textViewHeight;
		rp.bottomMargin = ResizeUtils.getSpecificLength(30);
		
		//etPwConfirm.
		rp = (RelativeLayout.LayoutParams) etPwConfirm.getLayoutParams();
		rp.width = width;
		rp.height = textViewHeight;
		
		//btnSignUp.
		rp = (RelativeLayout.LayoutParams) btnSignUp.getLayoutParams();
		rp.width = width;
		rp.height = buttonHeight;
		rp.topMargin = ResizeUtils.getSpecificLength(850);
		rp.bottomMargin = ResizeUtils.getSpecificLength(30);
		
		FontUtils.setFontSize(tvProfileTitle, 24);
		FontUtils.setFontStyle(tvProfileTitle, FontUtils.BOLD);
		FontUtils.setFontSize(tvProfile, 20);
		FontUtils.setFontSize(tvCommonInfo, 24);
		FontUtils.setFontStyle(tvCommonInfo, FontUtils.BOLD);
		FontUtils.setFontAndHintSize(etEmail.getEditText(), 24, 20);
		FontUtils.setFontAndHintSize(etPw.getEditText(), 24, 20);
		FontUtils.setFontAndHintSize(etPwConfirm.getEditText(), 24, 20);
		FontUtils.setFontSize(tvCheckEmail, 20);
		FontUtils.setFontSize(tvCheckPw, 20);
		FontUtils.setFontSize(tvCheckPwConfirm, 20);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_sign_up_for_common;
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
	public int getPageTitleTextResId() {

		return R.string.pageTitle_signUp;
	}

	@Override
	public int getRootViewResId() {

		return R.id.signUpForCommonPage_mainLayout;
	}
	
//////////////////// Custom methods.

	public boolean checkInfos() {

		if(!checkEmail()) {
			ToastUtils.showToast(R.string.checkEmail);
			
		} else if(!checkPw()) {
			ToastUtils.showToast(R.string.checkPassword);
			
		} else if(!checkPwConfirm()) {
			ToastUtils.showToast(R.string.checkPasswordConfirm);
		} else {
			return true;
		}
		
		return false;
	}
	
	public boolean checkEmail() {
		
		if(!StringUtils.isValidEmail(etEmail.getEditText())) {
			tvCheckEmail.setText(R.string.checkEmail);
			tvCheckEmail.setVisibility(View.VISIBLE);
			passEmail = false;
		} else{
			tvCheckEmail.setVisibility(View.INVISIBLE);
			passEmail = true;
		}
		
		return passEmail;
	}
	
	public void checkEmailDuplication() {
		
		String url = BCPAPIs.EMAIL_CHECK_URL + "?email=" + etEmail.getEditText().getText().toString();
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("SignUpForCommconPage.onError." + "\nurl : " + url);
				
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("SignUpForCommconPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") != 1) {
						tvCheckEmail.setVisibility(View.VISIBLE);
						tvCheckEmail.setText(objJSON.getString("message"));
						passEmail = false;
					} else {
						passEmail = true;
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public boolean checkPw() {
		
		if(StringUtils.checkTextLength(etPw.getEditText(), PASSWORD_MIN, PASSWORD_MAX)
				!= StringUtils.PASS
				|| StringUtils.checkForbidContains(etPw.getEditText(), false, true, false, true, true, false)) {
			tvCheckPw.setVisibility(View.VISIBLE);
			passPw = false;
		} else{
			tvCheckPw.setVisibility(View.INVISIBLE);
			passPw = true;
		}
		
		return passPw;
	}
	
	public boolean checkPwConfirm() {
		
		if(etPw.getEditText().getText() != null 
				&& etPwConfirm.getEditText().getText() != null
				&& !etPw.getEditText().getText().toString().equals(etPwConfirm.getEditText().getText().toString())) {
			tvCheckPwConfirm.setVisibility(View.VISIBLE);
			passPwConfirm = false;
		} else{
			tvCheckPwConfirm.setVisibility(View.INVISIBLE);
			passPwConfirm = true;
		}
		
		return passPwConfirm;
	}
	
	public void uploadImage() {
		
		ToastUtils.showToast(R.string.uploadingImage);
		
		OnAfterUploadImage oaui = new OnAfterUploadImage() {
			
			@Override
			public void onAfterUploadImage(String resultString) {
				
				try {
					selectedImageUrl = new JSONObject(resultString).getJSONObject("file").getString("url"); 
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}

				next();
			}
		};
		
		ImageUploadUtils.uploadImage(BCPAPIs.UPLOAD_URL, oaui, selectedSdCardPath);
	}
	
	public void next() {
		
		Bundle bundle = new Bundle();
		bundle.putString("profile_img_url", selectedImageUrl);
		bundle.putString("email", etEmail.getEditText().getText().toString());
		bundle.putString("pw", etPw.getEditText().getText().toString());
		mActivity.showPage(BCPConstants.PAGE_SIGN_UP_FOR_DEALER, bundle);
	}
}
