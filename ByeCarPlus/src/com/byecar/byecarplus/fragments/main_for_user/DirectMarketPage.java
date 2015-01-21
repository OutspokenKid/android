package com.byecar.byecarplus.fragments.main_for_user;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.byecarplus.classes.BCPAPIs;
import com.byecar.byecarplus.classes.BCPConstants;
import com.byecar.byecarplus.classes.BCPFragment;
import com.byecar.byecarplus.classes.ImagePagerAdapter;
import com.byecar.byecarplus.classes.ImagePagerAdapter.OnPagerItemClickedListener;
import com.byecar.byecarplus.models.Car;
import com.byecar.byecarplus.views.NormalCarView;
import com.byecar.byecarplus.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.views.OffsetScrollView;
import com.outspoken_kid.views.OffsetScrollView.OnScrollChangedListener;
import com.outspoken_kid.views.PageNavigatorView;

public class DirectMarketPage extends BCPFragment {

	private OffsetScrollView scrollView;
	private ViewPager viewPager;
	private View certifiedIcon;
	private PageNavigatorView pageNavigator;
	private TextView tvCarInfo1;
	private TextView tvCarInfo2;
	private TextView tvCurrentPrice;
	private TextView tvCurrentPriceText;
	private Button btnCertified;
	private Button btnRequestCertification;
	private LinearLayout normalLinear;
	private Button btnNormal;
	
	private ArrayList<Car> certified = new ArrayList<Car>();
	private ArrayList<Car> normal = new ArrayList<Car>();
	private ArrayList<NormalCarView> normalCarFrames = new ArrayList<NormalCarView>();
	private ImagePagerAdapter imagePagerAdapter;

