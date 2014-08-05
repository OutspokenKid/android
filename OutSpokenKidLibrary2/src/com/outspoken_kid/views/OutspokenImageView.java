package com.outspoken_kid.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.StringUtils;

public class OutspokenImageView extends ImageView {

	private String imageUrl;
	
	public OutspokenImageView(Context context) {
		this(context, null, 0);
	}
	
	public OutspokenImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public OutspokenImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setImageUrl(String imageUrl) {
		
		this.imageUrl = imageUrl;
		downloadImage();
	}
	
	public void downloadImage() {
		
		try {
			if(!StringUtils.isEmpty(imageUrl)) {
			
				final String IMAGE_URL = imageUrl;
				
				DownloadUtils.downloadBitmap(imageUrl,
						new OnBitmapDownloadListener() {

							@Override
							public void onError(String url) {

//								LogUtils.log("OutspokenImageView.onError." + "\nurl : " + url);
							}

							@Override
							public void onCompleted(String url, Bitmap bitmap) {

								try {
//									LogUtils.log("OutspokenImageView.onCompleted." +
//											"\nurl : " + url +
//											"\ntag : " + getTag());
									
									if (url != null
											&& url.equals(IMAGE_URL)) {
										setImageBitmap(bitmap);
									}
								} catch (Exception e) {
									LogUtils.trace(e);
								} catch (OutOfMemoryError oom) {
									LogUtils.trace(oom);
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

	public void clearImage() {
		
		try {
			Drawable d = getDrawable();
	        setImageDrawable(null);
	        setImageBitmap(null);
	        
	        if (d != null) {
	            d.setCallback(null);
	        }
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		
		if(visibility == View.VISIBLE) {
			downloadImage();
		} else {
            clearImage();
		}
	}
}
