package com.byecar.views;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplusfordealer.MainActivity;
import com.byecar.byecarplusfordealer.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPDownloadUtils;
import com.byecar.classes.BCPFragmentActivity;
import com.byecar.models.Car;
import com.byecar.models.Dealer;
import com.outspoken_kid.utils.AppInfoUtils;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.TimerUtils;
import com.outspoken_kid.utils.TimerUtils.OnTimeChangedListener;
import com.outspoken_kid.utils.ToastUtils;

public class CarView extends RelativeLayout {

	private Car car;
	
	private ImageView ivImage;
	private ImageView ivProfile;
	private View cover;
	private TextView tvDealerName;
	private View rankBadge;
	private TextView tvCarName;
	private TextView tvBiddingInfo;
	private TextView tvLikeText;
	private Button btnLike;
	private TextView tvInfo;
	private View[] infoBadges = new View[4];
	private PriceTextView priceTextView1;
	private PriceTextView priceTextView2;
	private Button btnComplete; 
	private TextView tvRemainTime;
	
	private LinearLayout buttonLinear;
	private View immediatlyBadge;
	private View statusBadge;
	private View rankBadge2;
	private View completeBadge;
	
	private BCPFragmentActivity activity;
	private OnTimeChangedListener onTimeChangedListener;
	
	public CarView(Context context) {
		this(context, null, 0);
	}
	