	private int scrollOffset;
	private int standardLength;
	private float diff;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.directMarketPage_titleBar);
		
		scrollView = (OffsetScrollView) mThisView.findViewById(R.id.directMarketPage_scrollView);
		viewPager = (ViewPager) mThisView.findViewById(R.id.directMarketPage_viewPager);
		certifiedIcon = mThisView.findViewById(R.id.directMarketPage_certifiedIcon);
		pageNavigator = (PageNavigatorView) mThisView.findViewById(R.id.directMarketPage_pageNavigator);
		tvCarInfo1 = (TextView) mThisView.findViewById(R.id.directMarketPage_tvCarInfo1);
		tvCarInfo2 = (TextView) mThisView.findViewById(R.id.directMarketPage_tvCarInfo2);
		tvCurrentPrice = (TextView) mThisView.findViewById(R.id.directMarketPage_tvCurrentPrice);
		tvCurrentPriceText = (TextView) mThisView.findViewById(R.id.directMarketPage_tvCurrentPriceText);
		btnCertified = (Button) mThisView.findViewById(R.id.directMarketPage_btnCertified);
		btnRequestCertification = (Button) mThisView.findViewById(R.id.directMarketPage_btnRequestCertification);
		normalLinear = (LinearLayout) mThisView.findViewById(R.id.directMarketPage_normalLinear);
		btnNormal = (Button) mThisView.findViewById(R.id.directMarketPage_btnNormal);
	}

	@Override
	public void setVariables() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createPage() {

		titleBar.setBgColor(Color.WHITE);
		titleBar.setBgAlpha(0);
		
		viewPager.setAdapter(imagePagerAdapter = new ImagePagerAdapter(mContext));
		
		if(normalLinear.getChildCount() == 0) {
			addNormalCarViews();
		}
	}

	@Override
	public void setListeners() {

		scrollView.setOnScrollChangedListener(new OnScrollChangedListener() {
			
			@Override
			public void onScrollChanged(int offset) {
				
				scrollOffset = offset;
				checkPageScrollOffset();
			}
		});
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {

				setPagerInfo(arg0);
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

		imagePagerAdapter.setOnPagerItemClickedListener(new OnPagerItemClickedListener() {
			
			@Override
			public void onPagerItemClicked(int position) {

				Bundle bundle = new Bundle();
				bundle.putInt("id", certified.get(position).getId());
				bundle.putInt("type", Car.TYPE_DIRECT_CERTIFIED);
				mActivity.showPage(BCPConstants.PAGE_CAR_DETAIL, bundle);
			}
		});
		
		btnCertified.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putInt("type", Car.TYPE_DIRECT_CERTIFIED);
				mActivity.showPage(BCPConstants.PAGE_CAR_LIST, bundle);
			}
		});
		
		btnRequestCertification.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				Bundle bundle = new Bundle();
				bundle.putInt("type", CarRegistrationPage.TYPE_REQUEST_CERTIFICATION);
				bundle.putInt("carType", Car.TYPE_DIRECT_NORMAL);
				mActivity.showPage(BCPConstants.PAGE_CAR_REGISTRATION, bundle);
			}
		});
		
		btnNormal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putInt("type", Car.TYPE_DIRECT_NORMAL);
				mActivity.showPage(BCPConstants.PAGE_CAR_LIST, bundle);
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		
		//viewPager.
		rp = (RelativeLayout.LayoutParams) viewPager.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(460);
		
		//pageNavigator.
		rp = (RelativeLayout.LayoutParams) pageNavigator.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(16);
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);
		
		//certifiedIcon.
		rp = (RelativeLayout.LayoutParams) certifiedIcon.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(95);
		rp.height = ResizeUtils.getSpecificLength(95);
		rp.leftMargin = ResizeUtils.getSpecificLength(12);
		rp.bottomMargin = ResizeUtils.getSpecificLength(18);
		
		//remainBg.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.directMarketPage_remainBg).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(147);
		
		//tvCarInfo1.
		rp = (RelativeLayout.LayoutParams) tvCarInfo1.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(340);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		rp.topMargin = ResizeUtils.getSpecificLength(10);
		
		//line.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.directMarketPage_line).getLayoutParams();
		rp.leftMargin = ResizeUtils.getSpecificLength(10);
		rp.topMargin = ResizeUtils.getSpecificLength(10);
		rp.rightMargin = ResizeUtils.getSpecificLength(10);
		
		//tvCarInfo2.
		rp = (RelativeLayout.LayoutParams) tvCarInfo2.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(340);
		rp.height = ResizeUtils.getSpecificLength(57);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		rp.topMargin = ResizeUtils.getSpecificLength(24);
		
		//tvCurrentPrice.
		rp = (RelativeLayout.LayoutParams) tvCurrentPrice.getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(26);
		rp.rightMargin = ResizeUtils.getSpecificLength(20);
		
		//tvCurrentPriceText.
		rp = (RelativeLayout.LayoutParams) tvCurrentPriceText.getLayoutParams();
		rp.rightMargin = ResizeUtils.getSpecificLength(4);
		rp.bottomMargin = ResizeUtils.getSpecificLength(8);
		
		//btnAuction.
		rp = (RelativeLayout.LayoutParams) btnCertified.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(320);
		rp.height = ResizeUtils.getSpecificLength(68);
		
		//btnRegistration.
		rp = (RelativeLayout.LayoutParams) btnRequestCertification.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(68);
		
		//directTitle.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.directMarketPage_directTitle).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(550);
		rp.topMargin = ResizeUtils.getSpecificLength(20);
		
		//normalLinear.
		rp = (RelativeLayout.LayoutParams) normalLinear.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.topMargin = ResizeUtils.getSpecificLength(88);

		//btnNormal.
		rp = (RelativeLayout.LayoutParams) btnNormal.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(68);
		
		//directTitle.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.directMarketPage_bottomBlank).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(20);
		
		FontUtils.setFontSize(tvCarInfo1, 32);
		FontUtils.setFontStyle(tvCarInfo1, FontUtils.BOLD);
		FontUtils.setFontSize(tvCarInfo2, 20);
		FontUtils.setFontSize(tvCurrentPrice, 32);
		FontUtils.setFontStyle(tvCurrentPrice, FontUtils.BOLD);
		FontUtils.setFontSize(tvCurrentPriceText, 20);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_directmarket;
	}

	@Override
	public int getBackButtonResId() {

		return R.drawable.directmarket_back_btn;
	}

	@Override
	public int getBackButtonWidth() {

		return 242;
	}

	@Override
	public int getBackButtonHeight() {

		return 60;
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
		
		downloadCertifiedList();
		downloadNormalList();
		
		checkPageScrollOffset();
	}
	
	@Override
	public void onDestroyView() {

		scrollView.setOnScrollChangedListener(null);
		viewPager.setOnPageChangeListener(null);
		imagePagerAdapter.setOnPagerItemClickedListener(null);
		
		super.onDestroyView();
	}
	
	@Override
	public int getRootViewResId() {

		return R.id.directMarketPage_mainLayout;
	}
	
