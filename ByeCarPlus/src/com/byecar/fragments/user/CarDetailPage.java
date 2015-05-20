package com.byecar.fragments.user;

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

import com.byecar.byecarplus.MainActivity;
import com.byecar.byecarplus.R;
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
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.TimerUtils;
import com.outspoken_kid.utils.TimerUtils.OnTimeChangedListener;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.OffsetScrollView;
import com.outspoken_kid.views.OffsetScrollView.OnScrollChangedListener;
import com.outspoken_kid.views.PageNavigatorView;

public class CarDetailPage extends BCPFragment {

	private int id;
	private Car car;
	
	private OffsetScrollView scrollView;

	private ViewPager viewPager;
	private PageNavigatorView pageNavigator;
	private View auctionIcon;
	private CarInfoView carInfoView;
	private RelativeLayout relativeForType;
	private DealerView[] dealerViews = new DealerView[3];
	
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
	
	private Button btnCall;
	private View buttonBg;
	
	private Button btnDelete;
	private Button btnEdit;
	private Button btnReport;
	
	private ImagePagerAdapter imagePagerAdapter;
	
	private int type;
	private int scrollOffset;
	private int standardLength;
	private float diff;
	
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
		
		btnCall = (Button) mThisView.findViewById(R.id.carDetailPage_btnCall);
		buttonBg = mThisView.findViewById(R.id.carDetailPage_buttonBg);
		
		btnDelete = (Button) mThisView.findViewById(R.id.carDetailPage_btnDelete);
		btnEdit = (Button) mThisView.findViewById(R.id.carDetailPage_btnEdit);
		btnReport = (Button) mThisView.findViewById(R.id.carDetailPage_btnReport);
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
	
