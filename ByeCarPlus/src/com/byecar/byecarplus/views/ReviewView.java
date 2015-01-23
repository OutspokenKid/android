package com.byecar.byecarplus.views;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.byecarplus.R.color;
import com.byecar.byecarplus.models.Review;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.views.OutSpokenRatingBar;

public class ReviewView extends LinearLayout {

	private ImageView ivImage;
	private View cover;
	private TextView tvNickname;
	private TextView tvCarName;
	private OutSpokenRatingBar ratingBar;
	private TextView tvContent;
	private TextView tvRegdate;
	
	public ReviewView(Context context) {
		super(context);
		init();
	}

	public void init() {
		
		this.setOrientation(LinearLayout.VERTICAL);
		
		//bg_top.
		View bgTop = new View(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 34, bgTop, 1, 0, null);
		bgTop.setBackgroundResource(R.drawable.dealer_talk_head);
		this.addView(bgTop);

		//relative.
		RelativeLayout relative = new RelativeLayout(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, relative, 1, 0, null);
		relative.setBackgroundResource(R.drawable.dealer_talk_body);
		this.addView(relative);
		
		//bg_bottom.
		View bgBottom = new View(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 34, bgBottom, 1, 0, null);
		bgBottom.setBackgroundResource(R.drawable.dealer_talk_foot);
		this.addView(bgBottom);
		
		//ivImage.
		ivImage = new ImageView(getContext());
		ResizeUtils.viewResizeForRelative(86, 86, ivImage, 
				new int[]{RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP}, 
				new int[]{0, 0}, 
				new int[]{26, 10, 0, 0});
		ivImage.setId(R.id.reviewView_ivImage);
		ivImage.setScaleType(ScaleType.CENTER_CROP);
		ivImage.setBackgroundResource(R.drawable.menu_default);
		relative.addView(ivImage);
		
		//cover.
		cover = new View(getContext());
		ResizeUtils.viewResizeForRelative(86, 86, cover, 
				new int[]{RelativeLayout.ALIGN_LEFT, RelativeLayout.ALIGN_TOP}, 
				new int[]{R.id.reviewView_ivImage, R.id.reviewView_ivImage}, 
				null);
		cover.setBackgroundResource(R.drawable.dealer_frame);
		relative.addView(cover);
		
		//tvNickname.
		tvNickname = new TextView(getContext());
		ResizeUtils.viewResizeForRelative(86, LayoutParams.WRAP_CONTENT, tvNickname, 
				new int[]{RelativeLayout.ALIGN_LEFT, RelativeLayout.BELOW}, 
				new int[]{R.id.reviewView_ivImage, R.id.reviewView_ivImage}, 
				new int[]{0, 4, 0, 0});
		tvNickname.setEllipsize(TruncateAt.END);
		tvNickname.setTextColor(color.holo_text);
		tvNickname.setGravity(Gravity.CENTER_HORIZONTAL);
		FontUtils.setFontSize(tvNickname, 18);
		FontUtils.setFontStyle(tvNickname, FontUtils.BOLD);
		relative.addView(tvNickname);
		
		//tvCarName.
		tvCarName = new TextView(getContext());
		ResizeUtils.viewResizeForRelative(260, 30, tvCarName, 
				new int[]{RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.RIGHT_OF}, 
				new int[]{0, R.id.reviewView_ivImage}, 
				new int[]{34, 0, 20, 0});
		tvCarName.setId(R.id.reviewView_tvCarName);
		tvCarName.setSingleLine();
		tvCarName.setEllipsize(TruncateAt.END);
		tvCarName.setGravity(Gravity.CENTER_VERTICAL);
		FontUtils.setFontSize(tvCarName, 20);
		FontUtils.setFontStyle(tvCarName, FontUtils.BOLD);
		relative.addView(tvCarName);
		
		//ratingBar.
		ratingBar = new OutSpokenRatingBar(getContext());
		ResizeUtils.viewResizeForRelative(120, 30, ratingBar, 
				new int[]{RelativeLayout.ALIGN_TOP, RelativeLayout.RIGHT_OF}, 
				new int[]{R.id.reviewView_tvCarName, R.id.reviewView_tvCarName},
				new int[]{0, 0, 26, 0});
		ratingBar.setLengths(ResizeUtils.getSpecificLength(18),
				ResizeUtils.getSpecificLength(6));
		ratingBar.setMinRating(1);
		ratingBar.setMaxRating(5);
		ratingBar.setEmptyStarColor(Color.rgb(195, 195, 195));
		ratingBar.setFilledStarColor(Color.rgb(254, 188, 42));
		ratingBar.setUnitRating(OutSpokenRatingBar.UNIT_ONE);
		ratingBar.setTouchable(false);
		relative.addView(ratingBar);
		
		//tvContent.
		tvContent = new TextView(getContext());
		ResizeUtils.viewResizeForRelative(394, LayoutParams.WRAP_CONTENT, tvContent, 
				new int[]{RelativeLayout.ALIGN_LEFT, RelativeLayout.BELOW}, 
				new int[]{R.id.reviewView_tvCarName, R.id.reviewView_tvCarName}, 
				new int[]{0, 0, 0, 0});
		tvContent.setMinHeight(ResizeUtils.getSpecificLength(80));
		tvContent.setId(R.id.reviewView_tvContent);
		tvContent.setTextColor(color.holo_text);
		FontUtils.setFontSize(tvContent, 22);
		relative.addView(tvContent);
		
		//tvRegdate.
		tvRegdate = new TextView(getContext());
		ResizeUtils.viewResizeForRelative(200, LayoutParams.WRAP_CONTENT, tvRegdate, 
				new int[]{RelativeLayout.ALIGN_RIGHT, RelativeLayout.BELOW},
				new int[]{R.id.reviewView_tvContent, R.id.reviewView_tvContent},
				new int[]{0, 4, 0, 0});
		tvRegdate.setTextColor(color.holo_text_hint);
		tvRegdate.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
		FontUtils.setFontSize(tvRegdate, 18);
		relative.addView(tvRegdate);
	}
	
	public void setReview(Review review) {
		
		tvNickname.setText(review.getReviewer_name());
		tvCarName.setText(review.getCar_full_name());
		ratingBar.setRating(review.getRating());
		tvContent.setText(review.getContent());
		tvRegdate.setText(StringUtils.getDateString("yyyy년 MM월 dd일", review.getCreated_at() * 1000));
	}
}
