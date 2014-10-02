package com.cmons.cph.wrappers;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.ViewWrapper;
import com.cmons.cph.models.Customer;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class ViewWrapperForCustomer_wholesale extends ViewWrapper {
	
	private Customer customer;

	private View icon;
	private TextView tvInfo;
	
	public ViewWrapperForCustomer_wholesale(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			icon = (TextView) row.findViewById(R.id.list_customer_wholesale_icon);
			tvInfo = (TextView) row.findViewById(R.id.list_customer_wholesale_tvInfo);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			RelativeLayout.LayoutParams rp = null;
			int p = ResizeUtils.getSpecificLength(20);
			
			rp = (RelativeLayout.LayoutParams) icon.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(146);
			rp.height = ResizeUtils.getSpecificLength(30);
			rp.leftMargin = p;
			rp.topMargin = p;
			
			tvInfo.setPadding(p, p/2, p, p);
			FontUtils.setFontSize(tvInfo, 30);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel instanceof Customer) {
				customer = (Customer) baseModel;
				tvInfo.setText(customer.getName() + 
						"(" + customer.getOwner_name() + ")" +
						" " + customer.getPhone_number());
				
				if(StringUtils.isEmpty(customer.getMall_url())) {
					icon.setBackgroundResource(R.drawable.offline_shop_icon);
				} else {
					icon.setBackgroundResource(R.drawable.online_shop_icon);
				}
			} else {
				setUnusableView();
			}
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setListeners() {

	}
	
	@Override
	public void setUnusableView() {
		// TODO Auto-generated method stub
		
	}
}
