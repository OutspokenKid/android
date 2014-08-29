package com.cmons.cph.wrappers;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.ViewWrapper;
import com.cmons.cph.models.Wholesale;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class ViewWrapperForFavoriteShop extends ViewWrapper {
	
	private Wholesale wholesale;
	
	public TextView textView;
	public View delete;
	
	public ViewWrapperForFavoriteShop(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			textView = (TextView) row.findViewById(R.id.list_wholesale_textView);
			delete = row.findViewById(R.id.list_wholesale_delete);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			textView.getLayoutParams().height = ResizeUtils.getSpecificLength(120);
			delete.getLayoutParams().width = ResizeUtils.getSpecificLength(50);
			delete.getLayoutParams().height = ResizeUtils.getSpecificLength(50);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel instanceof Wholesale) {
				wholesale = (Wholesale) baseModel;
				textView.setText(wholesale.getName() + "(" + wholesale.getOwner_name() + 
						") 청평화몰 " + wholesale.getLocation());
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
		
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				delete();
			}
		});
	}
	
	@Override
	public void setUnusableView() {
		// TODO Auto-generated method stub
		
	}
	
//////////////////// Custom methods.
	
	public void delete() {
		
	}
}
