package com.byecar.fragments.user;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.byecar.byecarplus.MainActivity;
import com.byecar.byecarplus.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPAdapter;
import com.byecar.classes.BCPAuctionableListFragment;
import com.byecar.classes.BCPConstants;
import com.byecar.models.Car;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.AppInfoUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;

public class CarListPage extends BCPAuctionableListFragment {

	private SwipeRefreshLayout swipeRefreshLayout;
	private Button btnRegistration;
	private Button btnRequestCertification;
	private Button btnCurrentOrder;
	private Button btnBanner;
	private Button btnGuide;
	private Button btnSearch;
	
	private int firstVisibleItem;
	private int standardLength;
	private float diff;
	private int type;
	private String orderString;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.carListPage_titleBar);
		
		swipeRefreshLayout = (SwipeRefreshLayout) mThisView.findViewById(R.id.carListPage_swipe_container);
		listView = (ListView) mThisView.findViewById(R.id.carListPage_listView);
		btnRegistration = (Button) mThisView.findViewById(R.id.carListPage_btnRegistration);
		btnRequestCertification = (Button) mThisView.findViewById(R.id.carListPage_btnRequestCertification);
		btnCurrentOrder = (Button) mThisView.findViewById(R.id.carListPage_btnCurrentOrder);
		btnBanner = (Button) mThisView.findViewById(R.id.carListPage_btnBanner);
		btnGuide = (Button) mThisView.findViewById(R.id.carListPage_btnGuide);
		btnSearch = (Button) mThisView.findViewById(R.id.carListPage_btnSearch);
	}

	@Override
	public void setVariables() {
		
		if(getArguments() != null) {
			type = getArguments().getInt("type");
		}
		
		orderString = "date";
	}

	@Override
	public void createPage() {

		adapter = new BCPAdapter(mContext, mActivity, this, mActivity.getLayoutInflater(), models);
		listView.setAdapter(adapter);
    	
        swipeRefreshLayout.setColorSchemeColors(
        		getResources().getColor(R.color.titlebar_bg_orange),
        		getResources().getColor(R.color.titlebar_bg_brown), 
        		getResources().getColor(R.color.titlebar_bg_orange), 
        		getResources().getColor(R.color.titlebar_bg_brown));
        
        swipeRefreshLayout.setEnabled(true);

		if(type != Car.TYPE_DIRECT_NORMAL) {
			titleBar.setBgColor(Color.WHITE);
			titleBar.setBgAlpha(0);
			
			listView.setDivider(new ColorDrawable(Color.TRANSPARENT));
    		listView.setDividerHeight(ResizeUtils.getSpecificLength(16));
		} else {
			listView.setDivider(null);
    		listView.setDividerHeight(0);
		}
		
		switch(type) {
		
		case Car.TYPE_BID:
			btnCurrentOrder.setBackgroundResource(R.drawable.sort_toggle_a);
			btnRegistration.setVisibility(View.VISIBLE);
			break;
			
		case Car.TYPE_DEALER:
			btnCurrentOrder.setBackgroundResource(R.drawable.used_sort1_btn);
			break;
			
		case Car.TYPE_DIRECT_CERTIFIED:
			btnCurrentOrder.setBackgroundResource(R.drawable.sort_toggle_c);
			btnRequestCertification.setVisibility(View.VISIBLE);
			break;
			
		case Car.TYPE_DIRECT_NORMAL:
			btnCurrentOrder.setBackgroundResource(R.drawable.sort_toggle_c);
			btnRegistration.setVisibility(View.VISIBLE);
			mThisView.findViewById(R.id.carListPage_btnGuide).setBackgroundResource(R.drawable.normal_direct_guide_btn);
			mThisView.findViewById(R.id.carListPage_btnSearch).setBackgroundResource(R.drawable.normal_direct_search_btn);
			btnBanner.setVisibility(View.VISIBLE);
			break;
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
				
				CarListPage.this.firstVisibleItem = firstVisibleItem;
				
				if(firstVisibleItem + visibleItemCount == totalItemCount) {
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
					Car car = (Car) models.get(position);
					((MainActivity)mActivity).showCarDetailPage(0, car, car.getType());
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

		btnCurrentOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				String[] strings = new String[] {
						null,
						getString(R.string.order_2)
				};
				
				if(type == Car.TYPE_BID) {
					strings[0] = getString(R.string.order_1);
				} else {
					strings[0] = getString(R.string.order_3);
				}
				
				mActivity.showSelectDialog(title, strings, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						if(which == 0) {
							orderString = "date";
							
							switch(type) {
							
							case Car.TYPE_BID:
								btnCurrentOrder.setBackgroundResource(R.drawable.sort_toggle1);
								break;
								
							case Car.TYPE_DEALER:
								btnCurrentOrder.setBackgroundResource(R.drawable.used_sort1_btn);
								break;
								
							case Car.TYPE_DIRECT_CERTIFIED:
							case Car.TYPE_DIRECT_NORMAL:
								btnCurrentOrder.setBackgroundResource(R.drawable.sort_toggle3);
								break;
							}
						} else {
							orderString = "like";
							
							switch(type) {
							
							case Car.TYPE_BID:
								btnCurrentOrder.setBackgroundResource(R.drawable.sort_toggle2);
								break;
								
							case Car.TYPE_DEALER:
								btnCurrentOrder.setBackgroundResource(R.drawable.used_sort2_btn);
								break;
								
							case Car.TYPE_DIRECT_CERTIFIED:
							case Car.TYPE_DIRECT_NORMAL:
								btnCurrentOrder.setBackgroundResource(R.drawable.sort_toggle2);
								break;
							}
						}
						
						refreshPage();
					}
				});
			}
		});
		
		btnRegistration.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putInt("type", CarRegistrationPage.TYPE_REGISTRATION);
				bundle.putInt("carType", type);
				mActivity.showPage(BCPConstants.PAGE_CAR_REGISTRATION, bundle);
			}
		});
		
		btnRequestCertification.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putInt("type", CarRegistrationPage.TYPE_REQUEST_CERTIFICATION);
				bundle.putInt("carType", Car.TYPE_DIRECT_CERTIFIED);
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

				Bundle bundle = new Bundle();
				bundle.putInt("type", type);
				mActivity.showPage(BCPConstants.PAGE_SEARCH_CAR, bundle);
			}
		});
	
		btnBanner.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putInt("type", Car.TYPE_DIRECT_CERTIFIED);
				mActivity.showPage(BCPConstants.PAGE_CAR_LIST, bundle);
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		
		//swipe_container.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.carListPage_swipe_container).getLayoutParams();
		rp.bottomMargin = ResizeUtils.getSpecificLength(89);
		
		if(type == Car.TYPE_DIRECT_NORMAL) {
			rp.topMargin = ResizeUtils.getSpecificLength(176);
		}
		
		//buttonBg.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.carListPage_buttonBg).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(100);
		
		int p = ResizeUtils.getSpecificLength(8);
		
		//btnCurrentOrder.
		rp = (RelativeLayout.LayoutParams) btnCurrentOrder.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(72);
		
		if(type == Car.TYPE_DEALER) {
			rp.width = ResizeUtils.getSpecificLength(624);
			rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		} else {
			rp.width = ResizeUtils.getSpecificLength(72);
			rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			rp.leftMargin = p;
		}

		rp.bottomMargin = p;
		
		//btnRegistration.
		rp = (RelativeLayout.LayoutParams) btnRegistration.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(544);
		rp.height = ResizeUtils.getSpecificLength(72);
		rp.rightMargin = p;
		rp.bottomMargin = p;
		
		//btnRequestCertification.
		rp = (RelativeLayout.LayoutParams) btnRequestCertification.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(544);
		rp.height = ResizeUtils.getSpecificLength(72);
		rp.rightMargin = p;
		rp.bottomMargin = p;

		//btnBanner.
		rp = (RelativeLayout.LayoutParams) btnBanner.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(96);
		
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
		
		case Car.TYPE_BID:
			return R.drawable.auction_back_btn;
			
		case Car.TYPE_DEALER:
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
		
		case Car.TYPE_BID:
			return 251;
			
		case Car.TYPE_DEALER:
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
		
		case Car.TYPE_BID:
			url = BCPAPIs.CAR_BID_LIST_URL;
			break;
			
		case Car.TYPE_DEALER:
			url = BCPAPIs.CAR_DEALER_LIST_URL;
			break;
			
		case Car.TYPE_DIRECT_CERTIFIED:
			url = BCPAPIs.CAR_DIRECT_CERTIFIED_LIST_URL;
			break;
			
		case Car.TYPE_DIRECT_NORMAL:
			url = BCPAPIs.CAR_DIRECT_NORMAL_LIST_URL;
			break;
		}

		url += "?order=" + orderString;
		
		super.downloadInfo();
	}
	
	@Override
	public boolean parseJSON(JSONObject objJSON) {

		int size = 0;
		int itemCode = 0;
		
		switch (type) {
		case Car.TYPE_BID:
			itemCode = BCPConstants.ITEM_CAR_BID;
			break;
		case Car.TYPE_DEALER:
			itemCode = BCPConstants.ITEM_CAR_DEALER;
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
	public void onPause() {
		super.onPause();

		try {
			
			
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	@Override
	public void onDestroyView() {
		
		listView.setOnScrollListener(null);
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
				if(models.size() == 0) {
					titleBar.setBgAlpha(0);
					return;
				} else if(adapter.getFirstView() != null) {

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

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void bidStatusChanged(String event, Car car) {

		if(event == null) {
			return;
		}

		car.setItemCode(BCPConstants.ITEM_CAR_BID);
		
		//경매가 시작되는 물건이 있는 경우.
		if(event.equals("auction_begun")) {
			//시간에 맞게 추가.
			boolean added = false;
			
			//기존에 같은 매물이 있다면 최신화.
			for(int i=0; i<models.size(); i++) {
				
				if(((Car)models.get(i)).getId() == car.getId()) {
					((Car)models.get(i)).copyValuesFromNewItem(car);
					adapter.notifyDataSetChanged();
					return;
				}
			}
			
			
			for(int i=0; i<models.size(); i++) {
				//새로운 매물보다 더 늦게 끝나는 매물이 있거나 경매중이 아닌 매물이 있다면 그 위에 삽입.
				if(((Car)models.get(i)).getBid_until_at() < car.getBid_begin_at()
						|| ((Car)models.get(i)).getStatus() != Car.STATUS_BIDDING) {
					models.add(i, car);
					adapter.notifyDataSetChanged();
					added = true;
					
					if(i <= listView.getFirstVisiblePosition()) {
						
						if(AppInfoUtils.checkMinVersionLimit(android.os.Build.VERSION_CODES.HONEYCOMB)) {
							listView.smoothScrollToPositionFromTop(listView.getFirstVisiblePosition() + 1, 
									listView.getChildAt(0).getTop(), 
									0);
						} else {
							listView.smoothScrollToPosition(listView.getFirstVisiblePosition() + 1);
						}
					}
					break;
				}
			}
			
			if(!added) {
				models.add(car);
				adapter.notifyDataSetChanged();
			}
			
		//경매 매물의 가격 변화가 있는 경우.
		} else if(event.equals("bid_price_updated")) {
			//해당 매물 수정.
			for(int i=0; i<models.size(); i++) {
			
				if(((Car)models.get(i)).getId() == car.getId()) {
					((Car)models.get(i)).copyValuesFromNewItem(car);
					adapter.notifyDataSetChanged();
				}
			}
			
		//관리자에 의해 보류된 경우.
		} else if(event.equals("auction_held")) {
			//해당 매물 삭제.
			
			for(int i=0; i<models.size(); i++) {
				
				if(((Car)models.get(i)).getId() == car.getId()) {
					models.remove(i);
					adapter.notifyDataSetChanged();
					
					if(i <= listView.getFirstVisiblePosition()
							&& listView.getFirstVisiblePosition() > 0) {
						
						if(AppInfoUtils.checkMinVersionLimit(android.os.Build.VERSION_CODES.HONEYCOMB)) {
							listView.smoothScrollToPositionFromTop(listView.getFirstVisiblePosition() - 1, 
									listView.getChildAt(0).getTop(),
									0);
						} else {
							listView.smoothScrollToPosition(listView.getFirstVisiblePosition() - 1);
						}
					}
					break;
				}
			}
			
		//딜러 선택 시간이 종료된 경우 (유찰).
		//유저가 딜러를 선택한 경우 (낙찰).
		} else if(event.equals("selection_time_ended")
				|| event.equals("dealer_selected")) {
			
			boolean added = false;
			
			//해당 매물 수정 및 위치 변경(리스트에 거래종료인 매물이 있으면 등록순으로 삽입, 거래종료상태인 매물이 없으면 제일 아래로 삽입).
			for(int i=0; i<models.size(); i++) {
				
				if(((Car)models.get(i)).getId() == car.getId()) {
					models.remove(i);
					continue;
				}
				
				//유찰, 거래종료를 상태이고 등록일이 빠른 매물이 있으면 해당 위치에 삽입.
				if(((Car)models.get(i)).getStatus() > Car.STATUS_BID_SUCCESS
						&& ((Car)models.get(i)).getCreated_at() < car.getCreated_at()) {
					models.add(i, car);
					adapter.notifyDataSetChanged();
					added = true;
					
					if(i <= listView.getFirstVisiblePosition()) {
						
						if(AppInfoUtils.checkMinVersionLimit(android.os.Build.VERSION_CODES.HONEYCOMB)) {
							listView.smoothScrollToPositionFromTop(listView.getFirstVisiblePosition() + 1, 
									listView.getChildAt(0).getTop(), 
									0);
						} else {
							listView.smoothScrollToPosition(listView.getFirstVisiblePosition() + 1);
						}
					}
					break;
				}
			}
			
			if(!added) {
				models.add(car);
				adapter.notifyDataSetChanged();
			}
		}
	}
}