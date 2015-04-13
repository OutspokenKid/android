package com.byecar.views;

import org.json.JSONObject;

import android.annotation.TargetApi;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPDownloadUtils;
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

public class OtherCarView extends RelativeLayout {

	private ImageView ivImage;
	private ImageView ivProfile;
	private TextView tvDealerName;
	private View rankBadge;
	private TextView tvCarName;
	private Button btnLike;
	private TextView tvInfo;
	private View[] infoBadges = new View[4];
	private PriceTextView priceTextView;
	
	public OtherCarView(Context context) {
		this(context, null, 0);
	}
	
	public OtherCarView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public OtherCarView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}
	
	public void init() {
	
		RelativeLayout.LayoutParams rp = null;
		
		//ivImage.
		ivImage = new ImageView(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(242), LayoutParams.MATCH_PARENT);
		ivImage.setLayoutParams(rp);
		ivImage.setId(R.id.usedCarView_ivImage);
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
		ivProfile.setId(R.id.usedCarView_ivProfile);
		ivProfile.setScaleType(ScaleType.CENTER_CROP);
		ivProfile.setBackgroundResource(R.drawable.detail_default);
		this.addView(ivProfile);
		
		//cover.
		View cover = new View(getContext());
		rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		cover.setLayoutParams(rp);
		cover.setBackgroundResource(R.drawable.main_used_car_frame2);
		this.addView(cover);
		
		//tvDealerName.
		tvDealerName = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(70), ResizeUtils.getSpecificLength(56));
		rp.addRule(ALIGN_PARENT_BOTTOM);
		rp.addRule(RIGHT_OF, R.id.usedCarView_ivProfile);
		rp.leftMargin = ResizeUtils.getSpecificLength(10);
		tvDealerName.setLayoutParams(rp);
		tvDealerName.setId(R.id.usedCarView_tvDealerName);
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
		rp.addRule(RIGHT_OF, R.id.usedCarView_tvDealerName);
		rp.bottomMargin = ResizeUtils.getSpecificLength(15);
		rankBadge.setLayoutParams(rp);
		this.addView(rankBadge);
		
		//tvCarName.
		tvCarName = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(160), ResizeUtils.getSpecificLength(60));
		rp.addRule(ALIGN_PARENT_TOP);
		rp.addRule(RIGHT_OF, R.id.usedCarView_ivImage);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		tvCarName.setLayoutParams(rp);
		tvCarName.setTextColor(getResources().getColor(R.color.holo_text));
		tvCarName.setSingleLine();
		tvCarName.setEllipsize(TruncateAt.END);
		FontUtils.setFontSize(tvCarName, 30);
		FontUtils.setFontStyle(tvCarName, FontUtils.BOLD);
		tvCarName.setGravity(Gravity.CENTER_VERTICAL);
		this.addView(tvCarName);
		
		//btnLike.
		btnLike = new Button(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(90), ResizeUtils.getSpecificLength(40));
		rp.addRule(ALIGN_PARENT_RIGHT);
		rp.topMargin = ResizeUtils.getSpecificLength(10);
		rp.rightMargin = ResizeUtils.getSpecificLength(9);
		btnLike.setLayoutParams(rp);
		btnLike.setPadding(ResizeUtils.getSpecificLength(32), 0, 
				ResizeUtils.getSpecificLength(10), ResizeUtils.getSpecificLength(2));
		btnLike.setId(R.id.usedCarView_btnLike);
		btnLike.setBackgroundResource(R.drawable.main_like_btn_a);
		btnLike.setTextColor(Color.WHITE);
		FontUtils.setFontSize(btnLike, 18);
		btnLike.setGravity(Gravity.CENTER);
		this.addView(btnLike);
		
		//tvLikeText.
		TextView tvLikeText = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, ResizeUtils.getSpecificLength(40));
		rp.addRule(ALIGN_TOP, R.id.usedCarView_btnLike);
		rp.addRule(LEFT_OF, R.id.usedCarView_btnLike);
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
		rp.addRule(RIGHT_OF, R.id.usedCarView_ivImage);
		rp.leftMargin = ResizeUtils.getSpecificLength(23);
		rp.bottomMargin = ResizeUtils.getSpecificLength(41);
		infoBadges[0].setLayoutParams(rp);
		infoBadges[0].setId(R.id.usedCarView_infoBadg1);
		this.addView(infoBadges[0]);
		
		infoBadges[1] = new View(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(62), ResizeUtils.getSpecificLength(23));
		rp.addRule(ALIGN_TOP, R.id.usedCarView_infoBadg1);
		rp.addRule(RIGHT_OF, R.id.usedCarView_infoBadg1);
		rp.leftMargin = ResizeUtils.getSpecificLength(7);
		infoBadges[1].setLayoutParams(rp);
		this.addView(infoBadges[1]);

		infoBadges[2] = new View(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(62), ResizeUtils.getSpecificLength(23));
		rp.addRule(ALIGN_LEFT, R.id.usedCarView_infoBadg1);
		rp.addRule(ALIGN_TOP, R.id.usedCarView_infoBadg1);
		rp.topMargin = ResizeUtils.getSpecificLength(30);
		infoBadges[2].setLayoutParams(rp);
		this.addView(infoBadges[2]);
		
		infoBadges[3] = new View(getContext());
		rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(62), ResizeUtils.getSpecificLength(23));
		rp.addRule(RIGHT_OF, R.id.usedCarView_infoBadg1);
		rp.addRule(ALIGN_TOP, R.id.usedCarView_infoBadg1);
		rp.leftMargin = ResizeUtils.getSpecificLength(7);
		rp.topMargin = ResizeUtils.getSpecificLength(30);
		infoBadges[3].setLayoutParams(rp);
		this.addView(infoBadges[3]);
		
		//priceTextView.
		priceTextView = new PriceTextView(getContext());
		rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, ResizeUtils.getSpecificLength(53));
		rp.addRule(ALIGN_TOP, R.id.usedCarView_infoBadg1);
		rp.addRule(ALIGN_PARENT_RIGHT);
		rp.rightMargin = ResizeUtils.getSpecificLength(15);
		priceTextView.setLayoutParams(rp);
		priceTextView.setType(PriceTextView.TYPE_USED_CAR);
		this.addView(priceTextView);
		
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

		if(car == null) {
			return;
		}

		if(!StringUtils.isEmpty(car.getRep_img_url())) {
			downloadImage(car.getRep_img_url(), ivImage);
		}
		
		if(car.getType() == Car.TYPE_DEALER) {
			downloadImage(car.getDealer_profile_img_url(), ivProfile);
			tvDealerName.setText(car.getDealer_name());
			
			rankBadge.setVisibility(View.VISIBLE);
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
			downloadImage(car.getSeller_profile_img_url(), ivProfile);
			tvDealerName.setText(car.getSeller_name());
			rankBadge.setVisibility(View.INVISIBLE);
		}
		
		tvCarName.setText(car.getModel_name());
		
		btnLike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setLike(car);
			}
		});
		
		int likesCount = car.getLikes_cnt();
		
		if(likesCount > 9999) {
			likesCount = 9999;
		}
		
		btnLike.setText("" + likesCount);
		
		tvInfo.setText(car.getYear() + "년 / "
				+ StringUtils.getFormattedNumber(car.getMileage()) + "km / "
				+ car.getArea());
		
		//무사고.
		if(car.getHad_accident() == 2) {
			infoBadges[0].setBackgroundResource(R.drawable.main_used_option1_a);
			
		//유사고.
		} else if(car.getHad_accident() == 1) {
			infoBadges[0].setBackgroundResource(R.drawable.main_used_option1_b);
			
		//사고여부 모름.
		} else {
			infoBadges[0].setBackgroundResource(R.drawable.main_used_option1_c);
		}
		
		//1인신조.
		if(car.getIs_oneman_owned() == 1) {
			infoBadges[1].setBackgroundResource(R.drawable.main_used_option2_a);
			
		//1인신조 아님.
		} else {
			infoBadges[1].setBackgroundResource(R.drawable.main_used_option2_b);
		}
		
		//4륜구동.
		if(car.getCar_wd().equals("4WD")) {
			infoBadges[2].setBackgroundResource(R.drawable.main_used_option3_a);
			
		//2륜구동.
		} else {
			infoBadges[2].setBackgroundResource(R.drawable.main_used_option3_b);
		}
		
		//수동.
		if(car.getTransmission_type().equals("manual")) {
			infoBadges[3].setBackgroundResource(R.drawable.main_used_option4_a);
			
		//자동
		} else {
			infoBadges[3].setBackgroundResource(R.drawable.main_used_option4_b);
		}
		
		priceTextView.setPrice(car.getPrice());
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressWarnings("deprecation")
	public void clearView() {

		ivImage.setImageDrawable(null);
		ivProfile.setImageDrawable(null);
		tvDealerName.setText(null);
		tvCarName.setText(null);
		btnLike.setText("0");
		tvInfo.setText("--년 / --km / --");

		if(AppInfoUtils.checkMinVersionLimit(16)) {
			rankBadge.setBackground(null);
			infoBadges[0].setBackground(null);
			infoBadges[1].setBackground(null);
			infoBadges[2].setBackground(null);
			infoBadges[3].setBackground(null);
		} else {
			rankBadge.setBackgroundDrawable(null);
			infoBadges[0].setBackgroundDrawable(null);
			infoBadges[1].setBackgroundDrawable(null);
			infoBadges[2].setBackgroundDrawable(null);
			infoBadges[3].setBackgroundDrawable(null);
		}
		
		priceTextView.setPrice(0);
	}
	
	public ImageView getIvImage() {
		
		return ivImage;
	}

	public void setLike(Car car) {
		
		String url = null;
		
		if(car.getIs_liked() == 0) {
			btnLike.setBackgroundResource(R.drawable.main_like_btn_b);
			car.setLikes_cnt(car.getLikes_cnt() + 1);
			car.setIs_liked(1);

			url = BCPAPIs.CAR_DEALER_LIKE_URL;
		} else {
			btnLike.setBackgroundResource(R.drawable.main_like_btn_a);
			car.setLikes_cnt(car.getLikes_cnt() - 1);
			car.setIs_liked(0);

			url = BCPAPIs.CAR_DEALER_UNLIKE_URL;
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