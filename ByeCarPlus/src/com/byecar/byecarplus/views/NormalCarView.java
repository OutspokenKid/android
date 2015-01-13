package com.byecar.byecarplus.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.byecarplus.models.Car;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class NormalCarView extends RelativeLayout {

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
		ResizeUtils.viewResizeForRelative(216, 66, tvCar,
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
		ResizeUtils.viewResizeForRelative(140, 66, tvCarInfo, 
				new int[]{RelativeLayout.ALIGN_PARENT_RIGHT}, new int[]{0},
				new int[]{0, 0, 18, 0});
		tvCarInfo.setTextColor(textColor);
		FontUtils.setFontSize(tvCarInfo, 18);
		tvCarInfo.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
		this.addView(tvCarInfo);

		//tvPrice.
		tvPrice = new TextView(getContext());
		ResizeUtils.viewResizeForRelative(LayoutParams.WRAP_CONTENT, 64, tvPrice, 
				new int[]{RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.ALIGN_PARENT_BOTTOM}, 
				new int[]{0, 0}, new int[]{0, 0, 18, 0});
		tvPrice.setId(R.id.normalCarView_tvPrice);
		FontUtils.setFontSize(tvPrice, 26);
		FontUtils.setFontStyle(tvPrice, FontUtils.BOLD);
		tvPrice.setTextColor(textColor);
		tvPrice.setGravity(Gravity.CENTER_VERTICAL);
		this.addView(tvPrice);
		
		TextView tvPriceText = new TextView(getContext());
		ResizeUtils.viewResizeForRelative(LayoutParams.WRAP_CONTENT, 64, tvPriceText,
				new int[]{RelativeLayout.LEFT_OF, RelativeLayout.ALIGN_PARENT_BOTTOM}, 
				new int[]{R.id.normalCarView_tvPrice, 0}, new int[]{0, 0, 18, 0});
		FontUtils.setFontSize(tvPriceText, 18);
		tvPriceText.setTextColor(textColor);
		tvPriceText.setGravity(Gravity.CENTER_VERTICAL);
		tvPriceText.setText(getContext().getString(R.string.salesPrice));
		this.addView(tvPriceText);
		
		setFrameToDefault();
	}

	public void setCar(Car car) {
		
		if (car != null) {
			downloadImage(car.getRep_img_url());
			
			tvCar.setText(car.getModel_name());
			tvCarInfo.setText(car.getYear() + getContext().getString(R.string.year) + " / "
					+ StringUtils.getFormattedNumber(car.getMileage()) + "km");
			tvPrice.setText(StringUtils.getFormattedNumber(car.getPrice())
					+ getContext().getString(R.string.won));
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
}
