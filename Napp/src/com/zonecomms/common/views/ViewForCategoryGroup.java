package com.zonecomms.common.views;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.utils.ResizeUtils;
import com.zonecomms.common.models.GridMenu;
import com.zonecomms.common.models.GridMenuGroup;
import com.zonecomms.napp.R;

public class ViewForCategoryGroup extends RelativeLayout {

	private static int madeCount = 870901;
	private static int l;
	private static int s;
	
	private GridMenuGroup gridMenuGroup;
	private boolean foldable;
	private boolean folded;
	
	private View viewForExpand;
	private TextView[] views;
	
	private OnMenuClickedListener onMenuClickedListener;
	
	public ViewForCategoryGroup(Context context) {
		super(context);
		
		if(l == 0) {
			l = ResizeUtils.getSpecificLength(150);
		}
		
		if(s == 0) {
			s = ResizeUtils.getSpecificLength(8);
		}
	}
	
	public void setGridMenuGroup(GridMenuGroup gridMenuGroup, boolean bigLeft) {
		
		this.gridMenuGroup = gridMenuGroup;
		foldable = gridMenuGroup.getGridMenus().size() > 4;
		folded = true;
		createViews(bigLeft);
		setViews();
	}
	
	public void createViews(boolean bigLeft) {
	
		int size = gridMenuGroup.getGridMenus().size();
		int madeSize = ((size-1)/4 + 1)*4;
		views = new TextView[madeSize];
		RelativeLayout.LayoutParams rp = null;
		
		madeCount += 100;
		
		//id : 0.
		TextView big = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(l*2 + s, l*2 + s);
		rp.addRule(ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		rp.leftMargin = bigLeft? 0 : l*2 + s*2;
		big.setLayoutParams(rp);
		big.setId(madeCount);
		big.setBackgroundColor(gridMenuGroup.getColor());
		big.setText(gridMenuGroup.getText());
		big.setTextColor(Color.WHITE);
		big.setGravity(Gravity.CENTER);
		big.setMaxLines(2);
		big.setEllipsize(TruncateAt.END);
		FontInfo.setFontSize(big, 50);
		this.addView(big);
		
		for(int i=0; i<madeSize; i++) {
			
			rp = new RelativeLayout.LayoutParams(l, l);
			
			switch(i) {
			case 0:
				if(bigLeft) {
					rp.addRule(RIGHT_OF, madeCount);
					rp.leftMargin = s;
				} else {
					rp.addRule(ALIGN_PARENT_LEFT);
				}
				
				rp.addRule(ALIGN_PARENT_TOP);
				break;
			case 1:
				rp.addRule(RIGHT_OF, madeCount + 1);
				rp.addRule(ALIGN_TOP, madeCount + 1);
				rp.leftMargin = s;
				break;
			case 2:
				rp.addRule(ALIGN_LEFT, madeCount + 1);
				rp.addRule(BELOW, madeCount + 1);
				rp.topMargin = s;
				break;
			case 3:
				rp.addRule(RIGHT_OF, madeCount + 3);
				rp.addRule(ALIGN_TOP, madeCount + 3);
				rp.leftMargin = s;
				break;
			default:

				if(i%4==0) {
					
					if(i==4) {
						rp.addRule(ALIGN_PARENT_LEFT);
						rp.addRule(BELOW, madeCount);
						rp.topMargin = s;
					} else {
						rp.addRule(ALIGN_PARENT_LEFT);
						rp.addRule(BELOW, madeCount + i);
						rp.topMargin = s;
					}
				} else {
					rp.addRule(ALIGN_TOP, madeCount + i);
					rp.addRule(RIGHT_OF, madeCount + i);
					rp.leftMargin = s;
				}
				break;
			}
			
			TextView small = new TextView(getContext());
			small.setLayoutParams(rp);
			small.setId(madeCount + i + 1);
			small.setBackgroundColor(gridMenuGroup.getColor());
			
			if(i<size) {
				small.setGravity(Gravity.CENTER);
				small.setMaxLines(3);
				small.setEllipsize(TruncateAt.END);
				small.setTextColor(Color.WHITE);
				FontInfo.setFontSize(small, 32);
				
				final int I = i;
				small.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						if(onMenuClickedListener != null) {
							onMenuClickedListener.onMenuClicked(I, gridMenuGroup.getGridMenus().get(I));
						}
					}
				});
			}
			small.setVisibility(size>4&&i>=4?View.GONE:View.VISIBLE);
			this.addView(small);
			views[i] = small;
		}
		
		if(size > 4) {
			int marginForArrow = ResizeUtils.getSpecificLength(18);
			viewForExpand = new View(getContext());
			rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(43), ResizeUtils.getSpecificLength(23));
			rp.addRule(ALIGN_BOTTOM, madeCount);
			rp.bottomMargin = marginForArrow;
			
			if(bigLeft) {
				rp.addRule(ALIGN_RIGHT, madeCount);
				rp.rightMargin = marginForArrow;
			} else {
				rp.addRule(ALIGN_LEFT, madeCount);
				rp.leftMargin = marginForArrow;
			}
			
			viewForExpand.setLayoutParams(rp);
			viewForExpand.setBackgroundResource(R.drawable.img_cate_arrow_down);
			this.addView(viewForExpand);
			
			big.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(folded) {
						expand();
					} else {
						collapse();
					}
				}
			});
		}
	}
	
	public void setViews() {
		
		for(int i=0; i<gridMenuGroup.getGridMenus().size(); i++) {
			views[i].setText(gridMenuGroup.getGridMenus().get(i).getText());
		}
	}
	
	public void collapse() {
		
		if(foldable && !folded) {
			
			folded = true;
			
			AlphaAnimation aaOut = new AlphaAnimation(1, 0);
			aaOut.setDuration(200);
			
			for(int i=4; i<views.length; i++) {
				
				views[i].startAnimation(aaOut);
				
				final int I = i;
				views[i].postDelayed(new Runnable() {
					
					@Override
					public void run() {
						views[I].setVisibility(View.GONE);
					}
				}, 200);
			}

			viewForExpand.setBackgroundResource(R.drawable.img_cate_arrow_down);
		}
	}
	
	public void expand() {
		
		if(foldable && folded) {
			
			folded = false;

			AlphaAnimation aaIn = new AlphaAnimation(0, 1);
			aaIn.setDuration(200);
			
			for(int i=4; i<views.length; i++) {
				views[i].setVisibility(View.VISIBLE);
				views[i].startAnimation(aaIn);
			}

			viewForExpand.setBackgroundResource(R.drawable.img_cate_arrow_up);
			setViews();
		}
	}

////////////////// Interfaces.

	public void setOnMenuClickedListener(OnMenuClickedListener onMenuClickedListener) {
		this.onMenuClickedListener = onMenuClickedListener;
	}

	public interface OnMenuClickedListener {
		
		public void onMenuClicked(int index, GridMenu gridMenu);
	}
}