package com.outspoken_kid.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class OutSpokenRatingBar extends View {

	public static final int UNIT_ONE = 0;
	public static final int UNIT_HALF = 1;
	
	private Paint paintFilled;
	private Paint paintEmpty;
	private Path path;
	
	private int starMargin;
	private int length;
	private int halfLength;
	private int[] leftMargins = new int[5];
	private int topMargin;
	
	private boolean isInit;
	private boolean touchable = true;
	
	private int length_8;
	private int length_10;
	private int length_13;
	private int length_14;
	private int length_20;
	private int length_25;
	private int length_27;
	private int length_30;
	private int length_32;
	private int length_34;
	private int length_40;

	private float rating = -1;
	private float minRating = -1;
	private float maxRating = -1;
	private int unitRating = -1;
	private int X;
	
	private OnRatingChangedListener onRatingChangedListener;
	
	public OutSpokenRatingBar(Context context) {
		this(context, null);
	}
	
	public OutSpokenRatingBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackgroundColor(Color.TRANSPARENT);
	}
	
	public void init() {
		
		if(paintFilled == null) {
			paintFilled = new Paint();
			paintFilled.setColor(Color.GREEN);
			paintFilled.setAntiAlias(true);
			paintFilled.setStyle(Paint.Style.FILL);
		}
		
		if(paintEmpty == null) {
			paintEmpty = new Paint();
			paintEmpty.setColor(Color.GRAY);
			paintEmpty.setAntiAlias(true);
			paintEmpty.setStyle(Paint.Style.FILL);
		}
		
	    path = new Path();
	    
	    if(length == 0) {
	    	setLengths(50, 10);
	    }
	    
	    leftMargins[0] = getLeftMargin(0);
	    leftMargins[1] = getLeftMargin(1);
	    leftMargins[2] = getLeftMargin(2);
	    leftMargins[3] = getLeftMargin(3);
	    leftMargins[4] = getLeftMargin(4);
	    topMargin = getTopMargin();

	    setStarLengths(length);

	    if(minRating == -1) {
	    	setMinRating(0);
	    }
	    
	    if(minRating == -1) {
	    	setMaxRating(5);
	    }
	    
	    if(rating == -1) {
	    	rating = getMinRating();
	    }
	    
	    if(unitRating == -1) {
	    	unitRating = UNIT_HALF;
	    }
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		
		if(!isInit) {
			isInit = true;
			init();
		}

		if(rating < getMinRating()) {
			rating = getMinRating();
		} else if(rating > getMaxRating()) {
			rating = getMaxRating();
		}
		
		if(unitRating == UNIT_HALF) {
			/**
			 * i와 rating 비교.
			 * 		rating을 integer로 캐스팅하고 남은 수 만큼 꽉찬 별 추가.
			 * 		rating과 float로 캐스팅한 filledCount가 다른 경우(소숫점이 있는 경우) 반쪽 별 추가.
			 *		남은 수 만큼 빈 별 추가.
			 */
			int filledCount = (int)(rating);
			boolean needHalf = (rating != (float)filledCount);
			int emptyCount = 5 - filledCount - (needHalf? 1 : 0);
			
			for(int i=0; i<filledCount; i++) {
				drawFull(canvas, i);
			}
			
			if(needHalf) {
				drawHalf(canvas, filledCount);
			}
			
			for(int i=5 - emptyCount; i<5; i++) {
				drawEmpty(canvas, i);
			}
		} else {
			
			/**
			 * i와 rating 비교.
			 * 		rating을 integer로 캐스팅하고 남은 수 만큼 꽉찬 별 추가.
			 *		남은 수 만큼 빈 별 추가.
			 */
			int filledCount = (int)(rating);
			int emptyCount = 5 - filledCount;
			
			for(int i=0; i<filledCount; i++) {
				drawFull(canvas, i);
			}
			
			for(int i=5 - emptyCount; i<5; i++) {
				drawEmpty(canvas, i);
			}
		}
	}
	
	@SuppressLint("ClickableViewAccessibility")
	public boolean onTouchEvent(MotionEvent event) {
	
		if(!touchable) {
			return false;
		}
		
		X = (int)event.getX(); 
		
		switch (event.getAction()) { 

		case MotionEvent.ACTION_DOWN:
        case MotionEvent.ACTION_MOVE:

        	if(unitRating == UNIT_HALF) {
        		if(X < leftMargins[0]) {
            		rating = 0;
            	} else if(X < leftMargins[0] + halfLength) {
            		rating = 0.5f;
            	} else if(X < leftMargins[1]) {
            		rating = 1;
            	} else if(X < leftMargins[1] + halfLength) {
            		rating = 1.5f;
            	} else if(X < leftMargins[2]) {
            		rating = 2;
            	} else if(X < leftMargins[2] + halfLength) {
            		rating = 2.5f;
            	} else if(X < leftMargins[3]) {
            		rating = 3;
            	} else if(X < leftMargins[3] + halfLength) {
            		rating = 3.5f;
            	} else if(X < leftMargins[4]) {
            		rating = 4;
            	} else if(X < leftMargins[4] + halfLength) {
            		rating = 4.5f;
            	} else {
            		rating = 5;
            	}
        	} else {
        		if(X < leftMargins[0] + halfLength) {
            		rating = 0;
            	} else if(X < leftMargins[1] + halfLength) {
            		rating = 1;
            	} else if(X < leftMargins[2] + halfLength) {
            		rating = 2;
            	} else if(X < leftMargins[3] + halfLength) {
            		rating = 3;
            	} else if(X < leftMargins[4] + halfLength) {
            		rating = 4;
            	} else {
            		rating = 5;
            	}
        	}

   		 	// Redraw the canvas
        	invalidate();
        	
        	if(onRatingChangedListener != null) {
        		onRatingChangedListener.onRatingChanged(rating);
        	}
        	break;
        	
        case MotionEvent.ACTION_UP:
        	break; 
		}	        

		return true; 
	}
	
	public void drawFull(Canvas canvas, int index) {
	
		path.reset();
		path.moveTo(leftMargins[index], topMargin +  length_14);
		path.lineTo(leftMargins[index] + length_13, topMargin +  length_13);
		path.lineTo(leftMargins[index] + length_20, topMargin +  0);
		path.lineTo(leftMargins[index] + length_27, topMargin +  length_13);
		path.lineTo(leftMargins[index] + length_40, topMargin +  length_14);
		path.lineTo(leftMargins[index] + length_30, topMargin +  length_25);
		path.lineTo(leftMargins[index] + length_32, topMargin +  length_40);
		path.lineTo(leftMargins[index] + length_20, topMargin +  length_34);
		path.lineTo(leftMargins[index] + length_8, topMargin +  length_40);
		path.lineTo(leftMargins[index] + length_10, topMargin +  length_25);
		path.lineTo(leftMargins[index], topMargin +  length_14);
		path.close();
		
		canvas.drawPath(path, paintFilled);
	}
	
	public void drawHalf(Canvas canvas, int index) {
	
		path.reset();
		path.moveTo(leftMargins[index], topMargin +  length_14);
		path.lineTo(leftMargins[index] + length_13, topMargin +  length_13);
		path.lineTo(leftMargins[index] + length_20, topMargin +  0);
		path.lineTo(leftMargins[index] + length_20, topMargin +  length_34);
		path.lineTo(leftMargins[index] + length_8, topMargin +  length_40);
		path.lineTo(leftMargins[index] + length_10, topMargin +  length_25);
		path.lineTo(leftMargins[index], topMargin +  length_14);
		path.close();
		
		canvas.drawPath(path, paintFilled);
		
		path.reset();
		path.moveTo(leftMargins[index] + length_20, topMargin +  0);
		path.lineTo(leftMargins[index] + length_27, topMargin +  length_13);
		path.lineTo(leftMargins[index] + length_40, topMargin +  length_14);
		path.lineTo(leftMargins[index] + length_30, topMargin +  length_25);
		path.lineTo(leftMargins[index] + length_32, topMargin +  length_40);
		path.lineTo(leftMargins[index] + length_20, topMargin +  length_34);
		path.close();
		
		canvas.drawPath(path, paintEmpty);
	}
	
	public void drawEmpty(Canvas canvas, int index) {
		
		path.reset();
		path.moveTo(leftMargins[index], topMargin +  length_14);
		path.lineTo(leftMargins[index] + length_13, topMargin +  length_13);
		path.lineTo(leftMargins[index] + length_20, topMargin +  0);
		path.lineTo(leftMargins[index] + length_27, topMargin +  length_13);
		path.lineTo(leftMargins[index] + length_40, topMargin +  length_14);
		path.lineTo(leftMargins[index] + length_30, topMargin +  length_25);
		path.lineTo(leftMargins[index] + length_32, topMargin +  length_40);
		path.lineTo(leftMargins[index] + length_20, topMargin +  length_34);
		path.lineTo(leftMargins[index] + length_8, topMargin +  length_40);
		path.lineTo(leftMargins[index] + length_10, topMargin +  length_25);
		path.lineTo(leftMargins[index], topMargin +  length_14);
		path.close();
		
		canvas.drawPath(path, paintEmpty);
	}

	private void setStarLengths(int length) {
		
		length_8 = (int)((float)length * 0.2f);
		length_10 = (int)((float)length * 0.25f);
		length_13 = (int)((float)length * 0.325f);
		length_14 = (int)((float)length * 0.35f);
		length_20 = (int)((float)length * 0.5f);
		length_25 = (int)((float)length * 0.625f);
		length_27 = (int)((float)length * 0.675f);
		length_30 = (int)((float)length * 0.75f);
		length_32 = (int)((float)length * 0.8f);
		length_34 = (int)((float)length * 0.85f);
		length_40 = (int)((float)length * 1);
	}
	
	public void setLengths(int length, int starMargin) {

		this.length = length;
		this.halfLength = length / 2;
	    this.starMargin = starMargin;
	    
	    invalidate();
	}
	
	public int getLeftMargin(int index) {
		
		int lm = (getMeasuredWidth() - length*5 - starMargin*4) / 2;
		
		return lm + (index * length) + (index==0 ? 0 : index * starMargin);
	}

	public int getTopMargin() {
		
		return (getMeasuredHeight() - length) / 2;
	}

	public void setFilledStarColor(int color) {
		
		paintFilled = new Paint();
		paintFilled.setColor(color);
		paintFilled.setAntiAlias(true);
		paintFilled.setStyle(Paint.Style.FILL);
		
		invalidate();
	}
	
	public void setEmptyStarColor(int color) {
	
		paintEmpty = new Paint();
		paintEmpty.setColor(color);
		paintEmpty.setAntiAlias(true);
		paintEmpty.setStyle(Paint.Style.FILL);
		
		invalidate();
	}
	
	public void setRating(float rating) {
		
		if(rating < minRating) {
			this.rating = minRating;
		} else if(rating > maxRating) {
			this.rating = maxRating;
		} else {
			this.rating = rating;
		}
		
	}
	
	public float getRating() {
		
		return rating;
	}

	public float getMinRating() {
		return minRating;
	}

	public void setMinRating(float minRating) {
		
		if(minRating < 0) {
			this.minRating = 0;
		} else {
			this.minRating = minRating;
		}
	}

	public float getMaxRating() {
		return maxRating;
	}

	public void setMaxRating(float maxRating) {
		
		if(maxRating > 5) {
			this.maxRating = 5;
		} else {
			this.maxRating = maxRating;
		}
	}

	public int getUnitRating() {
		return unitRating;
	}

	public void setUnitRating(int unitRating) {
		
		if(unitRating == UNIT_ONE) {
			this.unitRating = UNIT_ONE;
		} else if(unitRating == UNIT_HALF) {
			this.unitRating = UNIT_HALF;
		} else {
			//ignore.
		}
	}


	public boolean isTouchable() {
		return touchable;
	}

	public void setTouchable(boolean touchable) {
		this.touchable = touchable;
	}
	
	public void setOnRatingChangedListener(OnRatingChangedListener onRatingChangedListener) {
		
		this.onRatingChangedListener = onRatingChangedListener;
	}
	
//////////////////// Interfaces.
	
	public interface OnRatingChangedListener {
		
		public void onRatingChanged(float rating);
	}
}
