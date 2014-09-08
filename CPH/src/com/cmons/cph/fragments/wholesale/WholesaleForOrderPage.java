package com.cmons.cph.fragments.wholesale;

import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.cmons.cph.models.Order;
import com.cmons.cph.models.OrderSet;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

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
		
		String dateString = StringUtils.getDateString("yyyy.MM.dd aa hh:mm", 
				orderSet.getItems()[0].getCreated_at() * 1000);
		FontUtils.addSpan(tvOrder, dateString, 0, 0.8f);
		
		FontUtils.addSpan(tvOrder, "\n" + orderSet.getRetail_name(), 0, 1.5f);
		FontUtils.addSpan(tvOrder, " (" + orderSet.getRetail_phone_number() + ")", 0, 1);
		
		type.setBackgroundResource(R.drawable.offline_shop_icon);
		
		tvSoldOut.setText(R.string.disableSoldout);
		
		String accountString = "결제방식 : ";

		if(orderSet.getPayment_type() == 1) {
			accountString += "무통장입금" +
					"\n" + getWholesale().getAccounts()[orderSet.getPayment_account_id()];
		} else {
			accountString += "사입자대납" +
					"\n" + orderSet.getPayment_purchaser_info();
		}
		
		accountString += "\n총 금액";
		
		tvAccount.setText(accountString);
		tvTotalPrice.setText(StringUtils.getFormattedNumber(orderSet.getSum()) + "원");
		
		adapter = new CphAdapter(mContext, getActivity().getLayoutInflater(), models);
		listView.setAdapter(adapter);
		listView.setDivider(new ColorDrawable(Color.WHITE));
		listView.setDividerHeight(1);

		if(models.size() == 0) {
			for(Order order : orderSet.getItems()) {
				order.setItemCode(CphConstants.ITEM_ORDER_WHOLESALE);
				models.add(order);
			}
		}
	}

	@Override
	public void setListeners() {
	
		btnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				approval();
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Order order = (Order)models.get(arg2);
				order.setChecked(!order.isChecked());
				adapter.notifyDataSetChanged();
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
	
	public void approval() {
		
		
	}
}
