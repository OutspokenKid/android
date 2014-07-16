package com.zonecomms.golfn.fragments;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.res.Resources;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.classes.ViewUnbindHelper;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader.OnCompletedListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo_dark.HoloStyleButton;
import com.outspoken_kid.views.holo_dark.HoloStyleEditText;
import com.outspoken_kid.views.holo_dark.HoloStyleSpinnerPopup;
import com.outspoken_kid.views.holo_dark.HoloStyleSpinnerPopup.OnItemClickedListener;
import com.zonecomms.common.models.Member;
import com.zonecomms.common.models.Post;
import com.zonecomms.common.models.Reply;
import com.zonecomms.common.utils.AppInfoUtils;
import com.zonecomms.common.utils.ImageDownloadUtils;
import com.zonecomms.common.views.PostInfoLayout;
import com.zonecomms.common.views.ReplyLoadingView;
import com.zonecomms.common.views.ReplyLoadingView.OnLoadingViewClickedListener;
import com.zonecomms.common.views.ViewForReply;
import com.zonecomms.common.views.ViewForReply.OnShowPopupForReplyListener;
import com.zonecomms.common.views.ViewForReply.OnShowProfilePopupListener;
import com.zonecomms.golfn.MainActivity;
import com.zonecomms.golfn.R;
import com.zonecomms.golfn.WriteForFleaActivity;
import com.zonecomms.golfn.classes.ApplicationManager;
import com.zonecomms.golfn.classes.BaseFragment;
import com.zonecomms.golfn.classes.ZoneConstants;

public class PostPage extends BaseFragment {

	private static final int NUM_OF_REPLY = 10;
	
	private Post post;

	private PostInfoLayout postInfoLayout;
	private TextView tvText;
	private ScrollView scrollView;
	private LinearLayout innerLayout;
	private LinearLayout mustvalueLinear;
	private TextView[] tvInputs;
	private TextView[] tvValues;
	private TextView tvSpec;
	private LinearLayout contentLayout;
	private LinearLayout replyLinear;
	private LinearLayout targetLinear;
	private LinearLayout writeLinear;
	private HoloStyleEditText editText;
	private HoloStyleButton btnSubmit;
	private ReplyLoadingView replyLoadingView;

	private HoloStyleSpinnerPopup spForPost;
	private HoloStyleSpinnerPopup spForReply;
	private ViewForReply selectedVFR;

	private boolean isGethering;
	private int spot_nid;
	private int lastIndexno;
	private boolean isNeedToShowBottom;
	private int board_id;
	
	private ArrayList<Member> targets = new ArrayList<Member>();
	
