package com.byecar.fragments;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.json.JSONObject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.MainActivity;
import com.byecar.byecarplus.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPDownloadUtils;
import com.byecar.classes.BCPFragment;
import com.byecar.models.Car;
import com.byecar.models.CarModelDetailInfo;
import com.byecar.views.TitleBar;
import com.outspoken_kid.activities.BaseFragmentActivity;
import com.outspoken_kid.activities.MultiSelectGalleryActivity.OnAfterPickImageListener;
import com.outspoken_kid.utils.BitmapUtils;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ImageUploadUtils;
import com.outspoken_kid.utils.ImageUploadUtils.OnAfterUploadImage;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo.holo_light.HoloStyleEditText;

public class CarRegistrationPage extends BCPFragment {

	private static final int OPTION_VIEW_SIZE = 31;
	private static final int MAX_DESC_COUNT = 200;
	
	public static final int POPUP_IMMEDIATELY = 0;
	public static final int POPUP_ACCIDENT = 1;
	
	private boolean forDealer;
	private String packageName;
	private int carType;
	private Car car;
	private CarModelDetailInfo carModelDetailInfo;

	private TextView[] tvTitleTexts = new TextView[8];
	
	private ProgressBar progressBar;
	private TextView tvPercentage;
	private TextView tvPercentage2;
	
	private TextView tvWriteAllContents;
	
	private Button[] btnCarInfos;
	private Button[] btnFuels;
	private Button[] btnAccidents;
	private Button[] btnOwneds;
	private Button[] btnTransmissions;
	
	private HoloStyleEditText[] etDetailCarInfos;
	
	private TextView tvRightPhoto;
	private Button[] btnPhotos;
	private ImageView[] ivPhotos;
	private TextView[] tvCarPhotos;
	
	private TextView tvAddedPhoto;
	
	private Button btnCheck;
	
	private RelativeLayout relativeForOption;
	private View[] optionViews = new View[OPTION_VIEW_SIZE];
	
	private EditText etCarDescription;
	private TextView tvTextCount;
	
	private View termOfUseDescView;
	private View termOfUse;
	private Button btnTermOfUse;
	private View immediatlySale;
	private Button btnImmediatlySale;
	private Button btnComplete;

	private View cover;
	private FrameLayout popup;
	private TextView tvTitle;
	private Button btnClose;
	private View clock;
	private TextView tvDesc;
	private EditText etAccident;
	private Button btnCompleteWriting;
	
	private boolean[] checked = new boolean[OPTION_VIEW_SIZE];
	private boolean isTermOfUseClicked;
	private boolean isImmediatlySaleClicked;
	private int dong_id;
	private String areaString;
	private int year;
	private int month;
	private String history;
	private int trim_id;
	private int selectedIndex_fuel;
	private int selectedIndex_accident;
	private int selectedIndex_owned;
	private int selectedIndex_transmission;
	
	private int selectedImageIndex;
	private String[] selectedImageSdCardPaths = new String[9];
	private Bitmap[] carPhotoThumbnails = new Bitmap[9];
	
	private boolean isRegistrationCompleted;
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		BaseFragmentActivity.onAfterPickImageListener = new OnAfterPickImageListener() {
			
			@Override
			public void onAfterPickImage(String[] sdCardPaths, Bitmap[] thumbnails) {
				
				if(thumbnails != null && thumbnails.length > 0) {
					carPhotoThumbnails[selectedImageIndex] = thumbnails[0];
					ivPhotos[selectedImageIndex].setImageBitmap(thumbnails[0]);
					selectedImageSdCardPaths[selectedImageIndex] = sdCardPaths[0];
				}
				
				checkProgress();
			}
		};
		
