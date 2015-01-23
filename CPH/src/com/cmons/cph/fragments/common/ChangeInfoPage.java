package com.cmons.cph.fragments.common;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForShop;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.ResizeUtils;

public class ChangeInfoPage extends CmonsFragmentForShop {

	private Button btnChangePassword;
	private Button btnChangePhoneNumber;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.commonChangeInfoPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.commonChangeInfoPage_ivBg);
		
		btnChangePassword = (Button) mThisView.findViewById(R.id.commonChangeInfoPage_btnChangePassword);
		btnChangePhoneNumber = (Button) mThisView.findViewById(R.id.commonChangeInfoPage_btnChangePhoneNumber);
	}

	@Override
	public void setVariables() {

		title = "내 정보관리";
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		titleBar.getHomeButton().setVisibility(View.VISIBLE);
	}

	@Override
	public void setListeners() {

		btnChangePassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(CphConstants.PAGE_CHANGE_PASSWORD, null);
			}
		});
		
		btnChangePhoneNumber.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(CphConstants.PAGE_CHANGE_PHONENUMBER, null);
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

		return R.layout.fragment_common_changeinfo;
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

	@Override
	public int getBgResourceId() {

		return R.drawable.setting_bg2;
	}
}
