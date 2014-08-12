package com.outspoken_kid.views.holo.holo_light;

import android.R.color;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.views.holo.meterials.ViewForButtonBg;
import com.outspoken_kid.views.holo.meterials.ViewForButtonBgPressed;
import com.outspoken_kid.views.holo.meterials.ViewForButtonCover;

public class HoloStyleButton extends FrameLayout {

	private Paint paint1, paint2, paint3;
	private RectF rectF;
	
	private ViewForButtonBg viewForBg;
	private ViewForButtonBgPressed viewForBg2;
	private ViewForButtonCover viewForCover;
	private TextView textView;
	private View viewForImage;
	private AlphaAnimation aaOut;
	
	public HoloStyleButton(Context context) {
		this(context, null, 0);
	}
	
	public HoloStyleButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public HoloStyleButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		
		requestDisallowInterceptTouchEvent(true);
		setBackgroundColor(color.background_dark);
		
		viewForBg = new ViewForButtonBg(getContext());
		viewForBg.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		this.addView(viewForBg);
		
		viewForBg2 = new ViewForButtonBgPressed(getContext());
		viewForBg2.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		viewForBg2.setVisibility(View.INVISIBLE);
		this.addView(viewForBg2);
		
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
		textView.setGravity(Gravity.CENTER);
		this.addView(textView);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		switch (event.getAction()) {
		
		case MotionEvent.ACTION_DOWN:

			viewForCover.setVisibility(View.VISIBLE);
			viewForBg2.setVisibility(View.VISIBLE);
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			
			if(aaOut == null) {
				aaOut = new AlphaAnimation(1, 0);
				aaOut.setDuration(100);
			}

			viewForCover.setVisibility(View.INVISIBLE);
			viewForBg2.setVisibility(View.INVISIBLE);
			viewForCover.startAnimation(aaOut);
			viewForBg2.startAnimation(aaOut);
		}
		
		return super.onTouchEvent(event);
	}
	
	public TextView getTextView() {
		
		return textView;
	}
	
	public void setText(String text) {
		
		if(!StringUtils.isEmpty(text)) {
			textView.setText(text);
		}
	}
	
	public void setText(int resId) {
		
		textView.setText(resId);
	}
	
	public void setTextColor(int color) {
		
		textView.setTextColor(color);
	}
	
	public void setImage(int imgResId, int width, int height) {

		try {
			if(viewForImage == null) {
				viewForImage = new View(getContext());
				ResizeUtils.viewResize(width, height, viewForImage, 2, 
						Gravity.CENTER_VERTICAL|Gravity.LEFT, new int []{20, 0, 20, 0});
				this.addView(viewForImage);
			}
			
			viewForImage.setBackgroundResource(imgResId);
			ResizeUtils.setPadding(textView, new int[]{width + 40, 0, 20, 0});
		} catch(Exception e) {
			e.printStackTrace();
		} catch(OutOfMemoryError oom) {
			oom.printStackTrace();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		if(paint1 == null) {
			paint1 = new Paint();
			paint1.setAntiAlias(true);
			paint1.setARGB(255, 60, 60, 60);
		}
		
		if(paint2 == null) {
			paint2 = new Paint();
			paint2.setARGB(255, 120, 120, 120);
		}
		
		if(paint3 == null) {
			paint3 = new Paint();
			paint3.setARGB(255, 30, 30, 30);
		}
		
		if(rectF == null) {
			rectF = new RectF(4, 4, getMeasuredWidth() - 4, getMeasuredHeight() - 4);
		}

		canvas.drawRoundRect(rectF, 2, 2, paint1);
		
		canvas.drawLine(6, 4, getMeasuredWidth() - 6, 4, paint2);
		canvas.drawPoint(5, 5, paint2);
		canvas.drawPoint(getMeasuredWidth() - 5, 5, paint2);
		
		canvas.drawLine(6, getMeasuredHeight() - 4, getMeasuredWidth() - 6, getMeasuredHeight() - 4, paint3);
		canvas.drawPoint(5, getMeasuredHeight() - 5, paint3);
		canvas.drawPoint(getMeasuredWidth() - 5, getMeasuredHeight() - 5, paint3);
	}
}
