package com.byecar.views;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.models.Car;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class CarInfoView extends RelativeLayout {

	public static final int TYPE_MAIN_AUCTION = 0;
	
	private ProgressBar progressBar;
	private View centerView;
	private TextView tvRemainTime;
	private View timeIcon;
	private View infoBg;
	private TextView tvCarInfo1;
	private PriceTextView priceTextView;
	private View lineForCarInfo;
	private TextView tvCarInfo2;
	private TextView tvBidCount;
	
	private int type;
	
	public CarInfoView(Context context) {
		this(context, null, 0);
	}
	
	public CarInfoView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public CarInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public void init() {

		progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
		progressBar.setId(R.id.bidInfoView_progressBar);
		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, 42, progressBar, 
				new int[]{ALIGN_PARENT_TOP}, 
				new int[]{0}, null);
		progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_custom_orange));
		progressBar.setMax(1000);
		progressBar.setProgress(0);
		this.addView(progressBar);
		
		centerView = new View(getContext());
		centerView.setId(R.id.bidInfoView_centerView);
		ResizeUtils.viewResizeForRelative(ResizeUtils.getSpecificLength(10), 1, centerView, 
				new int[]{ALIGN_TOP, CENTER_HORIZONTAL}, 
				new int[]{R.id.bidInfoView_progressBar, 0}, null);
		this.addView(centerView);
		
		tvRemainTime = new TextView(getContext());
		tvRemainTime.setId(R.id.bidInfoView_tvRemainTime);
		ResizeUtils.viewResizeForRelative(LayoutParams.WRAP_CONTENT, 42, tvRemainTime, 
				new int[]{ALIGN_TOP, RIGHT_OF}, 
				new int[]{R.id.bidInfoView_progressBar, R.id.bidInfoView_centerView}, null);
		tvRemainTime.setTextColor(Color.WHITE);
		tvRemainTime.setGravity(Gravity.CENTER_VERTICAL);
		FontUtils.setFontSize(tvRemainTime, 30);
		FontUtils.setFontStyle(tvRemainTime, FontUtils.BOLD);
		this.addView(tvRemainTime);
		
		timeIcon = new View(getContext());
		ResizeUtils.viewResizeForRelative(163, 22, timeIcon, 
				new int[]{ALIGN_TOP, LEFT_OF}, 
				new int[]{R.id.bidInfoView_progressBar, R.id.bidInfoView_centerView},
				new int[]{0, 10, 0, 0});
		timeIcon.setBackgroundResource(R.drawable.main_time);
		this.addView(timeIcon);
		
		infoBg = new View(getContext());
		infoBg.setId(R.id.bidInfoView_infoBg);
		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, 126, infoBg, 
				new int[]{RelativeLayout.BELOW}, 
				new int[]{R.id.bidInfoView_progressBar}, null);
		infoBg.setBackgroundColor(Color.WHITE);
		this.addView(infoBg);
		
		tvCarInfo1 = new TextView(getContext());
		tvCarInfo1.setId(R.id.bidInfoView_tvCarInfo1);
		ResizeUtils.viewResizeForRelative(340, 63, tvCarInfo1, 
				new int[]{RelativeLayout.ALIGN_PARENT_LEFT, BELOW}, 
				new int[]{0, R.id.bidInfoView_progressBar}, 
				new int[]{20, 0, 0, 0});
		tvCarInfo1.setSingleLine();
		tvCarInfo1.setEllipsize(TruncateAt.END);
		tvCarInfo1.setTextColor(getResources().getColor(R.color.holo_text));
		tvCarInfo1.setGravity(Gravity.CENTER_VERTICAL);
		FontUtils.setFontSize(tvCarInfo1, 32);
		FontUtils.setFontStyle(tvCarInfo1, FontUtils.BOLD);
		this.addView(tvCarInfo1);
		
		priceTextView = new PriceTextView(getContext());
		priceTextView.setId(R.id.bidInfoView_priceTextView);
		ResizeUtils.viewResizeForRelative(LayoutParams.WRAP_CONTENT, 63, priceTextView, 
				new int[]{RelativeLayout.ALIGN_PARENT_RIGHT, BELOW}, 
				new int[]{0, R.id.bidInfoView_progressBar}, 
				new int[]{0, 0, 20, 0});
		this.addView(priceTextView);
		
		lineForCarInfo = new View(getContext());
		lineForCarInfo.setId(R.id.bidInfoView_lineForCarInfo);
		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, ResizeUtils.ONE, lineForCarInfo, 
				new int[]{RelativeLayout.ALIGN_PARENT_LEFT}, 
				new int[]{0}, 
				new int[]{12, 105, 12, 0});
		lineForCarInfo.setBackgroundColor(getResources().getColor(R.color.color_ltgray));
		this.addView(lineForCarInfo);
		
		tvCarInfo2 = new TextView(getContext());
		tvCarInfo2.setId(R.id.bidInfoView_tvCarInfo2);
		ResizeUtils.viewResizeForRelative(340, 63, tvCarInfo2, 
				new int[]{RelativeLayout.ALIGN_PARENT_LEFT, BELOW}, 
				new int[]{0, R.id.bidInfoView_lineForCarInfo}, 
				new int[]{20, 0, 0, 0});
		tvCarInfo2.setSingleLine();
		tvCarInfo2.setEllipsize(TruncateAt.END);
		tvCarInfo2.setTextColor(getResources().getColor(R.color.holo_text));
		tvCarInfo2.setGravity(Gravity.CENTER_VERTICAL);
		FontUtils.setFontSize(tvCarInfo2, 22);
		this.addView(tvCarInfo2);
		
		tvBidCount = new TextView(getContext());
		tvBidCount.setId(R.id.bidInfoView_tvBidCount);
		ResizeUtils.viewResizeForRelative(260, 63, tvBidCount, 
				new int[]{RelativeLayout.ALIGN_PARENT_RIGHT, BELOW}, 
				new int[]{0, R.id.bidInfoView_lineForCarInfo},
				new int[]{0, 0, 20, 0});
		tvBidCount.setSingleLine();
		tvBidCount.setEllipsize(TruncateAt.END);
		tvBidCount.setTextColor(getResources().getColor(R.color.holo_text));
		tvBidCount.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
		FontUtils.setFontSize(tvBidCount, 22);
		this.addView(tvBidCount);
		
		//Initialize.
		if(type == TYPE_MAIN_AUCTION) {
			tvRemainTime.setText("-- : -- : --");
			tvCarInfo1.setText("--");
			tvCarInfo2.setText("-- / -- / --");
			tvBidCount.setText("참여딜러 --명 / 입찰자 --명");
			priceTextView.setPrice(0);
		}
	}

	public void clearView() {
		
		try {
			progressBar.setProgress(0);
			tvCarInfo1.setText(null);
			tvCarInfo2.setText(null);
			priceTextView.setPrice(0);
			tvBidCount.setText(null);
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void clearTime() {
		
		try {
			progressBar.setProgress(1000);
			tvRemainTime.setText("  -- : -- : --");
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void setTime(Car car) {
		
		try {
			
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
//	    	long progressValue = 1000 - (remainTime * 1000 / progressTime);
	    	long progressValue = remainTime * 1000 / progressTime;
	    	
	    	String formattedRemainTime = StringUtils.getTimeString(remainTime);
	    	tvRemainTime.setText(formattedRemainTime);
	    	progressBar.setProgress((int)progressValue);
		} catch (Exception e) {
			LogUtils.trace(e);
			tvRemainTime.setText("-- : -- : --");
		}
	}
	
	public void statusChanged(Car car) {
		
		try {
			if(car.getStatus() < Car.STATUS_BID_COMPLETE) {
				progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_custom_orange));
			} else if(car.getStatus() == Car.STATUS_BID_COMPLETE) {
				progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_custom_green));
			} else {
				progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_custom_gray));
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void setCarInfo(Car car) {
		
		try {
			if(car.getStatus() < Car.STATUS_BID_COMPLETE) {
				progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_custom_orange));
			} else if(car.getStatus() == Car.STATUS_BID_COMPLETE) {
				progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_custom_green));
			} else {
				progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.progressbar_custom_gray));
			}
			
			tvCarInfo1.setText(car.getCar_full_name());
			tvCarInfo2.setText(car.getYear() + "년 / "
					+ StringUtils.getFormattedNumber(car.getMileage()) + "km / "
					+ car.getArea());
			priceTextView.setPrice(car.getPrice());
			
			if(type == TYPE_MAIN_AUCTION) {
				tvBidCount.setText("참여딜러 " + car.getBidders_cnt()
						+ "명 / 입찰자 " + car.getBids_cnt() + "명");
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
		
		if(type == TYPE_MAIN_AUCTION) {
			priceTextView.setType(PriceTextView.TYPE_DETAIL_AUCTION);
		}
	}
}