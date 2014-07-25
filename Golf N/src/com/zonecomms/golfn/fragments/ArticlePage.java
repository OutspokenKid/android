package com.zonecomms.golfn.fragments;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.util.Linkify;
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
import com.outspoken_kid.downloader.bitmapdownloader.BitmapDownloader;
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
import com.zonecomms.common.models.Article;
import com.zonecomms.common.models.Member;
import com.zonecomms.common.models.Reply;
import com.zonecomms.common.utils.AppInfoUtils;
import com.zonecomms.common.utils.ImageDownloadUtils;
import com.zonecomms.common.views.ArticleInfoLayout;
import com.zonecomms.common.views.ReplyLoadingView;
import com.zonecomms.common.views.ReplyLoadingView.OnLoadingViewClickedListener;
import com.zonecomms.common.views.ViewForReply;
import com.zonecomms.common.views.ViewForReply.OnShowPopupForReplyListener;
import com.zonecomms.common.views.ViewForReply.OnShowProfilePopupListener;
import com.zonecomms.golfn.MainActivity;
import com.zonecomms.golfn.R;
import com.zonecomms.golfn.YoutubePlayerActivity;
import com.zonecomms.golfn.classes.BaseFragment;
import com.zonecomms.golfn.classes.ZoneConstants;

public class ArticlePage extends BaseFragment {

	private static final int NUM_OF_REPLY = 10;
	private static final String TYPE_TEXT = "1";
	private static final String TYPE_IMAGE = "2";
	private static final String TYPE_VIDEO = "3";
	
	private Article article;

	private ArticleInfoLayout articleInfoLayout;
	private ScrollView scrollView;
	private LinearLayout innerLayout;
	private LinearLayout contentLayout;
	private LinearLayout replyLinear;
	private LinearLayout targetLinear;
	private LinearLayout writeLinear;
	private HoloStyleEditText editText;
	private HoloStyleButton btnSubmit;
	private ReplyLoadingView replyLoadingView;

	private HoloStyleSpinnerPopup spForReply;
	private ViewForReply selectedVFR;

	boolean isNeedToShowBottom;
	private int spot_nid;
	private int lastIndexno;
	
	private ArrayList<Member> targets = new ArrayList<Member>();
	private ArrayList<String> imageUrls = new ArrayList<String>();
	private String[] urls;
	
	@Override
	protected void bindViews() {
		
		scrollView = (ScrollView) mThisView.findViewById(R.id.articlePage_scrollView);
		innerLayout = (LinearLayout) mThisView.findViewById(R.id.articlePage_innerLayout);
		contentLayout = (LinearLayout) mThisView.findViewById(R.id.articlePage_contentLayout);
		
		replyLinear = (LinearLayout) mThisView.findViewById(R.id.articlePage_replyLinear);
		writeLinear = (LinearLayout) mThisView.findViewById(R.id.articlePage_writeLinear);
		editText = (HoloStyleEditText) mThisView.findViewById(R.id.articlePage_editText);
		btnSubmit = (HoloStyleButton) mThisView.findViewById(R.id.articlePage_submitButton);
		targetLinear = (LinearLayout) mThisView.findViewById(R.id.articlePage_targetLinear);
	}

	@Override
	protected void setVariables() {

		if(getArguments() != null) {
			spot_nid = getArguments().getInt("spot_nid");
			isNeedToShowBottom = getArguments().getBoolean("isNeedToShowBottom", false);
		}
		
		editText.getEditText().setHint(R.string.hintForReply);
		editText.getEditText().setHintTextColor(Color.rgb(120, 120, 120));
		editText.getEditText().setTextColor(Color.WHITE);
		editText.getEditText().setSingleLine(false);
		btnSubmit.setTextColor(Color.WHITE);
		btnSubmit.setText(R.string.submitReply);
	}

	@Override
	protected void createPage() {

		try {
			articleInfoLayout = new ArticleInfoLayout(mContext);
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 150, articleInfoLayout, 1, 0, new int[]{8, 8, 8, 8});
			innerLayout.addView(articleInfoLayout, 0);
			
			spForReply = new HoloStyleSpinnerPopup(mContext);
			((FrameLayout) mThisView.findViewById(R.id.articlePage_mainLayout)).addView(spForReply);
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
	}

	@Override
	protected void setSizes() {

		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, contentLayout, 1, Gravity.LEFT, new int[]{8, 0, 8, 0});
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

