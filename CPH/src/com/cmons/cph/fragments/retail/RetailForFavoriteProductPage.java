package com.cmons.cph.fragments.retail;

import org.json.JSONArray;
import org.json.JSONObject;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForRetail;
import com.cmons.cph.classes.CphAdapter;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Product;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class RetailForFavoriteProductPage extends CmonsFragmentForRetail {
	
	private TextView tvCount;
	private GridView gridView;
	
	@Override
	public void onResume() {
		super.onResume();

		if(models.size() == 0) {
			downloadInfo();
		}
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.retailFavoriteProductPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.retailFavoriteProductPage_ivBg);
		
		tvCount = (TextView) mThisView.findViewById(R.id.retailFavoriteProductPage_tvCount);
		gridView = (GridView) mThisView.findViewById(R.id.retailFavoriteProductPage_gridView);
	}

	@Override
	public void setVariables() {

	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		
		gridView.setNumColumns(2);
		adapter = new CphAdapter(mContext, getActivity().getLayoutInflater(), models);
		gridView.setAdapter(adapter);
	}

	@Override
	public void setListeners() {

		gridView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, 
					int visibleItemCount, int totalItemCount) {
				
				if(visibleItemCount < totalItemCount && firstVisibleItem + visibleItemCount == totalItemCount) {
					downloadInfo();
				}
			}
		});
	}

	@Override
	public void setSizes() {
		
		RelativeLayout.LayoutParams rp = null;
		
		//tvCount.
		rp = (RelativeLayout.LayoutParams) tvCount.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(120);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_retail_favoriteproduct;
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
	public void downloadInfo() {

		url = CphConstants.BASE_API_URL + "retails/favorite/products";
		super.downloadInfo();
	}
	
	@Override
	public boolean parseJSON(JSONObject objJSON) {

		try {
			JSONArray arJSON = objJSON.getJSONArray("products");
			
			int size = arJSON.length();
			for(int i=0; i<size; i++) {
				Product product = new Product(arJSON.getJSONObject(i));
				product.setItemCode(CphConstants.ITEM_PRODUCT);
				product.setType(Product.TYPE_RETAIL);
				product.setDeletable(true);
				models.add(product);
			}

			tvCount.setText("총 " + size + "개의 즐겨찾기 상품이 등록되었습니다.");
			
			if(size == 0 || size < NUMBER_OF_LISTITEMS) {
				return true;
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		return false;
	}
	
	@Override
	public int getBgResourceId() {

		return R.drawable.favorite_bg;
	}
}