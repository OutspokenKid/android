package com.cmons.cph.fragments.wholesale;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.ResizeUtils;

public class WholesaleForChangeInfoPage extends CmonsFragmentForWholesale {

	private Button btnChangePassword;
	private Button btnChangePhoneNumber;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.wholesaleChangeInfoPage_titleBar);
		
		btnChangePassword = (Button) mThisView.findViewById(R.id.wholesaleChangeInfoPage_btnChangePassword);
		btnChangePhoneNumber = (Button) mThisView.findViewById(R.id.wholesaleChangeInfoPage_btnChangePhoneNumber);
	}

	@Override
	public void setVariables() {

		title = "내 정보관리";
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		titleBar.getHomeButton().setVisibility(View.INVISIBLE);
	}

	@Override
	public void setListeners() {

		btnChangePassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showChangePasswordPage();
			}
		});
		
		btnChangePhoneNumber.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showChangePhoneNumberPage();
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		
		//btnChangePassword.
		rp = (RelativeLayout.LayoutParams) btnChangePassword.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(180);
		rp.topMargin = ResizeUtils.getSpecificLength(84);
		
		//btnChangePassword.
		rp = (RelativeLayout.LayoutParams) btnChangePhoneNumber.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(180);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_wholesale_changeinfo;
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
