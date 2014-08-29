package com.zonecomms.common.views;

import com.outspoken_kid.classes.FontInfo;
import com.zonecomms.napp.classes.ApplicationManager;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.zonecomms.napp.R;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class TitleBar extends FrameLayout {

	private Button btnSideMenu;
	private Button btnHome;
	private Button btnWrite;
	private Button btnPlusApp;
	private Button btnThema;
	private Button btnRegion;
	private TextView tvTitle;
	
	private OnSideMenuButtonClickedListener onSideMenuButtonClickedListener;
	private OnWriteButtonClickedListener onWriteButtonClickedListener;
	private OnPlusAppButtonClickedListener onPlusAppButtonClickedListener;
	private OnThemaButtonClickedListener onThemaButtonClickedListener;
	private OnRegionButtonClickedListener onRegionButtonClickedListener;
	
	public TitleBar(Context context) {
		this(context, null);
	}
	
	public TitleBar(Context context, AttributeSet attr) {
		super(context, attr);
		init();
	}
	
	private void init() {
		
		//Set TitleBar.
		this.setBackgroundResource(R.drawable.bg_top);
		this.setClickable(true);
		
		//btnSideMenu.
		btnSideMenu = new Button(getContext());
		ResizeUtils.viewResize(70, 70, btnSideMenu, 2, Gravity.CENTER_VERTICAL|Gravity.LEFT, new int[]{10, 0, 0, 0});
		btnSideMenu.setBackgroundResource(R.drawable.btn_top_menu);
		btnSideMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(onSideMenuButtonClickedListener != null) {
					onSideMenuButtonClickedListener.onSideMenuButtonClicked();
				}
			}
		});
		this.addView(btnSideMenu);
		
		//btnHome.
		btnHome = new Button(getContext());
		ResizeUtils.viewResize(70, 70, btnHome, 2, Gravity.CENTER_VERTICAL|Gravity.LEFT, new int[]{90, 0, 0, 0});
		btnHome.setBackgroundResource(R.drawable.btn_top_home);
		btnHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				ApplicationManager.clearFragmentsWithoutMain();
			}
		});
		this.addView(btnHome);
		
		setTvTitle(new TextView(getContext()));
		ResizeUtils.viewResize(320, LayoutParams.MATCH_PARENT, tvTitle, 2, Gravity.CENTER, null);
		tvTitle.setText(getContext().getString(R.string.app_name));
		tvTitle.setGravity(Gravity.CENTER);
		int p = ResizeUtils.getSpecificLength(8);
		tvTitle.setPadding(p, p, p, p);
		tvTitle.setEllipsize(TruncateAt.END);
		FontInfo.setFontSize(tvTitle, 36);
		FontInfo.setFontColor(tvTitle, Color.WHITE);
		FontInfo.setFontStyle(tvTitle, FontInfo.BOLD);
		this.addView(tvTitle);
		
		//btnWrite.
		btnWrite = new Button(getContext());
		ResizeUtils.viewResize(70, 70, btnWrite, 2, Gravity.CENTER_VERTICAL|Gravity.RIGHT, new int[]{0, 0, 90, 0});
		btnWrite.setBackgroundResource(R.drawable.btn_top_write);
		btnWrite.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(onWriteButtonClickedListener != null) {
					onWriteButtonClickedListener.onWriteButtonClicked();
				}
			}
		});
		this.addView(btnWrite);
		
		//btnPlusApp.
		btnPlusApp = new Button(getContext());
		ResizeUtils.viewResize(70, 70, btnPlusApp, 2, Gravity.CENTER_VERTICAL|Gravity.RIGHT, new int[]{0, 0, 10, 0});
		btnPlusApp.setBackgroundResource(R.drawable.btn_top_n);
		btnPlusApp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(onPlusAppButtonClickedListener != null) {
					onPlusAppButtonClickedListener.onPlusAppButtonClicked();
				}
			}
		});
		this.addView(btnPlusApp);
		
		//theme.
		btnThema = new Button(getContext());
		ResizeUtils.viewResize(70, 70, btnThema, 2, Gravity.CENTER_VERTICAL|Gravity.RIGHT, new int[]{0, 0, 10, 0});
		btnThema.setBackgroundResource(R.drawable.btn_thema);
		btnThema.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(onThemaButtonClickedListener != null) {
					onThemaButtonClickedListener.onThemaButtonClicked();
				}
			}
		});
		this.addView(btnThema);
		
		//region.
		btnRegion = new Button(getContext());
		ResizeUtils.viewResize(70, 70, btnRegion, 2, Gravity.CENTER_VERTICAL|Gravity.RIGHT, new int[]{0, 0, 10, 0});
		btnRegion.setBackgroundResource(R.drawable.btn_region);
		btnRegion.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(onRegionButtonClickedListener != null) {
					onRegionButtonClickedListener.onRegionButtonClicked();
				}
			}
		});
		this.addView(btnRegion);
	}
	
	public void setTitleText(String title) {
		
		if(!StringUtils.checkNullOrDefault(title, "")) {
			tvTitle.setText(title);
		}
	}

	public Button getBtnSideMenu() {
		return btnSideMenu;
	}

	public Button getBtnHome() {
		return btnHome;
	}

	public Button getBtnWrite() {
		return btnWrite;
	}

	public Button getBtnPlusApp() {
		return btnPlusApp;
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
			btnWrite.setVisibility(View.GONE);
		}
	}
	
	public void showPlusAppButton() {
		
		if(btnPlusApp.getVisibility() != View.VISIBLE) {
			btnPlusApp.setVisibility(View.VISIBLE);
		}
	}
	
	public void hidePlusAppButton() {
		
		if(btnPlusApp.getVisibility() == View.VISIBLE) {
			btnPlusApp.setVisibility(View.GONE);
		}
	}
	
	public void showThemaButton() {
		
		if(btnThema.getVisibility() != View.VISIBLE) {
			btnThema.setVisibility(View.VISIBLE);
		}
	}
	
	public void hideThemaButton() {
		
		if(btnThema.getVisibility() == View.VISIBLE) {
			btnThema.setVisibility(View.GONE);
		}
	}
	
	public void showRegionButton() {
		
		if(btnRegion.getVisibility() != View.VISIBLE) {
			btnRegion.setVisibility(View.VISIBLE);
		}
	}
	
	public void hideRegionButton() {
		
		if(btnRegion.getVisibility() == View.VISIBLE) {
			btnRegion.setVisibility(View.GONE);
		}
	}
	
	public void setOnSideMenuButtonClickedListener(OnSideMenuButtonClickedListener onSideMenuButtonClickedListener) {
		
		this.onSideMenuButtonClickedListener = onSideMenuButtonClickedListener;
	}
	
	public void setOnWriteButtonClickedListener(OnWriteButtonClickedListener onWriteButtonClickedListener) {
		
		this.onWriteButtonClickedListener = onWriteButtonClickedListener;
	}
	
	public void setOnPlusAppButtonClickedListener(OnPlusAppButtonClickedListener onPlusAppButtonClickedListener) {
		
		this.onPlusAppButtonClickedListener = onPlusAppButtonClickedListener;
	}
	
	public void setOnThemaButtonClickedListener(OnThemaButtonClickedListener onThemaButtonClickedListener) {
		
		this.onThemaButtonClickedListener = onThemaButtonClickedListener;
	}

	public void setOnRegionButtonClickedListener(OnRegionButtonClickedListener onRegionButtonClickedListener) {
		
		this.onRegionButtonClickedListener = onRegionButtonClickedListener;
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

	public interface OnPlusAppButtonClickedListener {
	
		public void onPlusAppButtonClicked();
	}
	
	public interface OnThemaButtonClickedListener {
		
		public void onThemaButtonClicked();
	}
	
	public interface OnRegionButtonClickedListener {
		
		public void onRegionButtonClicked();
	}
}
