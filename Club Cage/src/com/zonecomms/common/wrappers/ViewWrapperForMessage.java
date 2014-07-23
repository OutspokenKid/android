package com.zonecomms.common.wrappers;

import org.json.JSONObject;

import com.outspoken_kid.utils.StringUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.classes.ApplicationManager;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader.OnCompletedListener;
import com.zonecomms.common.utils.AppInfoUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.zonecomms.clubcage.MainActivity;
import com.zonecomms.clubcage.R;
import com.zonecomms.clubcage.MainActivity.OnPositiveClickedListener;
import com.zonecomms.clubcage.classes.ZoneConstants;
import com.zonecomms.common.models.BaseModel;
import com.zonecomms.common.models.Message;
import com.zonecomms.common.wrapperviews.WrapperView;

public class ViewWrapperForMessage extends ViewWrapper {

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
	public void setSize() {
		
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
					setImage(ivImage, message.getContent(), key, 400);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setListener() {

		if(MainActivity.myInfo.getMember_id().equals(message.getPost_member_id())) {
			row.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {

					if(message.getMicrospot_nid() != 0) {
						String title = ApplicationManager.getInstance().getMainActivity().getString(R.string.deleteMessage);
						String messageString = ApplicationManager.getInstance().getMainActivity().getString(R.string.wannaDelete);
						OnPositiveClickedListener opcl = new OnPositiveClickedListener() {
							
							@Override
							public void onPositiveClicked() {
								deleteMessage(message.getMicrospot_nid());
							}
						};
						ApplicationManager.getInstance().getMainActivity().showAlertDialog(title, messageString, opcl);
					}
					return false;
				}
			});
		}
		
		ivImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				message.getContent();
				ApplicationManager.getInstance().getMainActivity().showImageViewerActivity(null, new String[]{message.getContent()}, null, 0);
			}
		});
	}

	@Override
	public void setUnusableView() {
		// TODO Auto-generated method stub
		
	}

	public void deleteMessage(int microspot_nid) {
		
		String url = ZoneConstants.BASE_URL
				+ "microspot/microspot_delete" +
				"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
				"&microspot_nid=" + message.getMicrospot_nid();
		
		AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
			
			@Override
			public void onErrorRaised(String url, Exception e) {
				ToastUtils.showToast(R.string.failToDeleteMessage);
			}
			
			@Override
			public void onCompleted(String url, String result) {
				
				try {
					LogUtils.log("ViewWrapperForMessage.deleteMessage.  url : "+ url + "\nresult : " + result);
					
					JSONObject objJSON = new JSONObject(result);
					
					if(objJSON.has("errorCode") && objJSON.getInt("errorCode") == 1) {
						ToastUtils.showToast(R.string.deleteCompleted);
						ApplicationManager.refreshTopPage();
					} else {
						ToastUtils.showToast(R.string.failToDeleteMessage);
					}
				} catch(Exception e) {
					e.printStackTrace();
					ToastUtils.showToast(R.string.failToDeleteMessage);
				}
			}
		};
		ToastUtils.showToast(R.string.wait);
		AsyncStringDownloader.download(url, ApplicationManager.getDownloadKeyFromTopFragment(), ocl);
	}
}
