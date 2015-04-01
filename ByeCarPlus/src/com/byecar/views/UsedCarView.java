package com.byecar.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.classes.BCPDownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class UsedCarView extends RelativeLayout {

	private ImageView ivImage;
	private ImageView ivProfile;
	private TextView tvDealerName;
	private View rankBadge;
	private TextView tvCarName;
	private Button btnLike;
	private TextView tvInfo;
	private View[] infoBadges = new View[4];
	private PriceTextView priceTextView;
	
	private String imageUrl;
	
	public UsedCarView(Context context) {
		this(context, null, 0);
	}
	
	public UsedCarView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public UsedCarView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}
	
	public void init() {
	
		RelativeLayout.LayoutParams rp = null;
		
		//ivImage.
		ivImage = new ImageView(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(242), LayoutParams.MATCH_PARENT);
		ivImage.setLayoutParams(rp);
		ivImage.setId(R.id.usedCarView_ivImage);
		ivImage.setScaleType(ScaleType.CENTER_CROP);
		ivImage.setBackgroundResource(R.drawable.main_used_car_sub_frame_default);
		this.addView(ivImage);
		
		//ivProfile.
		ivProfile = new ImageView(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(43), 
				ResizeUtils.getSpecificLength(43));
		rp.addRule(ALIGN_PARENT_BOTTOM);
		rp.leftMargin = ResizeUtils.getSpecificLength(9);
		rp.bottomMargin = ResizeUtils.getSpecificLength(7);
		ivProfile.setLayoutParams(rp);
		ivProfile.setId(R.id.usedCarView_ivProfile);
		ivProfile.setScaleType(ScaleType.CENTER_CROP);
		ivProfile.setBackgroundResource(R.drawable.detail_default);
		this.addView(ivProfile);
		
		//cover.
		View cover = new View(getContext());
		rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		cover.setLayoutParams(rp);
		cover.setBackgroundResource(R.drawable.main_used_car_frame2);
		this.addView(cover);
		
		//tvDealerName.
		tvDealerName = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(70), ResizeUtils.getSpecificLength(56));
		rp.addRule(ALIGN_PARENT_BOTTOM);
		rp.addRule(RIGHT_OF, R.id.usedCarView_ivProfile);
		tvDealerName.setLayoutParams(rp);
		tvDealerName.setId(R.id.usedCarView_tvDealerName);
		tvDealerName.setTextColor(Color.WHITE);
		tvDealerName.setSingleLine();
		tvDealerName.setEllipsize(TruncateAt.END);
		FontUtils.setFontSize(tvDealerName, 20);
		tvDealerName.setGravity(Gravity.CENTER_VERTICAL);
		this.addView(tvDealerName);
		
		//rankBadge.
		rankBadge = new View(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(96), ResizeUtils.getSpecificLength(25));
		rp.addRule(ALIGN_PARENT_BOTTOM);
		rp.addRule(RIGHT_OF, R.id.usedCarView_tvDealerName);
		rp.bottomMargin = ResizeUtils.getSpecificLength(15);
		rankBadge.setLayoutParams(rp);
		this.addView(rankBadge);
		
		//tvCarName.
		tvCarName = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(160), ResizeUtils.getSpecificLength(60));
		rp.addRule(ALIGN_PARENT_TOP);
		rp.addRule(RIGHT_OF, R.id.usedCarView_ivImage);
		tvCarName.setLayoutParams(rp);
		tvCarName.setTextColor(getResources().getColor(R.color.holo_text));
		tvCarName.setSingleLine();
		tvCarName.setEllipsize(TruncateAt.END);
		FontUtils.setFontSize(tvCarName, 30);
		tvCarName.setGravity(Gravity.CENTER_VERTICAL);
		this.addView(tvCarName);
		
		//btnLike.
		btnLike = new Button(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(90), ResizeUtils.getSpecificLength(40));
		rp.addRule(ALIGN_PARENT_RIGHT);
		rp.topMargin = ResizeUtils.getSpecificLength(17);
		rp.rightMargin = ResizeUtils.getSpecificLength(15);
		btnLike.setLayoutParams(rp);
		btnLike.setId(R.id.usedCarView_btnLike);
		btnLike.setBackgroundResource(R.drawable.main_like_btn_a);
		this.addView(btnLike);
		
		//tvLikeText.
		TextView tvLikeText = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, ResizeUtils.getSpecificLength(40));
		rp.addRule(ALIGN_TOP, R.id.usedCarView_btnLike);
		rp.addRule(LEFT_OF, R.id.usedCarView_btnLike);
		tvLikeText.setLayoutParams(rp);
		tvLikeText.setTextColor(getResources().getColor(R.color.holo_text));
		FontUtils.setFontSize(tvLikeText, 18);
		tvLikeText.setGravity(Gravity.CENTER_VERTICAL);
		this.addView(tvLikeText);
		
		//tvInfo.
		TextView tvInfo = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(307), ResizeUtils.getSpecificLength(35));
		rp.addRule(ALIGN_PARENT_TOP);
		rp.addRule(ALIGN_PARENT_RIGHT);
		rp.topMargin = ResizeUtils.getSpecificLength(58);
		rp.rightMargin = ResizeUtils.getSpecificLength(15);
		tvInfo.setLayoutParams(rp);
		tvInfo.setTextColor(getResources().getColor(R.color.holo_text));
		FontUtils.setFontSize(tvInfo, 18);
		tvInfo.setGravity(Gravity.CENTER);
		this.addView(tvInfo);
		
		//infoBadges.
		infoBadges[0] = new View(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(62), ResizeUtils.getSpecificLength(23));
		rp.addRule(ALIGN_PARENT_BOTTOM);
		rp.addRule(RIGHT_OF, R.id.usedCarView_ivImage);
		rp.rightMargin = ResizeUtils.getSpecificLength(23);
		rp.bottomMargin = ResizeUtils.getSpecificLength(41);
		infoBadges[0].setLayoutParams(rp);
		infoBadges[0].setId(R.id.usedCarView_infoBadg1);
		this.addView(infoBadges[0]);
		
		infoBadges[1] = new View(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(62), ResizeUtils.getSpecificLength(23));
		rp.addRule(ALIGN_TOP, R.id.usedCarView_infoBadg1);
		rp.addRule(RIGHT_OF, R.id.usedCarView_infoBadg1);
		rp.leftMargin = ResizeUtils.getSpecificLength(7);
		infoBadges[1].setLayoutParams(rp);
		this.addView(infoBadges[1]);

		infoBadges[2] = new View(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(62), ResizeUtils.getSpecificLength(23));
		rp.addRule(ALIGN_LEFT, R.id.usedCarView_infoBadg1);
		rp.addRule(BELOW, R.id.usedCarView_infoBadg1);
		rp.topMargin = ResizeUtils.getSpecificLength(7);
		infoBadges[2].setLayoutParams(rp);
		this.addView(infoBadges[2]);
		
		infoBadges[3] = new View(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(62), ResizeUtils.getSpecificLength(23));
		rp.addRule(RIGHT_OF, R.id.usedCarView_infoBadg1);
		rp.addRule(BELOW, R.id.usedCarView_infoBadg1);
		rp.leftMargin = ResizeUtils.getSpecificLength(7);
		rp.topMargin = ResizeUtils.getSpecificLength(7);
		infoBadges[3].setLayoutParams(rp);
		this.addView(infoBadges[3]);
		
		//priceTextView.
		priceTextView = new PriceTextView(getContext());
		rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, ResizeUtils.getSpecificLength(53));
		rp.addRule(ALIGN_TOP, R.id.usedCarView_infoBadg1);
		rp.addRule(ALIGN_PARENT_RIGHT);
		rp.rightMargin = ResizeUtils.getSpecificLength(15);
		priceTextView.setLayoutParams(rp);
		priceTextView.setType(PriceTextView.TYPE_USED_CAR);
		this.addView(priceTextView);
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
	
	public void setTexts(String modelName, long price) {
		
//		tvCar.setText(modelName);
//		tvPrice.setText(StringUtils.getFormattedNumber(price/10000) + "만원");
	}

	public void clearView() {
		
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