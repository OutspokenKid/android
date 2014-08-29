package com.cmons.cph.fragments.retail;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForRetail;
import com.cmons.cph.classes.CphAdapter;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Wholesale;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.LogUtils;

public class RetailForFavoriteShopPage extends CmonsFragmentForRetail {

	private ListView listView;
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(models.size() == 0) {
			downloadInfo();
		}
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.retailFavoriteListPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.retailFavoriteListPage_ivBg);
		
		listView = (ListView) mThisView.findViewById(R.id.retailFavoriteListPage_listView);
	}

	@Override
	public void setVariables() {
		
		title = "즐겨찾기 매장";
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		
		adapter = new CphAdapter(mContext, getActivity().getLayoutInflater(), models);
		listView.setAdapter(adapter);
		listView.setDivider(new ColorDrawable(Color.WHITE));
	}

	@Override
	public void setListeners() {

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				showWholesale((Wholesale)models.get(arg2));
			}
		});
	}

	@Override
	public void setSizes() {
		
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_retail_favoriteshop;
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
	public boolean parseJSON(JSONObject objJSON) {

		try {
			JSONArray arJSON = objJSON.getJSONArray("wholesales");

			int size = arJSON.length();
			for(int i=0; i<size; i++) {
				Wholesale wholesale = new Wholesale(arJSON.getJSONObject(i));
				wholesale.setItemCode(CphConstants.ITEM_WHOLESALE);
				models.add(wholesale);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		return true;
	}
	
	@Override
	public void downloadInfo() {

		//http://cph.minsangk.com/retails/favorite/wholesales
		url = CphConstants.BASE_API_URL + "retails/favorite/wholesales";
		super.downloadInfo();
	}

	@Override
	public int getBgResourceId() {

		return R.drawable.shop_bg;
	}
	
//////////////////// Custom methods.

	public void showWholesale(Wholesale wholesale) {
		
	}
}
