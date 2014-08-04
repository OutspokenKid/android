package com.cmons.cph.fragments;

import android.widget.Button;
import android.widget.RelativeLayout;

import com.cmons.classes.BaseFragment;
import com.cmons.cph.R;
import com.cmons.cph.views.TitleBar;

public class WholesaleMainPage extends BaseFragment {

//	private TitleBar titleBar;
	
	private Button btnMain;
	private Button btnNotice;
	private Button btnManagement;
	private Button btnOrder;
	private Button btnSample;
	private Button btnPartner;
	private Button btnStaff;
	private Button btnSetting;
	
	@Override
	protected void bindViews() {

//		titleBar = (TitleBar) mThisView.findViewById(R.id.wholesaleMainPage_titleBar);
		
		btnMain = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnMain);
		btnNotice = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnNotice);
		btnManagement = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnManagement);
		btnOrder = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnOrder);
		btnSample = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnSample);
		btnPartner = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnPartner);
		btnStaff = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnStaff);
		btnSetting = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnSetting);
	}

	@Override
	protected void setVariables() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createPage() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setSizes() {

		RelativeLayout.LayoutParams rp = null;
	}

	@Override
	protected int getXmlResId() {
		// TODO Auto-generated method stub
		return 0;
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
