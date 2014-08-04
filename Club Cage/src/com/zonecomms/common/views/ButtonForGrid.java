package com.zonecomms.common.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.zonecomms.common.models.GridMenu;

public class ButtonForGrid extends FrameLayout {
	
	private View viewForImage;
	private TextView tvTitle;
	private ImageView ivImage;

	public ButtonForGrid(Context context) {
		this(context, null, 0);
	}
	
	public ButtonForGrid(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public ButtonForGrid(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setText(int resId) {
		
		try {
			if(tvTitle != null) {
				tvTitle.setText(resId);
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void setTextSize(int size) {
		
		if(tvTitle != null && size > 10) {
			FontUtils.setFontSize(tvTitle, size);
		}
	}
	
	public TextView getTextView() {
		
		return tvTitle;
	}
	
	public void setIcon(int resId) {
		
		try {
			viewForImage.setBackgroundResource(resId);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public View getViewForImage() {
		
		return viewForImage;
	}
	
	public void setButtonByMenu(GridMenu gridMenu) {
		
		if(gridMenu == null) {
			return;
		}
		
		if(gridMenu.getBaseColor() != null) {
			try {
				int[] baseColor = gridMenu.getBaseColor();
				
				if(baseColor != null) {
					int newR = (int)(baseColor[0] * Math.pow(0.8f, gridMenu.getColorLevel()));
					int newG = (int)(baseColor[1] * Math.pow(0.8f, gridMenu.getColorLevel()));
					int newB = (int)(baseColor[2] * Math.pow(0.8f, gridMenu.getColorLevel()));
					this.setBackgroundColor(Color.rgb(newR, newG, newB));
				}
			} catch(Exception e) {
				LogUtils.trace(e);
			}
		}
		
		boolean centerText = true;
		
		if(!StringUtils.isEmpty(gridMenu.getImageUrl())) {

			try {
				centerText = false;
				ivImage = new ImageView(getContext());
				ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, ivImage, 
						2, Gravity.CENTER, new int[]{8, 8, 8, 8});
				ivImage.setScaleType(ScaleType.FIT_XY);
				this.addView(ivImage);

				try {
					//Set onClickListener.
					ToastUtils.showToast("menuClicked : " + gridMenu.getUri());
				} catch(Exception e) {
					LogUtils.trace(e);
				}
				return;
			} catch(Exception e) {
				LogUtils.trace(e);
			}
		}
		
		if(!StringUtils.isEmpty(gridMenu.getUri())) {
			try {
				//Set onClickListener.
				ToastUtils.showToast("menuClicked : " + gridMenu.getUri());
			} catch(Exception e) {
				LogUtils.trace(e);
			}
		}
		
		if(gridMenu.getIconResId() != 0) {
			try {
				centerText = false;
				viewForImage = new View(getContext());
				ResizeUtils.viewResize(110, 110, viewForImage, 2, Gravity.LEFT, null);
				viewForImage.setBackgroundResource(gridMenu.getIconResId());
				this.addView(viewForImage);
			} catch(Exception e) {
				LogUtils.trace(e);
			}
		}
		
		if(!StringUtils.isEmpty(gridMenu.getIconUrl())) {
			try {
				//Download icon.
				centerText = false;
				viewForImage = new View(getContext());
				ResizeUtils.viewResize(110, 110, viewForImage, 2, Gravity.LEFT, null);
				this.addView(viewForImage);
			} catch(Exception e) {
				LogUtils.trace(e);
			}
		}
		
		if(!StringUtils.isEmpty(gridMenu.getText())) {
			try {
				tvTitle = new TextView(getContext());
				ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, tvTitle, 
						2, 0, null, new int[]{20, 20, 20, 20});
				tvTitle.setTextColor(Color.WHITE);
				tvTitle.setMaxLines(3);
				tvTitle.setText(gridMenu.getText());
				this.addView(tvTitle);
				
				if(centerText) {
					tvTitle.setGravity(Gravity.CENTER);

					if(!gridMenu.isGroup()) {
						FontUtils.setFontSize(tvTitle, 32);
					} else if(gridMenu.getText().length() >= 8){
						FontUtils.setFontSize(tvTitle, 42);
					} else if(gridMenu.getText().length() >= 4){
						FontUtils.setFontSize(tvTitle, 52);
					} else {
						FontUtils.setFontSize(tvTitle, 62);
					}
				} else {
					tvTitle.setGravity(Gravity.RIGHT|Gravity.BOTTOM);
					FontUtils.setFontSize(tvTitle, 22);
				}
			} catch(Exception e) {
				LogUtils.trace(e);
			}
		}
	}
}
