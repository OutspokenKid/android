package com.byecar.byecarplus.classes;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.byecar.byecarplus.R;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;

public class ImagePagerAdapter extends PagerAdapter {

	private Context context;
	private ArrayList<String> images = new ArrayList<String>();
	private OnPagerItemClickedListener OnPagerItemClickedListener;
	public ImagePagerAdapter() {
		
	}
	
	public ImagePagerAdapter(Context context) {
		
		this.context = context;
	}
	
	public void setArrayList(ArrayList<String> images) {
		
		this.images = images;
	}
	
	@Override
	public int getCount() {

		return images.size();
	}

	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		
		final ImageView ivImage = new ImageView(context);
		ivImage.setScaleType(ScaleType.CENTER_CROP);
		ivImage.setBackgroundResource(R.drawable.main_auction_default);
		container.addView(ivImage);
		
		String url = images.get(position);
		ivImage.setTag(url);
		DownloadUtils.downloadBitmap(url, new OnBitmapDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("ImagePagerAdapter.onError." + "\nurl : " + url);
			}

			@Override
			public void onCompleted(String url, Bitmap bitmap) {

				try {
					LogUtils.log("ImagePagerAdapter.onCompleted." + "\nurl : " + url);
					
					if(ivImage != null && bitmap != null && !bitmap.isRecycled()) {
						ivImage.setImageBitmap(bitmap);
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});

		if(OnPagerItemClickedListener != null) {
			ivImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					
					OnPagerItemClickedListener.onPagerItemClicked(position);
				}
			});
		}
		
		return ivImage;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {

		try {
			View v = (View) object;
			container.removeView(v);
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {

		return arg0 == arg1;
	}

	public void setOnPagerItemClickedListener(OnPagerItemClickedListener OnPagerItemClickedListener) {
		
		this.OnPagerItemClickedListener = OnPagerItemClickedListener;
	}
	
//////////////////// Interfaces.
	
	public interface OnPagerItemClickedListener {
		
		public void onPagerItemClicked(int position);
	}
}