package com.zonecomms.common.adapters;

import java.util.ArrayList;

import android.content.Context;

import com.outspoken_kid.model.BaseModel;
import com.zonecomms.clubcubic.R;
import com.zonecomms.clubcubic.classes.ZoneConstants;
import com.zonecomms.common.views.WrapperView;
import com.zonecomms.common.wrappers.ViewWrapper;
import com.zonecomms.common.wrappers.ViewWrapperForMessage;
import com.zonecomms.common.wrappers.ViewWrapperForMessageSample;
import com.zonecomms.common.wrappers.ViewWrapperForMusic;
import com.zonecomms.common.wrappers.ViewWrapperForNotice;
import com.zonecomms.common.wrappers.ViewWrapperForVideo;

public class ListAdapter extends ZoneAdapter {

	public ListAdapter(Context context, ArrayList<BaseModel> models,
			boolean useHardCache) {
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
			
		case ZoneConstants.ITEM_VIDEO:
			return R.layout.list_video;
			
		case ZoneConstants.ITEM_MUSIC:
			return R.layout.list_music;
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
			
		case ZoneConstants.ITEM_VIDEO:
			return new ViewWrapperForVideo(wrapperView, itemCode);
			
		case ZoneConstants.ITEM_MUSIC:
			return new ViewWrapperForMusic(wrapperView, itemCode);
		}
		
		return null;
	}

	@Override
	protected void setRow(int itemCode, int position, WrapperView wrapperView) {

		if(itemCode == ZoneConstants.ITEM_NOTICE) {
			((ViewWrapperForNotice)wrapperView.getViewWrapper()).setBgColor(position);
		}
	}
}
