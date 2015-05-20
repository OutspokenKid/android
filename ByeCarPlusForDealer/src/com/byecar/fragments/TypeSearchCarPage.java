package com.byecar.fragments;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplusfordealer.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPAdapter;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPFragment;
import com.byecar.models.Brand;
import com.byecar.models.CarModel;
import com.byecar.models.CarModelGroup;
import com.byecar.models.CarSearchString;
import com.byecar.models.CarTrim;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.ToastUtils;

public class TypeSearchCarPage extends BCPFragment {

	public static final int TYPE_BRAND = 1;
	public static final int TYPE_MODELGROUP = 2;
	public static final int TYPE_MODEL = 3;
	public static final int TYPE_TRIM = 4;
	public static final int TYPE_YEAR = 5;
	public static final int TYPE_ACCIDENT = 6;
	public static final int TYPE_FUEL = 7;
	public static final int TYPE_TRANSMISSION = 8;
	public static final int TYPE_ONEMANOWNED = 9;
	public static final int TYPE_MONTH = 10;
	
	private ListView listView;
	private GridView gridView;
	
	private View cover;
	private FrameLayout popup;
	private TextView tvTitle;
	private Button btnClose;
	private EditText etHistory;
	private Button btnComplete;

	private int type;
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(models.size() == 0) {
			downloadInfo();
		}
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.typeSearchCarPage_titleBar);
		
		listView = (ListView) mThisView.findViewById(R.id.typeSearchCarPage_listView);
		gridView = (GridView) mThisView.findViewById(R.id.typeSearchCarPage_gridView);
		
		cover = mThisView.findViewById(R.id.typeSearchCarPage_cover);
		popup = (FrameLayout) mThisView.findViewById(R.id.typeSearchCarPage_popup);
		tvTitle = (TextView) mThisView.findViewById(R.id.typeSearchCarPage_tvTitle);
		btnClose = (Button) mThisView.findViewById(R.id.typeSearchCarPage_btnClose);
		etHistory = (EditText) mThisView.findViewById(R.id.typeSearchCarPage_etHistory);
		btnComplete = (Button) mThisView.findViewById(R.id.typeSearchCarPage_btnComplete);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {
			
			type = getArguments().getInt("type");
			
			if(type < TYPE_BRAND || type > TYPE_MONTH) {
				closePage();
			}
		} else {
			closePage();
		}
	}

	@Override
	public void createPage() {

		adapter = new BCPAdapter(mContext, mActivity, getActivity().getLayoutInflater(), models);
		
		if(type == TYPE_BRAND) {
			listView.setVisibility(View.INVISIBLE);
			gridView.setVisibility(View.VISIBLE);
			
			gridView.setAdapter(adapter);
		} else {
			listView.setVisibility(View.VISIBLE);
			gridView.setVisibility(View.INVISIBLE);
			
			listView.setAdapter(adapter);
			listView.setDividerHeight(0);
			listView.setDivider(null);
		}
	}

	@Override
	public void setListeners() {
		
		if(type == TYPE_ACCIDENT) {
			
			btnClose.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					hidePopup();
				}
			});

			btnComplete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					if(etHistory.length() == 0) {
						ToastUtils.showToast(R.string.checkChangingPartsHistory);
					} else {
						mActivity.bundle = new Bundle();
						mActivity.bundle.putInt("type", TYPE_ACCIDENT);
						mActivity.bundle.putString("text", getString(R.string.carSearchString_accident2));
						mActivity.bundle.putString("history", etHistory.getText().toString());
						mActivity.closeTopPage();
					}
				}
			});
			
			cover.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					hidePopup();
				}
			});
		}
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = (RelativeLayout.LayoutParams) popup.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(562);
		rp.height = ResizeUtils.getSpecificLength(657);
	
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 88, tvTitle, 2, 0, new int[]{0, 4, 0, 0});
		ResizeUtils.viewResize(60, 60, btnClose, 2, Gravity.RIGHT, new int[]{0, 20, 11, 0});
		ResizeUtils.viewResize(515, 420, etHistory, 2, Gravity.CENTER_HORIZONTAL, 
				new int[]{0, 112, 0, 0}, new int[]{14, 14, 14, 14});
		ResizeUtils.viewResize(514, 83, btnComplete, 2, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 
				new int[]{0, 0, 0, 23});
		
		FontUtils.setFontSize(tvTitle, 30);
		FontUtils.setFontSize(etHistory, 24);
	}

	@Override
	public int getContentViewId() {
		
		return R.layout.fragment_common_type_search_car;
	}
	
	@Override
	public int getPageTitleTextResId() {
		
		switch(type) {
		
		case TYPE_BRAND:
			return R.string.pageTitle_typeSearchCar_brand;
			
		case TYPE_MODELGROUP:
			return R.string.pageTitle_typeSearchCar_modelGroup;
			
		case TYPE_MODEL:
			return R.string.pageTitle_typeSearchCar_model;
			
		case TYPE_TRIM:
			return R.string.pageTitle_typeSearchCar_trim;
			
		case TYPE_YEAR:
			return R.string.pageTitle_typeSearchCar_year;
		
		case TYPE_ACCIDENT:
			return R.string.pageTitle_typeSearchCar_accident;
			
		case TYPE_FUEL:
			return R.string.pageTitle_typeSearchCar_fuel;
			
		case TYPE_TRANSMISSION:
			return R.string.pageTitle_typeSearchCar_transmission;
			
		case TYPE_ONEMANOWNED:
			return R.string.pageTitle_typeSearchCar_oneman;
			
		case TYPE_MONTH:
			return R.string.pageTitle_typeSearchCar_month;
		}

		return 0;
	}

	@Override
	public void downloadInfo() {

		switch(type) {
		
		case TYPE_BRAND:
			url = BCPAPIs.SEARCH_CAR_BRAND
					+ "?num=0";
			super.downloadInfo();
			break;
			
		case TYPE_MODELGROUP:
			url = BCPAPIs.SEARCH_CAR_MODELGROUP
					+ "?num=0"
					+ "&brand_id=" + getArguments().getInt("brand_id");
			super.downloadInfo();
			break;
			
		case TYPE_MODEL:
			url = BCPAPIs.SEARCH_CAR_MODEL
					+ "?num=0"
					+ "&modelgroup_id=" + getArguments().getInt("modelgroup_id");
			super.downloadInfo();
			break;
			
		case TYPE_TRIM:
			url = BCPAPIs.SEARCH_CAR_TRIM
					+ "?num=0"
					+ "&model_id=" + getArguments().getInt("model_id");
			super.downloadInfo();
			break;
			
		case TYPE_YEAR:

			try {
				int year_begin = getArguments().getInt("year_begin");
				int year_end = getArguments().getInt("year_end") + 1;
				
				for(int i=year_begin; i<year_end; i++) {
					CarSearchString css = new CarSearchString(type, "" + i);
					css.setItemCode(BCPConstants.ITEM_CAR_TEXT);
					models.add(css);
				}
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
			break;
			
		case TYPE_ACCIDENT:
			CarSearchString css1 = new CarSearchString(type, getString(R.string.carSearchString_accident1));
			css1.setItemCode(BCPConstants.ITEM_CAR_TEXT);
			models.add(css1);
			
			CarSearchString css2 = new CarSearchString(type, getString(R.string.carSearchString_accident2));
			css2.setItemCode(BCPConstants.ITEM_CAR_TEXT);
			models.add(css2);
			
			CarSearchString css3 = new CarSearchString(type, getString(R.string.carSearchString_accident3));
			css3.setItemCode(BCPConstants.ITEM_CAR_TEXT);
			models.add(css3);
			break;
			
		case TYPE_FUEL:
			css1 = new CarSearchString(type, getString(R.string.carSearchString_fuel1));
			css1.setItemCode(BCPConstants.ITEM_CAR_TEXT);
			models.add(css1);
			
			css2 = new CarSearchString(type, getString(R.string.carSearchString_fuel2));
			css2.setItemCode(BCPConstants.ITEM_CAR_TEXT);
			models.add(css2);
			
			css3 = new CarSearchString(type, getString(R.string.carSearchString_fuel3));
			css3.setItemCode(BCPConstants.ITEM_CAR_TEXT);
			models.add(css3);
			
			CarSearchString css4 = new CarSearchString(type, getString(R.string.carSearchString_fuel4));
			css4.setItemCode(BCPConstants.ITEM_CAR_TEXT);
			models.add(css4);
			break;
			
		case TYPE_TRANSMISSION:
			css1 = new CarSearchString(type, getString(R.string.carSearchString_transmission1));
			css1.setItemCode(BCPConstants.ITEM_CAR_TEXT);
			models.add(css1);
			
			css2 = new CarSearchString(type, getString(R.string.carSearchString_transmission2));
			css2.setItemCode(BCPConstants.ITEM_CAR_TEXT);
			models.add(css2);
			break;
			
		case TYPE_ONEMANOWNED:
			css1 = new CarSearchString(type, getString(R.string.carSearchString_oneManOwned1));
			css1.setItemCode(BCPConstants.ITEM_CAR_TEXT);
			models.add(css1);
			
			css2 = new CarSearchString(type, getString(R.string.carSearchString_oneManOwned2));
			css2.setItemCode(BCPConstants.ITEM_CAR_TEXT);
			models.add(css2);
			
			css3 = new CarSearchString(type, getString(R.string.carSearchString_oneManOwned3));
			css3.setItemCode(BCPConstants.ITEM_CAR_TEXT_DESC);
			models.add(css3);
			break;
			
		case TYPE_MONTH:

			try {
				for(int i=1; i<13; i++) {
					CarSearchString css = new CarSearchString(type, "" + i);
					css.setItemCode(BCPConstants.ITEM_CAR_TEXT);
					models.add(css);
				}
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
			break;
		}
	}
	
	@Override
	public boolean parseJSON(JSONObject objJSON) {

		try {
			JSONArray arJSON = null;
			int size = 0;
			
			switch(type) {
			
			case TYPE_BRAND:
				arJSON = objJSON.getJSONArray("brands");
				size = arJSON.length();
				
				for(int i=0; i<size; i++) {
					Brand brand = new Brand(arJSON.getJSONObject(i));
					brand.setItemCode(BCPConstants.ITEM_CAR_BRAND);
					models.add(brand);
				}
				
				break;
				
			case TYPE_MODELGROUP:
				arJSON = objJSON.getJSONArray("modelgroups");
				size = arJSON.length();
				
				for(int i=0; i<size; i++) {
					CarModelGroup group = new CarModelGroup(arJSON.getJSONObject(i));
					group.setItemCode(BCPConstants.ITEM_CAR_TEXT);
					models.add(group);
				}
				break;
				
			case TYPE_MODEL:
				arJSON = objJSON.getJSONArray("models");
				size = arJSON.length();
				
				for(int i=0; i<size; i++) {
					CarModel model = new CarModel(arJSON.getJSONObject(i));
					model.setItemCode(BCPConstants.ITEM_CAR_TEXT);
					models.add(model);
				}
				break;
				
			case TYPE_TRIM:
				arJSON = objJSON.getJSONArray("trims");
				size = arJSON.length();
				
				for(int i=0; i<size; i++) {
					CarTrim transmission = new CarTrim(arJSON.getJSONObject(i));
					transmission.setItemCode(BCPConstants.ITEM_CAR_TEXT);
					models.add(transmission);
				}
				break;
			}
			
			return true;
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		return false;
	}

	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onBackPressed() {

		if(popup.getVisibility() == View.VISIBLE) {
			hidePopup();
			return true;
		}
		return false;
	}

	@Override
	public int getRootViewResId() {

		return R.id.typeSearchCarPage_mainLayout;
	}
	
//////////////////// Custom methods.
	
	public void closePage() {

		ToastUtils.showToast(R.string.failToLoadList);
		
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				if(mActivity != null) {
					mActivity.closeTopPage();
				}
			}
		}, 1000);
	}

	public void showPopup() {
		
		AlphaAnimation aaIn = new AlphaAnimation(0, 1);
		aaIn.setDuration(300);

		cover.setVisibility(View.VISIBLE);
		cover.startAnimation(aaIn);
		popup.setVisibility(View.VISIBLE);
		popup.startAnimation(aaIn);
		
	}
	
	public void hidePopup() {
		
		AlphaAnimation aaOut = new AlphaAnimation(1, 0);
		aaOut.setDuration(300);
		
		cover.startAnimation(aaOut);
		cover.setVisibility(View.INVISIBLE);
		popup.startAnimation(aaOut);
		popup.setVisibility(View.INVISIBLE);

		mThisView.postDelayed(new Runnable() {

			@Override
			public void run() {

				SoftKeyboardUtils.hideKeyboard(mContext, etHistory);
			}
		}, 300);
	}
}
