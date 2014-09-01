package com.cmons.cph.fragments.retail;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cmons.cph.R;
import com.cmons.cph.RetailActivity;
import com.cmons.cph.ShopActivity.OnAfterSelectCategoryListener;
import com.cmons.cph.classes.CmonsFragmentForRetail;
import com.cmons.cph.classes.CphAdapter;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Category;
import com.cmons.cph.models.Notice;
import com.cmons.cph.models.Product;
import com.cmons.cph.views.HeaderViewForRetailMain;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.views.HeaderGridView;

public class RetailMainPage extends CmonsFragmentForRetail {
	
	private HeaderViewForRetailMain headerView;
	private HeaderGridView gridView;

	private View cover;
	private RelativeLayout noticeRelative;
	private Button btnClose;
	private ListView listView;
	private CphAdapter noticeAdapter;
	private ArrayList<BaseModel> notices = new ArrayList<BaseModel>();
	
	private RelativeLayout menuRelative;
	private Button[] menuButtons;
	
	private TranslateAnimation taIn, taOut;
	private AlphaAnimation aaIn, aaOut;
	private boolean animating;
	
	private String[] orders;
	
	private int categoryIndex = 0;
	private int order = 0;
	
	@Override
	public void onResume() {
		super.onResume();
		
		downloadInfo();
		downloadNotices();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		headerView.setPlaying(false);
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.retailMainPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.retailMainPage_ivBg);
		
		gridView = (HeaderGridView) mThisView.findViewById(R.id.retailMainPage_gridView);
		menuRelative = (RelativeLayout) mThisView.findViewById(R.id.retailMainPage_menuRelative);

		menuButtons = new Button[9];
		menuButtons[0] = (Button) mThisView.findViewById(R.id.retailMainPage_btnNotice);
		menuButtons[1] = (Button) mThisView.findViewById(R.id.retailMainPage_btnCustomer);
		menuButtons[2] = (Button) mThisView.findViewById(R.id.retailMainPage_btnSearch);
		menuButtons[3] = (Button) mThisView.findViewById(R.id.retailMainPage_btnBasket);
		menuButtons[4] = (Button) mThisView.findViewById(R.id.retailMainPage_btnFavoriteShop);
		menuButtons[5] = (Button) mThisView.findViewById(R.id.retailMainPage_btnFavoriteProduct);
		menuButtons[6] = (Button) mThisView.findViewById(R.id.retailMainPage_btnStaff);
		menuButtons[7] = (Button) mThisView.findViewById(R.id.retailMainPage_btnBusiness);
		menuButtons[8] = (Button) mThisView.findViewById(R.id.retailMainPage_btnSetting);
		
