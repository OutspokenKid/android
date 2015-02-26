package com.byecar.fragments.user;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
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
import com.byecar.models.Dealer;
import com.byecar.views.DealerView;
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

	private static final int OPTION_VIEW_SIZE = 31;
	
	private int id;
	private Car car;
	
	private OffsetScrollView scrollView;

	private Button btnGuide;
	private Button btnBuy;
	private Button btnDelete;
	private Button btnEdit;
	private Button btnCall;
	
	private ViewPager viewPager;
	private PageNavigatorView pageNavigator;
	private View auctionIcon;
	private View remainBg;
	private RelativeLayout timeRelative;
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
	
	private View headerForType;
	private View arrowForType;
	private RelativeLayout relativeForType;
	private View lineForType;
	private View footerForType;
	
	private DealerView[] dealerViews;
	
	private RelativeLayout relativeForDealer;
	private DealerView[] dealerViews2;
	boolean[] selecteds = new boolean[3];
	private Button btnComplete;
	
	private View headerForInfo;
	private View arrowForInfo;
	private RelativeLayout relativeForInfo;
	private TextView[] detailInfoTexts;
	private TextView[] detailInfos;
	private View footerForInfo;
	
	private View headerForOption;
	private View arrowForOption;
	private RelativeLayout relativeForOption;
	private View footerForOption;
	
	private View headerForDescription;
	private View arrowForDescription;
	private TextView tvDescription;
	private View footerForDescription;
	
	private View[] optionViews;
	
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
		
		btnBuy = (Button) mThisView.findViewById(R.id.carDetailPage_btnBuy);
		btnCall = (Button) mThisView.findViewById(R.id.carDetailPage_btnCall);
		btnGuide = (Button) mThisView.findViewById(R.id.carDetailPage_btnGuide);
		btnDelete = (Button) mThisView.findViewById(R.id.carDetailPage_btnDelete);
		btnEdit = (Button) mThisView.findViewById(R.id.carDetailPage_btnEdit);
		
		viewPager = (ViewPager) mThisView.findViewById(R.id.carDetailPage_viewPager);
		pageNavigator = (PageNavigatorView) mThisView.findViewById(R.id.carDetailPage_pageNavigator);
		auctionIcon = mThisView.findViewById(R.id.carDetailPage_auctionIcon);
		remainBg = mThisView.findViewById(R.id.carDetailPage_remainBg);
		timeRelative = (RelativeLayout) mThisView.findViewById(R.id.carDetailPage_timeRelative);
		progressBar = (ProgressBar) mThisView.findViewById(R.id.carDetailPage_progressBar);
		tvRemainTime = (TextView) mThisView.findViewById(R.id.carDetailPage_tvRemainTime);
		tvRemainTimeText = (TextView) mThisView.findViewById(R.id.carDetailPage_tvRemainTimeText);
		timeIcon = mThisView.findViewById(R.id.carDetailPage_timeIcon);
		
		tvCarInfo1 = (TextView) mThisView.findViewById(R.id.carDetailPage_tvCarInfo1);
		tvCarInfo2 = (TextView) mThisView.findViewById(R.id.carDetailPage_tvCarInfo2);
		tvCurrentPrice = (TextView) mThisView.findViewById(R.id.carDetailPage_tvCurrentPrice);
		tvCurrentPriceText = (TextView) mThisView.findViewById(R.id.carDetailPage_tvCurrentPriceText);
		tvBidCount = (TextView) mThisView.findViewById(R.id.carDetailPage_tvBidCount);
		btnLike = (Button) mThisView.findViewById(R.id.carDetailPage_btnLike);
		
		headerForType = mThisView.findViewById(R.id.carDetailPage_headerForType);
		arrowForType = mThisView.findViewById(R.id.carDetailPage_arrowForType);
		relativeForType = (RelativeLayout) mThisView.findViewById(R.id.carDetailPage_relativeForType);
		lineForType = mThisView.findViewById(R.id.carDetailPage_lineForType);
		footerForType = mThisView.findViewById(R.id.carDetailPage_footerForType);
		
		relativeForDealer = (RelativeLayout) mThisView.findViewById(R.id.carDetailPage_relativeForDealer);
		btnComplete = (Button) mThisView.findViewById(R.id.carDetailPage_btnComplete);
		
		headerForInfo = mThisView.findViewById(R.id.carDetailPage_headerForInfo);
		arrowForInfo = mThisView.findViewById(R.id.carDetailPage_arrowForInfo);
		relativeForInfo = (RelativeLayout) mThisView.findViewById(R.id.carDetailPage_relativeForInfo);
		footerForInfo = mThisView.findViewById(R.id.carDetailPage_footerForInfo);
		
		headerForOption = mThisView.findViewById(R.id.carDetailPage_headerForOption);
		arrowForOption = mThisView.findViewById(R.id.carDetailPage_arrowForOption);
		relativeForOption = (RelativeLayout) mThisView.findViewById(R.id.carDetailPage_relativeForOption);
		footerForOption = mThisView.findViewById(R.id.carDetailPage_footerForOption);
		
		headerForDescription = mThisView.findViewById(R.id.carDetailPage_headerForDescription);
		arrowForDescription = mThisView.findViewById(R.id.carDetailPage_arrowForDescription);
		tvDescription = (TextView) mThisView.findViewById(R.id.carDetailPage_tvDescription);
		footerForDescription = mThisView.findViewById(R.id.carDetailPage_footerForDescription);
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
		}
		
		if(type == Car.TYPE_BID
				|| type == Car.TYPE_DIRECT_CERTIFIED) {
			setOnTimerListener();
		}
	}

	@Override
	public void createPage() {

		titleBar.setBgColor(Color.WHITE);
		titleBar.setBgAlpha(0);

		viewPager.setAdapter(imagePagerAdapter = new ImagePagerAdapter(mContext));
		
		if(relativeForOption.getChildCount() == 0) {
			addOptionViews();
		}
		
		if(relativeForInfo.getChildCount() == 0) {
			addDetailInfoTextViews();
		}
	}

	@SuppressLint("ClickableViewAccessibility")
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
		
		headerForType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				changeMenuOpenStatus(0);
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
		
		headerForInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				changeMenuOpenStatus(1);
			}
		});
		
		headerForOption.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				changeMenuOpenStatus(2);
			}
		});
		
		headerForDescription.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				changeMenuOpenStatus(3);
			}
		});
	
		btnGuide.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				ToastUtils.showToast("가이드");
			}
		});
		
		btnBuy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(type == Car.TYPE_DIRECT_NORMAL) {
					
					if(car.getSeller_id() != 0
							&& !StringUtils.isEmpty(car.getSeller_phone_number())) {
						mActivity.showAlertDialog(getString(R.string.call), 
								car.getSeller_name() + getString(R.string.wannaCallToSeller), 
								getString(R.string.confirm), 
								getString(R.string.cancel), 
								new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										IntentUtils.call(mContext, car.getSeller_phone_number());
									}
								}, null);
					}
				} else {
					Bundle bundle = new Bundle();
					bundle.putInt("type", EditUserInfoPage.TYPE_REQUEST_BUYER);
					bundle.putInt("carId", car.getId());
					bundle.putInt("carType", car.getType());
					mActivity.showPage(BCPConstants.PAGE_EDIT_USER_INFO, bundle);
				}
			}
		});
		
		btnCall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showAlertDialog(R.string.call, R.string.wannaCallToDealer, 
						R.string.confirm, R.string.cancel, 
						new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {

						IntentUtils.call(mContext, car.getDealer_phone_number());
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
						R.string.confirm, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						delete();
					}
				});
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;

		//btnGuide.
		rp = (RelativeLayout.LayoutParams) btnGuide.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(60);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.topMargin = ResizeUtils.getSpecificLength(14);
		rp.rightMargin = ResizeUtils.getSpecificLength(10);
		
		//btnDelete.
		rp = (RelativeLayout.LayoutParams) btnDelete.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(60);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.topMargin = ResizeUtils.getSpecificLength(14);
		rp.rightMargin = ResizeUtils.getSpecificLength(10);
		
		//btnEdit.
		rp = (RelativeLayout.LayoutParams) btnEdit.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(60);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.topMargin = ResizeUtils.getSpecificLength(14);
		rp.rightMargin = ResizeUtils.getSpecificLength(10);
		
		//buttonBg.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.carDetailPage_buttonBg).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(100);
		
		//btnBuy.
		rp = (RelativeLayout.LayoutParams) btnBuy.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(624);
		rp.height = ResizeUtils.getSpecificLength(72);
		rp.bottomMargin = ResizeUtils.getSpecificLength(8);
		
		//btnCall.
		rp = (RelativeLayout.LayoutParams) btnCall.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(624);
		rp.height = ResizeUtils.getSpecificLength(72);
		rp.bottomMargin = ResizeUtils.getSpecificLength(8);
		
		if(type != Car.TYPE_BID
				&& type != Car.TYPE_DIRECT_CERTIFIED) {
			rp = (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
			rp.bottomMargin = ResizeUtils.getSpecificLength(88);
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
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.carDetailPage_remainBg).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(147);
		
		//tvCarInfo1.
		rp = (RelativeLayout.LayoutParams) tvCarInfo1.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(340);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		rp.topMargin = ResizeUtils.getSpecificLength(10);
		
		//line.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.carDetailPage_line).getLayoutParams();
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
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.carDetailPage_tvLikeText).getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(5);
		rp.rightMargin = ResizeUtils.getSpecificLength(2);
		
		//headerForType.
		rp = (RelativeLayout.LayoutParams) headerForType.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(68);
		rp.topMargin = ResizeUtils.getSpecificLength(20);
		
		//arrowForType.
		rp = (RelativeLayout.LayoutParams) arrowForType.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(33);
		rp.height = ResizeUtils.getSpecificLength(33);
		rp.topMargin = ResizeUtils.getSpecificLength(18);
		rp.rightMargin = ResizeUtils.getSpecificLength(18);
		
		//relativeForType.
		rp = (RelativeLayout.LayoutParams) relativeForType.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		relativeForType.setPadding(0, 0, 0, ResizeUtils.getSpecificLength(16));
		
		//lineForType.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.carDetailPage_lineForType).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = 1;
		
		//footerForType.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.carDetailPage_footerForType).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(20);
		
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
		
		//footerForInfo.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.carDetailPage_footerForInfo).getLayoutParams();
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
		FontUtils.setFontSize((TextView)mThisView.findViewById(R.id.carDetailPage_tvLikeText), 20);
		
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
	public int getBackButtonResId() {

		return R.drawable.detail_back_btn;
	}

	@Override
	public int getBackButtonWidth() {

		return 212;
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
	
	@Override
	public int getRootViewResId() {

		return R.id.carDetailPage_mainLayout;
	}
	
//////////////////// Custom methods.

	public void addOptionViews() {
		
		relativeForOption.removeAllViews();
		
		optionViews = new View[OPTION_VIEW_SIZE];
		
		RelativeLayout.LayoutParams rp = null;
		

		for(int i=0; i<OPTION_VIEW_SIZE; i++) {

		//Texts.
			if(i == 0) {
				for(int j=0; j<3; j++) {
					relativeForOption.addView(getTextViewForOption(j));
				}
				
			} else if(i == 25) {
				View line = new View(mContext);
				rp = new RelativeLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, 1);
				rp.leftMargin = ResizeUtils.getSpecificLength(26);
				rp.rightMargin = ResizeUtils.getSpecificLength(26);
				rp.addRule(RelativeLayout.BELOW, 
						getResources().getIdentifier("carDetailPage_optionTextView25", 
								"id", "com.byecar.byecarplus"));
				line.setLayoutParams(rp);
				line.setBackgroundColor(Color.rgb(232, 232, 232));
				relativeForOption.addView(line);
				
				relativeForOption.addView(getTextViewForOption(3));
			}
			
		//Views.
			optionViews[i] = getViewForOption(i);
			relativeForOption.addView(optionViews[i]);
		}
		
		//Bottom blank.
		View blank = new View(mContext);
		rp = new RelativeLayout.LayoutParams(
				10, ResizeUtils.getSpecificLength(20));
		rp.addRule(RelativeLayout.BELOW, 
				getResources().getIdentifier("carDetailPage_optionView30", 
						"id", "com.byecar.byecarplus"));
		blank.setLayoutParams(rp);
		relativeForOption.addView(blank);
	}
	
	public TextView getTextViewForOption(int index) {
	
		try {
			TextView textView = new TextView(mContext);
			RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
					index == 3? LayoutParams.WRAP_CONTENT : ResizeUtils.getSpecificLength(200), 
					ResizeUtils.getSpecificLength(80));
			
			switch(index) {
			
			case 0:
				rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				rp.leftMargin = ResizeUtils.getSpecificLength(25);
				textView.setId(getResources().getIdentifier(
						"carDetailPage_optionTextView1", "id", "com.byecar.byecarplus"));
				break;
				
			case 1:
				rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
				rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				break;
				
			case 2:
				rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				rp.rightMargin = ResizeUtils.getSpecificLength(25);
				break;
				
			case 3:
				rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				rp.addRule(RelativeLayout.BELOW, 
						getResources().getIdentifier("carDetailPage_optionView25", 
								"id", "com.byecar.byecarplus"));
				rp.leftMargin = ResizeUtils.getSpecificLength(35);
				textView.setId(getResources().getIdentifier(
						"carDetailPage_optionTextView2", "id", "com.byecar.byecarplus"));
				break;
			}
			
			rp.topMargin = ResizeUtils.getSpecificLength(20);
			textView.setLayoutParams(rp);
			textView.setText(getResources().getIdentifier(
					"detailOptionText" + (index + 1), "string", "com.byecar.byecarplus"));
			textView.setGravity(Gravity.CENTER);
			textView.setTextColor(getResources().getColor(R.color.holo_text));
			return textView;
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		return null;
	}
	
	public View getViewForOption(int index) {
		
		try {
			View view = new View(mContext);
			RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
					ResizeUtils.getSpecificLength(160), 
					ResizeUtils.getSpecificLength(80));

			if(index < 24) {
				switch(index % 3) {
				
				case 0:
					rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					rp.leftMargin = ResizeUtils.getSpecificLength(35);
					
					if(index == 0) {
						rp.addRule(RelativeLayout.BELOW, 
								getResources().getIdentifier("carDetailPage_optionTextView1", 
										"id", "com.byecar.byecarplus"));
					} else {
						rp.addRule(RelativeLayout.BELOW, 
								getResources().getIdentifier("carDetailPage_optionView" + (index - 2),	//i - 3 + 1, 윗줄 아이콘. 
										"id", "com.byecar.byecarplus"));
					}
					
					rp.topMargin = ResizeUtils.getSpecificLength(24);
					break;
				case 1:
					rp.addRule(RelativeLayout.ALIGN_TOP, 
							getResources().getIdentifier("carDetailPage_optionView" + index,				//i - 1 + 1. 왼쪽 아이콘.
									"id", "com.byecar.byecarplus"));
					rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
					break;
				case 2:
					rp.addRule(RelativeLayout.ALIGN_TOP, 
							getResources().getIdentifier("carDetailPage_optionView" + index,				//i - 1 + 1. 왼쪽 아이콘.
									"id", "com.byecar.byecarplus"));
					rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					rp.rightMargin = ResizeUtils.getSpecificLength(35);
					break;
				}
			} else if(index == 24) {
				rp.addRule(RelativeLayout.BELOW, 
						getResources().getIdentifier("carDetailPage_optionView24",
								"id", "com.byecar.byecarplus"));
				rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
				rp.topMargin = ResizeUtils.getSpecificLength(24);
				
			} else if(index == 30) {
				rp.addRule(RelativeLayout.ALIGN_TOP, 
						getResources().getIdentifier("carDetailPage_optionView25",
								"id", "com.byecar.byecarplus"));
				rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				rp.rightMargin = ResizeUtils.getSpecificLength(35);
				
			} else {
				switch(index % 3) {
				
				case 1:
					rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					rp.leftMargin = ResizeUtils.getSpecificLength(35);
					
					if(index == 25) {
						rp.addRule(RelativeLayout.BELOW, 
								getResources().getIdentifier("carDetailPage_optionTextView2", 
										"id", "com.byecar.byecarplus"));
					} else {
						rp.addRule(RelativeLayout.BELOW, 
								getResources().getIdentifier("carDetailPage_optionView" + (index - 2),	//i - 3 + 1, 윗줄 아이콘. 
										"id", "com.byecar.byecarplus"));
					}
					break;
				case 2:
					rp.addRule(RelativeLayout.ALIGN_TOP, 
							getResources().getIdentifier("carDetailPage_optionView" + index,				//i - 1 + 1. 왼쪽 아이콘.
									"id", "com.byecar.byecarplus"));
					rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
					break;
				case 0:
					rp.addRule(RelativeLayout.ALIGN_TOP, 
							getResources().getIdentifier("carDetailPage_optionView" + index,				//i - 1 + 1. 왼쪽 아이콘.
									"id", "com.byecar.byecarplus"));
					rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					rp.rightMargin = ResizeUtils.getSpecificLength(35);
					break;
				}
			}
			
			view.setLayoutParams(rp);
			view.setId(getResources().getIdentifier("carDetailPage_optionView" + (index + 1), 
							"id", "com.byecar.byecarplus"));
			view.setBackgroundResource(
					getResources().getIdentifier("detail_optioin" + (index + 1) + "_btn_a", 
							"drawable", "com.byecar.byecarplus"));
			
			return view;
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		return null;
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
			
		case Car.TYPE_DIRECT_CERTIFIED:
			url = BCPAPIs.CAR_DIRECT_CERTIFIED_SHOW_URL;
			break;
			
		case Car.TYPE_DIRECT_NORMAL:
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
					
					setMainCarInfo();
					setDetailCarInfo();
					setOptionViews();
					setCarDescription();
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
		
		if(car.getType() == Car.TYPE_BID) {
			tvRemainTime.setText("-- : -- : --");
			
			if(car.getStatus() < Car.STATUS_BID_COMPLETE) {
				auctionIcon.setBackgroundResource(R.drawable.main_hotdeal_mark);
				progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_custom_orange));
				
				//경매 종료 시간 한시간 이내.
				if(car.getBid_until_at() -System.currentTimeMillis() / 1000 <= 3600) {
					auctionIcon.setVisibility(View.VISIBLE);
				} else {
					auctionIcon.setVisibility(View.INVISIBLE);
				}
			} else if(car.getStatus() == Car.STATUS_BID_COMPLETE) {
				progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_custom_green));
				auctionIcon.setBackgroundResource(R.drawable.main_hotdeal_mark2);
				auctionIcon.setVisibility(View.VISIBLE);
			} else {
				progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_custom_gray));
				auctionIcon.setBackgroundResource(R.drawable.main_hotdeal_mark3);
				auctionIcon.setVisibility(View.VISIBLE);
			}
		} else if(car.getType() == Car.TYPE_DIRECT_CERTIFIED) {
			tvRemainTime.setText("-- : -- : --");
			
			progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_custom_orange));
			auctionIcon.setBackgroundResource(R.drawable.directmarket_test_symbol);
		}
		
		tvCarInfo1.setText(car.getCar_full_name());
		tvCarInfo2.setText(car.getYear() + "년 / "
				+ StringUtils.getFormattedNumber(car.getMileage()) + "km / "
				+ car.getArea());

		if(car.getType() == Car.TYPE_BID) {
			tvCurrentPriceText.setText(R.string.currentPrice);
		} else {
			tvCurrentPriceText.setText(R.string.salesPrice);
		}
		
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
		
		switch(type) {
		
		case Car.TYPE_BID:
			
			//옥션, 내 매물, 딜러 선택해야할 상황이면,
			if(car.getStatus() == Car.STATUS_BID_COMPLETE
					&& car.getSeller_id() == MainActivity.user.getId()) {
				addViewsForSelectBid();
				hideRelativeForType();
				hideInfo();
				hideOption();
				hideDesc();
			} else {
				addViewsForAuction();
				showRelativeForType();
				showInfo();
				showOption();
				showDesc();
			}
			
			break;
			
		case Car.TYPE_DEALER:
			addViewsForUsed();
			showRelativeForType();
			showInfo();
			showOption();
			showDesc();
			break;
			
		case Car.TYPE_DIRECT_CERTIFIED:
			addViewsForDirectCertified();
			showRelativeForType();
			showInfo();
			showOption();
			hideDesc();
			break;
			
		case Car.TYPE_DIRECT_NORMAL:
			addViewsForDirectNormal();
			showRelativeForType();
			showInfo();
			showOption();
			hideDesc();
			break;
		}
	}
	
	public void updateMainCarInfo() {
		
		tvRemainTime.setText("-- : -- : --");
		
		if(car.getStatus() < Car.STATUS_BID_COMPLETE) {
			auctionIcon.setBackgroundResource(R.drawable.main_hotdeal_mark);
			progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_custom_orange));
			
			//경매 종료 시간 한시간 이내.
			if(car.getBid_until_at() -System.currentTimeMillis() / 1000 <= 3600) {
				auctionIcon.setVisibility(View.VISIBLE);
			} else {
				auctionIcon.setVisibility(View.INVISIBLE);
			}
		} else if(car.getStatus() == Car.STATUS_BID_COMPLETE) {
			progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_custom_green));
			auctionIcon.setBackgroundResource(R.drawable.main_hotdeal_mark2);
			auctionIcon.setVisibility(View.VISIBLE);
		} else {
			progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_custom_gray));
			auctionIcon.setBackgroundResource(R.drawable.main_hotdeal_mark3);
			auctionIcon.setVisibility(View.VISIBLE);
		}
		
		tvCurrentPrice.setText(StringUtils.getFormattedNumber(car.getPrice()) 
				+ getString(R.string.won));
		tvBidCount.setText("입찰자 " + car.getBids_cnt() + "명");
		
		if(relativeForDealer.getVisibility() != View.VISIBLE) {
			
			for(int i=0; i<3; i++) {
				
				if(i < car.getBids().size()) {
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
				}
			}
		}

		//내 매물, 딜러 선택해야할 상황이면,
		if(car.getStatus() == Car.STATUS_BID_COMPLETE
				&& car.getSeller_id() == MainActivity.user.getId()) {
			addViewsForSelectBid();
			hideRelativeForType();
			hideInfo();
			hideOption();
			hideDesc();
		} else {
			addViewsForAuction();
			showRelativeForType();
			showInfo();
			showOption();
			showDesc();
		}
	}
	
	public void setDetailCarInfo() {

		if(type == Car.TYPE_BID
				|| type == Car.TYPE_DIRECT_CERTIFIED) {
			auctionIcon.setVisibility(View.VISIBLE);
			timeRelative.setVisibility(View.VISIBLE);
			tvBidCount.setVisibility(View.VISIBLE);
			
			mThisView.findViewById(R.id.carDetailPage_buttonBg).setVisibility(View.INVISIBLE);
			
			((RelativeLayout.LayoutParams) btnLike.getLayoutParams()).rightMargin = ResizeUtils.getSpecificLength(14);
		} else {
			auctionIcon.setVisibility(View.INVISIBLE);
			timeRelative.setVisibility(View.GONE);
			tvBidCount.setVisibility(View.INVISIBLE);
			
			if(car.getHas_purchased() == 0) {
				btnBuy.setVisibility(View.VISIBLE);
			} else {
				btnCall.setVisibility(View.VISIBLE);
			}
			
			mThisView.findViewById(R.id.carDetailPage_buttonBg).setVisibility(View.VISIBLE);
			
			((RelativeLayout.LayoutParams) btnLike.getLayoutParams()).rightMargin = ResizeUtils.getSpecificLength(-100);
		}
		
		//내 매물인 경우.
		if(car != null && car.getSeller_id() == MainActivity.user.getId()) {		
				
			btnBuy.setVisibility(View.INVISIBLE);
			btnCall.setVisibility(View.INVISIBLE);
			mThisView.findViewById(R.id.carDetailPage_buttonBg).setVisibility(View.INVISIBLE);
			
			RelativeLayout.LayoutParams rp = (RelativeLayout.LayoutParams) scrollView.getLayoutParams();
			rp.bottomMargin = ResizeUtils.getSpecificLength(0);
			
			if(car.getType() == Car.TYPE_BID 
					&& car.getStatus() == Car.STATUS_BID_COMPLETE
					&& car.getSeller_id() == MainActivity.user.getId()) {
				btnEdit.setVisibility(View.GONE);
				btnDelete.setVisibility(View.GONE);
				btnGuide.setVisibility(View.GONE);
			} else if((car.getType() == Car.TYPE_BID && car.getStatus() < Car.STATUS_BIDDING)
					|| (car.getType() == Car.TYPE_DIRECT_NORMAL && car.getStatus() < Car.STATUS_TRADE_COMPLETE)) {
				btnEdit.setVisibility(View.VISIBLE);
				btnDelete.setVisibility(View.VISIBLE);
				btnGuide.setVisibility(View.INVISIBLE);
			} else {
//				btnGuide.setVisibility(View.VISIBLE);
				btnGuide.setVisibility(View.INVISIBLE);
				btnEdit.setVisibility(View.INVISIBLE);
				btnDelete.setVisibility(View.INVISIBLE);
			}
		} else {
//			btnGuide.setVisibility(View.VISIBLE);
			btnGuide.setVisibility(View.INVISIBLE);
			btnEdit.setVisibility(View.INVISIBLE);
			btnDelete.setVisibility(View.INVISIBLE);
		}

		//제조사.
		detailInfos[0].setText(car.getBrand_name());
		
		//모델.
		detailInfos[1].setText(car.getModel_name());
		
		//세부모델.
		detailInfos[2].setText(car.getTrim_name());
		
		//연월식.
		detailInfos[3].setText(car.getYear() + getString(R.string.year));
		
		//차량번호.
		detailInfos[4].setText(car.getCar_number());
		
		//주행거리.
		detailInfos[5].setText(StringUtils.getFormattedNumber(car.getMileage()) + "km");
		
		//배기량.
		detailInfos[6].setText(StringUtils.getFormattedNumber(car.getDisplacement()) + "cc");
		
		//판매지역.
		detailInfos[7].setText(car.getArea());
		
		//변속기.
		detailInfos[8].setText(("manual".equals(car.getTransmission_type())? 
				getString(R.string.carSearchString_transmission2) :
					getString(R.string.carSearchString_transmission1)));
		
		//연료.
		if("lpg".equals(car.getFuel_type())) {
			detailInfos[9].setText(R.string.carSearchString_fuel3);
		} else if("diesel".equals(car.getFuel_type())) {
			detailInfos[9].setText(R.string.carSearchString_fuel2);
		} else {
			detailInfos[9].setText(R.string.carSearchString_fuel1);
		}
		
		//사고유무.
		if(car.getHad_accident() == 2) {
			detailInfos[10].setText(R.string.carSearchString_accident1);
		} else if(car.getHad_accident() == 1) {
			detailInfos[10].setText(R.string.carSearchString_accident2);
		} else {
			detailInfos[10].setText(R.string.carSearchString_accident3);
		}
		
		//1인신조.
		if(car.getIs_oneman_owned() == 1) {
			detailInfos[11].setText(R.string.carSearchString_oneManOwned1);
		} else {
			detailInfos[11].setText(R.string.carSearchString_oneManOwned2);
		}
	}

	public void setOptionViews() {

		if(car.getOptions() == null) {
			return;
		}
		
		for(int i=0; i<OPTION_VIEW_SIZE; i++) {
			optionViews[i].setBackgroundResource(
					getResources().getIdentifier("detail_optioin" + (i + 1) + "_btn_a", 
							"drawable", "com.byecar.byecarplus"));
		}
		
		int size = car.getOptions().length;
		for(int i=0; i<size; i++) {

			int index = car.getOptions()[i];
			optionViews[index - 1].setBackgroundResource(
					getResources().getIdentifier("detail_optioin" + index + "_btn_b", 
							"drawable", "com.byecar.byecarplus"));
		}
	}
	
	public void setCarDescription() {
		
		tvDescription.setText(car.getDesc());
	}

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

	public void changeMenuOpenStatus(int index) {

		switch(index) {
		
		case 0:
			
			if(relativeForType.getVisibility() == View.VISIBLE) {
				relativeForType.setVisibility(View.GONE);
				arrowForType.setBackgroundResource(R.drawable.detail_toggle);
			} else {
				relativeForType.setVisibility(View.VISIBLE);
				arrowForType.setBackgroundResource(R.drawable.detail_toggle_up);
			}
			
			break;
			
		case 1:
			
			if(relativeForInfo.getVisibility() == View.VISIBLE) {
				relativeForInfo.setVisibility(View.GONE);
				arrowForInfo.setBackgroundResource(R.drawable.detail_toggle);
			} else {
				relativeForInfo.setVisibility(View.VISIBLE);
				arrowForInfo.setBackgroundResource(R.drawable.detail_toggle_up);
			}
			
			break;
			
		case 2:
			
			if(relativeForOption.getVisibility() == View.VISIBLE) {
				relativeForOption.setVisibility(View.GONE);
				arrowForOption.setBackgroundResource(R.drawable.detail_toggle);
			} else {
				relativeForOption.setVisibility(View.VISIBLE);
				arrowForOption.setBackgroundResource(R.drawable.detail_toggle_up);
			}
			
			break;
			
		case 3:
			
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
	
	public void addViewsForAuction() {
		
		//이전 상태 모두 지우기.
		relativeForType.removeAllViews();
		headerForType.setBackgroundResource(R.drawable.detail_head1);
		
		dealerViews = new DealerView[3];
		
		for(int i=0; i<3; i++) {
			dealerViews[i] = new DealerView(mContext, i);
			ResizeUtils.viewResizeForRelative(178, 290, dealerViews[i], 
					new int[]{RelativeLayout.ALIGN_PARENT_LEFT}, new int[]{0}, 
					new int[]{18 + (i*196), 14, 0, 14});
			relativeForType.addView(dealerViews[i]);
		}

		int size = car.getBids().size();
		
		if(size == 0) {
			
			for(int i=0; i<3; i++) {
				dealerViews[i].setVisibility(View.INVISIBLE);
			}
			
			View noOne = new View(mContext);
			ResizeUtils.viewResizeForRelative(218, 248, noOne, 
					new int[]{RelativeLayout.CENTER_IN_PARENT}, new int[]{0}, 
					null);
			noOne.setBackgroundResource(R.drawable.detail_no_one);
			relativeForType.addView(noOne);
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
			}
		}
	}

	public void addViewsForUsed() {
		
		//이전 상태 모두 지우기.
		relativeForType.removeAllViews();
		
		headerForType.setBackgroundResource(R.drawable.buy_header1);
		relativeForType.setBackgroundResource(R.drawable.detail_body4);
		
		if(car.getDealer_id() != 0) {
			
			//ivImage.
			final ImageView ivImage = new ImageView(mContext);
			ResizeUtils.viewResizeForRelative(130, 130, ivImage, 
					new int[]{RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.CENTER_HORIZONTAL}, 
					new int[]{0, 0}, 
					new int[]{0, 20, 0, 0});
			ivImage.setId(R.id.carDetailPage_used_ivImage);
			ivImage.setBackgroundResource(R.drawable.detail_default);
			ivImage.setScaleType(ScaleType.CENTER_CROP);
			relativeForType.addView(ivImage);
			
			ivImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					Bundle bundle = new Bundle();
					bundle.putBoolean("isCertifier", false);
					bundle.putInt("dealer_id", car.getDealer_id());
					mActivity.showPage(BCPConstants.PAGE_DEALER_CERTIFIER, bundle);
				}
			});
			
			ivImage.setTag(car.getDealer_profile_img_url());
			DownloadUtils.downloadBitmap(car.getDealer_profile_img_url(), new OnBitmapDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("CarDetailPage.onError." + "\nurl : " + url);

					// TODO Auto-generated method stub		
				}

				@Override
				public void onCompleted(String url, Bitmap bitmap) {

					try {
						LogUtils.log("CarDetailPage.onCompleted." + "\nurl : " + url);

						if(bitmap != null && !bitmap.isRecycled()
								&& ivImage != null) {
							ivImage.setImageBitmap(bitmap);
						}
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
					}
				}
			});
			
			//cover.
			View cover = new View(mContext);
			ResizeUtils.viewResizeForRelative(130, 130, cover, 
					new int[]{RelativeLayout.ALIGN_TOP, RelativeLayout.ALIGN_LEFT}, 
					new int[]{R.id.carDetailPage_used_ivImage, R.id.carDetailPage_used_ivImage}, 
					null);
			cover.setBackgroundResource(R.drawable.buy_detail_cover);
			relativeForType.addView(cover);
			
			//tvInfo1.
			TextView tvInfo1 = new TextView(mContext);
			ResizeUtils.viewResizeForRelative(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, tvInfo1, 
					new int[]{RelativeLayout.BELOW, RelativeLayout.CENTER_HORIZONTAL}, 
					new int[]{R.id.carDetailPage_used_ivImage, 0}, 
					new int[]{0, 20, 0, 0});
			tvInfo1.setId(R.id.carDetailPage_used_tvInfo1);
			tvInfo1.setText(null);
			FontUtils.addSpan(tvInfo1, car.getDealer_name(), 0, 1, true);
			FontUtils.addSpan(tvInfo1, " " + getString(R.string.dealer), 0, 1);
			tvInfo1.setTextColor(getResources().getColor(R.color.holo_text));
			FontUtils.setFontSize(tvInfo1, 30);
			relativeForType.addView(tvInfo1);
			
			//tvInfo2.
			TextView tvInfo2 = new TextView(mContext);
			ResizeUtils.viewResizeForRelative(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, tvInfo2, 
					new int[]{RelativeLayout.BELOW, RelativeLayout.CENTER_HORIZONTAL}, 
					new int[]{R.id.carDetailPage_used_tvInfo1, 0}, 
					new int[]{0, 5, 0, 0});
			tvInfo2.setId(R.id.carDetailPage_used_tvInfo2);
			tvInfo2.setText(car.getDealer_address() + "\n" + car.getDealer_company());
			tvInfo2.setTextColor(getResources().getColor(R.color.holo_text));
			tvInfo2.setGravity(Gravity.CENTER);
			FontUtils.setFontSize(tvInfo2, 16);
			relativeForType.addView(tvInfo2);
			
			//tvGrade.
			TextView tvGrade = new TextView(mContext);
			ResizeUtils.viewResizeForRelative(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, tvGrade, 
					new int[]{RelativeLayout.BELOW, RelativeLayout.CENTER_HORIZONTAL}, 
					new int[]{R.id.carDetailPage_used_tvInfo2, 0}, 
					new int[]{0, 5, 0, 0});
			tvGrade.setId(R.id.carDetailPage_used_tvGrade);
			
			switch(car.getDealer_level()) {
			
			case Dealer.LEVEL_FRESH_MAN:
				tvGrade.setText(R.string.dealerLevel1);
				tvGrade.setTextColor(getResources().getColor(R.color.color_dealer_level1));
				break;
				
			case Dealer.LEVEL_NORAML_DEALER:
				tvGrade.setText(R.string.dealerLevel2);
				tvGrade.setTextColor(getResources().getColor(R.color.color_dealer_level2));
				break;
				
			case Dealer.LEVEL_SUPERB_DEALER:
				tvGrade.setText(R.string.dealerLevel3);
				tvGrade.setTextColor(getResources().getColor(R.color.color_dealer_level3));
				break;
				
			case Dealer.LEVEL_POWER_DEALER:
				tvGrade.setText(R.string.dealerLevel4);
				tvGrade.setTextColor(getResources().getColor(R.color.color_dealer_level4));
				break;
			}
			
			FontUtils.setFontSize(tvGrade, 20);
			relativeForType.addView(tvGrade);
			
			//line.
			View line = new View(mContext);
			ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, ResizeUtils.ONE, line, 
					new int[]{RelativeLayout.BELOW, RelativeLayout.CENTER_HORIZONTAL}, 
					new int[]{R.id.carDetailPage_used_tvGrade, 0}, 
					new int[]{25, 20, 25, 20});
			line.setId(R.id.carDetailPage_used_line);
			line.setBackgroundColor(getResources().getColor(R.color.color_ltgray));
			relativeForType.addView(line);
			
			//btnMoveToPage.
			Button btnMoveToPage = new Button(mContext);
			ResizeUtils.viewResizeForRelative(164, 24, btnMoveToPage, 
					new int[]{RelativeLayout.BELOW, RelativeLayout.CENTER_HORIZONTAL}, 
					new int[]{R.id.carDetailPage_used_line, 0}, 
					new int[]{0, 0, 0, 20});
			btnMoveToPage.setBackgroundResource(R.drawable.buy_dealer_btn);
			btnMoveToPage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					Bundle bundle = new Bundle();
					bundle.putBoolean("isCertifier", false);
					bundle.putInt("dealer_id", car.getDealer_id());
					mActivity.showPage(BCPConstants.PAGE_DEALER_CERTIFIER, bundle);
				}
			});
			relativeForType.addView(btnMoveToPage);
		}
	}
	
	public void addViewsForDirectCertified() {

		//이전 상태 모두 지우기.
		relativeForType.removeAllViews();
		
		headerForType.setBackgroundResource(R.drawable.verification_header1);
		relativeForType.setBackgroundResource(R.drawable.detail_body4);

		//ivImage.
		final ImageView ivImage = new ImageView(mContext);
		ResizeUtils.viewResizeForRelative(130, 130, ivImage, 
				new int[]{RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.CENTER_HORIZONTAL}, 
				new int[]{0, 0}, 
				new int[]{0, 20, 0, 0});
		ivImage.setId(R.id.carDetailPage_certified_ivImage);
		ivImage.setBackgroundResource(R.drawable.detail_default);
		ivImage.setScaleType(ScaleType.CENTER_CROP);
		relativeForType.addView(ivImage);
		
		ivImage.setTag(car.getManager_profile_img_url());
		DownloadUtils.downloadBitmap(car.getManager_profile_img_url(), new OnBitmapDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("CarDetailPage.onError." + "\nurl : " + url);

				// TODO Auto-generated method stub		
			}

			@Override
			public void onCompleted(String url, Bitmap bitmap) {

				try {
					LogUtils.log("CarDetailPage.onCompleted." + "\nurl : " + url);

					if(bitmap != null && !bitmap.isRecycled()
							&& ivImage != null) {
						ivImage.setImageBitmap(bitmap);
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
		
		ivImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putBoolean("isCertifier", true);
				bundle.putInt("certifier_id", car.getManager_id());
				mActivity.showPage(BCPConstants.PAGE_DEALER_CERTIFIER, bundle);
			}
		});
		
		//cover.
		View cover = new View(mContext);
		ResizeUtils.viewResizeForRelative(130, 130, cover, 
				new int[]{RelativeLayout.ALIGN_TOP, RelativeLayout.ALIGN_LEFT}, 
				new int[]{R.id.carDetailPage_certified_ivImage, R.id.carDetailPage_certified_ivImage}, 
				null);
		cover.setBackgroundResource(R.drawable.buy_detail_cover);
		relativeForType.addView(cover);
		
		//tvInfo.
		TextView tvInfo = new TextView(mContext);
		ResizeUtils.viewResizeForRelative(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, tvInfo, 
				new int[]{RelativeLayout.BELOW, RelativeLayout.CENTER_HORIZONTAL}, 
				new int[]{R.id.carDetailPage_certified_ivImage, 0}, 
				new int[]{0, 20, 0, 0});
		tvInfo.setId(R.id.carDetailPage_certified_tvInfo);
		tvInfo.setText(null);
		FontUtils.addSpan(tvInfo, car.getManager_name(), 0, 1, true);
		FontUtils.addSpan(tvInfo, " " + getString(R.string.certifier), 0, 1);
		tvInfo.setTextColor(getResources().getColor(R.color.holo_text));
		FontUtils.setFontSize(tvInfo, 30);
		relativeForType.addView(tvInfo);
		
		//tvDesc.
		TextView tvDesc = new TextView(mContext);
		ResizeUtils.viewResizeForRelative(568, LayoutParams.WRAP_CONTENT, tvDesc, 
				new int[]{RelativeLayout.BELOW, RelativeLayout.CENTER_HORIZONTAL}, 
				new int[]{R.id.carDetailPage_certified_tvInfo, 0}, 
				new int[]{0, 20, 0, 0});
		tvDesc.setId(R.id.carDetailPage_certified_tvDesc);
		tvDesc.setText(car.getManager_desc());
		tvDesc.setTextColor(getResources().getColor(R.color.holo_text));
		tvDesc.setGravity(Gravity.CENTER);
		tvDesc.setMinHeight(ResizeUtils.getSpecificLength(64));
		tvDesc.setBackgroundResource(R.drawable.white_round_box);
		FontUtils.setFontSize(tvDesc, 20);
		relativeForType.addView(tvDesc);
		
		//line.
		View line = new View(mContext);
		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, ResizeUtils.ONE, line, 
				new int[]{RelativeLayout.BELOW, RelativeLayout.CENTER_HORIZONTAL}, 
				new int[]{R.id.carDetailPage_certified_tvDesc, 0}, 
				new int[]{25, 20, 25, 20});
		line.setId(R.id.carDetailPage_certified_line);
		line.setBackgroundColor(getResources().getColor(R.color.color_ltgray));
		relativeForType.addView(line);
		
		//btnMoveToPage.
		Button btnMoveToPage = new Button(mContext);
		ResizeUtils.viewResizeForRelative(178, 24, btnMoveToPage, 
				new int[]{RelativeLayout.BELOW, RelativeLayout.CENTER_HORIZONTAL}, 
				new int[]{R.id.carDetailPage_certified_line, 0}, 
				new int[]{0, 0, 0, 20});
		btnMoveToPage.setBackgroundResource(R.drawable.verification_home_btn);
		btnMoveToPage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putBoolean("isCertifier", true);
				bundle.putInt("certifier_id", car.getManager_id());
				mActivity.showPage(BCPConstants.PAGE_DEALER_CERTIFIER, bundle);
			}
		});
		relativeForType.addView(btnMoveToPage);
	}
	
	public void addViewsForDirectNormal() {
		
		//이전 상태 모두 지우기.
		relativeForType.removeAllViews();
		
		headerForType.setBackgroundResource(R.drawable.normal_direct_header);
		relativeForType.setBackgroundResource(R.drawable.detail_body4);
		
		//ivImage.
		final ImageView ivImage = new ImageView(mContext);
		ResizeUtils.viewResizeForRelative(130, 130, ivImage, 
				new int[]{RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.CENTER_HORIZONTAL}, 
				new int[]{0, 0}, 
				new int[]{0, 20, 0, 0});
		ivImage.setId(R.id.carDetailPage_normal_ivImage);
		ivImage.setBackgroundResource(R.drawable.detail_default);
		ivImage.setScaleType(ScaleType.CENTER_CROP);
		relativeForType.addView(ivImage);
		
		ivImage.setTag(car.getSeller_profile_img_url());
		DownloadUtils.downloadBitmap(car.getSeller_profile_img_url(), new OnBitmapDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("CarDetailPage.onError." + "\nurl : " + url);

				// TODO Auto-generated method stub		
			}

			@Override
			public void onCompleted(String url, Bitmap bitmap) {

				try {
					LogUtils.log("CarDetailPage.onCompleted." + "\nurl : " + url);

					if(bitmap != null && !bitmap.isRecycled()
							&& ivImage != null) {
						ivImage.setImageBitmap(bitmap);
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
		
		//cover.
		View cover = new View(mContext);
		ResizeUtils.viewResizeForRelative(130, 130, cover, 
				new int[]{RelativeLayout.ALIGN_TOP, RelativeLayout.ALIGN_LEFT}, 
				new int[]{R.id.carDetailPage_normal_ivImage, R.id.carDetailPage_normal_ivImage}, 
				null);
		cover.setBackgroundResource(R.drawable.buy_detail_cover);
		relativeForType.addView(cover);
		
		//tvInfo.
		TextView tvInfo = new TextView(mContext);
		ResizeUtils.viewResizeForRelative(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, tvInfo, 
				new int[]{RelativeLayout.BELOW, RelativeLayout.CENTER_HORIZONTAL}, 
				new int[]{R.id.carDetailPage_normal_ivImage, 0}, 
				new int[]{0, 20, 0, 0});
		tvInfo.setId(R.id.carDetailPage_normal_tvInfo);
		tvInfo.setText(car.getSeller_name());
		tvInfo.setTextColor(getResources().getColor(R.color.holo_text));
		FontUtils.setFontSize(tvInfo, 30);
		FontUtils.setFontStyle(tvInfo, FontUtils.BOLD);
		relativeForType.addView(tvInfo);
		
		//line1.
		View line1 = new View(mContext);
		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, ResizeUtils.ONE, line1, 
				new int[]{RelativeLayout.BELOW, RelativeLayout.CENTER_HORIZONTAL}, 
				new int[]{R.id.carDetailPage_normal_tvInfo, 0}, 
				new int[]{25, 20, 25, 20});
		line1.setId(R.id.carDetailPage_normal_line1);
		line1.setBackgroundColor(getResources().getColor(R.color.color_ltgray));
		relativeForType.addView(line1);
		
		//tvPhone.
		TextView tvPhone = new TextView(mContext);
		ResizeUtils.viewResizeForRelative(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, tvPhone, 
				new int[]{RelativeLayout.BELOW, RelativeLayout.CENTER_HORIZONTAL}, 
				new int[]{R.id.carDetailPage_normal_line1, 0}, 
				null);
		tvPhone.setId(R.id.carDetailPage_normal_tvPhone);
		tvPhone.setText(null);
		FontUtils.addSpan(tvPhone, R.string.phone, getResources().getColor(R.color.holo_text_hint), 1);
		FontUtils.addSpan(tvPhone, "\n" + car.getSeller_phone_number(), 0, 1, true);
		tvPhone.setTextColor(getResources().getColor(R.color.holo_text));
		tvPhone.setGravity(Gravity.CENTER);
		tvPhone.setMinHeight(ResizeUtils.getSpecificLength(64));
		FontUtils.setFontSize(tvPhone, 20);
		relativeForType.addView(tvPhone);
		
		//line2.
		View line2 = new View(mContext);
		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, ResizeUtils.ONE, line2, 
				new int[]{RelativeLayout.BELOW, RelativeLayout.CENTER_HORIZONTAL}, 
				new int[]{R.id.carDetailPage_normal_tvPhone, 0}, 
				new int[]{25, 20, 25, 20});
		line2.setId(R.id.carDetailPage_normal_line2);
		line2.setBackgroundColor(getResources().getColor(R.color.color_ltgray));
		relativeForType.addView(line2);
		
		//tvArea.
		TextView tvArea = new TextView(mContext);
		ResizeUtils.viewResizeForRelative(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, tvArea, 
				new int[]{RelativeLayout.BELOW, RelativeLayout.CENTER_HORIZONTAL}, 
				new int[]{R.id.carDetailPage_normal_line2, 0}, 
				null);
		tvArea.setId(R.id.carDetailPage_normal_tvArea);
		tvArea.setText(null);
		FontUtils.addSpan(tvArea, R.string.sellingArea, getResources().getColor(R.color.holo_text_hint), 1);
		FontUtils.addSpan(tvArea, "\n" + car.getSeller_address(), 0, 1, true);
		tvArea.setTextColor(getResources().getColor(R.color.holo_text));
		tvArea.setGravity(Gravity.CENTER);
		tvArea.setMinHeight(ResizeUtils.getSpecificLength(64));
		FontUtils.setFontSize(tvArea, 20);
		relativeForType.addView(tvArea);
	}

	public void addViewsForSelectBid() {

		//이전 상태 모두 지우기.
		relativeForDealer.removeAllViews();
		
		dealerViews2 = new DealerView[3];
		
		final Button[] selectButtons = new Button[3];
		
		for(int i=0; i<3; i++) {
			dealerViews2[i] = new DealerView(mContext, i);
			ResizeUtils.viewResizeForRelative(178, 290, dealerViews2[i], 
					new int[]{RelativeLayout.ALIGN_PARENT_LEFT}, new int[]{0}, 
					new int[]{18 + (i*196), 97, 0, 14});
			relativeForDealer.addView(dealerViews2[i]);
			
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
				dealerViews2[i].setVisibility(View.INVISIBLE);
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
				dealerViews2[i].setDealerInfo(car.getBids().get(i));
				
				final int I = i;
				dealerViews2[i].setOnClickListener(new OnClickListener() {

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
	
	public void addDetailInfoTextViews() {
		
		relativeForInfo.removeAllViews();
		
		int size = 12;
		detailInfoTexts = new TextView[size];
		detailInfos = new TextView[size];
				
		for(int i=0; i<12; i++) {
			
			detailInfoTexts[i] = new TextView(mContext);
			
			int thisViewResId = getResources().getIdentifier("carDetailPage_detailInfoText" + (i + 1), 
					"id", "com.byecar.byecarplus");
			int lastViewResId = getResources().getIdentifier("carDetailPage_detailInfoText" + i, 
					"id", "com.byecar.byecarplus");
			int textResId = getResources().getIdentifier("detailCarInfoString" + (i + 1), 
					"string", "com.byecar.byecarplus");
			
			ResizeUtils.viewResizeForRelative(LayoutParams.WRAP_CONTENT, 50, detailInfoTexts[i], 
					new int[]{RelativeLayout.ALIGN_PARENT_LEFT, i==0?RelativeLayout.ALIGN_PARENT_TOP:RelativeLayout.BELOW}, 
					new int[]{0, i==0?0:lastViewResId}, 
					new int[]{18, i==0?18:0, 0, 18});
			detailInfoTexts[i].setId(thisViewResId);
			detailInfoTexts[i].setText(textResId);
			detailInfoTexts[i].setTextColor(Color.rgb(115, 115, 115));
			detailInfoTexts[i].setGravity(Gravity.CENTER_VERTICAL);
			FontUtils.setFontSize(detailInfoTexts[i], 22);
			FontUtils.setFontStyle(detailInfoTexts[i], FontUtils.BOLD);
			relativeForInfo.addView(detailInfoTexts[i]);
			
			detailInfos[i] = new TextView(mContext);
			
			thisViewResId = getResources().getIdentifier("carDetailPage_detailInfoText" + (i + 1), 
					"id", "com.byecar.byecarplus");
			lastViewResId = getResources().getIdentifier("carDetailPage_detailInfoText" + i, 
					"id", "com.byecar.byecarplus");
			textResId = getResources().getIdentifier("detailCarInfoString" + (i + 1), 
					"string", "com.byecar.byecarplus");
			
			ResizeUtils.viewResizeForRelative(LayoutParams.WRAP_CONTENT, 50, detailInfos[i], 
					new int[]{RelativeLayout.ALIGN_PARENT_RIGHT, i==0?RelativeLayout.ALIGN_PARENT_TOP:RelativeLayout.BELOW}, 
					new int[]{0, i==0?0:lastViewResId}, 
					new int[]{0, i==0?18:0, 18, 18});
			detailInfos[i].setId(thisViewResId);
			detailInfos[i].setText(null);
			detailInfos[i].setTextColor(getResources().getColor(R.color.holo_text));
			detailInfos[i].setGravity(Gravity.CENTER_VERTICAL);
			FontUtils.setFontSize(detailInfos[i], 28);
			FontUtils.setFontStyle(detailInfos[i], FontUtils.BOLD);
			relativeForInfo.addView(detailInfos[i]);
		}
	}
	
	public void showRelativeForType() {
		
		headerForType.setVisibility(View.VISIBLE);
		arrowForType.setVisibility(View.VISIBLE);
		relativeForType.setVisibility(View.VISIBLE);
		lineForType.setVisibility(View.VISIBLE);
		footerForType.setVisibility(View.VISIBLE);
		
		relativeForDealer.setVisibility(View.GONE);
		btnComplete.setVisibility(View.GONE);
	}
	
	public void hideRelativeForType() {
		
		headerForType.setVisibility(View.GONE);
		arrowForType.setVisibility(View.GONE);
		relativeForType.setVisibility(View.GONE);
		lineForType.setVisibility(View.GONE);
		footerForType.setVisibility(View.GONE);
		
		relativeForDealer.setVisibility(View.VISIBLE);
		btnComplete.setVisibility(View.VISIBLE);
	}
	
	public void showInfo() {
		
		headerForInfo.setVisibility(View.VISIBLE);
		arrowForInfo.setVisibility(View.VISIBLE);
		relativeForInfo.setVisibility(View.GONE);
		footerForInfo.setVisibility(View.VISIBLE);
	}
	
	public void hideInfo() {
		
		headerForInfo.setVisibility(View.GONE);
		arrowForInfo.setVisibility(View.GONE);
		relativeForInfo.setVisibility(View.GONE);
		footerForInfo.setVisibility(View.GONE);
	}
	
	public void showOption() {
		
		headerForOption.setVisibility(View.VISIBLE);
		arrowForOption.setVisibility(View.VISIBLE);
		relativeForOption.setVisibility(View.GONE);
		footerForOption.setVisibility(View.VISIBLE);
	}
	
	public void hideOption() {
		
		headerForOption.setVisibility(View.GONE);
		arrowForOption.setVisibility(View.GONE);
		relativeForOption.setVisibility(View.GONE);
		footerForOption.setVisibility(View.GONE);
	}
	
	public void showDesc() {
		
		headerForDescription.setVisibility(View.VISIBLE);
		arrowForDescription.setVisibility(View.VISIBLE);
		tvDescription.setVisibility(View.GONE);
		footerForDescription.setVisibility(View.VISIBLE);
	}
	
	public void hideDesc() {
		
		headerForDescription.setVisibility(View.GONE);
		arrowForDescription.setVisibility(View.GONE);
		tvDescription.setVisibility(View.GONE);
		footerForDescription.setVisibility(View.GONE);
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
			
			switch(car.getType()) {
			
			case Car.TYPE_BID:
				url = BCPAPIs.CAR_BID_LIKE_URL;
				break;
				
			case Car.TYPE_DEALER:
				url = BCPAPIs.CAR_DEALER_LIKE_URL;
				break;
				
			case Car.TYPE_DIRECT_CERTIFIED:
				url = BCPAPIs.CAR_DIRECT_CERTIFIED_LIKE_URL;
				break;
				
			case Car.TYPE_DIRECT_NORMAL:
				url = BCPAPIs.CAR_DIRECT_NORMAL_LIKE_URL;
				break;
			
			}
		} else {
			btnLike.setBackgroundResource(R.drawable.main_like_btn_a);
			car.setLikes_cnt(car.getLikes_cnt() - 1);
			car.setIs_liked(0);
			
			switch(car.getType()) {
			
			case Car.TYPE_BID:
				url = BCPAPIs.CAR_BID_UNLIKE_URL;
				break;
				
			case Car.TYPE_DEALER:
				url = BCPAPIs.CAR_DEALER_UNLIKE_URL;
				break;
				
			case Car.TYPE_DIRECT_CERTIFIED:
				url = BCPAPIs.CAR_DIRECT_CERTIFIED_UNLIKE_URL;
				break;
				
			case Car.TYPE_DIRECT_NORMAL:
				url = BCPAPIs.CAR_DIRECT_NORMAL_UNLIKE_URL;
				break;
			}
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
				ToastUtils.showToast(R.string.failToDeleteCar);
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
					ToastUtils.showToast(R.string.failToDeleteCar);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					ToastUtils.showToast(R.string.failToDeleteCar);
				}
			}
		}, mActivity.getLoadingView());
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
						progressBar.setProgress(1000);
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
			        	
			        	if(remainTime < 0) {
			        		updateMainCarInfo();
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
						updateMainCarInfo();
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
			
			//관리자에 의해 보류된 경우.
			if(event.equals("auction_held")) {
				closePage(R.string.holdOffByAdmin2);
				
			//경매가 시작되는 물건이 있는 경우.
			//경매 매물의 가격 변화가 있는 경우.
			//딜러 선택 시간이 종료된 경우 (유찰).
			//유저가 딜러를 선택한 경우 (낙찰).
			} else {
				this.car.copyValuesFromNewItem(car);
				updateMainCarInfo();
			}
		}
	}
}
