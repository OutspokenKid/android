package com.byecar.fragments;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.MainActivity;
import com.byecar.byecarplus.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPFragment;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class PhoneInfoPage extends BCPFragment {

	private TextView tvPhoneInfo;
	private View check;
	private TextView tvNeedToCertify;
	private Button btnCertify;
	private Button btnComplete;
	
	private String phone_auth_key;
	private String requestedPhoneNumber;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.phoneInfoPage_titleBar);

		tvPhoneInfo = (TextView) mThisView.findViewById(R.id.phoneInfoPage_tvPhoneInfo);
		check = mThisView.findViewById(R.id.phoneInfoPage_check);
		tvNeedToCertify = (TextView) mThisView.findViewById(R.id.phoneInfoPage_tvNeedToCertify);
		btnCertify = (Button) mThisView.findViewById(R.id.phoneInfoPage_btnCertify);
		btnComplete = (Button) mThisView.findViewById(R.id.phoneInfoPage_btnComplete);
	}

	@Override
	public void setVariables() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createPage() {

		setViewSetting();
	}

	@Override
	public void setListeners() {

		btnCertify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(BCPConstants.PAGE_CERTIFY_PHONE_NUMBER, null);
			}
		});
		
		btnComplete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				updatePhoneNumber(phone_auth_key);
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		
		//tvPhoneInfo.
		rp = (RelativeLayout.LayoutParams) tvPhoneInfo.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(264);

		//check.
		rp = (RelativeLayout.LayoutParams) check.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(24);
		rp.height = ResizeUtils.getSpecificLength(18);
		rp.topMargin = ResizeUtils.getSpecificLength(123);
		rp.rightMargin = ResizeUtils.getSpecificLength(8);

		//tvNeedToCertify.
		rp = (RelativeLayout.LayoutParams) tvNeedToCertify.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(264);
		
		//btnCertify.
		rp = (RelativeLayout.LayoutParams) btnCertify.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(588);
		rp.height = ResizeUtils.getSpecificLength(83);
		
		//btnComplete.
		rp = (RelativeLayout.LayoutParams) btnComplete.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(588);
		rp.height = ResizeUtils.getSpecificLength(83);
		rp.topMargin = ResizeUtils.getSpecificLength(22);
		
		FontUtils.setFontSize(tvPhoneInfo, 30);
		FontUtils.setFontStyle(tvPhoneInfo, FontUtils.BOLD);
		FontUtils.setFontSize(tvNeedToCertify, 30);
		FontUtils.setFontStyle(tvNeedToCertify, FontUtils.BOLD);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_common_phone_info;
	}
	
	@Override
	public int getPageTitleTextResId() {

		return R.string.pageTitle_phoneInfo;
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
	public int getRootViewResId() {

		return R.id.phoneInfoPage_mainLayout;
	}

	@Override
	public void onResume() {

		super.onResume();
		
		if(mActivity.bundle != null) {
			phone_auth_key = mActivity.bundle.getString("phone_auth_key");
			requestedPhoneNumber = mActivity.bundle.getString("requestedPhoneNumber");
			setViewSetting();
			mActivity.bundle = null;
		}
	}
	
//////////////////// Custom methods.
	
	public void setViewSetting() {

		//요청된 번호가 1순위.
		if(!StringUtils.isEmpty(requestedPhoneNumber)) {
			tvPhoneInfo.setVisibility(View.VISIBLE);
			check.setVisibility(View.VISIBLE);
			tvNeedToCertify.setVisibility(View.INVISIBLE);
			btnComplete.setVisibility(View.VISIBLE);
			
			tvPhoneInfo.setText(requestedPhoneNumber);
			
		//요청된 번호가 없다면 유저정보에 있는 번호가 2순위.
		} else if(!StringUtils.isEmpty(MainActivity.user.getPhone_number())) {
			tvPhoneInfo.setVisibility(View.VISIBLE);
			check.setVisibility(View.VISIBLE);
			tvNeedToCertify.setVisibility(View.INVISIBLE);
			btnComplete.setVisibility(View.VISIBLE);
			
			tvPhoneInfo.setText(MainActivity.user.getPhone_number());
			
		} else {
			tvPhoneInfo.setVisibility(View.INVISIBLE);
			check.setVisibility(View.INVISIBLE);
			tvNeedToCertify.setVisibility(View.VISIBLE);
			btnComplete.setVisibility(View.INVISIBLE);
		}
	}
	
	public void updatePhoneNumber(final String phone_auth_key) {

		if(phone_auth_key == null || requestedPhoneNumber == null) {
			ToastUtils.showToast(R.string.complete_auth);
			mActivity.closeTopPage();
			return;
		}
		
		String url = BCPAPIs.PHONE_UPDATE_URL
				+ "?phone_auth_key=" + phone_auth_key;
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("PhoneInfoPage.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToSendAuthRequest);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("PhoneInfoPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);
					
					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_auth);
						MainActivity.user.setPhone_number(requestedPhoneNumber);
						mActivity.closeTopPage();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
					
				} catch (Exception e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToSendAuthRequest);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					ToastUtils.showToast(R.string.failToSendAuthRequest);
				}
			}
		}, mActivity.getLoadingView());
	}
}
