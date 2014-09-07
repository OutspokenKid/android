package com.cmons.cph.fragments.signin;

import java.net.URLEncoder;

import org.json.JSONObject;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragment;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class FindIdPwPage extends CmonsFragment {

	public static final int TYPE_FIND_ID = 0;
	public static final int TYPE_FIND_PW = 1;
	
	private EditText etId;
	private EditText etPhone;
	private EditText etCertification;
	private Button btnSend;
	private Button btnCertify;
	
	private int type;
	private String timeResponseKey;
	private String certifyingPhoneNumber;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.findIdPwPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.findIdPwPage_ivBg);
		
		etId = (EditText) mThisView.findViewById(R.id.findIdPwPage_etId);
		etPhone = (EditText) mThisView.findViewById(R.id.findIdPwPage_etPhone);
		etCertification = (EditText) mThisView.findViewById(R.id.findIdPwPage_etCertification);
		btnSend = (Button) mThisView.findViewById(R.id.findIdPwPage_btnSend);
		btnCertify = (Button) mThisView.findViewById(R.id.findIdPwPage_btnCertify);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {
			type = getArguments().getInt("type", TYPE_FIND_ID);
		}
		
		if(type == TYPE_FIND_ID) {
			title = getString(R.string.findId);
		} else {
			title = getString(R.string.findPw);
		}
	}

	@Override
	public void createPage() {
		
		titleBar.getBackButton().setVisibility(View.VISIBLE);
		
		if(type == TYPE_FIND_ID) {
			etId.setVisibility(View.GONE);
		} else {
			etId.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void setListeners() {

		titleBar.getBackButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		
		etPhone.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				
				if (actionId == EditorInfo.IME_ACTION_SEND) {
					checkPhone();
				}
				
				return false;
			}
		});
		
		etCertification.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				
				if (actionId == EditorInfo.IME_ACTION_SEND) {
					checkCertification();
				}
				
				return false;
			}
		});
	
		btnSend.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				if(type == TYPE_FIND_PW && 
						(etId.getText() == null
						|| StringUtils.isEmpty(etId.getText().toString()))) {
					ToastUtils.showToast(R.string.wrongId);
					return;
				}
				
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

		int padding = ResizeUtils.getSpecificLength(30);
		RelativeLayout.LayoutParams rp = null;
		
		//Shadow.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.findIdPwPage_titleShadow).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(14);

		//etId.
		rp = (RelativeLayout.LayoutParams) etId.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		rp.topMargin = ResizeUtils.getSpecificLength(70);
		etPhone.setPadding(padding, 0, padding, 0);
		
		//etPhone.
		rp = (RelativeLayout.LayoutParams) etPhone.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		etPhone.setPadding(padding, 0, padding, 0);
		
		//etCertification.
		rp = (RelativeLayout.LayoutParams) etCertification.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(448);
		rp.height = ResizeUtils.getSpecificLength(92);
		etCertification.setPadding(padding, 0, padding, 0);
		
		//btnSend.
		rp = (RelativeLayout.LayoutParams) btnSend.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//btnCertify.
		rp = (RelativeLayout.LayoutParams) btnCertify.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		
		FontUtils.setFontAndHintSize(etId, 30, 24);
		FontUtils.setFontAndHintSize(etPhone, 30, 24);
		FontUtils.setFontAndHintSize(etCertification, 30, 24);
	}
	
	@Override
	public int getContentViewId() {

		return R.layout.fragment_sign_in_findidpw;
	}

	@Override
	public void refreshPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean parseJSON(JSONObject objJSON) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void checkPhone() {

		if(etPhone.getText() == null
				|| StringUtils.isEmpty(etPhone.getText().toString())) {
			ToastUtils.showToast(R.string.wrongPhoneNumber);
			return;
		}
		
		certifyingPhoneNumber = etPhone.getText().toString();
		
		try {
			String url = CphConstants.BASE_API_URL + "users/auth/request" +
					"?no_sms=0" +
					"&phone_number=" + URLEncoder.encode(certifyingPhoneNumber, "utf-8");

			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("FindIdPwPage.onError." + "\nurl : " + url);
					ToastUtils.showToast(R.string.wrongPhoneNumber);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("FindIdPwPage.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);
						
						if(objJSON.getInt("result") == 1) {
							ToastUtils.showToast(R.string.complete_checkPhone);
							
							timeResponseKey = objJSON.getString("tempResponseKey");
							btnSend.setVisibility(View.INVISIBLE);
							etCertification.setVisibility(View.VISIBLE);
							btnCertify.setVisibility(View.VISIBLE);
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
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void checkCertification() {
		
		if(timeResponseKey.equals(etCertification.getText().toString())) {
			
			String url = CphConstants.BASE_API_URL + "users/auth/response" +
					"?phone_number=" + certifyingPhoneNumber +
					"&response_key=" + timeResponseKey;
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("FindIdPwPage.onError." + "\nurl : " + url);
					ToastUtils.showToast(R.string.wrongCertificationNumber);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("FindIdPwPage.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);
						
						if(objJSON.getInt("result") == 1
								&& objJSON.getJSONObject("authResponse") != null) {
							
							JSONObject objResponse = objJSON.getJSONObject("authResponse");
							
							if(objResponse.getString("phone_number") != null) {
								findIdPw(objResponse.getString("phone_number"));
							} else {
								ToastUtils.showToast(objJSON.getString("message"));
							}
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
		} else {
			ToastUtils.showToast(R.string.wrongCertificationNumber);
		}
	}
	
	public void findIdPw(String phone_number) {
		
		try {
			String url = CphConstants.BASE_API_URL + "users/find/";
			
			if(type == TYPE_FIND_ID) {
				url += "id";
			} else {
				url += "password";
				
			}
					
			url += "?phone_number=" + URLEncoder.encode(phone_number, "utf-8") +
					"&no_sms=0";
			
			if(type == TYPE_FIND_PW) {
				url += "&id=" + etId.getText().toString();
			}
			
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("FindIdPwPage.onError." + "\nurl : " + url);
					
					if(type == TYPE_FIND_ID) {
						ToastUtils.showToast(R.string.failToFindId);
					} else {
						ToastUtils.showToast(R.string.failToFindPw);
					}
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("FindIdPwPage.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						if(objJSON.getInt("result") == 1) {
							if(type == TYPE_FIND_ID) {
								ToastUtils.showToast(R.string.sendIdCompleted);
							} else {
								ToastUtils.showToast(R.string.sendPwCompleted);
							}
							
							getActivity().getSupportFragmentManager().popBackStack();
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
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
	}

	@Override
	public int getBgResourceId() {

		return R.drawable.bg2;
	}
}
