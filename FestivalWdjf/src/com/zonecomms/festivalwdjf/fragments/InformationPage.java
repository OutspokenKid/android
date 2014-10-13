package com.zonecomms.festivalwdjf.fragments;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.downloader.bitmapdownloader.BitmapDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader.OnCompletedListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.views.GestureSlidingLayout;
import com.outspoken_kid.views.holo_dark.HoloStyleSpinnerPopup;
import com.outspoken_kid.views.holo_dark.HoloStyleSpinnerPopup.OnItemClickedListener;
import com.zonecomms.common.models.PappInfo;
import com.zonecomms.common.models.PappInfo.Phone;
import com.zonecomms.common.utils.AppInfoUtils;
import com.zonecomms.festivalwdjf.IntentHandlerActivity;
import com.zonecomms.festivalwdjf.R;
import com.zonecomms.festivalwdjf.classes.BaseFragment;
import com.zonecomms.festivalwdjf.classes.ZoneConstants;

public class InformationPage extends BaseFragment {

	private static final int MODE_NONE = 0;
	private static final int MODE_DRAG = 1;
	
	private PappInfo pappInfo; 
	
	private int imageLength;
	private int padding;
	private int mode;
	private int downloadCount;
	
	private ArrayList<String> urls = new ArrayList<String>();
	private Bitmap[] bitmaps;
	
	private RelativeLayout relative;
	private TextView tvName;
	private ViewPager pager;
	private InfoAdapter adapter;
	private TextView tvText;
	private HoloStyleSpinnerPopup pCall;
	
	@Override
	protected void bindViews() {

		relative = (RelativeLayout) mThisView.findViewById(R.id.informationPage_relative);
	}

	@Override
	protected void setVariables() {

		imageLength = ResizeUtils.getSpecificLength(480);
		padding = (ResizeUtils.getScreenWidth() - imageLength)/2;
		title = "INFORMATION";
	}

