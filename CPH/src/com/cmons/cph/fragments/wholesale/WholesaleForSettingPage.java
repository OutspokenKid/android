package com.cmons.cph.fragments.wholesale;

import org.json.JSONObject;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class WholesaleForSettingPage extends CmonsFragmentForWholesale {

	private Button btnInfo;
	private Button btnNotice;
	private Button btnSuggest;
	private Button btnNotification;
	private Button btnWithdraw;
	private Button btnSignout;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.wholesaleSettingPage_titleBar);
		
		btnInfo = (Button) mThisView.findViewById(R.id.wholesaleSettingPage_btnInfo);
		btnNotice = (Button) mThisView.findViewById(R.id.wholesaleSettingPage_btnNotice);
		btnSuggest = (Button) mThisView.findViewById(R.id.wholesaleSettingPage_btnSuggest);
		btnNotification = (Button) mThisView.findViewById(R.id.wholesaleSettingPage_btnNotification);
		btnWithdraw = (Button) mThisView.findViewById(R.id.wholesaleSettingPage_btnWithdraw);
		btnSignout = (Button) mThisView.findViewById(R.id.wholesaleSettingPage_btnSignout);
	}

	@Override
	public void setVariables() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		titleBar.getHomeButton().setVisibility(View.INVISIBLE);
	}

	@Override
	public void setListeners() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSizes() {
		
		RelativeLayout.LayoutParams rp = null;
		
		//btnInfo.
		rp = (RelativeLayout.LayoutParams) btnInfo.getLayoutParams();
		rp.height = ResizeUtils.getScreenWidth();
		
		//btnNotice.
		rp = (RelativeLayout.LayoutParams) btnNotice.getLayoutParams();
		rp.width = ResizeUtils.getScreenWidth()/2;
		rp.height = ResizeUtils.getScreenWidth()/2;
		
		//btnSuggest.
		rp = (RelativeLayout.LayoutParams) btnSuggest.getLayoutParams();
		rp.height = ResizeUtils.getScreenWidth()/4;
		
		//btnNotification.
		rp = (RelativeLayout.LayoutParams) btnNotification.getLayoutParams();
		rp.height = ResizeUtils.getScreenWidth()/4;
		
		//btnWithdraw.
		rp = (RelativeLayout.LayoutParams) btnWithdraw.getLayoutParams();
		rp.width = ResizeUtils.getScreenWidth()/2;
		rp.height = ResizeUtils.getScreenWidth()/4;
		
		//btnSignout.
		rp = (RelativeLayout.LayoutParams) btnSignout.getLayoutParams();
		rp.height = ResizeUtils.getScreenWidth()/4;
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_wholesale_setting;
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
}
