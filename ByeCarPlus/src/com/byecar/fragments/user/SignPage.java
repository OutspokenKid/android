package com.byecar.fragments.user;

import org.json.JSONObject;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.byecarplus.SignActivity;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPFragment;
import com.byecar.classes.BCPFragmentActivity.OnAfterCheckSessionListener;
import com.outspoken_kid.classes.ViewUnbindHelper;
import com.outspoken_kid.fragment.sns.FacebookFragment;
import com.outspoken_kid.fragment.sns.FacebookFragment.FBUserInfo;
import com.outspoken_kid.fragment.sns.KakaoFragment;
import com.outspoken_kid.fragment.sns.KakaoFragment.KakaoUserInfo;
import com.outspoken_kid.fragment.sns.SNSFragment.OnAfterSignInListener;
import com.outspoken_kid.model.SNSUserInfo;
import com.outspoken_kid.utils.AppInfoUtils;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.PageNavigatorView;

public class SignPage extends BCPFragment {

	private static final int SLIDE_DURATION = 30000;
	private static final int ANIM_DURATION = 1000;
	private static final int DELAY = 2000;
	
	private static final float MOVE_DIST = 0.8f;
	private static final int NUMBER_OF_PAGE = 6;
	private static final int NUMBER_OF_SPLASH = 3;
	
	private ImageView[] floatingImageViews;
	private View catchphrase;
	private Button btnMember;
	private ViewPager viewPager;
	private Button btnNext;
	private PageNavigatorView navigator;
	
	private PagerAdapterForGuide pagerAdapter;
	private ImageView[] guideImageViews;
	private FrameLayout guideFrame;
	private TextView tvUserCount;
	
	//Sign relative and contents.
	private RelativeLayout signRelative;
	private Button btnKakao;
	private Button btnFacebook;
	private Button btnSignIn;
	private Button btnSignUp;
	private Button btnTermOfUse;
	
	private FacebookFragment ff;
	private KakaoFragment kf;

	private int playingIndex;
	private int playingCount;
	private boolean needPlay;
	private AsyncAnimTask currentTask;
	private Matrix[] matrices;
	
	private View cover;

	private int dealerCount;
	private boolean needStop;
	
