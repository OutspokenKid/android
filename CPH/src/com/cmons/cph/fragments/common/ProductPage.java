package com.cmons.cph.fragments.common;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForShop;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Product;
import com.cmons.cph.models.Wholesale;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.classes.ViewUnbindHelper;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;

public class ProductPage extends CmonsFragmentForShop {

	private boolean isWholesale;
	private Wholesale wholesale;
	
    private TextView tvStatus;
    private TextView tvSoldOut;
    private View checkbox;
    private Button btnShop;
    private TextView tvImage;
    private ViewPager viewPager;
    private View arrowLeft;
    private View arrowRight;
    private TextView tvName;
    private TextView tvName2;
    private TextView tvPrice;
    private TextView tvPrice2;
    private TextView tvCategory;
    private TextView tvCategory2;
    private TextView tvColor;
    private TextView tvColor2;
    private TextView tvSize;
    private TextView tvSize2;
    private TextView tvMixtureRate;
    private TextView tvMixtureRate2;
    private TextView tvDescription;
    private TextView tvDescription2;
    private TextView tvPullUp;
    private TextView tvPullUp2;
    private Button btnPullUp;
    private Button btnBasket;
    private Button btnSample;
    private Button btnOrder;
    
    private Product product;
    private PagerAdapterForProducts pagerAdapter;
    private boolean isSoldOut;
    
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.productPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.productPage_ivBg);
		
		tvStatus = (TextView) mThisView.findViewById(R.id.productPage_tvStatus);
		tvSoldOut = (TextView) mThisView.findViewById(R.id.productPage_tvSoldOut);
		checkbox = mThisView.findViewById(R.id.productPage_checkbox);
		btnShop = (Button) mThisView.findViewById(R.id.productPage_btnShop);
		tvImage = (TextView) mThisView.findViewById(R.id.productPage_tvImage);
		viewPager = (ViewPager) mThisView.findViewById(R.id.productPage_viewPager);
		arrowLeft = mThisView.findViewById(R.id.productPage_arrowLeft);
		arrowRight = mThisView.findViewById(R.id.productPage_arrowRight);
		tvName = (TextView) mThisView.findViewById(R.id.productPage_tvName);
		tvName2 = (TextView) mThisView.findViewById(R.id.productPage_tvName2);
		tvPrice = (TextView) mThisView.findViewById(R.id.productPage_tvPrice);
		tvPrice2 = (TextView) mThisView.findViewById(R.id.productPage_tvPrice2);
		tvCategory = (TextView) mThisView.findViewById(R.id.productPage_tvCategory);
		tvCategory2 = (TextView) mThisView.findViewById(R.id.productPage_tvCategory2);
		tvColor = (TextView) mThisView.findViewById(R.id.productPage_tvColor);
		tvColor2 = (TextView) mThisView.findViewById(R.id.productPage_tvColor2);
		tvSize = (TextView) mThisView.findViewById(R.id.productPage_tvSize);
		tvSize2 = (TextView) mThisView.findViewById(R.id.productPage_tvSize2);
		tvMixtureRate = (TextView) mThisView.findViewById(R.id.productPage_tvMixtureRate);
		tvMixtureRate2 = (TextView) mThisView.findViewById(R.id.productPage_tvMixtureRate2);
		tvDescription = (TextView) mThisView.findViewById(R.id.productPage_tvDescription);
		tvDescription2 = (TextView) mThisView.findViewById(R.id.productPage_tvDescription2);
		tvPullUp = (TextView) mThisView.findViewById(R.id.productPage_tvPullUp);
		tvPullUp2 = (TextView) mThisView.findViewById(R.id.productPage_tvPullUp2);
		btnPullUp = (Button) mThisView.findViewById(R.id.productPage_btnPullUp);
		btnBasket = (Button) mThisView.findViewById(R.id.productPage_btnBasket);
		btnSample = (Button) mThisView.findViewById(R.id.productPage_btnSample);
		btnOrder = (Button) mThisView.findViewById(R.id.productPage_btnOrder);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {
			product = (Product) getArguments().getSerializable("product");
			isWholesale = getArguments().getBoolean("isWholesale");
		}
		
		if(product != null) {
			title = product.getName();
			
			//isSoldOut 설정.
			//isPublic 설정.
			//needPush 설정.
		} else {
			title = "상품 상세 페이지";
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		titleBar.getHomeButton().setVisibility(View.VISIBLE);

		if(product != null) {
			pagerAdapter = new PagerAdapterForProducts();
			viewPager.setAdapter(pagerAdapter);
			
			if(product.getStatus() == -1) {
				isSoldOut = true;
				checkbox.setBackgroundResource(R.drawable.myshop_checkbox_b);
				tvStatus.setText(R.string.productSoldOut);
				
				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					btnBasket.setAlpha(0.5f);
					btnBasket.setEnabled(false);
					
					btnSample.setAlpha(0.5f);
					btnSample.setEnabled(false);
					
					btnOrder.setAlpha(0.5f);
					btnOrder.setEnabled(false);
				}
			} else{
				isSoldOut = false;
				checkbox.setBackgroundResource(R.drawable.myshop_checkbox_a);
				tvStatus.setText(R.string.productSelling);
			}
			
			tvName2.setText(product.getName());
			tvPrice2.setText(product.getPrice() + "원");
			tvCategory2.setText("");
			tvColor2.setText(product.getColors());
			tvSize2.setText(product.getSizes());
			tvMixtureRate2.setText(product.getMixture_rate());
			tvDescription2.setText(product.getDesc());
			
			if(isWholesale) {
				tvSoldOut.setVisibility(View.VISIBLE);
				checkbox.setVisibility(View.VISIBLE);
				tvPullUp.setVisibility(View.VISIBLE);
				tvPullUp2.setVisibility(View.VISIBLE);
				btnPullUp.setVisibility(View.VISIBLE);
				
				btnShop.setVisibility(View.GONE);
				btnBasket.setVisibility(View.GONE);
				btnSample.setVisibility(View.GONE);
				btnOrder.setVisibility(View.GONE);
			} else {
				tvSoldOut.setVisibility(View.GONE);
				checkbox.setVisibility(View.GONE);
				tvPullUp.setVisibility(View.GONE);
				tvPullUp2.setVisibility(View.GONE);
				btnPullUp.setVisibility(View.GONE);
				
				btnShop.setVisibility(View.VISIBLE);
				btnBasket.setVisibility(View.VISIBLE);
				btnSample.setVisibility(View.VISIBLE);
				btnOrder.setVisibility(View.VISIBLE);
			}
		}
		
		loadWholesale();
	}

	@Override
	public void setListeners() {
		
		if(product != null) {
			titleBar.getBtnReply().setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {

				}
			});

			if(isWholesale) {
				titleBar.getBtnEdit().setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {

						Bundle bundle = new Bundle();
						bundle.putSerializable("product", product);
						mActivity.showPage(CphConstants.PAGE_WHOLESALE_WRITE, bundle);
					}
				});
			} else {
				titleBar.getBtnFavorite().setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {

						addFavorite();
					}
				});
			}
			
			tvStatus.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {

					isSoldOut = !isSoldOut;
					submitSoldOut();
				}
			});
			
			btnPullUp.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					pullUp();
				}
			});

			btnShop.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					goToShop();
				}
			});
			
			btnBasket.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					if(!isSoldOut) {
						addToBasket();
					} else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
						ToastUtils.showToast(R.string.productSoldOut);
					}					
				}
			});
			
			btnSample.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					if(!isSoldOut && wholesale != null 
							&& wholesale.getSample_available() == 1) {
						requestSample();
					} else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
						if(isSoldOut) {
							ToastUtils.showToast(R.string.productSoldOut);
						} else {
							ToastUtils.showToast(R.string.forbiddenRequestSample);
						}
					}
				}
			});
			
			btnOrder.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					if(!isSoldOut) {
						order();
					} else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
						ToastUtils.showToast(R.string.productSoldOut);
					}	
				}
			});
			
			arrowLeft.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					int currentIndex = viewPager.getCurrentItem();
					
					if(currentIndex > 0) {
						viewPager.setCurrentItem(currentIndex - 1, true);
					}
				}
			});
			
			arrowRight.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					int currentIndex = viewPager.getCurrentItem();
					
					if(currentIndex < product.getProduct_images().length - 1) {
						viewPager.setCurrentItem(currentIndex + 1, true);
					}
				}
			});
		}
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;

		int padding = ResizeUtils.getSpecificLength(16);
		int titleTextHeight = ResizeUtils.getSpecificLength(100);
		int editTextHeight = ResizeUtils.getSpecificLength(92);
		int buttonHeight = ResizeUtils.getSpecificLength(180);

		//tvStatus.
		rp = (RelativeLayout.LayoutParams) tvStatus.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvStatus, 30);
		tvStatus.setPadding(padding, 0, padding, 0);
		
		//checkbox.
		rp = (RelativeLayout.LayoutParams) checkbox.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(44);
		rp.height = ResizeUtils.getSpecificLength(43);
		rp.topMargin = ResizeUtils.getSpecificLength(28);
		rp.rightMargin = ResizeUtils.getSpecificLength(20);
		
		//tvSoldOut.
		rp = (RelativeLayout.LayoutParams) tvSoldOut.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(43);
		
		//btnShop.
		rp = (RelativeLayout.LayoutParams) btnShop.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(142);
		rp.height = ResizeUtils.getSpecificLength(36);
		rp.topMargin = ResizeUtils.getSpecificLength(32);
		rp.rightMargin = ResizeUtils.getSpecificLength(20);
		
		//tvImage.
		rp = (RelativeLayout.LayoutParams) tvImage.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvImage, 30);
		tvImage.setPadding(padding, 0, 0, padding);

		//viewPager.
		rp = (RelativeLayout.LayoutParams) viewPager.getLayoutParams();
		rp.height = ResizeUtils.getScreenWidth();
		
		//arrowLeft.
		rp = (RelativeLayout.LayoutParams) arrowLeft.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(43);
		rp.height = ResizeUtils.getSpecificLength(66);
		rp.leftMargin = ResizeUtils.getSpecificLength(40);
		rp.topMargin = (ResizeUtils.getScreenWidth() - rp.height) / 2;
		
		//arrowRight.
		rp = (RelativeLayout.LayoutParams) arrowRight.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(43);
		rp.height = ResizeUtils.getSpecificLength(66);
		rp.topMargin = (ResizeUtils.getScreenWidth() - rp.height) / 2;
		rp.rightMargin = ResizeUtils.getSpecificLength(40);
		
		//tvName.
		rp = (RelativeLayout.LayoutParams) tvName.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvName, 30);
		tvName.setPadding(padding, 0, 0, padding);
		
		//tvName2.
		rp = (RelativeLayout.LayoutParams) tvName2.getLayoutParams();
		rp.height = editTextHeight;
		FontUtils.setFontSize(tvName2, 24);
		tvName2.setPadding(padding, 0, padding, 0);
		
		//tvPrice.
		rp = (RelativeLayout.LayoutParams) tvPrice.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvPrice, 30);
		tvPrice.setPadding(padding, 0, 0, padding);
		
		//tvPrice2.
		rp = (RelativeLayout.LayoutParams) tvPrice2.getLayoutParams();
		rp.height = editTextHeight;
		FontUtils.setFontSize(tvPrice2, 24);
		tvPrice2.setPadding(padding, 0, padding, 0);
		
		//tvCategory.
		rp = (RelativeLayout.LayoutParams) tvCategory.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvCategory, 30);
		tvCategory.setPadding(padding, 0, 0, padding);
		
		//tvCategory2.
		rp = (RelativeLayout.LayoutParams) tvCategory2.getLayoutParams();
		rp.height = editTextHeight;
		FontUtils.setFontSize(tvCategory2, 24);
		
		//tvColor.
		rp = (RelativeLayout.LayoutParams) tvColor.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvColor, 30);
		tvColor.setPadding(padding, 0, 0, padding);
		
		//tvColor2.
		rp = (RelativeLayout.LayoutParams) tvColor2.getLayoutParams();
		rp.height = editTextHeight;
		FontUtils.setFontSize(tvColor2, 24);
		
		//tvSize.
		rp = (RelativeLayout.LayoutParams) tvSize.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvSize, 30);
		tvSize.setPadding(padding, 0, 0, padding);
		
		//tvSize2.
		rp = (RelativeLayout.LayoutParams) tvSize2.getLayoutParams();
		rp.height = editTextHeight;
		FontUtils.setFontSize(tvSize2, 24);
		
		//tvMixtureRate.
		rp = (RelativeLayout.LayoutParams) tvMixtureRate.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvMixtureRate, 30);
		tvMixtureRate.setPadding(padding, 0, 0, padding);
		
		//tvMixtureRate2.
		rp = (RelativeLayout.LayoutParams) tvMixtureRate2.getLayoutParams();
		rp.height = editTextHeight;
		FontUtils.setFontSize(tvMixtureRate2, 24);
		tvMixtureRate2.setPadding(padding, 0, padding, 0);
		
		//tvDescription.
		rp = (RelativeLayout.LayoutParams) tvDescription.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvDescription, 30);
		tvDescription.setPadding(padding, 0, 0, padding);
		
		//tvDescription2.
		rp = (RelativeLayout.LayoutParams) tvDescription2.getLayoutParams();
		FontUtils.setFontSize(tvDescription2, 24);
		tvDescription2.setPadding(padding, padding, padding, padding);
		
		//tvPullUp.
		rp = (RelativeLayout.LayoutParams) tvPullUp.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvPullUp, 30);
		tvPullUp.setPadding(padding, 0, 0, padding);
		
		//tvPullUp2.
		rp = (RelativeLayout.LayoutParams) tvPullUp2.getLayoutParams();
		rp.height = titleTextHeight / 2;
		FontUtils.setFontSize(tvPullUp2, 20);
		tvPullUp2.setPadding(padding, 0, 0, padding);
		
		//btnPullUp.
		rp = (RelativeLayout.LayoutParams) btnPullUp.getLayoutParams();
		rp.height = buttonHeight;
		
		//btnBasket.
		rp = (RelativeLayout.LayoutParams) btnBasket.getLayoutParams();
		rp.width = ResizeUtils.getScreenWidth()/2;
		rp.height = buttonHeight;
		
		//btnSample.
		rp = (RelativeLayout.LayoutParams) btnSample.getLayoutParams();
		rp.height = buttonHeight;
		
		//btnOrder.
		rp = (RelativeLayout.LayoutParams) btnOrder.getLayoutParams();
		rp.height = buttonHeight;
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_common_product;
	}

	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onBackPressed() {

		return false;
	}

	@Override
	public boolean parseJSON(JSONObject objJSON) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getBgResourceId() {

		return R.drawable.myshop_bg;
	}
	
