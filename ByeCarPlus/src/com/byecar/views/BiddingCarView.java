package com.byecar.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.classes.BCPDownloadUtils;
import com.byecar.models.Car;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class BiddingCarView extends FrameLayout {

	private ImageView ivImage;
	private TextView tvRemainTime;
	private TextView tvCarName;
	private TextView tvBiddingInfo;
	private PriceTextView priceTextView;
	
	private String imageUrl;
	
	public BiddingCarView(Context context) {
		this(context, null, 0);
	}
	
	public BiddingCarView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public BiddingCarView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}
	
	public void init() {
	
		//ivImage.
		ivImage = new ImageView(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 136, ivImage, 2, 0, null);
		ivImage.setScaleType(ScaleType.CENTER_CROP);
		ivImage.setBackgroundResource(R.drawable.main_used_car_sub_frame_default);
		this.addView(ivImage);

		//frame.
		View frame = new View(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 
				frame, 2, 0, null);
		frame.setBackgroundResource(R.drawable.main_today_frame2);
		this.addView(frame);
		
		//tvRemainTime.
		tvRemainTime = new TextView(getContext());
		ResizeUtils.viewResize(160, 38, tvRemainTime, 2, Gravity.CENTER_HORIZONTAL, new int[]{0, 80, 0, 0}, new int[]{16, 0, 0, 0});
		tvRemainTime.setTextColor(Color.WHITE);
		FontUtils.setFontSize(tvRemainTime, 22);
		FontUtils.setFontStyle(tvRemainTime, FontUtils.BOLD);
		tvRemainTime.setGravity(Gravity.CENTER);
		tvRemainTime.setSingleLine();
		tvRemainTime.setEllipsize(TruncateAt.END);
		tvRemainTime.setBackgroundResource(R.drawable.main_today_time);
		this.addView(tvRemainTime);

		//tvCarName.
		tvCarName = new TextView(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 38, tvCarName, 2, Gravity.CENTER_HORIZONTAL, new int[]{10, 135, 10, 0});
		tvCarName.setTextColor(getContext().getResources().getColor(R.color.holo_text));
		FontUtils.setFontSize(tvCarName, 24);
		tvCarName.setGravity(Gravity.CENTER);
		tvCarName.setSingleLine();
		tvCarName.setEllipsize(TruncateAt.END);
		this.addView(tvCarName);
		
		//tvBiddingInfo.
		tvBiddingInfo = new TextView(getContext());
		ResizeUtils.viewResize(146, 26, tvBiddingInfo, 2, Gravity.CENTER_HORIZONTAL, new int[]{0, 174, 0, 0});
		tvBiddingInfo.setTextColor(getContext().getResources().getColor(R.color.holo_text));
		FontUtils.setFontSize(tvBiddingInfo, 16);
		tvBiddingInfo.setGravity(Gravity.CENTER);
		tvBiddingInfo.setSingleLine();
		tvBiddingInfo.setEllipsize(TruncateAt.END);
		this.addView(tvBiddingInfo);
		
		//priceTextView.
		priceTextView = new PriceTextView(getContext());
		ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, 44, priceTextView, 2, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, null);
		priceTextView.setType(PriceTextView.TYPE_MAIN_BIDDING);
		this.addView(priceTextView);
		
		//Initialize.
		tvRemainTime.setText("-- : -- : --");
		tvCarName.setText("--");
		tvBiddingInfo.setText("참여--명/입찰--회");
		priceTextView.setPrice(0);
	}
	
	public void downloadImage(String imageUrl) {
		
		ivImage.setImageDrawable(null);
		
		ivImage.setTag(imageUrl);
		BCPDownloadUtils.downloadBitmap(imageUrl, new OnBitmapDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("UsedCarView.downloadImage.onError." + "\nurl : " + url);
			}

			@Override
			public void onCompleted(String url, Bitmap bitmap) {

				try {
					LogUtils.log("UsedCarView.downloadImage.onCompleted." + "\nurl : " + url);
					
					if(bitmap != null && !bitmap.isRecycled()) {
						ivImage.setImageBitmap(bitmap);
					}
					
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		}, 176);
	}
	
	public void setCar(Car car) {
		
		try {
			tvCarName.setText(car.getCar_full_name());
			tvRemainTime.setText("-- : -- : --");
			tvBiddingInfo.setText("참여" + Math.min(99, car.getBidders_cnt()) 
					+ "명/입찰" + Math.min(999, car.getBids_cnt()) + "회");
			priceTextView.setPrice(car.getPrice());
			
			if(!StringUtils.isEmpty(car.getRep_img_url())) {
				setImageUrl(car.getRep_img_url() + "=w" + ResizeUtils.getSpecificLength(136));
			}
		} catch (Exception e) {
			clearView();
			LogUtils.trace(e);
		}
	}

	public void clearView() {
		
		tvRemainTime.setText("-- : -- : --");
		tvCarName.setText("--");
		tvBiddingInfo.setText("참여--명/입찰--회");
		priceTextView.setPrice(0);
	}
	
	public void setTime(Car car) {

		try {
			if(car.getStatus() != Car.STATUS_BIDDING) {
				clearTime();
				return;
			}

			long remainTime = car.getBid_until_at() * 1000 
					+ 86400000 - System.currentTimeMillis();

			if(remainTime < 0) {
				clearTime();
			} else {
		    	String formattedRemainTime = StringUtils.getTimeString(remainTime);
		    	tvRemainTime.setText(formattedRemainTime);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
			clearTime();
		}
	}
	
	public void clearTime() {

		try {
			tvRemainTime.setText("-- : -- : --");
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public ImageView getIvImage() {
		
		return ivImage;
	}
	
	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);

		if(visibility == View.VISIBLE) {
			
			if(!StringUtils.isEmpty(imageUrl)) {
				downloadImage(imageUrl);
			}
		} else {
			Drawable d = ivImage.getDrawable();
			ivImage.setImageDrawable(null);
	        
	        if (d != null) {
	            d.setCallback(null);
	        }
		}
		
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
		
		downloadImage(imageUrl);
	}
}