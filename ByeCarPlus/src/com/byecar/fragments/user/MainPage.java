package com.byecar.fragments.user;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
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
import com.byecar.fragments.OpenablePostListPage;
import com.byecar.models.Car;
import com.byecar.models.CompanyInfo;
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
import com.outspoken_kid.utils.TimerUtils;
import com.outspoken_kid.utils.TimerUtils.OnTimeChangedListener;
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
	
	private int scrollOffset; 
	private int standardLength;
	private float diff;
	
	private OnTimeChangedListener onTimeChangedListener;
	
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
		rp.width = ResizeUtils.getSpecificLength(600);
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
		
		TimerUtils.addOnTimeChangedListener(onTimeChangedListener);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		((MainActivity) mActivity).clearLeftViewUserInfo();
		
		TimerUtils.removeOnTimeChangedListener(onTimeChangedListener);
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
					} catch (Exception e) {
						LogUtils.trace(e);
					}
					
					setUsedCarViews();
					
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
						if(objJSON.has("notice")) {
							notice = new OpenablePost(objJSON.getJSONObject("notice"));
						}
					} catch (Exception e) {
						LogUtils.trace(e);
					}
					
					setNotice();
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void setUsedCarViews() {
		
		if(usedMarketLinear.getChildCount() != 0) {
			return;
		}
		
		for(int i=0; i<3; i++) {

			final int INDEX = i;
			
			try {
				usedCarViews[i] = new UsedCarView(mContext);
				
				if(i < dealers.size()) {
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
				}
				
				usedMarketLinear.addView(usedCarViews[i]);
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
		}
	}
	
	public void setNotice() {

		if(notice == null) {
			noticeTitle.setVisibility(View.GONE);
			ivNotice.setVisibility(View.GONE);
			btnNotice.setVisibility(View.GONE);
			return;
		} else {
			noticeTitle.setVisibility(View.VISIBLE);
			ivNotice.setVisibility(View.VISIBLE);
			btnNotice.setVisibility(View.VISIBLE);
		}
		
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
		
		Car car = bids.get(index);
		
		tvCarInfo1.setText(car.getCar_full_name());
		tvCarInfo2.setText(car.getYear() + "년 / "
				+ StringUtils.getFormattedNumber(car.getMileage()) + "km / "
				+ car.getArea());
		
		tvCurrentPrice.setText(StringUtils.getFormattedNumber(car.getPrice()) + getString(R.string.won));
		tvBidCount.setText("입찰자 " + car.getBids_cnt() + "명");
		
		if(bids.size() > 1) {
			pageNavigator.setVisibility(View.VISIBLE);
			pageNavigator.setIndex(index);
		} else {
			pageNavigator.setVisibility(View.INVISIBLE);
		}

		if(car.getStatus() < Car.STATUS_BID_COMPLETE) {
			auctionIcon.setBackgroundResource(R.drawable.main_hotdeal_mark);
			progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_custom_orange));
		} else if(car.getStatus() == Car.STATUS_BID_COMPLETE) {
			progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_custom_green));
			auctionIcon.setBackgroundResource(R.drawable.main_hotdeal_mark2);
		} else {
			progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_custom_gray));
			auctionIcon.setBackgroundResource(R.drawable.main_hotdeal_mark3);
		}
		
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

	public void setOnTimerListener() {
		
		if(onTimeChangedListener == null) {
			onTimeChangedListener = new OnTimeChangedListener() {
				
				@Override
				public void onTimeChanged() {

					if(bids.size() > 0) {
		        		
						if(bids.get(viewPager.getCurrentItem()).getStatus() > Car.STATUS_BID_COMPLETE) {
							progressBar.setProgress(10000);
							tvRemainTime.setText("-- : -- : --");
							return;
						}
						
						Car car = bids.get(viewPager.getCurrentItem());
						
						/*
						 * ms 단위.
						 * progress : 0 ~ 1000
						 * progressTime = (car.getStatus() < Car.STATUS_BID_COMPLETE ? (bid_until_at - bid_begin_at) * 1000 : 86400000);
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
				        	long progressValue = 1000 - (remainTime * 1000 / progressTime);
				        	
				        	String formattedRemainTime = StringUtils.getDateString("HH : mm : ss", remainTime);
				        	tvRemainTime.setText(formattedRemainTime);
				        	progressBar.setProgress((int)progressValue);
						} catch (Exception e) {
							LogUtils.trace(e);
							TimerUtils.removeOnTimeChangedListener(onTimeChangedListener);
							tvRemainTime.setText("-- : -- : --");
							progressBar.setProgress(10000);
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
}