	public CarView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public CarView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}
	
	public void init() {
	
		RelativeLayout.LayoutParams rp = null;
		
		//ivImage.
		ivImage = new ImageView(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(242), LayoutParams.MATCH_PARENT);
		ivImage.setLayoutParams(rp);
		ivImage.setId(R.id.carView_ivImage);
		ivImage.setScaleType(ScaleType.CENTER_CROP);
		ivImage.setBackgroundResource(R.drawable.main_used_car_sub_frame_default);
		this.addView(ivImage);
		
		//ivProfile.
		ivProfile = new ImageView(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(43), 
				ResizeUtils.getSpecificLength(43));
		rp.addRule(ALIGN_PARENT_BOTTOM);
		rp.leftMargin = ResizeUtils.getSpecificLength(9);
		rp.bottomMargin = ResizeUtils.getSpecificLength(7);
		ivProfile.setLayoutParams(rp);
		ivProfile.setId(R.id.carView_ivProfile);
		ivProfile.setScaleType(ScaleType.CENTER_CROP);
		ivProfile.setBackgroundResource(R.drawable.detail_default);
		this.addView(ivProfile);
		
		//cover.
		cover = new View(getContext());
		rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		cover.setLayoutParams(rp);
		this.addView(cover);
		
		//tvDealerName.
		tvDealerName = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(70), ResizeUtils.getSpecificLength(56));
		rp.addRule(ALIGN_PARENT_BOTTOM);
		rp.addRule(RIGHT_OF, R.id.carView_ivProfile);
		rp.leftMargin = ResizeUtils.getSpecificLength(10);
		tvDealerName.setLayoutParams(rp);
		tvDealerName.setId(R.id.carView_tvDealerName);
		tvDealerName.setTextColor(Color.WHITE);
		tvDealerName.setSingleLine();
		tvDealerName.setEllipsize(TruncateAt.END);
		FontUtils.setFontSize(tvDealerName, 20);
		tvDealerName.setGravity(Gravity.CENTER_VERTICAL);
		this.addView(tvDealerName);
		
		//rankBadge.
		rankBadge = new View(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(96), ResizeUtils.getSpecificLength(25));
		rp.addRule(ALIGN_PARENT_BOTTOM);
		rp.addRule(RIGHT_OF, R.id.carView_tvDealerName);
		rp.bottomMargin = ResizeUtils.getSpecificLength(15);
		rankBadge.setLayoutParams(rp);
		this.addView(rankBadge);
		
		//tvCarName.
		tvCarName = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(160), ResizeUtils.getSpecificLength(60));
		rp.addRule(ALIGN_PARENT_TOP);
		rp.addRule(RIGHT_OF, R.id.carView_ivImage);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		tvCarName.setLayoutParams(rp);
		tvCarName.setTextColor(getResources().getColor(R.color.new_color_text_brown));
		tvCarName.setSingleLine();
		tvCarName.setEllipsize(TruncateAt.END);
		FontUtils.setFontSize(tvCarName, 30);
		FontUtils.setFontStyle(tvCarName, FontUtils.BOLD);
		tvCarName.setGravity(Gravity.CENTER_VERTICAL);
		this.addView(tvCarName);
		
		//tvBiddingInfo.
		tvBiddingInfo = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(152), ResizeUtils.getSpecificLength(48));
		rp.addRule(ALIGN_PARENT_TOP);
		rp.addRule(ALIGN_PARENT_RIGHT);
		rp.topMargin = ResizeUtils.getSpecificLength(9);
		rp.rightMargin = ResizeUtils.getSpecificLength(16);
		tvBiddingInfo.setLayoutParams(rp);
		tvBiddingInfo.setTextColor(getResources().getColor(R.color.new_color_text_darkgray));
		tvBiddingInfo.setSingleLine();
		tvBiddingInfo.setEllipsize(TruncateAt.END);
		FontUtils.setFontSize(tvBiddingInfo, 16);
		tvBiddingInfo.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
		this.addView(tvBiddingInfo);
		
		//btnLike.
		btnLike = new Button(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(90), ResizeUtils.getSpecificLength(40));
		rp.addRule(ALIGN_PARENT_RIGHT);
		rp.topMargin = ResizeUtils.getSpecificLength(10);
		rp.rightMargin = ResizeUtils.getSpecificLength(9);
		btnLike.setLayoutParams(rp);
		btnLike.setPadding(ResizeUtils.getSpecificLength(32), 0, 
				ResizeUtils.getSpecificLength(10), ResizeUtils.getSpecificLength(2));
		btnLike.setId(R.id.carView_btnLike);
		btnLike.setBackgroundResource(R.drawable.main_like_btn_a);
		btnLike.setTextColor(Color.WHITE);
		FontUtils.setFontSize(btnLike, 18);
		btnLike.setGravity(Gravity.CENTER);
		this.addView(btnLike);
		
		//tvLikeText.
		tvLikeText = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, ResizeUtils.getSpecificLength(40));
		rp.addRule(ALIGN_TOP, R.id.carView_btnLike);
		rp.addRule(LEFT_OF, R.id.carView_btnLike);
		tvLikeText.setLayoutParams(rp);
		tvLikeText.setText(R.string.like);
		tvLikeText.setTextColor(getResources().getColor(R.color.holo_text));
		FontUtils.setFontSize(tvLikeText, 18);
		tvLikeText.setGravity(Gravity.CENTER_VERTICAL);
		this.addView(tvLikeText);
		
		//tvInfo.
		tvInfo = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(307), ResizeUtils.getSpecificLength(35));
		rp.addRule(ALIGN_PARENT_TOP);
		rp.addRule(ALIGN_PARENT_RIGHT);
		rp.topMargin = ResizeUtils.getSpecificLength(58);
		rp.rightMargin = ResizeUtils.getSpecificLength(15);
		tvInfo.setLayoutParams(rp);
		tvInfo.setTextColor(getResources().getColor(R.color.holo_text));
		tvInfo.setSingleLine();
		tvInfo.setEllipsize(TruncateAt.END);
		FontUtils.setFontSize(tvInfo, 18);
		tvInfo.setGravity(Gravity.CENTER);
		tvInfo.setPadding(ResizeUtils.getSpecificLength(4), 0, ResizeUtils.getSpecificLength(4), 0);
		this.addView(tvInfo);
		
		//infoBadges.
		infoBadges[0] = new View(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(62), ResizeUtils.getSpecificLength(23));
		rp.addRule(ALIGN_PARENT_BOTTOM);
		rp.addRule(RIGHT_OF, R.id.carView_ivImage);
		rp.leftMargin = ResizeUtils.getSpecificLength(17);
		rp.bottomMargin = ResizeUtils.getSpecificLength(41);
		infoBadges[0].setLayoutParams(rp);
		infoBadges[0].setId(R.id.carView_infoBadg1);
		infoBadges[0].setBackgroundResource(R.drawable.main_used_option1_a);
		this.addView(infoBadges[0]);
		
		infoBadges[1] = new View(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(62), ResizeUtils.getSpecificLength(23));
		rp.addRule(ALIGN_TOP, R.id.carView_infoBadg1);
		rp.addRule(RIGHT_OF, R.id.carView_infoBadg1);
		rp.leftMargin = ResizeUtils.getSpecificLength(7);
		infoBadges[1].setLayoutParams(rp);
		infoBadges[1].setBackgroundResource(R.drawable.main_used_option2_a);
		this.addView(infoBadges[1]);

		infoBadges[2] = new View(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(62), ResizeUtils.getSpecificLength(23));
		rp.addRule(ALIGN_LEFT, R.id.carView_infoBadg1);
		rp.addRule(ALIGN_TOP, R.id.carView_infoBadg1);
		rp.topMargin = ResizeUtils.getSpecificLength(30);
		infoBadges[2].setLayoutParams(rp);
		infoBadges[2].setBackgroundResource(R.drawable.main_used_option3_a);
		this.addView(infoBadges[2]);
		
		infoBadges[3] = new View(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(62), ResizeUtils.getSpecificLength(23));
		rp.addRule(RIGHT_OF, R.id.carView_infoBadg1);
		rp.addRule(ALIGN_TOP, R.id.carView_infoBadg1);
		rp.leftMargin = ResizeUtils.getSpecificLength(7);
		rp.topMargin = ResizeUtils.getSpecificLength(30);
		infoBadges[3].setLayoutParams(rp);
		infoBadges[3].setBackgroundResource(R.drawable.main_used_option4_b);
		this.addView(infoBadges[3]);

		LinearLayout priceLinear = new LinearLayout(getContext());
		priceLinear.setOrientation(LinearLayout.VERTICAL);
		ResizeUtils.viewResizeForRelative(172, 74, priceLinear, 
				new int[]{RelativeLayout.ALIGN_PARENT_RIGHT, ALIGN_PARENT_BOTTOM}, 
				new int[]{0, 0}, 
				new int[]{0, 0, 16, 0});
		this.addView(priceLinear);

		//topBlank.
		View topBlank = new View(getContext());
		topBlank.setLayoutParams(new LinearLayout.LayoutParams(10, 0, 1));
		priceLinear.addView(topBlank);
		
		//priceTextView1.
		priceTextView1 = new PriceTextView(getContext());
		ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, 34, priceTextView1, 1, Gravity.RIGHT, null);
		priceLinear.addView(priceTextView1);
		
		//priceTextView2.
		priceTextView2 = new PriceTextView(getContext());
		ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, 34, priceTextView2, 1, Gravity.RIGHT, null);
		priceTextView2.setVisibility(View.GONE);
		priceLinear.addView(priceTextView2);
		
		//bottomBlank.
		View bottomBlank = new View(getContext());
		bottomBlank.setLayoutParams(new LinearLayout.LayoutParams(10, 0, 1));
		priceLinear.addView(bottomBlank);
		
		//btnComplete.
		btnComplete = new Button(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(182), ResizeUtils.getSpecificLength(47));
		rp.addRule(ALIGN_PARENT_LEFT);
		rp.addRule(ALIGN_PARENT_TOP);
		rp.leftMargin = ResizeUtils.getSpecificLength(30);
		rp.topMargin = ResizeUtils.getSpecificLength(40);
		btnComplete.setLayoutParams(rp);
		btnComplete.setBackgroundResource(R.drawable.mycar_list_complete_btn);
		this.addView(btnComplete);
		
		//tvRemainTime.
		tvRemainTime = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(140), ResizeUtils.getSpecificLength(42));
		rp.addRule(ALIGN_PARENT_LEFT);
		rp.addRule(ALIGN_PARENT_BOTTOM);
		rp.leftMargin = ResizeUtils.getSpecificLength(62);
		rp.bottomMargin = ResizeUtils.getSpecificLength(11);
		tvRemainTime.setLayoutParams(rp);
		tvRemainTime.setTextColor(Color.WHITE);
		FontUtils.setFontSize(tvRemainTime, 26);
		FontUtils.setFontStyle(tvRemainTime, FontUtils.BOLD);
		tvRemainTime.setGravity(Gravity.CENTER);
		tvRemainTime.setSingleLine();
		tvRemainTime.setEllipsize(TruncateAt.END);
		tvRemainTime.setVisibility(View.INVISIBLE);
		this.addView(tvRemainTime);
		
		//buttonLinear.
		buttonLinear = new LinearLayout(getContext());
		rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		rp.leftMargin = ResizeUtils.getSpecificLength(7);
		rp.topMargin = ResizeUtils.getSpecificLength(7);
		buttonLinear.setLayoutParams(rp);
		buttonLinear.setOrientation(LinearLayout.VERTICAL);
		this.addView(buttonLinear);
		
		//immediatlyBadge.
		immediatlyBadge = new View(getContext());
		ResizeUtils.viewResize(90, 27, immediatlyBadge, 1, 0, new int[]{0, 0, 0, 7});
		immediatlyBadge.setBackgroundResource(R.drawable.auction_sale2_icon1);
		immediatlyBadge.setVisibility(View.GONE);
		buttonLinear.addView(immediatlyBadge);
		
		//statusBadge.
		statusBadge = new View(getContext());
		ResizeUtils.viewResize(90, 27, statusBadge, 1, 0, new int[]{0, 0, 0, 7});
		statusBadge.setVisibility(View.GONE);
		buttonLinear.addView(statusBadge);
		
		//rankBadge2.
		rankBadge2 = new View(getContext());
		ResizeUtils.viewResize(124, 31, rankBadge2, 1, 0, new int[]{0, 0, 0, 7});
		rankBadge2.setVisibility(View.GONE);
		buttonLinear.addView(rankBadge2);
		
		//rankBadge2.
		completeBadge = new View(getContext());
		ResizeUtils.viewResize(91, 31, completeBadge, 1, 0, new int[]{0, 0, 0, 7});
		completeBadge.setVisibility(View.GONE);
		buttonLinear.addView(completeBadge);
		
		clearView();
	}
	
	public void downloadImage(String imageUrl, final ImageView ivImage) {
		
		if(ivImage == null) {
			return;
		} else if(imageUrl == null || imageUrl.length() == 0) {
			ivImage.setImageDrawable(null);
			ivImage.setTag(null);
			return;
		} else if(ivImage.getTag() != null && imageUrl.equals(ivImage.getTag().toString())) {
			//Do nothing because of same image is already set.
			return;
		} else {
			ivImage.setImageDrawable(null);
		}
		
		ivImage.setTag(imageUrl);
		BCPDownloadUtils.downloadBitmap(imageUrl, new OnBitmapDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("UsedCarView.downloadImage.onError." + "\nurl : " + url);
			}

			@Override
			public void onCompleted(String url, Bitmap bitmap) {

				try {
					LogUtils.log("UsedCarView.downloadImage.onCompleted." + "\nurl : " + url);
					
					if(bitmap != null && !bitmap.isRecycled()) {
						ivImage.setImageBitmap(bitmap);
					}
					
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		}, 176);
	}
	
	public void setCar(final Car car) {

		this.car = car;
		
		if(car == null) {
			return;
		}

		if(!StringUtils.isEmpty(car.getRep_img_url())) {
			downloadImage(car.getRep_img_url(), ivImage);
		}

		LogUtils.log("###CarView.setCar.  itemCode : " + car.getItemCode() + ", carNAme : " + car.getCar_full_name());
		
		//Set cover.
		switch(car.getItemCode()) {
		
		case BCPConstants.ITEM_CAR_BID_IN_PROGRESS:
			
			if(car.getMy_bid_price() != 0) {
				cover.setBackgroundResource(R.drawable.bid_frame2);
			} else {
				cover.setBackgroundResource(R.drawable.bid_frame1);
			}
			break;
			
		case BCPConstants.ITEM_CAR_BID_MINE:
			cover.setBackgroundResource(R.drawable.bid_frame1);
			break;
			
		case BCPConstants.ITEM_CAR_BID_SUCCESS:
			
			int rank = 0;
			for(int i=0; i<car.getBids().size(); i++) {
				
				if(car.getBids().get(i).getDealer_id() == MainActivity.dealer.getId()) {
					rank = i;
				}
			}
			
			switch(rank) {
			
			case 0:
				cover.setBackgroundResource(R.drawable.complete_frame_1);
				break;
			case 1:
				cover.setBackgroundResource(R.drawable.complete_frame_2);
				break;
			case 2:
				cover.setBackgroundResource(R.drawable.complete_frame_3);
				break;
				
				default:
					cover.setBackgroundResource(R.drawable.complete_frame);
			}
			break;
			
		case BCPConstants.ITEM_CAR_DEALER:

			if(car.getDealer_id() == MainActivity.dealer.getId()) {
				cover.setBackgroundResource(R.drawable.used_market_car_frame2);
			} else {
				cover.setBackgroundResource(R.drawable.used_market_car_frame);
			}
			break;
			
		case BCPConstants.ITEM_CAR_DEALER_MINE:
			cover.setBackgroundResource(R.drawable.used_market_car_frame);
			break;
			
		case BCPConstants.ITEM_CAR_BID_COMPLETED:
			cover.setBackgroundResource(R.drawable.mypage_complete_frame);
			break;
			
		case BCPConstants.ITEM_CAR_DEALER_COMPLETED:
			cover.setBackgroundResource(R.drawable.mypage_complete_frame);
			break;
		}
		
		if(car.getType() == Car.TYPE_DEALER) {
			ivProfile.setVisibility(View.VISIBLE);
			tvDealerName.setVisibility(View.VISIBLE);
			rankBadge.setVisibility(View.VISIBLE);
			tvBiddingInfo.setVisibility(View.INVISIBLE);
			tvLikeText.setVisibility(View.VISIBLE);
			btnLike.setVisibility(View.VISIBLE);
			
			downloadImage(car.getDealer_profile_img_url(), ivProfile);
			tvDealerName.setText(car.getDealer_name());

			switch(car.getDealer_level()) {
			
			case Dealer.LEVEL_FRESH_MAN:
				rankBadge.setBackgroundResource(R.drawable.main_used_grade4);
				break;
			case Dealer.LEVEL_NORAML_DEALER:
				rankBadge.setBackgroundResource(R.drawable.main_used_grade3);
				break;
				
			case Dealer.LEVEL_SUPERB_DEALER:
				rankBadge.setBackgroundResource(R.drawable.main_used_grade2);
				break;
				
			case Dealer.LEVEL_POWER_DEALER:
				rankBadge.setBackgroundResource(R.drawable.main_used_grade1);
				break;
			}
		} else {
			ivProfile.setVisibility(View.INVISIBLE);
			tvDealerName.setVisibility(View.INVISIBLE);
			rankBadge.setVisibility(View.INVISIBLE);
			tvBiddingInfo.setVisibility(View.VISIBLE);
			tvLikeText.setVisibility(View.INVISIBLE);
			btnLike.setVisibility(View.INVISIBLE);
			
			downloadImage(car.getSeller_profile_img_url(), ivProfile);
		}
		
		tvCarName.setText(car.getModel_name());
		
		if(car.getType() == Car.TYPE_BID) {
			tvBiddingInfo.setText(
					"참여 " + car.getBidders_cnt() + "명 / 입찰 " + car.getBids_cnt() + "회");
			
		} else {
			int likesCount = car.getLikes_cnt();
			
			if(likesCount > 9999) {
				likesCount = 9999;
			}
			
			btnLike.setText("" + likesCount);
		}
		
		tvInfo.setText(car.getYear() + "년 / "
				+ StringUtils.getFormattedNumber(car.getMileage()) + "km / "
				+ car.getArea());
		
		//무사고.
		if(car.getHad_accident() == 2) {
			infoBadges[0].setVisibility(View.VISIBLE);
			
		//유사고.
		} else if(car.getHad_accident() == 1) {
			infoBadges[0].setVisibility(View.INVISIBLE);
			
		//사고여부 모름.
		} else {
			infoBadges[0].setVisibility(View.INVISIBLE);
		}
		
		//1인신조.
		if(car.getIs_oneman_owned() == 1) {
			infoBadges[1].setVisibility(View.VISIBLE);
			
		//1인신조 아님.
		} else {
			infoBadges[1].setVisibility(View.INVISIBLE);
		}
		
		//4륜구동.
		if(car.getCar_wd().equals("4WD")) {
			infoBadges[2].setVisibility(View.VISIBLE);
			
		//2륜구동.
		} else {
			infoBadges[2].setVisibility(View.INVISIBLE);
		}
		
		//수동.
		if(car.getTransmission_type().equals("manual")) {
			infoBadges[3].setVisibility(View.VISIBLE);
			
		//자동
		} else {
			infoBadges[3].setVisibility(View.INVISIBLE);
		}

		//내가 입찰한 경우에만 내 입찰가 표시.
		//내 입찰가가 현재가, 최고가, 낙찰가와 같은 경우 빨간색, 아닌 경우 검은색.
		if(car.getType() == Car.TYPE_DEALER) {
			//중고차 마켓 : 판매가.
			priceTextView1.setType(PriceTextView.TYPE_SELLING_SMALL);
			priceTextView1.setPrice(car.getPrice());
			priceTextView2.setVisibility(View.GONE);
		} else {

			long price1 = 0;
			
			switch(car.getItemCode()) {

			//입찰중 : 현재가 + 내 입찰가.
			case BCPConstants.ITEM_CAR_BID_IN_PROGRESS:
			//내 입찰 : 현재가 + 내 입찰가.
			case BCPConstants.ITEM_CAR_BID_MINE:
				priceTextView1.setType(PriceTextView.TYPE_CURRENT_SMALL);
				price1 = car.getPrice();
				break;
			//입찰 성공 : 최고가 + 내 입찰가.
			case BCPConstants.ITEM_CAR_BID_SUCCESS:
				priceTextView1.setType(PriceTextView.TYPE_HIGHEST);
				price1 = car.getMax_bid_price(); 
				break;
				
			//거래완료내역 - 바이카옥션 : 낙찰가 + 내 입찰가.
			case BCPConstants.ITEM_CAR_BID_COMPLETED:
				priceTextView1.setType(PriceTextView.TYPE_SUCCESS);
				price1 = car.getPrice();
				break;
			}

			priceTextView1.setPrice(price1);
			
			if(car.getMy_bid_price() != 0) {
				priceTextView2.setType(PriceTextView.TYPE_MY_BIDDING);
				priceTextView2.setPrice(car.getMy_bid_price());
				priceTextView2.setVisibility(View.VISIBLE);
				
				if(price1 == car.getMy_bid_price()) {
					priceTextView2.setTextColor(getResources().getColor(R.color.color_red));
				} else {
					priceTextView2.setTextColor(getResources().getColor(R.color.holo_text));
				}
			} else {
				priceTextView2.setVisibility(View.GONE);
			}
		}
		
		if(car.getItemCode() == BCPConstants.ITEM_CAR_DEALER_MINE) {
			btnComplete.setVisibility(View.VISIBLE);
			btnComplete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					setComplete(car.getId());
				}
			});
		} else {
			btnComplete.setVisibility(View.INVISIBLE);
		}
		
		if(car.getItemCode() == BCPConstants.ITEM_CAR_BID_IN_PROGRESS
				|| car.getItemCode() == BCPConstants.ITEM_CAR_BID_MINE) {
			tvRemainTime.setVisibility(View.VISIBLE);
			setOnTimeListener();
		} else {
			tvRemainTime.setVisibility(View.INVISIBLE);
		}
		
		//immediatlyBadge.
		if(car.getType() == Car.TYPE_BID
				&& car.getTo_sell_directly() == 1) {
			immediatlyBadge.setVisibility(View.VISIBLE);
		} else {
			immediatlyBadge.setVisibility(View.GONE);
		}
		
		//Set badges. (statusBadge, rankBadge2, completeBadge)
		statusBadge.setVisibility(View.GONE);
		rankBadge2.setVisibility(View.GONE);
		completeBadge.setVisibility(View.GONE);
		
		if(car.getItemCode() == BCPConstants.ITEM_CAR_BID_COMPLETED) {
			rankBadge2.setVisibility(View.VISIBLE);

			boolean isSelected = car.getDealer_id() == MainActivity.dealer.getId();
			
			switch(car.getMy_bid_ranking()) {
			
			case 1:
				rankBadge2.setBackgroundResource(
						isSelected?R.drawable.mypage_complete_icon1_1
								:R.drawable.mypage_complete_icon2_1 );
				break;
				
			case 2:
				rankBadge2.setBackgroundResource(
						isSelected?R.drawable.mypage_complete_icon1_2
								:R.drawable.mypage_complete_icon2_2 );
				break;
				
			case 3:
				rankBadge2.setBackgroundResource(
						isSelected?R.drawable.mypage_complete_icon1_3
								:R.drawable.mypage_complete_icon2_3 );
				break;
				
				default:
					rankBadge2.setBackgroundResource(R.drawable.mypage_complete_icon3);
					break;
			}
			
		} else if(car.getItemCode() == BCPConstants.ITEM_CAR_DEALER_COMPLETED) {
			completeBadge.setVisibility(View.VISIBLE);
		} else {
			
			if(car.getType() == Car.TYPE_BID) {

				statusBadge.setVisibility(View.VISIBLE);
				
				switch(car.getStatus()) {
				
				case Car.STATUS_STAND_BY_APPROVAL:
				case Car.STATUS_STAND_BY_BIDING:
					statusBadge.setVisibility(View.GONE);
					break;
					
				case Car.STATUS_BIDDING:
					
					if(car.getBid_until_at() -System.currentTimeMillis() / 1000 <= 3600) {
						statusBadge.setBackgroundResource(R.drawable.auction_sale2_icon2);
					} else {
						statusBadge.setVisibility(View.GONE);
					}
					break;
					
				case Car.STATUS_BID_COMPLETE:
					statusBadge.setBackgroundResource(R.drawable.auction_sale2_icon3);
					break;
					
				case Car.STATUS_BID_SUCCESS:
				case Car.STATUS_BID_FAIL:
				case Car.STATUS_PAYMENT_COMPLETED:
				case Car.STATUS_TRADE_COMPLETE:
					statusBadge.setBackgroundResource(R.drawable.auction_sale2_icon4);
					break;
				}
			} else {
				statusBadge.setVisibility(View.GONE);
			}
		}
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressWarnings("deprecation")
	public void clearView() {
		
		ivImage.setImageDrawable(null);
		ivProfile.setImageDrawable(null);
		tvDealerName.setText("--");
		tvCarName.setText("--");
		btnLike.setText("0");
		tvInfo.setText("--년 / --km / --");
		tvBiddingInfo.setText("참여 -명 / 입찰 -회");

		if(AppInfoUtils.checkMinVersionLimit(16)) {
			cover.setBackground(null);
			rankBadge.setBackground(null);
		} else {
			cover.setBackgroundDrawable(null);
			rankBadge.setBackgroundDrawable(null);
		}
		
		infoBadges[0].setVisibility(View.INVISIBLE);
		infoBadges[1].setVisibility(View.INVISIBLE);
		infoBadges[2].setVisibility(View.INVISIBLE);
		infoBadges[3].setVisibility(View.INVISIBLE);
		
		priceTextView1.setPrice(0);
		priceTextView2.setPrice(0);
		priceTextView2.setVisibility(View.GONE);
		tvRemainTime.setText("-- : -- : --");
		
		immediatlyBadge.setVisibility(View.GONE);
		statusBadge.setVisibility(View.GONE);
		rankBadge2.setVisibility(View.GONE);
		completeBadge.setVisibility(View.GONE);
	}
	
	public ImageView getIvImage() {
		
		return ivImage;
	}

	public void setComplete(int onsalecar_id) {
		
		//http://dev.bye-car.com/onsalecars/dealer/set_status.json?onsalecar_id=1&status=30
		String url = BCPAPIs.CAR_DEALER_STATUS_URL
				+ "?onsalecar_id=" + onsalecar_id
				+ "&status=" + Car.STATUS_TRADE_COMPLETE;
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("CarView.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToCompleteSelling);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("CarView.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_selling);
						MainActivity.activity.getTopFragment().refreshPage();
					} else {
						ToastUtils.showToast(R.string.failToCompleteSelling);
					}
				} catch (Exception e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToCompleteSelling);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					ToastUtils.showToast(R.string.failToCompleteSelling);
				}
			}
		});
	}

	public void setActivity(BCPFragmentActivity activity) {
		
		this.activity = activity;
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

					try {
						long remainTime = car.getBid_until_at() * 1000 
								+ (car.getStatus() < Car.STATUS_BID_COMPLETE ? 0 : 86400000) 
								- System.currentTimeMillis();

						if(remainTime < 0) {
							tvRemainTime.setText("-- : -- : --");
			        	} else {
			        		String formattedRemainTime = StringUtils.getTimeString(remainTime);
			    	    	tvRemainTime.setText(formattedRemainTime);
			        	}
					} catch (Exception e) {
						LogUtils.trace(e);
						tvRemainTime.setText("-- : -- : --");
					}
				}
				
				@Override
				public String getName() {

					return "CarView";
				}
				
				@Override
				public Activity getActivity() {

					return activity;
				}
			}; 
		}
		
		TimerUtils.addOnTimeChangedListener(onTimeChangedListener);
	}
}