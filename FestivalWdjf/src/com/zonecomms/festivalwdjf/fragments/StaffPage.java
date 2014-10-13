package com.zonecomms.festivalwdjf.fragments;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.downloader.bitmapdownloader.BitmapDownloader;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.views.GestureSlidingLayout;
import com.triposo.barone.EllipsizingTextView;
import com.zonecomms.common.models.Staff;
import com.zonecomms.common.views.StaffHeaderView;
import com.zonecomms.festivalwdjf.R;
import com.zonecomms.festivalwdjf.classes.BaseFragment;

public class StaffPage extends BaseFragment {

	private static final int MODE_NONE = 0;
	private static final int MODE_DRAG = 1;
	
	private Staff staff;

	private int imageLength;
	private int progressLength;
	private int padding;
	private int mode;
	private int downloadCount;
	
	private LinearLayout mainLayout;
	private StaffHeaderView header;
	private ViewPager pager;
	private EllipsizingTextView tvText;
	private ArrayList<String> urls = new ArrayList<String>();
	private Bitmap[] bitmaps;
	private FrameLayout[] frames;
	
	@Override
	protected void bindViews() {

		mainLayout = (LinearLayout) mThisView.findViewById(R.id.staffPage_mainLayout);
	}

	@Override
	protected void setVariables() {

		imageLength = ResizeUtils.getSpecificLength(480);
		progressLength = ResizeUtils.getSpecificLength(60);
		padding = (ResizeUtils.getScreenWidth() - imageLength)/2;
	}

