package com.cmons.cph.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class HeaderViewForShop extends RelativeLayout {

	private static int madeCount = 880627;
	
	public int diffLength;
	
	private ImageView ivImage;
	private TextView tvPhoneNumber;
	private TextView tvLocation;
	private TextView tvHit;
	private TextView tvWhat;
	private TextView tvTotalProduct;
	private TextView tvOrder;
	private Button btnCustomerType;
	private Button btnCategory;
	private Button btnOrder;
	
	
	public HeaderViewForShop(Context context) {
		this(context, null, 0);
	}
	
	public HeaderViewForShop(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public HeaderViewForShop(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	public void init() {

		createViews();
		setSizes();
		setValues();
	}
	
	public void createViews() {

		RelativeLayout.LayoutParams rp = null;
		
		madeCount += 10;
		
		//ivImage.					id : 0
		ivImage = new ImageView(getContext());
		rp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 
				LayoutParams.MATCH_PARENT);
		ivImage.setLayoutParams(rp);
		ivImage.setId(madeCount);
		ivImage.setBackgroundColor(Color.DKGRAY);
		addView(ivImage);
		
		//buttonLinear.				id : 1
		LinearLayout buttonLinear = new LinearLayout(getContext());
		buttonLinear.setOrientation(LinearLayout.HORIZONTAL);
		rp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 
				LayoutParams.WRAP_CONTENT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		buttonLinear.setLayoutParams(rp);
		buttonLinear.setId(madeCount + 1);
		addView(buttonLinear);
		
		//btnCustomerType.
		btnCustomerType = new Button(getContext());
		btnCustomerType.setTextColor(Color.WHITE);
		FontUtils.setFontSize(btnCustomerType, 20);
		buttonLinear.addView(btnCustomerType);
		
		//btnCategory.
		btnCategory = new Button(getContext());
		btnCategory.setTextColor(Color.WHITE);
		FontUtils.setFontSize(btnCategory, 20);
		buttonLinear.addView(btnCategory);
		
		//btnOrder.
		btnOrder = new Button(getContext());
		btnOrder.setTextColor(Color.WHITE);
		FontUtils.setFontSize(btnOrder, 20);
		buttonLinear.addView(btnOrder);
		
		//tvPhoneNumber.
		tvPhoneNumber = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		tvPhoneNumber.setLayoutParams(rp);
		tvPhoneNumber.setTextColor(Color.WHITE);
		FontUtils.setFontSize(tvPhoneNumber, 20);
		addView(tvPhoneNumber);
		
		//tvLocation.
		tvLocation = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT);
		rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		tvLocation.setLayoutParams(rp);
		tvLocation.setTextColor(Color.WHITE);
		FontUtils.setFontSize(tvLocation, 20);
		addView(tvLocation);
		
		//tvHit.
		tvHit = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		tvHit.setLayoutParams(rp);
		tvHit.setTextColor(Color.WHITE);
		FontUtils.setFontSize(tvHit, 20);
		addView(tvHit);
		
		//tvWhat.
		tvWhat = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.ABOVE, madeCount + 1);
		tvWhat.setLayoutParams(rp);
		tvWhat.setTextColor(Color.WHITE);
		FontUtils.setFontSize(tvWhat, 20);
		addView(tvWhat);
		
		//tvTotalProduct.
		tvTotalProduct = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT);
		rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		rp.addRule(RelativeLayout.ABOVE, madeCount + 1);
		tvTotalProduct.setLayoutParams(rp);
		tvTotalProduct.setTextColor(Color.WHITE);
		FontUtils.setFontSize(tvTotalProduct, 20);
		addView(tvTotalProduct);
		
		//tvOrder.
		tvOrder = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rp.addRule(RelativeLayout.ABOVE, madeCount + 1);
		tvOrder.setLayoutParams(rp);
		tvOrder.setTextColor(Color.WHITE);
		FontUtils.setFontSize(tvOrder, 20);
		addView(tvOrder);
	}
	
	public void setSizes() {
		
		RelativeLayout.LayoutParams rp = null;
		
		//ivImage.
		//tvPhoneNumber.
		//tvLocation.
		//tvHit.
		//tvWhat.
		//tvTotalProduct.
		//tvOrder.

		diffLength = ResizeUtils.getSpecificLength(96);
		LinearLayout.LayoutParams lp = null;
		
		//btnCustomerType.
		lp = (LinearLayout.LayoutParams) btnCustomerType.getLayoutParams();
		lp.height = diffLength;
		lp.weight = 1;
		
		//btnCategory.
		lp = (LinearLayout.LayoutParams) btnCategory.getLayoutParams();
		lp.height = diffLength;
		lp.weight = 1;
		
		//btnOrder.
		lp = (LinearLayout.LayoutParams) btnOrder.getLayoutParams();
		lp.height = diffLength;
		lp.weight = 1;
	}

	public void setValues() {
		
		tvPhoneNumber.setText("010-9813-8005");
		tvLocation.setText("금강빌딩5층");
		tvHit.setText("5/100");
		tvWhat.setText("뭐지");
		tvTotalProduct.setText("등록된 상품 총 20개");
		tvOrder.setText("거래건수");
		
		btnCustomerType.setText("전체");
		btnCategory.setText("전체");
		btnOrder.setText("최신순");
	}
}

