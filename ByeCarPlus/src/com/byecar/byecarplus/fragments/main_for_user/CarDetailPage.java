package com.byecar.byecarplus.fragments.main_for_user;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.byecarplus.classes.BCPAPIs;
import com.byecar.byecarplus.classes.BCPConstants;
import com.byecar.byecarplus.classes.BCPFragment;
import com.byecar.byecarplus.classes.ImagePagerAdapter;
import com.byecar.byecarplus.models.Car;
import com.byecar.byecarplus.views.DealerView;
import com.byecar.byecarplus.views.TitleBar;
import com.outspoken_kid.classes.ViewUnbindHelper;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.OffsetScrollView;
import com.outspoken_kid.views.OffsetScrollView.OnScrollChangedListener;
import com.outspoken_kid.views.PageNavigatorView;

public class CarDetailPage extends BCPFragment {
	
	private int id;
	private Car car;
	
	private OffsetScrollView scrollView;

	private Button btnGuide;
	private Button btnBuy;
	
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
	
	private View headerForType;
	private View arrowForType;
	private RelativeLayout relativeForType;
	
	private View headerForInfo;
	private View arrowForInfo;
	private TextView tvDetailInfo1;
	private TextView tvDetailInfo2;
	private RelativeLayout relativeForInfo;
	
	private View headerForOption;
	private View arrowForOption;
	private RelativeLayout relativeForOption;
	
	private View headerForDescription;
	private View arrowForDescription;
	private TextView tvDescription;
	private View footerForDescription;
	
	private View[] carDetailPage_optionViews;
	
	private AlphaAnimation aaIn, aaOut;
	
	private ImagePagerAdapter imagePagerAdapter;
	
