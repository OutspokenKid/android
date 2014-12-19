package com.byecar.byecarplus.common;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.byecarplus.classes.BCPAPIs;
import com.byecar.byecarplus.classes.BCPFragmentForMainForUser;
import com.byecar.byecarplus.models.Dealer;
import com.byecar.byecarplus.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.OffsetScrollView;
import com.outspoken_kid.views.OffsetScrollView.OnScrollChangedListener;

public class DealerPage extends BCPFragmentForMainForUser {

	private int id;
	private Dealer dealer;
	
	private OffsetScrollView scrollView;

	private View bg;
	private ImageView ivImage;
	
	private TextView tvDealerInfo1;
	private TextView tvDealerInfo2;
	
	private View headerForIntro;
	private View arrowForIntro;
	private TextView tvIntro;
	
	private View headerForReview;
	private View arrowForReview;
	private RelativeLayout relativeForReview;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.dealerPage_titleBar);
		
		scrollView = (OffsetScrollView) mThisView.findViewById(R.id.dealerPage_scrollView);
		bg = mThisView.findViewById(R.id.dealerPage_bg);
		ivImage = (ImageView) mThisView.findViewById(R.id.dealerPage_ivImage);
		
		tvDealerInfo1 = (TextView) mThisView.findViewById(R.id.dealerPage_tvDealerInfo1);
		tvDealerInfo2 = (TextView) mThisView.findViewById(R.id.dealerPage_tvDealerInfo2);

		headerForIntro = mThisView.findViewById(R.id.dealerPage_headerForIntro);
		arrowForIntro = mThisView.findViewById(R.id.dealerPage_arrowForIntro);
		tvIntro = (TextView) mThisView.findViewById(R.id.dealerPage_tvIntro);
		
		headerForReview = mThisView.findViewById(R.id.dealerPage_headerForReview);
		arrowForReview = mThisView.findViewById(R.id.dealerPage_arrowForReview);
		relativeForReview = (RelativeLayout) mThisView.findViewById(R.id.dealerPage_relativeForReview);
	}

	@Override
	public void setVariables() {
		
		if(getArguments() != null) {
			
			if(getArguments().containsKey("dealer")) {
				this.dealer = (Dealer) getArguments().getSerializable("dealer");
			} else if(getArguments().containsKey("id")) {
				this.id = getArguments().getInt("id");
			} else {
				closePage();
			}
		}
	}

	@Override
	public void createPage() {

		titleBar.setBgColor(Color.WHITE);
		titleBar.setBgAlpha(0);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public void setListeners() {

		scrollView.setOnScrollChangedListener(new OnScrollChangedListener() {
			
			@Override
			public void onScrollChanged(int offset) {

				try {
					if(offset < 500) {
						titleBar.setBgAlpha(0.002f * offset);
						
					} else if(offset < 700){
						titleBar.setBgAlpha(1);
					} else {
						//Do nothing.
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}
			}
		});
		
		headerForIntro.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				changeMenuOpenStatus(0);
			}
		});
		
		headerForReview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				changeMenuOpenStatus(1);
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;

		//ivImage.
		rp = (RelativeLayout.LayoutParams) ivImage.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(218);
		rp.height = ResizeUtils.getSpecificLength(218);
		rp.topMargin = ResizeUtils.getSpecificLength(136);
		
		//bg.
		rp = (RelativeLayout.LayoutParams) bg.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(493);
		
		//headerForIntro.
		rp = (RelativeLayout.LayoutParams) headerForIntro.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(632);
		rp.height = ResizeUtils.getSpecificLength(90);
		rp.topMargin = ResizeUtils.getSpecificLength(5);
		
		//arrowForIntro.
		rp = (RelativeLayout.LayoutParams) arrowForIntro.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(43);
		rp.height = ResizeUtils.getSpecificLength(43);
		rp.topMargin = ResizeUtils.getSpecificLength(24);
		rp.rightMargin = ResizeUtils.getSpecificLength(20);
		
		//tvIntro.
		rp = (RelativeLayout.LayoutParams) tvIntro.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(632);
		
		//footerForIntro.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.dealerPage_footerForIntro).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(632);
		rp.height = ResizeUtils.getSpecificLength(20);
		
		//headerForReview.
		rp = (RelativeLayout.LayoutParams) headerForReview.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(632);
		rp.height = ResizeUtils.getSpecificLength(90);
		rp.topMargin = ResizeUtils.getSpecificLength(5);
		
		//arrowForReview.
		rp = (RelativeLayout.LayoutParams) arrowForReview.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(43);
		rp.height = ResizeUtils.getSpecificLength(43);
		rp.topMargin = ResizeUtils.getSpecificLength(24);
		rp.rightMargin = ResizeUtils.getSpecificLength(20);
		
		//relativeForReview.
		rp = (RelativeLayout.LayoutParams) relativeForReview.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(632);
		
		//footerForReview.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.dealerPage_footerForReview).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(632);
		rp.height = ResizeUtils.getSpecificLength(20);
		
		FontUtils.setFontSize(tvDealerInfo1, 16);
		FontUtils.setFontStyle(tvDealerInfo1, FontUtils.BOLD);
		
		FontUtils.setFontSize(tvDealerInfo2, 32);
		FontUtils.setFontStyle(tvDealerInfo2, FontUtils.BOLD);
		
		FontUtils.setFontSize(tvIntro, 20);
		FontUtils.setFontStyle(tvIntro, FontUtils.BOLD);
		tvIntro.setPadding(
				ResizeUtils.getSpecificLength(20),
				ResizeUtils.getSpecificLength(20), 
				ResizeUtils.getSpecificLength(20), 
				ResizeUtils.getSpecificLength(20));
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_dealer;
	}

	@Override
	public int getBackButtonResId() {

		return R.drawable.dealer_back_btn;
	}

	@Override
	public int getBackButtonWidth() {

		return 213;
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
		
		if(dealer != null) {
			setDealerInfo();
			setIntro();
		} else {
			downloadCarInfo();
		}
	}
	
