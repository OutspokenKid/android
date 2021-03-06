package com.byecar.fragments;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

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
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo.holo_light.HoloStyleEditText;

public class CertifyPhoneNumberPage extends BCPFragment {

	private static int MODE_PHONE_NUMBER = 0;
	private static int MODE_CERTIFICATION_NUMBER = 1;
	
	private HoloStyleEditText etPhoneNumber;
	private HoloStyleEditText etCertificationNumber;
	private Button btnConfirm;

	private int mode;
	private String requestedPhoneNumber; 
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.certifyPhoneNumberPage_titleBar);
		
		etPhoneNumber = (HoloStyleEditText) mThisView.findViewById(R.id.certifyPhoneNumberPage_etPhoneNumber);
		etCertificationNumber = (HoloStyleEditText) mThisView.findViewById(R.id.certifyPhoneNumberPage_etCertificationNumber);
		btnConfirm = (Button) mThisView.findViewById(R.id.certifyPhoneNumberPage_btnConfirm);
	}

	@Override
	public void setVariables() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createPage() {

		etPhoneNumber.setHint(R.string.hintForPhoneNumber);
		etPhoneNumber.setInputType(InputType.TYPE_CLASS_PHONE);
		
		etCertificationNumber.setHint(R.string.hintForCertificationNumber);
		etCertificationNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
	}

	@Override
	public void setListeners() {
		
		btnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(mode == MODE_PHONE_NUMBER) {

					if(checkPhoneNumberLength()) {
						sendCertiyingMessage();
					} else {
						ToastUtils.showToast(R.string.checkPhoneNumber);
					}
				} else {
					
					if(checkCertificationNumberLength()) {
						requestCertifying();
					} else {
						ToastUtils.showToast(R.string.checkCertificationNumber);
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
		
		//etPhoneNumber.
		rp = (RelativeLayout.LayoutParams) etPhoneNumber.getLayoutParams();
		rp.width = width;
		rp.height = textViewHeight;
		rp.topMargin = ResizeUtils.getSpecificLength(60);
		rp.bottomMargin = ResizeUtils.getSpecificLength(30);

		//etCertificationNumber.
		rp = (RelativeLayout.LayoutParams) etCertificationNumber.getLayoutParams();
		rp.width = width;
		rp.height = textViewHeight;
		rp.bottomMargin = ResizeUtils.getSpecificLength(30);
		
		//btnConfirm.
		rp = (RelativeLayout.LayoutParams) btnConfirm.getLayoutParams();
		rp.width = width;
		rp.height = buttonHeight;
		rp.bottomMargin = ResizeUtils.getSpecificLength(30);

		FontUtils.setFontAndHintSize(etPhoneNumber.getEditText(), 30, 24);
		FontUtils.setFontAndHintSize(etCertificationNumber.getEditText(), 30, 24);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_common_certify_phone_number;
	}

	@Override
	public int getPageTitleTextResId() {

		return R.string.pageTitle_certifyPhoneNumber;
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

		return R.id.certifyPhoneNumberPage_mainLayout;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		try {
			if(savedInstanceState != null) {
				
				if(savedInstanceState.containsKey("mode")) {
					mode = savedInstanceState.getInt("mode");
				}
				
				if(savedInstanceState.containsKey("etPhoneNumber")) {
					etPhoneNumber.getEditText().setText(savedInstanceState.getString("etPhoneNumber"));
				}
				
				if(savedInstanceState.containsKey("etCertificationNumber")) {
					etCertificationNumber.getEditText().setText(savedInstanceState.getString("etCertificationNumber"));
				}
				
			//이전 내용 복원.
			} else {
				SharedPreferences prefs = mContext.getSharedPreferences(BCPConstants.PREFS_CERTIFY, Context.MODE_PRIVATE);
				
				if(prefs.contains("phoneNumber")) {
					etPhoneNumber.getEditText().setText(prefs.getString("phoneNumber", null));
				}
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		try {
			outState.putInt("mode", mode);
			outState.putString("requestedPhoneNumber", requestedPhoneNumber);
			
			if(etPhoneNumber.getEditText().length() > 0) {
				outState.putString("etPhoneNumber", etPhoneNumber.getEditText().getText().toString());	
			}
			
			if(etCertificationNumber.getEditText().length() > 0) {
				outState.putString("etCertificationNumber", etCertificationNumber.getEditText().getText().toString());	
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	@Override
	public void onStop() {
		super.onStop();
		
		if(etPhoneNumber.getEditText().length() != 0) {
			SharedPrefsUtils.addDataToPrefs(BCPConstants.PREFS_CERTIFY, "phoneNumber", 
					etPhoneNumber.getEditText().getText().toString());
		}
	}
	
//////////////////// Custom methods.

	public boolean checkPhoneNumberLength() {
		
		if(etPhoneNumber.getEditText().getText() != null
				&& etPhoneNumber.getEditText().getText().length() == 11) {
			return true;
		}
		
		return false;
	}
	
	public boolean checkCertificationNumberLength() {
		
		if(etCertificationNumber.getEditText().getText() != null
				&& etCertificationNumber.getEditText().getText().length() == 4) {
			return true;
		}
		
		return false;
	}
	
	public void sendCertiyingMessage() {
		
		final String PHONE_NUMBER = etPhoneNumber.getEditText().getText().toString();
		String url = BCPAPIs.PHONE_AUTH_REQUEST_URL
				+ "?no_sms=0"
				+ "&phone_number=" + PHONE_NUMBER
				+ "&role=200";
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("CertifyPhoneNumberPage.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToSendSMS);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("CertifyPhoneNumberPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_sendSMS);
						etCertificationNumber.getEditText().requestFocus();
						btnConfirm.setBackgroundResource(R.drawable.phone_confirm_btn);
						mode = MODE_CERTIFICATION_NUMBER;
						requestedPhoneNumber = PHONE_NUMBER;
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToSendSMS);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					ToastUtils.showToast(R.string.failToSendSMS);
				}
			}
		}, mActivity.getLoadingView());
	}
	
	public void requestCertifying() {
		
		final String RESPONSE_KEY = etCertificationNumber.getEditText().getText().toString();
		
		//http://byecar.minsangk.com/users/auth/response?phone_number=010-2082-1803&response_key=1234
		String url = BCPAPIs.PHONE_AUTH_RESPONSE_URL
				+ "?phone_number=" + requestedPhoneNumber
				+ "&response_key=" + RESPONSE_KEY;
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("CertifyPhoneNumberPage.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToSendAuthRequest);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("CertifyPhoneNumberPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_certifyPhoneNumber);
						mActivity.bundle = new Bundle();
						mActivity.bundle.putString("requestedPhoneNumber", requestedPhoneNumber);
						mActivity.bundle.putString("phone_auth_key", 
								objJSON.getJSONObject("authResponse").getString("phone_auth_key"));
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