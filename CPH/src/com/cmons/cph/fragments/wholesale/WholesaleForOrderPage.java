package com.cmons.cph.fragments.wholesale;

import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.classes.CphAdapter;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Account;
import com.cmons.cph.models.Order;
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

public class WholesaleForOrderPage extends CmonsFragmentForWholesale {

	private OrderSet orderSet;
	
	private TextView tvOrder;
	private View type;
	private Button btnOrder;
	private Button btnStandBy;
	private Button btnDeposit;
	private Button btnCompleted;
	private TextView tvSoldOut;
	
	private Button btnConfirm;
	private TextView tvAccount;
	private TextView tvTotalPrice;
	private ListView listView;
	
	private long totalPrice = -1;
	
	@Override
	public void onResume() {
		super.onResume();
		setTypeImage();
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.wholesaleOrderPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.wholesaleOrderPage_ivBg);
		
		tvOrder = (TextView) mThisView.findViewById(R.id.wholesaleOrderPage_tvOrder);
		type = mThisView.findViewById(R.id.wholesaleOrderPage_type);
		
		btnOrder = (Button) mThisView.findViewById(R.id.wholesaleOrderPage_btnOrder);
		btnStandBy = (Button) mThisView.findViewById(R.id.wholesaleOrderPage_btnStandBy);
		btnDeposit = (Button) mThisView.findViewById(R.id.wholesaleOrderPage_btnDeposit);
		btnCompleted = (Button) mThisView.findViewById(R.id.wholesaleOrderPage_btnCompleted);
		
		tvSoldOut = (TextView) mThisView.findViewById(R.id.wholesaleOrderPage_tvSoldOut);
		btnConfirm = (Button) mThisView.findViewById(R.id.wholesaleOrderPage_btnConfirm);
		tvAccount = (TextView) mThisView.findViewById(R.id.wholesaleOrderPage_tvAccount);
		tvTotalPrice = (TextView) mThisView.findViewById(R.id.wholesaleOrderPage_tvTotalPrice);
		
		listView = (ListView) mThisView.findViewById(R.id.wholesaleOrderPage_listView);
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

		switch(orderSet.getStatus()) {
		
		case 0:
			btnOrder.setBackgroundResource(R.drawable.order_recommand_a);
			btnStandBy.setBackgroundResource(R.drawable.order_wait_b);
			btnDeposit.setBackgroundResource(R.drawable.order_done_b);
			btnCompleted.setBackgroundResource(R.drawable.order_complete_b);
			tvSoldOut.setVisibility(View.VISIBLE);
			btnConfirm.setVisibility(View.VISIBLE);
			btnConfirm.setBackgroundResource(R.drawable.order_approve_btn);
			break;
			
		case 1:
			btnOrder.setBackgroundResource(R.drawable.order_recommand_b);
			btnStandBy.setBackgroundResource(R.drawable.order_wait_a);
			btnDeposit.setBackgroundResource(R.drawable.order_done_b);
			btnCompleted.setBackgroundResource(R.drawable.order_complete_b);
			tvSoldOut.setVisibility(View.INVISIBLE);
			btnConfirm.setVisibility(View.INVISIBLE);
			break;
			
		case 2:
			btnOrder.setBackgroundResource(R.drawable.order_recommand_b);
			btnStandBy.setBackgroundResource(R.drawable.order_wait_b);
			btnDeposit.setBackgroundResource(R.drawable.order_done_a);
			btnCompleted.setBackgroundResource(R.drawable.order_complete_b);
			tvSoldOut.setVisibility(View.INVISIBLE);
			btnConfirm.setVisibility(View.VISIBLE);
			btnConfirm.setBackgroundResource(R.drawable.retail_complete4_btn);
			break;
			
		case 3:
			btnOrder.setBackgroundResource(R.drawable.order_recommand_b);
			btnStandBy.setBackgroundResource(R.drawable.order_wait_b);
			btnDeposit.setBackgroundResource(R.drawable.order_done_b);
			btnCompleted.setBackgroundResource(R.drawable.order_complete_a);
			tvSoldOut.setVisibility(View.INVISIBLE);
			btnConfirm.setVisibility(View.VISIBLE);
			btnConfirm.setBackgroundResource(R.drawable.retail_delete4_btn);
			break;
		}
		
		String dateString = StringUtils.getDateString("yyyy.MM.dd aa hh:mm", 
				orderSet.getItems()[0].getCreated_at() * 1000);
		FontUtils.addSpan(tvOrder, dateString, 0, 0.8f);
		
		FontUtils.addSpan(tvOrder, "\n" + orderSet.getRetail_name(), 0, 1.5f);
		FontUtils.addSpan(tvOrder, " (" + orderSet.getRetail_phone_number() + ")", 0, 1);
		
		tvSoldOut.setText(R.string.disableSoldout);
		
		String accountString = "결제방식 : ";

		if(orderSet.getPayment_type() == 1) {
			accountString += "무통장입금";

			if(getWholesale().getAccounts() != null) {

				for(Account account : getWholesale().getAccounts()) {
					
					if(account.getId() == orderSet.getPayment_account_id()) {
						accountString += "\n" + account.getBank() + 
								" " + account.getNumber() + 
								"(" + account.getDepositor() + ")";
					}
				}
			}
		} else {
			accountString += "사입자대납" +
					"\n" + orderSet.getPayment_purchaser_info();
		}
		
		accountString += "\n총 금액";
		
		tvAccount.setText(accountString);
		
		adapter = new CphAdapter(mContext, getActivity().getLayoutInflater(), models);
		listView.setAdapter(adapter);
		listView.setDivider(new ColorDrawable(Color.WHITE));
		listView.setDividerHeight(1);

