package com.byecar.byecarplus.fragments.main_for_user;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.byecar.byecarplus.R;
import com.byecar.byecarplus.classes.BCPAPIs;
import com.byecar.byecarplus.classes.BCPAdapter;
import com.byecar.byecarplus.classes.BCPConstants;
import com.byecar.byecarplus.classes.BCPFragment;
import com.byecar.byecarplus.models.Car;
import com.byecar.byecarplus.views.TitleBar;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;

public class CarListPage extends BCPFragment {

	private SwipeRefreshLayout swipeRefreshLayout;
	private ListView listView;
	private Button btnRegistration;
	private Button btnRequestCertification;
	private Button btnGuide;
	private Button btnSearch;
	
	private AlphaAnimation aaIn, aaOut;
	
	private int firstVisibleItem;
	private int standardLength;
	private float diff;
	private int type;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.carListPage_titleBar);
		
		swipeRefreshLayout = (SwipeRefreshLayout) mThisView.findViewById(R.id.carListPage_swipe_container);
		listView = (ListView) mThisView.findViewById(R.id.carListPage_listView);
		btnRegistration = (Button) mThisView.findViewById(R.id.carListPage_btnRegistration);
		btnRequestCertification = (Button) mThisView.findViewById(R.id.carListPage_btnRequestCertification);
		btnGuide = (Button) mThisView.findViewById(R.id.carListPage_btnGuide);
		btnSearch = (Button) mThisView.findViewById(R.id.carListPage_btnSearch);
	}

	@Override
	public void setVariables() {

		aaIn = new AlphaAnimation(0, 1);
		aaIn.setDuration(300);
		
		aaOut = new AlphaAnimation(1, 0);
		aaOut.setDuration(300);
		
		if(getArguments() != null) {
			type = getArguments().getInt("type");
		}
	}

	@Override
	public void createPage() {

		if(type != Car.TYPE_DIRECT_NORMAL) {
			titleBar.setBgColor(Color.WHITE);
			titleBar.setBgAlpha(0);
		}
		
		if(type != Car.TYPE_AUCTION) {
			btnSearch.setVisibility(View.VISIBLE);
		} else {
			btnSearch.setVisibility(View.INVISIBLE);
		}

		if(type == Car.TYPE_AUCTION) {
			btnRegistration.setVisibility(View.VISIBLE);
		} else if(type == Car.TYPE_DIRECT_CERTIFIED) {
			btnRequestCertification.setVisibility(View.VISIBLE);
		}
		
		adapter = new BCPAdapter(mContext, mActivity, mActivity.getLayoutInflater(), models);
		listView.setAdapter(adapter);
    	
        swipeRefreshLayout.setColorSchemeColors(
        		Color.argb(255, 255, 102, 153), 
        		Color.argb(255, 255, 153, 153), 
        		Color.argb(255, 255, 204, 153), 
        		Color.argb(255, 255, 255, 153));
        swipeRefreshLayout.setEnabled(true);
        
        listView.setDivider(null);
		listView.setDividerHeight(0);
	}

	@Override
	public void setListeners() {

		listView.setOnScrollListener(new OnScrollListener() {

			int lastStatus = 0;
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
				View target = null;
				
				if(type == Car.TYPE_AUCTION) {
					target = btnRegistration;
				} else if(type == Car.TYPE_DIRECT_CERTIFIED) {
					target = btnRequestCertification;
				}
				
				if(target != null) {
					if(scrollState == 0 && lastStatus != 0) {
						target.setVisibility(View.VISIBLE);
						target.startAnimation(aaIn);
						
					} else if(scrollState != 0 && lastStatus == 0) {
						target.setVisibility(View.INVISIBLE);
						target.startAnimation(aaOut);
					}
					
					lastStatus = scrollState;
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				CarListPage.this.firstVisibleItem = firstVisibleItem;
				
				if(visibleItemCount < totalItemCount && firstVisibleItem + visibleItemCount == totalItemCount) {
					downloadInfo();
				}

				checkPageScrollOffset();
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				try {
					Bundle bundle = new Bundle();
					bundle.putInt("id", ((Car) models.get(position)).getId());
					bundle.putInt("type", type);
					mActivity.showPage(BCPConstants.PAGE_CAR_DETAIL, bundle);
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}
			}
		});
		
		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {

				swipeRefreshLayout.setRefreshing(true);
				
				new Handler().postDelayed(new Runnable() {
			        @Override 
			        public void run() {
			        	
			        	refreshPage();
			        }
			    }, 2000);
			}
		});
		
		btnRegistration.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putInt("type", CarRegistrationPage.TYPE_REGISTRATION);
				mActivity.showPage(BCPConstants.PAGE_CAR_REGISTRATION, bundle);
			}
		});
		
		btnRequestCertification.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putInt("type", CarRegistrationPage.TYPE_REQUEST_CERTIFICATION);
				mActivity.showPage(BCPConstants.PAGE_CAR_REGISTRATION, bundle);
			}
		});
		
		btnGuide.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				ToastUtils.showToast("가이드");
			}
		});

		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(BCPConstants.PAGE_SEARCH_CAR, null);
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		
		if(type == Car.TYPE_DIRECT_NORMAL) {
			rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
					R.id.carListPage_swipe_container).getLayoutParams();
			rp.topMargin = ResizeUtils.getSpecificLength(88);
		}
		
		//btnRegistration.
		rp = (RelativeLayout.LayoutParams) btnRegistration.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(120);
		rp.height = ResizeUtils.getSpecificLength(120);
		
		//btnRequestCertification.
		rp = (RelativeLayout.LayoutParams) btnRequestCertification.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(120);
		rp.height = ResizeUtils.getSpecificLength(120);
		
		//btnGuide.
		rp = (RelativeLayout.LayoutParams) btnGuide.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(60);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.topMargin = ResizeUtils.getSpecificLength(14);
		rp.rightMargin = ResizeUtils.getSpecificLength(10);
		
		//btnSearch.
		rp = (RelativeLayout.LayoutParams) btnSearch.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(60);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.rightMargin = ResizeUtils.getSpecificLength(10);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_car_list;
	}

	@Override
	public int getBackButtonResId() {

		switch(type) {
		
		case Car.TYPE_AUCTION:
			return R.drawable.auction_back_btn;
			
		case Car.TYPE_USED:
			return R.drawable.usedmarket_search_back_btn;
			
		case Car.TYPE_DIRECT_CERTIFIED:
			return R.drawable.directmarket_verification_back_btn;
			
		case Car.TYPE_DIRECT_NORMAL:
			return R.drawable.normal_direct_back_btn;
		}
		
		return R.drawable.auction_back_btn;
	}

	@Override
	public int getBackButtonWidth() {

		switch(type) {
		
		case Car.TYPE_AUCTION:
			return 251;
			
		case Car.TYPE_USED:
			return 216;
			
		case Car.TYPE_DIRECT_CERTIFIED:
			return 322;
			
		case Car.TYPE_DIRECT_NORMAL:
			return 235;
		}
		
		return 251;
	}

	@Override
	public int getBackButtonHeight() {

		return 60;
	}

	@Override
	public void downloadInfo() {
		
		switch(type) {
		
		case Car.TYPE_AUCTION:
			url = BCPAPIs.BIDS_LIST_URL;
			break;
			
		case Car.TYPE_USED:
			url = BCPAPIs.DEALER_LIST_URL;
			break;
			
		case Car.TYPE_DIRECT_CERTIFIED:
			url = BCPAPIs.DIRECT_MARKET_CERTIFIED_URL;
			break;
			
		case Car.TYPE_DIRECT_NORMAL:
			url = BCPAPIs.DIRECT_MARKET_NORMAL_URL;
			break;
		}
		
		super.downloadInfo();
	}
	
	@Override
	public boolean parseJSON(JSONObject objJSON) {

		int size = 0;
		int itemCode = 0;
		
		switch (type) {
		case Car.TYPE_AUCTION:
			itemCode = BCPConstants.ITEM_CAR_AUCTION;
			break;
		case Car.TYPE_USED:
			itemCode = BCPConstants.ITEM_CAR_USED;
			break;
		case Car.TYPE_DIRECT_CERTIFIED:
			itemCode = BCPConstants.ITEM_CAR_DIRECT_CERTIFIED;
			break;
		case Car.TYPE_DIRECT_NORMAL:
			itemCode = BCPConstants.ITEM_CAR_DIRECT_NORMAL;
		}
		
		try {
			JSONArray arJSON = objJSON.getJSONArray("onsalecars");
			
			size = arJSON.length();
			for(int i=0; i<size; i++) {
				Car car = new Car(arJSON.getJSONObject(i));
				car.setItemCode(itemCode);
				models.add(car);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		} finally {
			swipeRefreshLayout.setRefreshing(false);
		}
		
		if(size < NUMBER_OF_LISTITEMS) {
			return true;
		} else {
			return false;
		}
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
		listView.setOnItemClickListener(null);
		swipeRefreshLayout.setOnRefreshListener(null);
		
		super.onDestroyView();
	}
	
	@Override
	public int getRootViewResId() {

		return R.id.carListPage_mainLayout;
	}
	
//////////////////// Custom methods.
	
	public void checkPageScrollOffset() {

		if(type != Car.TYPE_DIRECT_NORMAL) {
			
			if(standardLength == 0) {
				standardLength = ResizeUtils.getSpecificLength(500);
			}
			
			if(diff == 0) {
				diff = 1f / (float) standardLength; 
			}
			
			try {
				if(adapter.getFirstView() != null) {
					
					if(firstVisibleItem == 0) {
						int offset = -adapter.getFirstView().getTop();
						
						if(offset < standardLength) {
							titleBar.setBgAlpha(diff * offset);
							return;
						}
					}
				}
				
				titleBar.setBgAlpha(1);
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
		}
	}
}
