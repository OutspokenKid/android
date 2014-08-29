package com.cmons.cph.wrappers;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.ViewWrapper;
import com.cmons.cph.models.WholesaleWish;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class ViewWrapperForWholesaleWish extends ViewWrapper {
	
	private WholesaleWish wholesaleWish;
	
	public TextView tvLeft;
	public TextView tvRight;
	
	public ViewWrapperForWholesaleWish(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			tvLeft = (TextView) row.findViewById(R.id.list_wholesale_wish_tvLeft);
			tvRight = (TextView) row.findViewById(R.id.list_wholesale_wish_tvRight);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			tvLeft.getLayoutParams().height = ResizeUtils.getSpecificLength(100);
			tvLeft.setMaxWidth(ResizeUtils.getSpecificLength(480));
			tvLeft.setPadding(ResizeUtils.getSpecificLength(20), 0, 0, 0);
			FontUtils.setFontSize(tvLeft, 20);

			tvRight.getLayoutParams().height = ResizeUtils.getSpecificLength(100);
			tvRight.setMaxWidth(ResizeUtils.getSpecificLength(240));
			tvRight.setPadding(0, 0, ResizeUtils.getSpecificLength(20), 0);
			FontUtils.setFontSize(tvRight, 20);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel instanceof WholesaleWish) {
				
				wholesaleWish = (WholesaleWish) baseModel;

				tvLeft.setText(null);
				
				SpannableStringBuilder sp1 = new SpannableStringBuilder(wholesaleWish.getName() + 
						" (" + wholesaleWish.getLocation() + ")");
				sp1.setSpan(new RelativeSizeSpan(1.5f), 0, sp1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				tvLeft.append(sp1); 
				
				SpannableStringBuilder sp2;
				
				if(wholesaleWish.getItems().length == 1) {
					sp2 = new SpannableStringBuilder(
							"\n" + wholesaleWish.getItems()[0].getProduct_name());
				} else {
					sp2 = new SpannableStringBuilder(
							"\n" + wholesaleWish.getItems()[0].getProduct_name() +
							" 외 " + (wholesaleWish.getItems().length - 1) + "개");
				}
				
				tvLeft.append(sp2); 
				
				tvRight.setText(null);
				
				String dateString = StringUtils.getDateString("yyyy.MM.dd", wholesaleWish.getItems()[0].getCreated_at() * 1000);
				SpannableStringBuilder sp3 = new SpannableStringBuilder(dateString);
				tvRight.append(sp3);
				
				SpannableStringBuilder sp5 = new SpannableStringBuilder(
						"\n" + StringUtils.getFormattedNumber(wholesaleWish.getSum()) + "원");
				sp5.setSpan(new ForegroundColorSpan(Color.RED), 0, sp5.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				sp5.setSpan(new RelativeSizeSpan(1.5f), 0, sp5.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				tvRight.append(sp5);
				
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
