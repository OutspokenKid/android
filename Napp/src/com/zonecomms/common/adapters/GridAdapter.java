package com.zonecomms.common.adapters;

import java.util.ArrayList;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.ResizeUtils;
import com.zonecomms.common.views.WrapperView;
import com.zonecomms.napp.R;
import com.zonecomms.napp.classes.ViewWrapper;
import com.zonecomms.napp.classes.ZoneConstants;
import com.zonecomms.common.wrappers.ViewWrapperForMember;
import com.zonecomms.common.wrappers.ViewWrapperForPapp;
import com.zonecomms.common.wrappers.ViewWrapperForPost;

import android.content.Context;

public class GridAdapter extends ZoneAdapter {
	
	public GridAdapter(Context context, ArrayList<BaseModel> models, boolean useHardCache) {

		super(context, models, useHardCache);
	}

	@Override
	protected int getLayoutResIdByItemCode(int itemCode) {

		switch(itemCode) {
		
		case ZoneConstants.ITEM_POST:
			return R.layout.grid_post;
		
		case ZoneConstants.ITEM_USER:
			return R.layout.grid_member;
			
		case ZoneConstants.ITEM_PAPP:
			return R.layout.grid_app;
		}
		
		return 0;
	}

	@Override
	protected void setRow(int itemCode, int position, WrapperView viewForWrapper) {

		int length = ResizeUtils.getSpecificLength(8);
		
		switch(itemCode) {
		case ZoneConstants.ITEM_POST:
			if(position % 2 == 0) {
				viewForWrapper.setPadding(length, 0, length/2, length);
			} else {
				viewForWrapper.setPadding(length/2, 0, length, length);
			}
			break;
		case ZoneConstants.ITEM_USER:
		case ZoneConstants.ITEM_PAPP:
			if(position % 4 == 0) {
				viewForWrapper.setPadding(length, 0, length/2, length);
			} else if(position % 4 != 3) {
				viewForWrapper.setPadding(length/2, 0, length/2, length);
			} else {
				viewForWrapper.setPadding(length/2, 0, length, length);
			}
			break;
		}
	}

	@Override
	protected ViewWrapper getViewWrapperByItemCode(WrapperView wrapperView,
			int itemCode) {
		
		switch(itemCode) {
		
		case ZoneConstants.ITEM_POST:
			return new ViewWrapperForPost(wrapperView, itemCode);

		case ZoneConstants.ITEM_USER:
			return new ViewWrapperForMember(wrapperView, itemCode);
		
		case ZoneConstants.ITEM_PAPP:
			return new ViewWrapperForPapp(wrapperView, itemCode);
		}
		return null;
	}}