//////////////////// Custom methods.

	public void loadWholesale() {
		
		if(product != null) {
			String url = CphConstants.BASE_API_URL + "wholesales/show" +
					"?wholesale_id=" + product.getWholesale_id();
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("ProductPage.loadWholesale..onError." + "\nurl : " + url);

					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							loadWholesale();
						}
					}, 100);
				}

				@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("ProductPage.loadWholesale..onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						if(objJSON.getInt("result") == 1) {
							wholesale = new Wholesale(objJSON.getJSONObject("wholesale"));
							
							if(wholesale.getSample_available() != 1 
									&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
								btnSample.setAlpha(0.5f);
								btnSample.setEnabled(false);
							}
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
	
	public void pullUp() {
		
	}

	public void submitSoldOut() {
		
		String url = CphConstants.BASE_API_URL + "products/set_status" +
				"?product_id=" + product.getId();
		
		if(isSoldOut) {
			url += "&status=-1";
		} else {
			url += "&status=1";
		}

		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WholesaleForProductPage.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("WholesaleForProductPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					ToastUtils.showToast(objJSON.getString("message"));
					
					if(objJSON.getInt("result") == 1) {
						
						if(url.contains("&status=-1")) {
							tvStatus.setText(R.string.productSoldOut);
							checkbox.setBackgroundResource(R.drawable.myshop_checkbox_b);
							product.setStatus(-1);
						} else {
							tvStatus.setText(R.string.productSelling);
							checkbox.setBackgroundResource(R.drawable.myshop_checkbox_a);
							product.setStatus(1);
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

	public void goToShop() {

		if(wholesale != null) {
			Bundle bundle = new Bundle();
			bundle.putSerializable("wholesale", wholesale);
			mActivity.showPage(CphConstants.PAGE_RETAIL_SHOP, bundle);
		}
	}
	
	public void addToBasket() {

		Bundle bundle = new Bundle();
		bundle.putSerializable("product", product);
		bundle.putSerializable("wholesale", wholesale);
		bundle.putBoolean("isBasket", true);
		mActivity.showPage(CphConstants.PAGE_RETAIL_ORDER, bundle);
	}
	
	public void requestSample() {
		
	}
	
	public void order() {
		
		Bundle bundle = new Bundle();
		bundle.putSerializable("product", product);
		bundle.putSerializable("wholesale", wholesale);
		bundle.putBoolean("isBasket", false);
		mActivity.showPage(CphConstants.PAGE_RETAIL_ORDER, bundle);
	}

	public void addFavorite() {
		
		//http://cph.minsangk.com/retails/favorite/products/add?id=1
		String url = CphConstants.BASE_API_URL + "retails/favorite/products/add" +
				"?id=" + product.getId();
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("ProductPage.addFavorite.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToAddFavorite);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("ProductPage.addFavorite.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_addFavorite);
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					ToastUtils.showToast(R.string.failToAddFavorite);
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					ToastUtils.showToast(R.string.failToAddFavorite);
					LogUtils.trace(oom);
				}
			}
		});
	}
	
////////////////////Custom classes.

	public class PagerAdapterForProducts extends PagerAdapter {
		
		@Override
		public int getCount() {
			
			return product.getProduct_images().length;
		}
		
		@Override
		public Object instantiateItem(ViewGroup container, final int position) {

			final String imageUrl = product.getProduct_images()[position];
			
			LogUtils.log("###WholesaleProductPage.instantiateItem.  position : " + position + 
					", url : " + imageUrl);

			FrameLayout frame = new FrameLayout(mContext);
			
			final ImageView ivImage = new ImageView(mContext);
			ResizeUtils.viewResize(540, LayoutParams.MATCH_PARENT, ivImage, 2, Gravity.CENTER_HORIZONTAL, null);
			ivImage.setScaleType(ScaleType.CENTER_CROP);
			frame.addView(ivImage);
			
			container.addView(frame);

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
			
			return frame;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {

			try {
				View v = (View) object;
				container.removeView(v);
				ViewUnbindHelper.unbindReferences(v);
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
