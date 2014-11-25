package com.cmons.cph.wrappers;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.ShopActivity;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.fragments.common.ReplyPage;
import com.cmons.cph.models.Reply;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class ViewWrapperForReply extends ViewWrapper {

	private Reply reply;
	
	private TextView tvInfo;
	private View shopIcon;
	private View privateIcon;
	private TextView tvContent;
	private View commentIcon;
	private Button btnReply;
	private Button btnDelete;
	
	private RelativeLayout childRelative;
	private TextView tvContent2;
	private View replyIcon;
	private TextView tvRegdate;
	private Button btnDelete2;
	
	public ViewWrapperForReply(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			tvInfo = (TextView) row.findViewById(R.id.list_reply_tvInfo);
			shopIcon = row.findViewById(R.id.list_reply_shopIcon);
			privateIcon = row.findViewById(R.id.list_reply_privateIcon);
			tvContent = (TextView) row.findViewById(R.id.list_reply_tvContent);
			commentIcon = row.findViewById(R.id.list_reply_commentIcon);
			btnReply = (Button) row.findViewById(R.id.list_reply_btnReply);
			btnDelete = (Button) row.findViewById(R.id.list_reply_btnDelete);
			
			childRelative = (RelativeLayout) row.findViewById(R.id.list_reply_childRelative);
			tvContent2 = (TextView) row.findViewById(R.id.list_reply_tvContent2);
			replyIcon = row.findViewById(R.id.list_reply_replyIcon);
			tvRegdate = (TextView) row.findViewById(R.id.list_reply_tvRegdate);
			btnDelete2 = (Button) row.findViewById(R.id.list_reply_btnDelete2);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			RelativeLayout.LayoutParams rp = null;
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
			tvContent.setPadding(ResizeUtils.getSpecificLength(40), 
					tp, 
					ResizeUtils.getSpecificLength(120),
					tp);
			
			//commentIcon.
			rp = (RelativeLayout.LayoutParams) commentIcon.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(23);
			rp.height = ResizeUtils.getSpecificLength(23);
			rp.leftMargin = ResizeUtils.getSpecificLength(10);
			rp.topMargin = ResizeUtils.getSpecificLength(34);

			//btnDelete.
			rp = (RelativeLayout.LayoutParams) btnDelete.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(30);
			rp.height = ResizeUtils.getSpecificLength(30);
			rp.topMargin = ResizeUtils.getSpecificLength(31);
			rp.rightMargin = ResizeUtils.getSpecificLength(20);
			
			//btnReply.
			rp = (RelativeLayout.LayoutParams) btnReply.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(30);
			rp.height = ResizeUtils.getSpecificLength(30);
			rp.rightMargin = ResizeUtils.getSpecificLength(30);
			
			//tvContent2
			tvContent2.setPadding(ResizeUtils.getSpecificLength(40), 
					ResizeUtils.getSpecificLength(30), 
					ResizeUtils.getSpecificLength(40), 
					tp);
			tvContent2.setMinHeight(ResizeUtils.getSpecificLength(92));
			
			//replyIcon.
			rp = (RelativeLayout.LayoutParams) replyIcon.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(19);
			rp.height = ResizeUtils.getSpecificLength(16);
			rp.leftMargin = ResizeUtils.getSpecificLength(10);
			rp.topMargin = ResizeUtils.getSpecificLength(44);
			
			//tvRegdate.
			rp = (RelativeLayout.LayoutParams) tvRegdate.getLayoutParams();
			rp.height = ResizeUtils.getSpecificLength(40);
			rp.rightMargin = ResizeUtils.getSpecificLength(20);
			
			//btnDelete2.
			rp = (RelativeLayout.LayoutParams) btnDelete2.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(30);
			rp.height = ResizeUtils.getSpecificLength(30);
			rp.rightMargin = ResizeUtils.getSpecificLength(20);
			
			FontUtils.setFontSize(tvInfo, 24);
			FontUtils.setFontSize(tvContent, 30);
			FontUtils.setFontSize(tvContent2, 30);
			FontUtils.setFontSize(tvRegdate, 22);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel instanceof Reply) {
				reply = (Reply) baseModel;
				
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
				
				//해당 게시글의 도매 사람이거나, 
				if(reply.getWholesale_id() == ShopActivity.getInstance().user.getWholesale_id()) {
					btnDelete.setVisibility(View.VISIBLE);
					btnReply.setVisibility(View.VISIBLE);
					btnDelete2.setVisibility(View.VISIBLE);
					
				//게시글 작성자인 경우에 삭제 버튼 노출.
				} else if(ShopActivity.getInstance().user.getId().equals(reply.getAuthor_id())) {
					btnDelete.setVisibility(View.VISIBLE);
					btnReply.setVisibility(View.GONE);
					btnDelete2.setVisibility(View.GONE);
				} else {
					btnDelete.setVisibility(View.GONE);
					btnReply.setVisibility(View.GONE);
					btnDelete2.setVisibility(View.GONE);
				}
				
				//그리고 이미 댓글이 있다면 댓글 버튼 숨김.
				if(reply.getReplies() != null && reply.getReplies().length > 0) {
					btnReply.setVisibility(View.GONE);

					tvContent2.setText(reply.getReplies()[0].getContent());
					
					String dateString = StringUtils.getDateString(
							"yyyy년 MM월 dd일 aa hh:mm", 
							reply.getReplies()[0].getCreated_at() * 1000);
					tvRegdate.setText(dateString);
					
					childRelative.setVisibility(View.VISIBLE);
				} else {
					childRelative.setVisibility(View.GONE);
				}
				
				tvContent.setText(reply.getContent());
			} else {
				setUnusableView();
			}
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setListeners() {
		
		btnReply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				writeReply();
			}
		});
		
		btnDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				ShopActivity.getInstance().showAlertDialog(
						"댓글 삭제", "해당 댓글을 삭제하시겠습니까?", 
						"확인", "취소", 
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								delete(reply);
							}
						}, 
						null);
			}
		});
	
		btnDelete2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				ShopActivity.getInstance().showAlertDialog(
						"댓글 삭제", "해당 댓글을 삭제하시겠습니까?", 
						"확인", "취소", 
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								delete(reply.getReplies()[0]);
							}
						}, 
						null);
			}
		});
	}
	
	@Override
	public void setUnusableView() {
		// TODO Auto-generated method stub
		
	}
	
//////////////////// Custom methods.
	
	public void delete(final Reply reply) {
		
		String url = CphConstants.BASE_API_URL + "products/replies/delete" +
				"?product_id=" + reply.getProduct_id() +
				"&reply_id=" + reply.getId();
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("ViewWrapperForReply.delete.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToDeleteReply);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("ViewWrapperForReply.delete.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						((ReplyPage)ShopActivity.getInstance().getTopFragment()).notifyDataSetChanged(reply);
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					ToastUtils.showToast(R.string.failToDeleteReply);
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					ToastUtils.showToast(R.string.failToDeleteReply);
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void writeReply() {
		
		if(ShopActivity.getInstance().getTopFragment() != null
				&& ShopActivity.getInstance().getTopFragment() instanceof ReplyPage) {
			((ReplyPage)ShopActivity.getInstance().getTopFragment()).setReplyScroll(reply);
		}
	}
}
