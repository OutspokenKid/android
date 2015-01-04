package com.outspoken_kid.views.holo.holo_light;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.views.holo.HoloConstants;

public class HoloStyleEditText extends FrameLayout {

	private EditText editText;
	
	private Paint paint, paint2;
	
	private int lengthOn;
	private int lengthOff;
	private int padding;

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
		
		//QHD
		if(ResizeUtils.getScreenWidth() >= 1440) {
			lengthOn = 8;
			lengthOff = 6;
			padding = 18;
			
		//Full HD
		} else if(ResizeUtils.getScreenWidth() >= 1080) {
			lengthOn = 6;
			lengthOff = 4;
			padding = 16;
			
		//HD
		} else if(ResizeUtils.getScreenWidth() >= 720) {
			lengthOn = 4;
			lengthOff = 3;
			padding = 9;
			
		} else {
			lengthOn = 3;
			lengthOff = 2;
			padding = 8;
		}
		
		setBackgroundColor(Color.TRANSPARENT);
		
		editText = new EditText(getContext());
		editText.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		editText.setBackgroundColor(Color.TRANSPARENT);
		editText.setTextColor(HoloConstants.COLOR_HOLO_TEXT);
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
		
		if(!StringUtils.isEmpty(hint)) {
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

		/*
		01-04 20:57:15.829: I/notice(4802): width : 303
		01-04 20:57:15.829: I/notice(4802): height : 78
		01-04 20:57:15.829: I/notice(4802):  lengthOn : 4
		01-04 20:57:15.829: I/notice(4802):  lengthOff : 3
		01-04 20:57:15.829: I/notice(4802):  padding : 9
		*/
		
		if(editText.isFocused()) {
			
			for(int i=0; i<lengthOn; i++) {
				
				//가로.
				canvas.drawLine(padding + lengthOn,					 
						getMeasuredHeight() - padding - i, 
						getMeasuredWidth() - padding - lengthOn, 
						getMeasuredHeight() - padding - i, paint);
				
				//왼쪽.
				canvas.drawLine(padding + i, 
						getMeasuredHeight() - padding * 2, 
						padding + i, 
						getMeasuredHeight() - (padding - 1), paint);
				
				//오른쪽.
				canvas.drawLine(getMeasuredWidth() - (padding + 1) - i, 
						getMeasuredHeight() - padding * 2, 
						getMeasuredWidth() - (padding + 1) - i, 
						getMeasuredHeight() - (padding - 1), paint);
			}
		} else {
			
			for(int i=0; i<lengthOff; i++) {
				
				//가로.
				canvas.drawLine(padding + lengthOff, 
						getMeasuredHeight() - padding - i, 
						getMeasuredWidth() - padding - lengthOff, 
						getMeasuredHeight() - padding - i, paint2);
				
				//왼쪽.
				canvas.drawLine(padding + i, 
						getMeasuredHeight() - padding * 2, 
						padding + i, 
						getMeasuredHeight() - (padding - 1) , paint2);
				
				//오른쪽.
				canvas.drawLine(getMeasuredWidth() - (padding + 1) - i, 
						getMeasuredHeight() - padding * 2, 
						getMeasuredWidth() - (padding + 1) - i, 
						getMeasuredHeight() - (padding - 1), paint2);
			}
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
		
		editText.setSelection(editText.length());
	}
}
