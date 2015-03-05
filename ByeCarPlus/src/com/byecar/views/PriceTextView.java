package com.byecar.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class PriceTextView extends RelativeLayout {

	public static final int TYPE_DETAIL_AUCTION = 0;
	public static final int TYPE_DETAIL_OTHERS = 1;
	public static final int TYPE_NORMAL_CAR = 2;

	/**
	 * 0 : 만원
	 * 1 : (가격)
	 * 2 : 판매가/현재가
	 */
	private TextView[] textViews;
	
	public PriceTextView(Context context) {
		this(context, null, 0);
	}
	
	public PriceTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public PriceTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}
	
	public void init() {
		
		textViews = new TextView[3];
		RelativeLayout.LayoutParams rp = null;
		
		for(int i=0; i<textViews.length; i++) {
			textViews[i] = new TextView(getContext());
			
			rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			rp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			
			switch(i) {
			
			case 0:
				rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				FontUtils.setFontSize(textViews[i], 20);
				textViews[i].setId(R.id.priceTextView_textView1);
				textViews[i].setPadding(0, 0, 0, ResizeUtils.getSpecificLength(6));
				break;
				
			case 1:
				rp.addRule(RelativeLayout.LEFT_OF, R.id.priceTextView_textView1);
				rp.rightMargin = ResizeUtils.getSpecificLength(6);
				FontUtils.setFontSize(textViews[i], 32);
				FontUtils.setFontStyle(textViews[i], FontUtils.BOLD);
				textViews[i].setId(R.id.priceTextView_textView2);
				break;
				
			case 2:
				rp.addRule(RelativeLayout.LEFT_OF, R.id.priceTextView_textView2);
				rp.rightMargin = ResizeUtils.getSpecificLength(4);
				FontUtils.setFontSize(textViews[i], 20);
				textViews[i].setPadding(0, 0, 0, ResizeUtils.getSpecificLength(6));
				break;
			}
			
			textViews[i].setLayoutParams(rp);
			this.addView(textViews[i]);
		}
		
		textViews[0].setText("만원");
	}
	
	public void setType(int type) {
		
		switch(type) {
		
		case TYPE_DETAIL_AUCTION:
			setTextColor(getContext().getResources().getColor(R.color.color_orange2));
			textViews[2].setText(R.string.currentPrice);
			break;
			
		case TYPE_DETAIL_OTHERS:
			setTextColor(getContext().getResources().getColor(R.color.color_orange2));
			textViews[2].setText(R.string.salesPrice);
			break;
			
		case TYPE_NORMAL_CAR:
			setTextColor(getContext().getResources().getColor(R.color.color_brown));
			textViews[2].setText(R.string.salesPrice);
			
			FontUtils.setFontSize(textViews[0], 18);
			textViews[0].setPadding(0, 0, 0, ResizeUtils.getSpecificLength(4));
			
			FontUtils.setFontSize(textViews[1], 24);
			FontUtils.setFontStyle(textViews[1], FontUtils.BOLD);
			
			FontUtils.setFontSize(textViews[2], 18);
			textViews[2].setPadding(0, 0, 0, ResizeUtils.getSpecificLength(4));
			break;
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