		if(models.size() == 0) {
			for(Order order : orderSet.getItems()) {
				order.setItemCode(CphConstants.ITEM_ORDER_WHOLESALE);
				
				if(order.getStatus() == orderSet.getStatus()) {
					order.setChecked(true);
				} else {
					order.setChecked(false);
				}
				
				order.setParentStatus(orderSet.getStatus());
				models.add(order);
			}
		}
		
		totalPrice = 0;
		int size = orderSet.getItems().length;
		for(int i=0; i<size; i++) {
			
			if(orderSet.getItems()[i].isChecked()) {
				totalPrice += orderSet.getItems()[i].getProduct_price()
						* orderSet.getItems()[i].getAmount();
			}
		}
		
		tvTotalPrice.setText(StringUtils.getFormattedNumber(totalPrice) + "원");
	}

	@Override
	public void setListeners() {
	
		btnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(totalPrice == 0) {
					ToastUtils.showToast("선택된 상품이 없습니다");
					return;
				}
				
				if(orderSet.getStatus() != 3) {
					changeOrderStatus();
				} else {
					deleteOrder();
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
		
		//type.
		rp = (RelativeLayout.LayoutParams) type.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(146);
		rp.height = ResizeUtils.getSpecificLength(30);
		rp.rightMargin = ResizeUtils.getSpecificLength(10);
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);
		
		//btnOrder.
		rp = (RelativeLayout.LayoutParams) btnOrder.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(195);
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//btnStandBy.
		rp = (RelativeLayout.LayoutParams) btnStandBy.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(220);
		rp.height = ResizeUtils.getSpecificLength(92);
		rp.leftMargin = -ResizeUtils.getSpecificLength(40);
		
		//btnDeposit.
		rp = (RelativeLayout.LayoutParams) btnDeposit.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(222);
		rp.height = ResizeUtils.getSpecificLength(92);
		rp.leftMargin = -ResizeUtils.getSpecificLength(36);
		
		//btnCompleted.
		rp = (RelativeLayout.LayoutParams) btnCompleted.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(197);
		rp.height = ResizeUtils.getSpecificLength(92);
		rp.leftMargin = -ResizeUtils.getSpecificLength(36);
		
		//tvSoldOut.
		rp = (RelativeLayout.LayoutParams) tvSoldOut.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);

		//tvAccount.
		tvAccount.setPadding(p, p, p, p);
		
		//tvTotalPrice.
		tvTotalPrice.setPadding(p, p, p, p);
		
		//btnConfirm.
		rp = (RelativeLayout.LayoutParams) btnConfirm.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(180);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_wholesale_order;
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

//////////////////// Custom methods.

	public void addToTotalPrice(long addedPrice) {
		
		totalPrice += addedPrice;
		tvTotalPrice.setText(StringUtils.getFormattedNumber(totalPrice) + "원");
	}

	public void changeOrderStatus() {
		
		//status : 변경할 상태값 (-1: 주문취소, 0: 주문요청, 1: 입금대기, 2: 입금완료, 3: 거래완료)
		String url = CphConstants.BASE_API_URL + "wholesales/orders/set_status" +
				"?status=" + (orderSet.getStatus() + 1);
		
		//"?order_ids[]=" + order_ids +
		int size = orderSet.getItems().length;
		for(int i=0; i<size; i++) {
			
			if(orderSet.getItems()[i].isChecked()) {
				url += "&order_ids[]=" + orderSet.getItems()[i].getId();
			}
		}
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WholesaleForOrderPage.changeOrderStatus.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToChangeOrderStatus);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("WholesaleForOrderPage.changeOrderStatus.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						
						switch(orderSet.getStatus()) {
						
						case 0:
							ToastUtils.showToast(R.string.complete_changeOrderStatus1);
							orderSet.setStatus(1);
							
							long sum = 0;
							
							int size = orderSet.getItems().length;
							for(int i=0; i<size; i++) {
								
								if(orderSet.getItems()[i].isChecked()) {
									sum += orderSet.getItems()[i].getProduct_price()
											* orderSet.getItems()[i].getAmount();
									orderSet.getItems()[i].setStatus(1);
								}
							}
							
							orderSet.setSum(sum);
							break;
						
						case 2:
							ToastUtils.showToast(R.string.complete_changeOrderStatus3);
							orderSet.setStatus(3);
							mActivity.closePageWithRefreshPreviousPage();
							break;
						}
						
						createPage();
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

		//http://cph.minsangk.com/wholesales/orders/delete?collapse_key=10001
		String url = CphConstants.BASE_API_URL + "wholesales/orders/delete" +
				"?collapse_key=" + orderSet.getCollapse_key();
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WholesaleForOrderPage.changeOrderStatus.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToDeleteOrder);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("WholesaleForOrderPage.changeOrderStatus.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_deleteOrder);
						mActivity.closePageWithRefreshPreviousPage();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					ToastUtils.showToast(R.string.failToDeleteOrder);
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					ToastUtils.showToast(R.string.failToDeleteOrder);
					LogUtils.trace(oom);
				}
			}
		});
	}

	public void setTypeImage() {
		
		String url = CphConstants.BASE_API_URL + "retails/show" + 
				"?retail_id=" + orderSet.getRetail_id();
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WholesaleForOrderPage.setTypeImage.onError." + "\nurl : " + url);
				type.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("WholesaleForOrderPage.setTypeImage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);
					Retail retail = new Retail(objJSON.getJSONObject("retail"));
					
					if(StringUtils.isEmpty(retail.getMall_url())) {
						type.setBackgroundResource(R.drawable.offline_shop_icon);
					} else {
						type.setBackgroundResource(R.drawable.online_shop_icon);
					}
					
					type.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
}