		//프래그먼트 복구.
		if(savedInstanceState != null) {
			
			try {
				//carType.
				carType = savedInstanceState.getInt("carType");
				
				//car.
				car = (Car) savedInstanceState.getSerializable("car");

				//carModelDetailInfo.
				carModelDetailInfo = (CarModelDetailInfo) savedInstanceState.getSerializable("carModelDetailInfo");
				
				//etDetailCarInfos.
				for(int i=0; i<etDetailCarInfos.length; i++) {
					
					if(savedInstanceState.containsKey("etDetailCarInfos" + i)) {
						etDetailCarInfos[i].getEditText().setText(savedInstanceState.getString("etDetailCarInfos" + i));
					}
				}
				
				//etCarDescription.
				if(savedInstanceState.containsKey("etCarDescription")) {
					etCarDescription.setText(savedInstanceState.getString("etCarDescription"));
				}
				
				//checked.
				for(int i=0; i<checked.length; i++) {
					checked[i] = savedInstanceState.getBoolean("checked" + i);
				}
				
				//isTermOfUseClicked.
				isTermOfUseClicked = savedInstanceState.getBoolean("isTermOfUseClicked");
				
				//isImmediatlySaleClicked.
				isImmediatlySaleClicked = savedInstanceState.getBoolean("isImmediatlySaleClicked");
				
				//dong_id.
				dong_id = savedInstanceState.getInt("dong_id");
				
				//areaString.
				areaString = savedInstanceState.getString("areaString");
				
				//year.
				year = savedInstanceState.getInt("year");
				
				//month.
				month = savedInstanceState.getInt("month");
				
				//history.
				history = savedInstanceState.getString("history");
				
				//trim_id.
				trim_id = savedInstanceState.getInt("trim_id");
				
				//selectedIndex_fuel.
				selectedIndex_fuel = savedInstanceState.getInt("selectedIndex_fuel");
				
				//selectedIndex_accident.
				selectedIndex_accident = savedInstanceState.getInt("selectedIndex_accident");
				
				//selectedIndex_owned.
				selectedIndex_owned = savedInstanceState.getInt("selectedIndex_owned");
				
				//selectedIndex_transmission.
				selectedIndex_transmission = savedInstanceState.getInt("selectedIndex_transmission");
				
				//selectedImageIndex.
				selectedImageIndex = savedInstanceState.getInt("selectedImageIndex");
				
				//selectedImageSdCardPaths.
				for(int i=0; i<selectedImageSdCardPaths.length; i++) {
				
					if(savedInstanceState.containsKey("selectedImageSdCardPaths" + i)) {
						selectedImageSdCardPaths[i] = savedInstanceState.getString("selectedImageSdCardPaths" + i);
					}
				}
				
				//carPhotoThumbnails.
				for(int i=0; i<carPhotoThumbnails.length; i++) {

					if(savedInstanceState.containsKey("carPhotoThumbnails" + i)) {
						carPhotoThumbnails[i] = savedInstanceState.getParcelable("carPhotoThumbnails" + i);
					}
					
					ivPhotos[i].setImageBitmap(carPhotoThumbnails[i]);
				}
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
			
		//새로 들어온 경우.
		} else {
			loadInfosFromPrefs();
		}
	};
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		try {
			//carType.
			outState.putInt("carType", carType);
			
			//car.
			outState.putSerializable("car", car);
			
			//carModelDetailInfo.
			outState.putSerializable("carModelDetailInfo", carModelDetailInfo);
			
			//etDetailCarInfos.
			for(int i=0; i<etDetailCarInfos.length; i++) {
				
				if(!StringUtils.isEmpty(etDetailCarInfos[i].getEditText())) {
					outState.putString("etDetailCarInfos" + i, etDetailCarInfos[i].getEditText().getText().toString());
				}
			}
			
			//etCarDescription.
			if(!StringUtils.isEmpty(etCarDescription)) {
				outState.putString("etCarDescription", etCarDescription.getText().toString());
			}
			
			//checked.
			for(int i=0; i<checked.length; i++) {
				outState.putBoolean("checked" + i, checked[i]);
			}
			
			//isTermOfUseClicked.
			outState.putBoolean("isTermOfUseClicked", isTermOfUseClicked);
			
			//isImmediatlySaleClicked.
			outState.putBoolean("isImmediatlySaleClicked", isImmediatlySaleClicked);
			
			//dong_id.
			outState.putInt("dong_id", dong_id);
			
			//areaString.
			outState.putString("areaString", areaString);
			
			//year.
			outState.putInt("year", year);
			
			//month.
			outState.putInt("month", month);
			
			//history.
			outState.putString("history", history);
			
			//trim_id.
			outState.putInt("trim_id", trim_id);

			//selectedIndex_fuel.
			outState.putInt("selectedIndex_fuel", selectedIndex_fuel);
			
			//selectedIndex_accident.
			outState.putInt("selectedIndex_accident", selectedIndex_accident);
			
			//selectedIndex_owned.
			outState.putInt("selectedIndex_owned", selectedIndex_owned);
			
			//selectedIndex_transmission.
			outState.putInt("selectedIndex_transmission", selectedIndex_transmission);
			
			//selectedImageIndex.
			outState.putInt("selectedImageIndex", selectedImageIndex);
			
			//selectedImageSdCardPaths.
			for(int i=0; i<selectedImageSdCardPaths.length; i++) {
				
				if(!StringUtils.isEmpty(selectedImageSdCardPaths[i])) {
					outState.putString("selectedImageSdCardPaths" + i, selectedImageSdCardPaths[i]);
				}
			}
			
			//carPhotoThumbnails.
			for(int i=0; i<carPhotoThumbnails.length; i++) {
			
				if(carPhotoThumbnails[i] != null) {
					outState.putParcelable("carPhotoThumbnails" + i, carPhotoThumbnails[i]);
				}
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	@Override
 	public void bindViews() {
		
		titleBar = (TitleBar) mThisView.findViewById(R.id.carRegistrationPage_titleBar);
		
		tvTitleTexts[0] = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvTitleText1);
		tvTitleTexts[1] = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvTitleText2);
		tvTitleTexts[2] = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvTitleText3);
		tvTitleTexts[3] = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvTitleText4);
		tvTitleTexts[4] = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvTitleText5);
		tvTitleTexts[5] = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvTitleText6);
		tvTitleTexts[6] = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvTitleText7);
		tvTitleTexts[7] = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvTitleText8);
		
		
		progressBar = (ProgressBar) mThisView.findViewById(R.id.carRegistrationPage_progressBar);
		tvPercentage = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvPercentage);
		tvPercentage2 = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvPercentage2);
		
		tvWriteAllContents = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvWriteAllContents);
		
		btnCarInfos = new Button[3];
		btnCarInfos[0] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarInfo1);
		btnCarInfos[1] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarInfo2);
		btnCarInfos[2] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarInfo3);
		
		btnFuels = new Button[4];
		btnFuels[0] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnFuel1);
		btnFuels[1] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnFuel2);
		btnFuels[2] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnFuel3);
		btnFuels[3] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnFuel4);
		
		btnAccidents = new Button[3];
		btnAccidents[0] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnAccident1);
		btnAccidents[1] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnAccident2);
		btnAccidents[2] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnAccident3);
		
		btnOwneds = new Button[2];
		btnOwneds[0] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnOwned1);
		btnOwneds[1] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnOwned2);
		
		btnTransmissions = new Button[2];
		btnTransmissions[0] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnTransmission1);
		btnTransmissions[1] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnTransmission2);

		etDetailCarInfos = new HoloStyleEditText[5];
		etDetailCarInfos[0] = (HoloStyleEditText) mThisView.findViewById(R.id.carRegistrationPage_etDetailCarInfo1);
		etDetailCarInfos[1] = (HoloStyleEditText) mThisView.findViewById(R.id.carRegistrationPage_etDetailCarInfo2);
		etDetailCarInfos[2] = (HoloStyleEditText) mThisView.findViewById(R.id.carRegistrationPage_etDetailCarInfo3);
		etDetailCarInfos[3] = (HoloStyleEditText) mThisView.findViewById(R.id.carRegistrationPage_etDetailCarInfo4);
		etDetailCarInfos[4] = (HoloStyleEditText) mThisView.findViewById(R.id.carRegistrationPage_etDetailCarInfo5);
		
		tvRightPhoto = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvRightPhoto);
		
		btnPhotos = new Button[8];
		btnPhotos[0] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarPhoto1);
		btnPhotos[1] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarPhoto2);
		btnPhotos[2] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarPhoto3);
		btnPhotos[3] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarPhoto4);
		btnPhotos[4] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnAddedPhoto1);
		btnPhotos[5] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnAddedPhoto2);
		btnPhotos[6] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnAddedPhoto3);
		btnPhotos[7] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnAddedPhoto4);

		ivPhotos = new ImageView[9];
		ivPhotos[0] = (ImageView) mThisView.findViewById(R.id.carRegistrationPage_ivCarPhoto1);
		ivPhotos[1] = (ImageView) mThisView.findViewById(R.id.carRegistrationPage_ivCarPhoto2);
		ivPhotos[2] = (ImageView) mThisView.findViewById(R.id.carRegistrationPage_ivCarPhoto3);
		ivPhotos[3] = (ImageView) mThisView.findViewById(R.id.carRegistrationPage_ivCarPhoto4);
		ivPhotos[4] = (ImageView) mThisView.findViewById(R.id.carRegistrationPage_ivAddedPhoto1);
		ivPhotos[5] = (ImageView) mThisView.findViewById(R.id.carRegistrationPage_ivAddedPhoto2);
		ivPhotos[6] = (ImageView) mThisView.findViewById(R.id.carRegistrationPage_ivAddedPhoto3);
		ivPhotos[7] = (ImageView) mThisView.findViewById(R.id.carRegistrationPage_ivAddedPhoto4);
		
		tvCarPhotos = new TextView[4];
		tvCarPhotos[0] = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvCarPhoto1);
		tvCarPhotos[1] = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvCarPhoto2);
		tvCarPhotos[2] = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvCarPhoto3);
		tvCarPhotos[3] = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvCarPhoto4);
		
		tvAddedPhoto = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvAddedPhoto);
		
		ivPhotos[8] = (ImageView) mThisView.findViewById(R.id.carRegistrationPage_ivCheck);
		btnCheck = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCheck);
		
		relativeForOption = (RelativeLayout) mThisView.findViewById(R.id.carRegistrationPage_relativeForOption);
		
		etCarDescription = (EditText) mThisView.findViewById(R.id.carRegistrationPage_etCarDescription);
		tvTextCount = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvTextCount);
		
		termOfUseDescView = mThisView.findViewById(R.id.carRegistrationPage_termOfUseDescView);
		
		termOfUse = mThisView.findViewById(R.id.carRegistrationPage_termOfUse);
		btnTermOfUse = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnTermOfUse);
		immediatlySale = mThisView.findViewById(R.id.carRegistrationPage_immediatlySale);
		btnImmediatlySale = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnImmediatlySale);
		btnComplete = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnComplete);
		
		cover = mThisView.findViewById(R.id.carRegistrationPage_cover);
		popup = (FrameLayout) mThisView.findViewById(R.id.carRegistrationPage_popup);
		tvTitle = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvTitle);
		btnClose = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnClose);
		clock = mThisView.findViewById(R.id.carRegistrationPage_clock);
		tvDesc = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvDesc);
		etAccident = (EditText) mThisView.findViewById(R.id.carRegistrationPage_etAccident);
		btnCompleteWriting = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCompleteWriting);
	}

	@Override
	public void setVariables() {
	
		if(getArguments() != null) {
			forDealer = getArguments().getBoolean("forDealer");
			carType = getArguments().getInt("carType");
			car = (Car) getArguments().getSerializable("car");
		}
		
		for(int i=0; i<selectedImageSdCardPaths.length; i++) {
			selectedImageSdCardPaths[i] = null;
		}
		
		if(forDealer) {
			packageName = "com.byecar.byecarplusfordealer";
		} else {
			packageName = "com.byecar.byecarplus";
		}
	}

	@Override
	public void createPage() {

		int size = etDetailCarInfos.length;
		for(int i=0; i<size; i++) {
			etDetailCarInfos[i].getEditText().setTextColor(getResources().getColor(R.color.new_color_text_gray));
			etDetailCarInfos[i].getEditText().setHintTextColor(getResources().getColor(R.color.new_color_text_gray));
			
			//차량 번호.
			//차량 색상.
			if(i == 0 || i == 3) {
				etDetailCarInfos[i].getEditText().setInputType(InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE);

			//주행거리.
			//배기량.
			//차량 가격.
			} else {
				etDetailCarInfos[i].getEditText().setInputType(InputType.TYPE_CLASS_PHONE);
			}
		}
		
		etDetailCarInfos[0].setHint(R.string.hintForDetailCarInfo1);
		etDetailCarInfos[1].setHint(R.string.hintForDetailCarInfo2);
		etDetailCarInfos[2].setHint(R.string.hintForDetailCarInfo3);
		etDetailCarInfos[3].setHint(R.string.hintForDetailCarInfo4);
		etDetailCarInfos[4].setHint(R.string.hintForDetailCarInfo5);
		
		if(!forDealer) {
			//색상 숨기기.
			etDetailCarInfos[3].setVisibility(View.GONE);
			
			//성능점검 기록부 부분 숨기기.
			tvTitleTexts[4].setVisibility(View.INVISIBLE);
			ivPhotos[8].setVisibility(View.INVISIBLE);
			btnCheck.setVisibility(View.INVISIBLE);
		}
		
		//즉시판매 관련 숨기기.
		if(carType != Car.TYPE_BID) {
			tvTitleTexts[7].setVisibility(View.GONE);
			termOfUseDescView.setVisibility(View.GONE);
			btnImmediatlySale.setVisibility(View.GONE);
			immediatlySale.setVisibility(View.GONE);
		}
		
		etCarDescription.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_DESC_COUNT)});
		tvTextCount.setText("0 / " + MAX_DESC_COUNT + "자");
		
		if(carType == Car.TYPE_BID) {
			termOfUseDescView.setVisibility(View.VISIBLE);
			immediatlySale.setVisibility(View.VISIBLE);
			btnImmediatlySale.setVisibility(View.VISIBLE);
		} else {
			termOfUseDescView.setVisibility(View.GONE);
			immediatlySale.setVisibility(View.GONE);
			btnImmediatlySale.setVisibility(View.GONE);
		}
		
		//titles.
		for(int i=0; i<8; i++) {
			String title = getString(getResources().getIdentifier("carRegistrationTitle" + (i + 1), "string", packageName));
			
			/*
			 * 						경매		직거래	딜러
			 * 0	차량정보			1		1		1
			 * 1	세부차량정보		2		2		2
			 * 2	필수사진			3		3		3
			 * 3	추가사진			4		4		4
			 * 4	성능점검 기록부		(4)		(4)		5
			 * 5	옵션 및 튜닝		5		5		6
			 * 6	판매자 차량설명		6		6		7
			 * 7	바이카 공지사항...	7		(7)		(8)
			 * 
			 * 딜러매물이 아니고 index가 3보다 클 때만 1 줄이기.
			 */
			tvTitleTexts[i].setText((i>3&&!forDealer? i : i+1) + ". " + title);
		}
	}

	@Override
	public void setListeners() {

		setImageViewsOnClickListener();
		
		for(int i=0; i<btnCarInfos.length; i++) {
			final int INDEX = i;
			
			btnCarInfos[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					if(INDEX == btnCarInfos.length - 1) {
						mActivity.showPage(BCPConstants.PAGE_SEARCH_AREA, null);
						return;
					} else if(INDEX + 1 != TypeSearchCarPage.TYPE_BRAND
							&& carModelDetailInfo == null) {
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
					}
					
					mActivity.showPage(BCPConstants.PAGE_TYPE_SEARCH_CAR, bundle);
				}
			});
		}
		
		for(int i=0; i<btnFuels.length; i++) {
			final int INDEX = i;
			
			btnFuels[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					
					selectedIndex_fuel = INDEX;
					setCarInfos_fuel();
				}
			});
		}
		
		for(int i=0; i<btnAccidents.length; i++) {
			final int INDEX = i;
			
			btnAccidents[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					
					//유사고.
					if(INDEX == 1) {
						showPopup(POPUP_ACCIDENT);
					} else {
						history = null;
						selectedIndex_accident = INDEX;
						setCarInfos_Accident();
					}
				}
			});
		}
		
		for(int i=0; i<btnOwneds.length; i++) {
			final int INDEX = i;
			
			btnOwneds[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					
					selectedIndex_owned = INDEX;
					setCarInfos_Owned();
				}
			});
		}
		
		for(int i=0; i<btnTransmissions.length; i++) {
			final int INDEX = i;
			
			btnTransmissions[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					
					selectedIndex_transmission = INDEX;
					setCarInfos_Transmission();
				}
			});
		}
		
		etCarDescription.addTextChangedListener(new TextWatcher() {
			
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
				
				checkProgress();
			}
		});
	
		btnTermOfUse.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(BCPConstants.PAGE_TERM_OF_USE, null);
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
	
		btnImmediatlySale.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				showPopup(POPUP_IMMEDIATELY);
			}
		});
		
		btnCompleteWriting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(etAccident.length() == 0) {
					ToastUtils.showToast(R.string.writeCarPartChangedHistory);
					return;
				}

				selectedIndex_accident = 1;
				setCarInfos_Accident();
				history = etAccident.getText().toString();
				
				hidePopup();
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
					
				} else if(StringUtils.isEmpty(MainActivity.user.getPhone_number())) {
					ToastUtils.showToast("휴대폰 번호 인증이 필요합니다.");
					mActivity.showPage(BCPConstants.PAGE_CERTIFY_PHONE_NUMBER, null);
					
				} else {
					SoftKeyboardUtils.hideKeyboard(mContext, etCarDescription);
					uploadImages();
				}
			}
		});
	
		for(int i=0; i<etDetailCarInfos.length; i++) {
			
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
		
		cover.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				hidePopup();
			}
		});
		
		popup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				//Do nothing.
			}
		});
		
		btnClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				hidePopup();
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;

		//progressBar.
		rp = (RelativeLayout.LayoutParams) progressBar.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(30);
		
		//tvPercentage.
		rp = (RelativeLayout.LayoutParams) tvPercentage.getLayoutParams();
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
		
		//tvWriteAllContents.
		rp = (RelativeLayout.LayoutParams) tvWriteAllContents.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(120);
		
		int titlePadding = ResizeUtils.getSpecificLength(20);
		
		for(int i=0; i<tvTitleTexts.length; i++) {
		
			rp = (RelativeLayout.LayoutParams) tvTitleTexts[i].getLayoutParams();
			rp.width = LayoutParams.MATCH_PARENT;
			rp.height = ResizeUtils.getSpecificLength(40);
			tvTitleTexts[i].setPadding(titlePadding, 0, 0, 0);
			
			switch(i) {
			case 0:
				//No top margin.
				break;
				
			case 1:
				rp.topMargin = ResizeUtils.getSpecificLength(72);
				break;
				
			case 2:
				rp.topMargin = ResizeUtils.getSpecificLength(50);
				break;
				
			case 3:
				rp.topMargin = ResizeUtils.getSpecificLength(270);
				break;
				
			case 4:
				rp.topMargin = ResizeUtils.getSpecificLength(40);
				break;
				
			case 5:

				if(forDealer) {
					rp.topMargin = ResizeUtils.getSpecificLength(640);
				} else {
					rp.topMargin = ResizeUtils.getSpecificLength(40);
				}
				break;
				
			case 6:
				rp.topMargin = ResizeUtils.getSpecificLength(50);
				break;
				
			case 7:
				rp.topMargin = ResizeUtils.getSpecificLength(80);
				break;
			}
		}
		
		//btnCarInfos.
		for(int i=0; i<btnCarInfos.length; i++) {
			rp = (RelativeLayout.LayoutParams) btnCarInfos[i].getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(586);
			rp.height = ResizeUtils.getSpecificLength(82);
			rp.topMargin = ResizeUtils.getSpecificLength(38);
		}
		
		//btnFuels.
		for(int i=0; i<btnFuels.length; i++) {
			rp = (RelativeLayout.LayoutParams) btnFuels[i].getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(142);
			rp.height = ResizeUtils.getSpecificLength(84);
			
			if(i == 0) {
				rp.leftMargin = ResizeUtils.getSpecificLength(25);
				rp.topMargin = ResizeUtils.getSpecificLength(42);
			} else {
				rp.leftMargin = ResizeUtils.getSpecificLength(7);
			}
		}
		
		//btnAccidents.
		for(int i=0; i<btnAccidents.length; i++) {
			rp = (RelativeLayout.LayoutParams) btnAccidents[i].getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(191);
			rp.height = ResizeUtils.getSpecificLength(84);
			
			if(i == 0) {
				rp.leftMargin = ResizeUtils.getSpecificLength(25);
				rp.topMargin = ResizeUtils.getSpecificLength(42);
			} else {
				rp.leftMargin = ResizeUtils.getSpecificLength(7);
			}
		}
		
		//btnOwneds.
		for(int i=0; i<btnOwneds.length; i++) {
			rp = (RelativeLayout.LayoutParams) btnOwneds[i].getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(290);
			rp.height = ResizeUtils.getSpecificLength(84);
			
			if(i == 0) {
				rp.leftMargin = ResizeUtils.getSpecificLength(25);
				rp.topMargin = ResizeUtils.getSpecificLength(42);
			} else {
				rp.leftMargin = ResizeUtils.getSpecificLength(7);
			}
		}
		
		//btnTransmissions.
		for(int i=0; i<btnTransmissions.length; i++) {
			rp = (RelativeLayout.LayoutParams) btnTransmissions[i].getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(290);
			rp.height = ResizeUtils.getSpecificLength(84);
			
			if(i == 0) {
				rp.leftMargin = ResizeUtils.getSpecificLength(25);
				rp.topMargin = ResizeUtils.getSpecificLength(42);
			} else {
				rp.leftMargin = ResizeUtils.getSpecificLength(7);
			}
		}

		//etDetailCarInfos
		for(int i=0; i<etDetailCarInfos.length; i++) {
			rp = (RelativeLayout.LayoutParams) etDetailCarInfos[i].getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(586);
			rp.height = ResizeUtils.getSpecificLength(60);
			rp.topMargin = ResizeUtils.getSpecificLength(20);
		}
		
		//tvRightPhoto.
		rp = (RelativeLayout.LayoutParams) tvRightPhoto.getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(210);
		
		//btnCarPhoto1.
		for(int i=0; i<btnPhotos.length; i++) {
			rp = (RelativeLayout.LayoutParams) btnPhotos[i].getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(144);
			rp.height = ResizeUtils.getSpecificLength(110);
			
			if(i == 0 || i == 4) {
				rp.leftMargin = ResizeUtils.getSpecificLength(20);
				rp.topMargin = ResizeUtils.getSpecificLength(34);
			} else {
				rp.leftMargin = ResizeUtils.getSpecificLength(10);
			}
		}
		
		//ivPhotos. 성능점검 기록부 제외.
		for(int i=0; i<ivPhotos.length - 1; i++) {
			rp = (RelativeLayout.LayoutParams) ivPhotos[i].getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(144);
			rp.height = ResizeUtils.getSpecificLength(110);
			
			if(i == 0 || i == 4) {
				rp.leftMargin = ResizeUtils.getSpecificLength(20);
				rp.topMargin = ResizeUtils.getSpecificLength(34);
			} else {
				rp.leftMargin = ResizeUtils.getSpecificLength(10);
			}
		}
		
		//tvCarPhotos.
		for(int i=0; i<tvCarPhotos.length; i++) {
			rp = (RelativeLayout.LayoutParams) tvCarPhotos[i].getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(144);
			rp.topMargin = ResizeUtils.getSpecificLength(10);
		}
		
		//tvAddedPhoto.
		rp = (RelativeLayout.LayoutParams) tvAddedPhoto.getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(40);
		
		//ivPhotos[ivPhotos.length-1].
		rp = (RelativeLayout.LayoutParams) ivPhotos[ivPhotos.length-1].getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(546);
		rp.height = ResizeUtils.getSpecificLength(404);
		rp.topMargin = ResizeUtils.getSpecificLength(24);
		
		//btnCheck.
		rp = (RelativeLayout.LayoutParams) btnCheck.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(589);
		rp.height = ResizeUtils.getSpecificLength(85);
		rp.topMargin = ResizeUtils.getSpecificLength(24);
		
		//etCarDescription.
		rp = (RelativeLayout.LayoutParams) etCarDescription.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(160);
		rp.topMargin = ResizeUtils.getSpecificLength(33);
		
		//termOfUseDescView.
		rp = (RelativeLayout.LayoutParams) termOfUseDescView.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(594);
		rp.height = ResizeUtils.getSpecificLength(356);
		rp.topMargin = ResizeUtils.getSpecificLength(50);
		
		//termOfUse.
		rp = (RelativeLayout.LayoutParams) termOfUse.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(313);
		rp.height = ResizeUtils.getSpecificLength(28);
		rp.topMargin = ResizeUtils.getSpecificLength(50);
		
		//btnTermOfUse.
		rp = (RelativeLayout.LayoutParams) btnTermOfUse.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(307);
		rp.height = ResizeUtils.getSpecificLength(43);
		rp.topMargin = ResizeUtils.getSpecificLength(16);
		
		//immediatlySale.
		rp = (RelativeLayout.LayoutParams) immediatlySale.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(93);
		rp.height = ResizeUtils.getSpecificLength(24);
		rp.topMargin = ResizeUtils.getSpecificLength(34);
		
		//btnImmediatlySale.
		rp = (RelativeLayout.LayoutParams) btnImmediatlySale.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(307);
		rp.height = ResizeUtils.getSpecificLength(43);
		rp.topMargin = ResizeUtils.getSpecificLength(16);

		//btnComplete.
		rp = (RelativeLayout.LayoutParams) btnComplete.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(588);
		rp.height = ResizeUtils.getSpecificLength(83);
		rp.topMargin = ResizeUtils.getSpecificLength(50);

		//popup.
		rp = (RelativeLayout.LayoutParams) popup.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(562);
		rp.height = ResizeUtils.getSpecificLength(657);
		
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 88, tvTitle, 2, 0, new int[]{0, 4, 0, 0});
		ResizeUtils.viewResize(60, 60, btnClose, 2, Gravity.RIGHT, new int[]{0, 20, 11, 0});
		ResizeUtils.viewResize(171, 216, clock, 2, Gravity.CENTER_HORIZONTAL, 
				new int[]{0, 150, 0, 0});
		ResizeUtils.viewResize(470, 260, tvDesc, 2, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 
				new int[]{0, 20, 0, 0});
		tvDesc.setLineSpacing(0, 1.5f);
		ResizeUtils.viewResize(515, 420, etAccident, 2, Gravity.CENTER_HORIZONTAL, 
				new int[]{0, 112, 0, 0}, new int[]{14, 14, 14, 14});
		ResizeUtils.viewResize(514, 83, btnCompleteWriting, 2, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 
				new int[]{0, 0, 0, 23});
		
		for(int i=0; i<tvTitleTexts.length; i++) {
			FontUtils.setFontSize(tvTitleTexts[i], 24);
			FontUtils.setFontStyle(tvTitleTexts[i], FontUtils.BOLD);
		}
		
		FontUtils.setFontSize(tvPercentage, 24);
		FontUtils.setFontSize(tvPercentage2, 16);
		FontUtils.setFontSize(tvWriteAllContents, 18);
		FontUtils.setFontStyle(tvWriteAllContents, FontUtils.BOLD);
		
		
		
		for(int i=0; i<btnCarInfos.length; i++) {
			FontUtils.setFontSize(btnCarInfos[i], 26);
		}
		
		for(int i=0; i<etDetailCarInfos.length; i++) {
			FontUtils.setFontSize(etDetailCarInfos[i].getEditText(), 20);
		}
		
		FontUtils.setFontSize(tvRightPhoto, 20);
		FontUtils.setFontStyle(tvRightPhoto, FontUtils.BOLD);
		
		for(int i=0; i<tvCarPhotos.length; i++) {
			FontUtils.setFontSize(tvCarPhotos[i], 20);
		}
		
		FontUtils.setFontSize(tvAddedPhoto, 20);
		FontUtils.setFontStyle(tvAddedPhoto, FontUtils.BOLD);
		
		FontUtils.setFontAndHintSize(etCarDescription, 20, 20);
		FontUtils.setFontSize(tvTextCount, 20);
		
		FontUtils.setFontSize(tvTitle, 30);
		FontUtils.setFontSize(tvDesc, 22);
		FontUtils.setFontSize(etAccident, 24);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_common_car_registration;
	}

	@Override
	public int getPageTitleTextResId() {

		//차량 수정.
		if(car != null) {
			return R.string.pageTitle_carRegistration_edit;
			
		//경매 등록.
		} else if(carType == Car.TYPE_BID) {
			return R.string.pageTitle_carRegistration_registration_bid;
			
		//직거래 등록.
		} else if(carType == Car.TYPE_DIRECT) {
			return R.string.pageTitle_carRegistration_registration_direct;
			
		//딜러 중고차 등록.
		} else {
			return R.string.pageTitle_carRegistration_registration_dealer;
		}
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

		if(popup.getVisibility() == View.VISIBLE) {
			hidePopup();
			return true;
		}
		
		return false;
	}

	@Override
	public void onResume() {
		super.onResume();
		
		if(relativeForOption.getChildCount() == 0) {
			addOptionViews();
		}
		
		/*
		 * 트림 선택, 연식 선택, 지역 선택, 전화번호 인증한 경우.
		 */
		if(mActivity.bundle != null) {
			checkBundle();
		
		/*
		 * 페이지에 처음 진입한 경우 - 차량 수정.
		 * car를 갖고 있지만 carModelDetailInfo는 없기 때문에,
		 * 새로 불러와야 한다.
		 * 
		 * 페이지에 처음 진입한 경우 - 차량 등록.
		 * 이전 값을 다시 불러온 경우엔 carModelDetailInfo는 복구하지 못하고 trim_id만 복구하므로,
		 * 새로 불러와야 한다.
		 * 
		 * 차량 선택을 하지 않은 상태에서 나갔다 들어온 경우 trim_id가 없으므로,
		 * 나머지 값들 세팅. 
		 */
		} else if(carModelDetailInfo == null) {
			
			if(car != null) {
				loadInfosFromCar();
				downloadCarDetailModelInfo();
			} else if(trim_id != 0) {
				downloadCarDetailModelInfo();
			} else {
				setCarDetailInfos();
			}
			
		/*
		 * 프래그먼트 복구인 경우 carModelDetailInfo 또한 복구하기 때문에,
		 * trim_id가 0이 아닌데 carModelDetailInfo가 null인 경우는 없다.
		 * 그러므로 값만 다시 세팅.
		 */
		} else {
			setCarDetailInfos();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		
		saveInfosToPrefs();
	}
	
	@Override
	public int getRootViewResId() {

		return R.id.carRegistrationPage_mainLayout;
	}
	
//////////////////// Custom method.
	
	public void saveInfosToPrefs() {

		if(car != null || isRegistrationCompleted) {
			return;
		}
		
		try {
			SharedPreferences prefs = mContext.getSharedPreferences(BCPConstants.PREFS_REG + carType, 
					Context.MODE_PRIVATE);
			Editor ed = prefs.edit();
			
			//etDetailCarInfos.
			for(int i=0; i<etDetailCarInfos.length; i++) {
				ed.putString("etDetailCarInfos" + i, etDetailCarInfos[i].getEditText().getText().toString());
			}
			
			//etCarDescription.
			ed.putString("etCarDescription", etCarDescription.getText().toString());
			
			//checked.
			for(int i=0; i<checked.length; i++) {
				ed.putBoolean("checked" + i, checked[i]);
			}
			
			//isTermOfUseClicked.
			ed.putBoolean("isTermOfUseClicked", isTermOfUseClicked);
			
			//isImmediatlySaleClicked.
			ed.putBoolean("isImmediatlySaleClicked", isImmediatlySaleClicked);
			
			//dong_id.
			ed.putInt("dong_id", dong_id);
			
			//areaString.
			ed.putString("areaString", areaString);
			
			//year.
			ed.putInt("year", year);
			
			//month.
			ed.putInt("month", month);
			
			//history.
			ed.putString("history", history);
			
			//trim_id.
			ed.putInt("trim_id", trim_id);

			//selectedIndex_fuel.
			ed.putInt("selectedIndex_fuel", selectedIndex_fuel);
			
			//selectedIndex_accident.
			ed.putInt("selectedIndex_accident", selectedIndex_accident);
			
			//selectedIndex_owned.
			ed.putInt("selectedIndex_owned", selectedIndex_owned);
			
			//selectedIndex_transmission.
			ed.putInt("selectedIndex_transmission", selectedIndex_transmission);
			
			//selectedImageIndex.
			ed.putInt("selectedImageIndex", selectedImageIndex);
			
			//selectedImageSdCardPaths.
			for(int i=0; i<selectedImageSdCardPaths.length; i++) {
				ed.putString("selectedImageSdCardPaths" + i, selectedImageSdCardPaths[i]);
			}
			
			ed.commit();
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void loadInfosFromPrefs() {
		
		try {
			SharedPreferences prefs = mContext.getSharedPreferences(BCPConstants.PREFS_REG + carType, 
					Context.MODE_PRIVATE);
			
			//etDetailCarInfos.
			for(int i=0; i<etDetailCarInfos.length; i++) {
				etDetailCarInfos[i].getEditText().setText(prefs.getString("etDetailCarInfos" + i, null));
			}
			
			//etCarDescription.
			etCarDescription.setText(prefs.getString("etCarDescription", null));
			
			//checked.
			for(int i=0; i<checked.length; i++) {
				checked[i] = prefs.getBoolean("checked" + i, false);
			}
			
			//isTermOfUseClicked.
			isTermOfUseClicked = prefs.getBoolean("isTermOfUseClicked", false);
			
			if(isTermOfUseClicked) {
				termOfUse.setBackgroundResource(R.drawable.registration_agree_btn_b);
			} else {
				termOfUse.setBackgroundResource(R.drawable.registration_agree_btn_a);
			}
			
			//isImmediatlySaleClicked.
			isImmediatlySaleClicked = prefs.getBoolean("isImmediatlySaleClicked", false);
			
			if(isImmediatlySaleClicked) {
				immediatlySale.setBackgroundResource(R.drawable.registration_direct_btn_b);
			} else {
				immediatlySale.setBackgroundResource(R.drawable.registration_direct_btn_a);
			}
			
			//dong_id.
			dong_id = prefs.getInt("dong_id", 0);
			
			//areaString.
			areaString = prefs.getString("areaString", null);
			
			//year.
			year = prefs.getInt("year", 0);
			
			//month.
			month = prefs.getInt("month", 0);
			
			//history.
			history = prefs.getString("history", null);
			
			//trim_id.
			trim_id = prefs.getInt("trim_id", 0);

			//selectedIndex_fuel.
			selectedIndex_fuel = prefs.getInt("selectedIndex_fuel", 0);
			
			//selectedIndex_accident.
			selectedIndex_accident = prefs.getInt("selectedIndex_accident", 0);
			
			//selectedIndex_owned.
			selectedIndex_owned = prefs.getInt("selectedIndex_owned", 0);
			
			//selectedIndex_transmission.
			selectedIndex_transmission = prefs.getInt("selectedIndex_transmission", 0);
			
			//selectedImageIndex.
			selectedImageIndex = prefs.getInt("selectedImageIndex", selectedImageIndex);
			
			boolean isLoadingImages = false;
			
			//selectedImageSdCardPaths.
			for(int i=0; i<selectedImageSdCardPaths.length; i++) {
				selectedImageSdCardPaths[i] = prefs.getString("selectedImageSdCardPaths" + i, null);
				
				if(!StringUtils.isEmpty(selectedImageSdCardPaths[i])) {
					
					isLoadingImages = true;
					
					if(selectedImageSdCardPaths[i].contains("http://")) {
						final int INDEX = i;
						ivPhotos[i].setTag(selectedImageSdCardPaths[i]);
						BCPDownloadUtils.downloadBitmap(selectedImageSdCardPaths[i],
								new OnBitmapDownloadListener() {

									@Override
									public void onError(String url) {

										LogUtils.log("CarRegistrationPage.onError."
												+ "\nurl : " + url);
										// TODO Auto-generated method stub		
									}

									@Override
									public void onCompleted(String url,
											Bitmap bitmap) {

										try {
											LogUtils.log("CarRegistrationPage.onCompleted."
													+ "\nurl : " + url);

											ivPhotos[INDEX].setImageBitmap(bitmap);
										} catch (Exception e) {
											LogUtils.trace(e);
										} catch (OutOfMemoryError oom) {
											LogUtils.trace(oom);
										}
									}
								}, 144);
						
					} else {
						(new AsyncLoadThumbnail(selectedImageSdCardPaths[i], ivPhotos[i])).execute();
					}
				}
			}
			
			if(isLoadingImages) {
				ToastUtils.showToast(R.string.loadingImage);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void loadInfosFromCar() {

		//연식.
		year = car.getYear();
		month = car.getMonth();
		
		//지역.
		dong_id = car.getDong_id();
		areaString = car.getArea();
		
		//연료.
		selectedIndex_fuel = Car.getFuelIndex(car.getFuel_type());
		
		//사고유무.
		selectedIndex_accident = Car.getAccidentIndexToUser(car.getHad_accident());
		history = car.getAccident_desc();
		
		//1인신조.
		selectedIndex_owned = Car.getOwnedIndexToUser(car.getIs_oneman_owned());
		
		//변속기.
		selectedIndex_transmission = Car.getTransmissionIndex(car.getTransmission_type());
		
		//차량번호.
		etDetailCarInfos[0].getEditText().setText(car.getCar_number());
		
		//주행거리.
		etDetailCarInfos[1].getEditText().setText("" + car.getMileage());
		
		//배기량.
		etDetailCarInfos[2].getEditText().setText("" + car.getDisplacement());
		
		//색상.
		etDetailCarInfos[3].getEditText().setText("" + car.getColor());
		
		//희망금액.
		etDetailCarInfos[4].getEditText().setText("" + car.getPrice());

		//차량 메인 사진.
		if(car.getM_images() != null 
				&& car.getM_images().length > 0) {

			int size = car.getM_images().length;
			for(int i=0; i<size; i++) {
				final ImageView target = ivPhotos[i];

				String url = car.getM_images()[i];
				target.setTag(url);
				BCPDownloadUtils.downloadBitmap(url,
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
						}, 144);
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
				BCPDownloadUtils.downloadBitmap(car.getA_images()[i],
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
						}, 144);
				selectedImageSdCardPaths[4 + i] = url;
			}
		}
		
		//성능점검 기록부.
		if(!StringUtils.isEmpty(car.getInspection_note_url())) {
			selectedImageSdCardPaths[selectedImageSdCardPaths.length - 1] = car.getInspection_note_url();
			BCPDownloadUtils.downloadBitmap(car.getInspection_note_url(),
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
									ivPhotos[ivPhotos.length-1].setImageBitmap(bitmap);
								}
							} catch (Exception e) {
								LogUtils.trace(e);
							} catch (OutOfMemoryError oom) {
								LogUtils.trace(oom);
							}
						}
					}, 546);
		}
		
		//차량 옵션 및 튜닝.
		if(car.getOptions() != null) {
			
			int size = car.getOptions().length;
			for(int i=0; i<size; i++) {
				checked[car.getOptions()[i] - 1] = true;
				optionViews[car.getOptions()[i] - 1].setBackgroundResource(
						getResources().getIdentifier("detail_option" + car.getOptions()[i] + "_btn_b", 
								"drawable", packageName));
			}
		}

		//즉시 판매.
		isImmediatlySaleClicked = car.getTo_sell_directly() == 1; 
		
		if(isImmediatlySaleClicked) {
			immediatlySale.setBackgroundResource(R.drawable.registration_direct_btn_b);
		} else {
			immediatlySale.setBackgroundResource(R.drawable.registration_direct_btn_a);
		}
		
		//판매자 차량설명.
		etCarDescription.setText(car.getDesc());
		
		//약관 동의.
		isTermOfUseClicked = true;
		termOfUse.setBackgroundResource(R.drawable.registration_agree_btn_b);
	}
	
	public void addOptionViews() {
		
		relativeForOption.removeAllViews();
		
		RelativeLayout.LayoutParams rp = null;

		for(int i=0; i<OPTION_VIEW_SIZE; i++) {

		//Texts.
			if(i == 0) {
				relativeForOption.addView(getTitleViewForOption(0));
			} else if(i == 25) {
				relativeForOption.addView(getTitleViewForOption(1));
			}
			
		//Views.
			optionViews[i] = getViewForOption(i);
			
			final int INDEX = i;
			optionViews[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					checked[INDEX] = !checked[INDEX];
					
					if(checked[INDEX]) {
						optionViews[INDEX].setBackgroundResource(
								getResources().getIdentifier("detail_option" + (INDEX + 1) + "_btn_b", 
										"drawable", packageName));
					} else {
						optionViews[INDEX].setBackgroundResource(
								getResources().getIdentifier("detail_option" + (INDEX + 1) + "_btn_a", 
										"drawable", packageName));
					}
				}
			});
			
			relativeForOption.addView(optionViews[i]);
		}
		
		//Bottom blank.
		View blank = new View(mContext);
		rp = new RelativeLayout.LayoutParams(
				10, ResizeUtils.getSpecificLength(20));
		rp.addRule(RelativeLayout.BELOW, 
				getResources().getIdentifier("carRegistrationPage_optionView30", 
						"id", packageName));
		blank.setLayoutParams(rp);
		relativeForOption.addView(blank);
	}
	
	public View getTitleViewForOption(int index) {
		
		try {
			View titleView = new View(mContext);
			RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
					ResizeUtils.getSpecificLength(index==0?580:307), 
					ResizeUtils.getSpecificLength(43));
			
			switch(index) {
			
			case 0:
				rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				titleView.setId(getResources().getIdentifier(
						"carRegistrationPage_optionTitleView1", "id", packageName));
				titleView.setBackgroundResource(R.drawable.registration_option_category1);
				break;
				
			case 1:
				rp.addRule(RelativeLayout.BELOW, 
						getResources().getIdentifier("carRegistrationPage_optionView26", 
								"id", packageName));
				titleView.setId(getResources().getIdentifier(
						"carRegistrationPage_optionTitleView2", "id", packageName));
				titleView.setBackgroundResource(R.drawable.registration_option_category4);
				break;
			}
			
			rp.topMargin = ResizeUtils.getSpecificLength(64);
			rp.bottomMargin = ResizeUtils.getSpecificLength(50);
			rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
			titleView.setLayoutParams(rp);
			return titleView;
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		return null;
	}
	
	public View getViewForOption(int index) {
		
		try {
			View view = new View(mContext);
			RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
					ResizeUtils.getSpecificLength(160), 
					ResizeUtils.getSpecificLength(80));

			if(index < 24) {
				switch(index % 3) {
				
				case 0:
					rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					rp.leftMargin = ResizeUtils.getSpecificLength(35);
					
					if(index == 0) {
						rp.addRule(RelativeLayout.BELOW, 
								getResources().getIdentifier("carRegistrationPage_optionTitleView1", 
										"id", packageName));
					} else {
						rp.addRule(RelativeLayout.BELOW, 
								getResources().getIdentifier("carRegistrationPage_optionView" + (index - 2),	//i - 3 + 1, 윗줄 아이콘. 
										"id", packageName));
						rp.topMargin = ResizeUtils.getSpecificLength(24);
					}
					
					break;
				case 1:
					rp.addRule(RelativeLayout.ALIGN_TOP, 
							getResources().getIdentifier("carRegistrationPage_optionView" + index,				//i - 1 + 1. 왼쪽 아이콘.
									"id", packageName));
					rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
					break;
				case 2:
					rp.addRule(RelativeLayout.ALIGN_TOP, 
							getResources().getIdentifier("carRegistrationPage_optionView" + index,				//i - 1 + 1. 왼쪽 아이콘.
									"id", packageName));
					rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					rp.rightMargin = ResizeUtils.getSpecificLength(35);
					break;
				}
			} else if(index == 24) {
				rp.addRule(RelativeLayout.BELOW, 
						getResources().getIdentifier("carRegistrationPage_optionView24",
								"id", packageName));
				rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
				rp.topMargin = ResizeUtils.getSpecificLength(24);
				
			} else if(index == 25) {
				rp.addRule(RelativeLayout.ALIGN_TOP, 
						getResources().getIdentifier("carRegistrationPage_optionView25",
								"id", packageName));
				rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				rp.rightMargin = ResizeUtils.getSpecificLength(35);
				
			} else {
				switch(index % 3) {
				
				case 2:
					rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					rp.leftMargin = ResizeUtils.getSpecificLength(35);
					
					if(index == 26) {
						rp.addRule(RelativeLayout.BELOW, 
								getResources().getIdentifier("carRegistrationPage_optionTitleView2", 
										"id", packageName));
					} else {
						rp.addRule(RelativeLayout.BELOW, 
								getResources().getIdentifier("carRegistrationPage_optionView" + (index - 2),	//i - 3 + 1, 윗줄 아이콘. 
										"id", packageName));
						rp.topMargin = ResizeUtils.getSpecificLength(24);
					}
					break;
				case 0:
					rp.addRule(RelativeLayout.ALIGN_TOP, 
							getResources().getIdentifier("carRegistrationPage_optionView" + index,				//i - 1 + 1. 왼쪽 아이콘.
									"id", packageName));
					rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
					break;
				case 1:
					rp.addRule(RelativeLayout.ALIGN_TOP, 
							getResources().getIdentifier("carRegistrationPage_optionView" + index,				//i - 1 + 1. 왼쪽 아이콘.
									"id", packageName));
					rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					rp.rightMargin = ResizeUtils.getSpecificLength(35);
					break;
				}
			}
			
			view.setLayoutParams(rp);
			view.setId(getResources().getIdentifier("carRegistrationPage_optionView" + (index + 1), 
							"id", packageName));
			
			if(checked[index]) {
				view.setBackgroundResource(
						getResources().getIdentifier("detail_option" + (index + 1) + "_btn_b", 
								"drawable", packageName));
			} else {
				view.setBackgroundResource(
						getResources().getIdentifier("detail_option" + (index + 1) + "_btn_a", 
								"drawable", packageName));
			}
			
			
			return view;
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		return null;
	}

	public void downloadCarDetailModelInfo() {
		
		String url = null;
		
		if(trim_id != 0) {
			url = BCPAPIs.SEARCH_CAR_DETAIL_INFO
					+ "?trim_id=" + trim_id;
		} else if(car != null && car.getCar_id() != 0) {
			url = BCPAPIs.SEARCH_CAR_DETAIL_INFO
					+ "?car_id=" + car.getCar_id();
		} else {
			return;
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
						setCarDetailInfos();
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
					BCPDownloadUtils.downloadBitmap(url,
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
							}, 144);
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
					BCPDownloadUtils.downloadBitmap(car.getA_images()[i],
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
							}, 144);
					selectedImageSdCardPaths[4 + i] = url;
				}
			}
			
			downloadCarDetailModelInfo();
			
			//차량번호.
			etDetailCarInfos[0].getEditText().setText(car.getCar_number());
			
			//주행거리.
			etDetailCarInfos[1].getEditText().setText("" + car.getMileage());
			
			//배기량.
			etDetailCarInfos[2].getEditText().setText("" + car.getDisplacement());
			
			//색상.
			etDetailCarInfos[3].getEditText().setText("" + car.getColor());
			
			//차량 옵션 및 튜닝.
			if(car.getOptions() != null) {
				
				int size = car.getOptions().length;
				for(int i=0; i<size; i++) {
					checked[car.getOptions()[i] - 1] = true;
					setOption(car.getOptions()[i] - 1);
				}
			}
			
			//판매자 차량설명.
			etCarDescription.setText(car.getDesc());
		
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
	
	public void setOption(int index) {
		
		if(checked[index]) {
			optionViews[index].setBackgroundResource(
					getResources().getIdentifier("detail_option" + (index + 1) + "_btn_b", 
							"drawable", packageName));
		} else {
			optionViews[index].setBackgroundResource(
					getResources().getIdentifier("detail_option" + (index + 1) + "_btn_a", 
							"drawable", packageName));
		}
	}
	
	public void setProgress(int progressValue) {

		progressValue = Math.min(progressValue, 100);
		progressValue = Math.max(progressValue, 0);
		
		tvPercentage.invalidate();
		
		progressBar.setProgress(progressValue);
		tvPercentage.setText(progressValue + "%");
	}

	public void checkBundle() {
		
		if(mActivity.bundle != null) {

			if(mActivity.bundle.containsKey("requestCode")) {
				
				switch(mActivity.bundle.getInt("requestCode")) {
				
				case BCPConstants.REQUEST_SEARCH_TRIM:
					trim_id = mActivity.bundle.getInt("trim_id");
					clearCarDetailInfosRelatedWithModel();
					downloadCarDetailModelInfo();
					break;
					
				case BCPConstants.REQUEST_SEARCH_AREA:
					dong_id = mActivity.bundle.getInt("dong_id");
					areaString = mActivity.bundle.getString("address");
					break;
					
				case BCPConstants.REQUEST_SEARCH_MONTH:
					year = Integer.parseInt(mActivity.bundle.getString("year"));
					month = Integer.parseInt(mActivity.bundle.getString("month"));
					break;
				}
				
				setCarDetailInfos();
				
			//전화번호 인증 후.
			} else if(mActivity.bundle.containsKey("requestedPhoneNumber")) {
				//Bundle[{requestedPhoneNumber=01098138005, phone_auth_key=zOIyDFxNbQgXQQNcYufQwvtitoHItsUysLKcmqzSIEViBWLLumZfqDuFBcgmicgw}]
				ToastUtils.showToast(R.string.registringPhoneNumber);
				updatePhoneNumber(mActivity.bundle.getString("requestedPhoneNumber"), 
						mActivity.bundle.getString("phone_auth_key"));
			}
			
			mActivity.bundle = null;
		}
	}

	public void clearCarDetailInfosRelatedWithModel() {
		
		carModelDetailInfo = null;
		
		//trim.
		btnCarInfos[0].setText(null);
		
		//연식.
		btnCarInfos[1].setText(null);
		
		year = 0;
		month = 0;
	}
	
	public void setCarDetailInfos() {
		
		setCarInfos_trimYearMonth();
		setCarInfos_Area();
		setCarInfos_fuel();
		setCarInfos_Accident();
		setCarInfos_Owned();
		setCarInfos_Transmission();
		
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
		
		btnCheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				selectedImageIndex = ivPhotos.length - 1;
				mActivity.showUploadPhotoPopup(1, Color.rgb(254, 188, 42));
			}
		});
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
				
			case Car.TYPE_DIRECT:
				url = BCPAPIs.CAR_DIRECT_NORMAL_SAVE_URL;
				break;
			}
			
			if(url != null) {

				StringBuilder sb = new StringBuilder("");
				
				//onsalecar[id] : 수정 시에만 기재
				if(car != null) {
					sb.append("?onsalecar[id]=" + car.getId());
					sb.append("&");
				} else {
					sb.append("?");
				}
				
				//onsalecar[car_id] : 차량 ID (브랜드, 모델, 트림 선택으로 나온 car_id)
				sb.append("&onsalecar[car_id]=").append(carModelDetailInfo.getId());
				
				//onsalecar[year], onsalecar[month] : 연식
				sb.append("&onsalecar[year]=").append(year);
				sb.append("&onsalecar[month]=").append(month);
				
				//onsalecar[had_accident] : 사고유무 (0: 사고여부 모름 / 1: 유사고 / 2: 무사고)
				sb.append("&onsalecar[had_accident]=").append(Car.getAccidentIndexToServer(selectedIndex_accident));
				
				if(!StringUtils.isEmpty(history)) {
					sb.append("&onsalecar[accident_desc]=").append(StringUtils.getUrlEncodedString(history));
				}
				
				//onsalecar[fuel_type] : 연료 타입 (gasoline/diesel/lpg)
				sb.append("&onsalecar[fuel_type]=").append(Car.getFuelString_ToServer(selectedIndex_fuel));
				
				//onsalecar[transmission_type] : 기어 타입 (auto/manual)
				sb.append("&onsalecar[transmission_type]=").append(Car.getTransmissionSring_ToServer(selectedIndex_transmission));
				
				//onsalecar[is_oneman_owned] : 1인신조(1: 1인신조차량 / 0: 1인신조 아님)
				sb.append("&onsalecar[is_oneman_owned]=").append(Car.getOwnedIndexToServer(selectedIndex_owned));
				
				//onsalecar[dong_id] : 판매지역 (동 검색 결과 id)
				sb.append("&onsalecar[dong_id]=").append(dong_id);
				
				//onsalecar[car_number] : 차량넘버
				sb.append("&onsalecar[car_number]=").append(StringUtils.getUrlEncodedString(etDetailCarInfos[0].getEditText()));
				
				//onsalecar[mileage] : 주행거리
				sb.append("&onsalecar[mileage]=").append(StringUtils.getUrlEncodedString(etDetailCarInfos[1].getEditText()));
				
				//onsalecar[displacement] : 배기량
				sb.append("&onsalecar[displacement]=").append(StringUtils.getUrlEncodedString(etDetailCarInfos[2].getEditText()));
				
				//onsalecar[color] : 색상
				sb.append("&onsalecar[color]=").append(StringUtils.getUrlEncodedString(etDetailCarInfos[3].getEditText()));
				
				//onsalecar[price] : 가격
				sb.append("&onsalecar[price]=").append(StringUtils.getUrlEncodedString(etDetailCarInfos[4].getEditText()));
				
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
				sb.append("&onsalecar[desc]=").append(StringUtils.getUrlEncodedString(etCarDescription));
				
				if(carType == Car.TYPE_BID) {
					//onsalecar[to_sell_directly] : 즉시판매 (1: 즉시판매 / 즉시판매 아님)
					sb.append("&onsalecar[to_sell_directly]=").append(isImmediatlySaleClicked? "1" : "0");
				}
				
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
				
				//onsalecar[inspection_note_url] : 성능점검기록부 이미지 URL
				if(selectedImageSdCardPaths != null) {
					sb.append("&onsalecar[inspection_note_url]=").append(selectedImageSdCardPaths[8]);
				}
				
				url += sb.toString();
				
				DownloadUtils.downloadJSONString(url,
						new OnJSONDownloadListener() {

							@Override
							public void onError(String url) {

								LogUtils.log("CarRegistrationPage.register.onError." + "\nurl : " + url);
								mActivity.hideLoadingView();
							}

							@Override
							public void onCompleted(String url,
									JSONObject objJSON) {

								try {
									LogUtils.log("CarRegistrationPage.register.onCompleted."
											+ "\nurl : " + url + "\nresult : "
											+ objJSON);

									if(objJSON.getInt("result") == 1) {
										SharedPrefsUtils.clearPrefs(BCPConstants.PREFS_REG + carType);
										isRegistrationCompleted = true;
										pageRequestCompleted();
									} else {
										ToastUtils.showToast(objJSON.getString("message"));
									}
								} catch (Exception e) {
									LogUtils.trace(e);
								} catch (OutOfMemoryError oom) {
									LogUtils.trace(oom);
								} finally {
									mActivity.hideLoadingView();
								}
							}
						}, mActivity.getLoadingView());
			} else {
				mActivity.hideLoadingView();
			}
		} catch (Exception e) {
			LogUtils.trace(e);
			mActivity.hideLoadingView();
		} catch (Error e) {
			LogUtils.trace(e);
			mActivity.hideLoadingView();
		}
	}

	public void pageRequestCompleted() {
		
		//수정.
		if(car != null) {
			mActivity.showAlertDialog(R.string.edit, R.string.complete_edit, R.string.confirm, 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							mActivity.closeTopPage();
						}
					});
			
		//차량 등록.
		} else {
			((MainActivity) mActivity).showPopup(MainActivity.POPUP_REGISTRATION);
		}
	}

	public void uploadImages() {

		try {
			mActivity.showLoadingView();
			
			int size = selectedImageSdCardPaths.length;
			
			for(int i=0; i<size; i++) {
				
				if(!StringUtils.isEmpty(selectedImageSdCardPaths[i])
						&& !selectedImageSdCardPaths[i].contains("http://")) {
					
					ToastUtils.showToast(R.string.uploadingImage);
					
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
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void checkProgress() {
		
		int progress = 0;
		
		//트림, 연식, 지역 개당 5, 총 15.
		for(int i=0; i<btnCarInfos.length; i++) {
			
			if(btnCarInfos[i].length() > 0) {
				progress += 5;
			}
		}
		
		//차량 사진 개당 10, 총 40.
		for(int i=0; i<4; i++) {
			if(selectedImageSdCardPaths[i] != null) {
				progress += 10;
			}
		}
		
		if(forDealer) {
			//세부차량 정보 개당 5, 총 20, 가격은 제외.
			
			for(int i=0; i<4; i++) {
				if(etDetailCarInfos[i].getEditText().length() > 0) {
					progress += 5;
				}
			}
		} else {
			//세부차량 정보 개당 10, 총 30, 가격은 제외.
			
			for(int i=0; i<3; i++) {
				if(etDetailCarInfos[i].getEditText().length() > 0) {
					progress += 10;
				}
			}
		}
		
		//약관, 15.
		if(isTermOfUseClicked) {
			progress += 15;
		}
		
		if(forDealer) {
			//성능점검 기록부, 10.
			if(selectedImageSdCardPaths[8] != null) {
				progress += 10;
			}
		}
		
		setProgress(progress);
	}

	public void closePage() {
		
		ToastUtils.showToast(R.string.failToLoadCarInfo);
		
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				if(mActivity != null) {
					mActivity.closeTopPage();
				}
			}
		}, 1000);
	}

	public void showPopup(int type) {
		
		if(type == POPUP_IMMEDIATELY) {
			clock.setVisibility(View.VISIBLE);
			tvDesc.setVisibility(View.VISIBLE);
			etAccident.setVisibility(View.INVISIBLE);
			btnCompleteWriting.setVisibility(View.INVISIBLE);
		} else {
			etAccident.setText(history);
			
			clock.setVisibility(View.INVISIBLE);
			tvDesc.setVisibility(View.INVISIBLE);
			etAccident.setVisibility(View.VISIBLE);
			btnCompleteWriting.setVisibility(View.VISIBLE);
		}
		
		AlphaAnimation aaIn = new AlphaAnimation(0, 1);
		aaIn.setDuration(300);

		cover.setVisibility(View.VISIBLE);
		cover.startAnimation(aaIn);
		popup.setVisibility(View.VISIBLE);
		popup.startAnimation(aaIn);
	}
	
	public void hidePopup() {
		
		AlphaAnimation aaOut = new AlphaAnimation(1, 0);
		aaOut.setDuration(300);
		
		cover.startAnimation(aaOut);
		cover.setVisibility(View.INVISIBLE);
		popup.startAnimation(aaOut);
		popup.setVisibility(View.INVISIBLE);
	}
	
	public void updatePhoneNumber(final String requestedPhoneNumber, String phone_auth_key) {
		
		/*
		http://dev.bye-car.com/users/update/additional_info.json
			?phone_auth_key=abc
		*/
		String url = BCPAPIs.EDIT_USER_INFO_UR_COMMON
				+ "?phone_auth_key=" + phone_auth_key;
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
						MainActivity.user.setPhone_number(requestedPhoneNumber);
						ToastUtils.showToast(R.string.complete_certifyPhoneNumber);
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

	public void setCarInfos_trimYearMonth() {
		
		if(carModelDetailInfo != null) {
			//trim.
			btnCarInfos[0].setBackgroundResource(R.drawable.registration_car_info_box);
			btnCarInfos[0].setText(carModelDetailInfo.getFull_name());
			
			//연식.
			if(year != 0 && month != 0) {
				btnCarInfos[1].setText(year + "년 " + month + "월");
				btnCarInfos[1].setBackgroundResource(R.drawable.registration_car_info_box);
			} else {
				btnCarInfos[1].setText(null);
				btnCarInfos[1].setBackgroundResource(R.drawable.registration_car_info2_btn);
			}
		}
	}
	
	public void setCarInfos_Area() {
		
		//지역.
		if(!StringUtils.isEmpty(areaString)) {
			btnCarInfos[2].setText(areaString);
			btnCarInfos[2].setBackgroundResource(R.drawable.registration_car_info_box);
		} else {
			btnCarInfos[2].setText(null);
			btnCarInfos[2].setBackgroundResource(R.drawable.registration_car_info8_btn);
		}
	}
	
	public void setCarInfos_fuel() {
		
		int[] bgResIds_on = new int[] {
				R.drawable.registration_car_info41_btn_b,
				R.drawable.registration_car_info42_btn_b,
				R.drawable.registration_car_info43_btn_b,
				R.drawable.registration_car_info44_btn_b,
		};
		
		int[] bgResIds_off = new int[] {
				R.drawable.registration_car_info41_btn_a,
				R.drawable.registration_car_info42_btn_a,
				R.drawable.registration_car_info43_btn_a,
				R.drawable.registration_car_info44_btn_a,
		};
		
		for(int i=0; i<btnFuels.length; i++) {
			
			if(i == selectedIndex_fuel) {
				btnFuels[i].setBackgroundResource(bgResIds_on[i]);
			} else {
				btnFuels[i].setBackgroundResource(bgResIds_off[i]);
			}
		}
	}
	
	public void setCarInfos_Accident() {

		int[] bgResIds_on = new int[] {
				R.drawable.registration_car_info31_btn_b,
				R.drawable.registration_car_info32_btn_b,
				R.drawable.registration_car_info33_btn_b,
		};
		
		int[] bgResIds_off = new int[] {
				R.drawable.registration_car_info31_btn_a,
				R.drawable.registration_car_info32_btn_a,
				R.drawable.registration_car_info33_btn_a,
		};
		
		for(int i=0; i<btnAccidents.length; i++) {
			
			if(i == selectedIndex_accident) {
				btnAccidents[i].setBackgroundResource(bgResIds_on[i]);
			} else {
				btnAccidents[i].setBackgroundResource(bgResIds_off[i]);
			}
		}
	}

	public void setCarInfos_Owned() {

		int[] bgResIds_on = new int[] {
				R.drawable.registration_car_info61_btn_b,
				R.drawable.registration_car_info62_btn_b,
		};
		
		int[] bgResIds_off = new int[] {
				R.drawable.registration_car_info61_btn_a,
				R.drawable.registration_car_info62_btn_a,
		};
		
		for(int i=0; i<btnOwneds.length; i++) {
			
			if(i == selectedIndex_owned) {
				btnOwneds[i].setBackgroundResource(bgResIds_on[i]);
			} else {
				btnOwneds[i].setBackgroundResource(bgResIds_off[i]);
			}
		}
	}

	public void setCarInfos_Transmission() {

		int[] bgResIds_on = new int[] {
				R.drawable.registration_car_info51_btn_b,
				R.drawable.registration_car_info52_btn_b,
		};
		
		int[] bgResIds_off = new int[] {
				R.drawable.registration_car_info51_btn_a,
				R.drawable.registration_car_info52_btn_a,
		};
		
		for(int i=0; i<btnTransmissions.length; i++) {
			
			if(i == selectedIndex_transmission) {
				btnTransmissions[i].setBackgroundResource(bgResIds_on[i]);
			} else {
				btnTransmissions[i].setBackgroundResource(bgResIds_off[i]);
			}
		}
	}
	
//////////////////// Classes.
	
	public class AsyncLoadThumbnail extends AsyncTask<Void, Void, Void> {

		private String path;
		private ImageView imageView;
		private Bitmap thumbnail;
		
		public AsyncLoadThumbnail(String path, ImageView imageView) {
			this.path = path;
			this.imageView = imageView;
		}
		
		@Override
		protected Void doInBackground(Void... params) {

			try {
				File image = new File(path);
				BitmapFactory.Options bmOptions = new BitmapFactory.Options();
				bmOptions.inSampleSize = BitmapUtils.getBitmapInSampleSize(path, 
						ResizeUtils.getSpecificLength(144)); 
				thumbnail = BitmapFactory.decodeFile(image.getAbsolutePath(), bmOptions);
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
		
			try {
				imageView.setImageBitmap(thumbnail);
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
		}
	}
}