//////////////////// Custom methods.

	public void addNormalCarViews() {
		
		normalLinear.removeAllViews();
		
		int size = 3;
		for(int i=0; i<size; i++) {
			
			if(i != 0) {
				View line = new View(mContext);
				ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 1, line, 1, 
						Gravity.CENTER_HORIZONTAL, new int[]{12, 14, 12, 14});
				line.setBackgroundColor(getResources().getColor(R.color.color_ltgray));
				normalLinear.addView(line);
			}
			
			NormalCarView ncv = new NormalCarView(mContext);
			normalCarFrames.add(ncv);
			normalLinear.addView(ncv);
			
			if(i == size -1) {
				View bottomBlank = new View(mContext);
				ResizeUtils.viewResize(10, 14, bottomBlank, 1, 0, null);
				normalLinear.addView(bottomBlank);
			}
		}
	}
	
	public void downloadCertifiedList() {
		
		String url = BCPAPIs.CAR_DIRECT_CERTIFIED_LIST_URL
				+ "?num=6";
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("DirectMarketPage.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("DirectMarketPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					int size = 0;
					ArrayList<String> images = new ArrayList<String>();
					
					certified.clear();
					JSONArray arJSON = objJSON.getJSONArray("onsalecars");
					size = arJSON.length();
					for(int i=0; i<size; i++) {
						Car car = new Car(arJSON.getJSONObject(i));
						certified.add(car);
						images.add(car.getRep_img_url());
					}

					imagePagerAdapter.setArrayList(images);
					viewPager.getAdapter().notifyDataSetChanged();
					viewPager.setCurrentItem(0);
					
					pageNavigator.setSize(certified.size());
					pageNavigator.setEmptyOffCircle();
					pageNavigator.invalidate();

					if(certified.size() > 0) {
						setPagerInfo(0);
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void downloadNormalList() {
		
		String url = BCPAPIs.CAR_DIRECT_NORMAL_LIST_URL
				+ "?num=3";
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("DirectMarketPage.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("DirectMarketPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					int size = 0;
					
					try {
						normal.clear();
						JSONArray arJSON = objJSON.getJSONArray("onsalecars");
						size = arJSON.length();
						for(int i=0; i<size; i++) {
							normal.add(new Car(arJSON.getJSONObject(i)));
						}
						
						for(int i=0; i<normalCarFrames.size(); i++) {
							normalCarFrames.get(i).setCar(normal.get(i));

							final int ID = normal.get(i).getId();
							normalCarFrames.get(i).setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View view) {

									Bundle bundle = new Bundle();
									bundle.putInt("id", ID);
									bundle.putInt("type", Car.TYPE_DIRECT_NORMAL);
									mActivity.showPage(BCPConstants.PAGE_CAR_DETAIL, bundle);
								}
							});
						}
					} catch (Exception e) {
						LogUtils.trace(e);
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void setPagerInfo(int index) {
		
		tvCarInfo1.setText(certified.get(index).getCar_full_name());
		tvCarInfo2.setText(certified.get(index).getYear() + "년 / "
				+ StringUtils.getFormattedNumber(certified.get(index).getMileage()) + "km / "
				+ certified.get(index).getArea());
		
		tvCurrentPrice.setText(StringUtils.getFormattedNumber(certified.get(index).getPrice()) + getString(R.string.won));
		
		if(certified.size() > 1) {
			pageNavigator.setVisibility(View.VISIBLE);
			pageNavigator.setIndex(index);
		} else {
			pageNavigator.setVisibility(View.INVISIBLE);
		}
	}

	public void checkPageScrollOffset() {

		if(standardLength == 0) {
			standardLength = ResizeUtils.getSpecificLength(500);
		}
		
		if(diff == 0) {
			diff = 1f / (float) standardLength; 
		}
		
		try {
			if(scrollOffset < standardLength) {
				titleBar.setBgAlpha(diff * scrollOffset);
			} else {
				titleBar.setBgAlpha(1);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
}
