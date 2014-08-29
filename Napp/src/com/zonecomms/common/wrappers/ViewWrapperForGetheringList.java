package com.zonecomms.common.wrappers;

import android.graphics.Color;
import android.net.Uri;

import com.outspoken_kid.utils.LogUtils;
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
import com.outspoken_kid.utils.ResizeUtils;
import com.zonecomms.common.views.WrapperView;
import com.zonecomms.common.models.Gethering;
import com.zonecomms.napp.IntentHandlerActivity;
import com.zonecomms.napp.R;
import com.zonecomms.napp.classes.ViewWrapperForZonecomms;
import com.zonecomms.napp.classes.ZoneConstants;

public class ViewWrapperForGetheringList extends ViewWrapperForZonecomms {
	
	private View imageBg;
	private ImageView ivImage;
	private TextView tvGetheringName;
	private TextView tvIntroduce;
	private TextView tvOwner;
	private View publicGethering;
	
	private Gethering gethering;
	
	public ViewWrapperForGetheringList(WrapperView row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		imageBg = row.findViewById(R.id.list_gethering_imageBg);
		ivImage = (ImageView) row.findViewById(R.id.list_gethering_ivImage);
		tvGetheringName = (TextView) row.findViewById(R.id.list_gethering_tvGetheringName);
		tvIntroduce = (TextView) row.findViewById(R.id.list_gethering_tvIntroduce);
		tvOwner = (TextView) row.findViewById(R.id.list_gethering_tvOwner);
		publicGethering = row.findViewById(R.id.list_gethering_publicGethering);
	}

	@Override
	public void setSizes() {

		int p = ResizeUtils.getSpecificLength(8);
		AbsListView.LayoutParams ap = new AbsListView.LayoutParams(ResizeUtils.getSpecificLength(640), 
				ResizeUtils.getSpecificLength(158));
		row.setLayoutParams(ap);
		row.setPadding(0, p, p, 0);
		
		ResizeUtils.viewResize(150, 150, imageBg, 2, Gravity.CENTER_VERTICAL, null);
		ResizeUtils.viewResize(150, 150, ivImage, 2, Gravity.CENTER_VERTICAL, null);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 50, tvGetheringName, 2, Gravity.LEFT, new int[]{158, 0, 0, 0});
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 50, tvIntroduce, 2, Gravity.LEFT, new int[]{158, 50, 0, 0});
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 50, tvOwner, 2, Gravity.LEFT, new int[]{158, 100, 0, 0});
		ResizeUtils.viewResize(50, 50, publicGethering, 2, Gravity.RIGHT, new int[]{0, 0, 8, 0});

		int maxWidth = ResizeUtils.getSpecificLength(420);
		tvGetheringName.setMaxWidth(maxWidth);
		tvIntroduce.setMaxWidth(maxWidth);
		tvOwner.setMaxWidth(maxWidth);
		
		FontInfo.setFontSize(tvGetheringName, 32);
		FontInfo.setFontSize(tvIntroduce, 32);
		FontInfo.setFontSize(tvOwner, 32);
	}

	@Override
	public void setListeners() {
		
		row.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(gethering != null) {
					String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/gethering"
							+ "?sb_id=" + gethering.getSb_id();
					IntentHandlerActivity.actionByUri(Uri.parse(uriString));
				}
			}
		});
	}

	@Override
	public void setValues(com.outspoken_kid.model.BaseModel baseModel) {

		if(ivImage != null) {
			ivImage.setVisibility(View.GONE);
		}
		
		try {
			if(baseModel != null && baseModel instanceof Gethering) {
				gethering = (Gethering) baseModel;
				
				if(!StringUtils.isEmpty(gethering.getSb_nickname())) {
					FontInfo.setFontStyle(tvGetheringName, FontInfo.BOLD);
					tvGetheringName.setText(gethering.getSb_nickname());
				} else {
					tvGetheringName.setText("");
				}
				
				if(!StringUtils.isEmpty(gethering.getSb_description())) {
					tvIntroduce.setText(gethering.getSb_description());
				} else {
					tvIntroduce.setText("");
				}
				
				String ownerString = "";
				
				if(!StringUtils.isEmpty(gethering.getReg_nickname())) {
					ownerString += gethering.getReg_nickname();
				}
				
				if(!StringUtils.isEmpty(gethering.getReg_id())) {
					ownerString += "(" + gethering.getReg_id() + ")";
				}
				
				tvOwner.setText(ownerString);
				
				if(gethering.isOwner()) {
					tvGetheringName.setTextColor(Color.YELLOW);
					tvIntroduce.setTextColor(Color.YELLOW);
					tvOwner.setTextColor(Color.YELLOW);
				} else {
					tvGetheringName.setTextColor(Color.WHITE);
					tvIntroduce.setTextColor(Color.WHITE);
					tvOwner.setTextColor(Color.WHITE);
				}
				
				if(gethering.getSb_public() == 1) {
					publicGethering.setBackgroundResource(R.drawable.img_unlocked_small);
				} else {
					publicGethering.setBackgroundResource(R.drawable.img_locked_small);
				}
				
				String key = ApplicationManager.getDownloadKeyFromTopFragment();
				setImage(ivImage, gethering.getMedia_src(), key, 320);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
}
