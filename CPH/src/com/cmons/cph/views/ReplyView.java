package com.cmons.cph.views;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.ShopActivity;
import com.cmons.cph.WholesaleActivity;
import com.cmons.cph.fragments.retail.RetailForOrderPage;
import com.cmons.cph.fragments.wholesale.WholesaleForOrderPage;
import com.cmons.cph.models.Reply;
import com.outspoken_kid.classes.BaseFragment;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class ReplyView extends RelativeLayout {

	private static int madeCount;
	
	private Context context;
	private Reply reply;
	
	private TextView tvShopName;
	private TextView tvRegdate;
	private TextView tvContent;
	private View commentIcon;
	private Button btnDelete;
	
	public ReplyView(Context context) {
		this(context, null, 0);
	}
	
	public ReplyView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public ReplyView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public void init() {

		madeCount += 1;
		
		this.setBackgroundColor(Color.TRANSPARENT);
		
		int textViewHeight = ResizeUtils.getSpecificLength(92);
		int p = ResizeUtils.getSpecificLength(10);
		RelativeLayout.LayoutParams rp = null;
		
		//id : 0.
		tvShopName = new TextView(context);
		rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, textViewHeight);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		tvShopName.setLayoutParams(rp);
		tvShopName.setId(madeCount);
		tvShopName.setPadding(p, 0, 0, 0);
		tvShopName.setTextColor(Color.WHITE);
		tvShopName.setGravity(Gravity.CENTER_VERTICAL);
		this.addView(tvShopName);
		
		tvRegdate = new TextView(context);
		rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, textViewHeight);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		tvRegdate.setLayoutParams(rp);
		tvRegdate.setPadding(0, 0, p, 0);
		tvRegdate.setTextColor(Color.WHITE);
		tvRegdate.setGravity(Gravity.CENTER_VERTICAL);
		this.addView(tvRegdate);
		
		tvContent = new TextView(context);
		rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		rp.addRule(RelativeLayout.BELOW, madeCount);
		tvContent.setLayoutParams(rp);
		tvContent.setMinHeight(textViewHeight);
		tvContent.setPadding(ResizeUtils.getSpecificLength(50), 0, 
				ResizeUtils.getSpecificLength(50), 0);
		tvContent.setTextColor(Color.BLACK);
		tvContent.setBackgroundColor(Color.argb(150, 255, 255, 255));
		tvContent.setGravity(Gravity.CENTER_VERTICAL);
		this.addView(tvContent);
		
		commentIcon = new View(context);
		rp = new RelativeLayout.LayoutParams(
				ResizeUtils.getSpecificLength(23), 
				ResizeUtils.getSpecificLength(23));
		rp.topMargin = ResizeUtils.getSpecificLength(36);
		rp.leftMargin = ResizeUtils.getSpecificLength(10);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.BELOW, madeCount);
		commentIcon.setLayoutParams(rp);
		commentIcon.setBackgroundResource(R.drawable.myshop_comment3_icon);
		this.addView(commentIcon);
		
		btnDelete = new Button(context);
		rp = new RelativeLayout.LayoutParams(
				ResizeUtils.getSpecificLength(30), 
				ResizeUtils.getSpecificLength(30));
		rp.topMargin = ResizeUtils.getSpecificLength(32);
		rp.rightMargin = ResizeUtils.getSpecificLength(10);
		rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rp.addRule(RelativeLayout.BELOW, madeCount);
		btnDelete.setLayoutParams(rp);
		btnDelete.setBackgroundResource(R.drawable.myshop_delete2_btn);
		this.addView(btnDelete);
		
		FontUtils.setFontSize(tvShopName, 40);
		FontUtils.setFontSize(tvContent, 30);
		FontUtils.setFontSize(tvRegdate, 22);
	}
	
	public void setListeners() {
		
		btnDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				ShopActivity.getInstance().showAlertDialog(
						"댓글 삭제", "해당 댓글을 삭제하시겠습니까?", 
						"확인", "취소", 
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								
								BaseFragment bf = ShopActivity.getInstance().getTopFragment();
								
								if(bf instanceof RetailForOrderPage) {
									((RetailForOrderPage)bf).deleteReply(reply);
								} else {
									((WholesaleForOrderPage)bf).deleteReply(reply);
								}
							}
						}, 
						null);
			}
		});
	}

	public void setValues(Reply reply) {
		
		this.reply = reply;

		boolean isWholesaleUser = ShopActivity.getInstance() instanceof WholesaleActivity;
		
		tvShopName.setText(null);
		
		//도매가 쓴 글.
		if("4100".equals(reply.getType())) {
			tvShopName.setBackgroundColor(Color.argb(178, 231, 93, 93));
			FontUtils.addSpan(tvShopName, reply.getWholesale_name(), 0, 1);
			
			//난 소매유저.
			if(!isWholesaleUser) {
				LogUtils.log("###ReplyView.setValues.  phoneNumber : " + reply.getWholesale_phone_number());
				FontUtils.addSpan(tvShopName, "  " + reply.getWholesale_phone_number(), 0, 0.5f);
				btnDelete.setVisibility(View.INVISIBLE);
			} else {
				btnDelete.setVisibility(View.VISIBLE);
			}
			
		//소매가 쓴 글.
		} else {
			tvShopName.setBackgroundColor(Color.argb(178, 96, 183, 202));
			FontUtils.addSpan(tvShopName, reply.getRetail_name(), 0, 1);
			
			//난 도매유저.
			if(isWholesaleUser) {
				FontUtils.addSpan(tvShopName, "  " + reply.getRetail_phone_number(), 0, 0.5f);
				btnDelete.setVisibility(View.INVISIBLE);
			} else {
				btnDelete.setVisibility(View.VISIBLE);
			}
		}
		
		tvContent.setText(reply.getContent());
		tvRegdate.setText(StringUtils.getDateString(
				"yyyy년 MM월 dd일 aa hh:mm", reply.getCreated_at() * 1000));
		
		setListeners();
	}
}
