package com.byecar.byecarplus.fragments.main_for_user;

import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.byecarplus.classes.BCPAPIs;
import com.byecar.byecarplus.classes.BCPFragmentForMainForUser;
import com.byecar.byecarplus.views.TitleBar;
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

public class EditUserInfoPage extends BCPFragmentForMainForUser {

	public static final int FROM_MENU = 0;
	public static final int FROM_REGISTRATION = 1;
	
	private static final int NAME_MIN = 2;
	private static final int NAME_MAX = 16;
	private static final int NICKNAME_MIN = 3;
	private static final int NICKNAME_MAX = 15;
	private static final int ADDRESS_MIN = 0;
	private static final int ADDRESS_MAX = 50;
	
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
	private Button btnEditPhoneNumber;
	private Button btnConfirm;
	
	private boolean focusOnName, focusOnNickname, focusOnAddress;
	private boolean passName, passNickname, passAddress;
	
	private String selectedSdCardPath;
	private String selectedImageUrl;
	
	private String phone_auth_key;
	private int from;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
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

		titleBar = (TitleBar) mThisView.findViewById(R.id.editUserInfoPage_titleBar);
		
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
		btnConfirm = (Button) mThisView.findViewById(R.id.editUserInfoPage_btnConfirm);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {
			from = getArguments().getInt("from");
		}
	}

	@Override
	public void createPage() {

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
		
		btnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(checkInfos()) {
					uploadImage();
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
		rp.topMargin = ResizeUtils.getSpecificLength(60);
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
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.topMargin = ResizeUtils.getSpecificLength(15);
		rp.rightMargin = ResizeUtils.getSpecificLength(26);
		
		//tvCertifyPhoneNumber.
		rp = (RelativeLayout.LayoutParams) tvCertifyPhoneNumber.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		
		//tvPhoneNumber.
		rp = (RelativeLayout.LayoutParams) tvPhoneNumber.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(95);
		
		//btnConfirm.
		rp = (RelativeLayout.LayoutParams) btnConfirm.getLayoutParams();
		rp.width = width;
		rp.height = buttonHeight;
		rp.topMargin = ResizeUtils.getSpecificLength(800);
		rp.bottomMargin = ResizeUtils.getSpecificLength(30);
		
		FontUtils.setFontSize(tvProfile, 20);
		FontUtils.setFontSize(tvBaseInfo, 34);
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

		if(from == FROM_MENU) {
			return R.drawable.menu_profile_back_btn;
		} else {
			return R.drawable.user_back_btn;
		}
	}

	@Override
	public int getBackButtonWidth() {

		return 238;
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

////////////////////Custom methods.
	
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
		
		if(StringUtils.isEmpty(selectedImageUrl)) {
			
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

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("EditUserInfoPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						mActivity.closeTopPage();
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
