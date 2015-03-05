package com.byecar.fragments.dealer;

import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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

public class SignUpForDealerPage extends BCPFragment {

	private static final int NAME_MIN = 1;
	private static final int NAME_MAX = 30;
	private static final int BIRTHDATE_MIN = 8;
	private static final int BIRTHDATE_MAX = 8;
	
	public static String phone_auth_key;
	public static String phone_number;
	
	private TextView tvCertifyPhoneNumberText;
	private TextView tvCertified;
	private Button btnCertifyPhoneNumber;
	
	private TextView tvInfoText;
	private Button[] btnImages;
	private ImageView[] ivImages;
	private TextView tvUploadText;
	private HoloStyleEditText[] etInfos;
	
	private TextView tvIntroduceText;
	private EditText etIntroduce;
	private TextView tvTermOfUse;
	private Button btnTermOfUse;
	private Button btnSignUp;
	
	private int selectedImageIndex;
	private String[] selectedImageSdCardPaths = new String[2];
	
	private String email;
	private String pw;
//	private String nickname;
	private String profile_img_url;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		BaseFragmentActivity.onAfterPickImageListener = new OnAfterPickImageListener() {
			
			@Override
			public void onAfterPickImage(String[] sdCardPaths, Bitmap[] thumbnails) {
				
				if(thumbnails != null && thumbnails.length > 0) {
					ivImages[selectedImageIndex].setImageBitmap(thumbnails[0]);
					selectedImageSdCardPaths[selectedImageIndex] = sdCardPaths[0];
				}
			}
		};
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.signUpForDealerPage_titleBar);
		
		tvCertifyPhoneNumberText = (TextView) mThisView.findViewById(R.id.signUpForDealerPage_tvCertifyPhoneNumberText);
		tvCertified = (TextView) mThisView.findViewById(R.id.signUpForDealerPage_tvCertified);
		btnCertifyPhoneNumber = (Button) mThisView.findViewById(R.id.signUpForDealerPage_btnCertifyPhoneNumber);
		
		tvInfoText = (TextView) mThisView.findViewById(R.id.signUpForDealerPage_tvInfoText);
		
		btnImages = new Button[2];
		btnImages[0] = (Button) mThisView.findViewById(R.id.signUpForDealerPage_btnImage1);
		btnImages[1] = (Button) mThisView.findViewById(R.id.signUpForDealerPage_btnImage2);
		
		ivImages = new ImageView[2];
		ivImages[0] = (ImageView) mThisView.findViewById(R.id.signUpForDealerPage_ivImage1);
		ivImages[1] = (ImageView) mThisView.findViewById(R.id.signUpForDealerPage_ivImage2);
		
		tvUploadText = (TextView) mThisView.findViewById(R.id.signUpForDealerPage_tvUploadText);
		
		etInfos = new HoloStyleEditText[6];
		etInfos[0] = (HoloStyleEditText) mThisView.findViewById(R.id.signUpForDealerPage_etInfo1);
		etInfos[1] = (HoloStyleEditText) mThisView.findViewById(R.id.signUpForDealerPage_etInfo2);
		etInfos[2] = (HoloStyleEditText) mThisView.findViewById(R.id.signUpForDealerPage_etInfo3);
		etInfos[3] = (HoloStyleEditText) mThisView.findViewById(R.id.signUpForDealerPage_etInfo4);
		etInfos[4] = (HoloStyleEditText) mThisView.findViewById(R.id.signUpForDealerPage_etInfo5);
		etInfos[5] = (HoloStyleEditText) mThisView.findViewById(R.id.signUpForDealerPage_etInfo6);
		
		tvIntroduceText = (TextView) mThisView.findViewById(R.id.signUpForDealerPage_tvIntroduce);
		etIntroduce = (EditText) mThisView.findViewById(R.id.signUpForDealerPage_etIntroduce);
		btnTermOfUse = (Button) mThisView.findViewById(R.id.signUpForDealerPage_btnTermOfUse);
		tvTermOfUse = (TextView) mThisView.findViewById(R.id.signUpForDealerPage_tvTermOfUse);
		btnSignUp = (Button) mThisView.findViewById(R.id.signUpForDealerPage_btnSignUp);
	}

	@Override
	public void setVariables() {
		
		if(getArguments() != null) {
			email = getArguments().getString("user[email]");
			pw = getArguments().getString("user[pw]");
//			nickname = getArguments().getString("user[nickname]");
			profile_img_url = getArguments().getString("user[profile_img_url]");
		}
		
		for(int i=0; i<selectedImageSdCardPaths.length; i++) {
			selectedImageSdCardPaths[i] = null;
		}
	}

	@Override
	public void createPage() {

		int[] hintTextResIds = new int[] {
			R.string.hintForName,
			R.string.hintForBirthDate,
			R.string.hintForAddress,
			R.string.hintForAssociation,
			R.string.hintForComplex,
			R.string.hintForCompany,
		};
		
		int size = etInfos.length;
		for(int i=0; i<size; i++) {
			etInfos[i].getEditText().setTextColor(getResources().getColor(R.color.holo_text));
			etInfos[i].getEditText().setHintTextColor(getResources().getColor(R.color.holo_text_hint));
			etInfos[i].setHint(hintTextResIds[i]);
			etInfos[i].getEditText().setSingleLine();
			
			if(i == 1) {
				etInfos[i].getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
			} else {
				etInfos[i].getEditText().setInputType(InputType.TYPE_CLASS_TEXT);
			}
		}
	}

	@Override
	public void setListeners() {
		
		setImageViewsOnClickListener();
		
		tvCertified.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putBoolean("forDealerSignUp", true);
				mActivity.showPage(BCPConstants.PAGE_CERTIFY_PHONE_NUMBER, bundle);
			}
		});

		btnCertifyPhoneNumber.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putBoolean("forDealerSignUp", true);
				mActivity.showPage(BCPConstants.PAGE_CERTIFY_PHONE_NUMBER, bundle);
			}
		});
		
		btnTermOfUse.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(BCPConstants.PAGE_TERM_OF_USE, null);
			}
		});
		
		btnSignUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(checkInformation()) {
					uploadImages();
				}
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		
		//tvCertifyPhoneNumberText.
		rp = (RelativeLayout.LayoutParams) tvCertifyPhoneNumberText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		rp.topMargin = ResizeUtils.getSpecificLength(25);
		
		//tvCertified.
		rp = (RelativeLayout.LayoutParams) tvCertified.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(592);
		rp.height = ResizeUtils.getSpecificLength(225);
		
		//btnCertifyPhoneNumber.
		rp = (RelativeLayout.LayoutParams) btnCertifyPhoneNumber.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(160);
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.topMargin = ResizeUtils.getSpecificLength(18);
		rp.rightMargin = ResizeUtils.getSpecificLength(25);
		
		for(int i=0; i<2; i++) {

			//btnImages.
			rp = (RelativeLayout.LayoutParams) btnImages[i].getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(257);
			rp.height = ResizeUtils.getSpecificLength(157);
			
			if(i == 0) {
				rp.leftMargin = ResizeUtils.getSpecificLength(52);
				rp.topMargin = ResizeUtils.getSpecificLength(13);
			} else {
				rp.leftMargin = ResizeUtils.getSpecificLength(24);
			}
			
			//ivImages.
			rp = (RelativeLayout.LayoutParams) ivImages[i].getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(257);
			rp.height = ResizeUtils.getSpecificLength(157);
		}
		
		//tvUploadText.
		rp = (RelativeLayout.LayoutParams) tvUploadText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(80);
		
		//etInfos.
		int size = etInfos.length;
		for(int i=0; i<size; i++) {
			rp = (RelativeLayout.LayoutParams) etInfos[i].getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(586);
			rp.height = ResizeUtils.getSpecificLength(60);
			
			if(i != 0) {
				rp.topMargin = ResizeUtils.getSpecificLength(32);
			}
			
			FontUtils.setFontAndHintSize(etInfos[i], 26, 20);
		}
		
		//tvIntroduceText.
		rp = (RelativeLayout.LayoutParams) tvIntroduceText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		
		//etIntroduce.
		rp = (RelativeLayout.LayoutParams) etIntroduce.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(160);
		
		//tvTermOfUse.
		rp = (RelativeLayout.LayoutParams) tvTermOfUse.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		rp.topMargin = ResizeUtils.getSpecificLength(40);
		
		//btnTermOfUse.
		rp = (RelativeLayout.LayoutParams) btnTermOfUse.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(140);
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.rightMargin = ResizeUtils.getSpecificLength(26);
		
		//btnSignUp.
		rp = (RelativeLayout.LayoutParams) btnSignUp.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(82);
		rp.topMargin = ResizeUtils.getSpecificLength(36);
		
		FontUtils.setFontSize(tvCertifyPhoneNumberText, 30);
		FontUtils.setFontSize(tvCertified, 30);
		
		FontUtils.setFontSize(tvInfoText, 30);
		FontUtils.setFontSize(tvUploadText, 20);
		
		FontUtils.setFontSize(tvIntroduceText, 30);
		FontUtils.setFontAndHintSize(etIntroduce, 26, 20);
		FontUtils.setFontSize(tvTermOfUse, 20);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_sign_up_for_dealer;
	}

	@Override
	public int getBackButtonResId() {

		return R.drawable.d_signin_back2_btn;
	}

	@Override
	public int getBackButtonWidth() {

		return 214;
	}

	@Override
	public int getBackButtonHeight() {

		return 60;
	}

	@Override
	public int getRootViewResId() {

		return R.id.signUpForDealerPage_mainLayout;
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
	public void onResume() {
		super.onResume();
		
		if(StringUtils.isEmpty(phone_auth_key)) {
			tvCertified.setTextColor(getResources().getColor(R.color.color_red));
			tvCertified.setText(R.string.requireCertifyPhoneNumber);
			tvCertified.setBackgroundResource(R.drawable.registration_box1);
			btnCertifyPhoneNumber.setVisibility(View.VISIBLE);
		} else {
			tvCertified.setTextColor(getResources().getColor(R.color.color_green));
			tvCertified.setText(phone_number + "\n"
					+ getString(R.string.phoneNumberCertified));
			tvCertified.setBackgroundResource(R.drawable.registration_box2);
			btnCertifyPhoneNumber.setVisibility(View.INVISIBLE);
		}
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		
		phone_number = null;
		phone_auth_key = null;
	}
	
//////////////////// Custom methods.

	public void setImageViewsOnClickListener() {
		
		int size = btnImages.length;
		for(int i=0; i<size; i++) {
			
			final int INDEX = i;
			btnImages[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					
					selectedImageIndex = INDEX;
					mActivity.showUploadPhotoPopup(1, Color.rgb(254, 188, 42));
				}
			});
		}
	}
	
	public boolean checkInformation() {

		if(btnCertifyPhoneNumber.getVisibility() == View.VISIBLE) {
			ToastUtils.showToast(R.string.checkToCertifyPhoneNumber);

		} else if(StringUtils.isEmpty(selectedImageSdCardPaths[0])) {
			ToastUtils.showToast(R.string.checkEmployeeCardImage);
		
		} else if(StringUtils.isEmpty(selectedImageSdCardPaths[1])) {
			ToastUtils.showToast(R.string.checkNameCardImage);
		
		} else if(StringUtils.checkForbidContains(etInfos[0].getEditText(), false, false, false, false, true, true)
				|| StringUtils.checkTextLength(etInfos[0].getEditText(), NAME_MIN, NAME_MAX) != StringUtils.PASS) {
			ToastUtils.showToast(R.string.hintForName);
		
		//숫자만 허용.
		} else if(StringUtils.checkForbidContains(etInfos[1].getEditText(), true, true, false, true, true, true)
				|| StringUtils.checkTextLength(etInfos[1].getEditText(), BIRTHDATE_MIN, BIRTHDATE_MAX) != StringUtils.PASS) {
			ToastUtils.showToast(R.string.hintForBirthDate);
		
		} else if(StringUtils.isEmpty(etInfos[2].getEditText())) {
			ToastUtils.showToast(R.string.hintForAddress);
		
		} else if(StringUtils.isEmpty(etInfos[3].getEditText())) {
			ToastUtils.showToast(R.string.hintForAssociation);
		
		} else if(StringUtils.isEmpty(etInfos[4].getEditText())) {
			ToastUtils.showToast(R.string.hintForComplex);
		
		} else if(StringUtils.isEmpty(etInfos[5].getEditText())) {
			ToastUtils.showToast(R.string.hintForCompany);
		
		} else if(StringUtils.isEmpty(etIntroduce)) {
			ToastUtils.showToast(R.string.hintForIntroduce);
		
		} else {
			return true;
		}
		
		return false;
	}

	public void uploadImages() {
		
		int size = selectedImageSdCardPaths.length;
		
		for(int i=0; i<size; i++) {
			
			if(!StringUtils.isEmpty(selectedImageSdCardPaths[i])
					&& !selectedImageSdCardPaths[i].contains("http://")) {
				
				final int INDEX = i;
				
				OnAfterUploadImage oaui = new OnAfterUploadImage() {
					
					@Override
					public void onAfterUploadImage(String resultString) {

						try {
							selectedImageSdCardPaths[INDEX] = new JSONObject(resultString).getJSONObject("file").getString("url");
						} catch (Exception e) {
							LogUtils.trace(e);
						} catch (Error e) {
							LogUtils.trace(e);
						}
						
						//Recursive call.
						uploadImages();
					}
				};
				
				ImageUploadUtils.uploadImage(BCPAPIs.UPLOAD_URL, oaui, selectedImageSdCardPaths[i]);
				return;
			}
		}
		
		signUp();
	}
	
	public void signUp() {

		/*
		String url = BCPAPIs.SIGN_UP_URL
		+ "?user[email]=" + StringUtils.getUrlEncodedString(etEmail.getEditText())
		+ "&user[pw]=" + StringUtils.getUrlEncodedString(etPw.getEditText())
		+ "&user[nickname]=" + StringUtils.getUrlEncodedString(etNickname.getEditText())
		+ "&user[profile_img_url]=" + StringUtils.getUrlEncodedString(selectedImageUrl);
		*/
		
		try {
			StringBuilder sb = new StringBuilder(BCPAPIs.SIGN_UP_URL);
			sb.append("?user[email]=").append(email)
					.append("&user[pw]=").append(pw)
//					.append("&user[nickname]=").append(nickname)
					.append("&user[name]=").append(StringUtils.getUrlEncodedString(etInfos[0].getEditText()))
					.append("&user[address]=").append(StringUtils.getUrlEncodedString(etInfos[2].getEditText()))
					.append("&dealer[birthdate]=").append(StringUtils.getUrlEncodedString(etInfos[1].getEditText()))
					.append("&dealer[association]=").append(StringUtils.getUrlEncodedString(etInfos[3].getEditText()))
					.append("&dealer[complex]=").append(StringUtils.getUrlEncodedString(etInfos[4].getEditText()))
					.append("&dealer[company]=").append(StringUtils.getUrlEncodedString(etInfos[5].getEditText()))
					.append("&user[desc]=").append(StringUtils.getUrlEncodedString(etIntroduce))
					.append("&phone_auth_key=").append(phone_auth_key)
					.append("&dealer[employee_card_img_url]=").append(StringUtils.getUrlEncodedString(selectedImageSdCardPaths[0]))
					.append("&dealer[name_card_img_url]=").append(StringUtils.getUrlEncodedString(selectedImageSdCardPaths[1]));
			
			if(!StringUtils.isEmpty(profile_img_url)) {
				sb.append("&user[profile_img_url]=").append(profile_img_url);
			}
			
			String url = sb.toString();
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("SignUpForDealerPage.onError." + "\nurl : " + url);
					ToastUtils.showToast(R.string.failToSignUp);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("SignUpForDealerPage.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						if(objJSON.getInt("result") == 2) {
							mActivity.clearFragments(true);
						}
						
						ToastUtils.showToast(objJSON.getString("message"));
					} catch (Exception e) {
						LogUtils.trace(e);
						ToastUtils.showToast(R.string.failToSignUp);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
						ToastUtils.showToast(R.string.failToSignUp);
					}
				}
			}, mActivity.getLoadingView());
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
}
