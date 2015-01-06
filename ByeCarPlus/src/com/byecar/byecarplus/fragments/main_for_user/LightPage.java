package com.byecar.byecarplus.fragments.main_for_user;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.byecar.byecarplus.R;
import com.byecar.byecarplus.SignActivity;
import com.byecar.byecarplus.classes.BCPFragment;

public class LightPage extends BCPFragment {

	@Override
	public void bindViews() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setVariables() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setListeners() {

		((Button)mThisView.findViewById(R.id.lightPage_btnMove)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				((SignActivity) mActivity).launchMainForUserActivity();
			}
		});
	}

	@Override
	public void setSizes() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_light;
	}

	@Override
	public int getBackButtonResId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBackButtonWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBackButtonHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRootViewResId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean parseJSON(JSONObject objJSON) {
		// TODO Auto-generated method stub
		return false;
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

}
