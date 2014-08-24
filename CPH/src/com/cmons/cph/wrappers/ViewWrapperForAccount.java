package com.cmons.cph.wrappers;

import android.view.View;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.ViewWrapper;
import com.cmons.cph.models.Account;
import com.cmons.cph.models.Category;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class ViewWrapperForAccount extends ViewWrapper {
	
	private Account account;
	
	public TextView textView;
	public View delete;
	
	public ViewWrapperForAccount(View row, int itemCode) {
		super(row, itemCode);
	}
	
	@Override
	public void bindViews() {

		try {
			textView = (TextView) row.findViewById(R.id.list_account_textView);
			delete = row.findViewById(R.id.list_account_delete);
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
			if(baseModel instanceof Account) {
				
				account = (Account) baseModel;

				LogUtils.log("###View.setValues.  account.bank : " + account.getBank());
				
				textView.setText(account.getBank() + 
						" " + account.getNumber() + 
						"(" + account.getDepositor() + ")");
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
		
		if(account != null) {
		}
	}
	
	@Override
	public void setUnusableView() {
		// TODO Auto-generated method stub
		
	}
}
