package com.byecar.byecarplus.fragments.main_for_user;

import org.json.JSONObject;

import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.MainActivity;
import com.byecar.byecarplus.R;
import com.byecar.byecarplus.classes.BCPAPIs;
import com.byecar.byecarplus.classes.BCPFragment;
import com.byecar.byecarplus.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo.holo_light.HoloStyleEditText;

public class CertifyPhoneNumberPage extends BCPFragment {

	private static int MODE_PHONE_NUMBER = 0;
	private static int MODE_CERTIFICATION_NUMBER = 1;
	
	private TextView tvCertifyPhoneNumber;
	private HoloStyleEditText etPhoneNumber;
	private HoloStyleEditText etCertificationNumber;
	private Button btnConfirm;

	private int mode;
	private String requestedPhoneNumber; 
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.certifyPhoneNumberPage_titleBar);
		
		tvCertifyPhoneNumber = (TextView) mThisView.findViewById(R.id.certifyPhoneNumberPage_tvCertifyPhoneNumber);
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
		
		//tvCertifyPhoneNumber.
		rp = (RelativeLayout.LayoutParams) tvCertifyPhoneNumber.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(30);
		rp.leftMargin = ResizeUtils.getSpecificLength(30);
		rp.topMargin = ResizeUtils.getSpecificLength(30);
		rp.bottomMargin = ResizeUtils.getSpecificLength(30);
		
		//etPhoneNumber.
		rp = (RelativeLayout.LayoutParams) etPhoneNumber.getLayoutParams();
		rp.width = width;
		rp.height = textViewHeight;
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

		FontUtils.setFontAndHintSize(etPhoneNumber.getEditText(), 30, 20);
		FontUtils.setFontAndHintSize(etCertificationNumber.getEditText(), 30, 20);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_certify_phone_number;
	}

	@Override
	public int getBackButtonResId() {

		return R.drawable.phone_reg_btn;
	}

	@Override
	public int getBackButtonWidth() {

		return 305;
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
	public int getRootViewResId() {

		return R.id.certifyPhoneNumberPage_mainLayout;
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
				+ "&phone_number=" + PHONE_NUMBER;
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
		});
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
						updatePhoneNumber(objJSON.getJSONObject("authResponse").getString("phone_auth_key"));
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
		});
	}
	
	public void updatePhoneNumber(String phone_auth_key) {

		String url = BCPAPIs.PHONE_UPDATE_URL
				+ "?phone_auth_key=" + phone_auth_key;
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
					ToastUtils.showToast(R.string.complete_auth);
					MainActivity.user.setPhone_number(requestedPhoneNumber);
					mActivity.closeTopPage();
				} catch (Exception e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToSendAuthRequest);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					ToastUtils.showToast(R.string.failToSendAuthRequest);
				}
			}
		});
	}
}
