package com.byecar.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.ImageViewer;
import com.byecar.byecarplus.R;
import com.byecar.byecarplus.R.color;
import com.byecar.classes.BCPDownloadUtils;
import com.byecar.models.Dealer;
import com.byecar.models.Review;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.views.OutSpokenRatingBar;

public class ReviewView extends LinearLayout {

	private View bgTop;
	private RelativeLayout relative;
	private View bgBottom;
	
	private ImageView ivImage;
	private View cover;
	private TextView tvNickname;
	private View gradeBadge;
	private TextView tvGrade;
	private View replyBadge;
	private TextView tvCarName;
	private OutSpokenRatingBar ratingBar;
	private TextView tvContent;
	private TextView tvRegdate;
	
	public ReviewView(Context context) {
		this(context, null);
	}
	
	public ReviewView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public void init() {
		
		this.setOrientation(LinearLayout.VERTICAL);
		
		//bg_top.
		bgTop = new View(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 34, bgTop, 1, 0, null);
		bgTop.setBackgroundResource(R.drawable.dealer_post_frame_head);
		this.addView(bgTop);

		//relative.
		relative = new RelativeLayout(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, relative, 1, 0, null);
		relative.setBackgroundResource(R.drawable.dealer_post_frame_body);
		this.addView(relative);
		
		//bg_bottom.
		bgBottom = new View(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 34, bgBottom, 1, 0, null);
		bgBottom.setBackgroundResource(R.drawable.dealer_post_frame_foot);
		this.addView(bgBottom);
		
		//ivImage.
		ivImage = new ImageView(getContext());
		ResizeUtils.viewResizeForRelative(100, 100, ivImage, 
				new int[]{RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP}, 
				new int[]{0, 0}, 
				new int[]{28, 4, 0, 0});
		ivImage.setId(R.id.reviewView_ivImage);
		ivImage.setScaleType(ScaleType.CENTER_CROP);
		ivImage.setBackgroundResource(R.drawable.menu_default);
		relative.addView(ivImage);
		
		//cover.
		cover = new View(getContext());
		ResizeUtils.viewResizeForRelative(100, 100, cover, 
				new int[]{RelativeLayout.ALIGN_LEFT, RelativeLayout.ALIGN_TOP}, 
				new int[]{R.id.reviewView_ivImage, R.id.reviewView_ivImage}, 
				null);
		cover.setBackgroundResource(R.drawable.dealer_frame);
		relative.addView(cover);
		
		//tvNickname.
		tvNickname = new TextView(getContext());
		ResizeUtils.viewResizeForRelative(135, LayoutParams.WRAP_CONTENT, tvNickname, 
				new int[]{RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.BELOW}, 
				new int[]{R.id.reviewView_ivImage, R.id.reviewView_ivImage}, 
				new int[]{10, 4, 0, 0});
		tvNickname.setId(R.id.reviewView_tvNickname);
		tvNickname.setEllipsize(TruncateAt.END);
		tvNickname.setTextColor(color.holo_text);
		tvNickname.setGravity(Gravity.CENTER);
		FontUtils.setFontSize(tvNickname, 26);
		FontUtils.setFontStyle(tvNickname, FontUtils.BOLD);
		relative.addView(tvNickname);
		
		//gradeBadge.
		gradeBadge = new View(getContext());
		ResizeUtils.viewResizeForRelative(25, 25, gradeBadge, 
				new int[]{RelativeLayout.ALIGN_LEFT, RelativeLayout.BELOW}, 
				new int[]{R.id.reviewView_tvNickname, R.id.reviewView_tvNickname}, 
				new int[]{30, 4, 0, 0});
		gradeBadge.setId(R.id.reviewView_gradeBadge);
		gradeBadge.setVisibility(View.INVISIBLE);
		relative.addView(gradeBadge);
		
		//tvGrade.
		tvGrade = new TextView(getContext());
		ResizeUtils.viewResizeForRelative(LayoutParams.WRAP_CONTENT, 25, tvGrade, 
				new int[]{RelativeLayout.RIGHT_OF, RelativeLayout.ALIGN_TOP}, 
				new int[]{R.id.reviewView_gradeBadge, R.id.reviewView_gradeBadge}, 
				new int[]{4, 0, 0, 0});
		tvGrade.setEllipsize(TruncateAt.END);
		tvGrade.setTextColor(color.holo_text);
		tvGrade.setGravity(Gravity.CENTER_VERTICAL);
		FontUtils.setFontSize(tvGrade, 14);
		FontUtils.setFontStyle(tvGrade, FontUtils.BOLD);
		tvGrade.setVisibility(View.INVISIBLE);
		relative.addView(tvGrade);
		
		//replyBadge.
		replyBadge = new View(getContext());
		ResizeUtils.viewResizeForRelative(17, 16, replyBadge, 
				new int[]{RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.RIGHT_OF}, 
				new int[]{0, R.id.reviewView_ivImage}, 
				new int[]{42, 6, 0, 0});
		replyBadge.setBackgroundResource(R.drawable.dealer_post_reply);
		replyBadge.setVisibility(View.INVISIBLE);
		relative.addView(replyBadge);
		
		//tvCarName.
		tvCarName = new TextView(getContext());
		ResizeUtils.viewResizeForRelative(280, LayoutParams.WRAP_CONTENT, tvCarName, 
				new int[]{RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.RIGHT_OF}, 
				new int[]{0, R.id.reviewView_ivImage}, 
				new int[]{42, 0, 20, 0});
		tvCarName.setId(R.id.reviewView_tvCarName);
		tvCarName.setSingleLine();
		tvCarName.setEllipsize(TruncateAt.END);
		tvCarName.setGravity(Gravity.CENTER_VERTICAL);
		tvCarName.setTextColor(color.holo_text);
		FontUtils.setFontSize(tvCarName, 26);
		FontUtils.setFontStyle(tvCarName, FontUtils.BOLD);
		relative.addView(tvCarName);
		
		//ratingBar.
		ratingBar = new OutSpokenRatingBar(getContext());
		ResizeUtils.viewResizeForRelative(74, 12, ratingBar, 
				new int[]{RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.ALIGN_PARENT_RIGHT}, 
				new int[]{0, 0},
				new int[]{0, 12, 31, 0});
		ratingBar.setLengths(ResizeUtils.getSpecificLength(12),
				ResizeUtils.getSpecificLength(3));
		ratingBar.setMinRating(1);
		ratingBar.setMaxRating(5);
		ratingBar.setEmptyStarColor(Color.rgb(195, 195, 195));
		ratingBar.setFilledStarColor(Color.rgb(254, 188, 42));
		ratingBar.setUnitRating(OutSpokenRatingBar.UNIT_ONE);
		ratingBar.setTouchable(false);
		relative.addView(ratingBar);
		
		//tvContent.
		tvContent = new TextView(getContext());
		ResizeUtils.viewResizeForRelative(370, LayoutParams.WRAP_CONTENT, tvContent, 
				new int[]{RelativeLayout.ALIGN_LEFT, RelativeLayout.BELOW}, 
				new int[]{R.id.reviewView_tvCarName, R.id.reviewView_tvCarName}, 
				new int[]{0, 20, 0, 0});
		tvContent.setMinHeight(ResizeUtils.getSpecificLength(84));
		tvContent.setId(R.id.reviewView_tvContent);
		tvContent.setTextColor(color.holo_text);
		FontUtils.setFontSize(tvContent, 22);
		relative.addView(tvContent);
		
		//tvRegdate.
		tvRegdate = new TextView(getContext());
		ResizeUtils.viewResizeForRelative(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, tvRegdate, 
				new int[]{RelativeLayout.ALIGN_RIGHT, RelativeLayout.BELOW},
				new int[]{R.id.reviewView_tvContent, R.id.reviewView_tvContent},
				new int[]{0, 4, 0, 0});
		tvRegdate.setTextColor(color.holo_text_hint);
		tvRegdate.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
		FontUtils.setFontSize(tvRegdate, 16);
		relative.addView(tvRegdate);
	}
	
