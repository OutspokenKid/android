package com.byecar.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class UsedCarView extends FrameLayout {

	private ImageView ivImage;
	private TextView tvCar;
	private TextView tvPrice;
	
	private String imageUrl;
	
	public UsedCarView(Context context) {
		super(context);
		init();
	}
	
	public void init() {
	
		ResizeUtils.viewResize(176, 214, this, 1, 0, new int[]{20, 0, 0, 0});
		
		//ivImage.
		ivImage = new ImageView(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 140, ivImage, 2, 0, null);
		ivImage.setScaleType(ScaleType.CENTER_CROP);
		ivImage.setBackgroundResource(R.drawable.main_used_car_sub_frame_default);
		this.addView(ivImage);

		//frame.
		View frame = new View(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 
				frame, 2, 0, null);
		frame.setBackgroundResource(R.drawable.main_used_car_sub_frame);
		this.addView(frame);
		
		//tvCar.
		tvCar = new TextView(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 34, tvCar, 2, 0, new int[]{0, 140, 0, 0});
		tvCar.setTextColor(getContext().getResources().getColor(R.color.holo_text));
		FontUtils.setFontSize(tvCar, 18);
		tvCar.setGravity(Gravity.CENTER);
		this.addView(tvCar);
		
		//tvPrice.
		tvPrice = new TextView(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 38, tvPrice, 2, 0, new int[]{0, 176, 0, 0});
		FontUtils.setFontSize(tvPrice, 22);
		FontUtils.setFontStyle(tvPrice, FontUtils.BOLD);
		tvPrice.setTextColor(getContext().getResources().getColor(R.color.color_brown));
		tvPrice.setGravity(Gravity.CENTER);
		this.addView(tvPrice);
	}
	
	public void downloadImage(String imageUrl) {
		
		ivImage.setImageDrawable(null);
		
		ivImage.setTag(imageUrl);
		DownloadUtils.downloadBitmap(imageUrl, new OnBitmapDownloadListener() {

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
		});
	}
	
	public void setTexts(String modelName, long price) {
		
		tvCar.setText(modelName);
		tvPrice.setText(StringUtils.getFormattedNumber(price/10000) + "만원");
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