package com.byecar.byecarplus.fragments.main_for_user;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.byecarplus.classes.BCPAPIs;
import com.byecar.byecarplus.classes.BCPAdapter;
import com.byecar.byecarplus.classes.BCPConstants;
import com.byecar.byecarplus.classes.BCPFragment;
import com.byecar.byecarplus.models.Brand;
import com.byecar.byecarplus.models.CarModel;
import com.byecar.byecarplus.models.CarModelGroup;
import com.byecar.byecarplus.models.CarTrim;
import com.byecar.byecarplus.views.TitleBar;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class SearchCarPage extends BCPFragment {

	private static final int MIN_PRICE = 0;
	private static final int MIDDLE_PRICE = 75000000;
	private static final int MAX_PRICE = 150000000;
	
	public static final int TYPE_BRAND = 1;
	public static final int TYPE_MODELGROUP = 2;
	public static final int TYPE_MODEL = 3;
	public static final int TYPE_TRIM = 4;
	
	private Button btnCommon;
	private Button btnDetail;

	private RelativeLayout relativeForCommonSearch;
	private TextView tvPriceRangeText;
	private View priceRangeLine;
	private TextView tvMinPriceText;
	private TextView tvMiddlePriceText;
	private TextView tvMaxPriceText;
	private FrameLayout frameForRelatives;
	private RelativeLayout relativeForMinPrice;
	private TextView tvMinPrice;
	private View arrowForMinPrice;
	private RelativeLayout relativeForMaxPrice;
	private TextView tvMaxPrice;
	private View arrowForMaxPrice;
	private View editTextBg;
	private EditText etMinPrice;
	private EditText etMaxPrice;
	private Button btnShowCommonSearchResult;
	
	private GridView gridView;
	private BCPAdapter gridAdapter;
	
	private ListView listView;
	private BCPAdapter listAdapter;
	
	private RelativeLayout relativeForSearchResult;
	private TextView tvSearchConditionText;
	private TextView tvSearchCondition;
	private Button btnShowDetailSearchResult;

	private int type;
	private int menuIndex;
	private int minPrice;
	private int maxPrice;
	private String conditionString;
	
	private ArrayList<BaseModel> modelsForGrid = new ArrayList<BaseModel>();
	private ArrayList<BaseModel> modelsForList = new ArrayList<BaseModel>();
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.searchCarPage_titleBar);
		
		btnCommon = (Button) mThisView.findViewById(R.id.searchCarPage_btnCommon);
		btnDetail = (Button) mThisView.findViewById(R.id.searchCarPage_btnDetail);
		
		relativeForCommonSearch = (RelativeLayout) mThisView.findViewById(R.id.searchCarPage_relativeForCommonSearch);
		tvPriceRangeText = (TextView) mThisView.findViewById(R.id.searchCarPage_tvPriceRangeText);
		priceRangeLine = mThisView.findViewById(R.id.searchCarPage_priceRangeLine);
		tvMinPriceText = (TextView) mThisView.findViewById(R.id.searchCarPage_tvMinPriceText);
		tvMiddlePriceText = (TextView) mThisView.findViewById(R.id.searchCarPage_tvMiddlePriceText);
		tvMaxPriceText = (TextView) mThisView.findViewById(R.id.searchCarPage_tvMaxPriceText);
		
		frameForRelatives = (FrameLayout) mThisView.findViewById(R.id.searchCarPage_frameForRelatives);
		
		relativeForMinPrice = (RelativeLayout) mThisView.findViewById(R.id.searchCarPage_relativeForMinPrice);
		tvMinPrice = (TextView) mThisView.findViewById(R.id.searchCarPage_tvMinPrice);
		arrowForMinPrice = mThisView.findViewById(R.id.searchCarPage_arrowForMinPrice);
		
		relativeForMaxPrice = (RelativeLayout) mThisView.findViewById(R.id.searchCarPage_relativeForMaxPrice);
		tvMaxPrice = (TextView) mThisView.findViewById(R.id.searchCarPage_tvMaxPrice);
		arrowForMaxPrice = mThisView.findViewById(R.id.searchCarPage_arrowForMaxPrice);
		
		editTextBg = mThisView.findViewById(R.id.searchCarPage_editTextBg);
		etMinPrice = (EditText) mThisView.findViewById(R.id.searchCarPage_etMinPrice);
		etMaxPrice = (EditText) mThisView.findViewById(R.id.searchCarPage_etMaxPrice);
		
		btnShowCommonSearchResult = (Button) mThisView.findViewById(R.id.searchCarPage_btnShowCommonSearchResult);
		
		listView = (ListView) mThisView.findViewById(R.id.searchCarPage_listView);
		gridView = (GridView) mThisView.findViewById(R.id.searchCarPage_gridView);
		
		relativeForSearchResult = (RelativeLayout) mThisView.findViewById(R.id.searchCarPage_relativeForSearchResult);
		tvSearchConditionText = (TextView) mThisView.findViewById(R.id.searchCarPage_tvSearchConditionText);
		tvSearchCondition = (TextView) mThisView.findViewById(R.id.searchCarPage_tvSearchCondition);
		btnShowDetailSearchResult = (Button) mThisView.findViewById(R.id.searchCarPage_btnShowDetailSearchResult);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {
			
			menuIndex = getArguments().getInt("menuIndex");

			if(menuIndex < 0 || menuIndex > 1) {
				menuIndex = 0;
			}
		}
		
		minPrice = MIN_PRICE;
		maxPrice = MAX_PRICE;
	}

	@Override
	public void createPage() {

		gridAdapter = new BCPAdapter(mContext, mActivity, getActivity().getLayoutInflater(), modelsForGrid);
		gridView.setAdapter(gridAdapter);
		
		listAdapter = new BCPAdapter(mContext, mActivity, getActivity().getLayoutInflater(), modelsForList);
		listView.setAdapter(listAdapter);
		listView.setDividerHeight(0);
		listView.setDivider(null);
		
		tvMinPriceText.setText(StringUtils.getFormattedNumber(MIN_PRICE) + getString(R.string.won));
		tvMiddlePriceText.setText(StringUtils.getFormattedNumber(MIDDLE_PRICE) + getString(R.string.won));
		tvMaxPriceText.setText(StringUtils.getFormattedNumber(MAX_PRICE) + getString(R.string.won));
		
		tvMinPrice.setText(minPrice + getString(R.string.won));
		tvMaxPrice.setText(maxPrice + getString(R.string.won));
	}

	@Override
	public void setListeners() {

		btnCommon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(0);
			}
		});
		
		btnDetail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				setMenu(1);
			}
		});
		
		gridView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				if(visibleItemCount < totalItemCount && firstVisibleItem + visibleItemCount == totalItemCount) {
					downloadInfo();
				}
			}
		});
		
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				conditionString = ((Brand)modelsForGrid.get(position)).getName();
				loadModelGroups(((Brand)modelsForGrid.get(position)).getId());
			}
		});
		
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				if(visibleItemCount < totalItemCount && firstVisibleItem + visibleItemCount == totalItemCount) {
					downloadInfo();
				}
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {

				switch(type) {
				
				case TYPE_MODELGROUP:
					conditionString += " / " + ((CarModelGroup)modelsForList.get(position)).getName();
					loadModels(((CarModelGroup)modelsForList.get(position)).getId());
					break;
					
				case TYPE_MODEL:
					conditionString += " / " + ((CarModel)modelsForList.get(position)).getName();
					loadTrims(((CarModel)modelsForList.get(position)).getId());
					break;
					
				case TYPE_TRIM:
					conditionString += " / " + ((CarTrim)modelsForList.get(position)).getName();
					showSelectedCondition();
					break;
				}
			}
		});
	}

	@Override
	public void setSizes() {

		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, 88, 
				mThisView.findViewById(R.id.searchCarPage_bgForButtons), null, null, null);
		
		ResizeUtils.viewResizeForRelative(320, 88, btnCommon, null, null, null);
		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, 88, btnDetail, null, null, null);
		
		ResizeUtils.viewResizeForRelative(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 
				tvPriceRangeText, null, null, new int[]{30, 30, 0, 0});
		ResizeUtils.viewResizeForRelative(530, 11, priceRangeLine, null, null, new int[]{0, 100, 0, 0});
		ResizeUtils.viewResizeForRelative(120, LayoutParams.WRAP_CONTENT, tvMinPriceText, null, null, null);
		ResizeUtils.viewResizeForRelative(120, LayoutParams.WRAP_CONTENT, tvMiddlePriceText, null, null, null);
		ResizeUtils.viewResizeForRelative(120, LayoutParams.WRAP_CONTENT, tvMaxPriceText, null, null, null);
		
		ResizeUtils.viewResizeForRelative(114, 42, tvMinPrice, null, null, null);
		ResizeUtils.viewResizeForRelative(48, 48, arrowForMinPrice, null, null, null);
		
		ResizeUtils.viewResizeForRelative(114, 42, tvMaxPrice, null, null, null);
		ResizeUtils.viewResizeForRelative(48, 48, arrowForMaxPrice, null, null, null);
		
		ResizeUtils.setMargin(frameForRelatives, new int[]{0, 40, 0, 0});
		tvMinPrice.setPadding(0, ResizeUtils.getSpecificLength(6), 0, 0);
		tvMaxPrice.setPadding(0, ResizeUtils.getSpecificLength(6), 0, 0);
		
		ResizeUtils.viewResizeForRelative(418, 52, editTextBg, null, null, new int[]{0, 70, 0, 0});
		ResizeUtils.viewResizeForRelative(160, 52, etMinPrice, null, null, null);
		ResizeUtils.viewResizeForRelative(160, 52, etMaxPrice, null, null, null);
		
		ResizeUtils.viewResizeForRelative(586, 82, 
				btnShowCommonSearchResult, null, null, new int[]{0, 60, 0, 0});
		
		ResizeUtils.viewResizeForRelative(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 
				tvSearchConditionText, null, null, new int[]{30, 30, 0, 0});
		ResizeUtils.viewResizeForRelative(586, 160, 
				tvSearchCondition, null, null, new int[]{0, 30, 0, 0});
		ResizeUtils.viewResizeForRelative(586, 82, 
				btnShowDetailSearchResult, null, null, new int[]{0, 0, 0, 30});
		
		FontUtils.setFontSize(tvPriceRangeText, 30);
		FontUtils.setFontSize(tvMinPriceText, 16);
		FontUtils.setFontSize(tvMiddlePriceText, 16);
		FontUtils.setFontSize(tvMaxPriceText, 16);
		
		FontUtils.setFontSize(tvMinPrice, 16);
		FontUtils.setFontSize(tvMaxPrice, 16);
		
		FontUtils.setFontSize(etMinPrice, 16);
		FontUtils.setFontSize(etMaxPrice, 16);
		
		int p = ResizeUtils.getSpecificLength(10);
		etMinPrice.setPadding(p, 0, p, 0);
		etMaxPrice.setPadding(p, 0, p, 0);
		
		FontUtils.setFontSize(tvSearchConditionText, 30);
		FontUtils.setFontSize(tvSearchCondition, 30);
	}

	@Override
	public int getContentViewId() {
		
		return R.layout.fragment_search_car;
	}

	@Override
	public int getBackButtonResId() {
		
		return R.drawable.filter_back_btn;
	}

	@Override
	public int getBackButtonWidth() {

		return 214;
	}

	@Override
	public int getBackButtonHeight() {

		return 60;
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
					brand.setNeedClickListener(false);
					modelsForGrid.add(brand);
				}
				
				gridAdapter.notifyDataSetChanged();
				return true;
				
			case TYPE_MODELGROUP:
				arJSON = objJSON.getJSONArray("modelgroups");
				size = arJSON.length();
				
				for(int i=0; i<size; i++) {
					CarModelGroup group = new CarModelGroup(arJSON.getJSONObject(i));
					group.setItemCode(BCPConstants.ITEM_CAR_TEXT);
					group.setNeedClickListener(false);
					modelsForList.add(group);
				}
				
				listAdapter.notifyDataSetChanged();
				break;
				
			case TYPE_MODEL:
				arJSON = objJSON.getJSONArray("models");
				size = arJSON.length();
				
				for(int i=0; i<size; i++) {
					CarModel model = new CarModel(arJSON.getJSONObject(i));
					model.setItemCode(BCPConstants.ITEM_CAR_TEXT);
					model.setNeedClickListener(false);
					modelsForList.add(model);
				}
				
				listAdapter.notifyDataSetChanged();
				break;
				
			case TYPE_TRIM:
				arJSON = objJSON.getJSONArray("trims");
				size = arJSON.length();
				
				for(int i=0; i<size; i++) {
					CarTrim transmission = new CarTrim(arJSON.getJSONObject(i));
					transmission.setItemCode(BCPConstants.ITEM_CAR_TEXT);
					transmission.setNeedClickListener(false);
					modelsForList.add(transmission);
				}
				
				listAdapter.notifyDataSetChanged();
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getRootViewResId() {

		return R.id.searchCarPage_mainLayout;
	}
	
	@Override
	public void onDestroyView() {
		
		gridView.setOnScrollListener(null);
		gridView.setOnItemClickListener(null);
		listView.setOnScrollListener(null);
		listView.setOnItemClickListener(null);
		
		super.onDestroyView();
	}
	
