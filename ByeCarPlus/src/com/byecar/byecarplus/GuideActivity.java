package com.byecar.byecarplus;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.byecar.classes.ImagePagerAdapter;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.views.PageNavigatorView;

public class GuideActivity extends Activity {

	public static final int TYPE_USER_AUCTION = 0;
	public static final int TYPE_USER_BUY = 1;
	public static final int TYPE_USER_DIRECT = 2;
	public static final int TYPE_USER_CERTIFIED = 3;
	public static final int TYPE_USER_NORMAL = 4;
	public static final int TYPE_USER_UPLOAD = 5;
	
	private int type;
	private int[] buttonResIds;
	private int[][] imageResIds;
	
	private Button btnBack;
	private LinearLayout menuLinear;
	private View bottomLine;
	private ArrayList<Button> menuButtons = new ArrayList<Button>();
	
	private ViewPager viewPager;
	private PageNavigatorView pageNavigator;
	private Button btnLeft;
	private Button btnRight;
	
	private ImagePagerAdapter imagePagerAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		
		bindViews();
		setVariables();
		
		setPage();
	}
	
	public void bindViews() {
		
		btnBack = (Button) findViewById(R.id.guideActivity_btnBack);
		menuLinear = (LinearLayout) findViewById(R.id.guideActivity_menuLinear);
		bottomLine = findViewById(R.id.guideActivity_bottomLine);
		
		viewPager = (ViewPager) findViewById(R.id.guideActivity_viewPager);
		pageNavigator = (PageNavigatorView) findViewById(R.id.guideActivity_pageNavigator);
		btnLeft = (Button) findViewById(R.id.guideActivity_btnLeft);
		btnRight = (Button) findViewById(R.id.guideActivity_btnRight);
	}
	
	public void setVariables() {

		type = getIntent().getIntExtra("type", 0);
		
		switch(type) {
		
		case TYPE_USER_AUCTION:
			buttonResIds = new int[]{
					R.drawable.infomation_auction_tab1_tab_a,
					R.drawable.infomation_auction_tab2_tab_a,
					R.drawable.infomation_auction_tab3_tab_a,
					R.drawable.infomation_auction_tab4_tab_a,
					R.drawable.infomation_auction_tab1_tab_b,
					R.drawable.infomation_auction_tab2_tab_b,
					R.drawable.infomation_auction_tab3_tab_b,
					R.drawable.infomation_auction_tab4_tab_b,
			};
			imageResIds = new int[][]{
					{
						R.drawable.infomation_auction1_img1,
						R.drawable.infomation_auction1_img2,
						R.drawable.infomation_auction1_img3,
					},
					
					{
						R.drawable.infomation_auction2_img1,
						R.drawable.infomation_auction2_img2,
						R.drawable.infomation_auction2_img3,
					},
					
					{
						R.drawable.infomation_auction3_img1,
						R.drawable.infomation_auction3_img2,
					},
					
					{
						R.drawable.infomation_auction4_img1,
						R.drawable.infomation_auction4_img2,
						R.drawable.infomation_auction4_img3,
					},
			};
			break;
			
		case TYPE_USER_BUY:
			buttonResIds = new int[]{
					R.drawable.information_buy_tab1_tab_a,
					R.drawable.information_buy_tab2_tab_a,
					R.drawable.information_buy_tab1_tab_b,
					R.drawable.information_buy_tab2_tab_b,
			};
			imageResIds = new int[][]{
					{
						R.drawable.information_buy_img1,
						R.drawable.information_buy_img2,
						R.drawable.information_buy_img3,
					},
					
					{
						R.drawable.information_buy2_img1,
						R.drawable.information_buy2_img2,
						R.drawable.information_buy2_img3,
					},
			};
			break;
			
		case TYPE_USER_DIRECT:
			imageResIds = new int[][]{
					{
						R.drawable.directmarket_information_img,
					},
			};
			break;

		case TYPE_USER_CERTIFIED:
			buttonResIds = new int[]{
					R.drawable.verification_information_tab1_tab_a,
					R.drawable.verification_information_tab2_tab_a,
					R.drawable.verification_information_tab1_tab_b,
					R.drawable.verification_information_tab2_tab_b,
			};
			imageResIds = new int[][]{
					{
						R.drawable.verification_information_img1,
						R.drawable.verification_information_img2,
						R.drawable.verification_information_img3,
						R.drawable.verification_information_img4,
					},
					
					{
						R.drawable.verification_information2_img1,
						R.drawable.verification_information2_img2,
					},
			};
			break;
			
		case TYPE_USER_NORMAL:
			buttonResIds = new int[]{
					R.drawable.normaldirect_information_tab1_tab_a,
					R.drawable.normaldirect_information_tab2_tab_a,
					R.drawable.normaldirect_information_tab1_tab_b,
					R.drawable.normaldirect_information_tab2_tab_b,
			};
			imageResIds = new int[][]{
					{
						R.drawable.normaldirect_information_img1,
						R.drawable.normaldirect_information_img2,
						R.drawable.normaldirect_information_img3,
					},
					
					{
						R.drawable.normaldirect_information2_img1,
						R.drawable.normaldirect_information2_img2,
					},
			};
			break;
			
		case TYPE_USER_UPLOAD:
			imageResIds = new int[][]{
					{
						R.drawable.directmarket_information_img2,
					},
			};
			break;
		}
	}
		
	public void setPage() {
		
		setTitleBar();
		setMenu();
		
		setMenuButtonBg(0);
		setPager(0);
	}
	
	public void setTitleBar() {

		findViewById(R.id.guideActivity_titleBg).getLayoutParams().height = ResizeUtils.getSpecificLength(88);

		int width = 0;
		
		switch(type) {
		
		case TYPE_USER_AUCTION:
			btnBack.setBackgroundResource(R.drawable.infomation_auction_back_btn);
			width = 271;
			break;
			
		case TYPE_USER_BUY:
			btnBack.setBackgroundResource(R.drawable.information_buy_back_btn);
			width = 268;
			break;
			
		case TYPE_USER_DIRECT:
			btnBack.setBackgroundResource(R.drawable.directmarket_information_back_btn);
			width = 344;
			break;

		case TYPE_USER_CERTIFIED:
			btnBack.setBackgroundResource(R.drawable.verification_information_back_btn);
			width = 348;
			break;
			
		case TYPE_USER_NORMAL:
			btnBack.setBackgroundResource(R.drawable.normaldirect_information_back_btn);
			width = 371;
			break;
			
		case TYPE_USER_UPLOAD:
			btnBack.setBackgroundResource(R.drawable.directmarket_reg_information_back_btn);
			width = 402;
			break;
		}
		
		RelativeLayout.LayoutParams rp = ((RelativeLayout.LayoutParams)btnBack.getLayoutParams());
		rp.width = ResizeUtils.getSpecificLength(width);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.leftMargin = ResizeUtils.getSpecificLength(8);
		rp.topMargin = ResizeUtils.getSpecificLength(14);
		
		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				finish();
			}
		});
	}
	
	public void setMenu() {
	
		if(buttonResIds != null) {
			findViewById(R.id.guideActivity_menuLinear).getLayoutParams().height = ResizeUtils.getSpecificLength(88);
			
			int size = buttonResIds.length / 2;
			for(int i=0; i<size; i++) {
				
				Button button = new Button(this);
				button.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1));
				menuButtons.add(button);
				menuLinear.addView(button);
				
				final int INDEX = i;
				button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {

						setMenuButtonBg(INDEX);
						setPager(INDEX);
					}
				});
			}
		} else {
			bottomLine.setVisibility(View.INVISIBLE);
		}
	}
	
	public void setMenuButtonBg(int index) {
		
		if(buttonResIds != null) {
			int size = buttonResIds.length / 2;
			for(int i=0; i<size; i++) {
				menuButtons.get(i).setBackgroundResource(
						buttonResIds[(i==index? 0 : size) + i]);
			}
		}
	}

	public void setPager(final int index) {
		
		if(imageResIds == null) {
			return;
		}
		
		if(imagePagerAdapter == null) {
			
			imagePagerAdapter = new ImagePagerAdapter(this);
			viewPager.setAdapter(imagePagerAdapter);
			viewPager.setOnPageChangeListener(new OnPageChangeListener() {
				
				@Override
				public void onPageSelected(int arg0) {

					pageNavigator.setIndex(arg0);
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
		
			//viewPager.
			RelativeLayout.LayoutParams rp = (RelativeLayout.LayoutParams) viewPager.getLayoutParams();
			rp.height = ResizeUtils.getSpecificLength(896);
			
			//pageNavigator.
			rp = (RelativeLayout.LayoutParams) pageNavigator.getLayoutParams();
			rp.height = ResizeUtils.getSpecificLength(16);
			rp.bottomMargin = ResizeUtils.getSpecificLength(700);
			
			//btnLeft.
			rp = (RelativeLayout.LayoutParams) btnLeft.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(40);
			rp.height = ResizeUtils.getSpecificLength(60);
			rp.leftMargin = ResizeUtils.getSpecificLength(15);
			
			btnLeft.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					if(viewPager.getCurrentItem() != 0) {
						viewPager.setCurrentItem(viewPager.getCurrentItem() - 1, true);
					}
				}
			});
			
			//btnRight.
			rp = (RelativeLayout.LayoutParams) btnRight.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(40);
			rp.height = ResizeUtils.getSpecificLength(60);
			rp.rightMargin = ResizeUtils.getSpecificLength(15);
			
			btnRight.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					if(viewPager.getCurrentItem() != imageResIds[index].length - 1) {
						viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
					}
				}
			});
		}
		
		if(imageResIds[index] != null &&imageResIds[index].length > 1) {
			
			btnLeft.setVisibility(View.VISIBLE);
			btnRight.setVisibility(View.VISIBLE);
			
			pageNavigator.setVisibility(View.VISIBLE);
			pageNavigator.setSize(imageResIds[index].length);
			pageNavigator.setColor(Color.rgb(254, 188, 42), Color.rgb(188, 188, 188));
			pageNavigator.invalidate();
			
		} else {
			btnLeft.setVisibility(View.INVISIBLE);
			btnRight.setVisibility(View.INVISIBLE);
			pageNavigator.setVisibility(View.INVISIBLE);
		}
		
		imagePagerAdapter.setImageResIds(imageResIds[index]);
		viewPager.getAdapter().notifyDataSetChanged();
		viewPager.setCurrentItem(0);
	}
}
