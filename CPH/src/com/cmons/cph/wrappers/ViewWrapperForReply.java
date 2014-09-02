package com.cmons.cph.wrappers;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.ViewWrapper;
import com.cmons.cph.models.Reply;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class ViewWrapperForReply extends ViewWrapper {

	private Reply reply;
	
	private TextView tvInfo;
	private View shopIcon;
	private View privateIcon;
	private TextView tvContent;
	private View commentIcon;
	private Button btnReply;
	private Button btnDelete;
	
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
			
			//btnReply.
			rp = (RelativeLayout.LayoutParams) btnReply.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(30);
			rp.height = ResizeUtils.getSpecificLength(30);
			rp.topMargin = ResizeUtils.getSpecificLength(31);
			rp.rightMargin = ResizeUtils.getSpecificLength(20);
			
			//btnDelete.
			rp = (RelativeLayout.LayoutParams) btnDelete.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(30);
			rp.height = ResizeUtils.getSpecificLength(30);
			rp.topMargin = ResizeUtils.getSpecificLength(31);
			rp.rightMargin = ResizeUtils.getSpecificLength(80);
			
			FontUtils.setFontSize(tvInfo, 24);
			FontUtils.setFontSize(tvContent, 30);
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

				delete();
			}
		});
	}
	
	@Override
	public void setUnusableView() {
		// TODO Auto-generated method stub
		
	}
	
//////////////////// Custom methods.
	
	public void delete() {
		
	}
	
	public void writeReply() {
		
	}
}
