package com.byecar.fragments.user;

import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.MainActivity;
import com.byecar.byecarplus.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPFragment;
import com.byecar.classes.ImagePagerAdapter;
import com.byecar.classes.ImagePagerAdapter.OnPagerItemClickedListener;
import com.byecar.models.Car;
import com.byecar.views.DealerView;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.TimerUtils;
import com.outspoken_kid.utils.TimerUtils.OnTimeChangedListener;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.OffsetScrollView;
import com.outspoken_kid.views.OffsetScrollView.OnScrollChangedListener;
import com.outspoken_kid.views.PageNavigatorView;

public class SelectBidPage extends BCPFragment {

	private int id;
	private Car car;
	
	private OffsetScrollView scrollView;
	
	private ViewPager viewPager;
	private PageNavigatorView pageNavigator;
	private View auctionIcon;
	private View remainBg;
	private ProgressBar progressBar;
	private TextView tvRemainTime;
	private TextView tvRemainTimeText;
	private View timeIcon;
	private TextView tvCarInfo1;
	private TextView tvCarInfo2;
	private TextView tvCurrentPrice;
	private TextView tvCurrentPriceText;
	private TextView tvBidCount;
	private Button btnLike;
	
	private RelativeLayout relativeForDealer;
	private Button btnComplete;
	
	private ImagePagerAdapter imagePagerAdapter;
	
	private int scrollOffset;
	private int standardLength;
	private float diff;
	
	private OnTimeChangedListener onTimeChangedListener;
	boolean[] selecteds = new boolean[3];
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.selectBidPage_titleBar);
		
		scrollView = (OffsetScrollView) mThisView.findViewById(R.id.selectBidPage_scrollView);

		viewPager = (ViewPager) mThisView.findViewById(R.id.selectBidPage_viewPager);
		pageNavigator = (PageNavigatorView) mThisView.findViewById(R.id.selectBidPage_pageNavigator);
		auctionIcon = mThisView.findViewById(R.id.selectBidPage_auctionIcon);
		remainBg = mThisView.findViewById(R.id.selectBidPage_remainBg);
		progressBar = (ProgressBar) mThisView.findViewById(R.id.selectBidPage_progressBar);
		tvRemainTime = (TextView) mThisView.findViewById(R.id.selectBidPage_tvRemainTime);
		tvRemainTimeText = (TextView) mThisView.findViewById(R.id.selectBidPage_tvRemainTimeText);
		timeIcon = mThisView.findViewById(R.id.selectBidPage_timeIcon);
		
		tvCarInfo1 = (TextView) mThisView.findViewById(R.id.selectBidPage_tvCarInfo1);
		tvCarInfo2 = (TextView) mThisView.findViewById(R.id.selectBidPage_tvCarInfo2);
		tvCurrentPrice = (TextView) mThisView.findViewById(R.id.selectBidPage_tvCurrentPrice);
		tvCurrentPriceText = (TextView) mThisView.findViewById(R.id.selectBidPage_tvCurrentPriceText);
		tvBidCount = (TextView) mThisView.findViewById(R.id.selectBidPage_tvBidCount);
		btnLike = (Button) mThisView.findViewById(R.id.selectBidPage_btnLike);
		
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
				closePage(R.string.failToLoadCarInfo);
			}
		}
		
		setOnTimerListener();
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

				scrollOffset = offset;
				checkPageScrollOffset();
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

		imagePagerAdapter.setOnPagerItemClickedListener(new OnPagerItemClickedListener() {
			
			@Override
			public void onPagerItemClicked(int position) {

				if(car != null 
						&& car.getImages() != null
						&& car.getImages().size() != 0) {
					int size = car.getImages().size();
					String[] imageUrls = new String[size];
					
					for(int i=0; i<size; i++) {
						imageUrls[i] = car.getImages().get(i);
					}
					
					mActivity.showImageViewer(position, null, imageUrls, null);
				}
			}
		});
		
		btnLike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setLike(car);
			}
		});
		
		btnComplete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				int dealer_id = 0;
				
				for(int i=0; i<3; i++) {
					
					if(selecteds[i]) {
						dealer_id = car.getBids().get(i).getDealer_id();
						break;
					}
				}
				
				if(dealer_id == 0) {
					ToastUtils.showToast(R.string.selectDealer);
				} else {
					selectDealer(dealer_id);
				}
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
		
		//auctionIcon.
		rp = (RelativeLayout.LayoutParams) auctionIcon.getLayoutParams();
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
		rp.leftMargin = -ResizeUtils.getSpecificLength(10);
		rp.topMargin = ResizeUtils.getSpecificLength(7);
		
		//remainBg.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.selectBidPage_remainBg).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(147);
		
		//tvCarInfo1.
		rp = (RelativeLayout.LayoutParams) tvCarInfo1.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(340);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		rp.topMargin = ResizeUtils.getSpecificLength(10);
		
		//line.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.selectBidPage_line).getLayoutParams();
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
		
		//tvBidCount.
		rp = (RelativeLayout.LayoutParams) tvBidCount.getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(24);
		
		//btnLike.
		rp = (RelativeLayout.LayoutParams) btnLike.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(90);
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.rightMargin = ResizeUtils.getSpecificLength(14);
		rp.bottomMargin = -ResizeUtils.getSpecificLength(8);
		btnLike.setPadding(ResizeUtils.getSpecificLength(32), 0, 
				ResizeUtils.getSpecificLength(10), ResizeUtils.getSpecificLength(2));
		
		//tvLikeText.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.selectBidPage_tvLikeText).getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(5);
		rp.rightMargin = ResizeUtils.getSpecificLength(2);
		
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
		
		FontUtils.setFontSize(btnLike, 18);
		FontUtils.setFontSize((TextView)mThisView.findViewById(R.id.selectBidPage_tvLikeText), 20);
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
		
		TimerUtils.addOnTimeChangedListener(onTimeChangedListener);
		
		downloadCarInfo();
		checkPageScrollOffset();
	}

	@Override
	public void onPause() {
		super.onPause();
		
		TimerUtils.removeOnTimeChangedListener(onTimeChangedListener);
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		
		scrollView.setOnScrollChangedListener(null);
	}
	
