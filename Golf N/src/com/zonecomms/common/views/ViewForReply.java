package com.zonecomms.common.views;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.text.util.Linkify;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader.OnCompletedListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.zonecomms.common.models.Member;
import com.zonecomms.common.models.Reply;
import com.zonecomms.common.utils.AppInfoUtils;
import com.zonecomms.common.utils.ImageDownloadUtils;
import com.zonecomms.golfn.MainActivity;
import com.zonecomms.golfn.R;
import com.zonecomms.golfn.classes.ApplicationManager;
import com.zonecomms.golfn.classes.ZoneConstants;
import com.zonecomms.golfn.fragments.ArticlePage;
import com.zonecomms.golfn.fragments.PostPage;

/**
 * @author HyungGunKim
 *
 */
@SuppressLint("ViewConstructor")
public class ViewForReply extends RelativeLayout {

	private Reply reply;

	private Context context;
	private int madeCount;
	private int spot_nid;
	private LinearLayout parent;
	
	private ImageView ivImage;
	private TextView tvNickname;
	private TextView tvRegdate;
	private TextView tvText;
	private LinearLayout targetLinear;
	
	private OnShowProfilePopupListener onShowProfilePopupListener;
	private OnShowPopupForReplyListener onShowPopupForReplyListener;
	
	public ViewForReply(Context context, int madeCount, int spot_nid, LinearLayout parent) {
		super(context);
		this.context = context;
		this.madeCount = madeCount;
		this.spot_nid = spot_nid;
		this.parent = parent;
		init();
	}
	
