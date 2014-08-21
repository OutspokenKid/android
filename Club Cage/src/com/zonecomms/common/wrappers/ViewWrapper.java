package com.zonecomms.common.wrappers;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.StringUtils;
import com.zonecomms.common.views.TitleBar;
import com.zonecomms.common.views.WrapperView;

public abstract class ViewWrapper {

	protected final int[] alphas = new int[] {
			76, 63, 50, 37, 24, 37, 50, 63
	};

	protected static int red, green, blue;
	
	protected WrapperView row;
	protected View bg;
	protected int itemCode;
	
	public ViewWrapper(WrapperView row, int itemCode) {
		setRow(row);
		setItemCode(itemCode);
		bindViews();
		setSizes();
		
		FontUtils.setGlobalFont(row);
	}

	public abstract void bindViews();
	public abstract void setSizes();
	public abstract void setValues(BaseModel baseModel);
	public abstract void setListeners();
	public abstract void setUnusableView();

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
	
	public void setBgColor(int position) {
		
		if(bg == null) {
			return;
		}

		try {
			int originalColor = TitleBar.titleBarColor;
			
			if(red == 0) {
				red = Color.red(originalColor);
			}
			
			if(green == 0) {
				green = Color.green(originalColor);
			}
			
			if(blue == 0) {
				blue = Color.blue(originalColor);
			}
			
			int alpha = alphas[position % 8];
			
			int newColor = Color.argb(alpha, red, green, blue);
			bg.setBackgroundColor(newColor);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	public void setImage(final ImageView ivImage, String url) {

		if(ivImage == null) {
			return;
		}
		
		if(url == null || url.length() == 0) {
			ivImage.setImageBitmap(null);
			ivImage.setVisibility(View.INVISIBLE);
			return;
		}

		ivImage.setVisibility(View.INVISIBLE);
		
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
