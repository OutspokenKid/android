package com.cmons.cph.fragments.wholesale;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.classes.CphAdapter;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Notice;
import com.cmons.cph.models.Product;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;

public class WholesaleMainPage extends CmonsFragmentForWholesale {
	
	private Button btnShop;
	private Button btnNotice;
	private Button btnManagement;
	private Button btnOrder;
	private Button btnSample;
	private Button btnCustomer;
	private Button btnStaff;
	private Button btnSetting;
	private TextView tvHit;
	private TextView tvWholesale;
	
	private ViewPager viewPager;
	private TextView tvBestTitle;
	private TextView tvBestSellingCount;
	private View badge;
	private TextView tvRank;
	private Button arrowLeft;
	private Button arrowRight;
	
	private View cover;
	private RelativeLayout noticeRelative;
	private Button btnClose;
	private ListView listView;
	
	private AlphaAnimation aaIn, aaOut;
	private boolean animating;
	
	private ArrayList<Product> products = new ArrayList<Product>();
	private PagerAdapterForProducts pagerAdapter;
	private boolean needPlay;
	private boolean isPlaying; 
	
	@Override
	public void onResume() {
		super.onResume();
		refreshPage();
		downloadProducts();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		needPlay = false;
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.wholesaleMainPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.wholesaleMainPage_ivBg);
		
