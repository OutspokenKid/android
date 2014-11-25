package com.outspoken_kid.classes;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.StringUtils;

public abstract class ViewWrapper {
	
	protected View row;
	protected int itemCode;
	
	public ViewWrapper(View row, int itemCode) {
		setRow(row);
		setItemCode(itemCode);
		bindViews();
		setSizes();
		
		if(row instanceof ViewGroup) {
			FontUtils.setGlobalFont((ViewGroup)row);
		}
	}

	public abstract void bindViews();
	public abstract void setSizes();
	public abstract void setValues(BaseModel baseModel);
	public abstract void setListeners();
	public abstract void setUnusableView();

	public View getRow() {
		
		return row;
	}
	
	public void setRow(View row) {

		this.row = row;
		row.setTag(this);
	}
	
	public int getItemCode() {
		
		return itemCode;
	}
	
	public void setItemCode(int itemCode) {
		
		this.itemCode = itemCode;
	}

	public void setImage(final ImageView ivImage, String url) {

		if(ivImage == null || url == null || url.length() == 0) {
			return;
		}

		if(ivImage.getTag() != null && url.equals(ivImage.getTag().toString())) {
			//Do nothing because of same image is already set.
			return;
		} else {
			ivImage.setImageDrawable(null);
		}
		
		ivImage.setTag(url);
		DownloadUtils.downloadBitmap(url, new OnBitmapDownloadListener() {
			
			@Override
			public void onError(String url) {
			}
			
			@Override
			public void onCompleted(String url, Bitmap bitmap) {

				try {
					String tag = ivImage.getTag().toString();
					
					//태그가 다른 경우 아무 것도 하지 않음.
					if(!StringUtils.isEmpty(tag)
							&& tag.equals(url)) {
						
						if(ivImage != null) {
							ivImage.setImageBitmap(bitmap);
							ivImage.setVisibility(View.VISIBLE);
						}
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}
			}
		});
	}
}
