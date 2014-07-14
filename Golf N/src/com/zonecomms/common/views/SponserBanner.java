package com.zonecomms.common.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.outspoken_kid.downloader.bitmapdownloader.BitmapDownloader;
import com.outspoken_kid.downloader.bitmapdownloader.BitmapDownloader.OnCompletedListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.zonecomms.common.models.Banner;
import com.zonecomms.golfn.R;
import com.zonecomms.golfn.classes.ApplicationManager;

@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class SponserBanner extends FrameLayout {

	private static int BANNER_DELAY = 4000;
	
	private Banner[] banners;
	private Bitmap[] bitmaps;
	private ImageView ivImage;
	private View close;
	private TranslateAnimation taIn, taOut;
	private int bannerIndex;
	private AsyncPlayBanner playTask;

	public SponserBanner(Context context) {
		super(context);
	}
	
	public SponserBanner(Context context, Banner[] banners) {
		super(context);
		this.banners = banners;
		
		if(banners != null && banners.length != 0) {
			bitmaps = new Bitmap[banners.length];
		}
		
		init();
	}

	public void init() {

		if(this.getChildCount() == 0) {
			this.setClickable(true);
			this.setBackgroundColor(Color.WHITE);
			this.setVisibility(View.INVISIBLE);
			
			ivImage = new ImageView(getContext());
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, ivImage, 2, 0, null);
			ivImage.setScaleType(ScaleType.CENTER_CROP);
			ivImage.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if(banners != null && banners.length > 0 && banners.length > bannerIndex 
							&& !StringUtils.isEmpty(banners[bannerIndex].getTarget_link())) {
						ApplicationManager.getInstance().getActivity().showDeviceBrowser(banners[bannerIndex].getTarget_link());
					}
				}
			});
			this.addView(ivImage);
			
			close = new View(getContext());
			ResizeUtils.viewResize(60, 60, close, 2, Gravity.TOP|Gravity.RIGHT, null);
			close.setBackgroundResource(R.drawable.img_closebanner);
			this.addView(close);
			
			close.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					hideBanner();
				}
			});
		}
	}
	
	public void downloadBanner() {
		
		if(banners == null || banners.length == 0) {
			return;
		}
		
		try {
			bannerIndex = (bannerIndex + 1) % banners.length;
			
			if(bitmaps[bannerIndex] != null) {
				showBanner(bannerIndex);
				return;
			}
			
			final int bitmapIndex = bannerIndex;
			
			BitmapDownloader.OnCompletedListener ocl = new OnCompletedListener() {
				
				@Override
				public void onErrorRaised(String url, Exception e) {
					hideBanner();
				}
				
				@Override
				public void onCompleted(String url, Bitmap bitmap, ImageView view) {

					try {
						if(url == null || bitmap == null || bitmap.isRecycled() || view == null 
								|| view.getTag() == null || !url.equals(view.getTag().toString())) {
							return;
						}

						bitmaps[bitmapIndex] = bitmap;
						showBanner(bitmapIndex);
					} catch(Exception e) {
						LogUtils.trace(e);
						hideBanner();
					}
				}
			};

			ivImage.setTag(banners[bannerIndex].getImg_url());
			BitmapDownloader.downloadImmediately(banners[bannerIndex].getImg_url(), null, ocl, null, ivImage, true);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void showBanner(int bannerIndex) {
		
		ivImage.setImageBitmap(bitmaps[bannerIndex]);

		if(getVisibility() != View.VISIBLE) {

			if(taIn == null) {
				taIn = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0, 
						TranslateAnimation.RELATIVE_TO_SELF, 0,
						TranslateAnimation.RELATIVE_TO_SELF, 1,
						TranslateAnimation.RELATIVE_TO_SELF, 0);
				taIn.setDuration(300);
				taIn.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						playBanner();
					}
				});
			}
			post(new Runnable() {
				
				@Override
				public void run() {
					setVisibility(View.VISIBLE);			
					startAnimation(taIn);
				}
			});
		} else {
			playBanner();
		}
	}
	
	public void hideBanner() {

		if(banners == null || banners.length == 0) {
			return;
		}
		
		pauseBanner();
		
		try {
			if(this.getVisibility() == View.VISIBLE) {
				
				if(taOut == null) {
					taOut = new TranslateAnimation(0, 0, 0, 1);
					taOut = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0, 
							TranslateAnimation.RELATIVE_TO_SELF, 0,
							TranslateAnimation.RELATIVE_TO_SELF, 0,
							TranslateAnimation.RELATIVE_TO_SELF, 1);
					taOut.setDuration(300); 
				}
				post(new Runnable() {
					
					@Override
					public void run() {
						setVisibility(View.INVISIBLE);
						startAnimation(taOut);
					}
				});
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	public void playBanner() {

		if(banners.length == 0) {
			return;
		}
		
		pauseBanner();
		
		try {
			if(banners.length > 1) {
				playTask = new AsyncPlayBanner();
				
				if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
					playTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				} else {
					playTask.execute();
				}
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void pauseBanner() {

		if(banners.length == 0) {
			return;
		}
		
		if(playTask != null) {
			playTask.cancel(true);
		}
	}
	
//////////////////// Classes.
	
	public class AsyncPlayBanner extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			
			try {
				Thread.sleep(BANNER_DELAY);
			} catch(Exception e) {
				LogUtils.trace(e);
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			
			downloadBanner();
		}
	}
}