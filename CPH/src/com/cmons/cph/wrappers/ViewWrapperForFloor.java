package com.cmons.cph.wrappers;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.TextView;

import com.cmons.cph.classes.ViewWrapper;
import com.cmons.cph.models.Floor;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class ViewWrapperForFloor extends ViewWrapper {
	
	private Floor floor;
	
	public TextView textView;
	
	public ViewWrapperForFloor(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			textView = (TextView) row;
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			TextView textView = (TextView) row;
			textView.setLayoutParams(new AbsListView.LayoutParams(
					LayoutParams.MATCH_PARENT, ResizeUtils.getSpecificLength(100)));
			textView.setPadding(ResizeUtils.getSpecificLength(40), 0, 
					ResizeUtils.getSpecificLength(40), 0);
			FontUtils.setFontSize(textView, 30);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel instanceof Floor) {
				
				floor = (Floor)baseModel;
				textView.setText(floor.floorName);
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
