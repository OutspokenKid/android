package com.byecar.fragments.user;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.MainActivity;
import com.byecar.byecarplus.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPAuctionableFragment;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.ImagePagerAdapter;
import com.byecar.classes.ImagePagerAdapter.OnPagerItemClickedListener;
import com.byecar.models.Bid;
import com.byecar.models.Car;
import com.byecar.models.CompanyInfo;
import com.byecar.models.Post;
import com.byecar.views.BiddingCarView;
import com.byecar.views.CarInfoView;
import com.byecar.views.DealerView;
import com.byecar.views.TitleBar;
import com.byecar.views.UsedCarView;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.TimerUtils;
import com.outspoken_kid.utils.TimerUtils.OnTimeChangedListener;
import com.outspoken_kid.views.OffsetScrollView;
import com.outspoken_kid.views.OffsetScrollView.OnScrollChangedListener;
import com.outspoken_kid.views.PageNavigatorView;

public class MainPage extends BCPAuctionableFragment {
	
	private static final int PAGER_TIME = 5;
	
	private OffsetScrollView scrollView;
	private ViewPager viewPager;
	private View auctionIcon;
	private PageNavigatorView pageNavigator;
	private CarInfoView carInfoView;
	private RelativeLayout relativeForTopDealers;
	private View noDealerView;
	private DealerView[] dealerViews = new DealerView[3];
	
	private RelativeLayout relativeForBidding;
	private TextView tvBiddingCount;
	private Button btnBidding;
	private BiddingCarView[] biddingCarViews = new BiddingCarView[3];
	
	private View usedMarketBg;
	private UsedCarView[] usedCarViews = new UsedCarView[3];
	private Button btnUsedMarket;
	private ImageView ivDirectMarket;
	private Button btnDirectMarket;
	
	private RelativeLayout relativeForVideo;
	private ImageView ivVideo;
	private Button btnVideo;
	private Button btnPlay;
	private TextView tvVideoTitle;
	
	private Button[] tabButtons = new Button[4];
	
//	private ArrayList<Dealer> topDealers = new ArrayList<Dealer>();
	private ArrayList<Car> bids1 = new ArrayList<Car>();
	private ArrayList<Car> bids2 = new ArrayList<Car>();
	private ArrayList<Car> dealers = new ArrayList<Car>();
	private ImagePagerAdapter imagePagerAdapter;
	private Post video;
	
	private int scrollOffset; 
	private int standardLength;
	private float diff;
	private int pagerTime;
	
	private Timer pagerTimer;
	private TimerTask pagerTimerTask;
	
