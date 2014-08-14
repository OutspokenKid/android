package com.cmons.cph.classes;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import com.cmons.cph.R;
import com.cmons.cph.wrappers.ViewWrapperForCategory;
import com.cmons.cph.wrappers.ViewWrapperForFloor;
import com.cmons.cph.wrappers.ViewWrapperForLine;
import com.cmons.cph.wrappers.ViewWrapperForNotice;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

/**
 * @author HyungGunKim
 *
 */
public class CphAdapter extends BaseAdapter {

	protected Context context;
	protected LayoutInflater inflater;
	protected ArrayList<BaseModel> models;
	
	public CphAdapter(Context context, LayoutInflater inflater, ArrayList<BaseModel> models) {
		
		this.context = context;
		this.models = models;
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
			ViewWrapper wrapper = null;
			BaseModel model = models.get(position);
			int itemCode = model.getItemCode();
			
			if(convertView == null) {									//Create new Layout.
				convertView = inflater.inflate(getLayoutResIdByItemCode(itemCode), null);
				wrapper = getViewWrapperByItemCode(convertView, itemCode);
				wrapper.setRow(convertView);
				
			} else {													//Maybe ListLoadingView.

				wrapper = (ViewWrapper) convertView.getTag();
				wrapper.setRow(convertView);
			}
			
			setRow(itemCode, position, convertView);
			wrapper.setValues(model);
			wrapper.setListeners();
			
			return convertView;
			
		} catch (Exception e) {
			LogUtils.trace(e);
			return getBlankView();
		} catch (Error e) {
			LogUtils.trace(e);
			return getBlankView();
		}
	}

	public int getLayoutResIdByItemCode(int itemCode) {
		
		switch (itemCode) {
		
		case CphConstants.ITEM_NOTICE:
			return R.layout.list_wholesale_notice;
			
		case CphConstants.ITEM_CATEGORY:
			return R.layout.list_signup_category;
			
		case CphConstants.ITEM_FLOOR:
			return R.layout.list_floor;
			
		case CphConstants.ITEM_LINE:
			return R.layout.list_line;
		}
		
		return 0;
	}
	
	public ViewWrapper getViewWrapperByItemCode(View convertView, int itemCode) {
		
		switch (itemCode) {
		
		case CphConstants.ITEM_NOTICE:
			return new ViewWrapperForNotice(convertView, itemCode);
			
		case CphConstants.ITEM_CATEGORY:
			return new ViewWrapperForCategory(convertView, itemCode);
			
		case CphConstants.ITEM_FLOOR:
			return new ViewWrapperForFloor(convertView, itemCode);
			
		case CphConstants.ITEM_LINE:
			return new ViewWrapperForLine(convertView, itemCode);
		}
		
		return null;
	}
	
	public void setRow(int itemCode, int position, View convertView) {
		
	}

	public View getBlankView() {
		
		if(context == null) {
			View blankView = new View(context);
			blankView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, 0));
			return blankView;
			
		} else {
			return null;
		}
	}
}
