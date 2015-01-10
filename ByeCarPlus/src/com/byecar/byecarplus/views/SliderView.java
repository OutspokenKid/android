package com.byecar.byecarplus.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.byecar.byecarplus.R;
import com.outspoken_kid.utils.ResizeUtils;

public class SliderView extends View {
	
	private static int HEIGHT_OF_SELECTED_BAR = 3;

	private int valueMin = 0;
	private int valueMax = 100;
	private int valueDelta = valueMax - valueMin;
	
	private Bg bg;
	protected NodeChangedListener nodeChangedListener;
	private Node[] nodes = new Node[2]; // array that holds the nodes
	private int balID = 0; // variable to know what node is being dragged
	
	private int startNodeValue, endNodeValue;//value to know the node position e.g: 0,40,..,100
	private int startNodeValueTmp, endNodeValueTmp;//the position on the X axis
	private Paint paintSelected;
	private Rect rectangleSelected, rectangleBg;
	private int startX, endX, startY, endY;//variables for the rectangles
	private boolean isInit;
	private int selectedBarHeight;
	private int lineCenterY;
	private int left_bound, right_bound, delta_bound;
	private double ratio;

	public SliderView(Context context) {
		this(context, null, 0);
	}
	
	public SliderView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public SliderView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setFocusable(true);
		
		bg = new Bg();
		bg.setBitmap(R.drawable.filter_bar);

		nodes[0] = new Node(getContext(),R.drawable.filter_node_left);
		nodes[0].setID(1);
		nodes[1] = new Node(getContext(),R.drawable.filter_node_right);
		nodes[1].setID(2);
		
		rectangleSelected = new Rect();
		
		lineCenterY = (int) ((float)nodes[0].getBitmap().getHeight() * 0.75f);
	}
	
	public void init() {

		Point pointNodeStart = new Point();
		Point pointNodeEnd = new Point();
		pointNodeStart.x = 0;
		pointNodeStart.y = 0;
		pointNodeEnd.x = getMeasuredWidth() - nodes[1].getBitmap().getWidth();
		pointNodeEnd.y = 0;
		
		nodes[0].setPoint(pointNodeStart);
		nodes[1].setPoint(pointNodeEnd);
		
		setStartNodeValue(valueMin);
		setEndNodeValue(valueMax);
		
		selectedBarHeight = ResizeUtils.getSpecificLength(HEIGHT_OF_SELECTED_BAR);
		int halfNodeWidth = nodes[0].getBitmap().getWidth() / 2;
		int left = halfNodeWidth;
		int right = getWidth() - halfNodeWidth;
		int height = (int)((float)(right - left) / 530f * 11f);
		int top = lineCenterY - selectedBarHeight - height/2 + 4;
		int bottom = lineCenterY - selectedBarHeight + height/2 + 4;
		rectangleBg = new Rect(left, top, right, bottom);
		
		left_bound = nodes[0].getBitmap().getWidth() / 2;
    	right_bound = getWidth() - nodes[1].getBitmap().getWidth() / 2;
    	delta_bound = right_bound - left_bound;
    	
    	ratio = (double)delta_bound/valueDelta;
    	
    	nodeChanged(true, true, getStartNodeValue(), getEndNodeValue());
	}

	public void setValues(int valueMin, int valueMax) {
		
		this.valueMin = valueMin;
		this.valueMax = valueMax;
		this.valueDelta = valueMax - valueMin;
	}
	
	@Override 
	protected void onDraw(Canvas canvas) {
		
		if(!isInit) {
			isInit = true;
			init();
		}

		//rectangle between nodes
		startX = nodes[0].getX() + nodes[0].getBitmap().getWidth() / 2;
		endX = nodes[1].getX() + nodes[1].getBitmap().getWidth() / 2;
		startY = lineCenterY - selectedBarHeight;
		endY = lineCenterY + selectedBarHeight;
		rectangleSelected.set(startX, startY, endX, endY);
		
		canvas.drawBitmap(bg.getBitmap(), null, rectangleBg, null);
		
		if(paintSelected != null) {
			canvas.drawRect(rectangleSelected, paintSelected);
		}
		
		canvas.drawBitmap(nodes[0].getBitmap(), nodes[0].getX(), nodes[0].getY(), null);
		canvas.drawBitmap(nodes[1].getBitmap(), nodes[1].getX(), nodes[1].getY(), null);
	}
	    
	@SuppressLint("ClickableViewAccessibility")
	public boolean onTouchEvent(MotionEvent event) {
		
		int eventaction = event.getAction();     
		int X = (int)event.getX(); 
		int Y = (int)event.getY();
		
		switch (eventaction) { 

		case MotionEvent.ACTION_DOWN:
			balID = 0;
			for (Node node : nodes) {				
				// check if inside the bounds of the node(circle)
				// get the centre of the node
				int centerX = node.getX() + node.getBitmap().getWidth();
				int centerY = node.getY() + node.getBitmap().getHeight();
        		// calculate the radius from the touch to the centre of the node
				double radCircle  = Math.sqrt( (double) (((centerX-X)*(centerX-X)) + (centerY-Y)*(centerY-Y)));
        		// if the radius is smaller then 33 (radius of a node is 22), then it must be on the ball
				if (radCircle < 3*node.getBitmap().getHeight()/2.0){
					balID = node.getID();
				}
			}
			
			if(balID == 0) {
				
				if(X < nodes[0].getX()) {
					nodes[0].setX(X - nodes[0].getBitmap().getWidth() / 2);
					balID = 1;
				} else if(X > nodes[1].getX()) {
					nodes[1].setX(X - nodes[0].getBitmap().getWidth() / 2);
					balID = 2;
				}
			}
			
			break; 
		
        case MotionEvent.ACTION_MOVE:	
        	startNodeValueTmp = 0;
        	endNodeValueTmp = 0;
        	
        	int radiusNode = nodes[0].getBitmap().getWidth()/2;

        	 // node position from centre
        	int left_node = nodes[0].getX() + radiusNode;
        	int right_node = nodes[1].getX() + radiusNode;
        	
        	// The calculated node value using
        	// the bounds, ratio, and actual node position 
        	startNodeValueTmp = (int)((valueMax*ratio - right_bound + left_node)/ratio);
        	endNodeValueTmp = (int)((valueMax*ratio - right_bound + right_node)/ratio);

        	//the first node should be between the left bound and the second node
        	if(balID == 1) {
        		if(X < left_bound) {
        			X = left_bound;
        		}
        		
        		if(X >= nodes[1].getX() + nodes[1].getBitmap().getWidth() / 2) {
        			X = nodes[1].getX() + nodes[1].getBitmap().getWidth() / 2;
        		}
        		
        		nodes[0].setX(X-radiusNode);
        		
        		//if the start value has changed then we pass it to the listener
        		if(startNodeValueTmp != getStartNodeValue()) {
        			setStartNodeValue(startNodeValueTmp);
	        		nodeChanged(true, false, getStartNodeValue(), getEndNodeValue());	            	
	        	}	            
        	}
        	
        	//the second node should between the first node and the right bound
        	if(balID == 2) {
        		if(X > right_bound) {
        			X = right_bound;
        		}
        			
        		if(X <= nodes[0].getX() + nodes[0].getBitmap().getWidth() / 2) {
        			X = nodes[0].getX() + nodes[0].getBitmap().getWidth() / 2;
        		}
        			
        		nodes[1].setX(X-radiusNode);
        		
        		//if the end value has changed then we pass it to the listener
	        	if(endNodeValueTmp != getEndNodeValue()) {
        			setEndNodeValue(endNodeValueTmp);
	        		nodeChanged(false, true, getStartNodeValue(), getEndNodeValue());	            	
	        	}
        	}
        	break;
        	
        case MotionEvent.ACTION_UP:
        	break; 
		}	        
		
		 // Redraw the canvas
		invalidate();  
		return true; 
	}

	public void setOnNodeChangedListener (NodeChangedListener l) {
		nodeChangedListener = l;
	}
	
	private void nodeChanged(boolean nodeStartChanged, boolean nodeEndChanged, int nodeStart, int nodeEnd) {
		
		if(nodeChangedListener != null) {
			nodeChangedListener.onChanged(nodeStartChanged, nodeEndChanged, nodeStart, nodeEnd);
		}
	}
	
	public void setSelectedColor(int color) {
		
		paintSelected = new Paint();
		paintSelected.setColor(color);
	}
	
	public int getStartNodeValue() {
		return startNodeValue;
	}

	public void setStartNodeValue(int startNodeValue) {
		this.startNodeValue = startNodeValue;
	}

	public int getEndNodeValue() {
		return endNodeValue;
	}

	public void setEndNodeValue(int endNodeValue) {
		this.endNodeValue = endNodeValue;
	}

	public int getNodeWidth() {

		if(nodes[0] != null) {
			return nodes[0].getBitmap().getWidth();
		}
		
		return 0;
	}
	
