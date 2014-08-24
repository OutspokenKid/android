package com.cmons.cph.fragments.wholesale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;

import com.cmons.cph.R;
import com.cmons.cph.WholesaleActivity.OnAfterSelectCategoryListener;
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.classes.CphAdapter;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Category;
import com.cmons.cph.models.Product;
import com.cmons.cph.views.HeaderViewForShop;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.views.HeaderGridView;

public class WholesaleForShopPage extends CmonsFragmentForWholesale {
	
	private HeaderViewForShop headerView;
	private HeaderGridView gridView;
	
	private String[] customerTypes;
	private String[] orders;
	
	private int customerType = 0;
	private int categoryIndex = 0;
	private int order = 0;
	
	@Override
	public void onResume() {
		super.onResume();
		
		downloadInfo();
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.wholesaleShopPage_titleBar);
		gridView = (HeaderGridView) mThisView.findViewById(R.id.wholesaleShopPage_gridView);
	}

	@Override
	public void setVariables() {

		title = mActivity.wholesale.getName();
	}

	@Override
	public void createPage() {

		customerTypes = new String[]{"전체", "거래처공개"};
		orders = new String[]{"최신순", "판매량순"};
		
		titleBar.getBackButton().setVisibility(View.VISIBLE);
		titleBar.getHomeButton().setVisibility(View.INVISIBLE);
		
		headerView = new HeaderViewForShop(mContext);
		headerView.init(mActivity);
		AbsListView.LayoutParams al = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		headerView.setLayoutParams(al);
		gridView.addHeaderView(headerView);
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
		
		titleBar.getBackButton().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		
		titleBar.getBtnAdd().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showWritePage(null);
			}
		});
	
		headerView.getBtnCustomerType().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showSelectDialog(null, customerTypes, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
					
						customerType = which;
						headerView.getBtnCustomerType().setText(customerTypes[which]);
						
						refreshPage();
					}
				});
			}
		});
		
		headerView.getBtnCategoryIndex().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				mActivity.showCategorySelectPopup(true, new OnAfterSelectCategoryListener() {
					
					@Override
					public void onAfterSelectCategory(Category category, Category subCategory) {

						if(category == null) {
							categoryIndex = 0;
							headerView.getBtnCategoryIndex().setText("전체");
						} else if(subCategory == null) {
							categoryIndex = category.getId();
							headerView.getBtnCategoryIndex().setText(category.getName());
						} else {
							categoryIndex = subCategory.getId();
							headerView.getBtnCategoryIndex().setText(subCategory.getName());
						}
						
						refreshPage();
					}
				});
			}
		});
		
		headerView.getBtnOrder().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showSelectDialog(null, orders, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
					
						order = which;
						headerView.getBtnOrder().setText(orders[which]);
						
						refreshPage();
					}
				});
			}
		});
	}

	@Override
	public void setSizes() {
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_wholesale_shop;
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
	public void downloadInfo() {

		url = CphConstants.BASE_API_URL + "products" +
				"?wholesale_id=" + mActivity.wholesale.getId();
				
		if(customerType == 0) {
			url += "&permission_type=all";
		} else {
			url += "&permission_type=private";
		}
		
		if(categoryIndex != 0) {
			url += "&category_id=" + categoryIndex;
		}
		
		if(order == 0) {
			url +="&order=date-desc";
		} else {
			url +="&order=order-desc";
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
				models.add(product);
			}

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
	
//////////////////// Custom methods.

	@Override
	public int getBgResourceId() {
		// TODO Auto-generated method stub
		return 0;
	}

////////////////////Custom classes.
}