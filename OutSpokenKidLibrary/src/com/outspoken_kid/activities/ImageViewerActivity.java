package com.outspoken_kid.activities;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.calciumion.widget.BasePagerAdapter;
import com.outspoken_kid.R;
import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.downloader.bitmapdownloader.BitmapDownloader;
import com.outspoken_kid.utils.ImageCacheUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.views.PinchImageView;
import com.outspoken_kid.views.PinchImageView.OnScrollChangedListener;

/**
 * @author HyungGunKim
 * @version 1.5
 * 
 * 1.5 - Set this Activity to abstract.
 * 1.4 - Change thumbnail's bg, add function to save image.
 * 1.3 - Fix the bugs(with thumbnails).
 * 1.2 - Fix the bugs.
 * 1.1 - ViewPager is adapted with thumbnails.
 */
public abstract class ImageViewerActivity extends RecyclingActivity {

	protected static final int MODE_NONE = 0;
	protected static final int MODE_DRAG = 1;
	
	protected int limitDist;
	
	protected Context context;
	protected String title;
	protected String[] imageUrls;
	protected String[] thumbnailUrls;
	protected ArrayList<ThumbnailSet> thumbnailSets = new ArrayList<ThumbnailSet>();
	protected ImageFrame[] imageFrames;
	protected ThumbnailFrame[] frames;
	protected int imageIndex;
	protected int thumbnailIndex;
	
	protected FrameLayout mainLayout;
	protected TextView tvTitle;
	protected Button btnSave;
	protected View cover;
	protected ViewPager viewPagerForImages;
	protected ViewPager viewPagerForThumbnails;
	protected View bgForPager;
	
	protected float x0, y0;
	protected int mode;
	protected boolean atStart;
	
	protected Animation fade_in, fade_out;
	
