package com.byecar.byecarplus.views;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.byecarplus.models.Dealer;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class DealerView extends FrameLayout {

	private Dealer dealer;
	
	private ImageView ivImage;
	private View cover;
	private TextView tvInfo;
	private TextView tvGrade;
	private TextView tvPrice;
	
	public DealerView(Context context) {
		super(context);
		init();
	}

	public void init() {
		
		int p = ResizeUtils.getSpecificLength(10);
		
		ivImage = new ImageView(getContext());
		ResizeUtils.viewResize(130, 130, ivImage, 2, Gravity.CENTER_HORIZONTAL|Gravity.TOP, new int[]{0, 16, 0, 0});
		ivImage.setScaleType(ScaleType.CENTER_CROP);
		ivImage.setBackgroundResource(R.drawable.menu_default);
		this.addView(ivImage);
		
		cover = new View(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, cover, 2, 0, null);
		cover.setBackgroundResource(R.drawable.detail_frame);
		this.addView(cover);
		
		tvInfo = new TextView(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 62, tvInfo, 2, Gravity.TOP, new int[]{0, 150, 0, 0});
		tvInfo.setGravity(Gravity.CENTER);
		tvInfo.setPadding(p, 0, p, 0);
		this.addView(tvInfo);
		
		tvGrade = new TextView(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 44, tvGrade, 2, Gravity.TOP, new int[]{0, 206, 0, 0});
		tvGrade.setGravity(Gravity.CENTER);
		tvGrade.setPadding(p, 0, p, 0);
		tvGrade.setTextColor(Color.rgb(254, 188, 42));
		this.addView(tvGrade);
		
		tvPrice = new TextView(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 44, tvPrice, 2, Gravity.BOTTOM, new int[]{0, 0, 0, 2});
		tvPrice.setGravity(Gravity.CENTER);
		tvPrice.setPadding(p, 0, p, 0);
		tvPrice.setTextColor(Color.rgb(96, 70, 52));
		this.addView(tvPrice);
		
		FontUtils.setFontSize(tvInfo, 16);
		FontUtils.setFontSize(tvGrade, 20);
		FontUtils.setFontStyle(tvGrade, FontUtils.BOLD);
		FontUtils.setFontSize(tvPrice, 26);
		FontUtils.setFontStyle(tvPrice, FontUtils.BOLD);
	}

	public void setDealerInfo(Dealer dealer) {
		
		this.dealer = dealer;
		
		tvInfo.setText(null);
		FontUtils.addSpan(tvInfo, "곽한구", getResources().getColor(R.color.holo_text), 1.6f);
		FontUtils.addSpan(tvInfo, "\n서울 관악구", getResources().getColor(R.color.holo_text_hint), 1);
		
		tvGrade.setText("우수딜러");
		
		tvPrice.setText(StringUtils.getFormattedNumber(39000000) 
						+ getContext().getString(R.string.won));
	}
}
