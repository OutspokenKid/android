package com.byecar.fragments.user;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.byecar.fragments.CarRegistrationPage;
import com.byecar.fragments.OpenablePostListPage;
import com.byecar.models.Car;
import com.byecar.models.OpenablePost;
import com.byecar.views.TitleBar;
import com.byecar.views.UsedCarView;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.views.OffsetScrollView;
import com.outspoken_kid.views.OffsetScrollView.OnScrollChangedListener;
import com.outspoken_kid.views.PageNavigatorView;

public class MainPage extends BCPFragment {
	
	private OffsetScrollView scrollView;
	private ViewPager viewPager;
	private View auctionIcon;
	private PageNavigatorView pageNavigator;
	private ProgressBar progressBar;
	private TextView tvRemainTime;
	private TextView tvRemainTimeText;
	private TextView tvCarInfo1;
	private TextView tvCarInfo2;
	private TextView tvCurrentPrice;
	private TextView tvCurrentPriceText;
	private TextView tvBidCount;
	private Button btnLike;
	private Button btnAuction;
	private Button btnRegistration;
	private View noticeTitle;
	private ImageView ivNotice;
	private Button btnNotice;
	private LinearLayout usedMarketLinear;
	private UsedCarView[] usedCarViews = new UsedCarView[3];
	private Button btnUsedMarket;
	private ImageView ivDirectMarket;
	private Button btnDirectMarket;
	
	private ArrayList<Car> bids = new ArrayList<Car>();
	private ArrayList<Car> dealers = new ArrayList<Car>();
	private ArrayList<Car> certifieds = new ArrayList<Car>();
	private ImagePagerAdapter imagePagerAdapter;
	private OpenablePost notice;
	
	private Handler mHandler;
	private Thread checkTime;
	private Runnable updateTime;
	public boolean needRunThread;
	
