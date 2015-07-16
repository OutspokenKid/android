package com.byecar.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.byecar.byecarplusfordealer.R;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class PriceTextView extends LinearLayout {

	public static final int TYPE_CURRENT_BIG = 0;		//현재가(옥션상세).
	public static final int TYPE_CURRENT_SMALL = 1;		//현재가(입찰중, 내 입찰).
	public static final int TYPE_HIGHEST = 2;			//최고가(입찰 성공).
	public static final int TYPE_MY_BIDDING = 3;		//내 입찰가(모든 옥션리스트).
	public static final int TYPE_SUCCESS = 4;			//낙찰가(마이페이지 - 거래완료내역 - 바이카옥션).
	public static final int TYPE_SELLING_BIG = 5;		//판매가(중고상세).
	public static final int TYPE_SELLING_SMALL = 6;		//판매가(중고리스트).
	public static final int TYPE_BIDDING_FINISH = 7;	//최종 낙찰가(옥션리스트, 상세).
	public static final int TYPE_BIDDING_FAIL = 8;		//최고가(옥션리스트, 상세).

	/**
	 * textViews[0] : 만원.
	 * textViews[1] : (가격).
	 * textViews[2] : 현재가/최고가/내 입찰가/낙찰가/판매가.
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
		
//		public static final int TYPE_CURRENT_BIG = 0;		//현재가(옥션상세).
		case TYPE_CURRENT_BIG:
			setTextColor(getContext().getResources().getColor(R.color.new_color_text_orange));
			textViews[0].setText(R.string.currentPrice);
			setTextSize(22);
			break;
			
//		public static final int TYPE_CURRENT_SMALL = 1;		//현재가(입찰중, 내 입찰).
		case TYPE_CURRENT_SMALL:
			setTextColor(getContext().getResources().getColor(R.color.new_color_text_orange));
			textViews[0].setText(R.string.currentPrice);
			setTextSize(15);
			break;
			
//		public static final int TYPE_HIGHEST = 2;			//최고가(입찰 성공).
		case TYPE_HIGHEST:
			setTextColor(getContext().getResources().getColor(R.color.new_color_text_orange));
			textViews[0].setText(R.string.highestPrice);
			setTextSize(15);
			break;
			
//		public static final int TYPE_MY_BIDDING = 3;		//내 입찰가(모든 옥션리스트).
		case TYPE_MY_BIDDING:
			setTextColor(getContext().getResources().getColor(R.color.color_brown));
			textViews[0].setText(R.string.myBiddingPrice);
			setTextSize(15);
			break;
			
//		public static final int TYPE_SUCCESS = 4;			//낙찰가(마이페이지 - 거래완료내역 - 바이카옥션).
		case TYPE_SUCCESS:
			setTextColor(getContext().getResources().getColor(R.color.new_color_text_orange));
			textViews[0].setText(R.string.biddingSuccessPrice);
			setTextSize(15);
			break;
			
//		public static final int TYPE_SELLING_BIG = 5;		//판매가(중고상세).
		case TYPE_SELLING_BIG:
			setTextColor(getContext().getResources().getColor(R.color.new_color_text_orange));
			textViews[0].setText(R.string.salesPrice);
			setTextSize(22);
			break;
			
//		public static final int TYPE_SELLING_SMALL = 6;		//판매가(중고리스트).
		case TYPE_SELLING_SMALL:
			setTextColor(getContext().getResources().getColor(R.color.new_color_text_orange));
			textViews[0].setText(R.string.salesPrice);
			setTextSize(15);
			break;
			
		case TYPE_BIDDING_FINISH:
			setTextColor(getContext().getResources().getColor(R.color.new_color_text_orange));
			textViews[0].setText(R.string.finalPrice);
			setTextSize(22);
			break;
			
		case TYPE_BIDDING_FAIL:
			setTextColor(getContext().getResources().getColor(R.color.new_color_text_orange));
			textViews[0].setText(R.string.highestPrice);
			setTextSize(22);
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
