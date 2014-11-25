package com.cmons.cph.wrappers;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.TextView;

import com.cmons.cph.models.Retail;
import com.cmons.cph.models.Shop;
import com.cmons.cph.models.Wholesale;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class ViewWrapperForShop extends ViewWrapper {
	
	private Shop shop;
	
	public TextView textView;
	
	public ViewWrapperForShop(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			textView = (TextView) row;
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			TextView textView = (TextView) row;
			textView.setLayoutParams(new AbsListView.LayoutParams(
					LayoutParams.MATCH_PARENT, ResizeUtils.getSpecificLength(100)));
			textView.setPadding(ResizeUtils.getSpecificLength(40), 0, 
					ResizeUtils.getSpecificLength(40), 0);
			FontUtils.setFontSize(textView, 30);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel instanceof Shop) {

				shop = (Shop) baseModel;
				
				String shopString = shop.getName();
				
				switch(shop.getType()) {
				case Shop.TYPE_WHOLESALE:
					shopString += ((Wholesale)shop).getLocation();
					break;
				case Shop.TYPE_RETAIL_OFFLINE:
					shopString += ((Retail)shop).getAddress();
					break;
				case Shop.TYPE_RETAIL_ONLINE:
					shopString += ((Retail)shop).getMall_url();
					break;
				}
				
				textView.setText(shopString);
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
