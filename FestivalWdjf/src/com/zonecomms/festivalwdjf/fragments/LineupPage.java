package com.zonecomms.festivalwdjf.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import at.technikum.mti.fancycoverflow.FancyCoverFlow;
import at.technikum.mti.fancycoverflow.FancyCoverFlowAdapter;

import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.downloader.bitmapdownloader.BitmapDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader.OnCompletedListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.views.GestureSlidingLayout;
import com.triposo.barone.EllipsizingTextView;
import com.zonecomms.common.models.Staff;
import com.zonecomms.common.views.StaffHeaderView;
import com.zonecomms.festivalwdjf.IntentHandlerActivity;
import com.zonecomms.festivalwdjf.R;
import com.zonecomms.festivalwdjf.classes.BaseFragment;
import com.zonecomms.festivalwdjf.classes.ZoneConstants;

public class LineupPage extends BaseFragment {

	public static Staff selectedStaff;
	
	private int imageLength;
	private int downloadCount;
	
	private LinearLayout mainLayout;
	private StaffHeaderView header;
	private FancyCoverFlow coverFlow;
	private EllipsizingTextView tvText;
	private ArrayList<Staff> staffs = new ArrayList<Staff>();
	private Bitmap[] bitmaps;
	private LineupAdapter adapter; 
	
	@Override
	protected void bindViews() {

		mainLayout = (LinearLayout) mThisView.findViewById(R.id.lineupPage_mainLayout);
	}

