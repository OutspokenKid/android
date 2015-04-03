package com.byecar.fragments.dealer;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.DialogInterface;
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
import com.byecar.models.Post;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class MainPage extends BCPAuctionableFragment {

	private SwipeRefreshLayout swipeRefreshLayout;
	
	private Button btnGuide;
	private Button btnAuction;
	private Button btnMyBids;
	private Button btnUsed;
	private Button btnOnSale;
	
	private View icon;
	private Button btnRegistration2;

	private RelativeLayout buttonRelative;
	private Button btnOrderSmall;
	private Button btnSearch;
	private Button btnRegistration;
	private Button btnOrderBig;
	
	private int menuIndex = -1;
	private String orderString;
	private Banner banner;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.mainPage_titleBar);
		
		btnGuide = (Button) mThisView.findViewById(R.id.mainPage_btnGuide);
		btnAuction = (Button) mThisView.findViewById(R.id.mainPage_btnAuction);
		btnMyBids = (Button) mThisView.findViewById(R.id.mainPage_btnMyBids);
		btnUsed = (Button) mThisView.findViewById(R.id.mainPage_btnUsed);
		btnOnSale = (Button) mThisView.findViewById(R.id.mainPage_btnOnSale);
		
		swipeRefreshLayout = (SwipeRefreshLayout) mThisView.findViewById(R.id.mainPage_swipe_container);
		listView = (ListView) mThisView.findViewById(R.id.mainPage_listView);
		
		icon = mThisView.findViewById(R.id.mainPage_icon);
		btnRegistration2 = (Button) mThisView.findViewById(R.id.mainPage_btnRegistration2);
		
		buttonRelative = (RelativeLayout) mThisView.findViewById(R.id.mainPage_buttonRelative);
		btnOrderSmall = (Button) mThisView.findViewById(R.id.mainPage_btnOrderSmall);
		btnSearch = (Button) mThisView.findViewById(R.id.mainPage_btnSearch);
		btnRegistration = (Button) mThisView.findViewById(R.id.mainPage_btnRegistration);
		btnOrderBig = (Button) mThisView.findViewById(R.id.mainPage_btnOrderBig);
	}

	@Override
	public void setVariables() {

		orderString = "date";
	}

	@Override
	public void createPage() {
		
		adapter = new BCPAdapter(mContext, mActivity, this, mActivity.getLayoutInflater(), models);
		listView.setAdapter(adapter);
		listView.setDivider(new ColorDrawable(Color.TRANSPARENT));
		
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

		titleBar.getBtnNotice().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(BCPConstants.PAGE_NOTIFICATION, null);
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
		
		btnAuction.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(0);
			}
		});
		
		btnMyBids.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(1);
			}
		});
		
		btnUsed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(2);
			}
		});
		
		btnOnSale.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(3);
			}
		});
	
		btnOrderSmall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setOrder();
			}
		});
		
		btnOrderBig.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setOrder();
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
		
		btnRegistration.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putInt("type", CarRegistrationPage.TYPE_REGISTRATION);
				mActivity.showPage(BCPConstants.PAGE_CAR_REGISTRATION, bundle);
			}
		});
		
		btnRegistration2.setOnClickListener(new OnClickListener() {

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
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;

		(titleBar.getMenuButton().getLayoutParams()).width = ResizeUtils.getSpecificLength(325);
		
		//bgForButtons.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.mainPage_bgForButtons).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(88);
		
		ResizeUtils.viewResizeForRelative(60, 60, btnGuide, null, null, new int[]{0, 14, 12, 0});
		ResizeUtils.viewResizeForRelative(160, 88, btnAuction, null, null, null);
		ResizeUtils.viewResizeForRelative(160, 88, btnMyBids, null, null, null);
		ResizeUtils.viewResizeForRelative(160, 88, btnUsed, null, null, null);
		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, 88, btnOnSale, null, null, null);
		
		ResizeUtils.viewResizeForRelative(453, 249, icon, null, null, new int[]{0, 100, 0, 80});
		ResizeUtils.viewResizeForRelative(544, 72, btnRegistration2, null, null, null);

		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, 100, buttonRelative, null, null, null);
		ResizeUtils.viewResizeForRelative(72, 72, btnOrderSmall, null, null, new int[]{8, 0, 0, 8});
		ResizeUtils.viewResizeForRelative(72, 72, btnSearch, null, null, new int[]{8, 0, 0, 8});
		ResizeUtils.viewResizeForRelative(464, 72, btnRegistration, null, null, new int[]{8, 0, 0, 8});
		ResizeUtils.viewResizeForRelative(544, 72, btnOrderBig, null, null, new int[]{0, 0, 8, 8});
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_main;
	}

	@Override
	public int getBackButtonResId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBackButtonWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBackButtonHeight() {
		// TODO Auto-generated method stub
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
			url = BCPAPIs.CAR_DEALER_LIST_URL + "?status=10";
			break;
			
		case 3:
			url = BCPAPIs.MY_CAR_URL + "?status=in_progress";
			break;
		}
		
		url += (url != null && url.contains("?") ? "&" : "?")
				+ "order=" + orderString;
		
		super.downloadInfo();
	}
	
	@Override
	public boolean parseJSON(JSONObject objJSON) {

		if(models.size() == 0 && menuIndex == 0 && banner != null) {
			models.add(banner);
		}
		
		int size = 0;
		int itemCode = 0;
		
		switch (menuIndex) {
		case 0:
			itemCode = BCPConstants.ITEM_CAR_BID;
			break;
		case 1:
			itemCode = BCPConstants.ITEM_CAR_BID_MY;
			break;
		case 2:
			itemCode = BCPConstants.ITEM_CAR_DEALER;
			break;
		case 3:
			itemCode = BCPConstants.ITEM_CAR_DEALER_IN_PROGRESS;
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
		
		if((menuIndex == 1 || menuIndex == 3) && models.size() == 0) {
			showIconAndButton();
		}
		
		if(banner != null && menuIndex == 0) {
			startIndex = 1;
		} else {
			startIndex = 0;
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
		
		if(models.size() == 0) {
			
			if(menuIndex == 0 && banner == null) {
				LogUtils.log("###MainPage.onResume.  checkCover");
				checkCover();
			} else {
				LogUtils.log("###MainPage.onResume.  downloadInfo");
				downloadInfo();
			}
		}
	}
	
//////////////////// Custom methods.
	
	public void setMenu(int index) {

		if(index == 0) {
			listView.setDividerHeight(0);
		} else {
			listView.setDividerHeight(ResizeUtils.getSpecificLength(16));
		}
		
		setMenuButtons(index);
		setIconAndButtons(index);
		setButtonRelative(index);
		
		int lastIndex = menuIndex;
		menuIndex = index;
		
		//처음 다운로드가 아니라면,
		if(lastIndex != -1) {
			refreshPage();
		}
	}
	
	public void setMenuButtons(int index) {
		
		switch(index) {
		
		case 0:
			btnAuction.setBackgroundResource(R.drawable.main_tab1_tab_a);
			btnMyBids.setBackgroundResource(R.drawable.main_tab2_tab_b);
			btnUsed.setBackgroundResource(R.drawable.main_tab3_tab_b);
			btnOnSale.setBackgroundResource(R.drawable.main_tab4_tab_b);
			break;
			
		case 1:
			btnAuction.setBackgroundResource(R.drawable.main_tab1_tab_b);
			btnMyBids.setBackgroundResource(R.drawable.main_tab2_tab_a);
			btnUsed.setBackgroundResource(R.drawable.main_tab3_tab_b);
			btnOnSale.setBackgroundResource(R.drawable.main_tab4_tab_b);
			break;
			
		case 2:
			btnAuction.setBackgroundResource(R.drawable.main_tab1_tab_b);
			btnMyBids.setBackgroundResource(R.drawable.main_tab2_tab_b);
			btnUsed.setBackgroundResource(R.drawable.main_tab3_tab_a);
			btnOnSale.setBackgroundResource(R.drawable.main_tab4_tab_b);
			break;
		case 3:
			btnAuction.setBackgroundResource(R.drawable.main_tab1_tab_b);
			btnMyBids.setBackgroundResource(R.drawable.main_tab2_tab_b);
			btnUsed.setBackgroundResource(R.drawable.main_tab3_tab_b);
			btnOnSale.setBackgroundResource(R.drawable.main_tab4_tab_a);
			break;
		}
	}
	
	public void setIconAndButtons(int index) {

		if(index == 1) {
			icon.setBackgroundResource(R.drawable.my_bid_toon);
		} else if(index == 3) {
			icon.setBackgroundResource(R.drawable.sell_toon);
		}
		
		icon.setVisibility(View.INVISIBLE);
		btnRegistration2.setVisibility(View.INVISIBLE);
	}
	
	public void setButtonRelative(int index) {
		
		switch(index) {
		
		case 0:
			buttonRelative.setVisibility(View.VISIBLE);
			ResizeUtils.setMargin(listView, new int[]{0, 0, 0, 88});
			btnOrderSmall.setVisibility(View.GONE);
			btnRegistration.setVisibility(View.GONE);
			btnOrderBig.setVisibility(View.VISIBLE);
			ResizeUtils.setMargin(swipeRefreshLayout, new int[]{0, 0, 0, 88});
			break;
			
		case 2:
			buttonRelative.setVisibility(View.VISIBLE);
			ResizeUtils.setMargin(listView, new int[]{0, 0, 0, 88});
			btnOrderSmall.setVisibility(View.VISIBLE);
			btnRegistration.setVisibility(View.VISIBLE);
			btnOrderBig.setVisibility(View.GONE);
			ResizeUtils.setMargin(swipeRefreshLayout, new int[]{0, 0, 0, 88});
			break;
		
		case 1:
		case 3:
			buttonRelative.setVisibility(View.INVISIBLE);
			ResizeUtils.setMargin(listView, new int[]{0, 0, 0, 0});
			ResizeUtils.setMargin(swipeRefreshLayout, new int[]{0, 0, 0, 0});
			break;
		}
	}
	
	public void showIconAndButton() {
		
		icon.setVisibility(View.VISIBLE);
		
		if(menuIndex == 3) {
			btnRegistration2.setVisibility(View.VISIBLE);
		}
	}

	public void setOrder() {

		String[] strings = new String[] {
				null,
				getString(R.string.order_2)
		};
		
		if(menuIndex == 0) {
			strings[0] = getString(R.string.order_1);
		} else if(menuIndex == 2) {
			strings[0] = getString(R.string.order_3);
		} else {
			return;
		}
		
		mActivity.showSelectDialog(title, strings, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				if(which == 0) {
					orderString = "date";
					
					if(menuIndex == 0) {
						btnOrderBig.setBackgroundResource(R.drawable.used_sort1_btn);
					} else {
						btnOrderSmall.setBackgroundResource(R.drawable.sort_toggle_c);
					}
				} else {
					orderString = "like";
					
					if(menuIndex == 0) {
						btnOrderBig.setBackgroundResource(R.drawable.used_sort2_btn);
					} else {
						btnOrderSmall.setBackgroundResource(R.drawable.sort_toggle_b);
					}
				}
				
				refreshPage();
			}
		});
	}

	public void checkNotification() {

		titleBar.setNoticeCount(0);
		
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

					if(objJSON.getInt("result") == 1) {
						titleBar.setNoticeCount(objJSON.getInt("unreadCount"));
					}
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
					
					if(objJSON.has("notice")) {
						Post openablePost = new Post(objJSON.getJSONObject("notice"));
						banner = new Banner();
						banner.setItemCode(BCPConstants.ITEM_BANNER);
						banner.setImageUrl(openablePost.getRep_img_url());
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

		car.setItemCode(BCPConstants.ITEM_CAR_BID);
		
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
			
			//좋아요 순이 아니라면,
			if(!getString(R.string.order_2).equals(orderString)) {
				reorderList(startIndex, car);
			}
			
		//관리자에 의해 보류된 경우.
		} else if(event.equals("auction_held")) {
			//해당 매물 삭제.
			deleteSelectedCar(car);
		}
	}
}
