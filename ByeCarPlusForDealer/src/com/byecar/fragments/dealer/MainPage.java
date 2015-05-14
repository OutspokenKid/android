package com.byecar.fragments.dealer;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.byecar.byecarplusfordealer.GuideActivity;
import com.byecar.byecarplusfordealer.MainActivity;
import com.byecar.byecarplusfordealer.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPAdapter;
import com.byecar.classes.BCPAuctionableFragment;
import com.byecar.classes.BCPConstants;
import com.byecar.fragments.CarRegistrationPage;
import com.byecar.fragments.OpenablePostListPage;
import com.byecar.models.Banner;
import com.byecar.models.Car;
import com.byecar.models.CompanyInfo;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;

public class MainPage extends BCPAuctionableFragment {

	private SwipeRefreshLayout swipeRefreshLayout;
	
	private Button[] btnTabs = new Button[4];
	private View icon;
	private View buttonBg;
	private Button btnRegistration;
	private Button btnGuide;
	private Button btnSearch;
	private Button btnMyCar;
	
	private int menuIndex = -1;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.mainPage_titleBar);
		
		btnTabs[0] = (Button) mThisView.findViewById(R.id.mainPage_btnTab1);
		btnTabs[1] = (Button) mThisView.findViewById(R.id.mainPage_btnTab2);
		btnTabs[2] = (Button) mThisView.findViewById(R.id.mainPage_btnTab3);
		btnTabs[3] = (Button) mThisView.findViewById(R.id.mainPage_btnTab4);
		
		swipeRefreshLayout = (SwipeRefreshLayout) mThisView.findViewById(R.id.mainPage_swipe_container);
		listView = (ListView) mThisView.findViewById(R.id.mainPage_listView);

		icon = mThisView.findViewById(R.id.mainPage_icon);

		buttonBg = mThisView.findViewById(R.id.mainPage_buttonBg);
		btnRegistration = (Button) mThisView.findViewById(R.id.mainPage_btnRegistration);
		
		btnGuide = (Button) mThisView.findViewById(R.id.mainPage_btnGuide);
		btnSearch = (Button) mThisView.findViewById(R.id.mainPage_btnSearch);
		btnMyCar = (Button) mThisView.findViewById(R.id.mainPage_btnMyCar);
	}

	@Override
	public void setVariables() {

	}

	@Override
	public void createPage() {
		
		adapter = new BCPAdapter(mContext, mActivity, mActivity.getLayoutInflater(), models);
		listView.setAdapter(adapter);
		listView.setDivider(new ColorDrawable(Color.TRANSPARENT));
		listView.setDividerHeight(ResizeUtils.getSpecificLength(30));
		
		swipeRefreshLayout.setColorSchemeColors(
        		getResources().getColor(R.color.titlebar_bg_brown),
        		getResources().getColor(R.color.titlebar_bg_orange),
        		getResources().getColor(R.color.titlebar_bg_brown), 
        		getResources().getColor(R.color.titlebar_bg_orange));
        
        swipeRefreshLayout.setEnabled(true);
		setMenu(0);
	}

	@Override
	public void setListeners() {

		titleBar.getMenuButton().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(((MainActivity)mActivity).isOpen()) {
					((MainActivity)mActivity).closeMenu();
				} else {
					((MainActivity)mActivity).openMenu();
				}
			}
		});
		
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				if(models.size() != 0 && firstVisibleItem + visibleItemCount == totalItemCount) {
					downloadInfo();
				}
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				try {
					if(models.get(position) instanceof Banner) {
						Bundle bundle = new Bundle();
						bundle.putInt("type", OpenablePostListPage.TYPE_NOTICE);
						mActivity.showPage(BCPConstants.PAGE_OPENABLE_POST_LIST, bundle);
						
					} else {
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
		
		btnTabs[0].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(0);
			}
		});
		
		btnTabs[1].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(1);
			}
		});
		
		btnTabs[2].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(2);
			}
		});
		
		btnTabs[3].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(3);
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

		btnGuide.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Intent intent = new Intent(mActivity, GuideActivity.class);
				intent.putExtra("type", GuideActivity.TYPE_DEALER);
				mActivity.startActivity(intent);
			}
		});
		
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				
				if(menuIndex == 0) {
					bundle.putInt("type", Car.TYPE_BID);
				} else {
					bundle.putInt("type", Car.TYPE_DEALER);
				}
				
				mActivity.showPage(BCPConstants.PAGE_SEARCH_CAR, bundle);
			}
		});
		
		btnMyCar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				ToastUtils.showToast("btnMyCar");
				
