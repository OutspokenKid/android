package com.zonecomms.common.wrappers;

import com.outspoken_kid.utils.StringUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.classes.ApplicationManager;
import com.outspoken_kid.utils.ResizeUtils;
import com.zonecomms.clubcage.R;
import com.zonecomms.common.models.BaseModel;
import com.zonecomms.common.models.Notice;
import com.zonecomms.common.wrapperviews.WrapperView;

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
			e.printStackTrace();
			setUnusableView();
		}
	}

	@Override
	public void setSize() {

		try {
			if(length == 0) {
				int s = ResizeUtils.getSpecificLength(8);
				int l = ResizeUtils.getSpecificLength(150);
				length = l*2 + s + s + s/2;
			}
			row.setLayoutParams(new AbsListView.LayoutParams(length, length / 2 * 3));
			
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 120, tvTitle, 2, Gravity.BOTTOM, null, new int[]{20, 0, 20, 0});
			FontInfo.setFontSize(tvTitle, 26);
		} catch(Exception e) {
			e.printStackTrace();
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
				
				String key = ApplicationManager.getDownloadKeyFromTopFragment();
				setImage(ivImage, notice.getMedias()[0].getThumbnail(), key, 308);
			} else {
				setUnusableView();
			}
		} catch(Exception e) {
			e.printStackTrace();
			setUnusableView();
		}
	}

	@Override
	public void setListener() {
		try {
			if(notice != null) {
				if(notice.getMedias() != null && notice.getMedias().length > 0) {
					row.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							ApplicationManager.getInstance().getMainActivity().showImageViewerActivity(notice.getNotice_title(), notice.getMedias(), 0);
						}
					});
				}
			} else {
				setUnusableView();
			}
		} catch(Exception e) {
			e.printStackTrace();
			setUnusableView();
		}
	}
	
	@Override
	public void setUnusableView() {
		// TODO Auto-generated method stub
		
	}
}
