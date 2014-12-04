package com.outspoken_kid.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

/** 
 * Copyright 2013 Hyung Gun Kim.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * ===============================================
 *
 * @author rlagudrjs87@gmail.com
 * @version 1.2
 * 
 * v1.2 - 2013.10.11
 * Disable scrollLock when opended.
 * 
 * v1.1 - 2013.07.11
 * rightView is updated.
 * Need any help, come to 'http://OutSpokenKid.tistory.com/' or send mail to me.
 * 
 * GestureSlidingLayout extends FrameLayout.
 * When you use, add any layouts or views to GestureSlidingLayout using by xml or Java code.
 * You must set topView and you can set leftView, rightView or both.
 */
public class GestureSlidingLayout extends FrameLayout {
	
	protected static final int DIRECTION_CHECK = 0;
	protected static final int DIRECTION_HORIZONTAL = 1;
	protected static final int DIRECTION_VERTICAL = 2;
	
	protected static final int MODE_NONE = 0;
	protected static final int MODE_DRAG_LEFT = 1;
	protected static final int MODE_DRAG_RIGHT = 2;

	private static boolean isOpenToLeft;
	private static boolean isOpenToRight;
	
	/**
	 * If true, prevent horizontal scroll.
	 */
	private static boolean scrollLock;

	/**
	 *  Minimum width that start to horizontal scroll.
	 */
	private final int CRITERION = 50;

	/**
	 * You must set this view.
	 * 
	 * top View will slide.
	 */
	private View topView;
	
	/**
	 * You must set this view.
	 * 
	 * leftView is view under the topView.
	 */
	private View leftView;
	
	/**
	 * You must set this view.
	 * 
	 * rightView is view under the topView.
	 */
	private View rightView;
	
	/**
	 * You must set this view, it just 'View'.
	 * 
	 * Cover will carry touch events to leftView when opened.
	 */
	private int topViewWidth;
	private int leftViewWidth;
	private int rightViewWidth;
	private int direction;
	private int sideMenuMode;
	private float x0, x1, x2, y0, y1;
	private int scrollCount;
	private boolean isAnimating;
	
	/**
	 * Called before open animation start.
	 */
	protected OnBeforeOpenListener onBeforeOpenToLeftListener;
	
	/**
	 * Called after open animation finished.
	 */
	protected OnAfterOpenListener onAfterOpenToLeftListener;
	
	/**
	 * Called before open animation start.
	 */
	protected OnBeforeOpenListener onBeforeOpenToRightListener;
	
	/**
	 * Called after open animation finished.
	 */
	protected OnAfterOpenListener onAfterOpenToRightListener;
	
	/**
	 * Called before close animation start.
	 */
	protected OnBeforeCloseListener onBeforeCloseFromLeftListener;
	
	/**
	 * Called after close animation finished.
	 */
	protected OnAfterCloseListener onAfterCloseFromLeftListener;
	
	/**
	 * Called before close animation start.
	 */
	protected OnBeforeCloseListener onBeforeCloseFromRightListener;
	
	/**
	 * Called after close animation finished.
	 */
	protected OnAfterCloseListener onAfterCloseFromRightListener;
	
	public GestureSlidingLayout(Context context) {
		this(context, null, 0);
	}
	
	public GestureSlidingLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public GestureSlidingLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		
		if((!isOpenToLeft && !isOpenToRight && scrollLock) || isAnimating) {
			return false;
		}
		
		if(leftView == null && rightView == null) {
			scrollLock = true;
			return false;
		}
		
		if(leftView != null && leftViewWidth == 0) {
			leftViewWidth = leftView.getMeasuredWidth();
		}
		
		if(rightView != null && rightViewWidth == 0) {
			rightViewWidth = rightView.getMeasuredWidth();
		}
		
