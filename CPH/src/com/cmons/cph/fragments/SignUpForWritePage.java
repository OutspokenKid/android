package com.cmons.cph.fragments;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.classes.BaseFragmentForSignUp;
import com.cmons.cph.R;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class SignUpForWritePage extends BaseFragmentForSignUp {

	private TitleBar titleBar;
	
	private TextView tvCompanyName;
	private EditText etCompanyName;
	
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
	protected void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.signUpForWritePage_titleBar);
		
		tvCompanyName = (TextView) mThisView.findViewById(R.id.signUpForWritePage_tvCompanyName);
		etCompanyName = (EditText) mThisView.findViewById(R.id.signUpForWritePage_etCompanyName);
		
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
	protected void setVariables() {

		if(getArguments() != null && getArguments().containsKey("type")) {
			type = getArguments().getInt("type");
		}
	}

	@Override
	protected void createPage() {

		titleBar.addBackButton(R.drawable.btn_back_position, 162, 92);
		titleBar.setTitleText(R.string.inputCompanyInfo);
		
		View bottomBlank = new View(mContext);
		RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(10, ResizeUtils.getSpecificLength(110));
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForWritePage_etCompanyRegistration1);
		bottomBlank.setLayoutParams(rp);
		((RelativeLayout) mThisView.findViewById(R.id.signUpForWritePage_relativeTerms)).addView(bottomBlank);
	}

	@Override
	protected void setListeners() {

		titleBar.getBackButton().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		
		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPersonalPage(type, null, null);
			}
		});
	}

	@Override
	protected void setSizes() {

		//titleBar.
		titleBar.getLayoutParams().height = ResizeUtils.getSpecificLength(96);
		
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
		FontUtils.setFontSize(tvCompanyAddress, 34);
		FontUtils.setFontSize(tvCompanyOwnerName, 34);
		FontUtils.setFontSize(tvCompanyRegistration, 34);
		
		FontUtils.setFontSize(etCompanyName, 30);
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
	protected int getXmlResId() {

		return R.layout.fragment_sign_up_write;
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
