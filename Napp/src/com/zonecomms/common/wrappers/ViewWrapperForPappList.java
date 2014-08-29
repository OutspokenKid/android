package com.zonecomms.common.wrappers;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.StringUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

import com.outspoken_kid.classes.FontInfo;
import com.zonecomms.napp.classes.ApplicationManager;
import com.outspoken_kid.utils.IntentUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.PackageUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.zonecomms.common.views.WrapperView;
import com.zonecomms.common.models.Papp;
import com.zonecomms.napp.BaseFragmentActivity.OnPositiveClickedListener;
import com.zonecomms.napp.R;
import com.zonecomms.napp.classes.ViewWrapperForZonecomms;
import com.zonecomms.napp.classes.ZoneConstants;

public class ViewWrapperForPappList extends ViewWrapperForZonecomms {

	private ImageView ivImage;
	private TextView tvTitle;
	private TextView tvSubTitle;
	private Papp papp;
	
	public ViewWrapperForPappList(WrapperView row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		bg = row.findViewById(R.id.list_papp_bg);
		
		ivImage = (ImageView) row.findViewById(R.id.list_papp_ivImage);
		tvTitle = (TextView) row.findViewById(R.id.list_papp_tvTitle);
		tvSubTitle = (TextView) row.findViewById(R.id.list_papp_tvSubTitle);
	}

	@Override
	public void setSizes() {

		int p = ResizeUtils.getSpecificLength(8);
		AbsListView.LayoutParams ap = new AbsListView.LayoutParams(ResizeUtils.getSpecificLength(640), 
				ResizeUtils.getSpecificLength(158));
		row.setLayoutParams(ap);
		row.setPadding(p, p, p, 0);
		
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 150, bg, 2, Gravity.CENTER_VERTICAL, null);
		
		ResizeUtils.viewResize(150, 150, row.findViewById(R.id.list_papp_imageBg), 2, Gravity.CENTER_VERTICAL, null);
		ResizeUtils.viewResize(150, 150, ivImage, 2, Gravity.CENTER_VERTICAL, null);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 50, tvTitle, 2, Gravity.LEFT, new int[]{170, 10, 60, 0});
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 80, tvSubTitle, 2, Gravity.LEFT, new int[]{170, 60, 60, 0});
		
		ResizeUtils.viewResize(25, 42, row.findViewById(R.id.list_papp_arrow), 2, 
				Gravity.CENTER_VERTICAL|Gravity.RIGHT, new int[]{0, 0, 20, 0});
		
		FontInfo.setFontSize(tvTitle, 32);
		FontInfo.setFontSize(tvSubTitle, 28);
	}

	@Override
	public void setListeners() {

		if(papp != null) {
			if(!StringUtils.isEmpty(papp.getSb_id())) {
				
				row.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {

						row.post(new Runnable() {
							
							@Override
							public void run() {
								
								final String packageName = "com.zonecomms." + papp.getSb_id();
								final boolean installed = PackageUtils.checkApplicationInstalled(
										ApplicationManager.getInstance().getActivity(), packageName);
								
								String title = row.getContext().getString(R.string.moveToPartnerApp);
								String message = "";
								OnPositiveClickedListener opcl = new OnPositiveClickedListener() {
									
									@Override
									public void onPositiveClicked() {
										
										boolean success = false;
										
										if(installed) {
											String id = SharedPrefsUtils.getStringFromPrefs(ZoneConstants.PREFS_SIGN, "id");
											String pw = SharedPrefsUtils.getStringFromPrefs(ZoneConstants.PREFS_SIGN, "pw");
											
											if(!StringUtils.isEmpty("id") && !StringUtils.isEmpty("pw")) {
												IntentUtils.invokeApp(row.getContext(), packageName, id, pw, null);
											} else{
												IntentUtils.invokeApp(row.getContext(), packageName, null);
											}
											success = IntentUtils.invokeApp(row.getContext(), packageName);
										} else {
											success = IntentUtils.showMarket(row.getContext(), packageName);
										}
										
										if(!success) {
											ToastUtils.showToast(R.string.unknownPackageName);
										}
									}
								};
								
								if(installed) {
									message = row.getContext().getString(R.string.wannaLaunchPartnerApp);
								} else {
									message = row.getContext().getString(R.string.wannaMoveToStore);
								}
								
								ApplicationManager.getInstance().getActivity().showAlertDialog(title, message, opcl);
							}
						});
					}
				});
			}
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		if(ivImage != null) {
			ivImage.setVisibility(View.GONE);
		}
		
		try {
			if(baseModel != null && baseModel instanceof Papp) {
				papp = (Papp) baseModel;
				
				if(!StringUtils.isEmpty(papp.getSb_nickname())) {
					FontInfo.setFontStyle(tvTitle, FontInfo.BOLD);
					tvTitle.setText(papp.getSb_nickname());
				}
				
				if(!StringUtils.isEmpty(papp.getSb_description())) {
					tvSubTitle.setText(papp.getSb_description());
				}
				
				String key = ApplicationManager.getDownloadKeyFromTopFragment();
				setImage(ivImage, papp.getMedia_src(), key, 320);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
}
