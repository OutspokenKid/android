package com.byecar.byecarplus.common;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.json.JSONObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.MainActivity;
import com.byecar.byecarplus.R;
import com.byecar.byecarplus.classes.BCPAPIs;
import com.byecar.byecarplus.classes.BCPConstants;
import com.byecar.byecarplus.classes.BCPFragment;
import com.byecar.byecarplus.fragments.user.EditUserInfoPage;
import com.byecar.byecarplus.models.Car;
import com.byecar.byecarplus.models.CarModelDetailInfo;
import com.byecar.byecarplus.views.TitleBar;
import com.outspoken_kid.activities.BaseFragmentActivity;
import com.outspoken_kid.activities.MultiSelectGalleryActivity.OnAfterPickImageListener;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ImageUploadUtils;
import com.outspoken_kid.utils.ImageUploadUtils.OnAfterUploadImage;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo.holo_light.HoloStyleEditText;

public class CarRegistrationPage extends BCPFragment {

	private static final int MIN_DESC_COUNT = 0;
	private static final int MAX_DESC_COUNT = 200;
	
	private static final int MAX_PRICE = 50000;
	
	public static final int TYPE_REGISTRATION = 0;
	public static final int TYPE_REQUEST_CERTIFICATION = 1;
	public static final int TYPE_EDIT = 2;
	
	private int id;
	private int carType;
	private Car car;
	private CarModelDetailInfo carModelDetailInfo;
	
	private ProgressBar progressBar;
	private TextView tvPercentage;
	private TextView tvPercentage2;
	private TextView tvWriteAllContents;
	private TextView tvInputDealerInfoText;
	private TextView tvDealerInfoCertified;
	private Button btnEditDealerInfo;
	private TextView tvDealerInfo;
	private TextView tvCarPhotoText;
	private Button[] btnPhotos;
	private ImageView[] ivPhotos;
	private TextView tvMainImageText;
	private TextView tvAddedPhotoText;
	private View lineAfterPhoto;
	private TextView tvPriceText;
	private EditText etPrice;
	private Button btnClearPrice;
	private View lineAfterPrice;
	private TextView tvCarInfoText;
	private Button[] btnCarInfos;
	private TextView tvDetailCarInfo;
	private HoloStyleEditText[] etDetailCarInfos;
	private RelativeLayout relativeForOption;
	private View lineAfterCarOption;
	private TextView tvCarDescriptionFromDealer;
	private EditText etCarDescriptionFromDealer;
	private TextView tvTextCount;
	private View termOfUse;
	private Button btnTermOfUse;
	private View immediatlySale;
	private Button btnImmediatlySale;
	private Button btnComplete;
	private Button btnRequest;
	
	private View[] optionViews;
	private boolean[] checked;
	
	private String[] carInfoStrings = new String[5];
	
	private int type;
	private boolean isTermOfUseClicked;
	private boolean isImmediatlySaleClicked;
	
