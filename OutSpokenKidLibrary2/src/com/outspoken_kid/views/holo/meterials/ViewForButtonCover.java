package com.outspoken_kid.views.holo.meterials;

import com.outspoken_kid.views.holo.holo_dark.HoloConstants;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

public class ViewForButtonCover extends View {
	
	private Paint paint;
	private RectF rectF;

	public ViewForButtonCover(Context context) {
		super(context);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		if(paint == null) {
			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setColor(HoloConstants.COLOR_HOLO_COVER);
		}
		
		if(rectF == null) {
			rectF = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
		}
		
		canvas.drawRoundRect(rectF, 6, 6, paint);
	}
}