package com.byecar.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.classes.BCPDownloadUtils;
import com.byecar.models.Bid;
import com.byecar.models.Dealer;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class DealerView extends FrameLayout {

	private Bid bid;
	
	private ImageView ivImage;
	private View cover;
	private View rankBadge;
	private TextView tvPrice;
	private TextView tvDealerName;
	private View gradeBadge;
	
	public DealerView(Context context, int index) {
		super(context);
		init();
	}
	
	public DealerView(Context context) {
		this(context, null, 0);
	}
	
	public DealerView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public DealerView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public void init() {
		
		ivImage = new ImageView(getContext());
		ResizeUtils.viewResize(104, 104, ivImage, 2, Gravity.CENTER_HORIZONTAL|Gravity.TOP, new int[]{0, 62, 0, 0});
		ivImage.setScaleType(ScaleType.CENTER_CROP);
		ivImage.setBackgroundResource(R.drawable.detail_default);
		this.addView(ivImage);
		
		cover = new View(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, cover, 2, 0, null);
		cover.setBackgroundResource(R.drawable.main_bid_frame2);
		this.addView(cover);
		
		rankBadge = new View(getContext());
		ResizeUtils.viewResize(33, 38, rankBadge, 2, Gravity.LEFT|Gravity.TOP, new int[]{4, 0, 0, 0});
		this.addView(rankBadge);
		
		tvPrice = new TextView(getContext());
		ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, 42, tvPrice, 2, Gravity.TOP, new int[]{58, 0, 0, 0});
		tvPrice.setGravity(Gravity.CENTER_VERTICAL);
		tvPrice.setTextColor(Color.WHITE);
		this.addView(tvPrice);
		
		tvDealerName = new TextView(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 40, tvDealerName, 2, Gravity.TOP, 
				new int[]{0, 170, 0, 0}, new int[]{20, 0, 20, 0});
		tvDealerName.setGravity(Gravity.CENTER);
		tvDealerName.setTextColor(getResources().getColor(R.color.holo_text));
		this.addView(tvDealerName);
		
		gradeBadge = new View(getContext());
		ResizeUtils.viewResize(135, 25, gradeBadge, 2, 
				Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, new int[]{0, 0, 0, 14});
		this.addView(gradeBadge);

		FontUtils.setFontSize(tvPrice, 26);
		FontUtils.setFontStyle(tvPrice, FontUtils.BOLD);
		FontUtils.setFontSize(tvDealerName, 26);
		FontUtils.setFontStyle(tvDealerName, FontUtils.BOLD);
		
		initInfos();
	}

	public void setDealerInfo(Bid bid, int index) {
		
		if(bid == null) {
			initInfos();
			return;
		} else {
			this.bid = bid;
		}

		rankBadge.setVisibility(View.VISIBLE);
		
		switch(index) {
		
		case 0:
			rankBadge.setBackgroundResource(R.drawable.main_rank1);
			break;
		case 1:
			rankBadge.setBackgroundResource(R.drawable.main_rank2);
			break;
		case 2:
			rankBadge.setBackgroundResource(R.drawable.main_rank3);
			break;
		}
		
		tvPrice.setText(null);
		FontUtils.addSpan(tvPrice, StringUtils.getFormattedNumber(bid.getPrice()/10000), 0, 1);
		FontUtils.addSpan(tvPrice, " 만원", 0, 0.7f);
		
		gradeBadge.setVisibility(View.VISIBLE);
		
		switch(bid.getDealer_level()) {
		
		case Dealer.LEVEL_FRESH_MAN:
			gradeBadge.setBackgroundResource(R.drawable.main_grade4);
			break;
			
		case Dealer.LEVEL_NORAML_DEALER:
			gradeBadge.setBackgroundResource(R.drawable.main_grade3);
			break;
			
		case Dealer.LEVEL_SUPERB_DEALER:
			gradeBadge.setBackgroundResource(R.drawable.main_grade2);
			break;
			
		case Dealer.LEVEL_POWER_DEALER:
			gradeBadge.setBackgroundResource(R.drawable.main_grade1);
			break;
		}

		tvDealerName.setText(bid.getDealer_name());
		
		if(!StringUtils.isEmpty(bid.getDealer_profile_img_url())) {
			
			ivImage.setTag(bid.getDealer_profile_img_url());
			BCPDownloadUtils.downloadBitmap(bid.getDealer_profile_img_url(), new OnBitmapDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("DealerView.onError." + "\nurl : " + url);

					// TODO Auto-generated method stub		
				}

				@Override
				public void onCompleted(String url, Bitmap bitmap) {

					try {
						LogUtils.log("DealerView.onCompleted." + "\nurl : " + url);

						if(bitmap != null && !bitmap.isRecycled()) {
							ivImage.setImageBitmap(bitmap);
						}
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
					}
				}
			}, 130);
		}
	}
	
	public void initInfos() {
		
		ivImage.setImageDrawable(null);
		
		tvPrice.setText(null);
		FontUtils.addSpan(tvPrice, "0", 0, 1);
		FontUtils.addSpan(tvPrice, " 만원", 0, 0.7f);
		
		tvDealerName.setText("--");
		
		rankBadge.setVisibility(View.INVISIBLE);
		gradeBadge.setVisibility(View.INVISIBLE);
	}
	
	public void setIndex(int index) {

		rankBadge.setVisibility(View.VISIBLE);
		
		switch(index) {
		
		case 0:
			rankBadge.setBackgroundResource(R.drawable.main_rank1);
			break;
		case 1:
			rankBadge.setBackgroundResource(R.drawable.main_rank2);
			break;
		case 2:
			rankBadge.setBackgroundResource(R.drawable.main_rank3);
			break;
		}
	}

	public Bid getBid() {
		return bid;
	}

	public void setSelectedDealer() {
		
		cover.setBackgroundResource(R.drawable.select_bid_frame2);
	}
}
