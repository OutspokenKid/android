package com.byecar.byecarplusfordealer.fragments.user;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.byecar.byecarplusfordealer.classes.BCPAPIs;
import com.byecar.byecarplusfordealer.classes.BCPAdapter;
import com.byecar.byecarplusfordealer.classes.BCPConstants;
import com.byecar.byecarplusfordealer.classes.BCPFragment;
import com.byecar.byecarplusfordealer.models.Car;
import com.byecar.byecarplusfordealer.models.Review;
import com.byecar.byecarplusfordealer.views.TitleBar;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class MyPage extends BCPFragment {

	private Button btnCar;
	private Button btnReview;
	
	private ListView listView;
	private int menuIndex;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.myPage_titleBar);
		
		btnCar = (Button) mThisView.findViewById(R.id.myPage_btnCar);
		btnReview = (Button) mThisView.findViewById(R.id.myPage_btnReview);
		listView = (ListView) mThisView.findViewById(R.id.myPage_listView);
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
		listView.setDividerHeight(ResizeUtils.getSpecificLength(20));
		listView.setSelector(new ColorDrawable(Color.TRANSPARENT));
	}

	@Override
	public void setListeners() {

		btnCar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(0);
			}
		});
		
		btnReview.setOnClickListener(new OnClickListener() {

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
			public void onItemClick(AdapterView<?> arg0, View row, int position,
					long arg3) {
				
				Bundle bundle = new Bundle();
				bundle.putInt("id", ((Car)models.get(position)).getId());
				bundle.putInt("type", ((Car)models.get(position)).getType());
				mActivity.showPage(BCPConstants.PAGE_CAR_DETAIL, bundle);
			}
		});
	}

	@Override
	public void setSizes() {

		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, 88, 
				mThisView.findViewById(R.id.myPage_bgForButtons), null, null, null);
		
		ResizeUtils.viewResizeForRelative(320, 88, btnCar, null, null, null);
		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, 88, btnReview, null, null, null);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_my;
	}

	@Override
	public int getBackButtonResId() {

		return R.drawable.mypage_back_btn;
	}

	@Override
	public int getBackButtonWidth() {

		return 235;
	}

	@Override
	public int getBackButtonHeight() {

		return 60;
	}

	@Override
	public int getRootViewResId() {

		return R.id.myPage_mainLayout;
	}

	@Override
	public void downloadInfo() {

		if(menuIndex == 0) {
			url = BCPAPIs.MY_CAR_URL;
		} else {
			url = BCPAPIs.MY_REVIEW_URL;
		}
		
		super.downloadInfo();
	}
	
	@Override
	public boolean parseJSON(JSONObject objJSON) {

		try {
			JSONArray arJSON = null;
			int size = 0;
			
			if(menuIndex == 0) {
				arJSON = objJSON.getJSONArray("onsalecars");
				size = arJSON.length();
				
				for(int i=0; i<size; i++) {
					Car car = new Car(arJSON.getJSONObject(i));
					car.setItemCode(BCPConstants.ITEM_CAR_MY);
					models.add(car);
				}
			} else {
				
				arJSON = objJSON.getJSONArray("reviews");
				size = arJSON.length();
				
				for(int i=0; i<size; i++) {
					Review review = new Review(arJSON.getJSONObject(i));
					review.setItemCode(BCPConstants.ITEM_REVIEW);
					models.add(review);
				}
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
	public void onDestroyView() {
		
		listView.setOnScrollListener(null);
		
		super.onDestroyView();
	}
	
//////////////////// Custom methods.
	
	public void setMenu(int menuIndex) {
		
		this.menuIndex = menuIndex;
		
		if(menuIndex == 0) {
			btnCar.setBackgroundResource(R.drawable.mypage_tab1_tab_a);
			btnReview.setBackgroundResource(R.drawable.mypage_tab2_tab_b);
		} else {
			btnCar.setBackgroundResource(R.drawable.mypage_tab1_tab_b);
			btnReview.setBackgroundResource(R.drawable.mypage_tab2_tab_a);
		}
		
		refreshPage();
	}
}
