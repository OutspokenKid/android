package com.byecar.fragments.user;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.GuideActivity;
import com.byecar.byecarplus.MainActivity;
import com.byecar.byecarplus.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPFragment;
import com.byecar.classes.ImagePagerAdapter;
import com.byecar.classes.ImagePagerAdapter.OnPagerItemClickedListener;
import com.byecar.models.Car;
import com.byecar.views.NormalCarView;
import com.byecar.views.PriceTextView;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.TimerUtils.OnTimeChangedListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.TimerUtils;
import com.outspoken_kid.views.OffsetScrollView;
import com.outspoken_kid.views.OffsetScrollView.OnScrollChangedListener;
import com.outspoken_kid.views.PageNavigatorView;

public class DirectMarketPage extends BCPFragment {

	private OffsetScrollView scrollView;
	private ViewPager viewPager;
	private View certifiedIcon;
	private PageNavigatorView pageNavigator;
	private ProgressBar progressBar;
	private TextView tvRemainTime;
	private TextView tvRemainTimeText;
	private TextView tvCarInfo1;
	private TextView tvCarInfo2;
	private PriceTextView priceTextView;
	private Button btnLike;
	private TextView tvLikeText;
	private Button btnCertified;
	private LinearLayout normalLinear;
	private Button btnNormal;
	private Button btnGuide;
	
	private ArrayList<Car> certified = new ArrayList<Car>();
	private ArrayList<Car> normal = new ArrayList<Car>();
	private ArrayList<NormalCarView> normalCarFrames = new ArrayList<NormalCarView>();
	private ImagePagerAdapter imagePagerAdapter;

	private int scrollOffset;
	private int standardLength;
	private float diff;
	
