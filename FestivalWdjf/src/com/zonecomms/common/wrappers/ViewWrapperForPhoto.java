package com.zonecomms.common.wrappers;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.zonecomms.common.models.Link;
import com.zonecomms.common.utils.ImageDownloadUtils;
import com.zonecomms.common.views.WrapperView;
import com.zonecomms.festivalwdjf.R;
import com.zonecomms.festivalwdjf.classes.ApplicationManager;
import com.zonecomms.festivalwdjf.classes.ViewWrapperForZonecomms;

public class ViewWrapperForPhoto extends ViewWrapperForZonecomms {

	private static int length;
	
	private ImageView ivImage;
	private TextView tvTitle;
	private Link link;
	
	public ViewWrapperForPhoto(WrapperView row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			ivImage = (ImageView) row.findViewById(R.id.grid_photo_ivImage);
			tvTitle = (TextView) row.findViewById(R.id.grid_photo_tvTitle);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setSizes() {

		try {
			if(length == 0) {
				int s = ResizeUtils.getSpecificLength(8);
				int l = ResizeUtils.getSpecificLength(150);
				length = l*2 + s + s + s/2;
			}
			row.setLayoutParams(new AbsListView.LayoutParams(length, length));
			
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 40, tvTitle, 2, Gravity.BOTTOM, null, new int[]{10, 0, 10, 0});
			FontInfo.setFontSize(tvTitle, 24);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			
			if(baseModel instanceof Link) {
				link = (Link) baseModel;
				
				if(!StringUtils.isEmpty(link.getTitle())) {
					tvTitle.setText(link.getTitle());
				}
			
				if(ivImage.getTag() == null || ivImage.getTag().toString() == null
						|| !ivImage.getTag().toString().equals(link.getThumbnails()[0])) {
					ivImage.setVisibility(View.INVISIBLE);
				}
				
				String key = ApplicationManager.getDownloadKeyFromTopFragment();
				ImageDownloadUtils.downloadImageImmediately(link.getLink_datas()[0], key, ivImage, 308, true);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void setListeners() {

		try {
			if(link != null) {
				if(link.getLink_datas() != null && link.getLink_datas().length > 0) {
					
					row.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							ApplicationManager.getInstance().getActivity().showImageViewerActivity(link.getTitle(), link.getLink_datas(), link.getThumbnails(), 0);
						}
					});
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