	private int selectedImageIndex;
	private String[] selectedImageSdCardPaths = new String[8];
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		BaseFragmentActivity.onAfterPickImageListener = new OnAfterPickImageListener() {
			
			@Override
			public void onAfterPickImage(String[] sdCardPaths, Bitmap[] thumbnails) {
				
				if(thumbnails != null && thumbnails.length > 0) {
					ivPhotos[selectedImageIndex].setImageBitmap(thumbnails[0]);
					selectedImageSdCardPaths[selectedImageIndex] = sdCardPaths[0];
				}
				
				checkProgress();
			}
		};
	}
	
	@Override
 	public void bindViews() {
		
		titleBar = (TitleBar) mThisView.findViewById(R.id.carRegistrationPage_titleBar);
		
		progressBar = (ProgressBar) mThisView.findViewById(R.id.carRegistrationPage_progressBar);
		tvPercentage = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvPercentage);
		tvPercentage2 = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvPercentage2);
		tvWriteAllContents = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvWriteAllContents);
		tvInputDealerInfoText = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvInputDealerInfo);
		tvDealerInfoCertified = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvDealerInfoCertified);
		btnEditDealerInfo = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnEditDealerInfo);
		tvDealerInfo = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvDealerInfo);
		tvCarPhotoText = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvCarPhoto);
		
		btnPhotos = new Button[8];
		btnPhotos[0] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarPhoto1);
		btnPhotos[1] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarPhoto2);
		btnPhotos[2] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarPhoto3);
		btnPhotos[3] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarPhoto4);
		btnPhotos[4] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnAddedPhoto1);
		btnPhotos[5] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnAddedPhoto2);
		btnPhotos[6] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnAddedPhoto3);
		btnPhotos[7] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnAddedPhoto4);

		ivPhotos = new ImageView[8];
		ivPhotos[0] = (ImageView) mThisView.findViewById(R.id.carRegistrationPage_ivCarPhoto1);
		ivPhotos[1] = (ImageView) mThisView.findViewById(R.id.carRegistrationPage_ivCarPhoto2);
		ivPhotos[2] = (ImageView) mThisView.findViewById(R.id.carRegistrationPage_ivCarPhoto3);
		ivPhotos[3] = (ImageView) mThisView.findViewById(R.id.carRegistrationPage_ivCarPhoto4);
		ivPhotos[4] = (ImageView) mThisView.findViewById(R.id.carRegistrationPage_ivAddedPhoto1);
		ivPhotos[5] = (ImageView) mThisView.findViewById(R.id.carRegistrationPage_ivAddedPhoto2);
		ivPhotos[6] = (ImageView) mThisView.findViewById(R.id.carRegistrationPage_ivAddedPhoto3);
		ivPhotos[7] = (ImageView) mThisView.findViewById(R.id.carRegistrationPage_ivAddedPhoto4);
		tvMainImageText = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvMainImage);
		tvAddedPhotoText = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvAddedPhoto);
		
		lineAfterPhoto = mThisView.findViewById(R.id.carRegistrationPage_lineAfterPhoto);
		
		tvPriceText = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvPrice);
		etPrice = (EditText) mThisView.findViewById(R.id.carRegistrationPage_etPrice);
		btnClearPrice = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnClearPrice);
		
		lineAfterPrice = mThisView.findViewById(R.id.carRegistrationPage_lineAfterPrice);
		
		tvCarInfoText = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvCarInfo);
		
		btnCarInfos = new Button[6];
		btnCarInfos[0] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarInfo1);
		btnCarInfos[1] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarInfo2);
		btnCarInfos[2] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarInfo3);
		btnCarInfos[3] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarInfo4);
		btnCarInfos[4] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarInfo5);
		btnCarInfos[5] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarInfo6);
		
		tvDetailCarInfo = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvDetailCarInfo);
		etDetailCarInfos = new HoloStyleEditText[5];
		etDetailCarInfos[0] = (HoloStyleEditText) mThisView.findViewById(R.id.carRegistrationPage_etDetailCarInfo1);
		etDetailCarInfos[1] = (HoloStyleEditText) mThisView.findViewById(R.id.carRegistrationPage_etDetailCarInfo2);
		etDetailCarInfos[2] = (HoloStyleEditText) mThisView.findViewById(R.id.carRegistrationPage_etDetailCarInfo3);
		etDetailCarInfos[3] = (HoloStyleEditText) mThisView.findViewById(R.id.carRegistrationPage_etDetailCarInfo4);
		etDetailCarInfos[4] = (HoloStyleEditText) mThisView.findViewById(R.id.carRegistrationPage_etDetailCarInfo5);
		relativeForOption = (RelativeLayout) mThisView.findViewById(R.id.carRegistrationPage_relativeForOption);
		lineAfterCarOption = mThisView.findViewById(R.id.carRegistrationPage_lineAfterCarOption);
		tvCarDescriptionFromDealer = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvCarDescriptionFromDealer);
		etCarDescriptionFromDealer = (EditText) mThisView.findViewById(R.id.carRegistrationPage_etCarDescriptionFromDealer);
		tvTextCount = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvTextCount);
		termOfUse = mThisView.findViewById(R.id.carRegistrationPage_termOfUse);
		btnTermOfUse = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnTermOfUse);
		immediatlySale = mThisView.findViewById(R.id.carRegistrationPage_immediatlySale);
		btnImmediatlySale = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnImmediatlySale);
		btnComplete = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnComplete);
		btnRequest = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnRequest);
	}

	@Override
	public void setVariables() {
	
		if(getArguments() != null) {
			type = getArguments().getInt("type");
			
			id = getArguments().getInt("id");
			carType = getArguments().getInt("carType");
			car = (Car) getArguments().getSerializable("car");
		}
	}

	@Override
	public void createPage() {

		titleBar.hideBottomLine();
		
		int size = etDetailCarInfos.length;
		for(int i=0; i<size; i++) {
			etDetailCarInfos[i].getEditText().setTextColor(getResources().getColor(R.color.holo_text));
			etDetailCarInfos[i].getEditText().setHintTextColor(getResources().getColor(R.color.holo_text_hint));
		}
		
		etDetailCarInfos[0].setHint(R.string.hintForDetailCarInfo1);
		etDetailCarInfos[1].setHint(R.string.hintForDetailCarInfo2);
		etDetailCarInfos[2].setHint(R.string.hintForDetailCarInfo3);
		etDetailCarInfos[3].setHint(R.string.hintForDetailCarInfo4);
		etDetailCarInfos[4].setHint(R.string.hintForDetailCarInfo5);
		
		etCarDescriptionFromDealer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_DESC_COUNT)});
		tvTextCount.setText("0 / " + MAX_DESC_COUNT + "자");
		
		if(type == TYPE_REQUEST_CERTIFICATION) {
			size = btnPhotos.length;
			for(int i=0; i<size; i++) {
				btnPhotos[i].setVisibility(View.GONE);
				ivPhotos[i].setVisibility(View.GONE);
			}
			
			tvCarPhotoText.setVisibility(View.GONE);
			tvMainImageText.setVisibility(View.GONE);
			tvAddedPhotoText.setVisibility(View.GONE);
			lineAfterPhoto.setVisibility(View.GONE);

			for(int i=2; i<6; i++) {
				btnCarInfos[i].setVisibility(View.GONE);
			}

			etDetailCarInfos[1].setVisibility(View.GONE);
			etDetailCarInfos[3].setVisibility(View.GONE);
			etDetailCarInfos[4].setVisibility(View.GONE);
			
			relativeForOption.setVisibility(View.GONE);
			lineAfterCarOption.setVisibility(View.GONE);
			
			tvCarDescriptionFromDealer.setVisibility(View.GONE);
			etCarDescriptionFromDealer.setVisibility(View.GONE);
			tvTextCount.setVisibility(View.GONE);
			
			tvWriteAllContents.setText(R.string.writeAllContentForRequestCertification);
			
			immediatlySale.setVisibility(View.GONE);
			btnImmediatlySale.setVisibility(View.GONE);
			
			btnRequest.setVisibility(View.VISIBLE);
			btnComplete.setVisibility(View.INVISIBLE);
		} else {
			tvWriteAllContents.setText(R.string.writeAllContentForRegistration);
			btnRequest.setVisibility(View.INVISIBLE);
			btnComplete.setVisibility(View.VISIBLE);
		}
		
		if(type == TYPE_REGISTRATION) {
			
			switch(carType) {
			
			case Car.TYPE_BID:
				tvPriceText.setText(R.string.priceForBid);
				etPrice.setHint(R.string.hintForPriceBid);
				break;
				
			case Car.TYPE_DIRECT_NORMAL:
				tvPriceText.setText(R.string.priceForNormal);
				etPrice.setHint(R.string.hintForPriceNormal);
				break;
			}
			
			tvPriceText.setVisibility(View.VISIBLE);
			etPrice.setVisibility(View.VISIBLE);
			btnClearPrice.setVisibility(View.VISIBLE);
			lineAfterPrice.setVisibility(View.VISIBLE);
		} else {
			tvPriceText.setVisibility(View.GONE);
			etPrice.setVisibility(View.GONE);
			btnClearPrice.setVisibility(View.GONE);
			lineAfterPrice.setVisibility(View.GONE);
		}
	}

	@Override
	public void setListeners() {

		btnEditDealerInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putInt("type", EditUserInfoPage.TYPE_DEALER_INFO);
				mActivity.showPage(BCPConstants.PAGE_EDIT_USER_INFO, bundle);
			}
		});

		setImageViewsOnClickListener();
		
		for(int i=0; i<btnCarInfos.length; i++) {
			final int INDEX = i;
			
			btnCarInfos[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					if(INDEX + 1 != TypeSearchCarPage.TYPE_BRAND && carModelDetailInfo == null) {
						ToastUtils.showToast(R.string.selectCarFirst);
						return;
					}
					
					Bundle bundle = new Bundle();

					switch(INDEX) {
					
					case 0:
						bundle.putInt("type", TypeSearchCarPage.TYPE_BRAND);
						break;
					case 1:
						bundle.putInt("type", TypeSearchCarPage.TYPE_YEAR);
						
						if(!StringUtils.isEmpty(carModelDetailInfo.getYear_begin())) {
							bundle.putInt("year_begin", Integer.parseInt(carModelDetailInfo.getYear_begin()));
						} else {
							bundle.putInt("year_begin", 1990);
						}
						
						if(!StringUtils.isEmpty(carModelDetailInfo.getYear_end())) {
							bundle.putInt("year_end", Integer.parseInt(carModelDetailInfo.getYear_end()));
						} else {
							
							try {
								SimpleDateFormat format = new SimpleDateFormat("yyyy", Locale.getDefault());
								bundle.putInt("year_end", Integer.parseInt(format.format(System.currentTimeMillis())));
							} catch (Exception e) {
								LogUtils.trace(e);
							}
						}
						
						break;
					case 2:
						bundle.putInt("type", TypeSearchCarPage.TYPE_FUEL);
						break;
					case 3:
						bundle.putInt("type", TypeSearchCarPage.TYPE_TRANSMISSION);
						break;
					case 4:
						bundle.putInt("type", TypeSearchCarPage.TYPE_ACCIDENT);
						break;
					case 5:
						bundle.putInt("type", TypeSearchCarPage.TYPE_ONEMANOWNED);
						break;
					}
					
					mActivity.showPage(BCPConstants.PAGE_TYPE_SEARCH_CAR, bundle);
				}
			});
		}
		
		etPrice.addTextChangedListener(new TextWatcher() {
			
			private String lastText = null;
			private int lastCursorIndex;
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
				if(s != null) {
					lastText = s.toString();
					lastCursorIndex = etPrice.getSelectionStart() - 1;
				}
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
				try {
					if(s == null || s.length() == 0) {
						return;
					}
					
					String text = s.toString();
					
					if(text.contains(".") 
							|| (text.length() > 1 && text.charAt(0) == '0')) {
						etPrice.setText(lastText);
						etPrice.setSelection(lastCursorIndex);
					}
					
					int tempPrice = Integer.parseInt(text);
					
					if(tempPrice < 1) {
						ToastUtils.showToast(R.string.minPriceMustOverZero);
						etPrice.setText(lastText);
						etPrice.setSelection(lastCursorIndex);
					} else if(tempPrice > MAX_PRICE) {
						ToastUtils.showToast(R.string.maxPriceMustLessThanLimit);
						etPrice.setText(lastText);
						etPrice.setSelection(lastCursorIndex);
						return;
					}
					
					checkProgress();
				} catch (Exception e) {
				}
			}
		});
		
		btnClearPrice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				etPrice.setText(null);
				etPrice.requestFocus();
			}
		});
		
		etCarDescriptionFromDealer.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				if(s != null && s.length() != 0) {
					tvTextCount.setText(s.length() + " / " + MAX_DESC_COUNT + "자");
				} else {
					tvTextCount.setText("0 / " + MAX_DESC_COUNT + "자");
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
				checkProgress();
			}
		});
	
		termOfUse.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				isTermOfUseClicked = !isTermOfUseClicked;
				
				if(isTermOfUseClicked) {
					termOfUse.setBackgroundResource(R.drawable.registration_agree_btn_b);
				} else {
					termOfUse.setBackgroundResource(R.drawable.registration_agree_btn_a);
				}
			}
		});
		
		immediatlySale.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				isImmediatlySaleClicked = !isImmediatlySaleClicked;
				
				if(isImmediatlySaleClicked) {
					immediatlySale.setBackgroundResource(R.drawable.registration_direct_btn_b);
				} else {
					immediatlySale.setBackgroundResource(R.drawable.registration_direct_btn_a);
				}
			}
		});
	
		btnComplete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(!isTermOfUseClicked) {
					ToastUtils.showToast(R.string.agreeTermOfUse);
					return;
				} else if(progressBar.getProgress() != 100){
					String text = getString(R.string.writeAllContentForRegistration);
					ToastUtils.showToast(text.replace("*", ""));
				} else {
					SoftKeyboardUtils.hideKeyboard(mContext, etCarDescriptionFromDealer);
					uploadImages();
				}
			}
		});
		
		btnRequest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(!isTermOfUseClicked) {
					ToastUtils.showToast(R.string.agreeTermOfUse);
					return;
				} else if(progressBar.getProgress() != 100){
					String text = getString(R.string.writeAllContentForRegistration);
					ToastUtils.showToast(text.replace("*", ""));
				} else {
					SoftKeyboardUtils.hideKeyboard(mContext, etCarDescriptionFromDealer);
					requestCertification();
				}
			}
		});
	
		for(int i=0; i<5; i++) {
			
			etDetailCarInfos[i].getEditText().addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					
					checkProgress();
				}
			});
		}
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;

		//progressBar.
		rp = (RelativeLayout.LayoutParams) progressBar.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(30);
		
		//tvPercentage.
		rp = (RelativeLayout.LayoutParams) tvPercentage.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(105);
		rp.height = ResizeUtils.getSpecificLength(30);
		
		//tvPercentage2.
		rp = (RelativeLayout.LayoutParams) tvPercentage2.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(30);
		
		//writeIcon.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.carRegistrationPage_writeIcon).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(16);
		rp.height = ResizeUtils.getSpecificLength(16);
		rp.topMargin = ResizeUtils.getSpecificLength(7);
		rp.rightMargin = ResizeUtils.getSpecificLength(4);
		
		//tvInputDealerInfo.
		rp = (RelativeLayout.LayoutParams) tvInputDealerInfoText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		
		//tvDealerInfoCertified.
		rp = (RelativeLayout.LayoutParams) tvDealerInfoCertified.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);

		//btnEditDealerInfo.
		rp = (RelativeLayout.LayoutParams) btnEditDealerInfo.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(160);
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.topMargin = ResizeUtils.getSpecificLength(15);
		rp.rightMargin = ResizeUtils.getSpecificLength(26);
		
		//tvDealerInfo.
		rp = (RelativeLayout.LayoutParams) tvDealerInfo.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(220);
		
		//tvCarPhoto.
		rp = (RelativeLayout.LayoutParams) tvCarPhotoText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		
		for(int i=0; i<8; i++) {

			//btnCarPhoto1.
			rp = (RelativeLayout.LayoutParams) btnPhotos[i].getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(144);
			rp.height = ResizeUtils.getSpecificLength(110);
			
			if(i == 0 || i == 4) {
				rp.leftMargin = ResizeUtils.getSpecificLength(20);
				rp.topMargin = ResizeUtils.getSpecificLength(6);
			} else {
				rp.leftMargin = ResizeUtils.getSpecificLength(10);
			}
			
			//ivPhotos.
			rp = (RelativeLayout.LayoutParams) ivPhotos[i].getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(144);
			rp.height = ResizeUtils.getSpecificLength(110);
			rp.leftMargin = ResizeUtils.getSpecificLength(20);
			rp.topMargin = ResizeUtils.getSpecificLength(6);
			
			if(i == 0 || i == 4) {
				rp.leftMargin = ResizeUtils.getSpecificLength(20);
				rp.topMargin = ResizeUtils.getSpecificLength(6);
			} else {
				rp.leftMargin = ResizeUtils.getSpecificLength(10);
			}
		}
		
		//tvMainImageText.
		rp = (RelativeLayout.LayoutParams) tvMainImageText.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(144);
		
		//tvAddedPhotoText.
		rp = (RelativeLayout.LayoutParams) tvAddedPhotoText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);

		//tvPriceText.
		rp = (RelativeLayout.LayoutParams) tvPriceText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		
		//etPrice.
		rp = (RelativeLayout.LayoutParams) etPrice.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(590);
		rp.height = ResizeUtils.getSpecificLength(86);
		rp.topMargin = ResizeUtils.getSpecificLength(6);
		etPrice.setPadding(ResizeUtils.getSpecificLength(138), 0, 
				ResizeUtils.getSpecificLength(138), 0);
		
		//btnClearPrice.
		rp = (RelativeLayout.LayoutParams) btnClearPrice.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(40);
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.topMargin = ResizeUtils.getSpecificLength(24);
		rp.rightMargin = ResizeUtils.getSpecificLength(20);
		
		//tvCarInfoText.
		rp = (RelativeLayout.LayoutParams) tvCarInfoText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		
		//btnCarInfos.
		for(int i=0; i<btnCarInfos.length; i++) {
			rp = (RelativeLayout.LayoutParams) btnCarInfos[i].getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(586);
			rp.height = ResizeUtils.getSpecificLength(82);
			rp.topMargin = ResizeUtils.getSpecificLength(i==0?16:40);
		}
		
		//tvDetailCarInfo.
		rp = (RelativeLayout.LayoutParams) tvDetailCarInfo.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		
		int size = etDetailCarInfos.length;
		for(int i=0; i<size; i++) {
			rp = (RelativeLayout.LayoutParams) etDetailCarInfos[i].getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(586);
			rp.height = ResizeUtils.getSpecificLength(60);
			
			if(i != 0) {
				rp.topMargin = ResizeUtils.getSpecificLength(32);
			}
			
			FontUtils.setFontAndHintSize(etDetailCarInfos[i].getEditText(), 26, 20);
		}
		
		//writeIcon.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.carRegistrationPage_tvOption).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		
		//tvCarDescriptionFromDealer.
		rp = (RelativeLayout.LayoutParams) tvCarDescriptionFromDealer.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		
		//etCarDescriptionFromDealer.
		rp = (RelativeLayout.LayoutParams) etCarDescriptionFromDealer.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(160);
		
		//termOfUse.
		rp = (RelativeLayout.LayoutParams) termOfUse.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(313);
		rp.height = ResizeUtils.getSpecificLength(30);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		rp.topMargin = ResizeUtils.getSpecificLength(40);

		//btnTermOfUse.
		rp = (RelativeLayout.LayoutParams) btnTermOfUse.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(259);
		rp.height = ResizeUtils.getSpecificLength(30);
		rp.rightMargin = ResizeUtils.getSpecificLength(30);
		
		//immediatlySale.
		rp = (RelativeLayout.LayoutParams) immediatlySale.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(100);
		rp.height = ResizeUtils.getSpecificLength(30);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		rp.topMargin = ResizeUtils.getSpecificLength(30);
		
		//btnImmediatlySale.
		rp = (RelativeLayout.LayoutParams) btnImmediatlySale.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(179);
		rp.height = ResizeUtils.getSpecificLength(30);
		rp.rightMargin = ResizeUtils.getSpecificLength(30);
		
		//btnComplete.
		rp = (RelativeLayout.LayoutParams) btnComplete.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(82);
		rp.topMargin = ResizeUtils.getSpecificLength(25);
		
		//btnRequest.
		rp = (RelativeLayout.LayoutParams) btnRequest.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(82);
		rp.topMargin = ResizeUtils.getSpecificLength(25);
		
		FontUtils.setFontSize(tvPercentage, 24);
		FontUtils.setFontSize(tvPercentage2, 16);
		FontUtils.setFontSize(tvWriteAllContents, 18);
		
		FontUtils.setFontSize(tvPriceText, 30);
		FontUtils.setFontAndHintSize(etPrice, 26, 20);
		
		FontUtils.setFontSize(tvInputDealerInfoText, 30);
		FontUtils.setFontSize(tvDealerInfoCertified, 18);
		FontUtils.setFontSize(tvDealerInfo, 28);
		FontUtils.setFontStyle(tvDealerInfo, FontUtils.BOLD);
		tvDealerInfo.setLineSpacing(0, 1.5f);

		FontUtils.setFontSize(tvCarPhotoText, 30);
		FontUtils.setFontSize(tvMainImageText, 20);
		FontUtils.setFontSize(tvAddedPhotoText, 30);
		
		FontUtils.setFontSize(tvCarInfoText, 30);
		FontUtils.setFontSize(tvDetailCarInfo, 30);
		
		for(int i=0; i<btnCarInfos.length; i++) {
			FontUtils.setFontSize(btnCarInfos[i], 26);
		}
		
		FontUtils.setFontSize(tvCarDescriptionFromDealer, 30);
		FontUtils.setFontAndHintSize(etCarDescriptionFromDealer, 26, 20);
		FontUtils.setFontSize(tvTextCount, 20);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_car_registration;
	}

	@Override
	public int getBackButtonResId() {

		if(type == TYPE_REQUEST_CERTIFICATION) {
			return R.drawable.demand_back_btn;
		} else {
			return R.drawable.registration_back_btn;
		}
	}

	@Override
	public int getBackButtonWidth() {

		if(type == TYPE_REQUEST_CERTIFICATION) {
			return 208;
		} else {
			return 220;
		}
	}

	@Override
	public int getBackButtonHeight() {

		return 60;
	}

	@Override
	public boolean parseJSON(JSONObject objJSON) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onResume() {
		super.onResume();
		
		if(relativeForOption.getChildCount() == 1) {
			addOptionButtons();
		}
		
		if(type == TYPE_EDIT && car == null) {

			if(id != 0) {
				downloadCarInfo(carType);
			} else if(car != null){
				setCarInfo();
			} else {
				closePage();
			}
		}
		
		setDealerInfo();
		checkBundle();
		checkProgress();		
	}
	
	@Override
	public int getRootViewResId() {

		return R.id.carRegistrationPage_mainLayout;
	}
	