//////////////////// Custom methods.

	public void setMenu(int menuIndex) {

		gridView.setVisibility(View.INVISIBLE);
		listView.setVisibility(View.INVISIBLE);
		relativeForSearchResult.setVisibility(View.INVISIBLE);
		
		if(menuIndex == 0) {
			btnCommon.setBackgroundResource(R.drawable.filter_detail_tab_a);
			btnDetail.setBackgroundResource(R.drawable.filter_normal_tab_b);
			
			relativeForCommonSearch.setVisibility(View.VISIBLE);
			
		} else {
			btnCommon.setBackgroundResource(R.drawable.filter_detail_tab_b);
			btnDetail.setBackgroundResource(R.drawable.filter_normal_tab_a);
			
			relativeForCommonSearch.setVisibility(View.INVISIBLE);
			loadBrands();
		}
		
		this.menuIndex = menuIndex;
	}

	public void loadBrands() {

		gridView.setVisibility(View.VISIBLE);

		conditionString = "";
		type = TYPE_BRAND;
		isLastList = false;
		modelsForGrid.clear();
		gridAdapter.notifyDataSetChanged();
		
		url = BCPAPIs.SEARCH_CAR_BRAND
				+ "?num=0";
		super.downloadInfo();
	}
	
	public void loadModelGroups(int brand_id) {

		gridView.setVisibility(View.INVISIBLE);
		listView.setVisibility(View.VISIBLE);
		
		type = TYPE_MODELGROUP;
		isLastList = false;
		modelsForList.clear();
		listAdapter.notifyDataSetChanged();
		
		url = BCPAPIs.SEARCH_CAR_MODELGROUP
				+ "?num=0"
				+ "&brand_id=" + brand_id;
		super.downloadInfo();
	}
	
	public void loadModels(int modelgroup_id) {

		type = TYPE_MODEL;
		isLastList = false;
		modelsForList.clear();
		listAdapter.notifyDataSetChanged();
		
		url = BCPAPIs.SEARCH_CAR_MODEL
				+ "?num=0"
				+ "&modelgroup_id=" + modelgroup_id;
		super.downloadInfo();
	}
	
	public void loadTrims(int model_id) {
		
		type = TYPE_TRIM;
		isLastList = false;
		modelsForList.clear();
		listAdapter.notifyDataSetChanged();
		
		url = BCPAPIs.SEARCH_CAR_TRIM
				+ "?num=0"
				+ "&model_id=" + model_id;
		super.downloadInfo();
	}
	
	public void showSelectedCondition() {
		
		listView.setVisibility(View.INVISIBLE);
		relativeForSearchResult.setVisibility(View.VISIBLE);
		
		tvSearchCondition.setText(conditionString);
	}
	
	public void closePage() {

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				ToastUtils.showToast(R.string.failToLoadList);
				mActivity.closeTopPage();
			}
		}, 1000);
	}
}
