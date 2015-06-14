package com.byecar.fragments.dealer;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplusfordealer.MainActivity;
import com.byecar.byecarplusfordealer.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPDownloadUtils;
import com.byecar.classes.BCPFragment;
import com.byecar.models.Dealer;
import com.byecar.models.Review;
import com.byecar.views.ReviewView;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.AppInfoUtils;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.IntentUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.OffsetScrollView;
import com.outspoken_kid.views.OffsetScrollView.OnScrollChangedListener;

public class MyInfoReviewPage extends BCPFragment {

	private OffsetScrollView scrollView;

	private View bg;
	private ImageView ivImage;
	
	private View gradeBadge;
	private TextView tvRating;
	private TextView tvSuccess;
	private TextView tvName;
	private TextView tvGrade;
	private TextView tvRatingText;
	private TextView tvSuccessText;
	private Button btnEditDealerInfo;
	
	private View headerForIntro;
	private TextView tvIntro;
	
	private View headerForGrade;
	private LinearLayout linearForGrade;
	private View gradeView;
	private Button btnDealerGrade;
	
	private View headerForReview;
	private LinearLayout linearForReview;
	private Button btnMore;
	
	ArrayList<Review> reviews = new ArrayList<Review>();

	private int scrollOffset;
	private int standardLength;
	private float diff;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.myInfoReviewPage_titleBar);
		
		scrollView = (OffsetScrollView) mThisView.findViewById(R.id.myInfoReviewPage_scrollView);
		bg = mThisView.findViewById(R.id.myInfoReviewPage_bg);
		ivImage = (ImageView) mThisView.findViewById(R.id.myInfoReviewPage_ivImage);
		
		gradeBadge = mThisView.findViewById(R.id.myInfoReviewPage_gradeBadge);
		
		tvRating = (TextView) mThisView.findViewById(R.id.myInfoReviewPage_tvRating);
		tvSuccess = (TextView) mThisView.findViewById(R.id.myInfoReviewPage_tvSuccess);
		tvName = (TextView) mThisView.findViewById(R.id.myInfoReviewPage_tvName);
		tvGrade = (TextView) mThisView.findViewById(R.id.myInfoReviewPage_tvGrade);
		tvRatingText = (TextView) mThisView.findViewById(R.id.myInfoReviewPage_tvRatingText);
		tvSuccessText = (TextView) mThisView.findViewById(R.id.myInfoReviewPage_tvSuccessText);
		btnEditDealerInfo = (Button) mThisView.findViewById(R.id.myInfoGradePage_btnEditDealerInfo);
		
		headerForIntro = mThisView.findViewById(R.id.myInfoReviewPage_headerForIntro);
		tvIntro = (TextView) mThisView.findViewById(R.id.myInfoReviewPage_tvIntro);
		
		headerForGrade = mThisView.findViewById(R.id.myInfoGradePage_headerForGrade);
		linearForGrade = (LinearLayout) mThisView.findViewById(R.id.myInfoGradePage_linearForGrade);
		gradeView = mThisView.findViewById(R.id.myInfoGradePage_gradeView);
		btnDealerGrade = (Button) mThisView.findViewById(R.id.myInfoGradePage_btnDealerGrade);
		
		headerForReview = mThisView.findViewById(R.id.myInfoReviewPage_headerForReview);
		linearForReview = (LinearLayout) mThisView.findViewById(R.id.myInfoReviewPage_linearForReview);
		btnMore = (Button) mThisView.findViewById(R.id.myInfoReviewPage_btnMore);
	}

	@Override
	public void setVariables() {

	}

	@Override
	public void createPage() {

		titleBar.setBgAlpha(0);
		
		tvRating.setText("0%");
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
	
		ivImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(!StringUtils.isEmpty(MainActivity.dealer.getProfile_img_url())) {
					mActivity.showImageViewer(0, getString(R.string.profileImage), 
							new String[]{MainActivity.dealer.getProfile_img_url()}, null);
				}
			}
		});

		btnEditDealerInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(BCPConstants.PAGE_EDIT_DEALER_INFO, null);
			}
		});
		
		btnDealerGrade.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(MainActivity.dealer.getLevel() == Dealer.LEVEL_SUPERB_DEALER) {
					IntentUtils.call(mContext, MainActivity.companyInfo.getPhone_number());
				}
			}
		});
		
		btnMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				loadReviews();
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;

		//ivImage.
		rp = (RelativeLayout.LayoutParams) ivImage.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(137);
		rp.height = ResizeUtils.getSpecificLength(137);
		rp.leftMargin = ResizeUtils.getSpecificLength(44);
		rp.topMargin = ResizeUtils.getSpecificLength(178);
		
		//bg.
		rp = (RelativeLayout.LayoutParams) bg.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(493);
		
		//gradeBadge.
		rp = (RelativeLayout.LayoutParams) gradeBadge.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(95);
		rp.height = ResizeUtils.getSpecificLength(95);
		rp.leftMargin = ResizeUtils.getSpecificLength(38);
		rp.topMargin = ResizeUtils.getSpecificLength(24);
		
		//tvRating.
		rp = (RelativeLayout.LayoutParams) tvRating.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(95);
		rp.height = ResizeUtils.getSpecificLength(95);
		rp.leftMargin = ResizeUtils.getSpecificLength(40);
		
		//tvSuccess.
		rp = (RelativeLayout.LayoutParams) tvSuccess.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(95);
		rp.height = ResizeUtils.getSpecificLength(95);
		rp.leftMargin = ResizeUtils.getSpecificLength(40);
		
		//tvName.
		rp = (RelativeLayout.LayoutParams) tvName.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(175);
		rp.height = ResizeUtils.getSpecificLength(62);
		rp.leftMargin = ResizeUtils.getSpecificLength(25);
		
		//tvGrade.
		rp = (RelativeLayout.LayoutParams) tvGrade.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(134);
		rp.height = ResizeUtils.getSpecificLength(62);
		
		//tvGrade.
		rp = (RelativeLayout.LayoutParams) tvGrade.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(134);
		rp.height = ResizeUtils.getSpecificLength(62);
		
		//tvRatingText.
		rp = (RelativeLayout.LayoutParams) tvRatingText.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(134);
		rp.height = ResizeUtils.getSpecificLength(62);
		
		//tvSuccessText.
		rp = (RelativeLayout.LayoutParams) tvSuccessText.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(134);
		rp.height = ResizeUtils.getSpecificLength(62);

		//btnEditDealerInfo.
		rp = (RelativeLayout.LayoutParams) btnEditDealerInfo.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(211);
		rp.height = ResizeUtils.getSpecificLength(44);
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);
		
		//headerForIntro.
		rp = (RelativeLayout.LayoutParams) headerForIntro.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(68);
		rp.topMargin = ResizeUtils.getSpecificLength(20);
		
		//tvIntro.
		rp = (RelativeLayout.LayoutParams) tvIntro.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		
		//footerForIntro.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.myInfoReviewPage_footerForIntro).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(20);
		
		//headerForGrade.
		rp = (RelativeLayout.LayoutParams) headerForGrade.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(68);
		rp.topMargin = ResizeUtils.getSpecificLength(20);
		
		//linearForGrade.
		rp = (RelativeLayout.LayoutParams) linearForGrade.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);

		//gradeView.
		ResizeUtils.viewResize(568, 166, gradeView, 1, Gravity.CENTER_HORIZONTAL, new int[]{0, 40, 0, 0});
		
		//btnDealerGrade.
		ResizeUtils.viewResize(568, 84, btnDealerGrade, 1, Gravity.CENTER_HORIZONTAL, new int[]{0, 26, 0, 20});
		
		//footerForGrade.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.myInfoGradePage_footerForGrade).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(20);
		
		//headerForReview.
		rp = (RelativeLayout.LayoutParams) headerForReview.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(68);
		rp.topMargin = ResizeUtils.getSpecificLength(20);
		
		//linearForReview.
		rp = (RelativeLayout.LayoutParams) linearForReview.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		
		//btnMore.
		ResizeUtils.viewResize(140, 60, btnMore, 1, Gravity.CENTER_HORIZONTAL, null);
		
		//footerForReview.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.myInfoReviewPage_footerForReview).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(20);
		
		FontUtils.setFontSize(tvRating, 36);
		FontUtils.setFontStyle(tvRating, FontUtils.BOLD);
		
		FontUtils.setFontSize(tvSuccess, 36);
		FontUtils.setFontStyle(tvSuccess, FontUtils.BOLD);
		
		FontUtils.setFontSize(tvName, 30);
		FontUtils.setFontStyle(tvName, FontUtils.BOLD);
		
		FontUtils.setFontSize(tvGrade, 24);
		FontUtils.setFontStyle(tvGrade, FontUtils.BOLD);
		
		FontUtils.setFontSize(tvRatingText, 24);
		FontUtils.setFontStyle(tvRatingText, FontUtils.BOLD);
		
		FontUtils.setFontSize(tvSuccessText, 24);
		FontUtils.setFontStyle(tvSuccessText, FontUtils.BOLD);
		
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

		return R.layout.fragment_my_info_review;
	}
	
	@Override
	public int getPageTitleTextResId() {

		return R.string.pageTitle_myInfoReview;
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
		
		setDealerInfo();
		setIntro();

		if(reviews.size() == 0) {
			loadReviews();
		} else {
			refreshReviews();
		}
		
		checkPageScrollOffset();
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		
		scrollView.setOnScrollChangedListener(null);
	}
	
	@Override
	public int getRootViewResId() {

		return R.id.myInfoReviewPage_mainLayout;
	}
	
