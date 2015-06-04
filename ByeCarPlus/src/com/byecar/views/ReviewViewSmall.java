package com.byecar.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
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

public class ReviewViewSmall extends FrameLayout {

	private ImageView ivImage;
	private View cover;
	private TextView tvBiddingInfo;
	private TextView tvCarName;
	private PriceTextView priceTextView;
	private TextView tvReview;
	
	public ReviewViewSmall(Context context) {
		this(context, null, 0);
	}
	
	public ReviewViewSmall(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public ReviewViewSmall(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public void init() {
		
		//ivImage.
		ivImage = new ImageView(getContext());
		ResizeUtils.viewResize(242, LayoutParams.MATCH_PARENT, ivImage, 2, Gravity.LEFT, null);
		ivImage.setId(R.id.usedCarView_ivImage);
		ivImage.setScaleType(ScaleType.CENTER_CROP);
		ivImage.setBackgroundResource(R.drawable.main_used_car_sub_frame_default);
		this.addView(ivImage);
		
		//cover.
		cover = new View(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, cover, 2, 0, null);
		cover.setBackgroundResource(R.drawable.main_complete_frame2);
		this.addView(cover);
		
		//tvBiddingInfo.
		tvBiddingInfo = new TextView(getContext());
		ResizeUtils.viewResize(236, 43, tvBiddingInfo, 2, Gravity.LEFT|Gravity.BOTTOM, new int[]{2, 0, 0, 0});
		tvBiddingInfo.setTextColor(Color.WHITE);
		tvBiddingInfo.setSingleLine();
		tvBiddingInfo.setEllipsize(TruncateAt.END);
		FontUtils.setFontSize(tvBiddingInfo, 18);
		tvBiddingInfo.setGravity(Gravity.CENTER);
		this.addView(tvBiddingInfo);
		
		//tvCarName.
		tvCarName = new TextView(getContext());
		ResizeUtils.viewResize(135, 55, tvCarName, 2, Gravity.LEFT, new int[]{260, 0, 0, 0});
		tvCarName.setTextColor(getResources().getColor(R.color.new_color_text_brown));
		tvCarName.setSingleLine();
		tvCarName.setEllipsize(TruncateAt.END);
		FontUtils.setFontSize(tvCarName, 26);
		FontUtils.setFontStyle(tvCarName, FontUtils.BOLD);
		tvCarName.setGravity(Gravity.CENTER_VERTICAL);
		this.addView(tvCarName);
		
		//priceTextView.
		priceTextView = new PriceTextView(getContext());
		ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, 55, priceTextView, 2, Gravity.RIGHT, new int[]{0, 0, 15, 0});
		priceTextView.setType(PriceTextView.TYPE_MAIN_REVIEW);
		this.addView(priceTextView);
		
		//tvReview.
		tvReview = new TextView(getContext());
		ResizeUtils.viewResize(286, 67, tvReview, 2, Gravity.LEFT, new int[]{267, 83, 0, 0});
		tvCarName.setTextColor(getResources().getColor(R.color.new_color_text_darkgray));
		tvReview.setMaxLines(2);
		tvReview.setEllipsize(TruncateAt.END);
		FontUtils.setFontSize(tvReview, 20);
		FontUtils.setFontStyle(tvReview, FontUtils.BOLD);
		tvReview.setGravity(Gravity.CENTER_VERTICAL);
		this.addView(tvReview);
		
		clearView();
	}
	
	public void clearView() {

		ivImage.setImageDrawable(null);
		tvBiddingInfo.setText("참여딜러 --명/입찰횟수 --회");
		tvCarName.setText("--");
		priceTextView.setPrice(0);
		tvReview.setText("--");
	}
	
	public void setReview(Car car) {
		
		if(!StringUtils.isEmpty(car.getRep_img_url())) {
			downloadImage(car.getRep_img_url(), ivImage);
		}
		
		tvBiddingInfo.setText("참여딜러 " + car.getBidders_cnt()
				+ "명/입찰횟수 " + car.getBids_cnt() + "회");
		tvCarName.setText(car.getModel_name());
		priceTextView.setPrice(car.getPrice());
		tvReview.setText(car.getReview().getContent());
	}
	
	public void downloadImage(String imageUrl, final ImageView ivImage) {
		
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
		}, 242);
	}
}
