package com.byecar.wrappers;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.models.AreaForSearch;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class ViewWrapperForArea extends ViewWrapper {

	private AreaForSearch area;
	
	private TextView tvArea;
	
	public ViewWrapperForArea(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		tvArea = (TextView) row.findViewById(R.id.list_area_tvArea);
	}

	@Override
	public void setSizes() {

		row.setLayoutParams(new AbsListView.LayoutParams(
				LayoutParams.MATCH_PARENT, ResizeUtils.getSpecificLength(82)));
		
		FontUtils.setFontSize(tvArea, 26);
	}

	@Override
	public void setValues(BaseModel baseModel) {

		if(baseModel instanceof AreaForSearch) {
			
			this.area = (AreaForSearch) baseModel;
			tvArea.setText(area.getAddress());
		}
	}

	@Override
	public void setListeners() {

	}

	@Override
	public void setUnusableView() {

	}
}
