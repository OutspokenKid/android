package com.outspoken_kid.views.holo.holo_light;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.views.holo.HoloConstants;
import com.outspoken_kid.views.holo.meterials.ViewForButtonBgPressed;
import com.outspoken_kid.views.holo.meterials.ViewForButtonCover;

public class HoloStyleSpinnerButton extends FrameLayout {

	private ViewForButtonCover viewForCover;
	private ViewForButtonBgPressed viewForBg;
	private Paint paint;
	private Path path;
	private TextView textView;
	private HoloStyleSpinnerPopup popup; 
	private int triangleLength;
	private int length;
	private int padding;
	private AlphaAnimation aaIn, aaOut;
	
	public HoloStyleSpinnerButton(Context context) {
		this(context, null, 0);
	}
	
	public HoloStyleSpinnerButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public HoloStyleSpinnerButton(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		
		//QHD
		if(ResizeUtils.getScreenWidth() >= 1440) {
			triangleLength = 60;
			length = 6;
			padding = 18;
			
		//Full HD
		} else if(ResizeUtils.getScreenWidth() >= 1080) {
			triangleLength = 40;
			length = 4;
			padding = 16;
			
		//HD
		} else if(ResizeUtils.getScreenWidth() >= 720) {
			triangleLength = 30;
			length = 3;
			padding = 9;
			
		} else {
			triangleLength = 20;
			length = 2;
			padding = 8;
		}

		setBackgroundColor(Color.TRANSPARENT);
		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				if(popup != null) {
					AlphaAnimation aaIn = new AlphaAnimation(0, 1);
					aaIn.setDuration(200);
					popup.setVisibility(View.VISIBLE);
					popup.startAnimation(aaIn);
				}
			}
		});

		viewForBg = new ViewForButtonBgPressed(getContext());
		viewForBg.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		viewForBg.setVisibility(View.INVISIBLE);
		this.addView(viewForBg);
		
		viewForCover = new ViewForButtonCover(getContext());
		viewForCover.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		viewForCover.setVisibility(View.INVISIBLE);
		this.addView(viewForCover);
		
		textView = new TextView(getContext());
		textView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		ResizeUtils.setPadding(textView, new int[]{20, 0, 20, 0});
		textView.setBackgroundColor(Color.TRANSPARENT);
		textView.setTextColor(HoloConstants.COLOR_HOLO_TEXT);
		textView.setEllipsize(TruncateAt.END);
		textView.setHintTextColor(HoloConstants.COLOR_HOLO_TEXT_HINT);
		textView.setSingleLine();
		textView.setGravity(Gravity.CENTER_VERTICAL);
		this.addView(textView);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:

			if(aaIn == null) {
				aaIn = new AlphaAnimation(0, 1);
				aaIn.setDuration(100);
			}

			viewForCover.setVisibility(View.VISIBLE);
			viewForCover.startAnimation(aaIn);
			viewForBg.setVisibility(View.VISIBLE);
			viewForBg.startAnimation(aaIn);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			
			if(aaOut == null) {
				aaOut = new AlphaAnimation(1, 0);
				aaOut.setDuration(100);
			}

			viewForCover.setVisibility(View.INVISIBLE);
			viewForCover.startAnimation(aaOut);
			viewForBg.setVisibility(View.INVISIBLE);
			viewForBg.startAnimation(aaOut);
			break;
		}
		
		return super.onTouchEvent(event);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		if(paint == null) {
			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setColor(HoloConstants.COLOR_HOLO_TARGET_OFF);
		}
		
		if(path == null) {
			int triangleEdgeWidth = getMeasuredWidth() - padding - length;
			int triangleEdgeHeight = getMeasuredHeight() - (padding - 1) - length;
			
			path = new Path();
			path.moveTo(triangleEdgeWidth, triangleEdgeHeight);
			path.lineTo(triangleEdgeWidth - triangleLength, triangleEdgeHeight);
			path.lineTo(triangleEdgeWidth, triangleEdgeHeight - triangleLength);
		}
		
		canvas.drawPath(path, paint);
		
		for(int i=0; i<length; i++) {
			
			//padding = 8, i = 0,1
			
			//가로.
			canvas.drawLine(padding + length, 
					getMeasuredHeight() - padding - i, 
					getMeasuredWidth() - padding - length, 
					getMeasuredHeight() - padding - i, paint);
			
			//10, h-9, w-10, h-9
			//10, h-8, w-10, h-8
		}
	}
	
	public TextView getTextView() {
		
		return textView;
	}
	
	public void setText(String text) {
		
		textView.setText(text);
	}
	
	public void setText(int resId) {
		
		textView.setText(resId);
	}
}
