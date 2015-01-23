package com.byecar.byecarplusfordealer.common;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

import com.byecar.byecarplusfordealer.MainActivity;
import com.byecar.byecarplusfordealer.R;
import com.byecar.byecarplusfordealer.classes.BCPAPIs;
import com.byecar.byecarplusfordealer.classes.BCPFragment;
import com.byecar.byecarplusfordealer.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class SettingPage extends BCPFragment {

	private View bg1;
	private LinearLayout alarmLinear;
	private View alarmOn;
	private View alarmOff;
	private View bg2;
	private Button btnWithdraw;
	private View bg3;
	private Button btnSignOut;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.settingPage_titleBar);

		bg1 = mThisView.findViewById(R.id.settingPage_bg1);
		alarmLinear = (LinearLayout) mThisView.findViewById(R.id.settingPage_alarmLinear);
		alarmOn = mThisView.findViewById(R.id.settingPage_alarmOn);
		alarmOff = mThisView.findViewById(R.id.settingPage_alarmOff);
		bg2 = mThisView.findViewById(R.id.settingPage_bg2);
		btnWithdraw = (Button) mThisView.findViewById(R.id.settingPage_btnWithdraw);
		bg3 = mThisView.findViewById(R.id.settingPage_bg3);
		btnSignOut = (Button) mThisView.findViewById(R.id.settingPage_btnSignOut);
	}

	@Override
	public void setVariables() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createPage() {

		alarmOn.setVisibility(View.VISIBLE);
	}

	@Override
	public void setListeners() {

		alarmLinear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(alarmOn.getVisibility() == View.VISIBLE) {
					alarmOn.setVisibility(View.INVISIBLE);
					alarmOff.setVisibility(View.VISIBLE);
					setPushSetting(false);
				} else {
					alarmOn.setVisibility(View.VISIBLE);
					alarmOff.setVisibility(View.INVISIBLE);
					setPushSetting(true);
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

		ResizeUtils.viewResizeForRelative(608, 177, bg1, null, null, new int[]{0, 24, 0, 0});
		ResizeUtils.viewResizeForRelative(154, 43, alarmLinear, null, null, new int[]{0, 0, 20, 34});
		ResizeUtils.viewResize(72, LayoutParams.MATCH_PARENT, alarmOff, 1, Gravity.CENTER_VERTICAL, null);
		ResizeUtils.viewResize(72, LayoutParams.MATCH_PARENT, alarmOn, 1, Gravity.CENTER_VERTICAL, null);
		ResizeUtils.viewResizeForRelative(608, 177, bg2, null, null, new int[]{0, 14, 0, 0});
		ResizeUtils.viewResizeForRelative(154, 43, btnWithdraw, null, null, new int[]{0, 0, 20, 34});
		ResizeUtils.viewResizeForRelative(608, 177, bg3, null, null, new int[]{0, 14, 0, 0});
		ResizeUtils.viewResizeForRelative(154, 43, btnSignOut, null, null, new int[]{0, 0, 20, 34});
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_setting;
	}

	@Override
	public int getBackButtonResId() {

		return R.drawable.setting_back_btn;
	}

	@Override
	public int getBackButtonWidth() {

		return 161;
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

		return R.id.settingPage_mainLayout;
	}

//////////////////// Custom methods.
	
	public void setPushSetting(boolean needPush) {
		
		String url = BCPAPIs.PUSH_SETTING_URL
				+ "?to_get_pushed=" + (needPush?"Y":"N");
		
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
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
}
