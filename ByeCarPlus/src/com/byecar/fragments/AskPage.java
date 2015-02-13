package com.byecar.fragments;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

import com.byecar.byecarplus.MainActivity;
import com.byecar.byecarplus.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPFragment;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.IntentUtils;
import com.outspoken_kid.utils.PackageUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;

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

		btnKakao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(PackageUtils.checkApplicationInstalled(mContext, "com.kakao.talk")) {
					IntentUtils.showDeviceBrowser(mActivity, BCPAPIs.GOTO_KAKAO_URL);
				} else {
					ToastUtils.showToast(R.string.kakaoTalkNotInstalled);
				}
			}
		});
		
		btnFacebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				
				if(MainActivity.companyInfo != null) {
					bundle.putString("url", MainActivity.companyInfo.getFacebook_url());
				}
				
				mActivity.showPage(BCPConstants.PAGE_WEB_BROWSER, bundle);
			}
		});
		
		btnEmail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(MainActivity.companyInfo != null) {
					IntentUtils.sendEmail(mContext, MainActivity.companyInfo.getEmail());
				}
			}
		});
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
