package com.cmons.cph.fragments.wholesale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.classes.CphAdapter;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Product;
import com.cmons.cph.views.HeaderViewForShop;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.views.HeaderGridView;

public class WholesaleForShopPage extends CmonsFragmentForWholesale {
	
	private static final int TYPE_MENU1 = 0;
	private static final int TYPE_MENU2 = 1;
	private static final int TYPE_MENU3 = 2;
	
	private HeaderViewForShop headerView;
	private HeaderGridView gridView;
	
	private LinearLayout menuLinear;
	private Button btnCategory1, btnCategory2, btnCategory3;
	
	private View cover;
	private RelativeLayout categoryRelative;
	private Button btnClose;
	private ListView listView;
	
	private AlphaAnimation aaIn, aaOut;
	private boolean animating;
	private int type;
	
	@Override
	public void onResume() {
		super.onResume();
		
		downloadCategory();
		downloadInfo();
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.wholesaleShopPage_titleBar);
		gridView = (HeaderGridView) mThisView.findViewById(R.id.wholesaleShopPage_gridView);
		
		menuLinear = (LinearLayout) mThisView.findViewById(R.id.wholesaleShopPage_menuLinear);
		btnCategory1 = (Button) mThisView.findViewById(R.id.wholesaleShopPage_btnCategory1);
		btnCategory2 = (Button) mThisView.findViewById(R.id.wholesaleShopPage_btnCategory2);
		btnCategory3 = (Button) mThisView.findViewById(R.id.wholesaleShopPage_btnCategory3);
		
		cover = mThisView.findViewById(R.id.wholesaleShopPage_cover);
		categoryRelative = (RelativeLayout) mThisView.findViewById(R.id.wholesaleShopPage_categoryRelative);
		btnClose = (Button) mThisView.findViewById(R.id.wholesaleShopPage_btnClose);
		listView = (ListView) mThisView.findViewById(R.id.wholesaleShopPage_listView);
	}

	@Override
	public void setVariables() {

		title = "매장";
		
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
		
		aaIn = new AlphaAnimation(0, 1);
		aaIn.setDuration(300);
		aaIn.setAnimationListener(al);
		
		aaOut = new AlphaAnimation(1, 0);
		aaOut.setDuration(300);
		aaOut.setAnimationListener(al);
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		titleBar.getHomeButton().setVisibility(View.INVISIBLE);
		titleBar.getBtnAdd().setVisibility(View.VISIBLE);
		
		headerView = new HeaderViewForShop(mContext);
		AbsListView.LayoutParams al = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, 500);
		headerView.setLayoutParams(al);
		gridView.addHeaderView(headerView);
		
		adapter = new CphAdapter(mContext, getActivity().getLayoutInflater(), models);
		gridView.setAdapter(adapter);
	}

	@Override
	public void setListeners() {

		gridView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, 
					int visibleItemCount, int totalItemCount) {

				if(firstVisibleItem < 3) {

					LogUtils.log("###where.onScroll.  -top : " + (-headerView.getTop()) +
							", diffLength : " + headerView.diffLength
							);
					
					if(-headerView.getTop() >= headerView.diffLength) {
						menuLinear.setVisibility(View.VISIBLE);
//						circleHeaderView.hideTitleBar();
					} else {
						menuLinear.setVisibility(View.INVISIBLE);
//						circleHeaderView.showTitleBar();
					}
				} else if(firstVisibleItem == 3) {
					menuLinear.setVisibility(View.VISIBLE);
//					circleHeaderView.hideTitleBar();
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

				mActivity.showWritePage();
			}
		});
		
		btnCategory1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				showCategoryRelative(TYPE_MENU1);
			}
		});
		
		btnCategory2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				showCategoryRelative(TYPE_MENU2);
			}
		});
		
		btnCategory3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				showCategoryRelative(TYPE_MENU3);
			}
		});
		
		cover.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				hideCategoryRelative();
			}
		});
		
		btnClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				hideCategoryRelative();
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		LinearLayout.LayoutParams lp = null;
		
		//menuLinear.
		rp = (RelativeLayout.LayoutParams) menuLinear.getLayoutParams();
		rp.height = headerView.diffLength;
		rp.topMargin = ResizeUtils.getSpecificLength(96);
		
		//btnCategory1
		//btnCategory2
		//btnCategory3
		
		//categoryRelative.
		rp = (RelativeLayout.LayoutParams) categoryRelative.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(626);
		rp.height = ResizeUtils.getSpecificLength(695);
		categoryRelative.setPadding(ResizeUtils.getSpecificLength(5), 
				ResizeUtils.getSpecificLength(6), 
				ResizeUtils.getSpecificLength(6), 
				ResizeUtils.getSpecificLength(6));

		//ListView.
		rp = (RelativeLayout.LayoutParams) listView.getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(101);
		
		//btnClose.
		rp = (RelativeLayout.LayoutParams) btnClose.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(97);
		rp.height = rp.width;
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

		url = CphConstants.BASE_API_URL + "products"; 
		super.downloadInfo();
	}
	
	@Override
	public boolean parseJSON(JSONObject objJSON) {

		try {
			JSONArray arJSON = objJSON.getJSONArray("products");
			
			for(int j=0; j<5; j++) {
				int size = arJSON.length();
				for(int i=0; i<size; i++) {
					Product product = new Product(arJSON.getJSONObject(i));
					product.setItemCode(CphConstants.ITEM_PRODUCT);
					models.add(product);
				}
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		return false;
	}
	
//////////////////// Custom methods.

	public void downloadCategory() {
		
		String url = CphConstants.BASE_API_URL + "categories";
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WholesaleForShopPage.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("WholesaleForShopPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);
					
					
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void showCategoryRelative(int type) {

		if(!animating && categoryRelative.getVisibility() == View.VISIBLE) {
			
			this.type = type;
			
			categoryRelative.setVisibility(View.VISIBLE);
			cover.setVisibility(View.VISIBLE);
			
			categoryRelative.startAnimation(aaIn);
			cover.startAnimation(aaIn);
		}
	}
	
	public void hideCategoryRelative() {

		if(!animating && categoryRelative.getVisibility() != View.VISIBLE) {
			
			categoryRelative.setVisibility(View.INVISIBLE);
			cover.setVisibility(View.INVISIBLE);
			
			categoryRelative.startAnimation(aaOut);
			cover.startAnimation(aaOut);
		}
	}

////////////////////Custom classes.
}