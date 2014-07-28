package com.zonecomms.clubcage.fragments;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.outspoken_kid.classes.ViewUnbindHelper;
import com.outspoken_kid.model.FontInfo;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo_dark.HoloStyleButton;
import com.outspoken_kid.views.holo_dark.HoloStyleEditText;
import com.outspoken_kid.views.holo_dark.HoloStyleSpinnerPopup;
import com.outspoken_kid.views.holo_dark.HoloStyleSpinnerPopup.OnItemClickedListener;
import com.zonecomms.clubcage.MainActivity;
import com.zonecomms.clubcage.R;
import com.zonecomms.clubcage.classes.BaseFragment;
import com.zonecomms.clubcage.classes.ZoneConstants;
import com.zonecomms.clubcage.classes.ZonecommsApplication;
import com.zonecomms.common.models.Member;
import com.zonecomms.common.models.Post;
import com.zonecomms.common.models.Reply;
import com.zonecomms.common.utils.AppInfoUtils;
import com.zonecomms.common.views.PostInfoLayout;
import com.zonecomms.common.views.ReplyLoadingView;
import com.zonecomms.common.views.ReplyLoadingView.OnLoadingViewClickedListener;

public class PostPage extends BaseFragment {

	private static int NUM_OF_REPLY = 10;
	
	private Post post;

	private PostInfoLayout postInfoLayout;
	private TextView tvText;
	private ScrollView scrollView;
	private LinearLayout innerLayout;
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
	
	private int spot_nid;
	private int lastIndexno;
	private boolean isNeedToShowBottom;
	
	private ArrayList<Member> targets = new ArrayList<Member>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if(container == null) {
			return null;
		}

