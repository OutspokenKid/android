package com.byecar.fragments;

import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.MainActivity;
import com.byecar.byecarplus.R;
import com.byecar.classes.BCPAPIs;
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

public class ProfilePage extends BCPFragment {

	private static final int NICKNAME_MIN = 2;
	private static final int NICKNAME_MAX = 15;

	private TextView tvProfileTitle;
	private Button btnProfile;
	private ImageView ivProfile;
	private TextView tvProfile;
	private TextView tvNickname;
	private HoloStyleEditText etNickname;
	private TextView tvCheckNickname;
	private TextView tvNickname2;
	private Button btnComplete;
	
	private boolean focusOnNickname;
	private boolean passNickname;
	
	private String selectedSdCardPath;
	private String selectedImageUrl;
	private Bitmap thumbnail;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		BaseFragmentActivity.onAfterPickImageListener = new OnAfterPickImageListener() {
			
			@Override
			public void onAfterPickImage(String[] sdCardPaths, Bitmap[] thumbnails) {
				
				if(thumbnails != null && thumbnails.length > 0) {
					thumbnail = thumbnails[0];
					ivProfile.setImageBitmap(thumbnail);
					selectedSdCardPath = sdCardPaths[0];
				}
			}
		};

		if(savedInstanceState != null) {
			
			try {
				//selectedSdCardPath.
				if(savedInstanceState.containsKey("selectedSdCardPath")) {
					this.selectedSdCardPath = savedInstanceState.getString("selectedSdCardPath");
				}

				//selectedImageUrl.
				if(savedInstanceState.containsKey("selectedImageUrl")) {
					this.selectedImageUrl = savedInstanceState.getString("selectedImageUrl");
				}
				
				//thumbnail.
				if(savedInstanceState.containsKey("thumbnail")) {
					this.selectedImageUrl = savedInstanceState.getString("selectedImageUrl");
					this.thumbnail = savedInstanceState.getParcelable("bitmap");
					ivProfile.setImageBitmap(thumbnail);
				}
				
				//etNickname.
				if(savedInstanceState.containsKey("etNickname")) {
					etNickname.getEditText().setText(savedInstanceState.getString("etNickname"));
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
			//selectedSdCardPath.
			if(!StringUtils.isEmpty(selectedSdCardPath)) {
				outState.putString("selectedSdCardPath", selectedSdCardPath);
			}

			//selectedImageUrl.
			if(!StringUtils.isEmpty(selectedImageUrl)) {
				outState.putString("selectedImageUrl", selectedImageUrl);
			}
			
			//thumbnail.
			if(thumbnail != null) {
				outState.putParcelable("thumbnail", thumbnail);
			}
			
			//etNickname.
			if(etNickname.getEditText().length() > 0) {
				outState.putString("etNickname", etNickname.getEditText().getText().toString());
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.profilePage_titleBar);
		
		tvProfileTitle = (TextView) mThisView.findViewById(R.id.profilePage_tvProfileTitle);
		btnProfile = (Button) mThisView.findViewById(R.id.profilePage_btnProfile);
		ivProfile = (ImageView) mThisView.findViewById(R.id.profilePage_ivProfile);
		tvProfile = (TextView) mThisView.findViewById(R.id.profilePage_tvProfile);
		tvNickname = (TextView) mThisView.findViewById(R.id.profilePage_tvNickname);
		etNickname = (HoloStyleEditText) mThisView.findViewById(R.id.profilePage_etNickname);
		tvCheckNickname = (TextView) mThisView.findViewById(R.id.profilePage_tvCheckNickname);
		tvNickname2 = (TextView) mThisView.findViewById(R.id.profilePage_tvNickname2);
		btnComplete = (Button) mThisView.findViewById(R.id.profilePage_btnComplete);
	}

	@Override
	public void setVariables() {
	}

	@Override
	public void createPage() {

		tvProfileTitle.setText("1. " + getString(R.string.profileImage));
		tvNickname.setText("2. " + getString(R.string.nickname));
		
		etNickname.setHint(R.string.hintForNickname);
		etNickname.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
		etNickname.getEditText().setTextColor(getResources().getColor(R.color.new_color_text_orange));
		tvNickname2.setText(R.string.inputNewNickname);
		
		selectedImageUrl = MainActivity.user.getProfile_img_url();
		etNickname.getEditText().setText(MainActivity.user.getNickname());
		
		ivProfile.setTag(MainActivity.user.getProfile_img_url());
		BCPDownloadUtils.downloadBitmap(MainActivity.user.getProfile_img_url(), new OnBitmapDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("MainForUserActivity.setLeftViewUserInfo.onError." + "\nurl : " + url);
				ivProfile.setImageDrawable(null);
			}

			@Override
			public void onCompleted(String url, Bitmap bitmap) {

				try {
					LogUtils.log("MainForUserActivity.setLeftViewUserInfo.onCompleted." + "\nurl : " + url);
					ivProfile.setImageBitmap(bitmap);;
				} catch (Exception e) {
					LogUtils.trace(e);
					ivProfile.setImageDrawable(null);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					ivProfile.setImageDrawable(null);
				}
			}
		}, 190);
	}

