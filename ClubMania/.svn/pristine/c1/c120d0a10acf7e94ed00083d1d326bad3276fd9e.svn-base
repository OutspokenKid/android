package com.zonecomms.common.wrappers;

import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.classes.ApplicationManager;
import com.outspoken_kid.utils.ResizeUtils;
import com.zonecomms.clubmania.IntentHandlerActivity;
import com.zonecomms.clubmania.R;
import com.zonecomms.common.models.BaseModel;
import com.zonecomms.common.models.Link;
import com.zonecomms.common.utils.ImageDownloadUtils;
import com.zonecomms.common.wrapperviews.WrapperView;

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
	public void setSize() {

		int p = ResizeUtils.getSpecificLength(8);
		AbsListView.LayoutParams ap = new AbsListView.LayoutParams(ResizeUtils.getSpecificLength(640), 
				ResizeUtils.getSpecificLength(158));
		row.setLayoutParams(ap);
		row.setPadding(p, p, p, 0);
		
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 150, bg, 2, Gravity.CENTER_VERTICAL, null);
		
		ResizeUtils.viewResize(150, 150, row.findViewById(R.id.list_music_imageBg), 2, Gravity.CENTER_VERTICAL, null);
		ResizeUtils.viewResize(150, 150, ivImage, 2, Gravity.CENTER_VERTICAL, null);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 50, tvTitle, 2, Gravity.LEFT, new int[]{160, 25, 60, 0});
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 50, tvSubTitle, 2, Gravity.LEFT, new int[]{160, 75, 60, 0});
		
		ResizeUtils.viewResize(25, 42, row.findViewById(R.id.list_music_arrow), 2, 
				Gravity.CENTER_VERTICAL|Gravity.RIGHT, new int[]{0, 0, 20, 0});
		
		FontInfo.setFontSize(tvTitle, 32);
		FontInfo.setFontSize(tvSubTitle, 32);
	}

	@Override
	public void setValues(BaseModel baseModel) {

		if(ivImage != null) {
			ivImage.setVisibility(View.GONE);
		}
		
		try {
			if(baseModel != null && baseModel instanceof Link) {
				link = (Link) baseModel;
				
				if(!TextUtils.isEmpty(link.getTitle())) {
					tvTitle.setText(link.getTitle());
				}
				
				if(!TextUtils.isEmpty(link.getName())) {
					tvSubTitle.setText(link.getName());
				}
				
				if(!TextUtils.isEmpty(link.getMain_image())
						&& (ivImage.getTag() == null || !ivImage.getTag().toString().equals(link.getMain_image()))) {
					String key = ApplicationManager.getDownloadKeyFromTopFragment();
					ImageDownloadUtils.downloadImageImmediately(link.getMain_image(), key, ivImage, 320, true);
				} else {
					ivImage.setVisibility(View.VISIBLE);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setListener() {

		if(link != null) {
			if(!TextUtils.isEmpty(link.getLink_data())) {
				
				row.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {

						if(link != null && !TextUtils.isEmpty(link.getLink_data())) {
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
