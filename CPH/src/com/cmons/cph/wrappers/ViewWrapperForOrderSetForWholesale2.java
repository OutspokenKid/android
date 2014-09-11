package com.cmons.cph.wrappers;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.ViewWrapper;
import com.cmons.cph.models.OrderSet;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class ViewWrapperForOrderSetForWholesale2 extends ViewWrapper {
	
	private OrderSet orderSet;

	public TextView tvName;
	public TextView tvRegdate;
	public TextView tvPrice;
	
	public ViewWrapperForOrderSetForWholesale2(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			tvName = (TextView) row.findViewById(R.id.list_orderset_wholesale2_tvName);
			tvRegdate = (TextView) row.findViewById(R.id.list_orderset_wholesale2_tvRegdate);
			tvPrice = (TextView) row.findViewById(R.id.list_orderset_wholesale2_tvPrice);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			RelativeLayout.LayoutParams rp = null;
			int width = ResizeUtils.getScreenWidth()/2;
			int height = ResizeUtils.getSpecificLength(50);
			int p = ResizeUtils.getSpecificLength(20);
			
			rp = (RelativeLayout.LayoutParams) tvName.getLayoutParams();
			rp.width = width;
			rp.leftMargin = p;
			
			rp = (RelativeLayout.LayoutParams) tvRegdate.getLayoutParams();
			rp.height = height;
			rp.rightMargin = p;
			
			rp = (RelativeLayout.LayoutParams) tvPrice.getLayoutParams();
			rp.height = height;
			rp.rightMargin = p;
			
			FontUtils.setFontSize(tvRegdate, 20);
			FontUtils.setFontSize(tvName, 32);
			FontUtils.setFontSize(tvPrice, 32);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel instanceof OrderSet) {
				
				orderSet = (OrderSet) baseModel;

				String orderSetString = orderSet.getItems()[0].getProduct_name();
				
				if(orderSet.getItems().length > 1) {
					orderSetString += " 외 " + (orderSet.getItems().length - 1) + "건";
				}

				tvName.setText(orderSetString);
				tvRegdate.setText(StringUtils.getDateString("yyyy.MM.dd aa hh:mm", 
						orderSet.getItems()[0].getCreated_at()*1000));
				tvPrice.setText(StringUtils.getFormattedNumber(orderSet.getSum()) + "원");
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