//				Bundle bundle = new Bundle();
//				
//				if(menuIndex == 0) {
//					bundle.putInt("type", Car.TYPE_BID);
//				} else {
//					bundle.putInt("type", Car.TYPE_DEALER);
//				}
//				
//				mActivity.showPage(BCPConstants.PAGE_SEARCH_CAR, bundle);
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		
		//bgForButtons.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.mainPage_bgForButtons).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(88);
		
		for(int i=0; i<btnTabs.length; i++) {
			ResizeUtils.viewResizeForRelative(i==btnTabs.length-1?LayoutParams.MATCH_PARENT:160, 88, 
					btnTabs[i], null, null, null);
		}
		
		ResizeUtils.viewResizeForRelative(246, 225, icon, null, null, new int[]{0, 100, 0, 80});

		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, 119, buttonBg, null, null, null);
		ResizeUtils.viewResizeForRelative(612, 84, btnRegistration, null, null, new int[]{0, 0, 0, 16});
		
		ResizeUtils.viewResizeForRelative(60, 60, btnGuide, null, null, new int[]{0, 14, 12, 0});
		ResizeUtils.viewResizeForRelative(60, 60, btnSearch, null, null, new int[]{0, 0, 20, 0});
		ResizeUtils.viewResizeForRelative(80, 60, btnMyCar, null, null, new int[]{0, 0, 20, 0});
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_main;
	}
	
	@Override
	public int getPageTitleTextResId() {

		return 0;
	}

	@Override
	public int getRootViewResId() {

		return R.id.mainPage_mainLayout;
	}

	@Override
	public void downloadInfo() {

		switch(menuIndex) {
			
		case 0:
			url = BCPAPIs.CAR_BID_LIST_URL + "?status=in_progress";
			break;
			
		case 1:
			url = BCPAPIs.MY_BIDS_URL + "?status=in_progress";
			break;
			
		case 2:
			url = BCPAPIs.MY_BIDS_URL + "?status=" + Car.STATUS_BID_SUCCESS;
			break;
			
		case 3:
			url = BCPAPIs.CAR_DEALER_LIST_URL + "?status=in_progress";
			break;
		}
		
		super.downloadInfo();
	}
	
	@Override
	public boolean parseJSON(JSONObject objJSON) {
		
		int size = 0;
		int itemCode = 0;
		
		switch (menuIndex) {
		case 0:
			itemCode = BCPConstants.ITEM_CAR_BID_IN_PROGRESS;
			break;
		case 1:
			itemCode = BCPConstants.ITEM_CAR_BID_MY;
			break;
		case 2:
			itemCode = BCPConstants.ITEM_CAR_BID_SUCCESS;
			break;
		case 3:
			itemCode = BCPConstants.ITEM_CAR_DEALER;
			break;
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
		
		titleBar.getMenuButton().setVisibility(View.VISIBLE);
		checkNotification();
		
		if(MainActivity.companyInfo == null) {
			checkCover();
		}
		
		if(models.size() == 0) {
			downloadInfo();
		}
	}
	
//////////////////// Custom methods.
	
	public void setMenu(int index) {
		
		setMenuButtons(index);
		
		int lastIndex = menuIndex;
		menuIndex = index;
		
		if(index == 3) {
			swipeRefreshLayout.setPadding(0, 0, 0, ResizeUtils.getSpecificLength(115));
			buttonBg.setVisibility(View.VISIBLE);
			btnRegistration.setVisibility(View.VISIBLE);
		} else {
			swipeRefreshLayout.setPadding(0, 0, 0, 0);
			buttonBg.setVisibility(View.INVISIBLE);
			btnRegistration.setVisibility(View.INVISIBLE);
		}
		
		//처음 다운로드가 아니라면,
		if(lastIndex != -1) {
			refreshPage();
		}
	}
	
	public void setMenuButtons(int index) {
		
		int[] btnResIdsOn = new int[]{
				R.drawable.main_tab1_tab_a,
				R.drawable.main_tab2_tab_a,
				R.drawable.main_tab3_tab_a,
				R.drawable.main_tab4_tab_a,
		};
		
		int[] btnResIdsOff = new int[]{
				R.drawable.main_tab1_tab_b,
				R.drawable.main_tab2_tab_b,
				R.drawable.main_tab3_tab_b,
				R.drawable.main_tab4_tab_b,
		};
		
		for(int i=0; i<btnTabs.length; i++) {
			
			if(index == i) {
				btnTabs[i].setBackgroundResource(btnResIdsOn[i]);
			} else {
				btnTabs[i].setBackgroundResource(btnResIdsOff[i]);
			}
		}
	}

	public void checkNotification() {

		titleBar.setNotificationCount(0);
		((MainActivity)mActivity).setNotificationCount(0);
		
		String url = BCPAPIs.NOTIFICATION_URL + "?num=1";
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("MainPage.checkNotification.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("MainPage.checkNotification.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					int count = 0;
					
					if(objJSON.getInt("result") == 1) {
						count = objJSON.getInt("unreadCount");
					}
					
					titleBar.setNotificationCount(count);
					((MainActivity)mActivity).setNotificationCount(count);
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}

	public void checkCover() {

		String url = BCPAPIs.MAIN_COVER_URL;
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("MainPage.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("MainPage.checkCover.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					try {
						MainActivity.companyInfo = new CompanyInfo(objJSON.getJSONObject("company_info"));
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (Error e) {
						LogUtils.trace(e);
					}

					downloadInfo();
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}

	@Override
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void bidStatusChanged(String event, Car car) {

		if(event == null) {
			return;
		}
		
		if(car.getStatus() > Car.STATUS_BID_COMPLETE) {
			return;
		}
		
		if(menuIndex == 3) {
			return;
		}

		switch(menuIndex) {
		
		case 0:
			car.setItemCode(BCPConstants.ITEM_CAR_BID_IN_PROGRESS);
			break;
			
		case 1:
			car.setItemCode(BCPConstants.ITEM_CAR_BID_MY);
			break;
			
		case 2:
			car.setItemCode(BCPConstants.ITEM_CAR_BID_SUCCESS);
			break;
		}
		
		//경매가 시작되는 물건이 있는 경우.
		//딜러 선택 시간이 종료된 경우 (유찰).
		//유저가 딜러를 선택한 경우 (낙찰).
		if(event.equals("auction_begun")
				|| event.equals("selection_time_ended")
				|| event.equals("dealer_selected")) {
			//새로운 매물보다 더 늦게 끝나는 매물이 있거나 경매중이 아닌 매물이 있다면 그 위에 삽입.
			reorderList(startIndex, car);
			
		//경매 매물의 가격 변화가 있는 경우.
		} else if(event.equals("bid_price_updated")) {
			updateSelectedCar(car);

		//경매가 종료된 물건이 있는 경우.
		} else if(event.equals("auction_ended")) {
			reorderList(startIndex, car);
			
		//관리자에 의해 보류된 경우.
		} else if(event.equals("auction_held")) {
			//해당 매물 삭제.
			deleteSelectedCar(car);
		}
	}
}
