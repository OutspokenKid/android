package com.byecar.byecarplus.fragments.main_for_user;

import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.byecarplus.classes.BCPAPIs;
import com.byecar.byecarplus.classes.BCPConstants;
import com.byecar.byecarplus.classes.BCPFragment;
import com.byecar.byecarplus.classes.ImagePagerAdapter;
import com.byecar.byecarplus.models.Car;
import com.byecar.byecarplus.views.DealerView;
import com.byecar.byecarplus.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.OffsetScrollView;
import com.outspoken_kid.views.OffsetScrollView.OnScrollChangedListener;
import com.outspoken_kid.views.PageNavigatorView;

public class BidDetailPage extends BCPFragment {

	private int id;
	private Car car;
	
	private OffsetScrollView scrollView;
	
	private ViewPager viewPager;
	private PageNavigatorView pageNavigator;
	private View bidIcon;
	private View remainBg;
	private RelativeLayout timeRelative;
	private ProgressBar progressBar;
	private TextView tvRemainTime;
	private TextView tvRemainTimeText;
	private View timeIcon;
	private TextView tvCarInfo1;
	private TextView tvCarInfo2;
	private TextView tvCurrentPrice;
	private TextView tvCurrentPriceText;
	private TextView tvBidCount;
	
	private View headerForDealer;
	private FrameLayout frameForDealer;
	
	private Button btnConfirm;
	
	private DealerView[] dealerViews;
	private Button[] selectedButtons;
	
