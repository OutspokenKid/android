package com.zonecomms.clubcage.classes;

import android.view.View;
import android.widget.ImageView;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.StringUtils;
import com.zonecomms.common.wrapperviews.WrapperView;

public abstract class ViewWrapper {

	protected WrapperView row;
	protected View bg;
	protected int itemCode;
	
	public ViewWrapper(WrapperView row, int itemCode) {
		setRow(row);
		setItemCode(itemCode);
		bindViews();
		setSizes();
	}

	public abstract void bindViews();
	public abstract void setSizes();
	public abstract void setValues(BaseModel baseModel);
	public abstract void setListeners();
	public abstract void downloadImageImmediately(String url, String key, ImageView ivImage, int size); 

	public WrapperView getRow() {
		
		return row;
	}
	
	public void setRow(WrapperView row) {

		this.row = row;
		row.setViewWrapper(this);
	}
	
	public int getItemCode() {
		
		return itemCode;
	}
	
	public void setItemCode(int itemCode) {
		
		this.itemCode = itemCode;
	}
	
	public void setImage(ImageView ivImage, String url, String key, int size) {

		if(ivImage == null) {
			return;
		}
		
		boolean hasUrl = !StringUtils.isEmpty(url);
		boolean hasTag = (ivImage.getTag() != null);
		boolean same = hasUrl && hasTag && ivImage.getTag().toString().equals(url);
		
		if(hasUrl) {

			if(same) {
				ivImage.setVisibility(View.VISIBLE);
			} else {
				ivImage.setImageBitmap(null);
				ivImage.setVisibility(View.INVISIBLE);
			}
			
			downloadImageImmediately(url, key, ivImage, size);
		} else {
			ivImage.setVisibility(View.INVISIBLE);
			ivImage.setImageBitmap(null);
			ivImage.setTag(null);
		}
	}
}
