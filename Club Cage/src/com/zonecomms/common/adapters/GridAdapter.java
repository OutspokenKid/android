package com.zonecomms.common.adapters;

import java.util.ArrayList;

import android.content.Context;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.ResizeUtils;
import com.zonecomms.clubcage.R;
import com.zonecomms.clubcage.classes.ZoneConstants;
import com.zonecomms.common.views.WrapperView;
import com.zonecomms.common.wrappers.ViewWrapper;
import com.zonecomms.common.wrappers.ViewWrapperForMember;
import com.zonecomms.common.wrappers.ViewWrapperForPhoto;
import com.zonecomms.common.wrappers.ViewWrapperForPost;
import com.zonecomms.common.wrappers.ViewWrapperForSchedule;

public class GridAdapter extends ZoneAdapter {

	public GridAdapter(Context context, ArrayList<BaseModel> models,
			boolean useHardCache) {
		super(context, models, useHardCache);
	}

	@Override
	protected int getLayoutResIdByItemCode(int itemCode) {
		
		switch (itemCode) {
		case ZoneConstants.ITEM_POST:
			return R.layout.grid_post;
		
		case ZoneConstants.ITEM_SCHEDULE:
			return R.layout.grid_schedule;
			
		case ZoneConstants.ITEM_USER:
			return R.layout.grid_member;
			
		case ZoneConstants.ITEM_PHOTO:
			return R.layout.grid_photo;
		}
		
		return 0;
	}

	@Override
	protected ViewWrapper getViewWrapperByItemCode(WrapperView wrapperView,
			int itemCode) {
		
		switch(itemCode) {
		
		case ZoneConstants.ITEM_POST:
			return new ViewWrapperForPost(wrapperView, itemCode);

		case ZoneConstants.ITEM_SCHEDULE:
			return new ViewWrapperForSchedule(wrapperView, itemCode);
			
		case ZoneConstants.ITEM_USER:
			return new ViewWrapperForMember(wrapperView, itemCode);

		case ZoneConstants.ITEM_PHOTO:
			return new ViewWrapperForPhoto(wrapperView, itemCode);
		}
		
		return null;
	}

	@Override
	protected void setRow(int itemCode, int position, WrapperView wrapperView) {

		int length = ResizeUtils.getSpecificLength(8);
		
		switch(itemCode) {
		
		case ZoneConstants.ITEM_POST:
		case ZoneConstants.ITEM_SCHEDULE:
		case ZoneConstants.ITEM_PHOTO:
			
			if(position % 2 == 0) {
				wrapperView.setPadding(length, 0, length/2, length);
			} else {
				wrapperView.setPadding(length/2, 0, length, length);
			}
			break;
			
		case ZoneConstants.ITEM_USER:
			
			if(position % 4 == 0) {
				wrapperView.setPadding(length, 0, length/2, length);
			} else if(position % 4 != 3) {
				wrapperView.setPadding(length/2, 0, length/2, length);
			} else {
				wrapperView.setPadding(length/2, 0, length, length);
			}
			break;
		}
	}

}
