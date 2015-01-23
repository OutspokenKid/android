package com.byecar.byecarplusfordealer.wrappers;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.byecar.byecarplusfordealer.R;
import com.byecar.byecarplusfordealer.models.Car;
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
	private View typeIcon;
	private View statusIcon;
	private TextView tvRegdate;
	private TextView tvCarName;
	private TextView tvYear;
	private TextView tvInfo;
	
	public ViewWrapperForMyCar(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			ivImage = (ImageView) row.findViewById(R.id.list_my_car_ivImage);
			cover = row.findViewById(R.id.list_my_car_cover);
			typeIcon = row.findViewById(R.id.list_my_car_typeIcon);
			statusIcon = row.findViewById(R.id.list_my_car_statusIcon);
			tvRegdate = (TextView) row.findViewById(R.id.list_my_car_tvRegdate);
			tvCarName = (TextView) row.findViewById(R.id.list_my_car_tvCarName);
			tvYear = (TextView) row.findViewById(R.id.list_my_car_tvYear);
			tvInfo = (TextView) row.findViewById(R.id.list_my_car_tvInfo);
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
			ResizeUtils.viewResizeForRelative(135, 30, typeIcon, null, null, new int[]{18, 20, 0, 0});
			ResizeUtils.viewResizeForRelative(92, 39, statusIcon, null, null, new int[]{15, 15, 0, 0});
			ResizeUtils.setMargin(tvRegdate, new int[]{0, 26, 18, 0});
			ResizeUtils.viewResizeForRelative(290, 64, tvCarName, null, null, new int[]{16, 4, 0, 0});
			ResizeUtils.setMargin(tvYear, new int[]{0, 20, 30, 0});
			ResizeUtils.viewResizeForRelative(260, 64, tvInfo, null, null, new int[]{16, 0, 0, 0});
			
			FontUtils.setFontSize(tvRegdate, 16);
			FontUtils.setFontSize(tvCarName, 30);
			FontUtils.setFontStyle(tvCarName, FontUtils.BOLD);
			FontUtils.setFontSize(tvYear, 20);
			FontUtils.setFontSize(tvInfo, 20);
			FontUtils.setFontStyle(tvInfo, FontUtils.BOLD);
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

				switch(car.getType()) {
				
				case Car.TYPE_BID:
					typeIcon.setBackgroundResource(R.drawable.mypage_title1);
					break;
					
				case Car.TYPE_DEALER:
					typeIcon.setBackgroundResource(R.drawable.mypage_title2);
					break;
					
				case Car.TYPE_DIRECT_CERTIFIED:
					typeIcon.setBackgroundResource(R.drawable.mypage_title3);
					break;
					
				case Car.TYPE_DIRECT_NORMAL:
					typeIcon.setBackgroundResource(R.drawable.mypage_title4);
					break;
					
				default:
					if(AppInfoUtils.checkMinVersionLimit(16)) {
						typeIcon.setBackground(null);
					} else {
						typeIcon.setBackgroundDrawable(null);
					}
				}

				switch(car.getStatus()) {
				
				case Car.STATUS_STAND_BY_APPROVAL:
				case Car.STATUS_STAND_BY_BID:
					statusIcon.setBackgroundResource(R.drawable.mypage_sale3);
					break;
					
				case Car.STATUS_BIDDING:
				case Car.STATUS_BID_COMPLETE:
					statusIcon.setBackgroundResource(R.drawable.mypage_sale);
					break;
					
				case Car.STATUS_TRADE_COMPLETE:
					statusIcon.setBackgroundResource(R.drawable.mypage_sale2);
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
				tvInfo.setText("차량 정보  " + StringUtils.getFormattedNumber(car.getPrice()) + row.getContext().getString(R.string.won));
				
				setImage(ivImage, car.getRep_img_url());
			}
		} catch (Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setListeners() {
//	
//		if(product != null) {
//			row.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View arg0) {
//
//					Bundle bundle = new Bundle();
//					bundle.putBoolean("isWholesale", product.getType() == Product.TYPE_WHOLESALE);
//					bundle.putSerializable("product", product);
//					ShopActivity.getInstance().showPage(CphConstants.PAGE_PRODUCT, bundle);
//				}
//			});
//			
//			if(product.isDeletable()) {
//				
//				row.setOnLongClickListener(new OnLongClickListener() {
//					
//					@Override
//					public boolean onLongClick(View arg0) {
//						
//						ShopActivity.getInstance().showAlertDialog("삭제", "해당 물품을 삭제하시겠습니까?",
//								"확인", "취소", 
//								new DialogInterface.OnClickListener() {
//									
//									@Override
//									public void onClick(DialogInterface dialog, int which) {
//										
//										deleteFavorite(product);
//									}
//								}, null);
//						return false;
//					}
//				});
//			}
//			
//			replyIcon.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View view) {
//
//					Bundle bundle = new Bundle();
//					bundle.putSerializable("product", product);
//					ShopActivity.getInstance().showPage(CphConstants.PAGE_REPLY, bundle);
//				}
//			});
//		}
	}
	
	@Override
	public void setUnusableView() {

	}
}
