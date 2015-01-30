package com.byecar.wrappers;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;

import com.byecar.byecarplus.R;
import com.byecar.models.Car;
import com.byecar.views.NormalCarView;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.ResizeUtils;

public class ViewWrapperForDirectNormal extends ViewWrapper {

	private Car car;
	private NormalCarView normalCarView;
	
	public ViewWrapperForDirectNormal(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		normalCarView = (NormalCarView) row.findViewById(R.id.list_direct_normal_normalCarView);
	}

	@Override
	public void setSizes() {

		row.setLayoutParams(new AbsListView.LayoutParams(
				LayoutParams.MATCH_PARENT, ResizeUtils.getSpecificLength(160)));
		
		ResizeUtils.viewResize(578, 132, normalCarView, 1, Gravity.CENTER_HORIZONTAL, null);
	}

	@Override
	public void setValues(BaseModel baseModel) {

		if(baseModel instanceof Car) {
			
			this.car = (Car) baseModel;
			normalCarView.setCar(car);
		}
	}

	@Override
	public void setListeners() {
	}

	@Override
	public void setUnusableView() {

	}
}
