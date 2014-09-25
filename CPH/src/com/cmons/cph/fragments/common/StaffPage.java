package com.cmons.cph.fragments.common;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForShop;
import com.cmons.cph.classes.CphAdapter;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.User;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;

public class StaffPage extends CmonsFragmentForShop {
	
	private Button btnRequest;
	private Button btnStaff;
	
	private ListView listView;
	
	private int menuIndex;
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(models.size() == 0) {
			downloadInfo();
		}
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.commonStaffPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.commonStaffPage_ivBg);
		
		btnRequest = (Button) mThisView.findViewById(R.id.commonStaffPage_btnRequest);
		btnStaff = (Button) mThisView.findViewById(R.id.commonStaffPage_btnStaff);
		
		listView = (ListView) mThisView.findViewById(R.id.commonStaffPage_listView);
	}

	@Override
	public void setVariables() {

		title = "직원관리";
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		
		adapter = new CphAdapter(mContext, getActivity().getLayoutInflater(), models);
		listView.setAdapter(adapter);
		listView.setDivider(new ColorDrawable(Color.WHITE));
		listView.setDividerHeight(1);
	}

	@Override
	public void setListeners() {

		btnRequest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(0);
			}
		});
		
		btnStaff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(1);
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				if(menuIndex == 0) {
					approval((User)models.get(arg2));
				} else {
					fire((User)models.get(arg2));
				}
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		
		//ivBg.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.commonStaffPage_ivBg).getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(96); 
		
		//btnRequest.
		rp = (RelativeLayout.LayoutParams) btnRequest.getLayoutParams();
		rp.width = ResizeUtils.getScreenWidth()/2;
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//btnStaff.
		rp = (RelativeLayout.LayoutParams) btnStaff.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_common_staff;
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
		
		try {
			JSONArray arJSON = objJSON.getJSONArray("staffs");
			
			int size = arJSON.length();
			for(int i=0; i<size; i++) {
				User user = new User(arJSON.getJSONObject(i));
				
				LogUtils.log("###StaffPage.parseJSON.  id : " + user.getId() + ", status : " + user.getStatus());
				
				//승인 요청.
				if(menuIndex == 0 && user.getStatus() == 1) {
					continue;
					
				//직원 목록.
				} else if(menuIndex == 1 && user.getStatus() == 0) {
					continue;
				}
				
				user.setItemCode(CphConstants.ITEM_STAFF);
				models.add(user);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		return false;
	}

	@Override
	public void downloadInfo() {

		url = CphConstants.BASE_API_URL + "users/staffs";
		super.downloadInfo();
	}
	
//////////////////// Custom methods.
	
	public void setMenu(int menuIndex) {
		
		switch (menuIndex) {
		
		case 0:
			btnRequest.setBackgroundResource(R.drawable.staff_confirm_btn_a);
			btnStaff.setBackgroundResource(R.drawable.staff_list_btn_b);
			break;
			
		case 1:
			btnRequest.setBackgroundResource(R.drawable.staff_confirm_btn_b);
			btnStaff.setBackgroundResource(R.drawable.staff_list_btn_a);
			break;
		}
		
		this.menuIndex = menuIndex;
		
		refreshPage();
	}
	
	public void approval(User user) {

		String url = CphConstants.BASE_API_URL + "users/staffs/accept" +
				"?staff_id=" + user.getId();
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("StaffPage.approval.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToApproval);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("StaffPage.approval.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_approval);
						refreshPage();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					ToastUtils.showToast(R.string.failToApproval);
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					ToastUtils.showToast(R.string.failToApproval);
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void fire(User user) {

		String url = CphConstants.BASE_API_URL + "users/staffs/decline" +
				"?staff_id=" + user.getId();
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("StaffPage.fire.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToFire);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("StaffPage.fire.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_fire);
						refreshPage();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					ToastUtils.showToast(R.string.failToFire);
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					ToastUtils.showToast(R.string.failToFire);
					LogUtils.trace(oom);
				}
			}
		});

	}

	@Override
	public int getBgResourceId() {

		return R.drawable.staff_bg;
	}
}
