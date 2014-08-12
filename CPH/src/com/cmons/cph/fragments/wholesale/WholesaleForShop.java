package com.cmons.cph.fragments.wholesale;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cmons.classes.CmonsFragmentForWholesale;
import com.cmons.cph.R;
import com.cmons.cph.views.TitleBar;

public class WholesaleForShop extends CmonsFragmentForWholesale {
	
	private static final int TYPE_TYPE1 = 0;
	private static final int TYPE_TYPE2 = 1;
	private static final int TYPE_TYPE3 = 2;
	
	private TitleBar titleBar;
	private Button btnWrite;
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
		btnWrite = (Button) mThisView.findViewById(R.id.wholesaleShopPage_btnWrite);
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
		// TODO Auto-generated method stub

	}

	@Override
	public void setListeners() {

		btnWrite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showWritePage();
			}
		});
		
		btnCategory1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				showCategoryRelative(TYPE_TYPE1);
			}
		});
		
		btnCategory2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				showCategoryRelative(TYPE_TYPE2);
			}
		});
		
		btnCategory3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				showCategoryRelative(TYPE_TYPE3);
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
		// TODO Auto-generated method stub

	}

	@Override
	public int getContentViewId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onRefreshPage() {
		// TODO Auto-generated method stub

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
}
