package com.byecar.views;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.models.Car;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class NormalCarView extends RelativeLayout {

	private Car car;
	
	public NormalCarView(Context context) {
		this(context, null, 0);
	}
	
	public NormalCarView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public NormalCarView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private ImageView ivImage;
	private TextView tvCar;
	private TextView tvCarInfo;
	private TextView tvPrice;
	private TextView tvLikeText;
	private Button btnLike;

	public void init() {
		
		ResizeUtils.viewResize(578, 132, this, 1, Gravity.CENTER_HORIZONTAL, null);

		// ivImage.
		ivImage = new ImageView(getContext());
		ResizeUtils.viewResizeForRelative(186, LayoutParams.MATCH_PARENT, 
				ivImage, null, null, null);
		ivImage.setId(R.id.normalCarView_ivImage);
		ivImage.setScaleType(ScaleType.CENTER_CROP);
		ivImage.setBackgroundResource(R.drawable.directmarket_default);
		this.addView(ivImage);

		// frame.
		View frame = new View(getContext());
		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, frame, null, null, null);
		frame.setBackgroundResource(R.drawable.directmarket_market_frame2);
		this.addView(frame);

		int textColor = getResources().getColor(R.color.holo_text);

		//tvCar.
		tvCar = new TextView(getContext());
		ResizeUtils.viewResizeForRelative(210, 66, tvCar,
				new int[]{RelativeLayout.RIGHT_OF}, new int[]{R.id.normalCarView_ivImage}, 
				new int[]{14, 0, 0, 0});
		tvCar.setTextColor(textColor);
		FontUtils.setFontSize(tvCar, 26);
		FontUtils.setFontStyle(tvCar, FontUtils.BOLD);
		tvCar.setEllipsize(TruncateAt.END);
		tvCar.setGravity(Gravity.CENTER_VERTICAL);
		this.addView(tvCar);

		//tvCarInfo.
		tvCarInfo = new TextView(getContext());
		ResizeUtils.viewResizeForRelative(150, 66, tvCarInfo, 
				new int[]{RelativeLayout.ALIGN_PARENT_RIGHT}, new int[]{0},
				new int[]{0, 0, 10, 0});
		tvCarInfo.setTextColor(textColor);
		FontUtils.setFontSize(tvCarInfo, 16);
		tvCarInfo.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
		this.addView(tvCarInfo);

		//tvLikeText.
		tvLikeText = new TextView(getContext());
		ResizeUtils.viewResizeForRelative(LayoutParams.WRAP_CONTENT, 64, tvLikeText, 
				new int[]{RelativeLayout.RIGHT_OF, RelativeLayout.ALIGN_BOTTOM}, 
				new int[]{R.id.normalCarView_ivImage, R.id.normalCarView_ivImage}, 
				new int[]{14, 0, 0, 0});
		tvLikeText.setId(R.id.normalCarView_tvLikeText);
		tvLikeText.setText(R.string.like);
		tvLikeText.setTextColor(textColor);
		FontUtils.setFontSize(tvLikeText, 20);
		tvLikeText.setGravity(Gravity.CENTER_VERTICAL);
		this.addView(tvLikeText);
		
		//btnLike.
		btnLike = new Button(getContext());
		ResizeUtils.viewResizeForRelative(90, 40, btnLike, 
				new int[]{RelativeLayout.RIGHT_OF, RelativeLayout.ALIGN_BOTTOM}, 
				new int[]{R.id.normalCarView_tvLikeText, R.id.normalCarView_tvLikeText},
				new int[]{0, 0, 0, 12});
		btnLike.setPadding(ResizeUtils.getSpecificLength(32), 0, 
				ResizeUtils.getSpecificLength(10), ResizeUtils.getSpecificLength(2));
		btnLike.setTextColor(Color.WHITE);
		FontUtils.setFontSize(btnLike, 18);
		btnLike.setGravity(Gravity.CENTER);
		this.addView(btnLike);

		//tvPrice.
		tvPrice = new TextView(getContext());
		ResizeUtils.viewResizeForRelative(LayoutParams.WRAP_CONTENT, 64, tvPrice, 
				new int[]{RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.ALIGN_PARENT_BOTTOM}, 
				new int[]{0, 0}, new int[]{0, 0, 10, 0});
		tvPrice.setId(R.id.normalCarView_tvPrice);
		FontUtils.setFontSize(tvPrice, 24);
		FontUtils.setFontStyle(tvPrice, FontUtils.BOLD);
		tvPrice.setTextColor(textColor);
		tvPrice.setGravity(Gravity.CENTER_VERTICAL);
		this.addView(tvPrice);
		
		TextView tvPriceText = new TextView(getContext());
		ResizeUtils.viewResizeForRelative(LayoutParams.WRAP_CONTENT, 64, tvPriceText,
				new int[]{RelativeLayout.LEFT_OF, RelativeLayout.ALIGN_PARENT_BOTTOM}, 
				new int[]{R.id.normalCarView_tvPrice, 0}, new int[]{0, 0, 4, 0});
		FontUtils.setFontSize(tvPriceText, 18);
		tvPriceText.setTextColor(textColor);
		tvPriceText.setGravity(Gravity.CENTER_VERTICAL);
		tvPriceText.setText(getContext().getString(R.string.salesPrice));
		this.addView(tvPriceText);
		
		setFrameToDefault();
	}

	public void setCar(Car car) {
		
		this.car = car;
		
		if (car != null) {
			downloadImage(car.getRep_img_url());
			
			tvCar.setText(car.getModel_name());
			tvCarInfo.setText(car.getYear() + getContext().getString(R.string.year) + " / "
					+ StringUtils.getFormattedNumber(car.getMileage()) + "km");
			tvPrice.setText(StringUtils.getFormattedNumber(car.getPrice())
					+ getContext().getString(R.string.won));
			
			if(car.getIs_liked() == 0) {
				btnLike.setBackgroundResource(R.drawable.main_like_btn_a);
			} else {
				btnLike.setBackgroundResource(R.drawable.main_like_btn_b);
			}

			int likesCount = car.getLikes_cnt();
			
			if(likesCount > 9999) {
				likesCount = 9999;
			}
			
			btnLike.setText("" + likesCount);
			
			if(car != null) {
				
				btnLike.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {

						setLike(NormalCarView.this.car);
					}
				});
			}
		} else {
			setFrameToDefault();
		}
	}

	public void downloadImage(String imageUrl) {

		ivImage.setImageDrawable(null);

		ivImage.setTag(imageUrl);
		DownloadUtils.downloadBitmap(imageUrl, new OnBitmapDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("NormalCarFrame.downloadImage.onError."
						+ "\nurl : " + url);
			}

			@Override
			public void onCompleted(String url, Bitmap bitmap) {

				try {
					LogUtils.log("NormalCarFrame.downloadImage.onCompleted."
							+ "\nurl : " + url);

					if (bitmap != null && !bitmap.isRecycled()) {
						ivImage.setImageBitmap(bitmap);
					}

				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}

	public ImageView getIvImage() {

		return ivImage;
	}

	public void setFrameToDefault() {
		
		ivImage.setImageDrawable(null);
		tvCar.setText(R.string.standingByRegistration);
		tvCarInfo.setText("--" + getContext().getString(R.string.year) + " / " + "--km");
		tvPrice.setText("0" + getContext().getString(R.string.won));
	}
	
	public void setLike(Car car) {
		
		String url = null;
		
		if(car.getIs_liked() == 0) {
			btnLike.setBackgroundResource(R.drawable.main_like_btn_b);
			car.setLikes_cnt(car.getLikes_cnt() + 1);
			car.setIs_liked(1);
			url = BCPAPIs.CAR_DIRECT_NORMAL_LIKE_URL;
			
		} else {
			btnLike.setBackgroundResource(R.drawable.main_like_btn_a);
			car.setLikes_cnt(car.getLikes_cnt() - 1);
			car.setIs_liked(0);
			url = BCPAPIs.CAR_DIRECT_NORMAL_UNLIKE_URL;
		}
		
		btnLike.setText("" + car.getLikes_cnt());
		
		url += "?onsalecar_id=" + car.getId();
		
		DownloadUtils.downloadJSONString(url,
				new OnJSONDownloadListener() {

					@Override
					public void onError(String url) {

						LogUtils.log("NormalCarView.onError." + "\nurl : "
								+ url);
					}

					@Override
					public void onCompleted(String url,
							JSONObject objJSON) {

						try {
							LogUtils.log("NormalCarView.onCompleted."
									+ "\nurl : " + url
									+ "\nresult : " + objJSON);
						} catch (Exception e) {
							LogUtils.trace(e);
						} catch (OutOfMemoryError oom) {
							LogUtils.trace(oom);
						}
					}
				});
	}
}
