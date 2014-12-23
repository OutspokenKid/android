package com.byecar.byecarplus.fragments.main_for_user;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.byecarplus.classes.BCPAPIs;
import com.byecar.byecarplus.classes.BCPConstants;
import com.byecar.byecarplus.classes.BCPFragmentForMainForUser;
import com.byecar.byecarplus.classes.ImagePagerAdapter;
import com.byecar.byecarplus.classes.ImagePagerAdapter.OnPagerItemClickedListener;
import com.byecar.byecarplus.models.Car;
import com.byecar.byecarplus.models.Notice;
import com.byecar.byecarplus.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.OffsetScrollView;
import com.outspoken_kid.views.OffsetScrollView.OnScrollChangedListener;
import com.outspoken_kid.views.PageNavigatorView;

public class MainForUserPage extends BCPFragmentForMainForUser {
	
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
	private Button btnAuction;
	private Button btnRegistration;
	private FrameLayout noticeFrame;
	private ImageView ivNotice;
	private LinearLayout usedMarketLinear;
	private UsedCarFrame[] usedCarFrames = new UsedCarFrame[3];
	private Button btnUsedMarket;
	private ImageView ivDirectMarket;
	private Button btnDirectMarket;
	
	private ArrayList<Car> bids = new ArrayList<Car>();
	private ArrayList<Car> dealers = new ArrayList<Car>();
	private ArrayList<Car> certifieds = new ArrayList<Car>();
	private ImagePagerAdapter imagePagerAdapter;
	private Notice notice;
	
	private Handler mHandler;
	private Thread checkTime;
	private Runnable updateTime;
	public boolean needRunThread;
	
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
		btnAuction = (Button) mThisView.findViewById(R.id.mainForUserPage_btnAuction);
		btnRegistration = (Button) mThisView.findViewById(R.id.mainForUserPage_btnRegistration);
		noticeFrame = (FrameLayout) mThisView.findViewById(R.id.mainForUserPage_noticeFrame);
		ivNotice = (ImageView) mThisView.findViewById(R.id.mainForUserPage_ivNotice);
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
	    	private int passTime;
	    	
	    	//남은 시간.
	    	private int remainTime;
	    	
	        public void run() {
	        	
	        	if(bids.size() > 0) {
	        		
	        		/* 24시간 기준. 24h = 86400s.
		        	 * bid_until_at - (현재시간) = (남은 시간)
		        	 * 24시간 - (남은 시간) = (지난 시간).
		        	 * 
		        	 * s 단위임.
		        	 */
		        	Car currentCar = bids.get(viewPager.getCurrentItem());
		        	remainTime = (int)(currentCar.getBid_until_at()
		        			- (System.currentTimeMillis() / 1000));
		        	passTime = 86400 - remainTime;
		        	
		        	if(remainTime <= 0) {
		        		currentCar.setStatus(Car.BID_COMPLETE);
		        		return;
		        	}
		        	
		        	String formattedRemainTime = StringUtils.getDateString("hh : mm : ss", remainTime * 1000);
		        	
		        	tvRemainTime.setText(formattedRemainTime);
		        	progressBar.setProgress(passTime);
	        	}
	        }
	    };
	}

	@Override
	public void createPage() {

		titleBar.setBgColor(Color.WHITE);
		titleBar.setBgAlpha(0);
		
		viewPager.setAdapter(imagePagerAdapter = new ImagePagerAdapter(mContext));
		addUsedCarFrames();
	}

	@Override
	public void setListeners() {

		titleBar.getMenuButton().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(mActivity.isOpen()) {
					mActivity.closeMenu();
				} else {
					mActivity.openMenu();
				}
			}
		});

		titleBar.getBtnNotice().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				ToastUtils.showToast("Notice button clicked.");
			}
		});
		
		scrollView.setOnScrollChangedListener(new OnScrollChangedListener() {
			
			@Override
			public void onScrollChanged(int offset) {
				
				try {
					if(offset < 500) {
						titleBar.setBgAlpha(0.002f * offset);
					} else {
						titleBar.setBgAlpha(1);
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
				bundle.putInt("id", bids.get(position).getId());
				mActivity.showPage(BCPConstants.PAGE_CAR_DETAIL, bundle);
			}
		});
		
		btnAuction.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(BCPConstants.PAGE_AUCTION_LIST, null);
			}
		});
	
		btnRegistration.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(BCPConstants.PAGE_AUCTION_REGISTRATION, null);
			}
		});
	
		btnUsedMarket.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(BCPConstants.PAGE_DEALER_LIST, null);
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
		
		//remainBg.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.mainForUserPage_remainBg).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(147);
		rp.topMargin = -2;
		
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
		
		//btnAuction.
		rp = (RelativeLayout.LayoutParams) btnAuction.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(320);
		rp.height = ResizeUtils.getSpecificLength(68);
		
		//btnRegistration.
		rp = (RelativeLayout.LayoutParams) btnRegistration.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(68);
		
		//noticeFrame.
		rp = (RelativeLayout.LayoutParams) noticeFrame.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(632);
		rp.height = ResizeUtils.getSpecificLength(336);
		rp.topMargin = ResizeUtils.getSpecificLength(9);
		
		//ivNotice.
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 
				ivNotice, 2, Gravity.TOP, new int[]{0, 88, 0, 0});
		
		//usedMarketTitle.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.mainForUserPage_usedMarketTitle).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(632);
		rp.height = ResizeUtils.getSpecificLength(338);
		rp.topMargin = ResizeUtils.getSpecificLength(9);
		
		//usedMarketLinear.
		rp = (RelativeLayout.LayoutParams) usedMarketLinear.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(632);
		rp.height = ResizeUtils.getSpecificLength(246);
		rp.topMargin = ResizeUtils.getSpecificLength(92);
		
		//btnUsedMarket.
		rp = (RelativeLayout.LayoutParams) btnUsedMarket.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(632);
		rp.height = ResizeUtils.getSpecificLength(69);
		
		//directMarketTitle.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.mainForUserPage_directMarketTitle).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(632);
		rp.height = ResizeUtils.getSpecificLength(89);
		rp.topMargin = ResizeUtils.getSpecificLength(9);
		
		//ivDirectMarket.
		rp = (RelativeLayout.LayoutParams) ivDirectMarket.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(632);
		rp.height = ResizeUtils.getSpecificLength(248);
		
		//btnDirectMarket.
		rp = (RelativeLayout.LayoutParams) btnDirectMarket.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(632);
		rp.height = ResizeUtils.getSpecificLength(69);
		
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
		
		titleBar.setNoticeCount(5);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		needRunThread = false;
		
		if(checkTime != null) {
			checkTime.interrupt();
		}
	}
	
