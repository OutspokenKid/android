package com.zonecomms.common.wrappers;

import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.zonecomms.clubvera.IntentHandlerActivity;
import com.zonecomms.clubvera.R;
import com.zonecomms.common.models.Link;
import com.zonecomms.common.views.WrapperView;

public class ViewWrapperForMusic extends ViewWrapper {

	private ImageView ivImage;
	private TextView tvTitle;
	private TextView tvSubTitle;
	private Link link;
	
	public ViewWrapperForMusic(WrapperView row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		//For set backgroundColor.
		bg = row.findViewById(R.id.list_music_bg);
		
		ivImage = (ImageView) row.findViewById(R.id.list_music_ivImage);
		tvTitle = (TextView) row.findViewById(R.id.list_music_tvTitle);
		tvSubTitle = (TextView) row.findViewById(R.id.list_music_tvSubTitle);
	}

	@Override
	public void setSizes() {

		int p = ResizeUtils.getSpecificLength(8);
		AbsListView.LayoutParams ap = new AbsListView.LayoutParams(ResizeUtils.getSpecificLength(640), 
				ResizeUtils.getSpecificLength(158));
		row.setLayoutParams(ap);
		row.setPadding(p, p, p, 0);
		
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 150, bg, 2, Gravity.CENTER_VERTICAL, null);
		
		ResizeUtils.viewResize(150, 150, row.findViewById(R.id.list_music_imageBg), 2, 
				Gravity.CENTER_VERTICAL, null);
		ResizeUtils.viewResize(150, 150, ivImage, 2, Gravity.CENTER_VERTICAL, null);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 40, tvTitle, 2, 
				Gravity.LEFT|Gravity.TOP, new int[]{166, 20, 60, 0});
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 40, tvSubTitle, 2, 
				Gravity.LEFT|Gravity.BOTTOM, new int[]{166, 0, 60, 20});
		ResizeUtils.viewResize(25, 42, row.findViewById(R.id.list_music_arrow), 2, 
				Gravity.CENTER_VERTICAL|Gravity.RIGHT, new int[]{0, 0, 20, 0});
		
		FontUtils.setFontSize(tvTitle, 28);
		FontUtils.setFontSize(tvSubTitle, 24);
	}

	@Override
	public void setValues(BaseModel baseModel) {

		if(ivImage != null) {
			ivImage.setVisibility(View.GONE);
		}
		
		try {
			if(baseModel != null && baseModel instanceof Link) {
				link = (Link) baseModel;
				
				if(!StringUtils.isEmpty(link.getTitle())) {
					tvTitle.setText(link.getTitle());
				}
				
				if(!StringUtils.isEmpty(link.getName())) {
					tvSubTitle.setText(link.getName());
				}
				
				setImage(ivImage, link.getMain_image());
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	@Override
	public void setListeners() {

		if(link != null) {
			if(!StringUtils.isEmpty(link.getLink_data())) {
				
				row.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {

						if(link != null && !StringUtils.isEmpty(link.getLink_data())) {
							IntentHandlerActivity.actionByUri(Uri.parse(link.getLink_data()));
						}
					}
				});
			}
		}
	}
	
	@Override
	public void setUnusableView() {
		// TODO Auto-generated method stub
	}
}