	private void init() {
		
		madeCount += 10;
		
		RelativeLayout.LayoutParams rp = null;
		int l = ResizeUtils.getSpecificLength(100);
		int m = ResizeUtils.getSpecificLength(10);
		
		View blank = new View(getContext());
		rp = new RelativeLayout.LayoutParams(1, l + m*2);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		blank.setLayoutParams(rp);
		this.addView(blank);
		
		//id : 0
		ivImage = new ImageView(getContext());
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		rp.leftMargin = m;
		rp.topMargin = m;
		rp.rightMargin = m;
		rp.bottomMargin = m;
		ivImage.setLayoutParams(rp);
		ivImage.setId(madeCount);
		ivImage.setScaleType(ScaleType.CENTER_CROP);
		ivImage.setBackgroundResource(R.drawable.bg_profile);
		this.addView(ivImage);
		
		//id : 1
		tvNickname = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, l/2);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount);
		tvNickname.setLayoutParams(rp);
		tvNickname.setId(madeCount + 1);
		tvNickname.setTextColor(Color.WHITE);
		tvNickname.setGravity(Gravity.LEFT|Gravity.BOTTOM);
		FontInfo.setFontSize(tvNickname, 30);
		FontInfo.setFontStyle(tvNickname, FontInfo.BOLD);
		this.addView(tvNickname);
		
		//id : 3
		targetLinear = new LinearLayout(getContext());
		rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 1);
		rp.addRule(RelativeLayout.BELOW, madeCount + 1);
		targetLinear.setLayoutParams(rp);
		targetLinear.setOrientation(LinearLayout.VERTICAL);
		targetLinear.setId(madeCount + 3);
		this.addView(targetLinear);
		
		//id : 2
		tvText = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 3);
		rp.addRule(RelativeLayout.BELOW, madeCount + 3);
		rp.rightMargin = 50;
		tvText.setLayoutParams(rp);
		tvText.setTextColor(Color.WHITE);
		tvText.setId(madeCount + 2);
		FontInfo.setFontSize(tvText, 30);
		this.addView(tvText);
		
		tvRegdate = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rp.addRule(RelativeLayout.ALIGN_BOTTOM, madeCount + 1);
		rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rp.topMargin = m;
		rp.rightMargin = m;
		tvRegdate.setLayoutParams(rp);
		tvRegdate.setTextColor(Color.WHITE);
		FontInfo.setFontSize(tvRegdate, 26);
		this.addView(tvRegdate);
		
		View bottomBlank = new View(getContext());
		rp = new RelativeLayout.LayoutParams(1, m);
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 2);
		rp.addRule(RelativeLayout.BELOW, madeCount + 2);
		bottomBlank.setLayoutParams(rp);
		this.addView(bottomBlank);
		
		View topLine = new View(getContext());
		rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		topLine.setLayoutParams(rp);
		topLine.setBackgroundColor(Color.DKGRAY);
		this.addView(topLine);
	}
	
	public void setReply(Reply reply) {
		
		this.reply = reply;
		
		if(reply.getMember() != null) {
			
			final Member member = reply.getMember();
			
			if(!StringUtils.isEmpty(member.getMember_nickname())) {
				tvNickname.setText(member.getMember_nickname());
			}
			
			if(!StringUtils.isEmpty(member.getMedia_src())) {
				ImageDownloadUtils.downloadImageImmediately(member.getMedia_src(), 
						ApplicationManager.getDownloadKeyFromTopFragment(),
						ivImage, 100, true);
			}
			
			ivImage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(onShowProfilePopupListener != null) {
						onShowProfilePopupListener.showProfilePopup(member.getMember_id(), member.getStatus());
					}
				}
			});
			
			this.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					
					if(ApplicationManager.getTopFragment() instanceof PostPage
							|| ApplicationManager.getTopFragment() instanceof ArticlePage) {
						
						if(onShowPopupForReplyListener != null) {
							if(MainActivity.myInfo.isAdmin()
									|| (!StringUtils.isEmpty(member.getMember_id()) 
											&& member.getMember_id().equals(MainActivity.myInfo.getMember_id()))) {
								onShowPopupForReplyListener.showPopupForReply(true, ViewForReply.this);
							} else {
								onShowPopupForReplyListener.showPopupForReply(false, ViewForReply.this);
							}
							return true;
						}
					}
					
					return false;
				}
			});
		} else {
			this.setOnLongClickListener(null);
		}
		
		if(!StringUtils.isEmpty(reply.getReg_dt())) {
			tvRegdate.setText(reply.getReg_dt());
		}
		
		if(!StringUtils.isEmpty(reply.getContent())) {
			tvText.setText(reply.getContent());
			Linkify.addLinks(tvText, Linkify.WEB_URLS|Linkify.EMAIL_ADDRESSES);
		}
		
		if(reply.getTarget_member().size() != 0) {
			setTargetViews();
		}
	}

	public void delete() {

		if(reply == null) {
			ToastUtils.showToast(R.string.failToDeleteReply);
			return;
		}
		
		try {
			AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
				
				@Override
				public void onErrorRaised(String url, Exception e) {
					ToastUtils.showToast(R.string.failToDeleteReply);
				}
				
				@Override
				public void onCompleted(String url, String result) {
				
					try {
						LogUtils.log("PostPage.DeleteReply.\nurl : " + url + "\nresult : " + result);
						
						JSONObject objJSON = new JSONObject(result);
						
						int errorCode = objJSON.getInt("errorCode");
						
						if(errorCode == 1) {
							removeFromParent();
							ToastUtils.showToast(R.string.deleteCompleted);
						} else {
							ToastUtils.showToast(R.string.failToDeleteReply);
						}
					} catch(Exception e){
						LogUtils.trace(e);
						ToastUtils.showToast(R.string.failToDeleteReply);
					}
				}
			};
			
			String url = null;
			
			switch(reply.getReplyType()) {
			
			case Reply.TYPE_NORMAL:
				url = ZoneConstants.BASE_URL + "reply/delete";
				break;
				
			case Reply.TYPE_GETHERING:
				url = ZoneConstants.BASE_URL + "boardreply/delete";
				break;
				
			case Reply.TYPE_ARTICLE:
				url = ZoneConstants.BASE_URL + "newSpotReply/delete";
				break;
			}
			
			url += "?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
					"&reply_nid=" + reply.getReply_nid() +
					"&spot_nid=" + spot_nid;
			
			AsyncStringDownloader.download(url, null, ocl);
		} catch(Exception e) {
			LogUtils.trace(e);
			ToastUtils.showToast(R.string.failToDeleteReply);
		}
	}
	
	public void accuse() {
		
		if(reply == null) {
			ToastUtils.showToast(R.string.failToAccuseReply);
			return;
		}

		try {
			AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
				
				@Override
				public void onErrorRaised(String url, Exception e) {
					ToastUtils.showToast(R.string.failToAccuseReply);
				}
				
				@Override
				public void onCompleted(String url, String result) {
				
					try {
						LogUtils.log("PostPage.AccuseReply.\nurl : " + url + "\nresult : " + result);
						
						JSONObject objJSON = new JSONObject(result);
						
						int errorCode = objJSON.getInt("errorCode");
						
						if(errorCode == 1) {
							ToastUtils.showToast(R.string.accuseReplyCompleted);
						} else {
							ToastUtils.showToast(R.string.failToAccuseReply);
						}
					} catch(Exception e){
						LogUtils.trace(e);
						ToastUtils.showToast(R.string.failToAccuseReply);
					}
				}
			};
			
			String url = null;
			
			switch(reply.getReplyType()) {
			
			case Reply.TYPE_NORMAL:
				url = ZoneConstants.BASE_URL + "reply/bad";
				break;
				
			case Reply.TYPE_GETHERING:
				url = ZoneConstants.BASE_URL + "boardreply/bad";
				break;
				
			case Reply.TYPE_ARTICLE:
				url = ZoneConstants.BASE_URL + "newSpotReply/bad";
				break;
			}
			
			url += "?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
					"&reply_nid=" + reply.getReply_nid() +
					"&bad_reason_kind=080";
			
			AsyncStringDownloader.download(url, null, ocl);
		} catch(Exception e) {
			LogUtils.trace(e);
			ToastUtils.showToast(R.string.failToAccuseReply);
		}
	}
	
	public void copy() {

		if(reply != null && !StringUtils.isEmpty(reply.getContent())) {
			if(StringUtils.copyStringToClipboard(context, reply.getContent())) {
				ToastUtils.showToast(R.string.copyReplyCompleted);
			} else {
				ToastUtils.showToast(R.string.failToCopyReply);
			}
		}
	}
	
	public void removeFromParent() {

		parent.removeView(this);
	}

	public void setTargetViews() {

		targetLinear.removeAllViews();
		
		if(reply.getTarget_member().size() == 0) {
			return;
		}
		
		int linearSize = (reply.getTarget_member().size() + 2) / 3;
		
		for(int i=0; i<linearSize; i++) {
			LinearLayout linear = new LinearLayout(context);
			linear.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			linear.setOrientation(LinearLayout.HORIZONTAL);
			targetLinear.addView(linear);
			
			int textSize = i == linearSize - 1 ? reply.getTarget_member().size() - i*3 : 3;
			
			for(int j=0; j<textSize; j++) {
				int index = i*3 + j;
				
				final Member MEMBER = reply.getTarget_member().get(index);
						
				
				TextView tv = new TextView(context);
				ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, tv, 1, 0, 
						new int[]{8, 16, 8, 16}, new int[]{6, 6, 6, 6});
				tv.setMaxWidth(ResizeUtils.getSpecificLength(120));
				tv.setTextColor(Color.WHITE);
				tv.setGravity(Gravity.CENTER);
				tv.setBackgroundColor(Color.DKGRAY);
				tv.setSingleLine();
				tv.setEllipsize(TruncateAt.END);
				tv.setText(MEMBER.getMember_nickname());
				tv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {

						if(onShowProfilePopupListener != null) {
							onShowProfilePopupListener.showProfilePopup(MEMBER.getMember_id(), MEMBER.getStatus());
						}
					}
				});
				FontInfo.setFontSize(tv, 20);
				linear.addView(tv);
			}
		}
	}
	
	public void setOnShowProfilePopupListener(OnShowProfilePopupListener onShowProfilePopupListener) {
		this.onShowProfilePopupListener = onShowProfilePopupListener;
	}
	
	public void setOnShowPopupForReplyListener(OnShowPopupForReplyListener onShowPopupForReplyListener) {
		this.onShowPopupForReplyListener = onShowPopupForReplyListener;
	}
	
/////////////////////////////// Interfaces.	
	
	public interface OnShowProfilePopupListener {
		
		public void showProfilePopup(String member_id, int status);
	}
	
	public interface OnShowPopupForReplyListener {
		
		public void showPopupForReply(boolean forOwner, ViewForReply vfr);
	}
}