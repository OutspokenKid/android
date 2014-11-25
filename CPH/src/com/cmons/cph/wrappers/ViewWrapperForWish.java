package com.cmons.cph.wrappers;

import android.view.View;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.models.Wish;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class ViewWrapperForWish extends ViewWrapper {
	
	private Wish wish;
	
	private TextView tvInfo;
	private TextView tvPrice;
	
	public ViewWrapperForWish(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			tvInfo = (TextView) row.findViewById(R.id.list_wish_tvInfo);
			tvPrice = (TextView) row.findViewById(R.id.list_wish_tvPrice);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			tvInfo.getLayoutParams().height = ResizeUtils.getSpecificLength(70);
			tvInfo.setPadding(ResizeUtils.getSpecificLength(20), 0, 0, 0);
			tvInfo.setMaxWidth(ResizeUtils.getSpecificLength(540));
			FontUtils.setFontSize(tvInfo, 24);
			
			tvPrice.getLayoutParams().height = ResizeUtils.getSpecificLength(70);
			tvPrice.setPadding(0, 0, ResizeUtils.getSpecificLength(20), 0);
			FontUtils.setFontSize(tvPrice, 24);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel instanceof Wish) {
				
				wish = (Wish) baseModel;

				tvInfo.setText(wish.getProduct_name() + 
						"(" + wish.getSize() + "/" + 
						wish.getColor() + "/" + 
						wish.getAmount() + "개)");
				
				tvPrice.setText(StringUtils.getFormattedNumber(
						wish.getProduct_price() * wish.getAmount()) + "원");
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
