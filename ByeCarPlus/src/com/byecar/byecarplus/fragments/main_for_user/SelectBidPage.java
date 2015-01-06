package com.byecar.byecarplus.fragments.main_for_user;

import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.byecarplus.classes.BCPAPIs;
import com.byecar.byecarplus.classes.BCPConstants;
import com.byecar.byecarplus.classes.BCPFragment;
import com.byecar.byecarplus.models.Car;
import com.byecar.byecarplus.views.DealerView;
import com.byecar.byecarplus.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.views.OffsetScrollView;
import com.outspoken_kid.views.OffsetScrollView.OnScrollChangedListener;

public class SelectBidPage extends BCPFragment {

	private int id;
	private Car car;
	
	private OffsetScrollView scrollView;
	
	private ImageView ivImage;
	private View bidIcon;
	private View remainBg;
	private ProgressBar progressBar;
	private TextView tvRemainTime;
	private TextView tvRemainTimeText;
	private View timeIcon;
	private TextView tvCarInfo1;
	private TextView tvCarInfo2;
	private TextView tvCurrentPrice;
	private TextView tvCurrentPriceText;
	private TextView tvBid;
	private RelativeLayout relativeForDealer;
	private Button btnComplete;
	
	private int scrollOffset;
	private int standardLength;
	private float diff;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.selectBidPage_titleBar);
		
		scrollView = (OffsetScrollView) mThisView.findViewById(R.id.selectBidPage_scrollView);
		ivImage = (ImageView) mThisView.findViewById(R.id.selectBidPage_ivImage);
		
		bidIcon = mThisView.findViewById(R.id.selectBidPage_bidIcon);
		remainBg = mThisView.findViewById(R.id.selectBidPage_remainBg);
		progressBar = (ProgressBar) mThisView.findViewById(R.id.selectBidPage_progressBar);
		tvRemainTime = (TextView) mThisView.findViewById(R.id.selectBidPage_tvRemainTime);
		tvRemainTimeText = (TextView) mThisView.findViewById(R.id.selectBidPage_tvRemainTimeText);
		timeIcon = mThisView.findViewById(R.id.selectBidPage_timeIcon);
		
		tvCarInfo1 = (TextView) mThisView.findViewById(R.id.selectBidPage_tvCarInfo1);
		tvCarInfo2 = (TextView) mThisView.findViewById(R.id.selectBidPage_tvCarInfo2);
		tvCurrentPrice = (TextView) mThisView.findViewById(R.id.selectBidPage_tvCurrentPrice);
		tvCurrentPriceText = (TextView) mThisView.findViewById(R.id.selectBidPage_tvCurrentPriceText);
		tvBid = (TextView) mThisView.findViewById(R.id.selectBidPage_tvBid);
		
		relativeForDealer = (RelativeLayout) mThisView.findViewById(R.id.selectBidPage_relativeForDealer);
		btnComplete = (Button) mThisView.findViewById(R.id.selectBidPage_btnComplete);
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
		
		btnComplete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				ToastUtils.showToast("구매");
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		
		//ivImage.
		rp = (RelativeLayout.LayoutParams) ivImage.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(460);
		
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
		
		//tvBid.
		rp = (RelativeLayout.LayoutParams) tvBid.getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(4);
		
		//relativeForDealer.
		rp = (RelativeLayout.LayoutParams) relativeForDealer.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(477);
		rp.topMargin = ResizeUtils.getSpecificLength(20);
		
		//btnComplete.
		rp = (RelativeLayout.LayoutParams) btnComplete.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(82);
		rp.topMargin = ResizeUtils.getSpecificLength(20);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_select_bid;
	}

	@Override
	public int getBackButtonResId() {

		return R.drawable.success_back_btn;
	}

	@Override
	public int getBackButtonWidth() {

		return 216;
	}

	@Override
	public int getBackButtonHeight() {

		return 60;
	}

	@Override
	public int getRootViewResId() {

		return R.id.selectBidPage_mainLayout;
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
		
		checkPageScrollOffset();
	}
	
	@Override
	public void onDestroyView() {

		scrollView.setOnScrollChangedListener(null);
		
		super.onDestroyView();
	}
	
