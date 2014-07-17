package com.zonecomms.common.views;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.classes.ViewUnbindHelper;
import com.outspoken_kid.downloader.bitmapdownloader.BitmapDownloader;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.views.holo_dark.HoloStyleButton;
import com.zonecomms.common.models.Popup;
import com.zonecomms.golfn.IntentHandlerActivity;
import com.zonecomms.golfn.R;
import com.zonecomms.golfn.classes.ZoneConstants;

public class NoticePopup extends FrameLayout {

	private TextView tvTitle;
	private ScrollView scrollView;
	private HoloStyleButton btnDontSeeAgainToday, btnConfirm;
	private Popup popup;
	private LinearLayout innerLinear;
	
	public NoticePopup(Context context) {
		super(context);
		init();
	}

	private void init() {

		setBackgroundColor(Color.argb(180, 0, 0, 0));
		setVisibility(View.INVISIBLE);
		
		tvTitle = new TextView(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 90, tvTitle, 2, Gravity.TOP, null);
		int p = ResizeUtils.getSpecificLength(10);
		tvTitle.setPadding(p, p, p, p);
		tvTitle.setGravity(Gravity.CENTER);
		tvTitle.setEllipsize(TruncateAt.END);
		tvTitle.setMaxLines(2);
		tvTitle.setTextColor(Color.WHITE);
		FontInfo.setFontSize(tvTitle, 30);
		this.addView(tvTitle);
		
		scrollView = new ScrollView(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 
				scrollView, 2, Gravity.LEFT, new int[]{0, 98, 0, 0});
		scrollView.setFillViewport(true);
		this.addView(scrollView);
		
		innerLinear = new LinearLayout(getContext());
		innerLinear.setLayoutParams(new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		innerLinear.setOrientation(LinearLayout.VERTICAL);
		scrollView.addView(innerLinear);
		
		btnDontSeeAgainToday = new HoloStyleButton(getContext());
		ResizeUtils.viewResize(400, 80, btnDontSeeAgainToday, 2, Gravity.LEFT|Gravity.BOTTOM, new int[]{20, 0, 0, 20});
		btnDontSeeAgainToday.getTextView().setText(R.string.dontSeeAgainToday);
		btnDontSeeAgainToday.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharedPrefsUtils.addDataToPrefs(ZoneConstants.PREFS_POPUP, "lastIndexno", popup.getNotice_nid());
				
				int currentDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
				SharedPrefsUtils.addDataToPrefs(ZoneConstants.PREFS_POPUP, "lastDate", currentDate);
				
				int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
				SharedPrefsUtils.addDataToPrefs(ZoneConstants.PREFS_POPUP, "lastMonth", currentMonth);
				
				hide(null);
			}
		});
		this.addView(btnDontSeeAgainToday);
		
		btnConfirm = new HoloStyleButton(getContext());
		ResizeUtils.viewResize(170, 80, btnConfirm, 2, Gravity.RIGHT|Gravity.BOTTOM, new int[]{0, 0, 20, 20});
		btnConfirm.getTextView().setText(R.string.confirm);
		btnConfirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				hide(null);
			}
		});
		this.addView(btnConfirm);
	}

	public void show(final Popup popup) {

		if(popup != null) {
			this.popup = popup;
		} else {
			return;
		}
		
		if(getVisibility() != View.VISIBLE) {
			
			//Set title.
			if(!StringUtils.isEmpty(popup.getNotice_title())) {
				
				//Show tvTitle.
				tvTitle.setVisibility(View.VISIBLE);
				tvTitle.setText(popup.getNotice_title());
				
			} else {
				//Hide tvTitle.
				tvTitle.setVisibility(View.INVISIBLE);
				ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 
						scrollView, 2, Gravity.LEFT, null);
			}
			
			//Set images.
			if(popup.getImageUrls() != null
					&& popup.getImageUrls().length > 0) {
				int size = popup.getImageUrls().length;
				for(int i=0; i<size; i++) {
					addImageFrame(popup.getImageUrls()[i]);
				}
			}
			
			//Set content.
			if(!StringUtils.isEmpty(popup.getContent())) {
				addContent(popup.getContent());
			}
			
			//Set link.
			if(!StringUtils.isEmpty(popup.getLink_url())) {
				addLink(popup.getLink_url());
			}

			View bottomBlank = new View(getContext());
			ResizeUtils.viewResize(10, 120, bottomBlank, 1, 0, null);
			innerLinear.addView(bottomBlank);
			
			this.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_from_bottom));
			this.setVisibility(View.VISIBLE);
		}
	}
	
	public void addImageFrame(final String imageUrl) {
		
		final FrameLayout imageFrame = new FrameLayout(getContext());
		ResizeUtils.viewResize(600, 600, imageFrame, 1, Gravity.CENTER_HORIZONTAL, null);
		innerLinear.addView(imageFrame);
		
		final ProgressBar progress = new ProgressBar(getContext());
		ResizeUtils.viewResize(40, 40, progress, 2, Gravity.CENTER, null);
		imageFrame.addView(progress);
		
		final ImageView ivImage = new ImageView(getContext());
		imageFrame.addView(ivImage);
		
		this.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				BitmapDownloader.OnCompletedListener ocl = new BitmapDownloader.OnCompletedListener() {
					
					@Override
					public void onErrorRaised(String url, Exception e) {
						
						imageFrame.setVisibility(View.GONE);
					}
					
					@Override
					public void onCompleted(String url, Bitmap bitmap, ImageView view) {
						
						try {
							if(bitmap != null && !bitmap.isRecycled() && view != null) {
								int height = (int)(624f
										* (float) bitmap.getHeight()
										/ (float) bitmap.getWidth());

								LogUtils.log("###NoticePopup.onCompleted.  " +
										"\nwidth : " + bitmap.getWidth() +
										"\nheight : " + bitmap.getHeight() +
										"\nscaledHeight : " + height);
								
								ResizeUtils.viewResize(624, height, imageFrame, 1, 
										Gravity.CENTER_HORIZONTAL, new int[]{0, 0, 0, 0});
								ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 
										LayoutParams.MATCH_PARENT, 
										ivImage, 2, 0, null);
								ivImage.setScaleType(ScaleType.FIT_XY);
								ivImage.setImageBitmap(bitmap);
							}
						} catch (Exception e) {
							LogUtils.trace(e);
							imageFrame.setVisibility(View.GONE);
						} catch (Error e) {
							LogUtils.trace(e);
							imageFrame.setVisibility(View.GONE);
						}
					}
				};
				BitmapDownloader.download(imageUrl, null, ocl, null, ivImage, true);
			}
		}, 2000);
	}
	
	public void addContent(String content) {
		
		int p = ResizeUtils.getSpecificLength(10);
		
		TextView tvContent = new TextView(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 
				tvContent, 1, Gravity.CENTER_HORIZONTAL, null);
		tvContent.setPadding(p, p, p, p);
		tvContent.setTextColor(Color.WHITE);
		tvContent.setText(content);
		FontInfo.setFontSize(tvContent, 26);
		innerLinear.addView(tvContent);
	}
	
	public void addLink(final String linkUrl) {
		
		innerLinear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				IntentHandlerActivity.actionByUri(Uri.parse(linkUrl));
			}
		});
	}
	
	public void hide(final OnAfterHideListener onAfterHideListener) {
	
		if(getVisibility() == View.VISIBLE) {
			this.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_to_bottom));
			this.setVisibility(View.INVISIBLE);
			this.postDelayed(new Runnable() {
				
				@Override
				public void run() {

					if(onAfterHideListener != null) {
						onAfterHideListener.onAfterHide();
					}
					
					clear();
				}
			}, 300);
		}
	}
	
	public void clear() {
		
		popup = null;
		ViewUnbindHelper.unbindReferences(scrollView);
	}
	
//////////////////// Interfaces.
	
	public interface OnAfterHideListener {
		
		public void onAfterHide();
	}
}