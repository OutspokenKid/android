package com.cmons.cph.fragments.wholesale;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cmons.classes.CmonsFragmentForWholesale;
import com.cmons.cph.R;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.ResizeUtils;

public class WholesaleMainPage extends CmonsFragmentForWholesale {

	private TitleBar titleBar;
	
	private Button btnShop;
	private Button btnNotice;
	private Button btnManagement;
	private Button btnOrder;
	private Button btnSample;
	private Button btnCustomer;
	private Button btnStaff;
	private Button btnSetting;
	
	private View cover;
	private RelativeLayout noticeRelative;
	private Button btnClose;
	private ListView listView;
	
	private AlphaAnimation aaIn, aaOut;
	private boolean animating;

	@Override
	public void onResume() {
		super.onResume();
		
		checkNotification();
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.wholesaleMainPage_titleBar);
		
		btnShop = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnShop);
		btnNotice = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnNotice);
		btnManagement = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnManagement);
		btnOrder = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnOrder);
		btnSample = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnSample);
		btnCustomer = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnCustomer);
		btnStaff = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnStaff);
		btnSetting = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnSetting);
		
		cover = mThisView.findViewById(R.id.wholesaleMainPage_cover);
		noticeRelative = (RelativeLayout) mThisView.findViewById(R.id.wholesaleMainPage_noticeRelative);
		btnClose = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnClose);
		listView = (ListView) mThisView.findViewById(R.id.wholesaleMainPage_listView);
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

		titleBar.getBtnNotice()
				.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(noticeRelative.getVisibility() == View.VISIBLE) {
					hideNoticeRelative();
				} else {
					showNoticeRelative();
				}
			}
		});
		
		titleBar.getBtnAdd()
				.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showWritePage();
			}
		});
		
		btnShop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				mActivity.showShopPage();
			}
		});
		
		btnNotice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				//Show notice popup.
			}
		});
		
		btnManagement.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showManagementPage();
			}
		});
		
		btnOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showOrderPage();
			}
		});
		
		btnSample.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showSamplePage();
			}
		});
		
		btnCustomer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showCustomerPage();
			}
		});
		
		btnStaff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showStaffPage();
			}
		});
		
		btnSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showSettingPage();
			}
		});
		
		cover.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				hideNoticeRelative();
			}
		});
		
		btnClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				hideNoticeRelative();
			}
		});
	}

	@Override
	public void setSizes() {
		
		titleBar.getLayoutParams().height = ResizeUtils.getSpecificLength(96);
		
		RelativeLayout.LayoutParams rp = null;
		
		int length_long = ResizeUtils.getSpecificLength(480);
		int length_short = ResizeUtils.getSpecificLength(240);
		
		//btnShop.
		rp = (RelativeLayout.LayoutParams) btnShop.getLayoutParams();
		rp.width = length_long;
		rp.height = length_long;
		
		//btnNotice.
		rp = (RelativeLayout.LayoutParams) btnNotice.getLayoutParams();
		rp.width = LayoutParams.MATCH_PARENT;
		rp.height = length_short;
		
		//btnManagement.
		rp = (RelativeLayout.LayoutParams) btnManagement.getLayoutParams();
		rp.width = LayoutParams.MATCH_PARENT;
		rp.height = length_short;
		
		//btnOrder.
		rp = (RelativeLayout.LayoutParams) btnManagement.getLayoutParams();
		rp.width = length_short;
		rp.height = length_short;
		
		//btnSample.
		rp = (RelativeLayout.LayoutParams) btnManagement.getLayoutParams();
		rp.width = length_short;
		rp.height = length_short;
		
		//btnPartner.
		rp = (RelativeLayout.LayoutParams) btnManagement.getLayoutParams();
		rp.width = LayoutParams.MATCH_PARENT;
		rp.height = length_short;
		
		//btnStaff.
		rp = (RelativeLayout.LayoutParams) btnStaff.getLayoutParams();
		rp.width = length_short;
		rp.height = length_short;
		
		//btnSetting.
		rp = (RelativeLayout.LayoutParams) btnStaff.getLayoutParams();
		rp.width = length_short;
		rp.height = length_short;
	}
	
	@Override
	public int getContentViewId() {

		return R.layout.fragment_wholesale_main;
	}

	@Override
	public void onRefreshPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onBackPressed() {
		
		if(noticeRelative.getVisibility() == View.VISIBLE) {
			hideNoticeRelative();
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}
	
//////////////////// Custom methods.
	
	public void checkNotification() {
		
		titleBar.getBtnNotice().setVisibility(View.VISIBLE);
	}
	
	public void showNoticeRelative() {

		if(!animating && noticeRelative.getVisibility() == View.VISIBLE) {
			
			noticeRelative.setVisibility(View.VISIBLE);
			cover.setVisibility(View.VISIBLE);
			
			noticeRelative.startAnimation(aaIn);
			cover.startAnimation(aaIn);
		}
	}
	
	public void hideNoticeRelative() {

		if(!animating && noticeRelative.getVisibility() != View.VISIBLE) {
			
			noticeRelative.setVisibility(View.INVISIBLE);
			cover.setVisibility(View.INVISIBLE);
			
			noticeRelative.startAnimation(aaOut);
			cover.startAnimation(aaOut);
		}
	}

}