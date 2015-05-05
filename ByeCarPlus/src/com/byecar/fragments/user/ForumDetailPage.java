package com.byecar.fragments.user;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.byecar.byecarplus.MainActivity;
import com.byecar.byecarplus.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPDownloadUtils;
import com.byecar.classes.BCPFragment;
import com.byecar.models.Post;
import com.byecar.models.Review;
import com.byecar.views.ForumReplyView;
import com.byecar.views.ForumView;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class ForumDetailPage extends BCPFragment {

	private Post post;
	private int post_id;
	
	private ScrollView scrollView;
	private Button btnLike;
	private Button btnAlert;
	private Button btnDelete;
	private Button btnEdit;
	private ForumView forumView;
	private LinearLayout contentsLinear;
	private TextView tvReplyTitle;
	private LinearLayout replyLinear;
	private EditText etReply;
	private Button btnReply;
	
	private boolean needToScrollDown;
	
	ArrayList<Review> reviews = new ArrayList<Review>();
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.forumDetailPage_titleBar);
		
		scrollView = (ScrollView) mThisView.findViewById(R.id.forumDetailPage_scrollView);
		btnLike = (Button) mThisView.findViewById(R.id.forumDetailPage_btnLike);
		btnAlert = (Button) mThisView.findViewById(R.id.forumDetailPage_btnAlert);
		btnDelete = (Button) mThisView.findViewById(R.id.forumDetailPage_btnDelete);
		btnEdit = (Button) mThisView.findViewById(R.id.forumDetailPage_btnEdit);
		forumView = (ForumView) mThisView.findViewById(R.id.forumDetailPage_forumView);
		contentsLinear = (LinearLayout) mThisView.findViewById(R.id.forumDetailPage_contentsLinear);
		tvReplyTitle = (TextView) mThisView.findViewById(R.id.forumDetailPage_tvReplyTitle);
		replyLinear = (LinearLayout) mThisView.findViewById(R.id.forumDetailPage_replyLinear);
		etReply = (EditText) mThisView.findViewById(R.id.forumDetailPage_etReply);
		btnReply = (Button) mThisView.findViewById(R.id.forumDetailPage_btnReply);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {
			
			if(getArguments().containsKey("post")) {
				this.post = (Post) getArguments().getSerializable("post");
			} else if(getArguments().containsKey("post_id")) {
				this.post_id = getArguments().getInt("post_id");
			} else {
				closePage();
			}
		}
	}

	@Override
	public void createPage() {

		contentsLinear.removeAllViews();
		downloadPost();
	}

	@Override
	public void setListeners() {

		btnDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(post == null) {
					return;
				}
				
				mActivity.showAlertDialog(R.string.delete, R.string.wannaDelete, 
						R.string.confirm, R.string.cancel, 
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {

								//http://byecar1.minsangk.com/posts/forum/delete.json?post_id=1
								String url = BCPAPIs.FORUM_DELETE_URL + "?post_id=" + post.getId();
								DownloadUtils.downloadJSONString(url,
										new OnJSONDownloadListener() {

											@Override
											public void onError(String url) {

												LogUtils.log("ForumDetailPage.onError." + "\nurl : " + url);

											}

											@Override
											public void onCompleted(String url,
													JSONObject objJSON) {

												try {
													LogUtils.log("ForumDetailPage.onCompleted."
															+ "\nurl : " + url + "\nresult : "
															+ objJSON);

													if(objJSON.getInt("result") == 1) {
														refreshMainCover();
													} else {
														ToastUtils.showToast(objJSON.getString("message"));
													}
												} catch (Exception e) {
													LogUtils.trace(e);
												} catch (OutOfMemoryError oom) {
													LogUtils.trace(oom);
												}
											}
										});
							}
						}, null);
			}
		});
		
		btnEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(post == null) {
					return;
				}
				
				Bundle bundle = new Bundle();
				bundle.putSerializable("post", post);
				mActivity.showPage(BCPConstants.PAGE_WRITE_FORUM, bundle);
			}
		});
		
		btnLike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(post == null) {
					return;
				}
				
				String url = null;
				
				if(post.getIs_liked() == 0) {
					url = BCPAPIs.FORUM_LIKE_URL;
				} else {
					url = BCPAPIs.FORUM_UNLIKE_URL;
				}
				
				url += "?post_id=" + post.getId();
				DownloadUtils.downloadJSONString(url,
						new OnJSONDownloadListener() {

							@Override
							public void onError(String url) {

								LogUtils.log("ForumDetailPage.onError." + "\nurl : " + url);

							}

							@Override
							public void onCompleted(String url,
									JSONObject objJSON) {

								try {
									LogUtils.log("ForumDetailPage.onCompleted."
											+ "\nurl : " + url + "\nresult : "
											+ objJSON);

									if(objJSON.getInt("result") == 1) {
										post.setIs_liked((post.getIs_liked() + 1)%2); 
									} else {
										ToastUtils.showToast(objJSON.getString("message"));
									}
								} catch (Exception e) {
									LogUtils.trace(e);
								} catch (OutOfMemoryError oom) {
									LogUtils.trace(oom);
								}
							}
						});
			}
		});
	
		btnAlert.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(post == null) {
					return;
				}
				
				String url = BCPAPIs.FORUM_REPORT_URL
						+ post.getId();
				DownloadUtils.downloadJSONString(url,
						new OnJSONDownloadListener() {

							@Override
							public void onError(String url) {

								LogUtils.log("ForumDetailPage.onError." + "\nurl : " + url);
								ToastUtils.showToast(R.string.failToReportPost);
							}

							@Override
							public void onCompleted(String url,
									JSONObject objJSON) {

								try {
									LogUtils.log("ForumDetailPage.onCompleted."
											+ "\nurl : " + url + "\nresult : "
											+ objJSON);

									if(objJSON.getInt("result") == 1) {
										ToastUtils.showToast(R.string.complete_report);
									} else {
										ToastUtils.showToast(objJSON.getString("message"));
									}
								} catch (Exception e) {
									LogUtils.trace(e);
								} catch (OutOfMemoryError oom) {
									LogUtils.trace(oom);
								}
							}
						});
			}
		});

		btnReply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				writeReply();
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;

		//btnLike.
		rp = (RelativeLayout.LayoutParams) btnLike.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(60);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.topMargin = ResizeUtils.getSpecificLength(16);
		rp.rightMargin = ResizeUtils.getSpecificLength(14);
		
		//btnAlert.
		rp = (RelativeLayout.LayoutParams) btnAlert.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(60);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.rightMargin = ResizeUtils.getSpecificLength(8);
		
		//btnDelete.
		rp = (RelativeLayout.LayoutParams) btnDelete.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(60);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.topMargin = ResizeUtils.getSpecificLength(16);
		rp.rightMargin = ResizeUtils.getSpecificLength(14);
		
		//btnEdit.
		rp = (RelativeLayout.LayoutParams) btnEdit.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(60);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.rightMargin = ResizeUtils.getSpecificLength(8);
		
		//bottomBg.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.forumDetailPage_bottomBg).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(88);
		
		//etReply.
		rp = (RelativeLayout.LayoutParams) etReply.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(477);
		rp.height = ResizeUtils.getSpecificLength(59);
		rp.leftMargin = ResizeUtils.getSpecificLength(13);
		rp.bottomMargin = ResizeUtils.getSpecificLength(13);
		etReply.setPadding(ResizeUtils.getSpecificLength(14), 0, ResizeUtils.getSpecificLength(14), 0);
		
		//btnReply.
		rp = (RelativeLayout.LayoutParams) btnReply.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(123);
		rp.height = ResizeUtils.getSpecificLength(57);
		rp.rightMargin = ResizeUtils.getSpecificLength(14);
		rp.bottomMargin = ResizeUtils.getSpecificLength(14);

		ResizeUtils.viewResize(578, 135, forumView, 1, Gravity.CENTER_HORIZONTAL, new int[]{0, 25, 0, 0});
		ResizeUtils.viewResize(578, 10, mThisView.findViewById(R.id.forumDetailPage_contentHeader), 
				1, Gravity.CENTER_HORIZONTAL, new int[]{0, 20, 0, 0});
		ResizeUtils.viewResize(578, LayoutParams.WRAP_CONTENT, contentsLinear, 1, 
				Gravity.CENTER_HORIZONTAL, null, new int[]{15, 15, 15, 15});
		ResizeUtils.viewResize(578, 10, mThisView.findViewById(R.id.forumDetailPage_contentFooter), 
				1, Gravity.CENTER_HORIZONTAL, new int[]{0, 0, 0, 20});
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 41, tvReplyTitle, 1, 0, 
				new int[]{0, 0, 0, 32}, new int[]{ResizeUtils.getSpecificLength(20), 0, 0, 0});
		
		FontUtils.setFontSize(etReply, 24);
		FontUtils.setFontSize(tvReplyTitle, 24);
		FontUtils.setFontStyle(tvReplyTitle, FontUtils.BOLD);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_forum_detail;
	}

	@Override
	public int getPageTitleTextResId() {

		return R.string.pageTitle_forum;
	}

	@Override
	public int getRootViewResId() {
		
		return R.id.forumDetailPage_mainLayout;
	}
	
	@Override
	public boolean parseJSON(JSONObject objJSON) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onResume() {
		super.onResume();
		
		setPost();
	}
	
	@Override
	public void refreshPage() {
		
		downloadPost();
	}
	