//////////////////// Classes.
	
	public class Node {
		private Bitmap img;
		private int coordX = 0;
		private int coordY = 0;
		private int id;
	 
		public Node(Context context, int drawable) {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			img = BitmapFactory.decodeResource(context.getResources(), drawable); 
		}
			
		public Node(Context context, int drawable, Point point) {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			img = BitmapFactory.decodeResource(context.getResources(), drawable);
			coordX= point.x;
			coordY = point.y;
		}
			
		public void setPoint(Point point) {
			
			coordX= point.x;
			coordY = point.y;
		}
		
		void setX(int newValue) {
			coordX = newValue;
		}
			
		public int getX() {
			return coordX;
		}

		void setY(int newValue) {
			coordY = newValue;
		}
			
		public int getY() {
			return coordY;
		}
		
		public void setID(int id) {
			this.id = id;
		}
		
		public int getID() {
			return id;
		}
			
		public Bitmap getBitmap() {
			return img;
		}		
	}
	
	public class Bg {
		
		private Bitmap bitmap;
		public int startX;
		public int startY;
		
		public Bitmap getBitmap() {
			
			return bitmap;
		}
		
		public void setBitmap(int resId) {
			
			try {
				bitmap = BitmapFactory.decodeResource(getContext().getResources(), resId);
			} catch (Exception e) {
			} catch (Error e) {
			}
		}
		
		public void setBitmap(Bitmap bitmap) {
			
			this.bitmap = bitmap;
		}
	}
	
//////////////////// Interfaces.
	
	public interface NodeChangedListener {
		void onChanged(boolean nodeStartChanged, boolean nodeEndChanged, int nodeStart, int nodeEnd);
	}
}