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

public class AuctionListPage extends BCPFragment {

	private SwipeRefreshLayout swipeRefreshLayout;
	private ListView listView;
	private Button btnResistration;
	private Button btnGuide;
	
	private AlphaAnimation aaIn, aaOut;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.auctionListPage_titleBar);
		
		swipeRefreshLayout = (SwipeRefreshLayout) mThisView.findViewById(R.id.auctionListPage_swipe_container);
		listView = (ListView) mThisView.findViewById(R.id.auctionListPage_listView);
		btnResistration = (Button) mThisView.findViewById(R.id.auctionListPage_btnRegistration);
		btnGuide = (Button) mThisView.findViewById(R.id.auctionListPage_btnGuide);
	}

	@Override
	public void setVariables() {

		aaIn = new AlphaAnimation(0, 1);
		aaIn.setDuration(300);
		
		aaOut = new AlphaAnimation(1, 0);
		aaOut.setDuration(300);
	}

	@Override
	public void createPage() {

		titleBar.setBgColor(Color.WHITE);
		titleBar.setBgAlpha(0);
		
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
				
				if(scrollState == 0 && lastStatus != 0) {
					btnResistration.setVisibility(View.VISIBLE);
					btnResistration.startAnimation(aaIn);
					
				} else if(scrollState != 0 && lastStatus == 0) {
					btnResistration.setVisibility(View.INVISIBLE);
					btnResistration.startAnimation(aaOut);
				}
				
				lastStatus = scrollState;
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				if(visibleItemCount < totalItemCount && firstVisibleItem + visibleItemCount == totalItemCount) {
					downloadInfo();
				}

				try {
					if(adapter.getFirstView() != null && firstVisibleItem == 0) {
						int offset = -adapter.getFirstView().getTop();
						
						if(offset < 500) {
							titleBar.setBgAlpha(0.002f * offset);
							
						} else if(offset < 700){
							titleBar.setBgAlpha(1);
						} else {
							//Do nothing.
						}
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				try {
					Bundle bundle = new Bundle();
					bundle.putInt("id", ((Car) models.get(position)).getId());
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
		
		btnResistration.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				ToastUtils.showToast("등록");
			}
		});
		
		btnGuide.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				ToastUtils.showToast("가이드");
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		
		//btnResistration.
		rp = (RelativeLayout.LayoutParams) btnResistration.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(120);
		rp.height = ResizeUtils.getSpecificLength(120);
		
		//btnGuide.
		rp = (RelativeLayout.LayoutParams) btnGuide.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(60);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.topMargin = ResizeUtils.getSpecificLength(14);
		rp.rightMargin = ResizeUtils.getSpecificLength(10);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_auction_list;
	}

	@Override
	public int getBackButtonResId() {

		return R.drawable.auction_back_btn;
	}

	@Override
	public int getBackButtonWidth() {

		return 251;
	}

	@Override
	public int getBackButtonHeight() {

		return 60;
	}

	@Override
	public void downloadInfo() {
		
		url = BCPAPIs.BIDS_LIST_URL;
		
		super.downloadInfo();
	}
	
	@Override
	public boolean parseJSON(JSONObject objJSON) {

		int size = 0;
		
		try {
			JSONArray arJSON = objJSON.getJSONArray("onsalecars");
			
			size = arJSON.length();
			for(int i=0; i<size; i++) {
				Car car = new Car(arJSON.getJSONObject(i));
				car.setItemCode(BCPConstants.ITEM_AUCTION);
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
}
