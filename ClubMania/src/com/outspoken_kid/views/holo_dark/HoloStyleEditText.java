package com.outspoken_kid.views.holo_dark;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

public class HoloStyleEditText extends FrameLayout {

	private EditText editText;
	
	private Paint paint, paint2;

	public HoloStyleEditText(Context context) {
		this(context, null, 0);
	}
	
	public HoloStyleEditText(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public HoloStyleEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		
		setBackgroundColor(Color.BLACK);
		
		editText = new EditText(getContext());
		editText.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		editText.setBackgroundColor(Color.TRANSPARENT);
		editText.setTextColor(Color.WHITE);
		editText.setHintTextColor(HoloConstants.COLOR_HOLO_TEXT_HINT);
		editText.setSingleLine();
		editText.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				invalidate();
			}
		});
		this.addView(editText);
	}
	
	public void setHint(int resId) {
		editText.setHint(resId);
	}
	
	public void setHint(String hint) {
		
		if(!TextUtils.isEmpty(hint)) {
			editText.setHint(hint);
		}
	}

	public void setInputType(int type) {
		
		editText.setInputType(type);
	}
	
	public EditText getEditText() {
		
		return editText;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		if(paint == null) {
			paint = new Paint();
			paint.setColor(HoloConstants.COLOR_HOLO_TARGET_ON);
		}
		
		if(paint2 == null) {
			paint2 = new Paint();
			paint2.setColor(HoloConstants.COLOR_HOLO_TARGET_OFF);
		}
		
		if(editText.isFocused()) {
			//가로
			canvas.drawLine(8, getMeasuredHeight() - 7, getMeasuredWidth() - 7, getMeasuredHeight() - 7, paint);
			canvas.drawLine(8, getMeasuredHeight() - 8, getMeasuredWidth() - 7, getMeasuredHeight() - 8, paint);
			canvas.drawLine(8, getMeasuredHeight() - 9, getMeasuredWidth() - 7, getMeasuredHeight() - 9, paint);
			
			//왼쪽
			canvas.drawLine(8, getMeasuredHeight() - 14, 8, getMeasuredHeight() - 7, paint);
			canvas.drawLine(9, getMeasuredHeight() - 14, 9, getMeasuredHeight() - 7, paint);
			canvas.drawLine(10, getMeasuredHeight() - 14, 10, getMeasuredHeight() - 7, paint);
			
			//오른쪽
			canvas.drawLine(getMeasuredWidth() - 7, getMeasuredHeight() - 14, getMeasuredWidth() - 7, getMeasuredHeight() - 7, paint);
			canvas.drawLine(getMeasuredWidth() - 8, getMeasuredHeight() - 14, getMeasuredWidth() - 8, getMeasuredHeight() - 7, paint);
			canvas.drawLine(getMeasuredWidth() - 9, getMeasuredHeight() - 14, getMeasuredWidth() - 9, getMeasuredHeight() - 7, paint);
		} else {
			//가로
			canvas.drawLine(8, getMeasuredHeight() - 7, getMeasuredWidth() - 7, getMeasuredHeight() - 7, paint2);
			canvas.drawLine(8, getMeasuredHeight() - 8, getMeasuredWidth() - 7, getMeasuredHeight() - 8, paint2);

			//왼쪽
			canvas.drawLine(8, getMeasuredHeight() - 14, 8, getMeasuredHeight() - 7, paint2);
			canvas.drawLine(9, getMeasuredHeight() - 14, 9, getMeasuredHeight() - 7, paint2);
			
			//오른쪽
			canvas.drawLine(getMeasuredWidth() - 8, getMeasuredHeight() - 14, getMeasuredWidth() - 8, getMeasuredHeight() - 7, paint2);
			canvas.drawLine(getMeasuredWidth() - 9, getMeasuredHeight() - 14, getMeasuredWidth() - 9, getMeasuredHeight() - 7, paint2);
		}
	}
}
