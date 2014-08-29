package com.cmons.cph.fragments.retail;

import org.json.JSONArray;
import org.json.JSONObject;

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
import com.cmons.cph.classes.CmonsFragmentForRetail;
import com.cmons.cph.classes.CphAdapter;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Customer;
import com.cmons.cph.models.OrderSet;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class RetailForCustomerListPage extends CmonsFragmentForRetail {

	private Button btnOrder;
	private Button btnPartner;
	private TextView tvCount;
	
	private ListView listView;
	
	private int menuIndex;
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(models.size() == 0) {
			downloadInfo();
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
				
//				if(menuIndex == 0) {
//					showPopup();
//				} else {
//					//Show CustomerPage.
//				}
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
					orderSet.setItemCode(CphConstants.ITEM_ORDERSET);
					models.add(orderSet);
				}
				
				tvCount.setText("총 주문 " + size + "건");
				
			} else {
				arJSON = objJSON.getJSONArray("customers");
				size = arJSON.length();
				
				for(int i=0; i<10; i++) {
					Customer customer = new Customer();
					customer.setItemCode(CphConstants.ITEM_CUSTOMER);
					models.add(customer);
				}

				tvCount.setText("거래처 " + size + " 승인중 거래처 "); 
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
		} else {
			url = CphConstants.BASE_API_URL + "retails/customers";
		}
		
		super.downloadInfo();
	}
	
	@Override
	public int getBgResourceId() {

		return R.drawable.order_bg;
	}
	
////////////////////Custom methods.
	
	public void setMenu(int menuIndex) {
		
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
		
		this.menuIndex = menuIndex;
		
		refreshPage();
	}
}