//////////////////// Custom methods.

	public void closePage(int message) {
		
		ToastUtils.showToast(message);
		
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				if(mActivity != null) {
					mActivity.closeTopPage();
				}
			}
		}, 1000);
	}

	public void downloadCarInfo() {

		String url = BCPAPIs.CAR_BID_SHOW_URL;
		
		if(car == null) {
			url += "?onsalecar_id=" + id;
		} else if(car != null) {
			url += "?onsalecar_id=" + car.getId();
		}
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("SelectBidPage.onError." + "\nurl : " + url);
				closePage(R.string.failToLoadCarInfo);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("SelectBidPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					Car newCar = new Car(objJSON.getJSONObject("onsalecar"));
					
					if(car == null) {
						car = newCar;
					} else {
						car.copyValuesFromNewItem(newCar);
					}
					
					setMainCarInfo();
				} catch (Exception e) {
					LogUtils.trace(e);
					closePage(R.string.failToLoadCarInfo);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					closePage(R.string.failToLoadCarInfo);
				}
			}
		});
	}
	
	public void setMainCarInfo() {
		
		if(car.getStatus() == -1) {
			closePage(R.string.holdOffByAdmin);
		}
		
		tvCarInfo1.setText(car.getCar_full_name());
		tvCarInfo2.setText(car.getYear() + "년 / "
				+ StringUtils.getFormattedNumber(car.getMileage()) + "km / "
				+ car.getArea());

		tvCurrentPriceText.setText(R.string.currentPrice);
		
		tvCurrentPrice.setText(StringUtils.getFormattedNumber(car.getPrice()) 
				+ getString(R.string.won));
		tvBidCount.setText("입찰자 " + car.getBids_cnt() + "명");

		if(car.getIs_liked() == 0) {
			btnLike.setBackgroundResource(R.drawable.main_like_btn_a);
		} else {
			btnLike.setBackgroundResource(R.drawable.main_like_btn_b);
		}

		int likesCount = car.getLikes_cnt();
		
		if(likesCount > 9999) {
			likesCount = 9999;
		}
		
		btnLike.setText("" + likesCount);
		
		imagePagerAdapter.setArrayList(car.getImages());
		viewPager.getAdapter().notifyDataSetChanged();
		viewPager.setCurrentItem(0);
		
		pageNavigator.setSize(car.getImages().size());
		pageNavigator.setEmptyOffCircle();
		pageNavigator.invalidate();

		addViewsForDealer();
	}
	
	public void addViewsForDealer() {
		
		//이전 상태 모두 지우기.
		relativeForDealer.removeAllViews();

		DealerView[] dealerViews = new DealerView[3];
		final Button[] selectButtons = new Button[3];
		
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
						bundle.putInt("dealer_id", car.getBids().get(I).getDealer_id());
						mActivity.showPage(BCPConstants.PAGE_DEALER_CERTIFIER, bundle);
					}
				});
				
				final int SELECTED_INDEX = i;
				selectButtons[i].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {

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
	
	public void setOnTimerListener() {
		
		if(onTimeChangedListener == null) {
			onTimeChangedListener = new OnTimeChangedListener() {
				
				@Override
				public void onTimeChanged() {

					if(car == null) {
						return;
					}
					
					if(car.getStatus() > Car.STATUS_BID_COMPLETE) {
						progressBar.setProgress(10000);
						tvRemainTime.setText("-- : -- : --");
						return;
					}
					
					/*
					 * ms 단위.
					 * progress : 0 ~ 1000
					 * progressTime = (car.getStatus() < Car.STATUS_BID_COMPLETE ? (bid_until_at - bid_begin_at) * 1000 : h000);
					 * (endTime = (bid_until_at * 1000) + (car.getStatus() < Car.STATUS_BID_COMPLETE ? 0 : 86400000))
					 * remainTime = endTime - currentTime
					 * 		=  (bid_until_at * 1000) + (car.getStatus() < Car.STATUS_BID_COMPLETE ? 0 : 86400000)) - currentTime
					 * (passedTime = progressTime - remainTime)
					 * progressValue = (passedTime / progressTime) * 1000
					 * 		= (progressTime - remainTime) / progressTime * 1000
					 * 		= 1000 - (remainTime * 1000 / progressTime)
					 */
		        	
		        	try {
						long progressTime = (car.getStatus() < Car.STATUS_BID_COMPLETE ? 
								(car.getBid_until_at() - car.getBid_begin_at()) * 1000 : 86400000);
						long remainTime = car.getBid_until_at() * 1000 
								+ (car.getStatus() < Car.STATUS_BID_COMPLETE ? 0 : 86400000) 
								- System.currentTimeMillis();
//			        	long progressValue = 1000 - (remainTime * 1000 / progressTime);
						long progressValue = remainTime * 1000 / progressTime;
			        	
			        	String formattedRemainTime = StringUtils.getTimeString(remainTime);
			        	tvRemainTime.setText(formattedRemainTime);
			        	progressBar.setProgress((int)progressValue);
					} catch (Exception e) {
						LogUtils.trace(e);
						TimerUtils.removeOnTimeChangedListener(onTimeChangedListener);
						tvRemainTime.setText("-- : -- : --");
						progressBar.setProgress(10000);
					}
				}
				
				@Override
				public String getName() {
					
					return "SelectBidPage";
				}
				
				@Override
				public Activity getActivity() {

					return mActivity;
				}
			};
		}
	}
	
	public void setLike(Car car) {
		
		String url = null;

		if(car.getIs_liked() == 0) {
			btnLike.setBackgroundResource(R.drawable.main_like_btn_b);
			car.setLikes_cnt(car.getLikes_cnt() + 1);
			car.setIs_liked(1);

			url = BCPAPIs.CAR_BID_LIKE_URL;
			
		} else {
			btnLike.setBackgroundResource(R.drawable.main_like_btn_a);
			car.setLikes_cnt(car.getLikes_cnt() - 1);
			car.setIs_liked(0);
			
			url = BCPAPIs.CAR_BID_UNLIKE_URL;
		}
		
		btnLike.setText("" + car.getLikes_cnt());
		
		url += "?onsalecar_id=" + car.getId();
		
		DownloadUtils.downloadJSONString(url,
				new OnJSONDownloadListener() {

					@Override
					public void onError(String url) {

						LogUtils.log("SelectBidPage.onError." + "\nurl : "
								+ url);
					}

					@Override
					public void onCompleted(String url,
							JSONObject objJSON) {

						try {
							LogUtils.log("SelectBidPage.onCompleted."
									+ "\nurl : " + url
									+ "\nresult : " + objJSON);
						} catch (Exception e) {
							LogUtils.trace(e);
						} catch (OutOfMemoryError oom) {
							LogUtils.trace(oom);
						}
					}
				});
	}

	public void selectDealer(int dealer_id) {
		
		//http://byecar.minsangk.com/onsalecars/bids/select.json?onsalecar_id=1&dealer_id=1
		String url = BCPAPIs.CAR_BID_SELECT_URL
				+ "?onsalecar_id=" + car.getId()
				+ "&dealer_id=" + dealer_id;
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("SelectBid.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToSelectDealer);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("SelectBid.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_selectDealer);
						mActivity.closeTopPage();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToSelectDealer);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					ToastUtils.showToast(R.string.failToSelectDealer);
				}
			}
		});
	}
	
	public void bidStatusChanged(String event, Car car) {

		if(event == null) {
			return;
		}
		
		if(this.car.getId() == car.getId()) {
			
			this.car.copyValuesFromNewItem(car);
			refreshPage();

			//경매가 시작되는 물건이 있는 경우.
			//딜러 선택 시간이 종료된 경우 (유찰). 
			if(event.equals("auction_begun")
					|| event.equals("dealer_selected")) {
				mActivity.closeTopPage();
				((MainActivity)mActivity).showCarDetailPage(0, car, car.getType());
			
			//관리자에 의해 보류된 경우.
			} else if(event.equals("auction_held")) {
				closePage(R.string.holdOffByAdmin2);

			//경매 매물의 가격 변화가 있는 경우.
			//유저가 딜러를 선택한 경우 (낙찰).
			} else {
				//Do nothing.				
			}
		}
	}
}
