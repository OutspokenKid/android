package com.cmons.cph.wrappers;

import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
	public TextView tvPrice;
	public TextView tvBuyCount;
	public Button replyIcon;
	public View heartIcon;
	
	public ViewWrapperForProduct(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			ivImage = (ImageView) row.findViewById(R.id.grid_product_ivImage);
			tvTitle = (TextView) row.findViewById(R.id.grid_product_tvTitle);
			tvPrice = (TextView) row.findViewById(R.id.grid_product_tvPrice);
			tvBuyCount = (TextView) row.findViewById(R.id.grid_product_tvBuyCount);
			replyIcon = (Button) row.findViewById(R.id.grid_product_replyIcon);
			heartIcon = row.findViewById(R.id.grid_product_heartIcon);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			AbsListView.LayoutParams al = new AbsListView.LayoutParams(
					ResizeUtils.getScreenWidth()/2, 
					ResizeUtils.getSpecificLength(560));
			row.setLayoutParams(al);
			
			RelativeLayout.LayoutParams rp = null;
				
			//tvTitle.
			rp = (RelativeLayout.LayoutParams) tvTitle.getLayoutParams();
			rp.height = ResizeUtils.getSpecificLength(92);
			tvTitle.setPadding(ResizeUtils.getSpecificLength(14), 0, 
					ResizeUtils.getSpecificLength(120), 0);
			
			//tvPrice.
			rp = (RelativeLayout.LayoutParams) tvPrice.getLayoutParams();
			rp.height = ResizeUtils.getSpecificLength(50);
			rp.rightMargin = ResizeUtils.getSpecificLength(30);
			
			//tvBuyCount.
			rp = (RelativeLayout.LayoutParams) tvBuyCount.getLayoutParams();
			rp.height = ResizeUtils.getSpecificLength(24);
			
			//heartIcon.
			rp = (RelativeLayout.LayoutParams) heartIcon.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(27);
			rp.height = ResizeUtils.getSpecificLength(24);
			rp.rightMargin = ResizeUtils.getSpecificLength(4);
			
			//replyIcon.
			rp = (RelativeLayout.LayoutParams) replyIcon.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(50);
			rp.height = ResizeUtils.getSpecificLength(50);
			rp.rightMargin = ResizeUtils.getSpecificLength(10);
			rp.topMargin = ResizeUtils.getSpecificLength(10);
			
			FontUtils.setFontSize(tvTitle, 30);
			FontUtils.setFontSize(tvBuyCount, 18);
			FontUtils.setFontSize(tvPrice, 22);
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
				tvBuyCount.setText("" + product.getOrdered_cnt());
				tvPrice.setText(product.getPrice() + "원");
				
				if(product.getProduct_images() != null) {
					setImage(ivImage, product.getProduct_images()[0]);
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
