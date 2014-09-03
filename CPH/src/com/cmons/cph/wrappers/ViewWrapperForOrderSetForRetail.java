package com.cmons.cph.wrappers;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.ViewWrapper;
import com.cmons.cph.models.Order;
import com.cmons.cph.models.OrderSet;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class ViewWrapperForOrderSetForRetail extends ViewWrapper {
	
	private OrderSet orderSet;
	
	public TextView tvRegdate;
	public TextView tvName;
	public TextView tvRight;
	
	public ViewWrapperForOrderSetForRetail(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			tvRegdate = (TextView) row.findViewById(R.id.list_orderset_retail_tvRegdate);
			tvName = (TextView) row.findViewById(R.id.list_orderset_retail_tvName);
			tvRight = (TextView) row.findViewById(R.id.list_orderset_retail_tvRight);
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
			tvRight.setPadding(0, 0, p, 0);
			
			FontUtils.setFontSize(tvRegdate, 20);
			FontUtils.setFontSize(tvName, 34);
			FontUtils.setFontSize(tvRight, 28);
			
			FontUtils.setFontStyle(tvName, FontUtils.BOLD);
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

				String dateString = StringUtils.getDateString(
						"yyyy년 MM월 dd일 aa hh:mm", 
						orderSet.getItems()[0].getCreated_at() * 1000);
				tvRegdate.setText(dateString);
				
				String nameString = orderSet.getItems()[0].getProduct_name();
				
				if(orderSet.getItems().length > 1) {
					nameString += " 외 " + (orderSet.getItems().length - 1) + "건";
				}
				
				tvName.setText(nameString);

				tvRight.setText(null);
				switch(orderSet.getStatus()) {
				
				case Order.STATUS_CANCELED:
					FontUtils.addSpan(tvRight, "주문취소", 0, 1);
					break;
					
				case Order.STATUS_REQUESTED:
					FontUtils.addSpan(tvRight, "주문요청", 0, 1);
					break;
					
				case Order.STATUS_STANDBY:
					FontUtils.addSpan(tvRight, "입금대기", 0, 1);
					break;
					
				case Order.STATUS_DEPOSIT:
					FontUtils.addSpan(tvRight, "입금완료", 0, 1);
					break;
					
				case Order.STATUS_COMPLETED:
					FontUtils.addSpan(tvRight, "거래완료", 0, 1);
					break;
				}

				FontUtils.addSpan(tvRight, "\n" + 
						StringUtils.getFormattedNumber(orderSet.getSum()) 
						+ "원", Color.RED, 1);
				
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
