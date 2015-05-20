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
	public static final int TYPE_MAIN_REVIEW = 2;
	public static final int TYPE_DETAIL_OTHERS = 3;
	public static final int TYPE_NORMAL_CAR = 4;
	public static final int TYPE_USED_CAR = 5;

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
		
		for(int i=0; i<textViews.length; i++) {
			textViews[i] = new TextView(getContext());
			ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 
					textViews[i], 1, Gravity.CENTER_VERTICAL, null);
			this.addView(textViews[i]);
		}
		
		FontUtils.setFontStyle(textViews[1], FontUtils.BOLD);
		textViews[2].setText(" 만원");
	}
	
	public void setType(int type) {
		
		switch(type) {
		
		case TYPE_DETAIL_AUCTION:
			setTextColor(getContext().getResources().getColor(R.color.new_color_text_orange));
			textViews[0].setText(R.string.currentPrice);
			setTextSize(22);
			break;
			
		case TYPE_DETAIL_OTHERS:
			setTextColor(getContext().getResources().getColor(R.color.new_color_text_orange));
			textViews[0].setText(R.string.salesPrice);
			setTextSize(22);
			break;
			
		case TYPE_MAIN_BIDDING:
			setTextColor(getContext().getResources().getColor(R.color.new_color_text_orange));
			textViews[0].setText(R.string.currentPrice);
			setTextSize(16);
			break;
			
		case TYPE_MAIN_REVIEW:
			setTextColor(getContext().getResources().getColor(R.color.new_color_text_orange));
			textViews[0].setText(R.string.biddingPrice);
			setTextSize(18);
			break;
			
		case TYPE_NORMAL_CAR:
			setTextColor(getContext().getResources().getColor(R.color.color_brown));
			textViews[0].setText(R.string.salesPrice);
			setTextSize(18);
			break;
			
		case TYPE_USED_CAR:
			setTextColor(getContext().getResources().getColor(R.color.new_color_text_orange));
			textViews[0].setText(null);
			setTextSize(22);
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

	public void setTextSize(float baseSize) {
		
		int fontSize_small = (int)baseSize;
		int fontSize_big = (int) (baseSize*1.6f);
		int padding = (int) (baseSize*0.3f);
		int margin = (int) (baseSize*0.3f);
		
		FontUtils.setFontSize(textViews[0], fontSize_small);
		ResizeUtils.setMargin(textViews[0], new int[]{0, 0, margin, 0});
		textViews[0].setPadding(0, padding, 0, 0);
		
		FontUtils.setFontSize(textViews[1], fontSize_big);
		
		FontUtils.setFontSize(textViews[2], fontSize_small);
		textViews[2].setPadding(0, padding, 0, 0);
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
