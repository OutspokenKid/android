package com.outspoken_kid.views;

import java.lang.reflect.Field;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.widget.Scroller;

public class SpeedControllableViewPager extends ViewPager {

	private FixedSpeedScroller fixedSpeedScroller;
	
	public SpeedControllableViewPager(Context context) {
		this(context, null);
	}
	
	public SpeedControllableViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public void init() {
		
		try {
		    Field mScroller;
		    mScroller = ViewPager.class.getDeclaredField("mScroller");
		    mScroller.setAccessible(true); 
		    Field interpolator = ViewPager.class.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);

		    fixedSpeedScroller = new FixedSpeedScroller(getContext(), 
		    		(Interpolator) interpolator.get(null));
		    mScroller.set(this, fixedSpeedScroller);
		} catch (NoSuchFieldException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
	}
	
	public void setDuration(int duration) {

		fixedSpeedScroller.setDuration(duration);
	}

	public class FixedSpeedScroller extends Scroller {

	    private int mDuration = 1000;

	    public FixedSpeedScroller(Context context) {
	        super(context);
	    }

	    public FixedSpeedScroller(Context context, Interpolator interpolator) {
	        super(context, interpolator);
	    }

	    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
		public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
	        super(context, interpolator, flywheel);
	    }

	    @Override
	    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
	        // Ignore received duration, use fixed one instead
	        super.startScroll(startX, startY, dx, dy, mDuration);
	    }

	    @Override
	    public void startScroll(int startX, int startY, int dx, int dy) {
	        // Ignore received duration, use fixed one instead
	        super.startScroll(startX, startY, dx, dy, mDuration);
	    }
	    
	    public void setDuration(int duration) {
	    	
	    	mDuration = duration;
	    }
	}
}
