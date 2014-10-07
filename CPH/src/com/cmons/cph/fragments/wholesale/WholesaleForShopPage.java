package com.cmons.cph.fragments.wholesale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;

import com.cmons.cph.R;
import com.cmons.cph.ShopActivity.OnAfterSelectCategoryListener;
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.classes.CphAdapter;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Category;
import com.cmons.cph.models.Product;
import com.cmons.cph.views.HeaderViewForWholesaleShop;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.views.HeaderGridView;

public class WholesaleForShopPage extends CmonsFragmentForWholesale {
	
	private HeaderViewForWholesaleShop headerView;
	private HeaderGridView gridView;
	
	private String[] customerTypes;
	private String[] orders;
	
	private int customerType = 0;
	private int categoryIndex = 0;
	private int order = 0;
	private int totalCount;
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(headerView != null) {
			headerView.refreshValues(getWholesale());
		}
		
		if(models.size() == 0) {
			downloadInfo();
		}
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.wholesaleShopPage_titleBar);
		gridView = (HeaderGridView) mThisView.findViewById(R.id.wholesaleShopPage_gridView);
	}

	@Override
	public void setVariables() {

		try {
			title = getWholesale().getName();
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	@Override
	public void createPage() {

		customerTypes = new String[]{"전체", "모두공개", "거래처공개"};
		orders = new String[]{"최신순", "판매량순"};
		
		titleBar.getBackButton().setVisibility(View.VISIBLE);
		
		headerView = new HeaderViewForWholesaleShop(mContext);
		headerView.init(getWholesale());
		
		LogUtils.log("###WholesaleForShopPage.createPage.  init.");
		
		headerView.setTotalProduct(totalCount);
		AbsListView.LayoutParams al = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		headerView.setLayoutParams(al);
		gridView.addHeaderView(headerView);
		gridView.setNumColumns(2);
		adapter = new CphAdapter(mContext, getActivity().getLayoutInflater(), models);
		gridView.setAdapter(adapter);
	}

	@Override
	public void setListeners() {
		
		LogUtils.log("###WholesaleForShopPage.setListeners.");
		
		titleBar.getBtnAdd().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(CphConstants.PAGE_WHOLESALE_WRITE, null);
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
				"?wholesale_id=" + getWholesale().getId();
				
		if(customerType == 0) {
			url += "&permission_type=all";
		} else if(customerType == 1) {
			url += "&permission_type=public";
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
		
		url += "num=0";
		
		super.downloadInfo();
	}
	
	@Override
	public boolean parseJSON(JSONObject objJSON) {

		try {
			//어차피 한번에 다 받을거라 중복으로 다운로드하는 경우 방지.
			if(models.size() != 0) {
				return true;
			}
			
			JSONArray arJSON = objJSON.getJSONArray("products");
			
			int size = arJSON.length();
			for(int i=0; i<size; i++) {
				Product product = new Product(arJSON.getJSONObject(i));
				product.setItemCode(CphConstants.ITEM_PRODUCT);
				product.setType(Product.TYPE_WHOLESALE);
				models.add(product);
			}
			
			if(size%2 == 1) {
				BaseModel emptyModel = new BaseModel() {};
				emptyModel.setItemCode(CphConstants.ITEM_PRODUCT);
				models.add(emptyModel);
			} else if(pageIndex == 1 && size == 0) {
				BaseModel emptyModel1 = new BaseModel() {};
				emptyModel1.setItemCode(CphConstants.ITEM_PRODUCT);
				models.add(emptyModel1);

				BaseModel emptyModel2 = new BaseModel() {};
				emptyModel2.setItemCode(CphConstants.ITEM_PRODUCT);
				models.add(emptyModel2);
			}
			
			totalCount = objJSON.getInt("productsCount");
			headerView.setTotalProduct(totalCount);

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