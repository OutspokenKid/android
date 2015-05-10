package com.byecar.wrappers;

import android.view.Gravity;
import android.view.View;

import com.byecar.byecarplus.R;
import com.byecar.models.Car;
import com.byecar.views.ReviewViewSmall;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class ViewWrapperForMyBidsReview extends ViewWrapper {

	private Car car;

	private ReviewViewSmall reviewViewSmall;
	
	public ViewWrapperForMyBidsReview(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			reviewViewSmall = (ReviewViewSmall) row.findViewById(R.id.list_my_bids_review_reviewViewSmall);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			ResizeUtils.viewResize(578, 175, reviewViewSmall, 1, Gravity.CENTER, null);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel instanceof Car) {
				car = (Car) baseModel;
				reviewViewSmall.setReview(car);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setListeners() {
		
	}
	
	@Override
	public void setUnusableView() {

	}
}
