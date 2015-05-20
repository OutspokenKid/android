package com.byecar.views;

import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.ImageViewer;
import com.byecar.byecarplus.MainActivity;
import com.byecar.byecarplus.R;
import com.byecar.byecarplus.R.color;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPDownloadUtils;
import com.byecar.classes.BCPFragmentActivity;
import com.byecar.fragments.user.ForumDetailPage;
import com.byecar.models.Post;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class ForumReplyView extends RelativeLayout {

	private View bgTop;
	private TextView tvContent;
	private TextView tvRegdate;
	
	private ImageView ivImage;
	private View cover;
	private TextView tvNickname;
	
	private View replyBadge;
	private TextView tvReplyText;
	
	private Button btnReply;
	private Button btnEdit;
	private Button btnDelete; 
	
	public ForumReplyView(Context context) {
		this(context, null);
	}
	
	public ForumReplyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public void init() {
		
		//bg_top.
		bgTop = new View(getContext());
		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, 30, bgTop, null, null, null);
		this.addView(bgTop);

		//tvContent.
		tvContent = new TextView(getContext());
		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 
				tvContent, null, null, new int[]{0, 30, 0, 0}, null);
		tvContent.setMinHeight(ResizeUtils.getSpecificLength(113));
		tvContent.setId(R.id.forumReplyView_tvContent);
		tvContent.setTextColor(color.new_color_text_darkgray);
		FontUtils.setFontSize(tvContent, 22);
		this.addView(tvContent);
		
		//tvRegdate.
		tvRegdate = new TextView(getContext());
		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, 68, tvRegdate, 
				new int[]{RelativeLayout.BELOW}, new int[]{R.id.forumReplyView_tvContent}, 
				null, new int[]{156, 4, 0, 0});
		tvRegdate.setId(R.id.forumReplyView_tvRegdate);
		tvRegdate.setTextColor(color.new_color_text_gray);
		tvRegdate.setGravity(Gravity.CENTER_VERTICAL);
		FontUtils.setFontSize(tvRegdate, 16);
		this.addView(tvRegdate);

		//ivImage.
		ivImage = new ImageView(getContext());
		ResizeUtils.viewResizeForRelative(100, 100, ivImage, 
				new int[]{RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP}, 
				new int[]{0, 0}, 
				new int[]{28, 37, 0, 0});
		ivImage.setId(R.id.forumReplyView_ivImage);
		ivImage.setScaleType(ScaleType.CENTER_CROP);
		ivImage.setBackgroundResource(R.drawable.menu_default);
		this.addView(ivImage);
		
		//cover.
		cover = new View(getContext());
		ResizeUtils.viewResizeForRelative(100, 100, cover, 
				new int[]{RelativeLayout.ALIGN_LEFT, RelativeLayout.ALIGN_TOP}, 
				new int[]{R.id.forumReplyView_ivImage, R.id.forumReplyView_ivImage}, 
				null);
		cover.setBackgroundResource(R.drawable.dealer_frame);
		this.addView(cover);

		//tvNickname.
		tvNickname = new TextView(getContext());
		ResizeUtils.viewResizeForRelative(135, LayoutParams.WRAP_CONTENT, tvNickname, 
				new int[]{RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.BELOW}, 
				new int[]{R.id.forumReplyView_ivImage, R.id.forumReplyView_ivImage}, 
				new int[]{10, 4, 0, 0});
		tvNickname.setId(R.id.forumReplyView_tvNickname);
		tvNickname.setEllipsize(TruncateAt.END);
		tvNickname.setTextColor(color.new_color_text_darkgray);
		tvNickname.setGravity(Gravity.CENTER);
		FontUtils.setFontSize(tvNickname, 22);
		FontUtils.setFontStyle(tvNickname, FontUtils.BOLD);
		this.addView(tvNickname);

		//replyBadge.
		replyBadge = new View(getContext());
		ResizeUtils.viewResizeForRelative(17, 16, replyBadge, 
				new int[]{RelativeLayout.ALIGN_TOP}, new int[]{R.id.forumReplyView_tvContent}, 
				new int[]{175, 15, 0, 0});
		replyBadge.setId(R.id.forumReplyView_replyBadge);
		replyBadge.setBackgroundResource(R.drawable.dealer_post_reply);
		replyBadge.setVisibility(View.INVISIBLE);
		this.addView(replyBadge);
		
		//tvReplyText.
		tvReplyText = new TextView(getContext());
		ResizeUtils.viewResizeForRelative(280, LayoutParams.WRAP_CONTENT, tvReplyText, 
				new int[]{RelativeLayout.ALIGN_LEFT, RelativeLayout.ALIGN_TOP}, 
				new int[]{R.id.forumReplyView_replyBadge, R.id.forumReplyView_tvContent}, 
				null);
		tvReplyText.setId(R.id.forumReplyView_tvReplyText);
		tvReplyText.setSingleLine();
		tvReplyText.setEllipsize(TruncateAt.END);
		tvReplyText.setGravity(Gravity.CENTER_VERTICAL);
		tvReplyText.setTextColor(color.holo_text);
		FontUtils.setFontSize(tvReplyText, 26);
		FontUtils.setFontStyle(tvReplyText, FontUtils.BOLD);
		this.addView(tvReplyText);
		
		//btnReply.
		btnReply = new Button(getContext());
		ResizeUtils.viewResizeForRelative(83, 32, btnReply, 
				new int[]{RelativeLayout.ALIGN_RIGHT, RelativeLayout.ALIGN_BOTTOM}, 
				new int[]{R.id.forumReplyView_tvRegdate, R.id.forumReplyView_tvRegdate},
				new int[]{0, 0, 18, 16});
		btnReply.setBackgroundResource(R.drawable.forum_reply_btn);
		btnReply.setVisibility(View.INVISIBLE);
		this.addView(btnReply);
		
		//btnEdit.
		btnEdit = new Button(getContext());
		ResizeUtils.viewResizeForRelative(83, 32, btnEdit, 
				new int[]{RelativeLayout.ALIGN_RIGHT, RelativeLayout.ALIGN_BOTTOM}, 
				new int[]{R.id.forumReplyView_tvRegdate, R.id.forumReplyView_tvRegdate},
				new int[]{0, 0, 18, 16});
		btnEdit.setBackgroundResource(R.drawable.forum_modify_btn);
		btnEdit.setVisibility(View.INVISIBLE);
		this.addView(btnEdit);
		
		//btnDelete.
		btnDelete = new Button(getContext());
		ResizeUtils.viewResizeForRelative(83, 32, btnDelete, 
				new int[]{RelativeLayout.ALIGN_RIGHT, RelativeLayout.ALIGN_BOTTOM}, 
				new int[]{R.id.forumReplyView_tvRegdate, R.id.forumReplyView_tvRegdate},
				new int[]{0, 0, 18, 16});
		btnDelete.setBackgroundResource(R.drawable.forum_delete_btn);
		btnDelete.setVisibility(View.INVISIBLE);
		this.addView(btnDelete);
	}
	
	public void setPost(final Post post, final BCPFragmentActivity activity, 
			final String to, final int origin_post_id) {
		
		try {
			bgTop.setBackgroundResource(R.drawable.dealer_post_frame_head);
			tvContent.setBackgroundResource(R.drawable.dealer_post_frame_body);
			tvRegdate.setBackgroundResource(R.drawable.dealer_post_frame_foot);
			cover.setBackgroundResource(R.drawable.dealer_post_pic_frame);
			tvContent.setPadding(ResizeUtils.getSpecificLength(175), 0, 
					ResizeUtils.getSpecificLength(34), 0);

			btnEdit.setVisibility(View.INVISIBLE);
			btnDelete.setVisibility(View.INVISIBLE);
			btnReply.setVisibility(View.INVISIBLE);
			
			//내가 쓴 글이면 수정 버튼.
			if(post.getAuthor_id() == MainActivity.user.getId()) {
				btnEdit.setVisibility(View.VISIBLE);
				btnEdit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {

						Bundle bundle = new Bundle();
						bundle.putSerializable("post", post);
						bundle.putString("to", to);
						bundle.putInt("post_id", origin_post_id);		//자유게시판 글 id.
						activity.showPage(BCPConstants.PAGE_FORUM_WRITE_REPLY, bundle);
					}
				});
				
				//대댓글이 아직 안달린 경우에만 삭제 버튼 노출.
				if(post.getHas_children() == 0) {
					ResizeUtils.setMargin(btnEdit, new int[]{0, 0, 111, 16});
					btnDelete.setVisibility(View.VISIBLE);
					btnDelete.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View view) {

							deleteReply(post.getId(), activity);
						}
					});
				}
				
			//남이 쓴 댓글이고 대댓글이 없으면 댓글 버튼.
			} else if(post.getHas_children() == 0){
				btnReply.setVisibility(View.VISIBLE);
				btnReply.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {

						Bundle bundle = new Bundle();
						bundle.putString("to", post.getAuthor_nickname());
						bundle.putInt("post_id", origin_post_id);		//자유게시판 글 id.
						bundle.putInt("parent_id", post.getId());	//대댓글인 경우 댓글의 id.
						activity.showPage(BCPConstants.PAGE_FORUM_WRITE_REPLY, bundle);
					}
				});
			}
			
			if(!StringUtils.isEmpty(post.getAuthor_profile_img_url())) {
				ivImage.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {

						Intent intent = new Intent(getContext(), ImageViewer.class);
						intent.putExtra("title", getContext().getResources().getString(R.string.profileImage));
						intent.putExtra("imageUrls", new String[]{post.getAuthor_profile_img_url()});
						getContext().startActivity(intent);
					}
				});
			}
			
			downloadImage(post.getAuthor_profile_img_url());
			tvNickname.setText(post.getAuthor_nickname());
			tvContent.setText(post.getContent());
			tvRegdate.setText(StringUtils.getDateString("등록일 yyyy년 MM월 dd일", post.getCreated_at() * 1000));
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	public void setReply(final Post post, final BCPFragmentActivity activity,
			final String to, final int origin_post_id) {
		
		try {
			replyBadge.setVisibility(View.VISIBLE);
			bgTop.setBackgroundResource(R.drawable.dealer_post_frame_head2);
			tvContent.setBackgroundResource(R.drawable.dealer_post_frame_body2);
			tvRegdate.setBackgroundResource(R.drawable.dealer_post_frame_foot2);
			cover.setBackgroundResource(R.drawable.dealer_post_pic_frame2);
			tvContent.setPadding(ResizeUtils.getSpecificLength(175), ResizeUtils.getSpecificLength(48), 0, 0);
			tvReplyText.setPadding(ResizeUtils.getSpecificLength(21), ResizeUtils.getSpecificLength(10), 0, 0);
			
			//내가 쓴 대댓글인 경우 수정, 삭제 가능.
			if(post.getAuthor_id() == MainActivity.user.getId()) {
				ResizeUtils.setMargin(btnEdit, new int[]{0, 0, 111, 16});
				
				btnEdit.setVisibility(View.VISIBLE);
				btnDelete.setVisibility(View.VISIBLE);
				
				btnEdit.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {

						Bundle bundle = new Bundle();
						bundle.putSerializable("post", post);			//수정인 경우.
						bundle.putString("to", to);						//원 댓글 작성자.
						bundle.putInt("post_id", origin_post_id);		//자유게시판 글 id.
						activity.showPage(BCPConstants.PAGE_FORUM_WRITE_REPLY, bundle);
					}
				});
				
				btnDelete.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {

						deleteReply(post.getId(), activity);
					}
				});
			}
			
			if(!StringUtils.isEmpty(post.getAuthor_profile_img_url())) {
				ivImage.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {

						Intent intent = new Intent(getContext(), ImageViewer.class);
						intent.putExtra("title", getContext().getResources().getString(R.string.profileImage));
						intent.putExtra("imageUrls", new String[]{post.getAuthor_profile_img_url()});
						getContext().startActivity(intent);
					}
				});
			}
			
			downloadImage(post.getAuthor_profile_img_url());
			tvNickname.setText(post.getAuthor_nickname());
			tvReplyText.setText("RE : " + to);
			tvContent.setText(post.getContent());
			tvRegdate.setText(StringUtils.getDateString("등록일 yyyy년 MM월 dd일", post.getCreated_at() * 1000));
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void downloadImage(String imageUrl) {

		if(imageUrl == null) {
			ivImage.setImageDrawable(null);
			return;
			
		} else if(ivImage.getTag() != null
				&& !ivImage.getTag().toString().equals(imageUrl)) {
			ivImage.setImageDrawable(null);
		}
		
		ivImage.setTag(imageUrl);
		BCPDownloadUtils.downloadBitmap(imageUrl, new OnBitmapDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("ReviewView.onError." + "\nurl : " + url);
			}

			@Override
			public void onCompleted(String url, Bitmap bitmap) {

				try {
					LogUtils.log("ReviewView.onCompleted." + "\nurl : " + url);

					if(ivImage != null
							&& !bitmap.isRecycled()) {
						ivImage.setImageBitmap(bitmap);
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		}, 86);
	}

	public void deleteReply(final int replyId, final BCPFragmentActivity activity) {
		
		activity.showAlertDialog(R.string.delete, R.string.wannaDelete, 
				R.string.confirm, R.string.cancel, 
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						String url = BCPAPIs.FORUM_REPLY_DELETE_URL
								+ "?reply_id=" + replyId;
						DownloadUtils.downloadJSONString(url,
								new OnJSONDownloadListener() {

									@Override
									public void onError(String url) {

										LogUtils.log("ForumReplyView.onError."
												+ "\nurl : " + url);
										ToastUtils.showToast(R.string.failToDelete);
									}

									@Override
									public void onCompleted(String url,
											JSONObject objJSON) {

										try {
											LogUtils.log("ForumReplyView.onCompleted."
													+ "\nurl : " + url
													+ "\nresult : " + objJSON);

											if(objJSON.getInt("result") == 1) {
												ToastUtils.showToast(R.string.complete_delete);
												((ForumDetailPage)activity.getTopFragment()).downloadPost();
											} else {
												ToastUtils.showToast(objJSON.getString("message"));
											}
										} catch (Exception e) {
											LogUtils.trace(e);
											ToastUtils.showToast(R.string.failToDelete);
										} catch (OutOfMemoryError oom) {
											LogUtils.trace(oom);
											ToastUtils.showToast(R.string.failToDelete);
										}
									}
								});
					}
				}, null);
	}
}