	public void setReview(final Review review) {
		
		try {
			if(!StringUtils.isEmpty(review.getReviewer_profile_img_url())) {
				ivImage.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {

						Intent intent = new Intent(getContext(), ImageViewer.class);
						intent.putExtra("title", getContext().getResources().getString(R.string.profileImage));
						intent.putExtra("imageUrls", new String[]{review.getReviewer_profile_img_url()});
						getContext().startActivity(intent);
					}
				});
			}
			
			downloadImage(review.getReviewer_profile_img_url());
			tvNickname.setText(review.getReviewer_name());
			tvCarName.setText(review.getCar_full_name());
			ratingBar.setRating(review.getRating());
			tvContent.setText(review.getContent());
			tvRegdate.setText(StringUtils.getDateString("등록일 yyyy년 MM월 dd일", review.getCreated_at() * 1000));
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void setReply(final Review reply) {
		
		try {
			gradeBadge.setVisibility(View.VISIBLE);
			tvGrade.setVisibility(View.VISIBLE);
			replyBadge.setVisibility(View.VISIBLE);
			ratingBar.setVisibility(View.INVISIBLE);
			tvCarName.setPadding(ResizeUtils.getSpecificLength(20), 0, 0, 0);
			
			cover.setBackgroundResource(R.drawable.dealer_post_pic_frame2);
			bgTop.setBackgroundResource(R.drawable.dealer_post_frame_head2);
			relative.setBackgroundResource(R.drawable.dealer_post_frame_body2);
			bgBottom.setBackgroundResource(R.drawable.dealer_post_frame_foot2);
			
			if(!StringUtils.isEmpty(reply.getReviewer_profile_img_url())) {
				ivImage.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {

						Intent intent = new Intent(getContext(), ImageViewer.class);
						intent.putExtra("title", getContext().getResources().getString(R.string.profileImage));
						intent.putExtra("imageUrls", new String[]{reply.getReviewer_profile_img_url()});
						getContext().startActivity(intent);
					}
				});
			}
			