	private OnTimeChangedListener onTimeChangedListener;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.mainForUserPage_titleBar);
		
		scrollView = (OffsetScrollView) mThisView.findViewById(R.id.mainForUserPage_scrollView);
		viewPager = (ViewPager) mThisView.findViewById(R.id.mainForUserPage_viewPager);
		auctionIcon = mThisView.findViewById(R.id.mainForUserPage_auctionIcon);
		pageNavigator = (PageNavigatorView) mThisView.findViewById(R.id.mainForUserPage_pageNavigator);
		carInfoView = (CarInfoView) mThisView.findViewById(R.id.mainForUserPage_carInfoView);
		
		relativeForTopDealers = (RelativeLayout) mThisView.findViewById(R.id.mainForUserPage_relativeForTopDealers);
		noDealerView = mThisView.findViewById(R.id.mainForUserPage_noDealerView);
		dealerViews[0] = (DealerView) mThisView.findViewById(R.id.mainForUserPage_dealerView1);
		dealerViews[1] = (DealerView) mThisView.findViewById(R.id.mainForUserPage_dealerView2);
		dealerViews[2] = (DealerView) mThisView.findViewById(R.id.mainForUserPage_dealerView3);
		
		relativeForBidding = (RelativeLayout) mThisView.findViewById(R.id.mainForUserPage_relativeForBidding);
		tvBiddingCount = (TextView) mThisView.findViewById(R.id.mainForUserPage_tvBiddingCount);
		btnBidding = (Button) mThisView.findViewById(R.id.mainForUserPage_btnBidding);
		biddingCarViews[0] = (BiddingCarView) mThisView.findViewById(R.id.mainForUserPage_biddingCarView1);
		biddingCarViews[1] = (BiddingCarView) mThisView.findViewById(R.id.mainForUserPage_biddingCarView2);
		biddingCarViews[2] = (BiddingCarView) mThisView.findViewById(R.id.mainForUserPage_biddingCarView3);
		
		usedMarketBg = mThisView.findViewById(R.id.mainForUserPage_usedMarketBg);
		btnUsedMarket = (Button) mThisView.findViewById(R.id.mainForUserPage_btnUsedMarket);
		usedCarViews[0] = (UsedCarView) mThisView.findViewById(R.id.mainForUserPage_usedCarView1);
		usedCarViews[1] = (UsedCarView) mThisView.findViewById(R.id.mainForUserPage_usedCarView2);
		usedCarViews[2] = (UsedCarView) mThisView.findViewById(R.id.mainForUserPage_usedCarView3);
		
		ivDirectMarket = (ImageView) mThisView.findViewById(R.id.mainForUserPage_ivDirectMarket);
		btnDirectMarket = (Button) mThisView.findViewById(R.id.mainForUserPage_btnDirectMarket);
		
		relativeForVideo = (RelativeLayout) mThisView.findViewById(R.id.mainForUserPage_relativeForVideo);
		ivVideo = (ImageView) mThisView.findViewById(R.id.mainForUserPage_ivVideo);
		btnVideo = (Button) mThisView.findViewById(R.id.mainForUserPage_btnVideo);
		btnPlay = (Button) mThisView.findViewById(R.id.mainForUserPage_btnPlay);
		tvVideoTitle = (TextView) mThisView.findViewById(R.id.mainForUserPage_tvVideoTitle);
		
		tabButtons[0] = (Button) mThisView.findViewById(R.id.mainForUserPage_btnTab1);
		tabButtons[1] = (Button) mThisView.findViewById(R.id.mainForUserPage_btnTab2);
		tabButtons[2] = (Button) mThisView.findViewById(R.id.mainForUserPage_btnTab3);
		tabButtons[3] = (Button) mThisView.findViewById(R.id.mainForUserPage_btnTab4);
	}

	@Override
	public void setVariables() {

		setOnTimerListener();
	}

	@Override
	public void createPage() {

		titleBar.setBgAlpha(0);
		
		viewPager.setAdapter(imagePagerAdapter = new ImagePagerAdapter(mContext));
		carInfoView.setType(CarInfoView.TYPE_MAIN_AUCTION);
		
		int size = dealerViews.length;
		for(int i=0; i<size; i++) {
			dealerViews[i].setIndex(i);
		}
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

		titleBar.getBtnNotice().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(BCPConstants.PAGE_NOTIFICATION, null);
			}
		});
		
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

				pagerTime = 0;
				setPagerInfo(arg0);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

				pagerTime = 0;
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		imagePagerAdapter.setOnPagerItemClickedListener(new OnPagerItemClickedListener() {
			
			@Override
			public void onPagerItemClicked(int position) {

				((MainActivity)mActivity).showCarDetailPage(bids1.get(position).getId(), null, Car.TYPE_BID);
			}
		});
		
