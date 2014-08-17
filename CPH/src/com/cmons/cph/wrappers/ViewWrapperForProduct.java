package com.cmons.cph.wrappers;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.ViewWrapper;
import com.cmons.cph.models.Product;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class ViewWrapperForProduct extends ViewWrapper {
	
	private Product product;

	public ImageView ivImage;
	public TextView tvTitle;
	public TextView tvBuyCount;
	public TextView tvPrice;
	
	public ViewWrapperForProduct(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			ivImage = (ImageView) row.findViewById(R.id.grid_product_ivImage);
			tvTitle = (TextView) row.findViewById(R.id.grid_product_tvTitle);
			tvBuyCount = (TextView) row.findViewById(R.id.grid_product_tvBuyCount);
			tvPrice = (TextView) row.findViewById(R.id.grid_product_tvPrice);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			AbsListView.LayoutParams al = new AbsListView.LayoutParams(
					ResizeUtils.getSpecificLength(240), 
					ResizeUtils.getSpecificLength(360));
			row.setLayoutParams(al);
			
			FontUtils.setFontSize(tvTitle, 30);
			FontUtils.setFontSize(tvBuyCount, 30);
			FontUtils.setFontSize(tvPrice, 30);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel instanceof Product) {

				product = (Product) baseModel;

				tvTitle.setText(product.getName());
				tvBuyCount.setText("구매 : " + product.getOrder_cnt());
				tvPrice.setText("가격이 없네");
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
