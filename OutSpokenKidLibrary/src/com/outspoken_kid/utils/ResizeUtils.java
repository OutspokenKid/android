package com.outspoken_kid.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * v1.0.1
 * 
 * @author HyungGunKim
 * v1.0.1 Remove method for RelativeLayout, optimization(Edit viewResize() and getSpecificLength(), create calculate()).
 */
public class ResizeUtils {
	
	private static int[] index;
	
	private static float scale;
	private static int screenWidth;
	private static int screenHeight;
	private static int screenDensityDpi;
	private static int baseWidth;
	private static int statusBarHeight;
	
	public static void setBasicValues(Activity activity, int _baseWidth) {
		
		if(baseWidth == 0) {
			DisplayMetrics metrics = new DisplayMetrics();
			activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
			screenWidth = metrics.widthPixels;
			screenHeight = metrics.heightPixels;
			setScreenDensityDpi(metrics.densityDpi);
			
			scale = (float) screenWidth / (float) _baseWidth;
			baseWidth = _baseWidth;
			index = new int[_baseWidth + 1];
		}
	}
	
	public static void setStatusBarHeight(Activity activity) {

		Window window = activity.getWindow();
		Rect rectgle= new Rect();
		window.getDecorView().getWindowVisibleDisplayFrame(rectgle);
		statusBarHeight= rectgle.top;
	}
	
