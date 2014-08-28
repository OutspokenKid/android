package com.cmons.cph.fragments.signup;

import java.net.URLEncoder;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.SignUpActivity;
import com.cmons.cph.classes.CmonsFragmentForSignUp;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Retail;
import com.cmons.cph.models.Shop;
import com.cmons.cph.models.Wholesale;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class SignUpForPersonalPage extends CmonsFragmentForSignUp {
	
	private TextView tvCompanyName1;
	private TextView tvCompanyName2;
	
	private TextView tvCompanyPhone1;
	private TextView tvCompanyPhone2;
	
	private TextView tvCompanyLocation1;
	private TextView tvCompanyLocation2;
	
	private TextView tvName;
	private EditText etName;
	
	private TextView tvId;
	private TextView tvCheckId;
	private EditText etId;
	
	private TextView tvPw;
	private TextView tvCheckPw;
	private EditText etPw;
	
	private TextView tvPwConfirm;
	private TextView tvCheckConfirmPw;
	private EditText etPwConfirm;
	
	private TextView tvPhone;
	private EditText etPhone;
	private EditText etCertification;
	private Button btnSendCertification;
	private Button btnCertify;
	
	private int type;
	private Shop shop;
	private String categoryString;
	private String userName;
	private boolean focusOnId, focusOnPw, focusOnPwConfirm;
	private String phone_number;
	private String timeResponseKey;
	private String phone_auth_key;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.signUpForPersonalPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.signUpForPersonalPage_ivBg);
		
		tvCompanyName1 = (TextView) mThisView.findViewById(R.id.signUpForPersonalPage_tvCompanyName1);
		tvCompanyName2 = (TextView) mThisView.findViewById(R.id.signUpForPersonalPage_tvCompanyName2);
		
		tvCompanyPhone1 = (TextView) mThisView.findViewById(R.id.signUpForPersonalPage_tvCompanyPhone1);
		tvCompanyPhone2 = (TextView) mThisView.findViewById(R.id.signUpForPersonalPage_tvCompanyPhone2);
		
		tvCompanyLocation1 = (TextView) mThisView.findViewById(R.id.signUpForPersonalPage_tvCompanyLocation1);
		tvCompanyLocation2 = (TextView) mThisView.findViewById(R.id.signUpForPersonalPage_tvCompanyLocation2);
		
		tvName = (TextView) mThisView.findViewById(R.id.signUpForPersonalPage_tvName);
		etName = (EditText) mThisView.findViewById(R.id.signUpForPersonalPage_etName);
		
		tvId = (TextView) mThisView.findViewById(R.id.signUpForPersonalPage_tvId);
		tvCheckId = (TextView) mThisView.findViewById(R.id.signUpForPersonalPage_tvCheckId);
		etId = (EditText) mThisView.findViewById(R.id.signUpForPersonalPage_etId);
		
		tvPw = (TextView) mThisView.findViewById(R.id.signUpForPersonalPage_tvPw);
		tvCheckPw = (TextView) mThisView.findViewById(R.id.signUpForPersonalPage_tvCheckPw);
		etPw = (EditText) mThisView.findViewById(R.id.signUpForPersonalPage_etPw);
		
		tvPwConfirm = (TextView) mThisView.findViewById(R.id.signUpForPersonalPage_tvConfirmPw);
		tvCheckConfirmPw = (TextView) mThisView.findViewById(R.id.signUpForPersonalPage_tvCheckConfirmPw);
		etPwConfirm = (EditText) mThisView.findViewById(R.id.signUpForPersonalPage_etConfirmPw);
		
		tvPhone = (TextView) mThisView.findViewById(R.id.signUpForPersonalPage_tvPhone);
		etPhone = (EditText) mThisView.findViewById(R.id.signUpForPersonalPage_etPhone);
		
		etCertification = (EditText) mThisView.findViewById(R.id.signUpForPersonalPage_etCertification);
		btnSendCertification = (Button) mThisView.findViewById(R.id.signUpForPersonalPage_btnSendCertification);
		btnCertify = (Button) mThisView.findViewById(R.id.signUpForPersonalPage_btnCertify);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {
			
			if(getArguments().containsKey("type")) {
				type = getArguments().getInt("type");
				
			}
			
			shop = (Shop) getArguments().getSerializable("shop");
			categoryString = getArguments().getString("categoryString");
		}
		
		title = getString(R.string.inputUserInfo);
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		
		switch(type) {
		
		case SignUpActivity.BUSINESS_WHOLESALE + SignUpActivity.POSITION_OWNER:
		case SignUpActivity.BUSINESS_WHOLESALE + SignUpActivity.POSITION_EMPLOYEE1:
		case SignUpActivity.BUSINESS_WHOLESALE + SignUpActivity.POSITION_EMPLOYEE2:
			tvCompanyPhone1.setText(R.string.phoneNumberForWholesale);
			tvCompanyLocation1.setText(R.string.addressForWholesale);

			tvCompanyName1.setVisibility(View.VISIBLE);
			tvCompanyName2.setVisibility(View.VISIBLE);
			tvCompanyPhone1.setVisibility(View.VISIBLE);
			tvCompanyName2.setVisibility(View.VISIBLE);
			tvCompanyLocation1.setVisibility(View.VISIBLE);
			tvCompanyLocation2.setVisibility(View.VISIBLE);
			tvName.setVisibility(View.VISIBLE);
			etName.setVisibility(View.VISIBLE);
			break;
		
		case SignUpActivity.BUSINESS_RETAIL_OFFLINE + SignUpActivity.POSITION_EMPLOYEE1:
		case SignUpActivity.BUSINESS_RETAIL_OFFLINE + SignUpActivity.POSITION_EMPLOYEE2:
		case SignUpActivity.BUSINESS_RETAIL_ONLINE + SignUpActivity.POSITION_EMPLOYEE1:
		case SignUpActivity.BUSINESS_RETAIL_ONLINE + SignUpActivity.POSITION_EMPLOYEE2:
			tvCompanyPhone1.setText(R.string.phoneNumberForRetail);
			tvCompanyLocation1.setText(R.string.addressForRetail);
		
			tvCompanyName1.setVisibility(View.VISIBLE);
			tvCompanyName2.setVisibility(View.VISIBLE);
			tvCompanyPhone1.setVisibility(View.VISIBLE);
			tvCompanyName2.setVisibility(View.VISIBLE);
			tvCompanyLocation1.setVisibility(View.VISIBLE);
			tvCompanyLocation2.setVisibility(View.VISIBLE);
			tvName.setVisibility(View.VISIBLE);
			etName.setVisibility(View.VISIBLE);
			break;
			
		default:
			tvCompanyName1.setVisibility(View.GONE);
			tvCompanyName2.setVisibility(View.GONE);
			tvCompanyPhone1.setVisibility(View.GONE);
			tvCompanyPhone2.setVisibility(View.GONE);
			tvCompanyLocation1.setVisibility(View.GONE);
			tvCompanyLocation2.setVisibility(View.GONE);
			tvName.setVisibility(View.GONE);
			etName.setVisibility(View.GONE);
		}
		
		if(shop != null) {
			tvCompanyName2.setText(shop.getName());
			tvCompanyPhone2.setText(shop.getPhone_number());
			
			if(shop instanceof Wholesale) {
				tvCompanyLocation2.setText(((Wholesale)shop).getLocation());
			} else if(shop instanceof Retail) {
				tvCompanyLocation2.setText(((Retail)shop).getAddress());
			}
		}
	}

	@Override
	public void setListeners() {

		etId.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				//포커스를 갖고 있다가 잃은 시점.
				if(focusOnId && !hasFocus) {
					checkId();
				}
				
				focusOnId = hasFocus;
			}
		});
		
		etPw.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				//포커스를 갖고 있다가 잃은 시점.
				if(focusOnPw && !hasFocus) {
					checkPw();
				}
				
				focusOnPw = hasFocus;
			}
		});
		
		etPwConfirm.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				//포커스를 갖고 있다가 잃은 시점.
				if(focusOnPwConfirm && !hasFocus) {
					checkPwConfirm();
				}
				
				focusOnPwConfirm = hasFocus;
			}
		});
		
		titleBar.getBackButton().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		
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
		
		titleBar.getBtnSubmit().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				checkInfo();
			}
		});
	}

	@Override
	public void setSizes() {
		
		RelativeLayout.LayoutParams rp = null;
		
		//shadow.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.signUpForPersonalPage_titleShadow).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(14);
		
		//tvCompanyName1.
		rp = (RelativeLayout.LayoutParams) tvCompanyName1.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(90);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		
		//tvCompanyName2.
		rp = (RelativeLayout.LayoutParams) tvCompanyName2.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//tvCompanyPhone1.
		rp = (RelativeLayout.LayoutParams) tvCompanyPhone1.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(120);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		
		//tvCompanyPhone2.
		rp = (RelativeLayout.LayoutParams) tvCompanyPhone2.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//tvCompanyLocation1.
		rp = (RelativeLayout.LayoutParams) tvCompanyLocation1.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(120);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		
		//tvCompanyLocation2.
		rp = (RelativeLayout.LayoutParams) tvCompanyLocation2.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//tvName.
		rp = (RelativeLayout.LayoutParams) tvName.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(120);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		
		//etName.
		rp = (RelativeLayout.LayoutParams) etName.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//tvId.
		rp = (RelativeLayout.LayoutParams) tvId.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(120);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		
		//tvCheckId.
		rp = (RelativeLayout.LayoutParams) tvCheckId.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(90);
		rp.leftMargin = ResizeUtils.getSpecificLength(30);
		
		//etId.
		rp = (RelativeLayout.LayoutParams) etId.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//tvPw.
		rp = (RelativeLayout.LayoutParams) tvPw.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(120);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		
		//tvCheckPw.
		rp = (RelativeLayout.LayoutParams) tvCheckPw.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(90);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		
		//etPw.
		rp = (RelativeLayout.LayoutParams) etPw.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//tvPwConfirm.
		rp = (RelativeLayout.LayoutParams) tvPwConfirm.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(120);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		
		//tvCheckConfirmPw.
		rp = (RelativeLayout.LayoutParams) tvCheckConfirmPw.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(90);
		rp.leftMargin = ResizeUtils.getSpecificLength(30);
		
		//etPwConfirm.
		rp = (RelativeLayout.LayoutParams) etPwConfirm.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//tvPhone.
		rp = (RelativeLayout.LayoutParams) tvPhone.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(120);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		
		//etPhone.
		rp = (RelativeLayout.LayoutParams) etPhone.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//etCertification.
		rp = (RelativeLayout.LayoutParams) etCertification.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(448);
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//btnSendCertification.
		rp = (RelativeLayout.LayoutParams) btnSendCertification.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(272);
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//btnCertify.
		rp = (RelativeLayout.LayoutParams) btnCertify.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(272);
		rp.height = ResizeUtils.getSpecificLength(92);
		
		FontUtils.setFontSize(tvCompanyName1, 30);
		FontUtils.setFontSize(tvCompanyName2, 30);
		FontUtils.setFontSize(tvCompanyPhone1, 30);
		FontUtils.setFontSize(tvCompanyPhone2, 30);
		FontUtils.setFontSize(tvCompanyLocation1, 30);
		FontUtils.setFontSize(tvCompanyLocation2, 30);

		FontUtils.setFontSize(tvName, 30);
		FontUtils.setFontSize(tvId, 30);
		FontUtils.setFontSize(tvPw, 30);
		FontUtils.setFontSize(tvPwConfirm, 30);
		FontUtils.setFontSize(tvPhone, 30);
		
		FontUtils.setFontSize(tvCheckId, 22);
		FontUtils.setFontSize(tvCheckPw, 22);
		FontUtils.setFontSize(tvCheckConfirmPw, 22);

		FontUtils.setFontAndHintSize(etName, 30, 24);
		FontUtils.setFontAndHintSize(etId, 30, 24);
		FontUtils.setFontAndHintSize(etPw, 30, 24);
		FontUtils.setFontAndHintSize(etPwConfirm, 30, 24);
		FontUtils.setFontAndHintSize(etName, 30, 24);
		FontUtils.setFontAndHintSize(etPhone, 30, 24);
		FontUtils.setFontAndHintSize(etCertification, 30, 24);
	}
	
	@Override
	public int getContentViewId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_sign_up_personal;
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
	
	public boolean checkName() {

		if(StringUtils.checkTextLength(etName, 0, 10) != StringUtils.PASS
				|| StringUtils.checkForbidContains(etId, false, false, true, false, true, true)) {
			return false;
		} else{
			return true;
		}
	}
	
	public boolean checkId() {

		if(StringUtils.checkTextLength(etId, 6, 64) != StringUtils.PASS
				|| StringUtils.checkForbidContains(etId, false, true, false, true, true, true)) {
			tvCheckId.setVisibility(View.VISIBLE);
			return false;
		} else{
			tvCheckId.setVisibility(View.INVISIBLE);
			return true;
		}
	}
	
	public boolean checkPw() {
		
		if(StringUtils.checkTextLength(etPw, 6, 64) != StringUtils.PASS
				|| StringUtils.checkForbidContains(etPw, false, true, false, true, true, true)) {
			tvCheckPw.setVisibility(View.VISIBLE);
			return false;
		} else{
			tvCheckPw.setVisibility(View.INVISIBLE);
			return true;
		}
	}
	
	public boolean checkPwConfirm() {
		
		if(etPw.getText() != null 
				&& etPwConfirm.getText() != null
				&& !etPw.getText().toString().equals(etPwConfirm.getText().toString())) {
			tvCheckConfirmPw.setVisibility(View.VISIBLE);
			return false;
		} else{
			tvCheckConfirmPw.setVisibility(View.INVISIBLE);
			return true;
		}
	}

	public void checkPhone() {

		if(StringUtils.checkTextLength(etPhone, 6, 15) != StringUtils.PASS) {
			ToastUtils.showToast(R.string.wrongPhoneNumber);
			return;
		}
		
		phone_number = etPhone.getText().toString();
		
		try {
			String url = CphConstants.BASE_API_URL + "users/auth/request" +
					"?no_sms=0" +
					"&phone_number=" + URLEncoder.encode(phone_number, "utf-8");

			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("SignUpForPersonalPage.onError." + "\nurl : " + url);
					ToastUtils.showToast(R.string.wrongPhoneNumber);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("SignUpForPersonalPage.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						
						if(objJSON.getInt("result") == 1) {
							ToastUtils.showToast(R.string.complete_checkPhone);
							timeResponseKey = objJSON.getString("tempResponseKey");
							
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
		
		if(timeResponseKey.equals(etCertification.getText().toString())) {
			
			try {
				String url = CphConstants.BASE_API_URL + "users/auth/response" +
						"?phone_number=" + URLEncoder.encode(phone_number, "utf-8") +
						"&response_key=" + timeResponseKey;
				DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

					@Override
					public void onError(String url) {

						LogUtils.log("SignUpForPersonalPage.onError." + "\nurl : " + url);
						ToastUtils.showToast(R.string.wrongCertificationNumber);
					}

					@Override
					public void onCompleted(String url, JSONObject objJSON) {

						try {
							LogUtils.log("SignUpForPersonalPage.onCompleted." + "\nurl : " + url
									+ "\nresult : " + objJSON);

							
							if(objJSON.getInt("result") == 1) {
								ToastUtils.showToast(R.string.complete_certification);
								JSONObject objResponse = objJSON.getJSONObject("authResponse");
								phone_number = objResponse.getString("phone_number");
								phone_auth_key = objResponse.getString("phone_auth_key");
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
		} else {
			ToastUtils.showToast(R.string.wrongCertificationNumber);
		}
	}

	public void checkInfo() {

		if(userName == null && etName.getText() != null) {
			userName = etName.getText().toString();
		}
		
		if(userName == null) {
			ToastUtils.showToast(R.string.wrongCompanyOwner);
			
		} else if(!checkId()) {
			tvCheckId.setVisibility(View.VISIBLE);
			ToastUtils.showToast(R.string.check_id);
			
		} else if(!checkPw()) {
			tvCheckPw.setVisibility(View.VISIBLE);
			ToastUtils.showToast(R.string.check_pw);
			
		} else if(!checkPwConfirm()) {
			tvCheckConfirmPw.setVisibility(View.VISIBLE);
			ToastUtils.showToast(R.string.check_pwConfirm);
			
		} else if(StringUtils.isEmpty(phone_auth_key)) {
			ToastUtils.showToast(R.string.wrongCertificationNumber);
			
		} else {
			
			switch(type) {

			case SignUpActivity.BUSINESS_WHOLESALE + SignUpActivity.POSITION_EMPLOYEE1:
			case SignUpActivity.BUSINESS_WHOLESALE + SignUpActivity.POSITION_EMPLOYEE2:
			
			case SignUpActivity.BUSINESS_WHOLESALE + SignUpActivity.POSITION_OWNER:
				mActivity.signUpForWholesale(
						etId.getText().toString(), 		//id
						etPw.getText().toString(), 		//pw
						"" + type, 						//type
						userName, 						//user id
						"" + shop.getId(), 				//shop id
						categoryString, 				//category
						phone_auth_key);				//phone certification
				break;
				
			case SignUpActivity.BUSINESS_RETAIL_OFFLINE + SignUpActivity.POSITION_OWNER:
			case SignUpActivity.BUSINESS_RETAIL_ONLINE + SignUpActivity.POSITION_OWNER:
				mActivity.signUpForRetailOwner(
						etId.getText().toString(), 		//id
						etPw.getText().toString(), 		//pw
						"" + type, 						//type
						userName, 						//user id 
						shop.getName(), 
						((Retail)shop).getAddress(), 
						((Retail)shop).getMall_url(), 
						shop.getCorp_reg_number(), 
						phone_auth_key);
				break;

			case SignUpActivity.BUSINESS_RETAIL_OFFLINE + SignUpActivity.POSITION_EMPLOYEE1:
			case SignUpActivity.BUSINESS_RETAIL_OFFLINE + SignUpActivity.POSITION_EMPLOYEE2:
			case SignUpActivity.BUSINESS_RETAIL_ONLINE + SignUpActivity.POSITION_EMPLOYEE1:
			case SignUpActivity.BUSINESS_RETAIL_ONLINE + SignUpActivity.POSITION_EMPLOYEE2:
				userName = etName.getText().toString();
				mActivity.signUpForRetailEmployee(
						etId.getText().toString(), 		//id
						etPw.getText().toString(), 		//pw
						"" + type, 						//type
						userName, 						//user id
						"" + shop.getId(), 
						phone_auth_key);
				break;
			}
		}
	}

	@Override
	public int getBgResourceId() {

		return R.drawable.bg2;
	}
}