//////////////////// Custom methods.
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void setDealerInfo() {
		
		BCPDownloadUtils.downloadBitmap(
				MainActivity.dealer.getProfile_img_url(), new OnBitmapDownloadListener() {

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
		}, 218);

		if(AppInfoUtils.checkMinVersionLimit(Build.VERSION_CODES.HONEYCOMB)) {
			final ValueAnimator animator = new ValueAnimator();
	        animator.setObjectValues(0, MainActivity.dealer.getAvg_rating());
	        animator.addUpdateListener(new AnimatorUpdateListener() {
	        	
	            public void onAnimationUpdate(ValueAnimator animation) {
	            	
	                tvRating.setText(null);
	        		FontUtils.addSpan(tvRating, "" + animation.getAnimatedValue(), 0, 1);
	        		FontUtils.addSpan(tvRating, "%", 0, 0.7f);
	            }
	        });
	        animator.setEvaluator(new TypeEvaluator<Integer>() {
	            public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
	                return Math.round((endValue - startValue) * fraction);
	            }
	        });
	        animator.setDuration(600);
	        
	        new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {

					animator.start();
				}
			}, 1000);
		} else {
			tvRating.setText(null);
    		FontUtils.addSpan(tvRating, "" + MainActivity.dealer.getAvg_rating(), 0, 1);
    		FontUtils.addSpan(tvRating, "%", 0, 0.7f);
		}
		
		tvSuccess.setText(null);
		FontUtils.addSpan(tvSuccess, "" + MainActivity.dealer.getSuccess_bids_cnt(), 0, 1);
		FontUtils.addSpan(tvSuccess, "대", 0, 0.7f);

		tvName.setText(null);
		FontUtils.addSpan(tvName, MainActivity.dealer.getName(), 0, 1);
		FontUtils.addSpan(tvName, "\n" + MainActivity.dealer.getCompany(), 0, 0.7f);
		
		switch(MainActivity.dealer.getLevel()) {
		
		case Dealer.LEVEL_FRESH_MAN:
			gradeBadge.setBackgroundResource(R.drawable.grade_icon4);
			tvGrade.setText(R.string.dealerLevel1);
			tvGrade.setTextColor(getResources().getColor(R.color.color_dealer_level1));
			
			gradeView.setBackgroundResource(R.drawable.my_grade_icon1);
			btnDealerGrade.setBackgroundResource(R.drawable.grade_icon1_text);
			break;
			
		case Dealer.LEVEL_NORAML_DEALER:
			gradeBadge.setBackgroundResource(R.drawable.grade_icon3);
			tvGrade.setText(R.string.dealerLevel2);
			tvGrade.setTextColor(getResources().getColor(R.color.color_dealer_level2));
			
			gradeView.setBackgroundResource(R.drawable.my_grade_icon2);
			btnDealerGrade.setBackgroundResource(R.drawable.grade_icon2_text);
			break;
			
		case Dealer.LEVEL_SUPERB_DEALER:
			gradeBadge.setBackgroundResource(R.drawable.grade_icon2);
			tvGrade.setText(R.string.dealerLevel3);
			tvGrade.setTextColor(getResources().getColor(R.color.color_dealer_level3));
			
			gradeView.setBackgroundResource(R.drawable.my_grade_icon3);
			btnDealerGrade.setBackgroundResource(R.drawable.my_info_call_btn);
			break;
			
		case Dealer.LEVEL_POWER_DEALER:
			gradeBadge.setBackgroundResource(R.drawable.grade_icon1);
			tvGrade.setText(R.string.dealerLevel4);
			tvGrade.setTextColor(getResources().getColor(R.color.color_dealer_level4));
			
			gradeView.setBackgroundResource(R.drawable.my_grade_icon4);
			btnDealerGrade.setBackgroundResource(R.drawable.grade_icon4_text);
			break;
		}
	}
	
	public void setIntro() {
		
		if(!StringUtils.isEmpty(MainActivity.dealer.getDesc())) {
			tvIntro.setText(MainActivity.dealer.getDesc());
		} else {
			tvIntro.setText(R.string.emptyDealerDesc);
		}
	}
	
	public void loadReviews() {
		
		String url = BCPAPIs.REVIEW_DEALER_URL
				+ "?dealer_id=" + MainActivity.dealer.getId();
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("DealerPage.onError." + "\nurl : " + url);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("DealerPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					JSONArray arJSON = objJSON.getJSONArray("reviews");
					
					ArrayList<Review> newReviews = new ArrayList<Review>();
					
					int size = arJSON.length();
					for(int i=0; i<size; i++) {
						newReviews.add(new Review(arJSON.getJSONObject(i)));
					}
					
					if(size < NUMBER_OF_LISTITEMS) {
						btnMore.setVisibility(View.GONE);
					} else {
						btnMore.setVisibility(View.VISIBLE);
					}
					
					addReviewViews(newReviews);
					reviews.addAll(newReviews);
					
					if(linearForReview.getChildCount() == 1 && reviews.size() == 0) {
						View noReview = new View(mContext);
						ResizeUtils.viewResize(223, 226, noReview, 1, Gravity.CENTER, new int[]{0, 16, 0, 16});
						noReview.setBackgroundResource(R.drawable.dealer_no_comment);
						linearForReview.addView(noReview);
						
						linearForReview.setPadding(0, 0, 0, 0);
					} else {
						linearForReview.setPadding(0, 0, 0, ResizeUtils.getSpecificLength(16));
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void refreshReviews() {

		linearForReview.removeAllViews();
		linearForReview.addView(btnMore);
		addReviewViews(reviews);
	}
	
	public void addReviewViews(ArrayList<Review> reviews) {
		
		int size = reviews.size();
		for(int i=0; i<size; i ++) {
			ReviewView rv = new ReviewView(mContext);
			ResizeUtils.viewResize(574, LayoutParams.WRAP_CONTENT, rv, 1, Gravity.CENTER_HORIZONTAL, new int[]{0, 16, 0, 0});
			rv.setReview(reviews.get(i), mActivity);
			linearForReview.addView(rv, linearForReview.getChildCount() - 1);
			
			if(reviews.get(i).getReply() != null) {
				ReviewView reply = new ReviewView(mContext);
				ResizeUtils.viewResize(574, LayoutParams.WRAP_CONTENT, reply, 1, Gravity.CENTER_HORIZONTAL, new int[]{0, 16, 0, 0});
				reply.setReply(reviews.get(i).getReply(), reviews.get(i), mActivity);
				linearForReview.addView(reply, linearForReview.getChildCount() - 1);
				
				if(i != size - 1) {
					final View line = new View(mContext);
					ResizeUtils.viewResize(544, ResizeUtils.ONE, line, 1, Gravity.CENTER_HORIZONTAL, new int[]{0, 28, 0, 14});
					line.setBackgroundColor(getResources().getColor(R.color.color_darkgray));
					linearForReview.addView(line, linearForReview.getChildCount() - 1);
				}
			}
		}
	}
	
	public void closePage() {
		
		ToastUtils.showToast(R.string.failToLoadDealerInfo);
		
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				
				if(mActivity != null) {
					mActivity.closeTopPage();
				}
			}
		}, 1000);
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
