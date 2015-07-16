package com.byecar.fragments;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.MainActivity;
import com.byecar.byecarplus.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPFragment;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.ToastUtils;

public class SettingPage extends BCPFragment {

	private TextView tvNotificationTitle;
	private TextView tvChangePwTitle;
	private TextView tvWithdrawTitle;
	private TextView tvSignOutTitle;
	private TextView tvNotification;
	private TextView tvBidding;
	private TextView tvChangePw;
	private TextView tvWithdraw;
	private TextView tvSignOut;
	private Button btnNotification;
	private Button btnBidding;
	private Button btnChangePw;
	private Button btnWithdraw;
	private Button btnSignOut;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.settingPage_titleBar);
		
		tvNotificationTitle = (TextView) mThisView.findViewById(R.id.settingPage_tvNotificationTitle);
		tvChangePwTitle = (TextView) mThisView.findViewById(R.id.settingPage_tvChangePwTitle);
		tvWithdrawTitle = (TextView) mThisView.findViewById(R.id.settingPage_tvWithdrawTitle);
		tvSignOutTitle = (TextView) mThisView.findViewById(R.id.settingPage_tvSignOutTitle);
		
		tvNotification = (TextView) mThisView.findViewById(R.id.settingPage_tvNotification);
		tvBidding = (TextView) mThisView.findViewById(R.id.settingPage_tvBidding);
		tvChangePw = (TextView) mThisView.findViewById(R.id.settingPage_tvChangePw);
		tvWithdraw = (TextView) mThisView.findViewById(R.id.settingPage_tvWithdraw);
		tvSignOut = (TextView) mThisView.findViewById(R.id.settingPage_tvSignOut);
		
		btnNotification = (Button) mThisView.findViewById(R.id.settingPage_btnNotification);
		btnBidding = (Button) mThisView.findViewById(R.id.settingPage_btnBidding);
		btnChangePw = (Button) mThisView.findViewById(R.id.settingPage_btnChangePw);
		btnWithdraw = (Button) mThisView.findViewById(R.id.settingPage_btnWithdraw);
		btnSignOut = (Button) mThisView.findViewById(R.id.settingPage_btnSignOut);
	}

	@Override
	public void setVariables() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createPage() {

		if(SharedPrefsUtils.getBooleanFromPrefs(BCPConstants.PREFS_PUSH, "noPush")) {
			btnNotification.setBackgroundResource(R.drawable.setting_toggle_off);
		} else {
			btnNotification.setBackgroundResource(R.drawable.setting_toggle_on);
		}
		
		if(SharedPrefsUtils.getBooleanFromPrefs(BCPConstants.PREFS_PUSH, "noBiddingPush")) {
			btnBidding.setBackgroundResource(R.drawable.setting_toggle_off);
		} else {
			btnBidding.setBackgroundResource(R.drawable.setting_toggle_on);
		}
	}

	@Override
	public void setListeners() {

		btnNotification.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setPushSetting(false, !SharedPrefsUtils.getBooleanFromPrefs(BCPConstants.PREFS_PUSH, "noPush"));
			}
		});
		
		btnBidding.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setPushSetting(true, !SharedPrefsUtils.getBooleanFromPrefs(BCPConstants.PREFS_PUSH, "noBiddingPush"));
			}
		});
		
		btnChangePw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(MainActivity.user.getEmail().startsWith("KA-")
						|| MainActivity.user.getEmail().startsWith("FA-")) {
					ToastUtils.showToast(R.string.snsLoginUserCanNotUseThisService);
				} else {
					mActivity.showPage(BCPConstants.PAGE_CHANGE_PASSWORD, null);
				}
			}
		});
		
		btnSignOut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showAlertDialog(R.string.signOut, R.string.wannaSignOut, 
						R.string.confirm, R.string.cancel, 
						new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {

						((MainActivity)mActivity).signOut();
					}
				}, null);
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

						((MainActivity)mActivity).withdraw();
					}
				}, null);
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		
		//tvNotificationTitle.
		rp = (RelativeLayout.LayoutParams) tvNotificationTitle.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(41);
		tvNotificationTitle.setPadding(ResizeUtils.getSpecificLength(20), 0, 0, 0);

		//tvChangePwTitle.
		rp = (RelativeLayout.LayoutParams) tvChangePwTitle.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(41);
		tvChangePwTitle.setPadding(ResizeUtils.getSpecificLength(20), 0, 0, 0);
		
		//tvWithdrawTitle.
		rp = (RelativeLayout.LayoutParams) tvWithdrawTitle.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(41);
		tvWithdrawTitle.setPadding(ResizeUtils.getSpecificLength(20), 0, 0, 0);
		
		//tvSignOutTitle.
		rp = (RelativeLayout.LayoutParams) tvSignOutTitle.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(41);
		tvSignOutTitle.setPadding(ResizeUtils.getSpecificLength(20), 0, 0, 0);
				
		//tvNotification.
		rp = (RelativeLayout.LayoutParams) tvNotification.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(110);
		tvNotification.setPadding(ResizeUtils.getSpecificLength(20), 0, 0, 0);
		