	public static void setPadding(View view, int[] _padding) {
		
		try {
			int[] newPadding = new int[4];
			
			if(_padding != null && _padding.length == 4) {
				for(int i=0; i<4; i++) {
					newPadding[i] = getSpecificLength(_padding[i]);
				}
			}
			
			view.setPadding(newPadding[0], newPadding[1], newPadding[2], newPadding[3]);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void setMargin(View view, int[] _margin) {

		int[] newMargin = new int[4];
		
		if(_margin != null && _margin.length == 4) {
			for(int i=0; i<4; i++) {
				if(_margin[i] != -1) {
					newMargin[i] = getSpecificLength(_margin[i]);
				} else {
					newMargin[i] = -1;
				}
			}
		}
		
		if(view.getLayoutParams() instanceof LinearLayout.LayoutParams) {
			LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
			
			if(newMargin[0] != -1) {
				lp.leftMargin = newMargin[0];
			}
			
			if(newMargin[1] != -1) {
				lp.topMargin = newMargin[1];
			}
			
			if(newMargin[2] != -1) {
				lp.rightMargin = newMargin[2];
			}
			
			if(newMargin[3] != -1) {
				lp.bottomMargin = newMargin[3];
			}
			view.setLayoutParams(lp);
		} else if(view.getLayoutParams() instanceof FrameLayout.LayoutParams) {
			FrameLayout.LayoutParams fp = (FrameLayout.LayoutParams) view.getLayoutParams();
			
			if(newMargin[0] != -1) {
				fp.leftMargin = newMargin[0];
			}
			
			if(newMargin[1] != -1) {
				fp.topMargin = newMargin[1];
			}
			
			if(newMargin[2] != -1) {
				fp.rightMargin = newMargin[2];
			}
			
			if(newMargin[3] != -1) {
				fp.bottomMargin = newMargin[3];
			}
			view.setLayoutParams(fp);
		}
	}
	
	public static void setSizes(View view, int width, int height) {
		
		int scaledWidth = 0;
		int scaledHeight = 0;
		
		if(width != LayoutParams.MATCH_PARENT && width != LayoutParams.WRAP_CONTENT) {
			scaledWidth = getSpecificLength(width);
		} else {
			scaledWidth = width;
		}
		
		if(height != LayoutParams.MATCH_PARENT && height != LayoutParams.WRAP_CONTENT) {
			scaledHeight = getSpecificLength(height);
		} else {
			scaledHeight = height;
		}
		
		LayoutParams p = view.getLayoutParams();
		p.width = scaledWidth;
		p.height = scaledHeight;
		view.setLayoutParams(p);
	}
	
	public static void setGravity(View view, int gravity) {
		
		if(view.getLayoutParams() instanceof LinearLayout.LayoutParams) {
			((LinearLayout.LayoutParams)view.getLayoutParams()).gravity = gravity;
		} else if(view.getLayoutParams() instanceof FrameLayout.LayoutParams) {
			((FrameLayout.LayoutParams)view.getLayoutParams()).gravity = gravity;
		}
	}
	
	public static void viewResize(int _width, int _height, View _view, int _parentType, int _gravity, int[] _margin) {

		LinearLayout.LayoutParams lp;
		FrameLayout.LayoutParams fp;
		
		int scaledWidth;
		int scaledHeight;
		
		if(_width != LayoutParams.MATCH_PARENT && _width != LayoutParams.WRAP_CONTENT) {
			scaledWidth = getSpecificLength(_width);
		} else {
			scaledWidth = _width;
		}
		
		if(_height != LayoutParams.MATCH_PARENT && _height != LayoutParams.WRAP_CONTENT) {
			scaledHeight = getSpecificLength(_height);
		} else {
			scaledHeight = _height;
		}
		
		int[] newMargin = new int[4];
		
		if(_margin != null && _margin.length == 4) {
			for(int i=0; i<4; i++) {
				if(_margin[i] != -1) {
					newMargin[i] = getSpecificLength(_margin[i]);;
				} else {
					newMargin[i] = -1;
				}
			}
		}
		
		if(_parentType == 1) {
			lp = new LinearLayout.LayoutParams(scaledWidth, scaledHeight);
			
			if(_gravity != 0)
				lp.gravity = _gravity;
			
			if(_margin != null) {
				if(newMargin[0] != -1) {
					lp.leftMargin = newMargin[0];
				}
				
				if(newMargin[1] != -1) {
					lp.topMargin = newMargin[1];
				}
				
				if(newMargin[2] != -1) {
					lp.rightMargin = newMargin[2];
				}
				
				if(newMargin[3] != -1) {
					lp.bottomMargin = newMargin[3];
				}
			}
			
			_view.setLayoutParams(lp);
			
		} else if(_parentType == 2){
			fp = new FrameLayout.LayoutParams(scaledWidth, scaledHeight);
		
			if(_gravity != 0)
				fp.gravity = _gravity;
			
			if(_margin != null) {
				if(newMargin[0] != -1) {
					fp.leftMargin = newMargin[0];
				}
				
				if(newMargin[1] != -1) {
					fp.topMargin = newMargin[1];
				}
				
				if(newMargin[2] != -1) {
					fp.rightMargin = newMargin[2];
				}
				
				if(newMargin[3] != -1) {
					fp.bottomMargin = newMargin[3];
				}
			}
			
			_view.setLayoutParams(fp);
		}
	}
	
	public static void viewResize(int _width, int _height, View _view, int _parentType, 
			int _gravity, int[] _margin, int[] _padding) {

		viewResize(_width, _height, _view, _parentType, _gravity, _margin);
		
		
		if(_padding != null) {
			setPadding(_view, _padding);
		}
	}

	public static float getScale() {
		
		return scale;
	}
	
	public static int getSpecificLength(int length) {

		//length = 0
		if(length == 0) {
			return 0;
			
		//0 < length <= baseWidth 
		} else if(length > 0 && length <= baseWidth) {
			
			if(index[length] == 0) {
				index[length] = calculate(length);
			}
			
			return index[length];
			
		//-baseWidth <= length < 0
		} else if(length < 0 && length >= -baseWidth) {
			
			if(index[-length] == 0) {
				index[-length] = calculate(-length);
			}
			
			return -index[-length];
			
		//Out of bound.
		} else{
			return calculate(length);
		}
	}
	
	public static int calculate(int length) {
		
		return (int)((float) length * scale);
	}
	
	public static int getScreenWidth() {
		
		return screenWidth;
	}
	
	public static int getScreenHeight() {
		return screenHeight;
	}
	
	public static int getBaseWidth() {
		
		return baseWidth;
	}

	public static int getScreenDensityDpi() {
		return screenDensityDpi;
	}

	public static void setScreenDensityDpi(int screenDensityDpi) {
		ResizeUtils.screenDensityDpi = screenDensityDpi;
	}

	public static int getStatusBarHeight() {
		return statusBarHeight;
	}

	public static void setStatusBarHeight(int statusBarHeight) {
		ResizeUtils.statusBarHeight = statusBarHeight;
	}
}
