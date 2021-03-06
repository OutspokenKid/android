package com.zonecomms.common.wrappers;

import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.zonecomms.common.models.Member;
import com.zonecomms.common.models.Post;
import com.zonecomms.common.views.WrapperView;
import com.zonecomms.festivalwdjf.IntentHandlerActivity;
import com.zonecomms.festivalwdjf.R;
import com.zonecomms.festivalwdjf.classes.ApplicationManager;
import com.zonecomms.festivalwdjf.classes.ViewWrapperForZonecomms;
import com.zonecomms.festivalwdjf.classes.ZoneConstants;

public class ViewWrapperForPost extends ViewWrapperForZonecomms {
	
	private static int length;

	private View imageBg;
	private ImageView ivImage;
	private ImageView profileImage;
	private TextView tvInfo;
	private TextView tvReplyCount;
	private TextView tvRegdate;
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
			tvText = (TextView) row.findViewById(R.id.grid_post_tvText);
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	@Override
	public void setSizes() {

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
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 70, tvText, 2, Gravity.BOTTOM, new int[]{70, 0, 10, 0});
			
			FontInfo.setFontSize(tvInfo, 22);
			FontInfo.setFontSize(tvReplyCount, 22);
			FontInfo.setFontSize(tvRegdate, 22);
			FontInfo.setFontSize(tvText, 22);
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	@Override
	public void setListeners() {

		try {
			if(post != null) {

				row.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						if(post != null) {
							
							String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/post" +
									"?title=" + ApplicationManager.getTopFragment().getTitleText() +
									"&spot_nid=" + post.getSpot_nid() +
									"&isGethering=" + post.isFromGethering();
							
							IntentHandlerActivity.actionByUri(Uri.parse(uriString));
						}
					}
				});
				
				profileImage.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						if(post != null && post.getMember() != null && !StringUtils.isEmpty(post.getMember().getMember_id())) {
							ApplicationManager.getInstance().getActivity()
								.showProfilePopup(post.getMember().getMember_id(), post.getMember().getStatus());
						}
					}
				});
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel instanceof Post) {
				post = (Post) baseModel;
				
				String key = ApplicationManager.getDownloadKeyFromTopFragment();
				
				if(post.getMember() != null) {
					Member member = post.getMember();
					setImage(profileImage, member.getMedia_src(), key, 60);
					String info = "";
					
					if(!StringUtils.isEmpty(member.getMember_nickname())) {
						info += member.getMember_nickname();
					}
					
					if(!StringUtils.isEmpty(member.getMember_id())) {
						info += "(" + member.getMember_id() + ")";
					}
					
					tvInfo.setText(info);
				}
				
				if(StringUtils.isEmpty(post.getMedia_src())) {
					imageBg.setBackgroundResource(R.drawable.bg_talk);
					setImage(ivImage, null, null, 308);
				} else {
					imageBg.setBackgroundResource(R.drawable.bg_post);
					setImage(ivImage, post.getMedia_src(), key, 308);
				}
				
				if(post.getReply_cnt() > 999) {
					tvReplyCount.setText("(999)");
				} else {
					tvReplyCount.setText("(" + post.getReply_cnt() + ")");
				}
				
				if(!StringUtils.isEmpty(post.getReg_dt())) {
					tvRegdate.setText(post.getReg_dt());
				}
				
				if(!StringUtils.isEmpty(post.getContent())) {
					tvText.setText(post.getContent());
				} else {
					tvText.setText(R.string.noContent);
				}
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
}
