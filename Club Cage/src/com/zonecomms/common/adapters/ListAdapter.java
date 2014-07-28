package com.zonecomms.common.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import com.outspoken_kid.classes.ViewUnbindHelper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;
import com.zonecomms.clubcage.MainActivity;
import com.zonecomms.clubcage.R;
import com.zonecomms.clubcage.classes.ZoneConstants;
import com.zonecomms.common.views.WrapperView;
import com.zonecomms.common.wrappers.ViewWrapper;
import com.zonecomms.common.wrappers.ViewWrapperForMessage;
import com.zonecomms.common.wrappers.ViewWrapperForMessageSample;
import com.zonecomms.common.wrappers.ViewWrapperForMusic;
import com.zonecomms.common.wrappers.ViewWrapperForNotice;
import com.zonecomms.common.wrappers.ViewWrapperForVideo;

public class ListAdapter extends BaseAdapter {

	private Context context;
	MainActivity mainActivity;
	private ArrayList<BaseModel> models;
	private ArrayList<View> hardCache = new ArrayList<View>();
	private boolean useHardCache;
	
	private final int[] LAYOUT_RESIDS = new int[] {
									0,
									0,
									R.layout.list_notice,
									0,
									R.layout.list_video,
									0,
									0,
									R.layout.list_notice,		//공지사항과 같다.
									R.layout.list_music,
									R.layout.list_message,
									R.layout.list_messagesample,
								};
	
	public ListAdapter(Context context, MainActivity mainActivity, ArrayList<BaseModel> models,
			boolean useHardCache) {
		this.context = context;
		this.mainActivity = mainActivity;
		this.models = models;
		this.useHardCache = useHardCache;
	}
	
	@Override
	public int getCount() {
		
		return models.size();
	}

	@Override
	public Object getItem(int position) {

		if(models.size() > position) {
			return models.get(position);
		} else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
	
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(position >= models.size()) {
			return null;
		}
		
		try {
			WrapperView viewForWrapper = null;
			ViewWrapper viewWrapper = null;
			BaseModel model = models.get(position);
			int itemCode = model.getItemCode();
			
			/**
			 * UserPage가 빡치게 해서 만듬.
			 * 무조건 생성, 뷰 통째로 캐쉬.
			 */
			if(useHardCache) {
				if(hardCache.size() > position) {
					return hardCache.get(position);
				}

				convertView = null;
			}
			
			if(convertView == null) {									//Create new custom Layout by itemCode.
				viewForWrapper = (WrapperView) mainActivity.getLayoutInflater().inflate(LAYOUT_RESIDS[itemCode], null);
				
				switch(itemCode) {
				
				case ZoneConstants.ITEM_NOTICE:
				case ZoneConstants.ITEM_EVENT:
					viewWrapper = new ViewWrapperForNotice(viewForWrapper, itemCode);
					break;
				case ZoneConstants.ITEM_VIDEO:
					viewWrapper = new ViewWrapperForVideo(viewForWrapper, itemCode);
					break;
				case ZoneConstants.ITEM_MUSIC:
					viewWrapper = new ViewWrapperForMusic(viewForWrapper, itemCode);
					break;
				case ZoneConstants.ITEM_MESSAGE:
					viewWrapper = new ViewWrapperForMessage(viewForWrapper, itemCode);
					break;
				case ZoneConstants.ITEM_MESSAGESAMPLE:
					viewWrapper = new ViewWrapperForMessageSample(viewForWrapper, itemCode);
					break;
				}
				viewWrapper.setRow(viewForWrapper);
				
			} else if(convertView instanceof WrapperView) {				//Use convertView.
				viewForWrapper = (WrapperView) convertView;
				viewWrapper = viewForWrapper.getViewWrapper();
				viewWrapper.setRow(viewForWrapper);
				
			} else {													//Maybe ListLoadingView.
				//It'll be filled.
			}
			
			if(itemCode == ZoneConstants.ITEM_NOTICE
					|| itemCode == ZoneConstants.ITEM_EVENT
					|| itemCode == ZoneConstants.ITEM_MUSIC) {
				viewWrapper.setBgColor(position);
			}
			
			viewWrapper.setValues(model);
			viewWrapper.setListeners();
			
			if(useHardCache) {
				hardCache.add(viewForWrapper);
			}
			
			return viewForWrapper;
		} catch(Exception e) {
			LogUtils.trace(e);
			return getBlankView();
		}		
	}

	public View getBlankView() {
		
		View blankView = new View(context);
		blankView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, 0));
		return blankView;
	}

	public void clearHardCache() {
		
		if(hardCache != null && useHardCache) {
			
			try {
				int length = hardCache.size();
				for(int i=0; i<length; i++) {
					ViewUnbindHelper.unbindReferences(hardCache.get(i));
				}
				hardCache.clear();
			} catch(Exception e) {
			}
		}
	}

}