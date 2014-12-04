package com.byecar.byecarplus.fragments;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.byecarplus.classes.BCPFragmentForMainForUser;
import com.byecar.byecarplus.views.TitleBar;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.OffsetScrollView;
import com.outspoken_kid.views.OffsetScrollView.OnScrollChangedListener;
import com.outspoken_kid.views.PageNavigatorView;

public class MainForUserPage extends BCPFragmentForMainForUser {
	
	private OffsetScrollView scrollView;
	private ViewPager viewPager;
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
	private LinearLayout usedMarketLinear;
	private UsedCarFrame[] usedCarFrames = new UsedCarFrame[3];
	private Button btnUsedMarket;
	private ImageView ivDirectMarket;
	private Button btnDirectMarket;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.mainForUserPage_titleBar);
		
		scrollView = (OffsetScrollView) mThisView.findViewById(R.id.mainForUserPage_scrollView);
		viewPager = (ViewPager) mThisView.findViewById(R.id.mainForUserPage_viewPager);
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
		usedMarketLinear = (LinearLayout) mThisView.findViewById(R.id.mainForUserPage_usedMarketLinear);
		btnUsedMarket = (Button) mThisView.findViewById(R.id.mainForUserPage_btnUsedMarket);
		ivDirectMarket = (ImageView) mThisView.findViewById(R.id.mainForUserPage_ivDirectMarket);
		btnDirectMarket = (Button) mThisView.findViewById(R.id.mainForUserPage_btnDirectMarket);
	}

	@Override
	public void setVariables() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createPage() {

		titleBar.setBgColor(Color.WHITE);
		titleBar.setBgAlpha(0);
		
		pageNavigator.setSize(3);
		pageNavigator.setIndex(0);
		pageNavigator.setEmptyOffCircle();
		pageNavigator.invalidate();
		
		addUsedCarFrames();

		setInfoTexts();
		setUsedCarFrames();
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
				
				if(offset < 500) {
					titleBar.setBgAlpha(0.002f * offset);
					
				} else if(offset < 700){
					titleBar.setBgAlpha(1);
				} else {
					//Do nothing.
				}
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		
		//viewPager.
		rp = (RelativeLayout.LayoutParams) viewPager.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(500);
		
		//pageNavigator.
		rp = (RelativeLayout.LayoutParams) pageNavigator.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(16);
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);
		
		//auctionIcon.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.mainForUserPage_auctionIcon).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(96);
		rp.height = ResizeUtils.getSpecificLength(96);
		rp.rightMargin = ResizeUtils.getSpecificLength(12);
		rp.bottomMargin = ResizeUtils.getSpecificLength(18);
		
		//remainBg.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.mainForUserPage_remainBg).getLayoutParams();
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
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.mainForUserPage_timeIcon).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(18);
		rp.height = ResizeUtils.getSpecificLength(18);
		rp.topMargin = ResizeUtils.getSpecificLength(6);
		rp.rightMargin = ResizeUtils.getSpecificLength(5);
		
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
		
		//usedMarketTitle.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.mainForUserPage_usedMarketTitle).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(626);
		rp.height = ResizeUtils.getSpecificLength(342);
		rp.topMargin = ResizeUtils.getSpecificLength(9);
		
		//usedMarketLinear.
		rp = (RelativeLayout.LayoutParams) usedMarketLinear.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(626);
		rp.height = ResizeUtils.getSpecificLength(246);
		rp.topMargin = ResizeUtils.getSpecificLength(92);
		
		//btnUsedMarket.
		rp = (RelativeLayout.LayoutParams) btnUsedMarket.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(626);
		rp.height = ResizeUtils.getSpecificLength(69);
		
		//directMarketTitle.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.mainForUserPage_directMarketTitle).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(626);
		rp.height = ResizeUtils.getSpecificLength(89);
		rp.topMargin = ResizeUtils.getSpecificLength(9);
		
		//ivDirectMarket.
		rp = (RelativeLayout.LayoutParams) ivDirectMarket.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(626);
		rp.height = ResizeUtils.getSpecificLength(248);
		
		//btnDirectMarket.
		rp = (RelativeLayout.LayoutParams) btnDirectMarket.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(626);
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
	}
	
//////////////////// Custom methods.
	
	public void addUsedCarFrames() {
		
		int[] bgResIds = new int[]{
				R.drawable.b1,
				R.drawable.b2,
				R.drawable.b3,
		};
		
		for(int i=0; i<3; i++) {
			usedCarFrames[i] = new UsedCarFrame(mContext, i);
			usedMarketLinear.addView(usedCarFrames[i]);
			
			usedCarFrames[i].getIvImage().setImageResource(bgResIds[i]);
		}
	}
	
	public void setInfoTexts() {
		
		tvRemainTime.setText("15 : 40 : 21");
		
		tvCarInfo1.setText("기아 소울 1.6 가솔린");
		tvCarInfo2.setText("2014년 / 60,452km / 서울 관악구");
		
		tvCurrentPrice.setText("55,000,000" + getString(R.string.won));
		tvBidCount.setText("입찰중 3명");
	}
	
	public void setUsedCarFrames() {
		
		usedCarFrames[0].setTexts("랜드로버", "레인지로버", 39000000);
		usedCarFrames[1].setTexts("현대", "제네시스", 18000000);
		usedCarFrames[2].setTexts("도요타", "캠리", 12000000);
	}
	
//////////////////// Custom classes.
	
	public class UsedCarFrame extends FrameLayout {

		private int index;
		private ImageView ivImage;
		private TextView tvCar;
		private TextView tvPrice;
		
		public UsedCarFrame(Context context, int index) {
			super(context);
			this.index = index;
			init();
		}
		
		public void init() {
		
			ResizeUtils.viewResize(185, 224, this, 1, Gravity.CENTER_VERTICAL, 
					new int[]{index==0?20:16, 0, 0, 0});
			
			//ivImage.
			ivImage = new ImageView(mContext);
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 145, ivImage, 2, 0, null);
			ivImage.setScaleType(ScaleType.CENTER_CROP);
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
			FontUtils.setFontSize(tvCar, 16);
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
			
		}
		
		public void setTexts(String companyName, String modelName, long price) {
			
			tvCar.setText(null);
			FontUtils.addSpan(tvCar, companyName + " ", Color.rgb(115, 115, 115), 1);
			FontUtils.addSpan(tvCar, modelName, Color.rgb(57, 57, 57), 1.2f);
			
			tvPrice.setText(StringUtils.getFormattedNumber(price) + getString(R.string.won));
		}

		public ImageView getIvImage() {
			
			return ivImage;
		}
	}
}
