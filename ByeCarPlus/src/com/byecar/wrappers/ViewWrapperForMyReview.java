package com.byecar.wrappers;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.byecar.byecarplus.R;
import com.byecar.classes.BCPFragmentActivity;
import com.byecar.models.Review;
import com.byecar.views.ReviewView;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class ViewWrapperForMyReview extends ViewWrapper {

	private Review review;
	private BCPFragmentActivity mActivity;

	private ReviewView reviewView;
	private ReviewView replyView;
	private View line;
	
	public ViewWrapperForMyReview(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			reviewView = (ReviewView) row.findViewById(R.id.list_my_review_reviewView);
			replyView = (ReviewView) row.findViewById(R.id.list_my_review_replyView);
			line = row.findViewById(R.id.list_my_review_line);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			ResizeUtils.viewResize(574, LayoutParams.WRAP_CONTENT, reviewView, 1, 
					Gravity.CENTER_HORIZONTAL, new int[]{0, 7, 0, 0});
			ResizeUtils.viewResize(574, LayoutParams.WRAP_CONTENT, replyView, 1, 
					Gravity.CENTER_HORIZONTAL, new int[]{0, 12, 0, 0});
			
			ResizeUtils.viewResize(544, ResizeUtils.ONE, line, 1, 
					Gravity.CENTER_HORIZONTAL, new int[]{0, 24, 0, 0});
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

				reviewView.setReview(review, mActivity);
				
				if(review.getReply() != null) {
					replyView.setVisibility(View.VISIBLE);
					replyView.setReply(review.getReply());
				} else {
					replyView.setVisibility(View.GONE);
				}
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
	
	public void setActivity(BCPFragmentActivity activity) {

		mActivity = activity;
	}
}
