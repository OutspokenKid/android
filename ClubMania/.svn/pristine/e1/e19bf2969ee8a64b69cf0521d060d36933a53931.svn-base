package com.outspoken_kid.imagefilter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;

public class RoundedConerFilter extends ImageFilter {

	@Override
	public Bitmap getFilteredBitmap(Bitmap bitmap) {

		if(bitmap == null) {
			return null;
		}
		
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Bitmap resultBitmap;
		
		try {
			resultBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		    Canvas canvas = new Canvas(resultBitmap);

		    final int color = 0xff424242;
		    final Paint paint = new Paint();
		    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		    final RectF rectF = new RectF(rect);

		    paint.setAntiAlias(true);
		    canvas.drawARGB(0, 0, 0, 0);
		    paint.setColor(color);
		    canvas.drawRoundRect(rectF, width / 6, height / 6, paint);
		    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		    canvas.drawBitmap(bitmap, rect, rect, paint);
		    
		    if(resultBitmap != null) {
		    	return resultBitmap;
		    } else {
		    	return null;
		    }
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		} catch(OutOfMemoryError e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public String getFilterText() {
		return "roundedConer";
	}
}
