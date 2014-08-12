package com.outspoken_kid.views.holo.holo_dark;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.TextView;

public class HoloStyleTextView extends FrameLayout {

	private TextView textView;
	private Paint paint;
	private boolean isBgOn = true;
	
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

		setBackgroundColor(Color.BLACK);
		
		textView = new TextView(getContext());
		textView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		textView.setBackgroundColor(Color.TRANSPARENT);
		textView.setTextColor(Color.WHITE);
		textView.setHintTextColor(HoloConstants.COLOR_HOLO_TEXT_HINT);
		this.addView(textView);
	}
	
	public void setText(String text) {
		
		textView.setText(text);
	}
	
	public void setText(int textResId) {
		
		textView.setText(textResId);
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
		
		//가로
		canvas.drawLine(8, getMeasuredHeight() - 7, getMeasuredWidth() - 7, getMeasuredHeight() - 7, paint);
		canvas.drawLine(8, getMeasuredHeight() - 8, getMeasuredWidth() - 7, getMeasuredHeight() - 8, paint);

		//왼쪽
		canvas.drawLine(8, getMeasuredHeight() - 14, 8, getMeasuredHeight() - 7, paint);
		canvas.drawLine(9, getMeasuredHeight() - 14, 9, getMeasuredHeight() - 7, paint);
		
		//오른쪽
		canvas.drawLine(getMeasuredWidth() - 8, getMeasuredHeight() - 14, getMeasuredWidth() - 8, getMeasuredHeight() - 7, paint);
		canvas.drawLine(getMeasuredWidth() - 9, getMeasuredHeight() - 14, getMeasuredWidth() - 9, getMeasuredHeight() - 7, paint);
	}

	public boolean isBgOn() {
		return isBgOn;
	}

	public void setBgOn(boolean isBgOn) {
		this.isBgOn = isBgOn;
	}
}
