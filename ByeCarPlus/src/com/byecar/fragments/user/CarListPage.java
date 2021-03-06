package com.byecar.fragments.user;

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
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.byecar.byecarplus.GuideActivity;
import com.byecar.byecarplus.MainActivity;
import com.byecar.byecarplus.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPAdapter;
import com.byecar.classes.BCPAuctionableFragment;
import com.byecar.classes.BCPConstants;
import com.byecar.models.Car;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;

public class CarListPage extends BCPAuctionableFragment {

	private SwipeRefreshLayout swipeRefreshLayout;
	private View buttonBg;
	private Button btnRegistration;
	private Button btnGuide;
	private Button btnSearch;
	
	private int firstVisibleItem;
	private int standardLength;
	private float diff;
	private int type;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.carListPage_titleBar);
		
		swipeRefreshLayout = (SwipeRefreshLayout) mThisView.findViewById(R.id.carListPage_swipe_container);
		listView = (ListView) mThisView.findViewById(R.id.carListPage_listView);
		buttonBg = mThisView.findViewById(R.id.carListPage_buttonBg);
		btnRegistration = (Button) mThisView.findViewById(R.id.carListPage_btnRegistration);
		btnGuide = (Button) mThisView.findViewById(R.id.carListPage_btnGuide);
		btnSearch = (Button) mThisView.findViewById(R.id.carListPage_btnSearch);
	}

	@Override
	public void setVariables() {
		
		if(getArguments() != null) {
			type = getArguments().getInt("type");
		}
	}

	@Override
	public void createPage() {

        swipeRefreshLayout.setColorSchemeColors(
        		getResources().getColor(R.color.titlebar_bg_orange),
        		getResources().getColor(R.color.titlebar_bg_brown), 
        		getResources().getColor(R.color.titlebar_bg_orange), 
        		getResources().getColor(R.color.titlebar_bg_brown));
        swipeRefreshLayout.setEnabled(true);

        adapter = new BCPAdapter(mContext, mActivity, mActivity.getLayoutInflater(), models);
		listView.setAdapter(adapter);
        
        //여백 설정.
        listView.setDivider(new ColorDrawable(Color.TRANSPARENT));
		
        switch(type) {
		
        case Car.TYPE_BID:
        	listView.setDividerHeight(ResizeUtils.getSpecificLength(16));
			break;
			
		case Car.TYPE_DEALER:
		case Car.TYPE_DIRECT:
			listView.setDividerHeight(ResizeUtils.getSpecificLength(30));
			break;
        }
		
        //타이틀바 설정.
        switch(type) {
		
		case Car.TYPE_BID:
			titleBar.setBgAlpha(0);
			break;
			
		case Car.TYPE_DEALER:
		case Car.TYPE_DIRECT:
			break;
        }
		
        //상단 버튼 설정.
        switch(type) {
		
		case Car.TYPE_BID:
			btnSearch.setVisibility(View.INVISIBLE);
			break;
			
		case Car.TYPE_DEALER:
		case Car.TYPE_DIRECT:
			btnSearch.setVisibility(View.VISIBLE);
			break;
		}
        
		//하단 버튼 설정.
		switch(type) {
		
		case Car.TYPE_BID:
			btnRegistration.setBackgroundResource(R.drawable.auction_request_btn);
			btnRegistration.setVisibility(View.VISIBLE);
			buttonBg.setVisibility(View.VISIBLE);
			break;
			
		case Car.TYPE_DEALER:
			btnRegistration.setVisibility(View.INVISIBLE);
			buttonBg.setVisibility(View.INVISIBLE);
			break;
			
		case Car.TYPE_DIRECT:
			btnRegistration.setBackgroundResource(R.drawable.direct_request_btn);
			btnRegistration.setVisibility(View.VISIBLE);
			buttonBg.setVisibility(View.VISIBLE);
			break;
		}

		//상단 버튼 설정.
		switch(type) {
		
		case Car.TYPE_BID:
		case Car.TYPE_DEALER:
			btnSearch.setBackgroundResource(R.drawable.search_btn);
			break;
			
		case Car.TYPE_DIRECT:
			btnSearch.setBackgroundResource(R.drawable.search_btn2);
			break;
		}
		
		if(mActivity != null) {
			
			switch(type) {
			
			case Car.TYPE_BID:
				mActivity.tracking(BCPConstants.TRACKING_LIST_CAR_AUCTION);	
				break;
				
			case Car.TYPE_DEALER:
				mActivity.tracking(BCPConstants.TRACKING_LIST_CAR_DEALER);
				break;
				
			case Car.TYPE_DIRECT:
				mActivity.tracking(BCPConstants.TRACKING_LIST_CAR_DIRECT);
				break;
			}
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
		
		btnRegistration.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(StringUtils.isEmpty(MainActivity.user.getPhone_number())) {
					ToastUtils.showToast("휴대폰 번호 인증이 필요합니다.");
					mActivity.showPage(BCPConstants.PAGE_CERTIFY_PHONE_NUMBER, null);
				} else {
					if(type == Car.TYPE_BID) {
						DownloadUtils.downloadJSONString(BCPAPIs.CAR_BID_CHECK_SAVE_URL,
								new OnJSONDownloadListener() {

									@Override
									public void onError(String url) {

										LogUtils.log("CarListPage.onError."
												+ "\nurl : " + url);

									}

									@Override
									public void onCompleted(String url,
											JSONObject objJSON) {

										try {
											LogUtils.log("CarListPage.onCompleted."
													+ "\nurl : "
													+ url
													+ "\nresult : "
													+ objJSON);

											//차량 등록 페이지 중복 호출 방지.
											if(mActivity.getTopFragment() instanceof CarListPage) {
												
												if(objJSON.getInt("result") == 1) {
													Bundle bundle = new Bundle();
													bundle.putInt("carType", Car.TYPE_BID);
													mActivity.showPage(BCPConstants.PAGE_CAR_REGISTRATION, bundle);
												} else {
													ToastUtils.showToast(objJSON.getString("message"));
												}
											}
										} catch (Exception e) {
											LogUtils.trace(e);
										} catch (OutOfMemoryError oom) {
											LogUtils.trace(oom);
										}
									}
								});
					} else {
						DownloadUtils.downloadJSONString(BCPAPIs.CAR_DIRECT_CHECK_SAVE_URL,
								new OnJSONDownloadListener() {

									@Override
									public void onError(String url) {

										LogUtils.log("CarListPage.onError."
												+ "\nurl : " + url);

									}

									@Override
									public void onCompleted(String url,
											JSONObject objJSON) {

										try {
											LogUtils.log("CarListPage.onCompleted."
													+ "\nurl : "
													+ url
													+ "\nresult : "
													+ objJSON);

											//차량 등록 페이지 중복 호출 방지.
											if(mActivity.getTopFragment() instanceof CarListPage) {
												
												if(objJSON.getInt("result") == 1) {
													Bundle bundle = new Bundle();
													bundle.putInt("carType", Car.TYPE_DIRECT);
													mActivity.showPage(BCPConstants.PAGE_CAR_REGISTRATION, bundle);
												} else {
													ToastUtils.showToast(objJSON.getString("message"));
												}
											}
										} catch (Exception e) {
											LogUtils.trace(e);
										} catch (OutOfMemoryError oom) {
											LogUtils.trace(oom);
										}
									}
								});
					}
				}
			}
		});
		
		btnGuide.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				/*
				1. 경매이용안내->옥션리스트

				2. 구매이용안내->중고마켓리스트
				
				4. 검증직거래이용안내 -> 검증직거래리스트
				
				5. 일반직거래이용안내 -> 일반직거래 리스트
				*/
				Intent intent = new Intent(mActivity, GuideActivity.class);
				
				switch(type) {
				
				case Car.TYPE_BID:
					intent.putExtra("type", GuideActivity.TYPE_USER_AUCTION);
					break;
					
				case Car.TYPE_DEALER:
					intent.putExtra("type", GuideActivity.TYPE_USER_DEALER);
					break;
					
				case Car.TYPE_DIRECT:
					intent.putExtra("type", GuideActivity.TYPE_USER_DIRECT);
					break;
				}
				
				mActivity.startActivity(intent);
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
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		
		//swipe_container.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.carListPage_swipe_container).getLayoutParams();
		
		//Set topMargin.
		if(type != Car.TYPE_BID) {
			rp.topMargin = ResizeUtils.getSpecificLength(88);
		}
		
		//Set bottomMargink.
		if(type != Car.TYPE_DEALER) {
			rp.bottomMargin = ResizeUtils.getSpecificLength(115);
		}
		
		//buttonBg.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.carListPage_buttonBg).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(123);
		
		//btnRegistration.
		rp = (RelativeLayout.LayoutParams) btnRegistration.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(611);
		rp.height = ResizeUtils.getSpecificLength(84);
		rp.bottomMargin = ResizeUtils.getSpecificLength(15);
		
		//btnGuide.
		rp = (RelativeLayout.LayoutParams) btnGuide.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(60);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.topMargin = ResizeUtils.getSpecificLength(16);
		rp.rightMargin = ResizeUtils.getSpecificLength(14);
		
		//btnSearch.
		rp = (RelativeLayout.LayoutParams) btnSearch.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(60);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.rightMargin = ResizeUtils.getSpecificLength(8);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_car_list;
	}

	@Override
	public int getPageTitleTextResId() {

		switch(type) {
		
		case Car.TYPE_BID:
			return R.string.pageTitle_carList_auction;
			
		case Car.TYPE_DEALER:
			return R.string.pageTitle_carList_dealer;
			
		case Car.TYPE_DIRECT:
			return R.string.pageTitle_carList_direct;
		}
		
		return 0;
	}

	@Override
	public void downloadInfo() {
		
		switch(type) {
		
		case Car.TYPE_BID:
			url = BCPAPIs.CAR_BID_LIST_URL;
			break;
			
		case Car.TYPE_DEALER:
			url = BCPAPIs.CAR_DEALER_LIST_URL + "?status=10";
			break;
			
		case Car.TYPE_DIRECT:
			url = BCPAPIs.CAR_DIRECT_NORMAL_LIST_URL;
			break;
		}
		
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
		case Car.TYPE_DIRECT:
			itemCode = BCPConstants.ITEM_CAR_DIRECT;
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
		
		if(mActivity.bundle != null) {
			
			if(mActivity.bundle.containsKey("phone_auth_key")
					&& mActivity.bundle.containsKey("requestedPhoneNumber")) {
				updatePhoneNumber(mActivity.bundle.getString("phone_auth_key"), 
						mActivity.bundle.getString("requestedPhoneNumber"));
			}
			
			mActivity.bundle = null;
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

		if(type == Car.TYPE_BID) {
			
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
	@Override
	public void bidStatusChanged(String event, Car car) {

		if(event == null) {
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
			reorderList(startIndex, car);
			
		//관리자에 의해 보류된 경우.
		} else if(event.equals("auction_held")) {
			//해당 매물 삭제.
			deleteSelectedCar(car);
		}
	}

	public void updatePhoneNumber(final String phone_auth_key, final String requestedPhoneNumber) {

		if(phone_auth_key == null || requestedPhoneNumber == null) {
			ToastUtils.showToast(R.string.complete_auth);
			mActivity.closeTopPage();
			return;
		}
		
		String url = BCPAPIs.PHONE_UPDATE_URL
				+ "?phone_auth_key=" + phone_auth_key;
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("CarListPage.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToSendAuthRequest);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("CarListPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);
					
					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_auth);
						MainActivity.user.setPhone_number(requestedPhoneNumber);
						mActivity.closeTopPage();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
					
				} catch (Exception e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToSendAuthRequest);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					ToastUtils.showToast(R.string.failToSendAuthRequest);
				}
			}
		}, mActivity.getLoadingView());
	}
}
