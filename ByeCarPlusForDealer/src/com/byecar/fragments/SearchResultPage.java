package com.byecar.fragments;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.byecar.byecarplusfordealer.MainActivity;
import com.byecar.byecarplusfordealer.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPAdapter;
import com.byecar.classes.BCPAuctionableFragment;
import com.byecar.classes.BCPConstants;
import com.byecar.models.Car;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class SearchResultPage extends BCPAuctionableFragment {

	private TextView tvCondition;
	private View noResult;

	private int type;
	private String conditionString;
	private int brand_id;
	private int modelgroup_id;
	private int model_id;
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
			type = getArguments().getInt("type");
			conditionString = getArguments().getString("conditionString");
			brand_id = getArguments().getInt("brand_id");
			modelgroup_id = getArguments().getInt("modelgroup_id");
			model_id = getArguments().getInt("model_id");
			trim_id = getArguments().getInt("trim_id");
			price_min = getArguments().getInt("price_min");
			price_max = getArguments().getInt("price_max");
		}
	}

	@Override
	public void createPage() {

		adapter = new BCPAdapter(mContext, mActivity, this, mActivity.getLayoutInflater(), models);
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
					Car car = (Car) models.get(position);
					((MainActivity)mActivity).showCarDetailPage(0, car, car.getType());
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

		return R.drawable.filter_result_back_btn;
	}

	@Override
	public int getBackButtonWidth() {

		return 210;
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

		switch(type) {
		
		case Car.TYPE_BID:
			url = BCPAPIs.CAR_BID_LIST_URL;
			break;
		case Car.TYPE_DEALER:
			url = BCPAPIs.CAR_DEALER_LIST_URL;
			break;
		}
		
		url = BCPAPIs.CAR_DEALER_LIST_URL;
		
		if(trim_id != 0) {
			url += "?trim_id=" + trim_id;
		} else if(model_id != 0) {
			url += "?model_id=" + model_id;
		} else if(modelgroup_id != 0) {
			url += "?modelgroup_id=" + modelgroup_id;
		} else if(brand_id != 0) {
			url += "?brand_id=" + brand_id;
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
				
				switch(type) {
				
				case Car.TYPE_BID:
					car.setItemCode(BCPConstants.ITEM_CAR_BID);
					break;
				case Car.TYPE_DEALER:
					car.setItemCode(BCPConstants.ITEM_CAR_DEALER);
					break;
				}
				
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
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void bidStatusChanged(String event, Car car) {

		if(event == null) {
			return;
		}

		car.setItemCode(BCPConstants.ITEM_CAR_BID);
		
		//경매가 시작되는 물건이 있는 경우.
		if(event.equals("auction_begun")) {
			
			//기존에 같은 매물이 있다면 최신화.
			//같은 매물이 없고, 새로운 매물보다 더 늦게 끝나는 매물이 있거나 경매중이 아닌 매물이 있다면 그 위에 삽입.
			if(!updateSelectedCar(car)) {
				reorderList(startIndex, car);
			}
			
		//경매 매물의 가격 변화가 있는 경우.
		} else if(event.equals("bid_price_updated")) {
			updateSelectedCar(car);

		//관리자에 의해 보류된 경우.
		} else if(event.equals("auction_ended")) {
			reorderList(startIndex, car);
			
		//관리자에 의해 보류된 경우.
		} else if(event.equals("auction_held")) {
			//해당 매물 삭제.
			deleteSelectedCar(car);
			
		//딜러 선택 시간이 종료된 경우 (유찰).
		//유저가 딜러를 선택한 경우 (낙찰).
		} else if(event.equals("selection_time_ended")
				|| event.equals("dealer_selected")) {
			reorderList(startIndex, car);
		}
	}
}