//////////////////// Custom methods.

	public void addViewsForDealer() {
		
		//이전 상태 모두 지우기.
		relativeForDealer.removeAllViews();

		DealerView[] dealerViews = new DealerView[3];
		final Button[] selectButtons = new Button[3];
		final boolean[] selecteds = new boolean[3]; 
		
		for(int i=0; i<3; i++) {
			dealerViews[i] = new DealerView(mContext, i);
			ResizeUtils.viewResizeForRelative(178, 290, dealerViews[i], 
					new int[]{RelativeLayout.ALIGN_PARENT_LEFT}, new int[]{0}, 
					new int[]{18 + (i*196), 97, 0, 14});
			relativeForDealer.addView(dealerViews[i]);
			
			selectButtons[i] = new Button(mContext);
			ResizeUtils.viewResizeForRelative(178, 50, selectButtons[i], 
					new int[]{RelativeLayout.ALIGN_PARENT_LEFT}, new int[]{0}, 
					new int[]{18 + (i*196), 406, 0, 14});
			selectButtons[i].setBackgroundResource(R.drawable.success_select_btn_a);
			relativeForDealer.addView(selectButtons[i]);
		}
		
		int size = car.getBids().size();
		
		if(size == 0) {
			
			for(int i=0; i<3; i++) {
				dealerViews[i].setVisibility(View.INVISIBLE);
				selectButtons[i].setVisibility(View.INVISIBLE);
			}
			
			View noOne = new View(mContext);
			ResizeUtils.viewResizeForRelative(218, 248, noOne, 
					new int[]{RelativeLayout.CENTER_IN_PARENT}, new int[]{0}, 
					null);
			noOne.setBackgroundResource(R.drawable.detail_no_one);
			relativeForDealer.addView(noOne);
		} else {
			for(int i=0; i<size; i++) {
				dealerViews[i].setDealerInfo(car.getBids().get(i));
				
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
				
				final int SELECTED_INDEX = i;
				selectButtons[i].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {

						ToastUtils.showToast("select " + SELECTED_INDEX);
						
						if(SELECTED_INDEX > car.getBids().size()) {
							return;
						}
						
						if(selecteds[SELECTED_INDEX]) {
							selectButtons[SELECTED_INDEX].setBackgroundResource(R.drawable.success_select_btn_a);
							selecteds[SELECTED_INDEX] = false;
							
						} else {
						
							for(int i=0; i<3; i++) {
								selectButtons[i].setBackgroundResource(R.drawable.success_select_btn_a);
								selecteds[i] = false;
							}
							
							selectButtons[SELECTED_INDEX].setBackgroundResource(R.drawable.success_select_btn_b);
							selecteds[SELECTED_INDEX] = true;
						}
					}
				});
			}
		}
	}
	
	public void closePage() {
		
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				ToastUtils.showToast(R.string.failToLoadCarInfo);
				mActivity.closeTopPage();
			}
		}, 1000);
	}
	
	public void setMainCarInfo() {
		
		tvRemainTime.setText("15 : 40 : 21");
		
		tvCarInfo1.setText(car.getCar_full_name());
		tvCarInfo2.setText(car.getYear() + "년 / "
				+ StringUtils.getFormattedNumber(car.getMileage()) + "km / "
				+ car.getArea());
		
		tvCurrentPrice.setText(StringUtils.getFormattedNumber(car.getPrice()) 
				+ getString(R.string.won));
		tvBid.setText("입찰중 " + car.getBids_cnt() + "명");

		addViewsForDealer();
	}

	public void downloadCarInfo() {

		String url = BCPAPIs.BID_SHOW_URL + "?onsalecar_id=" + id;
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("SelectBidPage.onError." + "\nurl : " + url);
				
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
					LogUtils.log("SelectBidPage.onCompleted." + "\nurl : " + url
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
