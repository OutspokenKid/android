package com.zonecomms.common.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.outspoken_kid.classes.ViewUnbindHelper;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class ImagePlayViewer extends FrameLayout {

	public static final int SLIDE_DURATION = 3000;
	
	private static final int ANIM_DURATION = 2000;
	private static final float MOVE_DIST = 0.05f;
	
	private ImageView[] imageViews = new ImageView[2];
	private Bitmap nextBitmap;
	
	private boolean needToPlayImage;
	private int imagePlayingIndex;
	private boolean isSliding;
	
	private AlphaAnimation aaIn, aaOut;
	
	public ImagePlayViewer(Context context) {
		this(context, null, 0);
	}
	
	public ImagePlayViewer(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public ImagePlayViewer(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	public void init() {
			
		imageViews[0] = new ImageView(getContext());
		imageViews[0].setLayoutParams(new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		imageViews[0].setScaleType(ScaleType.MATRIX);
		this.addView(imageViews[0]);
		
		imageViews[1] = new ImageView(getContext());
		imageViews[1].setLayoutParams(new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		imageViews[1].setScaleType(ScaleType.MATRIX);
		imageViews[1].setVisibility(View.INVISIBLE);
		this.addView(imageViews[1]);
		
		aaIn = new AlphaAnimation(0, 1);
		aaIn.setDuration(ANIM_DURATION);
		
		aaOut = new AlphaAnimation(1, 0);
		aaOut.setDuration(ANIM_DURATION);
	}
	
	public void showNext(String url, String nextUrl) {
		
		downloadImage(url);
		downloadNextBitmap(nextUrl);
	 }

	//현재 보여질 이미지 다운로드, 설정.
	public void downloadImage(final String url) {
		
		//처음일 땐 새로 다운받아야 함.
		if(nextBitmap == null) {
			DownloadUtils.downloadBitmap(url, new OnBitmapDownloadListener() {

				@Override
				public void onError(String url) {

//					LogUtils.log("ImagePlayViewer.onError." + "\nurl : " + url);

					// TODO Auto-generated method stub		
				}

				@Override
				public void onCompleted(String url, Bitmap bitmap) {

//					LogUtils.log("ImagePlayViewer.onCompleted." + "\nurl : " + url);
					
					try {
						imageViews[0].setImageBitmap(bitmap);
						imageViews[0].setImageMatrix(getBitmapMatrix(bitmap));
						slide(imageViews[0]);
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
					}
				}
			});
			
		//처음이 아닌 경우 nextBitmap 이용.
		} else {
			LogUtils.log("###ImagePlayViewer.downloadImage.  " + (imagePlayingIndex%2) + "번째 이미지뷰의 이미지를 변경.");
			imageViews[imagePlayingIndex % 2].setImageBitmap(nextBitmap);
			imageViews[imagePlayingIndex % 2].setImageMatrix(getBitmapMatrix(nextBitmap));
			changeImageViews(imagePlayingIndex);
		}
		
		imagePlayingIndex++;
	}
	
	//이미지 뷰 교체 애니메이션
	public void changeImageViews(int index) {
		
		imageViews[(index + 1) % 2].setVisibility(View.INVISIBLE);
		imageViews[index % 2].setVisibility(View.VISIBLE);
		
		if(needToPlayImage) {
			LogUtils.log("###ImagePlayViewer.changeImageViews.  " + (index%2) + "번째 이미지뷰를 보여주고, " + ((index + 1) %2) + "번째 이미지뷰 숨김.");
			
			imageViews[(index + 1) % 2].startAnimation(aaOut);
			imageViews[index % 2].startAnimation(aaIn);
			slide(imageViews[index % 2]);
		} else {
			//slide 효과.
		}
	}
	
	//슬라이드
	public void slide(final ImageView ivImage) {
	
		final Interpolator interpolator = new AccelerateDecelerateInterpolator();
		final long startTime = System.currentTimeMillis();
		final long duration = SLIDE_DURATION;
		final Matrix matrix = new Matrix(ivImage.getImageMatrix());
		final float totalDist = -ResizeUtils.getScreenWidth() * MOVE_DIST;

		setSliding(true);

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
				} else {
					setSliding(false);
				}
			}
		});
	}
	
	//nextUrl 이미지 다운로드.
	public void downloadNextBitmap(String url) {

		//다음 이미지 다운로드.
		DownloadUtils.downloadBitmap(url,
				new OnBitmapDownloadListener() {
			
			@Override
			public void onError(String url) {
				//서버에서 에러 안나는 이미지를 보내줬다고 가정.
//				LogUtils.log("###ImagePlayViewer.onError.  \nurl : " + url);
			}
			
			@Override
			public void onCompleted(String url, Bitmap bitmap) {
				
//				LogUtils.log("###ImagePlayViewer.onCompleted.  \nurl : " + url);
				nextBitmap = bitmap;
			}
		});
	}
	
	//화면 사이즈와 이미지 사이즈에 알맞는 Matrix 계산, 반환.
	public Matrix getBitmapMatrix(Bitmap bitmap) {

		if(bitmap != null && !bitmap.isRecycled()) {

			Matrix matrix = new Matrix();
			float[] value = new float[9];
			matrix.getValues(value);
			
			int screenWidth = ResizeUtils.getScreenWidth();
			int screenHeight = ResizeUtils.getScreenHeight();
			int bitmapWidth = bitmap.getWidth();
			int bitmapHeight = bitmap.getHeight();

			//세로에 맞게 변환.
			float scale = (float) screenHeight / (float) bitmapHeight;

			//가로가 부족한 경우.
			if(scale * (float) bitmapWidth < screenWidth * (1 + MOVE_DIST)) {
				
				//가로에 맞게 변환.
				scale *= (float) screenWidth * (1 + MOVE_DIST) / (scale * (float) bitmapWidth); 
			}
			
			matrix.postScale(scale, scale);
			return matrix;
		}
		
		return null;
	}
	
	public void setNeedToPlayImage(boolean needToPlayImage) {
		
		this.needToPlayImage = needToPlayImage;
	}

	public boolean isNeedToPlayImage() {
		
		return needToPlayImage;
	}
	
	public void clear() {

		try {
			ViewUnbindHelper.unbindReferences(imageViews[0]);
			ViewUnbindHelper.unbindReferences(imageViews[1]);
			
			if(nextBitmap != null) {
				nextBitmap.recycle();
				nextBitmap = null;
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	public boolean isSliding() {
		return isSliding;
	}

	public void setSliding(boolean isSliding) {
		this.isSliding = isSliding;
	}
}
