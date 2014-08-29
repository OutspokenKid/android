package com.zonecomms.common.views;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.utils.ResizeUtils;
import com.zonecomms.napp.R;

public class ReplyLoadingView extends LinearLayout {

	public static int MODE_LOADING = 0;
	public static int MODE_SEEMORE = 1;
	public static int MODE_LAST = 2;
	
	private int mode = -1;
	
	private ProgressBar progressBar;
	private TextView tvText;
	
	private OnLoadingViewClickedListener onLoadingViewClickedListener;
	
	public ReplyLoadingView(Context context) {
		super(context);
		init();
	}

	public void init() {
		
		this.setBackgroundColor(Color.rgb(60, 60, 60));
		this.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(onLoadingViewClickedListener != null) {
					onLoadingViewClickedListener.onLoadingViewClicked(mode);
				}
			}
		});
		
		View leftBlank = new View(getContext());
		leftBlank.setLayoutParams(new LinearLayout.LayoutParams(0, 1, 1));
		this.addView(leftBlank);
		
		progressBar = new ProgressBar(getContext());
		ResizeUtils.viewResize(40, 40, progressBar, 1, Gravity.CENTER_VERTICAL, new int[]{0, 0, 20, 0});
		this.addView(progressBar);
		
		tvText = new TextView(getContext());
		ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, tvText, 1, Gravity.CENTER_VERTICAL, null);
		tvText.setTextColor(Color.WHITE);
		FontInfo.setFontSize(tvText, 30);
		this.addView(tvText);
		
		View rightBlank = new View(getContext());
		rightBlank.setLayoutParams(new LinearLayout.LayoutParams(0, 1, 1));
		this.addView(rightBlank);
	}
	
	public void setMode(int mode) {

		if(this.mode == mode) {
			return;
		}
		
		this.mode = mode;

		if(mode == 0) {
			progressBar.setVisibility(View.VISIBLE);
			tvText.setText(R.string.loadingReplies);
		} else if(mode == 1) {
			progressBar.setVisibility(View.GONE);
			tvText.setText(R.string.seeMoreReplies);
		} else if(mode == 2) {
			progressBar.setVisibility(View.GONE);
			tvText.setText(R.string.noMoreReplies);
		}
	}

	public void setOnLoadingViewClickedListener(
			OnLoadingViewClickedListener onLoadingViewClickedListener) {
		this.onLoadingViewClickedListener = onLoadingViewClickedListener;
	}

	public interface OnLoadingViewClickedListener {
		
		public void onLoadingViewClicked(int mode);
	}
}