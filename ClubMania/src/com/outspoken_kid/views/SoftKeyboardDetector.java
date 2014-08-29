package com.outspoken_kid.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class SoftKeyboardDetector extends View {
	
	private int maxHeight;
	private OnShownSoftKeyboardListener onShownSoftKeyboardListener;
	private OnHiddenSoftKeyboardListener onHiddenSoftKeyBoardListener;
	
	public SoftKeyboardDetector(Context context) {
		super(context, null, 0);
	}
	
	public SoftKeyboardDetector(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
	}
	
	public SoftKeyboardDetector(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		
		if(oldw + oldh == 0) {
			maxHeight = h;
		} else if(h > maxHeight) {
			maxHeight = h;
		} else {
			if(h != maxHeight) {
				if(onShownSoftKeyboardListener != null) {
					onShownSoftKeyboardListener.onShownSoftKeyboard();
				}
			} else {
				if(onHiddenSoftKeyBoardListener != null) {
					onHiddenSoftKeyBoardListener.onHiddenSoftKeyboard();
				}
			}
		}
		
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	public interface OnShownSoftKeyboardListener {
		public void onShownSoftKeyboard();
	}
	
	public interface OnHiddenSoftKeyboardListener {
		public void onHiddenSoftKeyboard();
	}
	
	public void setOnShownKeyboardListener(OnShownSoftKeyboardListener listener) {
		onShownSoftKeyboardListener = listener;
	}
	
	public void setOnHiddenKeyboardListener(OnHiddenSoftKeyboardListener listener) {
		onHiddenSoftKeyBoardListener = listener;
	}
}
