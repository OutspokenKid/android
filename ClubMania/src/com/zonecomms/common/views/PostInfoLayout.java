package com.zonecomms.common.views;

import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.classes.ApplicationManager;
import com.outspoken_kid.utils.ResizeUtils;
import com.zonecomms.clubmania.R;
import com.zonecomms.common.models.Member;
import com.zonecomms.common.models.Post;
import com.zonecomms.common.utils.ImageDownloadUtils;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PostInfoLayout extends RelativeLayout {

	private static int madeCount;

	private ImageView ivImage;
	private TextView tvNicknameWithId;
	private TextView tvRegdate;
	private ImageView ivPapp;
	private TextView tvPapp;
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
		
		//id : 2
		tvNicknameWithId = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, l/3);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 1);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		rp.rightMargin = l;
		tvNicknameWithId.setLayoutParams(rp);
		tvNicknameWithId.setId(madeCount + 2);
		tvNicknameWithId.setPadding(s*2, 0, s*2, 0);
		tvNicknameWithId.setSingleLine();
		tvNicknameWithId.setEllipsize(TruncateAt.END);
		tvNicknameWithId.setTextColor(Color.WHITE);
		tvNicknameWithId.setGravity(Gravity.CENTER_VERTICAL);
		FontInfo.setFontSize(tvNicknameWithId, 30);
		FontInfo.setFontStyle(tvNicknameWithId, FontInfo.BOLD);
		this.addView(tvNicknameWithId);
		
		tvRegdate = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, l/3);
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 2);
		rp.addRule(RelativeLayout.BELOW, madeCount + 2);
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
		
		ivPapp = new ImageView(getContext());
		rp = new RelativeLayout.LayoutParams(l/3, l/3);
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 2);
		rp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rp.rightMargin = s;
		ivPapp.setLayoutParams(rp);
		ivPapp.setScaleType(ScaleType.CENTER_CROP);
		this.addView(ivPapp);
		
		tvPapp = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, l/3);
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 2);
		rp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rp.leftMargin = ResizeUtils.getSpecificLength(50);
		rp.rightMargin = s;
		tvPapp.setLayoutParams(rp);
		tvPapp.setPadding(s*2, 0, s*2, 0);
		tvPapp.setSingleLine();
		tvPapp.setEllipsize(TruncateAt.END);
		tvPapp.setTextColor(Color.WHITE);
		tvPapp.setGravity(Gravity.CENTER_VERTICAL);
		FontInfo.setFontSize(tvPapp, 30);
		FontInfo.setFontStyle(tvPapp, FontInfo.BOLD);
		this.addView(tvPapp);
		
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
			
			if(!TextUtils.isEmpty(member.getMedia_src())) {
				ImageDownloadUtils.downloadImageImmediately(member.getMedia_src(), 
						ApplicationManager.getDownloadKeyFromTopFragment(), 
						ivImage, 150, true);
			}
			
			if(!TextUtils.isEmpty(member.getMember_nickname())) {
				nicknameWithId += member.getMember_nickname();
			} else {
				nicknameWithId += "Noname";
			}
			
			if(!TextUtils.isEmpty(member.getMember_id())) {
				nicknameWithId += " (" + member.getMember_id() + ")";
			}
		}
		
		if(!TextUtils.isEmpty(nicknameWithId)) {
			tvNicknameWithId.setText(nicknameWithId);
		} else {
			tvNicknameWithId.setText("Noname");
		}
		
		if(!TextUtils.isEmpty(post.getReg_dt())) {
			tvRegdate.setText(post.getReg_dt());
		}

		if(!TextUtils.isEmpty(post.getBoard_name())) {
			tvPapp.setText(post.getBoard_name());
		}
		
		if(!TextUtils.isEmpty(post.getCopyright())) {
			ImageDownloadUtils.downloadImageImmediately(post.getCopyright(), 
					ApplicationManager.getDownloadKeyFromTopFragment(), 
					ivPapp, 50, true);
		}
		
		if(post != null && member != null) {
			ivImage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ApplicationManager.getInstance().getMainActivity().showProfilePopup(
							member.getMember_id(), member.getStatus());
				}
			});
		}
	}

	public void setMoreButtonClicked(OnClickListener ocl) {
		more.setOnClickListener(ocl);
	}
}