		cover = mThisView.findViewById(R.id.retailMainPage_cover);
		noticeRelative = (RelativeLayout) mThisView.findViewById(R.id.retailMainPage_noticeRelative);
		btnClose = (Button) mThisView.findViewById(R.id.retailMainPage_btnClose);
		listView = (ListView) mThisView.findViewById(R.id.retailMainPage_listView);
	}

	@Override
	public void setVariables() {

		AnimationListener al = new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {

				animating = true;
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {

				animating = false;
			}
		};
		
		taIn = new TranslateAnimation(
				TranslateAnimation.RELATIVE_TO_SELF, 0, 
				TranslateAnimation.RELATIVE_TO_SELF, 0,
				TranslateAnimation.RELATIVE_TO_SELF, -1, 
				TranslateAnimation.RELATIVE_TO_SELF, 0);
		taIn.setDuration(300);
		taIn.setAnimationListener(al);
		
		taOut = new TranslateAnimation(
				TranslateAnimation.RELATIVE_TO_SELF, 0, 
				TranslateAnimation.RELATIVE_TO_SELF, 0,
				TranslateAnimation.RELATIVE_TO_SELF, 0, 
				TranslateAnimation.RELATIVE_TO_SELF, -1);
		taOut.setDuration(300);
		taOut.setAnimationListener(al);
		
		aaIn = new AlphaAnimation(0, 1);
		aaIn.setDuration(300);
		aaIn.setAnimationListener(al);
		
		aaOut = new AlphaAnimation(1, 0);
		aaOut.setDuration(300);
		aaOut.setAnimationListener(al);
	}

	@Override
	public void createPage() {

		orders = new String[]{"최신순", "판매량순"};

		titleBar.getMenuButton().setVisibility(View.VISIBLE);
		
		headerView = new HeaderViewForRetailMain(mContext);
		headerView.init((RetailActivity)mActivity);
		AbsListView.LayoutParams al = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		headerView.setLayoutParams(al);
		gridView.addHeaderView(headerView);
		gridView.setNumColumns(2);
		adapter = new CphAdapter(mContext, getActivity().getLayoutInflater(), models);
		gridView.setAdapter(adapter);
		
		noticeAdapter = new CphAdapter(mContext, getActivity().getLayoutInflater(), notices);
		listView.setAdapter(noticeAdapter);
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
		
		titleBar.getMenuButton().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				if(menuRelative.getVisibility() == View.VISIBLE) {
					hideMenuRelative();
				} else {
					showMenuRelative();
				}
			}
		});
		
		titleBar.getBtnAddPartner().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(CphConstants.PAGE_RETAIL_ADD_PARTNER, null);
			}
		});
		
		titleBar.getBtnNotice().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(noticeRelative.getVisibility() == View.VISIBLE) {
					hideNoticeRelative();
				} else {
					showNoticeRelative();
				}
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

		cover.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(noticeRelative.getVisibility() == View.VISIBLE) {
					hideNoticeRelative();
				} else if(menuRelative.getVisibility() == View.VISIBLE){
					hideMenuRelative();
				}
			}
		});
		
		btnClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				hideNoticeRelative();
			}
		});
	
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				hideNoticeRelative();
			}
		});

		int size = menuButtons.length;
		for(int i=0; i<size; i++) {
			
			final int INDEX = i;
			
			menuButtons[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					hideMenuRelative();

					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							
							Bundle bundle = new Bundle();

							switch(INDEX) {
							
							case 0:
								if(noticeRelative.getVisibility() == View.VISIBLE) {
									hideNoticeRelative();
								} else {
									showNoticeRelative();
								}
								break;
							case 1:
								mActivity.showPage(CphConstants.PAGE_RETAIL_CUSTOMER_LIST, bundle);
								break;
							case 2:
								mActivity.showPage(CphConstants.PAGE_RETAIL_SEARCH, bundle);
								break;
							case 3:
								mActivity.showPage(CphConstants.PAGE_RETAIL_WISH_LIST, bundle);
								break;
							case 4:
								mActivity.showPage(CphConstants.PAGE_RETAIL_FAVORITE_SHOP, bundle);
								break;
							case 5:
								mActivity.showPage(CphConstants.PAGE_RETAIL_FAVORITE_PRODUCT, bundle);
								break;
							case 6:
								mActivity.showPage(CphConstants.PAGE_COMMON_STAFF, bundle);
								break;
							case 7:
								mActivity.showPage(CphConstants.PAGE_RETAIL_MANAGEMENT, bundle);
								break;
							case 8:
								mActivity.showPage(CphConstants.PAGE_RETAIL_SETTING, bundle);
								break;
							}
						}
					}, 300);
				}
			});
		}
	}

	@Override
	public void setSizes() {
		
		RelativeLayout.LayoutParams rp = null;

		int length_short = ResizeUtils.getScreenWidth()/3;
		
		//titleBar2.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.retailMainPage_titleBar2).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(96);
		
		//cover.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.retailMainPage_cover).getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(96);
		
		//noticeRelative.
		rp = (RelativeLayout.LayoutParams) menuRelative.getLayoutParams();
		rp.height = ResizeUtils.getScreenWidth() + ResizeUtils.getSpecificLength(30);
		
		//ivBg.
		rp = (RelativeLayout.LayoutParams) ivBg.getLayoutParams();
		rp.height = ResizeUtils.getScreenWidth();
		rp.bottomMargin = ResizeUtils.getSpecificLength(30);
	
		for(int i=0; i<menuButtons.length; i++) {
			rp = (RelativeLayout.LayoutParams) menuButtons[i].getLayoutParams();
			rp.width = (i % 3 == 2)? LayoutParams.MATCH_PARENT : length_short;
			rp.height = length_short;
		}
		
		//handle.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.retailMainPage_handle).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(30);
		
		//noticeRelative.
		rp = (RelativeLayout.LayoutParams) noticeRelative.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(630);
		rp.height = ResizeUtils.getSpecificLength(711);
		rp.leftMargin = ResizeUtils.getSpecificLength(62);
		rp.topMargin = ResizeUtils.getSpecificLength(58);
		
		//btnClose.
		rp = (RelativeLayout.LayoutParams) btnClose.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(52);
		rp.height = ResizeUtils.getSpecificLength(52);
		rp.topMargin = ResizeUtils.getSpecificLength(96);
		rp.rightMargin = ResizeUtils.getSpecificLength(32);
		
		//listView.
		rp = (RelativeLayout.LayoutParams) listView.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(530);
		rp.leftMargin = ResizeUtils.getSpecificLength(24);
		rp.rightMargin = ResizeUtils.getSpecificLength(24);
		rp.bottomMargin = ResizeUtils.getSpecificLength(24);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_retail_main;
	}

	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onBackPressed() {

		if(noticeRelative.getVisibility() == View.VISIBLE) {
			hideNoticeRelative();
			return true;
		} else if(menuRelative.getVisibility() == View.VISIBLE) {
			hideMenuRelative();
			return true;
		}
		
		return false;
	}

	@Override
	public void downloadInfo() {

		notices.clear();
		noticeAdapter.notifyDataSetChanged();
		
		url = CphConstants.BASE_API_URL + "products" +
				"?retail_id=" + retail.getId();
		
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
				product.setType(Product.TYPE_RETAIL);
				models.add(product);
			}

			headerView.setTotalProduct(objJSON.getInt("productsCount"));
					
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

		return R.drawable.retail_bg;
	}
	
