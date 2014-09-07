package com.cmons.cph.fragments.signup;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.SignUpActivity;
import com.cmons.cph.classes.CmonsFragmentForSignUp;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class SignUpForWritePage extends CmonsFragmentForSignUp {

	private TextView tvCompanyName;
	private EditText etCompanyName;
	
	private TextView tvMallUrl;
	private EditText etMallUrl;
	
	private TextView tvCompanyAddress;
	private EditText etCompanyAddress;
	
	private TextView tvCompanyOwnerName;
	private EditText etCompanyOwnerName;
	
	private TextView tvCompanyPhone;
	private EditText etCompanyPhone;
	
	private TextView tvCompanyRegistration;
	private EditText etCompanyRegistration;
	
	private int type;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.signUpForWritePage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.signUpForWritePage_ivBg);
		
		tvCompanyName = (TextView) mThisView.findViewById(R.id.signUpForWritePage_tvCompanyName);
		etCompanyName = (EditText) mThisView.findViewById(R.id.signUpForWritePage_etCompanyName);
		
		tvMallUrl = (TextView) mThisView.findViewById(R.id.signUpForWritePage_tvMallUrl);
		etMallUrl = (EditText) mThisView.findViewById(R.id.signUpForWritePage_etMallUrl);
		
		tvCompanyAddress = (TextView) mThisView.findViewById(R.id.signUpForWritePage_tvCompanyAddress);
		etCompanyAddress = (EditText) mThisView.findViewById(R.id.signUpForWritePage_etCompanyAddress);
		
		tvCompanyOwnerName = (TextView) mThisView.findViewById(R.id.signUpForWritePage_tvCompanyOwnerName);
		etCompanyOwnerName = (EditText) mThisView.findViewById(R.id.signUpForWritePage_etCompanyOwnerName);
		
		tvCompanyPhone = (TextView) mThisView.findViewById(R.id.signUpForWritePage_tvCompanyPhone);
		etCompanyPhone = (EditText) mThisView.findViewById(R.id.signUpForWritePage_etCompanyPhone);
		
		tvCompanyRegistration = (TextView) mThisView.findViewById(R.id.signUpForWritePage_tvCompanyRegistration);
		etCompanyRegistration = (EditText) mThisView.findViewById(R.id.signUpForWritePage_etCompanyRegistration);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null && getArguments().containsKey("type")) {
			type = getArguments().getInt("type");
		}
		
		title = getString(R.string.inputCompanyInfo);
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		
		//소매 - 오프라인.
		if(type < SignUpActivity.BUSINESS_RETAIL_ONLINE) {
			tvMallUrl.setVisibility(View.GONE);
			etMallUrl.setVisibility(View.GONE);
		}
	}

	@Override
	public void setListeners() {
		
		titleBar.getBtnNext().setOnClickListener(new OnClickListener() {

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
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.signUpForWritePage_titleShadow).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(14);
		
		// tvCompanyName.
		rp = (RelativeLayout.LayoutParams) tvCompanyName.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(120);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		
		// etCompanyName.
		rp = (RelativeLayout.LayoutParams) etCompanyName.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		
		// tvMallUrl.
		rp = (RelativeLayout.LayoutParams) tvMallUrl.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(120);
		
		// etMallUrl.
		rp = (RelativeLayout.LayoutParams) etMallUrl.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		
		// tvCompanyAddress.
		rp = (RelativeLayout.LayoutParams) tvCompanyAddress.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(120);
		
		// etCompanyAddress.
		rp = (RelativeLayout.LayoutParams) etCompanyAddress.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		
		// tvCompanyOwnerName.
		rp = (RelativeLayout.LayoutParams) tvCompanyOwnerName.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(120);
		
		// etCompanyOwnerName.
		rp = (RelativeLayout.LayoutParams) etCompanyOwnerName.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);

		// tvCompanyPhone.
		rp = (RelativeLayout.LayoutParams) tvCompanyPhone.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(120);
		
		// etCompanyPhone.
		rp = (RelativeLayout.LayoutParams) etCompanyPhone.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		
		// tvCompanyRegistration.
		rp = (RelativeLayout.LayoutParams) tvCompanyRegistration.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(120);
		
		// etCompanyRegistration.
		rp = (RelativeLayout.LayoutParams) etCompanyRegistration.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		
		FontUtils.setFontSize(tvCompanyName, 34);
		FontUtils.setFontSize(tvMallUrl, 34);
		FontUtils.setFontSize(tvCompanyAddress, 34);
		FontUtils.setFontSize(tvCompanyOwnerName, 34);
		FontUtils.setFontSize(tvCompanyPhone, 34);
		FontUtils.setFontSize(tvCompanyRegistration, 34);
		
		FontUtils.setFontAndHintSize(etCompanyName, 30, 24);
		FontUtils.setFontAndHintSize(etMallUrl, 30, 24);
		FontUtils.setFontAndHintSize(etCompanyAddress, 30, 24);
		FontUtils.setFontAndHintSize(etCompanyOwnerName, 30, 24);
		FontUtils.setFontAndHintSize(etCompanyPhone, 30, 24);
		FontUtils.setFontAndHintSize(etCompanyRegistration, 30, 24);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_sign_up_write;
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
	
	public void checkInfo() {
		
		if(StringUtils.isEmpty(etCompanyName.getText())) {
			ToastUtils.showToast(R.string.wrongCompanyName);

		} else if(type >= SignUpActivity.BUSINESS_RETAIL_ONLINE
				&& StringUtils.isEmpty(etMallUrl.getText())) {
			ToastUtils.showToast(R.string.wrongMallUrl);
			
		} else if(StringUtils.isEmpty(etCompanyAddress.getText())) {
			ToastUtils.showToast(R.string.wrongAddress);
			
		} else if(StringUtils.isEmpty(etCompanyOwnerName.getText())) {
			ToastUtils.showToast(R.string.wrongCompanyOwner);
		
		} else if(StringUtils.isEmpty(etCompanyPhone.getText())) {
			ToastUtils.showToast(R.string.wrongCompanyPhone);
			
		} else if(StringUtils.isEmpty(etCompanyRegistration.getText())) {
			ToastUtils.showToast(R.string.wrongCompanyReg);
			
		} else {
			String mallUrl = etMallUrl.getText().toString();
			if(!mallUrl.contains("http://") && !mallUrl.contains("https//")) {
				mallUrl = "http://" + mallUrl;
			}
			
			mActivity.showPersonalPage(type, 
					etCompanyOwnerName.getText().toString(), 
					etCompanyName.getText().toString(),
					etMallUrl.getText().toString(),
					etCompanyAddress.getText().toString(),
					etCompanyPhone.getText().toString(),
					etCompanyRegistration.getText().toString()); 
		}
	}

	@Override
	public int getBgResourceId() {

		return R.drawable.bg2;
	}
}
