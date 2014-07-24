package com.zonecomms.common.views;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;

import com.outspoken_kid.classes.ViewUnbindHelper;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.views.holo_dark.HoloStyleButton;
import com.zonecomms.clubcage.IntentHandlerActivity;
import com.zonecomms.clubcage.R;
import com.zonecomms.clubcage.classes.ZoneConstants;
import com.zonecomms.common.models.Popup;

public class NoticePopup extends FrameLayout {

	private ImageView ivImage;
	private HoloStyleButton btnDontSeeAgainToday, btnConfirm;
	private Popup popup;
	
	public NoticePopup(Context context) {
		super(context);
		init();
	}

	private void init() {

		setBackgroundColor(Color.argb(180, 0, 0, 0));
		setVisibility(View.INVISIBLE);
		setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				hide(null);
			}
		});
		
		ProgressBar progress = new ProgressBar(getContext());
		ResizeUtils.viewResize(40, 40, progress, 2, Gravity.CENTER, null);
		this.addView(progress);
		
		btnDontSeeAgainToday = new HoloStyleButton(getContext());
		ResizeUtils.viewResize(400, 80, btnDontSeeAgainToday, 2, Gravity.LEFT|Gravity.BOTTOM, new int[]{20, 0, 0, 20});
		btnDontSeeAgainToday.getTextView().setText(R.string.dontSeeAgainToday);
		btnDontSeeAgainToday.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharedPrefsUtils.addDataToPrefs(ZoneConstants.PREFS_POPUP, "lastIndex", popup.getNotice_nid());
				
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
		
		ivImage = new ImageView(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, ivImage, 2, 0, new int[]{0, 0, 0, 120});
		ivImage.setScaleType(ScaleType.FIT_CENTER);
		ivImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(popup != null && !StringUtils.isEmpty(popup.getLink_url())) {
					IntentHandlerActivity.actionByUri(Uri.parse(popup.getLink_url()));
				}
			}
		});
		this.addView(ivImage);
		
		if(getVisibility() != View.VISIBLE) {
			this.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_from_bottom));
			this.setVisibility(View.VISIBLE);
			this.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					ImageDownloadUtils.downloadImage(popup.getBg_img_url(), null, ivImage, 640);
				}
			}, 300);
		}
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
			}, 500);
		}
	}
	
	public void clear() {
		
		popup = null;
		ViewUnbindHelper.unbindReferences(ivImage);
	}
	
//////////////////// Interfaces.
	
	public interface OnAfterHideListener {
		
		public void onAfterHide();
	}
}