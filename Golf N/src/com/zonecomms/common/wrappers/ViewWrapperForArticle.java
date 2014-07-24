package com.zonecomms.common.wrappers;

import android.graphics.Color;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.zonecomms.common.models.Article;
import com.zonecomms.common.views.WrapperView;
import com.zonecomms.golfn.IntentHandlerActivity;
import com.zonecomms.golfn.R;
import com.zonecomms.golfn.classes.ApplicationManager;
import com.zonecomms.golfn.classes.ViewWrapperForZonecomms;
import com.zonecomms.golfn.classes.ZoneConstants;

public class ViewWrapperForArticle extends ViewWrapperForZonecomms {
	
	private View imageBg;
	private ImageView ivImage;
	private TextView tvTitle;
	private TextView tvRegdate;
	
	private Article article;
	
	public ViewWrapperForArticle(WrapperView row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			bg = row.findViewById(R.id.list_article_bg);
			imageBg = row.findViewById(R.id.list_article_imageBg);
			ivImage = (ImageView) row.findViewById(R.id.list_article_ivImage);
			tvTitle = (TextView) row.findViewById(R.id.list_article_tvTitle);
			tvRegdate = (TextView) row.findViewById(R.id.list_article_tvRegdate);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	@Override
	public void setSizes() {

		try {
			int p = ResizeUtils.getSpecificLength(8);
			AbsListView.LayoutParams ap = new AbsListView.LayoutParams(ResizeUtils.getSpecificLength(640), 
					ResizeUtils.getSpecificLength(158));
			row.setLayoutParams(ap);
			row.setPadding(p, p, p, 0);
			
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 150, bg, 2, Gravity.CENTER_VERTICAL, null);
			ResizeUtils.viewResize(134, 134, imageBg, 2, Gravity.LEFT, new int[]{8, 8, 0, 0});
			ResizeUtils.viewResize(134, 134, ivImage, 2, Gravity.LEFT, new int[]{8, 8, 0, 0});
			ResizeUtils.viewResize(460, 80, tvTitle, 2, Gravity.LEFT, new int[]{158, 10, 0, 0});
			ResizeUtils.viewResize(460, 40, tvRegdate, 2, Gravity.RIGHT|Gravity.BOTTOM, new int[]{0, 0, 20, 10});
			
			FontInfo.setFontSize(tvTitle, 32);
			FontInfo.setFontSize(tvRegdate, 22);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel instanceof Article) {
				article = (Article) baseModel;
				
				if(!StringUtils.isEmpty(article.getTitle())) {
					tvTitle.setText(article.getTitle());
				}

				String imageUrl = null;
				boolean findThumbnail = false;
				
				//Find thumbnail.
				int size = article.getContent().length;
				for(int i=0; i<size; i++) {
					
					if(findThumbnail) {
						break;
					}
					
					if(article.getContent()[i].type.equals("1")) {
					} else if(article.getContent()[i].type.equals("2")
							|| article.getContent()[i].type.equals("3")) {
						
						if(!StringUtils.isEmpty(article.getContent()[i].thumbnail)) {
							imageUrl = article.getContent()[i].thumbnail;
							findThumbnail = true;
						}
					}
				}
				
				String key = ApplicationManager.getDownloadKeyFromTopFragment();
				setImage(ivImage, imageUrl, key, 134);
				
				if(!StringUtils.isEmpty(article.getReg_dt())) {
					tvRegdate.setText(article.getReg_dt());
				}
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	@Override
	public void setListeners() {

		row.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/article" +
						"?title=" + ApplicationManager.getTopFragment().getTitleText() +
						"&spot_nid=" + article.getSpot_nid();
				
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
	}

	public void setBgColor(int position) {
		
		if(bg == null) {
			return;
		}

		try {
			if(position % 2 == 0) {
				bg.setBackgroundColor(Color.rgb(54, 72, 96));
			} else {
				bg.setBackgroundColor(Color.rgb(61, 82, 109));
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
}