	@Override
	public void bindViews() {
		
		viewPager = (ViewPager) mThisView.findViewById(R.id.signPage_viewPager);
		catchphrase = mThisView.findViewById(R.id.signPage_catchphrase);
		btnMember = (Button) mThisView.findViewById(R.id.signPage_btnMember);
		btnNext = (Button) mThisView.findViewById(R.id.signPage_btnNext);
		navigator = (PageNavigatorView) mThisView.findViewById(R.id.signPage_navigator);
		
		cover = mThisView.findViewById(R.id.signPage_cover);
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
		navigator.setEmptyOffCircle();
		navigator.invalidate();

		initFloatImageViews();
		initGuideImageViews();
		initSignRelative();
		
		downloadDealerCount();
		
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				checkSession();
			}
		}, 1000);
	}

	@Override
	public void setListeners() {

		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {

				navigator.setIndex(position);
				
				if(position == 0) {
					catchphrase.setVisibility(View.VISIBLE);
					btnMember.setVisibility(View.VISIBLE);
				} else {
					catchphrase.setVisibility(View.INVISIBLE);
					btnMember.setVisibility(View.INVISIBLE);
				}
				
				if(position == 1) {
					showUserCount(dealerCount);
				} else {
					tvUserCount.setText(null);
					FontUtils.addSpan(tvUserCount, "0", 0, 1, true);
					FontUtils.addSpan(tvUserCount, " 명", 0, 0.5f, false);
				}
				
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

		btnMember.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(BCPConstants.PAGE_SIGN_IN, null);
			}
		});
		
		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
			}
		});
	
		btnSignIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(BCPConstants.PAGE_SIGN_IN, null);
			}
		});
		
		btnSignUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(BCPConstants.PAGE_SIGN_UP_FOR_USER, null);
			}
		});
		
		btnTermOfUse.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(BCPConstants.PAGE_TERM_OF_USE, null);
			}
		});
	
		cover.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				//Do nothing.
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
			
		//logo.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.signPage_logo).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(258);
		rp.height = ResizeUtils.getSpecificLength(316);
		rp.topMargin = ResizeUtils.getSpecificLength(160);
		
		//catchphrase.
		rp = (RelativeLayout.LayoutParams) catchphrase.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(234);
		rp.height = ResizeUtils.getSpecificLength(24);
		rp.topMargin = ResizeUtils.getSpecificLength(20);
		
		//btnMember.
		rp = (RelativeLayout.LayoutParams) btnMember.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(422);
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.bottomMargin = ResizeUtils.getSpecificLength(190);

		//navigator.
		rp = (RelativeLayout.LayoutParams) navigator.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(16);
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);
		
		//btnNext.
		rp = (RelativeLayout.LayoutParams) btnNext.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(80);
		rp.height = ResizeUtils.getSpecificLength(40);
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
	public int getPageTitleTextResId() {

		return 0;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(matrices == null) {
			setImages();
			playingIndex = 0;
			playingCount++;
			needPlay = true;
			needStop = false;
			play();
		}
	}
	
	@Override
	public void onDetach() {

		needPlay = false;
		needStop = true;
		
		if(currentTask != null) {
			currentTask.cancel(true);
		}
		
		kf.setOnAfterSignInListener(null);
		ff.setOnAfterSignInListener(null);
		((FrameLayout)mThisView.findViewById(R.id.signPage_snsFrame)).removeAllViews();
		ViewUnbindHelper.unbindReferences(floatingImageViews[0]);
		ViewUnbindHelper.unbindReferences(floatingImageViews[1]);
		ViewUnbindHelper.unbindReferences(floatingImageViews[2]);
		ViewUnbindHelper.unbindReferences(guideImageViews[0]);
		ViewUnbindHelper.unbindReferences(guideImageViews[1]);
		ViewUnbindHelper.unbindReferences(guideImageViews[2]);
		ViewUnbindHelper.unbindReferences(signRelative);
		
		if(ff != null) {
			ff.logout();
		}
		
		if(kf != null) {
			kf.logout();
		}
		
		super.onDetach();
	}
	
	@Override
	public int getRootViewResId() {

		return R.id.signPage_mainLayout;
	}
	
