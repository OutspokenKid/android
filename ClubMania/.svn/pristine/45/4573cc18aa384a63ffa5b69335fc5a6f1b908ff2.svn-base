package com.zonecomms.common.wrappers;

import android.graphics.Color;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.outspoken_kid.classes.ApplicationManager;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.zonecomms.clubmania.IntentHandlerActivity;
import com.zonecomms.clubmania.R;
import com.zonecomms.clubmania.WriteActivity;
import com.zonecomms.clubmania.classes.ZoneConstants;
import com.zonecomms.common.models.Papp;
import com.zonecomms.common.models.BaseModel;
import com.zonecomms.common.utils.ImageDownloadUtils;
import com.zonecomms.common.wrapperviews.WrapperView;

public class ViewWrapperForPapp extends ViewWrapper {

	private static Papp selectedPapp;
	private static int selectedId;
	private static ViewWrapperForPapp selectedWrapper;
	
	private ImageView ivImage;
	private View cover;
	private CheckBox checkBox;
	
	private Papp app;
	
	public ViewWrapperForPapp(WrapperView row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			ivImage = (ImageView) row.findViewById(R.id.grid_app_ivImage);
			cover = row.findViewById(R.id.grid_app_cover);
			checkBox = (CheckBox) row.findViewById(R.id.grid_app_checkBox);
		} catch(Exception e) {
			e.printStackTrace();
			setUnusableView();
		}
	}

	@Override
	public void setSize() {

		try {
			int length = ResizeUtils.getScreenWidth()/4;
			row.setLayoutParams(new AbsListView.LayoutParams(length, length));
			ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, checkBox, 2, Gravity.TOP|Gravity.RIGHT, new int[]{0, 0, 5, 0});
		} catch(Exception e) {
			e.printStackTrace();
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {
		
		try {
			if(baseModel instanceof Papp) {
				app = (Papp) baseModel;
				
				if(app.isCheckable()) {
					checkBox.setVisibility(View.VISIBLE);
				} else {
					checkBox.setVisibility(View.INVISIBLE);
				}

				if(!app.isCheckable() || app.isSelectable()) {
					cover.setBackgroundColor(Color.TRANSPARENT);
				} else {
					cover.setBackgroundColor(Color.argb(127, 0, 0, 0));
				}

				String key = ApplicationManager.getDownloadKeyFromTopFragment();
				ImageDownloadUtils.downloadImageImmediately(app.getMedia_src(), key, ivImage, 150, true);
			} else {
				setUnusableView();
			}
		} catch(Exception e) {
			e.printStackTrace();
			setUnusableView();
		}
	}
	
	@Override
	public void setListener() {

		try {
			cover.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					LogUtils.log("papp clicked.  isCheckable : " + app.isCheckable() + "  isSelectable : "+ app.isSelectable());

					switch(app.getType()) {
					
					case Papp.TYPE_WRITE:
						if(app.isCheckable() && app.isSelectable()) {
							
							//Click already checked.
							if(app.getSb_nid() == selectedId) {
								checkBox.setChecked(false);
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
								checkBox.setChecked(true);
								selectedId = app.getSb_nid();
								selectedPapp = app;
								selectedWrapper = ViewWrapperForPapp.this;
							}
						}
						break;
						
					case Papp.TYPE_PHOTO:
						String title = app.getSb_nickname() + " " + row.getContext().getString(R.string.photo);
						String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/image" +
								"?title=" + title +
								"&sb_id=" + app.getSb_id();
						IntentHandlerActivity.actionByUri(Uri.parse(uriString));
						break;
						
					case Papp.TYPE_SCHEDULE:
						title = app.getSb_nickname() + " " + row.getContext().getString(R.string.schedule);
						uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/schedule" +
								"?title=" + title +
								"&sb_id=" + app.getSb_id();
						IntentHandlerActivity.actionByUri(Uri.parse(uriString));
						break;
					}
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
			setUnusableView();
		}
	}
	
	@Override
	public void setUnusableView() {
		// TODO Auto-generated method stub
		
	}

	public void check() {
		
		if(checkBox != null) {
			checkBox.setChecked(true);
		}
	}
	
	public void unCheck() {
		
		if(checkBox != null) {
			checkBox.setChecked(false);	
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
			selectedWrapper.unCheck();
			selectedWrapper = null;
		}
	}
}
