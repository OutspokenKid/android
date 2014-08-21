package com.zonecomms.common.wrappers;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.zonecomms.clubvera.R;
import com.zonecomms.clubvera.classes.ZonecommsApplication;
import com.zonecomms.common.models.Link;
import com.zonecomms.common.views.WrapperView;

public class ViewWrapperForPhoto extends ViewWrapper {

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
			LogUtils.trace(e);
			setUnusableView();
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
			FontUtils.setFontSize(tvTitle, 24);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
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
				
				setImage(ivImage, link.getLink_datas()[0]);
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

		try {
			if(link != null) {
				if(link.getLink_datas() != null && link.getLink_datas().length > 0) {
					
					row.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							ZonecommsApplication.getActivity().showImageViewerActivity(link.getTitle(), link.getLink_datas(), link.getThumbnails(), 0);
						}
					});
				}
			} else {
				setUnusableView();
			}
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setUnusableView() {
		// TODO Auto-generated method stub

	}
}
