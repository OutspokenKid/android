package com.byecar.byecarplus.wrappers;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.byecarplus.classes.BCPAPIs;
import com.byecar.byecarplus.classes.BCPConstants;
import com.byecar.byecarplus.models.Car;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class ViewWrapperForCar extends ViewWrapper {
	
	private Car car;
	
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
	private TextView tvCurrentPrice;
	private TextView tvCurrentPriceText;
	private TextView tvBidCount;
	private Button btnLike;
	
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
				
				tvRemainTime.setText("15 : 40 : 21");
				
				tvCarInfo1.setText(car.getCar_full_name());
				tvCarInfo2.setText(car.getYear() + "년 / "
						+ StringUtils.getFormattedNumber(car.getMileage()) + "km / "
						+ car.getArea());
				
				tvCurrentPrice.setText(StringUtils.getFormattedNumber(car.getPrice()) 
						+ row.getContext().getString(R.string.won));
				tvBidCount.setText("입찰중 " + car.getBids_cnt() + "명");
				
				if(car.getItemCode() == BCPConstants.ITEM_CAR_BID) {
					auctionIcon.setVisibility(View.VISIBLE);
					timeRelative.setVisibility(View.VISIBLE);
					tvBidCount.setVisibility(View.VISIBLE);
					
					if(car.getStatus() == 10) {
						auctionIcon.setBackgroundResource(R.drawable.main_hotdeal_mark);
					} else if(car.getStatus() == 20) {
						auctionIcon.setBackgroundResource(R.drawable.main_hotdeal_mark2);
					} else {
						auctionIcon.setBackgroundResource(R.drawable.main_hotdeal_mark3);
					}
					
					((RelativeLayout.LayoutParams) btnLike.getLayoutParams()).rightMargin = ResizeUtils.getSpecificLength(14);
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
			url = BCPAPIs.LIKE_URL;
		} else {
			btnLike.setBackgroundResource(R.drawable.main_like_btn_a);
			car.setLikes_cnt(car.getLikes_cnt() - 1);
			car.setIs_liked(0);
			url = BCPAPIs.UNLIKE_URL;
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
}
