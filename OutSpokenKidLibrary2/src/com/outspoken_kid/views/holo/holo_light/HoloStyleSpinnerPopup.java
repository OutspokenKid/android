package com.outspoken_kid.views.holo.holo_light;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

/**
 * HoloStyleSpinnerPopup.
 *
 * v1.0.1
 * 
 * @author HyungGunKim
 *
 * v1.0.1 - Hide titleText when title is null.
 *
 * For use this View,
 * 
 * 1. Set targetTextView.
 * 2. Set title.
 * 3. Add Strings.
 * 4. Call 'notifyDataSetChanged'.
 * 
 * That's all, so simple!
 */
public class HoloStyleSpinnerPopup extends FrameLayout {

	private OnItemClickedListener onItemClickedListener;
	private TextView targetTextView;
	private String title;
	private ArrayList<String> items = new ArrayList<String>();
	private LinearLayout popupLinear;
	
	public HoloStyleSpinnerPopup(Context context) {
		this(context, null);
	}
	
	public HoloStyleSpinnerPopup(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init() {
		this.setBackgroundColor(Color.argb(200, 0, 0, 0));
		this.setVisibility(View.INVISIBLE);
		this.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				hidePopup();
			}
		});
		
		popupLinear = new LinearLayout(getContext());
		popupLinear.setLayoutParams(new FrameLayout.LayoutParams(-2, -2, Gravity.CENTER));
		popupLinear.setOrientation(LinearLayout.VERTICAL);
		popupLinear.setClickable(true);
		popupLinear.setBackgroundResource(android.R.drawable.editbox_dropdown_dark_frame);
		this.addView(popupLinear);
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTitle() {
		
		return title;
	}

	public void addItem(String item) {
		
		if(!StringUtils.isEmpty(item)) {
			items.add(item);
		}
	}
	
	public void removeItem(String item) {
		
		if(!StringUtils.isEmpty(item)) {
			items.remove(item);
		}
	}
	
	public void clearItems() {
		
		items.clear();
	}

	public void notifyDataSetChanged() {
		
		popupLinear.removeAllViews();

		if(!StringUtils.isEmpty(title)) {
			TextView tvTitle = new TextView(getContext());
			ResizeUtils.viewResize(500, 80, tvTitle, 1, 0, null);
			tvTitle.setGravity(Gravity.CENTER);
			tvTitle.setText(title);
			tvTitle.setTextColor(HoloConstants.COLOR_HOLO_TARGET_ON);
			FontUtils.setFontSize(tvTitle, 35);
			FontUtils.setFontStyle(tvTitle, FontUtils.BOLD);
			popupLinear.addView(tvTitle);
			
			View blueLine = new View(getContext());
			blueLine.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 3));
			blueLine.setBackgroundColor(HoloConstants.COLOR_HOLO_TARGET_ON);
			popupLinear.addView(blueLine);
		}
		
		if(items == null || items.size() == 0) {
			View blank = new View(getContext());
			ResizeUtils.viewResize(500, 80, blank, 1, 0, null);
			popupLinear.addView(blank);
		} else {
			
			LinearLayout targetLinear = null;
			
			if(items.size() > 4) {
				ScrollView scrollView = new ScrollView(getContext());
				ResizeUtils.viewResize(500, LayoutParams.WRAP_CONTENT, scrollView, 1, 0, null);
				scrollView.setFillViewport(true);
				popupLinear.addView(scrollView);

				LinearLayout innerLayout = new LinearLayout(getContext());
				innerLayout.setOrientation(LinearLayout.VERTICAL);
				innerLayout.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
																		LayoutParams.WRAP_CONTENT));
				scrollView.addView(innerLayout);
				targetLinear = innerLayout;
			} else {
				targetLinear = popupLinear;
			}

			int size = items.size();
			for(int i=0; i<size; i++) {
				try {
					
					if(StringUtils.isEmpty(items.get(i))) {
						continue;
					}
					
					final String itemString = items.get(i);
					final int index = i;
					
					//Add item.
					TextView tvItem = new TextView(getContext());
					ResizeUtils.viewResize(500, 100, tvItem, 1, 0, null, new int[]{20, 0, 20, 0});
					tvItem.setText(itemString);
					tvItem.setGravity(Gravity.CENTER);
					tvItem.setTextColor(Color.rgb(220, 220, 220));
					FontUtils.setFontSize(tvItem, 32);
					FontUtils.setFontStyle(tvItem, FontUtils.BOLD);
					tvItem.setMaxLines(2);
					tvItem.setEllipsize(TruncateAt.END);
					tvItem.setBackgroundColor(Color.rgb(68, 68, 68));
					tvItem.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							hidePopup();

							if(getTargetTextView() != null) {
								getTargetTextView().setText(itemString);
							}
							
							if(onItemClickedListener != null) {
								onItemClickedListener.onItemClicked(index, itemString);
							}
						}
					});
					targetLinear.addView(tvItem);
					
					//Add lines.
					if(i != items.size() - 1) {
						View line1 = new View(getContext());
						line1.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1));
						line1.setBackgroundColor(Color.rgb(40, 40, 40));
						targetLinear.addView(line1);
						
						View line2 = new View(getContext());
						line2.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1));
						line2.setBackgroundColor(Color.rgb(80, 80, 80));
						targetLinear.addView(line2);
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void showPopup() {
		if(this.getVisibility() != View.VISIBLE) {
			AlphaAnimation aaIn = new AlphaAnimation(0, 1);
			aaIn.setDuration(200);
			this.setVisibility(View.VISIBLE);
			this.startAnimation(aaIn);
		}
	}
	
	public void hidePopup() {
		if(this.getVisibility() == View.VISIBLE) {
			AlphaAnimation aaOut = new AlphaAnimation(1, 0);
			aaOut.setDuration(200);
			this.setVisibility(View.INVISIBLE);
			this.startAnimation(aaOut);
		}
	}
	
	public void setTargetTextView(TextView textView) {
		
		this.targetTextView = textView;
	} 
	
	public TextView getTargetTextView() {
		
		return targetTextView;
	}
	
	public ArrayList<String> getItems() {
		
		return items;
	}
	
	public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
		
		this.onItemClickedListener = onItemClickedListener;
	}
	
//////////////// Interfaces.
	
	public interface OnItemClickedListener {
		
		public void onItemClicked(int position, String itemString);
	}
}
