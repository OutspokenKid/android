package com.zonecomms.common.wrappers;

import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.outspoken_kid.classes.ApplicationManager;
import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.zonecomms.clubmania.R;
import com.zonecomms.clubmania.YoutubePlayerActivity;
import com.zonecomms.common.models.BaseModel;
import com.zonecomms.common.models.Link;
import com.zonecomms.common.utils.ImageDownloadUtils;
import com.zonecomms.common.wrapperviews.WrapperView;

public class ViewWrapperForVideo extends ViewWrapper {

	private ImageView ivThumbnail;
	private TextView tvTitle;
	private Link link;

	public ViewWrapperForVideo(WrapperView row, int itemCode) {
		super(row, itemCode);
		
		bindViews();
		setSize();
	}

	@Override
	public void bindViews() {

		ivThumbnail = (ImageView) row.findViewById(R.id.list_video_ivThumbnail);
		tvTitle = (TextView) row.findViewById(R.id.list_video_tvTitle);
	}

	@Override
	public void setSize() {
		
		//624, 468
		int p = ResizeUtils.getSpecificLength(8);
		AbsListView.LayoutParams ap = new AbsListView.LayoutParams(ResizeUtils.getSpecificLength(640), 
				ResizeUtils.getSpecificLength(476));
		row.setLayoutParams(ap);
		row.setPadding(p, p, p, 0);
		
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 100, row.findViewById(R.id.list_video_bg), 2, 0, null);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 70, tvTitle, 2, 0, null, new int[]{20, 0, 20, 0});
		ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, 30, row.findViewById(R.id.list_video_tvYoutube), 2,
				Gravity.RIGHT|Gravity.TOP, new int[]{0, 60, 20, 0});

		FontInfo.setFontSize(tvTitle, 28);
		FontInfo.setFontStyle(tvTitle, FontInfo.BOLD);
		
		FontInfo.setFontSize((TextView)row.findViewById(R.id.list_video_tvYoutube), 20);
	}

	@Override
	public void setValues(BaseModel baseModel) {

		if(baseModel != null && baseModel instanceof Link) {

			link = (Link) baseModel;
			
			if(!TextUtils.isEmpty(link.getTitle())) {
				tvTitle.setText(link.getTitle());
			}
			
			if(!TextUtils.isEmpty(link.getLink_data())) {
				String key = ApplicationManager.getDownloadKeyFromTopFragment();
				String url = "http://img.youtube.com/vi/" + link.getLink_data() + "/0.jpg";
				ImageDownloadUtils.downloadImage(url, key, ivThumbnail, 468);
			}
		}
	}

	@Override
	public void setListener() {

		if(link != null) {
			row.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					showLink(link.getLink_data());
				}
			});
		}
	}
	
	public ImageView getThumbnailImageView() {
		
		return ivThumbnail;
	}

	@Override
	public void setUnusableView() {
		// TODO Auto-generated method stub
	}

	public void showLink(String videoUrl) {
		
		if(!StringUtils.isEmpty(videoUrl)) {
			try {
				Intent intent = new Intent(ApplicationManager.getInstance().getMainActivity(), YoutubePlayerActivity.class);
				intent.putExtra("videoId", videoUrl);
				ApplicationManager.getInstance().getMainActivity().startActivity(intent);
				
//				Uri uri = Uri.parse("http://www.youtube.com/watch?v=" + videoUrl);
//				Intent i=new Intent(Intent.ACTION_VIEW, uri); 
//				ZonecommsApplication.getActivity().startActivity(i);
			} catch(Exception e) {
				ToastUtils.showToast(R.string.failToShowVideo);
			}
		} else {
			ToastUtils.showToast(R.string.failToShowVideo);
		}
	}
}
