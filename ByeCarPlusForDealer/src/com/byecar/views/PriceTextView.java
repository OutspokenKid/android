package com.byecar.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplusfordealer.R;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class PriceTextView extends RelativeLayout {

	public static final int TYPE_CURRENT_BIG = 0;		//현재가(옥션상세).
	public static final int TYPE_CURRENT_SMALL = 1;		//현재가(입찰중, 내 입찰).
	public static final int TYPE_HIGHEST = 2;			//최고가(입찰 성공).
	public static final int TYPE_MY_BIDDING = 3;		//내 입찰가(모든 옥션리스트).
	public static final int TYPE_SUCCESS = 4;			//낙찰가(마이페이지 - 거래완료내역 - 바이카옥션).
	public static final int TYPE_SELLING_BIG = 5;		//판매가(중고상세).
	public static final int TYPE_SELLING_SMALL = 6;		//판매가(중고리스트).

	/**
	 * textViews[0] : 만원.
	 * textViews[1] : (가격).
	 * textViews[2] : 현재가/최고가/내 입찰가/낙찰가/판매가.
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
			rp.addRule(RelativeLayout.CENTER_VERTICAL);
			
			switch(i) {
			
			case 0:
				rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				textViews[i].setId(R.id.priceTextView_textView1);
				break;
				
			case 1:
				rp.addRule(RelativeLayout.LEFT_OF, R.id.priceTextView_textView1);
				FontUtils.setFontStyle(textViews[i], FontUtils.BOLD);
				textViews[i].setId(R.id.priceTextView_textView2);
				break;
				
			case 2:
				rp.addRule(RelativeLayout.LEFT_OF, R.id.priceTextView_textView2);
				break;
			}
			
			textViews[i].setLayoutParams(rp);
			this.addView(textViews[i]);
		}
		
		textViews[0].setText("만원");
	}
	
	public void setType(int type) {
		
		switch(type) {
		
//		public static final int TYPE_CURRENT_BIG = 0;		//현재가(옥션상세).
		case TYPE_CURRENT_BIG:
			setTextSize(true);
			textViews[2].setText(R.string.currentPrice);
			break;
			
//		public static final int TYPE_CURRENT_SMALL = 1;		//현재가(입찰중, 내 입찰).
		case TYPE_CURRENT_SMALL:
			setTextSize(false);
			textViews[2].setText(R.string.currentPrice);
			break;
			
//		public static final int TYPE_HIGHEST = 2;			//최고가(입찰 성공).
		case TYPE_HIGHEST:
			setTextSize(false);
			textViews[2].setText(R.string.highestPrice);
			break;
			
//		public static final int TYPE_MY_BIDDING = 3;		//내 입찰가(모든 옥션리스트).
		case TYPE_MY_BIDDING:
			setTextSize(false);
			textViews[2].setText(R.string.myBiddingPrice);
			break;
			
//		public static final int TYPE_SUCCESS = 4;			//낙찰가(마이페이지 - 거래완료내역 - 바이카옥션).
		case TYPE_SUCCESS:
			setTextSize(false);
			textViews[2].setText(R.string.biddingSuccessPrice);
			break;
			
//		public static final int TYPE_SELLING_BIG = 5;		//판매가(중고상세).
		case TYPE_SELLING_BIG:
			setTextSize(true);
			textViews[2].setText(R.string.salesPrice);
			break;
			
//		public static final int TYPE_SELLING_SMALL = 6;		//판매가(중고리스트).
		case TYPE_SELLING_SMALL:
			setTextSize(false);
			textViews[2].setText(R.string.salesPrice);
			break;
		}
		
		if(type == TYPE_MY_BIDDING) {
			setTextColor(getContext().getResources().getColor(R.color.holo_text));
		} else {
			setTextColor(getContext().getResources().getColor(R.color.new_color_text_orange));
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
	
	public void setTextSize(boolean isBig) {
		
		RelativeLayout.LayoutParams rp = null;
		
		if(isBig) {
			FontUtils.setFontSize(textViews[0], 20);
			textViews[0].setPadding(0, 0, 0, ResizeUtils.getSpecificLength(2));
			
			FontUtils.setFontSize(textViews[1], 32);
			rp = (RelativeLayout.LayoutParams) textViews[1].getLayoutParams();
			rp.rightMargin = ResizeUtils.getSpecificLength(6);
			
			FontUtils.setFontSize(textViews[2], 20);
			rp = (RelativeLayout.LayoutParams) textViews[2].getLayoutParams();
			rp.rightMargin = ResizeUtils.getSpecificLength(4);
			textViews[2].setPadding(0, 0, 0, ResizeUtils.getSpecificLength(3));
			
		//75%
		} else {
			FontUtils.setFontSize(textViews[0], 15);
			textViews[0].setPadding(0, 0, 0, ResizeUtils.getSpecificLength(2));
			
			FontUtils.setFontSize(textViews[1], 24);
			rp = (RelativeLayout.LayoutParams) textViews[1].getLayoutParams();
			rp.rightMargin = ResizeUtils.getSpecificLength(4);
			
			FontUtils.setFontSize(textViews[2], 15);
			rp = (RelativeLayout.LayoutParams) textViews[2].getLayoutParams();
			rp.rightMargin = ResizeUtils.getSpecificLength(3);
			textViews[2].setPadding(0, 0, 0, ResizeUtils.getSpecificLength(2));
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