	@Override
	protected void bindViews() {
		
		tvText = (TextView) mThisView.findViewById(R.id.postPage_tvText);
		
		scrollView = (ScrollView) mThisView.findViewById(R.id.postPage_scrollView);
		innerLayout = (LinearLayout) mThisView.findViewById(R.id.postPage_innerLayout);
		mustvalueLinear = (LinearLayout) mThisView.findViewById(R.id.postPage_mustvalueLinear);
		contentLayout = (LinearLayout) mThisView.findViewById(R.id.postPage_contentLayout);
		replyLinear = (LinearLayout) mThisView.findViewById(R.id.postPage_replyLinear);
		writeLinear = (LinearLayout) mThisView.findViewById(R.id.postPage_writeLinear);
		editText = (HoloStyleEditText) mThisView.findViewById(R.id.postPage_editText);
		btnSubmit = (HoloStyleButton) mThisView.findViewById(R.id.postPage_submitButton);
		targetLinear = (LinearLayout) mThisView.findViewById(R.id.postPage_targetLinear);
		
		tvInputs = new TextView[WriteForFleaActivity.NUMBER_OF_TEXTS];
		tvValues = new TextView[WriteForFleaActivity.NUMBER_OF_TEXTS];
		
		Resources res = getResources();
		int resId = 0;
		for(int i=0; i<WriteForFleaActivity.NUMBER_OF_TEXTS; i++) {
			
			try {
				resId = res.getIdentifier("postPage_tvInput" + (i+1), "id", 
						"com.zonecomms." + ZoneConstants.PAPP_ID);
				tvInputs[i] = (TextView) mThisView.findViewById(resId);
				
				resId = res.getIdentifier("postPage_tvValue" + (i+1), "id", 
						"com.zonecomms." + ZoneConstants.PAPP_ID);
				tvValues[i] = (TextView) mThisView.findViewById(resId);
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
		}
		
		tvSpec = (TextView) mThisView.findViewById(R.id.postPage_tvSpec);
	}

	@Override
	protected void setVariables() {

		if(getArguments() != null) {
			spot_nid = getArguments().getInt("spot_nid");
			isGethering = getArguments().getBoolean("isGethering");
			isNeedToShowBottom = getArguments().getBoolean("isNeedToShowBottom");
		}
		
		editText.getEditText().setHint(R.string.hintForReply);
		editText.getEditText().setHintTextColor(Color.rgb(120, 120, 120));
		editText.getEditText().setTextColor(Color.WHITE);
		editText.getEditText().setSingleLine(false);
		btnSubmit.setTextColor(Color.WHITE);
		btnSubmit.setText(R.string.submitReply);
		
		Resources res = getResources();
		int resId = 0;
		
		for(int i=0; i<WriteForFleaActivity.NUMBER_OF_TEXTS; i++) {
			try {
				resId = res.getIdentifier("textForWriteFlea" + (i+1), "string", 
						"com.zonecomms." + ZoneConstants.PAPP_ID);
				tvInputs[i].setText(getString(resId));
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
		}
		
		tvSpec.setText(R.string.textForWriteFlea_spec);
	}

	@Override
	protected void createPage() {

		try {
			postInfoLayout = new PostInfoLayout(mContext);
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 150, postInfoLayout, 1, 0, new int[]{8, 8, 8, 8});
			innerLayout.addView(postInfoLayout, 0);
			
			spForPost = new HoloStyleSpinnerPopup(mContext);
			((FrameLayout) mThisView.findViewById(R.id.postPage_mainLayout)).addView(spForPost);
			
			spForReply = new HoloStyleSpinnerPopup(mContext);
			((FrameLayout) mThisView.findViewById(R.id.postPage_mainLayout)).addView(spForReply);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	@Override
	protected void setListeners() {
		
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String text = editText.getEditText().getText().toString();
				
				if(!StringUtils.isEmpty(text)) {
					writeReply(text);
				} else {
					ToastUtils.showToast(R.string.inputContent);
				}
			}
		});

		spForPost.setOnItemClickedListener(new OnItemClickedListener() {
			
			@Override
			public void onItemClicked(int position, String itemString) {
				
				if(itemString.equals(getString(R.string.accuse))) {
					accuse();
				} else if(itemString.equals(getString(R.string.scrap))) {
					scrap();
				} else if(itemString.equals(getString(R.string.edit))) {
					edit();
				} else if(itemString.equals(getString(R.string.delete))) {
					delete();
				}
				
				spForPost.hidePopup();
			}
		});
		
		spForReply.setOnItemClickedListener(new OnItemClickedListener() {
			
			@Override
			public void onItemClicked(int position, String itemString) {
				
				if(selectedVFR != null) {
					if(itemString.equals(getString(R.string.reply_copy))) {
						selectedVFR.copy();
					} else if(itemString.equals(getString(R.string.reply_accuse))) {
						selectedVFR.accuse();
					} else if(itemString.equals(getString(R.string.reply_delete))) {
						selectedVFR.delete();
					}

					selectedVFR = null;
					spForReply.hidePopup();
				}
			}
		});
	
		postInfoLayout.setMoreButtonClicked(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				LogUtils.log("#########\nisAdmin : " + MainActivity.myInfo.isAdmin() +  
						"\nid : " + MainActivity.myInfo.getMember_id() +
						"\npost.id : " + post.getMember().getMember_id() +
						"\n#########");
				
				if(MainActivity.myInfo.isAdmin()
						|| MainActivity.myInfo.getMember_id().equals(post.getMember().getMember_id())) {
					showPopupForPost(true);
				} else {
					showPopupForPost(false);
				}
			}
		});
	}

	@Override
	protected void setSizes() {

		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, contentLayout, 1, Gravity.LEFT, new int[]{8, 0, 8, 0});

		for(int i=0; i<WriteForFleaActivity.NUMBER_OF_TEXTS; i++) {
			ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, 40, tvInputs[i], 1, 0, new int[]{(i==0?20:60), (i==1?0:20), 0, 0});
			ResizeUtils.viewResize(540, 70, tvValues[i], 1, 0, new int[]{(i==0?40:80), 0, 0, 0});
			
			FontInfo.setFontSize(tvInputs[i], 30);
			FontInfo.setFontSize(tvValues[i], 26);
		}
		
		ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, 40, tvSpec, 1, 0, new int[]{20, 20, 0, 0});
		FontInfo.setFontSize(tvSpec, 30);
		
		int p = ResizeUtils.getSpecificLength(20);
		tvText.setPadding(p, p, p, p);
		FontInfo.setFontSize(tvText, 30);
		
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, writeLinear, 1, Gravity.LEFT, new int[]{8, 8, 8, 8});
		
		int m = ResizeUtils.getSpecificLength(8);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, ResizeUtils.getSpecificLength(90), 1);
		lp.setMargins(m, m, m, m);
		editText.setLayoutParams(lp);
		FontInfo.setFontAndHintSize(editText.getEditText(), 25, 20);
		
		ResizeUtils.viewResize(120, 80, btnSubmit, 1, Gravity.CENTER_VERTICAL, new int[]{0, 8, 8, 8});
		FontInfo.setFontSize(btnSubmit.getTextView(), 20);
		
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, targetLinear, 2, Gravity.BOTTOM, new int[]{0, 0, 0, 108});
	}

	@Override
	protected void downloadInfo() {
		
		AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
			
			@Override
			public void onErrorRaised(String url, Exception e) {

				LogUtils.log("PostPage.downloadInfo.onError.\nurl : " + url);
				setPage(false);
			}
			
			@Override
			public void onCompleted(String url, String result) {
				
				LogUtils.log("PostPage.onCompleted.  url : " + url + "\nresult : " + result);
				
				try {
					JSONObject objResult = new JSONObject(result);
					post = new Post(objResult.getJSONObject("data"));
					setPage(true);
				} catch(Exception e) {
					LogUtils.trace(e);
					setPage(false);
				}
			}
		};
		
		String url = "";
		
		if(isGethering) { 
			url += ZoneConstants.BASE_URL + "boardspot/detail";
		} else {
			url += ZoneConstants.BASE_URL + "spot/detail";
		}
				
		url += "?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
				"&image_size=640" +
				"&spot_nid=" + spot_nid;
		super.downloadInfo();
		AsyncStringDownloader.download(url, getDownloadKey(), ocl);
	}
	
	@Override
	protected void setPage(boolean successDownload) {

		if(successDownload) {
			postInfoLayout.setPostInfo(post);

			if(post.getMustvalue() != null
					&& post.getMustvalue().length != 0
					&& !StringUtils.isEmpty(post.getMustvalue()[0])) {
				addMustValues();
				
				board_id = post.getBoard_nid();

				tvInputs[0].setVisibility(View.VISIBLE);
				tvInputs[5].setVisibility(View.VISIBLE);
				tvInputs[6].setVisibility(View.VISIBLE);
				tvValues[0].setVisibility(View.VISIBLE);
				tvValues[5].setVisibility(View.VISIBLE);
				tvValues[6].setVisibility(View.VISIBLE);
				
				switch(board_id) {
				
				case 5:
				case 6:
					tvInputs[1].setVisibility(View.VISIBLE);
					tvInputs[2].setVisibility(View.VISIBLE);
					tvInputs[3].setVisibility(View.VISIBLE);
					tvValues[1].setVisibility(View.VISIBLE);
					tvValues[2].setVisibility(View.VISIBLE);
					tvValues[3].setVisibility(View.VISIBLE);
					break;
				
				case 7:
					tvInputs[2].setVisibility(View.VISIBLE);
					tvInputs[3].setVisibility(View.VISIBLE);
					tvInputs[4].setVisibility(View.VISIBLE);
					tvValues[2].setVisibility(View.VISIBLE);
					tvValues[3].setVisibility(View.VISIBLE);
					tvValues[4].setVisibility(View.VISIBLE);
					break;
					
				case 8:
					break;
				}
			} else {
				mustvalueLinear.setVisibility(View.GONE);
			}
			
			if(!StringUtils.isEmpty(post.getContent())) {
				tvText.setText(post.getContent());
			}
			
			removeScreenshots();
			addScreenshots();

			innerLayout.setVisibility(View.VISIBLE);
			loadReplis();
		} else {
			ToastUtils.showToast(R.string.failToLoadPost);
		}
		
		super.setPage(successDownload);
	}

	@Override
	public String getTitleText() {

		if(title == null) {
			title = "POST";
		}
		
		return title;
	}

	@Override
	protected int getContentViewId() {

		return R.id.postPage_mainLayout;
	}

	@Override
	public boolean onBackKeyPressed() {
		
		if(spForPost.getVisibility() == View.VISIBLE) {
			spForPost.hidePopup();
			return true;
		} else if(spForReply.getVisibility() == View.VISIBLE) {
			spForReply.hidePopup();
			return true;
		}
		
		return false;
	}

	@Override
	public void onRefreshPage() {

		ArrayList<View> views = new ArrayList<View>();
		
		int length = replyLinear.getChildCount();
		for(int i=0; i<length; i++) {
			views.add(replyLinear.getChildAt(i));
		}
		
		replyLinear.removeAllViews();
		
		length = views.size();
		for(int i=0; i<length; i++) {
			
			ViewUnbindHelper.unbindReferences(views.get(i));
		}
		
		lastIndexno = 0;
		downloadInfo();
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		
		if(!hidden) {
			
			if(mActivity.getSponserBanner() != null) {
				mActivity.getSponserBanner().hideBanner();
			}
			
			mActivity.getTitleBar().showHomeButton();
			mActivity.getTitleBar().hideWriteButton();
			
			onRefreshPage();
		}
	}

	@Override
	public void onDetach() {
		
		super.onDetach();
	}
	
	@Override
	public void onSoftKeyboardShown() {
		scrollView.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				scrollView.fullScroll(ScrollView.FOCUS_DOWN);
			}
		}, 200);
	}

	@Override
	public void onSoftKeyboardHidden() {
		
	}

