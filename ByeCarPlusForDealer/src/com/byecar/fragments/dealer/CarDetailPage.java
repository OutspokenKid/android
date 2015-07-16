package com.byecar.fragments.dealer;

import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplusfordealer.MainActivity;
import com.byecar.byecarplusfordealer.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPDownloadUtils;
import com.byecar.classes.BCPFragment;
import com.byecar.classes.ImagePagerAdapter;
import com.byecar.classes.ImagePagerAdapter.OnPagerItemClickedListener;
import com.byecar.models.Car;
import com.byecar.models.Dealer;
import com.byecar.views.CarInfoView;
import com.byecar.views.DealerView;
import com.byecar.views.ReviewView;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.IntentUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.TimerUtils;
import com.outspoken_kid.utils.TimerUtils.OnTimeChangedListener;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.OffsetScrollView;
import com.outspoken_kid.views.OffsetScrollView.OnScrollChangedListener;
import com.outspoken_kid.views.PageNavigatorView;

public class CarDetailPage extends BCPFragment {

	private static final int MENU_BIDDING = 0;
	private static final int MENU_REVIEW = 1;
	private static final int MENU_INFO = 2;
	private static final int MENU_CHECK = 3;
	private static final int MENU_ACCIDENT = 4;
	private static final int MENU_OPTION = 5;
	private static final int MENU_DESC = 6;
	
	private int id;
	private Car car;
	
	private OffsetScrollView scrollView;

	private ViewPager viewPager;
	private PageNavigatorView pageNavigator;
	private View auctionIcon;
	private CarInfoView carInfoView;
	private RelativeLayout relativeForType;
	private DealerView[] dealerViews = new DealerView[3];
	
	private RelativeLayout relativeForBidding;
	private View headerForBidding;
	private RelativeLayout relativeForBidding2;
	private TextView tvPrice;
	private Button[] btnPrices = new Button[4];
	private Button btnClear;
	private Button btnBidding;
	private View footerForBidding;
	
	private RelativeLayout relativeForReview;
	private View headerForReview;
	private View arrowForReview;
	private LinearLayout linearForReview;
	private ReviewView review;
	private ReviewView reply;
	private View footerForReview;
	
	private View headerForInfo;
	private View arrowForInfo;
	private RelativeLayout relativeForInfo;
	private View[] detailInfoViews = new View[4];
	private TextView[] detailInfoTextViews = new TextView[7];
	private Button btnHistory;

	private RelativeLayout relativeForCheck;
	private View headerForCheck;
	private View arrowForCheck;
	private ImageView ivCheck;
	
	private RelativeLayout relativeForAccident;
	private View headerForAccident;
	private View arrowForAccident;
	private View bgForAccident;
	private TextView tvAccident;
	private Button btnAccident;
	
	private View headerForOption;
	private View arrowForOption;
	private RelativeLayout relativeForOption;
	private LinearLayout[] linearForOptions = new LinearLayout[3];
	private RelativeLayout relativeForOption2;
	
	private View headerForDescription;
	private View arrowForDescription;
	private TextView tvDescription;
	private View footerForDescription;
	
	private Button btnPayment;
	private Button btnCall;
	private View buttonBg;
	
	private Button btnDelete;
	private Button btnEdit;
	
	private ImagePagerAdapter imagePagerAdapter;
	
	private int type;
	private int scrollOffset;
	private int standardLength;
	private float diff;
	
	private long biddingPrice;
	
