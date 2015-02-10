package com.byecar.fragments.dealer;

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
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.byecar.byecarplusfordealer.MainActivity;
import com.byecar.byecarplusfordealer.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPAdapter;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPFragment;
import com.byecar.fragments.CarRegistrationPage;
import com.byecar.fragments.OpenablePostListPage;
import com.byecar.models.Banner;
import com.byecar.models.Car;
import com.byecar.models.OpenablePost;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class MainPage extends BCPFragment {

	private SwipeRefreshLayout swipeRefreshLayout;
	
	private Button btnGuide;
	private Button btnAuction;
	private Button btnMyBids;
	private Button btnUsed;
	private Button btnOnSale;
	
	private ListView listView;
	
	private View icon;
	private Button btnRegistration2;

	private RelativeLayout buttonRelative;
	private Button btnOrderSmall;
	private Button btnSearch;
	private Button btnRegistration;
	private Button btnOrderBig;
	
	private int menuIndex;
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
		
		adapter = new BCPAdapter(mContext, mActivity, mActivity.getLayoutInflater(), models);
		listView.setAdapter(adapter);
		listView.setDivider(new ColorDrawable(Color.TRANSPARENT));
		listView.setDividerHeight(ResizeUtils.getSpecificLength(16));
		
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
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {

				if(menuIndex == 0 && position == 0) {
					Bundle bundle = new Bundle();
					bundle.putInt("type", OpenablePostListPage.TYPE_NOTICE);
					mActivity.showPage(BCPConstants.PAGE_OPENABLE_POST_LIST, bundle);
				}
				
				try {
					int type = 0;

					if(menuIndex < 2) {
						type = Car.TYPE_BID;
					} else {
						type = Car.TYPE_DEALER;
					}
					
					Bundle bundle = new Bundle();
					bundle.putSerializable("car", (Car) models.get(position));
					bundle.putInt("type", type);
					mActivity.showPage(BCPConstants.PAGE_CAR_DETAIL, bundle);
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

				mActivity.showPage(BCPConstants.PAGE_SEARCH_CAR, null);
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
		ResizeUtils.viewResizeForRelative(624, 72, btnOrderBig, null, null, new int[]{8, 0, 0, 8});
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
			url = BCPAPIs.CAR_BID_LIST_URL;
			break;
			
		case 1:
			url = BCPAPIs.MY_BIDS_URL + "?status=10";
			break;
			
		case 2:
			url = BCPAPIs.CAR_DEALER_LIST_URL;
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
				checkCover();
			} else {
				downloadInfo();
			}
		}
	}
	
//////////////////// Custom methods.
	
	public void setMenu(int index) {

		setMenuButtons(index);
		setIconAndButtons(index);
		setButtonRelative(index);
		
		menuIndex = index;
		refreshPage();
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
			btnOrderSmall.setVisibility(View.INVISIBLE);
			btnSearch.setVisibility(View.INVISIBLE);
			btnRegistration.setVisibility(View.INVISIBLE);
			btnOrderBig.setVisibility(View.VISIBLE);
			ResizeUtils.setMargin(swipeRefreshLayout, new int[]{0, 0, 0, 88});
			break;
			
		case 2:
			buttonRelative.setVisibility(View.VISIBLE);
			ResizeUtils.setMargin(listView, new int[]{0, 0, 0, 88});
			btnOrderSmall.setVisibility(View.VISIBLE);
			btnSearch.setVisibility(View.VISIBLE);
			btnRegistration.setVisibility(View.VISIBLE);
			btnOrderBig.setVisibility(View.INVISIBLE);
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
		
		titleBar.setNoticeCount(5);
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
					LogUtils.log("MainPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.has("notice")) {
						OpenablePost openablePost = new OpenablePost(objJSON.getJSONObject("notice"));
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
}
