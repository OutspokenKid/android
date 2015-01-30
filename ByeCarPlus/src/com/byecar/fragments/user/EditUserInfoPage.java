package com.byecar.fragments.user;

import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.MainActivity;
import com.byecar.byecarplus.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPFragment;
import com.byecar.models.Car;
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

public class EditUserInfoPage extends BCPFragment {

	public static final int TYPE_EDIT_PROFILE = 0;
	public static final int TYPE_DEALER_INFO = 1;
	public static final int TYPE_REQUEST_BUYER = 2;
	
	private static final int NAME_MIN = 2;
	private static final int NAME_MAX = 16;
	private static final int NICKNAME_MIN = 3;
	private static final int NICKNAME_MAX = 15;
	private static final int ADDRESS_MIN = 0;
	private static final int ADDRESS_MAX = 50;

	private FrameLayout profileFrame;
	private Button btnProfile;
	private ImageView ivProfile;
	private TextView tvProfile;
	private TextView tvBaseInfo;
	private HoloStyleEditText etName;
	private TextView tvCheckName;
	private HoloStyleEditText etNickname;
	private TextView tvCheckNickname;
	private HoloStyleEditText etAddress;
	private TextView tvCheckAddress;
	private TextView tvCertifyPhoneNumber;
	private TextView tvPhoneNumber;
	private View checkIcon;
	private Button btnEditPhoneNumber;
	private View termOfUse;
	private Button btnTermOfUse;
	private Button btnConfirm;
	
	private boolean focusOnName, focusOnNickname, focusOnAddress;
	private boolean passName = true, passNickname = true, passAddress = true;
	
	private String selectedSdCardPath;
	private String selectedImageUrl;

	private boolean isTermOfUseClicked;
	
