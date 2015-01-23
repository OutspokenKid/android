package com.byecar.byecarplusfordealer.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.byecar.byecarplusfordealer.R;
import com.byecar.byecarplusfordealer.models.Bid;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class DealerView extends FrameLayout {

	private int index;
	
	private ImageView ivImage;
	private View cover;
	private View rankBadge;
	private TextView tvInfo;
	private TextView tvGrade;
	private TextView tvPrice;
	
	private View selectedIcon;
	private boolean selected;
	
	public DealerView(Context context, int index) {
		super(context);
		this.index = index;
		init();
	}

	public void init() {
		
		int p = ResizeUtils.getSpecificLength(10);
		
		ivImage = new ImageView(getContext());
		ResizeUtils.viewResize(130, 130, ivImage, 2, Gravity.CENTER_HORIZONTAL|Gravity.TOP, new int[]{0, 16, 0, 0});
		ivImage.setScaleType(ScaleType.CENTER_CROP);
		ivImage.setBackgroundResource(R.drawable.detail_default);
		this.addView(ivImage);
		
		cover = new View(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, cover, 2, 0, null);
		cover.setBackgroundResource(R.drawable.detail_frame);
		this.addView(cover);
		
		rankBadge = new View(getContext());
		ResizeUtils.viewResize(54, 54, rankBadge, 2, Gravity.LEFT|Gravity.TOP, null);
		
		switch(index) {
		
		case 0:
			rankBadge.setBackgroundResource(R.drawable.detail_rank1);
			break;
		case 1:
			rankBadge.setBackgroundResource(R.drawable.detail_rank2);
			break;
		case 2:
			rankBadge.setBackgroundResource(R.drawable.detail_rank3);
			break;
		}
		
		this.addView(rankBadge);
		
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
		
		selectedIcon = new View(getContext());
		ResizeUtils.viewResize(53, 55, selectedIcon, 2, Gravity.LEFT|Gravity.TOP, null);
		selectedIcon.setBackgroundResource(R.drawable.success_choice);
		selectedIcon.setVisibility(View.INVISIBLE);
		this.addView(selectedIcon);
		
		FontUtils.setFontSize(tvInfo, 16);
		FontUtils.setFontSize(tvGrade, 20);
		FontUtils.setFontStyle(tvGrade, FontUtils.BOLD);
		FontUtils.setFontSize(tvPrice, 26);
		FontUtils.setFontStyle(tvPrice, FontUtils.BOLD);
		
		initInfos();
	}

	public void setDealerInfo(Bid bid) {
		
		tvInfo.setText(null);
		FontUtils.addSpan(tvInfo, bid.getDealer_name(), getResources().getColor(R.color.holo_text), 1.6f, true);
		FontUtils.addSpan(tvInfo, "\n" + bid.getDealer_address(), getResources().getColor(R.color.holo_text_hint), 1);
		
		tvGrade.setText("우수딜러");
		
		tvPrice.setText(StringUtils.getFormattedNumber(bid.getPrice()) 
						+ getContext().getString(R.string.won));

		rankBadge.setVisibility(View.VISIBLE);
		
		if(!StringUtils.isEmpty(bid.getDealer_profile_img_url())) {
			
			ivImage.setTag(bid.getDealer_profile_img_url());
			DownloadUtils.downloadBitmap(bid.getDealer_profile_img_url(), new OnBitmapDownloadListener() {

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
			});
		}
	}
	
	public void setSelected(boolean selected) {
		
		this.selected = selected;
		
		if(selected) {
			selectedIcon.setVisibility(View.VISIBLE);
		} else {
			selectedIcon.setVisibility(View.INVISIBLE);
		}
	}
	
	public boolean getSelected() {
		
		return selected;
	}

	public void initInfos() {
		
		ivImage.setImageDrawable(null);

		tvInfo.setText(null);
		FontUtils.addSpan(tvInfo, getResources().getString(R.string.standingByBid), 
				getResources().getColor(R.color.holo_text), 1.6f, true);
		FontUtils.addSpan(tvInfo, "\n--", getResources().getColor(R.color.holo_text_hint), 1);
		
		tvGrade.setText(null);
		tvPrice.setText("0" + getContext().getString(R.string.won));
		rankBadge.setVisibility(View.INVISIBLE);
	}
}