	@Override
	public void setListeners() {

		btnProfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showUploadPhotoPopup(1, Color.rgb(254, 188, 42));
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

		btnComplete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(checkInfos()) {
					
					if(!StringUtils.isEmpty(selectedSdCardPath)) {
						uploadImage();
					} else {
						submit();
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
		rp = (RelativeLayout.LayoutParams) (mThisView.findViewById(R.id.profilePage_profileFrame)).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(219);
		rp.height = ResizeUtils.getSpecificLength(219);
		rp.topMargin = ResizeUtils.getSpecificLength(60);
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);

		//tvNickname.
		rp = (RelativeLayout.LayoutParams) tvNickname.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(41);
		rp.topMargin = ResizeUtils.getSpecificLength(60);
		rp.bottomMargin = ResizeUtils.getSpecificLength(30);
		tvNickname.setPadding(ResizeUtils.getSpecificLength(20), 0, 0, 0);
		
		//etNickname.
		rp = (RelativeLayout.LayoutParams) etNickname.getLayoutParams();
		rp.width = width;
		rp.height = textViewHeight;
		rp.bottomMargin = ResizeUtils.getSpecificLength(30);
		
		//btnComplete.
		rp = (RelativeLayout.LayoutParams) btnComplete.getLayoutParams();
		rp.width = width;
		rp.height = buttonHeight;
		rp.topMargin = ResizeUtils.getSpecificLength(850);
		rp.bottomMargin = ResizeUtils.getSpecificLength(30);
		
		FontUtils.setFontSize(tvProfileTitle, 24);
		FontUtils.setFontStyle(tvProfileTitle, FontUtils.BOLD);
		FontUtils.setFontSize(tvProfile, 20);
		FontUtils.setFontSize(tvNickname, 24);
		FontUtils.setFontStyle(tvNickname, FontUtils.BOLD);
		FontUtils.setFontAndHintSize(etNickname.getEditText(), 24, 24);
		FontUtils.setFontSize(tvCheckNickname, 20);
		FontUtils.setFontSize(tvNickname2, 24);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_profile;
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

		return R.id.profilePage_mainLayout;
	}
	
//////////////////// Custom methods.

	public boolean checkInfos() {

		if(!checkNickname()) {
			ToastUtils.showToast(R.string.checkNickname);
		} else {
			return true;
		}
		
		return false;
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

				submit();
			}
		};
		
		ImageUploadUtils.uploadImage(BCPAPIs.UPLOAD_URL, oaui, selectedSdCardPath);
	}
	
	public void submit() {
		
		/*
		http://dev.bye-car.com/users/update/additional_info.json
		?user[nickname]=%EB%AF%BC%EC%83%81kk
		&user[profile_img_url]=abc
		*/
		String url = BCPAPIs.EDIT_USER_INFO_UR_COMMON
				+ "?user[nickname]=" + StringUtils.getUrlEncodedString(etNickname.getEditText())
				+ "&user[profile_img_url]=" + StringUtils.getUrlEncodedString(selectedImageUrl);
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("ProfilePage.signUp.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToEditProfile);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("ProfilePage.signUp.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						MainActivity.user.setNickname(etNickname.getEditText().getText().toString());
						MainActivity.user.setProfile_img_url(selectedImageUrl);
						((MainActivity)mActivity).setLeftViewUserInfo();
						
						ToastUtils.showToast(R.string.complete_editProfile);
						mActivity.closeTopPage();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToEditProfile);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					ToastUtils.showToast(R.string.failToEditProfile);
				}
			}
		}, mActivity.getLoadingView());
	}
}
