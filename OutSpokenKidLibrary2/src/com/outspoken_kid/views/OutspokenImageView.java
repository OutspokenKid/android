package com.outspoken_kid.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.outspoken_kid.classes.BitmapContainer;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.StringUtils;

public class OutspokenImageView extends ImageView {

	private String url;
	private Bitmap bitmap;
	
	public OutspokenImageView(Context context) {
		this(context, null, 0);
	}
	
	public OutspokenImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public OutspokenImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void setImageUrl(String url) {

		if(url != null && !url.equals(this.url)) {
			clearImage();
		}
		
		this.url = url;
		downloadImage();
	}
	
	public void downloadImage() {
		
		try {
			if(!StringUtils.isEmpty(url)) {
				
				DownloadUtils.downloadBitmap(url,
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
									
									if (url.equals(OutspokenImageView.this.url)) {
										OutspokenImageView.this.bitmap = bitmap;
										setImageBitmap(bitmap);
									}
								} catch (Exception e) {
									LogUtils.trace(e);
								} catch (OutOfMemoryError oom) {
									LogUtils.trace(oom);
								}
							}
						});
			} else {
				clearImage();
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	public void clearImage() {
		
		try {
			BitmapContainer bc = BitmapContainer.getContainer(url, bitmap);
			
			if(bc != null) {
				bc.unreference();
			}
			
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
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
	}
}