	protected abstract FrameLayout.LayoutParams getSaveButtonLayoutParams();
	protected abstract int getSaveButtonBackgroundResId();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imageviewer);
		context = this;
		
		bindViews();
		setVariables();
		createPage();

		setSizes();
		setListeners();
		
		downloadInfo();
	}
	
	@Override
	protected void bindViews() {
	
		mainLayout = (FrameLayout) findViewById(R.id.imageviewerActivity_mainLayout);
		tvTitle = (TextView) findViewById(R.id.imageviewerActivity_title);
		btnSave = (Button) findViewById(R.id.imageviewerActivity_btnSave);
		cover = findViewById(R.id.imageviewerActivity_cover);
	}

	@Override
	protected void setVariables() {

		if(getIntent() != null) {
			if(getIntent().hasExtra("title")) {
				title = getIntent().getStringExtra("title");
			}
			
			if(getIntent().hasExtra("imageUrls")) {
				imageUrls = getIntent().getStringArrayExtra("imageUrls");
				imageFrames = new ImageFrame[imageUrls.length];
			}
			
			if(getIntent().hasExtra("thumbnailUrls")) {
				thumbnailUrls = getIntent().getStringArrayExtra("thumbnailUrls");
				frames = new ThumbnailFrame[thumbnailUrls.length];
				
				if(thumbnailUrls.length > 0) {
					
					int madeCount = (thumbnailUrls.length - 1) / 4 + 1;
					
					for(int i=0; i<madeCount; i++) {
						ThumbnailSet ts = new ThumbnailSet();
						ts.firstIndex = i * 4;
						int stringCount = thumbnailUrls.length == 4? 4
											: (i==madeCount-1? (thumbnailUrls.length-1)%4+1 : 4);
						
						for(int j=0; j<stringCount; j++) {
							ts.imageUrls.add(thumbnailUrls[i*4 + j]);
						}
						thumbnailSets.add(ts);
					}
				}
			}

			imageIndex = getIntent().getIntExtra("index", 0);
		} else {
			finish();
		}
		
		FontInfo.setFontSize(tvTitle, 32);
		limitDist = ResizeUtils.getSpecificLength(30);
		downloadKey = "IMAGEVIEWERACTIVITY";
		
		fade_in = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
		fade_out = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
	}

	@Override
	protected void createPage() {

		viewPagerForImages = new ViewPager(this);
		viewPagerForImages.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		viewPagerForImages.setAdapter(new PagerAdapterForImage());
		mainLayout.addView(viewPagerForImages, 0);
		
		if(thumbnailUrls != null && thumbnailUrls.length > 1) {

			bgForPager = new View(context);
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 240, bgForPager, 2, Gravity.BOTTOM, null);
			bgForPager.setBackgroundColor(Color.argb(100, 0, 0, 0));
			mainLayout.addView(bgForPager);
			
			viewPagerForThumbnails = new ViewPager(this);
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 240, viewPagerForThumbnails, 2, Gravity.BOTTOM, null);
			viewPagerForThumbnails.setAdapter(new PagerAdapterForThumbnail());
			mainLayout.addView(viewPagerForThumbnails);
		}
	}

	@Override
	protected void setSizes() {

		if(!StringUtils.isEmpty(title)) {
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 90, tvTitle, 2, Gravity.TOP, null);
		}
		
		btnSave.setLayoutParams(getSaveButtonLayoutParams());
		btnSave.setBackgroundResource(getSaveButtonBackgroundResId());
	}

	@SuppressLint("ClickableViewAccessibility") @Override
	protected void setListeners() {

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
					
					if(distX > limitDist || distY > limitDist) {
						mode = MODE_DRAG;
					}
				} else if(event.getAction() == MotionEvent.ACTION_UP && mode == MODE_NONE){
					
					if((tvTitle != null && tvTitle.getVisibility() == View.VISIBLE)
							|| (viewPagerForThumbnails != null && viewPagerForThumbnails.getVisibility() == View.VISIBLE)
							|| (btnSave != null && btnSave.getVisibility() == View.VISIBLE)) {
						hideMenus();
					} else {
						showMenus();
					}
				}
				
				viewPagerForImages.dispatchTouchEvent(event);
				return true;
			}
		});
		
		viewPagerForImages.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				
				if(viewPagerForThumbnails != null) {
					viewPagerForThumbnails.setCurrentItem(position/4, true);
				}
				
				imageIndex = position;
				
				if(frames != null) {
					int size = frames.length;
					for(int i=0; i<size; i++) {
						if(frames[i] != null) {
							frames[i].update(imageIndex);
							
						}
					}
				}
				
				if(imageFrames != null && imageFrames.length > position) {
					imageFrames[position].getImageView().checkScrollState();
				}
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
				public void onPageSelected(int position) {
					
					if(atStart) {
						atStart = false;
					} else if(thumbnailIndex > position && imageIndex != position * 4 + 3) {
						viewPagerForImages.setCurrentItem(position * 4 + 3, true);
						
					} else if(thumbnailIndex < position && imageIndex != position * 4) {
						viewPagerForImages.setCurrentItem(position * 4, true);
					}
					
					thumbnailIndex = position;
				}
				
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
				}
				
				@Override
				public void onPageScrollStateChanged(int arg0) {
				}
			});
		}
		
		if(btnSave != null) {
			
			btnSave.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					saveImage();
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
		
		if(tvTitle != null && !StringUtils.isEmpty(title)) {
			setTitle(title);
		}

		mainLayout.postDelayed(new Runnable() {
			
			@Override
			public void run() {

				if(imageIndex != 0) {
					atStart = true;
					LogUtils.log("ImageViewerActivity.setPage.  position : " + imageIndex);
					
					if(viewPagerForImages != null 
							&& viewPagerForImages.getAdapter() != null
							&& viewPagerForImages.getAdapter().getCount() > imageIndex) {
						viewPagerForImages.setCurrentItem(imageIndex);
					}

				} else if(frames != null && frames.length > 1 && frames[0] != null){
					frames[0].update(0);
				}
			}
		}, 100);
	}

	@Override
	public void finish() {

		super.finish();
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
		
		if(btnSave.getVisibility() != View.VISIBLE) {
			btnSave.setVisibility(View.VISIBLE);
			btnSave.startAnimation(fade_in);
		}
		
		if(viewPagerForThumbnails != null && viewPagerForThumbnails.getVisibility() != View.VISIBLE) {
			viewPagerForThumbnails.setVisibility(View.VISIBLE);
			viewPagerForThumbnails.startAnimation(fade_in);
			
			bgForPager.setVisibility(View.VISIBLE);
			bgForPager.startAnimation(fade_in);
		}
	}
	
	public void hideMenus() {
		
		if(tvTitle != null && tvTitle.getVisibility() == View.VISIBLE) {
			tvTitle.setVisibility(View.INVISIBLE);
			tvTitle.startAnimation(fade_out);
		}
		
		if(btnSave.getVisibility() == View.VISIBLE) {
			btnSave.setVisibility(View.INVISIBLE);
			btnSave.startAnimation(fade_out);
		}
		
		if(viewPagerForThumbnails != null && viewPagerForThumbnails.getVisibility() == View.VISIBLE) {
			viewPagerForThumbnails.setVisibility(View.INVISIBLE);
			viewPagerForThumbnails.startAnimation(fade_out);
			
			bgForPager.setVisibility(View.INVISIBLE);
			bgForPager.startAnimation(fade_out);
		}
	}

	public void saveImage() {
		
		if(imageFrames != null && imageFrames[imageIndex] != null) {
			Bitmap bitmap = imageFrames[imageIndex].getBitmap();
			
			if(bitmap != null) {
				String imageTitle = "golf_n";
				
				if(title != null) {
					imageTitle += "_" + title;
				}
				
				ImageCacheUtils.saveImage(context, bitmap, imageTitle + "_" + imageIndex, false);
			}
		}
	}

