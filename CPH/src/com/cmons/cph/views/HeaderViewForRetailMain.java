package com.cmons.cph.views;

import java.util.ArrayList;

import org.json.JSONArray;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.RetailActivity;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Wholesale;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnStringDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.views.SpeedControllableViewPager;

public class HeaderViewForRetailMain extends RelativeLayout {

	private static int madeCount = 870901;
	
	private RetailActivity activity;
	
	private SpeedControllableViewPager viewPager;
	private ArrayList<Wholesale> wholesales = new ArrayList<Wholesale>();
	private PagerAdapterForWholesale pagerAdapter;
	private boolean needPlay;
	private boolean isPlaying;
	private boolean needWait;
	
	private TextView tvPhoneNumber;
	private View phoneNumberIcon;
	private TextView tvLocation;
	private View locationIcon;
	private TextView tvHit;
	private View hitIcon;
	private TextView tvLike;
	private View likeIcon;
	private TextView tvPartner;
	private View partnerIcon;
	private Button btnCategoryIndex;
	private Button btnOrder;
	private TextView tvTotalProduct;
	private View totalProductIcon;
	private TextView tvRetailName;
	
	public HeaderViewForRetailMain(Context context) {
		this(context, null, 0);
	}
	
	public HeaderViewForRetailMain(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public HeaderViewForRetailMain(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	public void init(RetailActivity activity) {
		
		this.activity = activity;
		
		createViews();
		setListeners();
	}
	
	public void createViews() {

		RelativeLayout.LayoutParams rp = null;
		
		madeCount += 10;
		
		//viewPager.					id : 0
		viewPager = new SpeedControllableViewPager(getContext());
		rp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 
				ResizeUtils.getSpecificLength(440));
		viewPager.setLayoutParams(rp);
		viewPager.setId(madeCount);
		viewPager.setBackgroundResource(R.drawable.picture_default);
		addView(viewPager);
		
		pagerAdapter = new PagerAdapterForWholesale();
		viewPager.setAdapter(pagerAdapter);
		
		//tvPhoneNumber.
		tvPhoneNumber = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 
				ResizeUtils.getSpecificLength(40));
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		tvPhoneNumber.setLayoutParams(rp);
		tvPhoneNumber.setTextColor(Color.WHITE);
		tvPhoneNumber.setGravity(Gravity.CENTER_VERTICAL);
		tvPhoneNumber.setPadding(ResizeUtils.getSpecificLength(40), 0, 0, 0);
		FontUtils.setFontSize(tvPhoneNumber, 20);
		tvPhoneNumber.setBackgroundResource(R.drawable.myshop_pic_cell);
		addView(tvPhoneNumber);
		
		//phoneNumberIcon.
		phoneNumberIcon = new View(getContext());
		rp = new RelativeLayout.LayoutParams(
				ResizeUtils.getSpecificLength(22), 
				ResizeUtils.getSpecificLength(20));
		rp.leftMargin = ResizeUtils.getSpecificLength(12);
		rp.topMargin = ResizeUtils.getSpecificLength(10);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		phoneNumberIcon.setLayoutParams(rp);
		phoneNumberIcon.setBackgroundResource(R.drawable.myshop_tell_icon);
		addView(phoneNumberIcon);

		//tvLocation.				id : 1
		tvLocation = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, 
				ResizeUtils.getSpecificLength(40));
		rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		tvLocation.setLayoutParams(rp);
		tvLocation.setId(madeCount + 1);
		tvLocation.setTextColor(Color.WHITE);
		tvLocation.setGravity(Gravity.CENTER_VERTICAL);
		tvLocation.setPadding(ResizeUtils.getSpecificLength(20), 0, 0, 0);
		FontUtils.setFontSize(tvLocation, 20);
		addView(tvLocation);
		
		//locationIcon.
		locationIcon = new View(getContext());
		rp = new RelativeLayout.LayoutParams(
				ResizeUtils.getSpecificLength(15), 
				ResizeUtils.getSpecificLength(20));
		rp.topMargin = ResizeUtils.getSpecificLength(10);
		rp.addRule(RelativeLayout.LEFT_OF, madeCount + 1);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		locationIcon.setLayoutParams(rp);
		locationIcon.setBackgroundResource(R.drawable.myshop_pin_icon);
		addView(locationIcon);
		