//////////////////// Custom methods.

	public void downloadNotices() {
		
		String url = CphConstants.BASE_API_URL + "posts/notices" +
				"?num=0";
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("CmonsFragment.onError." + "\nurl : " + url);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("CmonsFragment.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					JSONArray arJSON = objJSON.getJSONArray("notices");
					
					int size = arJSON.length();
					
					for(int i=0; i<size; i++) {
						Notice notice = new Notice(arJSON.getJSONObject(i));
						notice.setItemCode(CphConstants.ITEM_NOTICE);
						notices.add(notice);
					}

					noticeAdapter.notifyDataSetChanged();
					checkNewMessage();
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}

	public void checkNewMessage() {
		
		titleBar.getBtnNotice().setVisibility(View.VISIBLE);
	}
	
	public void showMenuRelative() {
		
		if(!animating && menuRelative.getVisibility() != View.VISIBLE) {
			
			menuRelative.setVisibility(View.VISIBLE);
			cover.setVisibility(View.VISIBLE);
			
			menuRelative.startAnimation(taIn);
			cover.startAnimation(aaIn);
		}
	}
	
	public void hideMenuRelative() {
		
		if(!animating && menuRelative.getVisibility() == View.VISIBLE) {
			
			menuRelative.setVisibility(View.INVISIBLE);
			cover.setVisibility(View.INVISIBLE);
			
			menuRelative.startAnimation(taOut);
			cover.startAnimation(aaOut);
		}
	}

	public void showNoticeRelative() {

		if(!animating && noticeRelative.getVisibility() != View.VISIBLE) {
			
			noticeRelative.setVisibility(View.VISIBLE);
			cover.setVisibility(View.VISIBLE);
			
			noticeRelative.startAnimation(aaIn);
			cover.startAnimation(aaIn);
		}
	}
	
	public void hideNoticeRelative() {

		if(!animating && noticeRelative.getVisibility() == View.VISIBLE) {
			
			noticeRelative.setVisibility(View.INVISIBLE);
			cover.setVisibility(View.INVISIBLE);
			
			noticeRelative.startAnimation(aaOut);
			cover.startAnimation(aaOut);
		}
	}
}