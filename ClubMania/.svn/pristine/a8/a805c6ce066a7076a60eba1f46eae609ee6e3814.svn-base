package com.zonecomms.clubmania;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.calciumion.widget.BasePagerAdapter;
import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.classes.RecyclingActivity;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.views.PinchImageView;
import com.zonecomms.common.utils.ImageDownloadUtils;

/**
 * @author HyungGunKim
 * @version 1.3
 * 
 * 1.3 - Fix the bugs(with thumbnails).
 * 1.2 - Fix the bugs.
 * 1.1 - ViewPager is adapted with thumbnails.
 */
public class ImageViewerActivity extends RecyclingActivity {

	private static final int MODE_NONE = 0;
	private static final int MODE_DRAG = 1;
	
	private Context context;
	private String title;
	private String[] imageUrls;
	private String[] thumbnailUrls;
	private ArrayList<ThumbnailSet> thumbnailSets = new ArrayList<ThumbnailSet>();
	private int imageIndex;
	private int thumbnailIndex;
	
	private FrameLayout mainLayout;
	private TextView tvTitle;
	private ViewPager viewPagerForImages;
	private ViewPager viewPagerForThumbnails;
	private View bgForPager;
	private View bg;
	
	private float x0, y0;
	private int mode;
	
	private Animation fade_in, fade_out;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imageviewer);
		context = this;
		
		bindViews();
		setVariables();
		createPage();

		setSize();
		setListener();
		
		downloadInfo();
	}
	
	@Override
	protected void bindViews() {
	
		mainLayout = (FrameLayout) findViewById(R.id.imageviewerActivity_mainLayout);
		tvTitle = (TextView) findViewById(R.id.imageviewerActivity_title);
	}

	@Override
	protected void setVariables() {

		if(getIntent() != null) {
			if(getIntent().hasExtra("title")) {
				title = getIntent().getStringExtra("title");
			}
			
			if(getIntent().hasExtra("imageUrls")) {
				imageUrls = getIntent().getStringArrayExtra("imageUrls");
			}
			
			if(getIntent().hasExtra("thumbnailUrls")) {
				thumbnailUrls = getIntent().getStringArrayExtra("thumbnailUrls");

				LogUtils.log("size : " + thumbnailUrls.length);
				
				if(thumbnailUrls.length > 0) {
					
					int madeCount = (thumbnailUrls.length - 1) / 4 + 1;
					LogUtils.log("madeCount : " + madeCount);
					
					for(int i=0; i<madeCount; i++) {
						ThumbnailSet ts = new ThumbnailSet();
						ts.firstIndex = i * 4;
						int stringCount = thumbnailUrls.length == 4? 4
											: (i==madeCount-1? (thumbnailUrls.length-1)%4+1 : 4);
						
						LogUtils.log("stringCount : " + stringCount);
						
						for(int j=0; j<stringCount; j++) {
							LogUtils.log(i + " : " + thumbnailUrls[i*4 + j]);
							ts.imageUrls.add(thumbnailUrls[i*4 + j]);
						}
						thumbnailSets.add(ts);
					}
				}
			}
		} else {
			finish();
		}
		
		FontInfo.setFontSize(tvTitle, 32);
		
		downloadKey = "IMAGEVIEWERACTIVITY";
		
		fade_in = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
		fade_out = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
	}

	@Override
	protected void createPage() {

		View cover = new View(this);
		cover.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		cover.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if(event.getAction() == MotionEvent.ACTION_DOWN) {
					mode = MODE_NONE;
					x0 = event.getX();
					y0 = event.getY();
					
				} else if(event.getAction() == MotionEvent.ACTION_MOVE) {
					
					float distX = Math.abs(x0 - event.getX());
					float distY = Math.abs(y0 - event.getY());
					int limitDist = ResizeUtils.getSpecificLength(40);
					
					if(distX > limitDist || distY > limitDist) {
						mode = MODE_DRAG;
					}
				} else if(event.getAction() == MotionEvent.ACTION_UP && mode == MODE_NONE){
					
					if((tvTitle != null && tvTitle.getVisibility() == View.VISIBLE)
							|| (viewPagerForThumbnails != null && viewPagerForThumbnails.getVisibility() == View.VISIBLE)) {
						hideMenus();
					} else {
						showMenus();
					}
				}
				
				viewPagerForImages.dispatchTouchEvent(event);
				return true;
			}
		});
		mainLayout.addView(cover);
		
		viewPagerForImages = new ViewPager(this);
		viewPagerForImages.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		viewPagerForImages.setAdapter(new PagerAdapterForImage());
		mainLayout.addView(viewPagerForImages, 0);
		
		if(thumbnailUrls != null && thumbnailUrls.length > 1) {

			bgForPager = new View(context);
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 240, bgForPager, 2, Gravity.BOTTOM, null);
			bgForPager.setBackgroundColor(Color.argb(100, 0, 0, 0));
			mainLayout.addView(bgForPager);

			bg = new View(context);
			ResizeUtils.viewResize(128, 168, bg, 2, Gravity.BOTTOM, new int[]{16, 0, 0, 38});
			bg.setBackgroundColor(Color.RED);
			mainLayout.addView(bg);
			
			viewPagerForThumbnails = new ViewPager(this);
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 240, viewPagerForThumbnails, 2, Gravity.BOTTOM, null);
			viewPagerForThumbnails.setAdapter(new PagerAdapterForThumbnail());
			mainLayout.addView(viewPagerForThumbnails);
		}
	}

	@Override
	protected void setSize() {

		if(!TextUtils.isEmpty(title)) {
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 90, tvTitle, 2, Gravity.TOP, null);
		}
	}

	@Override
	protected void setListener() {

		viewPagerForImages.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				
				if(viewPagerForThumbnails != null) {
					viewPagerForThumbnails.setCurrentItem(position/4, true);
				}
				
				imageIndex = position;
				checkBg();
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
		if(thumbnailUrls != null && thumbnailUrls.length > 1) {
			viewPagerForThumbnails.setOnPageChangeListener(new OnPageChangeListener() {
				
				@Override
				public void onPageSelected(int arg0) {
					
					if(thumbnailIndex > arg0 && imageIndex != arg0 * 4 + 3) {
						viewPagerForImages.setCurrentItem(arg0 * 4 + 3, true);
						
					} else if(thumbnailIndex < arg0 && imageIndex != arg0 * 4) {
						viewPagerForImages.setCurrentItem(arg0 * 4, true);
					}
					
					thumbnailIndex = arg0;
				}
				
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}
				
				@Override
				public void onPageScrollStateChanged(int arg0) {
			
					if(viewPagerForThumbnails != null && viewPagerForThumbnails.getVisibility() == View.VISIBLE) {
						if(arg0 == ViewPager.SCROLL_STATE_DRAGGING) {
							bg.setVisibility(View.INVISIBLE);
						} else {
							bg.setVisibility(View.VISIBLE);
						}
					}
				}
			});
		}
	}

	@Override
	protected void downloadInfo() {

		setPage();
	}

	@Override
	protected void setPage() {
		
		if(tvTitle != null && !TextUtils.isEmpty(title)) {
			setTitle(title);
		}
	}

	@Override
	public void finish() {
		
		finishWithoutAnim();
		overridePendingTransition(android.R.anim.fade_in, R.anim.slide_out_to_bottom);
	}
	
	public void setTitle(String title) {
		
		tvTitle.setText(title);
	}

	@Override
	protected int getContentViewId() {

		return R.id.imageviewerActivity_mainLayout;
	}

	public void showMenus() {
		
		if(tvTitle != null && tvTitle.getVisibility() != View.VISIBLE) {
			tvTitle.setVisibility(View.VISIBLE);
			tvTitle.startAnimation(fade_in);
		}
		
		if(viewPagerForThumbnails != null && viewPagerForThumbnails.getVisibility() != View.VISIBLE) {
			viewPagerForThumbnails.setVisibility(View.VISIBLE);
			viewPagerForThumbnails.startAnimation(fade_in);
			
			bg.setVisibility(View.VISIBLE);
			bg.startAnimation(fade_in);
			
			bgForPager.setVisibility(View.VISIBLE);
			bgForPager.startAnimation(fade_in);
		}
	}
	
	public void hideMenus() {
		
		if(tvTitle != null && tvTitle.getVisibility() == View.VISIBLE) {
			tvTitle.setVisibility(View.INVISIBLE);
			tvTitle.startAnimation(fade_out);
		}
		
		if(viewPagerForThumbnails != null && viewPagerForThumbnails.getVisibility() == View.VISIBLE) {
			viewPagerForThumbnails.setVisibility(View.INVISIBLE);
			viewPagerForThumbnails.startAnimation(fade_out);
			
			bg.setVisibility(View.INVISIBLE);
			bg.startAnimation(fade_out);
			
			bgForPager.setVisibility(View.INVISIBLE);
			bgForPager.startAnimation(fade_out);
		}
	}
	
	public void checkBg() {
		
		ResizeUtils.setMargin(bg, new int[]{16 + (imageIndex % 4 * 160), 0, 0, 38});
	}
	
	public class PagerAdapterForImage extends PagerAdapter {
		
		@Override
		public int getCount() {

			if(imageUrls != null) {
				return imageUrls.length;
			} else {
				return 0;
			}
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
		
			FrameLayout frame = new FrameLayout(context);
			frame.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			container.addView(frame);
			
			ProgressBar progress = new ProgressBar(context);
			ResizeUtils.viewResize(60, 60, progress, 2, Gravity.CENTER, null);
			frame.addView(progress);
			
			PinchImageView imageView = new PinchImageView(context);
			imageView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			frame.addView(imageView);
			
			ImageDownloadUtils.downloadImage(imageUrls[position], downloadKey, imageView, 
					ResizeUtils.getScreenWidth());
			
			return frame;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

			container.removeView((View)object);
		}
		
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return arg0 == arg1;
		}
	}
	
	public class PagerAdapterForThumbnail extends BasePagerAdapter {
		
		@Override
		public int getCount() {
			
			if(thumbnailUrls != null && thumbnailUrls.length > 1) {
				return (thumbnailUrls.length-1)/4 + 1;
			} else {
				return 0;
			}
		}

		@Override
		protected Object getItem(int position) {
			
			return thumbnailSets.get(position);
		}

		@Override
		protected View getView(Object object, View convertView, ViewGroup parent) {
			
			LinearLayout linear = new LinearLayout(context);
			linear.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			linear.setOrientation(LinearLayout.HORIZONTAL);
			
			final ThumbnailSet ts = (ThumbnailSet)object;
			
			int size = ts.imageUrls.size();
			for(int i=0; i<size; i++) {
				final int INDEX = ts.firstIndex + i;
				
				FrameLayout frame = new FrameLayout(context);
				ResizeUtils.viewResize(160, 200, frame, 1, Gravity.CENTER_VERTICAL, null);
				frame.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {

						viewPagerForImages.setCurrentItem(INDEX);
					}
				});
				linear.addView(frame);
				
				ProgressBar progress = new ProgressBar(context);
				ResizeUtils.viewResize(20, 20, progress, 2, Gravity.CENTER, null);
				frame.addView(progress);
				
				ImageView imageView = new ImageView(context);
				ResizeUtils.viewResize(120, 160, imageView, 2, Gravity.CENTER, null);
				imageView.setScaleType(ScaleType.CENTER_CROP);
				frame.addView(imageView);

				LogUtils.log("ImageViewer.thumb " + i + " : " + ts.imageUrls.get(i));
				ImageDownloadUtils.downloadImage(ts.imageUrls.get(i), downloadKey, imageView, 160);
			}
			
			return linear;
		}
	}
	
	public class ThumbnailSet {

		public int firstIndex;
		public ArrayList<String> imageUrls = new ArrayList<String>();
	}
}