	private ImagePagerAdapter imagePagerAdapter;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.bidDetailPage_titleBar);
		
		scrollView = (OffsetScrollView) mThisView.findViewById(R.id.bidDetailPage_scrollView);

		viewPager = (ViewPager) mThisView.findViewById(R.id.bidDetailPage_viewPager);
		pageNavigator = (PageNavigatorView) mThisView.findViewById(R.id.bidDetailPage_pageNavigator);
		bidIcon = mThisView.findViewById(R.id.bidDetailPage_bidIcon);
		remainBg = mThisView.findViewById(R.id.bidDetailPage_remainBg);
		timeRelative = (RelativeLayout) mThisView.findViewById(R.id.bidDetailPage_timeRelative);
		progressBar = (ProgressBar) mThisView.findViewById(R.id.bidDetailPage_progressBar);
		tvRemainTime = (TextView) mThisView.findViewById(R.id.bidDetailPage_tvRemainTime);
		tvRemainTimeText = (TextView) mThisView.findViewById(R.id.bidDetailPage_tvRemainTimeText);
		timeIcon = mThisView.findViewById(R.id.bidDetailPage_timeIcon);
		
		tvCarInfo1 = (TextView) mThisView.findViewById(R.id.bidDetailPage_tvCarInfo1);
		tvCarInfo2 = (TextView) mThisView.findViewById(R.id.bidDetailPage_tvCarInfo2);
		tvCurrentPrice = (TextView) mThisView.findViewById(R.id.bidDetailPage_tvCurrentPrice);
		tvCurrentPriceText = (TextView) mThisView.findViewById(R.id.bidDetailPage_tvCurrentPriceText);
		tvBidCount = (TextView) mThisView.findViewById(R.id.bidDetailPage_tvBidCount);
		
		headerForDealer = mThisView.findViewById(R.id.bidDetailPage_headerForDealer);
		frameForDealer = (FrameLayout) mThisView.findViewById(R.id.bidDetailPage_frameForDealer);
		
		btnConfirm = (Button) mThisView.findViewById(R.id.bidDetailPage_btnConfirm);
	}

	@Override
	public void setVariables() {
		
		if(getArguments() != null) {
			
			if(getArguments().containsKey("car")) {
				this.car = (Car) getArguments().getSerializable("car");
			} else if(getArguments().containsKey("id")) {
				this.id = getArguments().getInt("id");
			} else {
				closePage();
			}
		}
	}

	@Override
	public void createPage() {

		titleBar.setBgColor(Color.WHITE);
		titleBar.setBgAlpha(0);
		
		viewPager.setAdapter(imagePagerAdapter = new ImagePagerAdapter(mContext));
	}

	@Override
	public void setListeners() {

		scrollView.setOnScrollChangedListener(new OnScrollChangedListener() {
			
			@Override
			public void onScrollChanged(int offset) {

				try {
					if(offset < 500) {
						titleBar.setBgAlpha(0.002f * offset);
						
					} else if(offset < 700){
						titleBar.setBgAlpha(1);
					} else {
						//Do nothing.
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}
			}
		});
	
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {

				if(car.getImages().size() > 1) {
					pageNavigator.setVisibility(View.VISIBLE);
					pageNavigator.setIndex(arg0);
				} else {
					pageNavigator.setVisibility(View.INVISIBLE);
				}
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
		
		//viewPager.
		rp = (RelativeLayout.LayoutParams) viewPager.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(460);
		
		//pageNavigator.
		rp = (RelativeLayout.LayoutParams) pageNavigator.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(16);
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);
		
		//bidIcon.
		rp = (RelativeLayout.LayoutParams) bidIcon.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(96);
		rp.height = ResizeUtils.getSpecificLength(96);
		rp.leftMargin = ResizeUtils.getSpecificLength(12);
		rp.bottomMargin = ResizeUtils.getSpecificLength(18);
		
		rp = (RelativeLayout.LayoutParams) remainBg.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(147);
		
		//progressBar.
		rp = (RelativeLayout.LayoutParams) progressBar.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(30);
		
		//tvRemainTime.
		rp = (RelativeLayout.LayoutParams) tvRemainTime.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(30);
		rp.topMargin = -ResizeUtils.getSpecificLength(3);
		tvRemainTime.setPadding(ResizeUtils.getSpecificLength(90), 0, 0, 0);
		
		//tvRemainTimeText.
		rp = (RelativeLayout.LayoutParams) tvRemainTimeText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(30);
		tvRemainTimeText.setPadding(ResizeUtils.getSpecificLength(22), 0, 0, 0);
		
		//timeIcon.
		rp = (RelativeLayout.LayoutParams) timeIcon.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(18);
		rp.height = ResizeUtils.getSpecificLength(18);
		rp.topMargin = ResizeUtils.getSpecificLength(6);
		rp.rightMargin = ResizeUtils.getSpecificLength(5);
		
		//tvCarInfo1.
		rp = (RelativeLayout.LayoutParams) tvCarInfo1.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(340);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		
		//tvCarInfo2.
		rp = (RelativeLayout.LayoutParams) tvCarInfo2.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(340);
		rp.height = ResizeUtils.getSpecificLength(57);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		
		//tvCurrentPrice.
		rp = (RelativeLayout.LayoutParams) tvCurrentPrice.getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(16);
		rp.rightMargin = ResizeUtils.getSpecificLength(20);
		
		//tvCurrentPriceText.
		rp = (RelativeLayout.LayoutParams) tvCurrentPriceText.getLayoutParams();
		rp.rightMargin = ResizeUtils.getSpecificLength(10);
		rp.bottomMargin = ResizeUtils.getSpecificLength(10);
		
		//tvBidCount.
		rp = (RelativeLayout.LayoutParams) tvBidCount.getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(4);
		
		//headerForDealer.
		rp = (RelativeLayout.LayoutParams) headerForDealer.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(632);
		rp.height = ResizeUtils.getSpecificLength(90);
		rp.topMargin = ResizeUtils.getSpecificLength(5);
		
		//frameForDealer.
		rp = (RelativeLayout.LayoutParams) frameForDealer.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(632);
		
		//footerForDealer.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.bidDetailPage_footerForDealer).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(632);
		rp.height = ResizeUtils.getSpecificLength(20);
		
		//btnConfirm.
		rp = (RelativeLayout.LayoutParams) btnConfirm.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(82);
		
		FontUtils.setFontSize(tvRemainTime, 24);
		FontUtils.setFontStyle(tvRemainTime, FontUtils.BOLD);
		FontUtils.setFontSize(tvRemainTimeText, 16);
		FontUtils.setFontSize(tvCarInfo1, 32);
		FontUtils.setFontStyle(tvCarInfo1, FontUtils.BOLD);
		FontUtils.setFontSize(tvCarInfo2, 20);
		FontUtils.setFontSize(tvCurrentPrice, 32);
		FontUtils.setFontStyle(tvCurrentPrice, FontUtils.BOLD);
		FontUtils.setFontSize(tvCurrentPriceText, 20);
		FontUtils.setFontSize(tvBidCount, 20);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_bid_detail;
	}

	@Override
	public int getBackButtonResId() {

		return R.drawable.success_back_btn;
	}

	@Override
	public int getBackButtonWidth() {

		return 212;
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
		
		if(car != null) {
			setMainCarInfo();
		} else {
			downloadCarInfo();
		}
	}
	
//////////////////// Custom methods.

	public void downloadCarInfo() {

		String url = BCPAPIs.BID_SHOW_URL + "?onsalecar_id=" + id;
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("AuctionDetailPage.onError." + "\nurl : " + url);
				
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						downloadCarInfo();
					}
				}, 1000);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("AuctionDetailPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					car = new Car(objJSON.getJSONObject("onsalecar"));
					setMainCarInfo();
				} catch (Exception e) {
					LogUtils.trace(e);
					closePage();
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					closePage();
				}
			}
		});
	}
	
	public void setMainCarInfo() {
		
		tvRemainTime.setText("15 : 40 : 21");
		
		tvCarInfo1.setText(car.getCar_full_name());
		tvCarInfo2.setText(car.getYear() + "년 / "
				+ StringUtils.getFormattedNumber(car.getMileage()) + "km / "
				+ car.getArea());
		
		tvCurrentPrice.setText(StringUtils.getFormattedNumber(car.getPrice()) 
				+ getString(R.string.won));
		tvBidCount.setText("입찰중 " + car.getBids_cnt() + "명");

		imagePagerAdapter.setArrayList(car.getImages());
		viewPager.getAdapter().notifyDataSetChanged();
		viewPager.setCurrentItem(0);
		
		pageNavigator.setSize(car.getImages().size());
		pageNavigator.setEmptyOffCircle();
		pageNavigator.invalidate();
	
		addDealerViews();
	}

	public void closePage() {
		
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				ToastUtils.showToast(R.string.failToLoadBidsInfo);
				mActivity.closeTopPage();
			}
		}, 1000);
	}
	
	public void addDealerViews() {
		
		//이전 상태 모두 지우기.
		frameForDealer.removeAllViews();

		final int size = car.getBids().size();
		dealerViews = new DealerView[size];
		selectedButtons = new Button[size];
		for(int i=0; i<size; i++) {
			dealerViews[i] = new DealerView(mContext);
			ResizeUtils.viewResize(184, 300, dealerViews[i], 2, Gravity.LEFT, 
					new int[]{
						20 + (i*204),
						14, 0, 0});
			dealerViews[i].setDealerInfo(car.getBids().get(i));
			frameForDealer.addView(dealerViews[i]);
			
			selectedButtons[i] = new Button(mContext);
			ResizeUtils.viewResize(184, 52, selectedButtons[i], 2, Gravity.LEFT, 
					new int[]{
						20 + (i*204),
						312, 0, 0});
			frameForDealer.addView(selectedButtons[i]);
			
			final int I = i;
			
			dealerViews[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					Bundle bundle = new Bundle();
					bundle.putBoolean("isCertifier", false);
					bundle.putInt("id", car.getBids().get(I).getDealer_id());
					mActivity.showPage(BCPConstants.PAGE_COMMON_DEALER_CERTIFIER, bundle);
				}
			});
			selectedButtons[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					for(int i=0; i<size; i++) {
						
						if(dealerViews[i].getSelected()) {
							dealerViews[i].setSelected(false);
							selectedButtons[i].setBackgroundResource(R.drawable.success_select_btn_a);
						}
					}
					
					dealerViews[I].setSelected(true);
					selectedButtons[I].setBackgroundResource(R.drawable.success_select_btn_b);
				}
			});
		}
	}
}
