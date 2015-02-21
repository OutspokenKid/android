package com.byecar.wrappers;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPFragmentActivity;
import com.byecar.models.Car;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.AppInfoUtils;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class ViewWrapperForMyCar extends ViewWrapper {

	BCPFragmentActivity activity;
	private Car car;
	
	private ImageView ivImage;
	private View cover;
	private View typeIcon;
	private View statusIcon;
	private TextView tvRegdate;
	private TextView tvCarName;
	private TextView tvYear;
	private TextView tvInfo;
	private Button btnReview;
	private Button btnComplete;
	
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
			btnReview = (Button) row.findViewById(R.id.list_my_car_btnReview);
			btnComplete = (Button) row.findViewById(R.id.list_my_car_btnComplete);
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
			ResizeUtils.viewResizeForRelative(70, 64, tvYear, null, null, new int[]{0, 0, 30, 0});
			ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, 64, tvInfo, null, null, new int[]{16, 0, 0, 0});
			ResizeUtils.viewResizeForRelative(88, 34, btnReview, null, null, new int[]{0, 0, 0, 13});
			ResizeUtils.viewResizeForRelative(88, 34, btnComplete, null, null, new int[]{0, 0, 0, 13});
			
			FontUtils.setFontSize(tvRegdate, 16);
			FontUtils.setFontSize(tvCarName, 26);
			FontUtils.setFontStyle(tvCarName, FontUtils.BOLD);
			FontUtils.setFontSize(tvYear, 20);
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
				
				//타입.
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
				
				//등록일.
				tvRegdate.setText(StringUtils.getDateString(
						"등록일 yyyy년 MM월 dd일", car.getCreated_at() * 1000));
				
				//이미지.
				setImage(ivImage, car.getRep_img_url());
				
				//차 이름.
				tvCarName.setText(car.getCar_full_name());
				
				//연식.
				tvYear.setText(car.getYear() + row.getContext().getString(R.string.year));

				//판매 상태.
				switch(car.getStatus()) {
				
				//0: 승인대기, 5 : 입찰대기, 10: 입찰중, 15: 입찰종료, 20: 낙찰, 21: 유찰, 30: 거래완료
//				public static final int STATUS_STAND_BY_APPROVAL = 0;
//				public static final int STATUS_STAND_BY_BIDING = 5;
				
//				public static final int STATUS_BIDDING = 10;
//				public static final int STATUS_BID_COMPLETE = 15;
				
//				public static final int STATUS_BID_SUCCESS = 20;
//				public static final int STATUS_BID_FAIL = 21;
//				public static final int STATUS_TRADE_COMPLETE = 30;
				
				case Car.STATUS_STAND_BY_APPROVAL:
				case Car.STATUS_STAND_BY_BIDING:
					statusIcon.setBackgroundResource(R.drawable.mypage_sale3);
					break;
					
				case Car.STATUS_BIDDING:
				case Car.STATUS_BID_COMPLETE:
					statusIcon.setBackgroundResource(R.drawable.mypage_sale);
					break;
					
				case Car.STATUS_BID_SUCCESS:
				case Car.STATUS_BID_FAIL:
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

				//itemCode 확인.
				//내 매물인 경우.
				if(car.getItemCode() == BCPConstants.ITEM_CAR_MY) {
					//가격.
					tvInfo.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
					
					if(car.getType() == Car.TYPE_BID) {
						tvInfo.setText("입찰자 " + car.getBids_cnt() + "명" + "   " 
								+ StringUtils.getFormattedNumber(car.getPrice()) + row.getContext().getString(R.string.won));
					} else {
						tvInfo.setText("판매가 " + StringUtils.getFormattedNumber(car.getPrice()) + row.getContext().getString(R.string.won));
					}
					
					//리뷰 버튼.
					//대기 중인 경우.
					if(car.getStatus() < Car.STATUS_BIDDING) {
						btnReview.setVisibility(View.INVISIBLE);
						btnComplete.setVisibility(View.INVISIBLE);
					
					//종료된 경우.
					} else if(car.getStatus() > Car.STATUS_BID_COMPLETE) {
						btnComplete.setVisibility(View.INVISIBLE);
						
						//옥션, 검증직거래 매물이고, 후기를 작성하지 않은 경우.
						if(car.getType() != Car.TYPE_DIRECT_NORMAL && car.getHas_review() != 1) {
							btnReview.setVisibility(View.VISIBLE);
							
						//중고마켓 매물이거나, 후기를 작성한 경우.
						} else {
							btnReview.setVisibility(View.INVISIBLE);
						}
						
					//판매 중인 경우.
					} else {
						btnReview.setVisibility(View.INVISIBLE);
						btnComplete.setVisibility(View.VISIBLE);
					}
					
				//구매 신청내역인 경우.
				} else {
					//가격.
					tvInfo.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
					tvInfo.setText(null);
					FontUtils.addSpan(tvInfo, "판매금액 ", 0, 1, false);
					FontUtils.addSpan(tvInfo, StringUtils.getFormattedNumber(car.getPrice()) 
							+ row.getContext().getString(R.string.won), 0, 1, true);
					
					//리뷰 버튼.
					btnReview.setVisibility(View.GONE);
					btnComplete.setVisibility(View.GONE);
				}
			}
		} catch (Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setListeners() {
		
		if(car != null) {
			
			btnReview.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					
					Bundle bundle = new Bundle();
					bundle.putSerializable("car", car);
					bundle.putInt("manager_id", car.getManager_id());
					bundle.putInt("dealer_id", car.getDealer_id());
					bundle.putInt("onsalecar_id", car.getId());
					activity.showPage(BCPConstants.PAGE_WRITE_REVIEW, bundle);
				}
			});
			
			btnComplete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					activity.showAlertDialog("거래완료", "거래완료 처리하시겠습니까?",
							"확인", "취소", 
							new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									
									completeSelling();
								}
							}, null);
				}
			});
		}
	}
	
	@Override
	public void setUnusableView() {

	}

	public void setActivity(BCPFragmentActivity activity) {
		
		this.activity = activity;
	}
	
	public void completeSelling() {
		
		String url = BCPAPIs.CAR_BID_COMPLETE_URL 
				+ "?onsalecar_id=" + car.getId()
				+ "&status=30";
		
		DownloadUtils.downloadJSONString(url,
				new OnJSONDownloadListener() {

					@Override
					public void onError(String url) {

						LogUtils.log("ViewWrapperForMyCar.onError." + "\nurl : "
								+ url);
					}

					@Override
					public void onCompleted(String url,
							JSONObject objJSON) {

						try {
							LogUtils.log("ViewWrapperForMyCar.onCompleted."
									+ "\nurl : " + url
									+ "\nresult : " + objJSON);

							if(objJSON.getInt("result") == 1) {
								ToastUtils.showToast(R.string.complete_selling);
							} else {
								ToastUtils.showToast(objJSON.getString("message"));
							}
						} catch (Exception e) {
							LogUtils.trace(e);
						} catch (OutOfMemoryError oom) {
							LogUtils.trace(oom);
						}
					}
				});
	}
}
