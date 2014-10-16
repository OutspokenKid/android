package com.cmons.cph.fragments.retail;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForRetail;
import com.cmons.cph.classes.CphAdapter;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.WholesaleWish;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class RetailForWishListPage extends CmonsFragmentForRetail {

	private TextView tvCount;
	private ListView listView;
	
	@Override
	public void onResume() {
		super.onResume();
		
		refreshPage();
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.retailWishListPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.retailWishListPage_ivBg);
		
		tvCount = (TextView) mThisView.findViewById(R.id.retailWishListPage_tvCount);
		listView = (ListView) mThisView.findViewById(R.id.retailWishListPage_listView);
	}

	@Override
	public void setVariables() {

		title = "장바구니";
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
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				
				Bundle bundle = new Bundle();
				bundle.putSerializable("wholesaleWish", (WholesaleWish)models.get(position));
				mActivity.showPage(CphConstants.PAGE_RETAIL_WISH, bundle);
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		
		//tvCount.
		rp = (RelativeLayout.LayoutParams) tvCount.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		FontUtils.setFontSize(tvCount, 30);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_retail_wishlist;
	}

	@Override
	public void downloadInfo() {

		url = CphConstants.BASE_API_URL + "retails/cart" +
				"?num=0";
		super.downloadInfo();
	}
	
	@Override
	public boolean parseJSON(JSONObject objJSON) {

		try {
			JSONArray arJSON = objJSON.getJSONArray("wholesalesWithItems");
			
			int size = arJSON.length();
			for(int i=0; i<size; i++) {
				WholesaleWish wholesaleWish = new WholesaleWish(arJSON.getJSONObject(i));
				wholesaleWish.setItemCode(CphConstants.ITEM_WHOLESALE_WISH);
				models.add(wholesaleWish);
			}

			int cartItemsCount = objJSON.getInt("cartItemsCount");
			
			if(cartItemsCount != 0) {
				tvCount.setText("총 " + cartItemsCount + "개의 상품이 있습니다.");
			} else {
				tvCount.setText(R.string.noResult_wish);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		return true;
	}

	@Override
	public int getBgResourceId() {

		return R.drawable.myshop_bg;
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

}
