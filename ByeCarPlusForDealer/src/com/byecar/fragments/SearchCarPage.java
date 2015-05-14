package com.byecar.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.byecar.byecarplusfordealer.MainActivity;
import com.byecar.byecarplusfordealer.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPAdapter;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPFragment;
import com.byecar.models.Area;
import com.byecar.models.Car;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class SearchCarPage extends BCPFragment {
	
	public final String[] DISPLACEMENT_TEXTS = new String[]{
			"0cc", "1000cc", "1300cc", "1600cc", "2000cc", "3000cc", "4000cc" , "4000cc 초과"};
	
	public final int[] DISPLACEMENTS = new int[]{
			0, 1000, 1300, 1600, 2000, 3000, 4000, -1};
	
	private SwipeRefreshLayout swipeRefreshLayout;
	private Button btnFilter;
	private RelativeLayout relativeForFilter;
	private Button btnNation;
	private Button btnMinPrice;
	private Button btnMaxPrice;
	private Button btnMinDisplacement;
	private Button btnMaxDisplacement;
	private Button btnArea1;
	private Button btnArea2;
	private EditText etCarName;
	private Button btnSearch;
	private ListView listView;
	private View noResultView;

	private boolean isAnimating;
	
	private boolean isDomestic = true;
	private int minPriceIndex = -1;
	private int maxPriceIndex = -1;
	private int minDisplacementIndex = -1;
	private int maxDisplacementIndex = -1;
	private int area1Index = -1;
	private int area2Index = -1;
	
	private Animation aIn, aOut;
	private ArrayList<PriceText> priceTexts;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.searchCarPage_titleBar);
		
		swipeRefreshLayout = (SwipeRefreshLayout) mThisView.findViewById(R.id.searchCarPage_swipe_container);
		btnFilter = (Button) mThisView.findViewById(R.id.searchCarPage_btnFilter);
		relativeForFilter = (RelativeLayout) mThisView.findViewById(R.id.searchCarPage_relativeForFilter);
		btnNation = (Button) mThisView.findViewById(R.id.searchCarPage_btnNation);
		btnMinPrice = (Button) mThisView.findViewById(R.id.searchCarPage_btnMinPrice);
		btnMaxPrice = (Button) mThisView.findViewById(R.id.searchCarPage_btnMaxPrice);
		btnMinDisplacement = (Button) mThisView.findViewById(R.id.searchCarPage_btnMinDisplacement);
		btnMaxDisplacement = (Button) mThisView.findViewById(R.id.searchCarPage_btnMaxDisplacement);
		btnArea1 = (Button) mThisView.findViewById(R.id.searchCarPage_btnArea1);
		btnArea2 = (Button) mThisView.findViewById(R.id.searchCarPage_btnArea2);
		etCarName = (EditText) mThisView.findViewById(R.id.searchCarPage_etCarName);
		btnSearch = (Button) mThisView.findViewById(R.id.searchCarPage_btnSearch);
		listView = (ListView) mThisView.findViewById(R.id.searchCarPage_listView);
		noResultView = mThisView.findViewById(R.id.searchCarPage_noResultView);
	}

	@Override
	public void setVariables() {

	}

	@Override
	public void createPage() {

		swipeRefreshLayout.setColorSchemeColors(
        		getResources().getColor(R.color.titlebar_bg_orange),
        		getResources().getColor(R.color.titlebar_bg_brown), 
        		getResources().getColor(R.color.titlebar_bg_orange), 
        		getResources().getColor(R.color.titlebar_bg_brown));
        swipeRefreshLayout.setEnabled(true);
		
		adapter = new BCPAdapter(mContext, mActivity, getActivity().getLayoutInflater(), models);
		listView.setAdapter(adapter);
		listView.setDivider(new ColorDrawable(Color.TRANSPARENT));
		listView.setDividerHeight(ResizeUtils.getSpecificLength(30));
		
		AnimationListener al = new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				
				isAnimating = true;
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {

				isAnimating = false;
			}
		};
		
		aOut = AnimationUtils.loadAnimation(mContext, R.anim.slide_out_to_top);
		aOut.setAnimationListener(al);
		
		aIn = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_from_top);
		aIn.setAnimationListener(al);
		
		//가격 구간 설정.
		priceTexts = new ArrayList<PriceText>();
		int price = 0;
		
		while(price <= 100000000) {
			
			priceTexts.add(new PriceText(price));
			
			//천만원 이하일 땐 100만원씩 추가.
			if(price < 10000000) {
				price += 1000000;
				
			//천만원 이상일 땐 500만원씩 추가.
			} else {
				price += 5000000;
			}
		}
		
		//무제한 추가.
		priceTexts.add(new PriceText(-1));
		
		btnMinPrice.setText("최저가");
		btnMaxPrice.setText("최고가");
		btnMinDisplacement.setText("최저 배기량");
		btnMaxDisplacement.setText("최고 배기량");
		btnArea1.setText("시도");
		btnArea2.setText("시군구");
	}

	@Override
	public void setListeners() {

		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {

				swipeRefreshLayout.setRefreshing(true);
				
				new Handler().postDelayed(new Runnable() {
			        @Override 
			        public void run() {
			        	
			        	refreshPage();
			        }
			    }, 2000);
			}
		});
		
		btnFilter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(isAnimating) {
					return;
				}
				if(relativeForFilter.getVisibility() == View.VISIBLE) {
					hideFilter();
				} else {
					showFilter();
				}
			}
		});

		btnNation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				ToastUtils.showToast("current : " + isDomestic);
				
				isDomestic = !isDomestic;
				
				if(isDomestic) {
					btnNation.setBackgroundResource(R.drawable.filter_car_btn_a);
				} else {
					btnNation.setBackgroundResource(R.drawable.filter_car_btn_b);
				}
			}
		});

		btnMinPrice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				selectPrice(true);
			}
		});

		btnMaxPrice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				selectPrice(false);
			}
		});
		
		btnMinDisplacement.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				selectDisplacement(true);
			}
		});
		
		btnMaxDisplacement.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				selectDisplacement(false);
			}
		});
		
		btnArea1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				selectArea1();
			}
		});
		
		btnArea2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				selectArea2();
			}
		});
		
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				hideFilter();
				search();
			}
		});
		
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				if(firstVisibleItem + visibleItemCount == totalItemCount) {
					downloadInfo();
				}
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {

			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;

		//listView.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.searchCarPage_swipe_container).getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(88);
		
		//btnFilter.
		rp = (RelativeLayout.LayoutParams) btnFilter.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(60);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.topMargin = ResizeUtils.getSpecificLength(14);
		rp.rightMargin = ResizeUtils.getSpecificLength(14);
		
		//relativeForFilter.
		rp = (RelativeLayout.LayoutParams) relativeForFilter.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(424);
		
		//btnNation.
		rp = (RelativeLayout.LayoutParams) btnNation.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(52);
		rp.topMargin = ResizeUtils.getSpecificLength(12);
		
		//btnMinPrice.
		rp = (RelativeLayout.LayoutParams) btnMinPrice.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(294);
		rp.height = ResizeUtils.getSpecificLength(52);
		rp.topMargin = ResizeUtils.getSpecificLength(16);
		
		//btnMaxPrice.
		rp = (RelativeLayout.LayoutParams) btnMaxPrice.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(294);
		rp.height = ResizeUtils.getSpecificLength(52);
		rp.topMargin = ResizeUtils.getSpecificLength(16);
		
		//btnMinDisplacement.
		rp = (RelativeLayout.LayoutParams) btnMinDisplacement.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(294);
		rp.height = ResizeUtils.getSpecificLength(52);
		rp.topMargin = ResizeUtils.getSpecificLength(16);
		
		//btnMaxDisplacement.
		rp = (RelativeLayout.LayoutParams) btnMaxDisplacement.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(294);
		rp.height = ResizeUtils.getSpecificLength(52);
		rp.topMargin = ResizeUtils.getSpecificLength(16);
		
		//btnArea1.
		rp = (RelativeLayout.LayoutParams) btnArea1.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(294);
		rp.height = ResizeUtils.getSpecificLength(52);
		rp.topMargin = ResizeUtils.getSpecificLength(16);
		
		//btnArea2.
		rp = (RelativeLayout.LayoutParams) btnArea2.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(294);
		rp.height = ResizeUtils.getSpecificLength(52);
		rp.topMargin = ResizeUtils.getSpecificLength(16);
		
		//etCarName.
		rp = (RelativeLayout.LayoutParams) etCarName.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(52);
		rp.topMargin = ResizeUtils.getSpecificLength(16);
		
		//btnSearch.
		rp = (RelativeLayout.LayoutParams) btnSearch.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(52);
		rp.topMargin = ResizeUtils.getSpecificLength(16);
		
		//noResultView.
		rp = (RelativeLayout.LayoutParams) noResultView.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(218);
		rp.height = ResizeUtils.getSpecificLength(248);
		rp.topMargin = ResizeUtils.getSpecificLength(88);
		
		FontUtils.setFontSize(btnMinPrice, 24);
		FontUtils.setFontSize(btnMaxPrice, 24);
		FontUtils.setFontSize(btnMinDisplacement, 24);
		FontUtils.setFontSize(btnMaxDisplacement, 24);
		FontUtils.setFontSize(btnArea1, 24);
		FontUtils.setFontSize(btnArea2, 24);
		FontUtils.setFontSize(etCarName, 24);
		
		int p = ResizeUtils.getSpecificLength(60);
		etCarName.setPadding(p, 0, p, 0);
	}

	@Override
	public int getContentViewId() {
		
		return R.layout.fragment_common_search_car;
	}
	
	@Override
	public int getPageTitleTextResId() {

		return R.string.pageTitle_searchCar;
	}
	
	@Override
	public boolean parseJSON(JSONObject objJSON) {
		
		try {
			int size = 0;
			JSONArray arJSON = objJSON.getJSONArray("onsalecars");
			size = arJSON.length();
			
			for(int i=0; i<size; i++) {
				Car car = new Car(arJSON.getJSONObject(i));
				car.setItemCode(BCPConstants.ITEM_CAR_DEALER);
				models.add(car);
			}
			
			adapter.notifyDataSetChanged();

			if(size < NUMBER_OF_LISTITEMS) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		} finally {
			swipeRefreshLayout.setRefreshing(false);
		}
		
		return false;
	}

	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onBackPressed() {
		
		if(relativeForFilter.getVisibility() == View.VISIBLE) {
			hideFilter();
			return true;
		}
		
		return false;
	}

	@Override
	public int getRootViewResId() {

		return R.id.searchCarPage_mainLayout;
	}
	
