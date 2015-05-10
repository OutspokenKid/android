package com.byecar.wrappers;

import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.MainActivity;
import com.byecar.byecarplus.R;
import com.byecar.models.Post;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class ViewWrapperForVideo extends ViewWrapper {

	private Post post;
	
	private ImageView ivVideo;
	private View cover;
	private Button btnPlay;
	private TextView tvVideoTitle;
	
	public ViewWrapperForVideo(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		ivVideo = (ImageView) row.findViewById(R.id.list_video_ivVideo);
		cover = row.findViewById(R.id.list_video_cover);
		btnPlay = (Button) row.findViewById(R.id.list_video_btnPlay);
		tvVideoTitle = (TextView) row.findViewById(R.id.list_video_tvVideoTitle);
	}

	@Override
	public void setSizes() {

		row.setLayoutParams(new AbsListView.LayoutParams(
				LayoutParams.MATCH_PARENT, ResizeUtils.getSpecificLength(249)));

		RelativeLayout.LayoutParams rp = null;

		//ivVideo.
		rp = (RelativeLayout.LayoutParams) ivVideo.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(249);
		
		//cover.
		rp = (RelativeLayout.LayoutParams) cover.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(249);
		
		//btnPlay.
		rp = (RelativeLayout.LayoutParams) btnPlay.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(84);
		rp.height = ResizeUtils.getSpecificLength(85);
		rp.topMargin = ResizeUtils.getSpecificLength(60);
		
		//tvVideoTitle.
		rp = (RelativeLayout.LayoutParams) tvVideoTitle.getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(36);
		tvVideoTitle.setMaxWidth(ResizeUtils.getSpecificLength(304));
	}

	@Override
	public void setValues(BaseModel baseModel) {

		if(baseModel instanceof Post) {
			
			this.post = (Post) baseModel;
			
			String url = "http://img.youtube.com/vi/" + post.getYoutube_id() + "/0.jpg";
			ivVideo.setTag(url);
			DownloadUtils.downloadBitmap(url, new OnBitmapDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("ViewWrapperForVideo.onError." + "\nurl : " + url);

					ivVideo.setImageDrawable(null);
					btnPlay.setVisibility(View.INVISIBLE);
					tvVideoTitle.setText(null);
				}

				@Override
				public void onCompleted(String url, Bitmap bitmap) {

					try {
						LogUtils.log("ViewWrapperForVideo.onCompleted." + "\nurl : " + url);
						
						if(ivVideo != null && bitmap != null && !bitmap.isRecycled()) {
							ivVideo.setImageBitmap(bitmap);
						}
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
					}
				}
			});
			
			btnPlay.setVisibility(View.VISIBLE);
			tvVideoTitle.setText(post.getContent());
		}
	}

	@Override
	public void setListeners() {

		if(!StringUtils.isEmpty(post.getYoutube_id())) {
			btnPlay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					try {
						MainActivity.activity.showVideo(post.getYoutube_id());
					} catch (Exception e) {
						LogUtils.trace(e);
					}
				}
			});
		}
	}

	@Override
	public void setUnusableView() {

	}
}
