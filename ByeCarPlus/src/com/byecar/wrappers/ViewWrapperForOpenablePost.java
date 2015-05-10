package com.byecar.wrappers;

import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.MainActivity;
import com.byecar.byecarplus.R;
import com.byecar.classes.BCPDownloadUtils;
import com.byecar.models.Post;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class ViewWrapperForOpenablePost extends ViewWrapper {

	private Post openablePost;

	private TextView tvHeaderView;
	private View arrow;
	private ImageView ivImage;
	private TextView tvText;
	private View footerView;
	
	public ViewWrapperForOpenablePost(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			tvHeaderView = (TextView) row.findViewById(R.id.list_openable_post_tvHeaderView);
			arrow = row.findViewById(R.id.list_openable_post_arrow);
			ivImage = (ImageView) row.findViewById(R.id.list_openable_post_ivImage);
			tvText = (TextView) row.findViewById(R.id.list_openable_post_tvText);
			footerView = row.findViewById(R.id.list_openable_post_footerView);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			RelativeLayout.LayoutParams rp = null;
			int pw = ResizeUtils.getSpecificLength(20);
			int ph = ResizeUtils.getSpecificLength(30);
			
			rp = (RelativeLayout.LayoutParams) tvHeaderView.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(608);
			rp.height = ResizeUtils.getSpecificLength(68);
			rp.topMargin = ResizeUtils.getSpecificLength(26);
			tvHeaderView.setPadding(pw, 0, ResizeUtils.getSpecificLength(64), 0);
			
			rp = (RelativeLayout.LayoutParams) arrow.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(33);
			rp.height = ResizeUtils.getSpecificLength(33);
			rp.topMargin = ResizeUtils.getSpecificLength(18);
			rp.rightMargin = ResizeUtils.getSpecificLength(18);

			rp = (RelativeLayout.LayoutParams) tvText.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(608);
			tvText.setPadding(pw, ph, pw, ph);
			
			rp = (RelativeLayout.LayoutParams) footerView.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(608);
			rp.height = ResizeUtils.getSpecificLength(15);
			
			FontUtils.setFontSize(tvHeaderView, 18);
			FontUtils.setFontStyle(tvHeaderView, FontUtils.BOLD);
			FontUtils.setFontSize(tvText, 18);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			openablePost = (Post) baseModel;
			
			tvHeaderView.setText(openablePost.getTitle());
			tvText.setText(openablePost.getContent());
			
			if(openablePost.isOpened()) {
				tvText.setVisibility(View.VISIBLE);
				ivImage.setVisibility(View.VISIBLE);
				arrow.setBackgroundResource(R.drawable.detail_toggle_up);
				
				if(!StringUtils.isEmpty(openablePost.getRep_img_url())) {
					setImage(ivImage, openablePost.getRep_img_url());
				}
			} else {
				tvText.setVisibility(View.GONE);
				ivImage.setVisibility(View.GONE);
				arrow.setBackgroundResource(R.drawable.detail_toggle);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setListeners() {

		try {
			if(openablePost != null) {
				row.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						
						if(openablePost.isOpened()) {
							close();
						} else {
							open();
						}
					}
				});
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	@Override
	public void setUnusableView() {

	}
	
////////////////////Custom methods.

	public void open() {

		openablePost.setOpened(true);
		
		tvText.setVisibility(View.VISIBLE);
		arrow.setBackgroundResource(R.drawable.detail_toggle_up);
		
		if(!StringUtils.isEmpty(openablePost.getRep_img_url())) {
			ivImage.setVisibility(View.VISIBLE);
			setImage(ivImage, openablePost.getRep_img_url());
		} else {
			ivImage.setVisibility(View.GONE);
		}
	}
	
	public void close() {

		openablePost.setOpened(false);
		
		tvText.setVisibility(View.GONE);
		ivImage.setVisibility(View.GONE);
		arrow.setBackgroundResource(R.drawable.detail_toggle);
		
		ivImage.setImageDrawable(null);
		
		RelativeLayout.LayoutParams rp = (RelativeLayout.LayoutParams) ivImage.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(240);
	}

	@Override
	public void setImage(final ImageView ivImage, String url) {
		
		if(ivImage == null || url == null || url.length() == 0) {
			return;
		}
		
		ivImage.setTag(url);
		BCPDownloadUtils.downloadBitmap(url, new OnBitmapDownloadListener() {
			
			@Override
			public void onError(String url) {
			}
			
			@Override
			public void onCompleted(final String url, Bitmap bitmap) {

				try {
					String tag = ivImage.getTag().toString().replace("/src/", "/thumb/");
					
					LogUtils.log("###where.onCompleted.  "
							+ "\n url : " + url
							+ "\n tag : " + tag);
					
					//태그가 다른 경우 아무 것도 하지 않음.
					if(!StringUtils.isEmpty(tag)
							&& url.contains(tag)) {
						
						if(ivImage != null) {
							ivImage.setImageBitmap(bitmap);

							RelativeLayout.LayoutParams rp = (RelativeLayout.LayoutParams) ivImage.getLayoutParams();
							rp.width = ResizeUtils.getSpecificLength(608);
							rp.height = (int)(rp.width * ((float)bitmap.getHeight() / (float)bitmap.getWidth()));
							
							ivImage.setVisibility(View.VISIBLE);
							
							ivImage.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View view) {

									MainActivity.activity.showImageViewer(0, null, new String[]{url}, null);
								}
							});
						}
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}
			}
		}, 608);
	}
}