	private OnTimeChangedListener onTimeChangedListener;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.directMarketPage_titleBar);
		
		scrollView = (OffsetScrollView) mThisView.findViewById(R.id.directMarketPage_scrollView);
		viewPager = (ViewPager) mThisView.findViewById(R.id.directMarketPage_viewPager);
		certifiedIcon = mThisView.findViewById(R.id.directMarketPage_certifiedIcon);
		pageNavigator = (PageNavigatorView) mThisView.findViewById(R.id.directMarketPage_pageNavigator);
		progressBar = (ProgressBar) mThisView.findViewById(R.id.directMarketPage_progressBar);
		tvRemainTime = (TextView) mThisView.findViewById(R.id.directMarketPage_tvRemainTime);
		tvRemainTimeText = (TextView) mThisView.findViewById(R.id.directMarketPage_tvRemainTimeText);
		tvCarInfo1 = (TextView) mThisView.findViewById(R.id.directMarketPage_tvCarInfo1);
		tvCarInfo2 = (TextView) mThisView.findViewById(R.id.directMarketPage_tvCarInfo2);
		priceTextView = (PriceTextView) mThisView.findViewById(R.id.directMarketPage_priceTextView);
		btnLike = (Button) mThisView.findViewById(R.id.directMarketPage_btnLike);
		tvLikeText = (TextView) mThisView.findViewById(R.id.directMarketPage_tvLikeText);
		btnCertified = (Button) mThisView.findViewById(R.id.directMarketPage_btnCertified);
		normalLinear = (LinearLayout) mThisView.findViewById(R.id.directMarketPage_normalLinear);
		btnNormal = (Button) mThisView.findViewById(R.id.directMarketPage_btnNormal);
		btnGuide = (Button) mThisView.findViewById(R.id.directMarketPage_btnGuide);
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
		
		if(normalLinear.getChildCount() == 0) {
			addNormalCarViews();
		}
		
		priceTextView.setType(PriceTextView.TYPE_DETAIL_OTHERS);
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

				((MainActivity)mActivity).showCarDetailPage(certified.get(position).getId(), 
						null, Car.TYPE_DIRECT_CERTIFIED);
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
		
		btnNormal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putInt("type", Car.TYPE_DIRECT_NORMAL);
				mActivity.showPage(BCPConstants.PAGE_CAR_LIST, bundle);
			}
		});
	
		btnGuide.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Intent intent = new Intent(mActivity, GuideActivity.class);
				intent.putExtra("type", GuideActivity.TYPE_USER_DIRECT);
				mActivity.startActivity(intent);
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
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.directMarketPage_timeIcon).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(18);
		rp.height = ResizeUtils.getSpecificLength(18);
		rp.leftMargin = -ResizeUtils.getSpecificLength(10);
		rp.topMargin = ResizeUtils.getSpecificLength(7);
		
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
		
		//priceTextView.
		rp = (RelativeLayout.LayoutParams) priceTextView.getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(32);
		rp.rightMargin = ResizeUtils.getSpecificLength(20);
		
		//btnLike.
		rp = (RelativeLayout.LayoutParams) btnLike.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(90);
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.topMargin = -ResizeUtils.getSpecificLength(8);
		rp.rightMargin = ResizeUtils.getSpecificLength(12);
		btnLike.setPadding(ResizeUtils.getSpecificLength(32), 0, 
				ResizeUtils.getSpecificLength(10), ResizeUtils.getSpecificLength(2));
		
		//tvLikeText.
		rp = (RelativeLayout.LayoutParams) tvLikeText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.topMargin = -ResizeUtils.getSpecificLength(2);
		rp.rightMargin = ResizeUtils.getSpecificLength(2);
		tvLikeText.setPadding(0, 0, 0, ResizeUtils.getSpecificLength(2));
		
		//btnCertified.
		rp = (RelativeLayout.LayoutParams) btnCertified.getLayoutParams();
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
		
		//btnGuide.
		rp = (RelativeLayout.LayoutParams) btnGuide.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(60);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.topMargin = ResizeUtils.getSpecificLength(14);
		rp.rightMargin = ResizeUtils.getSpecificLength(10);
		
		FontUtils.setFontSize(tvRemainTime, 24);
		FontUtils.setFontStyle(tvRemainTime, FontUtils.BOLD);
		FontUtils.setFontSize(tvRemainTimeText, 16);
		FontUtils.setFontSize(tvCarInfo1, 32);
		FontUtils.setFontStyle(tvCarInfo1, FontUtils.BOLD);
		FontUtils.setFontSize(tvCarInfo2, 20);
		FontUtils.setFontSize(btnLike, 18);
		FontUtils.setFontSize(tvLikeText, 20);
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
		
		TimerUtils.addOnTimeChangedListener(onTimeChangedListener);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
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

									((MainActivity)mActivity).showCarDetailPage(ID, null, Car.TYPE_DIRECT_NORMAL);
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
		
		if(certified.size() == 0) {
			tvCarInfo1.setText(null);
			tvCarInfo2.setText(null);
			priceTextView.setPrice(0);
			pageNavigator.setVisibility(View.INVISIBLE);
			certifiedIcon.setVisibility(View.INVISIBLE);
			progressBar.setProgress(0);
			btnLike.setBackgroundResource(R.drawable.main_like_btn_a);
			btnLike.setText(null);
		}
		
		progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_custom_orange));
		
		tvCarInfo1.setText(certified.get(index).getCar_full_name());
		tvCarInfo2.setText(certified.get(index).getYear() + "년 / "
				+ StringUtils.getFormattedNumber(certified.get(index).getMileage()) + "km / "
				+ certified.get(index).getArea());
		
		if(certified.size() > 1) {
			pageNavigator.setVisibility(View.VISIBLE);
			pageNavigator.setIndex(index);
		} else {
			pageNavigator.setVisibility(View.INVISIBLE);
		}
		
		priceTextView.setPrice(certified.get(index).getPrice());
		
		if(certified.get(index).getIs_liked() == 0) {
			btnLike.setBackgroundResource(R.drawable.main_like_btn_a);
		} else {
			btnLike.setBackgroundResource(R.drawable.main_like_btn_b);
		}
		
		final int INDEX = index;
		btnLike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setLike(certified.get(INDEX));
			}
		});

		int likesCount = certified.get(index).getLikes_cnt();
		
		if(likesCount > 9999) {
			likesCount = 9999;
		}
		
		btnLike.setText("" + likesCount);
	}

	public void setOnTimerListener() {
		
		if(onTimeChangedListener == null) {
			onTimeChangedListener = new OnTimeChangedListener() {
				
				@Override
				public void onTimeChanged() {

					if(certified.size() > 0) {

						if(certified.get(viewPager.getCurrentItem()).getStatus() > Car.STATUS_BID_COMPLETE) {
							progressBar.setProgress(1000);
							tvRemainTime.setText("-- : -- : --");
							return;
						}
						
						Car car = certified.get(viewPager.getCurrentItem());

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
//				        	long progressValue = 1000 - (remainTime * 1000 / progressTime);
				        	long progressValue = remainTime * 1000 / progressTime;
				        	
				        	if(remainTime < 0) {
				        		TimerUtils.removeOnTimeChangedListener(onTimeChangedListener);
								tvRemainTime.setText("-- : -- : --");
								progressBar.setProgress(0);
				        	} else {
				        		String formattedRemainTime = StringUtils.getTimeString(remainTime);
					        	tvRemainTime.setText(formattedRemainTime);
					        	progressBar.setProgress((int)progressValue);
				        	}
						} catch (Exception e) {
							LogUtils.trace(e);
							TimerUtils.removeOnTimeChangedListener(onTimeChangedListener);
							tvRemainTime.setText("-- : -- : --");
							progressBar.setProgress(1000);
						}
		        	}
				}
				
				@Override
				public String getName() {
					
					return "directMarketPageViewPager";
				}
				
				@Override
				public Activity getActivity() {

					return mActivity;
				}
			};
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

	public void setLike(Car car) {
		
		String url = null;

		if(car.getIs_liked() == 0) {
			btnLike.setBackgroundResource(R.drawable.main_like_btn_b);
			car.setLikes_cnt(car.getLikes_cnt() + 1);
			car.setIs_liked(1);
			
			url = BCPAPIs.CAR_DIRECT_CERTIFIED_LIKE_URL;
		} else {
			btnLike.setBackgroundResource(R.drawable.main_like_btn_a);
			car.setLikes_cnt(car.getLikes_cnt() - 1);
			car.setIs_liked(0);

			url = BCPAPIs.CAR_DIRECT_CERTIFIED_UNLIKE_URL;
		}
		
		btnLike.setText("" + car.getLikes_cnt());
		
		url += "?onsalecar_id=" + car.getId();
		
		DownloadUtils.downloadJSONString(url,
				new OnJSONDownloadListener() {

					@Override
					public void onError(String url) {

						LogUtils.log("CarDetailPage.onError." + "\nurl : "
								+ url);
					}

					@Override
					public void onCompleted(String url,
							JSONObject objJSON) {

						try {
							LogUtils.log("CarDetailPage.onCompleted."
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