/////////////////////////////////////////// Classes.
	
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

			ImageFrame imageFrame = new ImageFrame(context, imageUrls[position]);
			imageFrame.downloadImage();
			
			if(imageFrames != null) {
				imageFrames[position] = imageFrame;
			}
			
			container.addView(imageFrame);
			return imageFrame;
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
				
				ThumbnailFrame tf = new ThumbnailFrame(context, ts.imageUrls.get(i), ts.firstIndex + i);
				frames[INDEX] = tf;
				tf.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {

						viewPagerForImages.setCurrentItem(INDEX);
					}
				});
				tf.download();
				linear.addView(tf);
				
				if(frames != null && frames[INDEX] != null) {
					frames[INDEX] = tf;
				}
			}
			
			return linear;
		}
	}

	public class ImageFrame extends FrameLayout {

		protected String url;
		
		protected ProgressBar progress;
		protected PinchImageView imageView;
		
		public ImageFrame(Context context, String url) {
			super(context);
			this.url = url;
			init();
		}
		
		public void init() {
			this.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			
			progress = new ProgressBar(context);
			ResizeUtils.viewResize(60, 60, progress, 2, Gravity.CENTER, null);
			this.addView(progress);
			
			imageView = new PinchImageView(context);
			imageView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			imageView.setOnScrollChangedListener(new OnScrollChangedListener() {
				
				@Override
				public void onScrollChanged(boolean needLockParentTouch) {

					viewPagerForImages.requestDisallowInterceptTouchEvent(needLockParentTouch);
				}
			});
			this.addView(imageView);
		}
		
		public void downloadImage() {

			BitmapDownloader.downloadImage(url, downloadKey, imageView, false, true);
		}
		
		public Bitmap getBitmap() {
			
			try {
				return ((BitmapDrawable)imageView.getDrawable()).getBitmap();
			} catch (Exception e) {
				e.printStackTrace();
			} catch (Error e) {
				e.printStackTrace();
			}
			
			return null;
		}
	
		public PinchImageView getImageView() {
			
			return imageView;
		}
	}
	
	public class ThumbnailFrame extends FrameLayout {

		protected String url;
		protected int index;
		
		protected View bg;
		protected ProgressBar progress;
		protected ImageView imageView;
		
		public ThumbnailFrame(Context context, String url, int index) {
			super(context);
			this.url = url;
			this.index = index;
			init();
		}
		
		public void init() {
			
			ResizeUtils.viewResize(132, 172, this, 1, Gravity.CENTER_VERTICAL, new int[]{14, 14, 14, 14});
			
			bg = new View(getContext());
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, bg, 2, Gravity.CENTER, null);
			bg.setBackgroundColor(Color.WHITE);
			bg.setVisibility(View.INVISIBLE);
			this.addView(bg);
			
			progress = new ProgressBar(context);
			ResizeUtils.viewResize(20, 20, progress, 2, Gravity.CENTER, null);
			this.addView(progress);
			
			imageView = new ImageView(context);
			ResizeUtils.viewResize(120, 160, imageView, 2, Gravity.CENTER, new int[]{6, 6, 6, 6});
			imageView.setScaleType(ScaleType.CENTER_CROP);
			this.addView(imageView);
		}
		
		public void download() {
			
			BitmapDownloader.downloadImage(url, downloadKey, imageView, false, true);
		}

		public void hideBg() {
			
			if(bg.getVisibility() == View.VISIBLE) {
				bg.setVisibility(View.INVISIBLE);
			}
		}
		
		public void showBg() {
			
			if(bg.getVisibility() != View.VISIBLE) {
				bg.setVisibility(View.VISIBLE);
			}
		}
	
		public void update(int currentIndex) {
			
			if(currentIndex == index) {
				showBg();
			} else {
				hideBg();
			}
		}
	}
	
	public class ThumbnailSet {

		public int firstIndex;
		public ArrayList<String> imageUrls = new ArrayList<String>();
	}
}
