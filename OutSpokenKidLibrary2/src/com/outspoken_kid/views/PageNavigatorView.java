package com.outspoken_kid.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class PageNavigatorView extends View {
	
	private Paint paintOn;
	private Paint paintOff;
	
	private int size;
	private int index;
	private int r;
	private int p;
	private int startCX;
	
	public PageNavigatorView(Context context) {
		this(context, null, 0);
	}

	public PageNavigatorView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub
	}
	
	public PageNavigatorView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public void init() {
		
		paintOn = new Paint();
		paintOn.setColor(Color.WHITE);
		paintOn.setAntiAlias(true);
		
		paintOff = new Paint();
		paintOff.setColor(Color.DKGRAY);
		paintOff.setAntiAlias(true);
		
		r = ResizeUtils.getSpecificLength(6);
		p = ResizeUtils.getSpecificLength(12);
	}
	
	public void setSize(int size) {
		
		this.size = size;

		if(size > 0) {

			if(size % 2 == 0) {
				//size == 2
				//startCX = sw/2 - p/2 - r;
				startCX = ResizeUtils.getScreenWidth()/2 - p/2 - r;
				startCX -= (p + r * 2) * (size / 2 - 1);
				
				LogUtils.log("###\n2\n2\n2\n.setSize.  startCX : " + startCX);
			} else {
				//size == 1
				//startCX = sw/2;
				startCX = ResizeUtils.getScreenWidth()/2;
				startCX -= (p + r * 2) * (size / 2);
			}
		}
		/*
		size == 1
		startCX = sw/2;
		
		size == 2
		startCX = sw/2 - p/2 - r;
		
		size == 3
		startCX = sw/2 - (p + 2r);
		
		size == 4
		startCX = sw/2 - p/2 - r - (p + 2r)
		
		size == 5
		startCX = sw/2 - (p + 2r) - (p + 2r); 
		*/
	}
	
	public void setIndex(int index) {
		
		this.index = index;
		this.invalidate();
	}
	
	public void setColor(int selectedColor, int unselectedColor) {

		paintOn = new Paint();
		paintOn.setColor(selectedColor);
		paintOn.setAntiAlias(true);
		
		paintOff = new Paint();
		paintOff.setColor(unselectedColor);
		paintOff.setAntiAlias(true);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		for(int i=0; i<size; i++) {
			canvas.drawCircle(startCX + (r * 2 + p) * i, r, r, (i==index?paintOn:paintOff));
		}
	}
}
