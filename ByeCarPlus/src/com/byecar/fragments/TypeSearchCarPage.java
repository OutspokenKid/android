package com.byecar.fragments;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.View;
import android.widget.GridView;
import android.widget.ListView;

import com.byecar.byecarplus.R;
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
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ToastUtils;

public class TypeSearchCarPage extends BCPFragment {

	public static final int TYPE_BRAND = 1;
	public static final int TYPE_MODELGROUP = 2;
	public static final int TYPE_MODEL = 3;
	public static final int TYPE_TRIM = 4;
	public static final int TYPE_YEAR = 5;
	public static final int TYPE_MONTH = 6;
	
	private ListView listView;
	private GridView gridView;

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
			listView.setSelector(new ColorDrawable(getResources().getColor(R.color.titlebar_bg_orange)));
		}
	}

	@Override
	public void setListeners() {
		
	}

	@Override
	public void setSizes() {

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
					CarSearchString css = new CarSearchString(type, i + "년");
					css.setItemCode(BCPConstants.ITEM_CAR_TEXT);
					models.add(css);
				}
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
			break;
			
		case TYPE_MONTH:

			try {
				for(int i=1; i<13; i++) {
					CarSearchString css = new CarSearchString(type, i + "월");
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
}