	@Override
	protected void createPage() {

		int l = ResizeUtils.getSpecificLength(150);
		int s = ResizeUtils.getSpecificLength(8);
		int p = ResizeUtils.getSpecificLength(20);

		RelativeLayout.LayoutParams rp = null;
		
		//titleImage.  id : 0
		View logo = new ImageView(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		rp.leftMargin = s;
		rp.topMargin = s;
		rp.rightMargin = s;
		rp.bottomMargin = s;
		logo.setId(madeCount);
		logo.setLayoutParams(rp);
		logo.setBackgroundResource(R.drawable.btn_papp_logo);
		relative.addView(logo);
		
		//Name.
		tvName = new TextView(mContext);
		rp = new RelativeLayout.LayoutParams(l*3 + s*2, l);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount);
		tvName.setLayoutParams(rp);
		tvName.setTextColor(Color.WHITE);
		tvName.setText(R.string.app_name);
		tvName.setPadding(p, p, p, p);
		tvName.setGravity(Gravity.CENTER_VERTICAL);
		FontInfo.setFontSize(tvName, 30);
		FontInfo.setFontStyle(tvName, FontInfo.BOLD);
		relative.addView(tvName);
		
		//Web.		  id : 1
		View web = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount);
		rp.addRule(RelativeLayout.BELOW, madeCount);
		rp.rightMargin = s;
		web.setLayoutParams(rp);
		web.setId(madeCount + 1);
		web.setBackgroundResource(R.drawable.btn_info_web);
		web.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				IntentHandlerActivity.actionByUri(Uri.parse(pappInfo.getSb_web_url()));
			}
		});
		relative.addView(web);
		
		//Facebook.  id : 2
		View facebook = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 1);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 1);
		rp.rightMargin = s;
		facebook.setLayoutParams(rp);
		facebook.setId(madeCount + 2);
		facebook.setBackgroundResource(R.drawable.btn_info_fb);
		facebook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				IntentHandlerActivity.actionByUri(Uri.parse(pappInfo.getSb_facebook()));
			}
		});
		relative.addView(facebook);
		
		//Location.  id : 3
		View location = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 2);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 2);
		rp.rightMargin = s;
		location.setLayoutParams(rp);
		location.setId(madeCount + 3);
		location.setBackgroundResource(R.drawable.btn_info_location);
		location.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/map" +
							"?latitude=" + pappInfo.getSb_latitude() +
							"&longitude=" + pappInfo.getSb_longitude();
					IntentHandlerActivity.actionByUri(Uri.parse(uriString));
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}
			}
		});
		relative.addView(location);
		
		//Call.
		View call = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 3);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 3);
		rp.rightMargin = s;
		call.setLayoutParams(rp);
		call.setBackgroundResource(R.drawable.btn_info_call);
		call.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				pCall.showPopup();
			}
		});
		relative.addView(call);
		
		//ViewPager.		id : 4
		pager = new ViewPager(mContext);
		rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, imageLength);
		rp.topMargin = padding/2;
		rp.bottomMargin = padding/2;
		rp.addRule(RelativeLayout.BELOW, madeCount + 1);
		pager.setLayoutParams(rp);
		pager.setId(madeCount +4);
		pager.setPageMargin(-padding/2*3);
		pager.setVisibility(View.INVISIBLE);
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
		relative.addView(pager);
		
		//Introduce.
		tvText = new TextView(mContext);
		rp = new RelativeLayout.LayoutParams(l*4 + s*3, LayoutParams.MATCH_PARENT);
		rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		rp.addRule(RelativeLayout.BELOW, madeCount + 4);
		rp.bottomMargin = s;
		tvText.setLayoutParams(rp);
		tvText.setPadding(p, p, p, p);
		tvText.setTextColor(Color.WHITE);
		FontInfo.setFontSize(tvText, 26);
		FontInfo.setFontStyle(tvText, FontInfo.BOLD);
		relative.addView(tvText);
		
		pCall = new HoloStyleSpinnerPopup(mContext);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, pCall, 2, 0, null);
		pCall.setLayoutParams(rp);
		((FrameLayout)mThisView.findViewById(R.id.informationPage_mainLayout)).addView(pCall);
	}

	@Override
	protected void setListeners() {

		pCall.setOnItemClickedListener(new OnItemClickedListener() {
			
			@Override
			public void onItemClicked(int position, String itemString) {

				try {
					String phoneNumber = pappInfo.getPhones()[position].sb_tel;
					Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
					mActivity.startActivity(intent);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	protected void setSizes() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void downloadInfo() {

		if(isDownloading) {
			return;
		}
		
		super.downloadInfo();
		
		String url = ZoneConstants.BASE_URL + "sb/pappInfo" +
				"?image_size=" + imageLength +
				"&" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL);
		
		AsyncStringDownloader.OnCompletedListener ocl0 = new OnCompletedListener() {
			
			@Override
			public void onErrorRaised(String url, Exception e) {

				LogUtils.log("InformationPage.onError.\nrul : " + url);
				setPage(false);
			}
			
			@Override
			public void onCompleted(String url, String result) {
				
				LogUtils.log("InformationPage.onCompleted.\nrul : " + url + "\nreuslt : " + result);
				
				try {
					pappInfo = new PappInfo((new JSONObject(result)).getJSONObject("data"));
					setPage(true);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		};
		AsyncStringDownloader.download(url, getDownloadKey(), ocl0);
	}

	@Override
	protected void setPage(boolean successDownload) {

		if(!successDownload) {
			super.setPage(successDownload);
		}
		
		tvName.setText(pappInfo.getSimple_sb_intro());
		
		if(pappInfo.getPhones() != null) {

			pCall.setTitle(getString(R.string.call));
			
			Phone[] phones = pappInfo.getPhones();
			int size = phones.length;
			for(int i=0; i<size; i++) {
				pCall.addItem(phones[i].sb_tel + " - " + phones[i].sb_tel_name);
			}
			
			pCall.notifyDataSetChanged();
		}
		
		int size = pappInfo.getSub_media_src().length;
		for(int i=0; i<size; i++) {
			urls.add(pappInfo.getSub_media_src()[i]);
		}
		
		if(pager.getVisibility() != View.VISIBLE && bitmaps == null) {
			super.downloadInfo();
			size = urls.size();
			bitmaps = new Bitmap[size];
			downloadCount = 0;
			for(int i=0; i<size; i++) {
				final int Index = i;
				BitmapDownloader.OnCompletedListener ocl = new BitmapDownloader.OnCompletedListener() {
					
					@Override
					public void onErrorRaised(String url, Exception e) {

						LogUtils.log("InformationPage.downloadImage.onError.\nurl : " + url);
						downloadCount++;
						
						if(downloadCount == bitmaps.length) {
							show();
						}
					}
					
					@Override
					public void onCompleted(String url, Bitmap bitmap, ImageView view) {

						LogUtils.log("InformationPage.downloadImage.onCompleted.\nurl : " + url);
						
						bitmaps[Index] = bitmap;
						downloadCount++;
						
						if(downloadCount == bitmaps.length) {
							show();
						}
					}
					
					public void show() {
						InformationPage.super.setPage(true);
						
						adapter = new InfoAdapter();
						pager.setAdapter(adapter);
						adapter.notifyDataSetChanged();
						pager.setVisibility(View.VISIBLE);
						
						AlphaAnimation aaIn = new AlphaAnimation(0, 1);
						aaIn.setDuration(600);
						pager.startAnimation(aaIn);
					}
				};
				BitmapDownloader.downloadImmediately(urls.get(i), getDownloadKey(), ocl, null, null, true);
			}
		}
		
		tvText.setText(pappInfo.getSb_description());
	}

	@Override
	public String getTitleText() {

		return getString(R.string.app_name);
	}

	@Override
	protected int getContentViewId() {

		return R.id.informationPage_mainLayout;
	}

	@Override
	public boolean onBackKeyPressed() {

		if(pCall.getVisibility() == View.VISIBLE) {
			pCall.hidePopup();
			return true;
		}
		
		return false;
	}

	@Override
	public void onRefreshPage() {
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		
		if(!hidden) {
			mActivity.getTitleBar().showHomeButton();
			mActivity.getTitleBar().hideWriteButton();
			
			if(mActivity.getSponserBanner() != null) {
				mActivity.getSponserBanner().downloadBanner();
			}
			
			if(!isDownloading && bitmaps == null) {
				downloadInfo();
			}
		}
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
	protected String generateDownloadKey() {
		
		return "INFORMATIONPAGE" + madeCount;
	}

	@Override
	protected int getXmlResId() {

		return R.layout.page_information;
	}
	
	public class InfoAdapter extends PagerAdapter {

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

			LinearLayout linear = new LinearLayout(mContext);
			linear.setOrientation(LinearLayout.HORIZONTAL);
			
			int p = ResizeUtils.getSpecificLength(80);
			if(position == 0) {
				linear.setPadding(p, 0, 0, 0);
			} else {
				linear.setPadding(p, 0, p, 0);
			}
			
			ImageView imageView = new ImageView(mContext);
			imageView.setLayoutParams(new LinearLayout.LayoutParams(imageLength, imageLength));
			linear.addView(imageView);
			
			try {
				Bitmap bitmap = bitmaps[position];
				
				if(bitmap != null && !bitmap.isRecycled()) {
					
					Matrix matrix = new Matrix();
			        int imageWidth = bitmaps[position].getWidth();
			        int imageHeight = bitmaps[position].getHeight();
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
			}
			
			((ViewPager) collection).addView(linear);
			return linear;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == ((View) object);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
		}
	}

}
