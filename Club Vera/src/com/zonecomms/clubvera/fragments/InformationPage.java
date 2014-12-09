package com.zonecomms.clubvera.fragments;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.views.holo.HoloConstants;
import com.zonecomms.clubvera.IntentHandlerActivity;
import com.zonecomms.clubvera.R;
import com.zonecomms.clubvera.classes.ZoneConstants;
import com.zonecomms.clubvera.classes.ZonecommsFragment;
import com.zonecomms.common.models.VIPFloorInfo;
import com.zonecomms.common.utils.AppInfoUtils;
import com.zonecomms.common.views.TitleBar;

public class InformationPage extends ZonecommsFragment {

	private RelativeLayout relative;
	private LinearLayout VIPInfoLayout;
	
	private VIPFloorInfo[] VIPInfos;
	
	@Override
	public void bindViews() {

		relative = (RelativeLayout) mThisView.findViewById(R.id.informationPage_relative);
	}

	@Override
	public void setVariables() {
		super.setVariables();
		
		title = getString(R.string.app_name);
	}

	@Override
	public void createPage() {

		madeCount += 20;
		
		int l = ResizeUtils.getSpecificLength(150);
		int s = ResizeUtils.getSpecificLength(8);
		int p = ResizeUtils.getSpecificLength(20);

		RelativeLayout.LayoutParams rp = null;

		//titleFrame.  id : 0
		FrameLayout titleFrame = new FrameLayout(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		rp.leftMargin = s;
		rp.topMargin = s;
		rp.rightMargin = s;
		rp.bottomMargin = s;
		titleFrame.setLayoutParams(rp);
		titleFrame.setId(madeCount);
		titleFrame.setBackgroundColor(TitleBar.titleBarColor);
		relative.addView(titleFrame);
		
		View titleBg = new View(mContext);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 
				titleBg, 2, 0, new int[]{2, 2, 2, 2});
		titleBg.setBackgroundColor(Color.WHITE);
		titleFrame.addView(titleBg);
		
		View titleImage = new View(mContext);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 
				titleImage, 2, 0, null);
		titleImage.setBackgroundResource(R.drawable.btn_club_logo);
		titleFrame.addView(titleImage);
		
