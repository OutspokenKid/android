package com.byecar.wrappers;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.byecar.byecarplusfordealer.R;
import com.byecar.models.Car;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.AppInfoUtils;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class ViewWrapperForMyCar extends ViewWrapper {
	
	private Car car;
	
	private ImageView ivImage;
	private View cover;
	private View statusIcon;
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
			statusIcon = row.findViewById(R.id.list_my_car_statusIcon);
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
			ResizeUtils.viewResizeForRelative(260, 64, tvInfo, null, null, new int[]{16, 0, 0, 0});
			
			FontUtils.setFontSize(tvRegdate, 16);
			FontUtils.setFontSize(tvCarName, 30);
			FontUtils.setFontStyle(tvCarName, FontUtils.BOLD);
			FontUtils.setFontSize(tvYear, 20);
			FontUtils.setFontSize(tvBidding, 20);
			FontUtils.setFontSize(tvInfo, 20);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel instanceof Car) {
				car = (Car) baseModel;

				switch(car.getStatus()) {
					
				case Car.STATUS_BIDDING:
				case Car.STATUS_BID_COMPLETE:
					statusIcon.setBackgroundResource(R.drawable.deal_complete_sale);
					ResizeUtils.viewResizeForRelative(92, 39, statusIcon, null, null, new int[]{0, 18, 0, 0});
					break;
					
				case Car.STATUS_TRADE_COMPLETE:
					statusIcon.setBackgroundResource(R.drawable.deal_complete_sale4);
					ResizeUtils.viewResizeForRelative(92, 39, statusIcon, null, null, new int[]{0, 18, 0, 0});
					break;
					
				default:
					if(AppInfoUtils.checkMinVersionLimit(16)) {
						statusIcon.setBackground(null);
					} else {
						statusIcon.setBackgroundDrawable(null);
					}
				}
				
				tvRegdate.setText(StringUtils.getDateString(
						"등록일 yyyy년 MM월 dd일", car.getCreated_at() * 1000));
				tvCarName.setText(car.getCar_full_name());
				tvYear.setText(car.getYear() + row.getContext().getString(R.string.year));
				
				tvInfo.setText(null);
				FontUtils.addSpan(tvInfo, R.string.auctionedPrice, 0, 1);
				FontUtils.addSpan(tvInfo, " " + StringUtils.getFormattedNumber(car.getPrice()) + 
						row.getContext().getString(R.string.won), 0, 1, true);
				
				tvBidding.setText("입찰자 " + "x" + "명");
				
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
