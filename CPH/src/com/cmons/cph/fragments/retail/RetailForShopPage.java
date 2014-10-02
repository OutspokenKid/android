package com.cmons.cph.fragments.retail;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;

import com.cmons.cph.R;
import com.cmons.cph.ShopActivity.OnAfterSelectCategoryListener;
import com.cmons.cph.classes.CmonsFragmentForRetail;
import com.cmons.cph.classes.CphAdapter;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Category;
import com.cmons.cph.models.Product;
import com.cmons.cph.models.Wholesale;
import com.cmons.cph.views.HeaderViewForRetailShop;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.IntentUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.HeaderGridView;

public class RetailForShopPage extends CmonsFragmentForRetail {
	
	private Wholesale wholesale;
	
	private HeaderViewForRetailShop headerView;
	private HeaderGridView gridView;
	
	private int categoryIndex = 0;
	private int totalCount;
	
	@Override
	public void onResume() {
		super.onResume();

		if(headerView != null) {
			headerView.refreshValues(wholesale);
		}
		
		if(models.size() == 0) {
			downloadInfo();
		}
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.retailShopPage_titleBar);
		gridView = (HeaderGridView) mThisView.findViewById(R.id.retailShopPage_gridView);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {
			wholesale = (Wholesale) getArguments().getSerializable("wholesale");
			title = wholesale.getName();
		} else {
			mActivity.closeTopPage();
		}
	}

	@Override
	public void createPage() {
		
		titleBar.getBackButton().setVisibility(View.VISIBLE);
		titleBar.getHomeButton().setVisibility(View.VISIBLE);
		
		headerView = new HeaderViewForRetailShop(mContext);
		headerView.init(wholesale);
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

		gridView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, 
					int visibleItemCount, int totalItemCount) {
			}
		});
		
		titleBar.getBtnAdd().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				requestPartnership();
			}
		});

		titleBar.getBtnNotice2().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putBoolean("isAppNotice", false);
				bundle.putBoolean("isOurNotice", false);
				bundle.putInt("wholesale_id", wholesale.getId());
				mActivity.showPage(CphConstants.PAGE_COMMON_NOTICE_LIST, bundle);
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

		headerView.getBtnFavorite().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				addFavorite();
			}
		});
		
		headerView.getBtnCall().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				call();
			}
		});
	}

	@Override
	public void setSizes() {
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_retail_shop;
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
				"?wholesale_id=" + wholesale.getId();
				
		if(categoryIndex != 0) {
			url += "&category_id=" + categoryIndex;
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

	public void requestPartnership() {
		
		String url = CphConstants.BASE_API_URL + "retails/customers/request" +
				"?wholesale_id=" + wholesale.getId();
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("RetailForShopPage.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToRequestPartnership);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("RetailForShopPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_requestPartnership);
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					ToastUtils.showToast(R.string.failToRequestPartnership);
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					ToastUtils.showToast(R.string.failToRequestPartnership);
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void addFavorite() {

		String url = CphConstants.BASE_API_URL + "retails/favorite/wholesales/add" +
				"?id=" + wholesale.getId();
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("RetailForShopPage.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToAddFavorite);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("RetailForShopPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_addFavorite);
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					ToastUtils.showToast(R.string.failToAddFavorite);
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					ToastUtils.showToast(R.string.failToAddFavorite);
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void call() {

		IntentUtils.call(mContext, "010" + wholesale.getPhone_number());
	}

////////////////////Custom classes.
}