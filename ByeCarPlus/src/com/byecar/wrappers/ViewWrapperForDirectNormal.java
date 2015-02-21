package com.byecar.wrappers;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;

import com.byecar.byecarplus.R;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPFragmentActivity;
import com.byecar.models.Car;
import com.byecar.views.NormalCarView;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.ResizeUtils;

public class ViewWrapperForDirectNormal extends ViewWrapper {

	private Car car;
	private NormalCarView normalCarView;
	private BCPFragmentActivity mActivity; 
	
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
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		ResizeUtils.viewResize(578, 132, normalCarView, 1, Gravity.CENTER_HORIZONTAL, new int[]{0, 14, 0, 14});
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
		
		normalCarView.setOnClickListener(new OnClickListener() {

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