				LogUtils.log("ArticlePage.downloadInfo.onError.\nurl : " + url);
				setPage(false);
			}
			
			@Override
			public void onCompleted(String url, String result) {
				
				LogUtils.log("ArticlePage.onCompleted.  url : " + url + "\nresult : " + result);
				
				try {
					JSONObject objResult = new JSONObject(result);
					
					article = new Article(objResult.getJSONObject("data"));
					setPage(true);
				} catch(Exception e) {
					LogUtils.trace(e);
					setPage(false);
				}
			}
		};
		
		String url = "";
		
		url += ZoneConstants.BASE_URL + "newSpot/detail" +
				"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
				"&image_size=640" +
				"&spot_nid=" + spot_nid;
		super.downloadInfo();
		AsyncStringDownloader.download(url, getDownloadKey(), ocl);
	}
	
	@Override
	protected void setPage(boolean successDownload) {

		if(successDownload) {
			articleInfoLayout.setArticleInfo(article);
			innerLayout.setVisibility(View.VISIBLE);
			
			if(article != null 
					&& article.getContent() != null
					&& article.getContent().length != 0) {
				removeAllContents();
				
				int size = article.getContent().length;
				for(int i=0; i<size; i++) {
					
					try {
						String type = article.getContent()[i].type;
						
						if(type.equals(TYPE_TEXT)) {
							addText(article.getContent()[i].data);
						} else if(type.equals(TYPE_IMAGE)){
							addImage(article.getContent()[i].data, imageUrls.size());
						} else if(type.equals(TYPE_VIDEO)){
							addVideo(article.getContent()[i].data);
						}
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (Error e) {
						LogUtils.trace(e);
					}
				}
				
				View blank = new View(mContext);
				ResizeUtils.viewResize(1, 10, blank, 1, 0, null);
				contentLayout.addView(blank);
			}
			
			loadReplis();
		} else {
			ToastUtils.showToast(R.string.failToLoadPost);
		}
		
		super.setPage(successDownload);
	}

	@Override
	public String getTitleText() {

		if(title == null) {
			title = "ARTICLE";
		}
		
		return title;
	}

	@Override
	protected int getContentViewId() {

		return R.id.articlePage_mainLayout;
	}

	@Override
	public boolean onBackKeyPressed() {
		
		if(spForReply.getVisibility() == View.VISIBLE) {
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

			if(contentLayout.getChildCount() == 0) {
				onRefreshPage();
			}
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

	@Override
	protected String generateDownloadKey() {
		return "ARTICLEPAGE" + madeCount;
	}

	@Override
	protected int getXmlResId() {
		return R.layout.page_article;
	}
	
////////////////////// Custom methods.
	
	public void removeAllContents() {

		try {
			ArrayList<View> views = new ArrayList<View>();

			int length = contentLayout.getChildCount();
			for(int i=0; i<length; i++) {
				views.add(contentLayout.getChildAt(i));
			}

			length = views.size();
			for(int i=0; i<length; i++) {
				contentLayout.removeView(views.get(i));
			}
			
			length = views.size();
			for(int i=0; i<length; i++) {
				ViewUnbindHelper.unbindReferences(views.get(i));
			}
			
			views.clear();
			views = null;
		} catch(Exception e) {
		}
	}
	
	public void addText(String text) {
		
		if(StringUtils.isEmpty(text)) {
			return;
		}
		
		try {
			TextView textView = new TextView(mContext);
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 
					textView, 1, 0, new int[]{20, 20, 20, 0});
			textView.setTextColor(Color.WHITE);
			textView.setText(text);
			textView.setAutoLinkMask(Linkify.WEB_URLS|Linkify.EMAIL_ADDRESSES);
			FontInfo.setFontSize(textView, 30);
			contentLayout.addView(textView);
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void addImage(String imageUrl, final int index) {
		
		if(StringUtils.isEmpty(imageUrl)) {
			return;
		}
		
		try {
			final FrameLayout frame = new FrameLayout(mContext);
			ResizeUtils.viewResize(480, 360, frame, 1, Gravity.CENTER_HORIZONTAL, new int[]{0, 20, 0, 0});
			frame.setBackgroundColor(Color.LTGRAY);
			contentLayout.addView(frame);

			final ProgressBar progress = new ProgressBar(mContext);
			ResizeUtils.viewResize(50, 50, progress, 2, Gravity.CENTER, null);
			frame.addView(progress);
			
			final ImageView ivImage = new ImageView(mContext);
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, ivImage, 2, 0, null);
			ivImage.setScaleType(ScaleType.FIT_XY);
			ivImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					
					showImageViewer(index);
				}
			});
			frame.addView(ivImage);
			
			BitmapDownloader.OnCompletedListener ocl = new BitmapDownloader.OnCompletedListener() {
				
				@Override
				public void onErrorRaised(String url, Exception e) {

					if(progress != null && progress.getVisibility() == View.VISIBLE) {
						progress.setVisibility(View.INVISIBLE);
					}
				}
				
				@Override
				public void onCompleted(String url, Bitmap bitmap, ImageView view) {
					
					if(progress != null && progress.getVisibility() == View.VISIBLE) {
						progress.setVisibility(View.INVISIBLE);
					}
					
					if(view != null && bitmap != null && !bitmap.isRecycled()) {
						int scaledHeight = (int)(480f * (float)bitmap.getHeight() / (float)bitmap.getWidth());
						ResizeUtils.viewResize(480, scaledHeight, frame, 1, 
								Gravity.CENTER_HORIZONTAL, new int[]{0, 20, 0, 0});
						ivImage.setImageBitmap(bitmap);
					}
				}
			};
			BitmapDownloader.downloadImmediately(imageUrl, getDownloadKey(), ocl, null, ivImage, true);
			
			imageUrls.add(imageUrl);
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void addVideo(final String id) {
		
		if(StringUtils.isEmpty(id)) {
			return;
		}
		
		try {
			FrameLayout frame = new FrameLayout(mContext);
			ResizeUtils.viewResize(480, 360, frame, 1, Gravity.CENTER_HORIZONTAL, new int[]{0, 20, 0, 0});
			frame.setBackgroundColor(Color.LTGRAY);
			frame.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					showLink(id);
				}
			});
			contentLayout.addView(frame);
			
			ImageView ivImage = new ImageView(mContext);
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, ivImage, 2, 0, null);
			ivImage.setScaleType(ScaleType.FIT_XY);
			frame.addView(ivImage);
			
			View play = new View(mContext);
			ResizeUtils.viewResize(100, 100, play, 2, Gravity.CENTER, null);
			play.setBackgroundResource(R.drawable.btn_play);
			frame.addView(play);
			
			String url = "http://img.youtube.com/vi/" + id + "/0.jpg";
			ImageDownloadUtils.downloadImageImmediately(url, getDownloadKey(), ivImage, 480, true);
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
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
				LogUtils.log("loadReplies.onError.\nurl : " + url);
				ToastUtils.showToast(R.string.failToLoadReplies);
				removeLoadingView();
			}
			
			@Override
			public void onCompleted(String url, String result) {
				
				LogUtils.log("loadReplies.onCompleted.\nurl : " + url + "\nresult : " + result);
				
				try {
					JSONArray arJSON = (new JSONObject(result)).getJSONArray("data");
					
					int length = arJSON.length();

					ArrayList<ViewForReply> replyViews = new ArrayList<ViewForReply>();

					if(length != 0) {
						
						for(int i=0; i<length; i++) {
							final Reply reply = new Reply(arJSON.getJSONObject(i));
							reply.setReplyType(Reply.TYPE_ARTICLE);
							ViewForReply vfr = new ViewForReply(mContext, madeCount, article.getSpot_nid(), replyLinear);
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
									ArticlePage.this.showPopupForReply(forOwner, vfr);
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

						if(isNeedToShowBottom) {
							isNeedToShowBottom = false;
							scrollView.postDelayed(new Runnable() {
								
								@Override
								public void run() {
									scrollView.fullScroll(ScrollView.FOCUS_DOWN);
								}
							}, 100);
						}
					} else {
						removeLoadingView();
					}
				} catch(Exception e) {
					LogUtils.trace(e);
				}
			}
		};
		
		String url = ZoneConstants.BASE_URL + "newSpotReply/list" +
				"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
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
			String url = ZoneConstants.BASE_URL + "newSpotReply/write" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
					"&spot_nid=" + article.getSpot_nid() +
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
	
	public void showPopupForReply(boolean forOwner, ViewForReply vfr) {

		if(vfr == null) {
			return;
		} else {
			selectedVFR = vfr;
		}
		
		spForReply.setTitle(getString(R.string.selectPlease));
		
		spForReply.clearItems();
		spForReply.addItem(getString(R.string.reply_copy));
		
		if(forOwner) {
			spForReply.addItem(getString(R.string.reply_delete));
		} else{
			spForReply.addItem(getString(R.string.reply_accuse));
		}
		
		spForReply.notifyDataSetChanged();
		spForReply.showPopup();
	}

	public void showImageViewer(int index) {

		if(urls == null) {
			int size = imageUrls.size();
			urls = new String[size];
			
			for(int i=0; i<size; i++) {
				urls[i] = imageUrls.get(i);
			}
		}
		
		mActivity.showImageViewerActivity(null, urls, null, index);
	}
	
	public void showLink(String videoUrl) {

		if(!StringUtils.isEmpty(videoUrl)) {

			try {
				Intent intent = new Intent(mActivity, YoutubePlayerActivity.class);
				intent.putExtra("videoId", videoUrl);
				mActivity.startActivity(intent);
				
//				Uri uri = Uri.parse("http://www.youtube.com/watch?v=" + videoUrl);
//				Intent i = new Intent(Intent.ACTION_VIEW, uri); 
//				mActivity.startActivity(i);
			} catch (Exception e) {
				ToastUtils.showToast(R.string.failToShowVideo);
				LogUtils.trace(e);
			} catch (Error e) {
				ToastUtils.showToast(R.string.failToShowVideo);
				LogUtils.trace(e);
			}
		} else {
			ToastUtils.showToast(R.string.failToShowVideo);
		}
	}

	public int getSpotNid() {
		
		return article.getSpot_nid();
	}
	
	public void setNeedToShowBottom(boolean isNeedToShowBottom) {
		
		this.isNeedToShowBottom = isNeedToShowBottom;
	}
}