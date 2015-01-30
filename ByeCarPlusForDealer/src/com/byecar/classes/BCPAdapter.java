package com.byecar.classes;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.outspoken_kid.classes.OutSpokenAdapter;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;

/**
 * @author HyungGunKim
 *
 */
public class BCPAdapter extends OutSpokenAdapter {

	private BCPFragmentActivity activity;
	
	public BCPAdapter(Context context, BCPFragmentActivity activity, 
			LayoutInflater inflater, ArrayList<BaseModel> models) {
		super(context, inflater, models);
		this.activity = activity;
	}

	@Override
	public int getLayoutResIdByItemCode(int itemCode) {
		
		return 0;
	}
	
	@Override
	public ViewWrapper getViewWrapperByItemCode(View convertView, int itemCode) {
		
		return null;
	}
	
	@Override
	public void setRow(int itemCode, int position, View convertView) {
		
	}
}
