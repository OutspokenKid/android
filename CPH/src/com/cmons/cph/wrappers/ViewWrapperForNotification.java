package com.cmons.cph.wrappers;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.ViewWrapper;
import com.cmons.cph.models.Notification;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class ViewWrapperForNotification extends ViewWrapper {
	
	private Notification notification;
	
	public TextView tvNotice;
	public TextView tvRegdate;
	public View icon;
	
	public ViewWrapperForNotification(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			tvNotice = (TextView) row.findViewById(R.id.list_notice_tvNotice);
			tvRegdate = (TextView) row.findViewById(R.id.list_notice_tvRegedit);
			icon = row.findViewById(R.id.list_notice_icon);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			row.setLayoutParams(new AbsListView.LayoutParams(
					LayoutParams.MATCH_PARENT, ResizeUtils.getSpecificLength(128)));
			
			int p = ResizeUtils.getSpecificLength(20);
			tvNotice.setPadding(p, 0, p, 0);
			
			tvRegdate.getLayoutParams().width = ResizeUtils.getSpecificLength(120);
			
			ResizeUtils.viewResize(29, 30, icon, 1, Gravity.CENTER_VERTICAL, new int[]{20, 0, 20, 0});
			
			FontUtils.setFontSize(tvNotice, 28);
			FontUtils.setFontSize(tvRegdate, 20);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel instanceof Notification) {
				notification = (Notification) baseModel;
				tvNotice.setText(notification.getMessage());
				
				String dateString = StringUtils.getDateString("yyyy.MM.dd\naa hh:mm", 
						notification.getPushed_at() * 1000);
				tvRegdate.setText(dateString);
				
				if(notification.getRead_at() != 0) {
					tvNotice.setTextColor(Color.rgb(120, 120, 120));
					icon.setBackgroundResource(R.drawable.mail_icon_b);
				} else {
					tvNotice.setTextColor(Color.BLACK);
					icon.setBackgroundResource(R.drawable.mail_icon_a);
				}
			} else {
				setUnusableView();
			}
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setListeners() {
	}
	
	@Override
	public void setUnusableView() {
		// TODO Auto-generated method stub
		
	}
}