		headerForReview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				changeMenuOpenStatus(0);
			}
		});
		
		headerForInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				changeMenuOpenStatus(1);
			}
		});
		
		headerForCheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				changeMenuOpenStatus(2);
			}
		});
		
		headerForAccident.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				changeMenuOpenStatus(3);
			}
		});
		
		headerForOption.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				changeMenuOpenStatus(4);
			}
		});
		
		headerForDescription.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				changeMenuOpenStatus(5);
			}
		});
		
		btnCall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				String message = null;
				
				if(type == Car.TYPE_DEALER) {
					message = getString(R.string.wannaCallToDealer);
				} else {
					message = car.getSeller_name() + getString(R.string.wannaCallToSeller);
				}
				
				mActivity.showAlertDialog(getString(R.string.call), message, 
						getString(R.string.confirm), getString(R.string.cancel), 
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {

								IntentUtils.call(mContext, 
										type == Car.TYPE_DEALER?
												car.getDealer_phone_number()
												: car.getSeller_phone_number());
							}
						}, null);
			}
		});
	
		btnEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putInt("type", CarRegistrationPage.TYPE_EDIT);
				bundle.putInt("carType", Car.TYPE_BID);
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

		btnReport.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showAlertDialog(R.string.report, R.string.wannaReport, 
						R.string.confirm, R.string.cancel, 
						new DialogInterface.OnClickListener() {
					
							@Override
							public void onClick(DialogInterface dialog, int which) {
								
								report();
							}
						}, null);
			}
		});
	}
	
	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		
		//중고차 매물 또는 내 직거래 매물.
		if(type != Car.TYPE_BID) {

			//buttonBg.
			rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.carDetailPage_buttonBg).getLayoutParams();
			rp.height = ResizeUtils.getSpecificLength(123);
			
			//btnCall.
			rp = (RelativeLayout.LayoutParams) btnCall.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(611);
			rp.height = ResizeUtils.getSpecificLength(84);
			rp.bottomMargin = ResizeUtils.getSpecificLength(15);
			
			if(type == Car.TYPE_DEALER) {
				btnCall.setBackgroundResource(R.drawable.buy_call_btn);
			} else {
				btnCall.setBackgroundResource(R.drawable.normal_direct_call_btn);
			}
			
			//scrollView.
			rp = (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
			rp.bottomMargin = ResizeUtils.getSpecificLength(115);
		}

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
		
		//btnReport.
		rp = (RelativeLayout.LayoutParams) btnReport.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(60);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.topMargin = ResizeUtils.getSpecificLength(16);
		rp.rightMargin = ResizeUtils.getSpecificLength(14);
		
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
			
		case Car.TYPE_DIRECT:
			url = BCPAPIs.CAR_DIRECT_NORMAL_SHOW_URL;
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
					
					setRelativeForType();
					setMainCarInfo();
					setReviews();
					setInfos();
					setAccident();
					setOptions();
					setCheck();
					setDescription();
					setCallButton();
					setTitleBarButtons();
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
		
		//딜러에게 전화 걸기를 위해서.
		MainActivity.dealerPhoneNumber = car.getDealer_phone_number();

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
	}

	public void setRelativeForType() {

		if(car == null) {
			return;
		}
		
		relativeForType.removeAllViews();
		
		switch(type) {
		
		case Car.TYPE_BID:
			
			//내 매물인 경우.
			if(car.getSeller_id() == MainActivity.user.getId()) {
				
				switch(car.getStatus()) {
				
				//입찰 중.
				case Car.STATUS_BIDDING:
					//딜러뷰만.
					addViewsForBidding_dealerViewOnly();
					break;
					
				//입찰 종료, 딜러 선택.
				case Car.STATUS_BID_COMPLETE:
					//딜러뷰 + 딜러 선택 버튼.
					addViewsForBidding_withSelectButtons();
					break;
					
				//낙찰, 딜러와 연락 및 거래.
				case Car.STATUS_BID_SUCCESS:
					//딜러뷰 + 딜러에게 연락, 완료 버튼.
					addViewsForBidding_withButtons(true);
					break;
					
				//유찰, 딜러 선택 실패.
				case Car.STATUS_BID_FAIL:
					//딜러뷰만.
					addViewsForBidding_dealerViewOnly();
					break;
					
				case Car.STATUS_PAYMENT_COMPLETED:
					addViewsForBidding_withButtons(true);
					break;
					
				//거래 완료.
				case Car.STATUS_TRADE_COMPLETE:
					addViewsForBidding_withButtons(false);
					break;
				}
				
			//내 매물이 아닌 경우.
			} else {
				//딜러뷰만.
				addViewsForBidding_dealerViewOnly();
			}
			break;
			
		case Car.TYPE_DEALER:
			addViewsForDealer();
			break;
			
		case Car.TYPE_DIRECT:
			addViewsForDirect();
			break;
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
	
	public void addViewsForBidding_withSelectButtons() {
		
		RelativeLayout.LayoutParams rp = (RelativeLayout.LayoutParams) relativeForType.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(461);
		relativeForType.setBackgroundResource(R.drawable.select_bid_frame1);
		
		int width = ResizeUtils.getSpecificLength(184);
		int height = ResizeUtils.getSpecificLength(60);
		int bottomMargin = ResizeUtils.getSpecificLength(27);

		addDealerViews();
		
		int size = car.getBids().size();
		for(int i=0; i<size; i++) {
			
			Button btnSelectDealer = new Button(mContext);
			btnSelectDealer.setBackgroundResource(R.drawable.select_dealer_btn);
			relativeForType.addView(btnSelectDealer);
			
			rp = new RelativeLayout.LayoutParams(width, height);
			rp.bottomMargin = bottomMargin;
			rp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			
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
			
			final int INDEX = i;
			btnSelectDealer.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					Bundle bundle = new Bundle();
					bundle.putInt("dealer_id", car.getBids().get(INDEX).getDealer_id());
					bundle.putBoolean("needSelect", true);
					bundle.putInt("onsalecar_id", car.getId());
					mActivity.showPage(BCPConstants.PAGE_DEALER, bundle);
				}
			});
			
			btnSelectDealer.setLayoutParams(rp);
		}
	}
	
	public void addViewsForBidding_withButtons(final boolean needCompleteButton) {
		
		RelativeLayout.LayoutParams rp = (RelativeLayout.LayoutParams) relativeForType.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(461);
		relativeForType.setBackgroundResource(R.drawable.select_bid_frame1);
		
		int width = ResizeUtils.getSpecificLength(184);
		int height = ResizeUtils.getSpecificLength(60);
		int bottomMargin = ResizeUtils.getSpecificLength(27);

		addDealerViews();
		
		for(int i=0; i<3; i++) {
			Button button = new Button(mContext);
			relativeForType.addView(button);
			
			rp = new RelativeLayout.LayoutParams(width, height);
			rp.bottomMargin = bottomMargin;
			rp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			
			switch(i) {
			
			case 0:
				rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				rp.leftMargin = ResizeUtils.getSpecificLength(24);
				button.setBackgroundResource(R.drawable.select_contact_call_btn);
				button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {

						callToDealerOrSeller();
					}
				});
				break;
				
			case 1:
				rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
				button.setBackgroundResource(R.drawable.select_contact_message_btn);
				button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {

						requestDealing();
					}
				});
				break;
				
			case 2:
				rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				rp.rightMargin = ResizeUtils.getSpecificLength(24);
				
				if(needCompleteButton) {
					button.setBackgroundResource(R.drawable.select_complete_btn);
				} else {
					button.setBackgroundResource(R.drawable.select_contact_post_btn);
				}
				
				button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {

						if(needCompleteButton) {
							
							if(car.getStatus() == Car.STATUS_BID_SUCCESS) {
								//입금 전까지 거래완료 버튼 비활성화.
							} else {
								setStatusToComplete();
							}
						} else {
							
							if(car.getHas_review() == 1) {
								//리뷰를 작성한 경우 리뷰 버튼 비활성화.
							} else {
								Bundle bundle = new Bundle();
								bundle.putSerializable("car", car);
								mActivity.showPage(BCPConstants.PAGE_WRITE_REVIEW, bundle);
							}
						}
					}
				});
				break;
			}
			
			button.setLayoutParams(rp);
		}
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
	
	public void addViewsForDirect() {
		
		if(car == null) {
			return;
		}
		
		RelativeLayout.LayoutParams rp = (RelativeLayout.LayoutParams) relativeForType.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(314);
		relativeForType.setBackgroundResource(R.drawable.normal_direct_frame);
		
		//ivImage.
		final ImageView ivImage = new ImageView(mContext);
		ResizeUtils.viewResizeForRelative(100, 100, ivImage, null, null, new int[]{50, 120, 0, 0});
		ivImage.setScaleType(ScaleType.CENTER_CROP);
		ivImage.setBackgroundResource(R.drawable.detail_default);
		relativeForType.addView(ivImage);

		ivImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showImageViewer(0, getString(R.string.profileImage), 
						new String[]{car.getSeller_profile_img_url()}, null);
			}
		});
		ivImage.setTag(car.getSeller_profile_img_url());
		BCPDownloadUtils.downloadBitmap(car.getSeller_profile_img_url(), new OnBitmapDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("CarDetailPage.onError." + "\nurl : " + url);

				// TODO Auto-generated method stub		
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
		ResizeUtils.viewResizeForRelative(100, 100, cover, null, null, new int[]{50, 120, 0, 0});
		cover.setBackgroundResource(R.drawable.buy_detail_cover);
		relativeForType.addView(cover);
		
		//tvSellerName.
		TextView tvSellerName = new TextView(mContext);
		ResizeUtils.viewResizeForRelative(126, 100, tvSellerName, null, null, new int[]{170, 120, 0, 0});
		tvSellerName.setGravity(Gravity.CENTER_VERTICAL);
		tvSellerName.setTextColor(getResources().getColor(R.color.new_color_text_darkgray));
		FontUtils.setFontSize(tvSellerName, 30);
		relativeForType.addView(tvSellerName);
		
		tvSellerName.setText(car.getSeller_name());
		
		//tvPhoneNumber.
		TextView tvPhoneNumber = new TextView(mContext);
		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, 100, tvPhoneNumber, null, null, new int[]{334, 120, 20, 0});
		tvPhoneNumber.setGravity(Gravity.CENTER_VERTICAL);
		tvPhoneNumber.setTextColor(getResources().getColor(R.color.new_color_text_darkgray));
		FontUtils.setFontSize(tvPhoneNumber, 30);
		relativeForType.addView(tvPhoneNumber);
		
		tvPhoneNumber.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				callToDealerOrSeller();
			}
		});
		tvPhoneNumber.setText(car.getSeller_phone_number());
	}
	
	public void setReviews() {
		
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
				reply.setReply(car.getReview().getReply());
			}

			rp = (RelativeLayout.LayoutParams) footerForReview.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(608);
			rp.height = ResizeUtils.getSpecificLength(20);
		} else {
			relativeForReview.setVisibility(View.GONE);
		}
	}
	
	public void setInfos() {
	
		if(car == null) {
			return;
		}
		
		//무사고.
		if(car.getHad_accident() == 2) {
			detailInfoViews[0].setBackgroundResource(R.drawable.detail_info1_icon_a);
			btnHistory.setBackgroundResource(R.drawable.detail_no_parts);
			btnHistory.setOnClickListener(null);
			
		//유사고.
		} else if(car.getHad_accident() == 1) {
			detailInfoViews[0].setBackgroundResource(R.drawable.detail_info1_icon_b);
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
			detailInfoViews[0].setBackgroundResource(R.drawable.detail_info1_icon_c);
			btnHistory.setBackgroundResource(R.drawable.detail_no_parts);
			btnHistory.setOnClickListener(null);
		}
		
		//1인 신조.
		if(car.getIs_oneman_owned() == 1) {
			detailInfoViews[1].setBackgroundResource(R.drawable.detail_info2_icon_a);
			
		//1인 신조 아님.
		} else {
			detailInfoViews[1].setBackgroundResource(R.drawable.detail_info2_icon_b);
		}
		
		//4륜 구동.
		if("4WD".equals(car.getCar_wd())) {
			detailInfoViews[2].setBackgroundResource(R.drawable.detail_info3_icon_a);
			
		//2륜 구동.
		} else {
			detailInfoViews[2].setBackgroundResource(R.drawable.detail_info3_icon_b);
		}
		
		//자동.
		if("auto".equals(car.getTransmission_type())) {
			detailInfoViews[3].setBackgroundResource(R.drawable.detail_info4_icon_a);
			
		//수동.
		} else {
			detailInfoViews[3].setBackgroundResource(R.drawable.detail_info4_icon_b);
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
		detailInfoTextViews[0].setText(car.getYear() + getString(R.string.year) + " " + car.getMonth() + getString(R.string.month));
		
		//주행거리.
		detailInfoTextViews[1].setText(StringUtils.getFormattedNumber(car.getMileage()) + "km");
		
		//배기량.
		detailInfoTextViews[2].setText(StringUtils.getFormattedNumber(car.getDisplacement()) + "cc");
		
		//연료.
		detailInfoTextViews[3].setText(Car.getFuelTypeString(mContext, car.getFuel_type()));
		
		//번호.
		detailInfoTextViews[4].setText(car.getCar_number());

		//색상.
		detailInfoTextViews[5].setText(car.getColor());
		
		//주소.
		detailInfoTextViews[6].setText(car.getSeller_address());
	}

	public void setCheck() {

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
	}
	
	public void setAccident() {
		
		if(car.getHad_accident() == 1) {
			relativeForAccident.setVisibility(View.VISIBLE);
		} else {
			relativeForAccident.setVisibility(View.GONE);
		}
	}
	
	public void setOptions() {

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
	}
	
	/**
	 * 
	 * @param index(1~31)
	 * @return
	 */
	public View getOptionView(int index) {
		
		try {
			View optionView = new View(mContext);
			optionView.setBackgroundResource(
					getResources().getIdentifier("detail_option" + index + "_btn_b", 
							"drawable", "com.byecar.byecarplus"));
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
		
		tvDescription.setText(car.getDesc());
	}
	
	public void setCallButton() {
		
		if(type == Car.TYPE_DIRECT
				&& car.getSeller_id() == MainActivity.user.getId()) {
			buttonBg.setVisibility(View.GONE);
			btnCall.setVisibility(View.GONE);
			
			//scrollView.
			RelativeLayout.LayoutParams rp = (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
			rp.bottomMargin = 0;
		}
	}

	public void setTitleBarButtons() {

		switch(car.getType()) {

		//옥션. -> 내가 올린 경우에만 수정, 삭제.
		case Car.TYPE_BID:

			btnReport.setVisibility(View.GONE);
			
			if(car.getSeller_id() == MainActivity.user.getId()) {
				btnEdit.setVisibility(View.VISIBLE);
				btnDelete.setVisibility(View.VISIBLE);
			} else {
				btnEdit.setVisibility(View.GONE);
				btnDelete.setVisibility(View.GONE);
			}
			
			break;

		case Car.TYPE_DEALER:
		case Car.TYPE_DIRECT:
			btnReport.setVisibility(View.VISIBLE);
			btnEdit.setVisibility(View.GONE);
			btnDelete.setVisibility(View.GONE);
			break;
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
				setMainCarInfo();
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
			if(car.getStatus() < Car.STATUS_BID_COMPLETE) {
				car.setStatus(Car.STATUS_BID_COMPLETE);
				auctionIcon.setBackgroundResource(R.drawable.auction_sale_icon3);
				
			//입찰 종료.
			} else {
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
		case 0:
			
			if(linearForReview.getVisibility() == View.VISIBLE) {
				linearForReview.setVisibility(View.GONE);
				arrowForReview.setBackgroundResource(R.drawable.detail_toggle);
			} else {
				linearForReview.setVisibility(View.VISIBLE);
				arrowForReview.setBackgroundResource(R.drawable.detail_toggle_up);
			}
			
			break;
		
		//차량정보.
		case 1:
			
			if(relativeForInfo.getVisibility() == View.VISIBLE) {
				relativeForInfo.setVisibility(View.GONE);
				arrowForInfo.setBackgroundResource(R.drawable.detail_toggle);
			} else {
				relativeForInfo.setVisibility(View.VISIBLE);
				arrowForInfo.setBackgroundResource(R.drawable.detail_toggle_up);
			}
			
			break;
			
		//성능점검 기록부.
		case 2:
			
			if(ivCheck.getVisibility() == View.VISIBLE) {
				ivCheck.setVisibility(View.GONE);
				arrowForCheck.setBackgroundResource(R.drawable.detail_toggle);
			} else {
				ivCheck.setVisibility(View.VISIBLE);
				arrowForCheck.setBackgroundResource(R.drawable.detail_toggle_up);
			}
			
			break;
			
		//사고보험이력조회.
		case 3:
			
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
		case 4:
			
			if(relativeForOption.getVisibility() == View.VISIBLE) {
				relativeForOption.setVisibility(View.GONE);
				arrowForOption.setBackgroundResource(R.drawable.detail_toggle);
			} else {
				relativeForOption.setVisibility(View.VISIBLE);
				arrowForOption.setBackgroundResource(R.drawable.detail_toggle_up);
			}
			
			break;
			
		//판매자 한마디.
		case 5:
			
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
		
		String url = null;
		
		if(type == Car.TYPE_BID) {
			url = BCPAPIs.CAR_BID_DELETE_URL; 
		} else {
			url = BCPAPIs.CAR_DIRECT_NORMAL_DELETE_URL;
		}
		
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

		String url = null;
		
		if(car.getType() == Car.TYPE_DEALER) {
			url = BCPAPIs.CAR_DEALER_REPORT_URL;
		} else {
			url = BCPAPIs.CAR_DIRECT_NORMAL_REPORT_URL;
		}
		
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
	
	public void callToDealerOrSeller() {
		
		String message = getString(R.string.wannaCallToDealer);
		
		mActivity.showAlertDialog(getString(R.string.call), message, 
				getString(R.string.confirm), getString(R.string.cancel), 
				new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {

						IntentUtils.call(mContext, car.getDealer_phone_number());
					}
				}, null);
	}
	
	public void requestDealing() {

		//http://dev.bye-car.com/onsalecars/bids/request.json?onsalecar_id=1
		String url = BCPAPIs.CAR_BID_REQUEST_URL + "?onsalecar_id=" + car.getId();
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

					IntentUtils.sendSMS(mContext, "거래요청", car.getDealer_phone_number());
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void setStatusToComplete() {

		String url = BCPAPIs.CAR_BID_COMPLETE_URL 
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
}