	@Override
	protected void setVariables() {

		imageLength = ResizeUtils.getSpecificLength(320);
		title = "LINE UP";
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void createPage() {

		int p = ResizeUtils.getSpecificLength(40);
		
		//Add headerView.
		header = new StaffHeaderView(mContext);
		mainLayout.addView(header);
		
		//Add coverFlow.
		coverFlow = new FancyCoverFlow(mContext);
		ResizeUtils.viewResize(624, 320, coverFlow, 1, Gravity.CENTER_HORIZONTAL, null);
        coverFlow.setUnselectedAlpha(1.0f);
        coverFlow.setUnselectedSaturation(0.0f);
        coverFlow.setUnselectedScale(0.5f);
        coverFlow.setSpacing(-ResizeUtils.getSpecificLength(60));
        coverFlow.setMaxRotation(60);
        coverFlow.setScaleDownGravity(0.5f);
        coverFlow.setActionDistance(FancyCoverFlow.ACTION_DISTANCE_AUTO);
        coverFlow.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				switch(event.getAction()) {
				
				case MotionEvent.ACTION_DOWN:
					GestureSlidingLayout.setScrollLock(true);
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					GestureSlidingLayout.setScrollLock(false);
					break;
				}
				return false;
			}
		});
		coverFlow.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {

				if(staffs.get(arg2) != null) {
					header.setStaff(staffs.get(arg2));
					tvText.setText(staffs.get(arg2).getProfile());
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
        coverFlow.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
			
				if(staffs.get(arg2) != null) {
					selectedStaff = staffs.get(arg2);
					String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/staff";
					IntentHandlerActivity.actionByUri(Uri.parse(uriString));
				}
			}
		});
		mainLayout.addView(coverFlow);
		
		//Add textView.
		tvText = new EllipsizingTextView(mContext);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 
				tvText, 1, 0, null);
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

		title = "LINEUP";
		return "LINEUP";
	}

	@Override
	protected int getContentViewId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected void downloadInfo() {
		
		if(isDownloading) {
			return;
		}
		
		super.downloadInfo();
		
		String url = ZoneConstants.BASE_URL + "staff/getStaffList" +
				"?sb_id=" + ZoneConstants.PAPP_ID +
				"&image_size=560";
		
		AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
			
			@Override
			public void onErrorRaised(String url, Exception e) {

				LogUtils.log("LineupPage.onError.\nurl : " + url);
				setPage(false);
			}
			
			@Override
			public void onCompleted(String url, String result) {

				LogUtils.log("LineupPage.onCompleted.\nurl : " + url + "\nresult : " + result);
				
				try {
					JSONArray arResult = new JSONArray(result);
					
					int size = arResult.length();
					for(int i=0; i<size; i++) {
						try {
							staffs.add(new Staff(arResult.getJSONObject(i)));
						} catch(Exception e) {
						}
					}
					
					setPage(true);
				} catch(Exception e) {
					e.printStackTrace();
					setPage(false);
				}
			}
		};
		AsyncStringDownloader.download(url, getDownloadKey(), ocl);
	}
	
	@Override
	protected void setPage(boolean downloadSuccess) {

		if(!downloadSuccess) {
			super.setPage(downloadSuccess);
		}
		
		if(mainLayout.getVisibility() == View.INVISIBLE && downloadSuccess) {
			
			int size = staffs.size();
			bitmaps = new Bitmap[size];
			downloadCount = 0;
			for(int i=0; i<size; i++) {
				final int Index = i;
				BitmapDownloader.OnCompletedListener ocl = new BitmapDownloader.OnCompletedListener() {
					
					@Override
					public void onErrorRaised(String url, Exception e) {

						LogUtils.log("LineupPage.downloadImage.onError.\nurl : " + url);
						downloadCount++;
						
						if(bitmaps.length == downloadCount) {
							show();
						}
					}
					
					@Override
					public void onCompleted(String url, Bitmap bitmap, ImageView view) {

						bitmaps[Index] = bitmap;
						downloadCount++;
						
						if(bitmaps.length == downloadCount) {
							show();
						}
					}
					
					public void show() {
						
						//Set page to first staff.
						header.setStaff(staffs.get(0));
						tvText.setText(staffs.get(0).getProfile());
						
						LineupPage.super.setPage(true);
						
						adapter = new LineupAdapter();
						coverFlow.setAdapter(adapter);
						adapter.notifyDataSetChanged();
						mainLayout.setVisibility(View.VISIBLE);
						
						AlphaAnimation aaIn = new AlphaAnimation(0, 1);
						aaIn.setDuration(600);
						mainLayout.startAnimation(aaIn);
					}
				};
				BitmapDownloader.downloadImmediately(staffs.get(i).getMain_image(), getDownloadKey(), ocl, null, null, true);
			}
		}
	}
	
	@Override
	public void onRefreshPage() {
	}

	@Override
	public boolean onBackKeyPressed() {
		return false;
	}

	@Override
	public void onSoftKeyboardShown() {
	}

	@Override
	public void onSoftKeyboardHidden() {
	}

	@Override
	public void onHiddenChanged(boolean hidden) {

		super.onHiddenChanged(hidden);
		
		if(!hidden) {
			mActivity.getTitleBar().showHomeButton();
			mActivity.getTitleBar().hideWriteButton();
			
			if(!isDownloading && !isRefreshing && staffs.size() == 0) {
				downloadInfo();
			}
		}
	}

	@Override
	protected String generateDownloadKey() {
		
		return "LINEUPPAGE" + madeCount;
	}

	@Override
	protected int getXmlResId() {

		return R.layout.page_lineup;
	}
	
	public class LineupAdapter extends FancyCoverFlowAdapter {

	    @Override
	    public int getCount() {
	        return staffs.size();
	    }

	    @Override
	    public Staff getItem(int i) {
	        return staffs.get(i);
	    }

	    @Override
	    public long getItemId(int i) {
	        return i;
	    }

	    @Override
	    public View getCoverFlowItem(int i, View reuseableView, ViewGroup viewGroup) {
	        
	    	ImageView imageView = null;

	        if (reuseableView != null) {
	            imageView = (ImageView) reuseableView;
	        } else {
	            imageView = new ImageView(viewGroup.getContext());
	            imageView.setLayoutParams(new FancyCoverFlow.LayoutParams(imageLength, imageLength));
	        }
	        
	        try {
	        	Matrix matrix = new Matrix();
		        int imageWidth = bitmaps[i].getWidth();
		        int imageHeight = bitmaps[i].getHeight();
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
		        imageView.setImageBitmap(bitmaps[i]);
			} catch(Exception e) {
			}
	        
	        return imageView;
	    }
	}
}
