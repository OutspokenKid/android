package com.byecar.byecarplus.classes;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.byecar.byecarplus.R;
import com.byecar.byecarplus.wrappers.ViewWrapperForAuction;
import com.outspoken_kid.classes.OutSpokenAdapter;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;

/**
 * @author HyungGunKim
 *
 */
public class BCPAdapter extends OutSpokenAdapter {
	
	public BCPAdapter(Context context, LayoutInflater inflater, ArrayList<BaseModel> models) {

		super(context, inflater, models);
	}

	@Override
	public int getLayoutResIdByItemCode(int itemCode) {
		
		switch (itemCode) {
		
		case BCPConstants.ITEM_AUCTION:
			return R.layout.list_auction;
		}
		
		return 0;
	}
	
	@Override
	public ViewWrapper getViewWrapperByItemCode(View convertView, int itemCode) {
		
		switch (itemCode) {
		case BCPConstants.ITEM_AUCTION:
			return new ViewWrapperForAuction(convertView, itemCode);
		}
		
		return null;
	}
	
	@Override
	public void setRow(int itemCode, int position, View convertView) {
	
		switch (itemCode) {
		
//		case CphConstants.ITEM_PRODUCT:
//			break;
		}
	}
}
