package com.cmons.cph.wrappers;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.classes.ViewWrapper;
import com.cmons.cph.models.Order;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class ViewWrapperForOrder extends ViewWrapper {
	
	private Order order;
	
	public TextView tvOrder;
	public TextView tvPrice;
	public View checkbox;
	
	public ViewWrapperForOrder(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			tvOrder = (TextView) row.findViewById(R.id.list_order_tvOrder);
			tvPrice = (TextView) row.findViewById(R.id.list_order_tvPrice);
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
			tvOrder.setPadding(ResizeUtils.getSpecificLength(20), 0, 
					ResizeUtils.getSpecificLength(180), 0);
			
			FontUtils.setFontSize(tvPrice, 30);
			tvPrice.setPadding(0, 0, ResizeUtils.getSpecificLength(20), 0);

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
				
				if(order.getItemCode() == CphConstants.ITEM_ORDER_WHOLESALE) {
					tvOrder.setText(order.getProduct_name() + " / " +
							order.getSize() + " / " +
							order.getColor() + " / " + 
							order.getAmount() + "장");
					tvPrice.setText(null);
					checkbox.setVisibility(View.VISIBLE);
					if(order.isChecked()) {
						checkbox.setBackgroundResource(R.drawable.order_check_box_b);
					} else {
						checkbox.setBackgroundResource(R.drawable.order_check_box_a);
					}
				} else {
					tvOrder.setText(order.getProduct_name() + " (" +
							order.getSize() + "/" +
							order.getColor() + "/" + 
							order.getAmount() + "개)");
					tvPrice.setText(StringUtils.getFormattedNumber(order.getProduct_price()) + "원");
					checkbox.setVisibility(View.INVISIBLE);
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
