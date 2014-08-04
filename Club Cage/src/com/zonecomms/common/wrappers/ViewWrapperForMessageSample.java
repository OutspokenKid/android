package com.zonecomms.common.wrappers;

import org.json.JSONObject;

import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.zonecomms.clubcage.IntentHandlerActivity;
import com.zonecomms.clubcage.MainActivity.OnPositiveClickedListener;
import com.zonecomms.clubcage.R;
import com.zonecomms.clubcage.classes.ZoneConstants;
import com.zonecomms.clubcage.classes.ZonecommsApplication;
import com.zonecomms.common.models.MessageSample;
import com.zonecomms.common.utils.AppInfoUtils;
import com.zonecomms.common.views.WrapperView;

public class ViewWrapperForMessageSample extends ViewWrapper {

	private View imageBg;
	private ImageView ivProfile;
	private TextView tvNickname;
	private TextView tvRegdate;
	private TextView tvContent;
	private MessageSample messageSample;
	private View newCheck;
	
	public ViewWrapperForMessageSample(WrapperView row, int itemCode) {
		super(row, itemCode);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void bindViews() {

		imageBg = row.findViewById(R.id.list_messagesample_imageBg); 
		ivProfile = (ImageView) row.findViewById(R.id.list_messagesample_ivProfile);
		tvNickname = (TextView) row.findViewById(R.id.list_messagesample_tvNickName);
		tvRegdate = (TextView) row.findViewById(R.id.list_messagesample_tvRegdate);
		tvContent = (TextView) row.findViewById(R.id.list_messagesample_tvContent);
		newCheck = row.findViewById(R.id.list_messagesample_newCheck);
	}

	@Override
	public void setSizes() {

		int p = ResizeUtils.getSpecificLength(8);
		row.setPadding(p, p, p, p);
		
		ResizeUtils.viewResize(80, 80, imageBg, 2, Gravity.LEFT, null);
		ResizeUtils.viewResize(80, 80, ivProfile, 2, Gravity.LEFT, null);
		ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, 40, tvNickname, 2, Gravity.LEFT|Gravity.TOP, new int[]{88, 0, 0, 0});
		ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, 40, tvRegdate, 2, Gravity.RIGHT|Gravity.TOP, null);
		ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, 40, tvContent, 2, Gravity.LEFT|Gravity.BOTTOM, new int[]{88, 0, 0, 0});
		ResizeUtils.viewResize(40, 40, newCheck, 2, Gravity.RIGHT|Gravity.BOTTOM, null);
		
		tvContent.setTextColor(Color.WHITE);
		tvContent.setSingleLine();
		tvContent.setEllipsize(TruncateAt.END);
		
		FontUtils.setFontSize(tvNickname, 32);
		FontUtils.setFontSize(tvRegdate, 24);
		FontUtils.setFontSize(tvContent, 24);
	}

	@Override
	public void setValues(BaseModel baseModel) {

		if(ivProfile != null) {
			ivProfile.setVisibility(View.INVISIBLE);
		}
		
		try {
			if(baseModel != null && baseModel instanceof MessageSample) {
				messageSample = (MessageSample) baseModel;
				
				if(!StringUtils.isEmpty(messageSample.getMember_nickname())) {
					tvNickname.setText(messageSample.getMember_nickname());
					
					setImage(ivProfile, messageSample.getMedia_src());
				}
				
				if(!StringUtils.isEmpty(messageSample.getLatest_date())) {
					tvRegdate.setText(messageSample.getLatest_date());
				}
				
				if(!StringUtils.isEmpty(messageSample.getLatest_msg())) {
					tvContent.setVisibility(View.VISIBLE);
					tvContent.setText(messageSample.getLatest_msg());
				}
				
				if(!StringUtils.isEmpty(messageSample.getNew_check()) && !messageSample.getNew_check().equals("old")) {
					newCheck.setVisibility(View.VISIBLE);
				} else {
					newCheck.setVisibility(View.INVISIBLE);
				}
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	@Override
	public void setListeners() {

		row.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(messageSample != null && !StringUtils.isEmpty(messageSample.getMember_id())) {
					messageSample.setNew_check("old");
					newCheck.setVisibility(View.INVISIBLE);
					
					String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/message" +
							"?member_id=" + messageSample.getMember_id();
					
					IntentHandlerActivity.actionByUri(Uri.parse(uriString));
				}
			}
		});
	
		row.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {

				if(messageSample.getRelation_nid() != 0) {
					String title = row.getContext().getString(R.string.deleteMessage);
					String message = row.getContext().getString(R.string.wannaDelete);
					OnPositiveClickedListener opcl = new OnPositiveClickedListener() {
						
						@Override
						public void onPositiveClicked() {
							deleteMessage(messageSample.getRelation_nid());
						}
					};
					ZonecommsApplication.getActivity().showAlertDialog(title, message, opcl);
				}
				return false;
			}
		});
	}

	@Override
	public void setUnusableView() {
		// TODO Auto-generated method stub
		
	}

	public void deleteMessage(int relation_nid) {
		
		String url = ZoneConstants.BASE_URL 
				+ "microspot/delete" +
				"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
				"&relation_nid=" + messageSample.getRelation_nid();

		ToastUtils.showToast(R.string.wait);
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				ToastUtils.showToast(R.string.failToDeleteMessage);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("ViewWrapperForMessageSample.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.has("errorCode") && objJSON.getInt("errorCode") == 1) {
						ToastUtils.showToast(R.string.deleteCompleted);
						ZonecommsApplication.getTopFragment().onRefreshPage();
					} else {
						ToastUtils.showToast(R.string.failToDeleteMessage);
					}
				} catch (Exception e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToDeleteMessage);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					ToastUtils.showToast(R.string.failToDeleteMessage);
				}
			}
		});
	}
}
