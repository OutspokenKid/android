package com.byecar.fragments;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPAdapter;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPFragment;
import com.byecar.models.Car;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class SearchResultPage extends BCPFragment {

	private TextView tvCondition;
	private View noResult;
	private ListView listView;

	private String conditionString;
	private int trim_id;
	private int price_min;
	private int price_max;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.searchResultPage_titleBar);

		tvCondition = (TextView) mThisView.findViewById(R.id.searchResultPage_tvCondition);
		noResult = mThisView.findViewById(R.id.searchResultPage_noResult);
		listView = (ListView) mThisView.findViewById(R.id.searchResultPage_listView);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {
			conditionString = getArguments().getString("conditionString");
			trim_id = getArguments().getInt("trim_id");
			price_min = getArguments().getInt("price_min");
			price_max = getArguments().getInt("price_max");
		}
	}

	@Override
	public void createPage() {

		adapter = new BCPAdapter(mContext, mActivity, mActivity.getLayoutInflater(), models);
		listView.setAdapter(adapter);
		listView.setDivider(new ColorDrawable(Color.TRANSPARENT));
		listView.setDividerHeight(ResizeUtils.getSpecificLength(16));
		
		tvCondition.setText(conditionString);
	}

	@Override
	public void setListeners() {

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {

				try {
					Bundle bundle = new Bundle();
					bundle.putInt("id", ((Car) models.get(position)).getId());
					bundle.putInt("type", Car.TYPE_DEALER);
					mActivity.showPage(BCPConstants.PAGE_CAR_DETAIL, bundle);
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}
			}
		});
	}

	@Override
	public void setSizes() {

		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, 86, tvCondition, null, null, null);
		ResizeUtils.viewResizeForRelative(229, 281, noResult, null, null, new int[]{0, 50, 0, 0});
		
		FontUtils.setFontSize(tvCondition, 30);
		FontUtils.setFontStyle(tvCondition, FontUtils.BOLD);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_common_search_result;
	}

	@Override
	public int getBackButtonResId() {

		return R.drawable.filter_back2_btn;
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
	public int getRootViewResId() {

		return R.id.searchResultPage_mainLayout;
	}

	@Override
	public void downloadInfo() {
		
		url = BCPAPIs.CAR_DEALER_LIST_URL;
		
		if(trim_id != 0) {
			url += "?trim_id=" + trim_id;
		} else if(price_max != 0) {
			url += "?price_min=" + price_min + "&price_max=" + price_max;
		} else {
			listView.setVisibility(View.INVISIBLE);
			noResult.setVisibility(View.VISIBLE);
			return;
		}

		listView.setVisibility(View.VISIBLE);
		noResult.setVisibility(View.INVISIBLE);
		
		super.downloadInfo();
	}
	
	@Override
	public boolean parseJSON(JSONObject objJSON) {

		int size = 0;
		
		try {
			JSONArray arJSON = objJSON.getJSONArray("onsalecars");
			
			size = arJSON.length();
			for(int i=0; i<size; i++) {
				Car car = new Car(arJSON.getJSONObject(i));
				car.setItemCode(BCPConstants.ITEM_CAR_DEALER);
				models.add(car);
			}
			
			if(models.size() == 0) {
				noResult.setVisibility(View.VISIBLE);
			} else {
				noResult.setVisibility(View.INVISIBLE);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		if(size < NUMBER_OF_LISTITEMS) {
			return true;
		} else {
			return false;
		}
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
	public void onResume() {
		super.onResume();
		
		if(models.size() == 0) {
			downloadInfo();
		}
	}
	
//////////////////// Custom methods.
}
