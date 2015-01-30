package com.byecar.wrappers;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPFragmentActivity;
import com.byecar.models.Review;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.views.OutSpokenRatingBar;

public class ViewWrapperForMyReview extends ViewWrapper {

	private Review review;
	private BCPFragmentActivity mActivity;
	
	private View bgTop;
	private TextView tvCarName;
	private Button btnEditReview;
	private TextView tvRegdate;
	private TextView tvTo;
	private OutSpokenRatingBar ratingBar;
	private TextView tvContent;
	private View bgBottom;
	
	public ViewWrapperForMyReview(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			bgTop = row.findViewById(R.id.list_my_review_bgTop);
			tvCarName = (TextView) row.findViewById(R.id.list_my_review_tvCarName);
			btnEditReview = (Button) row.findViewById(R.id.list_my_review_btnEditReview);
			tvRegdate = (TextView) row.findViewById(R.id.list_my_review_tvRegdate);
			tvTo = (TextView) row.findViewById(R.id.list_my_review_tvTo);
			ratingBar = (OutSpokenRatingBar) row.findViewById(R.id.list_my_review_ratingBar);
			tvContent = (TextView) row.findViewById(R.id.list_my_review_tvContent);
			bgBottom = row.findViewById(R.id.list_my_review_bgBottom);
			
			ratingBar.setMinRating(1);
			ratingBar.setMaxRating(5);
			ratingBar.setEmptyStarColor(Color.rgb(195, 195, 195));
			ratingBar.setFilledStarColor(Color.rgb(254, 188, 42));
			ratingBar.setUnitRating(OutSpokenRatingBar.UNIT_ONE);
			ratingBar.setTouchable(false);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			ResizeUtils.viewResizeForRelative(608, 100, bgTop, null, null, new int[]{0, 0, 0, 0});
			ResizeUtils.viewResizeForRelative(290, 66, tvCarName, null, null, new int[]{20, 4, 0, 0});
			ResizeUtils.viewResizeForRelative(88, 34, btnEditReview, null, null, new int[]{0, 20, 16, 0});
			ResizeUtils.viewResizeForRelative(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 
					tvRegdate, null, null, new int[]{0, 6, 16, 0});
			ResizeUtils.viewResizeForRelative(608, LayoutParams.WRAP_CONTENT, tvContent, null, null, null);
			tvContent.setMinHeight(ResizeUtils.getSpecificLength(90));
			ResizeUtils.viewResizeForRelative(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, tvTo, 
					null, null, new int[]{30, 0, 0, 0});
			ResizeUtils.viewResizeForRelative(120, 24, ratingBar, null, null, new int[]{0, 0, 26, 0});
			ratingBar.setLengths(ResizeUtils.getSpecificLength(18),
					ResizeUtils.getSpecificLength(6));
			ResizeUtils.viewResizeForRelative(608, 29, bgBottom, null, null, new int[]{0, 0, 0, 0});
			int p = ResizeUtils.getSpecificLength(30);
			tvContent.setPadding(p, p, p, 0);
			
			FontUtils.setFontSize(tvRegdate, 16);
			FontUtils.setFontSize(tvCarName, 30);
			FontUtils.setFontStyle(tvCarName, FontUtils.BOLD);
			FontUtils.setFontSize(tvTo, 20);
			FontUtils.setFontStyle(tvTo, FontUtils.BOLD);
			FontUtils.setFontSize(tvContent, 20);
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
				
				tvRegdate.setText(StringUtils.getDateString(
						"등록일 yyyy년 MM월 dd일", review.getCreated_at() * 1000));
				tvCarName.setText(review.getCar_full_name());
				
				if(review.getCertifier_id() != 0) {
					tvTo.setText(review.getCertifier_name() + " 검증사에게");
				} else if(review.getDealer_id() != 0) {
					tvTo.setText(review.getDealer_name() + " 딜러에게");
				}
				
				ratingBar.setRating(review.getRating());
				tvContent.setText(review.getContent());
			}
		} catch (Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setListeners() {
		
		if(review != null) {
			btnEditReview.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					if(mActivity != null) {
						Bundle bundle = new Bundle();
						bundle.putSerializable("review", review);
						mActivity.showPage(BCPConstants.PAGE_WRITE_REVIEW, bundle);
					}
				}
			});
		}
	}
	
	@Override
	public void setUnusableView() {

	}
	
	public void setActivity(BCPFragmentActivity activity) {

		mActivity = activity;
	}
}
