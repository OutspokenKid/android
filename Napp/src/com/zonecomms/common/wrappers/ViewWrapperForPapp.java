package com.zonecomms.common.wrappers;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.zonecomms.napp.classes.ApplicationManager;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.zonecomms.common.views.WrapperView;
import com.zonecomms.napp.R;
import com.zonecomms.napp.WriteActivity;
import com.zonecomms.napp.classes.ViewWrapperForZonecomms;
import com.zonecomms.common.models.Papp;

public class ViewWrapperForPapp extends ViewWrapperForZonecomms {

	private static Papp selectedPapp;
	private static int selectedId;
	private static ViewWrapperForPapp selectedWrapper;
	
	private ImageView ivImage;
	private View cover;
	private View check;
	private boolean checked;
	
	private Papp app;
	
	public ViewWrapperForPapp(WrapperView row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			ivImage = (ImageView) row.findViewById(R.id.grid_app_ivImage);
			cover = row.findViewById(R.id.grid_app_cover);
			check = row.findViewById(R.id.grid_app_check);
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	@Override
	public void setSizes() {

		try {
			int length = ResizeUtils.getScreenWidth()/4;
			row.setLayoutParams(new AbsListView.LayoutParams(length, length));
			ResizeUtils.viewResize(50, 50, check, 2, Gravity.TOP|Gravity.RIGHT, new int[]{0, 0, 5, 0});
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {
		
		try {
			if(baseModel instanceof Papp) {
				app = (Papp) baseModel;
				
				if(app.isCheckable()) {
					check.setVisibility(View.VISIBLE);
				} else {
					check.setVisibility(View.INVISIBLE);
				}

				if(!app.isCheckable() || app.isSelectable()) {
					cover.setBackgroundColor(Color.TRANSPARENT);
				} else {
					cover.setBackgroundColor(Color.argb(127, 0, 0, 0));
				}
				
				String key = ApplicationManager.getDownloadKeyFromTopFragment();
				setImage(ivImage, app.getMedia_src(), key, 150);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	@Override
	public void setListeners() {

		try {
			cover.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					if(app.isCheckable() && app.isSelectable()) {

						//Click already checked.
						if(app.getSb_nid() == selectedId) {
							setChecked(false);
							selectedId = 0;
							selectedPapp = null;
							selectedWrapper = null;
						//Click not checked.
						} else {
							//Uncheck if other papp is checked.
							clearSelected();

							//Uncheck WriteActivity.cbPost if checked.
							WriteActivity.unCheckIfNeeded(v.getContext());
							
							//Check this papp.
							setChecked(true);
							selectedId = app.getSb_nid();
							selectedPapp = app;
							selectedWrapper = ViewWrapperForPapp.this;
						}
					}
				}
			});
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	public void setChecked(boolean checked) {
		
		this.checked = checked;
		
		try {
			if(checked) {
				check.setBackgroundResource(R.drawable.img_checkbox_on);
			} else {
				check.setBackgroundResource(R.drawable.img_checkbox_off);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public static int getSelectedId() {
		
		return selectedId;
	}
	
	public static Papp getSelectedPapp() {
		
		return selectedPapp;
	}
	
	public static void clearSelected() {
		
		if(selectedId != 0) {
			selectedId = 0;
		}
			
		if(selectedPapp != null) {
			selectedPapp = null;
		}
		
		if(selectedWrapper != null) {
			selectedWrapper.setChecked(false);
			selectedWrapper = null;
		}
	}
}
