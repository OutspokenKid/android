package com.outspoken_kid.views.holo.holo_light;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.views.holo.HoloConstants;

public class HoloStyleTextView extends FrameLayout {

	private TextView textView;
	private Paint paint;
	private boolean isBgOn = true;
	private int length;
	private int padding;
	
	public HoloStyleTextView(Context context) {
		this(context, null, 0);
	}
	
	public HoloStyleTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public HoloStyleTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public void init() {

		//QHD
		if(ResizeUtils.getScreenWidth() >= 1440) {
			length = 6;
			padding = 18;
			
		//Full HD
		} else if(ResizeUtils.getScreenWidth() >= 1080) {
			length = 4;
			padding = 16;
			
		//HD
		} else if(ResizeUtils.getScreenWidth() >= 720) {
			length = 3;
			padding = 9;
			
		} else {
			length = 2;
			padding = 8;
		}
		
		setBackgroundColor(Color.TRANSPARENT);
		
		textView = new TextView(getContext());
		textView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		textView.setBackgroundColor(Color.TRANSPARENT);
		textView.setTextColor(HoloConstants.COLOR_HOLO_TEXT);
		textView.setHintTextColor(HoloConstants.COLOR_HOLO_TEXT_HINT);
		this.addView(textView);
	}
	
	public void setText(String text) {
		
		textView.setText(text);
	}
	
	public void setText(int textResId) {
		
		textView.setText(textResId);
	}
	
	public void setTextColor(int color) {
		
		textView.setTextColor(color);
	}
	
	public void setGravity(int gravity) {
		
		textView.setGravity(gravity);
	}
	
	public void setMaxWidth(int width) {
		
		textView.setMaxWidth(width);
	}
	
	public void setMaxLines(int maxlines) {
		
		textView.setMaxLines(maxlines);
	}
	
	public void setEllipsize(TruncateAt where) {
		
		textView.setEllipsize(where);
	}
	
	public void setHint(String hint) {
		
		textView.setHint(hint);
	}
	
	public void setHint(int hint) {
		
		textView.setHint(hint);
	}
	
	public void setSingleLine() {
		
		textView.setSingleLine();
	}
	
	public TextView getTextView() {
		
		return textView;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		if(paint == null) {
			paint = new Paint();
			paint.setColor(isBgOn?HoloConstants.COLOR_HOLO_TARGET_ON:HoloConstants.COLOR_HOLO_TARGET_OFF);
		}

		for(int i=0; i<length; i++) {
			
			//padding = 8, i = 0,1
			
			//가로.
			canvas.drawLine(padding + length, 
					getMeasuredHeight() - padding - i, 
					getMeasuredWidth() - padding - length, 
					getMeasuredHeight() - padding - i, paint);
			
			//10, h-9, w-10, h-9
			//10, h-8, w-10, h-8
			
			//왼쪽.
			canvas.drawLine(padding + i, 
					getMeasuredHeight() - padding * 2, 
					padding + i, 
					getMeasuredHeight() - (padding - 1) , paint);
			
			//8, h-16, 8, h-8
			//9, h-16, 8, h-8
			
			//오른쪽.
			canvas.drawLine(getMeasuredWidth() - (padding + 1) - i, 
					getMeasuredHeight() - padding * 2, 
					getMeasuredWidth() - (padding + 1) - i, 
					getMeasuredHeight() - (padding - 1), paint);
			
			//w-8, h-16, w-8, h-8
			//w-9, h-16, w-9, h-8
		}
		
//		//가로
//		canvas.drawLine(8, getMeasuredHeight() - 7, getMeasuredWidth() - 7, getMeasuredHeight() - 7, paint);
//		canvas.drawLine(8, getMeasuredHeight() - 8, getMeasuredWidth() - 7, getMeasuredHeight() - 8, paint);
//
//		//왼쪽
//		canvas.drawLine(8, getMeasuredHeight() - 14, 8, getMeasuredHeight() - 7, paint);
//		canvas.drawLine(9, getMeasuredHeight() - 14, 9, getMeasuredHeight() - 7, paint);
//		
//		//오른쪽
//		canvas.drawLine(getMeasuredWidth() - 8, getMeasuredHeight() - 14, getMeasuredWidth() - 8, getMeasuredHeight() - 7, paint);
//		canvas.drawLine(getMeasuredWidth() - 9, getMeasuredHeight() - 14, getMeasuredWidth() - 9, getMeasuredHeight() - 7, paint);
	}

	public boolean isBgOn() {
		return isBgOn;
	}

	public void setBgOn(boolean isBgOn) {
		this.isBgOn = isBgOn;
	}
}
