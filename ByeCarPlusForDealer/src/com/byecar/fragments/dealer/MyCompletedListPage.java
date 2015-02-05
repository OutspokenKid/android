package com.byecar.fragments.dealer;

import org.json.JSONArray;
import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.byecar.byecarplusfordealer.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPAdapter;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPFragment;
import com.byecar.models.Car;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class MyCompletedListPage extends BCPFragment {

	private Button btnAuction;
	private Button btnDealer;
	
	private ListView listView;
	
	private int menuIndex;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.myCompletedListPage_titleBar);
		
		btnAuction = (Button) mThisView.findViewById(R.id.myCompletedListPage_btnAuction);
		btnDealer = (Button) mThisView.findViewById(R.id.myCompletedListPage_btnDealer);
		
		listView = (ListView) mThisView.findViewById(R.id.myCompletedListPage_listView);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {
			
			menuIndex = getArguments().getInt("menuIndex");

			if(menuIndex < 0 || menuIndex > 1) {
				menuIndex = 0;
			}
		}
	}

	@Override
	public void createPage() {

		
		adapter = new BCPAdapter(mContext, mActivity, getActivity().getLayoutInflater(), models);
		listView.setAdapter(adapter);
		listView.setDividerHeight(0);
		listView.setDivider(null);
	}

	@Override
	public void setListeners() {

		btnAuction.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(0);
			}
		});
		
		btnDealer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				setMenu(1);
			}
		});
		
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				if(visibleItemCount < totalItemCount && firstVisibleItem + visibleItemCount == totalItemCount) {
					downloadInfo();
				}
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {

			}
		});
	}

	@Override
	public void setSizes() {

		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, 88, 
				mThisView.findViewById(R.id.myCompletedListPage_bgForButtons), null, null, null);
		
		ResizeUtils.viewResizeForRelative(320, 88, btnAuction, null, null, null);
		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, 88, btnDealer, null, null, null);
	}

	@Override
	public int getContentViewId() {
		
		return R.layout.fragment_my_completed_list;
	}

	@Override
	public int getBackButtonResId() {
		
		return R.drawable.deal_complete_back_btn;
	}

	@Override
	public int getBackButtonWidth() {

		return 261;
	}

	@Override
	public int getBackButtonHeight() {

		return 60;
	}
	
	@Override
	public void downloadInfo() {

		if(menuIndex == 0) {
			url = BCPAPIs.MY_CAR_URL + "?status=ended";
		} else {
			url = BCPAPIs.MY_CAR_URL + "?status=ended";
		}
		 
		super.downloadInfo();
	}
	
	@Override
	public boolean parseJSON(JSONObject objJSON) {
		
		try {
			JSONArray arJSON = objJSON.getJSONArray("onsalecars");
			int size = arJSON.length();
			
			for(int i=0; i<size; i++) {
				Car car = new Car(arJSON.getJSONObject(i));
				
				if(menuIndex == 0) {
					car.setItemCode(BCPConstants.ITEM_CAR_MY_AUCTION);
				} else {
					car.setItemCode(BCPConstants.ITEM_CAR_MY_DEALER);
				}
				
				models.add(car);
			}
			
			return true;
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
	public int getRootViewResId() {

		return R.id.myCompletedListPage_mainLayout;
	}

	@Override
	public void onResume() {
		super.onResume();
		
		if(models.size() == 0) {
			downloadInfo();
		}
	}
	
//////////////////// Custom methods.

	public void setMenu(int menuIndex) {
		
		if(menuIndex == 0) {
			btnAuction.setBackgroundResource(R.drawable.deal_tab1_a);
			btnDealer.setBackgroundResource(R.drawable.deal_tab2_b);
		} else {
			btnAuction.setBackgroundResource(R.drawable.deal_tab1_b);
			btnDealer.setBackgroundResource(R.drawable.deal_tab2_a);
		}
		
		this.menuIndex = menuIndex;
		
		refreshPage();
	}
}