//////////////////// Custom methods.

	public void closePage() {
		
		ToastUtils.showToast(R.string.failToLoadPostInfo);
		
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				
				if(mActivity != null) {
					mActivity.closeTopPage();
				}
			}
		}, 1000);
	}
	
	public void downloadPost() {
		
		String url = BCPAPIs.FORUM_DETAIL_URL;
		
		if(post != null) {
			url += "?post_id=" + post.getId();
		} else {
			url += "?post_id=" + post_id;
		}
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("ForumDetailPage.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("ForumDetailPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					Post newPost = new Post(objJSON.getJSONObject("post"));
					
					if(post != null) {
						post.copyValuesFromNewItem(newPost);
					} else {
						post = newPost;
					}
					
					setPost();
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void setPost() {
		
		if(post == null) {
			return;
		}
		
		//Buttons.
		if(post.getAuthor_id() == MainActivity.user.getId()) {
			btnAlert.setVisibility(View.INVISIBLE);
			btnLike.setVisibility(View.INVISIBLE);
			btnDelete.setVisibility(View.VISIBLE);
			btnEdit.setVisibility(View.VISIBLE);
		} else {
			btnAlert.setVisibility(View.VISIBLE);
			btnLike.setVisibility(View.VISIBLE);
			btnDelete.setVisibility(View.INVISIBLE);
			btnEdit.setVisibility(View.INVISIBLE);
		}
		
		//ForumView.
		forumView.setForum(post, -1);
		
		contentsLinear.removeAllViews();
		
		//Images.
		if(post.getImages() != null && post.getImages().length > 0) {
			int size = post.getImages().length;
			for(int i=0; i<size; i++) {
				addImageView(post.getImages()[i]);
			}
		}
		
		//Text.
		addTextView();
		
		//Review.
		setReviews();
		
		if(needToScrollDown) {
			needToScrollDown = false;
			mThisView.postDelayed(new Runnable() {

				@Override
				public void run() {

					scrollView.fullScroll(ScrollView.FOCUS_DOWN);
				}
			}, 500);
		}
	}
	
	public void addImageView(final String imageUrl) {
		
		final ImageView imageView = new ImageView(mContext);
		ResizeUtils.viewResize(548, 548, imageView, 1, Gravity.CENTER_HORIZONTAL, new int[]{0, 0, 0, 16});
		imageView.setBackgroundColor(Color.argb(100, 255, 0, 0));
		imageView.setScaleType(ScaleType.CENTER_CROP);
		contentsLinear.addView(imageView);

		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				mActivity.showImageViewer(0, null, new String[]{imageUrl}, null);
			}
		});
		imageView.setTag(imageUrl);
		BCPDownloadUtils.downloadBitmap(imageUrl, new OnBitmapDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("ForumDetailPage.onError." + "\nurl : " + url);
			}

			@Override
			public void onCompleted(String url, Bitmap bitmap) {

				try {
					LogUtils.log("ForumDetailPage.onCompleted." + "\nurl : " + url);

					if(imageView != null && !bitmap.isRecycled()) {
						imageView.setImageBitmap(bitmap);
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		}, 548);
	}
	
	public void addTextView() {
		
		if(post == null) {
			return;
		}
		
		TextView textView = new TextView(mContext);
		ResizeUtils.viewResize(548, LayoutParams.WRAP_CONTENT, textView, 1, Gravity.CENTER_HORIZONTAL, new int[]{0, 16, 0, 16});
		FontUtils.setFontSize(textView, 24);
		textView.setTextColor(getResources().getColor(R.color.holo_text));
		contentsLinear.addView(textView);
		
		textView.setText(post.getContent());
	}
	
	public void setReviews() {

		replyLinear.removeAllViews();
		
		//댓글이 없을 때.
		if(post.getReplies() == null || post.getReplies().length == 0) {
			View noReview = new View(mContext);
			ResizeUtils.viewResize(223, 226, noReview, 1, Gravity.CENTER_HORIZONTAL, new int[]{0, 16, 0, 16});
			noReview.setBackgroundResource(R.drawable.dealer_no_comment);
			replyLinear.addView(noReview);
			
			return;
		}
		
		int size = post.getReplies().length;
		for(int i=0; i<size; i++) {
			ForumReplyView frv = new ForumReplyView(mContext);
			ResizeUtils.viewResize(574, LayoutParams.WRAP_CONTENT, frv, 1, Gravity.CENTER_HORIZONTAL, new int[]{0, 0, 0, 16});
			frv.setPost(post.getReplies()[i], mActivity, post.getAuthor_nickname(), post.getId());
			replyLinear.addView(frv);
		}
	}

	public void refreshMainCover() {

		String coverUrl = BCPAPIs.MAIN_COVER_URL;
		DownloadUtils.downloadJSONString(coverUrl, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("ForumDetailPage.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("ForumDetailPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);
					
					try {
						MainPage.forums.clear();
						JSONArray arJSON = objJSON.getJSONArray("forum_best");
						int size = arJSON.length();
						for(int i=0; i<size; i++) {
							MainPage.forums.add(new Post(arJSON.getJSONObject(i)));
						}
						
						mActivity.closePageWithRefreshPreviousPage();
					} catch (Exception e) {
						LogUtils.trace(e);
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}

	public void writeReply() {
		
		if(etReply.length() == 0) {
			ToastUtils.showToast(R.string.checkReplyContent);
			return;
		}
		
		//http://byecar1.minsangk.com/posts/reply/save.json?reply[post_id]=1&reply[content]=%EB%8C%93%EA%B8%80%EB%82%B4%EC%9A%A9
		String url = BCPAPIs.FORUM_REPLY_WRITE_URL 
				+ "?reply[post_id]=" + post.getId()
				+ "&reply[content]=" + StringUtils.getUrlEncodedString(etReply);
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("ForumDetailPage.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToWriteReply);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("ForumDetailPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_writeReply);

						etReply.setText(null);
						
						SoftKeyboardUtils.hideKeyboard(mContext, etReply);
						needToScrollDown = true;
						downloadPost();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToWriteReply);
				}
			}
		});
	}
}
