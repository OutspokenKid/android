package com.cmons.cph.wrappers;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.ViewWrapper;
import com.cmons.cph.models.Order;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class ViewWrapperForOrder extends ViewWrapper {
	
	private Order order;
	
	public TextView tvOrder;
	public View checkbox;
	
	public ViewWrapperForOrder(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			tvOrder = (TextView) row.findViewById(R.id.list_order_tvOrder);
			checkbox = row.findViewById(R.id.list_order_checkbox);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			tvOrder.getLayoutParams().height = ResizeUtils.getSpecificLength(100);
			FontUtils.setFontSize(tvOrder, 30);
			tvOrder.setPadding(ResizeUtils.getSpecificLength(10), 0, 
					ResizeUtils.getSpecificLength(100), 0);

			RelativeLayout.LayoutParams rp = null;
			rp = (RelativeLayout.LayoutParams) checkbox.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(44);
			rp.height = ResizeUtils.getSpecificLength(43);
			rp.rightMargin = ResizeUtils.getSpecificLength(20);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel instanceof Order) {
				
				order = (Order) baseModel;
				
				tvOrder.setText("티셔츠 / XL / 파란색 / 240장");
				
				if(order.isChecked()) {
					checkbox.setBackgroundResource(R.drawable.order_check_box_b);
				} else {
					checkbox.setBackgroundResource(R.drawable.order_check_box_a);
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
