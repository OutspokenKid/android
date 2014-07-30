package com.zonecomms.common.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.outspoken_kid.model.FontInfo;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.zonecomms.clubcage.R;
import com.zonecomms.clubcage.classes.ZonecommsApplication;
import com.zonecomms.common.models.Member;
import com.zonecomms.common.models.Post;

public class PostInfoLayout extends RelativeLayout {

	private static int madeCount;

	private ImageView ivImage;
	private TextView tvNicknameWithId;
	private TextView tvRegdate;
	private View more;
	
	public PostInfoLayout(Context context) {
		this(context, null, 0);
	}
	
	public PostInfoLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public PostInfoLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		
		this.setBackgroundColor(Color.rgb(55, 55, 55));

		int l = ResizeUtils.getSpecificLength(150);
		int s = ResizeUtils.getSpecificLength(8);
		RelativeLayout.LayoutParams rp = null;

		madeCount += 12014;
		
		//id : 0
		ivImage = new ImageView(getContext());
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		ivImage.setLayoutParams(rp);
		ivImage.setScaleType(ScaleType.CENTER_CROP);
		ivImage.setBackgroundResource(R.drawable.bg_profile_150);
		ivImage.setId(madeCount);
		this.addView(ivImage);
		
		//id : 1
		View blank = new View(getContext());
		rp = new RelativeLayout.LayoutParams(s, l);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		blank.setLayoutParams(rp);
		blank.setId(madeCount + 1);
		blank.setBackgroundColor(Color.BLACK);
		this.addView(blank);
		
		tvNicknameWithId = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, l/3);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 1);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		rp.rightMargin = l;
		tvNicknameWithId.setLayoutParams(rp);
		tvNicknameWithId.setPadding(s*2, 0, s*2, 0);
		tvNicknameWithId.setSingleLine();
		tvNicknameWithId.setEllipsize(TruncateAt.END);
		tvNicknameWithId.setTextColor(Color.WHITE);
		tvNicknameWithId.setGravity(Gravity.CENTER_VERTICAL);
		FontInfo.setFontSize(tvNicknameWithId, 30);
		FontInfo.setFontStyle(tvNicknameWithId, FontInfo.BOLD);
		this.addView(tvNicknameWithId);
		
		tvRegdate = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, l/2);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 1);
		rp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rp.rightMargin = s;
		tvRegdate.setLayoutParams(rp);
		tvRegdate.setPadding(s*2, 0, s*2, 0);
		tvRegdate.setSingleLine();
		tvRegdate.setEllipsize(TruncateAt.END);
		tvRegdate.setTextColor(Color.WHITE);
		tvRegdate.setGravity(Gravity.CENTER_VERTICAL);
		FontInfo.setFontSize(tvRegdate, 30);
		FontInfo.setFontStyle(tvRegdate, FontInfo.BOLD);
		this.addView(tvRegdate);
		
		more = new View(getContext());
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rp.addRule(RelativeLayout.CENTER_VERTICAL);
		more.setLayoutParams(rp);
		more.setBackgroundResource(R.drawable.img_dot);
		this.addView(more);
	}

	public void setPostInfo(Post post) {
		
		if(post == null) {
			return;
		}
		
		String nicknameWithId = "";
		
		final Member member = post.getMember();
		if(member != null) {
			
			if(!StringUtils.isEmpty(member.getMedia_src())) {
				DownloadUtils.downloadBitmap(member.getMedia_src(), 
						new OnBitmapDownloadListener() {
					
					@Override
					public void onError(String url) {
					}
					
					@Override
					public void onCompleted(String url, Bitmap bitmap) {

						if(ivImage != null) {
							ivImage.setImageBitmap(bitmap);
						}
					}
				});
			}
			
			if(!StringUtils.isEmpty(member.getMember_nickname())) {
				nicknameWithId += member.getMember_nickname();
			} else {
				nicknameWithId += "Noname";
			}
			
			if(!StringUtils.isEmpty(member.getMember_id())) {
				nicknameWithId += " (" + member.getMember_id() + ")";
			}
		}
		
		if(!StringUtils.isEmpty(nicknameWithId)) {
			tvNicknameWithId.setText(nicknameWithId);
		} else {
			tvNicknameWithId.setText("Noname");
		}
		
		if(!StringUtils.isEmpty(post.getReg_dt())) {
			tvRegdate.setText(post.getReg_dt());
		}

		if(post != null && member != null) {
			ivImage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ZonecommsApplication.getActivity().showProfilePopup(
							member.getMember_id(), member.getStatus());
				}
			});
		}
	}

	public void setMoreButtonClicked(OnClickListener ocl) {
		more.setOnClickListener(ocl);
	}
}
