package com.byecar.fragments.dealer;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.byecar.byecarplusfordealer.MainActivity;
import com.byecar.byecarplusfordealer.R;
import com.byecar.classes.BCPFragment;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.ResizeUtils;

public class MainPage extends BCPFragment {

	private Button btnGuide;
	private Button btnNotice;
	private Button btnAuction;
	private Button btnMyBids;
	private Button btnUsed;
	private Button btnOnSale;
	private ImageView ivBanner;
	
	private ListView listView;
	
	private View icon;
	private Button btnRegistration2;

	private RelativeLayout buttonRelative;
	private Button btnOrderSmall;
	private Button btnSearch;
	private Button btnRegistration;
	private Button btnOrderBig;
	
	private int menuIndex;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.mainPage_titleBar);
		
		btnGuide = (Button) mThisView.findViewById(R.id.mainPage_btnGuide);
		btnNotice = (Button) mThisView.findViewById(R.id.mainPage_btnNotice);
		btnAuction = (Button) mThisView.findViewById(R.id.mainPage_btnAuction);
		btnMyBids = (Button) mThisView.findViewById(R.id.mainPage_btnMyBids);
		btnUsed = (Button) mThisView.findViewById(R.id.mainPage_btnUsed);
		btnOnSale = (Button) mThisView.findViewById(R.id.mainPage_btnOnSale);
		ivBanner = (ImageView) mThisView.findViewById(R.id.mainPage_ivBanner);
		
		listView = (ListView) mThisView.findViewById(R.id.mainPage_listView);
		
		icon = mThisView.findViewById(R.id.mainPage_icon);
		btnRegistration2 = (Button) mThisView.findViewById(R.id.mainPage_btnRegistration2);
		
		buttonRelative = (RelativeLayout) mThisView.findViewById(R.id.mainPage_buttonRelative);
		btnOrderSmall = (Button) mThisView.findViewById(R.id.mainPage_btnOrderSmall);
		btnSearch = (Button) mThisView.findViewById(R.id.mainPage_btnSearch);
		btnRegistration = (Button) mThisView.findViewById(R.id.mainPage_btnRegistration);
		btnOrderBig = (Button) mThisView.findViewById(R.id.mainPage_btnOrderBig);
	}

	@Override
	public void setVariables() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createPage() {

		setMenu(0);
	}

	@Override
	public void setListeners() {

		titleBar.getMenuButton().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(((MainActivity)mActivity).isOpen()) {
					((MainActivity)mActivity).closeMenu();
				} else {
					((MainActivity)mActivity).openMenu();
				}
			}
		});
	
		btnAuction.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(0);
			}
		});
		
		btnMyBids.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(1);
			}
		});
		
		btnUsed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(2);
			}
		});
		
		btnOnSale.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(3);
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;

		(titleBar.getMenuButton().getLayoutParams()).width = ResizeUtils.getSpecificLength(325);
		
		//bgForButtons.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.mainPage_bgForButtons).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(88);
		
		ResizeUtils.viewResizeForRelative(60, 60, btnGuide, null, null, new int[]{0, 14, 12, 0});
		ResizeUtils.viewResizeForRelative(60, 60, btnNotice, null, null, new int[]{0, 0, 6, 0});
		ResizeUtils.viewResizeForRelative(160, 88, btnAuction, null, null, null);
		ResizeUtils.viewResizeForRelative(160, 88, btnMyBids, null, null, null);
		ResizeUtils.viewResizeForRelative(160, 88, btnUsed, null, null, null);
		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, 88, btnOnSale, null, null, null);
		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, 88, ivBanner, null, null, null);
		
		ResizeUtils.viewResizeForRelative(453, 249, icon, null, null, new int[]{0, 100, 0, 80});
		ResizeUtils.viewResizeForRelative(544, 72, btnRegistration2, null, null, null);

		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, 100, buttonRelative, null, null, null);
		ResizeUtils.viewResizeForRelative(72, 72, btnOrderSmall, null, null, new int[]{8, 0, 0, 8});
		ResizeUtils.viewResizeForRelative(72, 72, btnSearch, null, null, new int[]{8, 0, 0, 8});
		ResizeUtils.viewResizeForRelative(464, 72, btnRegistration, null, null, new int[]{8, 0, 0, 8});
		ResizeUtils.viewResizeForRelative(624, 72, btnOrderBig, null, null, new int[]{8, 0, 0, 8});
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_main_for_dealer;
	}

	@Override
	public int getBackButtonResId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBackButtonWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBackButtonHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRootViewResId() {

		return R.id.mainPage_mainLayout;
	}

	@Override
	public boolean parseJSON(JSONObject objJSON) {
		// TODO Auto-generated method stub
		return false;
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
	public void onResume() {
		
		super.onResume();
		titleBar.getMenuButton().setVisibility(View.VISIBLE);
	}
	
//////////////////// Custom methods.
	
	public void setMenu(int index) {

		setMenuButtons(index);
		setIconAndButtons(index);
		setButtonRelative(index);
		
		menuIndex = index;
		refreshPage();
	}
	
	public void setMenuButtons(int index) {
		
		switch(index) {
		
		case 0:
			btnAuction.setBackgroundResource(R.drawable.main_tab1_tab_a);
			btnMyBids.setBackgroundResource(R.drawable.main_tab2_tab_b);
			btnUsed.setBackgroundResource(R.drawable.main_tab3_tab_b);
			btnOnSale.setBackgroundResource(R.drawable.main_tab4_tab_b);
			break;
			
		case 1:
			btnAuction.setBackgroundResource(R.drawable.main_tab1_tab_b);
			btnMyBids.setBackgroundResource(R.drawable.main_tab2_tab_a);
			btnUsed.setBackgroundResource(R.drawable.main_tab3_tab_b);
			btnOnSale.setBackgroundResource(R.drawable.main_tab4_tab_b);
			break;
			
		case 2:
			btnAuction.setBackgroundResource(R.drawable.main_tab1_tab_b);
			btnMyBids.setBackgroundResource(R.drawable.main_tab2_tab_b);
			btnUsed.setBackgroundResource(R.drawable.main_tab3_tab_a);
			btnOnSale.setBackgroundResource(R.drawable.main_tab4_tab_b);
			break;
		case 3:
			btnAuction.setBackgroundResource(R.drawable.main_tab1_tab_b);
			btnMyBids.setBackgroundResource(R.drawable.main_tab2_tab_b);
			btnUsed.setBackgroundResource(R.drawable.main_tab3_tab_b);
			btnOnSale.setBackgroundResource(R.drawable.main_tab4_tab_a);
			break;
		}
	}
	
	public void setIconAndButtons(int index) {

		if(index == 1) {
			icon.setBackgroundResource(R.drawable.my_bid_toon);
		} else if(index == 3) {
			icon.setBackgroundResource(R.drawable.sell_toon);
		}
		
		icon.setVisibility(View.INVISIBLE);
		btnRegistration2.setVisibility(View.INVISIBLE);
	}
	
	public void setButtonRelative(int index) {
		
		switch(index) {
		
		case 0:
			buttonRelative.setVisibility(View.VISIBLE);
			ResizeUtils.setMargin(listView, new int[]{0, 0, 0, 88});
			btnOrderSmall.setVisibility(View.INVISIBLE);
			btnSearch.setVisibility(View.INVISIBLE);
			btnRegistration.setVisibility(View.INVISIBLE);
			btnOrderBig.setVisibility(View.VISIBLE);
			break;
			
		case 2:
			buttonRelative.setVisibility(View.VISIBLE);
			ResizeUtils.setMargin(listView, new int[]{0, 0, 0, 88});
			btnOrderSmall.setVisibility(View.VISIBLE);
			btnSearch.setVisibility(View.VISIBLE);
			btnRegistration.setVisibility(View.VISIBLE);
			btnOrderBig.setVisibility(View.INVISIBLE);
			break;
		
		case 1:
		case 3:
			buttonRelative.setVisibility(View.INVISIBLE);
			ResizeUtils.setMargin(listView, new int[]{0, 0, 0, 0});
			break;
		}
	}
	
	public void showIconAndButton(int index) {
		
		icon.setVisibility(View.VISIBLE);
		
		if(index == 3) {
			btnRegistration2.setVisibility(View.VISIBLE);
		}
	}
}