		//tvHit.					id : 2
		tvHit = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, 
				ResizeUtils.getSpecificLength(40));
		rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		tvHit.setLayoutParams(rp);
		tvHit.setId(madeCount + 2);
		tvHit.setTextColor(Color.WHITE);
		tvHit.setGravity(Gravity.CENTER_VERTICAL);
		tvHit.setPadding(0, 0, ResizeUtils.getSpecificLength(10), 0);
		FontUtils.setFontSize(tvHit, 20);
		addView(tvHit);

		//hitIcon.
		hitIcon = new View(getContext());
		rp = new RelativeLayout.LayoutParams(
				ResizeUtils.getSpecificLength(30), 
				ResizeUtils.getSpecificLength(20));
		rp.topMargin = ResizeUtils.getSpecificLength(10);
		rp.addRule(RelativeLayout.LEFT_OF, madeCount + 2);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		hitIcon.setLayoutParams(rp);
		hitIcon.setBackgroundResource(R.drawable.myshop_customer_icon);
		addView(hitIcon);

		//tvLike.
		tvLike = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, 
				ResizeUtils.getSpecificLength(40));
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.ALIGN_BOTTOM, madeCount);
		tvLike.setLayoutParams(rp);
		tvLike.setTextColor(Color.WHITE);
		tvLike.setGravity(Gravity.CENTER_VERTICAL);
		tvLike.setPadding(ResizeUtils.getSpecificLength(46), 0, 0, 0);
		FontUtils.setFontSize(tvLike, 20);
		tvLike.setBackgroundResource(R.drawable.myshop_pic_cell);
		addView(tvLike);
		
		//likeIcon.
		likeIcon = new View(getContext());
		rp = new RelativeLayout.LayoutParams(
				ResizeUtils.getSpecificLength(22), 
				ResizeUtils.getSpecificLength(20));
		rp.leftMargin = ResizeUtils.getSpecificLength(12);
		rp.bottomMargin = ResizeUtils.getSpecificLength(10);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.ALIGN_BOTTOM, madeCount);
		likeIcon.setLayoutParams(rp);
		likeIcon.setBackgroundResource(R.drawable.myshop_heart1_icon);
		addView(likeIcon);
		
		//tvPartner.				id : 3
		tvPartner = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, 
				ResizeUtils.getSpecificLength(40));
		rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rp.addRule(RelativeLayout.ALIGN_BOTTOM, madeCount);
		tvPartner.setLayoutParams(rp);
		tvPartner.setId(madeCount + 3);
		tvPartner.setTextColor(Color.WHITE);
		tvPartner.setGravity(Gravity.CENTER_VERTICAL);
		tvPartner.setPadding(0, 0, ResizeUtils.getSpecificLength(10), 0);
		FontUtils.setFontSize(tvPartner, 20);
		addView(tvPartner);
		
		//partnerIcon.
		partnerIcon = new View(getContext());
		rp = new RelativeLayout.LayoutParams(
				ResizeUtils.getSpecificLength(20), 
				ResizeUtils.getSpecificLength(20));
		rp.rightMargin = ResizeUtils.getSpecificLength(10);
		rp.bottomMargin = ResizeUtils.getSpecificLength(10);
		rp.addRule(RelativeLayout.LEFT_OF, madeCount + 3);
		rp.addRule(RelativeLayout.ALIGN_BOTTOM, madeCount);
		partnerIcon.setLayoutParams(rp);
		partnerIcon.setBackgroundResource(R.drawable.myshop_business_icon);
		addView(partnerIcon);
		
		//btnCategoryIndex.				id : 4
		btnCategoryIndex = new Button(getContext());
		rp = new RelativeLayout.LayoutParams(
				ResizeUtils.getSpecificLength(360), 
				ResizeUtils.getSpecificLength(92));
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.BELOW, madeCount);
		btnCategoryIndex.setLayoutParams(rp);
		btnCategoryIndex.setId(madeCount + 4);
		btnCategoryIndex.setTextColor(Color.WHITE);
		btnCategoryIndex.setGravity(Gravity.CENTER);
		btnCategoryIndex.setBackgroundResource(R.drawable.retail_main_drop1_btn);
		FontUtils.setFontSize(btnCategoryIndex, 24);
		addView(btnCategoryIndex);
		
		//btnOrder.
		btnOrder = new Button(getContext());
		rp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 
				ResizeUtils.getSpecificLength(92));
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 4);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 4);
		btnOrder.setLayoutParams(rp);
		btnOrder.setTextColor(Color.WHITE);
		btnOrder.setGravity(Gravity.CENTER);
		btnOrder.setBackgroundResource(R.drawable.retail_main_drop2_btn);
		FontUtils.setFontSize(btnOrder, 24);
		addView(btnOrder);
		
		View bottomBg = new View(getContext());
		rp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, 
				ResizeUtils.getSpecificLength(40));
		rp.addRule(RelativeLayout.BELOW, madeCount + 4);
		bottomBg.setLayoutParams(rp);
		bottomBg.setBackgroundColor(Color.rgb(238, 141, 141));
		addView(bottomBg);
		
		//tvTotalProduct.			id : 5
		tvTotalProduct = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, 
				ResizeUtils.getSpecificLength(40));
		rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		rp.addRule(RelativeLayout.BELOW, madeCount + 4);
		tvTotalProduct.setLayoutParams(rp);
		tvTotalProduct.setId(madeCount + 5);
		tvTotalProduct.setGravity(Gravity.CENTER);
		tvTotalProduct.setTextColor(Color.WHITE);
		tvTotalProduct.setPadding(ResizeUtils.getSpecificLength(20), 0, 0, 0);
		FontUtils.setFontSize(tvTotalProduct, 20);
		addView(tvTotalProduct);
		
		//totalProductIcon.
		totalProductIcon = new View(getContext());
		rp = new RelativeLayout.LayoutParams(
				ResizeUtils.getSpecificLength(20), 
				ResizeUtils.getSpecificLength(20));
		rp.rightMargin = ResizeUtils.getSpecificLength(10);
		rp.topMargin = ResizeUtils.getSpecificLength(10);
		rp.addRule(RelativeLayout.LEFT_OF, madeCount + 5);
		rp.addRule(RelativeLayout.BELOW, madeCount + 4);
		totalProductIcon.setLayoutParams(rp);
		totalProductIcon.setBackgroundResource(R.drawable.myshop_goods_icon);
		addView(totalProductIcon);
		
		//tvRetailName.
		tvRetailName = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT);
		rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		rp.addRule(RelativeLayout.ALIGN_BOTTOM, madeCount);
		rp.bottomMargin = ResizeUtils.getSpecificLength(100);
		tvRetailName.setLayoutParams(rp);
		tvRetailName.setTextColor(Color.WHITE);
		FontUtils.setFontSize(tvRetailName, 32);
		FontUtils.setShadow(tvRetailName, new float[]{
				ResizeUtils.getSpecificLength(6),
				ResizeUtils.getSpecificLength(-2),
				ResizeUtils.getSpecificLength(-4),
				255, 0, 0, 0});
		addView(tvRetailName);
	}
	
	public void setValues() {

		btnCategoryIndex.setText("전체");
		btnOrder.setText("최신순");
		
		downloadWholesales();
	}

	public void downloadWholesales() {
		
		wholesales.clear();
		pagerAdapter.notifyDataSetChanged();
		
		String url = CphConstants.BASE_API_URL + "wholesales/coverflow";
		
		DownloadUtils.downloadString(url, new OnStringDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("HeaderViewForRetailShop.onError." + "\nurl : " + url);
			}

			@Override
			public void onCompleted(String url, String result) {

				try {
					LogUtils.log("HeaderViewForRetailShop.onCompleted." + "\nurl : " + url
							+ "\nresult : " + result);

					JSONArray arJSON = new JSONArray(result);
					
					int size = arJSON.length();
					for(int i=0; i<size; i++) {
						wholesales.add(new Wholesale(arJSON.getJSONObject(i)));
					}
					
					pagerAdapter.notifyDataSetChanged();
					
					if(size > 0) {
						viewPager.setCurrentItem(0);
						tvPhoneNumber.setText(wholesales.get(0).getPhone_number());
						tvLocation.setText(wholesales.get(0).getLocation());
						tvHit.setText(wholesales.get(0).getToday_visited_cnt() + 
								" / " + wholesales.get(0).getTotal_visited_cnt());
						tvLike.setText("" + wholesales.get(0).getFavorited_cnt());
						tvPartner.setText("" + wholesales.get(0).getCustomers_cnt());
						tvRetailName.setText(wholesales.get(0).getName());
						
						needPlay = true;
						
						if(!isPlaying) {
							playPager();
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
	
	public void setListeners() {

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(final int position) {

				tvPhoneNumber.setText("010-" + wholesales.get(position).getPhone_number());
				tvLocation.setText("청평화몰 " + wholesales.get(position).getLocation());
				tvHit.setText(wholesales.get(position).getToday_visited_cnt() + 
						" / " + wholesales.get(position).getTotal_visited_cnt());
				tvLike.setText("" + wholesales.get(position).getFavorited_cnt());
				tvPartner.setText("" + wholesales.get(position).getCustomers_cnt());
				tvRetailName.setText(wholesales.get(position).getName());
				
				needWait = true;
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
	
	public Button getBtnCategoryIndex() {
		
		return btnCategoryIndex;
	}
	
	public Button getBtnOrder() {
		
		return btnOrder;
	}

	public void playPager() {
		
		isPlaying = true;
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {

				if(!needPlay) {
					isPlaying = false;
					return;
				} else if(needWait) {
					needWait = false;
					playPager();
					return;
				}
				
				if(wholesales != null &&wholesales.size() > 0) {
					int position = viewPager.getCurrentItem();
					viewPager.setCurrentItem((position + 1) % wholesales.size(), true);
				}
				
				playPager();
			}
		}, 3000);
	}

	public void setPlaying(boolean isPlaying) {
		
		this.isPlaying = isPlaying;
	}

	public void setTotalProduct(int totalProduct) {
		
		tvTotalProduct.setText("총 등록 상품 " +  totalProduct);
	}
	
////////////////////Custom classes.
	
	public class PagerAdapterForWholesale extends PagerAdapter {

		@Override
		public int getCount() {

			return wholesales.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			final int index = position % wholesales.size();
			final String imageUrl = wholesales.get(index).getRep_image_url();

			final ImageView ivImage = new ImageView(getContext());
			ivImage.setScaleType(ScaleType.CENTER_CROP);
			ivImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					if(activity != null) {
						Bundle bundle = new Bundle();
						bundle.putSerializable("wholesale", wholesales.get(index));
						activity.showPage(CphConstants.PAGE_RETAIL_SHOP, bundle);
					}
				}
			});
			container.addView(ivImage);

			if(!StringUtils.isEmpty(imageUrl)) {
				DownloadUtils.downloadBitmap(imageUrl,
						new OnBitmapDownloadListener() {

							@Override
							public void onError(String url) {

								LogUtils.log("instantiateItem.onError."
										+ "\nurl : " + url);
							}

							@Override
							public void onCompleted(String url, Bitmap bitmap) {

								try {
									LogUtils.log("instantiateItem.onCompleted."
											+ "\nurl : " + url);
									
									if(bitmap == null) {
										return;
									}
									
									if(!StringUtils.isEmpty(url)) {
										ivImage.setImageBitmap(bitmap);
									}
								} catch (Exception e) {
									LogUtils.trace(e);
								} catch (OutOfMemoryError oom) {
									LogUtils.trace(oom);
								}
							}
						});
			} else {
				ivImage.setImageResource(R.drawable.picture_default);
			}
			
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
	
	public void refreshValues(RetailActivity activity) {

		this.activity = activity;
		setValues();
	}
}

