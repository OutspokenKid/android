package com.byecar.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils.TruncateAt;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.byecar.byecarplus.R;
import com.byecar.classes.BCPDownloadUtils;
import com.byecar.models.Post;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class ForumView extends FrameLayout {

	private ImageView ivProfile;
	private View cover;
	private View badge;
	private TextView tvName;
	private TextView tvContent;
	private TextView tvRegdate;
	private TextView tvHitCount;
	private TextView tvReplyCount;
	private TextView tvLikeCount;
	
	public ForumView(Context context) {
		this(context, null, 0);
	}
	
	public ForumView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public ForumView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public void init() {
		
		ivProfile = new ImageView(getContext());
		ResizeUtils.viewResize(76, 76, ivProfile, 2, Gravity.LEFT|Gravity.TOP, new int[]{12, 15, 0, 0});
		ivProfile.setScaleType(ScaleType.CENTER_CROP);
		ivProfile.setBackgroundResource(R.drawable.detail_default);
		this.addView(ivProfile);
		
		cover = new View(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, cover, 2, 0, null);
		cover.setBackgroundResource(R.drawable.main_forum_frame2_a);
		this.addView(cover);
		
		badge = new View(getContext());
		ResizeUtils.viewResize(37, 58, badge, 2, Gravity.LEFT|Gravity.TOP, new int[]{6, 0, 0, 0});
		badge.setBackgroundColor(Color.argb(100, 255, 0, 0));
		this.addView(badge);
		
		tvName = new TextView(getContext());
		ResizeUtils.viewResize(100, 46, tvName, 2, Gravity.LEFT|Gravity.BOTTOM, null);
		tvName.setTextColor(getResources().getColor(R.color.holo_text));
		tvName.setGravity(Gravity.CENTER);
		tvName.setSingleLine();
		tvName.setEllipsize(TruncateAt.END);
		this.addView(tvName);
		
		tvContent = new TextView(getContext());
		ResizeUtils.viewResize(436, 70, tvContent, 2, Gravity.LEFT|Gravity.TOP, new int[]{116, 17, 0, 2});
		tvContent.setTextColor(getResources().getColor(R.color.holo_text));
		tvContent.setGravity(Gravity.CENTER_VERTICAL);
		tvContent.setMaxLines(2);
		tvContent.setEllipsize(TruncateAt.END);
		this.addView(tvContent);

		tvRegdate = new TextView(getContext());
		ResizeUtils.viewResize(120, 46, tvRegdate, 2, Gravity.LEFT|Gravity.BOTTOM, new int[]{129, 0, 0, 2});
		tvRegdate.setTextColor(getResources().getColor(R.color.holo_text));
		tvRegdate.setGravity(Gravity.CENTER_VERTICAL);
		tvRegdate.setSingleLine();
		tvRegdate.setEllipsize(TruncateAt.END);
		this.addView(tvRegdate);
		
		tvHitCount = new TextView(getContext());
		ResizeUtils.viewResize(90, 46, tvHitCount, 2, Gravity.LEFT|Gravity.BOTTOM, new int[]{282, 0, 0, 2});
		tvHitCount.setTextColor(getResources().getColor(R.color.holo_text));
		tvHitCount.setGravity(Gravity.CENTER_VERTICAL);
		tvHitCount.setSingleLine();
		tvHitCount.setEllipsize(TruncateAt.END);
		this.addView(tvHitCount);
		
		tvReplyCount = new TextView(getContext());
		ResizeUtils.viewResize(90, 46, tvReplyCount, 2, Gravity.LEFT|Gravity.BOTTOM, new int[]{393, 0, 0, 2});
		tvReplyCount.setTextColor(getResources().getColor(R.color.holo_text));
		tvReplyCount.setGravity(Gravity.CENTER_VERTICAL);
		tvReplyCount.setSingleLine();
		tvReplyCount.setEllipsize(TruncateAt.END);
		this.addView(tvReplyCount);
		
		tvLikeCount = new TextView(getContext());
		ResizeUtils.viewResize(90, 46, tvLikeCount, 2, Gravity.LEFT|Gravity.BOTTOM, new int[]{510, 0, 0, 2});
		tvLikeCount.setTextColor(getResources().getColor(R.color.holo_text));
		tvLikeCount.setGravity(Gravity.CENTER_VERTICAL);
		tvLikeCount.setSingleLine();
		tvLikeCount.setEllipsize(TruncateAt.END);
		this.addView(tvLikeCount);
		
		FontUtils.setFontSize(tvName, 18);
		FontUtils.setFontStyle(tvName, FontUtils.BOLD);
		FontUtils.setFontSize(tvContent, 22);
		FontUtils.setFontSize(tvRegdate, 20);
		FontUtils.setFontSize(tvHitCount, 20);
		FontUtils.setFontSize(tvReplyCount, 20);
		FontUtils.setFontSize(tvLikeCount, 20);
		
		clearView();
	}
	
	public void clearView() {

		ivProfile.setImageDrawable(null);
		badge.setVisibility(View.INVISIBLE);
		tvName.setText("--");
		tvContent.setText("--");
		tvRegdate.setText("----, --, --");
		tvHitCount.setText("--");
		tvReplyCount.setText("--");
		tvLikeCount.setText("--");
	}
	
	public void setForum(Post post, int index) {
		
		if(!StringUtils.isEmpty(post.getRep_img_url())) {
			
			ivProfile.setTag(post.getRep_img_url());
			BCPDownloadUtils.downloadBitmap(post.getRep_img_url(), new OnBitmapDownloadListener() {

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
							ivProfile.setImageBitmap(bitmap);
						}
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
					}
				}
			}, 76);
		}
		
		badge.setVisibility(View.VISIBLE);
		switch(index) {
		
		case 0:
			badge.setBackgroundResource(R.drawable.main_forum_rank1);
			break;
			
		case 1:
			badge.setBackgroundResource(R.drawable.main_forum_rank2);
			break;
			
		case 2:
			badge.setBackgroundResource(R.drawable.main_forum_rank3);
			break;
			
		default:
			badge.setVisibility(View.INVISIBLE);
		}
		
		tvName.setText(post.getAuthor_nickname());

		if(System.currentTimeMillis()/1000 - post.getCreated_at() < 86400 * 10) {
			SpannableStringBuilder ssb = new SpannableStringBuilder(post.getContent() + "  ");
			Drawable newIconDrawable = getResources().getDrawable(R.drawable.new_icon);
			newIconDrawable.setBounds(0, 0, ResizeUtils.getSpecificLength(28), ResizeUtils.getSpecificLength(28));
			ssb.setSpan(new ImageSpan(newIconDrawable, DynamicDrawableSpan.ALIGN_BOTTOM), 
					ssb.length() - 1, ssb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			tvContent.setText(ssb, BufferType.SPANNABLE);
		} else {
			tvContent.setText(post.getContent());
		}
		
		tvRegdate.setText(StringUtils.getDateString("yyyy, MM, dd", post.getCreated_at()*1000));
		tvHitCount.setText(post.getHits_cnt() + "회");
		tvReplyCount.setText(post.getReplies_cnt() + "개");
		tvLikeCount.setText(post.getLikes_cnt() + "회");
	}
}
