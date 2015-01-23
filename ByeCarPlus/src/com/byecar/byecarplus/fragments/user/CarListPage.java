package com.byecar.byecarplus.fragments.user;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.byecar.byecarplus.R;
import com.byecar.byecarplus.classes.BCPAPIs;
import com.byecar.byecarplus.classes.BCPAdapter;
import com.byecar.byecarplus.classes.BCPConstants;
import com.byecar.byecarplus.classes.BCPFragment;
import com.byecar.byecarplus.common.CarRegistrationPage;
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

		adapter = new BCPAdapter(mContext, mActivity, mActivity.getLayoutInflater(), models);
		listView.setAdapter(adapter);
    	
		//56 24 0
		//254 188 42
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
			btnSearch.setVisibility(View.INVISIBLE);
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

				mActivity.showPage(BCPConstants.PAGE_SEARCH_CAR, null);
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
