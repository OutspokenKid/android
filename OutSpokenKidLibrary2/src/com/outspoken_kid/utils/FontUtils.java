package com.outspoken_kid.utils;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.TextView;


/**
 * v1.0.1
 * 
 * @author HyungGunKim
 * 
 * v1.0.1 - Add setHintSize()
 */
public class FontUtils {
	
	public static final int NONE = 0, BOLD = 1, ITELIC = 2, UNDERLINE = 4;
	
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
}
