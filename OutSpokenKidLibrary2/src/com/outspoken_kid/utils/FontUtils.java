package com.outspoken_kid.utils;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * v1.0.2
 * 
 * @author HyungGunKim
 * 
 * v1.0.2 - Add addSpan() etc.
 * v1.0.1 - Add setHintSize()
 */
public class FontUtils {
	
	public static final int NONE = 0, BOLD = 1, ITELIC = 2, UNDERLINE = 4;
	private static Typeface mTypeface;
	
	private int fontSize = 0;
	private int[] fontColor = null; 		//a r g b (0~255).
	private float[] shadow = null;		//radius, dx, dy, a, r, g, b (a,r,g,b).
	private int fontStyle = 0;			//0 : none.		1 : bold.		2 : itelic.		4 : underline.
	
	public FontUtils(int fontSize, int[] fontColor, float[] shadow, int fontStyle) {
		
		this.fontSize = fontSize;
		this.fontColor = fontColor;
		this.shadow = shadow;
		this.fontStyle = fontStyle;
	}
	
	public static void setFontSize(TextView textView, int fontSize) {
		
		if(textView == null) {
			return;
		}
		
		if(fontSize != 0) {
			textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResizeUtils.getSpecificLength(fontSize));
		}
	}
	
	public static void setFontColor(TextView textView, int[] fontColor) {
		
		if(textView == null) {
			return;
		}
		
		if(fontColor != null) {
			textView.setTextColor(Color.argb(fontColor[0], fontColor[1], fontColor[2], fontColor[3]));
		}
	}
	
	public static void setFontColor(TextView textView, int fontColor) {
		
		if(textView == null) {
			return;
		}
		
		textView.setTextColor(fontColor);
	}
	
	public static void setShadow(TextView textView, float[] shadow) {
		
		if(textView == null) {
			return;
		}
		
		if(shadow != null) {
			textView.setShadowLayer(shadow[0], shadow[1], shadow[2], 
					Color.argb((int)shadow[3], (int)shadow[4], (int)shadow[5], (int)shadow[6]));
		}
	}
	
	public static void setFontStyle(TextView textView, int fontStyle) {

		if(textView == null) {
			return;
		}
		
		int fs = fontStyle;
		String str = "";
		
		if(fs >= 4) {
			str += 1;
			fs -= 4;
		} else {
			str += 0;
		}
		
		if(fs >= 2) {
			str += 1;
			fs -= 2;
		} else {
			str += 0;
		}
		
		if(fs == 1) {
			str += 1;
		} else {
			str += 0;
		}

		char[] ar = new char[3];
		str.getChars(0, str.length(), ar, 0);

		if(ar[0] == '1') {
			textView.setText(Html.fromHtml("<u>" + textView.getText() + "</u>"));
		}
		
		if(ar[1] == '1') {
			textView.setTypeface(null, Typeface.ITALIC);
		}
		
		if(ar[2] == '1') {
			textView.setPaintFlags(textView.getPaintFlags()|Paint.FAKE_BOLD_TEXT_FLAG);
		}
	}
	
	public static void setTextView(TextView textView, FontUtils fontInfo) {
		
		if(textView == null || fontInfo == null) {
			return;
		}

		FontUtils.setFontSize(textView, fontInfo.getFontSize());
		FontUtils.setFontColor(textView, fontInfo.getFontColor());
		FontUtils.setShadow(textView, fontInfo.getShadow());
		FontUtils.setFontStyle(textView, fontInfo.getFontStyle());
	}

	public int getFontSize() {
		return fontSize;
	}

	public void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	
	public int[] getFontColor() {
		return fontColor;
	}

	public void setFontColor(int[] fontColor) {
		this.fontColor = fontColor;
	}

	public float[] getShadow() {
		return shadow;
	}

	public void setShadow(float[] shadow) {
		this.shadow = shadow;
	}

	public int getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(int fontStyle) {
		this.fontStyle = fontStyle;
	}

	public static void setFontAndHintSize(final EditText editText, final int fontSize, final int hintSize) {

		if(editText.getEditableText() == null || editText.getEditableText().length() == 0) {
			FontUtils.setFontSize(editText, hintSize);
		} else {
			FontUtils.setFontSize(editText, fontSize);
		}
		
		editText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				//Empty.
				if(s == null || s.length() == 0) {
					FontUtils.setFontSize(editText, hintSize);
					
				//First input.
				} else if(before == 0){
					FontUtils.setFontSize(editText, fontSize);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}
	
	/**
	 * 커스텀 폰트 적용.
	 * http://t.dittos.pe.kr/post/9665021933
	 * 
	 * 에서 퍼옴.
	 * 감사합니다!
	 */
	
	//Activity 전용.
	public static void setGlobalFont(Activity activity, int rootResId, String fontFileName) {
		
		try {
			ViewGroup root = (ViewGroup) LayoutInflater.from(activity).inflate(rootResId, null);
			
			if(mTypeface == null) {
				mTypeface = Typeface.createFromAsset(activity.getAssets(), fontFileName);
			}

			setGlobalFont(root);
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	//Fragment 전용.
	public static void setGlobalFont(Activity activity, View view, String fontFileName) {

		try {
			ViewGroup root = (ViewGroup) view;
			
			if(mTypeface == null) {
				mTypeface = Typeface.createFromAsset(activity.getAssets(), fontFileName);
			}

			setGlobalFont(root);
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public static void setGlobalFont(ViewGroup root) {
		
	    for (int i = 0; i < root.getChildCount(); i++) {
	        View child = root.getChildAt(i);
	        if (child instanceof TextView)
	            ((TextView)child).setTypeface(mTypeface);
	        else if (child instanceof ViewGroup)
	            setGlobalFont((ViewGroup)child);
	    }
	}

	public static void setGlobalFont(TextView textView) {
		
		textView.setTypeface(mTypeface);
	}

	public static void addSpan(TextView textView, String text, int color, float scale) {

		SpannableStringBuilder spb = new SpannableStringBuilder(text);
		
		if(color != 0) {
			spb.setSpan(new ForegroundColorSpan(color), 0, spb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		
		if(scale != 1) {
			spb.setSpan(new RelativeSizeSpan(scale), 0, spb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		
		textView.append(spb);
	}
}
