package com.cmons.cph.fragments.retail;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
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
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.ToastUtils;

public class RetailForSearchPage extends CmonsFragmentForRetail {

	private TextView tvSearch;
	private EditText etSearch;
	private Button btnSearch;
	private TextView tvResult;
	private GridView gridView;
	
	private String keyword;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.retailSearchPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.retailSearchPage_ivBg);
		
		tvSearch = (TextView) mThisView.findViewById(R.id.retailSearchPage_tvSearch);
		etSearch = (EditText) mThisView.findViewById(R.id.retailSearchPage_etSearch);
		btnSearch = (Button) mThisView.findViewById(R.id.retailSearchPage_btnSearch);
		tvResult = (TextView) mThisView.findViewById(R.id.retailSearchPage_tvResult);
		gridView = (GridView) mThisView.findViewById(R.id.retailSearchPage_gridView);
	}

	@Override
	public void setVariables() {

		title = "상품 검색";
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		
		adapter = new CphAdapter(mContext, getActivity().getLayoutInflater(), models);
		gridView.setAdapter(adapter);
	}

	@Override
	public void setListeners() {

		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(etSearch.getEditableText() == null
						|| etSearch.getEditableText().length() == 0) {
					ToastUtils.showToast(R.string.wrongSearchKeyword);
					return;
				}

				SoftKeyboardUtils.hideKeyboard(mContext, btnSearch);
				
				keyword = etSearch.getEditableText().toString();
				refreshPage();
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		
		//tvSearch.
		rp = (RelativeLayout.LayoutParams) tvSearch.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(120);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		FontUtils.setFontSize(tvSearch, 30);
		
		//etSearch.
		rp = (RelativeLayout.LayoutParams) etSearch.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(550);
		rp.height = ResizeUtils.getSpecificLength(92);
		FontUtils.setFontAndHintSize(etSearch, 30, 24);
		
		//btnSearch.
		rp = (RelativeLayout.LayoutParams) btnSearch.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//tvResult.
		rp = (RelativeLayout.LayoutParams) tvResult.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		FontUtils.setFontSize(tvResult, 24);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_retail_search;
	}

	@Override
	public void downloadInfo() {

		if(keyword == null || keyword.length() == 0) {
			tvResult.setText("검색된 상품이 없습니다");
			return;
		}

		//http://cph.minsangk.com/products?keyword=%EB%A7%A5%EC%A3%BC
		
		try {
			url = CphConstants.BASE_API_URL + "products" +
					"?num=0" +
					"&keyword=" + URLEncoder.encode(keyword, "utf-8");
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
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
				models.add(product);
			}
			
			if(size%2 == 1) {
				BaseModel emptyModel = new BaseModel() {};
				emptyModel.setItemCode(CphConstants.ITEM_PRODUCT);
				models.add(emptyModel);
			} else if(pageIndex == 0 && size == 0) {
				BaseModel emptyModel1 = new BaseModel() {};
				emptyModel1.setItemCode(CphConstants.ITEM_PRODUCT);
				models.add(emptyModel1);

				BaseModel emptyModel2 = new BaseModel() {};
				emptyModel2.setItemCode(CphConstants.ITEM_PRODUCT);
				models.add(emptyModel2);
			}
			
			tvResult.setText("총 " + size + "개의 상품이 검색되었습니다.");
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		return true;
	}

	@Override
	public int getBgResourceId() {

		return R.drawable.order_bg;
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

//////////////////// Custom methods.
}
