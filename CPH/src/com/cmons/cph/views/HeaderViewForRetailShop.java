package com.cmons.cph.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.models.Wholesale;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class HeaderViewForRetailShop extends RelativeLayout {

	private static int madeCount = 880627;

	private Wholesale wholesale;
	
	private ImageView ivImage;
	private TextView tvPhoneNumber;
	private View phoneNumberIcon;
	private TextView tvLocation;
	private View locationIcon;
	private TextView tvHit;
	private View hitIcon;
	private TextView tvLike;
	private View likeIcon;
	private TextView tvPartner;
	private View partnerIcon;
	private Button btnCategoryIndex;
	private Button btnFavorite;
	private Button btnCall;
	private TextView tvTotalProduct;
	private View totalProductIcon;
	
	public HeaderViewForRetailShop(Context context) {
		this(context, null, 0);
	}
	
	public HeaderViewForRetailShop(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public HeaderViewForRetailShop(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void init(Wholesale wholesale) {
		
		this.wholesale = wholesale;
		
		createViews();
		setListeners();
		setValues();
	}
	
	public void createViews() {

		RelativeLayout.LayoutParams rp = null;
		
		madeCount += 10;
		
		//ivImage.					id : 0
		ivImage = new ImageView(getContext());
		rp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 
				ResizeUtils.getSpecificLength(440));
		ivImage.setLayoutParams(rp);
		ivImage.setId(madeCount);
		ivImage.setScaleType(ScaleType.CENTER_CROP);
		ivImage.setBackgroundResource(R.drawable.picture_default);
		addView(ivImage);
		
		//tvPhoneNumber.
		tvPhoneNumber = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 
				ResizeUtils.getSpecificLength(40));
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		tvPhoneNumber.setLayoutParams(rp);
		tvPhoneNumber.setTextColor(Color.WHITE);
		tvPhoneNumber.setGravity(Gravity.CENTER_VERTICAL);
		tvPhoneNumber.setPadding(ResizeUtils.getSpecificLength(40), 0, 0, 0);
		FontUtils.setFontSize(tvPhoneNumber, 20);
		tvPhoneNumber.setBackgroundColor(Color.argb(178, 164, 164, 164));
		addView(tvPhoneNumber);
		
		//phoneNumberIcon.
		phoneNumberIcon = new View(getContext());
		rp = new RelativeLayout.LayoutParams(
				ResizeUtils.getSpecificLength(22), 
				ResizeUtils.getSpecificLength(20));
		rp.leftMargin = ResizeUtils.getSpecificLength(12);
		rp.topMargin = ResizeUtils.getSpecificLength(10);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		phoneNumberIcon.setLayoutParams(rp);
		phoneNumberIcon.setBackgroundResource(R.drawable.myshop_tell_icon);
		addView(phoneNumberIcon);

		//tvLocation.				id : 1
		tvLocation = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, 
				ResizeUtils.getSpecificLength(40));
		rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		tvLocation.setLayoutParams(rp);
		tvLocation.setId(madeCount + 1);
		tvLocation.setTextColor(Color.WHITE);
		tvLocation.setGravity(Gravity.CENTER_VERTICAL);
		tvLocation.setPadding(ResizeUtils.getSpecificLength(20), 0, 0, 0);
		FontUtils.setFontSize(tvLocation, 20);
		addView(tvLocation);
		
		//locationIcon.
		locationIcon = new View(getContext());
		rp = new RelativeLayout.LayoutParams(
				ResizeUtils.getSpecificLength(15), 
				ResizeUtils.getSpecificLength(20));
		rp.topMargin = ResizeUtils.getSpecificLength(10);
		rp.addRule(RelativeLayout.LEFT_OF, madeCount + 1);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		locationIcon.setLayoutParams(rp);
		locationIcon.setBackgroundResource(R.drawable.myshop_pin_icon);
		addView(locationIcon);
		
		//tvHit.					id : 2
		tvHit = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, 
				ResizeUtils.getSpecificLength(40));
		rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		tvHit.setLayoutParams(rp);
		tvHit.setId(madeCount + 2);
		tvHit.setTextColor(Color.WHITE);
		tvHit.setGravity(Gravity.CENTER_VERTICAL);
		tvHit.setPadding(0, 0, ResizeUtils.getSpecificLength(10), 0);
		FontUtils.setFontSize(tvHit, 20);
		addView(tvHit);

		//hitIcon.
		hitIcon = new View(getContext());
		rp = new RelativeLayout.LayoutParams(
				ResizeUtils.getSpecificLength(30), 
				ResizeUtils.getSpecificLength(20));
		rp.topMargin = ResizeUtils.getSpecificLength(10);
		rp.addRule(RelativeLayout.LEFT_OF, madeCount + 2);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		hitIcon.setLayoutParams(rp);
		hitIcon.setBackgroundResource(R.drawable.myshop_customer_icon);
		addView(hitIcon);

		//tvLike.
		tvLike = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, 
				ResizeUtils.getSpecificLength(40));
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.ALIGN_BOTTOM, madeCount);
		tvLike.setLayoutParams(rp);
		tvLike.setTextColor(Color.WHITE);
		tvLike.setGravity(Gravity.CENTER_VERTICAL);
		tvLike.setPadding(ResizeUtils.getSpecificLength(46), 0, 0, 0);
		FontUtils.setFontSize(tvLike, 20);
		tvLike.setBackgroundColor(Color.argb(178, 164, 164, 164));
		addView(tvLike);
		
		//likeIcon.
		likeIcon = new View(getContext());
		rp = new RelativeLayout.LayoutParams(
				ResizeUtils.getSpecificLength(22), 
				ResizeUtils.getSpecificLength(20));
		rp.leftMargin = ResizeUtils.getSpecificLength(12);
		rp.bottomMargin = ResizeUtils.getSpecificLength(10);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.ALIGN_BOTTOM, madeCount);
		likeIcon.setLayoutParams(rp);
		likeIcon.setBackgroundResource(R.drawable.myshop_heart1_icon);
		addView(likeIcon);
		
		//tvPartner.				id : 3
		tvPartner = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, 
				ResizeUtils.getSpecificLength(40));
		rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rp.addRule(RelativeLayout.ALIGN_BOTTOM, madeCount);
		tvPartner.setLayoutParams(rp);
		tvPartner.setId(madeCount + 3);
		tvPartner.setTextColor(Color.WHITE);
		tvPartner.setGravity(Gravity.CENTER_VERTICAL);
		tvPartner.setPadding(0, 0, ResizeUtils.getSpecificLength(10), 0);
		FontUtils.setFontSize(tvPartner, 20);
		addView(tvPartner);
		
		//partnerIcon.
		partnerIcon = new View(getContext());
		rp = new RelativeLayout.LayoutParams(
				ResizeUtils.getSpecificLength(20), 
				ResizeUtils.getSpecificLength(20));
		rp.rightMargin = ResizeUtils.getSpecificLength(10);
		rp.bottomMargin = ResizeUtils.getSpecificLength(10);
		rp.addRule(RelativeLayout.LEFT_OF, madeCount + 3);
		rp.addRule(RelativeLayout.ALIGN_BOTTOM, madeCount);
		partnerIcon.setLayoutParams(rp);
		partnerIcon.setBackgroundResource(R.drawable.myshop_business_icon);
		addView(partnerIcon);
		
		//btnCategoryIndex.			id : 4
		btnCategoryIndex = new Button(getContext());
		rp = new RelativeLayout.LayoutParams(
				ResizeUtils.getSpecificLength(240), 
				ResizeUtils.getSpecificLength(92));
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.BELOW, madeCount);
		btnCategoryIndex.setLayoutParams(rp);
		btnCategoryIndex.setId(madeCount + 4);
		btnCategoryIndex.setTextColor(Color.WHITE);
		btnCategoryIndex.setGravity(Gravity.CENTER);
		btnCategoryIndex.setBackgroundResource(R.drawable.retail_list_drop);
		FontUtils.setFontSize(btnCategoryIndex, 24);
		addView(btnCategoryIndex);

		//btnFavorite.				id : 5
		btnFavorite = new Button(getContext());
		rp = new RelativeLayout.LayoutParams(
				ResizeUtils.getSpecificLength(240), 
				ResizeUtils.getSpecificLength(92));
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 4);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 4);
		btnFavorite.setLayoutParams(rp);
		btnFavorite.setId(madeCount + 5);
		btnFavorite.setTextColor(Color.WHITE);
		btnFavorite.setGravity(Gravity.CENTER);
		btnFavorite.setBackgroundResource(R.drawable.retail_list_favorite);
		FontUtils.setFontSize(btnFavorite, 24);
		addView(btnFavorite);
		
		//btnCall.
		btnCall = new Button(getContext());
		rp = new RelativeLayout.LayoutParams(
				ResizeUtils.getSpecificLength(240), 
				ResizeUtils.getSpecificLength(92));
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 5);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 5);
		btnCall.setLayoutParams(rp);
		btnCall.setTextColor(Color.WHITE);
		btnCall.setGravity(Gravity.CENTER);
		btnCall.setBackgroundResource(R.drawable.retail_list_call);
		FontUtils.setFontSize(btnCall, 24);
		addView(btnCall);
		
		View bottomBg = new View(getContext());
		rp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 
				ResizeUtils.getSpecificLength(40));
		rp.addRule(RelativeLayout.BELOW, madeCount + 4);
		bottomBg.setLayoutParams(rp);
		bottomBg.setBackgroundColor(Color.rgb(238, 141, 141));
		addView(bottomBg);
		
		//tvTotalProduct.			id : 6
		tvTotalProduct = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, 
				ResizeUtils.getSpecificLength(40));
		rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		rp.addRule(RelativeLayout.BELOW, madeCount + 4);
		tvTotalProduct.setLayoutParams(rp);
		tvTotalProduct.setId(madeCount + 6);
		tvTotalProduct.setGravity(Gravity.CENTER);
		tvTotalProduct.setTextColor(Color.WHITE);
		tvTotalProduct.setPadding(ResizeUtils.getSpecificLength(20), 0, 0, 0);
		FontUtils.setFontSize(tvTotalProduct, 20);
		addView(tvTotalProduct);
		
		//totalProductIcon.
		totalProductIcon = new View(getContext());
		rp = new RelativeLayout.LayoutParams(
				ResizeUtils.getSpecificLength(20), 
				ResizeUtils.getSpecificLength(20));
		rp.rightMargin = ResizeUtils.getSpecificLength(10);
		rp.topMargin = ResizeUtils.getSpecificLength(10);
		rp.addRule(RelativeLayout.LEFT_OF, madeCount + 6);
		rp.addRule(RelativeLayout.BELOW, madeCount + 4);
		totalProductIcon.setLayoutParams(rp);
		totalProductIcon.setBackgroundResource(R.drawable.myshop_goods_icon);
		addView(totalProductIcon);
	}
	
	public void setValues() {

		try {
			if(wholesale != null) {
				tvPhoneNumber.setText(wholesale.getPhone_number());
				tvLocation.setText("청평화몰 " + wholesale.getLocation());
				tvHit.setText(wholesale.getToday_visited_cnt() + 
						" / " + wholesale.getTotal_visited_cnt());
				tvLike.setText("" + wholesale.getFavorited_cnt());
				tvPartner.setText("" + wholesale.getCustomers_cnt());
				btnCategoryIndex.setText("전체");
				
				downloadProfile();
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	public void downloadProfile() {
		
		try {
			if(wholesale.getStatus() == -1) {
				ivImage.setImageResource(R.drawable.out_bg);
				return;
			}
			
			if(wholesale != null
					&& wholesale.getRep_image_url() != null) {
				DownloadUtils.downloadBitmap(wholesale.getRep_image_url(), new OnBitmapDownloadListener() {

					@Override
					public void onError(String url) {

						LogUtils.log("HeaderViewForShop.onError." + "\nurl : " + url);
					}

					@Override
					public void onCompleted(String url, Bitmap bitmap) {

						try {
							LogUtils.log("HeaderViewForShop.onCompleted." + "\nurl : " + url);
							if(ivImage != null) {
								ivImage.setImageBitmap(bitmap);
							}
						} catch (Exception e) {
							LogUtils.trace(e);
						} catch (OutOfMemoryError oom) {
							LogUtils.trace(oom);
						}
					}
				});
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void setListeners() {
	}
	
	public Button getBtnCategoryIndex() {
		
		return btnCategoryIndex;
	}
	
	public Button getBtnFavorite() {
		
		return btnFavorite;
	}
	
	public Button getBtnCall() {
		
		return btnCall;
	}

	public void setTotalProduct(int totalProduct) {
		
		tvTotalProduct.setText("총 등록 상품 " +  totalProduct);
	}
	
	public void refreshValues(Wholesale wholesale) {

		this.wholesale = wholesale;
		setValues();
	}
}