	private int scrollOffset; 
	private int standardLength;
	private float diff;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.mainForUserPage_titleBar);
		
		scrollView = (OffsetScrollView) mThisView.findViewById(R.id.mainForUserPage_scrollView);
		viewPager = (ViewPager) mThisView.findViewById(R.id.mainForUserPage_viewPager);
		auctionIcon = mThisView.findViewById(R.id.mainForUserPage_auctionIcon);
		pageNavigator = (PageNavigatorView) mThisView.findViewById(R.id.mainForUserPage_pageNavigator);
		progressBar = (ProgressBar) mThisView.findViewById(R.id.mainForUserPage_progressBar);
		tvRemainTime = (TextView) mThisView.findViewById(R.id.mainForUserPage_tvRemainTime);
		tvRemainTimeText = (TextView) mThisView.findViewById(R.id.mainForUserPage_tvRemainTimeText);
		tvCarInfo1 = (TextView) mThisView.findViewById(R.id.mainForUserPage_tvCarInfo1);
		tvCarInfo2 = (TextView) mThisView.findViewById(R.id.mainForUserPage_tvCarInfo2);
		tvCurrentPrice = (TextView) mThisView.findViewById(R.id.mainForUserPage_tvCurrentPrice);
		tvCurrentPriceText = (TextView) mThisView.findViewById(R.id.mainForUserPage_tvCurrentPriceText);
		tvBidCount = (TextView) mThisView.findViewById(R.id.mainForUserPage_tvBidCount);
		btnLike = (Button) mThisView.findViewById(R.id.mainForUserPage_btnLike);
		btnAuction = (Button) mThisView.findViewById(R.id.mainForUserPage_btnAuction);
		btnRegistration = (Button) mThisView.findViewById(R.id.mainForUserPage_btnRegistration);
		noticeTitle = mThisView.findViewById(R.id.mainForUserPage_noticeTitle);
		ivNotice = (ImageView) mThisView.findViewById(R.id.mainForUserPage_ivNotice);
		btnNotice = (Button) mThisView.findViewById(R.id.mainForUserPage_btnNotice);
		usedMarketLinear = (LinearLayout) mThisView.findViewById(R.id.mainForUserPage_usedMarketLinear);
		btnUsedMarket = (Button) mThisView.findViewById(R.id.mainForUserPage_btnUsedMarket);
		ivDirectMarket = (ImageView) mThisView.findViewById(R.id.mainForUserPage_ivDirectMarket);
		btnDirectMarket = (Button) mThisView.findViewById(R.id.mainForUserPage_btnDirectMarket);
	}

	@Override
	public void setVariables() {

		mHandler = new Handler();
	    updateTime = new Runnable() {

	    	//지난 시간.
	    	private long passTime;
	    	
	    	//남은 시간.
	    	private long remainTime;
	    	
	        public void run() {
	        	
	        	if(bids.size() > 0) {
	        		
	        		/* 24시간 기준. 24h = 86400s.
		        	 * remainTime = bid_until_at - (현재 시간 / 1000) = (남은 시간) [초]
		        	 * 24시간 - (남은 시간) = (지난 시간).
		        	 * 
		        	 * 11시간 11분 11초
		        	 * 시간 : 60초 * 60분
		        	 * 분 : 60초
		        	 * 초 : 1초
		        	 * 
		        	 * 11 * 60초 * 60분 = 11시간 = 11 * 3600 = 39600
		        	 * 11 * 60초 = 11분 = 11 * 60 = 660
		        	 * 11초 = 11초 = 11
		        	 * 
		        	 * 합계 = 40271
		        	 * 
		        	 * 40271 / 3600 = 11
		        	 * 40271 % 3600 / 60 = 11
		        	 * 40271 % 3600 % 60 = 11
		        	 * 
		        	 * s 단위임.
		        	 */
		        	Car currentCar = bids.get(viewPager.getCurrentItem());
		        	remainTime = currentCar.getBid_until_at()
		        			- (System.currentTimeMillis() / 1000);
		        	passTime = 86400 - remainTime;
		        	
		        	if(remainTime <= 0) {
		        		currentCar.setStatus(Car.STATUS_BID_COMPLETE);
		        		return;
		        	}
		        	
		        	String formattedRemainTime = StringUtils.getDateString("HH : mm : ss", remainTime * 1000);
		        	tvRemainTime.setText(formattedRemainTime);
		        	
		        	int percentage = (int)((double)passTime / 86400d * 10000);
		        	progressBar.setProgress(percentage);
	        	}
	        }
	    };
	}

	@Override
	public void createPage() {

		titleBar.setBgColor(Color.WHITE);
		titleBar.setBgAlpha(0);
		
		viewPager.setAdapter(imagePagerAdapter = new ImagePagerAdapter(mContext));
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

		btnLike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				try {
					setLike(bids.get(viewPager.getCurrentItem()));
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}
			}
		});
		
		imagePagerAdapter.setOnPagerItemClickedListener(new OnPagerItemClickedListener() {
			
			@Override
			public void onPagerItemClicked(int position) {

				Bundle bundle = new Bundle();
				bundle.putInt("id", bids.get(position).getId());
				bundle.putInt("type", Car.TYPE_BID);
				mActivity.showPage(BCPConstants.PAGE_CAR_DETAIL, bundle);
			}
		});
		
		btnAuction.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putInt("type", Car.TYPE_BID);
				mActivity.showPage(BCPConstants.PAGE_CAR_LIST, bundle);
			}
		});
	
		btnRegistration.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putInt("type", CarRegistrationPage.TYPE_REGISTRATION);
				bundle.putInt("carType", Car.TYPE_BID);
				mActivity.showPage(BCPConstants.PAGE_CAR_REGISTRATION, bundle);
			}
		});
	
		btnNotice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putInt("type", OpenablePostListPage.TYPE_NOTICE);
				mActivity.showPage(BCPConstants.PAGE_OPENABLE_POST_LIST, bundle);
			}
		});
		
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
		rp.leftMargin = -ResizeUtils.getSpecificLength(5);
		tvRemainTimeText.setPadding(ResizeUtils.getSpecificLength(22), 0, 0, 0);
		
		//timeIcon.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.mainForUserPage_timeIcon).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(18);
		rp.height = ResizeUtils.getSpecificLength(18);
		rp.leftMargin = -ResizeUtils.getSpecificLength(10);
		rp.topMargin = ResizeUtils.getSpecificLength(7);
		
		//remainBg.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.mainForUserPage_remainBg).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(147);
		
		//tvCarInfo1.
		rp = (RelativeLayout.LayoutParams) tvCarInfo1.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(340);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		rp.topMargin = ResizeUtils.getSpecificLength(10);
		
		//line.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.mainForUserPage_line).getLayoutParams();
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
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.mainForUserPage_tvLikeText).getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(5);
		rp.rightMargin = ResizeUtils.getSpecificLength(2);
		
		//btnAuction.
		rp = (RelativeLayout.LayoutParams) btnAuction.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(320);
		rp.height = ResizeUtils.getSpecificLength(68);
		
		//btnRegistration.
		rp = (RelativeLayout.LayoutParams) btnRegistration.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(68);

		//noticeTitle.
		rp = (RelativeLayout.LayoutParams) noticeTitle.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(68);
		rp.topMargin = ResizeUtils.getSpecificLength(20);
		
		//ivNotice.
		rp = (RelativeLayout.LayoutParams) ivNotice.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(254);
		
		//btnNotice.
		rp = (RelativeLayout.LayoutParams) btnNotice.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(68);
		
		//usedMarketTitle.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.mainForUserPage_usedMarketTitle).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(320);
		rp.topMargin = ResizeUtils.getSpecificLength(20);
		
		//usedMarketLinear.
		rp = (RelativeLayout.LayoutParams) usedMarketLinear.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.topMargin = ResizeUtils.getSpecificLength(88);

		//btnUsedMarket.
		rp = (RelativeLayout.LayoutParams) btnUsedMarket.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(68);
		
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
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(68);
		
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
		FontUtils.setFontSize((TextView)mThisView.findViewById(R.id.mainForUserPage_tvLikeText), 20);
		
		FontUtils.setFontSize((TextView)mThisView.findViewById(R.id.mainForUserPage_tvCopyright), 16);
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
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		needRunThread = false;
		
		if(checkTime != null) {
			checkTime.interrupt();
		}

		((MainActivity) mActivity).clearLeftViewUserInfo();
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
						ArrayList<String> images = new ArrayList<String>();
						
						bids.clear();
						JSONArray arJSON = objJSON.getJSONArray("bids");
						size = arJSON.length();
						for(int i=0; i<size; i++) {
							Car car = new Car(arJSON.getJSONObject(i));
							bids.add(car);
							images.add(car.getRep_img_url());
						}

						imagePagerAdapter.setArrayList(images);
						viewPager.getAdapter().notifyDataSetChanged();
						viewPager.setCurrentItem(0);
						
						pageNavigator.setSize(bids.size());
						pageNavigator.setEmptyOffCircle();
						pageNavigator.invalidate();

						if(bids.size() > 0) {
							setPagerInfo(0);
						}
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
						
						setUsedCarViews();
					} catch (Exception e) {
						LogUtils.trace(e);
					}
					
					try {
						certifieds.clear();
						JSONArray arJSON = objJSON.getJSONArray("certified");
						size = arJSON.length();
						for(int i=0; i<size; i++) {
							certifieds.add(new Car(arJSON.getJSONObject(i)));
						}
					} catch (Exception e) {
						LogUtils.trace(e);
					}
					
					try {
						notice = new OpenablePost(objJSON.getJSONObject("notice"));
						setNotice();
					} catch (Exception e) {
						LogUtils.trace(e);
					}

					runThread();
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void setUsedCarViews() {
		
		for(int i=0; i<Math.min(3, dealers.size()); i++) {

			final int INDEX = i;
			
			try {
				usedCarViews[i] = new UsedCarView(mContext, i);
				usedCarViews[i].setTexts(dealers.get(i).getModel_name(), 
						dealers.get(i).getPrice());
				usedCarViews[i].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						
						if(INDEX < dealers.size()) {
							Bundle bundle = new Bundle();
							bundle.putInt("id", dealers.get(INDEX).getId());
							bundle.putInt("type", Car.TYPE_DEALER);
							mActivity.showPage(BCPConstants.PAGE_CAR_DETAIL, bundle);
						}
					}
				});
				usedCarViews[i].setImageUrl(dealers.get(i).getRep_img_url());
				usedMarketLinear.addView(usedCarViews[i]);
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
		}
	}
	
	public void setNotice() {

		ivNotice.setImageDrawable(null);
		
		String url = notice.getRep_img_url();
		ivNotice.setTag(url);
		DownloadUtils.downloadBitmap(url, new OnBitmapDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("MainForUserPage.setNotice.onError." + "\nurl : " + url);
				noticeTitle.setVisibility(View.GONE);
				ivNotice.setVisibility(View.GONE);
				btnNotice.setVisibility(View.GONE);
			}

			@Override
			public void onCompleted(String url, Bitmap bitmap) {

				try {
					LogUtils.log("MainForUserPage.setNotice.onCompleted." + "\nurl : " + url);
					
					if(bitmap != null && !bitmap.isRecycled()) {
						ivNotice.setImageBitmap(bitmap);

						noticeTitle.setVisibility(View.VISIBLE);
						ivNotice.setVisibility(View.VISIBLE);
						btnNotice.setVisibility(View.VISIBLE);
					} else {
						noticeTitle.setVisibility(View.GONE);
						ivNotice.setVisibility(View.GONE);
						btnNotice.setVisibility(View.GONE);
					}
					return;
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}

				noticeTitle.setVisibility(View.GONE);
				ivNotice.setVisibility(View.GONE);
				btnNotice.setVisibility(View.GONE);
			}
		});
	}
	
	public void setPagerInfo(int index) {
		
		tvCarInfo1.setText(bids.get(index).getCar_full_name());
		tvCarInfo2.setText(bids.get(index).getYear() + "년 / "
				+ StringUtils.getFormattedNumber(bids.get(index).getMileage()) + "km / "
				+ bids.get(index).getArea());
		
		tvCurrentPrice.setText(StringUtils.getFormattedNumber(bids.get(index).getPrice()) + getString(R.string.won));
		tvBidCount.setText("입찰자 " + bids.get(index).getBids_cnt() + "명");
		
		if(bids.size() > 1) {
			pageNavigator.setVisibility(View.VISIBLE);
			pageNavigator.setIndex(index);
		} else {
			pageNavigator.setVisibility(View.INVISIBLE);
		}

		auctionIcon.setBackgroundResource(R.drawable.main_hotdeal_mark);
		
		if(bids.get(index).getIs_liked() == 0) {
			btnLike.setBackgroundResource(R.drawable.main_like_btn_a);
		} else {
			btnLike.setBackgroundResource(R.drawable.main_like_btn_b);
		}

		int likesCount = bids.get(index).getLikes_cnt();
		
		if(likesCount > 9999) {
			likesCount = 9999;
		}
		
		btnLike.setText("" + likesCount);
	}

	public void runThread() {
		
		needRunThread = true;
		
		if(checkTime != null) {
			checkTime.interrupt();
		}
		
		checkTime = new Thread() {
			
	        public void run() {
	            try {
	                while(needRunThread) {
	                	Thread.sleep(1000);
	                    mHandler.post(updateTime);
	                }
	            } catch (Exception e) {
	            }
	        }
	    };
	    checkTime.start();
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
		
		titleBar.setNoticeCount(5);
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

						LogUtils.log("MainForUserPage.onError." + "\nurl : "
								+ url);
					}

					@Override
					public void onCompleted(String url,
							JSONObject objJSON) {

						try {
							LogUtils.log("MainForUserPage.onCompleted."
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
}
