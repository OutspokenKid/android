package com.byecar.byecarplus.classes;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.byecar.byecarplus.R;
import com.byecar.byecarplus.wrappers.ViewWrapperForBrand;
import com.byecar.byecarplus.wrappers.ViewWrapperForCar;
import com.byecar.byecarplus.wrappers.ViewWrapperForSearchText;
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
		
		switch (itemCode) {
		
		case BCPConstants.ITEM_AUCTION:
			return R.layout.list_car;
			
		case BCPConstants.ITEM_CAR_BRAND: 
			return R.layout.grid_brand;
			
		case BCPConstants.ITEM_CAR_TEXT: 
			return R.layout.list_search_text;
		}
		
		return 0;
	}
	
	@Override
	public ViewWrapper getViewWrapperByItemCode(View convertView, int itemCode) {
		
		switch (itemCode) {
		case BCPConstants.ITEM_AUCTION:
			return new ViewWrapperForCar(convertView, itemCode);
			
		case BCPConstants.ITEM_CAR_BRAND:
			
			ViewWrapperForBrand vwfb = new ViewWrapperForBrand(convertView, itemCode);
			vwfb.setActivity(activity);
			return vwfb;
			
		case BCPConstants.ITEM_CAR_TEXT:
			ViewWrapperForSearchText vwfst = new ViewWrapperForSearchText(convertView, itemCode);
			vwfst.setActivity(activity);
			return vwfst;
		}
		
		return null;
	}
	
	@Override
	public void setRow(int itemCode, int position, View convertView) {
	
		switch (itemCode) {
		
		case BCPConstants.ITEM_AUCTION:
			break;
		}
	}
}