	@Override
	protected void createPage() {
		
		int p = ResizeUtils.getSpecificLength(8);
		
		//Add headerView.
		header = new StaffHeaderView(mContext);
		mainLayout.addView(header);
		
		//Add viewPager.
		pager = new ViewPager(mContext);
		pager.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, imageLength));
		pager.setAdapter(new StaffAdapter());
		pager.setPageMargin(-padding/2*3);
		pager.setOnTouchListener(new OnTouchListener() {

			private final int limitDist = ResizeUtils.getSpecificLength(40);
			private float distX;
			private float distY;
			private float x0, y0;
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				switch(event.getAction()) {
				
				case MotionEvent.ACTION_DOWN:
					mode = MODE_NONE;
					x0 = event.getX();
					y0 = event.getY();
					GestureSlidingLayout.setScrollLock(true);
					pager.requestDisallowInterceptTouchEvent(true);
					break;
					
				case MotionEvent.ACTION_MOVE:
					distX = Math.abs(x0 - event.getX());
					distY = Math.abs(y0 - event.getY());
					
					if(distX > limitDist) {
						mode = MODE_DRAG;
					} else if(mode == MODE_NONE && distY > limitDist) {
						GestureSlidingLayout.setScrollLock(false);
						pager.requestDisallowInterceptTouchEvent(false);
					}
					break;
					
				case MotionEvent.ACTION_UP:

					if(event.getAction() == MotionEvent.ACTION_UP && mode == MODE_NONE){
						int index = pager.getCurrentItem();
						
						if(!StringUtils.isEmpty(urls.get(index))) {
							String[] imageUrls = new String[]{urls.get(index)};
							mActivity.showImageViewerActivity(getString(R.string.app_name), imageUrls, null, 0);
						}
					}
					
				case MotionEvent.ACTION_CANCEL:
					GestureSlidingLayout.setScrollLock(false);
					pager.requestDisallowInterceptTouchEvent(false);
					break;
				}
				return false;
			}
		});
		mainLayout.addView(pager);
		
		//Add textView.
		tvText = new EllipsizingTextView(mContext);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 
				tvText, 1, 0, new int[]{0, padding/2, 0, 0});
		tvText.setTextColor(Color.WHITE);
		tvText.setPadding(p, p, p, p);
		FontInfo.setFontSize(tvText, 26);
		mainLayout.addView(tvText);
	}

	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setSizes() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getTitleText() {
		
		if(title == null) {
			return "STAFF";
		} else {
			return title;
		}
	}

	@Override
	protected int getContentViewId() {

		return R.id.staffPage_mainLayout;
	}

	@Override
	protected void downloadInfo() {
		
		staff = LineupPage.selectedStaff;
		title = staff.getMember_name();
		mActivity.getTitleBar().setTitleText(title);

		LogUtils.log("#########\nstaff : " + staff + 
				"\nlength : " + (staff == null? "null" : (staff.getProfile_image() == null? "null" : staff.getProfile_image().length)));
		
		if(staff == null 
				|| staff.getProfile_image() == null 
				|| staff.getProfile_image().length == 0) {
			return;
		}
		
		header.setStaff(staff);
		tvText.setText(staff.getProfile());
		
		super.downloadInfo();
		downloadCount = 0;
		final int size = staff.getProfile_image().length;

		if(bitmaps == null) {
			bitmaps = new Bitmap[size];
			frames = new FrameLayout[size];
		}
		
		for(int i=0; i<size; i++) {
			urls.add(staff.getProfile_image()[i]);
			
			final int fIndex = i;
			
			BitmapDownloader.OnCompletedListener ocl = new BitmapDownloader.OnCompletedListener() {
				
				@Override
				public void onErrorRaised(String url, Exception e) {

					LogUtils.log("StaffPage.downloadImage.onError.\nurl : " + url);
				}
				
				@Override
				public void onCompleted(String url, Bitmap bitmap, ImageView view) {

					bitmaps[fIndex] = bitmap;

					try {
						((ImageView)frames[fIndex].getChildAt(1)).setImageBitmap(bitmap);
					} catch(Exception e) {
					}
					
					if(++downloadCount == size) {
						setPage(true);
					}
				}
			};
			BitmapDownloader.downloadImmediately(urls.get(i), getDownloadKey(), ocl, null, null, true);
		}
	}
	
	@Override
	public void onRefreshPage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onBackKeyPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onSoftKeyboardShown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSoftKeyboardHidden() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHiddenChanged(boolean hidden) {

		if(!hidden) {
			mActivity.getTitleBar().showHomeButton();
			mActivity.getTitleBar().hideWriteButton();

			if(!isDownloading && !isRefreshing && bitmaps == null) {
				downloadInfo();
			}
		}
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		
		LineupPage.selectedStaff = null;
	}
	
	@Override
	protected String generateDownloadKey() {

		return "STAFFPAGE" + madeCount;
	}

	@Override
	protected int getXmlResId() {

		return R.layout.page_staff;
	}

	public class StaffAdapter extends PagerAdapter {

		@Override
		public int getCount() {

			int size = 0;
			
			if(bitmaps != null) {
				size = bitmaps.length;
			}
			
			return size;
		}

		@Override
		public Object instantiateItem(View collection, final int position) {

			try {
				FrameLayout frame = null;
				ImageView imageView = null;
				
				if(frames[position] == null
						|| frames[position].getChildCount() != 2
						|| frames[position].getChildAt(1) == null) {
					frame = new FrameLayout(mContext);
					frames[position] = frame;
					
					int p = ResizeUtils.getSpecificLength(80);
					if(position == 0) {
						frame.setPadding(p, 0, 0, 0);
					} else {
						frame.setPadding(p, 0, p, 0);
					}
					
					ProgressBar progress = new ProgressBar(mContext);
					FrameLayout.LayoutParams fp = new FrameLayout.LayoutParams(progressLength, progressLength);
					fp.gravity = Gravity.CENTER;
					progress.setLayoutParams(fp);
					frame.addView(progress);
					
					imageView = new ImageView(mContext);
					fp = new FrameLayout.LayoutParams(imageLength, imageLength);
					fp.gravity = Gravity.LEFT;
					imageView.setLayoutParams(fp);
					frame.addView(imageView);
				} else {
					imageView = (ImageView) frames[position].getChildAt(1);
				}
				
				setBitmapToImageView(imageView, bitmaps[position]);
				
				((ViewPager) collection).addView(frame);
				return frame;
			} catch (Exception e) {
			} catch (Error e) {
			}
			
			return null;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((View) object);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
		}
	}
	
	public void setBitmapToImageView(ImageView imageView, Bitmap bitmap) {
		
		try {
			if(bitmap != null && !bitmap.isRecycled()) {
				Matrix matrix = new Matrix();
		        int imageWidth = bitmap.getWidth();
		        int imageHeight = bitmap.getHeight();
		        float scale = 0f;
				/*

	 			iw : 400
	 			ih : 200
	 			iL : 100
	 			
	 			scale : 0.5
	 			
	 			sw : 200
	 			sh : 100
	 			 
	 			tx : (200 - 100) / 2 
	 
		         * 
		         */
	        	//가로가 더 길거나 같은 경우, 세로를 imageLength에 맞게 스케일링.
	        	if(imageWidth >= imageHeight) {
	        		scale = (float)imageLength/(float)imageHeight;
	        		matrix.postScale(scale, scale, 0, 0);
	        		matrix.postTranslate(-((float)imageWidth * scale - imageLength)/2, 0);
	        		
	        	//세로가 더 긴 경우, 가로를 imageLength에 맞게 스케일링.
	        	} else {
	        		scale = (float)imageLength/(float)imageWidth;
	        		matrix.postScale(scale, scale, 0, 0);
	        		matrix.postTranslate(0, -((float)imageHeight * scale - imageLength)/2);
	        	}
	        	
	        	imageView.setScaleType(ScaleType.MATRIX);
	        	imageView.setImageMatrix(matrix);
				imageView.setImageBitmap(bitmap);
			}
		} catch(Exception e) {
			e.printStackTrace();
		} catch(OutOfMemoryError oom) {
			oom.printStackTrace();
		}
	}

}
