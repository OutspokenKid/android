package com.byecar.fragments.dealer;

import org.json.JSONObject;

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

import com.byecar.byecarplusfordealer.MainActivity;
import com.byecar.byecarplusfordealer.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPDownloadUtils;
import com.byecar.classes.BCPFragment;
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
	
	public static String phone_auth_key;
	public static String phone_number;
	
	private TextView tvTitleText1;
	private TextView tvCertified;
	private View checked;
	private Button btnCertifyPhoneNumber;
	
	private TextView tvTitleText2;
	private Button[] btnImages;
	private ImageView[] ivImages;
	private TextView tvUploadText;
	private HoloStyleEditText[] etInfos;
	private Button btnSearch;
	
	private TextView tvTitleText3;
	private TextView tvIntroduceText;
	private EditText etIntroduce;
	private Button btnSubmit;
	
	private int selectedImageIndex;
	private String[] selectedImageSdCardPaths = new String[2];
	private Bitmap thumbnail1;
	private Bitmap thumbnail2;
	private String address;
	private int dong_id;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		BaseFragmentActivity.onAfterPickImageListener = new OnAfterPickImageListener() {
			
			@Override
			public void onAfterPickImage(String[] sdCardPaths, Bitmap[] thumbnails) {
				
				if(thumbnails != null && thumbnails.length > 0) {
					ivImages[selectedImageIndex].setImageBitmap(thumbnails[0]);
					selectedImageSdCardPaths[selectedImageIndex] = sdCardPaths[0];
					
					if(selectedImageIndex == 0) {
						thumbnail1 = thumbnails[0];
					} else {
						thumbnail2 = thumbnails[0];
					}
				}
			}
		};
		
		if(savedInstanceState != null) {
			
			try {
				//phone_auth_key.
				if(savedInstanceState.containsKey("phone_auth_key")) {
					phone_auth_key = savedInstanceState.getString("phone_auth_key");
				}

				//phone_number.
				if(savedInstanceState.containsKey("phone_number")) {
					phone_number = savedInstanceState.getString("phone_number");
				}
				
				//etInfos[0].
				if(savedInstanceState.containsKey("etInfos[0]")) {
					etInfos[0].getEditText().setText(savedInstanceState.getString("etInfos[0]"));
				}
				
				//etInfos[1].
				if(savedInstanceState.containsKey("etInfos[1]")) {
					etInfos[1].getEditText().setText(savedInstanceState.getString("etInfos[1]"));
				}
				
				//etIntroduce.
				if(savedInstanceState.containsKey("etIntroduce")) {
					etIntroduce.setText(savedInstanceState.getString("etIntroduce"));
				}
				
				//selectedImageSdCardPaths[0].
				if(savedInstanceState.containsKey("selectedImageSdCardPaths[0]")) {
					selectedImageSdCardPaths[0] = savedInstanceState.getString("selectedImageSdCardPaths[0]");
				}
				
				//selectedImageSdCardPaths[1].
				if(savedInstanceState.containsKey("selectedImageSdCardPaths[1]")) {
					selectedImageSdCardPaths[1] = savedInstanceState.getString("selectedImageSdCardPaths[1]");
				}
				
				//address.
				if(savedInstanceState.containsKey("address")) {
					address = savedInstanceState.getString("address");
					btnSearch.setText(address);
				}
				
				//dong_id.
				if(savedInstanceState.containsKey("dong_id")) {
					dong_id = savedInstanceState.getInt("dong_id");
				}
				
				//thumbnail1.
				if(savedInstanceState.containsKey("thumbnail1")) {
					thumbnail1 = savedInstanceState.getParcelable("thumbnail1");
					ivImages[0].setImageBitmap(thumbnail1);
				}
				
				//thumbnail2.
				if(savedInstanceState.containsKey("thumbnail2")) {
					thumbnail2 = savedInstanceState.getParcelable("thumbnail2");
					ivImages[1].setImageBitmap(thumbnail2);
				}
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		try {
			//phone_auth_key.
			if(!StringUtils.isEmpty(phone_auth_key)) {
				outState.putString("phone_auth_key", phone_auth_key);
			}

			//phone_number.
			if(!StringUtils.isEmpty(phone_number)) {
				outState.putString("phone_number", phone_number);
			}
			
			//etInfos[0].
			if(etInfos[0].getEditText().length() > 0) {
				outState.putString("etInfos[0]", etInfos[0].getEditText().getText().toString());
			}
			
			//etInfos[1].
			if(etInfos[1].getEditText().length() > 0) {
				outState.putString("etInfos[1]", etInfos[1].getEditText().getText().toString());
			}
			
			//etIntroduce.
			if(etIntroduce.length() > 0) {
				outState.putString("etIntroduce", etIntroduce.getText().toString());
			}
			
			//selectedImageSdCardPaths[0].
			if(selectedImageSdCardPaths[0] != null) {
				outState.putString("selectedImageSdCardPaths[0]", selectedImageSdCardPaths[0]);
			}
			
			//selectedImageSdCardPaths[1].
			if(selectedImageSdCardPaths[1] != null) {
				outState.putString("selectedImageSdCardPaths[1]", selectedImageSdCardPaths[1]);
			}
			
			//address.
			if(!StringUtils.isEmpty(address)) {
				outState.putString("address", address);
			}
			
			//dong_id.
			if(dong_id != 0) {
				outState.putInt("dong_id", dong_id);
			}
			
			//thumbnail1.
			if(thumbnail1 != null) {
				outState.putParcelable("thumbnail1", thumbnail1);
			}
			
			//thumbnail2.
			if(thumbnail2 != null) {
				outState.putParcelable("thumbnail2", thumbnail2);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.editDealerInfoPage_titleBar);
		
		tvTitleText1 = (TextView) mThisView.findViewById(R.id.editDealerInfoPage_tvTitleText1);
		tvCertified = (TextView) mThisView.findViewById(R.id.editDealerInfoPage_tvCertified);
		checked = mThisView.findViewById(R.id.editDealerInfoPage_checked);
		btnCertifyPhoneNumber = (Button) mThisView.findViewById(R.id.editDealerInfoPage_btnCertifyPhoneNumber);
		
		tvTitleText2 = (TextView) mThisView.findViewById(R.id.editDealerInfoPage_tvTitleText2);
		
		btnImages = new Button[2];
		btnImages[0] = (Button) mThisView.findViewById(R.id.editDealerInfoPage_btnImage1);
		btnImages[1] = (Button) mThisView.findViewById(R.id.editDealerInfoPage_btnImage2);
		
		ivImages = new ImageView[2];
		ivImages[0] = (ImageView) mThisView.findViewById(R.id.editDealerInfoPage_ivImage1);
		ivImages[1] = (ImageView) mThisView.findViewById(R.id.editDealerInfoPage_ivImage2);
		
		tvUploadText = (TextView) mThisView.findViewById(R.id.editDealerInfoPage_tvUploadText);
		
		etInfos = new HoloStyleEditText[2];
		etInfos[0] = (HoloStyleEditText) mThisView.findViewById(R.id.editDealerInfoPage_etInfo1);
		etInfos[1] = (HoloStyleEditText) mThisView.findViewById(R.id.editDealerInfoPage_etInfo2);
		
		btnSearch = (Button) mThisView.findViewById(R.id.editDealerInfoPage_btnSearchArea);
		
		tvTitleText3 = (TextView) mThisView.findViewById(R.id.editDealerInfoPage_tvTitleText3);
		tvIntroduceText = (TextView) mThisView.findViewById(R.id.editDealerInfoPage_tvIntroduce);
		etIntroduce = (EditText) mThisView.findViewById(R.id.editDealerInfoPage_etIntroduce);
		btnSubmit = (Button) mThisView.findViewById(R.id.editDealerInfoPage_btnSubmit);
	}

	@Override
	public void setVariables() {
		
		for(int i=0; i<selectedImageSdCardPaths.length; i++) {
			selectedImageSdCardPaths[i] = null;
		}
	}

	@Override
	public void createPage() {

		tvTitleText1.setText(R.string.certifyPhoneNumber);
		tvTitleText2.setText(R.string.addedInfo);
		tvTitleText3.setText(R.string.introduce);
		
		int[] hintTextResIds = new int[] {
			R.string.hintForName,
			R.string.hintForComplex,
			R.string.hintForAddress,
		};
		
		int size = etInfos.length;
		for(int i=0; i<size; i++) {
			etInfos[i].getEditText().setTextColor(getResources().getColor(R.color.new_color_text_gray));
			etInfos[i].getEditText().setHintTextColor(getResources().getColor(R.color.new_color_text_gray));
			etInfos[i].setHint(hintTextResIds[i]);
			etInfos[i].getEditText().setSingleLine();
			etInfos[i].getEditText().setInputType(InputType.TYPE_CLASS_TEXT);
		}

		//딜러증 이미지.
		if(!StringUtils.isEmpty(MainActivity.dealer.getEmployee_card_img_url())) {
			selectedImageSdCardPaths[0] = MainActivity.dealer.getEmployee_card_img_url();
			
			BCPDownloadUtils.downloadBitmap(MainActivity.dealer.getEmployee_card_img_url(), new OnBitmapDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("EditDealerInfoPage.onError." + "\nurl : " + url);

					// TODO Auto-generated method stub		
				}

				@Override
				public void onCompleted(String url, Bitmap bitmap) {

					try {
						LogUtils.log("EditDealerInfoPage.onCompleted." + "\nurl : " + url);

						if(bitmap != null && !bitmap.isRecycled()
								&& ivImages != null && ivImages[0] != null) {
							ivImages[0].setImageBitmap(bitmap);
						}
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
					}
				}
			}, 256);
		}
		
		//명함 이미지.
		if(!StringUtils.isEmpty(MainActivity.dealer.getName_card_img_url())) {
			
			selectedImageSdCardPaths[1] = MainActivity.dealer.getName_card_img_url();
			
			BCPDownloadUtils.downloadBitmap(MainActivity.dealer.getName_card_img_url(), new OnBitmapDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("EditDealerInfoPage.onError." + "\nurl : " + url);

					// TODO Auto-generated method stub		
				}

				@Override
				public void onCompleted(String url, Bitmap bitmap) {

					try {
						LogUtils.log("EditDealerInfoPage.onCompleted." + "\nurl : " + url);

						if(bitmap != null && !bitmap.isRecycled()
								&& ivImages != null && ivImages[1] != null) {
							ivImages[1].setImageBitmap(bitmap);
						}
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
					}
				}
			}, 256);
		}
		
		//본명.
		if(!StringUtils.isEmpty(MainActivity.dealer.getName())) {
			etInfos[0].getEditText().setText(MainActivity.dealer.getName());
		}
		
		//상사 이름.
		if(!StringUtils.isEmpty(MainActivity.dealer.getCompany())) {
			etInfos[1].getEditText().setText(MainActivity.dealer.getCompany());
		}
		
		//상사주소.
		if(!StringUtils.isEmpty(MainActivity.dealer.getAddress())) {
			btnSearch.setText(MainActivity.dealer.getAddress());
			dong_id = MainActivity.user.getDong_id();
		}
		
		//자기소개.
		if(!StringUtils.isEmpty(MainActivity.dealer.getDesc())) {
			etIntroduce.setText(MainActivity.dealer.getDesc());
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
	
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(BCPConstants.PAGE_SEARCH_AREA, null);
			}
		});
		
		btnSubmit.setOnClickListener(new OnClickListener() {

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
		
		//tvTitleText1.
		rp = (RelativeLayout.LayoutParams) tvTitleText1.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(41);
		tvTitleText1.setPadding(ResizeUtils.getSpecificLength(20), 0, 0, 0);
		
		//tvCertified.
		rp = (RelativeLayout.LayoutParams) tvCertified.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(176);
		
		//checked.
		rp = (RelativeLayout.LayoutParams) checked.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(24);
		rp.height = ResizeUtils.getSpecificLength(18);
		rp.topMargin = ResizeUtils.getSpecificLength(80);
		rp.rightMargin = ResizeUtils.getSpecificLength(8);
		
		//btnCertifyPhoneNumber.
		rp = (RelativeLayout.LayoutParams) btnCertifyPhoneNumber.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(588);
		rp.height = ResizeUtils.getSpecificLength(83);
		
		//tvTitleText2.
		rp = (RelativeLayout.LayoutParams) tvTitleText2.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(41);
		rp.topMargin = ResizeUtils.getSpecificLength(57);
		tvTitleText2.setPadding(ResizeUtils.getSpecificLength(20), 0, 0, 0);
		
		for(int i=0; i<2; i++) {

			//btnImages.
			rp = (RelativeLayout.LayoutParams) btnImages[i].getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(257);
			rp.height = ResizeUtils.getSpecificLength(157);
			
			if(i == 0) {
				rp.leftMargin = ResizeUtils.getSpecificLength(52);
				rp.topMargin = ResizeUtils.getSpecificLength(54);
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
		
		//btnSearch.
		rp = (RelativeLayout.LayoutParams) btnSearch.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(584);
		rp.height = ResizeUtils.getSpecificLength(82);
		rp.topMargin = ResizeUtils.getSpecificLength(32);
		
		//tvTitleText3.
		rp = (RelativeLayout.LayoutParams) tvTitleText3.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(41);
		rp.topMargin = ResizeUtils.getSpecificLength(72);
		tvTitleText3.setPadding(ResizeUtils.getSpecificLength(20), 0, 0, 0);
		
		//etIntroduce.
		rp = (RelativeLayout.LayoutParams) etIntroduce.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(594);
		rp.height = ResizeUtils.getSpecificLength(226);
		rp.topMargin = ResizeUtils.getSpecificLength(38);
		
		//btnSubmit.
		rp = (RelativeLayout.LayoutParams) btnSubmit.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(82);
		rp.topMargin = ResizeUtils.getSpecificLength(36);

		FontUtils.setFontSize(tvTitleText1, 24);
		FontUtils.setFontStyle(tvTitleText1, FontUtils.BOLD);
		FontUtils.setFontSize(tvCertified, 34);

		FontUtils.setFontSize(tvTitleText2, 24);
		FontUtils.setFontStyle(tvTitleText2, FontUtils.BOLD);
		FontUtils.setFontSize(tvUploadText, 20);
		
		FontUtils.setFontSize(btnSearch, 30);
		
		FontUtils.setFontSize(tvTitleText3, 24);
		FontUtils.setFontStyle(tvTitleText3, FontUtils.BOLD);
		FontUtils.setFontSize(tvIntroduceText, 30);
		FontUtils.setFontAndHintSize(etIntroduce, 26, 20);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_edit_dealer_info;
	}

	@Override
	public int getPageTitleTextResId() {

		return R.string.pageTitle_editDealerInfo;
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
		
		if(StringUtils.isEmpty(MainActivity.user.getPhone_number())) {
			tvCertified.setTextColor(getResources().getColor(R.color.color_red));
			tvCertified.setText(R.string.phoneNumberisNotCertified);
			checked.setVisibility(View.INVISIBLE);
		} else {
			phone_number = MainActivity.user.getPhone_number();
			tvCertified.setTextColor(getResources().getColor(R.color.color_green));
			tvCertified.setText(phone_number);
			checked.setVisibility(View.VISIBLE);
		}
		
		if(mActivity.bundle != null) {
			address = mActivity.bundle.getString("address");
			btnSearch.setText(address);
			dong_id = mActivity.bundle.getInt("dong_id");
			mActivity.bundle = null;
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

		if(checked.getVisibility() != View.VISIBLE) {
			ToastUtils.showToast(R.string.checkToCertifyPhoneNumber);

		} else if(StringUtils.isEmpty(selectedImageSdCardPaths[0])) {
			ToastUtils.showToast(R.string.checkEmployeeCardImage);
		
		} else if(StringUtils.isEmpty(selectedImageSdCardPaths[1])) {
			ToastUtils.showToast(R.string.checkNameCardImage);
		
		} else if(StringUtils.checkForbidContains(etInfos[0].getEditText(), false, false, false, false, true, true)
				|| StringUtils.checkTextLength(etInfos[0].getEditText(), NAME_MIN, NAME_MAX) != StringUtils.PASS) {
			ToastUtils.showToast(R.string.hintForName);
		
		} else if(StringUtils.isEmpty(etInfos[1].getEditText())) {
			ToastUtils.showToast(R.string.hintForComplex);
			
		} else if(dong_id == 0) {
			ToastUtils.showToast(R.string.hintForAddress);
		
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
			
				ToastUtils.showToast(R.string.uploadingImage);
				
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
		http://dev.bye-car.com/users/update/additional_info.json
		?phone_auth_key=abc&user[nickname]=%EB%AF%BC%EC%83%81kk
		
		&user[profile_img_url]=abc&user[name]=%EA%B9%80%EB%AF%BC%EC%83%81
		&user[dong_id]=1594
		&dealer[company]=%EC%83%81%EC%83%81%EC%82%AC
		&dealer[employee_card_img_url]=ecu1
		&dealer[name_card_img_url]=nciu1
		
		phone_auth_key : 전화번호 인증키
		user[name] : 본명
		user[dong_id] : 동 검색 결과 id
		user[desc] : 설명
		dealer[company] : 상사
		dealer[employee_card_img_url] : 사원증 이미지 URL
		dealer[name_card_img_url] : 명함 이미지 URL
		 */
		try {
			StringBuilder sb = new StringBuilder(BCPAPIs.EDIT_DEALER_INFO_URL);
			sb.append("?dealer[employee_card_img_url]=").append(StringUtils.getUrlEncodedString(selectedImageSdCardPaths[0]))
					.append("&dealer[name_card_img_url]=").append(StringUtils.getUrlEncodedString(selectedImageSdCardPaths[1]))
					.append("&user[name]=").append(StringUtils.getUrlEncodedString(etInfos[0].getEditText()))
					.append("&dealer[company]=").append(StringUtils.getUrlEncodedString(etInfos[1].getEditText()))
					.append("&user[dong_id]=").append(dong_id)
					.append("&user[desc]=").append(StringUtils.getUrlEncodedString(etIntroduce));
			
			if(phone_auth_key != null) {
				sb.append("&phone_auth_key=").append(phone_auth_key);
			}
			
			String url = sb.toString();
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("SignUpForDealerPage.onError." + "\nurl : " + url);
					ToastUtils.showToast(R.string.failToEditUserInfo);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("SignUpForDealerPage.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						if(objJSON.getInt("result") == 1) {
							mActivity.closeTopPage();
							((MainActivity)mActivity).checkSession();
						} else {
							ToastUtils.showToast(objJSON.getString("message"));
						}
					} catch (Exception e) {
						LogUtils.trace(e);
						ToastUtils.showToast(R.string.failToEditUserInfo);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
						ToastUtils.showToast(R.string.failToEditUserInfo);
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