//////////////////// Custom methods.
	
	public void initFloatImageViews() {
		
		floatingImageViews = new ImageView[NUMBER_OF_SPLASH];
		
		RelativeLayout mainLayout = (RelativeLayout) mThisView.findViewById(R.id.signPage_mainLayout);
		
		for(int i=0; i<NUMBER_OF_SPLASH; i++) {
			floatingImageViews[i] = new ImageView(mContext);
			floatingImageViews[i].setLayoutParams(new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			floatingImageViews[i].setScaleType(ScaleType.MATRIX);
			mainLayout.addView(floatingImageViews[i], 0);

			if(i == 0) {
				floatingImageViews[i].setVisibility(View.VISIBLE);
			} else {
				floatingImageViews[i].setVisibility(View.INVISIBLE);
			}
		}
	}
	
	public void initGuideImageViews() {
		
		int size = NUMBER_OF_PAGE - 2;
		
		guideImageViews = new ImageView[size];
		
		int[] resIds = new int[]{
				R.drawable.splash2_0,
				R.drawable.splash2_1,
				R.drawable.splash2_2,
				R.drawable.splash2_3,
		};
		
		for(int i=0; i<size; i++) {
			guideImageViews[i] = new ImageView(mContext);
			guideImageViews[i].setScaleType(ScaleType.CENTER_CROP);

			BitmapDrawable bd = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), resIds[i]));
			Bitmap bitmap = bd.getBitmap();
			guideImageViews[i].setImageBitmap(bitmap);
		}
	}
	
	public void initSignRelative() {
		
		signRelative = (RelativeLayout) mActivity.getLayoutInflater().inflate(R.layout.relative_sign, null);
		
		RelativeLayout.LayoutParams rp = null;

		btnTermOfUse = (Button) signRelative.findViewById(R.id.signRelative_btnTermOfUse);
		btnSignIn = (Button) signRelative.findViewById(R.id.signRelative_btnSignIn);
		btnSignUp = (Button) signRelative.findViewById(R.id.signRelative_btnSignUp);
		btnKakao = (Button) signRelative.findViewById(R.id.signRelative_btnKakao);
		btnFacebook = (Button) signRelative.findViewById(R.id.signRelative_btnFacebook);
		
		rp = (RelativeLayout.LayoutParams) btnTermOfUse.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(280);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.bottomMargin = ResizeUtils.getSpecificLength(80);

		rp = (RelativeLayout.LayoutParams) btnSignIn.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(285);
		rp.height = ResizeUtils.getSpecificLength(83);
		rp.leftMargin = ResizeUtils.getSpecificLength(27);
		rp.bottomMargin = ResizeUtils.getSpecificLength(45);
		
		rp = (RelativeLayout.LayoutParams) btnSignUp.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(286);
		rp.height = ResizeUtils.getSpecificLength(83);
		rp.leftMargin = ResizeUtils.getSpecificLength(16);

		rp = (RelativeLayout.LayoutParams) btnKakao.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(587);
		rp.height = ResizeUtils.getSpecificLength(83);
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);
		
		rp = (RelativeLayout.LayoutParams) btnFacebook.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(587);
		rp.height = ResizeUtils.getSpecificLength(83);
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);
		
		///////////

		btnKakao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				kf.getCoverImage().performClick();
			}
		});
		
		btnFacebook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				ff.getCoverImage().performClick();
			}
		});
		
		kf = new KakaoFragment() {
			
			@Override
			protected int getLoginImageResId() {
				
				return R.drawable.login_kakao_btn;
			}
		};
		mActivity.getSupportFragmentManager().beginTransaction().add(R.id.signPage_snsFrame, kf, "kkFragment").commit();
		kf.setOnAfterSignInListener(new OnAfterSignInListener() {
			
			@Override
			public void OnAfterSignIn(SNSUserInfo userInfo) {

				try {
					KakaoUserInfo kakaoUserInfo = (KakaoUserInfo) userInfo;
					kakaoUserInfo.printSNSUserInfo();
					
					signInWithSNS("kakaotalk", "" + kakaoUserInfo.id, 
							kakaoUserInfo.nickname, kakaoUserInfo.profile_image);
				} catch (Exception e) {
					LogUtils.trace(e);
					kf.logout();
				}
			}
		});
		
		ff = new FacebookFragment() {
			
			@Override
			protected int getLoginImageResId() {
				
				return R.drawable.login_facebook_btn;
			}
		};
	    mActivity.getSupportFragmentManager().beginTransaction().add(R.id.signPage_snsFrame, ff, "fbFragment").commit();
	    ff.setOnAfterSignInListener(new OnAfterSignInListener() {
			
			@Override
			public void OnAfterSignIn(SNSUserInfo userInfo) {

				try {
					FBUserInfo fbUserInfo = (FBUserInfo) userInfo;
					fbUserInfo.printSNSUserInfo();
					
					signInWithSNS("facebook", fbUserInfo.id, 
							fbUserInfo.userName, fbUserInfo.profileImage);
				} catch (Exception e) {
					LogUtils.trace(e);
					ff.logout();
				}
			}
		});
	}
	
	public void setImages() {

		matrices = new Matrix[NUMBER_OF_SPLASH];
		
		int[] resIds = new int[]{
				R.drawable.splash_bg1,
				R.drawable.splash_bg2,
				R.drawable.splash_bg3,
		};
		
		int size = matrices.length;
		for(int i=0; i<size; i++) {
			
			BitmapDrawable bd = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), resIds[i]));
			Bitmap bitmap = bd.getBitmap();
			
			floatingImageViews[i].setImageBitmap(bitmap);
			
			matrices[i] = getBitmapMatrix(i, bitmap);
			floatingImageViews[i].setImageMatrix(matrices[i]);
			
			if(i == 0) {
				floatingImageViews[i].setVisibility(View.VISIBLE);
			} else {
				floatingImageViews[i].setVisibility(View.INVISIBLE);
			}
		}
	}
	
	public void play() {

		LogUtils.log("###SignPage.play.  playingIndex : " + playingIndex);
		
		if(!needPlay) {
			return;
		}

		int index = playingIndex % NUMBER_OF_SPLASH;
		currentTask = new AsyncAnimTask(floatingImageViews[index]); 
		currentTask.execute();
		slide(playingIndex, floatingImageViews[index]);
		playingIndex++;

		final int lastPlayingCount = playingCount;
		
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				
				if(needPlay && lastPlayingCount == playingCount) {
					play();
				}
			}
		}, SLIDE_DURATION - DELAY);
	}
	
	//화면 사이즈와 이미지 사이즈에 알맞는 Matrix 계산, 반환.
	public Matrix getBitmapMatrix(int index, Bitmap bitmap) {
		
		Matrix matrix = new Matrix();
		
		int screenWidth = ResizeUtils.getScreenWidth();
		int screenHeight = ResizeUtils.getScreenHeight();
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		
		float scale = (float) screenHeight / (float) height;

		//배율 설정.
		matrix.postScale(scale, scale);
		
	//정렬 설정.
		//짝수번째 스플래쉬 인 경우 오른쪽 정렬.
		if(index % 2 == 1) {
			matrix.postTranslate(screenWidth - (float)width*scale, 0);
		}
		
		return matrix;
	}
	
	public void slide(final int index, final ImageView ivImage) {
		
		final Interpolator interpolator = new AccelerateDecelerateInterpolator();
		final long startTime = System.currentTimeMillis();
		final long duration = SLIDE_DURATION;
		final Matrix matrix = new Matrix(matrices[index%NUMBER_OF_SPLASH]);
		
		//짝수 번째는 왼쪽으로, 홀수 번째는 오른쪽으로.
		final float totalDist = (index % 2 == 0? -1 : 1) * ResizeUtils.getScreenWidth() * MOVE_DIST;

		ivImage.post(new Runnable() {

			float r, r0, t, distX;

			@Override
			public void run() {

				if(!needPlay) {
					return;
				}
				
				try {
					t = (float) (System.currentTimeMillis() - startTime) / duration;
					t = t > 1.0f ? 1.0f : t;
					r = interpolator.getInterpolation(t);
					distX = (r - r0) * totalDist;

					matrix.postTranslate(distX, 0);
					ivImage.setImageMatrix(matrix);

					r0 = r;

					//반복.
					if (t < 1f) {
						ivImage.post(this);
						
					//저장.
					} else {
						matrices[index % NUMBER_OF_SPLASH] = matrix;
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}
			}
		});
	}
	
	public void checkSession() {

		if(mActivity != null) {
			mActivity.checkSession(new OnAfterCheckSessionListener() {
				
				@Override
				public void onAfterCheckSession(boolean isSuccess, JSONObject objJSON) {

					if(isSuccess) {
						
						if(mActivity != null) {
							((SignActivity)mActivity).launchMainForUserActivity();
						}
					} else {
						hideCover();
						showButtons();
					}
				}
			});
		}
	}
	
	public void hideCover() {
		
		cover.setVisibility(View.INVISIBLE);
	}
	
	public void showButtons() {

		if(btnMember.getVisibility() != View.VISIBLE) {
			AlphaAnimation aaIn = new AlphaAnimation(0, 1);
			aaIn.setDuration(300);
			
			btnMember.setVisibility(View.VISIBLE);
			btnNext.setVisibility(View.VISIBLE);
			navigator.setVisibility(View.VISIBLE);
			
			btnMember.startAnimation(aaIn);
			btnNext.startAnimation(aaIn);
			navigator.startAnimation(aaIn);
		}
	}
	
	public void signInWithSNS(String sns_key, String sns_user_key, 
			String nickname, String profileUrl) {
		/*
		http://byecar.minsangk.com/users/sns_join.json
		?sns_key=facebook
		&sns_user_key=2342829
		&user[nickname]=%EB%AF%BC%EC%83%81k
		&user[profile_img_url]=/images/20141106/da2eb7df5113d10b79571f4754b2add5.png
		 */
		String url = BCPAPIs.SIGN_IN_WITH_SNS_URL 
				+ "?sns_key=" + sns_key
				+ "&sns_user_key=" + sns_user_key
				+ "&user[nickname]=" + StringUtils.getUrlEncodedString(nickname);

		if(!StringUtils.isEmpty(profileUrl)) {
			url += "&user[profile_img_url]=" + StringUtils.getUrlEncodedString(profileUrl);
		}
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("SignInPage.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToSignIn);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("SignInPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);
					
					if(objJSON.getInt("result") == 1) {
						((SignActivity)mActivity).launchMainForUserActivity();
						ff.logout();
						kf.logout();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		}, mActivity.getLoadingView());
	}
	
	public void downloadDealerCount() {
		
		String url = BCPAPIs.DEALER_COUNT_URL;
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("SignPage.onError." + "\nurl : " + url);
				downloadAgain();
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("SignPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						dealerCount = objJSON.getJSONObject("count").getInt("dealer");
					} else {
						downloadAgain();
					}
				} catch (Exception e) {
					LogUtils.trace(e);
					downloadAgain();
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					downloadAgain();
				}
			}
			
			public void downloadAgain() {
				
				if(needStop) {
					return;
				}
				
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						
						downloadDealerCount();
					}
				}, 500);
			}
		});
	}
	
