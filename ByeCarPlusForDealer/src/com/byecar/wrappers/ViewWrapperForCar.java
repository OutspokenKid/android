package com.byecar.wrappers;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;

import com.byecar.byecarplusfordealer.R;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPFragmentActivity;
import com.byecar.models.Car;
import com.byecar.views.CarView;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.ResizeUtils;

public class ViewWrapperForCar extends ViewWrapper {

	private Car car;
	private CarView carView;
	private BCPFragmentActivity mActivity; 
	
	public ViewWrapperForCar(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		carView = (CarView) row.findViewById(R.id.list_car_carView);
	}

	@Override
	public void setSizes() {

		row.setLayoutParams(new AbsListView.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		ResizeUtils.viewResize(578, 175, carView, 1, Gravity.CENTER_HORIZONTAL, null);
	}

	@Override
	public void setValues(BaseModel baseModel) {

		if(baseModel instanceof Car) {
			
			this.car = (Car) baseModel;
			carView.setActivity(mActivity);
			carView.setCar(car);
		}
	}

	@Override
	public void setListeners() {
		
		carView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(mActivity != null && car != null) {
					Bundle bundle = new Bundle();
					bundle.putSerializable("car", car);
					bundle.putInt("type", car.getType());
					mActivity.showPage(BCPConstants.PAGE_CAR_DETAIL, bundle);
				}
			}
		});
	}

	@Override
	public void setUnusableView() {

	}
	
	public void setActivity(BCPFragmentActivity activity) {

		mActivity = activity;
	}
}
