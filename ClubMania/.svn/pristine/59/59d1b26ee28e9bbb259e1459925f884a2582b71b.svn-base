package com.zonecomms.common.adapters;

import java.util.ArrayList;

import com.outspoken_kid.classes.ApplicationManager;
import com.outspoken_kid.classes.ViewUnbindHelper;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.zonecomms.clubmania.R;
import com.zonecomms.clubmania.classes.ZoneConstants;
import com.zonecomms.common.models.BaseModel;
import com.zonecomms.common.wrappers.ViewWrapper;
import com.zonecomms.common.wrappers.ViewWrapperForMember;
import com.zonecomms.common.wrappers.ViewWrapperForPapp;
import com.zonecomms.common.wrappers.ViewWrapperForPhoto;
import com.zonecomms.common.wrappers.ViewWrapperForPost;
import com.zonecomms.common.wrappers.ViewWrapperForSchedule;
import com.zonecomms.common.wrapperviews.WrapperView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

public class GridAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<BaseModel> models;
	private ArrayList<View> hardCache = new ArrayList<View>();
	private boolean useHardCache;
	
	private final int[] LAYOUT_RESIDS = new int[] {
									R.layout.grid_post,
									R.layout.grid_schedule,
									R.layout.grid_member,
									R.layout.grid_photo,
									R.layout.grid_app
								};
	
	public GridAdapter(Context context, ArrayList<BaseModel> models, boolean useHardCache) {

		this.context = context;
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
				viewForWrapper = (WrapperView) ApplicationManager.getInstance().getMainActivity().
						getLayoutInflater().inflate(LAYOUT_RESIDS[itemCode], null);
				
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
				case ZoneConstants.ITEM_PAPP:
					viewWrapper = new ViewWrapperForPapp(viewForWrapper, itemCode);
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
			viewWrapper.setListener();
			
			if(useHardCache) {
				hardCache.add(viewForWrapper);
			}
			
			return viewForWrapper;
		} catch(Exception e) {
			e.printStackTrace();
			return getBlankView();
		} catch(OutOfMemoryError oom) {
			oom.printStackTrace();
			ToastUtils.showToast(R.string.notEnoughMemory);
			ApplicationManager.clearFragmentsWithoutMain();
			return getBlankView();
		}
	}
	
	public View getBlankView() {
		
		View blankView = new View(context);
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