	private int type;
	private int carId;
	private int carType;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		BaseFragmentActivity.onAfterPickImageListener = new OnAfterPickImageListener() {
			
			@Override
			public void onAfterPickImage(String[] sdCardPaths, Bitmap[] thumbnails) {
				
				LogUtils.log("###EditUserInfoPage.onAfterPickImage.  "
						+ "\n sdCardPaths : " + sdCardPaths
						+ "\n thumbnails : " + thumbnails);
				
				if(thumbnails != null && thumbnails.length > 0) {
					ivProfile.setImageBitmap(thumbnails[0]);
					selectedSdCardPath = sdCardPaths[0];
				}
			}
		};
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.editUserInfoPage_titleBar);
		
		profileFrame = (FrameLayout) mThisView.findViewById(R.id.editUserInfoPage_profileFrame);
		btnProfile = (Button) mThisView.findViewById(R.id.editUserInfoPage_btnProfile);
		ivProfile = (ImageView) mThisView.findViewById(R.id.editUserInfoPage_ivProfile);
		tvProfile = (TextView) mThisView.findViewById(R.id.editUserInfoPage_tvProfile);
		tvBaseInfo = (TextView) mThisView.findViewById(R.id.editUserInfoPage_tvBaseInfo);
		etName = (HoloStyleEditText) mThisView.findViewById(R.id.editUserInfoPage_etName);
		tvCheckName = (TextView) mThisView.findViewById(R.id.editUserInfoPage_tvCheckName);
		etNickname = (HoloStyleEditText) mThisView.findViewById(R.id.editUserInfoPage_etNickname);
		tvCheckNickname = (TextView) mThisView.findViewById(R.id.editUserInfoPage_tvCheckNickname);
		etAddress = (HoloStyleEditText) mThisView.findViewById(R.id.editUserInfoPage_etAddress);
		tvCheckAddress = (TextView) mThisView.findViewById(R.id.editUserInfoPage_tvCheckAddress);
		tvCertifyPhoneNumber = (TextView) mThisView.findViewById(R.id.editUserInfoPage_tvCertifyPhoneNumber);
		btnEditPhoneNumber = (Button) mThisView.findViewById(R.id.editUserInfoPage_btnEditPhoneNumber);
		tvPhoneNumber = (TextView) mThisView.findViewById(R.id.editUserInfoPage_tvPhoneNumber);
		checkIcon = mThisView.findViewById(R.id.editUserInfoPage_checkIcon);
		termOfUse = mThisView.findViewById(R.id.editUserInfoPage_termOfUse);
		btnTermOfUse = (Button) mThisView.findViewById(R.id.editUserInfoPage_btnTermOfUse);
		btnConfirm = (Button) mThisView.findViewById(R.id.editUserInfoPage_btnConfirm);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {
			type = getArguments().getInt("type");
			carId = getArguments().getInt("carId");
			carType = getArguments().getInt("carType");
		}
	}

	@Override
	public void createPage() {

		if(MainActivity.user == null) {
			closePage();
		}
		
		if(type == TYPE_REQUEST_BUYER) {
			profileFrame.setVisibility(View.GONE);
			tvProfile.setVisibility(View.GONE);
			termOfUse.setVisibility(View.VISIBLE);
			btnTermOfUse.setVisibility(View.VISIBLE);
			tvBaseInfo.setText(R.string.buyerInfo);
			btnConfirm.setBackgroundResource(R.drawable.request_complete_btn);
		} else {
			profileFrame.setVisibility(View.VISIBLE);
			tvProfile.setVisibility(View.VISIBLE);
			termOfUse.setVisibility(View.GONE);
			btnTermOfUse.setVisibility(View.GONE);
			tvBaseInfo.setText(R.string.commonInfo);
			btnConfirm.setBackgroundResource(R.drawable.user_done_btn);
		}
		
		etName.setHint(R.string.hintForName);
		etName.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
		
		etNickname.setHint(R.string.hintForNickname);
		etNickname.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
		
		etAddress.setHint(R.string.hintForAddress);
		etAddress.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_NORMAL);
	}

	@Override
	public void setListeners() {

		btnProfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showUploadPhotoPopup(1, Color.rgb(254, 188, 42));
			}
		});

		etName.getEditText().addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				if(s != null && s.length() != 0) {
					etName.getEditText().setGravity(Gravity.CENTER);
				} else {
					etName.getEditText().setGravity(Gravity.LEFT);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {}
		});
		
		etNickname.getEditText().addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				if(s != null && s.length() != 0) {
					etNickname.getEditText().setGravity(Gravity.CENTER);
				} else {
					etNickname.getEditText().setGravity(Gravity.LEFT);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {}
		});

		etAddress.getEditText().addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				if(s != null && s.length() != 0) {
					etAddress.getEditText().setGravity(Gravity.CENTER);
				} else {
					etAddress.getEditText().setGravity(Gravity.LEFT);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {}
		});
		
		etName.getEditText().setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				//포커스를 갖고 있다가 잃은 시점.
				if(focusOnName && !hasFocus) {
					checkName();
				}
				
				focusOnName = hasFocus;
			}
		});
		
		etNickname.getEditText().setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				//포커스를 갖고 있다가 잃은 시점.
				if(focusOnNickname && !hasFocus) {
					checkNickname();
				}
				
				focusOnNickname = hasFocus;
			}
		});
		
		etAddress.getEditText().setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				//포커스를 갖고 있다가 잃은 시점.
				if(focusOnAddress && !hasFocus) {
					checkAddress();
				}
				
				focusOnAddress = hasFocus;
			}
		});
		
		btnEditPhoneNumber.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(BCPConstants.PAGE_CERTIFY_PHONE_NUMBER, null);
			}
		});
		
		termOfUse.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				isTermOfUseClicked = !isTermOfUseClicked;
				
				if(isTermOfUseClicked) {
					termOfUse.setBackgroundResource(R.drawable.registration_agree_btn_b);
				} else {
					termOfUse.setBackgroundResource(R.drawable.registration_agree_btn_a);
				}
			}
		});
		
		btnTermOfUse.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(BCPConstants.PAGE_TERM_OF_USE, null);
			}
		});
		
		btnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(type == TYPE_REQUEST_BUYER) {
					
					if(!checkName()) {
						ToastUtils.showToast(R.string.checkName);
					
					} else if(!checkNickname()) {
						ToastUtils.showToast(R.string.checkNickname);
					
					} else if(!checkAddress()) {
						ToastUtils.showToast(R.string.checkAddress);
						
					} else if(tvPhoneNumber.getVisibility() != View.VISIBLE) {
						ToastUtils.showToast(R.string.checkPhoneNumber);
						
					} else if(!isTermOfUseClicked) {
						ToastUtils.showToast(R.string.agreeTermOfUse);
						
					} else {
						purchase();
					}
				} else {
					if(checkInfos()) {
						uploadImage();
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

		//profileFrame
		rp = (RelativeLayout.LayoutParams) (mThisView.findViewById(R.id.editUserInfoPage_profileFrame)).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(219);
		rp.height = ResizeUtils.getSpecificLength(219);
		rp.topMargin = ResizeUtils.getSpecificLength(60);
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);

		//tvBaseInfo.
		rp = (RelativeLayout.LayoutParams) tvBaseInfo.getLayoutParams();
		rp.leftMargin = ResizeUtils.getSpecificLength(30);
		rp.topMargin = ResizeUtils.getSpecificLength(40);
		rp.bottomMargin = ResizeUtils.getSpecificLength(30);
		
		//etName.
		rp = (RelativeLayout.LayoutParams) etName.getLayoutParams();
		rp.width = width;
		rp.height = textViewHeight;
		rp.bottomMargin = ResizeUtils.getSpecificLength(30);

		//etNickname.
		rp = (RelativeLayout.LayoutParams) etNickname.getLayoutParams();
		rp.width = width;
		rp.height = textViewHeight;
		rp.bottomMargin = ResizeUtils.getSpecificLength(30);
		
		//etAddress.
		rp = (RelativeLayout.LayoutParams) etAddress.getLayoutParams();
		rp.width = width;
		rp.height = textViewHeight;
		rp.bottomMargin = ResizeUtils.getSpecificLength(30);
		
		//tvCertifyPhoneNumber.
		rp = (RelativeLayout.LayoutParams) tvCertifyPhoneNumber.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);

		//btnEditPhoneNumber.
		rp = (RelativeLayout.LayoutParams) btnEditPhoneNumber.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(160);
		rp.height = ResizeUtils.getSpecificLength(30);
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
		
		//termOfUse.
		rp = (RelativeLayout.LayoutParams) termOfUse.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(313);
		rp.height = ResizeUtils.getSpecificLength(30);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		rp.bottomMargin = ResizeUtils.getSpecificLength(30);

		//btnTermOfUse.
		rp = (RelativeLayout.LayoutParams) btnTermOfUse.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(259);
		rp.height = ResizeUtils.getSpecificLength(30);
		rp.rightMargin = ResizeUtils.getSpecificLength(30);
		
		//btnConfirm.
		rp = (RelativeLayout.LayoutParams) btnConfirm.getLayoutParams();
		rp.width = width;
		rp.height = buttonHeight;
		
		FontUtils.setFontSize(tvProfile, 20);
		FontUtils.setFontSize(tvBaseInfo, 30);
		FontUtils.setFontAndHintSize(etName.getEditText(), 30, 20);
		FontUtils.setFontAndHintSize(etNickname.getEditText(), 30, 20);
		FontUtils.setFontAndHintSize(etAddress.getEditText(), 30, 20);
		FontUtils.setFontSize(tvCheckName, 20);
		FontUtils.setFontSize(tvCheckNickname, 20);
		FontUtils.setFontSize(tvCheckAddress, 20);
		FontUtils.setFontSize(tvCertifyPhoneNumber, 30);
		FontUtils.setFontSize(tvPhoneNumber, 30);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_edit_user_info;
	}

	@Override
	public int getBackButtonResId() {

		switch(type) {
		case TYPE_EDIT_PROFILE:
			return R.drawable.menu_profile_back_btn;
			
		case TYPE_DEALER_INFO:
			return R.drawable.user_back_btn;
			
		case TYPE_REQUEST_BUYER:
			return R.drawable.request_back_btn;
			
			default:
				return R.drawable.menu_profile_back_btn;	
		}
	}

	@Override
	public int getBackButtonWidth() {

		switch(type) {
		case TYPE_EDIT_PROFILE:
		case TYPE_DEALER_INFO:
			return 238;
			
		case TYPE_REQUEST_BUYER:
			return 210;
			
			default:
				return 294;	
		}
	}

	@Override
	public int getBackButtonHeight() {

		return 60;
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
		
		setUserInfo();
		checkPhoneNumberCertified();
	}

	@Override
	public void onDestroyView() {

		etName.getEditText().setOnFocusChangeListener(null);
		etNickname.getEditText().setOnFocusChangeListener(null);
		etAddress.getEditText().setOnFocusChangeListener(null);
		
		super.onDestroyView();
	}
	
	@Override
	public int getRootViewResId() {

		return R.id.editUserInfoPage_mainLayout;
	}
	
