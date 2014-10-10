package com.cmons.cph.fragments.common;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.ShopActivity;
import com.cmons.cph.classes.CmonsFragmentForShop;
import com.cmons.cph.classes.CphAdapter;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Product;
import com.cmons.cph.models.Reply;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class ReplyPage extends CmonsFragmentForShop {

	private int product_id;

	private TextView tvEmpty;
	private EditText etReply;
	private Button btnSubmit;
	private TextView tvPrivate;
	private View cbPrivate;
	private View icPrivate;
	private ListView listView;
	
	private View cover;
	private ScrollView replyScroll;
	private TextView tvInfo;
	private View shopIcon;
	private View privateIcon;
	private TextView tvContent;
	private View commentIcon;
	private EditText etReply2;
	private Button btnSubmit2;
	private View replyIcon;

	private boolean isPrivate;
	private AlphaAnimation aaIn, aaOut;
	private Reply selectedReply;
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(models.size() == 0) {
			downloadInfo();
		}
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.replyPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.replyPage_ivBg);
		
		tvEmpty = (TextView) mThisView.findViewById(R.id.replyPage_tvEmpty);
		etReply = (EditText) mThisView.findViewById(R.id.replyPage_etReply);
		btnSubmit = (Button) mThisView.findViewById(R.id.replyPage_btnSubmit);
		tvPrivate = (TextView) mThisView.findViewById(R.id.replyPage_tvPrivate);
		cbPrivate = mThisView.findViewById(R.id.replyPage_cbPrivate);
		icPrivate = mThisView.findViewById(R.id.replyPage_icPrivate);
		
		listView = (ListView) mThisView.findViewById(R.id.replyPage_listView);
		
		cover = mThisView.findViewById(R.id.replyPage_cover);
		replyScroll = (ScrollView) mThisView.findViewById(R.id.replyPage_replyScroll);
		tvInfo = (TextView) mThisView.findViewById(R.id.replyPage_tvInfo);
		shopIcon = mThisView.findViewById(R.id.replyPage_shopIcon);
		privateIcon = mThisView.findViewById(R.id.replyPage_privateIcon);
		tvContent = (TextView) mThisView.findViewById(R.id.replyPage_tvContent);
		commentIcon = mThisView.findViewById(R.id.replyPage_commentIcon);
		etReply2 = (EditText) mThisView.findViewById(R.id.replyPage_etReply2);
		btnSubmit2 = (Button) mThisView.findViewById(R.id.replyPage_btnSubmit2);
		replyIcon = mThisView.findViewById(R.id.replyPage_replyIcon);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {
			
			if(getArguments().containsKey("product")) {
				product_id = ((Product) getArguments().getSerializable("product")).getId();
			} else if(getArguments().containsKey("product_id")) {
				product_id = getArguments().getInt("product_id");
			}
			
			title = "상품 댓글";
		}

		aaIn = new AlphaAnimation(0, 1);
		aaIn.setDuration(300);
		
		aaOut = new AlphaAnimation(1, 0);
		aaOut.setDuration(300);
	}

	@Override
	public void createPage() {
		
		if(mActivity.user.getRetail_id() != 0) {
			etReply.setVisibility(View.VISIBLE);
			btnSubmit.setVisibility(View.VISIBLE);
			tvPrivate.setVisibility(View.VISIBLE);
			cbPrivate.setVisibility(View.VISIBLE);
			icPrivate.setVisibility(View.VISIBLE);
		} else {
			etReply.setVisibility(View.GONE);
			btnSubmit.setVisibility(View.GONE);
			tvPrivate.setVisibility(View.GONE);
			cbPrivate.setVisibility(View.GONE);
			icPrivate.setVisibility(View.GONE);
		}
		
		titleBar.getBackButton().setVisibility(View.VISIBLE);
		titleBar.getHomeButton().setVisibility(View.VISIBLE);
		
		adapter = new CphAdapter(mContext, getActivity().getLayoutInflater(), models);
		listView.setAdapter(adapter);
		listView.setDivider(new ColorDrawable(Color.WHITE));
	}

	@Override
	public void setListeners() {
		
		icPrivate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				isPrivate = !isPrivate;
				
				if(isPrivate) {
					cbPrivate.setBackgroundResource(R.drawable.reply_checkbox_b);
				} else {
					cbPrivate.setBackgroundResource(R.drawable.reply_checkbox_a);
				}
			}
		});
		
		cbPrivate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				isPrivate = !isPrivate;
				
				if(isPrivate) {
					cbPrivate.setBackgroundResource(R.drawable.reply_checkbox_b);
				} else {
					cbPrivate.setBackgroundResource(R.drawable.reply_checkbox_a);
				}
			}
		});
		
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(etReply.getText() == null) {
					ToastUtils.showToast(R.string.wrongReply);
					return;
				}
				
				writeReply(false);
			}
		});
		
		btnSubmit2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(etReply2.getText() == null) {
					ToastUtils.showToast(R.string.wrongReply);
					return;
				}
				
				writeReply(true);
			}
		});
	
		cover.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				hideReplyScroll();
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		int p = ResizeUtils.getSpecificLength(10);
		//etReply.
		rp = (RelativeLayout.LayoutParams) etReply.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(550);
		rp.height = ResizeUtils.getSpecificLength(92);
		etReply.setPadding(p, 0, p, 0);
		
		//btnSubmit.
		rp = (RelativeLayout.LayoutParams) btnSubmit.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);

		//tvPrivate.
		rp = (RelativeLayout.LayoutParams) tvPrivate.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(56);
		tvPrivate.setPadding(p, 0, 0, 0);

		//cbPrivate.
		rp = (RelativeLayout.LayoutParams) cbPrivate.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(44);
		rp.height = ResizeUtils.getSpecificLength(43);
		rp.rightMargin = ResizeUtils.getSpecificLength(10);
		rp.bottomMargin = ResizeUtils.getSpecificLength(6);
		
		//icPrivate.
		rp = (RelativeLayout.LayoutParams) icPrivate.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(97);
		rp.height = ResizeUtils.getSpecificLength(27);
		rp.rightMargin = ResizeUtils.getSpecificLength(10);
		rp.bottomMargin = ResizeUtils.getSpecificLength(15);
		
		FontUtils.setFontSize(etReply, 30);
		FontUtils.setFontSize(tvPrivate, 24);
		
		/////////////// replyScroll.
		
		int tp = ResizeUtils.getSpecificLength(20);
		//tvInfo.
		rp = (RelativeLayout.LayoutParams) tvInfo.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		tvInfo.setPadding(tp, 0, tp, 0);
		
		//shopIcon.
		rp = (RelativeLayout.LayoutParams) shopIcon.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(146);
		rp.height = ResizeUtils.getSpecificLength(30);
		rp.topMargin = ResizeUtils.getSpecificLength(20);
		rp.rightMargin = ResizeUtils.getSpecificLength(20);
		
		//privateIcon.
		rp = (RelativeLayout.LayoutParams) privateIcon.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(70);
		rp.height = ResizeUtils.getSpecificLength(21);
		rp.topMargin = ResizeUtils.getSpecificLength(10);
		rp.rightMargin = ResizeUtils.getSpecificLength(10);
		
		//tvContent.
		rp = (RelativeLayout.LayoutParams) tvContent.getLayoutParams();
		tvContent.setMinHeight(ResizeUtils.getSpecificLength(92));
		tvContent.setPadding(ResizeUtils.getSpecificLength(40), tp, tp, tp);
		
		//commentIcon.
		rp = (RelativeLayout.LayoutParams) commentIcon.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(23);
		rp.height = ResizeUtils.getSpecificLength(23);
		rp.leftMargin = ResizeUtils.getSpecificLength(10);
		rp.topMargin = ResizeUtils.getSpecificLength(34);
		
		//etReply.
		rp = (RelativeLayout.LayoutParams) etReply2.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(550);
		rp.height = ResizeUtils.getSpecificLength(92);
		etReply2.setPadding(ResizeUtils.getSpecificLength(40), 0, p, 0);
		
		//btnSubmit.
		rp = (RelativeLayout.LayoutParams) btnSubmit2.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//replyIcon.
		rp = (RelativeLayout.LayoutParams) replyIcon.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(19);
		rp.height = ResizeUtils.getSpecificLength(16);
		rp.leftMargin = ResizeUtils.getSpecificLength(10);
		rp.topMargin = ResizeUtils.getSpecificLength(38);
		
		FontUtils.setFontSize(etReply2, 30);
		FontUtils.setFontSize(tvInfo, 24);
		FontUtils.setFontSize(tvContent, 30);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_common_reply;
	}

	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onBackPressed() {
		
		if(cover.getVisibility() == View.VISIBLE) {
			hideReplyScroll();
			return true;
		}
		
		return false;
	}

	@Override
	public boolean parseJSON(JSONObject objJSON) {

		try {
			JSONArray arJSON = objJSON.getJSONArray("replies");
			int size = arJSON.length();
			
			for(int i=0; i<size; i++) {
				Reply reply = new Reply(arJSON.getJSONObject(i));
				reply.setItemCode(CphConstants.ITEM_REPLY);
				models.add(reply);
			}
			
			if(pageIndex == 1 && size == 0) {
				tvEmpty.setVisibility(View.VISIBLE);
			} else {
				tvEmpty.setVisibility(View.INVISIBLE);
			}
				
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}

		return false;
	}

	@Override
	public void downloadInfo() {
		
		url = CphConstants.BASE_API_URL + "products/replies" +
				"?product_id=" + product_id;
		super.downloadInfo();
	}
	
	@Override
	public int getBgResourceId() {

		return R.drawable.staff_bg;
	}
	
