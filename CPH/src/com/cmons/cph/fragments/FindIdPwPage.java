package com.cmons.cph.fragments;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.cmons.classes.BaseFragment;
import com.cmons.classes.BaseFragmentActivity.OnPositiveClickedListener;
import com.cmons.cph.R;
import com.cmons.cph.SignInActivity;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.ResizeUtils;

public class FindIdPwPage extends BaseFragment {

	public static final int TYPE_FIND_ID = 0;
	public static final int TYPE_FIND_PW = 1;
	
	private int type;

	private TitleBar titleBar;
	private EditText etPhone;
	private EditText etCertification;
	private Button btnSend;
	private Button btnCertify;
	
	@Override
	protected void bindViews() {
		
		titleBar = (TitleBar) mThisView.findViewById(R.id.findIdPwPage_titleBar);
		etPhone = (EditText) mThisView.findViewById(R.id.findIdPwPage_etPhone);
		etCertification = (EditText) mThisView.findViewById(R.id.findIdPwPage_etCertification);
		btnSend = (Button) mThisView.findViewById(R.id.findIdPwPage_btnSend);
		btnCertify = (Button) mThisView.findViewById(R.id.findIdPwPage_btnCertify);
	}

	@Override
	protected void setVariables() {

		if(getArguments() != null) {
			type = getArguments().getInt("type", TYPE_FIND_ID);
		}
	}

	@Override
	protected void createPage() {

		titleBar.addBackButton(R.drawable.btn_back_login, 162, 92);
		
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
	protected void setListeners() {

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
	protected void setSizes() {

		int padding = ResizeUtils.getSpecificLength(30);
		RelativeLayout.LayoutParams rp = null;
		
		//titleBar.
		titleBar.getLayoutParams().height = ResizeUtils.getSpecificLength(96);
		
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
	protected int getXmlResId() {

		return R.layout.fragment_findidpw;
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

	public void checkPhone() {
		
		btnSend.setVisibility(View.INVISIBLE);
		etCertification.setVisibility(View.VISIBLE);
		btnCertify.setVisibility(View.VISIBLE);
	}
	
	public void checkCertification() {
		
		certify();
	}
	
	public void certify() {

		showAlert();
	}
	
	public void showAlert() {
		
		String message = null;
		
		if(type == TYPE_FIND_ID) {
			message = getString(R.string.sendIdCompleted);
		} else{
			message = getString(R.string.sendPwCompleted);
		}
		
		((SignInActivity)getActivity()).showAlertDialog(null, message, 
				new OnPositiveClickedListener() {
			
			@Override
			public void onPositiveClicked() {

				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
	}
}