////////////////////Custom methods.
	
	public void setUserInfo() {
		
		if(selectedSdCardPath == null 
				&& !StringUtils.isEmpty(MainActivity.user.getProfile_img_url())) {
			LogUtils.log("###EditUserInfoPage.setUserInfo.  selectedSdCardPath : " + selectedSdCardPath);
			
			selectedImageUrl = MainActivity.user.getProfile_img_url();
			
			ivProfile.setTag(MainActivity.user.getProfile_img_url());
			DownloadUtils.downloadBitmap(MainActivity.user.getProfile_img_url(), new OnBitmapDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("EditUserInfoPage.onError." + "\nurl : " + url);
				}

				@Override
				public void onCompleted(String url, Bitmap bitmap) {

					try {
						LogUtils.log("EditUserInfoPage.onCompleted." + "\nurl : " + url);

						if(bitmap != null && !bitmap.isRecycled()) {
							ivProfile.setImageBitmap(bitmap);
						}
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
					}
				}
			});
		} else {
			LogUtils.log("###EditUserInfoPage.setUserInfo.  selectedSdCardPath : " + selectedSdCardPath);
		}
		
		if(!StringUtils.isEmpty(MainActivity.user.getName())) {
			etName.getEditText().setText(MainActivity.user.getName());
		}
		
		if(!StringUtils.isEmpty(MainActivity.user.getNickname())) {
			etNickname.getEditText().setText(MainActivity.user.getNickname());
		}
		
		if(!StringUtils.isEmpty(MainActivity.user.getAddress())) {
			etAddress.getEditText().setText(MainActivity.user.getAddress());
		}
	}
	
	public void checkPhoneNumberCertified() {
		
		tvPhoneNumber.setText(null);
		
		if(!StringUtils.isEmpty(MainActivity.user.getPhone_number())) {
			checkIcon.setVisibility(View.VISIBLE);
			FontUtils.addSpan(tvPhoneNumber, MainActivity.user.getPhone_number(), 0, 1);
			
		} else {
			checkIcon.setVisibility(View.INVISIBLE);
			tvPhoneNumber.setText(null);
			FontUtils.addSpan(tvPhoneNumber, 
					R.string.notCertifiedPhoneNumber1, 
					getResources().getColor(R.color.color_red), 
					1, true);
			FontUtils.addSpan(tvPhoneNumber, 
					"\n" + getString(R.string.notCertifiedPhoneNumber2), 
					getResources().getColor(R.color.color_red), 
					0.7f);
		}
	}
	
	public boolean checkInfos() {

		if(passName 
				&& passNickname 
				&& passAddress) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean checkName() {
		
		if(StringUtils.checkTextLength(etName.getEditText(), NAME_MIN, NAME_MAX)
				!= StringUtils.PASS
				|| StringUtils.checkForbidContains(etName.getEditText(), false, false, false, true, true, false)) {
			tvCheckName.setText(R.string.checkName);
			tvCheckName.setVisibility(View.VISIBLE);
			passName = false;
		} else{
			tvCheckName.setVisibility(View.INVISIBLE);
			passName = true;
		}
		
		return passName;
	}

	public boolean checkNickname() {
		
		if(StringUtils.checkTextLength(etNickname.getEditText(), NICKNAME_MIN, NICKNAME_MAX)
				!= StringUtils.PASS
				|| StringUtils.checkForbidContains(etNickname.getEditText(), false, false, false, true, true, false)) {
			tvCheckNickname.setText(R.string.checkNickname);
			tvCheckNickname.setVisibility(View.VISIBLE);
			passNickname = false;
		} else{
			tvCheckNickname.setVisibility(View.INVISIBLE);
			passNickname = true;
		}
		
		return passNickname;
	}
	
	public boolean checkAddress() {
		
		if(StringUtils.checkTextLength(etAddress.getEditText(), ADDRESS_MIN, ADDRESS_MAX)
				!= StringUtils.PASS
				|| StringUtils.checkForbidContains(etAddress.getEditText(), false, false, false, true, true, false)) {
			tvCheckAddress.setText(R.string.checkAddress);
			tvCheckAddress.setVisibility(View.VISIBLE);
			passAddress = false;
		} else{
			tvCheckAddress.setVisibility(View.INVISIBLE);
			passAddress = true;
		}
		
		return passAddress;
	}
	
	public void uploadImage() {
		
		if(StringUtils.isEmpty(selectedSdCardPath)) {
			confirm();
			return;
		}
		
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

				confirm();
			}
		};
		
		ImageUploadUtils.uploadImage(BCPAPIs.UPLOAD_URL, oaui, selectedSdCardPath);
	}
	
	public void confirm() {
		
		//?phone_auth_key=&user[nickname]=&user[profile_img_url]=&user[name]=&user[address]=
		String url = BCPAPIs.EDIT_USER_INFO_UR_COMMON
				+ "?phone_auth_key="
				+ "&user[nickname]=" + StringUtils.getUrlEncodedString(etNickname.getEditText())
				+ "&user[profile_img_url]=" + StringUtils.getUrlEncodedString(selectedImageUrl)
				+ "&user[name]=" + StringUtils.getUrlEncodedString(etName.getEditText())
				+ "&user[address]=" + StringUtils.getUrlEncodedString(etAddress.getEditText());
		
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
						
						if(!StringUtils.isEmpty(selectedImageUrl)) {
							MainActivity.user.setProfile_img_url(selectedImageUrl);
						}

						LogUtils.log("###EditUserInfoPage.onCompleted.  "
								+ "\nafter url : " + MainActivity.user.getProfile_img_url());
						
						LogUtils.log("###EditUserInfoPage.onCompleted.  "
								+ "\nafter MainActivity.user url : " + MainActivity.user.getProfile_img_url());
						
						if(!StringUtils.isEmpty(etNickname.getEditText())) {
							MainActivity.user.setNickname(etNickname.getEditText().getText().toString());
						}
						
						if(!StringUtils.isEmpty(etName.getEditText())) {
							MainActivity.user.setName(etName.getEditText().getText().toString());
						}
						
						if(!StringUtils.isEmpty(etAddress.getEditText())) {
							MainActivity.user.setAddress(etAddress.getEditText().getText().toString());
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
		});
	}

	public void purchase() {

		switch(carType) {
		
		case Car.TYPE_DEALER:
			url = BCPAPIs.CAR_DEALER_PURCHASES_URL;
			break;
			
		case Car.TYPE_DIRECT_CERTIFIED:
			url = BCPAPIs.CAR_DIRECT_CERTIFIED_PURCHASES_URL;
			break;
			
		case Car.TYPE_DIRECT_NORMAL:
			url = BCPAPIs.CAR_DIRECT_NORMAL_PURCHASES_URL;
			break;
		}
		
		if(url == null) {
			return;
		}
		
		url += "?onsalecar_id=" + carId;
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("EditUserInfoPage.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("EditUserInfoPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						((MainActivity)mActivity).showPopup(MainActivity.POPUP_REQUEST_BUY);
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
	
	public void closePage() {
		
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				ToastUtils.showToast(R.string.failToLoadUserInfo);
				mActivity.closeTopPage();
			}
		}, 1000);
	}
}
