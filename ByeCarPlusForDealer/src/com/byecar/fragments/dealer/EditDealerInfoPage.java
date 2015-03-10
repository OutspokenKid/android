package com.byecar.fragments.dealer;

import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplusfordealer.MainActivity;
import com.byecar.byecarplusfordealer.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPFragment;
import com.byecar.models.Dealer;
import com.byecar.views.TitleBar;
import com.outspoken_kid.activities.BaseFragmentActivity;
import com.outspoken_kid.activities.MultiSelectGalleryActivity.OnAfterPickImageListener;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ImageUploadUtils;
import com.outspoken_kid.utils.ImageUploadUtils.OnAfterUploadImage;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo.holo_light.HoloStyleEditText;

public class EditDealerInfoPage extends BCPFragment {

	private static final int NAME_MIN = 1;
	private static final int NAME_MAX = 30;
//	private static final int NICKNAME_MIN = 3;
//	private static final int NICKNAME_MAX = 15;
	private static final int BIRTHDATE_MIN = 8;
	private static final int BIRTHDATE_MAX = 8;
	
	public static String PHONE_AUTH_KEY;
	public static String tempPhoneNumber;
	
	private Button[] btnImages;
	private ImageView[] ivImages;
	private TextView tvProfile;
	
	private TextView tvCertifyPhoneNumberText;
	private Button btnEditPhoneNumber;
	private TextView tvPhoneNumber;
	private View checkIcon;
	
	private TextView tvCommonInfoText;
	private HoloStyleEditText[] etInfos;
	
	private TextView tvAddedInfoText;
	private TextView tvUploadText;
	
	private TextView tvIntroduceText;
	private EditText etIntroduce;
	private Button btnComplete;
	
	private int selectedImageIndex;
	private String[] selectedImageSdCardPaths = new String[3];
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
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

		titleBar = (TitleBar) mThisView.findViewById(R.id.editDealerInfoPage_titleBar);
		
		btnImages = new Button[3];
		btnImages[0] = (Button) mThisView.findViewById(R.id.editDealerInfoPage_btnProfile);
		btnImages[1] = (Button) mThisView.findViewById(R.id.editDealerInfoPage_btnImage1);
		btnImages[2] = (Button) mThisView.findViewById(R.id.editDealerInfoPage_btnImage2);
		
		ivImages = new ImageView[3];
		ivImages[0] = (ImageView) mThisView.findViewById(R.id.editDealerInfoPage_ivProfile);
		ivImages[1] = (ImageView) mThisView.findViewById(R.id.editDealerInfoPage_ivImage1);
		ivImages[2] = (ImageView) mThisView.findViewById(R.id.editDealerInfoPage_ivImage2);
		
		tvProfile = (TextView) mThisView.findViewById(R.id.editDealerInfoPage_tvProfile);
		
		tvCertifyPhoneNumberText = (TextView) mThisView.findViewById(R.id.editDealerInfoPage_tvCertifyPhoneNumberText);
		btnEditPhoneNumber = (Button) mThisView.findViewById(R.id.editDealerInfoPage_btnEditPhoneNumber);
		tvPhoneNumber = (TextView) mThisView.findViewById(R.id.editDealerInfoPage_tvPhoneNumber);
		checkIcon = mThisView.findViewById(R.id.editDealerInfoPage_checkIcon);
		
		tvCommonInfoText = (TextView) mThisView.findViewById(R.id.editDealerInfoPage_tvCommonInfoText);
		
		etInfos = new HoloStyleEditText[7];
		etInfos[0] = (HoloStyleEditText) mThisView.findViewById(R.id.editDealerInfoPage_etCommonInfo1);
		etInfos[1] = (HoloStyleEditText) mThisView.findViewById(R.id.editDealerInfoPage_etCommonInfo2);
		etInfos[2] = (HoloStyleEditText) mThisView.findViewById(R.id.editDealerInfoPage_etCommonInfo3);
		etInfos[3] = (HoloStyleEditText) mThisView.findViewById(R.id.editDealerInfoPage_etAddedInfo1);
		etInfos[4] = (HoloStyleEditText) mThisView.findViewById(R.id.editDealerInfoPage_etAddedInfo2);
		etInfos[5] = (HoloStyleEditText) mThisView.findViewById(R.id.editDealerInfoPage_etAddedInfo3);
		etInfos[6] = (HoloStyleEditText) mThisView.findViewById(R.id.editDealerInfoPage_etAddedInfo4);
		
