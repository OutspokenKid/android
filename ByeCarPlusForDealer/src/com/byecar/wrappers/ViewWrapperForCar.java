package com.byecar.wrappers;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplusfordealer.R;
import com.byecar.classes.BCPConstants;
import com.byecar.models.Car;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.TimerUtils;
import com.outspoken_kid.utils.TimerUtils.OnTimeChangedListener;
import com.outspoken_kid.utils.ToastUtils;

public class ViewWrapperForCar extends ViewWrapper {
	
	private Car car;
	
	private Activity activity;
	
	private ImageView ivImage;
	private View auctionIcon;
	private TextView tvBiddingPrice;
	private TextView tvBiddingPriceText;
	private Button btnComplete;
	private View remainBg;
	private RelativeLayout timeRelative;
	private ProgressBar progressBar;
	private TextView tvRemainTime;
	private TextView tvRemainTimeText;
	private View timeIcon;
	private TextView tvCarInfo1;
	private TextView tvCarInfo2;
	private TextView tvCurrentPrice;
	private TextView tvCurrentPriceText;
	private TextView tvBidCount;
	private Button btnLike;
	
	private OnTimeChangedListener onTimeChangedListener;
	
	public ViewWrapperForCar(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			ivImage = (ImageView) row.findViewById(R.id.list_car_ivImage);
			auctionIcon = row.findViewById(R.id.list_car_auctionIcon);
			tvBiddingPrice = (TextView) row.findViewById(R.id.list_car_tvBiddingPrice);
			tvBiddingPriceText = (TextView) row.findViewById(R.id.list_car_tvBiddingPriceText);
			btnComplete = (Button) row.findViewById(R.id.list_car_btnComplete);
			remainBg = row.findViewById(R.id.list_car_remainBg);
			timeRelative = (RelativeLayout) row.findViewById(R.id.list_car_timeRelative);
			progressBar = (ProgressBar) row.findViewById(R.id.list_car_progressBar);
			tvRemainTime = (TextView) row.findViewById(R.id.list_car_tvRemainTime);
			tvRemainTimeText = (TextView) row.findViewById(R.id.list_car_tvRemainTimeText);
			timeIcon = row.findViewById(R.id.list_car_timeIcon);
			
			tvCarInfo1 = (TextView) row.findViewById(R.id.list_car_tvCarInfo1);
			tvCarInfo2 = (TextView) row.findViewById(R.id.list_car_tvCarInfo2);
			tvCurrentPrice = (TextView) row.findViewById(R.id.list_car_tvCurrentPrice);
			tvCurrentPriceText = (TextView) row.findViewById(R.id.list_car_tvCurrentPriceText);
			tvBidCount = (TextView) row.findViewById(R.id.list_car_tvBidCount);
			btnLike = (Button) row.findViewById(R.id.list_car_btnLike);
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
			rp.width = ResizeUtils.getSpecificLength(96);
			rp.height = ResizeUtils.getSpecificLength(96);
			rp.leftMargin = ResizeUtils.getSpecificLength(12);
			rp.bottomMargin = ResizeUtils.getSpecificLength(18);

			//tvBiddingPrice.
			rp = (RelativeLayout.LayoutParams) tvBiddingPrice.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(322);
			rp.height = ResizeUtils.getSpecificLength(72);
			rp.bottomMargin = ResizeUtils.getSpecificLength(25);
			tvBiddingPrice.setPadding(ResizeUtils.getSpecificLength(110), 
					0, ResizeUtils.getSpecificLength(20), 0);
			
			//tvBiddingPriceText.			
			rp = (RelativeLayout.LayoutParams) tvBiddingPriceText.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(90);
			rp.height = ResizeUtils.getSpecificLength(72);
			rp.leftMargin = ResizeUtils.getSpecificLength(26);
			
			//btnComplete.
			rp = (RelativeLayout.LayoutParams) btnComplete.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(322);
			rp.height = ResizeUtils.getSpecificLength(72);
			rp.bottomMargin = ResizeUtils.getSpecificLength(25);

			//remainBg.
			rp = (RelativeLayout.LayoutParams) remainBg.getLayoutParams();
			rp.height = ResizeUtils.getSpecificLength(147);
			
			//progressBar.
			rp = (RelativeLayout.LayoutParams) progressBar.getLayoutParams();
			rp.height = ResizeUtils.getSpecificLength(30);
			
			//tvRemainTime.
			rp = (RelativeLayout.LayoutParams) tvRemainTime.getLayoutParams();
			rp.height = ResizeUtils.getSpecificLength(30);
			rp.topMargin = -ResizeUtils.getSpecificLength(3);
			tvRemainTime.setPadding(ResizeUtils.getSpecificLength(90), 0, 0, 0);
			
			//tvRemainTimeText.
			rp = (RelativeLayout.LayoutParams) tvRemainTimeText.getLayoutParams();
			rp.height = ResizeUtils.getSpecificLength(30);
			tvRemainTimeText.setPadding(ResizeUtils.getSpecificLength(22), 0, 0, 0);
			
			//timeIcon.
			rp = (RelativeLayout.LayoutParams) timeIcon.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(18);
			rp.height = ResizeUtils.getSpecificLength(18);
			rp.topMargin = ResizeUtils.getSpecificLength(6);
			rp.rightMargin = ResizeUtils.getSpecificLength(5);
			
			//tvCarInfo1.
			rp = (RelativeLayout.LayoutParams) tvCarInfo1.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(340);
			rp.height = ResizeUtils.getSpecificLength(60);
			rp.leftMargin = ResizeUtils.getSpecificLength(20);
			rp.topMargin = ResizeUtils.getSpecificLength(10);
			
			//line.
			rp = (RelativeLayout.LayoutParams) row.findViewById(R.id.list_car_line).getLayoutParams();
			rp.leftMargin = ResizeUtils.getSpecificLength(10);
			rp.topMargin = ResizeUtils.getSpecificLength(10);
			rp.rightMargin = ResizeUtils.getSpecificLength(10);
			
			//tvCarInfo2.
			rp = (RelativeLayout.LayoutParams) tvCarInfo2.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(340);
			rp.height = ResizeUtils.getSpecificLength(57);
			rp.leftMargin = ResizeUtils.getSpecificLength(20);
			rp.topMargin = ResizeUtils.getSpecificLength(24);
			
			//tvCurrentPrice.
			rp = (RelativeLayout.LayoutParams) tvCurrentPrice.getLayoutParams();
			rp.topMargin = ResizeUtils.getSpecificLength(26);
			rp.rightMargin = ResizeUtils.getSpecificLength(20);
			
			//tvCurrentPriceText.
			rp = (RelativeLayout.LayoutParams) tvCurrentPriceText.getLayoutParams();
			rp.rightMargin = ResizeUtils.getSpecificLength(4);
			rp.bottomMargin = ResizeUtils.getSpecificLength(8);
			
			//tvBidCount.
			rp = (RelativeLayout.LayoutParams) tvBidCount.getLayoutParams();
			rp.topMargin = ResizeUtils.getSpecificLength(24);

			//btnLike.
			rp = (RelativeLayout.LayoutParams) btnLike.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(90);
			rp.height = ResizeUtils.getSpecificLength(40);
			rp.bottomMargin = -ResizeUtils.getSpecificLength(8);
			btnLike.setPadding(ResizeUtils.getSpecificLength(32), 0, 
					ResizeUtils.getSpecificLength(10), ResizeUtils.getSpecificLength(2));
			
			//tvLikeText.
			rp = (RelativeLayout.LayoutParams) row.findViewById(R.id.list_car_tvLikeText).getLayoutParams();
			rp.topMargin = ResizeUtils.getSpecificLength(5);
			rp.rightMargin = ResizeUtils.getSpecificLength(2);

			FontUtils.setFontSize(tvBiddingPrice, 28);
			FontUtils.setFontStyle(tvBiddingPrice, FontUtils.BOLD);
			FontUtils.setFontSize(tvBiddingPriceText, 18);
			FontUtils.setFontSize(tvRemainTime, 24);
			FontUtils.setFontStyle(tvRemainTime, FontUtils.BOLD);
			FontUtils.setFontSize(tvRemainTimeText, 16);
			FontUtils.setFontSize(tvCarInfo1, 32);
			FontUtils.setFontStyle(tvCarInfo1, FontUtils.BOLD);
			FontUtils.setFontSize(tvCarInfo2, 20);
			FontUtils.setFontSize(tvCurrentPrice, 32);
			FontUtils.setFontStyle(tvCurrentPrice, FontUtils.BOLD);
			FontUtils.setFontSize(tvCurrentPriceText, 20);
			FontUtils.setFontSize(tvBidCount, 20);
			
			FontUtils.setFontSize(btnLike, 18);
			FontUtils.setFontSize((TextView)row.findViewById(R.id.list_car_tvLikeText), 20);
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
				
				tvCarInfo1.setText(car.getCar_full_name());
				tvCarInfo2.setText(car.getYear() + "년 / "
						+ StringUtils.getFormattedNumber(car.getMileage()) + "km / "
						+ car.getArea());
				
				if(car.getType() == Car.TYPE_BID) {
					tvCurrentPriceText.setText(R.string.currentPrice);
				} else {
					tvCurrentPriceText.setText(R.string.salesPrice);
				}
				
				tvCurrentPrice.setText(StringUtils.getFormattedNumber(car.getPrice()) 
						+ row.getContext().getString(R.string.won));
				tvBidCount.setText("입찰자 " + car.getBids_cnt() + "명");
				
				if(car.getItemCode() == BCPConstants.ITEM_CAR_BID
					|| car.getItemCode() == BCPConstants.ITEM_CAR_BID_MY) {
					tvRemainTime.setText("-- : -- : --");
					
					auctionIcon.setVisibility(View.VISIBLE);
					timeRelative.setVisibility(View.VISIBLE);
					tvBidCount.setVisibility(View.VISIBLE);
					
					if(car.getStatus() < Car.STATUS_BID_COMPLETE) {
						auctionIcon.setBackgroundResource(R.drawable.main_hotdeal_mark);
						progressBar.setProgressDrawable(row.getContext().getResources().getDrawable(R.drawable.progressbar_custom_orange));
					} else if(car.getStatus() == Car.STATUS_BID_COMPLETE) {
						progressBar.setProgressDrawable(row.getContext().getResources().getDrawable(R.drawable.progressbar_custom_green));
						auctionIcon.setBackgroundResource(R.drawable.main_hotdeal_mark2);
					} else {
						progressBar.setProgressDrawable(row.getContext().getResources().getDrawable(R.drawable.progressbar_custom_gray));
						auctionIcon.setBackgroundResource(R.drawable.main_hotdeal_mark3);
					}
					
					((RelativeLayout.LayoutParams) btnLike.getLayoutParams()).rightMargin = ResizeUtils.getSpecificLength(14);
					
					setOnTimeListener();
				} else {
					auctionIcon.setVisibility(View.INVISIBLE);
					timeRelative.setVisibility(View.GONE);
					tvBidCount.setVisibility(View.INVISIBLE);
					
					((RelativeLayout.LayoutParams) btnLike.getLayoutParams()).rightMargin = ResizeUtils.getSpecificLength(-100);
				}
				
				if(car.getItemCode() == BCPConstants.ITEM_CAR_DEALER_IN_PROGRESS) {
					btnComplete.setVisibility(View.VISIBLE);
				} else {
					btnComplete.setVisibility(View.INVISIBLE);
				}
				
				if(car.getItemCode() == BCPConstants.ITEM_CAR_BID_MY) {
					tvBiddingPrice.setVisibility(View.VISIBLE);
					tvBiddingPriceText.setVisibility(View.VISIBLE);
					
					tvBiddingPrice.setText(
							StringUtils.getFormattedNumber(car.getPrice())
							+ row.getContext().getString(R.string.won));
				} else {
					tvBiddingPrice.setVisibility(View.INVISIBLE);
					tvBiddingPriceText.setVisibility(View.INVISIBLE);
				}
				
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
				
				setImage(ivImage, car.getRep_img_url());
			}
		} catch (Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setListeners() {

		if(car != null) {
			btnComplete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					ToastUtils.showToast("완료버튼");
				}
			});
		}
	}
	
	@Override
	public void setUnusableView() {

	}

	public void setOnTimeListener() {

		if(onTimeChangedListener == null) {
			onTimeChangedListener = new OnTimeChangedListener() {
				
				@Override
				public void onTimeChanged() {

					if(car == null) {
						return;
					}
					
					if(car.getStatus() > Car.STATUS_BID_COMPLETE) {
						progressBar.setProgress(10000);
						tvRemainTime.setText("-- : -- : --");
						return;
					}
					
					/*
					 * ms 단위.
					 * progress : 0 ~ 1000
					 * progressTime = (car.getStatus() < Car.STATUS_BID_COMPLETE ? (bid_until_at - bid_begin_at) * 1000 : 86400000);
					 * (endTime = (bid_until_at * 1000) + (car.getStatus() < Car.STATUS_BID_COMPLETE ? 0 : 86400000))
					 * remainTime = endTime - currentTime
					 * 		=  (bid_until_at * 1000) + (car.getStatus() < Car.STATUS_BID_COMPLETE ? 0 : 86400000)) - currentTime
					 * (passedTime = progressTime - remainTime)
					 * progressValue = (passedTime / progressTime) * 1000
					 * 		= (progressTime - remainTime) / progressTime * 1000
					 * 		= 1000 - (remainTime * 1000 / progressTime)
					 */

					long progressTime = (car.getStatus() < Car.STATUS_BID_COMPLETE ? 
							(car.getBid_until_at() - car.getBid_begin_at()) * 1000 : 86400000);
					long remainTime = car.getBid_until_at() * 1000 
							+ (car.getStatus() < Car.STATUS_BID_COMPLETE ? 0 : 86400000) 
							- System.currentTimeMillis();
		        	long progressValue = 1000 - (remainTime * 1000 / progressTime);
		        	
		        	String formattedRemainTime = StringUtils.getDateString("HH : mm : ss", remainTime);
		        	tvRemainTime.setText(formattedRemainTime);
		        	progressBar.setProgress((int)progressValue);
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
}
