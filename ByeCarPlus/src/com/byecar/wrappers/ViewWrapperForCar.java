package com.byecar.wrappers;

import org.json.JSONObject;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPAuctionableFragment;
import com.byecar.classes.BCPConstants;
import com.byecar.models.Car;
import com.byecar.views.PriceTextView;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.TimerUtils;
import com.outspoken_kid.utils.TimerUtils.OnTimeChangedListener;

public class ViewWrapperForCar extends ViewWrapper {
	
	private Car car;
	
	private Activity activity;
	private BCPAuctionableFragment fragment;
	
	private ImageView ivImage;
	private View auctionIcon;
	private View remainBg;
	private RelativeLayout timeRelative;
	private ProgressBar progressBar;
	private TextView tvRemainTime;
	private TextView tvRemainTimeText;
	private View timeIcon;
	private TextView tvCarInfo1;
	private TextView tvCarInfo2;
	private PriceTextView priceTextView;
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
			remainBg = row.findViewById(R.id.list_car_remainBg);
			timeRelative = (RelativeLayout) row.findViewById(R.id.list_car_timeRelative);
			progressBar = (ProgressBar) row.findViewById(R.id.list_car_progressBar);
			tvRemainTime = (TextView) row.findViewById(R.id.list_car_tvRemainTime);
			tvRemainTimeText = (TextView) row.findViewById(R.id.list_car_tvRemainTimeText);
			timeIcon = row.findViewById(R.id.list_car_timeIcon);
			
			tvCarInfo1 = (TextView) row.findViewById(R.id.list_car_tvCarInfo1);
			tvCarInfo2 = (TextView) row.findViewById(R.id.list_car_tvCarInfo2);
			priceTextView = (PriceTextView) row.findViewById(R.id.list_car_priceTextView);
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
			rp.leftMargin = -ResizeUtils.getSpecificLength(10);
			rp.topMargin = ResizeUtils.getSpecificLength(7);
			
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
			
			//priceTextView.
			rp = (RelativeLayout.LayoutParams) priceTextView.getLayoutParams();
			rp.topMargin = ResizeUtils.getSpecificLength(28);
			rp.rightMargin = ResizeUtils.getSpecificLength(20);
			
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