		btnShop = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnShop);
		btnNotice = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnNotice);
		btnManagement = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnManagement);
		btnOrder = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnOrder);
		btnSample = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnSample);
		btnCustomer = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnCustomer);
		btnStaff = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnStaff);
		btnSetting = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnSetting);
		
		tvHit = (TextView) mThisView.findViewById(R.id.wholesaleMainPage_tvHit);
		tvWholesale = (TextView) mThisView.findViewById(R.id.wholesaleMainPage_tvWholesale);
		
		viewPager = (ViewPager)  mThisView.findViewById(R.id.wholesaleMainPage_viewPager);
		tvBestTitle = (TextView) mThisView.findViewById(R.id.wholesaleMainPage_tvBestTitle);
		tvBestSellingCount = (TextView) mThisView.findViewById(R.id.wholesaleMainPage_tvBestSellingCount);
		badge = mThisView.findViewById(R.id.wholesaleMainPage_badge);
		tvRank = (TextView) mThisView.findViewById(R.id.wholesaleMainPage_tvRank);
		arrowLeft = (Button) mThisView.findViewById(R.id.wholesaleMainPage_arrowLeft);
		arrowRight = (Button) mThisView.findViewById(R.id.wholesaleMainPage_arrowRight);
		
		cover = mThisView.findViewById(R.id.wholesaleMainPage_cover);
		noticeRelative = (RelativeLayout) mThisView.findViewById(R.id.wholesaleMainPage_noticeRelative);
		btnClose = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnClose);
		listView = (ListView) mThisView.findViewById(R.id.wholesaleMainPage_listView);
	}

	@Override
	public void setVariables() {

		AnimationListener al = new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {

				animating = true;
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {

				animating = false;
			}
		};
		
		aaIn = new AlphaAnimation(0, 1);
		aaIn.setDuration(300);
		aaIn.setAnimationListener(al);
		
		aaOut = new AlphaAnimation(1, 0);
		aaOut.setDuration(300);
		aaOut.setAnimationListener(al);
	}

	@Override
	public void createPage() {

		tvHit.setText("오늘 방문 " + wholesale.getToday_visited_cnt());
		
		SpannableStringBuilder sp1 = new SpannableStringBuilder(wholesale.getName());
		sp1.setSpan(new RelativeSizeSpan(1.5f), 0, sp1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvWholesale.append(sp1); 
		
		SpannableStringBuilder sp2 = new SpannableStringBuilder("\n청평화몰 " + wholesale.getLocation());
		sp2.setSpan(new RelativeSizeSpan(1.0f), 0, sp2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvWholesale.append(sp2);
		
		adapter = new CphAdapter(mContext, getActivity().getLayoutInflater(), models);
		listView.setAdapter(adapter);
		
		pagerAdapter = new PagerAdapterForProducts();
		viewPager.setAdapter(pagerAdapter);
	}

	@Override
	public void setListeners() {

		titleBar.getBtnNotice()
				.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(noticeRelative.getVisibility() == View.VISIBLE) {
					hideNoticeRelative();
				} else {
					showNoticeRelative();
				}
			}
		});
		
		titleBar.getBtnAdd()
				.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(CphConstants.PAGE_WHOLESALE_WRITE, null);
			}
		});
		
		btnShop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				mActivity.showPage(CphConstants.PAGE_WHOLESALE_SHOP, null);
			}
		});
		
		btnNotice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(noticeRelative.getVisibility() == View.VISIBLE) {
					hideNoticeRelative();
				} else {
					showNoticeRelative();
				}
			}
		});
		
		btnManagement.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(CphConstants.PAGE_WHOLESALE_MANAGEMENT, null);
			}
		});
		
		btnOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(CphConstants.PAGE_WHOLESALE_ORDER_LIST, null);
			}
		});
		
		btnSample.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(CphConstants.PAGE_WHOLESALE_SAMPLE_LIST, null);
			}
		});
		
		btnCustomer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(CphConstants.PAGE_WHOLESALE_CUSTOMER_LIST, null);
			}
		});
		
		btnStaff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(CphConstants.PAGE_COMMON_STAFF, null);
			}
		});
		
		btnSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(CphConstants.PAGE_COMMON_SETTING, null);
			}
		});
		
		cover.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				hideNoticeRelative();
			}
		});
		
		btnClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				hideNoticeRelative();
			}
		});
	
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				hideNoticeRelative();
			}
		});

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(final int position) {

				LogUtils.log("###viewPager.onPageSelected.  position : " + position);
				tvBestTitle.setText(products.get(position).getName());
				tvBestSellingCount.setText("구매 : " + products.get(position).getOrdered_cnt());
				tvRank.setText((position + 1) + "위");
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
		
		int length_short = ResizeUtils.getScreenWidth()/3;
		int length_long = length_short * 2; 
		
		//btnShop.
		rp = (RelativeLayout.LayoutParams) btnShop.getLayoutParams();
		rp.width = length_long;
		rp.height = length_long;
		
		//btnNotice.
		rp = (RelativeLayout.LayoutParams) btnNotice.getLayoutParams();
		rp.width = LayoutParams.MATCH_PARENT;
		rp.height = length_short;
		
		//btnManagement.
		rp = (RelativeLayout.LayoutParams) btnManagement.getLayoutParams();
		rp.width = LayoutParams.MATCH_PARENT;
		rp.height = length_short;
		
		//btnOrder.
		rp = (RelativeLayout.LayoutParams) btnOrder.getLayoutParams();
		rp.width = length_short;
		rp.height = length_short;
		
		//btnSample.
		rp = (RelativeLayout.LayoutParams) btnSample.getLayoutParams();
		rp.width = length_short;
		rp.height = length_short;
		
		//btnCustomer.
		rp = (RelativeLayout.LayoutParams) btnCustomer.getLayoutParams();
		rp.width = LayoutParams.MATCH_PARENT;
		rp.height = length_short;
		
		//btnStaff.
		rp = (RelativeLayout.LayoutParams) btnStaff.getLayoutParams();
		rp.width = length_short;
		rp.height = length_short;
		
		//btnSetting.
		rp = (RelativeLayout.LayoutParams) btnSetting.getLayoutParams();
		rp.width = length_short;
		rp.height = length_short;
		
		//hitIcon.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.wholesaleMainPage_hitIcon).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(28);
		rp.height = ResizeUtils.getSpecificLength(19);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		rp.topMargin = ResizeUtils.getSpecificLength(20);
		
		//tvHit.
		rp = (RelativeLayout.LayoutParams) tvHit.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.leftMargin = ResizeUtils.getSpecificLength(10);
		
		//tvWholesale.
		rp = (RelativeLayout.LayoutParams) tvWholesale.getLayoutParams();
		rp.width = length_long;
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);
		FontUtils.setFontSize(tvWholesale, 22);
		
		//pagerBg.
		rp = (RelativeLayout.LayoutParams) mThisView.
				findViewById(R.id.wholesaleMainPage_pagerBg).getLayoutParams();
		rp.height = length_long;
		
		//viewPager.
		rp = (RelativeLayout.LayoutParams) viewPager.getLayoutParams();
		rp.height = length_long;
		
		//tvBestTitle.
		rp = (RelativeLayout.LayoutParams) tvBestTitle.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		FontUtils.setFontSize(tvBestTitle, 26);
		
		//tvBestSellingCount.
		rp = (RelativeLayout.LayoutParams) tvBestSellingCount.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		FontUtils.setFontSize(tvBestSellingCount, 28);
		
		//badge.
		rp = (RelativeLayout.LayoutParams) badge.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(97);
		rp.height = ResizeUtils.getSpecificLength(23);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		rp.topMargin = ResizeUtils.getSpecificLength(20);
		
		//tvRank.
		rp = (RelativeLayout.LayoutParams) tvRank.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(97);
		rp.height = ResizeUtils.getSpecificLength(40);
		FontUtils.setFontSize(tvBestSellingCount, 30);
		
		//arrowLeft.
		rp = (RelativeLayout.LayoutParams) arrowLeft.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(43);
		rp.height = ResizeUtils.getSpecificLength(66);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		rp.topMargin = length_short - ResizeUtils.getSpecificLength(33);
		
		//arrowRight.
		rp = (RelativeLayout.LayoutParams) arrowRight.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(43);
		rp.height = ResizeUtils.getSpecificLength(66);
		rp.rightMargin = ResizeUtils.getSpecificLength(20);
		rp.topMargin = length_short - ResizeUtils.getSpecificLength(33);
		
		//noticeRelative.
		rp = (RelativeLayout.LayoutParams) noticeRelative.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(630);
		rp.height = ResizeUtils.getSpecificLength(711);
		rp.leftMargin = ResizeUtils.getSpecificLength(62);
		rp.topMargin = ResizeUtils.getSpecificLength(58);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		
		//btnClose.
		rp = (RelativeLayout.LayoutParams) btnClose.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(52);
		rp.height = ResizeUtils.getSpecificLength(52);
		rp.topMargin = ResizeUtils.getSpecificLength(96);
		rp.rightMargin = ResizeUtils.getSpecificLength(32);
		
		//listView.
		rp = (RelativeLayout.LayoutParams) listView.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(530);
		rp.leftMargin = ResizeUtils.getSpecificLength(24);
		rp.rightMargin = ResizeUtils.getSpecificLength(24);
		rp.bottomMargin = ResizeUtils.getSpecificLength(24);
	}
	
	@Override
	public int getContentViewId() {

		return R.layout.fragment_wholesale_main;
	}

	@Override
	public boolean onBackPressed() {
		
		if(noticeRelative.getVisibility() == View.VISIBLE) {
			hideNoticeRelative();
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean parseJSON(JSONObject objJSON) {

		checkNewMessage();
		
		try {
			JSONArray arJSON = objJSON.getJSONArray("notices");
			
			int size = arJSON.length();
			
			for(int i=0; i<size; i++) {
				Notice notice = new Notice(arJSON.getJSONObject(i));
				notice.setItemCode(CphConstants.ITEM_NOTICE);
				models.add(notice);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		return false;
	}
	
	@Override
	public void downloadInfo() {
		
		url = CphConstants.BASE_API_URL + "wholesales/notices";
		
		if(isDownloading || isLastList) {
			return;
		}
		
		if(!isRefreshing) {
			showLoadingView();
		}
		
		isDownloading = true;
		url += "?num=0";
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("CmonsFragment.onError." + "\nurl : " + url);
				setPage(false);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("CmonsFragment.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);
					
					isLastList = parseJSON(objJSON);
					setPage(true);
				} catch (Exception e) {
					LogUtils.trace(e);
					setPage(false);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					setPage(false);
				}
			}
		});
	}
	
//////////////////// Custom methods.

	public void downloadProducts() {
		
		if(products.size() > 0) {
			viewPager.setCurrentItem(0);
			mThisView.findViewById(R.id.wholesaleMainPage_pagerBg).setVisibility(View.INVISIBLE);
			viewPager.setVisibility(View.VISIBLE);
			tvBestTitle.setVisibility(View.VISIBLE);
			tvBestSellingCount.setVisibility(View.VISIBLE);
			badge.setVisibility(View.VISIBLE);
			tvRank.setVisibility(View.VISIBLE);
			
			needPlay = true;
			
			if(!isPlaying) {
				playPager();
			}
			
			return;
		}
		
		String url = CphConstants.BASE_API_URL + "products/weekly_best" +
				"?wholesale_id=" + wholesale.getId();
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WholesaleMainPage.onError." + "\nurl : " + url);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("WholesaleMainPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						JSONArray arJSON = objJSON.getJSONArray("products");
						
						int size = arJSON.length();
						for(int i=0; i<size; i++) {
							Product product = new Product(arJSON.getJSONObject(i));
							product.setItemCode(CphConstants.ITEM_PRODUCT);
							products.add(product);
						}

						LogUtils.log("###.onCompleted.  products.size : " + products.size());
						
						if(size > 0) {
							pagerAdapter.notifyDataSetChanged();
							viewPager.setCurrentItem(0);
							tvBestTitle.setText(products.get(0).getName());
							tvBestSellingCount.setText("구매 : " + products.get(0).getOrdered_cnt());
							tvRank.setText("1위");
							
							mThisView.findViewById(R.id.wholesaleMainPage_pagerBg).setVisibility(View.INVISIBLE);
							viewPager.setVisibility(View.VISIBLE);
							tvBestTitle.setVisibility(View.VISIBLE);
							tvBestSellingCount.setVisibility(View.VISIBLE);
							badge.setVisibility(View.VISIBLE);
							tvRank.setVisibility(View.VISIBLE);
							
							needPlay = true;
							
							if(!isPlaying) {
								playPager();
							}
						}
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}

	public void playPager() {
		
		isPlaying = true;
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {

				if(!needPlay) {
					isPlaying = false;
					return;
				}
				
				if(products != null &&products.size() > 0) {
					
					int position = viewPager.getCurrentItem();
					viewPager.setCurrentItem((position + 1) % products.size(), true);
				}
				
				playPager();
			}
		}, 3000);
	}
	
	public void checkNewMessage() {
		
		int nonReadCount = models.size();
		
		String readListString = SharedPrefsUtils.getStringFromPrefs(CphConstants.PREFS_NOTICE, "readList");
		
		if(readListString != null) {
			String[] readList = (readListString).split(",");
			
			Notice notice;
			boolean isRead;
			
			for(int i=0; i<models.size(); i++) {

				notice = (Notice) models.get(i);
				isRead = false;
				
				for(String read : readList) {
					
					//읽은적 있음.
					if(read.equals("" + notice.getId())) {
						isRead = true;
						nonReadCount--;
						break;
					}
				}
				
				notice.setRead(isRead);
			}
		}
		
		//새로운 메시지가 있느냐의 여부에 따라 titleBar.getNotice() 노출 결정.
		if(nonReadCount > 0) {
			titleBar.getBtnNotice().setVisibility(View.VISIBLE);
		} else {
			titleBar.getBtnNotice().setVisibility(View.INVISIBLE);
		}
	}
	
	public void showNoticeRelative() {

		if(!animating && noticeRelative.getVisibility() != View.VISIBLE) {
			
			refreshPage();
			
			noticeRelative.setVisibility(View.VISIBLE);
			cover.setVisibility(View.VISIBLE);
			
			noticeRelative.startAnimation(aaIn);
			cover.startAnimation(aaIn);
		}
	}
	
	public void hideNoticeRelative() {

		if(!animating && noticeRelative.getVisibility() == View.VISIBLE) {
			
			noticeRelative.setVisibility(View.INVISIBLE);
			cover.setVisibility(View.INVISIBLE);
			
			noticeRelative.startAnimation(aaOut);
			cover.startAnimation(aaOut);
		}
	}

	@Override
	public int getBgResourceId() {

		return R.drawable.main_bg;
	}

//////////////////// Classes.
	
	public class PagerAdapterForProducts extends PagerAdapter {
		
		@Override
		public int getCount() {
			
			return products.size();
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, final int position) {

			final String imageUrl = products.get(position).getProduct_images()[0];
			
			LogUtils.log("###WholesaleMainPage.instantiateItem.  position : " + position + 
					", url : " + imageUrl);
			
			final ImageView ivImage = new ImageView(mContext);
			ivImage.setScaleType(ScaleType.CENTER_CROP);
			ivImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					Bundle bundle = new Bundle();
					bundle.putSerializable("product", products.get(position));
					bundle.putBoolean("isWholesale", false);
					mActivity.showPage(CphConstants.PAGE_COMMON_PRODUCT, bundle);
				}
			});
			container.addView(ivImage);

			DownloadUtils.downloadBitmap(imageUrl, new OnBitmapDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("instantiateItem.onError." + "\nurl : " + url);
				}

				@Override
				public void onCompleted(String url, Bitmap bitmap) {

					try {
						LogUtils.log("instantiateItem.onCompleted." + "\nurl : " + url);
						ivImage.setImageBitmap(bitmap);
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
					}
				}
			});
			
			return ivImage;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

			try {
				View v = (View) object;
				container.removeView(v);
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