		//textContainer.
		FrameLayout textContainer = new FrameLayout(mContext);
		rp = new RelativeLayout.LayoutParams(l*3 + s*2, l);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount);
		textContainer.setLayoutParams(rp);
		textContainer.setBackgroundColor(TitleBar.titleBarColor);
		relative.addView(textContainer);
		
		View textContainerBg = new View(mContext);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 
				textContainerBg, 2, 0, new int[]{2, 2, 2, 2});
		textContainerBg.setBackgroundColor(Color.WHITE);
		textContainer.addView(textContainerBg);
		
		//Name.
		TextView tvName = new TextView(mContext);
		tvName.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 
				Gravity.TOP|Gravity.LEFT));
		tvName.setTextColor(HoloConstants.COLOR_HOLO_TEXT);
		tvName.setText(R.string.app_name);
		tvName.setPadding(p, p, p, p);
		FontUtils.setFontSize(tvName, 30);
		FontUtils.setFontStyle(tvName, FontUtils.BOLD);
		textContainer.addView(tvName);
		
		//Address.
		TextView tvAddress = new TextView(mContext);
		tvAddress.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 
				Gravity.BOTTOM|Gravity.LEFT));
		tvAddress.setTextColor(HoloConstants.COLOR_HOLO_TEXT);
		tvAddress.setText(R.string.clubAddress);
		tvAddress.setPadding(p, p, p, p);
		FontUtils.setFontSize(tvAddress, 26);
		FontUtils.setFontStyle(tvAddress, FontUtils.BOLD);
		textContainer.addView(tvAddress);

		View locationBg = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount);
		rp.addRule(RelativeLayout.BELOW, madeCount);
		rp.rightMargin = s;
		rp.bottomMargin = s;
		locationBg.setLayoutParams(rp);
		locationBg.setBackgroundColor(TitleBar.titleBarColor);
		relative.addView(locationBg);
		
		//Location.  id : 1
		View location = new View(mContext);
		location.setLayoutParams(rp);
		location.setId(madeCount + 1);
		location.setBackgroundResource(R.drawable.btn_club_location);
		location.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				mainActivity.showLocationWithNaverMap();
			}
		});
		relative.addView(location);
		
		View callBg = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 1);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 1);
		rp.rightMargin = s;
		callBg.setLayoutParams(rp);
		callBg.setBackgroundColor(TitleBar.titleBarColor);
		relative.addView(callBg);
		
		//Call.  id : 2
		View call = new View(mContext);
		call.setLayoutParams(rp);
		call.setId(madeCount + 2);
		call.setBackgroundResource(R.drawable.btn_club_call);
		call.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				try {
					Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + getString(R.string.textForCall)));
					mainActivity.startActivity(intent);
				} catch(Exception e) {
					LogUtils.trace(e);
				}
			}
		});
		relative.addView(call);
		
		View fbBg = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 2);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 2);
		rp.rightMargin = s;
		fbBg.setLayoutParams(rp);
		fbBg.setBackgroundColor(TitleBar.titleBarColor);
		relative.addView(fbBg);
		
		//Facebook.  id : 3
		View facebook = new View(mContext);
		facebook.setLayoutParams(rp);
		facebook.setId(madeCount + 3);
		facebook.setBackgroundResource(R.drawable.btn_club_fb);
		facebook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				IntentHandlerActivity.actionByUri(Uri.parse(getString(R.string.textForFacebook)));
			}
		});
		relative.addView(facebook);
		
		View cafeBg = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 3);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 3);
		rp.rightMargin = s;
		cafeBg.setLayoutParams(rp);
		cafeBg.setBackgroundColor(TitleBar.titleBarColor);
		relative.addView(cafeBg);
		
		//Cafe.
		View cafe = new View(mContext);
		cafe.setLayoutParams(rp);
		cafe.setBackgroundResource(R.drawable.btn_club_cafe);
		cafe.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				IntentHandlerActivity.actionByUri(Uri.parse(getString(R.string.textForCafe)));
			}
		});
		relative.addView(cafe);
		
		//Banner.  id : 4
		View banner = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l*4 + s*3, l*2 + s);
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 1);
		rp.addRule(RelativeLayout.BELOW, madeCount + 1);
		rp.rightMargin = s;
		rp.bottomMargin = s;
		banner.setLayoutParams(rp);
		banner.setId(madeCount + 4);
		banner.setBackgroundResource(R.drawable.img_clubphoto);
		banner.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				mainActivity.showImageViewerActivity(getString(R.string.app_name), 
						R.drawable.img_clubphoto);
			}
		});
		relative.addView(banner);
		
		//VIP info.	id : 5
		VIPInfoLayout = new LinearLayout(mContext);
		rp = new RelativeLayout.LayoutParams(l*4 + s*3, ResizeUtils.getSpecificLength(50));
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 4);
		rp.addRule(RelativeLayout.BELOW, madeCount + 4);
		VIPInfoLayout.setLayoutParams(rp);
		VIPInfoLayout.setId(madeCount + 5);
		VIPInfoLayout.setBackgroundColor(Color.WHITE);
		VIPInfoLayout.setPadding(p, 0, p, 0);
		VIPInfoLayout.setVisibility(View.GONE);
		relative.addView(VIPInfoLayout);
		
		FrameLayout introduceFrame = new FrameLayout(mContext);
		rp = new RelativeLayout.LayoutParams(l*4 + s*3, LayoutParams.MATCH_PARENT);
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 5);
		rp.addRule(RelativeLayout.BELOW, madeCount + 5);
		rp.bottomMargin = s;
		introduceFrame.setLayoutParams(rp);
		introduceFrame.setBackgroundColor(TitleBar.titleBarColor);
		relative.addView(introduceFrame);
		
		//Introduce.
		TextView tvIntroduce = new TextView(mContext);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 
				tvIntroduce, 2, 0, new int[]{2, 2, 2, 2});
		tvIntroduce.setBackgroundColor(Color.WHITE);
		tvIntroduce.setLineSpacing(0, 1.5f);
		tvIntroduce.setPadding(p, p, p, p);
		tvIntroduce.setText(R.string.textForIntroduce);
		tvIntroduce.setTextColor(HoloConstants.COLOR_HOLO_TEXT);
		FontUtils.setFontSize(tvIntroduce, 26);
		FontUtils.setFontStyle(tvIntroduce, FontUtils.BOLD);
		introduceFrame.addView(tvIntroduce);
	}

	@Override
	public void setListeners() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSizes() {
		// TODO Auto-generated method stub

	}

	@Override
	public void downloadInfo() {

		super.downloadInfo();
		
		String url = ZoneConstants.BASE_URL + "link/list" +
				"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
				"&link_type=7" +
				"&image_size=640";
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {
				
				if(VIPInfoLayout != null) {
					VIPInfoLayout.setVisibility(View.GONE);
				}
				
				VIPInfos = null;
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("from.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.has("data")) {
						JSONArray arJSON = objJSON.getJSONArray("data");
						
						int size = arJSON.length();
						
						if(size != 0) {
							VIPInfos = new VIPFloorInfo[size];
						}
						
						for(int i=0; i<size; i++) {
							VIPInfos[i] = new VIPFloorInfo(arJSON.getJSONObject(i));
						}
					}
					
					setPage(true);
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}

	@Override
	public void setPage(boolean successDownload) {

		super.setPage(successDownload);
		
		if(successDownload && VIPInfoLayout != null 
				&& VIPInfos != null && VIPInfos.length > 0) {
			VIPInfoLayout.removeAllViews();
			
			TextView tv = new TextView(mContext);
			tv.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1));
			tv.setGravity(Gravity.CENTER_VERTICAL);
			tv.setTextColor(Color.WHITE);
			tv.setText(R.string.VIPTableInfo);
			FontUtils.setFontSize(tv, 30);
			VIPInfoLayout.addView(tv);
			
			int size = VIPInfos.length;
			for(int i=0; i<size; i++) {
				
				try {
					final int I = i;
					
					TextView tvFloor = new TextView(mContext);
					ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, tvFloor, 1, 
							Gravity.CENTER_VERTICAL, null, new int[]{15, 0, 15, 0});
					tvFloor.setGravity(Gravity.CENTER_VERTICAL);
					tvFloor.setTextColor(Color.WHITE);
					tvFloor.setText(VIPInfos[i].getFloor());
					FontUtils.setFontSize(tvFloor, 30);
					tvFloor.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							if(VIPInfos[I].getLink_datas() != null && VIPInfos[I].getLink_datas().length > 0) {
								VIPFloorInfo vfi = VIPInfos[I];
								mainActivity.showImageViewerActivity(vfi.getTitle(), vfi.getLink_datas(), vfi.getThumbnail(), I);
							}
						}
					});
					VIPInfoLayout.addView(tvFloor);
					
				} catch(Exception e) {
					LogUtils.trace(e);
				}
			}
			
			if(VIPInfoLayout.getChildCount() > 1) {
				VIPInfoLayout.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public int getContentViewId() {

		return R.layout.page_information;
	}
	
	@Override
	public void hideLoadingView() {

		mainActivity.hideLoadingView();
	}

	@Override
	public void showLoadingView() {

		mainActivity.showLoadingView();
	}
	
	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refreshPage() {
		
	}

	@Override
	public void onResume() {
		super.onResume();
		
		mainActivity.showTitleBar();
		mainActivity.getTitleBar().hideCircleButton();
		mainActivity.getTitleBar().showHomeButton();
		mainActivity.getTitleBar().hideWriteButton();
		
		if(mainActivity.getSponserBanner() != null) {
			mainActivity.getSponserBanner().downloadBanner();
		}
		
		if(!isDownloading) {
			downloadInfo();
		}
	}
}
