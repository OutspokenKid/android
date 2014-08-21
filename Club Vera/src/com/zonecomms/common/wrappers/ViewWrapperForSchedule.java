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
import com.zonecomms.common.models.Notice;
import com.zonecomms.common.views.WrapperView;

public class ViewWrapperForSchedule extends ViewWrapper {
	
	private static int length;

	private ImageView ivImage;
	private TextView tvTitle;
	private Notice notice;
	
	public ViewWrapperForSchedule(WrapperView row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			ivImage = (ImageView) row.findViewById(R.id.grid_schedule_ivImage);
			tvTitle = (TextView) row.findViewById(R.id.grid_schedule_tvTitle);
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
			row.setLayoutParams(new AbsListView.LayoutParams(length, length / 2 * 3));
			
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 120, tvTitle, 2, Gravity.BOTTOM, null, new int[]{20, 0, 20, 0});
			FontUtils.setFontSize(tvTitle, 26);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel != null && baseModel instanceof Notice) {
				notice = (Notice) baseModel;
				
				if(!StringUtils.isEmpty(notice.getNotice_title())) {
					tvTitle.setText(notice.getNotice_title());
				}
				
				setImage(ivImage, notice.getMedias()[0].getThumbnail());
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
			if(notice != null) {
				if(notice.getMedias() != null && notice.getMedias().length > 0) {
					row.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							ZonecommsApplication.getActivity().showImageViewerActivity(notice.getNotice_title(), notice.getMedias(), 0);
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