		if(isOpenToLeft){
			if(ev.getAction() == MotionEvent.ACTION_DOWN && ev.getX() > leftViewWidth) {
				sideMenuMode = MODE_DRAG_LEFT;
			} else {
				sideMenuMode = MODE_NONE;
			}
			return true;
		} else if(isOpenToRight) {
			if(ev.getAction() == MotionEvent.ACTION_DOWN && ev.getX() < topViewWidth - rightViewWidth) {
				sideMenuMode = MODE_DRAG_RIGHT;
			} else {
				sideMenuMode = MODE_NONE;
			}
			return true;
		} else {
			if(ev.getAction() == MotionEvent.ACTION_DOWN) {
				direction = DIRECTION_CHECK;
			}
			
			switch(direction) {
			
			case DIRECTION_HORIZONTAL:
				return true;
			case DIRECTION_VERTICAL:
				return false;
			case DIRECTION_CHECK:
				break;
			}
			
			switch(ev.getAction()) {
			
			case MotionEvent.ACTION_DOWN:
				x0 = ev.getX();
				y0 = ev.getY();
				scrollCount = 0;
				break;
			
			case MotionEvent.ACTION_MOVE:
				scrollCount++;
				x1 = ev.getX();
				y1 = ev.getY();

				float distX = Math.abs(x1 - x0);
				float distY = Math.abs(y1 - y0);
				
				if((distX > distY * 3 && distX > CRITERION /2)
						|| (distX > distY && distX > CRITERION)
						||(scrollCount >3 && distX > distY * 3)) {
					direction = DIRECTION_HORIZONTAL;
					
					if(x1 > x0) {
						
						if(leftView != null) {
							sideMenuMode = MODE_DRAG_LEFT;
							leftView.setVisibility(View.VISIBLE);
						}
					} else {
						if(rightView != null) {
							sideMenuMode = MODE_DRAG_RIGHT;
							rightView.setVisibility(View.VISIBLE);
						}
					}
					
					isOpenToLeft = false;
					isOpenToRight = false;
					
					if(sideMenuMode == MODE_DRAG_LEFT) {
						if(onBeforeOpenToLeftListener != null) {
							try {
								onBeforeOpenToLeftListener.onBeforeOpen();
							} catch(Exception e) {
								e.printStackTrace();
							} catch(OutOfMemoryError oom) {
								oom.printStackTrace();
							}
						}
					} else {
						if(onBeforeOpenToRightListener != null) {
							try {
								onBeforeOpenToRightListener.onBeforeOpen();
							} catch(Exception e) {
								e.printStackTrace();
							} catch(OutOfMemoryError oom) {
								oom.printStackTrace();
							}
						}
					}
					
				} else if(scrollCount > 3){
					direction = DIRECTION_VERTICAL;
					sideMenuMode = MODE_NONE;
				}
			}
		}
        return false;
	}
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		if(isAnimating) {
			return false;
		}
		
		x2 = (int) event.getX();
		
		if(isOpenToLeft && sideMenuMode == MODE_NONE) {
			leftView.dispatchTouchEvent(event);
		} else if(isOpenToRight && sideMenuMode == MODE_NONE) {
			rightView.dispatchTouchEvent(event);
		} else {
			switch(event.getAction()) {
			
			case MotionEvent.ACTION_DOWN:
				if(isOpenToLeft && x2 > leftViewWidth) {
					sideMenuMode = MODE_DRAG_LEFT;
					x0 = x2;
					x1 = x2;
					topView.clearAnimation();
					topView.scrollTo(-leftViewWidth, 0);
				} else if(isOpenToRight && x2 < rightViewWidth) {
					sideMenuMode = MODE_DRAG_RIGHT;
					x0 = x2;
					x1 = x2;
					topView.clearAnimation();
					topView.scrollTo(rightViewWidth, 0);
				} else {
					sideMenuMode = MODE_NONE;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if(sideMenuMode == MODE_DRAG_LEFT || sideMenuMode == MODE_DRAG_RIGHT) {
					int dist = (int)(x1 - x2);
					scrollTopView(dist);
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				if(sideMenuMode == MODE_DRAG_LEFT) {
					if(x0 < x2) {
						open(true, null);
					} else {
						close(true, null);
					}
				} else if(sideMenuMode == MODE_DRAG_RIGHT) {
					if(x0 > x2) {
						open(false, null);
					} else {
						close(false, null);
					}
				}
				break;
			}

			x0 = x1;
			x1 = x2;
		}
		
		return true;
	}
	
	public void scrollTopView(int dist) {

		if(dist == 0) {
			return;
		}
		
		int scX = -topView.getScrollX();
		
		if(sideMenuMode == MODE_DRAG_LEFT && scX - dist >= 0 && scX - dist <= leftViewWidth) {
			topView.scrollBy(dist, 0);
		} else if(sideMenuMode == MODE_DRAG_RIGHT && scX - dist >= -rightViewWidth && scX - dist <= 0) {
			topView.scrollBy(dist, 0);
		}
	}

	/**
	 * Start translate animation to open. 
	 * 
	 * @param doAfter	: Input OnAfterOpenListener when you want to do something more at open animation finished.
	 */
	public void open(final boolean toLeft, final OnAfterOpenListener doAfter) {
		
		if(topView == null
				|| (toLeft && leftView == null)
				|| (!toLeft && rightView == null)
				|| isAnimating 
				|| (isOpenToLeft && sideMenuMode != MODE_DRAG_LEFT)
				|| (isOpenToRight && sideMenuMode != MODE_DRAG_RIGHT)) {
			return;
		}
		
		if(topView != null && topViewWidth == 0) {
			topViewWidth = topView.getMeasuredWidth();
		}
		
		if(leftView != null && leftViewWidth == 0) {
			leftViewWidth = leftView.getMeasuredWidth();
		}
		
		if(rightView != null && rightViewWidth == 0) {
			rightViewWidth = rightView.getMeasuredWidth();
		}
		
		if(toLeft) {
			if(onBeforeOpenToLeftListener != null) {
				try {
					onBeforeOpenToLeftListener.onBeforeOpen();
				} catch(Exception e) {
					e.printStackTrace();
				} catch(OutOfMemoryError oom) {
					oom.printStackTrace();
				}
			}
		} else {
			if(onBeforeOpenToRightListener != null) {
				try {
					onBeforeOpenToRightListener.onBeforeOpen();
				} catch(Exception e) {
					e.printStackTrace();
				} catch(OutOfMemoryError oom) {
					oom.printStackTrace();
				}
			}
		}
	
		if(toLeft) {
			isOpenToLeft = true;
			leftView.setVisibility(View.VISIBLE);
		} else {
			isOpenToRight = true;
			rightView.setVisibility(View.VISIBLE);
		}
		isAnimating = true;
		float scX = topView.getScrollX();
		float endX = 0;
		
		if(scX == 0) {
			endX = (float)(toLeft?leftViewWidth:-rightViewWidth) / (float)topViewWidth;
		} else {
			endX = (float)(toLeft?scX + leftViewWidth : scX - rightViewWidth) / (float)topViewWidth;
		}

		TranslateAnimation taOut = new TranslateAnimation(
				TranslateAnimation.RELATIVE_TO_PARENT, 0,
				TranslateAnimation.RELATIVE_TO_PARENT, endX,
				TranslateAnimation.RELATIVE_TO_PARENT, 0,
				TranslateAnimation.RELATIVE_TO_PARENT, 0
			);
		taOut.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {}
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				isAnimating = false;
				sideMenuMode = MODE_NONE;
				
				if(toLeft) {
					if(onAfterOpenToLeftListener != null) {
						try {
							onAfterOpenToLeftListener.onAfterOpen();
						} catch(Exception e) {
							e.printStackTrace();
						} catch(OutOfMemoryError oom) {
							oom.printStackTrace();
						}
					}
				} else {
					if(onAfterOpenToRightListener != null) {
						try {
							onAfterOpenToRightListener.onAfterOpen();
						} catch(Exception e) {
							e.printStackTrace();
						} catch(OutOfMemoryError oom) {
							oom.printStackTrace();
						}
					}
				}
				
				if(doAfter != null) {
					try {
						doAfter.onAfterOpen();
					} catch(Exception e) {
						e.printStackTrace();
					} catch(OutOfMemoryError oom) {
						oom.printStackTrace();
					}
				}
			}
		});
		taOut.setFillAfter(true);
		taOut.setInterpolator(getContext(), android.R.anim.accelerate_decelerate_interpolator);
		taOut.setDuration(250);
		topView.startAnimation(taOut);
	}
	
	/**
	 * Start translate animation to close.
	 * 
	 * @param doAfter	: Input OnAfterCloseListener when you want to do something more at close animation finished.
	 */
	public void close(final boolean fromLeft, final OnAfterCloseListener doAfter) {
		
		if(topView == null
				|| (fromLeft && leftView == null)
				|| (!fromLeft && rightView == null)
				|| isAnimating 
				|| (topView.getScrollX() == 0 && (sideMenuMode == MODE_DRAG_LEFT || sideMenuMode == MODE_DRAG_RIGHT))) {
			return;
		}
		
		if(topViewWidth == 0) {
			topViewWidth = topView.getMeasuredWidth();
		}
		
		if(leftView != null && leftViewWidth == 0) {
			leftViewWidth = leftView.getMeasuredWidth();
		}
		
		if(rightView != null && rightViewWidth == 0) {
			rightViewWidth = rightView.getMeasuredWidth();
		}
		
		if(fromLeft) {
			if(onBeforeCloseFromLeftListener != null) {
				try {
					onBeforeCloseFromLeftListener.onBeforeClose();
				} catch(Exception e) {
					e.printStackTrace();
				} catch(OutOfMemoryError oom) {
					oom.printStackTrace();
				}
			}
		} else {
			if(onBeforeCloseFromRightListener != null) {
				try {
					onBeforeCloseFromRightListener.onBeforeClose();
				} catch(Exception e) {
					e.printStackTrace();
				} catch(OutOfMemoryError oom) {
					oom.printStackTrace();
				}
			}
		}
		
		isAnimating = true;
		float offset = (float)-topView.getScrollX() / (float)topViewWidth;
		
		if(offset == 0) {
			offset = (float)(fromLeft?leftViewWidth:-rightViewWidth) / (float)topViewWidth;
		} else {
			topView.scrollTo(0, 0);
		}
		
		TranslateAnimation taIn = new TranslateAnimation(
				TranslateAnimation.RELATIVE_TO_PARENT, offset,
				TranslateAnimation.RELATIVE_TO_PARENT, 0,
				TranslateAnimation.RELATIVE_TO_PARENT, 0,
				TranslateAnimation.RELATIVE_TO_PARENT, 0
			);
		taIn.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				topView.clearAnimation();

				isAnimating = false;
				sideMenuMode = MODE_NONE;
				
				if(fromLeft) {
					isOpenToLeft = false;
					leftView.setVisibility(View.INVISIBLE);
					
					if(onAfterCloseFromLeftListener != null) {
						try {
							onAfterCloseFromLeftListener.onAfterClose();
						} catch(Exception e) {
							e.printStackTrace();
						} catch(OutOfMemoryError oom) {
							oom.printStackTrace();
						}
					}
				} else {
					isOpenToRight = false;
					rightView.setVisibility(View.INVISIBLE);
					
					if(onAfterCloseFromRightListener != null) {
						try {
							onAfterCloseFromRightListener.onAfterClose();
						} catch(Exception e) {
							e.printStackTrace();
						} catch(OutOfMemoryError oom) {
							oom.printStackTrace();
						}
					}
				}
				
				if(doAfter != null) {
					try {
						doAfter.onAfterClose();
					} catch(Exception e) {
						e.printStackTrace();
					} catch(OutOfMemoryError oom) {
						oom.printStackTrace();
					}
				}
			}
		});
		taIn.setDuration(200);
		taIn.setInterpolator(getContext(), android.R.anim.accelerate_decelerate_interpolator);
		topView.startAnimation(taIn);
	}
	
