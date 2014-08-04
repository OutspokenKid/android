package com.zonecomms.common.views;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.zonecomms.clubcage.MainActivity;
import com.zonecomms.clubcage.MainActivity.OnAfterLoginListener;
import com.zonecomms.clubcage.R;
import com.zonecomms.clubcage.classes.ZoneConstants;
import com.zonecomms.clubcage.classes.ZonecommsApplication;
import com.zonecomms.clubcage.fragments.PostPage;
import com.zonecomms.common.models.Member;
import com.zonecomms.common.models.Post;
import com.zonecomms.common.models.Reply;
import com.zonecomms.common.utils.AppInfoUtils;

public class ViewForReply extends RelativeLayout {

	private Context context;
	private Reply reply;
	
	private ImageView ivImage;
	private TextView tvNickname;
	private TextView tvRegdate;
	private TextView tvText;
	private LinearLayout targetLinear;
	
	public ViewForReply(Context context) {
		super(context);
		this.context = context;
		init();
	}
	
	private void init() {
		
		int madeCount = 110612;
		
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
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount);
		rp.leftMargin = l + m * 2;
		tvNickname.setLayoutParams(rp);
		tvNickname.setId(madeCount + 1);
		tvNickname.setTextColor(Color.WHITE);
		tvNickname.setGravity(Gravity.LEFT|Gravity.BOTTOM);
		FontUtils.setFontSize(tvNickname, 30);
		FontUtils.setFontStyle(tvNickname, FontUtils.BOLD);
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
		FontUtils.setFontSize(tvText, 30);
		this.addView(tvText);
		
		tvRegdate = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rp.addRule(RelativeLayout.ALIGN_BOTTOM, madeCount + 1);
		rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rp.topMargin = m;
		rp.rightMargin = m;
		tvRegdate.setLayoutParams(rp);
		tvRegdate.setTextColor(Color.WHITE);
		FontUtils.setFontSize(tvRegdate, 26);
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
	
	public void setReply(final Post post, Reply reply) {
		
		this.reply = reply;
		
		if(reply.getMember() != null) {
			
			final Member member = reply.getMember();
			
			if(!StringUtils.isEmpty(member.getMember_nickname())) {
				tvNickname.setText(member.getMember_nickname());
			}
			
			ivImage.setTag(member.getMedia_src());
			DownloadUtils.downloadBitmap(member.getMedia_src(),
					new OnBitmapDownloadListener() {

						@Override
						public void onError(String url) {
							// TODO Auto-generated method stub		
						}

						@Override
						public void onCompleted(String url, Bitmap bitmap) {

							try {
								LogUtils.log("PostPage.onCompleted."
										+ "\nurl : " + url);

								if (ivImage != null
										&& ivImage.getTag() != null
										&& ivImage.getTag().toString()
												.equals(url)) {
									ivImage.setImageBitmap(bitmap);
								}
							} catch (Exception e) {
								LogUtils.trace(e);
							} catch (OutOfMemoryError oom) {
								LogUtils.trace(oom);
							}
						}
					});
			
			ivImage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ZonecommsApplication.getActivity().showProfilePopup(member.getMember_id(), member.getStatus());
				}
			});
			
			this.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					
					ZonecommsApplication.getActivity().checkLoginAndExecute(new OnAfterLoginListener() {
						
						@Override
						public void onAfterLogin() {

							if(ZonecommsApplication.getTopFragment() != null
									&& ZonecommsApplication.getTopFragment() instanceof PostPage) {
								
								if(MainActivity.myInfo.isAdmin()
										|| (!StringUtils.isEmpty(member.getMember_id()) 
												&& member.getMember_id().equals(MainActivity.myInfo.getMember_id()))
										|| (!StringUtils.isEmpty(post.getMember().getMember_id()) 
												&& post.getMember().getMember_id().equals(MainActivity.myInfo.getMember_id()))) {
									((PostPage) ZonecommsApplication.getTopFragment()).showPopupForReply(true, ViewForReply.this);
								} else {
									((PostPage) ZonecommsApplication.getTopFragment()).showPopupForReply(false, ViewForReply.this);
								}
							}
						}
					});
					
					return true;
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
		}
		
		if(reply.getTarget_member().size() != 0) {
			setTargetViews();
		}
	}

	public void accuse() {
		
		if(reply == null) {
			ToastUtils.showToast(R.string.failToAccuseReply);
			return;
		}
		
		try {
			String url = ZoneConstants.BASE_URL + "reply/bad" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
					"&reply_nid=" + reply.getReply_nid() +
					"&bad_reason_kind=080";

			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {
					
					ToastUtils.showToast(R.string.failToAccuseReply);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("PostPage.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						int errorCode = objJSON.getInt("errorCode");
						
						if(errorCode == 1) {
							ToastUtils.showToast(R.string.accuseReplyCompleted);
						} else {
							ToastUtils.showToast(R.string.failToAccuseReply);
						}
					} catch (Exception e) {
						LogUtils.trace(e);
						ToastUtils.showToast(R.string.failToAccuseReply);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
						ToastUtils.showToast(R.string.failToAccuseReply);
					}
				}
			});
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
						
						ZonecommsApplication.getActivity().showProfilePopup(MEMBER.getMember_id(), MEMBER.getStatus());
					}
				});
				FontUtils.setFontSize(tv, 20);
				linear.addView(tv);
			}
		}
	}

	public Reply getReply() {
		
		return reply;
	}
}