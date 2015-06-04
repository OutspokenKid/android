package com.byecar.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.ImageViewer;
import com.byecar.byecarplus.MainActivity;
import com.byecar.byecarplus.R;
import com.byecar.byecarplus.R.color;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPDownloadUtils;
import com.byecar.classes.BCPFragmentActivity;
import com.byecar.models.Dealer;
import com.byecar.models.Review;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.views.OutSpokenRatingBar;

public class ReviewView extends RelativeLayout {

	private View bgTop;
	private TextView tvContent;
	private TextView tvRegdate;
	
	private ImageView ivImage;
	private View cover;
	private TextView tvNickname;
	private View gradeBadge;
	private TextView tvGrade;
	
	private View replyBadge;
	private TextView tvCarName;
	private OutSpokenRatingBar ratingBar;
	private Button btnEdit;
	
	public ReviewView(Context context) {
		this(context, null);
	}
	
	public ReviewView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public void init() {
		
		//bg_top.
		bgTop = new View(getContext());
		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, 30, bgTop, null, null, null);
		this.addView(bgTop);

		//tvContent.
		tvContent = new TextView(getContext());
		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 
				tvContent, null, null, new int[]{0, 30, 0, 0}, null);
		tvContent.setMinHeight(ResizeUtils.getSpecificLength(113));
		tvContent.setId(R.id.reviewView_tvContent);
		tvContent.setTextColor(getResources().getColor(R.color.new_color_text_darkgray));
		FontUtils.setFontSize(tvContent, 22);
		this.addView(tvContent);
		
		//tvRegdate.
		tvRegdate = new TextView(getContext());
		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, 68, tvRegdate, 
				new int[]{RelativeLayout.BELOW}, new int[]{R.id.reviewView_tvContent}, 
				null, new int[]{156, 4, 0, 0});
		tvRegdate.setId(R.id.reviewView_tvRegdate);
		tvRegdate.setTextColor(getResources().getColor(R.color.new_color_text_gray));
		tvRegdate.setGravity(Gravity.CENTER_VERTICAL);
		FontUtils.setFontSize(tvRegdate, 16);
		this.addView(tvRegdate);

		//ivImage.
		ivImage = new ImageView(getContext());
		ResizeUtils.viewResizeForRelative(100, 100, ivImage, 
				new int[]{RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP}, 
				new int[]{0, 0}, 
				new int[]{28, 37, 0, 0});
		ivImage.setId(R.id.reviewView_ivImage);
		ivImage.setScaleType(ScaleType.CENTER_CROP);
		ivImage.setBackgroundResource(R.drawable.menu_default);
		this.addView(ivImage);
		
		//cover.
		cover = new View(getContext());
		ResizeUtils.viewResizeForRelative(100, 100, cover, 
				new int[]{RelativeLayout.ALIGN_LEFT, RelativeLayout.ALIGN_TOP}, 
				new int[]{R.id.reviewView_ivImage, R.id.reviewView_ivImage}, 
				null);
		cover.setBackgroundResource(R.drawable.dealer_frame);
		this.addView(cover);

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
		FontUtils.setFontSize(tvNickname, 22);
		FontUtils.setFontStyle(tvNickname, FontUtils.BOLD);
		this.addView(tvNickname);

		//gradeBadge.
		gradeBadge = new View(getContext());
		ResizeUtils.viewResizeForRelative(25, 25, gradeBadge, 
				new int[]{RelativeLayout.ALIGN_LEFT, RelativeLayout.BELOW}, 
				new int[]{R.id.reviewView_tvNickname, R.id.reviewView_tvNickname}, 
				new int[]{30, 4, 0, 0});
		gradeBadge.setId(R.id.reviewView_gradeBadge);
		gradeBadge.setVisibility(View.INVISIBLE);
		this.addView(gradeBadge);
		
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
		this.addView(tvGrade);

		//replyBadge.
		replyBadge = new View(getContext());
		ResizeUtils.viewResizeForRelative(17, 16, replyBadge, 
				new int[]{RelativeLayout.ALIGN_TOP}, new int[]{R.id.reviewView_tvContent}, 
				new int[]{175, 15, 0, 0});
		replyBadge.setId(R.id.reviewView_replyBadge);
		replyBadge.setBackgroundResource(R.drawable.dealer_post_reply);
		replyBadge.setVisibility(View.INVISIBLE);
		this.addView(replyBadge);
		
		//tvCarName.
		tvCarName = new TextView(getContext());
		ResizeUtils.viewResizeForRelative(280, LayoutParams.WRAP_CONTENT, tvCarName, 
				new int[]{RelativeLayout.ALIGN_LEFT, RelativeLayout.ALIGN_TOP}, 
				new int[]{R.id.reviewView_replyBadge, R.id.reviewView_tvContent}, 
				null);
		tvCarName.setId(R.id.reviewView_tvCarName);
		tvCarName.setSingleLine();
		tvCarName.setEllipsize(TruncateAt.END);
		tvCarName.setGravity(Gravity.CENTER_VERTICAL);
		tvCarName.setTextColor(getResources().getColor(R.color.new_color_text_brown));
		FontUtils.setFontSize(tvCarName, 26);
		FontUtils.setFontStyle(tvCarName, FontUtils.BOLD);
		this.addView(tvCarName);

		//ratingBar.
		ratingBar = new OutSpokenRatingBar(getContext());
		ResizeUtils.viewResizeForRelative(74, 12, ratingBar, 
				new int[]{RelativeLayout.ALIGN_TOP, RelativeLayout.RIGHT_OF}, 
				new int[]{R.id.reviewView_tvCarName, R.id.reviewView_tvCarName},
				new int[]{12, 12, 0, 0});
		ratingBar.setLengths(ResizeUtils.getSpecificLength(12),
				ResizeUtils.getSpecificLength(3));
		ratingBar.setMinRating(1);
		ratingBar.setMaxRating(5);
		ratingBar.setEmptyStarColor(Color.rgb(195, 195, 195));
		ratingBar.setFilledStarColor(Color.rgb(254, 188, 42));
		ratingBar.setUnitRating(OutSpokenRatingBar.UNIT_ONE);
		ratingBar.setTouchable(false);
		this.addView(ratingBar);
		
		//btnEdit.
		btnEdit = new Button(getContext());
		ResizeUtils.viewResizeForRelative(83, 32, btnEdit, 
				new int[]{RelativeLayout.ALIGN_RIGHT, RelativeLayout.ALIGN_BOTTOM}, 
				new int[]{R.id.reviewView_tvRegdate, R.id.reviewView_tvRegdate},
				new int[]{0, 0, 18, 16});
		btnEdit.setBackgroundResource(R.drawable.mypage_review_modify_btn);
		btnEdit.setVisibility(View.INVISIBLE);
		this.addView(btnEdit);
	}
	
	public void setReview(final Review review, final BCPFragmentActivity activity) {
		
		try {
			bgTop.setBackgroundResource(R.drawable.dealer_post_frame_head);
			tvContent.setBackgroundResource(R.drawable.dealer_post_frame_body);
			tvRegdate.setBackgroundResource(R.drawable.dealer_post_frame_foot);
			cover.setBackgroundResource(R.drawable.dealer_post_pic_frame);
			tvContent.setPadding(ResizeUtils.getSpecificLength(175), ResizeUtils.getSpecificLength(48), 
					ResizeUtils.getSpecificLength(34), 0);
			
			//내가 쓴 리뷰이고 아직 리플이 안달린 경우 수정 버튼 노출.
			if(review.getReviewer_id() == MainActivity.user.getId()
					&& review.getReply() == null) {
				btnEdit.setVisibility(View.VISIBLE);
				btnEdit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {

						Bundle bundle = new Bundle();
						bundle.putSerializable("review", review);
						activity.showPage(BCPConstants.PAGE_WRITE_REVIEW, bundle);
					}
				});
			} else {
				btnEdit.setVisibility(View.INVISIBLE);
			}
			
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
			bgTop.setBackgroundResource(R.drawable.dealer_post_frame_head2);
			tvContent.setBackgroundResource(R.drawable.dealer_post_frame_body2);
			tvRegdate.setBackgroundResource(R.drawable.dealer_post_frame_foot2);
			cover.setBackgroundResource(R.drawable.dealer_post_pic_frame2);
			tvContent.setPadding(ResizeUtils.getSpecificLength(175), ResizeUtils.getSpecificLength(48), 
					ResizeUtils.getSpecificLength(34), 0);
			tvCarName.setPadding(ResizeUtils.getSpecificLength(21), ResizeUtils.getSpecificLength(10), 0, 0);
			
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
