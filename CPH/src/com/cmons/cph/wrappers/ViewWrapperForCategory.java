package com.cmons.cph.wrappers;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.models.Category;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class ViewWrapperForCategory extends ViewWrapper {
	
	private Category categoryForSignUp;
	
	public TextView textView;
	public View check;
	
	public ViewWrapperForCategory(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			textView = (TextView) row.findViewById(R.id.list_signup_category_textView);
			check = row.findViewById(R.id.list_signup_category_check);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			textView.getLayoutParams().height = ResizeUtils.getSpecificLength(120);
			check.getLayoutParams().width = ResizeUtils.getSpecificLength(52);
			check.getLayoutParams().height = ResizeUtils.getSpecificLength(34);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel instanceof Category) {
				
				categoryForSignUp = (Category) baseModel;
				
				textView.setText(categoryForSignUp.getName());
				
				if(categoryForSignUp.isSelected()) {
					check.setVisibility(View.VISIBLE);
				} else {
					check.setVisibility(View.INVISIBLE);
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
		
		if(categoryForSignUp != null) {
			row.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					if(categoryForSignUp.isSelected()) {
						categoryForSignUp.setSelected(false);
						check.setVisibility(View.INVISIBLE);
					} else {
						categoryForSignUp.setSelected(true);
						check.setVisibility(View.VISIBLE);
					}
				}
			});
		}
	}
	
	@Override
	public void setUnusableView() {
		// TODO Auto-generated method stub
		
	}
}
