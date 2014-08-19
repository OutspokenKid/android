package com.cmons.cph.fragments.wholesale;

import org.json.JSONObject;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class WholesaleForNotificationSettingPage extends CmonsFragmentForWholesale {

	private Button btnNotification;
	private TextView tvNotification;
	private Button btnDoNotDisturb;
	private TextView tvTime;
	private TextView tvDoNotDisturb;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.wholesaleNotificationSettingPage_titleBar);
		
		btnNotification = (Button) mThisView.findViewById(R.id.wholesaleNotificationSettingPage_btnNotification);
		tvNotification = (TextView) mThisView.findViewById(R.id.wholesaleNotificationSettingPage_tvNotification);
		btnDoNotDisturb = (Button) mThisView.findViewById(R.id.wholesaleNotificationSettingPage_btnDoNotDisturb);
		tvTime = (TextView) mThisView.findViewById(R.id.wholesaleNotificationSettingPage_tvTime);
		tvDoNotDisturb = (TextView) mThisView.findViewById(R.id.wholesaleNotificationSettingPage_tvDoNotDisturb);
	}

	@Override
	public void setVariables() {

		title = "알림설정";
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		titleBar.getHomeButton().setVisibility(View.VISIBLE);
		
		//알림 설정.
		//setting_notification_btn_b
		btnNotification.setBackgroundResource(R.drawable.setting_notification2_btn_b);
		
		//시간 설정.
		tvTime.setText("AM 11:00\nPM 03:00");
	}

	@Override
	public void setListeners() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		int p = ResizeUtils.getSpecificLength(20);
		
		//btnNotification.
		rp = (RelativeLayout.LayoutParams) btnNotification.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(180);
		rp.topMargin = ResizeUtils.getSpecificLength(84);
		
		//tvNotification.
		rp = (RelativeLayout.LayoutParams) tvNotification.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(140);
		rp.leftMargin = ResizeUtils.getSpecificLength(44);
		FontUtils.setFontSize(tvNotification, 30);
		tvNotification.setPadding(p, p, p, p);
		
		//btnDoNotDisturb.
		rp = (RelativeLayout.LayoutParams) btnDoNotDisturb.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(180);
		
		//tvTime.
		rp = (RelativeLayout.LayoutParams) tvTime.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(180);
		rp.rightMargin = ResizeUtils.getSpecificLength(120);
		FontUtils.setFontSize(tvTime, 40);
		
		//tvDoNotDisturb.
		rp = (RelativeLayout.LayoutParams) tvDoNotDisturb.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(150);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		FontUtils.setFontSize(tvDoNotDisturb, 30);
		tvDoNotDisturb.setPadding(p, p, p, p);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_wholesale_notificationsetting;
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
