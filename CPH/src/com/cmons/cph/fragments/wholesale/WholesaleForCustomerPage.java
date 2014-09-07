package com.cmons.cph.fragments.wholesale;

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
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.classes.CphAdapter;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.OrderSet;
import com.cmons.cph.models.Retail;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class WholesaleForCustomerPage extends CmonsFragmentForWholesale {

	private TextView tvShopName;
	private TextView tvOwnerName;
	private TextView tvPhone;
	private TextView tvCategory;
	private TextView tvAddress;
	private TextView tvTotalCount;
	private ListView listView;
	private Button btnCancel;

	private Retail retail;
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(models.size() == 0) {
			downloadInfo();
		}
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.wholesaleCustomerPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.wholesaleCustomerPage_ivBg);
		
		tvShopName = (TextView) mThisView.findViewById(R.id.wholesaleCustomerPage_tvShopName);
		tvOwnerName = (TextView) mThisView.findViewById(R.id.wholesaleCustomerPage_tvOwnerName);
		tvPhone = (TextView) mThisView.findViewById(R.id.wholesaleCustomerPage_tvPhone);
		tvCategory = (TextView) mThisView.findViewById(R.id.wholesaleCustomerPage_tvCategory);
		tvAddress = (TextView) mThisView.findViewById(R.id.wholesaleCustomerPage_tvAddress);
		tvTotalCount = (TextView) mThisView.findViewById(R.id.wholesaleCustomerPage_tvTotalCount);
		listView = (ListView) mThisView.findViewById(R.id.wholesaleCustomerPage_listView);
		btnCancel = (Button) mThisView.findViewById(R.id.wholesaleCustomerPage_btnCancel);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {
			retail = (Retail) getArguments().getSerializable("retail");
		}
		
		title = "거래처 정보";
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		
		adapter = new CphAdapter(mContext, getActivity().getLayoutInflater(), models);
		listView.setAdapter(adapter);

		tvShopName.setText(retail.getName());
		tvOwnerName.setText("대표성함 : " + retail.getOwner_name());
		tvPhone.setText("연락처 : " + retail.getPhone_number());
		
		if(StringUtils.isEmpty(retail.getMall_url())) {
			tvAddress.setText("매장주소 : " + retail.getAddress());
			tvCategory.setText("매장분류 : 오프라인매장");
		} else {
			tvAddress.setText("홈페이지 주소 : " + retail.getMall_url());
			tvCategory.setText("매장분류 : 온라인매장");
		}
	}

	@Override
	public void setListeners() {

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View convertView, int position,
					long id) {
			}
		});

		
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				cancelPartnerShip();
			}
		});
	}

	@Override
	public void setSizes() {
		
		RelativeLayout.LayoutParams rp = null;

		int textViewHeight = ResizeUtils.getSpecificLength(88);
		int p = ResizeUtils.getSpecificLength(40);
		
		//tvShopName.
		rp = (RelativeLayout.LayoutParams) tvShopName.getLayoutParams();
		rp.height = textViewHeight;
		FontUtils.setFontSize(tvShopName, 30);
		
		//tvOwnerName.
		rp = (RelativeLayout.LayoutParams) tvOwnerName.getLayoutParams();
		rp.height = textViewHeight;
		tvOwnerName.setPadding(p, 0, p, 0);
		FontUtils.setFontSize(tvOwnerName, 30);
		
		//tvPhone.
		rp = (RelativeLayout.LayoutParams) tvPhone.getLayoutParams();
		rp.height = textViewHeight;
		tvPhone.setPadding(p, 0, p, 0);
		FontUtils.setFontSize(tvPhone, 30);
		
		//tvCategory.
		rp = (RelativeLayout.LayoutParams) tvCategory.getLayoutParams();
		rp.height = textViewHeight;
		tvCategory.setPadding(p, 0, p, 0);
		FontUtils.setFontSize(tvCategory, 30);
		
		//tvAddress.
		rp = (RelativeLayout.LayoutParams) tvAddress.getLayoutParams();
		rp.height = textViewHeight;
		tvAddress.setPadding(p, 0, p, 0);
		FontUtils.setFontSize(tvAddress, 30);
		
		//tvTotalCount.
		rp = (RelativeLayout.LayoutParams) tvTotalCount.getLayoutParams();
		rp.height = textViewHeight;
		tvTotalCount.setPadding(p*2, 0, 0, 0);
		FontUtils.setFontSize(tvTotalCount, 30);
		
		//btnCancel.
		rp = (RelativeLayout.LayoutParams) btnCancel.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(180);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_wholesale_customer;
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
		
		try {
			JSONArray arJSON = null;
			arJSON = objJSON.getJSONArray("orders");

			int size = arJSON.length();
			for(int i=0; i<size; i++) {
				OrderSet orderSet = new OrderSet(arJSON.getJSONObject(i));
				retail.setItemCode(CphConstants.ITEM_ORDERSET_WHOLESALE2);
				models.add(orderSet);
			}
			
			tvTotalCount.setText("거래완료 " + objJSON.getInt("ordersCount") + "건");
			
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		return false;
	}

	@Override
	public void downloadInfo() {
		
		//http://cph.minsangk.com/wholesales/orders?retail_id=1
		url = CphConstants.BASE_API_URL + "wholesales/orders" +
				"?retail_id=" + retail.getId() +
				"&status=3";
		super.downloadInfo();
	}
	
////////////////////Custom methods.

	@Override
	public int getBgResourceId() {

		return R.drawable.business_bg;
	}

	public void cancelPartnerShip() {
		
		String url = CphConstants.BASE_API_URL + "wholesales/customers/delete"
				+ "?retail_id=" + retail.getId();
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WholesaleForCustomerListPage.onError."
						+ "\nurl : " + url);
				ToastUtils.showToast(R.string.failTobreakPartnerShip);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("WholesaleForCustomerListPage.onCompleted."
							+ "\nurl : " + url + "\nresult : " + objJSON);

					if (objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_breakPartnerShip);
						mActivity.closeTopPage();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					ToastUtils.showToast(R.string.failTobreakPartnerShip);
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					ToastUtils.showToast(R.string.failTobreakPartnerShip);
					LogUtils.trace(oom);
				}
			}
		});
	}
}
