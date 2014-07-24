package com.zonecomms.common.wrappers;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.zonecomms.common.models.Message;
import com.zonecomms.common.views.WrapperView;
import com.zonecomms.golfn.BaseFragmentActivity.OnPositiveClickedListener;
import com.zonecomms.golfn.MainActivity;
import com.zonecomms.golfn.R;
import com.zonecomms.golfn.classes.ApplicationManager;
import com.zonecomms.golfn.classes.ViewWrapperForZonecomms;
import com.zonecomms.golfn.fragments.MessagePage;

public class ViewWrapperForMessage extends ViewWrapperForZonecomms {

	private final int TYPE_TEXT = 1;
	private final int TYPE_IMAGE = 2;
	
	private View imageBg;
	private ImageView ivProfile;
	private ImageView ivImage;
	private TextView tvNickname;
	private TextView tvRegdate;
	private LinearLayout contentsLinear;
	private TextView tvContent;
	private Message message;
	
	public ViewWrapperForMessage(WrapperView row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {
		
		imageBg = row.findViewById(R.id.list_message_imageBg); 
		ivProfile = (ImageView) row.findViewById(R.id.list_message_ivProfile);
		tvNickname = (TextView) row.findViewById(R.id.list_message_tvNickName);
		tvRegdate = (TextView) row.findViewById(R.id.list_message_tvRegdate);
		contentsLinear = (LinearLayout) row.findViewById(R.id.list_message_contentsLinear);
		tvContent = (TextView) row.findViewById(R.id.list_message_tvContent);
		ivImage = (ImageView) row.findViewById(R.id.list_message_ivImage);
	}

	@Override
	public void setSizes() {
		
		ResizeUtils.setPadding(row, new int[]{8, 8, 20, 12});
		
		ResizeUtils.viewResize(80, 80, imageBg, 2, Gravity.LEFT, null);
		ResizeUtils.viewResize(80, 80, ivProfile, 2, Gravity.LEFT, null);
		ResizeUtils.viewResize(360, 360, ivImage, 1, Gravity.CENTER_HORIZONTAL, new int[]{0, 20, 0, 20});
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, tvContent, 1, 0, new int[]{8, 8, 8, 8});
		
		FontInfo.setFontSize(tvNickname, 32);
		FontInfo.setFontSize(tvRegdate, 26);
		FontInfo.setFontSize(tvContent, 32);
	}

	@Override
	public void setValues(BaseModel baseModel) {

		if(ivProfile != null) {
			ivProfile.setVisibility(View.INVISIBLE);
		}
		
		try {
			if(baseModel != null && baseModel instanceof Message) {
				message = (Message) baseModel;
				
				String key = ApplicationManager.getDownloadKeyFromTopFragment();
				
				if(!StringUtils.isEmpty(message.getPost_member_id())) {
//////////////////////////////////////////////////////////////////////////					
					//내가 보낸 메세지
					if(message.getPost_member_id().equals(MainActivity.myInfo.getMember_id())) {
						imageBg.setVisibility(View.GONE);
						ivProfile.setVisibility(View.GONE);
						tvNickname.setVisibility(View.GONE);

						ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, 40, tvRegdate, 1, Gravity.RIGHT, new int[]{0, 0, 8, 0});
						ResizeUtils.viewResize(400, LayoutParams.WRAP_CONTENT, contentsLinear, 1,Gravity.RIGHT, null);
						contentsLinear.setBackgroundResource(R.drawable.img_message_send);
						
//////////////////////////////////////////////////////////////////////////
					//다른 유저가 보낸 메세지.
					} else {
						imageBg.setVisibility(View.VISIBLE);
						ivProfile.setVisibility(View.VISIBLE);
						tvNickname.setVisibility(View.VISIBLE);
						
						ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, 40, tvNickname, 1, Gravity.LEFT, new int[]{88, 0, 0, 0});
						ResizeUtils.viewResize(400, LayoutParams.WRAP_CONTENT, contentsLinear, 1, Gravity.TOP|Gravity.LEFT, new int[]{88, 8, 0, 0});
						ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, 40, tvRegdate, 1, Gravity.LEFT, new int[]{8, 0, 0, 0});
						contentsLinear.setBackgroundResource(R.drawable.img_message_receive);
						
						tvNickname.setText(message.getPost_member_nickname());
						setImage(ivProfile, message.getPost_media_src(), key, 160);
					}
					
//////////////////////////////////////////////////////////////////////////					
				}
				
				if(!StringUtils.isEmpty(message.getReg_dt())) {
					tvRegdate.setText(message.getReg_dt());
				}
				
				if(message.getContent_type() == TYPE_TEXT) {
					tvContent.setVisibility(View.VISIBLE);
					ivImage.setVisibility(View.GONE);
					
					if(!StringUtils.isEmpty(message.getContent())) {
						tvContent.setText(message.getContent());
					}
				} else if(message.getContent_type() == TYPE_IMAGE) {
					tvContent.setVisibility(View.GONE);
					ivImage.setVisibility(View.INVISIBLE);
					setImage(ivImage, message.getContent(), key, 400);
				}
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	@Override
	public void setListeners() {

		row.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {

				if(message.getMicrospot_nid() != 0) {
					
					LogUtils.log("###ViewWrapperForMessage.onLongClick.  postId : " + message.getPost_member_id() + ", memberId : " + MainActivity.myInfo.getMember_id());
					
					//isOwner.
					if(message.getPost_member_id().equals(MainActivity.myInfo.getMember_id())) {
						//copy, delete -> pmessage
						((MessagePage) ApplicationManager.getTopFragment())
							.showDialogForMessage(message.getMicrospot_nid(), message.getContent());
						
					//notOwner.
					} else{
						//copy -> dialog.
						String title = row.getContext().getString(R.string.copy);
						String messageString = row.getContext().getString(R.string.wannaCopy);
						OnPositiveClickedListener opcl = new OnPositiveClickedListener() {
							
							@Override
							public void onPositiveClicked() {
								
								if(StringUtils.copyStringToClipboard(row.getContext(), message.getContent())) {
									ToastUtils.showToast(R.string.copyReplyCompleted);
								} else {
									ToastUtils.showToast(R.string.failToCopyReply);
								}
							}
						};
						ApplicationManager.getInstance().getActivity().showAlertDialog(title, messageString, opcl);
					}
				}
				return false;
			}
		});
		
		ivImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				ApplicationManager.getInstance().getActivity()
					.showImageViewerActivity(null, new String[]{message.getContent()}, null, 0);
			}
		});
	}
}
