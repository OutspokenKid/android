package com.cmons.cph.fragments.signin;

import java.net.URLEncoder;

import org.json.JSONObject;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragment;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class FindIdPwPage extends CmonsFragment {

	public static final int TYPE_FIND_ID = 0;
	public static final int TYPE_FIND_PW = 1;
	
	private int type;
	private String timeResponseKey;
	private String certifyingPhoneNumber;

	private TitleBar titleBar;
	private EditText etPhone;
	private EditText etCertification;
	private Button btnSend;
	private Button btnCertify;
	
	@Override
	public void bindViews() {
		
		titleBar = (TitleBar) mThisView.findViewById(R.id.findIdPwPage_titleBar);
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
	}

	@Override
	public void createPage() {
		
		if(type == TYPE_FIND_ID) {
			titleBar.setTitleText(R.string.findId);
		} else {
			titleBar.setTitleText(R.string.findPw);
		}
		
		View bottomBlank = new View(mContext);
		RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(10, ResizeUtils.getSpecificLength(110));
		rp.addRule(RelativeLayout.BELOW, R.id.findIdPwPage_btnCertify);
		bottomBlank.setLayoutParams(rp);
		((RelativeLayout) mThisView.findViewById(R.id.findIdPwPage_relative)).addView(bottomBlank);
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
		
		//ScrollView.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.findIdPwPage_scrollView).getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(96);
		
		//etPhone.
		rp = (RelativeLayout.LayoutParams) etPhone.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(92);
		rp.topMargin = ResizeUtils.getSpecificLength(70);
		etPhone.setPadding(padding, 0, padding, 0);
		
		//btnSend.
		rp = (RelativeLayout.LayoutParams) btnSend.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(74);
		rp.topMargin = ResizeUtils.getSpecificLength(12);
		
		//etCertification.
		rp = (RelativeLayout.LayoutParams) etCertification.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(92);
		rp.topMargin = ResizeUtils.getSpecificLength(12);
		etCertification.setPadding(padding, 0, padding, 0);
		
		//btnCertify.
		rp = (RelativeLayout.LayoutParams) btnCertify.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(74);
		rp.topMargin = ResizeUtils.getSpecificLength(12);
		
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.findIdPwPage_ivCopyright).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(352);
		rp.height = ResizeUtils.getSpecificLength(18);
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);
	}
	
	@Override
	public int getContentViewId() {

		return R.layout.fragment_findidpw;
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
		
		String url = CphConstants.BASE_API_URL + "users/auth/request" +
				"?no_sms=0" +
				"&phone_number=" + certifyingPhoneNumber;

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
						timeResponseKey = objJSON.getString("tempResponseKey");
						btnSend.setVisibility(View.INVISIBLE);
						etCertification.setVisibility(View.VISIBLE);
						btnCertify.setVisibility(View.VISIBLE);
						
						ToastUtils.showToast(R.string.sendCertificationCompleted);
					} else {
						ToastUtils.showToast(R.string.wrongPhoneNumber);
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
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
					"&no_sms=1";
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

}
