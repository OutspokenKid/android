package com.zonecomms.common.adapters;

import java.util.ArrayList;

import com.outspoken_kid.model.BaseModel;
import com.zonecomms.common.views.WrapperView;
import com.zonecomms.napp.R;
import com.zonecomms.napp.classes.ViewWrapper;
import com.zonecomms.napp.classes.ZoneConstants;
import com.zonecomms.common.wrappers.ViewWrapperForGetheringList;
import com.zonecomms.common.wrappers.ViewWrapperForGetheringMemberList;
import com.zonecomms.common.wrappers.ViewWrapperForMessage;
import com.zonecomms.common.wrappers.ViewWrapperForMessageSample;
import com.zonecomms.common.wrappers.ViewWrapperForNotice;
import com.zonecomms.common.wrappers.ViewWrapperForPappList;

import android.content.Context;

public class ListAdapter extends ZoneAdapter {
	
	public ListAdapter(Context context, ArrayList<BaseModel> models, boolean useHardCache) {

		super(context, models, useHardCache);
	}

	@Override
	protected int getLayoutResIdByItemCode(int itemCode) {
		
		switch(itemCode) {
		
		case ZoneConstants.ITEM_NOTICE:
			return R.layout.list_notice;
		
		case ZoneConstants.ITEM_MESSAGE:
			return R.layout.list_message;
		
		case ZoneConstants.ITEM_MESSAGESAMPLE:
			return R.layout.list_messagesample;
			
		case ZoneConstants.ITEM_PAPP:
			return R.layout.list_papp;
			
		case ZoneConstants.ITEM_GETHERING:
			return R.layout.list_gethering;
			
		case ZoneConstants.ITEM_USER:
			return R.layout.list_member;
		}
		
		return 0;
	}

	@Override
	protected ViewWrapper getViewWrapperByItemCode(WrapperView wrapperView,
			int itemCode) {
		
		switch(itemCode) {
		
		case ZoneConstants.ITEM_NOTICE:
			return new ViewWrapperForNotice(wrapperView, itemCode);
			
		case ZoneConstants.ITEM_MESSAGE:
			return new ViewWrapperForMessage(wrapperView, itemCode);
			
		case ZoneConstants.ITEM_MESSAGESAMPLE:
			return new ViewWrapperForMessageSample(wrapperView, itemCode);
			
		case ZoneConstants.ITEM_PAPP:
			return new ViewWrapperForPappList(wrapperView, itemCode);
			
		case ZoneConstants.ITEM_GETHERING:
			return new ViewWrapperForGetheringList(wrapperView, itemCode);
			
		case ZoneConstants.ITEM_USER:
			return new ViewWrapperForGetheringMemberList(wrapperView, itemCode);
		}
		
		return null;
	}

	@Override
	protected void setRow(int itemCode, int position,
			WrapperView wrapperView) {

		if(itemCode == ZoneConstants.ITEM_NOTICE) {
			((ViewWrapperForNotice)wrapperView.getViewWrapper()).setBgColor(position);
		}
	}
}