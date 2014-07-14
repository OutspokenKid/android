package com.zonecomms.common.views;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.zonecomms.golfn.R;
import com.zonecomms.golfn.classes.ApplicationManager;

public class TitleBar extends LinearLayout {

	private FrameLayout btnSideMenu;
	private FrameLayout btnHome;
	private FrameLayout btnWrite;
	private TextView tvTitle;
	private ImageView ivTitle;
	
	private OnSideMenuButtonClickedListener onSideMenuButtonClickedListener;
	private OnWriteButtonClickedListener onWriteButtonClickedListener;
	
	public TitleBar(Context context) {
		this(context, null);
	}
	
	public TitleBar(Context context, AttributeSet attr) {
		super(context, attr);
		init();
	}
	
	private void init() {
		
		//Set TitleBar.
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setBackgroundColor(Color.WHITE);
		this.setClickable(true);
		
		//btnSideMenu.
		btnSideMenu = new FrameLayout(getContext());
		ResizeUtils.viewResize(80, 60, btnSideMenu, 1, Gravity.CENTER_VERTICAL, new int[]{10, 0, 0, 0});
		btnSideMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(onSideMenuButtonClickedListener != null) {
					onSideMenuButtonClickedListener.onSideMenuButtonClicked();
				}
			}
		});
		this.addView(btnSideMenu);
		
		View sideMenu = new View(getContext());
		ResizeUtils.viewResize(60, 30, sideMenu, 2, Gravity.CENTER, new int[]{0, 2, 0, 0});
		sideMenu.setBackgroundResource(R.drawable.btn_top_menu);
		btnSideMenu.addView(sideMenu);

		//btnHome.
		btnHome = new FrameLayout(getContext());
		ResizeUtils.viewResize(50, 60, btnHome, 1, Gravity.CENTER_VERTICAL, new int[]{10, 0, 0, 0});
		btnHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				ApplicationManager.clearFragmentsWithoutMain();
			}
		});
		this.addView(btnHome);

		View home = new View(getContext());
		ResizeUtils.viewResize(30, 30, home, 2, Gravity.CENTER, new int[]{0, 2, 0, 0});
		home.setBackgroundResource(R.drawable.btn_top_home);
		btnHome.addView(home);
		
		FrameLayout titleFrame = new FrameLayout(getContext());
		titleFrame.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1));
		this.addView(titleFrame);
		
		//Title Text.
		tvTitle = new TextView(getContext());
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, tvTitle, 2, 0, null);
		tvTitle.setText(getContext().getString(R.string.app_name));
		tvTitle.setGravity(Gravity.CENTER);
		int p = ResizeUtils.getSpecificLength(8);
		tvTitle.setPadding(p, p, p, p);
		tvTitle.setEllipsize(TruncateAt.END);
		FontInfo.setFontSize(tvTitle, 36);
		FontInfo.setFontColor(tvTitle, Color.BLACK);
		FontInfo.setFontStyle(tvTitle, FontInfo.BOLD);
		titleFrame.addView(tvTitle);
		
		//Title image.
		ivTitle = new ImageView(getContext());
		ResizeUtils.viewResize(114, 36, ivTitle, 2, Gravity.CENTER, null);
		ivTitle.setBackgroundResource(R.drawable.img_title);
		ivTitle.setVisibility(INVISIBLE);
		titleFrame.addView(ivTitle);
		
		//btnWrite.
		btnWrite = new FrameLayout(getContext());
		ResizeUtils.viewResize(100, 60, btnWrite, 1, Gravity.CENTER_VERTICAL, new int[]{50, 0, 4, 0});
		btnWrite.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(onWriteButtonClickedListener != null) {
					onWriteButtonClickedListener.onWriteButtonClicked();
				}
			}
		});
		this.addView(btnWrite);
		
		View write = new View(getContext());
		ResizeUtils.viewResize(80, 30, write, 2, Gravity.CENTER, null);
		write.setBackgroundResource(R.drawable.btn_top_write);
		btnWrite.addView(write);
	}
	
	public void setTitleText(String title) {
		
		if(!StringUtils.checkNullOrDefault(title, "")) {
			tvTitle.setText(title);
			
			tvTitle.setVisibility(View.VISIBLE);
			ivTitle.setVisibility(View.INVISIBLE);
		} else {
			tvTitle.setVisibility(View.INVISIBLE);
			ivTitle.setVisibility(View.VISIBLE);
		}
	}

	public FrameLayout getBtnSideMenu() {
		return btnSideMenu;
	}

	public FrameLayout getBtnHome() {
		return btnHome;
	}

	public FrameLayout getBtnWrite() {
		return btnWrite;
	}

	public TextView getTvTitle() {
		return tvTitle;
	}
	
	public void setTvTitle(TextView tvTitle) {
		this.tvTitle = tvTitle;
	}
	
	public void showHomeButton() {
		
		if(btnHome.getVisibility() != View.VISIBLE) {
			btnHome.setVisibility(View.VISIBLE);
		}
	}
	
	public void hideHomeButton() {
		
		if(btnHome.getVisibility() == View.VISIBLE) {
			btnHome.setVisibility(View.INVISIBLE);
		}
	}
	
	public void showWriteButton() {
		
		if(btnWrite.getVisibility() != View.VISIBLE) {
			btnWrite.setVisibility(View.VISIBLE);
		}
	}
	
	public void hideWriteButton() {
		
		if(btnWrite.getVisibility() == View.VISIBLE) {
			btnWrite.setVisibility(View.INVISIBLE);
		}
	}
	
	public void setOnSideMenuButtonClickedListener(OnSideMenuButtonClickedListener onSideMenuButtonClickedListener) {
		
		this.onSideMenuButtonClickedListener = onSideMenuButtonClickedListener;
	}
	
	public void setOnWriteButtonClickedListener(OnWriteButtonClickedListener onWriteButtonClickedListener) {
		
		this.onWriteButtonClickedListener = onWriteButtonClickedListener;
	}
	
//////////////////// Interfaces.
	
	public interface OnSideMenuButtonClickedListener {
		
		public void onSideMenuButtonClicked();
	}
	
	public interface OnHomeButtonClickedListener {
		
		public void onHomeButtonClicked();
	}
	
	public interface OnWriteButtonClickedListener {
		
		public void onWriteButtonClicked();
	}
}
