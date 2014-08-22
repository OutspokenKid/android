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
import com.zonecomms.clubvera.classes.ZonecommsApplication;
import com.zonecomms.common.views.WrapperView;
import com.zonecomms.common.wrappers.ViewWrapper;

/**
 * @author HyungGunKim
 *
 */
public abstract class ZoneAdapter extends BaseAdapter {

	private Context context;
	protected ArrayList<BaseModel> models;
	private ArrayList<View> hardCache = new ArrayList<View>();
	private boolean useHardCache;
	
	protected abstract int getLayoutResIdByItemCode(int itemCode);
	protected abstract ViewWrapper getViewWrapperByItemCode(WrapperView wrapperView, int itemCode);
	protected abstract void setRow(int itemCode, int position, WrapperView wrapperView);
	
	public ZoneAdapter(Context context, ArrayList<BaseModel> models, boolean useHardCache) {
		
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
		
		if(models == null || models.size() >= position) {
			return null;
		} else {
			return position;
		}
	}

	@Override
	public long getItemId(int position) {
		
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(position >= models.size()) {
			return getBlankView();
		}
		
		try {
			WrapperView wrapperView = null;
			ViewWrapper wrapper = null;
			BaseModel model = models.get(position);
			int itemCode = model.getItemCode();
			
			/**
			 * UserPage에서 사용.
			 * 무조건 생성, 뷰 통째로 캐쉬.
			 */
			if(useHardCache) {
				if(hardCache.size() > position) {
					return hardCache.get(position);
				}

				convertView = null;
			}
			
			if(convertView == null) {									//Create new Layout.
				wrapperView = (WrapperView) ZonecommsApplication.getActivity().
						getLayoutInflater().inflate(getLayoutResIdByItemCode(itemCode), null);
				wrapper = getViewWrapperByItemCode(wrapperView, itemCode);
				wrapper.setRow(wrapperView);
				
			} else if(convertView instanceof WrapperView) {				//Use convertView.
				wrapperView = (WrapperView) convertView;
				wrapper = wrapperView.getViewWrapper();
				wrapper.setRow(wrapperView);				
			} else {													//Maybe ListLoadingView.
				//It'll be filled.
			}
			
			setRow(itemCode, position, wrapperView);
			wrapper.setValues(model);
			wrapper.setListeners();
			
			if(useHardCache) {
				hardCache.add(wrapperView);
			}
			
			return wrapperView;
			
		} catch (Exception e) {
			LogUtils.trace(e);
			return getBlankView();
		} catch (Error e) {
			LogUtils.trace(e);
			return getBlankView();
		}
	}
	
	public void clearHardCache() {
		
		if(hardCache != null && useHardCache) {
			int size = hardCache.size();
			
			if(size == 0) {
				return;
			}
			
			for(int i=size-1; i<0; i--) {
				try {
					ViewUnbindHelper.unbindReferences(hardCache.get(i));
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}
			}
			hardCache.clear();
		}
	}
	
	public View getBlankView() {
		
		if(context != null) {
			View blankView = new View(context);
			blankView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, 0));
			return blankView;
			
		} else {
			return null;
		}
	}
}