//////////////////// Custom methods.
	
	public void downloadCarInfo() {

		String url = BCPAPIs.BID_SHOW_URL + "?onsalecar_id=" + id;
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("DealerPage.onError." + "\nurl : " + url);
				
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
					LogUtils.log("DealerPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);
					
					dealer = new Dealer(objJSON.getJSONObject("dealer"));
					setDealerInfo();
					setIntro();
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
	
	public void setDealerInfo() {
		
		DownloadUtils.downloadBitmap(dealer.getProfile_img_url(), new OnBitmapDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("DealerPage.onError." + "\nurl : " + url);
			}

			@Override
			public void onCompleted(String url, Bitmap bitmap) {

				try {
					LogUtils.log("DealerPage.onCompleted." + "\nurl : " + url);
					
					if(ivImage != null && !bitmap.isRecycled()) {
						ivImage.setImageBitmap(bitmap);
					}
					
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
		
		tvDealerInfo1.setText(null);
		FontUtils.addSpan(tvDealerInfo1, dealer.getName(), 0, 2, true);
		FontUtils.addSpan(tvDealerInfo1, dealer.getAddress(), 0, 1, true);

		tvDealerInfo2.setText("★" + "우수딜러");
	}
	
	public void setIntro() {
		
		tvIntro.setText(dealer.getDesc());
	}

	public void closePage() {
		
		ToastUtils.showToast(R.string.failToLoadDealerInfo);
		mActivity.closeTopPage();
	}

	public void changeMenuOpenStatus(int index) {

		switch(index) {
		
		case 0:
			
			if(tvIntro.getVisibility() == View.VISIBLE) {
				tvIntro.setVisibility(View.GONE);
				arrowForIntro.setBackgroundResource(R.drawable.detail_toggle);
			} else {
				tvIntro.setVisibility(View.VISIBLE);
				arrowForIntro.setBackgroundResource(R.drawable.detail_toggle_up);
			}
			
			break;
			
		case 1:
			
			if(relativeForReview.getVisibility() == View.VISIBLE) {
				relativeForReview.setVisibility(View.GONE);
				arrowForReview.setBackgroundResource(R.drawable.detail_toggle);
			} else {
				relativeForReview.setVisibility(View.VISIBLE);
				arrowForReview.setBackgroundResource(R.drawable.detail_toggle_up);
			}
			
			break;
		}
	}
}