	private int type;
	private int scrollOffset;
	private int standardLength;
	private float diff;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.carDetailPage_titleBar);
		
		scrollView = (OffsetScrollView) mThisView.findViewById(R.id.carDetailPage_scrollView);
		
		btnBuy = (Button) mThisView.findViewById(R.id.carDetailPage_btnBuy);
		btnGuide = (Button) mThisView.findViewById(R.id.carDetailPage_btnGuide);
		
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
		
		headerForType = mThisView.findViewById(R.id.carDetailPage_headerForType);
		arrowForType = mThisView.findViewById(R.id.carDetailPage_arrowForType);
		relativeForType = (RelativeLayout) mThisView.findViewById(R.id.carDetailPage_relativeForType);
		
		headerForInfo = mThisView.findViewById(R.id.carDetailPage_headerForInfo);
		arrowForInfo = mThisView.findViewById(R.id.carDetailPage_arrowForInfo);
		relativeForInfo = (RelativeLayout) mThisView.findViewById(R.id.carDetailPage_relativeForInfo);
		tvDetailInfo1 = (TextView) mThisView.findViewById(R.id.carDetailPage_tvDetailInfo1);
		tvDetailInfo2 = (TextView) mThisView.findViewById(R.id.carDetailPage_tvDetailInfo2);
		
		headerForOption = mThisView.findViewById(R.id.carDetailPage_headerForOption);
		arrowForOption = mThisView.findViewById(R.id.carDetailPage_arrowForOption);
		relativeForOption = (RelativeLayout) mThisView.findViewById(R.id.carDetailPage_relativeForOption);
		
		headerForDescription = mThisView.findViewById(R.id.carDetailPage_headerForDescription);
		arrowForDescription = mThisView.findViewById(R.id.carDetailPage_arrowForDescription);
		tvDescription = (TextView) mThisView.findViewById(R.id.carDetailPage_tvDescription);
		footerForDescription = mThisView.findViewById(R.id.carDetailPage_footerForDescription);
	}

	@Override
	public void setVariables() {

		aaIn = new AlphaAnimation(0, 1);
		aaIn.setDuration(300);
		
		aaOut = new AlphaAnimation(1, 0);
		aaOut.setDuration(300);
		
		if(getArguments() != null) {
			
			if(getArguments().containsKey("car")) {
				this.car = (Car) getArguments().getSerializable("car");
			} else if(getArguments().containsKey("id")) {
				this.id = getArguments().getInt("id");
			} else {
				closePage();
			}
			
			if(getArguments().containsKey("type")) {
				this.type = getArguments().getInt("type");
			}
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
		
		scrollView.setOnTouchListener(new OnTouchListener() {
			
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if(car != null && type != Car.TYPE_BID) {
					
					switch(event.getAction()) {
					
					case MotionEvent.ACTION_MOVE:
						
						if(btnBuy.getVisibility() == View.VISIBLE) {
							btnBuy.setVisibility(View.INVISIBLE);
							btnBuy.startAnimation(aaOut);
						}
						break;
						
					case MotionEvent.ACTION_UP:
					case MotionEvent.ACTION_CANCEL:
						if(btnBuy.getVisibility() != View.VISIBLE) {
							btnBuy.setVisibility(View.VISIBLE);
							btnBuy.startAnimation(aaIn);
						}
						break;
					}
				}
				
				return false;
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
		
		headerForType.setOnClickListener(new OnClickListener() {

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

				Bundle bundle = new Bundle();
				bundle.putInt("type", EditUserInfoPage.TYPE_REQUEST_BUYER);
				mActivity.showPage(BCPConstants.PAGE_EDIT_USER_INFO, bundle);
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
		
		//btnBuy.
		rp = (RelativeLayout.LayoutParams) btnBuy.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(120);
		rp.height = ResizeUtils.getSpecificLength(120);
		
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
		rp.topMargin = ResizeUtils.getSpecificLength(6);
		rp.rightMargin = ResizeUtils.getSpecificLength(5);
		
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
		
		FontUtils.setFontSize(tvDetailInfo1, 20);
		FontUtils.setFontStyle(tvDetailInfo1, FontUtils.BOLD);
		tvDetailInfo1.setPadding(ResizeUtils.getSpecificLength(20), 
				ResizeUtils.getSpecificLength(6), 0, 0);
		tvDetailInfo1.setLineSpacing(0, 1.25f);
		
		FontUtils.setFontSize(tvDetailInfo2, 20);
		FontUtils.setFontStyle(tvDetailInfo2, FontUtils.BOLD);
		tvDetailInfo2.setPadding(0, ResizeUtils.getSpecificLength(4), ResizeUtils.getSpecificLength(20), 0);
		tvDetailInfo2.setLineSpacing(0, 0.577f);
		
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
		
		if(car != null) {
			setMainCarInfo();
			setDetailCarInfo();
			setOptionViews();
			setCarDescription();
		} else {
			downloadCarInfo();
		}
		
		checkPageScrollOffset();
	}

	@Override
	public void onDestroyView() {
		
		scrollView.setOnScrollChangedListener(null);
		viewPager.setOnPageChangeListener(null);
			
		int size = carDetailPage_optionViews.length;
		for(int i=0; i<size; i++) {
			ViewUnbindHelper.unbindReferences(carDetailPage_optionViews[i]);
		}
		
		super.onDestroyView();
	}
	
	@Override
	public int getRootViewResId() {

		return R.id.carDetailPage_mainLayout;
	}
	
//////////////////// Custom methods.

	public void addOptionViews() {
		
		relativeForOption.removeAllViews();
		
		int size = 30;
		carDetailPage_optionViews = new View[size];
		
		RelativeLayout.LayoutParams rp = null;
		

		for(int i=0; i<size; i++) {

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
			carDetailPage_optionViews[i] = getViewForOption(i);
			relativeForOption.addView(carDetailPage_optionViews[i]);
			
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

		String url = BCPAPIs.BID_SHOW_URL + "?onsalecar_id=" + id;
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("AuctionDetailPage.onError." + "\nurl : " + url);
				
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						downloadCarInfo();
					}
				}, 1000);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("AuctionDetailPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					car = new Car(objJSON.getJSONObject("onsalecar"));
					setMainCarInfo();
					setDetailCarInfo();
					setOptionViews();
					setCarDescription();
				} catch (Exception e) {
					LogUtils.trace(e);
					closePage();
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					closePage();
				}
			}
		});
	}
	
	public void setMainCarInfo() {
		
		tvRemainTime.setText("15 : 40 : 21");
		
		tvCarInfo1.setText(car.getCar_full_name());
		tvCarInfo2.setText(car.getYear() + "년 / "
				+ StringUtils.getFormattedNumber(car.getMileage()) + "km / "
				+ car.getArea());
		
		tvCurrentPrice.setText(StringUtils.getFormattedNumber(car.getPrice()) 
				+ getString(R.string.won));
		tvBidCount.setText("입찰중 " + car.getBids_cnt() + "명");

		imagePagerAdapter.setArrayList(car.getImages());
		viewPager.getAdapter().notifyDataSetChanged();
		viewPager.setCurrentItem(0);
		
		pageNavigator.setSize(car.getImages().size());
		pageNavigator.setEmptyOffCircle();
		pageNavigator.invalidate();
		
		switch(type) {
		
		case Car.TYPE_BID:
			addViewsForAuction();
			showDesc();
			break;
			
		case Car.TYPE_DEALER:
			addViewsForUsed();
			showDesc();
			break;
			
		case Car.TYPE_DIRECT_CERTIFIED:
			addViewsForDirectCertified();
			hideDesc();
			break;
			
		case Car.TYPE_DIRECT_NORMAL:
			addViewsForDirectNormal();
			hideDesc();
			break;
		}
	}
	
	public void setDetailCarInfo() {

		if(type == Car.TYPE_BID) {
			auctionIcon.setVisibility(View.VISIBLE);
			timeRelative.setVisibility(View.VISIBLE);
			tvBidCount.setVisibility(View.VISIBLE);
			
			btnGuide.setVisibility(View.VISIBLE);
			btnBuy.setVisibility(View.INVISIBLE);
		} else {
			auctionIcon.setVisibility(View.INVISIBLE);
			timeRelative.setVisibility(View.GONE);
			tvBidCount.setVisibility(View.INVISIBLE);
			
			btnGuide.setVisibility(View.INVISIBLE);
			btnBuy.setVisibility(View.VISIBLE);
		}
		
		int size = 12;
		
		String infoString = "\n" + getString(R.string.detailCarInfoString1);
		for(int i=1; i<size; i++) {

			int resId = getResources().getIdentifier("detailCarInfoString" + (i + 1), 
					"string", "com.byecar.byecarplus");
			infoString += "\n\n" + getString(resId);
			
			if(i == size - 1) {
				infoString += "\n";
			}
		}
		
		tvDetailInfo1.setText(infoString);
		tvDetailInfo2.setText(null);
		
		float scale = 1.5f;
		
		//제조사.
		FontUtils.addSpan(tvDetailInfo2, "\n" + car.getBrand_name() + "\n", 0, scale);
		
		//모델.
		FontUtils.addSpan(tvDetailInfo2, "\n\n" + car.getModel_name() + "\n", 0, scale);
		
		//세부모델.
		FontUtils.addSpan(tvDetailInfo2, "\n\n" + car.getTrim_name() + "\n", 0, scale);
		
		//연월식.
		FontUtils.addSpan(tvDetailInfo2, "\n\n" + car.getYear() + getString(R.string.year) + "\n", 0, scale);
		
		//차량번호.
		FontUtils.addSpan(tvDetailInfo2, "\n\n" + car.getCar_number() + "\n", 0, scale);
		
		//주행거리.
		FontUtils.addSpan(tvDetailInfo2, "\n\n" + StringUtils.getFormattedNumber(car.getMileage()) + "km" + "\n", 0, scale);
		
		//배기량.
		FontUtils.addSpan(tvDetailInfo2, "\n\n" + StringUtils.getFormattedNumber(car.getDisplacement()) + "cc" + "\n", 0, scale);
		
		//판매지역.
		FontUtils.addSpan(tvDetailInfo2, "\n\n" + car.getArea() + "\n", 0, scale);
		
		//변속기.
		FontUtils.addSpan(tvDetailInfo2, "\n\n" + 
				("manual".equals(car.getTransmission_type())? 
						getString(R.string.carSearchString_transmission2) :
							getString(R.string.carSearchString_transmission1))
				+ "\n", 0, scale);
		
		//연료.
		if("lpg".equals(car.getFuel_type())) {
			FontUtils.addSpan(tvDetailInfo2, "\n\n" + getString(R.string.carSearchString_fuel3) + "\n", 0, scale);
		} else if("diesel".equals(car.getFuel_type())) {
			FontUtils.addSpan(tvDetailInfo2, "\n\n" + getString(R.string.carSearchString_fuel2) + "\n", 0, scale);
		} else {
			FontUtils.addSpan(tvDetailInfo2, "\n\n" + getString(R.string.carSearchString_fuel1) + "\n", 0, scale);
		}
		
		//사고유무.
		if(car.getHad_accident() == 2) {
			FontUtils.addSpan(tvDetailInfo2, "\n\n" + getString(R.string.carSearchString_accident1) + "\n", 0, scale);
		} else if(car.getHad_accident() == 1) {
			FontUtils.addSpan(tvDetailInfo2, "\n\n" + getString(R.string.carSearchString_accident2) + "\n", 0, scale);
		} else {
			FontUtils.addSpan(tvDetailInfo2, "\n\n" + getString(R.string.carSearchString_accident3) + "\n", 0, scale);
		}
		
		//1인신조.
		if(car.getIs_oneman_owned() == 1) {
			FontUtils.addSpan(tvDetailInfo2, "\n\n" + getString(R.string.carSearchString_oneManOwned1) + "\n", 0, scale);
		} else {
			FontUtils.addSpan(tvDetailInfo2, "\n\n" + getString(R.string.carSearchString_oneManOwned2), 0, scale);
		}
	}

	public void setOptionViews() {

		if(car.getOptions() == null) {
			return;
		}
		
		for(int i=0; i<30; i++) {
			carDetailPage_optionViews[i].setBackgroundResource(
					getResources().getIdentifier("detail_optioin" + (i + 1) + "_btn_a", 
							"drawable", "com.byecar.byecarplus"));
		}
		
		int size = car.getOptions().length;
		for(int i=0; i<size; i++) {

			int index = car.getOptions()[i];
			carDetailPage_optionViews[index - 1].setBackgroundResource(
					getResources().getIdentifier("detail_optioin" + index + "_btn_b", 
							"drawable", "com.byecar.byecarplus"));
		}
	}
	
	public void setCarDescription() {
		
		tvDescription.setText(car.getDesc());
	}

	public void closePage() {
		
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				ToastUtils.showToast(R.string.failToLoadCarInfo);
				mActivity.closeTopPage();
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

		DealerView[] dealerViews = new DealerView[3];
		
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
						bundle.putInt("id", car.getBids().get(I).getDealer_id());
						mActivity.showPage(BCPConstants.PAGE_COMMON_DEALER_CERTIFIER, bundle);
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
		
		if(car.getSeller_id() != 0) {
			
			//ivImage.
			ImageView ivImage = new ImageView(mContext);
			ResizeUtils.viewResizeForRelative(130, 130, ivImage, 
					new int[]{RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.CENTER_HORIZONTAL}, 
					new int[]{0, 0}, 
					new int[]{0, 20, 0, 0});
			ivImage.setId(R.id.carDetailPage_used_ivImage);
			ivImage.setBackgroundResource(R.drawable.detail_default);
			ivImage.setScaleType(ScaleType.CENTER_CROP);
			relativeForType.addView(ivImage);
			
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
			FontUtils.addSpan(tvInfo1, car.getSeller_name(), 0, 1, true);
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
			tvInfo2.setText(car.getSeller_address() + "\n" + "소속 없음");
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
			tvGrade.setText("우수딜러");
			tvGrade.setTextColor(getResources().getColor(R.color.color_orange));
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
			relativeForType.addView(btnMoveToPage);
		}
	}
	
	public void addViewsForDirectCertified() {

		//이전 상태 모두 지우기.
		relativeForType.removeAllViews();
		
		headerForType.setBackgroundResource(R.drawable.verification_header1);
		relativeForType.setBackgroundResource(R.drawable.detail_body4);

		//ivImage.
		ImageView ivImage = new ImageView(mContext);
		ResizeUtils.viewResizeForRelative(130, 130, ivImage, 
				new int[]{RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.CENTER_HORIZONTAL}, 
				new int[]{0, 0}, 
				new int[]{0, 20, 0, 0});
		ivImage.setId(R.id.carDetailPage_certified_ivImage);
		ivImage.setBackgroundResource(R.drawable.detail_default);
		ivImage.setScaleType(ScaleType.CENTER_CROP);
		relativeForType.addView(ivImage);
		
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
		FontUtils.addSpan(tvInfo, car.getSeller_name(), 0, 1, true);
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
		tvDesc.setText("검증사 소개");
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
		relativeForType.addView(btnMoveToPage);
	}
	
	public void addViewsForDirectNormal() {
		
		//이전 상태 모두 지우기.
		relativeForType.removeAllViews();
		
		headerForType.setBackgroundResource(R.drawable.normal_direct_header);
		relativeForType.setBackgroundResource(R.drawable.detail_body4);
		
		//ivImage.
		ImageView ivImage = new ImageView(mContext);
		ResizeUtils.viewResizeForRelative(130, 130, ivImage, 
				new int[]{RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.CENTER_HORIZONTAL}, 
				new int[]{0, 0}, 
				new int[]{0, 20, 0, 0});
		ivImage.setId(R.id.carDetailPage_normal_ivImage);
		ivImage.setBackgroundResource(R.drawable.detail_default);
		ivImage.setScaleType(ScaleType.CENTER_CROP);
		relativeForType.addView(ivImage);
		
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
}
