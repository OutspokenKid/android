package com.cmons.cph.fragments.common;

import java.net.URLEncoder;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForShop;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class ChangePhoneNumberPage extends CmonsFragmentForShop {

	private TextView tvPhoneNumber;
	private EditText etPhoneNumber;
	private EditText etCertify;
	private Button btnSendCertification;
	private Button btnCertify;
	
	private String phone_number;
	private String phone_auth_key;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.commonChangePhoneNumberPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.commonChangePhoneNumberPage_ivBg);
		
		tvPhoneNumber = (TextView) mThisView.findViewById(R.id.commonChangePhoneNumberPage_tvPhoneNumber);
		etPhoneNumber = (EditText) mThisView.findViewById(R.id.commonChangePhoneNumberPage_etPhoneNumber);
		etCertify = (EditText) mThisView.findViewById(R.id.commonChangePhoneNumberPage_etCertify);
		btnSendCertification = (Button) mThisView.findViewById(R.id.commonChangePhoneNumberPage_btnSendCertification);
		btnCertify = (Button) mThisView.findViewById(R.id.commonChangePhoneNumberPage_btnCertify);
	}

	@Override
	public void setVariables() {

		title = "전화번호 변경";
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		titleBar.getHomeButton().setVisibility(View.VISIBLE);
	}

	@Override
	public void setListeners() {

		btnSendCertification.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				checkPhone();
			}
		});
		
		btnCertify.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				checkCertification();
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		int p = ResizeUtils.getSpecificLength(10);
		
		//tvPhoneNumber.
		rp = (RelativeLayout.LayoutParams) tvPhoneNumber.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		tvPhoneNumber.setPadding(p, 0, 0, p);
		FontUtils.setFontSize(tvPhoneNumber, 30);
		
		//etPhoneNumber.
		rp = (RelativeLayout.LayoutParams) etPhoneNumber.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		FontUtils.setFontAndHintSize(etPhoneNumber, 30, 24);
		
		//etCertify.
		rp = (RelativeLayout.LayoutParams) etCertify.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(448);
		rp.height = ResizeUtils.getSpecificLength(92);
		FontUtils.setFontAndHintSize(etCertify, 30, 24);
		
		//btnSendCertification.
		rp = (RelativeLayout.LayoutParams) btnSendCertification.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//btnCertify.
		rp = (RelativeLayout.LayoutParams) btnCertify.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_common_changephonenumber;
	}

	@Override
	public void refreshPage() {
		// TODO Auto-generated method stub

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
	public boolean parseJSON(JSONObject objJSON) {
		// TODO Auto-generated method stub
		return false;
	}

//////////////////// Custom classes.

	public void checkPhone() {

		if(StringUtils.checkTextLength(etPhoneNumber, 6, 15) != StringUtils.PASS) {
			ToastUtils.showToast(R.string.wrongPhoneNumber);
			return;
		}
		
		try {
			phone_number = etPhoneNumber.getText().toString();
			
			String url = CphConstants.BASE_API_URL + "users/auth/request" +
					"?no_sms=0" +
					"&find=0" +
					"&phone_number=" + URLEncoder.encode(phone_number, "utf-8");

			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("ChangePhoneNumberPage.onError." + "\nurl : " + url);
					ToastUtils.showToast(R.string.wrongPhoneNumber);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("ChangePhoneNumberPage.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						if(objJSON.getInt("result") == 1) {
							ToastUtils.showToast(R.string.complete_checkPhone);
							btnSendCertification.setVisibility(View.INVISIBLE);
							btnCertify.setVisibility(View.VISIBLE);
						} else {
							ToastUtils.showToast(objJSON.getString("message"));
						}
					} catch (Exception e) {
						LogUtils.trace(e);
						ToastUtils.showToast(R.string.wrongPhoneNumber);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
						ToastUtils.showToast(R.string.wrongPhoneNumber);
					}
				}
			});
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void checkCertification() {

		try {
			if(etCertify.getText() == null
					&& etCertify.getText().toString().length() != 4) {
				ToastUtils.showToast(R.string.wrongCertificationNumber);
				return;
			}
			
			String url = CphConstants.BASE_API_URL + "users/auth/response" +
					"?phone_number=" + URLEncoder.encode(phone_number, "utf-8") +
					"&response_key=" + etCertify.getText().toString();
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("ChangePhoneNumberPage.onError." + "\nurl : " + url);
					ToastUtils.showToast(R.string.wrongCertificationNumber);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("ChangePhoneNumberPage.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);
						
						if(objJSON.getInt("result") == 1) {
							JSONObject objResponse = objJSON.getJSONObject("authResponse");
							phone_number = objResponse.getString("phone_number");
							phone_auth_key = objResponse.getString("phone_auth_key");
							changePhoneNumber();
						} else {
							ToastUtils.showToast(objJSON.getString("message"));
						}
					} catch (Exception e) {
						ToastUtils.showToast(R.string.wrongCertificationNumber);
						LogUtils.trace(e);
					} catch (OutOfMemoryError oom) {
						ToastUtils.showToast(R.string.wrongCertificationNumber);
						LogUtils.trace(oom);
					}
				}
			});
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}	
	
	public void changePhoneNumber() {

		String url = CphConstants.BASE_API_URL + "users/update/phone_number" +
				"?phone_auth_key=" + phone_auth_key;
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("ChangePhoneNumberPage.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToChangePhoneNumber);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("ChangePhoneNumberPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_changePhoneNumber);
						mActivity.closeTopPage();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToChangePhoneNumber);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					ToastUtils.showToast(R.string.failToChangePhoneNumber);
				}
			}
		});
	}

	@Override
	public int getBgResourceId() {

		return R.drawable.setting_bg2;
	}
}
