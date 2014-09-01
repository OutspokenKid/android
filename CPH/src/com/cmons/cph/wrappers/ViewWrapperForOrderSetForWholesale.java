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

public class ViewWrapperForOrderSetForWholesale extends ViewWrapper {
	
	private OrderSet orderSet;
	
	public TextView tvRegdate;
	public TextView tvName;
	public View type;
	public TextView tvPrice;
	
	public ViewWrapperForOrderSetForWholesale(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			tvRegdate = (TextView) row.findViewById(R.id.list_orderset_wholesale_tvRegdate);
			type = row.findViewById(R.id.list_orderset_wholesale_type);
			tvName = (TextView) row.findViewById(R.id.list_orderset_wholesale_tvName);
			tvPrice = (TextView) row.findViewById(R.id.list_orderset_wholesale_tvPrice);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			int p = ResizeUtils.getSpecificLength(30);
			
			tvRegdate.getLayoutParams().height = ResizeUtils.getSpecificLength(45);
			tvRegdate.setPadding(p, 0, 0, 0);
			
			tvName.getLayoutParams().height = ResizeUtils.getSpecificLength(55);
			tvName.setPadding(p, 0, 0, 0);
			
			RelativeLayout.LayoutParams rp = null;
			
			rp = (RelativeLayout.LayoutParams) type.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(146);
			rp.height = p;
			rp.topMargin = ResizeUtils.getSpecificLength(16);
			rp.rightMargin = p;
			
			tvPrice.getLayoutParams().height = ResizeUtils.getSpecificLength(55);
			tvPrice.setPadding(0, 0, p, 0);
			
			FontUtils.setFontSize(tvRegdate, 20);
			FontUtils.setFontSize(tvName, 30);
			FontUtils.setFontSize(tvPrice, 28);
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

				tvRegdate.setText("2014년 08월 16일 AM 07:46");
				tvName.setText("스타일콩");
				tvPrice.setText("1,000,000원");
				
				if(orderSet.isOnline()) {
					type.setBackgroundResource(R.drawable.online_shop_icon);
				} else {
					type.setBackgroundResource(R.drawable.offline_shop_icon);
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