//////////////////// Custom methods.
	
	public void addUsedCarFrames() {
		
		for(int i=0; i<3; i++) {
			usedCarFrames[i] = new UsedCarFrame(mContext);
			usedMarketLinear.addView(usedCarFrames[i]);
		}
	}

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
						
						setUsedCarFrames();
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
						notice = new Notice(objJSON.getJSONObject("notice"));
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
	
	public void setUsedCarFrames() {
		
		for(int i=0; i<Math.min(3, dealers.size()); i++) {

			final int INDEX = i;
			
			try {
				usedCarFrames[i].setTexts(dealers.get(i).getModel_name(), 
						dealers.get(i).getPrice());

				usedCarFrames[i].setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {

						Bundle bundle = new Bundle();
						bundle.putInt("id", bids.get(INDEX).getId());
						mActivity.showPage(BCPConstants.PAGE_CAR_DETAIL, bundle);
					}
				});
				
				usedCarFrames[i].downloadImage(dealers.get(i).getRep_img_url());
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
				noticeFrame.setVisibility(View.GONE);
			}

			@Override
			public void onCompleted(String url, Bitmap bitmap) {

				try {
					LogUtils.log("MainForUserPage.setNotice.onCompleted." + "\nurl : " + url);
					
					if(bitmap != null && !bitmap.isRecycled()) {
						ivNotice.setImageBitmap(bitmap);
						noticeFrame.setVisibility(View.VISIBLE);
					} else {
						noticeFrame.setVisibility(View.GONE);
					}
					return;
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
				
				noticeFrame.setVisibility(View.GONE);
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
	
//////////////////// Custom classes.
	
	public class UsedCarFrame extends FrameLayout {

		private ImageView ivImage;
		private TextView tvCar;
		private TextView tvPrice;
		
		public UsedCarFrame(Context context) {
			super(context);
			init();
		}
		
		public void init() {
		
			ResizeUtils.viewResize(184, 224, this, 1, Gravity.CENTER_VERTICAL, 
					new int[]{20, 0, 0, 0});
			
			//ivImage.
			ivImage = new ImageView(mContext);
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 145, ivImage, 2, 0, null);
			ivImage.setScaleType(ScaleType.CENTER_CROP);
			ivImage.setBackgroundResource(R.drawable.main_used_car_sub_frame_default);
			this.addView(ivImage);

			//frame.
			View frame = new View(mContext);
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 
					frame, 2, 0, null);
			frame.setBackgroundResource(R.drawable.main_used_car_sub_frame);
			this.addView(frame);
			
			//tvCar.
			tvCar = new TextView(mContext);
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 38, tvCar, 2, 0, new int[]{0, 145, 0, 0});
			tvCar.setTextColor(Color.rgb(57, 57, 57));
			FontUtils.setFontSize(tvCar, 18);
			tvCar.setGravity(Gravity.CENTER);
			this.addView(tvCar);
			
			//tvPrice.
			tvPrice = new TextView(mContext);
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 38, tvPrice, 2, 0, new int[]{0, 184, 0, 0});
			FontUtils.setFontSize(tvPrice, 24);
			FontUtils.setFontStyle(tvPrice, FontUtils.BOLD);
			tvPrice.setTextColor(Color.rgb(96, 70, 51));
			tvPrice.setGravity(Gravity.CENTER);
			this.addView(tvPrice);
		}
		
		public void downloadImage(String imageUrl) {
			
			ivImage.setImageDrawable(null);
			
			ivImage.setTag(imageUrl);
			DownloadUtils.downloadBitmap(imageUrl, new OnBitmapDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("UsedCarFrame.downloadImage.onError." + "\nurl : " + url);
				}

				@Override
				public void onCompleted(String url, Bitmap bitmap) {

					try {
						LogUtils.log("UsedCarFrame.downloadImage.onCompleted." + "\nurl : " + url);
						
						if(bitmap != null && !bitmap.isRecycled()) {
							ivImage.setImageBitmap(bitmap);
						}
						
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
					}
				}
			});
		}
		
		public void setTexts(String modelName, long price) {
			
			tvCar.setText(modelName);
			tvPrice.setText(StringUtils.getFormattedNumber(price) + getString(R.string.won));
		}

		public ImageView getIvImage() {
			
			return ivImage;
		}
	}
}
