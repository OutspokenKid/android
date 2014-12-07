package com.byecar.byecarplus.wrappers;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.byecarplus.models.Car;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class ViewWrapperForAuction extends ViewWrapper {
	
	private Car car;
	
	private ImageView ivImage;
	private View auctionIcon;
	private View remainBg;
	private ProgressBar progressBar;
	private TextView tvRemainTime;
	private TextView tvRemainTimeText;
	private View timeIcon;
	private TextView tvCarInfo1;
	private TextView tvCarInfo2;
	private TextView tvCurrentPrice;
	private TextView tvCurrentPriceText;
	private TextView tvBidCount;
	
	public ViewWrapperForAuction(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			ivImage = (ImageView) row.findViewById(R.id.list_bid_ivImage);
			auctionIcon = row.findViewById(R.id.list_bid_auctionIcon);
			remainBg = row.findViewById(R.id.list_bid_remainBg);
			progressBar = (ProgressBar) row.findViewById(R.id.list_bid_progressBar);
			tvRemainTime = (TextView) row.findViewById(R.id.list_bid_tvRemainTime);
			tvRemainTimeText = (TextView) row.findViewById(R.id.list_bid_tvRemainTimeText);
			timeIcon = row.findViewById(R.id.list_bid_timeIcon);
			
			tvCarInfo1 = (TextView) row.findViewById(R.id.list_bid_tvCarInfo1);
			tvCarInfo2 = (TextView) row.findViewById(R.id.list_bid_tvCarInfo2);
			tvCurrentPrice = (TextView) row.findViewById(R.id.list_bid_tvCurrentPrice);
			tvCurrentPriceText = (TextView) row.findViewById(R.id.list_bid_tvCurrentPriceText);
			tvBidCount = (TextView) row.findViewById(R.id.list_bid_tvBidCount);
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
			rp.height = ResizeUtils.getSpecificLength(500);
			
			//auctionIcon.
			rp = (RelativeLayout.LayoutParams) auctionIcon.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(96);
			rp.height = ResizeUtils.getSpecificLength(96);
			rp.leftMargin = ResizeUtils.getSpecificLength(12);
			rp.bottomMargin = ResizeUtils.getSpecificLength(18);
			
			//remainBg.
			rp = (RelativeLayout.LayoutParams) remainBg.getLayoutParams();
			rp.height = ResizeUtils.getSpecificLength(147);
			rp.topMargin = -2;
			
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
			
			//tvCarInfo2.
			rp = (RelativeLayout.LayoutParams) tvCarInfo2.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(340);
			rp.height = ResizeUtils.getSpecificLength(57);
			rp.leftMargin = ResizeUtils.getSpecificLength(20);
			
			//tvCurrentPrice.
			rp = (RelativeLayout.LayoutParams) tvCurrentPrice.getLayoutParams();
			rp.topMargin = ResizeUtils.getSpecificLength(16);
			rp.rightMargin = ResizeUtils.getSpecificLength(20);
			
			//tvCurrentPriceText.
			rp = (RelativeLayout.LayoutParams) tvCurrentPriceText.getLayoutParams();
			rp.rightMargin = ResizeUtils.getSpecificLength(10);
			rp.bottomMargin = ResizeUtils.getSpecificLength(10);
			
			//tvBidCount.
			rp = (RelativeLayout.LayoutParams) tvBidCount.getLayoutParams();
			rp.topMargin = ResizeUtils.getSpecificLength(4);

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
//					ShopActivity.getInstance().showPage(CphConstants.PAGE_COMMON_PRODUCT, bundle);
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
//					ShopActivity.getInstance().showPage(CphConstants.PAGE_COMMON_REPLY, bundle);
//				}
//			});
//		}
	}
	
	@Override
	public void setUnusableView() {

	}
}
