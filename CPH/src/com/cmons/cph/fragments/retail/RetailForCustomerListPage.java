package com.cmons.cph.fragments.retail;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.ShopActivity;
import com.cmons.cph.classes.CmonsFragmentForRetail;
import com.cmons.cph.classes.CphAdapter;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Customer;
import com.cmons.cph.models.OrderSet;
import com.cmons.cph.models.Wholesale;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class RetailForCustomerListPage extends CmonsFragmentForRetail {

	private Button btnOrder;
	private Button btnPartner;
	private TextView tvCount;
	
	private ListView listView;
	
	private int menuIndex;
	private int totalCount;
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(models.size() == 0) {
			setMenu(menuIndex);
		}
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.retailCustomerListPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.retailCustomerListPage_ivBg);
		
		btnOrder = (Button) mThisView.findViewById(R.id.retailCustomerListPage_btnOrder);
		btnPartner = (Button) mThisView.findViewById(R.id.retailCustomerListPage_btnPartner);
		
		tvCount = (TextView) mThisView.findViewById(R.id.retailCustomerListPage_tvCount);
		
		listView = (ListView) mThisView.findViewById(R.id.retailCustomerListPage_listView);
	}

	@Override
	public void setVariables() {

		title = "거래처관리";
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		
		adapter = new CphAdapter(mContext, getActivity().getLayoutInflater(), models);
		listView.setAdapter(adapter);
		listView.setDivider(new ColorDrawable(Color.WHITE));
		listView.setDividerHeight(1);
		
		switch (menuIndex) {
		
		case 0:
			btnOrder.setBackgroundResource(R.drawable.retail_statement_btn_a);
			btnPartner.setBackgroundResource(R.drawable.retail_customer2_btn_b);
			break;
			
		case 1:
			btnOrder.setBackgroundResource(R.drawable.retail_statement_btn_b);
			btnPartner.setBackgroundResource(R.drawable.retail_customer2_btn_a);
			break;
		}
		
		if(menuIndex == 0) {
			tvCount.setText("총 주문 " + totalCount + "건");
		} else {
			tvCount.setText("거래처 " + totalCount + " 승인중 거래처 ");
		}
	}

	@Override
	public void setListeners() {

		btnOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(0);
			}
		});
		
		btnPartner.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(1);
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View convertView, int position,
					long id) {
				
				if(menuIndex == 0) {
					Bundle bundle = new Bundle();
					bundle.putSerializable("orderSet", (OrderSet) models.get(position));
					ShopActivity.getInstance().showPage(CphConstants.PAGE_RETAIL_ORDER, bundle);
				}
			}
		});
	}

	@Override
	public void setSizes() {
		
		RelativeLayout.LayoutParams rp = null;
		
		//btnOrder.
		rp = (RelativeLayout.LayoutParams) btnOrder.getLayoutParams();
		rp.width = ResizeUtils.getScreenWidth()/2;
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//btnPartner.
		rp = (RelativeLayout.LayoutParams) btnPartner.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//tvCount.
		rp = (RelativeLayout.LayoutParams) tvCount.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		int p = ResizeUtils.getSpecificLength(10);
		tvCount.setPadding(p, p/2, p, p);
		FontUtils.setFontSize(tvCount, 30);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_retail_customerlist;
	}

	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onBackPressed() {

		return false;
	}

	@Override
	public boolean parseJSON(JSONObject objJSON) {

		JSONArray arJSON = null;
		int size = 0;
		try {
			if(menuIndex == 0) {
				arJSON = objJSON.getJSONArray("orders"); 
				size = arJSON.length();
				
				for(int i=0; i<size; i++) {
					OrderSet orderSet = new OrderSet(arJSON.getJSONObject(i));
					orderSet.setItemCode(CphConstants.ITEM_ORDERSET_RETAIL);
					models.add(orderSet);
				}

				totalCount = size;
				tvCount.setText("총 주문 " + totalCount + "건");
				
			} else {
				arJSON = objJSON.getJSONArray("customers");
				size = arJSON.length();
				
				for(int i=0; i<10; i++) {
					Customer customer = new Customer();
					customer.setItemCode(CphConstants.ITEM_CUSTOMER_RETAIL);
					models.add(customer);
				}

				totalCount = size;
				tvCount.setText("거래처 " + totalCount + " 승인중 거래처 "); 
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		return false;
	}

	@Override
	public void downloadInfo() {
		
		if(menuIndex == 0) {
			url = CphConstants.BASE_API_URL + "retails/orders";
		}
		
		super.downloadInfo();
	}
	
	@Override
	public int getBgResourceId() {

		return R.drawable.order_bg;
	}
	
////////////////////Custom methods.
	
	public void setMenu(int menuIndex) {

		this.menuIndex = menuIndex;
		
		if(getArguments() != null) {
			getArguments().putInt("menuIndex", menuIndex);
		}
		
		switch (menuIndex) {
		
		case 0:
			btnOrder.setBackgroundResource(R.drawable.retail_statement_btn_a);
			btnPartner.setBackgroundResource(R.drawable.retail_customer2_btn_b);
			refreshPage();
			break;
			
		case 1:
			btnOrder.setBackgroundResource(R.drawable.retail_statement_btn_b);
			btnPartner.setBackgroundResource(R.drawable.retail_customer2_btn_a);
			refreshCustomer();
			break;
		}
	}
	
	public void refreshCustomer() {

		if(isRefreshing) {
			return;
		}
		
		try {
			isRefreshing = true;
			isDownloading = false;
			isLastList = false;
			pageIndex = 0;
			models.clear();
			adapter.notifyDataSetChanged();
			
			downloadCustomer1();
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void downloadCustomer1() {
		
		url = CphConstants.BASE_API_URL + "retails/customers/requested" +
				"?num=0";
		
		if(isDownloading || isLastList) {
			return;
		}
		
		if(!isRefreshing) {
			showLoadingView();
		}
		
		isDownloading = true;
		isLastList = true;
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("CmonsFragment.onError." + "\nurl : " + url);
				downloadCustomer2(0);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				int size = 0;
				
				try {
					LogUtils.log("CmonsFragment.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);
					
					JSONArray arJSON = objJSON.getJSONArray("requestedCustomers");
					
					size = arJSON.length();
					for(int i=0; i<size; i++) {
						Wholesale wholesale = new Wholesale(arJSON.getJSONObject(i));
						wholesale.setItemCode(CphConstants.ITEM_CUSTOMER_RETAIL);
						wholesale.setStandingBy(true);
						models.add(wholesale);
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				} finally {
					downloadCustomer2(size);
				}
			}
		});
	}

	public void downloadCustomer2(final int standingBySize) {

		url = CphConstants.BASE_API_URL + "retails/customers" +
				"?num=0";
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("CmonsFragment.onError." + "\nurl : " + url);
				hideLoadingView();
				isRefreshing = false;
				isDownloading = false;
				adapter.notifyDataSetChanged();
				
				tvCount.setText("거래처 0  승인중거래처 " + standingBySize);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				int size = 0;
				
				try {
					LogUtils.log("CmonsFragment.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);
					
					JSONArray arJSON = objJSON.getJSONArray("customers");
					
					size = arJSON.length();
					for(int i=0; i<size; i++) {
						Wholesale wholesale = new Wholesale(arJSON.getJSONObject(i));
						wholesale.setItemCode(CphConstants.ITEM_CUSTOMER_RETAIL);
						wholesale.setStandingBy(false);
						models.add(wholesale);
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				} finally {
					tvCount.setText("거래처 " + size + "  승인중거래처 " + standingBySize);
					hideLoadingView();
					isRefreshing = false;
					isDownloading = false;
					adapter.notifyDataSetChanged();
				}
			}
		});
	}
}
