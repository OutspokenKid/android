package com.cmons.cph.fragments.retail;

import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForRetail;
import com.cmons.cph.classes.CphAdapter;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Order;
import com.cmons.cph.models.OrderSet;
import com.cmons.cph.models.Wholesale;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class RetailForOrderPage extends CmonsFragmentForRetail {

	private OrderSet orderSet;
	
	private TextView tvOrder;
	private Button btnShop;
	private View order;
	private View standBy;
	private View deposit;
	private View completed;
	
	private Button btnConfirm;
	private TextView tvAccount;
	private ListView listView;
	
	private Wholesale wholesale;
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(wholesale == null) {
			downloadWholesale(orderSet.getWholesale_id());
		}
		
		if(models.size() == 0 && orderSet.getItems() != null) {
			int size = orderSet.getItems().length;
			for(int i=0; i<size; i++) {
				Order order = orderSet.getItems()[i];
				order.setItemCode(CphConstants.ITEM_ORDER_RETAIL);
				models.add(order);
			}
			adapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.retailOrderPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.retailOrderPage_ivBg);
		
		tvOrder = (TextView) mThisView.findViewById(R.id.retailOrderPage_tvOrder);
		btnShop = (Button) mThisView.findViewById(R.id.retailOrderPage_btnShop);
		
		order = mThisView.findViewById(R.id.retailOrderPage_order);
		standBy = mThisView.findViewById(R.id.retailOrderPage_standBy);
		deposit = mThisView.findViewById(R.id.retailOrderPage_deposit);
		completed = mThisView.findViewById(R.id.retailOrderPage_completed);
		
		btnConfirm = (Button) mThisView.findViewById(R.id.retailOrderPage_btnConfirm);
		tvAccount = (TextView) mThisView.findViewById(R.id.retailOrderPage_tvAccount);
		
		listView = (ListView) mThisView.findViewById(R.id.retailOrderPage_listView);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {
			orderSet = (OrderSet) getArguments().getSerializable("orderSet");
		}
		
		title = "주문내역";
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		titleBar.getHomeButton().setVisibility(View.VISIBLE);
		
		setMenu(orderSet.getStatus());
		
		String dateString = StringUtils.getDateString(
				"yyyy년 MM월 dd일 aa hh:mm", 
				orderSet.getItems()[0].getCreated_at() * 1000);
		SpannableStringBuilder sp1 = new SpannableStringBuilder(dateString + "\n");
		sp1.setSpan(new RelativeSizeSpan(0.8f), 0, sp1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvOrder.append(sp1); 
		
		SpannableStringBuilder sp2 = new SpannableStringBuilder(orderSet.getWholesale_name());
		sp2.setSpan(new RelativeSizeSpan(1.3f), 0, sp2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvOrder.append(sp2);
		
		SpannableStringBuilder sp3 = new SpannableStringBuilder(
				"(청평화몰 " + orderSet.getWholesale_location() + ")");
		tvOrder.append(sp3);
		
		tvAccount.setText("결제방식 : 무통장입금\n국민은행(홍길동)123-12-11234151");
		
		adapter = new CphAdapter(mContext, getActivity().getLayoutInflater(), models);
		listView.setAdapter(adapter);
		listView.setDivider(new ColorDrawable(Color.WHITE));
		listView.setDividerHeight(1);

		setMenu(orderSet.getStatus());
	}

	@Override
	public void setListeners() {
		
		btnShop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(wholesale != null) {
					Bundle bundle = new Bundle();
					bundle.putSerializable("wholesale", wholesale);
					mActivity.showPage(CphConstants.PAGE_RETAIL_SHOP, bundle);
				}
			}
		});
	
		btnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				switch(orderSet.getStatus()) {
				
				case Order.STATUS_CANCELED:
				case Order.STATUS_REQUESTED:
					return;
					
				//입금완료.
				case Order.STATUS_STANDBY:
				//거래완료.
				case Order.STATUS_DEPOSIT:
					requestStatus(orderSet.getStatus());
					break;
				//내역삭제.
				case Order.STATUS_COMPLETED:
					deleteOrder();
					break;
				}
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		
		//tvOrder.
		rp = (RelativeLayout.LayoutParams) tvOrder.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(120);
		int p = ResizeUtils.getSpecificLength(10);
		tvOrder.setPadding(p, p, p, p);
		
		//btnShop.
		rp = (RelativeLayout.LayoutParams) btnShop.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(142);
		rp.height = ResizeUtils.getSpecificLength(36);
		rp.rightMargin = ResizeUtils.getSpecificLength(10);
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);
		
		//order.
		rp = (RelativeLayout.LayoutParams) order.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(195);
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//standBy.
		rp = (RelativeLayout.LayoutParams) standBy.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(220);
		rp.height = ResizeUtils.getSpecificLength(92);
		rp.leftMargin = -ResizeUtils.getSpecificLength(40);
		
		//deposit.
		rp = (RelativeLayout.LayoutParams) deposit.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(222);
		rp.height = ResizeUtils.getSpecificLength(92);
		rp.leftMargin = -ResizeUtils.getSpecificLength(36);
		
		//completed.
		rp = (RelativeLayout.LayoutParams) completed.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(197);
		rp.height = ResizeUtils.getSpecificLength(92);
		rp.leftMargin = -ResizeUtils.getSpecificLength(36);

		//tvAccount.
		tvAccount.setPadding(p, p, p, p);
		
		//btnConfirm.
		rp = (RelativeLayout.LayoutParams) btnConfirm.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(180);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_retail_order;
	}

	@Override
	public boolean parseJSON(JSONObject objJSON) {

		return false;
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
	public int getBgResourceId() {

		return R.drawable.order_bg;
	}
	
////////////////////Custom classes.
	
	public void setMenu(int menuIndex) {

		switch(orderSet.getStatus()) {
		
		case Order.STATUS_CANCELED:
		case Order.STATUS_REQUESTED:
			order.setBackgroundResource(R.drawable.order_recommand_a);
			standBy.setBackgroundResource(R.drawable.order_wait_b);
			deposit.setBackgroundResource(R.drawable.order_done_b);
			completed.setBackgroundResource(R.drawable.order_complete_b);
			btnConfirm.setVisibility(View.INVISIBLE);
			break;
			
		case Order.STATUS_STANDBY:
			order.setBackgroundResource(R.drawable.order_recommand_b);
			standBy.setBackgroundResource(R.drawable.order_wait_a);
			deposit.setBackgroundResource(R.drawable.order_done_b);
			completed.setBackgroundResource(R.drawable.order_complete_b);
			btnConfirm.setBackgroundResource(R.drawable.retail_complete3_btn);
			btnConfirm.setVisibility(View.VISIBLE);
			break;
			
		case Order.STATUS_DEPOSIT:
			order.setBackgroundResource(R.drawable.order_recommand_b);
			standBy.setBackgroundResource(R.drawable.order_wait_b);
			deposit.setBackgroundResource(R.drawable.order_done_a);
			completed.setBackgroundResource(R.drawable.order_complete_b);
			btnConfirm.setBackgroundResource(R.drawable.retail_complete4_btn);
			btnConfirm.setVisibility(View.VISIBLE);
			break;
			
		case Order.STATUS_COMPLETED:
			order.setBackgroundResource(R.drawable.order_recommand_b);
			standBy.setBackgroundResource(R.drawable.order_wait_b);
			deposit.setBackgroundResource(R.drawable.order_done_b);
			completed.setBackgroundResource(R.drawable.order_complete_a);
			btnConfirm.setBackgroundResource(R.drawable.retail_delete4_btn);
			btnConfirm.setVisibility(View.VISIBLE);
			break;
		}
	}

	public void downloadWholesale(final int wholesale_id) {

		String url = CphConstants.BASE_API_URL + "wholesales/show" +
				"?wholesale_id=" + wholesale_id;
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("RetailForOrderPage.downloadWholesale.onError." + "\nurl : " + url);
				
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {

						downloadWholesale(wholesale_id);
					}
				}, 100);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("RetailForOrderPage.downloadWholesale.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.has("wholesale")) {
						wholesale = new Wholesale(objJSON.getJSONObject("wholesale"));
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}

	public void requestStatus(int status) {

		String order_ids = null;
		
		int size = orderSet.getItems().length;
		for(int i=0; i<size; i++) {
			
			if(i != 0) {
				order_ids += ",";
			}
			
			order_ids += orderSet.getItems()[i];
		}

		//http://cph.minsangk.com/retails/orders/delete?order_id=10001
		String url = CphConstants.BASE_API_URL + "retails/orders/set_status" +
				"?order_ids[]=" + order_ids +
				"&status=" + status;
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("RetailForOrderPage.requestStatus.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToChangeOrderStatus);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("RetailForOrderPage.requestStatus.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_changeOrderStatus);
						mActivity.closeTopPage();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					ToastUtils.showToast(R.string.failToChangeOrderStatus);
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					ToastUtils.showToast(R.string.failToChangeOrderStatus);
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void deleteOrder() {
		
	}
}