		tvAddedInfoText = (TextView) mThisView.findViewById(R.id.editDealerInfoPage_tvAddedInfoText);
		tvUploadText = (TextView) mThisView.findViewById(R.id.editDealerInfoPage_tvUploadText);
		
		tvIntroduceText = (TextView) mThisView.findViewById(R.id.editDealerInfoPage_tvIntroduceText);
		etIntroduce = (EditText) mThisView.findViewById(R.id.editDealerInfoPage_etIntroduce);
		btnComplete = (Button) mThisView.findViewById(R.id.editDealerInfoPage_btnComplete);
	}

	@Override
	public void setVariables() {
		
		PHONE_AUTH_KEY = "";

		for(int i=0; i<selectedImageSdCardPaths.length; i++) {
			selectedImageSdCardPaths[i] = null;
		}
	}

	@Override
	public void createPage() {

		etInfos[0].getEditText().setHint(R.string.hintForName);
		etInfos[1].getEditText().setHint(R.string.hintForNickname);
		etInfos[2].getEditText().setHint(R.string.hintForBirthDate);
		etInfos[3].getEditText().setHint(R.string.hintForAddress);
		etInfos[4].getEditText().setHint(R.string.hintForAssociation);
		etInfos[5].getEditText().setHint(R.string.hintForComplex);
		etInfos[6].getEditText().setHint(R.string.hintForCompany);
		
		setInfos();
		downloadImages();
	}

	@Override
	public void setListeners() {

		for(int i=0; i<3; i++) {
			final int I = i;
			btnImages[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					selectedImageIndex = I;
					mActivity.showUploadPhotoPopup(1, Color.rgb(254, 188, 42));
				}
			});
		}
		
		btnEditPhoneNumber.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putBoolean("forEditDealerInfo", true);
				mActivity.showPage(BCPConstants.PAGE_CERTIFY_PHONE_NUMBER, bundle);
			}
		});
		
		btnComplete.setOnClickListener(new OnClickListener() {

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

		//profileFrame
		rp = (RelativeLayout.LayoutParams) (mThisView.findViewById(R.id.editDealerInfoPage_profileFrame)).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(219);
		rp.height = ResizeUtils.getSpecificLength(219);
		rp.topMargin = ResizeUtils.getSpecificLength(60);
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);

		//tvCertifyPhoneNumberText.
		rp = (RelativeLayout.LayoutParams) (mThisView.findViewById(R.id.editDealerInfoPage_tvCertifyPhoneNumberText)).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);

		//btnEditPhoneNumber.
		rp = (RelativeLayout.LayoutParams) btnEditPhoneNumber.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(160);
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.topMargin = ResizeUtils.getSpecificLength(20);
		rp.rightMargin = ResizeUtils.getSpecificLength(26);
		
		//tvPhoneNumber.
		rp = (RelativeLayout.LayoutParams) tvPhoneNumber.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(95);
		
		//checkIcon.
		rp = (RelativeLayout.LayoutParams) checkIcon.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(24);
		rp.height = ResizeUtils.getSpecificLength(18);
		rp.topMargin = ResizeUtils.getSpecificLength(38);
		rp.rightMargin = ResizeUtils.getSpecificLength(10);
		
		//tvCommonInfoText.
		rp = (RelativeLayout.LayoutParams) tvCommonInfoText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		
		//etInfos.
		int size = etInfos.length;
		for(int i=0; i<size; i++) {
			
			if(i == 1) {
				continue;
			}
			
			rp = (RelativeLayout.LayoutParams) etInfos[i].getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(586);
			rp.height = ResizeUtils.getSpecificLength(60);
			
			if(i != 0) {
				rp.topMargin = ResizeUtils.getSpecificLength(32);
			}
			
			FontUtils.setFontAndHintSize(etInfos[i], 26, 20);
		}
		
		//tvAddedInfoText.
		rp = (RelativeLayout.LayoutParams) tvAddedInfoText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		
		for(int i=1; i<3; i++) {

			//btnImages.
			rp = (RelativeLayout.LayoutParams) btnImages[i].getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(257);
			rp.height = ResizeUtils.getSpecificLength(157);
			
			if(i == 1) {
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
		
		//tvIntroduceText.
		rp = (RelativeLayout.LayoutParams) tvIntroduceText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		
		//etIntroduce.
		rp = (RelativeLayout.LayoutParams) etIntroduce.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(160);
		
		//btnComplete.
		rp = (RelativeLayout.LayoutParams) btnComplete.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(82);
		
		FontUtils.setFontSize(tvProfile, 20);
		FontUtils.setFontSize(tvCertifyPhoneNumberText, 30);
		FontUtils.setFontSize(tvPhoneNumber, 30);
		FontUtils.setFontSize(tvCommonInfoText, 30);
		FontUtils.setFontSize(tvAddedInfoText, 30);
		FontUtils.setFontSize(tvUploadText, 20);
		FontUtils.setFontSize(tvIntroduceText, 30);
		FontUtils.setFontAndHintSize(etIntroduce, 26, 20);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_edit_dealer_info;
	}

	@Override
	public int getBackButtonResId() {

		return R.drawable.profile_back_btn;
	}

	@Override
	public int getBackButtonWidth() {

		return 236;
	}

	@Override
	public int getBackButtonHeight() {

		return 60;
	}

	@Override
	public int getRootViewResId() {

		return R.id.editDealerInfoPage_mainLayout;
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
		
		if(tempPhoneNumber != null) {
			tvPhoneNumber.setText(tempPhoneNumber);
		} else if(!StringUtils.isEmpty(MainActivity.user.getPhone_number())) {
			tvPhoneNumber.setText(MainActivity.user.getPhone_number());
		}
	}
	
//////////////////// Custom methods.

	public void setInfos() {
		
		if(!StringUtils.isEmpty(MainActivity.user.getProfile_img_url())) {
			selectedImageSdCardPaths[0] = MainActivity.user.getProfile_img_url();
		}
		
		if(!StringUtils.isEmpty(MainActivity.user.getName())) {
			etInfos[0].getEditText().setText(MainActivity.user.getName());
		}
		
		if(!StringUtils.isEmpty(MainActivity.user.getNickname())) {
			etInfos[1].getEditText().setText(MainActivity.user.getNickname());
		}
		
		if(!StringUtils.isEmpty(MainActivity.dealer.getBirthdate())) {
			etInfos[2].getEditText().setText(MainActivity.dealer.getBirthdate());
		}
		
		if(!StringUtils.isEmpty(MainActivity.dealer.getEmployee_card_img_url())) {
			selectedImageSdCardPaths[1] = MainActivity.dealer.getEmployee_card_img_url();
		}
		
		if(!StringUtils.isEmpty(MainActivity.dealer.getName_card_img_url())) {
			selectedImageSdCardPaths[2] = MainActivity.dealer.getName_card_img_url();
		}
		
		if(!StringUtils.isEmpty(MainActivity.dealer.getAddress())) {
			etInfos[3].getEditText().setText(MainActivity.dealer.getAddress());
		}
		
		if(!StringUtils.isEmpty(MainActivity.dealer.getAssociation())) {
			etInfos[4].getEditText().setText(MainActivity.dealer.getAssociation());
		}
		
		if(!StringUtils.isEmpty(MainActivity.dealer.getComplex())) {
			etInfos[5].getEditText().setText(MainActivity.dealer.getComplex());
		}
		
		if(!StringUtils.isEmpty(MainActivity.dealer.getCompany())) {
			etInfos[6].getEditText().setText(MainActivity.dealer.getCompany());
		}
		
		if(!StringUtils.isEmpty(MainActivity.dealer.getDesc())) {
			etIntroduce.setText(MainActivity.dealer.getDesc());
		}
	}
	
	public void downloadImages() {

		for(int i=0; i<3; i++) {
			
			final int INDEX = i;
			
			if(!StringUtils.isEmpty(selectedImageSdCardPaths[i])) {
				
				ivImages[i].setTag(selectedImageSdCardPaths[i]);
				DownloadUtils.downloadBitmap(selectedImageSdCardPaths[i], new OnBitmapDownloadListener() {

					@Override
					public void onError(String url) {

						LogUtils.log("EditDealerInfoPage.downloadImages.onError." + "\nurl : " + url);

						// TODO Auto-generated method stub		
					}

					@Override
					public void onCompleted(String url, Bitmap bitmap) {

						try {
							LogUtils.log("EditDealerInfoPage.downloadImages.onCompleted." + "\nurl : " + url);

							ivImages[INDEX].setImageBitmap(bitmap);
						} catch (Exception e) {
							LogUtils.trace(e);
						} catch (OutOfMemoryError oom) {
							LogUtils.trace(oom);
						}
					}
				});
			}
		}
	}
	
	public boolean checkInformation() {

		//name.
		if(StringUtils.checkForbidContains(etInfos[0].getEditText(), false, false, false, false, true, true)
				|| StringUtils.checkTextLength(etInfos[0].getEditText(), NAME_MIN, NAME_MAX) != StringUtils.PASS) {
			ToastUtils.showToast(R.string.hintForName);
		
//		//nickname.
//		} else if(StringUtils.checkTextLength(etInfos[1].getEditText(), NICKNAME_MIN, NICKNAME_MAX) != StringUtils.PASS
//				|| StringUtils.checkForbidContains(etInfos[1].getEditText(), false, false, false, true, true, false)) {
//			ToastUtils.showToast(R.string.checkNickname);
			
		//birthdate.
		} else if(StringUtils.checkForbidContains(etInfos[2].getEditText(), true, true, false, true, true, true)
				|| StringUtils.checkTextLength(etInfos[2].getEditText(), BIRTHDATE_MIN, BIRTHDATE_MAX) != StringUtils.PASS) {
			ToastUtils.showToast(R.string.hintForBirthDate);
			
		} else if(StringUtils.isEmpty(selectedImageSdCardPaths[1])) {
			ToastUtils.showToast(R.string.checkEmployeeCardImage);
		
		} else if(StringUtils.isEmpty(selectedImageSdCardPaths[2])) {
			ToastUtils.showToast(R.string.checkNameCardImage);
		
		//address.
		} else if(StringUtils.isEmpty(etInfos[2].getEditText())) {
			ToastUtils.showToast(R.string.hintForAddress);
		
		//association.
		} else if(StringUtils.isEmpty(etInfos[3].getEditText())) {
			ToastUtils.showToast(R.string.hintForAssociation);
		
		//complex.
		} else if(StringUtils.isEmpty(etInfos[4].getEditText())) {
			ToastUtils.showToast(R.string.hintForComplex);
		
		//company.
		} else if(StringUtils.isEmpty(etInfos[5].getEditText())) {
			ToastUtils.showToast(R.string.hintForCompany);
		
		//introduce.
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

			ToastUtils.showToast(R.string.uploadingImage);
			
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
		
		submit();
	}
	
	public void submit() {

		/*
		 * 바꾸고 싶은 것만 전송.
		 * http://byecar.minsangk.com/users/update/additional_info.json
		 * &user[profile_img_url]=abc
		 * ?phone_auth_key=abc
		 * &user[name]=%EA%B9%80%EB%AF%BC%EC%83%81
		 * &user[nickname]=%EB%AF%BC%EC%83%81kk
		 * &dealer[birthdate]=12345678
		 * &dealer[employee_card_img_url]=ecu1
		 * &dealer[name_card_img_url]=nciu1
		 * &user[address]=%EC%84%9C%EC%9A%B8%EA%B0%95%EB%82%A8%EB%8F%84%EA%B3%A1
		 * &dealer[association]=%EC%A1%B0%EC%A1%B0%ED%95%A9
		 * &dealer[complex]=%EB%8B%A8%EB%8B%A8%EC%A7%80
		 * &dealer[company]=%EC%83%81%EC%83%81%EC%82%AC
		 */
		String url = BCPAPIs.EDIT_DEALER_INFO_URL
				+ "?user[profile_img_url]=" + StringUtils.getUrlEncodedString(selectedImageSdCardPaths[0])
				+ "&phone_auth_key=" + PHONE_AUTH_KEY
				+ "&user[name]=" + StringUtils.getUrlEncodedString(etInfos[0].getEditText())
//				+ "&user[nickname]=" + StringUtils.getUrlEncodedString(etInfos[1].getEditText())
				+ "&dealer[birthdate]=" + StringUtils.getUrlEncodedString(etInfos[2].getEditText())
				+ "&dealer[employee_card_img_url]=" + StringUtils.getUrlEncodedString(selectedImageSdCardPaths[1])
				+ "&dealer[name_card_img_url]=" + StringUtils.getUrlEncodedString(selectedImageSdCardPaths[2])
				+ "&user[address]=" + StringUtils.getUrlEncodedString(etInfos[3].getEditText())
				+ "&dealer[association]=" + StringUtils.getUrlEncodedString(etInfos[4].getEditText())
				+ "&dealer[complex]=" + StringUtils.getUrlEncodedString(etInfos[5].getEditText())
				+ "&dealer[company]=" + StringUtils.getUrlEncodedString(etInfos[6].getEditText())
				+ "&user[desc]=" + StringUtils.getUrlEncodedString(etIntroduce);
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("EditUserInfoPage.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToUpdateUserInfo);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("EditUserInfoPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						
						LogUtils.log("###EditUserInfoPage.onCompleted.  "
								+ "\nbefore url : " + MainActivity.user.getProfile_img_url());
						
						if(selectedImageSdCardPaths[0] != null) {
							MainActivity.user.setProfile_img_url(selectedImageSdCardPaths[0]);
							MainActivity.dealer.setProfile_img_url(selectedImageSdCardPaths[0]);
						}

						if(!StringUtils.isEmpty(PHONE_AUTH_KEY)) {
							MainActivity.user.setPhone_number(tvPhoneNumber.getText().toString());
						}
						
						if(!StringUtils.isEmpty(etInfos[0].getEditText())) {
							MainActivity.user.setName(etInfos[0].getEditText().getText().toString());
							MainActivity.dealer.setName(etInfos[0].getEditText().getText().toString());
						}
						
						if(!StringUtils.isEmpty(etInfos[1].getEditText())) {
							MainActivity.user.setNickname(etInfos[1].getEditText().getText().toString());
						}
						
						if(!StringUtils.isEmpty(etInfos[2].getEditText())) {
							MainActivity.dealer.setBirthdate(etInfos[2].getEditText().getText().toString());
						}
						
						if(selectedImageSdCardPaths[1] != null) {
							MainActivity.dealer.setEmployee_card_img_url(selectedImageSdCardPaths[1]);
						}
						
						if(selectedImageSdCardPaths[2] != null) {
							MainActivity.dealer.setName_card_img_url(selectedImageSdCardPaths[2]);
						}
						
						if(!StringUtils.isEmpty(etInfos[3].getEditText())) {
							MainActivity.user.setAddress(etInfos[3].getEditText().getText().toString());
							MainActivity.dealer.setAddress(etInfos[3].getEditText().getText().toString());
						}
						
						if(!StringUtils.isEmpty(etInfos[4].getEditText())) {
							MainActivity.dealer.setAssociation(etInfos[4].getEditText().getText().toString());
						}
						
						if(!StringUtils.isEmpty(etInfos[5].getEditText())) {
							MainActivity.dealer.setComplex(etInfos[5].getEditText().getText().toString());
						}
						
						if(!StringUtils.isEmpty(etInfos[6].getEditText())) {
							MainActivity.dealer.setCompany(etInfos[6].getEditText().getText().toString());
						}
						
						if(!StringUtils.isEmpty(etIntroduce)) {
							MainActivity.dealer.setDesc(etIntroduce.getText().toString());
						}
						
						if(getArguments() != null
								&& getArguments().containsKey("dealer")) {
							
							Dealer dealer = (Dealer) getArguments().getSerializable("dealer");
							dealer.setProfile_img_url(MainActivity.dealer.getProfile_img_url());
							dealer.setName(MainActivity.dealer.getName());
							dealer.setAddress(MainActivity.dealer.getAddress());
							dealer.setAssociation(MainActivity.dealer.getAssociation());
							dealer.setComplex(MainActivity.dealer.getComplex());
							dealer.setCompany(MainActivity.dealer.getCompany());
							dealer.setDesc(MainActivity.dealer.getDesc());
						}
						
						ToastUtils.showToast(R.string.complete_editUserInfo);
						mActivity.closeTopPage();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToUpdateUserInfo);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					ToastUtils.showToast(R.string.failToUpdateUserInfo);
				}
			}
		}, mActivity.getLoadingView());
	}
}
