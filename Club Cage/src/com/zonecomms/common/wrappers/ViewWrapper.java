package com.zonecomms.common.wrappers;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.zonecomms.common.views.WrapperView;

public abstract class ViewWrapper {

	private static final int lowColor = 65;
	private static final int highColor = 125;
	private static final int difference = 10;
	
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
			int newColor = 0;
			
			/**		/6		%6		nc
			 * 0	0		0		65
			 * 1	0		1		75
			 * 2	0		2		85
			 * 3	0		3		95
			 * 4	0		4		105
			 * 5	0		5		115
			 * 6	1		0		125
			 * 7	1		1		115
			 * 8	1		2		105
			 * 9	1		3		95
			 * 10	1		4		85
			 * 11	1		5		75
			 * 12	2		0		65
			 * 13	2		1		75
			 * 14	2		2		85
			 * 15	2		3		95
			 */
			if( (position/6) %2 == 0) {
				newColor = highColor - (position%6) * difference;		// highColor ~ (highColor - 50) = 125 ~ 75
			} else {
				newColor = lowColor + (position%6) * difference;		// lowColor ~ (lowColor + 50) = 65 ~ 115
			}
			
			bg.setBackgroundColor(Color.rgb(newColor, newColor, newColor));
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void setImage(ImageView ivImage, String url) {

		if(ivImage == null) {
			return;
		}

		ivImage.setVisibility(View.INVISIBLE);
		
		ivImage.setTag(url);
		DownloadUtils.downloadBitmap(url, ivImage, new OnBitmapDownloadListener() {
			
			@Override
			public void onError(String url, ImageView ivImage) {
			}
			
			@Override
			public void onCompleted(String url, ImageView ivImage, Bitmap bitmap) {

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
