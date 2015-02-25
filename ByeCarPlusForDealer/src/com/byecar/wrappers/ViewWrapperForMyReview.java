package com.byecar.wrappers;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.byecar.byecarplusfordealer.R;
import com.byecar.models.Review;
import com.byecar.views.ReviewView;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class ViewWrapperForMyReview extends ViewWrapper {

	private Review review;

	private ReviewView reviewView; 
	
	public ViewWrapperForMyReview(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			reviewView = (ReviewView) row.findViewById(R.id.list_my_review_reviewView);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			ResizeUtils.viewResize(574, LayoutParams.WRAP_CONTENT, reviewView, 1, Gravity.CENTER_HORIZONTAL, new int[]{0, 16, 0, 0});
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel instanceof Review) {
				review = (Review) baseModel;
				reviewView.setReview(review);
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
