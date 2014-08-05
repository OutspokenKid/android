package com.zonecomms.common.views;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.outspoken_kid.classes.ViewUnbindHelper;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.ResizeUtils;

public class ImagePlayViewer extends FrameLayout {

	public static final int SLIDE_DURATION = 3000;
	public static int imageIndex;
	
	private static final int ANIM_DURATION = 2000;
	private static final float MOVE_DIST = 0.05f;
	
	private static final int MODE_DUAL = 0;
	private static final int MODE_MORE = 1;
	
	private ImageView[] imageViews = new ImageView[2];
	private Bitmap nextBitmap;
	
	private ArrayList<String> imageUrls;
	private boolean needPlayImage;
	private int mode;
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
	
	public void setImageUrls(ArrayList<String> imageUrls) {
		
		//서버에서 이미지가 한장만 오는 경우는 없고, 잘못된 이미지가 올라가 있는 경우가 없다고 가정.
		//모드 설정.
		if(imageUrls.size() == 2){
			mode = MODE_DUAL;
		} else {
			mode = MODE_MORE;
		}

		//이미지 url 셋 설정.
		this.imageUrls = imageUrls;
		
		//초기 설정, imageUrls의 url중 첫번째 이미지를 가리키므로 imageIndex = 0.
		imageIndex = 0;
		
		//첫번째 이미지 다운로드, 끝난 후 이미지 세팅.
		DownloadUtils.downloadBitmap(imageUrls.get(0), new OnBitmapDownloadListener() {
			
			@Override
			public void onError(String url) {
				//서버에서 에러 안나는 이미지를 보내줬다고 가정.
			}
			
			@Override
			public void onCompleted(String url, Bitmap bitmap) {
				imageViews[0].setImageBitmap(bitmap);
				imageViews[0].setImageMatrix(getBitmapMatrix(bitmap));
			}
		});
	}
	
	public void start() {

		slideImage(imageViews[0]);
	}
	
	public void showNext() {
		
		if(mode == MODE_DUAL) {
			changeImageViews();
		
		} else {
			//이전 이미지 숨김 및 Recycle.
			hideCurrentImageViewAndRecycle();
			
			//새로운 이미지 노출.
			showNewImageView();
		}
	 }
	
	//MODE_DUAL 일때만 사용.
	public void changeImageViews() {
		
		imageViews[(imageIndex + 1) % 2].setVisibility(View.INVISIBLE);
		imageViews[imageIndex % 2].setVisibility(View.VISIBLE);
		imageViews[imageIndex % 2].setImageBitmap(nextBitmap);
		
		if(needPlayImage) {
			aaOut.setAnimationListener(null);
			imageViews[(imageIndex + 1) % 2].startAnimation(aaOut);
			imageViews[imageIndex % 2].startAnimation(aaIn);
			slideImage(imageViews[imageIndex % 2]);
		} else {
			downloadNextBitmap();
		}
	}
	
	//MODE_MORE 일때만 사용.
	public void hideCurrentImageViewAndRecycle() {
		
		LogUtils.log("###where.hideCurrentImageViewAndRecycle.  index : " + ((imageIndex + 1) % 2));
		
		aaOut.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				
				imageViews[(imageIndex + 1) % 2].setVisibility(View.INVISIBLE);
				ViewUnbindHelper.unbindReferences(imageViews[(imageIndex + 1) % 2]);
			}
		});
		imageViews[(imageIndex + 1) % 2].startAnimation(aaOut);
	}
	
	//MODE_MORE 일때만 사용.
	public void showNewImageView() {
		
		LogUtils.log("###where.showNewImageView.  index : " + (imageIndex % 2));
		
		imageViews[imageIndex % 2].setImageBitmap(nextBitmap);
		imageViews[imageIndex % 2].setImageMatrix(getBitmapMatrix(nextBitmap));
		imageViews[imageIndex % 2].setVisibility(View.VISIBLE);
		imageViews[imageIndex % 2].startAnimation(aaIn);
		slideImage(imageViews[imageIndex % 2]);

	}
	
	private void slideImage(final ImageView ivImage) {
		
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
                	downloadNextBitmap();
                }
            }
        });
	}
	
	public void downloadNextBitmap() {

		//다음 이미지 다운로드.
		DownloadUtils.downloadBitmap(imageUrls.get((imageIndex+1)%imageUrls.size()),
				new OnBitmapDownloadListener() {
			
			@Override
			public void onError(String url) {
				//서버에서 에러 안나는 이미지를 보내줬다고 가정.
				LogUtils.log("###ImagePlayViewer.onError.  \nurl : " + url);
			}
			
			@Override
			public void onCompleted(String url, Bitmap bitmap) {
				
				LogUtils.log("###ImagePlayViewer.onCompleted.  \nurl : " + url);
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
	
	public void setNeedPlayImage(boolean needPlayImage) {
		
		this.needPlayImage = needPlayImage;
	}
	
	public void clear() {

		try {
			ViewUnbindHelper.unbindReferences(imageViews[0]);
			ViewUnbindHelper.unbindReferences(imageViews[1]);
			nextBitmap.recycle();
			nextBitmap = null;
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
