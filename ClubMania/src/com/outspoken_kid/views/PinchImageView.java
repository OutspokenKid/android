package com.outspoken_kid.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class PinchImageView extends ImageView implements OnTouchListener  {

	//Variables.
	private static int NONE = 0;
	private static int DRAG = 1;
	private static int ZOOM = 2;
	private static float oldDist = 1.0f;
	
	public static int mode = NONE;
	
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();
	private Matrix savedMatrix2 = new Matrix();
	
	private boolean isInit = false;		
	private boolean edgeLeft = false;
	private boolean edgeRight = false;
	private boolean wasZooming = false;		//줌 후 이전 스케일로 돌아가는 버그를 막기 위한 플래그.
	
	private PointF start = new PointF();
	private PointF mid = new PointF();	
	
	//Methods.
	public PinchImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setOnTouchListener(this);		
		setScaleType(ScaleType.MATRIX);	
	}
	
	public PinchImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public PinchImageView(Context context) {
		this(context, null);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		if(!isInit) {
			init();
			isInit = true;
		}
	}
	
	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		isInit = false;
		init();
	}
	
	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		isInit = false;
		init();
	}
	
	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		isInit = false;
		init();
	}
	
	protected void init() {
		
		matrixTurning(matrix, this);
		setImageMatrix(matrix);
		setImagePit();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		
		ImageView view = (ImageView) v;
		
		switch(event.getAction() & MotionEvent.ACTION_MASK) {
		
		case MotionEvent.ACTION_DOWN :
			savedMatrix.set(matrix);
			start.set(event.getX(), event.getY());
			mode = DRAG;
			break;
			
		case MotionEvent.ACTION_POINTER_DOWN :
			oldDist = calculateDistance(event);
			if(oldDist > 10.0f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
				
				wasZooming = true;
			}
			break;
			
		case MotionEvent.ACTION_UP :
			wasZooming = false;
			mode = NONE;
			break;
		case MotionEvent.ACTION_POINTER_UP :
			mode = DRAG;
			break;
			
		case MotionEvent.ACTION_MOVE :
			
			if(mode == DRAG && wasZooming) {
				return false;
			}
			
			if(mode == DRAG) {
				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
			} else if (mode == ZOOM) {
				float newDist = calculateDistance(event);
				if(newDist > 10.0f) {
					float scale = newDist / oldDist;
					
					if(edgeLeft && edgeRight && scale < 1.0f) {
						return true;
					}
					
					matrix.set(savedMatrix);
					matrix.postScale(scale, scale, mid.x, mid.y);
				}
			}
			break;
		}
		
		matrixTurning(matrix, view);
		view.setImageMatrix(matrix);

		return true;
	}
	
	public float calculateDistance(MotionEvent event) {
		
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);

		return FloatMath.sqrt(x * x + y * y);
	}
	
	public void midPoint(PointF point, MotionEvent event) {
		
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x/2, y/2);
	}
	
	public void matrixTurning(Matrix matrix, ImageView view) {
		
		float[] value = new float[9];
		float[] savedValue = new float[9];
		
		matrix.getValues(value);		
		savedMatrix.getValues(savedValue);
		
		int width = view.getWidth();
		int height = view.getHeight();
		
		Drawable d = view.getDrawable();
		if(d == null)
			return;
		int imageWidth = d.getIntrinsicWidth();
		int imageHeight = d.getIntrinsicHeight();
		int scaleWidth = (int) (imageWidth * value[0]);
		int scaleHeight = (int) (imageHeight * value[4]);
		
		if(value[2] >= 0) {
			edgeLeft = true;
		} else if(value[2] <= width - scaleWidth) {
			edgeRight = true;
		} else if(width >= scaleWidth) {
			edgeLeft = true;
			edgeRight = true;
		} else {
			edgeLeft = false;
			edgeRight = false;
		}
		
		//오른쪽으로 넘어가면 못 넘어가게.
		if(value[2] <= width - scaleWidth)
			value[2] = width - scaleWidth;

		//아래로 넘어가면 못 넘어가게.
		if(value[5] <= height - scaleHeight)
			value[5] = height - scaleHeight;
		
		//왼쪽으로 넘어가면 못 넘어가게.
		if(value[2] > 0)
			value[2] = 0;
		
		//위로 넘어가면 못 넘어가게.
		if(value[5] > 0)
			value[5] = 0;
		
		if(value[0] > 10 || value[4] > 10) {
			value[0] = savedValue[0];
			value[4] = savedValue[4];
			value[2] = savedValue[2];
			value[5] = savedValue[5];
		}
		
		if(scaleWidth < width) {
			value[0] = value[4] = (float) width/imageWidth;
			
			scaleWidth = (int) (imageWidth * value[0]);		
			scaleHeight = (int) (imageHeight * value[4]);	
			
			if(scaleWidth > width)							
				value[0] = value[4] = (float) width/imageWidth;
		}

		scaleWidth = (int) (imageWidth * value[0]);
		scaleHeight = (int) (imageHeight * value[4]);
		
		if(scaleWidth <= width)						
			value[2] = (float) (width - scaleWidth)/ 2;
			
		if(scaleHeight <= height)
			value[5] = (float) (height - scaleHeight) / 2;
		
		matrix.setValues(value);
		savedMatrix2.set(matrix);
	}

	public void setImagePit() {
		
		float[] value = new float[9];
		this.matrix.getValues(value);
		
		int width = this.getWidth();
		int height = this.getHeight();
		
		Drawable d = this.getDrawable();
		if(d == null)
			return;
		int imageWidth = d.getIntrinsicWidth();
		int imageHeight = d.getIntrinsicHeight();
		int scaleWidth = (int) (imageWidth * value[0]);
		int scaleHeight = (int) (imageHeight * value[4]);
		
		value[2] = 0;
		value[5] = 0;
		
		if(imageWidth > width) {
			
			value[0] = value[4] = (float) width/imageWidth;			
			
			scaleWidth = (int) (imageWidth * value[0]);
			scaleHeight = (int) (imageHeight * value[4]);
			
			if(scaleWidth > width) 
				value[0] = value[4] = (float) width/imageWidth;
		}
		
		if(imageWidth < width) {
			
			value[0] = value[4] = (float) width / imageWidth;
			
			scaleWidth = (int) (imageWidth * value[0]);
			scaleHeight = (int) (imageHeight * value[4]);
			
			if(scaleWidth < width) 
				value[0] = value[4] = (float) width/imageWidth;
		}
		
		scaleWidth = (int) (imageWidth * value[0]);
		scaleHeight = (int) (imageHeight * value[4]);
		
		if(scaleWidth < width)
			value[2] = (float) (width - scaleWidth) / 2;
		if(scaleHeight < height)
			value[5] = (float) (height - scaleHeight) / 2;
		
		matrix.setValues(value);
		setImageMatrix(matrix);
	}
}