package com.cmons.byecarplus.fragments;

import org.json.JSONObject;

import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.cmons.byecarplus.R;
import com.cmons.byecarplus.classes.BCPFragment;
import com.cmons.byecarplus.classes.BCPFragmentForSign;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.views.PageNavigatorView;

public class SignPage extends BCPFragmentForSign {

	public static final int SLIDE_DURATION = 5000;
	private static final float MOVE_DIST = 0.2f;
	private static final int NUMBER_OF_PAGE = 5;
	
	private ImageView[] floatingImageViews;
	private ViewPager viewPager;
	private Button btnNext;
	private PageNavigatorView navigator;
	
	private PagerAdapterForGuide pagerAdapter;
	private ImageView[] guideImageViews;
	
	//Sign relative and contents.
	private RelativeLayout signRelative;
	private Button btnFb;
	private Button btnKakao;
	private Button btnSignIn;
	private Button btnSignUp;
	private Button btnTermOfUse;

	private int playingIndex;
	private boolean needPlay;
	
	@Override
	public void bindViews() {
		
		viewPager = (ViewPager) mThisView.findViewById(R.id.signPage_viewPager);
		btnNext = (Button) mThisView.findViewById(R.id.signPage_btnNext);
		navigator = (PageNavigatorView) mThisView.findViewById(R.id.signPage_navigator);
	}

	@Override
	public void setVariables() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createPage() {

		pagerAdapter = new PagerAdapterForGuide();
		viewPager.setAdapter(pagerAdapter);
		
		navigator.setSize(NUMBER_OF_PAGE);
		navigator.setIndex(0);
		navigator.invalidate();
		
		initFloatImageViews();
		initGuideImageViews();
		initSignRelative();
	}

	@Override
	public void setListeners() {

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {

				navigator.setIndex(position);
				
				if(position == NUMBER_OF_PAGE - 1) {
					btnNext.setVisibility(View.INVISIBLE);
				} else {
					btnNext.setVisibility(View.VISIBLE);
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
	
		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
			
		//logo.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.signPage_logo).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(352);
		rp.height = ResizeUtils.getSpecificLength(299);
		rp.topMargin = ResizeUtils.getSpecificLength(180);

		//navigator.
		rp = (RelativeLayout.LayoutParams) navigator.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(16);
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);
		
		//btnNext.
		rp = (RelativeLayout.LayoutParams) btnNext.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(200);
		rp.height = ResizeUtils.getSpecificLength(80);
		rp.rightMargin = ResizeUtils.getSpecificLength(40);
		rp.bottomMargin = ResizeUtils.getSpecificLength(40);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_sign;
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
		
		needPlay = true;
		play();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		needPlay = false;
	}
	
//////////////////// Custom methods.
	
	public void initFloatImageViews() {

		int size = 3;
		
		floatingImageViews = new ImageView[size];
		
		int[] resIds = new int[]{
				R.drawable.splash_bg1,
				R.drawable.splash_bg2,
				R.drawable.splash_bg3,
		};
		
		RelativeLayout mainLayout = (RelativeLayout) mThisView.findViewById(R.id.signPage_mainLayout);
		
		for(int i=0; i<size; i++) {
			floatingImageViews[i] = new ImageView(mContext);
			floatingImageViews[i].setLayoutParams(new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			floatingImageViews[i].setScaleType(ScaleType.MATRIX);
			floatingImageViews[i].setBackgroundResource(resIds[i]);
			mainLayout.addView(floatingImageViews[i], mainLayout.getChildCount() - 4);
		}
	}
	
	public void initGuideImageViews() {
		
		int size = NUMBER_OF_PAGE - 2;
		
		guideImageViews = new ImageView[size];
		
		int[] resIds = new int[]{
				R.drawable.a1,
				R.drawable.a2,
				R.drawable.a3,
		};
		
		for(int i=0; i<size; i++) {
			guideImageViews[i] = new ImageView(mContext);
			guideImageViews[i].setScaleType(ScaleType.CENTER_CROP);
			guideImageViews[i].setBackgroundResource(resIds[i]);
		}
	}
	
	public void initSignRelative() {

		int idCount = madeCount;
		int width, height;
		RelativeLayout.LayoutParams rp = null;
		
		signRelative = new RelativeLayout(mContext);
		width = LayoutParams.MATCH_PARENT;
		height = LayoutParams.WRAP_CONTENT;
		rp = new RelativeLayout.LayoutParams(width, height);
		rp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		signRelative.setLayoutParams(rp);
		
		//btnTermOfUse.			id : 0.
		btnTermOfUse = new Button(mContext);
		width = ResizeUtils.getSpecificLength(280);
		height = ResizeUtils.getSpecificLength(60);
		rp = new RelativeLayout.LayoutParams(width, height);
		rp.bottomMargin = ResizeUtils.getSpecificLength(80);
		rp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		btnTermOfUse.setLayoutParams(rp);
		btnTermOfUse.setId(idCount);
		btnTermOfUse.setBackgroundResource(R.drawable.login_terms_btn);
		signRelative.addView(btnTermOfUse);
		
		//btnSignIn.			id : 1.
		btnSignIn = new Button(mContext);
		width = ResizeUtils.getSpecificLength(285);
		height = ResizeUtils.getSpecificLength(83);
		rp = new RelativeLayout.LayoutParams(width, height);
		rp.leftMargin = ResizeUtils.getSpecificLength(30);
		rp.bottomMargin = ResizeUtils.getSpecificLength(45);
		rp.addRule(RelativeLayout.ABOVE, idCount);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		btnSignIn.setLayoutParams(rp);
		btnSignIn.setId(idCount + 1);
		btnSignIn.setBackgroundResource(R.drawable.login_login_btn);
		signRelative.addView(btnSignIn);
		
		//btnSignUp.
		btnSignUp = new Button(mContext);
		width = ResizeUtils.getSpecificLength(286);
		height = ResizeUtils.getSpecificLength(83);
		rp = new RelativeLayout.LayoutParams(width, height);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		rp.addRule(RelativeLayout.ALIGN_TOP, idCount + 1);
		rp.addRule(RelativeLayout.RIGHT_OF, idCount + 1);
		btnSignUp.setLayoutParams(rp);
		btnSignUp.setBackgroundResource(R.drawable.login_signup_btn);
		signRelative.addView(btnSignUp);
		
		//btnKakao.			id : 2.
		btnKakao = new Button(mContext);
		width = ResizeUtils.getSpecificLength(587);
		height = ResizeUtils.getSpecificLength(83);
		rp = new RelativeLayout.LayoutParams(width, height);
		btnKakao.setId(idCount + 2);
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);
		rp.addRule(RelativeLayout.ABOVE, idCount + 1);
		rp.addRule(RelativeLayout.ALIGN_LEFT, idCount + 1);
		btnKakao.setLayoutParams(rp);
		btnKakao.setBackgroundResource(R.drawable.login_kakao_btn);
		signRelative.addView(btnKakao);
		
		//btnFb.
		btnFb = new Button(mContext);
		width = ResizeUtils.getSpecificLength(587);
		height = ResizeUtils.getSpecificLength(83);
		rp = new RelativeLayout.LayoutParams(width, height);
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);
		rp.addRule(RelativeLayout.ABOVE, idCount + 2);
		rp.addRule(RelativeLayout.ALIGN_LEFT, idCount + 2);
		btnFb.setLayoutParams(rp);
		btnFb.setBackgroundResource(R.drawable.login_facebook_btn);
		signRelative.addView(btnFb);
	}

