package com.cmons.cph.wrappers;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.TextView;

import com.cmons.cph.models.Sample;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;

public class ViewWrapperForSample extends ViewWrapper {
	
	private Sample sample;
	
	public TextView tvSample;
	
	public ViewWrapperForSample(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {
		
		LogUtils.log("###ViewWrapperForSample.bindViews.  ");

		try {
			tvSample = (TextView) row;
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		LogUtils.log("###ViewWrapperForSample.setSizes.  ");
		
		try {
			tvSample.setLayoutParams(new AbsListView.LayoutParams(
					LayoutParams.MATCH_PARENT, ResizeUtils.getSpecificLength(110)));
			tvSample.setPadding(ResizeUtils.getSpecificLength(30), 0, 
					ResizeUtils.getSpecificLength(30), 0);
			FontUtils.setFontSize(tvSample, 30);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		LogUtils.log("###ViewWrapperForSample.setValues.  ");
		
		try {
			if(baseModel instanceof Sample) {
				sample = (Sample) baseModel;
				
				tvSample.setText(sample.getWholesale_name() + " - " + 
						sample.getProduct_name() + 
						" (" + sample.getSize() + ", " + sample.getColor() + ")");
			} else {
				ToastUtils.showToast("2");
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
