package com.zonecomms.common.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.zonecomms.clubvera.CircleMainActivity;
import com.zonecomms.clubvera.R;
import com.zonecomms.clubvera.classes.ZonecommsApplication;

public class TitleBar extends LinearLayout {

	public static int titleBarColor;
	
	private Button btnSideMenu;
	private Button btnCircle;
	private Button btnHome;
	private Button btnWrite;
	private Button btnN;
	private TextView tvTitle;
	
	private OnSideMenuButtonClickedListener onSideMenuButtonClickedListener;
	private OnWriteButtonClickedListener onWriteButtonClickedListener;
	private OnNButtonClickedListener onNButtonClickedListener;
	
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
		this.setBackgroundColor(titleBarColor);
		this.setClickable(true);
		
		//btnSideMenu.
		btnSideMenu = new Button(getContext());
		ResizeUtils.viewResize(60, 82, btnSideMenu, 1, Gravity.CENTER_VERTICAL, new int[]{10, 0, 0, 0});
		btnSideMenu.setBackgroundResource(R.drawable.topicon_menu_01);
		btnSideMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(onSideMenuButtonClickedListener != null) {
					onSideMenuButtonClickedListener.onSideMenuButtonClicked();
				}
			}
		});
		this.addView(btnSideMenu);
		
		//btnCircle.
		btnCircle = new Button(getContext());
		ResizeUtils.viewResize(60, 82, btnCircle, 1, Gravity.CENTER_VERTICAL, new int[]{10, 0, 0, 0});
		btnCircle.setBackgroundResource(R.drawable.topicon_home_01);
		btnCircle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(getContext(), CircleMainActivity.class);
				getContext().startActivity(intent);
				ZonecommsApplication.getActivity().finish();
			}
		});
		this.addView(btnCircle);
		
		//btnHome.
		btnHome = new Button(getContext());
		ResizeUtils.viewResize(60, 82, btnHome, 1, Gravity.CENTER_VERTICAL, new int[]{10, 0, 0, 0});
		btnHome.setBackgroundResource(R.drawable.topicon_metro_01);
		btnHome.setVisibility(View.GONE);
		btnHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				ZonecommsApplication.getActivity().clearFragments(true);
			}
		});
		this.addView(btnHome);
		
		setTvTitle(new TextView(getContext()));
		tvTitle.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1));
		tvTitle.setText(getContext().getString(R.string.app_name));
		tvTitle.setGravity(Gravity.CENTER);
		int p = ResizeUtils.getSpecificLength(8);
		tvTitle.setPadding(p, p, p, p);
		tvTitle.setEllipsize(TruncateAt.END);
		FontUtils.setGlobalFont(tvTitle);
		FontUtils.setFontSize(tvTitle, 36);
		FontUtils.setFontColor(tvTitle, Color.WHITE);
		FontUtils.setFontStyle(tvTitle, FontUtils.BOLD);
		this.addView(tvTitle);
		
		//btnWrite.
		btnWrite = new Button(getContext());
		ResizeUtils.viewResize(60, 82, btnWrite, 1, Gravity.CENTER_VERTICAL, new int[]{0, 0, 10, 0});
		btnWrite.setBackgroundResource(R.drawable.topicon_write_01);
		btnWrite.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(onWriteButtonClickedListener != null) {
					onWriteButtonClickedListener.onWriteButtonClicked();
				}
			}
		});
		this.addView(btnWrite);
		
		//btnN.
		btnN = new Button(getContext());
		ResizeUtils.viewResize(60, 82, btnN, 1, Gravity.CENTER_VERTICAL, new int[]{0, 0, 10, 0});
		btnN.setBackgroundResource(R.drawable.topicon_n_01);
		btnN.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(onNButtonClickedListener != null) {
					onNButtonClickedListener.onNButtonClicked();
				}
			}
		});
		this.addView(btnN);
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

	public Button getBtnN() {
		return btnN;
	}

	public TextView getTvTitle() {
		return tvTitle;
	}
	
	public void setTvTitle(TextView tvTitle) {
		this.tvTitle = tvTitle;
	}
	
	public void showCircleButton() {
		
		if(btnCircle.getVisibility() != View.VISIBLE) {
			btnCircle.setVisibility(View.VISIBLE);
		}
	}
	
	public void hideCircleButton() {
		
		if(btnCircle.getVisibility() == View.VISIBLE) {
			btnCircle.setVisibility(View.GONE);
		}
	}
	
	public void showHomeButton() {
		
		if(btnHome.getVisibility() != View.VISIBLE) {
			btnHome.setVisibility(View.VISIBLE);
		}
	}
	
	public void hideHomeButton() {
		
		if(btnHome.getVisibility() == View.VISIBLE) {
			btnHome.setVisibility(View.GONE);
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
	
	public void setOnNButtonClickedListener(OnNButtonClickedListener onNButtonClickedListener) {
		
		this.onNButtonClickedListener = onNButtonClickedListener;
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

	public interface OnNButtonClickedListener {
	
		public void onNButtonClicked();
	}
}
