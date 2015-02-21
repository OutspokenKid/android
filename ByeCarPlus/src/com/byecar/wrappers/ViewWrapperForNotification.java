package com.byecar.wrappers;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.models.Notification;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class ViewWrapperForNotification extends ViewWrapper {

	private Notification notification;
	
	private ImageView ivImage;
	private View cover;
	private View newIcon;
	private TextView tvRegdate;
	private TextView tvInfo;
	
	public ViewWrapperForNotification(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			ivImage = (ImageView) row.findViewById(R.id.list_notification_ivImage);
			cover = row.findViewById(R.id.list_notification_cover);
			newIcon = row.findViewById(R.id.list_notification_newIcon);
			tvRegdate = (TextView) row.findViewById(R.id.list_notification_tvRegdate);
			tvInfo = (TextView) row.findViewById(R.id.list_notification_tvInfo);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			ResizeUtils.viewResizeForRelative(185, 132, ivImage, null, null, new int[]{31, 0, 0, 0});
			ResizeUtils.viewResizeForRelative(578, 132, cover, null, null, null);
			ResizeUtils.viewResizeForRelative(59, 59, newIcon, null, null, null);
			ResizeUtils.setMargin(tvInfo, new int[]{18, 26, 52, 0});
			ResizeUtils.setMargin(tvRegdate, new int[]{0, 0, 14, 10});
			
			FontUtils.setFontSize(tvInfo, 20);
			FontUtils.setFontSize(tvRegdate, 16);
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

				if(notification.getRead_at() == 0) {
					newIcon.setVisibility(View.VISIBLE);
					cover.setBackgroundResource(R.drawable.push_frame);
				} else {
					newIcon.setVisibility(View.INVISIBLE);
					cover.setBackgroundResource(R.drawable.push_frame2);
				}
				
				tvInfo.setText(notification.getMessage());
				tvRegdate.setText(StringUtils.getDateString(
						"yyyy년 MM월 dd일", notification.getCreated_at() * 1000));
				
				setImage(ivImage, notification.getImg_url());
			}
		} catch (Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setListeners() {
	}
	
	@Override
	public void setUnusableView() {

	}
}
