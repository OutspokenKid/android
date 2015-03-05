package com.byecar.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
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
import com.byecar.models.CarTrim;
import com.byecar.views.SliderView;
import com.byecar.views.SliderView.NodeChangedListener;
import com.byecar.views.TitleBar;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class SearchCarPage extends BCPFragment {

	private static final int MIN_PRICE = 0;
	private static final int MAX_PRICE = 50000;
	
	public static final int TYPE_BRAND = 1;
	public static final int TYPE_MODELGROUP = 2;
	public static final int TYPE_MODEL = 3;
	public static final int TYPE_TRIM = 4;
	
	private Button btnCommon;
	private Button btnDetail;

	private RelativeLayout relativeForCommonSearch;
	private TextView tvPriceRangeText;
	private SliderView sliderView;
	private TextView tvMinPriceText;
	private TextView tvMiddlePriceText;
	private TextView tvMaxPriceText;
	private View editTextBg;
	private EditText etMinPrice;
	private EditText etMaxPrice;
	private Button btnClearMin;
	private Button btnClearMax;
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
	private int carType;
	private int menuIndex;
	private int minPrice;
	private int maxPrice;
	private String conditionString;
	private int brand_id;
	private int modelgroup_id;
	private int model_id;
	private int trim_id;
	
	private ArrayList<BaseModel> modelsForGrid = new ArrayList<BaseModel>();
	private ArrayList<BaseModel> modelsForList = new ArrayList<BaseModel>();
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.searchCarPage_titleBar);
		
		btnCommon = (Button) mThisView.findViewById(R.id.searchCarPage_btnCommon);
		btnDetail = (Button) mThisView.findViewById(R.id.searchCarPage_btnDetail);
		
		relativeForCommonSearch = (RelativeLayout) mThisView.findViewById(R.id.searchCarPage_relativeForCommonSearch);
		tvPriceRangeText = (TextView) mThisView.findViewById(R.id.searchCarPage_tvPriceRangeText);
		sliderView = (SliderView) mThisView.findViewById(R.id.searchCarPage_sliderView);
		tvMinPriceText = (TextView) mThisView.findViewById(R.id.searchCarPage_tvMinPriceText);
		tvMiddlePriceText = (TextView) mThisView.findViewById(R.id.searchCarPage_tvMiddlePriceText);
		tvMaxPriceText = (TextView) mThisView.findViewById(R.id.searchCarPage_tvMaxPriceText);
		
		editTextBg = mThisView.findViewById(R.id.searchCarPage_editTextBg);
		etMinPrice = (EditText) mThisView.findViewById(R.id.searchCarPage_etMinPrice);
		etMaxPrice = (EditText) mThisView.findViewById(R.id.searchCarPage_etMaxPrice);
		btnClearMin = (Button) mThisView.findViewById(R.id.searchCarPage_btnClearMin);
		btnClearMax = (Button) mThisView.findViewById(R.id.searchCarPage_btnClearMax);
		
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
			carType = getArguments().getInt("type");

			if(menuIndex < 0 || menuIndex > 1) {
				menuIndex = 0;
			}
		}
		
		minPrice = MIN_PRICE;
		maxPrice = MAX_PRICE;
	}

	@Override
	public void createPage() {

		sliderView.setValues(0, 50000);
		setPriceEditTexts();
		
		gridAdapter = new BCPAdapter(mContext, mActivity, getActivity().getLayoutInflater(), modelsForGrid);
		gridView.setAdapter(gridAdapter);
		
		listAdapter = new BCPAdapter(mContext, mActivity, getActivity().getLayoutInflater(), modelsForList);
		listView.setAdapter(listAdapter);
		listView.setDividerHeight(0);
		listView.setDivider(null);
		
		tvMinPriceText.setText(R.string.searchPrice1);
		tvMiddlePriceText.setText(R.string.searchPrice2);
		tvMaxPriceText.setText(R.string.searchPrice3);
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
		
		sliderView.setOnNodeChangedListener(new NodeChangedListener() {

			@Override
			public void onChanged(boolean nodeStartChanged,
					boolean nodeEndChanged, int nodeStart, int nodeEnd) {

				
				if(nodeStartChanged) {
					minPrice = nodeStart;
				} else if(nodeEndChanged) {
					maxPrice = nodeEnd;
				}
				
				setPriceEditTexts();
			}
		});
		
		etMinPrice.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if(!hasFocus) {
					
					if(StringUtils.isEmpty(etMinPrice.getText())) {
						minPrice = 0;
					}
					
					etMinPrice.setText(StringUtils.getFormattedNumber(minPrice));
					sliderView.moveNodes(minPrice, maxPrice);
				} else {
					
					try {
						etMinPrice.setText(etMinPrice.getText().toString().replace(",", ""));
					} catch (Exception e) {
					}
				}
			}
		});
		
		etMinPrice.addTextChangedListener(new TextWatcher() {
			
			private String lastText = null;
			private int lastCursorIndex;
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
				if(s != null) {
					lastText = s.toString();
					lastCursorIndex = etMinPrice.getSelectionStart() - 1;
				}
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
				try {
					if(s == null || s.length() == 0) {
						return;
					}
					
					int tempPrice = Integer.parseInt(s.toString());
					
					if(tempPrice == Integer.parseInt(lastText)) {
						return;
					}
					
					if(tempPrice < 0) {
						ToastUtils.showToast(R.string.minPriceMustOverZero);
						etMinPrice.setText(lastText);
						etMinPrice.setSelection(lastCursorIndex);
						return;
					} else if(tempPrice > maxPrice) {
						ToastUtils.showToast(R.string.minPriceCantOverMaxPrice);
						etMinPrice.setText(lastText);
						etMinPrice.setSelection(lastCursorIndex);
						return;
					} else {
						minPrice = tempPrice;
						sliderView.moveNodes(minPrice, maxPrice);
						sliderView.invalidate();
					}
				} catch (Exception e) {
				}
			}
		});
		
		etMaxPrice.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if(!hasFocus) {
					
					if(StringUtils.isEmpty(etMaxPrice.getText())) {
						maxPrice = 0;
					}
					
					etMaxPrice.setText(StringUtils.getFormattedNumber(maxPrice));
					sliderView.moveNodes(minPrice, maxPrice);
				} else {
				
					try {
						etMaxPrice.setText(etMaxPrice.getText().toString().replace(",", ""));
					} catch (Exception e) {
					}
				}
			}
		});
		
		etMaxPrice.addTextChangedListener(new TextWatcher() {
			
			private String lastText = null;
			private int lastCursorIndex;
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
				if(s != null) {
					lastText = s.toString();
					lastCursorIndex = etMaxPrice.getSelectionStart() - 1;
				}
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
				try {
					if(s == null || s.length() == 0) {
						return;
					}
					
					String text = s.toString();
					
					if(text.equals(lastText)) {
						return;
					}
					
					if(text.contains(".") 
							|| (text.length() > 1 && text.charAt(0) == '0')) {
						etMaxPrice.setText(lastText);
						etMaxPrice.setSelection(lastCursorIndex);
					}
					
					int tempPrice = Integer.parseInt(text);
					
					if(tempPrice < minPrice) {
						ToastUtils.showToast(R.string.maxPriceCantOverMinPrice);						
					} else if(tempPrice > MAX_PRICE) {
						ToastUtils.showToast(R.string.maxPriceMustLessThanLimit);
						etMaxPrice.setText(lastText);
						etMaxPrice.setSelection(lastCursorIndex);
						return;
					} else {
						maxPrice = tempPrice;
						sliderView.moveNodes(minPrice, maxPrice);
						sliderView.invalidate();
					}
				} catch (Exception e) {
				}
			}
		});
		
		btnClearMin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				etMinPrice.setText(null);
				etMinPrice.requestFocus();
			}
		});
		
		btnClearMax.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				etMaxPrice.setText(null);
				etMaxPrice.requestFocus();
			}
		});
		
		gridView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				if(firstVisibleItem + visibleItemCount == totalItemCount) {
					downloadInfo();
				}
			}
		});
		
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				conditionString = ((Brand)modelsForGrid.get(position)).getName();
				brand_id = ((Brand)modelsForGrid.get(position)).getId();
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
				
				if(firstVisibleItem + visibleItemCount == totalItemCount) {
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
					if(((CarModelGroup)modelsForList.get(position)).getId() == 0) {
						showSelectedCondition();
					} else {
						conditionString += " / " + ((CarModelGroup)modelsForList.get(position)).getName();
						modelgroup_id = ((CarModelGroup)modelsForList.get(position)).getId();
						loadModels(((CarModelGroup)modelsForList.get(position)).getId());
					}
					break;
					
				case TYPE_MODEL:
					if(((CarModel)modelsForList.get(position)).getId() == 0) {
						showSelectedCondition();
					} else {
						conditionString += " / " + ((CarModel)modelsForList.get(position)).getName();
						model_id = ((CarModel)modelsForList.get(position)).getId();
						loadTrims(((CarModel)modelsForList.get(position)).getId());
					}
					break;
					
				case TYPE_TRIM:
					conditionString += " / " + ((CarTrim)modelsForList.get(position)).getName();
					trim_id = ((CarTrim)modelsForList.get(position)).getId();
					showSelectedCondition();
					break;
				}
			}
		});

		btnShowCommonSearchResult.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				String conditionString = StringUtils.getFormattedNumber(minPrice) + "만원 부터 ~ "
						+ StringUtils.getFormattedNumber(maxPrice) + "만원 까지";
				
				Bundle bundle = new Bundle();
				bundle.putInt("type", carType);
				bundle.putInt("price_min", minPrice * 10000);
				bundle.putInt("price_max", maxPrice * 10000);
				bundle.putString("conditionString", conditionString);
				mActivity.showPage(BCPConstants.PAGE_SEARCH_RESULT, bundle);
			}
		});
		
		btnShowDetailSearchResult.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putInt("type", carType);
				bundle.putInt("brand_id", brand_id);
				bundle.putInt("modelgroup_id", modelgroup_id);
				bundle.putInt("model_id", model_id);
				bundle.putInt("trim_id", trim_id);
				bundle.putString("conditionString", conditionString);
				mActivity.showPage(BCPConstants.PAGE_SEARCH_RESULT, bundle);
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
		
		RelativeLayout.LayoutParams rp = (RelativeLayout.LayoutParams) sliderView.getLayoutParams();
		rp.width = LayoutParams.MATCH_PARENT;
		rp.height = sliderView.getNodeHeight();
		rp.leftMargin = ResizeUtils.getSpecificLength(30);
		rp.topMargin = ResizeUtils.getSpecificLength(30);
		rp.rightMargin = ResizeUtils.getSpecificLength(30);

		rp = (RelativeLayout.LayoutParams) tvMinPriceText.getLayoutParams();
		rp.width = sliderView.getNodeWidth();
		
		rp = (RelativeLayout.LayoutParams) tvMaxPriceText.getLayoutParams();
		rp.width = sliderView.getNodeWidth();
		
		ResizeUtils.viewResizeForRelative(494, 52, editTextBg, null, null, new int[]{0, 70, 0, 0});
		ResizeUtils.viewResizeForRelative(117, 45, etMinPrice, null, null, new int[]{4, 4, 0, 0});
		ResizeUtils.viewResizeForRelative(117, 45, etMaxPrice, null, null, new int[]{0, 4, 80, 0});
		ResizeUtils.viewResizeForRelative(40, 40, btnClearMin, null, null, new int[]{0, 2, 0, 0});
		ResizeUtils.viewResizeForRelative(40, 40, btnClearMax, null, null, new int[]{117, 2, 0, 0});
		
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
		
		FontUtils.setFontSize(etMinPrice, 22);
		FontUtils.setFontSize(etMaxPrice, 22);
		
		int p = ResizeUtils.getSpecificLength(10);
		etMinPrice.setPadding(p, 0, p, 0);
		etMaxPrice.setPadding(p, 0, p, 0);
		
		FontUtils.setFontSize(tvSearchConditionText, 30);
		FontUtils.setFontSize(tvSearchCondition, 30);
	}

	@Override
	public int getContentViewId() {
		
		return R.layout.fragment_common_search_car;
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
				
				CarModelGroup cg = new CarModelGroup();
				cg.setName("전체");
				cg.setItemCode(BCPConstants.ITEM_CAR_TEXT);
				cg.setNeedClickListener(false);
				modelsForList.add(cg);
				
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
				
				CarModel cm = new CarModel();
				cm.setName("전체");
				cm.setItemCode(BCPConstants.ITEM_CAR_TEXT);
				cm.setNeedClickListener(false);
				modelsForList.add(cm);
				
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
				
				CarTrim ct = new CarTrim();
				ct.setName("전체");
				ct.setItemCode(BCPConstants.ITEM_CAR_TEXT);
				ct.setNeedClickListener(false);
				modelsForList.add(ct);
				
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
	
//////////////////// Custom methods.

	public void setMenu(int menuIndex) {

		brand_id = 0;
		modelgroup_id = 0;
		model_id = 0;
		trim_id = 0;
		
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

	public void setPriceEditTexts() {
		
		etMinPrice.setText(StringUtils.getFormattedNumber(minPrice));
		etMaxPrice.setText(StringUtils.getFormattedNumber(maxPrice));
		
		if(etMinPrice.hasFocus()) {
			etMinPrice.setSelection(etMinPrice.length());
		}
		
		if(etMaxPrice.hasFocus()) {
			etMaxPrice.setSelection(etMaxPrice.length());
		}
	}
}