////////////////////// Custom methods.

	public void addMustValues() {

		try {
			String[] mustvalues = post.getMustvalue();
			
			for(int i=0; i<WriteForFleaActivity.NUMBER_OF_TEXTS; i++) {
				tvValues[i].setText("-");
			}
			
			int size = Math.min(mustvalues.length, WriteForFleaActivity.NUMBER_OF_TEXTS);
			for (int i = 0; i < size; i++) {

				if(!StringUtils.isEmpty(mustvalues[i])) {
					tvValues[i].setText(mustvalues[i]);
				}
				
				tvInputs[i].setVisibility(View.GONE);
				tvValues[i].setVisibility(View.GONE);
			}
			
			mustvalueLinear.setVisibility(View.VISIBLE);
		} catch (Exception e) {
			LogUtils.trace(e);
			mustvalueLinear.setVisibility(View.GONE);
		} catch (Error e) {
			LogUtils.trace(e);
			mustvalueLinear.setVisibility(View.GONE);
		}
	}
	
	public void removeScreenshots() {

		try {
			ArrayList<View> views = new ArrayList<View>();

			int length = contentLayout.getChildCount();
			for(int i=0; i<length; i++) {
				
				if(!(contentLayout.getChildAt(i) instanceof TextView)) {
					views.add(contentLayout.getChildAt(i));
				}
			}

			length = views.size();
			for(int i=0; i<length; i++) {
				contentLayout.removeView(views.get(i));
			}
			
			length = views.size();
			for(int i=0; i<length; i++) {
				ViewUnbindHelper.unbindReferences(views.get(i));
			}
		} catch(Exception e) {
		}
	}
	
	public void addScreenshots() {
		
		if(post == null || post.getMedias() == null || post.getMedias().length == 0) {
			tvText.setMinHeight(ResizeUtils.getSpecificLength(466));
			return;
		} else {
			tvText.setMinHeight(0);
		}
		
		int size = post.getMedias().length;
		for(int i=0; i<size; i++) {
			
			final int index = i;
			
			int width = post.getMedias()[0].getWidth();
			int height = post.getMedias()[0].getHeight();
			float scale = (float) height / (float) width;
			int scaledHeight = (int)(624 * scale);
			
			FrameLayout frame = new FrameLayout(mContext);
			ResizeUtils.viewResize(624, scaledHeight, frame, 1, Gravity.CENTER_HORIZONTAL, new int[]{0, 20, 0, 20});
			contentLayout.addView(frame);
			
			ProgressBar progress = new ProgressBar(mContext);
			ResizeUtils.viewResize(50, 50, progress, 2, Gravity.CENTER, null);
			frame.addView(progress);
			
			ImageView image = new ImageView(mContext);
			image.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			image.setScaleType(ScaleType.CENTER_CROP);
			image.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					mActivity.showImageViewerActivity(null, post.getMedias(), index);
				}
			});
			image.setDrawingCacheEnabled(true);
			frame.addView(image);
			
			ImageDownloadUtils.downloadImageImmediately(post.getMedias()[i].getMedia_src(), getDownloadKey(), image, 640, true);
		}
	}

	public void addReplyLoadingView() {

		try {
			if(replyLoadingView == null) {
				replyLoadingView = new ReplyLoadingView(mContext);
				ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 80, replyLoadingView, 1, 0, new int[]{8, 8, 8, 0});
				replyLoadingView.setOnLoadingViewClickedListener(new OnLoadingViewClickedListener() {
					
					@Override
					public void onLoadingViewClicked(int mode) {

						if(mode == ReplyLoadingView.MODE_SEEMORE) {
							loadReplis();
						}
					}
				});
			}
			
			removeLoadingView();
			innerLayout.addView(replyLoadingView, innerLayout.getChildCount() - 1);
			writeLinear.setVisibility(View.VISIBLE);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void removeLoadingView() {
		
		try {
			if(replyLoadingView != null) {
				innerLayout.removeView(replyLoadingView);
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void loadReplis() {
		
		if(replyLoadingView != null) {
			replyLoadingView.setMode(ReplyLoadingView.MODE_LOADING);
		}
		
		AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
			
			@Override
			public void onErrorRaised(String url, Exception e) {
				ToastUtils.showToast(R.string.failToLoadReplies);
				removeLoadingView();
			}
			
			@Override
			public void onCompleted(String url, String result) {
				
				try {
					LogUtils.log("loadReplies.onCompleted.\nurl : " + url + "\nresult : " + result);
					
					JSONArray arJSON = (new JSONObject(result)).getJSONArray("data");
					
					int length = arJSON.length();

					if(replyLinear != null) {
						replyLinear.removeAllViews();
					}
					
					ArrayList<ViewForReply> replyViews = new ArrayList<ViewForReply>();
					
					if(length != 0) {
						
						for(int i=0; i<length; i++) {
							final Reply reply = new Reply(arJSON.getJSONObject(i));
							
							if(isGethering) {
								reply.setReplyType(Reply.TYPE_GETHERING);
							} else {
								reply.setReplyType(Reply.TYPE_NORMAL);
							}
							
							ViewForReply vfr = new ViewForReply(mContext, madeCount, post.getSpot_nid(), replyLinear);
							ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, vfr, 
									1, 0, new int[]{8, 0, 8, 0});
							vfr.setReply(reply);
							vfr.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									
									if(!hasMember(reply.getMember())) {
										targets.add(0, reply.getMember());
										setTargetViews();
									}
								}
							});
							vfr.setOnShowPopupForReplyListener(new OnShowPopupForReplyListener() {
								
								@Override
								public void showPopupForReply(boolean forOwner, ViewForReply vfr) {
									PostPage.this.showPopupForReply(forOwner, vfr);
								}
							});
							vfr.setOnShowProfilePopupListener(new OnShowProfilePopupListener() {
								
								@Override
								public void showProfilePopup(String member_id, int status) {
									mActivity.showProfilePopup(member_id, status);
								}
							});
							replyViews.add(vfr);
							
							if(i == 0) {
								lastIndexno = reply.getIndexno();
							}
						}

						for(int i=length - 1; i>=0; i--) {
							replyLinear.addView(replyViews.get(i), 0);
						}
						
						if(length == NUM_OF_REPLY) {
							addReplyLoadingView();
							replyLoadingView.setMode(ReplyLoadingView.MODE_SEEMORE);
						} else {
							removeLoadingView();
						}

						LogUtils.log("###PostPage.onCompleted.  isNeedToShowBottom : " + isNeedToShowBottom);
						
						if(isNeedToShowBottom) {
							isNeedToShowBottom = false;
							scrollView.postDelayed(new Runnable() {
								
								@Override
								public void run() {
									scrollView.fullScroll(ScrollView.FOCUS_DOWN);
								}
							}, 100);
						} else {
							
						}
					} else {
						removeLoadingView();
					}
				} catch(Exception e) {
					LogUtils.trace(e);
				}
			}
		};
		
		String url = null;
		
		if(isGethering) {
			url = ZoneConstants.BASE_URL + "boardreply/list";
		} else{
			url = ZoneConstants.BASE_URL + "reply/list";
		}
		
		url += "?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
				"&spot_nid=" + spot_nid +
				"&image_size=100" +
				"&last_reply_nid=" + lastIndexno;
		AsyncStringDownloader.download(url, getDownloadKey(), ocl);
	}
	
	public void writeReply(String text) {

		mActivity.showLoadingView();
		
		AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
			
			@Override
			public void onErrorRaised(String url, Exception e) {
				
				LogUtils.log("writeReply.onError.  url : " + url);
				
				ToastUtils.showToast(R.string.failToSendReply);
				mActivity.hideLoadingView();
			}
			
			@Override
			public void onCompleted(String url, String result) {
				
				mActivity.hideLoadingView();
				
				LogUtils.log("writeReply.onCompleted.\nurl : " + url +"\nresult : " + result);
				
				try {
					if((new JSONObject(result)).getInt("errorCode") == 1) {
						SoftKeyboardUtils.hideKeyboard(mContext, editText);
						editText.getEditText().setText("");
						
						isNeedToShowBottom = true;
						replyLinear.removeAllViews();
						lastIndexno = 0;
						loadReplis();
						
						targets.clear();
						setTargetViews();
					} else {
						ToastUtils.showToast(R.string.failToSendReply);
					}
				} catch(Exception e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToSendReply);
				}
			}
		};
		
		try {
			String url = null;
			
			if(isGethering) {
				url = ZoneConstants.BASE_URL + "boardreply/write?origin_sb_id=" + 
						ZoneConstants.PAPP_ID;
			} else {
				url = ZoneConstants.BASE_URL + "reply/write?sb_id=" +
						ZoneConstants.PAPP_ID;
			}
			
			url += "&" + AppInfoUtils.getAppInfo(AppInfoUtils.WITHOUT_SB_ID) +
					"&spot_nid=" + post.getSpot_nid() +
					"&content=" + URLEncoder.encode(text, "UTF-8");
			
			if(targets.size() != 0) {
				
				url += "&target_id=";

				int size = targets.size();
				for(int i=0; i<size; i++) {
					
					if(i != 0) {
						url += ":::";
					}
					
					url += targets.get(i).getMember_id();
				}
			}
			
			AsyncStringDownloader.download(url, getDownloadKey(), ocl);
		} catch(Exception e) {
			LogUtils.trace(e);
			ToastUtils.showToast(R.string.failToSendReply);
		}
	}
	
	public boolean hasMember(Member member) {
		
		if(StringUtils.isEmpty(member.getMember_id())
				 || member.getMember_id().equals(MainActivity.myInfo.getMember_id())) {
			return true;
		}
		
		for(Member m : targets) {
			if(m != null && !StringUtils.isEmpty(m.getMember_id())
					&& m.getMember_id().equals(member.getMember_id())) {
				return true;
			}
		}
		
		return false;
	}
	
	public void setTargetViews() {

		targetLinear.removeAllViews();
		
		if(targets.size() == 0) {
			return;
		}
		
		int linearSize = (targets.size() + 2) / 3;
		
		for(int i=0; i<linearSize; i++) {
			LinearLayout linear = new LinearLayout(mContext);
			linear.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			linear.setOrientation(LinearLayout.HORIZONTAL);
			targetLinear.addView(linear);
			
			int textSize = i == linearSize - 1 ? targets.size() - i*3 : 3;
			
			for(int j=0; j<textSize; j++) {
				int index = i*3 + j;
				
				TextView tv = new TextView(mContext);
				ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, tv, 1, 0, 
						new int[]{16, 16, 16, 16}, new int[]{8, 8, 8, 8});
				tv.setMaxWidth(ResizeUtils.getSpecificLength(180));
				tv.setTextColor(Color.WHITE);
				tv.setGravity(Gravity.CENTER);
				tv.setBackgroundColor(Color.DKGRAY);
				tv.setText(targets.get(index).getMember_nickname());
				tv.setTag(targets.get(index));
				tv.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						targets.remove((Member)v.getTag());
						setTargetViews();
					}
				});
				FontInfo.setFontSize(tv, 22);
				linear.addView(tv);
			}
		}
	}

	public void showPopupForPost(boolean forOwner) {

		if(post == null) {
			return;
		}
		
		spForPost.clearItems();

		if(forOwner) {
			spForPost.addItem(getString(R.string.edit));
			spForPost.addItem(getString(R.string.delete));
		} else {
			spForPost.addItem(getString(R.string.accuse));
			spForPost.addItem(getString(R.string.scrap));
		}
		
		spForPost.notifyDataSetChanged();
		spForPost.showPopup();
	}
	
	public void showPopupForReply(boolean forOwner, ViewForReply vfr) {

		if(vfr == null) {
			return;
		} else {
			selectedVFR = vfr;
		}
		
		spForReply.setTitle(getString(R.string.selectPlease));
		
		spForReply.clearItems();
		spForReply.addItem(getString(R.string.reply_copy));
		spForReply.addItem(getString(R.string.reply_accuse));
		
		if(forOwner) {
			spForReply.addItem(getString(R.string.reply_delete));
		}
		
		spForReply.notifyDataSetChanged();
		spForReply.showPopup();
	}

	public void accuse() {

		try {
			AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
				
				@Override
				public void onErrorRaised(String url, Exception e) {
					
					LogUtils.log("accuse.error\nurl : " + url);
					ToastUtils.showToast(R.string.failToAccuse);
				}
				
				@Override
				public void onCompleted(String url, String result) {
	
					LogUtils.log("accuse.complete\nurl : " + url + "\nresult : " + result);
					
					try {
						JSONObject objJSON = new JSONObject(result);
						
						int errorCode = objJSON.getInt("errorCode");
						
						if(errorCode == 1) {
							
							if(objJSON.has("errorMsg")) {
								ToastUtils.showToast(objJSON.getString("errorMsg"));
							} else {
								ToastUtils.showToast(R.string.accuseCompleted);
							}
						} else {
							ToastUtils.showToast(R.string.failToAccuse);
						}
					} catch(Exception e){
						LogUtils.trace(e);
						ToastUtils.showToast(R.string.failToAccuse);
					}
				}
			};

			String url = null;
			
			if(isGethering) {
				url = ZoneConstants.BASE_URL + "boardspot/badSpot";
			} else {
				url = ZoneConstants.BASE_URL + "spot/badSpot";
			}
			
			url = "?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
					"&spot_nid=" + post.getSpot_nid() +
					"&bad_reason_kind=080";
			
			AsyncStringDownloader.download(url, null, ocl);
		} catch(Exception e) {
			LogUtils.trace(e);
			ToastUtils.showToast(R.string.failToAccuse);
		}
		
	}
	
	public void scrap() {
		
		try {
			AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
				
				@Override
				public void onErrorRaised(String url, Exception e) {
					
					LogUtils.log("scrap.error\nurl : " + url);
					ToastUtils.showToast(R.string.failToScrapPost);
				}
				
				@Override
				public void onCompleted(String url, String result) {

					LogUtils.log("scrap.error\nurl : " + url);
					LogUtils.log("scrap.complete\nurl : " + url + "\nresult : " + result);
					
					try {
						JSONObject objJSON = new JSONObject(result);
						
						int errorCode = objJSON.getInt("errorCode");
						
						if(errorCode == 1) {
							
							if(objJSON.has("errorMsg")) {
								ToastUtils.showToast(objJSON.getString("errorMsg"));
							} else {
								ToastUtils.showToast(R.string.scrapCompleted);
							}
						} else {
							ToastUtils.showToast(R.string.failToScrapPost);
						}
					} catch(Exception e){
						LogUtils.trace(e);
						ToastUtils.showToast(R.string.failToScrapPost);
					}
				}
			};
			
			String url = ZoneConstants.BASE_URL + "spot/scrap" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
					"&spot_nid=" + post.getSpot_nid();
			
			AsyncStringDownloader.download(url, null, ocl);
		} catch(Exception e) {
			LogUtils.trace(e);
			ToastUtils.showToast(R.string.failToScrapPost);
		}
	}
	
	public void edit() {
		
		String[] imageUrls = null;
		
		if(post.getMedias() != null) {
			
			int length = post.getMedias().length;
			imageUrls = new String[length];
			for(int i=0; i<length; i++) {
				imageUrls[i] = post.getMedias()[i].getMedia_src();
			}
		}

		if(post.getMustvalue() != null
				&& post.getMustvalue()[0] != null
				&& !StringUtils.isEmpty(post.getMustvalue()[0])) {
			mActivity.showWriteForFleaActivity(board_id, getString(R.string.editPost), isGethering, 
					post.getSpot_nid(), post.getContent(), imageUrls, post.getMustvalue());
		} else {
			mActivity.showWriteActivity(getString(R.string.editPost), isGethering, 
					post.getSpot_nid(), post.getContent(), imageUrls);
		}
	}
	
	public void delete() {
		
		try {
			AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
				
				@Override
				public void onErrorRaised(String url, Exception e) {
					
					LogUtils.log("delete.error\nurl : " + url);
					ToastUtils.showToast(R.string.failToDeletePost);
				}
				
				@Override
				public void onCompleted(String url, String result) {
					
					LogUtils.log("delete.complete\nurl : " + url + "\nresult : " + result);
					ToastUtils.showToast(R.string.deleteCompleted);
					mActivity.finishFragment(PostPage.this);
					ApplicationManager.refreshTopPage();
				}
			};

			String url = null;
			
			if(isGethering) {
				url = ZoneConstants.BASE_URL + "boardspot/delete";
			} else {
				url = ZoneConstants.BASE_URL + "spot/delete";
			}
			
			url += "?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
					"&spot_nid=" + post.getSpot_nid();
			
			AsyncStringDownloader.download(url, null, ocl);
		} catch(Exception e) {
			LogUtils.trace(e);
			ToastUtils.showToast(R.string.failToDeletePost);
		}
	}

	@Override
	protected String generateDownloadKey() {
		return "POSTPAGE" + madeCount;
	}

	@Override
	protected int getXmlResId() {
		return R.layout.page_post;
	}

	public int getSpotNid() {
		
		return post.getSpot_nid();
	}
	
	public void setNeedToShowBottom(boolean needToShowBottom) {
		
		isNeedToShowBottom = needToShowBottom;
	}
}