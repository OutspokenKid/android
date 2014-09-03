package com.cmons.cph.wrappers;

import android.view.View;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.classes.ViewWrapper;
import com.cmons.cph.models.Retail;
import com.cmons.cph.models.Wholesale;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class ViewWrapperForCustomer extends ViewWrapper {
	
	private Wholesale wholesale;
	private Retail retail;
	
	public TextView tvCustomer;
	public TextView tvStatus;
	
	public ViewWrapperForCustomer(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			tvCustomer = (TextView) row.findViewById(R.id.list_customer_tvCustomer);
			tvStatus = (TextView) row.findViewById(R.id.list_customer_tvStatus);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			int p = ResizeUtils.getSpecificLength(20);
			tvCustomer.setMaxWidth(ResizeUtils.getSpecificLength(540));
			tvCustomer.setPadding(p, p, p, p);
			FontUtils.setFontSize(tvCustomer, 30);
			
			tvStatus.setPadding(0, 0, p, 0);
			FontUtils.setFontSize(tvStatus, 30);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel.getItemCode() == CphConstants.ITEM_CUSTOMER_WHOLESALE) {
				
				if(baseModel instanceof Retail) {
					retail = (Retail) baseModel;
					
					tvCustomer.setText("도매 - 거래처목록 - " + retail.getName());
					tvStatus.setText(null);
				} else {
					setUnusableView();
				}
				
			} else if(baseModel.getItemCode() == CphConstants.ITEM_CUSTOMER_RETAIL) {
				
				if(baseModel instanceof Wholesale) {
					wholesale = (Wholesale) baseModel;
					tvCustomer.setText(wholesale.getName() + " 청평화몰 " + wholesale.getLocation());
					
					if(wholesale.isStandingBy()) {
						tvStatus.setText("승인대기중");
					} else {
						tvStatus.setText(null);
					}
				} else {
					setUnusableView();
				}
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
