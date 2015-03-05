package com.byecar.wrappers;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.byecar.byecarplusfordealer.MainActivity;
import com.byecar.byecarplusfordealer.R;
import com.byecar.classes.BCPConstants;
import com.byecar.models.Car;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class ViewWrapperForMyCar extends ViewWrapper {
	
	private Car car;
	
	private ImageView ivImage;
	private View cover;
	private TextView tvStatus;
	private TextView tvRegdate;
	private TextView tvCarName;
	private TextView tvYear;
	private TextView tvInfo;
	private TextView tvBidding;
	
	public ViewWrapperForMyCar(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			ivImage = (ImageView) row.findViewById(R.id.list_my_car_ivImage);
			cover = row.findViewById(R.id.list_my_car_cover);
			tvStatus = (TextView) row.findViewById(R.id.list_my_car_tvStatus);
			tvRegdate = (TextView) row.findViewById(R.id.list_my_car_tvRegdate);
			tvCarName = (TextView) row.findViewById(R.id.list_my_car_tvCarName);
			tvYear = (TextView) row.findViewById(R.id.list_my_car_tvYear);
			tvInfo = (TextView) row.findViewById(R.id.list_my_car_tvInfo);
			tvBidding = (TextView) row.findViewById(R.id.list_my_car_tvBidding);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			ResizeUtils.viewResizeForRelative(186, 132, ivImage, null, null, new int[]{31, 84, 0, 0});
			ResizeUtils.viewResizeForRelative(608, 232, cover, null, null, new int[]{0, 0, 0, 0});
			ResizeUtils.setMargin(tvRegdate, new int[]{0, 26, 18, 0});
			ResizeUtils.viewResizeForRelative(290, 64, tvCarName, null, null, new int[]{16, 4, 0, 0});
			ResizeUtils.setMargin(tvYear, new int[]{0, 20, 30, 0});
			ResizeUtils.viewResizeForRelative(LayoutParams.WRAP_CONTENT, 64, tvBidding, null, null, null);
			ResizeUtils.viewResizeForRelative(LayoutParams.WRAP_CONTENT, 64, tvInfo, null, null, null);
			
			tvStatus.setPadding(0, 0, ResizeUtils.getSpecificLength(16), 0);
			
			FontUtils.setFontSize(tvStatus, 18);
			FontUtils.setFontStyle(tvStatus, FontUtils.BOLD);
			FontUtils.setFontSize(tvRegdate, 16);
			FontUtils.setFontSize(tvCarName, 28);
			FontUtils.setFontStyle(tvCarName, FontUtils.BOLD);
			FontUtils.setFontSize(tvYear, 20);
			FontUtils.setFontSize(tvBidding, 20);
			FontUtils.setFontSize(tvInfo, 20);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel instanceof Car) {
				car = (Car) baseModel;
				
				tvStatus.setText(null);
				tvBidding.setText(null);
				tvInfo.setText(null);
				
				//바이카 옥션.
				if(car.getItemCode() == BCPConstants.ITEM_CAR_MY_AUCTION) {

					FontUtils.addSpan(tvInfo, R.string.auctionedPrice, 0, 1);
					tvBidding.setText("입찰자 " + car.getBids_cnt() + "명");
					
					//낙찰 성공.
					if(car.getDealer_id() == MainActivity.dealer.getId()) {
						tvStatus.setBackgroundResource(R.drawable.deal_complete_sale);
						ResizeUtils.viewResizeForRelative(92, 39, tvStatus, null, null, new int[]{0, 18, 0, 0});
					
					//낙찰 순위권.
					} else if(car.getMy_bid_ranking() != 0) {
						tvStatus.setBackgroundResource(R.drawable.deal_complete_sale2);
						ResizeUtils.viewResizeForRelative(133, 39, tvStatus, null, null, new int[]{0, 18, 0, 0});
						tvStatus.setText(car.getMy_bid_ranking() + "위");
						
					//낙찰 순위권 외.
					} else {
						tvStatus.setBackgroundResource(R.drawable.deal_complete_sale3);
						ResizeUtils.viewResizeForRelative(133, 39, tvStatus, null, null, new int[]{0, 18, 0, 0});
					}
						
				//중고마켓.
				} else {
					
					FontUtils.addSpan(tvInfo, R.string.salesPrice, 0, 1);
					
					if(car.getStatus() == -1) {
						tvStatus.setBackgroundResource(R.drawable.deal_complete_sale5);
					} else {
						tvStatus.setBackgroundResource(R.drawable.deal_complete_sale4);
						
					}
					
					ResizeUtils.viewResizeForRelative(92, 39, tvStatus, null, null, new int[]{0, 18, 0, 0});
				}
				
				tvRegdate.setText(StringUtils.getDateString(
						"등록일 yyyy년 MM월 dd일", car.getCreated_at() * 1000));
				tvCarName.setText(car.getCar_full_name());
				tvYear.setText(car.getYear() + row.getContext().getString(R.string.year));
				
				FontUtils.addSpan(tvInfo, " " + StringUtils.getFormattedNumber(car.getPrice()) + 
						"만원", 0, 1, true);
				
				setImage(ivImage, car.getRep_img_url());
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
}
