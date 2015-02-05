package com.byecar.fragments;

import org.json.JSONObject;

import android.view.Gravity;
import android.widget.Button;
import android.widget.FrameLayout;

import com.byecar.byecarplus.R;
import com.byecar.classes.BCPFragment;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.ResizeUtils;

public class AskPage extends BCPFragment {

	private FrameLayout askFrame;
	private Button btnKakao;
	private Button btnFacebook;
	private Button btnEmail;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.askPage_titleBar);

		askFrame = (FrameLayout) mThisView.findViewById(R.id.askPage_askFrame);
		btnKakao = (Button) mThisView.findViewById(R.id.askPage_btnKakao);
		btnFacebook = (Button) mThisView.findViewById(R.id.askPage_btnFacebook);
		btnEmail = (Button) mThisView.findViewById(R.id.askPage_btnEmail);
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
		// TODO Auto-generated method stub

	}

	@Override
	public void setSizes() {

		ResizeUtils.viewResizeForRelative(608, 407, askFrame, null, null, new int[]{0, 30, 0, 0});
		ResizeUtils.viewResize(488, 82, btnKakao, 2, Gravity.CENTER_HORIZONTAL|Gravity.TOP, new int[]{0, 90, 0, 0});
		ResizeUtils.viewResize(488, 82, btnFacebook, 2, Gravity.CENTER_HORIZONTAL|Gravity.TOP, new int[]{0, 196, 0, 0});
		ResizeUtils.viewResize(488, 82, btnEmail, 2, Gravity.CENTER_HORIZONTAL|Gravity.TOP, new int[]{0, 302, 0, 0});
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_common_ask;
	}

	@Override
	public int getBackButtonResId() {

		return R.drawable.ask_back_btn;
	}

	@Override
	public int getBackButtonWidth() {

		return 235;
	}

	@Override
	public int getBackButtonHeight() {

		return 60;
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

	@Override
	public int getRootViewResId() {

		return R.id.askPage_mainLayout;
	}
}