//		btnAuction.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View view) {
//
//				Bundle bundle = new Bundle();
//				bundle.putInt("type", Car.TYPE_BID);
//				mActivity.showPage(BCPConstants.PAGE_CAR_LIST, bundle);
//			}
//		});
	
		btnUsedMarket.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putInt("type", Car.TYPE_DEALER);
				mActivity.showPage(BCPConstants.PAGE_CAR_LIST, bundle);
			}
		});
	
		btnDirectMarket.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(BCPConstants.PAGE_DIRECT_MARKET, null);				
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
		rp.width = ResizeUtils.getSpecificLength(95);
		rp.height = ResizeUtils.getSpecificLength(95);
		rp.leftMargin = ResizeUtils.getSpecificLength(12);
		rp.bottomMargin = ResizeUtils.getSpecificLength(18);

		//relativeForTopDealers
		rp = (RelativeLayout.LayoutParams) relativeForTopDealers.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(350);
		
		//noDealerView.
		rp = (RelativeLayout.LayoutParams) noDealerView.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(218);
		rp.height = ResizeUtils.getSpecificLength(248);
		rp.topMargin = ResizeUtils.getSpecificLength(72);
		
		int size = dealerViews.length;
		for(int i=0; i<3; i++) {
			ResizeUtils.viewResizeForRelative(184, 251, dealerViews[i], null, null, new int[]{24, 72, 0, 0});
		}
		
		//relativeForBidding
		rp = (RelativeLayout.LayoutParams) relativeForBidding.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(373);
		rp.topMargin = ResizeUtils.getSpecificLength(18);
		
		//tvBiddingCount.
		rp = (RelativeLayout.LayoutParams) tvBiddingCount.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(68);
		rp.leftMargin = ResizeUtils.getSpecificLength(190);
		
		//btnBidding.
		rp = (RelativeLayout.LayoutParams) btnBidding.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(120);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.topMargin = ResizeUtils.getSpecificLength(6);
		rp.rightMargin = ResizeUtils.getSpecificLength(6);
		
		//biddingCarViews.
		size = biddingCarViews.length;
		for(int i=0; i<3; i++) {
			ResizeUtils.viewResizeForRelative(174, 255, biddingCarViews[i], null, null, new int[]{i==0?24:18, 90, 0, 0});
		}
		
		//usedMarketBg.
		rp = (RelativeLayout.LayoutParams) usedMarketBg.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(659);
		rp.topMargin = ResizeUtils.getSpecificLength(18);

		//btnUsedMarket.
		rp = (RelativeLayout.LayoutParams) btnUsedMarket.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(120);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.topMargin = ResizeUtils.getSpecificLength(6);
		rp.rightMargin = ResizeUtils.getSpecificLength(6);
		
		//usedCarViews.		
		size = usedCarViews.length;
		for(int i=0; i<size; i++) {
			rp = (RelativeLayout.LayoutParams) usedCarViews[i].getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(578);
			rp.height = ResizeUtils.getSpecificLength(175);
			rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
			rp.topMargin = ResizeUtils.getSpecificLength(i==0?84:18);
		}

		//directMarketTitle.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.mainForUserPage_directMarketTitle).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(68);
		rp.topMargin = ResizeUtils.getSpecificLength(20);
		
		//ivDirectMarket.
		rp = (RelativeLayout.LayoutParams) ivDirectMarket.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(248);
		
		//btnDirectMarket.
		rp = (RelativeLayout.LayoutParams) btnDirectMarket.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(120);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.topMargin = ResizeUtils.getSpecificLength(6);
		rp.rightMargin = ResizeUtils.getSpecificLength(6);
		
		//relativeForVideo.
		rp = (RelativeLayout.LayoutParams) relativeForVideo.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(316);
		rp.topMargin = ResizeUtils.getSpecificLength(18);
		
		//ivVideo.
		rp = (RelativeLayout.LayoutParams) ivVideo.getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(65);
		
		//btnVideo.
		rp = (RelativeLayout.LayoutParams) btnVideo.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(120);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.topMargin = ResizeUtils.getSpecificLength(6);
		rp.rightMargin = ResizeUtils.getSpecificLength(6);
		
		//btnPlay.
		rp = (RelativeLayout.LayoutParams) btnPlay.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(84);
		rp.height = ResizeUtils.getSpecificLength(85);
		rp.topMargin = ResizeUtils.getSpecificLength(60);
		
		//tvVideoTitle.
		rp = (RelativeLayout.LayoutParams) tvVideoTitle.getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(36);
		tvVideoTitle.setMaxWidth(ResizeUtils.getSpecificLength(304));
		
		//bottomBlank.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.mainForUserPage_bottomBlank).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(136);
		
		//buttonLinear.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.mainForUserPage_buttonLinear).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(123);
		rp.bottomMargin = ResizeUtils.getSpecificLength(4);
		
		size = tabButtons.length;
		for(int i=0; i<size; i++) {
			ResizeUtils.viewResize(128, 90, tabButtons[i], 1, Gravity.CENTER_VERTICAL, new int[]{i==0?16:32, 0, 0, 0});
		}
		
		FontUtils.setFontSize(tvBiddingCount, 30);
		FontUtils.setFontStyle(tvBiddingCount, FontUtils.BOLD);
		FontUtils.setFontSize(tvVideoTitle, 22);
	}

	@Override
	public int getContentViewId() {
		// TODO Auto-generated method stub
		return R.layout.fragment_main_for_user;
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
	public boolean parseJSON(JSONObject objJSON) {

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
		downloadMainInfos();
		
		checkPageScrollOffset();
		checkNotification();
		
		((MainActivity) mActivity).setLeftViewUserInfo();
		
		TimerUtils.addOnTimeChangedListener(onTimeChangedListener);
		setPagerTimer();
	}
	
	@Override
	public void onPause() {
		super.onPause();

		TimerUtils.removeOnTimeChangedListener(onTimeChangedListener);
		clearPagerTimer();
	}
	
	@Override
	public void onDestroyView() {
		
		imagePagerAdapter.setOnPagerItemClickedListener(null);
		scrollView.setOnScrollChangedListener(null);
		super.onDestroyView();
	}
	
	@Override
	public int getRootViewResId() {

		return R.id.mainForUserPage_mainLayout;
	}
	
//////////////////// Custom methods.

	public void downloadMainInfos() {
		
		//http://byecar.minsangk.com/appinfo/cover.json
		String url = BCPAPIs.MAIN_COVER_URL;
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("MainForUserPage.downloadMainInfos.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("MainForUserPage.downloadMainInfos.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					int size = 0;
					
					try {
						MainActivity.companyInfo = new CompanyInfo(objJSON.getJSONObject("company_info"));
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (Error e) {
						LogUtils.trace(e);
					}
					
					try {
						ArrayList<String> images = new ArrayList<String>();
						
						bids1.clear();
						JSONArray arJSON = objJSON.getJSONArray("bids1");
						size = arJSON.length();
						for(int i=0; i<size; i++) {
							Car car = new Car(arJSON.getJSONObject(i));
							bids1.add(car);
							images.add(car.getRep_img_url());
						}

						imagePagerAdapter.setArrayList(images);
						viewPager.getAdapter().notifyDataSetChanged();
						viewPager.setCurrentItem(0);
						
						pageNavigator.setSize(bids1.size());
						pageNavigator.setEmptyOffCircle();
						pageNavigator.invalidate();

						if(bids1.size() > 0) {
							setPagerInfo(0);
						}
					} catch (Exception e) {
						LogUtils.trace(e);
					}
					
					try {
						if(objJSON.has("bids_onsalecars_today_cnt")) {
							tvBiddingCount.setText(objJSON.getInt("bids_onsalecars_today_cnt") + "대");
						}
						
						bids2.clear();
						JSONArray arJSON = objJSON.getJSONArray("bids2");
						size = arJSON.length();
						for(int i=0; i<size; i++) {
							Car car = new Car(arJSON.getJSONObject(i));
							bids2.add(car);
						}

						setBiddingInfo();
					} catch (Exception e) {
						LogUtils.trace(e);
					}
					
					try {
						dealers.clear();
						JSONArray arJSON = objJSON.getJSONArray("dealer");
						size = arJSON.length();
						for(int i=0; i<size; i++) {
							dealers.add(new Car(arJSON.getJSONObject(i)));
						}
					} catch (Exception e) {
						LogUtils.trace(e);
					}
					
					setUsedCarViews();
					
					try {
						video = new Post(objJSON.getJSONObject("video"));
					} catch (Exception e) {
						LogUtils.trace(e);
					}
					
					setVideo();
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void setUsedCarViews() {
		
		int size = dealers.size();
		for(int i=0; i<size; i++) {

			final int INDEX = i;
			
			try {
				usedCarViews[i].setCar(dealers.get(i));
				usedCarViews[i].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						
						if(INDEX < dealers.size()) {
							((MainActivity)mActivity).showCarDetailPage(dealers.get(INDEX).getId(), null, Car.TYPE_DEALER);
						}
					}
				});
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
		}
	}

	public void setVideo() {
		
		if(video == null) {
			ivVideo.setImageDrawable(null);
			btnPlay.setVisibility(View.INVISIBLE);
			tvVideoTitle.setText(null);
		} else {
			ivVideo.setImageDrawable(null);
			
			String url = "http://img.youtube.com/vi/" + video.getYoutube_id() + "/0.jpg";
			ivVideo.setTag(url);
			DownloadUtils.downloadBitmap(url, new OnBitmapDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("MainPage.onError." + "\nurl : " + url);

					ivVideo.setImageDrawable(null);
					btnPlay.setVisibility(View.INVISIBLE);
					tvVideoTitle.setText(null);
				}

				@Override
				public void onCompleted(String url, Bitmap bitmap) {

					try {
						LogUtils.log("MainPage.onCompleted." + "\nurl : " + url);
						
						if(ivVideo != null && bitmap != null && !bitmap.isRecycled()) {
							ivVideo.setImageBitmap(bitmap);
						}
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
					}
				}
			});
			
			btnPlay.setVisibility(View.VISIBLE);
			btnPlay.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					mActivity.showVideo(video.getYoutube_id());
				}
			});
			
			tvVideoTitle.setText(video.getContent());
		}
	}
	
	public void setPagerInfo(int index) {
		
		if(bids1.size() == 0) {
			carInfoView.clearView();
			pageNavigator.setVisibility(View.INVISIBLE);
			auctionIcon.setVisibility(View.INVISIBLE);
			
		} else {
			Car car = bids1.get(index);
			
			carInfoView.setCarInfo(car);
			setTopDealerViews(car.getBids());
			
			pageNavigator.setVisibility(View.VISIBLE);
			pageNavigator.setSize(bids1.size());
			pageNavigator.setIndex(index);

			if(car.getStatus() < Car.STATUS_BID_COMPLETE) {
				auctionIcon.setBackgroundResource(R.drawable.main_hotdeal_mark);
				
				//경매 종료 시간 한시간 이내.
				if(car.getBid_until_at() -System.currentTimeMillis() / 1000 <= 3600) {
					auctionIcon.setVisibility(View.VISIBLE);
				} else {
					auctionIcon.setVisibility(View.INVISIBLE);
				}
			} else if(car.getStatus() == Car.STATUS_BID_COMPLETE) {
				auctionIcon.setBackgroundResource(R.drawable.main_hotdeal_mark2);
				auctionIcon.setVisibility(View.VISIBLE);
			} else {
				auctionIcon.setBackgroundResource(R.drawable.main_hotdeal_mark3);
				auctionIcon.setVisibility(View.VISIBLE);
			}
		}
	}
	
	public void setTopDealerViews(ArrayList<Bid> topDealers) {
		
		int size = dealerViews.length;
		for(int i=0; i<size; i++) {
			dealerViews[i].initInfos();
		}
		
		if(topDealers != null && topDealers.size() > 0) {
			
			size = topDealers.size();
			for(int i=0; i<size; i++) {
				dealerViews[i].setDealerInfo(topDealers.get(i), i);
			}
		}
	}

	public void setBiddingInfo() {
		
		int size = biddingCarViews.length;
		for(int i=0; i<size; i++) {
			biddingCarViews[i].clearView();
		}
		
		size = bids2.size();
		for(int i=0; i<size; i++) {
			biddingCarViews[i].setCar(bids2.get(i));
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

	public void checkNotification() {
		
		titleBar.setNoticeCount(0);
		
		String url = BCPAPIs.NOTIFICATION_URL + "?num=1";
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("MainPage.checkNotification.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("MainPage.checkNotification.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						titleBar.setNoticeCount(objJSON.getInt("unreadCount"));
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}

	public void setOnTimerListener() {
		
		if(onTimeChangedListener == null) {
			onTimeChangedListener = new OnTimeChangedListener() {
				
				@Override
				public void onTimeChanged() {

					if(bids1.size() > 0) {
						
						if(bids1.get(viewPager.getCurrentItem()).getStatus() > Car.STATUS_BID_COMPLETE) {
							carInfoView.clearTime();
							return;
						}
						
						Car car = bids1.get(viewPager.getCurrentItem());

						try {
							long remainTime = car.getBid_until_at() * 1000 
									+ (car.getStatus() < Car.STATUS_BID_COMPLETE ? 0 : 86400000) 
									- System.currentTimeMillis();
				        	
				        	if(remainTime < 0) {
				        		
				        		//경매 종료.
				        		if(car.getStatus() < Car.STATUS_BID_COMPLETE) {
				        			car.setStatus(Car.STATUS_BID_COMPLETE);
				    				auctionIcon.setBackgroundResource(R.drawable.main_hotdeal_mark2);
				    				
				        		//입찰 종료.
				        		} else {
				        			car.setStatus(Car.STATUS_BID_FAIL);
				    				auctionIcon.setBackgroundResource(R.drawable.main_hotdeal_mark3);
				        		}
				        		
				        		carInfoView.statusChanged(car);
				        	} else {
					        	carInfoView.setTime(car);
				        	}
						} catch (Exception e) {
							LogUtils.trace(e);
							carInfoView.clearTime();
						}
		        	}
					
					if(bids2.size() > 0) {
						
						int size = bids2.size();
						for(int i=0; i<size; i++) {
						
							try {
								Car car = bids2.get(i);
					        	biddingCarViews[i].setTime(car);
							} catch (Exception e) {
								carInfoView.clearTime();
								LogUtils.trace(e);
							}
						}
		        	}
				}
				
				@Override
				public String getName() {
					
					return "mainPageViewPager";
				}
				
				@Override
				public Activity getActivity() {

					return mActivity;
				}
			};
		}
	}

	public void setPagerTimer() {
		
		pagerTime = 0;
		pagerTimerTask = new TimerTask() {
			
			@Override
			public void run() {
				
				//하나 이상일 때만.
				if(viewPager == null || bids1.size() <= 1) {
					return;
				}

				//지정한 시간이 되면 페이저 넘기고 시간 초기화.
				if(pagerTime == PAGER_TIME) {
					mActivity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							
							viewPager.setCurrentItem((viewPager.getCurrentItem() + 1) % bids1.size(), true);
						}
					});
				} else {
					pagerTime++;
				}
			}
		};
		
		pagerTimer = new Timer();
		pagerTimer.schedule(pagerTimerTask, 0, 1000);
	}

	public void clearPagerTimer() {
		
		pagerTimerTask.cancel();
		pagerTimerTask = null;
		
		pagerTimer.purge();
		pagerTimer = null;
	}
	
	@Override
	public void bidStatusChanged(String event, Car car) {

		for(int i=0; i<bids1.size(); i++) {
			
			if(bids1.get(i).getId() == car.getId()) {
				
				downloadMainInfos();
				return;
			}
		}
		
		for(int i=0; i<bids2.size(); i++) {
			
			if(bids2.get(i).getId() == car.getId()) {
				
				downloadMainInfos();
				return;
			}
		}
	}
}
