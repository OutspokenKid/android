package com.cmons.cph.fragments.wholesale;

import org.json.JSONObject;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.classes.CphAdapter;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Order;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.ResizeUtils;

public class WholesaleForOrderPage extends CmonsFragmentForWholesale {

	private TextView tvOrder;
	private View type;
	private Button btnOrder;
	private Button btnStandBy;
	private Button btnDeposit;
	private Button btnCompleted;
	private TextView tvSoldOut;
	
	private Button btnConfirm;
	private TextView tvAccount;
	private ListView listView;
	
	private int menuIndex;
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(models.size() == 0) {
			setMenu(0);
		}
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.wholesaleOrderPage_titleBar);
		
		tvOrder = (TextView) mThisView.findViewById(R.id.wholesaleOrderPage_tvOrder);
		type = mThisView.findViewById(R.id.wholesaleOrderPage_type);
		
		btnOrder = (Button) mThisView.findViewById(R.id.wholesaleOrderPage_btnOrder);
		btnStandBy = (Button) mThisView.findViewById(R.id.wholesaleOrderPage_btnStandBy);
		btnDeposit = (Button) mThisView.findViewById(R.id.wholesaleOrderPage_btnDeposit);
		btnCompleted = (Button) mThisView.findViewById(R.id.wholesaleOrderPage_btnCompleted);
		
		tvSoldOut = (TextView) mThisView.findViewById(R.id.wholesaleOrderPage_tvSoldOut);
		btnConfirm = (Button) mThisView.findViewById(R.id.wholesaleOrderPage_btnConfirm);
		tvAccount = (TextView) mThisView.findViewById(R.id.wholesaleOrderPage_tvAccount);
		
		listView = (ListView) mThisView.findViewById(R.id.wholesaleOrderPage_listView);
	}

	@Override
	public void setVariables() {

		title = "주문내역";
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		titleBar.getHomeButton().setVisibility(View.VISIBLE);
		
		SpannableStringBuilder sp1 = new SpannableStringBuilder("2014년 08월 31일 PM 06:45\n\n");
		sp1.setSpan(new RelativeSizeSpan(0.8f), 0, sp1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvOrder.append(sp1); 
		
		SpannableStringBuilder sp2 = new SpannableStringBuilder("스타일콩");
		sp2.setSpan(new RelativeSizeSpan(1.5f), 0, sp2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvOrder.append(sp2);
		
		SpannableStringBuilder sp3 = new SpannableStringBuilder(" 010-1234-5678");
		tvOrder.append(sp3);
		
		type.setBackgroundResource(R.drawable.offline_shop_icon);
		
		tvSoldOut.setText(R.string.disableSoldout);
		tvAccount.setText("결제방식 : 무통장입금\n국민은행(홍길동)123-12-11234151");
		
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
		
		btnStandBy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(1);
			}
		});
		
		btnDeposit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(2);
			}
		});
		
		btnCompleted.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(3);
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

		for(int i=0; i<10; i++) {
			Order order = new Order();
			order.setItemCode(CphConstants.ITEM_ORDER);
			models.add(order);
		}
		
		return false;
	}
	
	@Override
	public void downloadInfo() {
		
		url = CphConstants.BASE_API_URL + "wholesales/notices";
		super.downloadInfo();
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
	
////////////////////Custom classes.
	
	public void setMenu(int menuIndex) {

		switch(menuIndex) {
		
		case 0:
			btnOrder.setBackgroundResource(R.drawable.order_recommand_a);
			btnStandBy.setBackgroundResource(R.drawable.order_wait_b);
			btnDeposit.setBackgroundResource(R.drawable.order_done_b);
			btnCompleted.setBackgroundResource(R.drawable.order_complete_b);
			btnConfirm.setBackgroundResource(R.drawable.order_approve_btn);
			break;
			
		case 1:
			btnOrder.setBackgroundResource(R.drawable.order_recommand_b);
			btnStandBy.setBackgroundResource(R.drawable.order_wait_a);
			btnDeposit.setBackgroundResource(R.drawable.order_done_b);
			btnCompleted.setBackgroundResource(R.drawable.order_complete_b);
			btnConfirm.setBackgroundResource(R.drawable.order_progressing_btn);
			break;
			
		case 2:
			btnOrder.setBackgroundResource(R.drawable.order_recommand_b);
			btnStandBy.setBackgroundResource(R.drawable.order_wait_b);
			btnDeposit.setBackgroundResource(R.drawable.order_done_a);
			btnCompleted.setBackgroundResource(R.drawable.order_complete_b);
			btnConfirm.setBackgroundResource(R.drawable.order_complete_btn);
			break;
			
		case 3:
			btnOrder.setBackgroundResource(R.drawable.order_recommand_b);
			btnStandBy.setBackgroundResource(R.drawable.order_wait_b);
			btnDeposit.setBackgroundResource(R.drawable.order_done_b);
			btnCompleted.setBackgroundResource(R.drawable.order_complete_a);
			btnConfirm.setBackgroundResource(R.drawable.order_complete_btn);
			break;
		}

		this.menuIndex = menuIndex;

		refreshPage();
	}
}
