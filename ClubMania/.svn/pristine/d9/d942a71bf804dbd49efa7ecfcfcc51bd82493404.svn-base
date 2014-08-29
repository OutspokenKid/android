package com.outspoken_kid.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ResizeUtils {
	
	private static float scale;
	private static int screenWidth;
	private static int screenHeight;
	private static int screenDensityDpi;
	private static int baseWidth;
	private static int statusBarHeight;
	
	public static void setBasicValues(Activity activity, int _baseWidth) {
		
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		setScreenDensityDpi(metrics.densityDpi);
		
		scale = (float) screenWidth / (float) _baseWidth;
		baseWidth = _baseWidth;
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
			
			if(_padding != null) {
				for(int i=0; i<4; i++) {
					newPadding[i] = ((int)((float)_padding[i]*scale));
				}
			}
			
			view.setPadding(newPadding[0], newPadding[1], newPadding[2], newPadding[3]);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void setMargin(View view, int[] _margin) {

		int[] newMargin = new int[4];
		
		if(_margin != null) {
			for(int i=0; i<4; i++) {
				if(_margin[i] != -1) {
					newMargin[i] = ((int)((float)_margin[i]*scale));
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
	
	public static void setSize(View view, int width, int height) {
		
		int scaledWidth = 0;
		int scaledHeight = 0;
		
		if(width != LayoutParams.MATCH_PARENT && width != LayoutParams.WRAP_CONTENT) {
			scaledWidth = (int) ( (float) width * scale);
		
		} else {
			scaledWidth = width;
		}
		
		if(height != LayoutParams.MATCH_PARENT && height != LayoutParams.WRAP_CONTENT) {
			scaledHeight = (int) ( (float) height * scale);
		
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
			scaledWidth = (int) ( (float) _width * scale);
		
		} else {
			scaledWidth = _width;
		}
		
		if(_height != LayoutParams.MATCH_PARENT && _height != LayoutParams.WRAP_CONTENT) {
			scaledHeight = (int) ( (float) _height * scale);
		
		} else {
			scaledHeight = _height;
		}
		
		int[] newMargin = new int[4];
		
		if(_margin != null) {
			for(int i=0; i<4; i++) {
				if(_margin[i] != -1) {
					newMargin[i] = ( (int) ( (float) _margin[i]*scale));
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
	
	public static void viewResize(int width, int height, View view, int[] margin, int[] padding, int[] rules, int[] targets) {
				
		int scaledWidth;
		int scaledHeight;
		
		if(width != LayoutParams.MATCH_PARENT && width != LayoutParams.WRAP_CONTENT) {
			scaledWidth = (int) ( (float) width * scale);
		
		} else {
			scaledWidth = width;
		}
		
		if(height != LayoutParams.MATCH_PARENT && height != LayoutParams.WRAP_CONTENT) {
			scaledHeight = (int) ( (float) height * scale);
		
		} else {
			scaledHeight = height;
		}

		RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(scaledWidth, scaledHeight);
		
		if(margin != null) {
			int[] newMargin = new int[4];
			
			for(int i=0; i<4; i++) {
				newMargin[i] = ( (int) ( (float) margin[i]*scale));
			}
			rp.setMargins(newMargin[0], newMargin[1], newMargin[2], newMargin[3]);
		}
		
		if(padding != null) {
			setPadding(view, padding);
		}

		int size = rules.length;
		for(int i=0; i<size; i++) {

			try {
				if(targets[i] != -1) {
					rp.addRule(rules[i]);
				} else {
					rp.addRule(rules[i], targets[i]);
				}
			} catch(Exception e) {}
		}
		
		view.setLayoutParams(rp);
	}

	public static float getScale() {
		
		return scale;
	}
	
	public static int getSpecificLength(int _length) {
		
		return (int) ( (float) _length * scale);
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