//////////////////// Custom methods.

	public void setReplyScroll(Reply reply) {
		
		try {
			if(reply != null) {
				selectedReply = reply;
				
				tvInfo.setText(null);
				FontUtils.addSpan(tvInfo, StringUtils.getDateString(
						"yyyy년 MM월 dd일 aa hh:mm", 
						reply.getCreated_at()*1000), 0, 0.8f);
				FontUtils.addSpan(tvInfo, "\n" + reply.getRetail_name(), 0, 1.4f);
				FontUtils.addSpan(tvInfo, " " + reply.getRetail_phone_number(), 0, 1.2f);
				
				//온라인.
				if(reply.getRetail_type() == 21) {
					shopIcon.setBackgroundResource(R.drawable.online_shop_icon);
					
				//오프라인.
				} else {
					shopIcon.setBackgroundResource(R.drawable.offline_shop_icon);
				}
				
				if(reply.getIs_private() == 1) {
					privateIcon.setVisibility(View.VISIBLE);
				} else {
					privateIcon.setVisibility(View.INVISIBLE);
				}
				
				//해당 게시글이 속한 도매 사람인 경우 작성 부분 숨김.
				if(reply.getWholesale_id() == ShopActivity.getInstance().user.getWholesale_id()) {
					etReply.setVisibility(View.GONE);
					btnSubmit.setVisibility(View.GONE);
					tvPrivate.setVisibility(View.GONE);
					cbPrivate.setVisibility(View.GONE);
					icPrivate.setVisibility(View.GONE);
					
				//그 외의 경우 작성부분 노출.
				} else {
					etReply.setVisibility(View.VISIBLE);
					btnSubmit.setVisibility(View.VISIBLE);
					tvPrivate.setVisibility(View.VISIBLE);
					cbPrivate.setVisibility(View.VISIBLE);
					icPrivate.setVisibility(View.VISIBLE);
				}
				
				tvContent.setText(reply.getContent());
				SoftKeyboardUtils.showKeyboard(mContext, etReply2);
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		} finally {
			showReplyScroll();
		}
	}

	public void showReplyScroll() {

		if(replyScroll.getVisibility() != View.VISIBLE) {
			cover.setVisibility(View.VISIBLE);
			cover.startAnimation(aaIn);
			
			replyScroll.setVisibility(View.VISIBLE);
			replyScroll.startAnimation(aaIn);
			
			etReply2.setVisibility(View.VISIBLE);
			etReply2.startAnimation(aaIn);
			
			btnSubmit2.setVisibility(View.VISIBLE);
			btnSubmit2.startAnimation(aaIn);
			
			replyIcon.setVisibility(View.VISIBLE);
			replyIcon.startAnimation(aaIn);
			
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					etReply2.requestFocus();
				}
			}, 300);
		}
	}
	
	public void hideReplyScroll() {
		
		if(replyScroll.getVisibility() == View.VISIBLE) {
			cover.setVisibility(View.INVISIBLE);
			cover.startAnimation(aaOut);
			
			replyScroll.setVisibility(View.INVISIBLE);
			replyScroll.startAnimation(aaOut);
			
			etReply2.setVisibility(View.INVISIBLE);
			etReply2.startAnimation(aaOut);
			
			btnSubmit2.setVisibility(View.INVISIBLE);
			btnSubmit2.startAnimation(aaOut);
			
			replyIcon.setVisibility(View.INVISIBLE);
			replyIcon.startAnimation(aaOut);
			
			SoftKeyboardUtils.hideKeyboard(mContext, etReply2);
		}
	}
	
	public void writeReply(final boolean isChild) {

		try {
			String url = CphConstants.BASE_API_URL + "products/replies/save" +
					"?product_id=" + product_id;
					
			
			if(isChild) {
				url += "&parent_id=" + selectedReply.getId() + 
						"&content=" + URLEncoder.encode(etReply2.getText().toString(), "utf-8");
			} else {
				url += "&is_private=" + (isPrivate? 1 : 0) +
						"&content=" + URLEncoder.encode(etReply.getText().toString(), "utf-8");
			}
			
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("ReplyPage.writeReply.onError." + "\nurl : " + url);
					ToastUtils.showToast(R.string.failToWriteReply);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("ReplyPage.writeReply.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						if(objJSON.getInt("result") == 1) {
							
							if(isChild) {
								Reply newReply = new Reply(objJSON.getJSONObject("reply"));
								
								Reply[] selectedReplies = selectedReply.getReplies();
								
								if(selectedReply.getReplies() == null) {
									selectedReply.setReplies(new Reply[]{newReply});
								} else {
									int size = selectedReplies.length;
									Reply[] newReplies = new Reply[size + 1];
									
									for(int i=0; i<size; i++) {
										newReplies[i] = selectedReplies[i];
									}
									
									newReplies[newReplies.length - 1] = newReply;
									selectedReply.setReplies(newReplies);
								}
								
								if(isChild) {
									etReply2.setText(null);
								} else {
									etReply.setText(null);
								}
								
								adapter.notifyDataSetChanged();
								hideReplyScroll();
							} else {
								etReply.setText(null);
								refreshPage();
							}
							
							ToastUtils.showToast(R.string.complete_writeReply);
							
							SoftKeyboardUtils.hideKeyboard(mContext, etReply);
						} else {
							ToastUtils.showToast(objJSON.getString("message"));
						}
					} catch (Exception e) {
						ToastUtils.showToast(R.string.failToWriteReply);
						LogUtils.trace(e);
					} catch (OutOfMemoryError oom) {
						ToastUtils.showToast(R.string.failToWriteReply);
						LogUtils.trace(oom);
					}
				}
			});
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	public void notifyDataSetChanged(Reply deletedReply) {
	
		//댓글의 댓글.
		if(deletedReply.getParent_id() != 0) {
			for(int i=0; i<models.size(); i++) {
				
				if(((Reply)models.get(i)).getId() == deletedReply.getParent_id()) {
					((Reply)models.get(i)).setReplies(null);
				}
			}
			
		//댓글.
		} else {
			for(int i=0; i<models.size(); i++) {
				
				if(((Reply)models.get(i)).getId() == deletedReply.getId()) {
					models.remove(i);
				}
			}
		}
		
		adapter.notifyDataSetChanged();
	}
}
