package com.outspoken_kid.views.holo_dark;

import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.views.holo_dark.meterials.ViewForButtonBgPressed;
import com.outspoken_kid.views.holo_dark.meterials.ViewForButtonCover;

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

public class HoloStyleSpinnerButton extends FrameLayout {

	private ViewForButtonCover viewForCover;
	private ViewForButtonBgPressed viewForBg;
	private Paint paint;
	private Path path;
	private TextView textView;
	private HoloStyleSpinnerPopup popup; 
	private int l;
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
		setBackgroundColor(Color.BLACK);
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
		textView.setTextColor(Color.WHITE);
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
			paint.setARGB(255, 80, 80, 80);
		}
		
		if(l == 0) {
			l = ResizeUtils.getSpecificLength(20);
		}
		
		if(path == null) {
			path = new Path();
			path.moveTo(getMeasuredWidth() - 7, getMeasuredHeight() - 7);
			path.lineTo(getMeasuredWidth() - 7 - l, getMeasuredHeight() - 7);
			path.lineTo(getMeasuredWidth() - 7, getMeasuredHeight() - 7 - l);
		}
		
		canvas.drawPath(path, paint);
		canvas.drawLine(7, getMeasuredHeight() - 8, getMeasuredWidth() - 7, getMeasuredHeight() - 8, paint);
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