			downloadImage(reply.getReviewer_profile_img_url());
			tvNickname.setText(reply.getReviewer_name());
			tvCarName.setText("RE : " + reply.getCar_full_name());
			tvContent.setText(reply.getContent());
			tvRegdate.setText(StringUtils.getDateString("등록일 yyyy년 MM월 dd일", reply.getCreated_at() * 1000));
			
			switch(reply.getDealer_level()) {
			
			case Dealer.LEVEL_FRESH_MAN:
				gradeBadge.setBackgroundResource(R.drawable.grade_icon4);
				tvGrade.setText(R.string.dealerLevel1);
				tvGrade.setTextColor(getResources().getColor(R.color.color_dealer_level1));
				break;
				
			case Dealer.LEVEL_NORAML_DEALER:
				gradeBadge.setBackgroundResource(R.drawable.grade_icon3);
				tvGrade.setText(R.string.dealerLevel2);
				tvGrade.setTextColor(getResources().getColor(R.color.color_dealer_level2));
				break;
				
			case Dealer.LEVEL_SUPERB_DEALER:
				gradeBadge.setBackgroundResource(R.drawable.grade_icon2);
				tvGrade.setText(R.string.dealerLevel3);
				tvGrade.setTextColor(getResources().getColor(R.color.color_dealer_level3));
				break;
				
			case Dealer.LEVEL_POWER_DEALER:
				gradeBadge.setBackgroundResource(R.drawable.grade_icon1);
				tvGrade.setText(R.string.dealerLevel4);
				tvGrade.setTextColor(getResources().getColor(R.color.color_dealer_level4));
				break;
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void downloadImage(String imageUrl) {

		if(imageUrl == null) {
			ivImage.setImageDrawable(null);
			return;
			
		} else if(ivImage.getTag() != null
				&& !ivImage.getTag().toString().equals(imageUrl)) {
			ivImage.setImageDrawable(null);
		}
		
		ivImage.setTag(imageUrl);
		BCPDownloadUtils.downloadBitmap(imageUrl, new OnBitmapDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("ReviewView.onError." + "\nurl : " + url);
			}

			@Override
			public void onCompleted(String url, Bitmap bitmap) {

				try {
					LogUtils.log("ReviewView.onCompleted." + "\nurl : " + url);

					if(ivImage != null
							&& !bitmap.isRecycled()) {
						ivImage.setImageBitmap(bitmap);
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		}, 86);
	}
}
