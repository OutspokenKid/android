package com.outspoken_kid.views.holo_dark.meterials;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class ViewForButtonBgPressed extends View {

	private Paint paint1, paint2, paint3;
	private RectF rectF;

	public ViewForButtonBgPressed(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		if(paint1 == null) {
			paint1 = new Paint();
			paint1.setAntiAlias(true);
			paint1.setARGB(255, 180, 180, 180);
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