/////////////////////// Get, Set methods.
	
	public void setTopView(View topView) {
		this.topView = topView;
	}
	
	public void setLeftView(View leftView) {
		this.leftView = leftView;
		leftView.setVisibility(View.INVISIBLE);
	}
	
	public void setRightView(View rightView) {
		this.rightView = rightView;
		rightView.setVisibility(View.INVISIBLE);
	}
	
	/**
	 * Return sliding status. 
	 * 
	 * @return	Return true when top view is sliding or opened, false when top view is closed.
	 */
	public static boolean isOpenToLeft() {
		return isOpenToLeft;
	}
	
	/**
	 * Return sliding status. 
	 * 
	 * @return	Return true when top view is sliding or opened, false when top view is closed.
	 */
	public static boolean isOpenToRight() {
		return isOpenToRight;
	}

	/**
	 * You don't use this method.
	 * This will only used in this Class.
	 * 
	 * @param isOpen
	 */
	protected static void setOpenToLeft(boolean isOpen) {
		GestureSlidingLayout.isOpenToLeft = isOpen;
	}
	
	/**
	 * You don't use this method.
	 * This will only used in this Class.
	 * 
	 * @param isOpen
	 */
	protected static void setOpenToRight(boolean isOpen) {
		GestureSlidingLayout.isOpenToRight = isOpen;
	}
	
	/**
	 * Return scrollLock status.
	 * 
	 * @return Return true when you prevent scroll, false you can scroll.
	 */
	public static boolean isScrollLock() {
		return scrollLock;
	}

	/**
	 * If you wanna prevent horizontal scroll, set this true.
	 * 
	 * @param scrollLock	Set true will prevent scroll, set false will make top view horizontal scrollable. 
	 */
	public static void setScrollLock(boolean scrollLock) {
		GestureSlidingLayout.scrollLock = scrollLock;
	}

	public void setOnBeforeOpenToLeftListener(OnBeforeOpenListener onBeforeOpenListener) {
		this.onBeforeOpenToLeftListener = onBeforeOpenListener;
	}
	
	public void setOnBeforeOpenToRightListener(OnBeforeOpenListener onBeforeOpenListener) {
		this.onBeforeOpenToRightListener = onBeforeOpenListener;
	}

	public void setOnAfterOpenToLeftListener(OnAfterOpenListener onAfterOpenListener) {
		this.onAfterOpenToLeftListener = onAfterOpenListener;
	}
	
	public void setOnAfterOpenToRightListener(OnAfterOpenListener onAfterOpenListener) {
		this.onAfterOpenToRightListener = onAfterOpenListener;
	}

	public void setOnBeforeCloseFromLeftListener(OnBeforeCloseListener onBeforeCloseListener) {
		this.onBeforeCloseFromLeftListener = onBeforeCloseListener;
	}
	
	public void setOnBeforeCloseFromRightListener(OnBeforeCloseListener onBeforeCloseListener) {
		this.onBeforeCloseFromRightListener = onBeforeCloseListener;
	}

	public void setOnAfterCloseFromLeftListener(OnAfterCloseListener onAfterCloseListener) {
		this.onAfterCloseFromLeftListener = onAfterCloseListener;
	}
	
	public void setOnAfterCloseFromRightListener(OnAfterCloseListener onAfterCloseListener) {
		this.onAfterCloseFromRightListener = onAfterCloseListener;
	}

/////////////////////// Interfaces.

	protected boolean isAnimating() {
		return isAnimating;
	}

	protected void setAnimating(boolean isAnimating) {
		this.isAnimating = isAnimating;
	}
	
	public interface OnBeforeOpenListener {
		
		public void onBeforeOpen();
	}
	
	public interface OnAfterOpenListener {
		
		public void onAfterOpen();
	}
	
	public interface OnBeforeCloseListener {
		
		public void onBeforeClose();
	}
	
	public interface OnAfterCloseListener {
		
		public void onAfterClose();
	}
}