//////////////////// Custom method.
	
	public void addOptionButtons() {

		int size = 30;
		optionViews = new View[size];
		checked = new boolean[size];
		
		RelativeLayout.LayoutParams rp = null;
		
		for(int i=0; i<size; i++) {
			
			optionViews[i] = new View(mContext);
			rp = new RelativeLayout.LayoutParams(
					ResizeUtils.getSpecificLength(160), 
					ResizeUtils.getSpecificLength(80));
			
			switch(i % 3) {
			
			case 0:
				rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				rp.leftMargin = ResizeUtils.getSpecificLength(35);
				
				if(i == 0) {
					rp.addRule(RelativeLayout.BELOW, 
							R.id.carRegistrationPage_tvOption);
				} else {
					rp.addRule(RelativeLayout.BELOW, 
							getResources().getIdentifier("carRegistrationPage_optionView" + (i - 2),	//i - 3 + 1, 윗줄 아이콘. 
									"id", "com.byecar.byecarplus"));
				}
				
				rp.topMargin = ResizeUtils.getSpecificLength(24);
				break;
			case 1:
				rp.addRule(RelativeLayout.ALIGN_TOP, 
						getResources().getIdentifier("carRegistrationPage_optionView" + i,				//i - 1 + 1. 왼쪽 아이콘.
								"id", "com.byecar.byecarplus"));
				rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
				break;
			case 2:
				rp.addRule(RelativeLayout.ALIGN_TOP, 
						getResources().getIdentifier("carRegistrationPage_optionView" + i,				//i - 1 + 1. 왼쪽 아이콘.
								"id", "com.byecar.byecarplus"));
				rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				rp.rightMargin = ResizeUtils.getSpecificLength(35);
				break;
			}
			
			optionViews[i].setLayoutParams(rp);
			optionViews[i].setId(getResources().getIdentifier("carRegistrationPage_optionView" + (i + 1), 
							"id", "com.byecar.byecarplus"));
			optionViews[i].setBackgroundResource(
					getResources().getIdentifier("detail_optioin" + (i + 1) + "_btn_a", 
							"drawable", "com.byecar.byecarplus"));

			final int INDEX = i;
			optionViews[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					checked[INDEX] = !checked[INDEX];
					
					if(checked[INDEX]) {
						optionViews[INDEX].setBackgroundResource(
								getResources().getIdentifier("detail_optioin" + (INDEX + 1) + "_btn_b", 
										"drawable", "com.byecar.byecarplus"));
					} else {
						optionViews[INDEX].setBackgroundResource(
								getResources().getIdentifier("detail_optioin" + (INDEX + 1) + "_btn_a", 
										"drawable", "com.byecar.byecarplus"));
					}
				}
			});
			
			relativeForOption.addView(optionViews[i]);
		}
	}
	
	public void downloadCarInfo(int carType) {
		
		//다운로드 완료 후 setCarInfo, downloadCarDetailModelInfo 호출.
		String url = null;
		
		switch(carType) {
		
		case Car.TYPE_BID:
			url = BCPAPIs.CAR_BID_SHOW_URL;
			break;
			
		case Car.TYPE_DEALER:
			url = BCPAPIs.CAR_DEALER_SHOW_URL;
			break;
			
		case Car.TYPE_DIRECT_CERTIFIED:
			url = BCPAPIs.CAR_DIRECT_CERTIFIED_SHOW_URL;
			break;
			
		case Car.TYPE_DIRECT_NORMAL:
			url = BCPAPIs.CAR_DIRECT_NORMAL_SHOW_URL;
			break;
		}
		
		if(url != null) {
			url += "?onsalecar_id=" + id;
		}
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("CarRegistrationPage.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("CarRegistrationPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					car = new Car(objJSON.getJSONObject("onsalecar"));
					setCarInfo();
					downloadCarDetailModelInfo();
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}

	public void downloadCarDetailModelInfo() {
		
		String url = null;
		
		if(type == TYPE_EDIT) {

			if(car != null) {
				url = BCPAPIs.SEARCH_CAR_DETAIL_INFO
						+ "?car_id=" + car.getCar_id();
			}
		} else {
			url = BCPAPIs.SEARCH_CAR_DETAIL_INFO
					+ "?trim_id=" + mActivity.bundle.getInt("trim_id");
		}
		
		DownloadUtils.downloadJSONString(url,
			new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("CarRegistrationPage.onError." + "\nurl : " + url);

				}

				@Override
				public void onCompleted(String url,
						JSONObject objJSON) {

					try {
						LogUtils.log("CarRegistrationPage.onCompleted."
								+ "\nurl : " + url
								+ "\nresult : " + objJSON);

						carModelDetailInfo = new CarModelDetailInfo(objJSON.getJSONObject("car"));
						
						if(car != null) {
							//연식.
							carInfoStrings[0] = "" + car.getYear();
							
							//연료.
							carInfoStrings[1] = Car.getFuelTypeString(mContext, car.getFuel_type()); 

							//변속기.
							carInfoStrings[2] = Car.getTransmissionTypeString(mContext, car.getTransmission_type());
							
							//사고유무.
							carInfoStrings[3] = Car.getAccidentTypeString(mContext, car.getHad_accident());
							
							//1인신조.
							carInfoStrings[4] = Car.getOneManOwnedTypeString(mContext, car.getIs_oneman_owned());
						}
						
						setBtnCarInfos();
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
					}
				}
			});
	}
	
	public void setCarInfo() {
		
		if(car != null) {
			
			//차량 메인 사진.
			if(car.getM_images() != null 
					&& car.getM_images().length > 0) {

				int size = car.getM_images().length;
				for(int i=0; i<size; i++) {
					final ImageView target = ivPhotos[i];

					String url = car.getM_images()[i];
					target.setTag(url);
					DownloadUtils.downloadBitmap(url,
							new OnBitmapDownloadListener() {

								@Override
								public void onError(String url) {

									LogUtils.log("CarRegistrationPage.onError." + "\nurl : "
											+ url);

									// TODO Auto-generated method stub		
								}

								@Override
								public void onCompleted(String url,
										Bitmap bitmap) {

									try {
										LogUtils.log("CarRegistrationPage.onCompleted."
												+ "\nurl : " + url);

										if(bitmap != null && !bitmap.isRecycled()) {
											target.setImageBitmap(bitmap);
										}
									} catch (Exception e) {
										LogUtils.trace(e);
									} catch (OutOfMemoryError oom) {
										LogUtils.trace(oom);
									}
								}
							});
					selectedImageSdCardPaths[i] = url;
				}
			}

			//차량 추가 사진.
			if(car.getA_images() != null
					&& car.getA_images().length > 0) {
				int size = car.getA_images().length;
				for(int i=0; i<size; i++) {
					final ImageView target = ivPhotos[4 + i];
					String url = car.getA_images()[i];
					target.setTag(url);
					DownloadUtils.downloadBitmap(car.getA_images()[i],
							new OnBitmapDownloadListener() {

								@Override
								public void onError(String url) {

									LogUtils.log("CarRegistrationPage.onError." + "\nurl : "
											+ url);

									// TODO Auto-generated method stub		
								}

								@Override
								public void onCompleted(String url,
										Bitmap bitmap) {

									try {
										LogUtils.log("CarRegistrationPage.onCompleted."
												+ "\nurl : " + url);

										if(bitmap != null && !bitmap.isRecycled()) {
											target.setImageBitmap(bitmap);
										}
									} catch (Exception e) {
										LogUtils.trace(e);
									} catch (OutOfMemoryError oom) {
										LogUtils.trace(oom);
									}
								}
							});
					selectedImageSdCardPaths[4 + i] = url;
				}
			}
			
			//차량번호.
			etDetailCarInfos[0].getEditText().setText(car.getCar_number());
			
			//차량색상.
			etDetailCarInfos[1].getEditText().setText(car.getColor());
			
			//거래 가능 지역.
			etDetailCarInfos[2].getEditText().setText(car.getArea());
			
			//주행거리.
			etDetailCarInfos[3].getEditText().setText("" + car.getMileage());
			
			//배기량.
			etDetailCarInfos[4].getEditText().setText("" + car.getDisplacement());
			
			//차량 옵션 및 튜닝.
			if(car.getOptions() != null) {
				
				int size = car.getOptions().length;
				for(int i=0; i<size; i++) {
					checked[car.getOptions()[i] - 1] = true;
					optionViews[car.getOptions()[i] - 1].setBackgroundResource(
							getResources().getIdentifier("detail_optioin" + car.getOptions()[i] + "_btn_b", 
									"drawable", "com.byecar.byecarplus"));
				}
			}
			
			//판매자 차량설명.
			etCarDescriptionFromDealer.setText(car.getDesc());
			
			//즉시 판매.
			isImmediatlySaleClicked = car.getTo_sell_directly() == 1;
			
			if(isImmediatlySaleClicked) {
				immediatlySale.setBackgroundResource(R.drawable.registration_direct_btn_b);
			} else {
				immediatlySale.setBackgroundResource(R.drawable.registration_direct_btn_a);
			}
			
			//약관 동의.
			isTermOfUseClicked = true;
			termOfUse.setBackgroundResource(R.drawable.registration_agree_btn_b);
		}
	}
	
	public void setProgress(int progressValue) {

		progressValue = Math.min(progressValue, 100);
		progressValue = Math.max(progressValue, 0);

		if(progressValue == 100) {
			tvPercentage.getLayoutParams().width = ResizeUtils.getSpecificLength(110);
		} else {
			tvPercentage.getLayoutParams().width = ResizeUtils.getSpecificLength(95);
		}
		
		tvPercentage.invalidate();
		
		progressBar.setProgress(progressValue);
		tvPercentage.setText(progressValue + "%");
	}
	
	public void setDealerInfo() {

		if(!StringUtils.isEmpty(MainActivity.user.getPhone_number())) {
			tvDealerInfo.setTextColor(getResources().getColor(R.color.color_green));
			tvDealerInfo.setText(MainActivity.user.getName()
					+ "\n" + MainActivity.user.getPhone_number()
					+ "\n" + MainActivity.user.getAddress());
			tvDealerInfo.setBackgroundResource(R.drawable.registration_box2);
			tvDealerInfoCertified.setVisibility(View.VISIBLE);
		} else {
			tvDealerInfo.setTextColor(getResources().getColor(R.color.color_red));
			tvDealerInfo.setText(R.string.requireCertifyDealerInfo);
			tvDealerInfo.setBackgroundResource(R.drawable.registration_box1);
			tvDealerInfoCertified.setVisibility(View.INVISIBLE);
		}
	}

	public void checkBundle() {
		
		if(mActivity.bundle != null) {
			int type = mActivity.bundle.getInt("type");
			
			if(type == TypeSearchCarPage.TYPE_TRIM) {
				carInfoStrings[0] = null;
				btnCarInfos[0].setText(null);
				downloadCarDetailModelInfo();
			} else {
				carInfoStrings[type - 5] = mActivity.bundle.getString("text");
				setBtnCarInfos();
			}
			
			mActivity.bundle = null;
		}
	}

	public void setBtnCarInfos() {
		
		if(carModelDetailInfo != null) {
			btnCarInfos[0].setBackgroundResource(R.drawable.registration_car_info_box);
			btnCarInfos[0].setText(carModelDetailInfo.getFull_name());

			for(int i=0; i<5; i++) {
				
				if(!StringUtils.isEmpty(carInfoStrings[i])) {
					btnCarInfos[i + 1].setText(carInfoStrings[i]);
					btnCarInfos[i + 1].setBackgroundResource(R.drawable.registration_car_info_box);
				} else {
					int resId = getResources().getIdentifier("registration_car_info" + (i + 2) + "_btn", "drawable", "com.byecar.byecarplus");
					btnCarInfos[i + 1].setBackgroundResource(resId);
					btnCarInfos[i + 1].setText(null);
				}
			}
		} else {
			btnCarInfos[0].setBackgroundResource(R.drawable.registration_car_info1_btn);
			btnCarInfos[0].setText(null);
		}
		
		checkProgress();
	}

	public void setImageViewsOnClickListener() {
		
		int size = btnPhotos.length;
		for(int i=0; i<size; i++) {
			
			final int INDEX = i;
			btnPhotos[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					
					selectedImageIndex = INDEX;
					mActivity.showUploadPhotoPopup(1, Color.rgb(254, 188, 42));
				}
			});
		}
	}

	public void register() {

		try {
			String url = null;
			
			switch(carType) {
				
			case Car.TYPE_BID:
				url = BCPAPIs.CAR_BID_SAVE_URL;
				break;
				
			case Car.TYPE_DEALER:
				url = BCPAPIs.CAR_DEALER_SAVE_URL;
				break;
				
			case Car.TYPE_DIRECT_NORMAL:
				url = BCPAPIs.CAR_DIRECT_NORMAL_SAVE_URL;
				break;
			}
			
			if(url != null) {

				StringBuilder sb = new StringBuilder("");
				
//				onsalecar[id] : 수정 시에만 기재
				if(car != null) {
					sb.append("?onsalecar[id]=" + car.getId());
					sb.append("&");
				} else {
					sb.append("?");
				}
				
				//onsalecar[car_id] : 차량 ID (브랜드, 모델, 트림 선택으로 나온 car_id)
				sb.append("onsalecar[car_id]=").append(carModelDetailInfo.getId());
				
				//onsalecar[year] : 연식
				sb.append("&onsalecar[year]=").append(carInfoStrings[0]);
				
				//onsalecar[fuel_type] : 연료 타입 (gasoline/diesel/lpg)
				sb.append("&onsalecar[fuel_type]=").append(Car.getFuelOriginString(mContext, carInfoStrings[1]));
				
				//onsalecar[transmission_type] : 기어 타입 (auto/manual)
				sb.append("&onsalecar[transmission_type]=").append(Car.getTransmissionOriginString(mContext, carInfoStrings[2]));
				
				//onsalecar[had_accident] : 사고유무 (0: 사고여부 모름 / 1: 유사고 / 2: 무사고)
				sb.append("&onsalecar[had_accident]=").append(Car.getAccidentOriginString(mContext, carInfoStrings[3]));
				
				//onsalecar[is_oneman_owned] : 1인신조(1: 1인신조차량 / 0: 1인신조 아님)
				sb.append("&onsalecar[is_oneman_owned]=").append(Car.getOneManOwnedOriginString(mContext, carInfoStrings[4]));
				
				//onsalecar[car_number] : 차량넘버
				sb.append("&onsalecar[car_number]=").append(StringUtils.getUrlEncodedString(etDetailCarInfos[0].getEditText()));
				
				//onsalecar[color] : 색상
				sb.append("&onsalecar[color]=").append(StringUtils.getUrlEncodedString(etDetailCarInfos[1].getEditText()));
				
				//onsalecar[area] : 판매지역
				sb.append("&onsalecar[area]=").append(StringUtils.getUrlEncodedString(etDetailCarInfos[2].getEditText()));
				
				//onsalecar[mileage] : 주행거리
				sb.append("&onsalecar[mileage]=").append(StringUtils.getUrlEncodedString(etDetailCarInfos[3].getEditText()));
				
				//onsalecar[displacement] : 배기량
				sb.append("&onsalecar[displacement]=").append(StringUtils.getUrlEncodedString(etDetailCarInfos[4].getEditText()));
				
				//onsalecar[options][option_id] : 옵션 아이디를 키로 하는 객체
				//&onsalecar[options][1]=1
				//&onsalecar[options][2]=1
				
				int size = checked.length;
				for(int i=0; i<size; i++) {
					
					if(checked[i]) {
						sb.append("&onsalecar[options][")
								.append((i+1))
								.append("]=1");
					}
				}
				
				//onsalecar[desc] : 기타 설명
				sb.append("&onsalecar[desc]=").append(StringUtils.getUrlEncodedString(etCarDescriptionFromDealer));
				
				//onsalecar[to_sell_directly] : 즉시판매 (1: 즉시판매 / 즉시판매 아님)
				sb.append("&onsalecar[to_sell_directly]=").append(isImmediatlySaleClicked? "1" : "0");
				
				//onsalecar[m_images][] : 필수 사진 배열(순서대로 앞측면/뒷측면/시트변속기네비/주행거리)
//				&onsalecar[m_images][0]=url1
//				&onsalecar[m_images][1]=url2
				for(int i=0; i<4; i++) {
					sb.append("&onsalecar[m_images][")
							.append(i)
							.append("]=")
							.append(selectedImageSdCardPaths[i]);
				}
				
				//onsalecar[a_images][] : 추가 사진 배열 (최대 4장, 순서 무관)
				for(int i=0; i<4; i++) {
					if(selectedImageSdCardPaths[i+4] != null) {
						sb.append("&onsalecar[a_images][")
						.append(i)
						.append("]=")
						.append(selectedImageSdCardPaths[i+4]);
					}
				}
				
				url += sb.toString();
				DownloadUtils.downloadJSONString(url,
						new OnJSONDownloadListener() {

							@Override
							public void onError(String url) {

								LogUtils.log("CarRegistrationPage.register.onError." + "\nurl : " + url);
							}

							@Override
							public void onCompleted(String url,
									JSONObject objJSON) {

								try {
									LogUtils.log("CarRegistrationPage.register.onCompleted."
											+ "\nurl : " + url + "\nresult : "
											+ objJSON);

									if(objJSON.getInt("result") == 1) {
										pageRequestCompleted();
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
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	public void requestCertification() {
		
		try {
			StringBuilder sb = new StringBuilder(BCPAPIs.CAR_DIRECT_CERTIFIED_SAVE_URL);
			
			//onsalecar[car_id] : 차량 ID (브랜드, 모델, 트림 선택으로 나온 car_id)
			sb.append("?onsalecar[car_id]=").append(carModelDetailInfo.getId());
			
			//onsalecar[year] : 연식
			sb.append("&onsalecar[year]=").append(carInfoStrings[0]);
			
			//onsalecar[car_number] : 차량넘버
			sb.append("&onsalecar[car_number]=").append(StringUtils.getUrlEncodedString(etDetailCarInfos[0].getEditText()));
			
			//onsalecar[area] : 판매지역
			sb.append("&onsalecar[area]=").append(StringUtils.getUrlEncodedString(etDetailCarInfos[2].getEditText()));
			
			String url = sb.toString();
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("CarRegistrationPage.onError." + "\nurl : " + url);

				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("CarRegistrationPage.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						if(objJSON.getInt("result") == 1) {
							pageRequestCompleted();
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
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void pageRequestCompleted() {
		
		switch(type) {
		
		case TYPE_REGISTRATION:
			((MainActivity) mActivity).showPopup(MainActivity.POPUP_REGISTRATION);
			break;
			
		case TYPE_EDIT:
			mActivity.showAlertDialog(R.string.edit, R.string.complete_edit, R.string.confirm, 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							mActivity.closeTopPage();
						}
					});
			break;
			
		case TYPE_REQUEST_CERTIFICATION:
			((MainActivity) mActivity).showPopup(MainActivity.POPUP_REQUEST_CERTIFICATION);
			break;
		}
	}

	public void uploadImages() {
		
		int size = selectedImageSdCardPaths.length;
		
		for(int i=0; i<size; i++) {
			
			if(selectedImageSdCardPaths[i] != null
					&& !selectedImageSdCardPaths[i].contains("http://")) {
				
				final int INDEX = i;
				
				OnAfterUploadImage oaui = new OnAfterUploadImage() {
					
					@Override
					public void onAfterUploadImage(String resultString) {

						try {
							selectedImageSdCardPaths[INDEX] = new JSONObject(resultString).getJSONObject("file").getString("url");
						} catch (Exception e) {
							LogUtils.trace(e);
						} catch (Error e) {
							LogUtils.trace(e);
						}
						
						//Recursive call.
						uploadImages();
					}
				};
				
				ImageUploadUtils.uploadImage(BCPAPIs.UPLOAD_URL, oaui, selectedImageSdCardPaths[i]);
				return;
			}
		}
		
		//모든 이미지 업로드 완료.
		register();
	}
	
	public void checkProgress() {
		
		int progress = 0;
		
		LogUtils.log("###CarRegistrationPage.checkProgress.  ########################################");
		
		if(type == TYPE_EDIT|| type == TYPE_REGISTRATION) {
			
			//판매자 정보 인증 10.
			if(tvDealerInfoCertified.getVisibility() == View.VISIBLE) {
				progress += 10;
				LogUtils.log("###CarRegistrationPage.checkProgress.  add 10 from certification.");
			}
			
			//차량 사진 개당 5, 총 20.
			for(int i=0; i<4; i++) {
				if(selectedImageSdCardPaths[i] != null) {
					progress += 5;
					
					LogUtils.log("###CarRegistrationPage.checkProgress.  add 5 from photo.");
				}
			}
			
			//가격 입력 10.
			if(etPrice.length() > 0) {
				progress += 10;
				LogUtils.log("###CarRegistrationPage.checkProgress.  add 10 from price.");
			}
			
			//차량검색 5, 나머지 검색 개당 3, 총 20.
			for(int i=0; i<6; i++) {
				
				if(btnCarInfos[i].length() > 0) {
					progress += i==0? 5 : 3;
					
					if(i == 0) {
						LogUtils.log("###CarRegistrationPage.checkProgress.  add 5 from search.");
					} else {
						LogUtils.log("###CarRegistrationPage.checkProgress.  add 3 from search.");
					}
				}
			}
			
			//세부차량 정보 개당 4, 총 20.
			for(int i=0; i<5; i++) {
				if(etDetailCarInfos[i].getEditText().length() > 0) {
					progress += 4;
					LogUtils.log("###CarRegistrationPage.checkProgress.  add 4 from detailInfo.");
				}
			}
			
			//판매자 차량설명 20.
			if(etCarDescriptionFromDealer.length() > MIN_DESC_COUNT) {
				progress += 20;
				LogUtils.log("###CarRegistrationPage.checkProgress.  add 20 from desc.");
			}
		} else {

			//판매자 정보 인증 30.
			if(tvDealerInfoCertified.getVisibility() == View.VISIBLE) {
				progress += 30;
			}
			
			//차량검색 개당 20, 총 40.
			if(btnCarInfos[0].length() > 0) {
				progress += 20;
			}
			
			if(btnCarInfos[1].length() > 0) {
				progress += 20;
			}
			
			//세부차량 정보 개당 15, 총 30.
			if(etDetailCarInfos[0].getEditText().length() > 0) {
				progress += 15;
			}
			
			if(etDetailCarInfos[2].getEditText().length() > 0) {
				progress += 15;
			}
		}
		
		setProgress(progress);
	}

	public void closePage() {
		
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				ToastUtils.showToast(R.string.failToLoadCarInfo);
				mActivity.closeTopPage();
			}
		}, 1000);
	}
}

