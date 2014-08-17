package com.cmons.cph.fragments.signup;

import org.json.JSONObject;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForSignUp;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class SignUpForWritePage extends CmonsFragmentForSignUp {

	private TitleBar titleBar;
	
	private TextView tvCompanyName;
	private EditText etCompanyName;
	
	private TextView tvMallUrl;
	private EditText etMallUrl;
	
	private TextView tvCompanyAddress;
	private EditText etCompanyAddress;
	
	private TextView tvCompanyOwnerName;
	private EditText etCompanyOwnerName;
	
	private TextView tvCompanyRegistration;
	private EditText etCompanyRegistration1;
	private EditText etCompanyRegistration2;
	private EditText etCompanyRegistration3;
	
	private Button btnNext;
	
	private int type;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.signUpForWritePage_titleBar);
		
		tvCompanyName = (TextView) mThisView.findViewById(R.id.signUpForWritePage_tvCompanyName);
		etCompanyName = (EditText) mThisView.findViewById(R.id.signUpForWritePage_etCompanyName);
		
		tvMallUrl = (TextView) mThisView.findViewById(R.id.signUpForWritePage_tvMallUrl);
		etMallUrl = (EditText) mThisView.findViewById(R.id.signUpForWritePage_etMallUrl);
		
		tvCompanyAddress = (TextView) mThisView.findViewById(R.id.signUpForWritePage_tvCompanyAddress);
		etCompanyAddress = (EditText) mThisView.findViewById(R.id.signUpForWritePage_etCompanyAddress);
		
		tvCompanyOwnerName = (TextView) mThisView.findViewById(R.id.signUpForWritePage_tvCompanyOwnerName);
		etCompanyOwnerName = (EditText) mThisView.findViewById(R.id.signUpForWritePage_etCompanyOwnerName);
		
		tvCompanyRegistration = (TextView) mThisView.findViewById(R.id.signUpForWritePage_tvCompanyRegistration);
		etCompanyRegistration1 = (EditText) mThisView.findViewById(R.id.signUpForWritePage_etCompanyRegistration1);
		etCompanyRegistration2 = (EditText) mThisView.findViewById(R.id.signUpForWritePage_etCompanyRegistration2);
		etCompanyRegistration3 = (EditText) mThisView.findViewById(R.id.signUpForWritePage_etCompanyRegistration3);
		
		btnNext = (Button) mThisView.findViewById(R.id.signUpForWritePage_btnNext);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null && getArguments().containsKey("type")) {
			type = getArguments().getInt("type");
		}
	}

	@Override
	public void createPage() {

		titleBar.setTitleText(R.string.inputCompanyInfo);
		
		View bottomBlank = new View(mContext);
		RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(10, ResizeUtils.getSpecificLength(110));
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForWritePage_etCompanyRegistration1);
		bottomBlank.setLayoutParams(rp);
		((RelativeLayout) mThisView.findViewById(R.id.signUpForWritePage_relativeTerms)).addView(bottomBlank);
	}

	@Override
	public void setListeners() {

		etCompanyOwnerName.setNextFocusDownId(R.id.signUpForWritePage_etCompanyRegistration1);
		
		etCompanyRegistration1.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				
				if(etCompanyRegistration1.length() == 3) {
					etCompanyRegistration2.requestFocus();
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		etCompanyRegistration2.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				if(etCompanyRegistration2.length() == 2) {
					etCompanyRegistration3.requestFocus();
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		etCompanyRegistration3.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				if(etCompanyRegistration3.length() > 5) {
					etCompanyRegistration3.setText(s.toString().substring(
							etCompanyRegistration3.length()-5));
					etCompanyRegistration3.setSelection(etCompanyRegistration3.length());
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		
		titleBar.getBackButton().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		
		btnNext.setOnClickListener(new OnClickListener() {

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
		rp.height = ResizeUtils.getSpecificLength(90);
		rp.leftMargin = ResizeUtils.getSpecificLength(70);
		rp.topMargin = ResizeUtils.getSpecificLength(34);
		
		// etCompanyName.
		rp = (RelativeLayout.LayoutParams) etCompanyName.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(92);
		
		// tvMallUrl.
		rp = (RelativeLayout.LayoutParams) tvMallUrl.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(90);
		rp.topMargin = ResizeUtils.getSpecificLength(30);
		
		// etMallUrl.
		rp = (RelativeLayout.LayoutParams) etMallUrl.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(92);
		
		// tvCompanyAddress.
		rp = (RelativeLayout.LayoutParams) tvCompanyAddress.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(90);
		rp.topMargin = ResizeUtils.getSpecificLength(30);
		
		// etCompanyAddress.
		rp = (RelativeLayout.LayoutParams) etCompanyAddress.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(92);
		
		// tvCompanyOwnerName.
		rp = (RelativeLayout.LayoutParams) tvCompanyOwnerName.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(90);
		rp.topMargin = ResizeUtils.getSpecificLength(30);
		
		// etCompanyOwnerName.
		rp = (RelativeLayout.LayoutParams) etCompanyOwnerName.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(92);
		
		// tvCompanyRegistration.
		rp = (RelativeLayout.LayoutParams) tvCompanyRegistration.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(90);
		rp.topMargin = ResizeUtils.getSpecificLength(30);
		
		// etCompanyRegistration1.
		rp = (RelativeLayout.LayoutParams) etCompanyRegistration1.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(157);
		rp.height = ResizeUtils.getSpecificLength(92);
		
		// etCompanyRegistration2.
		rp = (RelativeLayout.LayoutParams) etCompanyRegistration2.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(134);
		rp.height = ResizeUtils.getSpecificLength(92);
		rp.leftMargin = ResizeUtils.getSpecificLength(19);
		
		// etCompanyRegistration3.
		rp = (RelativeLayout.LayoutParams) etCompanyRegistration3.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(254);
		rp.height = ResizeUtils.getSpecificLength(92);
		rp.leftMargin = ResizeUtils.getSpecificLength(19);
		
		// btnNext.
		rp = (RelativeLayout.LayoutParams) btnNext.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(74);
		rp.topMargin = ResizeUtils.getSpecificLength(30);
		
		FontUtils.setFontSize(tvCompanyName, 34);
		FontUtils.setFontSize(tvMallUrl, 34);
		FontUtils.setFontSize(tvCompanyAddress, 34);
		FontUtils.setFontSize(tvCompanyOwnerName, 34);
		FontUtils.setFontSize(tvCompanyRegistration, 34);
		
		FontUtils.setFontSize(etCompanyName, 30);
		FontUtils.setFontSize(etMallUrl, 30);
		FontUtils.setFontSize(etCompanyAddress, 30);
		FontUtils.setFontSize(etCompanyOwnerName, 30);
		FontUtils.setFontSize(etCompanyRegistration1, 30);
		FontUtils.setFontSize(etCompanyRegistration2, 30);
		FontUtils.setFontSize(etCompanyRegistration3, 30);
		
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.signUpForWritePage_ivCopyright).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(352);
		rp.height = ResizeUtils.getSpecificLength(18);
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);
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

		} else if(StringUtils.isEmpty(etMallUrl.getText())) {
			ToastUtils.showToast(R.string.wrongMallUrl);
			
		} else if(StringUtils.isEmpty(etCompanyAddress.getText())) {
			ToastUtils.showToast(R.string.wrongAddress);
			
		} else if(StringUtils.isEmpty(etCompanyOwnerName.getText())) {
			ToastUtils.showToast(R.string.wrongCompanyOwner);
			
		} else if(StringUtils.isEmpty(etCompanyRegistration1.getText())
				|| StringUtils.isEmpty(etCompanyRegistration2.getText())
				|| StringUtils.isEmpty(etCompanyRegistration3.getText())) {
			ToastUtils.showToast(R.string.wrongCompanyReg);
			
		} else {
			mActivity.showPersonalPage(type, 
					etCompanyOwnerName.getText().toString(), 
					etCompanyName.getText().toString(),
					etMallUrl.getText().toString(),
					etCompanyAddress.getText().toString(),
					etCompanyRegistration1.getText().toString() 
						+ etCompanyRegistration2.getText().toString() 
						+ etCompanyRegistration3.getText().toString());
		}
	}
}
