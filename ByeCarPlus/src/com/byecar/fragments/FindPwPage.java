package com.byecar.fragments;

import org.json.JSONObject;

import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.byecar.byecarplus.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPFragment;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo.holo_light.HoloStyleEditText;

public class FindPwPage extends BCPFragment {

	private HoloStyleEditText etEmail;
	private Button btnFindPw;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.findPwPage_titleBar);
		
		etEmail = (HoloStyleEditText) mThisView.findViewById(R.id.findPwPage_etEmail);
		btnFindPw = (Button) mThisView.findViewById(R.id.findPwPage_btnConfirm);
	}

	@Override
	public void setVariables() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createPage() {

		etEmail.setHint(R.string.hintForFindPw);
		etEmail.getEditText().setTextColor(getResources().getColor(R.color.new_color_text_gray));
		etEmail.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
	}

	@Override
	public void setListeners() {
	
		btnFindPw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				findPw();
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		
		int width = ResizeUtils.getSpecificLength(586);
		int textViewHeight = ResizeUtils.getSpecificLength(60);
		int buttonHeight = ResizeUtils.getSpecificLength(82);

		//etEmail.
		rp = (RelativeLayout.LayoutParams) etEmail.getLayoutParams();
		rp.width = width;
		rp.height = textViewHeight;
		rp.topMargin = textViewHeight;
		
		//btnFindPw.
		rp = (RelativeLayout.LayoutParams) btnFindPw.getLayoutParams();
		rp.width = width;
		rp.height = buttonHeight;
		rp.topMargin = ResizeUtils.getSpecificLength(20);
		rp.bottomMargin = ResizeUtils.getSpecificLength(40);
		
		FontUtils.setFontAndHintSize(etEmail.getEditText(), 30, 24);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_common_find_pw;
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
	public int getPageTitleTextResId() {

		return R.string.pageTitle_findPw;
	}

	@Override
	public int getRootViewResId() {

		return R.id.findPwPage_mainLayout;
	}
	
//////////////////// Custom methods.

	public void findPw() {

		//http://byecar.minsangk.com/users/find/password.json?email=minsangk@me.com
		String url = BCPAPIs.FIND_PW_URL
				+ "?email=" + StringUtils.getUrlEncodedString(etEmail.getEditText());
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("FindPwPage.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToFindPw);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("FindPwPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);
					
					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_sendEmail);
						mActivity.closeTopPage();
						
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		}, mActivity.getLoadingView());
	}
}
