package com.cmons.cph.fragments;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.classes.BaseFragmentActivity.OnPositiveClickedListener;
import com.cmons.classes.BaseFragmentForSignUp;
import com.cmons.cph.R;
import com.cmons.cph.SignUpActivity;
import com.cmons.cph.models.Wholesale;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class SignUpForPersonalPage extends BaseFragmentForSignUp {

	private TitleBar titleBar;
	
	private TextView tvCompanyName1;
	private TextView tvCompanyName2;
	
	private TextView tvCompanyPhone1;
	private TextView tvCompanyPhone2;
	
	private TextView tvCompanyLocation1;
	private TextView tvCompanyLocation2;
	
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
	
	private Button btnComplete;
	
	private int type;
	private Wholesale wholesale;
	private String categoryString;
	
	@Override
	protected void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.signUpForPersonalPage_titleBar);
		
		tvCompanyName1 = (TextView) mThisView.findViewById(R.id.signUpForPersonalPage_tvCompanyName1);
		tvCompanyName2 = (TextView) mThisView.findViewById(R.id.signUpForPersonalPage_tvCompanyName2);
		
		tvCompanyPhone1 = (TextView) mThisView.findViewById(R.id.signUpForPersonalPage_tvCompanyPhone1);
		tvCompanyPhone2 = (TextView) mThisView.findViewById(R.id.signUpForPersonalPage_tvCompanyPhone2);
		
		tvCompanyLocation1 = (TextView) mThisView.findViewById(R.id.signUpForPersonalPage_tvCompanyLocation1);
		tvCompanyLocation2 = (TextView) mThisView.findViewById(R.id.signUpForPersonalPage_tvCompanyLocation2);
		
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
		
		btnComplete = (Button) mThisView.findViewById(R.id.signUpForPersonalPage_btnComplete);
	}

	@Override
	protected void setVariables() {

		if(getArguments() != null) {
			
			if(getArguments().containsKey("type")) {
				type = getArguments().getInt("type");
			}
			
			wholesale = (Wholesale) getArguments().getSerializable("wholesale");
			categoryString = getArguments().getString("categoryString");
		}
	}

	@Override
	protected void createPage() {

		View bottomBlank = new View(mContext);
		RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(10, ResizeUtils.getSpecificLength(110));
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForPersonalPage_btnComplete);
		bottomBlank.setLayoutParams(rp);
		((RelativeLayout) mThisView.findViewById(R.id.signUpForPersonalPage_relativeBusiness)).addView(bottomBlank);
		
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
			break;
		
		default:
			tvCompanyPhone1.setText(R.string.phoneNumberForRetail);
			tvCompanyLocation1.setText(R.string.addressForRetail);
			
			tvCompanyName1.setVisibility(View.INVISIBLE);
			tvCompanyName2.setVisibility(View.INVISIBLE);
			tvCompanyPhone1.setVisibility(View.INVISIBLE);
			tvCompanyPhone2.setVisibility(View.INVISIBLE);
			tvCompanyLocation1.setVisibility(View.INVISIBLE);
			tvCompanyLocation2.setVisibility(View.INVISIBLE);
		}
		
		tvCompanyName2.setText(wholesale.getName());
		tvCompanyPhone2.setText(wholesale.getPhone_number());
		tvCompanyLocation2.setText(wholesale.getLocation());
		
		titleBar.addBackButton(R.drawable.btn_back_findcompany, 162, 92);
		titleBar.setTitleText(R.string.inputUserInfo);
	}

	@Override
	protected void setListeners() {

		titleBar.getBackButton().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		
		btnSendCertification.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showAlertDialog(
						getString(R.string.notice), 
						getString(R.string.sendCertificationCompleted), 
						new OnPositiveClickedListener() {
							
							@Override
							public void onPositiveClicked() {
								//To to.
							}
						});
			}
		});
		
		btnComplete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				((SignUpActivity) getActivity()).launchMainActivity();
			}
		});
	}

	@Override
	protected void setSizes() {

		//titleBar.
		titleBar.getLayoutParams().height = ResizeUtils.getSpecificLength(96);
		
		RelativeLayout.LayoutParams rp = null;
		
		//shadow.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.signUpForPersonalPage_titleShadow).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(14);
		
		//tvCompanyName.
		rp = (RelativeLayout.LayoutParams) tvCompanyName1.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(90);
		rp.leftMargin = ResizeUtils.getSpecificLength(70);
		rp.topMargin = ResizeUtils.getSpecificLength(74);
		
		//etCompanyName.
		rp = (RelativeLayout.LayoutParams) tvCompanyName2.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//tvCompanyPhone.
		rp = (RelativeLayout.LayoutParams) tvCompanyPhone1.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(90);
		rp.topMargin = ResizeUtils.getSpecificLength(30);
		
		//etCompanyPhone.
		rp = (RelativeLayout.LayoutParams) tvCompanyPhone2.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//tvCompanyLocation.
		rp = (RelativeLayout.LayoutParams) tvCompanyLocation1.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(90);
		rp.topMargin = ResizeUtils.getSpecificLength(30);
		
		//etCompanyLocation.
		rp = (RelativeLayout.LayoutParams) tvCompanyLocation2.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//tvId.
		rp = (RelativeLayout.LayoutParams) tvId.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(90);
		rp.leftMargin = ResizeUtils.getSpecificLength(70);
		
		switch(type) {
				
		case SignUpActivity.BUSINESS_WHOLESALE + SignUpActivity.POSITION_OWNER:
		case SignUpActivity.BUSINESS_WHOLESALE + SignUpActivity.POSITION_EMPLOYEE1:
		case SignUpActivity.BUSINESS_WHOLESALE + SignUpActivity.POSITION_EMPLOYEE2:
			rp.topMargin = ResizeUtils.getSpecificLength(712);
			break;
		
		default:
			rp.topMargin = ResizeUtils.getSpecificLength(74);
		}
		
		//tvCheckId.
		rp = (RelativeLayout.LayoutParams) tvCheckId.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(90);
		rp.leftMargin = ResizeUtils.getSpecificLength(30);
		
		//etId.
		rp = (RelativeLayout.LayoutParams) etId.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//tvPw.
		rp = (RelativeLayout.LayoutParams) tvPw.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(90);
		rp.topMargin = ResizeUtils.getSpecificLength(30);
		
		//tvCheckPw.
		rp = (RelativeLayout.LayoutParams) tvCheckPw.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(90);
		rp.leftMargin = ResizeUtils.getSpecificLength(30);
		
		//etPw.
		rp = (RelativeLayout.LayoutParams) etPw.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//tvPwConfirm.
		rp = (RelativeLayout.LayoutParams) tvPwConfirm.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(90);
		rp.topMargin = ResizeUtils.getSpecificLength(30);
		
		//tvCheckConfirmPw.
		rp = (RelativeLayout.LayoutParams) tvCheckConfirmPw.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(90);
		rp.leftMargin = ResizeUtils.getSpecificLength(30);
		
		//etPwConfirm.
		rp = (RelativeLayout.LayoutParams) etPwConfirm.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//tvPhone.
		rp = (RelativeLayout.LayoutParams) tvPhone.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(90);
		rp.topMargin = ResizeUtils.getSpecificLength(30);
		
		//etPhone.
		rp = (RelativeLayout.LayoutParams) etPhone.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//etCertification.
		rp = (RelativeLayout.LayoutParams) etCertification.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(325);
		rp.height = ResizeUtils.getSpecificLength(92);
		rp.topMargin = ResizeUtils.getSpecificLength(24);
		
		//btnSendCertification.
		rp = (RelativeLayout.LayoutParams) btnSendCertification.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(234);
		rp.height = ResizeUtils.getSpecificLength(92);
		rp.leftMargin = ResizeUtils.getSpecificLength(23);
		
		//btnComplete.
		rp = (RelativeLayout.LayoutParams) btnComplete.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(74);
		rp.topMargin = ResizeUtils.getSpecificLength(24);
		
		FontUtils.setFontSize(tvCompanyName1, 34);
		FontUtils.setFontSize(tvCompanyName2, 30);
		FontUtils.setFontSize(tvCompanyPhone1, 34);
		FontUtils.setFontSize(tvCompanyPhone2, 30);
		FontUtils.setFontSize(tvCompanyLocation1, 34);
		FontUtils.setFontSize(tvCompanyLocation2, 30);
		
		FontUtils.setFontSize(tvId, 34);
		FontUtils.setFontSize(tvPw, 34);
		FontUtils.setFontSize(tvPwConfirm, 34);
		FontUtils.setFontSize(tvPhone, 34);
		
		FontUtils.setFontSize(tvCheckId, 30);
		FontUtils.setFontSize(tvCheckPw, 30);
		FontUtils.setFontSize(tvCheckConfirmPw, 30);
		
		FontUtils.setFontSize(etCertification, 30);
		
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.signUpForPersonalPage_ivCopyright).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(352);
		rp.height = ResizeUtils.getSpecificLength(18);
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);
	}

	@Override
	protected int getXmlResId() {

		return R.layout.fragment_sign_up_personal;
	}

	@Override
	public void onRefreshPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onBackKeyPressed() {
		// TODO Auto-generated method stub
		return false;
	}
}
