package com.zonecomms.common.views;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.zonecomms.common.models.Article;

public class ArticleInfoLayout extends LinearLayout {

	private TextView tvTitle;
	private TextView tvRegdate;
	
	public ArticleInfoLayout(Context context) {
		this(context, null);
	}
	
	public ArticleInfoLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	private void init() {
		
		this.setBackgroundColor(Color.rgb(55, 55, 55));
		this.setOrientation(LinearLayout.VERTICAL);

		int p = ResizeUtils.getSpecificLength(16);
		
		tvTitle = new TextView(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 100, tvTitle, 1, 0, null);
		tvTitle.setPadding(p, p, p, 0);
		tvTitle.setTextColor(Color.WHITE);
		tvTitle.setMaxLines(2);
		FontInfo.setFontSize(tvTitle, 30);
		this.addView(tvTitle);
		
		tvRegdate = new TextView(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 50, tvRegdate, 1, 0, null);
		tvRegdate.setPadding(0, 0, p, 0);
		tvRegdate.setSingleLine();
		tvRegdate.setEllipsize(TruncateAt.END);
		tvRegdate.setTextColor(Color.WHITE);
		tvRegdate.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
		FontInfo.setFontSize(tvRegdate, 22);
		this.addView(tvRegdate);
	}

	public void setArticleInfo(Article article) {

		String title = "";
		
		for(int i=0; i<10; i++) {
			title += "title";
		}
		tvTitle.setText(title);
		tvRegdate.setText("2014-03-16 20:20:20");
		
		if(article == null) {
			return;
		}
		
		if(!StringUtils.isEmpty(article.getTitle())) {
			tvTitle.setText(article.getTitle());
		}
		
		if(!StringUtils.isEmpty(article.getReg_dt())) {
			tvRegdate.setText(article.getReg_dt());
		}
	}
}
