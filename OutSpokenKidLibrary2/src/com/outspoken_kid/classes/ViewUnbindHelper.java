package com.outspoken_kid.classes;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.outspoken_kid.views.OffsetScrollView;
 
/**
 * Utility class to help unbinding resources consumed by Views in Activity.
 * 
 * @author Hwan Jo(nimbusob@gmail.com)
 */
public class ViewUnbindHelper {
    /**
     * Removes the reference to the activity from every view in a view hierarchy
     * (listeners, images etc.). This method should be called in the onDestroy() method
     * of each activity.
     * This code may stinks, but better than worse - suspiciously, Android framework
     * does not free resources immediately which are consumed by Views and this leads to
     * OutOfMemoryError sometimes although there are no user mistakes.
     * 
     * @param view View to free from memory
     * @see http://code.google.com/p/android/issues/detail?id=8488
     */
    public static void unbindReferences(View view) {
        try {
            if (view != null) {
                unbindViewReferences(view);
                if (view instanceof ViewGroup) {
                    unbindViewGroupReferences((ViewGroup)view);
                }
            }
        } catch (Exception ignore) {
            /* whatever exception is thrown just ignore it because a crash is
             * always worse than this method not doing what it's supposed to do
             */
        }
    }
 
    /**
     * Removes the reference to the activity from every view in a view hierarchy
     * (listeners, images etc.). This method should be called in the onDestroy() method
     * of each activity.
     * This code may stinks, but better than worse - suspiciously, Android framework
     * does not free resources immediately which are consumed by Views and this leads to
     * OutOfMemoryError sometimes although there are no user mistakes.
     * 
     * @param view View to free from memory
     * @see http://code.google.com/p/android/issues/detail?id=8488
     */
    public static void unbindReferences(Activity activity, int viewID) {
        try {
        	
            View view = activity.findViewById(viewID);
            if (view != null) {
                unbindViewReferences(view);
                if (view instanceof ViewGroup) {
                    unbindViewGroupReferences((ViewGroup)view);
                }
            }
        } catch (Exception ignore) {
            /* whatever exception is thrown just ignore it because a crash is
             * always worse than this method not doing what it's supposed to do.
             */
        }
    }
 
    private static void unbindViewGroupReferences(ViewGroup viewGroup) {
        int nrOfChildren = viewGroup.getChildCount();
        for (int i = 0; i < nrOfChildren; i++) {
            View view = viewGroup.getChildAt(i);
            unbindViewReferences(view);
            if (view instanceof ViewGroup) {
                unbindViewGroupReferences((ViewGroup)view);
            }
        }
 
        try {
            viewGroup.removeAllViews();
        } catch (Exception ignore) {
            // AdapterViews, ListViews and potentially other ViewGroups don't support the removeAllViews operation
        }
    }
 
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressWarnings("deprecation")
	private static void unbindViewReferences(View view) {
 
        // Set everything to null (API Level 8)
        try {
            view.setOnClickListener(null);
        } catch (Exception ignore) {}
 
        try {
            view.setOnCreateContextMenuListener(null);
        } catch (Exception ignore) {}
 
        try {
            view.setOnFocusChangeListener(null);
        } catch (Exception ignore) {}
 
        try {
            view.setOnKeyListener(null);
        } catch (Exception ignore) {}
 
        try {
            view.setOnLongClickListener(null);
        } catch (Exception ignore) {}
 
        try {
            view.setOnClickListener(null);
        } catch (Exception ignore) {}
        
        try {
            view.setTouchDelegate(null);
        } catch (Exception ignore) {}
 
        Drawable d = view.getBackground();
        if (d != null) {
            try {
                d.setCallback(null);
            } catch (Exception ignore) {}
        }
         
        if (view instanceof ImageView) {
        	
            ImageView imageView = (ImageView)view;
            d = imageView.getDrawable();
            imageView.setImageDrawable(null);
            imageView.setImageBitmap(null);
            
            if (d != null) {
                d.setCallback(null);
            }
             
            if (d instanceof BitmapDrawable) {
                Bitmap bm = ((BitmapDrawable)d).getBitmap();
                
                if(bm != null && !bm.isRecycled()) {
                	bm.recycle();
                }
            }
            
        } else if (view instanceof WebView) {
            ((WebView)view).destroyDrawingCache();
            ((WebView)view).destroy();
        } else if(view instanceof ListView) {
        	((ListView)view).setOnScrollListener(null);
        	((ListView)view).setOnItemClickListener(null);
        } else if(view instanceof GridView) {
        	((GridView)view).setOnScrollListener(null);
        	((GridView)view).setOnItemClickListener(null);
        } else if(view instanceof ViewPager) {
        	ViewPager viewPager = (ViewPager)view;
        	viewPager.setOnPageChangeListener(null);
        	
        	int size = ((ViewPager)view).getChildCount();
    		for(int i=0; i<size; i++) {
    			ViewUnbindHelper.unbindReferences(viewPager.getChildAt(i));
    		}
        } else if(view instanceof SwipeRefreshLayout) {
        	((SwipeRefreshLayout)view).setOnRefreshListener(null);
        } else if(view instanceof OffsetScrollView) {
        	((OffsetScrollView)view).setOnScrollChangedListener(null);
        } else if(view instanceof EditText) {
        	((EditText)view).setOnFocusChangeListener(null);
        }
 
        try {
    	    if (android.os.Build.VERSION.SDK_INT >= 16) {
    	    	view.setBackground(null);
    	    } else {
    	    	view.setBackgroundDrawable(null);
    	    }
        } catch (Exception ignore) {}
         
        try {
            view.setAnimation(null);
        } catch (Exception ignore) {}
 
        try {
            view.setContentDescription(null);
        } catch (Exception ignore) {}
 
        try {
            view.setTag(null);
        } catch (Exception ignore) {}
    }
}
