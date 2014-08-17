package com.cmons.cph.wrappers;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.ViewWrapper;
import com.cmons.cph.models.Staff;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class ViewWrapperForStaff extends ViewWrapper {
	
	private Staff staff;
	
	public TextView textView;
	public View action;
	
	public ViewWrapperForStaff(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			textView = (TextView) row.findViewById(R.id.list_staff_textView);
			action = row.findViewById(R.id.list_staff_action);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			RelativeLayout.LayoutParams rp = null;
			
			rp = (RelativeLayout.LayoutParams) textView.getLayoutParams();
			rp.width = LayoutParams.MATCH_PARENT;
			rp.height = ResizeUtils.getSpecificLength(100);
			textView.setPadding(ResizeUtils.getSpecificLength(40), 0, 
					ResizeUtils.getSpecificLength(160), 0);
			FontUtils.setFontSize(textView, 30);
			
			rp = (RelativeLayout.LayoutParams) action.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(112);
			rp.height = ResizeUtils.getSpecificLength(56);
			rp.rightMargin = ResizeUtils.getSpecificLength(24);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel instanceof Staff) {

				staff = (Staff) baseModel;
				textView.setText("직원이름 (아이디) 010 0000 0000");
				
				if(staff.isInRequest()) {
					action.setBackgroundResource(R.drawable.staff_approve_btn);
				} else {
					action.setBackgroundResource(R.drawable.staff_layoff_btn);
				}
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