	private OnTimeChangedListener onTimeChangedListener;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.carDetailPage_titleBar);
		
		scrollView = (OffsetScrollView) mThisView.findViewById(R.id.carDetailPage_scrollView);
		
		viewPager = (ViewPager) mThisView.findViewById(R.id.carDetailPage_viewPager);
		pageNavigator = (PageNavigatorView) mThisView.findViewById(R.id.carDetailPage_pageNavigator);
		auctionIcon = mThisView.findViewById(R.id.carDetailPage_auctionIcon);
		carInfoView = (CarInfoView) mThisView.findViewById(R.id.carDetailPage_carInfoView);
		relativeForType = (RelativeLayout) mThisView.findViewById(R.id.carDetailPage_relativeForType);
		
		relativeForBidding = (RelativeLayout) mThisView.findViewById(R.id.carDetailPage_relativeForBidding);
		headerForBidding = mThisView.findViewById(R.id.carDetailPage_headerForBidding);
		relativeForBidding2 = (RelativeLayout) mThisView.findViewById(R.id.carDetailPage_relativeForBidding2);
		tvPrice = (TextView) mThisView.findViewById(R.id.carDetailPage_tvPrice);
		btnPrices[0] = (Button) mThisView.findViewById(R.id.carDetailPage_btnPrice1);
		btnPrices[1] = (Button) mThisView.findViewById(R.id.carDetailPage_btnPrice2);
		btnPrices[2] = (Button) mThisView.findViewById(R.id.carDetailPage_btnPrice3);
		btnPrices[3] = (Button) mThisView.findViewById(R.id.carDetailPage_btnPrice4);
		btnClear = (Button) mThisView.findViewById(R.id.carDetailPage_btnClear);
		btnBidding = (Button) mThisView.findViewById(R.id.carDetailPage_btnBidding);
		footerForBidding = mThisView.findViewById(R.id.carDetailPage_footerForBidding);
		
		relativeForReview = (RelativeLayout) mThisView.findViewById(R.id.carDetailPage_relativeForReview);
		headerForReview = mThisView.findViewById(R.id.carDetailPage_headerForReview);
		arrowForReview = mThisView.findViewById(R.id.carDetailPage_arrowForReview);
		linearForReview = (LinearLayout) mThisView.findViewById(R.id.carDetailPage_linearForReview);
		review = (ReviewView) mThisView.findViewById(R.id.carDetailPage_review);
		reply = (ReviewView) mThisView.findViewById(R.id.carDetailPage_reply);
		footerForReview = mThisView.findViewById(R.id.carDetailPage_footerForReview);
		
		headerForInfo = mThisView.findViewById(R.id.carDetailPage_headerForInfo);
		arrowForInfo = mThisView.findViewById(R.id.carDetailPage_arrowForInfo);
		relativeForInfo = (RelativeLayout) mThisView.findViewById(R.id.carDetailPage_relativeForInfo);
		detailInfoViews[0] = mThisView.findViewById(R.id.carDetailPage_detailInfoView1);
		detailInfoViews[1] = mThisView.findViewById(R.id.carDetailPage_detailInfoView2);
		detailInfoViews[2] = mThisView.findViewById(R.id.carDetailPage_detailInfoView3);
		detailInfoViews[3] = mThisView.findViewById(R.id.carDetailPage_detailInfoView4);
		detailInfoTextViews[0] = (TextView) mThisView.findViewById(R.id.carDetailPage_detailInfoTextView1);
		detailInfoTextViews[1] = (TextView) mThisView.findViewById(R.id.carDetailPage_detailInfoTextView2);
		detailInfoTextViews[2] = (TextView) mThisView.findViewById(R.id.carDetailPage_detailInfoTextView3);
		detailInfoTextViews[3] = (TextView) mThisView.findViewById(R.id.carDetailPage_detailInfoTextView4);
		detailInfoTextViews[4] = (TextView) mThisView.findViewById(R.id.carDetailPage_detailInfoTextView5);
		detailInfoTextViews[5] = (TextView) mThisView.findViewById(R.id.carDetailPage_detailInfoTextView6);
		detailInfoTextViews[6] = (TextView) mThisView.findViewById(R.id.carDetailPage_detailInfoTextView7);
		btnHistory = (Button) mThisView.findViewById(R.id.carDetailPage_btnHistory);

		relativeForCheck = (RelativeLayout) mThisView.findViewById(R.id.carDetailPage_relativeForCheck);
		headerForCheck = mThisView.findViewById(R.id.carDetailPage_headerForCheck);
		arrowForCheck = mThisView.findViewById(R.id.carDetailPage_arrowForCheck);
		ivCheck = (ImageView) mThisView.findViewById(R.id.carDetailPage_ivCheck);
		
		relativeForAccident = (RelativeLayout) mThisView.findViewById(R.id.carDetailPage_relativeForAccident);
		headerForAccident = mThisView.findViewById(R.id.carDetailPage_headerForAccident);
		arrowForAccident = mThisView.findViewById(R.id.carDetailPage_arrowForAccident);
		bgForAccident = mThisView.findViewById(R.id.carDetailPage_bgForAccident);
		tvAccident = (TextView) mThisView.findViewById(R.id.carDetailPage_tvAccident);
		btnAccident = (Button) mThisView.findViewById(R.id.carDetailPage_btnAccident);
		
		headerForOption = mThisView.findViewById(R.id.carDetailPage_headerForOption);
		arrowForOption = mThisView.findViewById(R.id.carDetailPage_arrowForOption);
		relativeForOption = (RelativeLayout) mThisView.findViewById(R.id.carDetailPage_relativeForOption);
		linearForOptions[0] = (LinearLayout) mThisView.findViewById(R.id.carDetailPage_linearForOption1);
		linearForOptions[1] = (LinearLayout) mThisView.findViewById(R.id.carDetailPage_linearForOption2);
		linearForOptions[2] = (LinearLayout) mThisView.findViewById(R.id.carDetailPage_linearForOption3);
		relativeForOption2 = (RelativeLayout) mThisView.findViewById(R.id.carDetailPage_relativeForOption2);
		
		headerForDescription = mThisView.findViewById(R.id.carDetailPage_headerForDescription);
		arrowForDescription = mThisView.findViewById(R.id.carDetailPage_arrowForDescription);
		tvDescription = (TextView) mThisView.findViewById(R.id.carDetailPage_tvDescription);
		footerForDescription = mThisView.findViewById(R.id.carDetailPage_footerForDescription);
		
		btnPayment = (Button) mThisView.findViewById(R.id.carDetailPage_btnPayment);
		btnCall = (Button) mThisView.findViewById(R.id.carDetailPage_btnCall);
		buttonBg = mThisView.findViewById(R.id.carDetailPage_buttonBg);
		
		btnDelete = (Button) mThisView.findViewById(R.id.carDetailPage_btnDelete);
		btnEdit = (Button) mThisView.findViewById(R.id.carDetailPage_btnEdit);
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
			
			if(getArguments().containsKey("type")) {
				this.type = getArguments().getInt("type");
			}
			
			if(type != Car.TYPE_BID && type != Car.TYPE_DEALER && type != Car.TYPE_DIRECT) {
				closePage(R.string.failToLoadCarInfo);
			}
		}
	}

	@Override
	public void createPage() {

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

		headerForBidding.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				changeMenuOpenStatus(MENU_BIDDING);
			}
		});
		
		headerForReview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				changeMenuOpenStatus(MENU_REVIEW);
			}
		});
		
		headerForInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				changeMenuOpenStatus(MENU_INFO);
			}
		});
		
		headerForCheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				changeMenuOpenStatus(MENU_CHECK);
			}
		});
		
		headerForAccident.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				changeMenuOpenStatus(MENU_ACCIDENT);
			}
		});
		
		headerForOption.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				changeMenuOpenStatus(MENU_OPTION);
			}
		});
		
		headerForDescription.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				changeMenuOpenStatus(MENU_DESC);
			}
		});
		
		btnPayment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				checkPayable();
			}
		});
		
		btnCall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				call();
			}
		});
	
		btnEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putInt("carType", Car.TYPE_DEALER);
				bundle.putBoolean("forDealer", true);
				bundle.putSerializable("car", car);
				mActivity.showPage(BCPConstants.PAGE_CAR_REGISTRATION, bundle);
			}
		});
		
		btnDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showAlertDialog(R.string.delete, R.string.wannaDelete,
						R.string.confirm, R.string.cancel, 
						new DialogInterface.OnClickListener() {
					
							@Override
							public void onClick(DialogInterface dialog, int which) {

								delete();
							}
						}, null);
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
		rp.width = ResizeUtils.getSpecificLength(161);
		rp.height = ResizeUtils.getSpecificLength(51);
		rp.leftMargin = ResizeUtils.getSpecificLength(12);
		rp.bottomMargin = ResizeUtils.getSpecificLength(18);
		
		//headerForInfo.
		rp = (RelativeLayout.LayoutParams) headerForInfo.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(68);
		rp.topMargin = ResizeUtils.getSpecificLength(20);
		
		//arrowForInfo.
		rp = (RelativeLayout.LayoutParams) arrowForInfo.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(33);
		rp.height = ResizeUtils.getSpecificLength(33);
		rp.topMargin = ResizeUtils.getSpecificLength(18);
		rp.rightMargin = ResizeUtils.getSpecificLength(18);
		
		//relativeForInfo.
		rp = (RelativeLayout.LayoutParams) relativeForInfo.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(359);
		
		//detailInfoViews.
		for(int i=0; i<detailInfoViews.length; i++) {
			rp = (RelativeLayout.LayoutParams) detailInfoViews[i].getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(i==6?LayoutParams.WRAP_CONTENT:118);
			rp.height = ResizeUtils.getSpecificLength(44);
			rp.leftMargin = ResizeUtils.getSpecificLength(i==0?26:30);
			rp.topMargin = ResizeUtils.getSpecificLength(24);
		}
		
		//btnHistory.
		rp = (RelativeLayout.LayoutParams) btnHistory.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(416);
		rp.height = ResizeUtils.getSpecificLength(64);
		rp.topMargin = ResizeUtils.getSpecificLength(16);
		
		//footerForInfo.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.carDetailPage_footerForInfo).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(20);

		//relativeForCheck.
		rp = (RelativeLayout.LayoutParams) relativeForCheck.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.topMargin = ResizeUtils.getSpecificLength(20);

		//headerForCheck.
		rp = (RelativeLayout.LayoutParams) headerForCheck.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(68);

		//arrowForCheck.
		rp = (RelativeLayout.LayoutParams) arrowForCheck.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(33);
		rp.height = ResizeUtils.getSpecificLength(33);
		rp.topMargin = ResizeUtils.getSpecificLength(18);
		rp.rightMargin = ResizeUtils.getSpecificLength(18);

		int p = ResizeUtils.getSpecificLength(30);
		
		//ivCheck.
		rp = (RelativeLayout.LayoutParams) ivCheck.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(460);
		ivCheck.setPadding(p, p, p, p);
		
		//footerForCheck.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.carDetailPage_footerForCheck).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(20);
		
		//relativeForAccident.
		rp = (RelativeLayout.LayoutParams) relativeForAccident.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.topMargin = ResizeUtils.getSpecificLength(20);

		//headerForAccident.
		rp = (RelativeLayout.LayoutParams) headerForAccident.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(68);

		//arrowForAccident.
		rp = (RelativeLayout.LayoutParams) arrowForAccident.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(33);
		rp.height = ResizeUtils.getSpecificLength(33);
		rp.topMargin = ResizeUtils.getSpecificLength(18);
		rp.rightMargin = ResizeUtils.getSpecificLength(18);

		//bgForAccident.
		rp = (RelativeLayout.LayoutParams) bgForAccident.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(220);
		
		//tvAccident.
		rp = (RelativeLayout.LayoutParams) tvAccident.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(120);
		
		//btnAccident.
		rp = (RelativeLayout.LayoutParams) btnAccident.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(412);
		rp.height = ResizeUtils.getSpecificLength(60);		
		
		//footerForAccident.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.carDetailPage_footerForAccident).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(20);
		
		//headerForOption.
		rp = (RelativeLayout.LayoutParams) headerForOption.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(68);
		rp.topMargin = ResizeUtils.getSpecificLength(20);
		
		//arrowForOption.
		rp = (RelativeLayout.LayoutParams) arrowForOption.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(33);
		rp.height = ResizeUtils.getSpecificLength(33);
		rp.topMargin = ResizeUtils.getSpecificLength(18);
		rp.rightMargin = ResizeUtils.getSpecificLength(18);
		
		//relativeForOption.
		rp = (RelativeLayout.LayoutParams) relativeForOption.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		relativeForOption.setPadding(0, 0, 0, ResizeUtils.getSpecificLength(46));
		
		//linearForOptions.
		for(int i=0; i<3; i++) {
			rp = (RelativeLayout.LayoutParams) linearForOptions[i].getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(203);
		}
		
		//carDetailPage_optionTitle1
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.carDetailPage_optionTitle1).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(578);
		rp.height = ResizeUtils.getSpecificLength(43);
		rp.topMargin = ResizeUtils.getSpecificLength(46);
		
		//footerForOption.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.carDetailPage_footerForOption).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(20);

		//headerForDescription.
		rp = (RelativeLayout.LayoutParams) headerForDescription.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(68);
		rp.topMargin = ResizeUtils.getSpecificLength(20);
		
		//arrowForDescription.
		rp = (RelativeLayout.LayoutParams) arrowForDescription.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(33);
		rp.height = ResizeUtils.getSpecificLength(33);
		rp.topMargin = ResizeUtils.getSpecificLength(18);
		rp.rightMargin = ResizeUtils.getSpecificLength(18);
		
		//tvDescription.
		rp = (RelativeLayout.LayoutParams) tvDescription.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		
		//footerForDescription.
		rp = (RelativeLayout.LayoutParams) footerForDescription.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(20);
		
		//btnDelete.
		rp = (RelativeLayout.LayoutParams) btnDelete.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(60);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.topMargin = ResizeUtils.getSpecificLength(16);
		rp.rightMargin = ResizeUtils.getSpecificLength(14);
		
		//btnEdit.
		rp = (RelativeLayout.LayoutParams) btnEdit.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(60);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.rightMargin = ResizeUtils.getSpecificLength(8);
		
		FontUtils.setFontSize(tvAccident, 20);
		
		for(int i=0; i<detailInfoTextViews.length; i++) {
			FontUtils.setFontSize(detailInfoTextViews[i], 20);
		}
		
		FontUtils.setFontSize(tvDescription, 20);
		FontUtils.setFontStyle(tvDescription, FontUtils.BOLD);
		tvDescription.setPadding(
				ResizeUtils.getSpecificLength(20),
				ResizeUtils.getSpecificLength(20), 
				ResizeUtils.getSpecificLength(20), 
				ResizeUtils.getSpecificLength(20));
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_car_detail;
	}
	
	@Override
	public int getPageTitleTextResId() {

		return R.string.pageTitle_carDetail;
	}
	
	@Override
	public int getRootViewResId() {

		return R.id.carDetailPage_mainLayout;
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
	
////////////////////Custom methods.

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

		String url = null;
		
		switch(type) {
		
		case Car.TYPE_BID:
			url = BCPAPIs.CAR_BID_SHOW_URL;
			break;
			
		case Car.TYPE_DEALER:
			url = BCPAPIs.CAR_DEALER_SHOW_URL;
			break;
		}
		
		if(url == null) {
			return;
		}
		
		if(car == null) {
			url += "?onsalecar_id=" + id;
		} else if(car != null) {
			url += "?onsalecar_id=" + car.getId();
		}
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("CarDetailPage.onError." + "\nurl : " + url);
				closePage(R.string.failToLoadCarInfo);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("CarDetailPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					Car newCar = new Car(objJSON.getJSONObject("onsalecar"));
					
					if(car == null) {
						car = newCar;
					} else {
						car.copyValuesFromNewItem(newCar);
					}

					setAllInfos();
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

	public void setAllInfos() {
		
		try {
			setRelativeForType();
			setMainCarInfo();
			setBiddingViews();
			setReviews();
			setInfos();
			setAccident();
			setOptions();
			setCheck();
			setDescription();
			setButtomButtons();
			setTitleBarButtons();
		} catch (Exception e) {
			LogUtils.trace(e);
			closePage(R.string.failToLoadCarInfo);
		} catch (Error e) {
			LogUtils.trace(e);
			closePage(R.string.failToLoadCarInfo);
		}
	}
	
	public void setMainCarInfo() {

		try {
			if(car.getStatus() == -1) {
				closePage(R.string.holdOffByAdmin);
			}
			
//			//딜러에게 전화 걸기를 위해서.
//			MainActivity.dealerPhoneNumber = car.getDealer_phone_number();

			setAuctionButton(car);
			
			if(car.getType() == Car.TYPE_BID) {
				setOnTimerListener();
				TimerUtils.addOnTimeChangedListener(onTimeChangedListener);
			}
			
			carInfoView.setDetail();
			carInfoView.setCarInfo(car);
			
			imagePagerAdapter.setArrayList(car.getImages());
			viewPager.getAdapter().notifyDataSetChanged();
			viewPager.setCurrentItem(0);
			
			pageNavigator.setSize(car.getImages().size());
			pageNavigator.setEmptyOffCircle();
			pageNavigator.invalidate();
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	public void setRelativeForType() {

		if(car == null) {
			return;
		}
		
		try {
			relativeForType.removeAllViews();
			
			switch(type) {
			
			case Car.TYPE_BID:
				addViewsForBidding_dealerViewOnly();
				break;
				
			case Car.TYPE_DEALER:
				addViewsForDealer();
				break;
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void addViewsForBidding_dealerViewOnly() {

		if(car.getBids_cnt() == 0) {
			addNoDealerView();
			return;
		}
		
		RelativeLayout.LayoutParams rp = (RelativeLayout.LayoutParams) relativeForType.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(350);
		relativeForType.setBackgroundResource(R.drawable.main_bid_frame1);
		
		addDealerViews();
	}
	
	public void addNoDealerView() {
		
		RelativeLayout.LayoutParams rp = (RelativeLayout.LayoutParams) relativeForType.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(350);
		relativeForType.setBackgroundResource(R.drawable.main_bid_frame1);

		View noDealerView = new View(mContext);
		ResizeUtils.viewResizeForRelative(218, 248, noDealerView, 
				new int[]{RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.CENTER_HORIZONTAL}, 
				new int[]{0, 0}, new int[]{0, 65, 0, 0});
		noDealerView.setBackgroundResource(R.drawable.detail_no_one);
		relativeForType.addView(noDealerView);
	}
	
	public void addDealerViews() {

		RelativeLayout.LayoutParams rp = null;
		int width = ResizeUtils.getSpecificLength(184);
		int height = ResizeUtils.getSpecificLength(251);
		int topMargin = ResizeUtils.getSpecificLength(72);
		
		int size = dealerViews.length;
		for(int i=0; i<size; i++) {
			dealerViews[i] = new DealerView(mContext);
			relativeForType.addView(dealerViews[i]);
			
			rp = new RelativeLayout.LayoutParams(width, height);
			rp.topMargin = topMargin;
			
			switch(i) {
			
			case 0:
				rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				rp.leftMargin = ResizeUtils.getSpecificLength(24);
				break;
				
			case 1:
				rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
				break;
				
			case 2:
				rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				rp.rightMargin = ResizeUtils.getSpecificLength(24);
				break;
			}
			
			dealerViews[i].setLayoutParams(rp);
			dealerViews[i].setIndex(i);
			dealerViews[i].initInfos();
		}
		
		size = car.getBids().size();
		for(int i=0; i<size; i++) {
			dealerViews[i].setDealerInfo(car.getBids().get(i), i);

			if(car.getDealer_id() == car.getBids().get(i).getDealer_id()) {
				dealerViews[i].setSelectedDealer();
			}
			
			final int INDEX = i;
			dealerViews[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					Bundle bundle = new Bundle();
					bundle.putInt("dealer_id", car.getBids().get(INDEX).getDealer_id());
					mActivity.showPage(BCPConstants.PAGE_DEALER, bundle);
				}
			});
		}
	}
	
	public void addViewsForDealer() {

		if(car == null) {
			return;
		}
		
		RelativeLayout.LayoutParams rp = (RelativeLayout.LayoutParams) relativeForType.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(402);
		relativeForType.setBackgroundResource(R.drawable.buy_dealer_frame);
		
		//ivImage.
		final ImageView ivImage = new ImageView(mContext);
		ResizeUtils.viewResizeForRelative(129, 129, ivImage, null, null, new int[]{50, 100, 0, 0});
		ivImage.setScaleType(ScaleType.CENTER_CROP);
		ivImage.setBackgroundResource(R.drawable.detail_default);
		relativeForType.addView(ivImage);
		
		ivImage.setTag(car.getDealer_profile_img_url());
		BCPDownloadUtils.downloadBitmap(car.getDealer_profile_img_url(), new OnBitmapDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("CarDetailPage.onError." + "\nurl : " + url);
			}

			@Override
			public void onCompleted(String url, Bitmap bitmap) {

				try {
					if(bitmap != null && !bitmap.isRecycled() && ivImage != null) {
						ivImage.setImageBitmap(bitmap);
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		}, 100);
		
		//cover.
		View cover = new View(mContext);
		ResizeUtils.viewResizeForRelative(129, 129, cover, null, null, new int[]{50, 100, 0, 0});
		cover.setBackgroundResource(R.drawable.buy_detail_cover);
		relativeForType.addView(cover);
		
		//tvDealerName.
		TextView tvDealerName = new TextView(mContext);
		ResizeUtils.viewResizeForRelative(150, 100, tvDealerName, null, null, new int[]{190, 115, 0, 0});
		tvDealerName.setGravity(Gravity.CENTER);
		tvDealerName.setTextColor(getResources().getColor(R.color.new_color_text_darkgray));
		FontUtils.setFontSize(tvDealerName, 32);
		FontUtils.setFontStyle(tvDealerName, FontUtils.BOLD);
		relativeForType.addView(tvDealerName);
		
		tvDealerName.setText(car.getDealer_name());
		
		//gradeBadge.
		View gradeBadge = new View(mContext);
		ResizeUtils.viewResizeForRelative(229, 53, gradeBadge, null, null, new int[]{345, 138, 0, 0});
		relativeForType.addView(gradeBadge);
		
		switch(car.getDealer_level()) {
		
		case Dealer.LEVEL_FRESH_MAN:
			gradeBadge.setBackgroundResource(R.drawable.buy_grade4);
			break;
			
		case Dealer.LEVEL_NORAML_DEALER:
			gradeBadge.setBackgroundResource(R.drawable.buy_grade3);
			break;
			
		case Dealer.LEVEL_SUPERB_DEALER:
			gradeBadge.setBackgroundResource(R.drawable.buy_grade2);
			break;
			
		case Dealer.LEVEL_POWER_DEALER:
			gradeBadge.setBackgroundResource(R.drawable.buy_grade1);
			break;
		}
		
		//btnDealerPage.
		Button btnDealerPage = new Button(mContext);
		ResizeUtils.viewResizeForRelative(416, 66, btnDealerPage, 
				new int[]{RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.ALIGN_PARENT_BOTTOM}, 
				new int[]{0, 0}, 
				new int[]{0, 0, 0, 40});
		btnDealerPage.setBackgroundResource(R.drawable.buy_dealer_btn);
		relativeForType.addView(btnDealerPage);
		
		btnDealerPage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putInt("dealer_id", car.getDealer_id());
				mActivity.showPage(BCPConstants.PAGE_DEALER, bundle);
			}
		});
	}
	
	public void setBiddingViews() {
		
		try {
			if(car.getType() == Car.TYPE_BID
					&& car.getStatus() == Car.STATUS_BIDDING) {
				relativeForBidding.setVisibility(View.VISIBLE);
				
				RelativeLayout.LayoutParams rp = null;
				
				rp = (RelativeLayout.LayoutParams) relativeForBidding.getLayoutParams();
				rp.width = ResizeUtils.getSpecificLength(608);
				rp.height = LayoutParams.WRAP_CONTENT;
				rp.topMargin = ResizeUtils.getSpecificLength(20);
				
				rp = (RelativeLayout.LayoutParams) headerForBidding.getLayoutParams();
				rp.height = ResizeUtils.getSpecificLength(68);
				
				rp = (RelativeLayout.LayoutParams) relativeForBidding2.getLayoutParams();
				rp.width = ResizeUtils.getSpecificLength(608);
				rp.height = ResizeUtils.getSpecificLength(291);

				rp = (RelativeLayout.LayoutParams) tvPrice.getLayoutParams();
				rp.width = ResizeUtils.getSpecificLength(358);
				rp.height = ResizeUtils.getSpecificLength(147);
				rp.leftMargin = ResizeUtils.getSpecificLength(23);
				rp.topMargin = ResizeUtils.getSpecificLength(23);
				FontUtils.setFontSize(tvPrice, 60);
				FontUtils.setFontStyle(tvPrice, FontUtils.BOLD);
				
				for(int i=0; i<btnPrices.length; i++) {
					
					rp = (RelativeLayout.LayoutParams) btnPrices[i].getLayoutParams();
					rp.width = ResizeUtils.getSpecificLength(84);
					rp.height = ResizeUtils.getSpecificLength(64);
					
					if(i == 2) {
						rp.topMargin = ResizeUtils.getSpecificLength(19);
					} else {
						rp.leftMargin = ResizeUtils.getSpecificLength(19);
					}
					
					FontUtils.setFontSize(btnPrices[i], 24);
					
					final int INDEX = i;
					btnPrices[i].setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View view) {

							long newPrice = biddingPrice;
							
							switch(INDEX) {
							
							case 0:
								newPrice += 1000000;
								break;
								
							case 1:
								newPrice += 500000;
								break;
								
							case 2:
								newPrice += 100000;
								break;
								
							case 3:
								newPrice += 50000;
								break;
							}
							
							setBiddingPrice(newPrice);
						}
					});
				}
				
				rp = (RelativeLayout.LayoutParams) btnClear.getLayoutParams();
				rp.width = ResizeUtils.getSpecificLength(84);
				rp.height = ResizeUtils.getSpecificLength(84);
				rp.topMargin = ResizeUtils.getSpecificLength(19);
				
				btnClear.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {

						setBiddingPrice(car.getPrice());
					}
				});
				
				rp = (RelativeLayout.LayoutParams) btnBidding.getLayoutParams();
				rp.width = ResizeUtils.getSpecificLength(461);
				rp.height = ResizeUtils.getSpecificLength(84);
				rp.leftMargin = ResizeUtils.getSpecificLength(19);
				
				btnBidding.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {

						boolean needCheck = SharedPrefsUtils.getBooleanFromPrefs(BCPConstants.PREFS_PUSH, "noBiddingPush");
						
						if(needCheck) {
							bid();
						} else {
							showBiddingCheckDialog();
						}
					}
				});

				rp = (RelativeLayout.LayoutParams) footerForBidding.getLayoutParams();
				rp.width = ResizeUtils.getSpecificLength(608);
				rp.height = ResizeUtils.getSpecificLength(20);

				//biddingPrice가 car.getPrice()보다 5만원 이상 비싸면 패스.
				if(biddingPrice < car.getPrice() + 50000) {
					biddingPrice = car.getPrice();
					setBiddingPrice(biddingPrice + 50000);
				}
			} else {
				relativeForBidding.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void setBiddingPrice(long price) {

		try {
			biddingPrice = price;
			
			tvPrice.setText(null);
			FontUtils.addSpan(tvPrice, StringUtils.getFormattedNumber(price/10000), 0, 1);
			FontUtils.addSpan(tvPrice, "만원", 0, 0.4f);
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void setReviews() {
		
		try {
			//옥션 매물이고, 리뷰가 있는 경우에만 리뷰 영역 노출.
			if(car.getType() == Car.TYPE_BID
					&& car.getHas_review() == 1 && car.getReview() != null) {
				relativeForReview.setVisibility(View.VISIBLE);
				
				RelativeLayout.LayoutParams rp = null;
				
				rp = (RelativeLayout.LayoutParams) relativeForReview.getLayoutParams();
				rp.width = ResizeUtils.getSpecificLength(608);
				rp.height = LayoutParams.WRAP_CONTENT;
				rp.topMargin = ResizeUtils.getSpecificLength(20);
				
				rp = (RelativeLayout.LayoutParams) headerForReview.getLayoutParams();
				rp.height = ResizeUtils.getSpecificLength(68);

				rp = (RelativeLayout.LayoutParams) arrowForReview.getLayoutParams();
				rp.width = ResizeUtils.getSpecificLength(33);
				rp.height = ResizeUtils.getSpecificLength(33);
				rp.topMargin = ResizeUtils.getSpecificLength(18);
				rp.rightMargin = ResizeUtils.getSpecificLength(18);
				
				rp = (RelativeLayout.LayoutParams) linearForReview.getLayoutParams();
				rp.width = ResizeUtils.getSpecificLength(608);
				linearForReview.setPadding(0, ResizeUtils.getSpecificLength(18), 
						0, ResizeUtils.getSpecificLength(18));
				
				ResizeUtils.viewResize(574, LayoutParams.WRAP_CONTENT, review, 1, Gravity.CENTER_HORIZONTAL, null);
				review.setReview(car.getReview(), mActivity);
				
				if(car.getReview().getReply() != null) {
					ResizeUtils.viewResize(574, LayoutParams.WRAP_CONTENT, reply, 1, Gravity.CENTER_HORIZONTAL, new int[]{0, 16, 0, 0});
					reply.setReply(car.getReview().getReply(), car.getReview(), mActivity);
				}

				rp = (RelativeLayout.LayoutParams) footerForReview.getLayoutParams();
				rp.width = ResizeUtils.getSpecificLength(608);
				rp.height = ResizeUtils.getSpecificLength(20);
			} else {
				relativeForReview.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void setInfos() {
	
		if(car == null) {
			return;
		}
		
		try {
			//무사고.
			if(car.getHad_accident() == 2) {
				detailInfoViews[0].setVisibility(View.VISIBLE);
				btnHistory.setBackgroundResource(R.drawable.detail_no_parts);
				btnHistory.setOnClickListener(null);
				
			//유사고.
			} else if(car.getHad_accident() == 1) {
				detailInfoViews[0].setVisibility(View.INVISIBLE);
				btnHistory.setBackgroundResource(R.drawable.detail_parts_btn);
				btnHistory.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {

						mActivity.showAlertDialog(getString(R.string.changingPartsHistory), 
								car.getAccident_desc(), getString(R.string.confirm), null);
					}
				});
				
			//사고여부 모름.
			} else {
				detailInfoViews[0].setVisibility(View.INVISIBLE);
				btnHistory.setBackgroundResource(R.drawable.detail_no_parts2);
				btnHistory.setOnClickListener(null);
			}
			
			//1인 신조.
			if(car.getIs_oneman_owned() == 1) {
				detailInfoViews[1].setVisibility(View.VISIBLE);
				
			//1인 신조 아님.
			} else {
				detailInfoViews[1].setVisibility(View.INVISIBLE);
			}
			
			//4륜 구동.
			if("4WD".equals(car.getCar_wd())) {
				detailInfoViews[2].setVisibility(View.VISIBLE);
				
			//2륜 구동.
			} else {
				detailInfoViews[2].setVisibility(View.INVISIBLE);
			}
			
			//자동.
			if("auto".equals(car.getTransmission_type())) {
				detailInfoViews[3].setVisibility(View.INVISIBLE);
				
			//수동.
			} else {
				detailInfoViews[3].setVisibility(View.VISIBLE);
			}

			int padding = ResizeUtils.getSpecificLength(4);
			RelativeLayout.LayoutParams rp = null;
			
			//detailInfoTextViews.
			for(int i=0; i<detailInfoTextViews.length; i++) {
				
				//경매나 직거래라면 색상영역을 숨긴다.
				if(type != Car.TYPE_DEALER && i == 5) {
					continue;
				}
				
				rp = (RelativeLayout.LayoutParams) detailInfoTextViews[i].getLayoutParams();
				rp.width = ResizeUtils.getSpecificLength(i==detailInfoTextViews.length-1|i==3?304:152);
				rp.height = ResizeUtils.getSpecificLength(88);
				
				if(i == 0) {
					rp.topMargin = ResizeUtils.getSpecificLength(22);
				} else if(i == 4) {
					rp.topMargin = ResizeUtils.getSpecificLength(2);
				}

				detailInfoTextViews[i].setPadding(padding, 0, padding, 0);
			}
			
			//연월식.
			detailInfoTextViews[0].setText(car.getYear() + "." + car.getMonth());
			
			//주행거리.
			detailInfoTextViews[1].setText(StringUtils.getFormattedNumber(car.getMileage()) + "km");
			
			//배기량.
			detailInfoTextViews[2].setText(StringUtils.getFormattedNumber(car.getDisplacement()) + "cc");
			
			//연료.
			if("gasoline".equals(car.getFuel_type())) {
				detailInfoTextViews[3].setText("휘발유");
			} else if("diesel".equals(car.getFuel_type())) {
				detailInfoTextViews[3].setText("디젤");
			} else if("lpg".equals(car.getFuel_type())) {
				detailInfoTextViews[3].setText("LPG");
			} else {
				detailInfoTextViews[3].setText("LPG장애");
			}
			
			//번호.
			detailInfoTextViews[4].setText(car.getCar_number());

			//색상.
			detailInfoTextViews[5].setText(car.getColor());
			
			//주소.
			detailInfoTextViews[6].setText(car.getArea());
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	public void setCheck() {

		try {
			if(car.getType() == Car.TYPE_DEALER
					&& !StringUtils.isEmpty(car.getInspection_note_url())) {
				relativeForCheck.setVisibility(View.VISIBLE);
				
				ivCheck.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {

						mActivity.showImageViewer(0, null, new String[]{car.getInspection_note_url()}, null);
					}
				});
				
				ivCheck.setTag(car.getInspection_note_url());
				BCPDownloadUtils.downloadBitmap(car.getInspection_note_url(), new OnBitmapDownloadListener() {
					
					@Override
					public void onError(String url) {

						LogUtils.log("CarDetailPage.onError." + "\nurl : " + url);
						relativeForCheck.setVisibility(View.GONE);
					}
					
					@Override
					public void onCompleted(String url, Bitmap bitmap) {

						try {
							LogUtils.log("CarDetailPage.onCompleted." + "\nurl : " + url);

							if(ivCheck != null && bitmap != null && !bitmap.isRecycled()) {
								ivCheck.setImageBitmap(bitmap);
							}
						} catch (Exception e) {
							LogUtils.trace(e);
							relativeForCheck.setVisibility(View.GONE);
						} catch (OutOfMemoryError oom) {
							LogUtils.trace(oom);
							relativeForCheck.setVisibility(View.GONE);
						}
					}
				}, 540);
			} else {
				relativeForCheck.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void setAccident() {

		try {
			if(type == Car.TYPE_BID && car.getHas_review() == 0) {
				relativeForAccident.setVisibility(View.VISIBLE);
				
				if(car.getHas_carhistory() == 1) {
					tvAccident.setText(R.string.hadAccident);
					btnAccident.setBackgroundResource(R.drawable.detail_accident_btn);
					btnAccident.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View view) {

							if(car != null) {
								Bundle bundle = new Bundle();
								bundle.putString("carNumber", car.getCar_number());
								mActivity.showPage(BCPConstants.PAGE_CAR_HISTORY, bundle);
							}
						}
					});
				} else {
					tvAccident.setText(R.string.noCarHistory);
					btnAccident.setBackgroundResource(R.drawable.detail_accident_btn_b);
					btnAccident.setOnClickListener(null);
				}
			} else {
				relativeForAccident.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void setOptions() {

		try {
			boolean isSetTitle2 = false;
			int[] addedCount = new int[3];
			int biggestIndex = 0;
			int addedAtRelativeCount = 0;
			RelativeLayout.LayoutParams rp = null;
			
			linearForOptions[0].removeAllViews();
			linearForOptions[1].removeAllViews();
			linearForOptions[2].removeAllViews();
			relativeForOption2.removeAllViews();
			
			//서버에서 보내주는 것도 이미지처럼 1~31 인 듯?
			int size = car.getOptions().length;
			for(int i=0; i<size; i++) {

				View optionView = getOptionView(car.getOptions()[i]);
				
				if(car.getOptions()[i] < 25) {
					addedCount[(car.getOptions()[i]-1)%3]++;
					ResizeUtils.viewResize(180, 90, optionView, 1, 0, new int[]{0, 40, 0, 0});
					linearForOptions[(car.getOptions()[i]-1)%3].addView(optionView);
					
				} else if(car.getOptions()[i] == 25 || car.getOptions()[i] == 26) {
					addedCount[car.getOptions()[i]%3]++;
					ResizeUtils.viewResize(180, 90, optionView, 1, 0, new int[]{0, 40, 0, 0});
					linearForOptions[car.getOptions()[i]%3].addView(optionView);
					
				} else {
					
					if(!isSetTitle2) {
						isSetTitle2 = true;
						
						if(addedCount[0] < addedCount[1]) {
							
							if(addedCount[1] > addedCount[2]) {
								biggestIndex = 1;
							} else {
								biggestIndex = 2;
							}
						} else if(addedCount[0] < addedCount[2]) {
							biggestIndex = 2;
						}
						
						rp = (RelativeLayout.LayoutParams) 
								mThisView.findViewById(R.id.carDetailPage_optionTitle2).getLayoutParams();
						rp.width = ResizeUtils.getSpecificLength(308);
						rp.height = ResizeUtils.getSpecificLength(43);
						rp.topMargin = ResizeUtils.getSpecificLength(46);
						
						switch(biggestIndex) {
						
						case 0:
							rp.addRule(RelativeLayout.BELOW, R.id.carDetailPage_linearForOption1);
							break;
							
						case 1:
							rp.addRule(RelativeLayout.BELOW, R.id.carDetailPage_linearForOption2);
							break;
							
						case 2:
							rp.addRule(RelativeLayout.BELOW, R.id.carDetailPage_linearForOption3);
							break;
						}
					}
					
					optionView.setLayoutParams(getRelativeLayoutParamsForOptionView(addedAtRelativeCount));
					relativeForOption2.addView(optionView);
					addedAtRelativeCount++;
				}
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	/**
	 * 
	 * @param index(1~31)
	 * @return
	 */
	public View getOptionView(final int index) {
		
		try {
			View optionView = new View(mContext);
			optionView.setBackgroundResource(
					getResources().getIdentifier("detail_option" + index + "_btn_b", 
							"drawable", "com.byecar.byecarplusfordealer"));
			return optionView;
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		return null;
	}

	public RelativeLayout.LayoutParams getRelativeLayoutParamsForOptionView(int index) {

		try {
			RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
					ResizeUtils.getSpecificLength(180), 
					ResizeUtils.getSpecificLength(90));
			
			rp.addRule(RelativeLayout.BELOW, R.id.carDetailPage_optionTitle2);
			
			switch(index % 3) {
			
			case 0:
				rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				rp.leftMargin = ResizeUtils.getSpecificLength(15);
				break;
				
			case 1:
				rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
				break;
				
			case 2:
				rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				rp.rightMargin = ResizeUtils.getSpecificLength(15);
				break;
			}
			
			if(index < 3) {
				rp.topMargin = ResizeUtils.getSpecificLength(40);
			} else {
				rp.topMargin = ResizeUtils.getSpecificLength(170);
			}
			
			return rp;
		} catch (Exception e) {
			LogUtils.trace(e);
		}
		
		return null;
	}
	
	public void setDescription() {
		
		try {
			tvDescription.setText(car.getDesc());
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void setButtomButtons() {
		
		try {
			RelativeLayout.LayoutParams rp = null;
			
			if(car.getDealer_id() == MainActivity.dealer.getId()
					&& type == Car.TYPE_BID
					&& (car.getStatus() == Car.STATUS_BID_SUCCESS
							|| car.getStatus() == Car.STATUS_PAYMENT_COMPLETED)) {
				
				if(car.getStatus() == Car.STATUS_BID_SUCCESS) {
					btnPayment.setVisibility(View.VISIBLE);
					btnCall.setVisibility(View.GONE);
					
					//btnPayment.
					rp = (RelativeLayout.LayoutParams) btnPayment.getLayoutParams();
					rp.width = ResizeUtils.getSpecificLength(612);
					rp.height = ResizeUtils.getSpecificLength(84);
					rp.bottomMargin = ResizeUtils.getSpecificLength(15);
					

				} else {
					btnPayment.setVisibility(View.GONE);
					btnCall.setVisibility(View.VISIBLE);
					
					//btnCall.
					rp = (RelativeLayout.LayoutParams) btnCall.getLayoutParams();
					rp.width = ResizeUtils.getSpecificLength(612);
					rp.height = ResizeUtils.getSpecificLength(84);
					rp.bottomMargin = ResizeUtils.getSpecificLength(15);
				}
				
				buttonBg.setVisibility(View.VISIBLE);
				
				//buttonBg.
				rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.carDetailPage_buttonBg).getLayoutParams();
				rp.height = ResizeUtils.getSpecificLength(123);
				
				//scrollView.
				rp = (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
				rp.bottomMargin = ResizeUtils.getSpecificLength(115);
				
			} else {
				buttonBg.setVisibility(View.GONE);
				btnPayment.setVisibility(View.GONE);
				btnCall.setVisibility(View.GONE);
				
				//scrollView.
				rp = (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
				rp.bottomMargin = 0;
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void setTitleBarButtons() {

		try {
			switch(car.getType()) {

			//중고차거래. -> 내가 올린 경우에만 수정, 삭제.
			case Car.TYPE_DEALER:

				if(car.getSeller_id() == MainActivity.user.getId()) {
					btnEdit.setVisibility(View.VISIBLE);
					btnDelete.setVisibility(View.VISIBLE);
				} else {
					btnEdit.setVisibility(View.GONE);
					btnDelete.setVisibility(View.GONE);
				}
				
				break;

			case Car.TYPE_BID:
				btnEdit.setVisibility(View.GONE);
				btnDelete.setVisibility(View.GONE);
				break;
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
						carInfoView.clearTime();
						return;
					}
		        	
		        	try {
						long remainTime = car.getBid_until_at() * 1000 
								+ (car.getStatus() < Car.STATUS_BID_COMPLETE ? 0 : 86400000) 
								- System.currentTimeMillis();
			        	
			        	if(remainTime < 0) {
			        		carInfoView.statusChanged(car);
			        	} else {
			        		setAuctionButton(car);
				        	carInfoView.setTime(car);
			        	}
					} catch (Exception e) {
						LogUtils.trace(e);
						TimerUtils.removeOnTimeChangedListener(onTimeChangedListener);
						carInfoView.clearTime();
					}
				}
				
				@Override
				public String getName() {
					
					return "CarDetailPage";
				}
				
				@Override
				public Activity getActivity() {

					return mActivity;
				}
			};
		}
	}
	
	public void bidStatusChanged(String event, Car car) {

		if(event == null) {
			return;
		}
		
		if(this.car.getId() == car.getId()) {
			
			//관리자에 의해 보류된 경우.
			if(event.equals("auction_held")) {
				closePage(R.string.holdOffByAdmin2);
				
			//경매가 시작되는 물건이 있는 경우.
			//경매 매물의 가격 변화가 있는 경우.
			//딜러 선택 시간이 종료된 경우 (유찰).
			//유저가 딜러를 선택한 경우 (낙찰).
			} else {
				this.car.copyValuesFromNewItem(car);
				setAllInfos();
			}
		}
	}

	public void setAuctionButton(Car car) {
		
		if(car.getType() != Car.TYPE_BID) {
			auctionIcon.setVisibility(View.INVISIBLE);
			return;
		}
		
		long remainTime = car.getBid_until_at() * 1000 
				+ (car.getStatus() < Car.STATUS_BID_COMPLETE ? 0 : 86400000) 
				- System.currentTimeMillis();
		
		auctionIcon.setVisibility(View.VISIBLE);
		
		if(remainTime < 0) {
			//경매 종료.
			if(car.getStatus() == Car.STATUS_BIDDING) {
				car.setStatus(Car.STATUS_BID_COMPLETE);
				auctionIcon.setBackgroundResource(R.drawable.auction_sale_icon3);
				
			//입찰 종료.
			} else if(car.getStatus() == Car.STATUS_BID_COMPLETE) {
				car.setStatus(Car.STATUS_BID_FAIL);
				auctionIcon.setBackgroundResource(R.drawable.auction_sale_icon4);
			}

		} else {

			if(car.getStatus() < Car.STATUS_BID_COMPLETE) {
				auctionIcon.setBackgroundResource(R.drawable.auction_sale_icon2);
				
				//경매 종료 시간 한시간 이내.
				if(car.getBid_until_at() -System.currentTimeMillis() / 1000 <= 3600) {
					auctionIcon.setVisibility(View.VISIBLE);
				} else {
					auctionIcon.setVisibility(View.INVISIBLE);
				}
			} else if(car.getStatus() == Car.STATUS_BID_COMPLETE) {
				auctionIcon.setBackgroundResource(R.drawable.auction_sale_icon3);
				auctionIcon.setVisibility(View.VISIBLE);
			} else {
				auctionIcon.setBackgroundResource(R.drawable.auction_sale_icon4);
				auctionIcon.setVisibility(View.VISIBLE);
			}
		}
	}

	public void changeMenuOpenStatus(int index) {

		switch(index) {
		
		//후기.
		case MENU_REVIEW:
			
			if(linearForReview.getVisibility() == View.VISIBLE) {
				linearForReview.setVisibility(View.GONE);
				arrowForReview.setBackgroundResource(R.drawable.detail_toggle);
			} else {
				linearForReview.setVisibility(View.VISIBLE);
				arrowForReview.setBackgroundResource(R.drawable.detail_toggle_up);
			}
			
			break;
		
		//차량정보.
		case MENU_INFO:
			
			if(relativeForInfo.getVisibility() == View.VISIBLE) {
				relativeForInfo.setVisibility(View.GONE);
				arrowForInfo.setBackgroundResource(R.drawable.detail_toggle);
			} else {
				relativeForInfo.setVisibility(View.VISIBLE);
				arrowForInfo.setBackgroundResource(R.drawable.detail_toggle_up);
			}
			
			break;
			
		//성능점검 기록부.
		case MENU_CHECK:
			
			if(ivCheck.getVisibility() == View.VISIBLE) {
				ivCheck.setVisibility(View.GONE);
				arrowForCheck.setBackgroundResource(R.drawable.detail_toggle);
			} else {
				ivCheck.setVisibility(View.VISIBLE);
				arrowForCheck.setBackgroundResource(R.drawable.detail_toggle_up);
			}
			
			break;
			
		//사고보험이력조회.
		case MENU_ACCIDENT:
			
			if(bgForAccident.getVisibility() == View.VISIBLE) {
				bgForAccident.setVisibility(View.GONE);
				tvAccident.setVisibility(View.GONE);
				btnAccident.setVisibility(View.GONE);
				arrowForAccident.setBackgroundResource(R.drawable.detail_toggle);
			} else {
				bgForAccident.setVisibility(View.VISIBLE);
				tvAccident.setVisibility(View.VISIBLE);
				btnAccident.setVisibility(View.VISIBLE);
				arrowForAccident.setBackgroundResource(R.drawable.detail_toggle_up);
			}
			
			break;
			
		//옵션 및 튜닝.
		case MENU_OPTION:
			
			if(relativeForOption.getVisibility() == View.VISIBLE) {
				relativeForOption.setVisibility(View.GONE);
				arrowForOption.setBackgroundResource(R.drawable.detail_toggle);
			} else {
				relativeForOption.setVisibility(View.VISIBLE);
				arrowForOption.setBackgroundResource(R.drawable.detail_toggle_up);
			}
			
			break;
			
		//판매자 한마디.
		case MENU_DESC:
			
			if(tvDescription.getVisibility() == View.VISIBLE) {
				tvDescription.setVisibility(View.GONE);
				arrowForDescription.setBackgroundResource(R.drawable.detail_toggle);
			} else {
				tvDescription.setVisibility(View.VISIBLE);
				arrowForDescription.setBackgroundResource(R.drawable.detail_toggle_up);
			}
			
			break;
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

	public void delete() {
		
		String url = BCPAPIs.CAR_DEALER_DELETE_URL;
		
		url += "?onsalecar_id=" + car.getId();
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("CarDetailPage.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToDelete);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("CarDetailPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_delete);
						mActivity.closePageWithRefreshPreviousPage();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToDelete);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					ToastUtils.showToast(R.string.failToDelete);
				}
			}
		}, mActivity.getLoadingView());
	}

	public void report() {

		String url = BCPAPIs.CAR_DEALER_REPORT_URL;
		
		if(url != null) {
			url += "?onsalecar_id=" + car.getId();
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("CarDetailPage.onError." + "\nurl : " + url);
					ToastUtils.showToast(R.string.failToReportCar);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("CarDetailPage.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);
						
						if(objJSON.getInt("result") == 1) {
							ToastUtils.showToast(R.string.complete_report);
						} else {
							ToastUtils.showToast(objJSON.getString("message"));
						}
						
					} catch (Exception e) {
						LogUtils.trace(e);
						ToastUtils.showToast(R.string.failToReportCar);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
						ToastUtils.showToast(R.string.failToReportCar);
					}
				}
			});
		}
	}
	
	public void setStatusToComplete() {

		String url = BCPAPIs.CAR_DEALER_COMPLETE_URL 
				+ "?onsalecar_id=" + car.getId()
				+ "&status=" + Car.STATUS_TRADE_COMPLETE;
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("CarDetailPage.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("CarDetailPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						downloadCarInfo();
						ToastUtils.showToast(R.string.complete_selling);
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}

	public void showBiddingCheckDialog() {

		mActivity.showAlertDialog(getString(R.string.bidding), 
				StringUtils.getFormattedNumber(biddingPrice/10000) + getString(R.string.wannaBiddingAt), 
				getString(R.string.confirm), getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {

						bid();
					}
				}, null);
		
	}
	
	public void bid() {
		
		//http://dev.bye-car.com/onsalecars/bids/bid.json?onsalecar_id=1&price=1000000
		String url = BCPAPIs.CAR_BID_URL
				+ "?onsalecar_id=" + car.getId()
				+ "&price=" + biddingPrice;
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("CarDetailPage.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToBidding);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("CarDetailPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_bid);
						downloadCarInfo();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToBidding);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					ToastUtils.showToast(R.string.failToBidding);
				}
			}
		});
	}

	public void checkPayable() {
		
		String url = BCPAPIs.CAR_BID_CHECK_PAY + "?onsalecar_id=" + car.getId();
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("CarDetailPage.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("CarDetailPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);
					
					if(objJSON.getInt("result") == 1) {
						int amount_to_pay = (int)(objJSON.getLong("amount_to_pay")/10000);
						
						mActivity.showAlertDialog("결제", "결제하시겠습니까?\n(적립금 " + amount_to_pay + "만원이 차감됩니다.)", 
								"확인", "취소",
								new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {

										pay();
									}
								}, null);
					} else {
						int amount_to_need = (int)(objJSON.getLong("amount_to_need")/10000);
						mActivity.showAlertDialog("결제", "금액이 " + amount_to_need + "만원 부족합니다.\n적립금 페이지로 이동하시겠습니까?", 
								"확인", "취소",
								new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {

										mActivity.showPage(BCPConstants.PAGE_MY_POINT, null);
									}
								}, null);
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void pay() {
		
		String url = BCPAPIs.CAR_BID_PAY + "?onsalecar_id=" + car.getId();
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("CarDetailPage.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToPay);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("CarDetailPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						mActivity.showAlertDialog("결제", "결제가 완료되었습니다.\n(소비자 통화 후 매입진행 해주세요.)", 
								"확인", null);
						downloadCarInfo();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToPay);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					ToastUtils.showToast(R.string.failToPay);
				}
			}
		});
	}

	public void call() {
		
		IntentUtils.call(mContext, car.getSeller_phone_number());
	}
}
