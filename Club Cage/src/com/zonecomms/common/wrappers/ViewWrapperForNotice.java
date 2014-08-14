package com.zonecomms.common.wrappers;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.zonecomms.clubcage.R;
import com.zonecomms.common.models.Notice;
import com.zonecomms.common.views.WrapperView;

public class ViewWrapperForNotice extends ViewWrapper {

	private static ViewWrapperForNotice openedNotice;
	
	private TextView tvTitle;
	private View arrow;
	private LinearLayout contentLinear;
	private TextView tvContent;
	private ImageView ivImage;
	private ProgressBar progress;
	private Notice notice;

	public ViewWrapperForNotice(WrapperView row, int itemCode) {
		super(row, itemCode);
		
		bindViews();
		setSizes();
	}

	@Override
	public void bindViews() {

		//For set backgroundColor.
		bg = row.findViewById(R.id.list_notice_headerLayout);
		
		tvTitle = (TextView) row.findViewById(R.id.list_notice_tvTitle);
		arrow = row.findViewById(R.id.list_notice_arrow);
		contentLinear = (LinearLayout) row.findViewById(R.id.list_notice_contentLinear);
		tvContent = (TextView) row.findViewById(R.id.list_notice_tvContent);
		ivImage = (ImageView) row.findViewById(R.id.list_notice_ivImage);
		progress = (ProgressBar) row.findViewById(R.id.list_notice_progress);
	}

	@Override
	public void setSizes() {
		
		int p = ResizeUtils.getSpecificLength(8);
		AbsListView.LayoutParams ap = new AbsListView.LayoutParams(ResizeUtils.getSpecificLength(640), 
				LayoutParams.WRAP_CONTENT);
		row.setLayoutParams(ap);
		row.setPadding(p, p, p, 0);
		
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 150, row.findViewById(R.id.list_notice_headerLayout), 1, 0, null);
		ResizeUtils.viewResize(41, 24, arrow, 2, Gravity.CENTER_VERTICAL|Gravity.RIGHT, new int[]{0, 0, 20, 0});

		ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, tvTitle, 2, 
				Gravity.CENTER_VERTICAL, new int[]{30, 0, 60, 0});
		FontUtils.setFontSize(tvTitle, 30);

		ResizeUtils.viewResize(624, 0, ivImage, 2, Gravity.CENTER_HORIZONTAL, new int[]{0, 8, 0, 0});
		
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, tvContent, 
				1, Gravity.CENTER_HORIZONTAL, new int[]{0, 8, 0, 0}, new int[]{30, 30, 30, 30});
		FontUtils.setFontSize(tvContent, 30);
	}

	@Override
	public void setValues(BaseModel baseModel) {

		if(ivImage != null) {
			ivImage.setVisibility(View.GONE);
		}
		
		try {
			if(baseModel != null && baseModel instanceof Notice) {
				notice = (Notice) baseModel;
				
				if(!StringUtils.isEmpty(notice.getNotice_title())) {
					tvTitle.setText(notice.getNotice_title());
				}

				if(!StringUtils.isEmpty(notice.getContent())) {
					tvContent.setText(notice.getContent());
				}
			}
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		} finally {
			close();
		}
	}
	
	@Override
	public void setListeners() {
		
		final ViewWrapperForNotice thisNotice = this;
		
		if(notice != null) {
			row.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(contentLinear.getVisibility() == View.VISIBLE) {
						close();
					} else {
						if(openedNotice != null) {
							openedNotice.close();
						}
						open();
						openedNotice = thisNotice;
					}
				}
			});
		}
	}

	@Override
	public void setUnusableView() {
		// TODO Auto-generated method stub
	}
	
	public void open() {
		
		if(notice.getMedias()[0] != null && !StringUtils.isEmpty(notice.getMedias()[0].getMedia_src())) {

			if(ivImage.getMeasuredHeight() == 0) {
				FrameLayout.LayoutParams fp = (FrameLayout.LayoutParams) ivImage.getLayoutParams();
				
				int width = notice.getMedias()[0].getWidth();
				int height = notice.getMedias()[0].getHeight();
				float scale = (float) height / (float) width;
				int scaledHeight = (int)(fp.width * scale);

				fp.height = scaledHeight;
				ivImage.setLayoutParams(fp);
			}
			
			setImage(ivImage, notice.getMedias()[0].getMedia_src());
		} else {
			progress.setVisibility(View.GONE);
			ivImage.setVisibility(View.GONE);
		}
		
		arrow.setBackgroundResource(R.drawable.img_arrow_up);
		contentLinear.setVisibility(View.VISIBLE);
	}
	
	public void close() {
		ivImage.setVisibility(View.GONE);
		arrow.setBackgroundResource(R.drawable.img_arrow_down);
		contentLinear.setVisibility(View.GONE);
	}
	
	public Notice getNotice() {
		
		return notice;
	}
}
