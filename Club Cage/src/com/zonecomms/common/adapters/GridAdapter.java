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
import com.outspoken_kid.utils.ResizeUtils;
import com.zonecomms.clubcage.MainActivity;
import com.zonecomms.clubcage.R;
import com.zonecomms.clubcage.classes.ZoneConstants;
import com.zonecomms.common.views.WrapperView;
import com.zonecomms.common.wrappers.ViewWrapper;
import com.zonecomms.common.wrappers.ViewWrapperForMember;
import com.zonecomms.common.wrappers.ViewWrapperForPhoto;
import com.zonecomms.common.wrappers.ViewWrapperForPost;
import com.zonecomms.common.wrappers.ViewWrapperForSchedule;

public class GridAdapter extends BaseAdapter {

	private MainActivity mainActivity;
	private ArrayList<BaseModel> models;
	private ArrayList<View> hardCache = new ArrayList<View>();
	private boolean useHardCache;
	
	private final int[] LAYOUT_RESIDS = new int[] {
									0,
									R.layout.grid_post,
									0,
									R.layout.grid_schedule,
									0,
									R.layout.grid_member,
									R.layout.grid_photo
								};
	
	public GridAdapter(Context context, MainActivity mainActivity, ArrayList<BaseModel> models, boolean useHardCache) {

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
			
			if(convertView == null) {									//Create new Layout.
				viewForWrapper = (WrapperView) mainActivity.getLayoutInflater().inflate(LAYOUT_RESIDS[itemCode], null);
				
				switch(itemCode) {
				
				case ZoneConstants.ITEM_POST:
					viewWrapper = new ViewWrapperForPost(viewForWrapper, itemCode);
					break;
				case ZoneConstants.ITEM_SCHEDULE:
					viewWrapper = new ViewWrapperForSchedule(viewForWrapper, itemCode);
					break;
				case ZoneConstants.ITEM_USER:
					viewWrapper = new ViewWrapperForMember(viewForWrapper, itemCode);
					break;
				case ZoneConstants.ITEM_PHOTO:
					viewWrapper = new ViewWrapperForPhoto(viewForWrapper, itemCode);
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
			
			setRowPadding(itemCode, position, viewForWrapper);
			viewWrapper.setValues(model);
			viewWrapper.setListeners();
			
			if(useHardCache) {
				hardCache.add(viewForWrapper);
			}
			
			return viewForWrapper;
		} catch(Exception e) {
			e.printStackTrace();
			return getBlankView();
		} catch(OutOfMemoryError oom) {
			oom.printStackTrace();
			return getBlankView();
		}
	}
	
	public View getBlankView() {
		
		View blankView = new View(mainActivity);
		blankView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, 0));
		return blankView;
	}
	
	public void setRowPadding(int itemCode, int position, View viewForWrapper) {
		int length = ResizeUtils.getSpecificLength(8);
		
		switch(itemCode) {
		case ZoneConstants.ITEM_POST:
		case ZoneConstants.ITEM_SCHEDULE:
		case ZoneConstants.ITEM_PHOTO:
			if(position % 2 == 0) {
				viewForWrapper.setPadding(length, 0, length/2, length);
			} else {
				viewForWrapper.setPadding(length/2, 0, length, length);
			}
			break;
		case ZoneConstants.ITEM_USER:
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
