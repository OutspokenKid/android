package com.cmons.cph.fragments.common;

import org.json.JSONObject;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.RetailActivity;
import com.cmons.cph.WholesaleActivity;
import com.cmons.cph.classes.CmonsFragmentForShop;
import com.outspoken_kid.utils.ResizeUtils;

public class GuidePage extends CmonsFragmentForShop {

	private boolean isWholesale;
	
	private ViewPager viewPager;
	private Button btnLeft;
	private Button btnRight;
	private TextView tvDesc;
	private Button btnSkip;
	
	@Override
	public void bindViews() {

		ivBg = (ImageView) mThisView.findViewById(R.id.commonGuidePage_ivBg);
		
		viewPager = (ViewPager) mThisView.findViewById(R.id.commonGuidePage_viewPager);
		btnLeft = (Button) mThisView.findViewById(R.id.commonGuidePage_btnLeft);
		btnRight = (Button) mThisView.findViewById(R.id.commonGuidePage_btnRight);
		tvDesc = (TextView) mThisView.findViewById(R.id.commonGuidePage_tvDesc);
		btnSkip = (Button) mThisView.findViewById(R.id.commonGuidePage_btnSkip);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {
			isWholesale = getArguments().getBoolean("isWholesale");
		}
	}

	@Override
	public void createPage() {
		
	}

	@Override
	public void setListeners() {
		
		btnLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

			}
		});
		
		btnRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;

		//viewPager.
		rp = (RelativeLayout.LayoutParams) viewPager.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(300);
		
		//btnLeft.
		rp = (RelativeLayout.LayoutParams) btnLeft.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(50);
		rp.height = ResizeUtils.getSpecificLength(50);
		
		//btnRight.
		rp = (RelativeLayout.LayoutParams) btnRight.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(50);
		rp.height = ResizeUtils.getSpecificLength(50);
		
		//tvDesc.
		rp = (RelativeLayout.LayoutParams) tvDesc.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(300);
		
		//btnSkip.
		rp = (RelativeLayout.LayoutParams) btnSkip.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(100);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_common_guide;
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
		
		return false;
	}

	@Override
	public int getBgResourceId() {

		return R.drawable.staff_bg;
	}
	
//////////////////// Custom methods.

	public void showPrevious() {
		
	}
	
	public void showNext() {
		
	}
	
	public void showMainPage() {
		
		if(isWholesale) {
			((WholesaleActivity)mActivity).showMainPage();
		} else {
			((RetailActivity)mActivity).showMainPage();
		}
	}
}
