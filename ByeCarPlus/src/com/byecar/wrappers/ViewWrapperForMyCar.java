package com.byecar.wrappers;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.byecar.byecarplus.MainActivity;
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
	private TextView tvInfo;
	private Button btnReview;
	private Button btnComplete;
	private Button btnSelectDealer;
	private Button btnLike;
	private TextView tvLikeText;
	
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
			tvInfo = (TextView) row.findViewById(R.id.list_my_car_tvInfo);
			btnReview = (Button) row.findViewById(R.id.list_my_car_btnReview);
			btnComplete = (Button) row.findViewById(R.id.list_my_car_btnComplete);
			btnSelectDealer = (Button) row.findViewById(R.id.list_my_car_btnSelectDealer);
			btnLike = (Button) row.findViewById(R.id.list_my_car_btnLike);
			tvLikeText = (TextView) row.findViewById(R.id.list_my_car_tvLikeText);
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
			ResizeUtils.viewResizeForRelative(174, 30, typeIcon, null, null, new int[]{18, 20, 0, 0});
			ResizeUtils.viewResizeForRelative(94, 41, statusIcon, null, null, new int[]{15, 15, 0, 0});
			ResizeUtils.setMargin(tvRegdate, new int[]{0, 26, 18, 0});
			ResizeUtils.viewResizeForRelative(290, 64, tvCarName, null, null, new int[]{16, 4, 0, 0});
			ResizeUtils.viewResizeForRelative(LayoutParams.WRAP_CONTENT, 64, tvInfo, null, null, new int[]{16, 0, 0, 0});
			ResizeUtils.viewResizeForRelative(157, 46, btnReview, null, null, new int[]{0, 0, 42, 10});
			ResizeUtils.viewResizeForRelative(157, 46, btnComplete, null, null, new int[]{0, 0, 42, 10});
			ResizeUtils.viewResizeForRelative(157, 46, btnSelectDealer, null, null, new int[]{0, 0, 42, 10});
			ResizeUtils.viewResizeForRelative(90, 40, btnLike, null, null, new int[]{0, 0, 42, 11}, new int[]{32, 0, 9, 2});
			ResizeUtils.viewResizeForRelative(LayoutParams.WRAP_CONTENT, 64, tvLikeText, null, null, new int[]{0, 0, 2, 0});
			
			FontUtils.setFontSize(tvRegdate, 16);
			FontUtils.setFontSize(tvCarName, 26);
			FontUtils.setFontStyle(tvCarName, FontUtils.BOLD);
			FontUtils.setFontSize(tvInfo, 20);
			FontUtils.setFontSize(btnLike, 18);
			FontUtils.setFontSize(tvLikeText, 20);
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
				
				//type(입찰진행중, 직거래, 중고차마켓)에 따른 아이콘 설정.
				switch(car.getType()) {
				
				case Car.TYPE_BID:
					typeIcon.setBackgroundResource(R.drawable.mypage_title1);
					break;
					
				case Car.TYPE_DEALER:
					typeIcon.setBackgroundResource(R.drawable.mypage_title3);
					break;
					
				case Car.TYPE_DIRECT:
					typeIcon.setBackgroundResource(R.drawable.mypage_title2);
					break;
					
				default:
					if(AppInfoUtils.checkMinVersionLimit(16)) {
						typeIcon.setBackground(null);
					} else {
						typeIcon.setBackgroundDrawable(null);
					}
				}

				if(car.getType() == Car.TYPE_BID) {
					//status에 따른 아이콘(검수중, 진행중, 거래완료)
					switch(car.getStatus()) {
					
					case Car.STATUS_STAND_BY_APPROVAL:
					case Car.STATUS_STAND_BY_BIDING:
						statusIcon.setBackgroundResource(R.drawable.mypage_sale3);
						break;
						
					case Car.STATUS_BIDDING:
					case Car.STATUS_BID_COMPLETE:
					case Car.STATUS_BID_SUCCESS:
					case Car.STATUS_BID_FAIL:
					case Car.STATUS_PAYMENT_COMPLETED:
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
				} else {
					statusIcon.setVisibility(View.INVISIBLE);
				}
				
				//나의 차량, 구매 신청내역에서는 등록일, 좋아요에서는 찜한 날, 직거래의 경우 마감일도 있음.
				if(car.getItemCode() == BCPConstants.ITEM_CAR_MY_LIKE) {
					//찜한날.
					tvRegdate.setText(StringUtils.getDateString(
							"찜한 날 yyyy년 MM월 dd일", car.getCreated_at() * 1000));
				} else {
					//직거래는 등록일과 마감일, 나의 차량과 구매 신청내역에서는 등록일.
					if(car.getType() == Car.TYPE_DIRECT) {
						tvRegdate.setText(StringUtils.getDateString(
								"등록일 yyyy년 MM월 dd일", car.getCreated_at() * 1000)
								+ StringUtils.getDateString(
								"\n마감일 yyyy년 MM월 dd일", car.getBid_until_at() * 1000));
					} else {
						tvRegdate.setText(StringUtils.getDateString(
								"등록일 yyyy년 MM월 dd일", car.getCreated_at() * 1000));
					}
				}
				
				//이미지.
				setImage(ivImage, car.getRep_img_url());
				
				//차 이름.
				tvCarName.setText(car.getCar_full_name());
				
				//나의 차량일 때 완료하기, 후기작성, 딜러선택하기 버튼 노출.
				boolean showButton = false;
				btnReview.setVisibility(View.INVISIBLE);
				btnComplete.setVisibility(View.INVISIBLE);
				btnSelectDealer.setVisibility(View.INVISIBLE);
				
				if(car.getItemCode() == BCPConstants.ITEM_CAR_MY
						&& car.getType() == Car.TYPE_BID) {
					
					switch(car.getStatus()) {
					
					case Car.STATUS_BID_COMPLETE:
						btnSelectDealer.setVisibility(View.VISIBLE);
						showButton = true;
						break;
					
					case Car.STATUS_PAYMENT_COMPLETED:
						btnComplete.setVisibility(View.VISIBLE);
						showButton = true;
						break;
						
					case Car.STATUS_TRADE_COMPLETE:
						if(car.getHas_review() != 1) {
							btnReview.setVisibility(View.VISIBLE);
							showButton = true;
						}
						break;
					}
				}
				
				tvInfo.setText(null);
				
				//입찰진행중 상태일 때는 "참여딜러 x명, xx만원”, 그 외에는 “판매가 xx만원”
				if(car.getType() == Car.TYPE_BID) {
					if(showButton) {
						FontUtils.addSpan(tvInfo, "참여딜러 " + car.getBids_cnt() + "명" + "\n", 0, 1);
						FontUtils.addSpan(tvInfo, StringUtils.getFormattedNumber(car.getPrice()/10000) + "만원", 
								row.getContext().getResources().getColor(R.color.new_color_text_darkgray), 1);
					} else {
						FontUtils.addSpan(tvInfo, "참여딜러 " + car.getBids_cnt() + "명" + "  ", 0, 1);
						FontUtils.addSpan(tvInfo, StringUtils.getFormattedNumber(car.getPrice()/10000) + "만원", 
								row.getContext().getResources().getColor(R.color.new_color_text_darkgray), 1);
					}
				} else {
					FontUtils.addSpan(tvInfo, "판매가 ", 0, 1);
					FontUtils.addSpan(tvInfo, StringUtils.getFormattedNumber(car.getPrice()/10000) + "만원", 
							row.getContext().getResources().getColor(R.color.new_color_text_darkgray), 1);
				}
				
				//좋아요 버튼.
				if(car.getItemCode() == BCPConstants.ITEM_CAR_MY_LIKE) {
					btnLike.setVisibility(View.VISIBLE);
					tvLikeText.setVisibility(View.VISIBLE);
					btnLike.setText("" + Math.min(9999, car.getLikes_cnt()));
				} else {
					btnLike.setVisibility(View.INVISIBLE);
					tvLikeText.setVisibility(View.INVISIBLE);
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

			btnSelectDealer.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					((MainActivity)activity).showCarDetailPage(0, car, car.getType());
				}
			});
			
			btnReview.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					
					Bundle bundle = new Bundle();
					bundle.putSerializable("car", car);
					bundle.putInt("dealer_id", car.getDealer_id());
					bundle.putInt("onsalecar_id", car.getId());
					activity.showPage(BCPConstants.PAGE_WRITE_REVIEW, bundle);
				}
			});
			
			btnComplete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					activity.showAlertDialog(R.string.complete_deal, R.string.wannaCompleteDealing,
							R.string.confirm, R.string.cancel, 
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
				+ "&status=" + Car.STATUS_TRADE_COMPLETE;
		
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
								activity.getTopFragment().refreshPage();
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
