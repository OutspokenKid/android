package com.cmons.cph.fragments.common;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForShop;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.views.PageNavigatorView;
import com.outspoken_kid.views.SpeedControllableViewPager;

public class GuidePage extends CmonsFragmentForShop {

	private boolean isWholesale;

	private View topImage;
	private SpeedControllableViewPager viewPager;
	private PageNavigatorView pageNavigatorView;
	private Button btnLeft;
	private Button btnRight;
	private TextView tvDesc;
	private Button btnSkip;
	
	private PagerAdapterForTutorial pagerAdapter;
	private int[] imageResId;
	private int[] stringResId;
	
	@Override
	public void onResume() {
		super.onResume();
		
		setView(viewPager.getCurrentItem());
	}
	
	@Override
	public void bindViews() {

		ivBg = (ImageView) mThisView.findViewById(R.id.commonGuidePage_ivBg);
		
		topImage = mThisView.findViewById(R.id.commonGuidePage_topImage);
		viewPager = (SpeedControllableViewPager) mThisView.findViewById(R.id.commonGuidePage_viewPager);
		pageNavigatorView = (PageNavigatorView) mThisView.findViewById(R.id.commonGuidePage_pageNavigatorView);
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
		
		if(isWholesale) {
			imageResId = new int[]{
					R.drawable.a1,
					R.drawable.a2,
					R.drawable.a3,
					R.drawable.a4,
					R.drawable.a5,
					R.drawable.a6,
					R.drawable.a7,
					R.drawable.a8,
					R.drawable.a9,
					R.drawable.a10,
					R.drawable.a11,
					R.drawable.a12,
					R.drawable.a13,
					R.drawable.a14,
			};
			
			stringResId = new int[]{
					R.string.texta01,
					R.string.texta02,
					R.string.texta03,
					R.string.texta04,
					R.string.texta05,
					R.string.texta06,
					R.string.texta07,
					R.string.texta08,
					R.string.texta09,
					R.string.texta10,
					R.string.texta11,
					R.string.texta12,
					R.string.texta13,
					R.string.texta14,
			};
		} else {
			imageResId = new int[]{
					R.drawable.b1,
					R.drawable.b2,
					R.drawable.b3,
					R.drawable.b4,
					R.drawable.b5,
					R.drawable.b6,
					R.drawable.b7,
					R.drawable.b8,
					R.drawable.b9,
					R.drawable.b10,
					R.drawable.b11,
					R.drawable.b12,
					R.drawable.b13,
					R.drawable.b14,
					R.drawable.b15,
					R.drawable.b16,
					R.drawable.b17,
					R.drawable.b18,
			};
			
			stringResId = new int[]{
					R.string.textb01,
					R.string.textb02,
					R.string.textb03,
					R.string.textb04,
					R.string.textb05,
					R.string.textb06,
					R.string.textb07,
					R.string.textb08,
					R.string.textb09,
					R.string.textb10,
					R.string.textb11,
					R.string.textb12,
					R.string.textb13,
					R.string.textb14,
					R.string.textb15,
					R.string.textb16,
					R.string.textb17,
					R.string.textb18,
			};
		}
	}

	@Override
	public void createPage() {

		if(isWholesale) {
			topImage.setBackgroundResource(R.drawable.tutorial_title1);
			btnLeft.setBackgroundResource(R.drawable.tutorial_next2_btn);
			btnRight.setBackgroundResource(R.drawable.tutorial_next1_btn);
		} else {
			topImage.setBackgroundResource(R.drawable.tutorial_title2);
			btnLeft.setBackgroundResource(R.drawable.tutorial_next2b_btn);
			btnRight.setBackgroundResource(R.drawable.tutorial_next1b_btn);
		}
		
		if(imageResId != null) {
			pageNavigatorView.setSize(imageResId.length);
			pageNavigatorView.setIndex(0);
			pageNavigatorView.invalidate();
		}
		
		pagerAdapter = new PagerAdapterForTutorial();
		viewPager.setAdapter(pagerAdapter);
		
		pageNavigatorView.setColor(Color.WHITE, Color.argb(100, 255, 255, 255));
	}

	@Override
	public void setListeners() {
		
		btnLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
			}
		});
		
		btnRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
			}
		});

		btnSkip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.closeTopPage();
			}
		});
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(final int position) {

				setView(position);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;

		//topImage.
		rp = (RelativeLayout.LayoutParams) topImage.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(578);
		rp.height = ResizeUtils.getSpecificLength(84);
		rp.topMargin = ResizeUtils.getSpecificLength(17);
		
		//viewPager.
		rp = (RelativeLayout.LayoutParams) viewPager.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(710);
		rp.topMargin = ResizeUtils.getSpecificLength(120);
		
		//pageNavigatorView.
		rp = (RelativeLayout.LayoutParams) pageNavigatorView.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(16);
		rp.topMargin = -ResizeUtils.getSpecificLength(20);
		
		//btnLeft.
		rp = (RelativeLayout.LayoutParams) btnLeft.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(70);
		rp.height = ResizeUtils.getSpecificLength(83);
		
		//btnRight.
		rp = (RelativeLayout.LayoutParams) btnRight.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(70);
		rp.height = ResizeUtils.getSpecificLength(83);
		
		//tvDesc.
		int p = ResizeUtils.getSpecificLength(20);
		tvDesc.setPadding(p, p, p, p);
		FontUtils.setFontSize(tvDesc, 24);
		
		//btnSkip.
		rp = (RelativeLayout.LayoutParams) btnSkip.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(88);
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
		
		//백키 무시.
		return true;
	}

	@Override
	public boolean parseJSON(JSONObject objJSON) {
		
		return false;
	}

	@Override
	public int getBgResourceId() {

		if(isWholesale) {
			return R.drawable.tutorial_bg;
		} else {
			return R.drawable.tutorial_bg2;
		}
	}
	
//////////////////// Custom methods.

	public void showPrevious() {
		
	}
	
	public void showNext() {
		
	}

	public void setView(int position) {
		
		pageNavigatorView.setIndex(position);
		tvDesc.setText(stringResId[position]);
		
		if(position == 0) {
			btnLeft.setVisibility(View.INVISIBLE);
			btnRight.setVisibility(View.VISIBLE);
			btnSkip.setBackgroundResource(R.drawable.tutorial_skip_btn);
		} else if(position == imageResId.length - 1) {
			btnLeft.setVisibility(View.VISIBLE);
			btnRight.setVisibility(View.INVISIBLE);
			btnSkip.setBackgroundResource(R.drawable.tutorial_start_btn);
		} else {
			btnLeft.setVisibility(View.VISIBLE);
			btnRight.setVisibility(View.VISIBLE);
			btnSkip.setBackgroundResource(R.drawable.tutorial_skip_btn);
		}
	}
	
//////////////////// Custom classes.
	
	public class PagerAdapterForTutorial extends PagerAdapter {
		
		@Override
		public int getCount() {
			
			return imageResId.length;
		}
		
		@TargetApi(Build.VERSION_CODES.HONEYCOMB)
		@Override
		public Object instantiateItem(ViewGroup container, final int position) {

			final ImageView ivImage = new ImageView(mContext);
			
			if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
				 ivImage.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
			}
			
			ivImage.setScaleType(ScaleType.CENTER_CROP);
			container.addView(ivImage);
			ivImage.setImageResource(imageResId[position]);
			
			return ivImage;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

			try {
				ImageView iv = (ImageView) object;
				iv.setImageDrawable(null);
				container.removeView(iv);
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
		}
		
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return arg0 == arg1;
		}
	}
}