//		//tvBidding.
//		rp = (RelativeLayout.LayoutParams) tvBidding.getLayoutParams();
//		rp.height = ResizeUtils.getSpecificLength(110);
//		tvBidding.setPadding(ResizeUtils.getSpecificLength(20), 0, 0, 0);

		//tvChangePw.
		rp = (RelativeLayout.LayoutParams) tvChangePw.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(110);
		tvChangePw.setPadding(ResizeUtils.getSpecificLength(20), 0, 0, 0);
		
		//tvWithdraw.
		rp = (RelativeLayout.LayoutParams) tvWithdraw.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(110);
		tvWithdraw.setPadding(ResizeUtils.getSpecificLength(20), 0, 0, 0);
		
		//tvSignOut.
		rp = (RelativeLayout.LayoutParams) tvSignOut.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(110);
		tvSignOut.setPadding(ResizeUtils.getSpecificLength(20), 0, 0, 0);
				
		//btnNotification.
		rp = (RelativeLayout.LayoutParams) btnNotification.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(228);
		rp.height = ResizeUtils.getSpecificLength(52);
		rp.topMargin = ResizeUtils.getSpecificLength(28);
		rp.rightMargin = ResizeUtils.getSpecificLength(16);
		
//		//btnBidding.
//		rp = (RelativeLayout.LayoutParams) btnBidding.getLayoutParams();
//		rp.width = ResizeUtils.getSpecificLength(228);
//		rp.height = ResizeUtils.getSpecificLength(52);
//		rp.topMargin = ResizeUtils.getSpecificLength(28);
//		rp.rightMargin = ResizeUtils.getSpecificLength(16);

		//btnChangePw.
		rp = (RelativeLayout.LayoutParams) btnChangePw.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(227);
		rp.height = ResizeUtils.getSpecificLength(52);
		rp.topMargin = ResizeUtils.getSpecificLength(28);
		rp.rightMargin = ResizeUtils.getSpecificLength(16);
		
		//btnWithdraw.
		rp = (RelativeLayout.LayoutParams) btnWithdraw.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(227);
		rp.height = ResizeUtils.getSpecificLength(52);
		rp.topMargin = ResizeUtils.getSpecificLength(28);
		rp.rightMargin = ResizeUtils.getSpecificLength(16);

		//btnSignOut.
		rp = (RelativeLayout.LayoutParams) btnSignOut.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(227);
		rp.height = ResizeUtils.getSpecificLength(52);
		rp.topMargin = ResizeUtils.getSpecificLength(28);
		rp.rightMargin = ResizeUtils.getSpecificLength(16);
		
		FontUtils.setFontSize(tvNotificationTitle, 24);
		FontUtils.setFontStyle(tvNotificationTitle, FontUtils.BOLD);
		FontUtils.setFontSize(tvChangePwTitle, 24);
		FontUtils.setFontStyle(tvChangePwTitle, FontUtils.BOLD);
		FontUtils.setFontSize(tvWithdrawTitle, 24);
		FontUtils.setFontStyle(tvWithdrawTitle, FontUtils.BOLD);
		FontUtils.setFontSize(tvSignOutTitle, 24);
		FontUtils.setFontStyle(tvSignOutTitle, FontUtils.BOLD);
		FontUtils.setFontSize(tvNotification, 24);
		FontUtils.setFontSize(tvBidding, 24);
		FontUtils.setFontSize(tvChangePw, 24);
		FontUtils.setFontSize(tvWithdraw, 24);
		FontUtils.setFontSize(tvSignOut, 24);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_common_setting;
	}

	@Override
	public int getPageTitleTextResId() {

		return R.string.pageTitle_setting;
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

		return R.id.settingPage_mainLayout;
	}

//////////////////// Custom methods.
	
	public void setPushSetting(final boolean isBiddingPush, final boolean isOnNow) {

		
		if(!isBiddingPush) {
			String url = BCPAPIs.PUSH_SETTING_URL
					+ "?to_get_pushed=" + (isOnNow?"N":"Y");
			
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("SettingPage.onError." + "\nurl : " + url);

				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("SettingPage.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);
						
						//반대로 들어감. 원래 on(true)이었다면, noPush가 true, off가 된다.
						SharedPrefsUtils.addDataToPrefs(BCPConstants.PREFS_PUSH, "noPush", isOnNow);
						
						if(isOnNow) {
							btnNotification.setBackgroundResource(R.drawable.setting_toggle_off);
						} else {
							btnNotification.setBackgroundResource(R.drawable.setting_toggle_on);
						}
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
					}
				}
			});
		} else {
			SharedPrefsUtils.addDataToPrefs(BCPConstants.PREFS_PUSH, "noBiddingPush", isOnNow);
			
			if(isOnNow) {
				btnBidding.setBackgroundResource(R.drawable.setting_toggle_off);
			} else {
				btnBidding.setBackgroundResource(R.drawable.setting_toggle_on);
			}
		}
	}
}
