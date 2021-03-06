package com.byecar.fragments.user;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.byecar.byecarplus.MainActivity;
import com.byecar.byecarplus.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPAdapter;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPFragment;
import com.byecar.models.Car;
import com.byecar.models.Review;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class MyPage extends BCPFragment {

	private Button btnCar;
	private Button btnLike;
	private Button btnReview;
	
	private ListView listView;
	private int menuIndex;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.myPage_titleBar);
		
		btnCar = (Button) mThisView.findViewById(R.id.myPage_btnCar);
		btnReview = (Button) mThisView.findViewById(R.id.myPage_btnReview);
		btnLike = (Button) mThisView.findViewById(R.id.myPage_btnLike);
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

		btnLike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				setMenu(1);
			}
		});
		
		btnReview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				setMenu(2);
			}
		});
		
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
					if(menuIndex != 2) {
						Car car = (Car) models.get(position);
						((MainActivity)mActivity).showCarDetailPage(0, car, car.getType());
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

		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, 88, 
				mThisView.findViewById(R.id.myPage_bgForButtons), null, null, null);
		
		ResizeUtils.viewResizeForRelative(214, 88, btnCar, null, null, null);
		ResizeUtils.viewResizeForRelative(214, 88, btnLike, null, null, null);
		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, 88, btnReview, null, null, null);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_my;
	}

	@Override
	public int getPageTitleTextResId() {

		return R.string.pageTitle_my;
	}

	@Override
	public int getRootViewResId() {

		return R.id.myPage_mainLayout;
	}

	@Override
	public void downloadInfo() {

		if(menuIndex == 0) {
			url = BCPAPIs.MY_CAR_URL;
		} else if(menuIndex == 1) {
			url = BCPAPIs.MY_LIKE_URL;
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
			} else if(menuIndex == 1) {
				arJSON = objJSON.getJSONArray("onsalecars");
				size = arJSON.length();
				
				for(int i=0; i<size; i++) {
					Car car = new Car(arJSON.getJSONObject(i));
					car.setItemCode(BCPConstants.ITEM_CAR_MY_LIKE);
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
	
//////////////////// Custom methods.
	
	public void setMenu(int menuIndex) {
		
		this.menuIndex = menuIndex;
		
		if(menuIndex == 0) {
			btnCar.setBackgroundResource(R.drawable.mypage_tab1_tab_a);
			btnLike.setBackgroundResource(R.drawable.mypage_tab3_tab_b);
			btnReview.setBackgroundResource(R.drawable.mypage_tab4_tab_b);
			
		} else if(menuIndex == 1) {
			btnCar.setBackgroundResource(R.drawable.mypage_tab1_tab_b);
			btnLike.setBackgroundResource(R.drawable.mypage_tab3_tab_a);
			btnReview.setBackgroundResource(R.drawable.mypage_tab4_tab_b);
			
		} else {
			btnCar.setBackgroundResource(R.drawable.mypage_tab1_tab_b);
			btnLike.setBackgroundResource(R.drawable.mypage_tab3_tab_b);
			btnReview.setBackgroundResource(R.drawable.mypage_tab4_tab_a);
		}
		
		refreshPage();
	}
}