			FontUtils.setFontSize(tvRemainTime, 24);
			FontUtils.setFontStyle(tvRemainTime, FontUtils.BOLD);
			FontUtils.setFontSize(tvRemainTimeText, 16);
			FontUtils.setFontSize(tvCarInfo1, 32);
			FontUtils.setFontStyle(tvCarInfo1, FontUtils.BOLD);
			FontUtils.setFontSize(tvCarInfo2, 20);
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
					priceTextView.setType(PriceTextView.TYPE_DETAIL_AUCTION);
				} else {
					priceTextView.setType(PriceTextView.TYPE_DETAIL_OTHERS);
				}
				
				priceTextView.setPrice(car.getPrice());
				
				if(car.getItemCode() == BCPConstants.ITEM_CAR_BID) {
					tvRemainTime.setText("-- : -- : --");
					
					auctionIcon.setVisibility(View.VISIBLE);
					timeRelative.setVisibility(View.VISIBLE);
					tvBidCount.setVisibility(View.VISIBLE);
					tvBidCount.setText("입찰자 " + car.getBids_cnt() + "명");
					
					if(car.getStatus() < Car.STATUS_BID_COMPLETE) {
						auctionIcon.setBackgroundResource(R.drawable.main_hotdeal_mark);
						progressBar.setProgressDrawable(row.getContext().getResources().getDrawable(R.drawable.progressbar_custom_orange));
						
						//경매 종료 시간 한시간 이내.
						if(car.getBid_until_at() -System.currentTimeMillis() / 1000 <= 3600) {
							auctionIcon.setVisibility(View.VISIBLE);
						} else {
							auctionIcon.setVisibility(View.INVISIBLE);
						}
					} else if(car.getStatus() == Car.STATUS_BID_COMPLETE) {
						progressBar.setProgressDrawable(row.getContext().getResources().getDrawable(R.drawable.progressbar_custom_green));
						auctionIcon.setBackgroundResource(R.drawable.main_hotdeal_mark2);
						auctionIcon.setVisibility(View.VISIBLE);
					} else {
						progressBar.setProgressDrawable(row.getContext().getResources().getDrawable(R.drawable.progressbar_custom_gray));
						auctionIcon.setBackgroundResource(R.drawable.main_hotdeal_mark3);
						auctionIcon.setVisibility(View.VISIBLE);
					}
					
					((RelativeLayout.LayoutParams) btnLike.getLayoutParams()).rightMargin = ResizeUtils.getSpecificLength(14);
					
					setOnTimeListener();
					
				} else {
					auctionIcon.setVisibility(View.INVISIBLE);
					timeRelative.setVisibility(View.GONE);
					tvBidCount.setVisibility(View.INVISIBLE);
					
					((RelativeLayout.LayoutParams) btnLike.getLayoutParams()).rightMargin = ResizeUtils.getSpecificLength(-100);
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
			
			btnLike.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					setLike(car);
				}
			});
		}
	}
	
	@Override
	public void setUnusableView() {

	}

	public void setLike(Car car) {
		
		String url = null;
		
		if(car.getIs_liked() == 0) {
			btnLike.setBackgroundResource(R.drawable.main_like_btn_b);
			car.setLikes_cnt(car.getLikes_cnt() + 1);
			car.setIs_liked(1);

			switch (car.getType()) {
			
			case Car.TYPE_BID:
				url = BCPAPIs.CAR_BID_LIKE_URL;
				break;
				
			case Car.TYPE_DEALER:
				url = BCPAPIs.CAR_DEALER_LIKE_URL;
				break;
			}
		} else {
			btnLike.setBackgroundResource(R.drawable.main_like_btn_a);
			car.setLikes_cnt(car.getLikes_cnt() - 1);
			car.setIs_liked(0);
			
			switch (car.getType()) {
			
			case Car.TYPE_BID:
				url = BCPAPIs.CAR_BID_UNLIKE_URL;
				break;
				
			case Car.TYPE_DEALER:
				url = BCPAPIs.CAR_DEALER_UNLIKE_URL;
				break;
			}
		}
		
		btnLike.setText("" + car.getLikes_cnt());
		
		url += "?onsalecar_id=" + car.getId();
		
		DownloadUtils.downloadJSONString(url,
				new OnJSONDownloadListener() {

					@Override
					public void onError(String url) {

						LogUtils.log("ViewWrapperForCar.onError." + "\nurl : "
								+ url);
					}

					@Override
					public void onCompleted(String url,
							JSONObject objJSON) {

						try {
							LogUtils.log("ViewWrapperForCar.onCompleted."
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
						progressBar.setProgress(1000);
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

					try {
						long progressTime = (car.getStatus() < Car.STATUS_BID_COMPLETE ? 
								(car.getBid_until_at() - car.getBid_begin_at()) * 1000 : 86400000);
						long remainTime = car.getBid_until_at() * 1000 
								+ (car.getStatus() < Car.STATUS_BID_COMPLETE ? 0 : 86400000) 
								- System.currentTimeMillis();
//			        	long progressValue = 1000 - (remainTime * 1000 / progressTime);
						long progressValue = remainTime * 1000 / progressTime;
			        	
			        	if(remainTime < 0) {
			        		
			        		//경매 종료.
			        		if(car.getStatus() < Car.STATUS_BID_COMPLETE) {
			        			car.setStatus(Car.STATUS_BID_COMPLETE);
			        			progressBar.setProgressDrawable(row.getContext().getResources().getDrawable(R.drawable.progressbar_custom_green));
			    				auctionIcon.setBackgroundResource(R.drawable.main_hotdeal_mark2);
			    				
			        		//입찰 종료.
			        		} else {
			        			car.setStatus(Car.STATUS_BID_FAIL);
			        			progressBar.setProgressDrawable(row.getContext().getResources().getDrawable(R.drawable.progressbar_custom_gray));
			    				auctionIcon.setBackgroundResource(R.drawable.main_hotdeal_mark3);
			        		}
			        		
			        		fragment.bidStatusChanged("time_over", car);
			        		
			        	} else {
			        		String formattedRemainTime = StringUtils.getTimeString(remainTime);
				        	tvRemainTime.setText(formattedRemainTime);
				        	progressBar.setProgress((int)progressValue);
			        	}
			        	
					} catch (Exception e) {
						LogUtils.trace(e);
						TimerUtils.removeOnTimeChangedListener(onTimeChangedListener);
						tvRemainTime.setText("-- : -- : --");
						progressBar.setProgress(1000);
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
	
	public void setFragment(BCPAuctionableFragment fragment) {
		
		this.fragment = fragment;
	}

	public Car getCar() {
		
		return car;
	}
}