	public void play() {

		if(!needPlay) {
			return;
		}

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				
				if(needPlay) {
					new AsyncSlideTask(guideImageViews[playingIndex++]).execute();
				}
			}
		}, SLIDE_DURATION - 500);
	}
	
//////////////////// Classes.
	
	public class AsyncSlideTask extends AsyncTask<Void, Void, Void> {

		ImageView ivImage;
		
		public AsyncSlideTask(ImageView ivImage) {
			
			this.ivImage = ivImage;
		}
		
		@Override
		protected void onPreExecute() {

			if(ivImage.getVisibility() != View.VISIBLE) {
				AlphaAnimation aaIn = new AlphaAnimation(0, 1);
				aaIn.setDuration(1000);
				ivImage.startAnimation(aaIn);
			}
			
			slide(ivImage);
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			
			try {
				Thread.sleep(SLIDE_DURATION);
			} catch (InterruptedException e) {
				LogUtils.trace(e);
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			
			AlphaAnimation aaOut = new AlphaAnimation(1, 0);
			aaOut.setDuration(1000);
			ivImage.startAnimation(aaOut);
			ivImage.setVisibility(View.INVISIBLE);
		}
		
		public void slide(final ImageView ivImage) {
			
			final Interpolator interpolator = new AccelerateDecelerateInterpolator();
			final long startTime = System.currentTimeMillis();
			final long duration = SLIDE_DURATION;
			final Matrix matrix = new Matrix(ivImage.getImageMatrix());
			final float totalDist = -ResizeUtils.getScreenWidth() * MOVE_DIST;

			ivImage.post(new Runnable() {

				float r, r0, t, distX;

				@Override
				public void run() {

					t = (float) (System.currentTimeMillis() - startTime) / duration;
					t = t > 1.0f ? 1.0f : t;
					r = interpolator.getInterpolation(t);
					distX = (r - r0) * totalDist;

					matrix.postTranslate(distX, 0);
					ivImage.setImageMatrix(matrix);

					r0 = r;

					if (t < 1f) {
						ivImage.post(this);
					}
				}
			});
		}
	}
	
	public class PagerAdapterForGuide extends PagerAdapter {

		@Override
		public int getCount() {
	
			return NUMBER_OF_PAGE;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			
			switch(position) {
			
			case 0:
				//Transparent.
				break;
				
			case 1:
			case 2:
			case 3:
				container.addView(guideImageViews[position-1]);
				return guideImageViews[position-1];
				
			case 4:
				container.addView(signRelative);
				return signRelative;
				
			}
			
			return null;
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
}
