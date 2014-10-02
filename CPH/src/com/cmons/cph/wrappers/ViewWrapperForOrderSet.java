package com.cmons.cph.wrappers;

import android.graphics.Color;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.ShopActivity;
import com.cmons.cph.classes.ViewWrapper;
import com.cmons.cph.models.Order;
import com.cmons.cph.models.OrderSet;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class ViewWrapperForOrderSet extends ViewWrapper {
	
	private OrderSet orderSet;
	
	public TextView tvLeft;
	public TextView tvRight;
	
	public ViewWrapperForOrderSet(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			tvLeft = (TextView) row.findViewById(R.id.list_orderset_tvLeft);
			tvRight = (TextView) row.findViewById(R.id.list_orderset_tvRight);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			int p = ResizeUtils.getSpecificLength(30);
			int p2 = ResizeUtils.getSpecificLength(15);
			tvLeft.setPadding(p, p2, 0, p2);
			tvRight.setPadding(0, p2, p, p2);
			tvLeft.setLineSpacing(0, 1.2f);
			tvRight.setLineSpacing(0, 1.2f);
			
			FontUtils.setFontSize(tvLeft, 30);
			FontUtils.setFontSize(tvRight, 30);
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

				String leftString = orderSet.getItems()[0].getProduct_name();
				
				if(orderSet.getItems().length > 1) {
					leftString += " 외 " + (orderSet.getItems().length - 1) + "건";
				}
				
				tvLeft.setText(Html.fromHtml(
						"<B>" +
						(ShopActivity.getInstance().user.getWholesale_id() != 0? 
								orderSet.getRetail_name() : 
								orderSet.getWholesale_name()) +
						"</B><BR>" +
						leftString));

				tvRight.setText(null);
				
				String dateString = StringUtils.getDateString(
						"yyyy.MM.dd\n", 
						orderSet.getItems()[0].getCreated_at() * 1000);
				FontUtils.addSpan(tvRight, dateString, 0, 0.7f);
				
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
