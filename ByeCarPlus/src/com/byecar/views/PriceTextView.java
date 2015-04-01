package com.byecar.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class PriceTextView extends LinearLayout {

	public static final int TYPE_DETAIL_AUCTION = 0;
	public static final int TYPE_MAIN_BIDDING = 1;
	public static final int TYPE_DETAIL_OTHERS = 2;
	public static final int TYPE_NORMAL_CAR = 3;
	public static final int TYPE_USED_CAR = 4;

	/**
	 * 0 : 만원
	 * 1 : (가격)
	 * 2 : 판매가/현재가
	 */
	private TextView[] textViews;
	
	public PriceTextView(Context context) {
		this(context, null);
	}
	
	public PriceTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public void init() {
		
		textViews = new TextView[3];
		
		FontUtils.setFontStyle(textViews[1], FontUtils.BOLD);
		
		for(int i=0; i<textViews.length; i++) {
			textViews[i] = new TextView(getContext());
			ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, textViews[i], 
					1, Gravity.CENTER_VERTICAL, i==0?new int[]{6, 0, 4, 0}:null);
			this.addView(textViews[i]);
		}
		
		textViews[2].setText(" 만원");
	}
	
	public void setType(int type) {
		
		switch(type) {
		
		case TYPE_DETAIL_AUCTION:
		case TYPE_DETAIL_OTHERS:
			setTextColor(getContext().getResources().getColor(R.color.color_orange2));
			
			if(type == TYPE_DETAIL_AUCTION) {
				textViews[0].setText(R.string.currentPrice);
			} else {
				textViews[0].setText(R.string.salesPrice);
			}
			
			FontUtils.setFontSize(textViews[0], 22);
			
			FontUtils.setFontSize(textViews[1], 28);
			textViews[1].setPadding(0, 0, 0, ResizeUtils.getSpecificLength(2));
			FontUtils.setFontStyle(textViews[1], FontUtils.BOLD);
			
			FontUtils.setFontSize(textViews[2], 22);
			break;
			
		case TYPE_MAIN_BIDDING:
			setTextColor(getContext().getResources().getColor(R.color.color_orange2));
			textViews[0].setText(R.string.currentPrice);
			
			FontUtils.setFontSize(textViews[0], 16);
			
			FontUtils.setFontSize(textViews[1], 24);
			textViews[1].setPadding(0, 0, 0, ResizeUtils.getSpecificLength(2));
			FontUtils.setFontStyle(textViews[1], FontUtils.BOLD);
			
			FontUtils.setFontSize(textViews[2], 16);
			break;
			
		case TYPE_NORMAL_CAR:
			setTextColor(getContext().getResources().getColor(R.color.color_brown));
			textViews[0].setText(R.string.salesPrice);
			
			FontUtils.setFontSize(textViews[0], 18);
			
			FontUtils.setFontSize(textViews[1], 24);
			textViews[1].setPadding(0, 0, 0, ResizeUtils.getSpecificLength(2));
			FontUtils.setFontStyle(textViews[1], FontUtils.BOLD);
			
			FontUtils.setFontSize(textViews[2], 18);
			break;
			
		case TYPE_USED_CAR:
			setTextColor(getContext().getResources().getColor(R.color.color_orange2));
			
			textViews[0].setText(null);
			FontUtils.setFontSize(textViews[0], 22);
			
			FontUtils.setFontSize(textViews[1], 28);
			textViews[1].setPadding(0, 0, 0, ResizeUtils.getSpecificLength(2));
			FontUtils.setFontStyle(textViews[1], FontUtils.BOLD);
			
			FontUtils.setFontSize(textViews[2], 22);
		}
	}
	
	public void setPrice(long price) {
		
		try {
			textViews[1].setText(StringUtils.getFormattedNumber(price/10000));
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void setTextColor(int color) {
		
		try {
			for(int i=0; i<textViews.length; i++) {
				textViews[i].setTextColor(color);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
}
