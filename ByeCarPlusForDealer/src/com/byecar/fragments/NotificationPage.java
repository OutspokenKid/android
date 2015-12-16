package com.byecar.fragments;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.byecar.byecarplusfordealer.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPAdapter;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPFragment;
import com.byecar.models.Notification;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;

public class NotificationPage extends BCPFragment {

	private ListView listView;
	private Button btnReadAll;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.notificationPage_titleBar);
		listView = (ListView) mThisView.findViewById(R.id.notificationPage_listView);
	}

	@Override
	public void setVariables() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createPage() {

		adapter = new BCPAdapter(mContext, mActivity, getActivity().getLayoutInflater(), models);
		listView.setAdapter(adapter);
		listView.setDivider(new ColorDrawable(Color.TRANSPARENT));
		listView.setDividerHeight(ResizeUtils.getSpecificLength(38));
		listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		
		
		if(btnReadAll == null || btnReadAll.getParent() == null) {
			
			btnReadAll = new Button(mContext);
			RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
					ResizeUtils.getSpecificLength(161), ResizeUtils.getSpecificLength(60));
			rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			rp.topMargin = ResizeUtils.getSpecificLength(14);
			rp.rightMargin = ResizeUtils.getSpecificLength(14);
			btnReadAll.setLayoutParams(rp);
			btnReadAll.setBackgroundResource(R.drawable.back_btn);
			titleBar.addView(btnReadAll);
			
			btnReadAll.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					readAll();
				}
			});
		}
	}

	@Override
	public void setListeners() {
		
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				if(firstVisibleItem + visibleItemCount == totalItemCount) {
					downloadInfo();
				}
			}
		});
	
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View row, int position,
					long arg3) {
				
				try {
					Notification notification = (Notification) models.get(position);
					
					if(notification.getRead_at() == 0) {
						requestReadNotification(notification.getId());
						notification.setRead_at(System.currentTimeMillis()/1000);
						adapter.notifyDataSetChanged();
					}
					
					//Action.
					if(notification.getUri() != null) {
						mActivity.handleUri(Uri.parse(notification.getUri()));
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}

			}
		});
	}

	@Override
	public void setSizes() {

	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_common_notification;
	}

	@Override
	public int getRootViewResId() {

		return R.id.notificationPage_mainLayout;
	}

	@Override
	public void downloadInfo() {
		
		url = BCPAPIs.NOTIFICATION_URL;
		super.downloadInfo();
	}
	
	@Override
	public boolean parseJSON(JSONObject objJSON) {

		try {
			JSONArray arJSON = null;
			int size = 0;

			arJSON = objJSON.getJSONArray("notifications");
			size = arJSON.length();
			
			for(int i=0; i<size; i++) {
				Notification notification = new Notification(arJSON.getJSONObject(i));
				notification.setItemCode(BCPConstants.ITEM_NOTIFICATION);
				models.add(notification);
			}
			
			if(size < NUMBER_OF_LISTITEMS) {
				return true;
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
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
	public void onResume() {
		super.onResume();
		
		if(models.size() == 0) {
			downloadInfo();
		}
	}
	
	@Override
	public int getPageTitleTextResId() {

		return R.string.pageTitle_notification;
	}
	
//////////////////// Custom methods.

	public void readAll() {
		
		ToastUtils.showToast("read all notification");
	}
	
	public void deleteNotification(Notification notification) {

		try {
			models.remove(notification);
			adapter.notifyDataSetChanged();
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void requestReadNotification(int notification_id) {
		
		String url = BCPAPIs.NOTIFICATION_READ_URL
				+ "notification_id=" + notification_id;
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("NotificationPage.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("NotificationPage.onCompleted." + "\nurl : " + url
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
