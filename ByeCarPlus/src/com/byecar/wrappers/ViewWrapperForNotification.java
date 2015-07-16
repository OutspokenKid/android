package com.byecar.wrappers;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.byecar.byecarplus.MainActivity;
import com.byecar.byecarplus.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.fragments.NotificationPage;
import com.byecar.models.Notification;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class ViewWrapperForNotification extends ViewWrapper {

	private Notification notification;
	
	private ImageView ivImage;
	private View cover;
	private View newIcon;
	private TextView tvRegdate;
	private TextView tvInfo;
	private Button btnDelete;
	
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
			btnDelete = (Button) row.findViewById(R.id.list_notification_btnDelete);
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
			ResizeUtils.viewResizeForRelative(35, 35, btnDelete, null, null, new int[]{0, 0, 35, 0});
			
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
		
		btnDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				delete();
			}
		});
	}
	
	@Override
	public void setUnusableView() {

	}
	
	public void delete() {

		if(notification == null) {
			return;
		}
		
		//http://dev.bye-car.com/notifications/delete.json?notification_id=1
		String url = BCPAPIs.NOTIFICATION_DELETE_URL + "?notification_id=" + notification.getId();
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("ViewWrapperForNotification.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("ViewWrapperForNotification.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						((NotificationPage)MainActivity.activity.getTopFragment()).deleteNotification(notification);
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
}