//////////////////// Custom methods.
	
	public void showFilter() {

		relativeForFilter.setVisibility(View.VISIBLE);
		relativeForFilter.startAnimation(aIn);
		
		btnFilter.setBackgroundResource(R.drawable.filter_arrow_up);
	}
	
	public void hideFilter() {
		
		relativeForFilter.setVisibility(View.INVISIBLE);
		relativeForFilter.startAnimation(aOut);
		
		btnFilter.setBackgroundResource(R.drawable.filter_arrow_down);
	}

	public void selectPrice(final boolean isMinPrice) {

		int size = priceTexts.size();
		final String[] PRICE_TEXTS = new String[size];
		
		for(int i=0; i<size; i++) {
			PRICE_TEXTS[i] = priceTexts.get(i).getPriceText();
		}
		
		mActivity.showSelectDialog("가격을 선택해주세요.", PRICE_TEXTS, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				if(isMinPrice) {

					//선택된 값이 최고가보다 높은 경우 경고 메세지 출력.
					if(maxPriceIndex != -1 && maxPriceIndex < which) {
						ToastUtils.showToast(R.string.minPriceCannotOverMaxPrice);
					} else {
						minPriceIndex = which;
						btnMinPrice.setText(PRICE_TEXTS[which]);
					}
				} else {
					
					//선택된 값이 최저가보다 낮은 경우 경고 메세지 출력.
					if(minPriceIndex != -1 && minPriceIndex > which) {
						ToastUtils.showToast(R.string.maxPriceCannotUnderMinPrice);
					} else {
						maxPriceIndex = which;
						btnMaxPrice.setText(PRICE_TEXTS[which]);
					}
				}
			}
		});
	}
	
	public void selectDisplacement(final boolean isMinDisplacement) {
		
		mActivity.showSelectDialog("가격을 선택해주세요.", DISPLACEMENT_TEXTS, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if(isMinDisplacement) {

					//선택된 값이 최고 배기량보다 높은 경우 경고 메세지 출력.
					if(maxDisplacementIndex != -1 && maxDisplacementIndex < which) {
						ToastUtils.showToast(R.string.minDisplacementCannotOverMaxDisplacement);
					} else {
						minDisplacementIndex = which;
						btnMinDisplacement.setText(DISPLACEMENT_TEXTS[which]);
					}
				} else {
					
					//선택된 값이 최저 배기량보다 낮은 경우 경고 메세지 출력.
					if(minDisplacementIndex != -1 && minDisplacementIndex > which) {
						ToastUtils.showToast(R.string.maxDisplacementCannotUnderMinDisplacement);
					} else {
						maxDisplacementIndex = which;
						btnMaxDisplacement.setText(DISPLACEMENT_TEXTS[which]);
					}
				}
			}
		});
	}

	public void selectArea1() {
		
		try {
			int size = MainActivity.area.getAreaSet().size();
			final String[] texts = new String[size];
			for(int i=0; i<size; i++) {
				texts[i] = MainActivity.area.getAreaSet().get(i).getName();
			}
			
			mActivity.showSelectDialog("지역을 선택해주세요.", texts, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {

					area1Index = which;
					area2Index = -1;
					btnArea1.setText(texts[which]);
					btnArea2.setText("시군구");
				}
			});
		} catch (Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void selectArea2() {
		
		try {
			if(area1Index == -1) {
				ToastUtils.showToast(R.string.selectArea1First);
				return;
			}
			
			ArrayList<Area> areaSet = MainActivity.area.getAreaSet().get(area1Index).getAreaSet();
			int size = areaSet.size();
			final String[] texts = new String[size];
			
			for(int i=0; i<size; i++) {
				texts[i] = areaSet.get(i).getName();
			}
			
			mActivity.showSelectDialog("지역을 선택해주세요.", texts, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {

					area2Index = which;
					btnArea2.setText(texts[which]);
				}
			});
		} catch (Exception e) {
			LogUtils.trace(e);
		}
	}

	public void search() {

		try {
			url = BCPAPIs.CAR_DEALER_LIST_URL;
			
			//국산/수입.
			url += "?brand_from=" + StringUtils.getUrlEncodedString(isDomestic?"한국":"수입");

			int size = priceTexts.size();
			final long[] PRICES = new long[size];
			
			for(int i=0; i<size; i++) {
				PRICES[i] = priceTexts.get(i).getPrice();
			}
			
			//최저가.
			if(minPriceIndex != -1 && PRICES[minPriceIndex] != -1) {
				
				url += "&price_min=" + PRICES[minPriceIndex];
			}
			
			//최고가.
			if(maxPriceIndex != -1 && PRICES[maxPriceIndex] != -1) {
				url += "&price_max=" + PRICES[maxPriceIndex];
			}
			
			//최저 배기량.
			if(minDisplacementIndex != -1 && DISPLACEMENTS[minDisplacementIndex] != -1) {
				url += "&displacement_min=" + DISPLACEMENTS[minDisplacementIndex];
			}
			
			//최고 배기량.
			if(maxDisplacementIndex != -1 && DISPLACEMENTS[maxDisplacementIndex] != -1) {
				url += "&displacement_max=" + DISPLACEMENTS[maxDisplacementIndex];
			}
			
			//시도.
			if(area1Index != -1) {
				url += "&sido=" + StringUtils.getUrlEncodedString(btnArea1.getText().toString());
			}
			
			//시군구.
			if(area2Index != -1) {
				url += "&gugun=" + StringUtils.getUrlEncodedString(btnArea2.getText().toString());
			}
			
			//차량명.
			if(etCarName.length() != 0) {
				url += "&keyword=" + StringUtils.getUrlEncodedString(etCarName);
			}

			refreshPage();
			
		} catch (Exception e) {
			LogUtils.trace(e);
		}
	}

//////////////////// Classes.
	
	public class PriceText {
		
		private long price;
		private String priceText;
		
		public PriceText(long price) {
			this.price = price;
			
			if(price == -1) {
				priceText = "무제한";
			} else if(price == 0) {
				priceText = "0원";
			} else if(price > 100000000) {
				priceText = (price / 100000000) + "억원";
			} else {
				priceText = (price / 10000) + "만원";
			}
		}
		
		public long getPrice() {
			
			return price;
		}
		
		public String getPriceText() {
			
			return priceText;
		}
	}
}
