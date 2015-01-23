package com.byecar.byecarplusfordealer.wrappers;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.byecar.byecarplusfordealer.R;
import com.byecar.byecarplusfordealer.classes.BCPConstants;
import com.byecar.byecarplusfordealer.classes.BCPFragmentActivity;
import com.byecar.byecarplusfordealer.common.TypeSearchCarPage;
import com.byecar.byecarplusfordealer.models.Brand;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class ViewWrapperForBrand extends ViewWrapper {
	
	private BCPFragmentActivity mActivity;
	private Brand brand;
	
	private ImageView ivImage;
	
	public ViewWrapperForBrand(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			ivImage = (ImageView) row.findViewById(R.id.grid_brand_ivImage);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			row.setLayoutParams(new AbsListView.LayoutParams(
					LayoutParams.MATCH_PARENT, ResizeUtils.getScreenWidth()/4));
			
			ResizeUtils.viewResize(132, 132, ivImage, 2, Gravity.CENTER, null);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel instanceof Brand) {
				brand = (Brand) baseModel;
				setImage(ivImage, brand.getImg_url());
			}
		} catch (Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setListeners() {
		
		if(brand != null && !brand.isNeedClickListener()) {
			return;
		}
		
		try {
			row.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					
					if(mActivity != null) {
						Bundle bundle = new Bundle();
						bundle.putInt("type", TypeSearchCarPage.TYPE_MODELGROUP);
						bundle.putInt("brand_id", brand.getId());
						mActivity.showPage(BCPConstants.PAGE_TYPE_SEARCH_CAR, bundle);
					}
				}
			});
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	@Override
	public void setUnusableView() {

	}
	
//////////////////// Custom methods.
	
	public void setActivity(BCPFragmentActivity activity) {
		
		mActivity = activity;
	}
}