		mThisView = inflater.inflate(R.layout.page_post, null);
		return mThisView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		bindViews();
		setVariables();
		createPage();
		setListeners();
		setSizes();
	}
	
	@Override
	protected void bindViews() {
		
		tvText = (TextView) mThisView.findViewById(R.id.postPage_tvText);
		
		scrollView = (ScrollView) mThisView.findViewById(R.id.postPage_scrollView);
		innerLayout = (LinearLayout) mThisView.findViewById(R.id.postPage_innerLayout);
		contentLayout = (LinearLayout) mThisView.findViewById(R.id.postPage_contentLayout);
		
		replyLinear = (LinearLayout) mThisView.findViewById(R.id.postPage_replyLinear);
		writeLinear = (LinearLayout) mThisView.findViewById(R.id.postPage_writeLinear);
		editText = (HoloStyleEditText) mThisView.findViewById(R.id.postPage_editText);
		btnSubmit = (HoloStyleButton) mThisView.findViewById(R.id.postPage_submitButton);
		targetLinear = (LinearLayout) mThisView.findViewById(R.id.postPage_targetLinear);
	}

	@Override
	protected void setVariables() {

		setDownloadKey("POSTPAGE" + madeCount);
		
		if(getArguments() != null) {
			spot_nid = getArguments().getInt("spot_nid");
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
			postInfoLayout = new PostInfoLayout(mContext);
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 150, postInfoLayout, 1, 0, new int[]{8, 8, 8, 8});
			innerLayout.addView(postInfoLayout, 0);
			
			spForPost = new HoloStyleSpinnerPopup(mContext);
			((FrameLayout) mThisView.findViewById(R.id.postPage_mainLayout)).addView(spForPost);
			
			spForReply = new HoloStyleSpinnerPopup(mContext);
			((FrameLayout) mThisView.findViewById(R.id.postPage_mainLayout)).addView(spForReply);
		} catch(Exception e) {
			e.printStackTrace();
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
				
				LogUtils.log("#####\nisAdmin : " + MainActivity.myInfo.isAdmin() + 
						"\nid : " + MainActivity.myInfo.getMember_id() +
						"\npost.id : " + post.getMember().getMember_id() +
						"\n#####");
				
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
		
		String url = ZoneConstants.BASE_URL + "spot/detail" +
				"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
				"&image_size=" + ResizeUtils.getSpecificLength(640) +
				"&spot_nid=" + spot_nid;
		super.downloadInfo();
		DownloadUtils.downloadString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {
				
				setPage(false);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("PostPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					post = new Post(objJSON.getJSONObject("data"));
					setPage(true);
				} catch (Exception e) {
					LogUtils.trace(e);
					setPage(false);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					setPage(false);
				}
			}
		});
	}
	
	@Override
	protected void setPage(boolean successDownload) {

		if(successDownload) {
			boardIndex = post.getBoard_nid();
			postInfoLayout.setPostInfo(post);

			if(!StringUtils.isEmpty(post.getContent())) {
				tvText.setText(post.getContent());
			}
			
			addScreenshots();

			innerLayout.setVisibility(View.VISIBLE);
			loadReplis();
		} else {
			ToastUtils.showToast(R.string.failToLoadPost);
		}
		
		super.setPage(successDownload);
	}

	@Override
	protected String getTitleText() {

		return getString(R.string.write);
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
			mActivity.getTitleBar().showHomeButton();
			mActivity.getTitleBar().hideWriteButton();
			
			if(mActivity.getSponserBanner() != null) {
				mActivity.getSponserBanner().hideBanner();
			}
			
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

	@Override
	public void finish(boolean needAnim, boolean isBeforeMain) {
		
		SoftKeyboardUtils.hideKeyboard(mContext, editText);
		super.finish(needAnim, isBeforeMain);
	}
	
////////////////////// Custom methods.
	
	public void addScreenshots() {
		
		if(post == null || post.getMedias() == null || post.getMedias().length == 0) {
			tvText.setMinHeight(ResizeUtils.getSpecificLength(466));
			return;
		} else {
			tvText.setMinHeight(0);
		}
		
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
			
			String url = post.getMedias()[i].getMedia_src();
			image.setTag(url);
			DownloadUtils.downloadBitmap(url, image,
					new OnBitmapDownloadListener() {

						@Override
						public void onError(String url, ImageView ivImage) {
							// TODO Auto-generated method stub		
						}

						@Override
						public void onCompleted(String url, ImageView ivImage,
								Bitmap bitmap) {

							try {
								LogUtils.log("PostPage.onCompleted." + "\nurl : "
										+ url);

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
			e.printStackTrace();
		}
	}
	
	public void removeLoadingView() {
		
		try {
			if(replyLoadingView != null) {
				innerLayout.removeView(replyLoadingView);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadReplis() {
		
		if(replyLoadingView != null) {
			replyLoadingView.setMode(ReplyLoadingView.MODE_LOADING);
		}
		
		String url = ZoneConstants.BASE_URL + "reply/list" +
				"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
				"&spot_nid=" + spot_nid +
				"&image_size=" + ResizeUtils.getSpecificLength(100) +
				"&last_reply_nid=" + lastIndexno;
		DownloadUtils.downloadString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {
				
				ToastUtils.showToast(R.string.failToLoadReplies);
				removeLoadingView();
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("PostPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					JSONArray arJSON = objJSON.getJSONArray("data");
					
					int length = arJSON.length();

					ArrayList<ViewForReply> replyViews = new ArrayList<ViewForReply>();

					if(length != 0) {
						
						for(int i=0; i<length; i++) {
							final Reply reply = new Reply(arJSON.getJSONObject(i));
							ViewForReply vfr = new ViewForReply(mContext);
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
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void writeReply(String text) {

		mActivity.showLoadingView();
		mActivity.showCover();
		
		try {
			String url = ZoneConstants.BASE_URL + "reply/write" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
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

			DownloadUtils.downloadString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {
					
					ToastUtils.showToast(R.string.failToSendReply);
					mActivity.hideLoadingView();
					mActivity.hideCover();
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("PostPage.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						mActivity.hideLoadingView();
						mActivity.hideCover();
						
						if(objJSON.getInt("errorCode") == 1) {
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
					} catch (Exception e) {
						LogUtils.trace(e);
						ToastUtils.showToast(R.string.failToSendReply);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
						ToastUtils.showToast(R.string.failToSendReply);
					}
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
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
			String url = ZoneConstants.BASE_URL + "spot/badSpot" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
					"&spot_nid=" + post.getSpot_nid();

			DownloadUtils.downloadString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {
					
					ToastUtils.showToast(R.string.failToAccuse);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("PostPage.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

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
					} catch (Exception e) {
						LogUtils.trace(e);
						ToastUtils.showToast(R.string.failToAccuse);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
						ToastUtils.showToast(R.string.failToAccuse);
					}
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
			ToastUtils.showToast(R.string.failToAccuse);
		}
	}
	
	public void scrap() {
		
		try {
			String url = ZoneConstants.BASE_URL + "spot/scrap" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
					"&spot_nid=" + post.getSpot_nid();

			DownloadUtils.downloadString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {
					
					ToastUtils.showToast(R.string.failToScrapPost);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("PostPage.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

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
					} catch (Exception e) {
						LogUtils.trace(e);
						ToastUtils.showToast(R.string.failToScrapPost);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
						ToastUtils.showToast(R.string.failToScrapPost);
					}
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
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

		ZonecommsApplication.getActivity().
				showWriteActivity(post.getSpot_nid(), post.getContent(), imageUrls, post.getMember().getMember_id());
	}
	
	public void delete() {
		
		try {
			String url = ZoneConstants.BASE_URL + "spot/delete" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
					"&spot_nid=" + post.getSpot_nid();

			DownloadUtils.downloadString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {
					
					ToastUtils.showToast(R.string.failToDeletePost);
					LogUtils.log("PostPage.delete.onError.\nurl : " + url);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("PostPage.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						ToastUtils.showToast(R.string.deleteCompleted);
						ZonecommsApplication.getActivity().closeTopPage();
						ZonecommsApplication.getTopFragment().onRefreshPage();
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
					}
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
			ToastUtils.showToast(R.string.failToDeletePost);
		}
	}
	
//////////////////////Classes.
	
	private class ViewForReply extends RelativeLayout {

		private Reply reply;
		
		private ImageView ivImage;
		private TextView tvNickname;
		private TextView tvRegdate;
		private TextView tvText;
		private LinearLayout targetLinear;
		
		public ViewForReply(Context context) {
			super(context);
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
				
				ivImage.setTag(member.getMedia_src());
				DownloadUtils.downloadBitmap(member.getMedia_src(), ivImage,
						new OnBitmapDownloadListener() {

							@Override
							public void onError(String url, ImageView ivImage) {
								// TODO Auto-generated method stub		
							}

							@Override
							public void onCompleted(String url,
									ImageView ivImage, Bitmap bitmap) {

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
						mActivity.showProfilePopup(member.getMember_id(), member.getStatus());
					}
				});
				
				this.setOnLongClickListener(new OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View v) {
						
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
							return true;
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
				String url = ZoneConstants.BASE_URL + "reply/delete" +
						"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
						"&reply_nid=" + reply.getReply_nid() +
						"&spot_nid=" + post.getSpot_nid();
				
				DownloadUtils.downloadString(url, new OnJSONDownloadListener() {

					@Override
					public void onError(String url) {
						
						ToastUtils.showToast(R.string.failToDeleteReply);
					}

					@Override
					public void onCompleted(String url, JSONObject objJSON) {

						try {
							LogUtils.log("PostPage.onCompleted." + "\nurl : " + url
									+ "\nresult : " + objJSON);

							int errorCode = objJSON.getInt("errorCode");
							
							if(errorCode == 1) {
								removeFromParent();
								ToastUtils.showToast(R.string.deleteCompleted);
							} else {
								ToastUtils.showToast(R.string.failToDeleteReply);
							}
						} catch (Exception e) {
							LogUtils.trace(e);
							ToastUtils.showToast(R.string.failToDeleteReply);
						} catch (OutOfMemoryError oom) {
							LogUtils.trace(oom);
							ToastUtils.showToast(R.string.failToDeleteReply);
						}
					}
				});
			} catch(Exception e) {
				e.printStackTrace();
				ToastUtils.showToast(R.string.failToDeleteReply);
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

				DownloadUtils.downloadString(url, new OnJSONDownloadListener() {

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
				e.printStackTrace();
				ToastUtils.showToast(R.string.failToAccuseReply);
			}
		}
		
		public void copy() {

			if(reply != null && !StringUtils.isEmpty(reply.getContent())) {
				if(StringUtils.copyStringToClipboard(mContext, reply.getContent())) {
					ToastUtils.showToast(R.string.copyReplyCompleted);
				} else {
					ToastUtils.showToast(R.string.failToCopyReply);
				}
			}
		}
		
		public void removeFromParent() {

			replyLinear.removeView(this);
		}
	
		public void setTargetViews() {

			targetLinear.removeAllViews();
			
			if(reply.getTarget_member().size() == 0) {
				return;
			}
			
			int linearSize = (reply.getTarget_member().size() + 2) / 3;
			
			for(int i=0; i<linearSize; i++) {
				LinearLayout linear = new LinearLayout(mContext);
				linear.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
				linear.setOrientation(LinearLayout.HORIZONTAL);
				targetLinear.addView(linear);
				
				int textSize = i == linearSize - 1 ? reply.getTarget_member().size() - i*3 : 3;
				
				for(int j=0; j<textSize; j++) {
					int index = i*3 + j;
					
					final Member MEMBER = reply.getTarget_member().get(index);
							
					
					TextView tv = new TextView(mContext);
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
							
							mActivity.showProfilePopup(MEMBER.getMember_id(), MEMBER.getStatus());
						}
					});
					FontInfo.setFontSize(tv, 20);
					linear.addView(tv);
				}
			}
		}
	}
}