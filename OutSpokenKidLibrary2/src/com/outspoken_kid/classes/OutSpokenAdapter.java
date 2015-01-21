package com.outspoken_kid.classes;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

public abstract class OutSpokenAdapter extends BaseAdapter {

	protected Context context;
	protected LayoutInflater inflater;
	protected ArrayList<BaseModel> models;
	protected View firstView;
	
	public OutSpokenAdapter(Context context, LayoutInflater inflater, ArrayList<BaseModel> models) {
		
		this.context = context;
		this.inflater = inflater;
		this.models = models;
	}

	public abstract ViewWrapper getViewWrapperByItemCode(View convertView, int itemCode);
	public abstract int getLayoutResIdByItemCode(int itemCode);
	public abstract void setRow(int itemCode, int position, View convertView);
	
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
			
			if(convertView == null) {
				convertView = inflater.inflate(getLayoutResIdByItemCode(itemCode), null);
				wrapper = getViewWrapperByItemCode(convertView, itemCode);
				wrapper.setRow(convertView);
			} else {
				wrapper = (ViewWrapper) convertView.getTag();
				wrapper.setRow(convertView);
			}
			
			setRow(itemCode, position, convertView);
			wrapper.setValues(model);
			wrapper.setListeners();
			
			if(position == 0) {
				firstView = convertView;
			}
			
			return convertView;
		} catch (Exception e) {
			LogUtils.trace(e);
			return getBlankView();
		} catch (Error e) {
			LogUtils.trace(e);
			return getBlankView();
		}
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

	public View getFirstView() {
		
		return firstView;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		
		if(models.size() == 0) {
			firstView = null;
		}
	}
}
