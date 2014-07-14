package com.zonecomms.common.wrappers;

import android.graphics.Color;
import android.text.method.LinkMovementMethod;
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

import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.zonecomms.common.models.Notice;
import com.zonecomms.common.views.WrapperView;
import com.zonecomms.golfn.R;
import com.zonecomms.golfn.classes.ApplicationManager;
import com.zonecomms.golfn.classes.ViewWrapperForZonecomms;

public class ViewWrapperForNotice extends ViewWrapperForZonecomms {

	private static ViewWrapperForNotice openedNotice;
	private static final int lowColor = 65;
	private static final int highColor = 125;
	private static final int difference = 10;
	
	private TextView tvTitle;
	private View arrow;
	private LinearLayout contentLinear;
	private TextView tvContent;
	private TextView tvLink;
	private ImageView ivImage;
	private ProgressBar progress;
	private Notice notice;
	private boolean opened;

	public ViewWrapperForNotice(WrapperView row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		//For set backgroundColor.
		bg = row.findViewById(R.id.list_notice_headerLayout);
		
		tvTitle = (TextView) row.findViewById(R.id.list_notice_tvTitle);
		arrow = row.findViewById(R.id.list_notice_arrow);
		contentLinear = (LinearLayout) row.findViewById(R.id.list_notice_contentLinear);
		tvContent = (TextView) row.findViewById(R.id.list_notice_tvContent);
		tvLink = (TextView) row.findViewById(R.id.list_notice_tvLink);
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
		FontInfo.setFontSize(tvTitle, 30);

		contentLinear.setMinimumHeight(ResizeUtils.getSpecificLength(150));
		
		ResizeUtils.viewResize(624, 0, ivImage, 2, Gravity.CENTER_HORIZONTAL, new int[]{0, 8, 0, 0});
		
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, tvContent, 
				1, Gravity.CENTER_HORIZONTAL, new int[]{0, 8, 0, 0}, new int[]{8, 8, 8, 8});
		FontInfo.setFontSize(tvContent, 30);
		
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, tvLink, 
				1, Gravity.CENTER_HORIZONTAL, new int[]{0, 8, 0, 20}, new int[]{8, 8, 8, 8});
		FontInfo.setFontSize(tvLink, 30);
		tvLink.setMovementMethod(LinkMovementMethod.getInstance());
	}

	@Override
	public void setValues(BaseModel baseModel) {

		if(ivImage != null) {
			ivImage.setVisibility(View.GONE);
		}
		
		if(opened) {
			open();
		} else {
			close();
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
		} finally {
			close();
		}
	}
	
	@Override
	public void setListeners() {
		
		final ViewWrapperForNotice thisNotice = this;
		
		if(notice != null) {
			bg.setOnClickListener(new OnClickListener() {
				
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
	
	public void open() {
		
		opened = true;

		if(!StringUtils.isEmpty(notice.getLink_url()) 
				&& (notice.getLink_url().contains("http://") 
						|| notice.getLink_url().contains("https://"))) {
			tvLink.setVisibility(View.VISIBLE);
			tvLink.setText(notice.getLink_url());
		} else {
			tvLink.setVisibility(View.GONE);
		}
		
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
			
			String key = ApplicationManager.getDownloadKeyFromTopFragment();
			final String url = notice.getMedias()[0].getMedia_src();
			setImage(ivImage, url, key, 640);
			
			ivImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					ApplicationManager.getInstance().getActivity().showImageViewerActivity(null, new String[]{url}, null, 0);
				}
			});
		} else {
			progress.setVisibility(View.GONE);
			ivImage.setVisibility(View.GONE);
		}
		
		arrow.setBackgroundResource(R.drawable.img_arrow_up);
		contentLinear.setVisibility(View.VISIBLE);
	}
	
	public void close() {
		
		opened = false;
		
		ivImage.setVisibility(View.GONE);
		arrow.setBackgroundResource(R.drawable.img_arrow_down);
		contentLinear.setVisibility(View.GONE);
	}
	
	public Notice getNotice() {
		
		return notice;
	}

	public void setBgColor(int position) {
		
		if(bg == null) {
			return;
		}

		try {
			int newColor = 0;
			
			/**		/6		%6		nc
			 * 0	0		0		65
			 * 1	0		1		75
			 * 2	0		2		85
			 * 3	0		3		95
			 * 4	0		4		105
			 * 5	0		5		115
			 * 6	1		0		125
			 * 7	1		1		115
			 * 8	1		2		105
			 * 9	1		3		95
			 * 10	1		4		85
			 * 11	1		5		75
			 * 12	2		0		65
			 * 13	2		1		75
			 * 14	2		2		85
			 * 15	2		3		95
			 */
			if( (position/6) %2 == 0) {
				newColor = highColor - (position%6) * difference;		// highColor ~ (highColor - 50) = 125 ~ 75
			} else {
				newColor = lowColor + (position%6) * difference;		// lowColor ~ (lowColor + 50) = 65 ~ 115
			}
			
			bg.setBackgroundColor(Color.rgb(newColor, newColor, newColor));
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
}
