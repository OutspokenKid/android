package com.byecar.wrappers;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.byecar.byecarplus.R;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPDownloadUtils;
import com.byecar.models.Car;
import com.byecar.views.CarInfoView;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.TimerUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.TimerUtils.OnTimeChangedListener;

public class ViewWrapperForAuctionCar extends ViewWrapper {
	
	private Car car;
	
	private Activity activity;
	
	private ImageView ivImage;
	private View auctionIcon;
	private CarInfoView carInfoView;
	
	private OnTimeChangedListener onTimeChangedListener;
	
	public ViewWrapperForAuctionCar(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			ivImage = (ImageView) row.findViewById(R.id.list_biddingcar_ivImage);
			auctionIcon = row.findViewById(R.id.list_biddingcar_auctionIcon);
			carInfoView = (CarInfoView) row.findViewById(R.id.list_biddingcar_carInfoView);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			RelativeLayout.LayoutParams rp = null;
			
			//ivImage.
			rp = (RelativeLayout.LayoutParams) ivImage.getLayoutParams();
			rp.height = ResizeUtils.getSpecificLength(460);
			
			//auctionIcon.
			rp = (RelativeLayout.LayoutParams) auctionIcon.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(161);
			rp.height = ResizeUtils.getSpecificLength(51);
			rp.leftMargin = ResizeUtils.getSpecificLength(12);
			rp.bottomMargin = ResizeUtils.getSpecificLength(18);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel instanceof Car) {
				car = (Car) baseModel;
				
				carInfoView.setCarInfo(car);
				
				if(car.getItemCode() == BCPConstants.ITEM_CAR_BID) {
					setAuctionButton(car);
					setOnTimeListener();
				} else {
					auctionIcon.setVisibility(View.INVISIBLE);
				}
				
				downloadImage(car.getRep_img_url(), ivImage);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setListeners() {

	}
	
	@Override
	public void setUnusableView() {

	}

	public void downloadImage(String imageUrl, final ImageView ivImage) {
		
		if(ivImage == null) {
			return;
		} else if(imageUrl == null || imageUrl.length() == 0) {
			ivImage.setImageDrawable(null);
			ivImage.setTag(null);
			return;
		} else if(ivImage.getTag() != null && imageUrl.equals(ivImage.getTag().toString())) {
			//Do nothing because of same image is already set.
			return;
		} else {
			ivImage.setImageDrawable(null);
		}
		
		ivImage.setTag(imageUrl);
		BCPDownloadUtils.downloadBitmap(imageUrl, new OnBitmapDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("ViewWrapperForAuctionCar.downloadImage.onError." + "\nurl : " + url);
			}

			@Override
			public void onCompleted(String url, Bitmap bitmap) {

				try {
					LogUtils.log("ViewWrapperForAuctionCar.downloadImage.onCompleted." + "\nurl : " + url);
					
					String tag = ivImage.getTag().toString().replace("/src/", "/thumb/");
					
					if(tag.contains("?")) {
						tag = tag.split("?")[0];
					}
					
					if(bitmap != null 
							&& !bitmap.isRecycled()
							&& url.contains(tag)) {
						ivImage.setImageBitmap(bitmap);
					}
					
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		}, 640);
	}
	
	public void setOnTimeListener() {

		if(car.getType() != Car.TYPE_BID) {
			return;
		}
		
		if(onTimeChangedListener == null) {
			onTimeChangedListener = new OnTimeChangedListener() {
				
				@Override
				public void onTimeChanged() {

					if(car == null) {
						return;
					}
					
					if(car.getStatus() > Car.STATUS_BID_COMPLETE) {
						carInfoView.clearTime();
						return;
					}

					try {
						long remainTime = car.getBid_until_at() * 1000 
								+ (car.getStatus() < Car.STATUS_BID_COMPLETE ? 0 : 86400000) 
								- System.currentTimeMillis();

						if(remainTime < 0) {
			        		carInfoView.statusChanged(car);
			        	} else {
			        		setAuctionButton(car);
				        	carInfoView.setTime(car);
			        	}
					} catch (Exception e) {
						LogUtils.trace(e);
						TimerUtils.removeOnTimeChangedListener(onTimeChangedListener);
						carInfoView.clearTime();
					}
				}
				
				@Override
				public String getName() {

					return "ViewWrapperForCar";
				}
				
				@Override
				public Activity getActivity() {

					return activity;
				}
			}; 
		}
		
		TimerUtils.addOnTimeChangedListener(onTimeChangedListener);
	}
	
	public void setActivity(Activity activity) {

		this.activity = activity;
	}

	public Car getCar() {
		
		return car;
	}

	public void setAuctionButton(Car car) {

		if(car.getType() != Car.TYPE_BID) {
			auctionIcon.setVisibility(View.INVISIBLE);
			return;
		}
		
		long remainTime = car.getBid_until_at() * 1000 
				+ (car.getStatus() < Car.STATUS_BID_COMPLETE ? 0 : 86400000) 
				- System.currentTimeMillis();
		
		auctionIcon.setVisibility(View.VISIBLE);
		
		if(remainTime < 0) {
			//경매 종료.
			if(car.getStatus() < Car.STATUS_BID_COMPLETE) {
				car.setStatus(Car.STATUS_BID_COMPLETE);
				auctionIcon.setBackgroundResource(R.drawable.auction_sale_icon3);
				
			//입찰 종료.
			} else {
				car.setStatus(Car.STATUS_BID_FAIL);
				auctionIcon.setBackgroundResource(R.drawable.auction_sale_icon4);
			}

		} else {

			if(car.getStatus() < Car.STATUS_BID_COMPLETE) {
				auctionIcon.setBackgroundResource(R.drawable.auction_sale_icon2);
				
				//경매 종료 시간 한시간 이내.
				if(car.getBid_until_at() -System.currentTimeMillis() / 1000 <= 3600) {
					auctionIcon.setVisibility(View.VISIBLE);
				} else {
					auctionIcon.setVisibility(View.INVISIBLE);
				}
			} else if(car.getStatus() == Car.STATUS_BID_COMPLETE) {
				auctionIcon.setBackgroundResource(R.drawable.auction_sale_icon3);
				auctionIcon.setVisibility(View.VISIBLE);
			} else {
				auctionIcon.setBackgroundResource(R.drawable.auction_sale_icon4);
				auctionIcon.setVisibility(View.VISIBLE);
			}
		}
	}
}