//////////////////// Classes.
	
	public class AsyncAnimTask extends AsyncTask<Void, Void, Void> {

		private ImageView ivImage;
		
		public AsyncAnimTask(ImageView ivImage) {
			
			this.ivImage = ivImage;
		}
		
		@Override
		protected void onPreExecute() {
			
			if(ivImage.getVisibility() != View.VISIBLE) {
				AlphaAnimation aaIn = new AlphaAnimation(0, 1);
				aaIn.setDuration(ANIM_DURATION);
				ivImage.setVisibility(View.VISIBLE);
				ivImage.startAnimation(aaIn);
			}
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			
			try {
				Thread.sleep(SLIDE_DURATION - DELAY);
			} catch (InterruptedException e) {
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			
			if(ivImage.getVisibility() == View.VISIBLE) {
				AlphaAnimation aaOut = new AlphaAnimation(1, 0);
				aaOut.setDuration(ANIM_DURATION + 2);
				ivImage.setVisibility(View.INVISIBLE);
				ivImage.startAnimation(aaOut);
			}
		}
	}
	
	public class PagerAdapterForGuide extends PagerAdapter {

		@Override
		public int getCount() {
	
			return NUMBER_OF_PAGE;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			
			if(position == 0) {
				
			} else if(position == 1) {
				
				if(guideFrame == null) {
					guideFrame = new FrameLayout(mContext);
					guideFrame.addView(guideImageViews[0]);
					
					if(tvUserCount == null) {
						 int topMargin = (int)(ResizeUtils.getScreenHeight()/2) + ResizeUtils.getSpecificLength(156);
						
						tvUserCount = new TextView(mContext);
						FrameLayout.LayoutParams fp = new FrameLayout.LayoutParams(
								LayoutParams.MATCH_PARENT, ResizeUtils.getSpecificLength(80),
								Gravity.CENTER_HORIZONTAL|Gravity.TOP);
						fp.topMargin = topMargin;
						tvUserCount.setLayoutParams(fp);
						tvUserCount.setGravity(Gravity.CENTER);
						tvUserCount.setTextColor(Color.WHITE);
						FontUtils.setFontSize(tvUserCount, 50);
						FontUtils.setShadow(tvUserCount, 
								new float[]{
									ResizeUtils.getSpecificLength(2),		//radius.
									ResizeUtils.getSpecificLength(2),		//dx.
									ResizeUtils.getSpecificLength(2),		//dy.
									255, 235, 155, 35						//a, r, g, b.
								});
						FontUtils.setFontStyle(tvUserCount, FontUtils.BOLD);
						guideFrame.addView(tvUserCount);
						
						tvUserCount.setText(null);
						FontUtils.addSpan(tvUserCount, "0", 0, 1, true);
						FontUtils.addSpan(tvUserCount, " 명", 0, 0.5f, false);
					}
				}

				if(guideFrame.getParent() == null) {
					container.addView(guideFrame);
				}
				
				return guideFrame;
				
			} else if(position < NUMBER_OF_PAGE - 1) {
				if(guideImageViews[position-1].getParent() == null) {
					container.addView(guideImageViews[position-1]);
				}
				
				return guideImageViews[position-1];
			} else {
				if(signRelative.getParent() == null) {
					container.addView(signRelative);
				}
				
				return signRelative;
			}
			
			return null;
		}
		
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
		}
		
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {

			return arg0 == arg1;
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void showUserCount(final int count) {

		if(AppInfoUtils.checkMinVersionLimit(Build.VERSION_CODES.HONEYCOMB)) {
			ValueAnimator animator = new ValueAnimator();
	        animator.setObjectValues(0, count);
	        animator.addUpdateListener(new AnimatorUpdateListener() {
	        	
	            public void onAnimationUpdate(ValueAnimator animation) {
	            	
	            	int animatedCount = Integer.parseInt(animation.getAnimatedValue().toString());
	            	
	        		tvUserCount.setText(null);
	        		FontUtils.addSpan(tvUserCount, StringUtils.getFormattedNumber(
	        				animatedCount), 0, 1, true);
	        		FontUtils.addSpan(tvUserCount, " 명", 0, 0.5f, false);
	        		
	        		if(animatedCount == count) {
	        			animateTextViewSize();
	        		}
	            }
	        });
	        animator.setEvaluator(new TypeEvaluator<Integer>() {
	            public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
	                return Math.round((endValue - startValue) * fraction);
	            }
	        });
	        animator.setDuration(1000);
	        animator.start();
		} else {
			tvUserCount.setText(null);
    		FontUtils.addSpan(tvUserCount, StringUtils.getFormattedNumber(count), 0, 1, true);
    		FontUtils.addSpan(tvUserCount, " 명", 0, 0.5f, false);
		}
	}
	
	public void animateTextViewSize() {
		
		ScaleAnimation saUp = new ScaleAnimation(
				1, 1.1f, 
				1, 1.1f, 
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f, 
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
		saUp.setInterpolator(mContext, android.R.anim.accelerate_interpolator);
		saUp.setDuration(150);
		saUp.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				
				ScaleAnimation saDown = new ScaleAnimation(
						1.1f, 1, 
						1.1f, 1, 
						ScaleAnimation.RELATIVE_TO_SELF, 0.5f, 
						ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
				saDown.setInterpolator(mContext, android.R.anim.accelerate_interpolator);
				saDown.setDuration(200);
				tvUserCount.startAnimation(saDown);
			}
		});
		tvUserCount.startAnimation(saUp);
	}
}
