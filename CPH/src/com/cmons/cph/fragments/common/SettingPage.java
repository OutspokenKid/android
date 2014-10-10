package com.cmons.cph.fragments.common;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForShop;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.IntentUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;

public class SettingPage extends CmonsFragmentForShop {

	private Button btnInfo;
	private Button btnNotice;
	private Button btnSuggest;
	private Button btnFont;
	private Button btnNotification;
	private Button btnWithdraw;
	private Button btnSignout;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.settingPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.settingPage_ivBg);
		
		btnInfo = (Button) mThisView.findViewById(R.id.settingPage_btnInfo);
		btnNotice = (Button) mThisView.findViewById(R.id.settingPage_btnNotice);
		btnSuggest = (Button) mThisView.findViewById(R.id.settingPage_btnSuggest);
		btnFont = (Button) mThisView.findViewById(R.id.settingPage_btnFont);
		btnNotification = (Button) mThisView.findViewById(R.id.settingPage_btnNotification);
		btnWithdraw = (Button) mThisView.findViewById(R.id.settingPage_btnWithdraw);
		btnSignout = (Button) mThisView.findViewById(R.id.settingPage_btnSignout);
	}

	@Override
	public void setVariables() {

		title = "설정";
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		
		setFontButton();
	}

	@Override
	public void setListeners() {

		btnInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(CphConstants.PAGE_COMMON_CHANGE_INFO, null);
			}
		});
		
		btnNotice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				Bundle bundle = new Bundle();
				bundle.putBoolean("isAppNotice", true);
				bundle.putBoolean("isOurNotice", false);
				mActivity.showPage(CphConstants.PAGE_COMMON_NOTICE_LIST, bundle);
			}
		});
	
		btnSuggest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				IntentUtils.sendEmail(mContext, getString(R.string.suggestMail));
			}
		});

		btnFont.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				changeFontSetting();
			}
		});
		
		btnNotification.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(CphConstants.PAGE_COMMON_NOTIFICATION_SETTING, null);
			}
		});
		
		btnWithdraw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showAlertDialog(R.string.withdraw, R.string.wannaWithdraw, 
						R.string.confirm, R.string.cancel, 
						new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {

						withdraw();
					}
				}, null);
			}
		});
		
		btnSignout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showAlertDialog(R.string.signOut, R.string.wannaSignOut, 
						R.string.confirm, R.string.cancel, 
						new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {

						mActivity.signOut();
					}
				}, null);
			}
		});
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
		rp.height = ResizeUtils.getScreenWidth()/4;
		
		//btnSuggest.
		rp = (RelativeLayout.LayoutParams) btnSuggest.getLayoutParams();
		rp.height = ResizeUtils.getScreenWidth()/4;

		//btnFont.
		rp = (RelativeLayout.LayoutParams) btnFont.getLayoutParams();
		rp.width = ResizeUtils.getScreenWidth()/2;
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

		return R.layout.fragment_common_setting;
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

//////////////////// Custom methods.
	
	public void changeFontSetting() {
		
		boolean bigFont = SharedPrefsUtils.getBooleanFromPrefs(CphConstants.PREFS_BIG_FONT, "bigfont");
		SharedPrefsUtils.addDataToPrefs(CphConstants.PREFS_BIG_FONT, "bigfont", !bigFont);
		
		setFontButton();
	}
	
	public void setFontButton() {
		
		boolean bigFont = SharedPrefsUtils.getBooleanFromPrefs(CphConstants.PREFS_BIG_FONT, "bigfont");
		
		if(bigFont) {
			btnFont.setBackgroundResource(R.drawable.setting_big_btn_a);
		} else {
			btnFont.setBackgroundResource(R.drawable.setting_big_btn_b);
		}
	}
	
	public void withdraw() {
		
		//http://cph.minsangk.com/users/withdraw
		url = CphConstants.BASE_API_URL + "users/withdraw";
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WholesaleForSettingPage.onError." + "\nurl : " + url);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("WholesaleForSettingPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);
					
					SharedPrefsUtils.clearCookie(CphConstants.PREFS_COOKIE_CPH_D1);
					SharedPrefsUtils.clearCookie(CphConstants.PREFS_COOKIE_CPH_S);
					
					mActivity.launchSignInActivity();
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}

	@Override
	public int getBgResourceId() {

		return R.drawable.setting_bg;
	}
}
