package com.cmons.cph.fragments.wholesale;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.ResizeUtils;

public class WholesaleForShopPage extends CmonsFragmentForWholesale {
	
	private static final int TYPE_MENU1 = 0;
	private static final int TYPE_MENU2 = 1;
	private static final int TYPE_MENU3 = 2;
	
	private GridView gridView;
	
	private RelativeLayout menuRelative;
	private Button btnCategory1, btnCategory2, btnCategory3;
	
	private View cover;
	private RelativeLayout categoryRelative;
	private Button btnClose;
	private ListView listView;
	
	private AlphaAnimation aaIn, aaOut;
	private boolean animating;
	private int type;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.wholesaleShopPage_titleBar);
		gridView = (GridView) mThisView.findViewById(R.id.wholesaleShopPage_gridView);
		
		menuRelative = (RelativeLayout) mThisView.findViewById(R.id.wholesaleShopPage_menuRelative);
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

		titleBar.addBackButton(R.drawable.btn_back_category, 162, 92);
		titleBar.getBtnAdd().setVisibility(View.VISIBLE);
	}

	@Override
	public void setListeners() {

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
		
		titleBar.getLayoutParams().height = ResizeUtils.getSpecificLength(96);

		RelativeLayout.LayoutParams rp = null;
		
		//menuRelative.
		rp = (RelativeLayout.LayoutParams) menuRelative.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(96);
		rp.topMargin = ResizeUtils.getSpecificLength(96);
		
		//btnCategory1
		rp = (RelativeLayout.LayoutParams) btnCategory1.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(100);
		rp.height = ResizeUtils.getSpecificLength(80);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		
		//btnCategory2
		rp = (RelativeLayout.LayoutParams) btnCategory2.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(100);
		rp.height = ResizeUtils.getSpecificLength(80);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		
		//btnCategory3
		rp = (RelativeLayout.LayoutParams) btnCategory3.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(100);
		rp.height = ResizeUtils.getSpecificLength(80);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		
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
	public boolean parseJSON(JSONObject objJSON) {
		// TODO Auto-generated method stub
		return false;
	}
	
//////////////////// Custom methods.
	
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
