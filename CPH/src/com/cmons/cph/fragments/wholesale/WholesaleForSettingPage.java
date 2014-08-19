package com.cmons.cph.fragments.wholesale;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.ToastUtils;

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

		title = "설정";
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		titleBar.getHomeButton().setVisibility(View.INVISIBLE);
	}

	@Override
	public void setListeners() {

		btnInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showChangeInfoPage();
			}
		});
		
		btnNotice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				mActivity.showNoticeListPage();
			}
		});
		
		btnNotification.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showNotificationSettingPage();
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

						signOut();
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

						withdraw();
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

//////////////////// Custom methods.
	
	public void signOut() {

		//http://cph.minsangk.com/users/logout
		url = CphConstants.BASE_API_URL + "logout";
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

					ToastUtils.showToast(objJSON.getString("result"));
					
					SharedPrefsUtils.removeVariableFromPrefs(CphConstants.PREFS_SIGN, "id");
					SharedPrefsUtils.removeVariableFromPrefs(CphConstants.PREFS_SIGN, "pw");
					
					mActivity.launchSignInActivity();
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void withdraw() {
		
		//http://cph.minsangk.com/users/logout
		url = CphConstants.BASE_API_URL + "logout";
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

					ToastUtils.showToast(objJSON.getString("result"));
					
					SharedPrefsUtils.removeVariableFromPrefs(CphConstants.PREFS_SIGN, "id");
					SharedPrefsUtils.removeVariableFromPrefs(CphConstants.PREFS_SIGN, "pw");
					
					mActivity.launchSignInActivity();
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
}
