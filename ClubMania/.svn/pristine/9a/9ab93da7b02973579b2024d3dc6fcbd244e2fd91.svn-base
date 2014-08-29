package com.zonecomms.common.wrappers;

import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.classes.ApplicationManager;
import com.outspoken_kid.utils.ResizeUtils;
import com.zonecomms.clubmania.IntentHandlerActivity;
import com.zonecomms.clubmania.R;
import com.zonecomms.clubmania.classes.ZoneConstants;
import com.zonecomms.common.models.BaseModel;
import com.zonecomms.common.models.Member;
import com.zonecomms.common.models.Post;
import com.zonecomms.common.utils.ImageDownloadUtils;
import com.zonecomms.common.wrapperviews.WrapperView;

public class ViewWrapperForPost extends ViewWrapper {
	
	private static int length;

	private View imageBg;
	private ImageView ivImage;
	private ImageView profileImage;
	private TextView tvInfo;
	private TextView tvReplyCount;
	private TextView tvRegdate;
	private ImageView pappImage;
	private TextView tvText;
	
	private Post post;
	
	public ViewWrapperForPost(WrapperView row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			imageBg = row.findViewById(R.id.grid_post_imageBg);
			ivImage = (ImageView) row.findViewById(R.id.grid_post_ivImage);
			profileImage = (ImageView) row.findViewById(R.id.grid_post_profileImage);
			tvInfo = (TextView) row.findViewById(R.id.grid_post_tvInfo);
			tvReplyCount = (TextView) row.findViewById(R.id.grid_post_tvReplyCount);
			tvRegdate = (TextView) row.findViewById(R.id.grid_post_tvRegdate);
			pappImage = (ImageView) row.findViewById(R.id.grid_post_pappImage);
			tvText = (TextView) row.findViewById(R.id.grid_post_tvText);
		} catch(Exception e) {
			e.printStackTrace();
			setUnusableView();
		}
	}

	@Override
	public void setSize() {

		try {
			if(length == 0) {
				int s = ResizeUtils.getSpecificLength(8);
				int l = ResizeUtils.getSpecificLength(150);
				length = l*2 + s + s + s/2;
			}
			row.setLayoutParams(new AbsListView.LayoutParams(length, length));
			
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 140,
					row.findViewById(R.id.grid_post_bg), 2, Gravity.BOTTOM, null);
			ResizeUtils.viewResize(60, 60, row.findViewById(R.id.grid_post_profileBg), 
					2, Gravity.BOTTOM|Gravity.LEFT, new int[]{5, 0, 0, 75});
			ResizeUtils.viewResize(60, 60, profileImage, 2, Gravity.BOTTOM|Gravity.LEFT, new int[]{5, 0, 0, 75});
			ResizeUtils.viewResize(180, 30, tvInfo, 2, Gravity.BOTTOM|Gravity.LEFT, new int[]{70, 0, 0, 105});
			ResizeUtils.viewResize(140, 30, tvRegdate, 2, Gravity.BOTTOM|Gravity.LEFT, new int[]{70, 0, 0, 75});
			ResizeUtils.viewResize(50, 30, tvReplyCount, 2, Gravity.RIGHT|Gravity.BOTTOM, new int[]{0, 0, 10, 105});
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 2, row.findViewById(R.id.grid_post_horizontalLine), 
					2, Gravity.BOTTOM, new int[]{0, 0, 0, 68});
			
			ResizeUtils.viewResize(60, 60, row.findViewById(R.id.grid_post_pappBg), 
					2, Gravity.BOTTOM|Gravity.LEFT, new int[]{5, 0, 0, 5});
			ResizeUtils.viewResize(60, 60, pappImage, 2, Gravity.BOTTOM|Gravity.LEFT, new int[]{5, 0, 0, 5});
			
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 70, tvText, 2, Gravity.BOTTOM, new int[]{70, 0, 10, 0});
			
			FontInfo.setFontSize(tvInfo, 22);
			FontInfo.setFontSize(tvReplyCount, 22);
			FontInfo.setFontSize(tvRegdate, 22);
			FontInfo.setFontSize(tvText, 22);
		} catch(Exception e) {
			e.printStackTrace();
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel instanceof Post) {
				post = (Post) baseModel;
				
				if(post.getMember() != null) {

					Member member = post.getMember();
					
					String key = ApplicationManager.getDownloadKeyFromTopFragment();
	
					if(!TextUtils.isEmpty(member.getMedia_src())) {
						ImageDownloadUtils.downloadImage(member.getMedia_src(), key, profileImage, 60);
					} else {
						profileImage.setVisibility(View.INVISIBLE);
					}
					
					String info = "";
					
					if(!TextUtils.isEmpty(member.getMember_nickname())) {
						info += member.getMember_nickname();
					}
					
					if(!TextUtils.isEmpty(member.getMember_id())) {
						info += "(" + member.getMember_id() + ")";
					}
					
					tvInfo.setText(info);
				}
				
				if(TextUtils.isEmpty(post.getMedia_src())) {
					ivImage.setVisibility(View.INVISIBLE);
					imageBg.setBackgroundResource(R.drawable.bg_talk);
				} else {
					imageBg.setBackgroundResource(R.drawable.bg_post);

					if(ivImage.getTag() == null || ivImage.getTag().toString() == null
							|| !ivImage.getTag().toString().equals(post.getMedia_src())) {
						ivImage.setVisibility(View.INVISIBLE);
					}
					
					String key = ApplicationManager.getDownloadKeyFromTopFragment();
					ImageDownloadUtils.downloadImageImmediately(post.getMedia_src(), key, ivImage, 308, true);
				}
				
				if(post.getReply_cnt() > 999) {
					tvReplyCount.setText("(999)");
				} else {
					tvReplyCount.setText("(" + post.getReply_cnt() + ")");
				}
				
				if(!TextUtils.isEmpty(post.getReg_dt())) {
					tvRegdate.setText(post.getReg_dt());
				}
				
				if(!TextUtils.isEmpty(post.getContent())) {
					tvText.setText(post.getContent());
				} else {
					tvText.setText(R.string.noContent);
				}
				
				if(TextUtils.isEmpty(post.getCopyright())) {
					pappImage.setVisibility(View.INVISIBLE);
				} else {
					if(pappImage.getTag() == null || pappImage.getTag().toString() == null
							|| !pappImage.getTag().toString().equals(post.getMedia_src())) {
						pappImage.setVisibility(View.INVISIBLE);
					}
					
					String key = ApplicationManager.getDownloadKeyFromTopFragment();
					ImageDownloadUtils.downloadImageImmediately(post.getCopyright(), key, pappImage, 60, true);
				}
			} else {
				setUnusableView();
			}
		} catch(Exception e) {
			e.printStackTrace();
			setUnusableView();
		}
	}
	
	@Override
	public void setListener() {

		try {
			if(post != null) {

				row.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						if(post != null) {
							String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/post" +
									"?title=" + ApplicationManager.getTopFragment().getTitle() +
									"&spot_nid=" + post.getSpot_nid();
							IntentHandlerActivity.actionByUri(Uri.parse(uriString));
						}
					}
				});
				
				profileImage.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						if(post != null && post.getMember() != null && !TextUtils.isEmpty(post.getMember().getMember_id())) {
							ApplicationManager.getInstance().getMainActivity().showProfilePopup(
									post.getMember().getMember_id(), post.getMember().getStatus());
						}
					}
				});
			}
		} catch(Exception e) {
			e.printStackTrace();
			setUnusableView();
		}
	}
	
	@Override
	public void setUnusableView() {
		// TODO Auto-generated method stub
		
	}
}
