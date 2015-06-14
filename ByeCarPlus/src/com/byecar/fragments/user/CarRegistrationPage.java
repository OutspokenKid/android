package com.byecar.fragments.user;

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
import com.byecar.fragments.TypeSearchCarPage;
import com.byecar.models.Car;
import com.byecar.models.CarModelDetailInfo;
import com.byecar.views.TitleBar;
import com.outspoken_kid.activities.BaseFragmentActivity;
import com.outspoken_kid.activities.MultiSelectGalleryActivity.OnAfterPickImageListener;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ImageUploadUtils;
import com.outspoken_kid.utils.ImageUploadUtils.OnAfterUploadImage;
import com.outspoken_kid.utils.BitmapUtils;
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
	
	public static final int TYPE_REGISTRATION = 0;
	public static final int TYPE_EDIT = 1;
	
	private int id;
	private int carType;
	private Car car;
	private CarModelDetailInfo carModelDetailInfo;
	
	private ProgressBar progressBar;
	private TextView tvPercentage;
	private TextView tvPercentage2;
	
	private TextView tvWriteAllContents;
	
	private TextView tvCarInfoTitle;
	private Button[] btnCarInfos;
	
	private TextView tvDetailCarInfoTitle;
	private HoloStyleEditText[] etDetailCarInfos;
	
	private TextView tvCarPhotoTitle;
	private TextView tvRightPhoto;
	private TextView tvAddedPhotoTitle;
	private Button[] btnPhotos;
	private ImageView[] ivPhotos;
	private TextView[] tvCarPhotos;
	
	private TextView tvAddedPhoto;
	
	private TextView tvOptionTitle;
	private RelativeLayout relativeForOption;
	private View[] optionViews = new View[OPTION_VIEW_SIZE];
	
	private TextView tvCarDescriptionTitle;
	private EditText etCarDescription;
	private TextView tvTextCount;
	
	private TextView tvTermOfUseTitle;
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
	
	private String[] carInfoStrings = new String[6];
	private boolean[] checked = new boolean[OPTION_VIEW_SIZE];
	private int type;
	private boolean isTermOfUseClicked;
	private boolean isImmediatlySaleClicked;
	private int dong_id;
	private int year;
	private int month;
	private String history;
	private int trim_id;
	
	private int selectedImageIndex;
	private String[] selectedImageSdCardPaths = new String[8];
	private Bitmap[] carPhotoThumbnails = new Bitmap[8];
	
	private boolean isDownloadedCarInfo;
	private boolean isSetInfo;
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
				//id.
				if(savedInstanceState.containsKey("id")) {
					id = savedInstanceState.getInt("id");
				}
				
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
				
				//carInfoStrings.
				for(int i=0; i<carInfoStrings.length; i++) {
				
					if(savedInstanceState.containsKey("carInfoStrings" + i)) {
						carInfoStrings[i] = savedInstanceState.getString("carInfoStrings" + i);
					}
				}
				
				setBtnCarInfos();
				
				//checked.
				for(int i=0; i<checked.length; i++) {
					checked[i] = savedInstanceState.getBoolean("checked" + i);
				}
				
				//type.
				type = savedInstanceState.getInt("type");
				
				//isTermOfUseClicked.
				isTermOfUseClicked = savedInstanceState.getBoolean("isTermOfUseClicked");
				
				//isImmediatlySaleClicked.
				isImmediatlySaleClicked = savedInstanceState.getBoolean("isImmediatlySaleClicked");
				
				//dong_id.
				dong_id = savedInstanceState.getInt("dong_id");
				
				//year.
				year = savedInstanceState.getInt("year");
				
				//month.
				month = savedInstanceState.getInt("month");
				
				//history.
				history = savedInstanceState.getString("history");
				
				//trim_id.
				trim_id = savedInstanceState.getInt("trim_id");
				
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
				
				//isDownloadedCarInfo.
				isDownloadedCarInfo = savedInstanceState.getBoolean("isDownloadedCarInfo");
				
				//isSetInfo.
				isSetInfo = savedInstanceState.getBoolean("isSetInfo");
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
			
		//이전 작성 불러오기.
		} else if(!isSetInfo) {
			loadInfos();
		}
	};
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		try {
			//id.
			outState.putInt("id", id);
			
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
			
			//carInfoStrings.
			for(int i=0; i<carInfoStrings.length; i++) {
			
				if(!StringUtils.isEmpty(carInfoStrings[i])) {
					outState.putString("carInfoStrings" + i, carInfoStrings[i]);
				}
			}
			
			//checked.
			for(int i=0; i<checked.length; i++) {
				outState.putBoolean("checked" + i, checked[i]);
			}
			
			//type.
			outState.putInt("type", type);
			
			//isTermOfUseClicked.
			outState.putBoolean("isTermOfUseClicked", isTermOfUseClicked);
			
			//isImmediatlySaleClicked.
			outState.putBoolean("isImmediatlySaleClicked", isImmediatlySaleClicked);
			
			//dong_id.
			outState.putInt("dong_id", dong_id);
			
			//year.
			outState.putInt("year", year);
			
			//month.
			outState.putInt("month", month);
			
			//history.
			outState.putString("history", history);
			
			//trim_id.
			outState.putInt("trim_id", trim_id);
			
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
			
			//isDownloadedCarInfo.
			outState.putBoolean("isDownloadedCarInfo", isDownloadedCarInfo);
			
			//isSetInfo.
			outState.putBoolean("isSetInfo", isSetInfo);
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	@Override
 	public void bindViews() {
		
		titleBar = (TitleBar) mThisView.findViewById(R.id.carRegistrationPage_titleBar);
		
		progressBar = (ProgressBar) mThisView.findViewById(R.id.carRegistrationPage_progressBar);
		tvPercentage = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvPercentage);
		tvPercentage2 = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvPercentage2);
		
		tvWriteAllContents = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvWriteAllContents);
		
		tvCarInfoTitle = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvCarInfoTitle);

		btnCarInfos = new Button[7];
		btnCarInfos[0] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarInfo1);
		btnCarInfos[1] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarInfo2);
		btnCarInfos[2] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarInfo3);
		btnCarInfos[3] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarInfo4);
		btnCarInfos[4] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarInfo5);
		btnCarInfos[5] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarInfo6);
		btnCarInfos[6] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarInfo7);
		
		tvDetailCarInfoTitle = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvDetailCarInfoTitle);
		etDetailCarInfos = new HoloStyleEditText[4];
		etDetailCarInfos[0] = (HoloStyleEditText) mThisView.findViewById(R.id.carRegistrationPage_etDetailCarInfo1);
		etDetailCarInfos[1] = (HoloStyleEditText) mThisView.findViewById(R.id.carRegistrationPage_etDetailCarInfo2);
		etDetailCarInfos[2] = (HoloStyleEditText) mThisView.findViewById(R.id.carRegistrationPage_etDetailCarInfo3);
		etDetailCarInfos[3] = (HoloStyleEditText) mThisView.findViewById(R.id.carRegistrationPage_etDetailCarInfo4);
		
		tvCarPhotoTitle = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvCarPhotoTitle);
		tvRightPhoto = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvRightPhoto);
		tvAddedPhotoTitle = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvAddedPhotoTitle);
		
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
		
		tvCarPhotos = new TextView[4];
		tvCarPhotos[0] = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvCarPhoto1);
		tvCarPhotos[1] = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvCarPhoto2);
		tvCarPhotos[2] = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvCarPhoto3);
		tvCarPhotos[3] = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvCarPhoto4);
		
		tvAddedPhoto = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvAddedPhoto);
		
		tvOptionTitle = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvOptionTitle);
		relativeForOption = (RelativeLayout) mThisView.findViewById(R.id.carRegistrationPage_relativeForOption);
		
		tvCarDescriptionTitle = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvCarDescriptionTitle);
		etCarDescription = (EditText) mThisView.findViewById(R.id.carRegistrationPage_etCarDescription);
		tvTextCount = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvTextCount);
		
		tvTermOfUseTitle = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvTermOfUseTitle);
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
	}

	@Override
	public void setVariables() {
	
		if(getArguments() != null) {
			type = getArguments().getInt("type");
			
			id = getArguments().getInt("id");
			carType = getArguments().getInt("carType");
			car = (Car) getArguments().getSerializable("car");
		}
		
		for(int i=0; i<selectedImageSdCardPaths.length; i++) {
			selectedImageSdCardPaths[i] = null;
		}
	}

	@Override
	public void createPage() {

		int size = etDetailCarInfos.length;
		for(int i=0; i<size; i++) {
			etDetailCarInfos[i].getEditText().setTextColor(getResources().getColor(R.color.new_color_text_gray));
			etDetailCarInfos[i].getEditText().setHintTextColor(getResources().getColor(R.color.new_color_text_gray));
			
			//차량 번호.
			if(i == 0) {
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
					case 2:
						bundle.putInt("type", TypeSearchCarPage.TYPE_ACCIDENT);
						break;
					case 3:
						bundle.putInt("type", TypeSearchCarPage.TYPE_FUEL);
						break;
					case 4:
						bundle.putInt("type", TypeSearchCarPage.TYPE_TRANSMISSION);
						break;
					case 5:
						bundle.putInt("type", TypeSearchCarPage.TYPE_ONEMANOWNED);
						break;
					}
					
					mActivity.showPage(BCPConstants.PAGE_TYPE_SEARCH_CAR, bundle);
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

				showPopup();
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
		
		//tvCarInfoTitle.
		rp = (RelativeLayout.LayoutParams) tvCarInfoTitle.getLayoutParams();
		rp.width = LayoutParams.MATCH_PARENT;
		rp.height = ResizeUtils.getSpecificLength(40);
		tvCarInfoTitle.setPadding(titlePadding, 0, 0, 0);
		
		//btnCarInfos.
		for(int i=0; i<btnCarInfos.length; i++) {
			rp = (RelativeLayout.LayoutParams) btnCarInfos[i].getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(586);
			rp.height = ResizeUtils.getSpecificLength(82);
			rp.topMargin = ResizeUtils.getSpecificLength(38);
		}

		//tvDetailCarInfoTitle.
		rp = (RelativeLayout.LayoutParams) tvDetailCarInfoTitle.getLayoutParams();
		rp.width = LayoutParams.MATCH_PARENT;
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.topMargin = ResizeUtils.getSpecificLength(72);
		tvDetailCarInfoTitle.setPadding(titlePadding, 0, 0, 0);

		//etDetailCarInfos
		for(int i=0; i<etDetailCarInfos.length; i++) {
			rp = (RelativeLayout.LayoutParams) etDetailCarInfos[i].getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(586);
			rp.height = ResizeUtils.getSpecificLength(60);
			rp.topMargin = ResizeUtils.getSpecificLength(20);
		}
		
		//tvCarPhotoTitle.
		rp = (RelativeLayout.LayoutParams) tvCarPhotoTitle.getLayoutParams();
		rp.width = LayoutParams.MATCH_PARENT;
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.topMargin = ResizeUtils.getSpecificLength(50);
		tvCarPhotoTitle.setPadding(titlePadding, 0, 0, 0);
		
		//tvRightPhoto.
		rp = (RelativeLayout.LayoutParams) tvRightPhoto.getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(210);
		
		//tvAddedPhotoTitle.
		rp = (RelativeLayout.LayoutParams) tvAddedPhotoTitle.getLayoutParams();
		rp.width = LayoutParams.MATCH_PARENT;
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.topMargin = ResizeUtils.getSpecificLength(270);
		tvAddedPhotoTitle.setPadding(titlePadding, 0, 0, 0);
		
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
		
		//ivPhotos.
		for(int i=0; i<ivPhotos.length; i++) {
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
		
		//tvOptionTitle.
		rp = (RelativeLayout.LayoutParams) tvOptionTitle.getLayoutParams();
		rp.width = LayoutParams.MATCH_PARENT;
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.topMargin = ResizeUtils.getSpecificLength(40);
		tvOptionTitle.setPadding(titlePadding, 0, 0, 0);
		
		//tvCarDescriptionTitle.
		rp = (RelativeLayout.LayoutParams) tvCarDescriptionTitle.getLayoutParams();
		rp.width = LayoutParams.MATCH_PARENT;
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.topMargin = ResizeUtils.getSpecificLength(50);
		tvCarDescriptionTitle.setPadding(titlePadding, 0, 0, 0);
		
		//etCarDescription.
		rp = (RelativeLayout.LayoutParams) etCarDescription.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(160);
		rp.topMargin = ResizeUtils.getSpecificLength(33);
		
		//tvTermOfUseTitle.
		rp = (RelativeLayout.LayoutParams) tvTermOfUseTitle.getLayoutParams();
		rp.width = LayoutParams.MATCH_PARENT;
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.topMargin = ResizeUtils.getSpecificLength(80);
		tvTermOfUseTitle.setPadding(titlePadding, 0, 0, 0);
		
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
		
		FontUtils.setFontSize(tvPercentage, 24);
		FontUtils.setFontSize(tvPercentage2, 16);
		FontUtils.setFontSize(tvWriteAllContents, 18);
		FontUtils.setFontStyle(tvWriteAllContents, FontUtils.BOLD);
		
		FontUtils.setFontSize(tvCarInfoTitle, 24);
		FontUtils.setFontStyle(tvCarInfoTitle, FontUtils.BOLD);
		
		for(int i=0; i<btnCarInfos.length; i++) {
			FontUtils.setFontSize(btnCarInfos[i], 26);
		}
		
		FontUtils.setFontSize(tvDetailCarInfoTitle, 24);
		FontUtils.setFontStyle(tvDetailCarInfoTitle, FontUtils.BOLD);
		
		for(int i=0; i<etDetailCarInfos.length; i++) {
			FontUtils.setFontSize(etDetailCarInfos[i].getEditText(), 20);
		}
		
		FontUtils.setFontSize(tvCarPhotoTitle, 24);
		FontUtils.setFontStyle(tvCarPhotoTitle, FontUtils.BOLD);
		
		FontUtils.setFontSize(tvRightPhoto, 20);
		FontUtils.setFontStyle(tvRightPhoto, FontUtils.BOLD);
		
		FontUtils.setFontSize(tvAddedPhotoTitle, 24);
		FontUtils.setFontStyle(tvAddedPhotoTitle, FontUtils.BOLD);
		
		for(int i=0; i<tvCarPhotos.length; i++) {
			FontUtils.setFontSize(tvCarPhotos[i], 20);
		}
		
		FontUtils.setFontSize(tvAddedPhoto, 20);
		FontUtils.setFontStyle(tvAddedPhoto, FontUtils.BOLD);
		
		FontUtils.setFontSize(tvOptionTitle, 24);
		FontUtils.setFontStyle(tvOptionTitle, FontUtils.BOLD);
		
		FontUtils.setFontSize(tvCarDescriptionTitle, 24);
		FontUtils.setFontStyle(tvCarDescriptionTitle, FontUtils.BOLD);
		FontUtils.setFontAndHintSize(etCarDescription, 20, 20);
		FontUtils.setFontSize(tvTextCount, 20);
		
		FontUtils.setFontSize(tvTermOfUseTitle, 24);
		FontUtils.setFontStyle(tvTermOfUseTitle, FontUtils.BOLD);
		
		FontUtils.setFontSize(tvTitle, 30);
		FontUtils.setFontSize(tvDesc, 22);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_car_registration;
	}

	@Override
	public int getPageTitleTextResId() {

		if(type == TYPE_EDIT) {
			return R.string.pageTitle_carRegistration_edit;
		} else if(carType == Car.TYPE_BID) {
			return R.string.pageTitle_carRegistration_registration_bid;
		} else {
			return R.string.pageTitle_carRegistration_registration_direct;
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
		
		if(type == TYPE_EDIT && !isDownloadedCarInfo) {
			isDownloadedCarInfo = true;
			
			if(id != 0) {
				downloadCarInfo(carType);
			} else if(car != null){
				setCarInfo();
			} else {
				closePage();
			}
		}
		
		checkBundle();
		checkProgress();		
	}

	@Override
	public void onStop() {
		super.onStop();
		
		saveInfos();
	}
	
	@Override
	public int getRootViewResId() {

		return R.id.carRegistrationPage_mainLayout;
	}
	
//////////////////// Custom method.
	
	public void saveInfos() {

		if(id != 0 || car != null || isRegistrationCompleted) {
			return;
		}
		
		try {
			SharedPreferences prefs = mContext.getSharedPreferences(BCPConstants.PREFS_REG, Context.MODE_PRIVATE);
			Editor ed = prefs.edit();

			//carType.
			ed.putInt("carType", carType);
			
			//etDetailCarInfos.
			for(int i=0; i<etDetailCarInfos.length; i++) {
				ed.putString("etDetailCarInfos" + i, etDetailCarInfos[i].getEditText().getText().toString());
			}
			
			//etCarDescription.
			ed.putString("etCarDescription", etCarDescription.getText().toString());
			
			//carInfoStrings.
			for(int i=0; i<carInfoStrings.length; i++) {
				ed.putString("carInfoStrings" + i, carInfoStrings[i]);	
			}
			
			//checked.
			for(int i=0; i<checked.length; i++) {
				ed.putBoolean("checked" + i, checked[i]);
			}
			
			//type.
			ed.putInt("type", type);
			
			//isTermOfUseClicked.
			ed.putBoolean("isTermOfUseClicked", isTermOfUseClicked);
			
			//isImmediatlySaleClicked.
			ed.putBoolean("isImmediatlySaleClicked", isImmediatlySaleClicked);
			
			//dong_id.
			ed.putInt("dong_id", dong_id);
			
			//year.
			ed.putInt("year", year);
			
			//month.
			ed.putInt("month", month);
			
			//history.
			ed.putString("history", history);
			
			//trim_id.
			ed.putInt("trim_id", trim_id);
			
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
	
	public void loadInfos() {

		if(id != 0 || car != null) {
			return;
		}
		
		try {
			SharedPreferences prefs = mContext.getSharedPreferences(BCPConstants.PREFS_REG, Context.MODE_PRIVATE);

			if(!prefs.contains("carType")) {
				return;
			}
			
			//carType.
			carType = prefs.getInt("carType", 0);
			
			//etDetailCarInfos.
			for(int i=0; i<etDetailCarInfos.length; i++) {
				etDetailCarInfos[i].getEditText().setText(prefs.getString("etDetailCarInfos" + i, null));
			}
			
			//etCarDescription.
			etCarDescription.setText(prefs.getString("etCarDescription", null));
			
			//carInfoStrings.
			for(int i=0; i<carInfoStrings.length; i++) {
				carInfoStrings[i] = prefs.getString("carInfoStrings" + i, null);
			}
			
			//checked.
			for(int i=0; i<checked.length; i++) {
				checked[i] = prefs.getBoolean("checked" + i, false);
			}
			
			//type.
			type = prefs.getInt("type", 0);
			
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
			
			//year.
			year = prefs.getInt("year", 0);
			
			//month.
			month = prefs.getInt("month", 0);
			
			//history.
			history = prefs.getString("history", null);
			
			//trim_id.
			trim_id = prefs.getInt("trim_id", 0);
			downloadCarDetailModelInfo();

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
										"drawable", "com.byecar.byecarplus"));
					} else {
						optionViews[INDEX].setBackgroundResource(
								getResources().getIdentifier("detail_option" + (INDEX + 1) + "_btn_a", 
										"drawable", "com.byecar.byecarplus"));
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
						"id", "com.byecar.byecarplus"));
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
						"carRegistrationPage_optionTitleView1", "id", "com.byecar.byecarplus"));
				titleView.setBackgroundResource(R.drawable.registration_option_category1);
				break;
				
			case 1:
				rp.addRule(RelativeLayout.BELOW, 
						getResources().getIdentifier("carRegistrationPage_optionView26", 
								"id", "com.byecar.byecarplus"));
				titleView.setId(getResources().getIdentifier(
						"carRegistrationPage_optionTitleView2", "id", "com.byecar.byecarplus"));
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
										"id", "com.byecar.byecarplus"));
					} else {
						rp.addRule(RelativeLayout.BELOW, 
								getResources().getIdentifier("carRegistrationPage_optionView" + (index - 2),	//i - 3 + 1, 윗줄 아이콘. 
										"id", "com.byecar.byecarplus"));
						rp.topMargin = ResizeUtils.getSpecificLength(24);
					}
					
					break;
				case 1:
					rp.addRule(RelativeLayout.ALIGN_TOP, 
							getResources().getIdentifier("carRegistrationPage_optionView" + index,				//i - 1 + 1. 왼쪽 아이콘.
									"id", "com.byecar.byecarplus"));
					rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
					break;
				case 2:
					rp.addRule(RelativeLayout.ALIGN_TOP, 
							getResources().getIdentifier("carRegistrationPage_optionView" + index,				//i - 1 + 1. 왼쪽 아이콘.
									"id", "com.byecar.byecarplus"));
					rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					rp.rightMargin = ResizeUtils.getSpecificLength(35);
					break;
				}
			} else if(index == 24) {
				rp.addRule(RelativeLayout.BELOW, 
						getResources().getIdentifier("carRegistrationPage_optionView24",
								"id", "com.byecar.byecarplus"));
				rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
				rp.topMargin = ResizeUtils.getSpecificLength(24);
				
			} else if(index == 25) {
				rp.addRule(RelativeLayout.ALIGN_TOP, 
						getResources().getIdentifier("carRegistrationPage_optionView25",
								"id", "com.byecar.byecarplus"));
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
										"id", "com.byecar.byecarplus"));
					} else {
						rp.addRule(RelativeLayout.BELOW, 
								getResources().getIdentifier("carRegistrationPage_optionView" + (index - 2),	//i - 3 + 1, 윗줄 아이콘. 
										"id", "com.byecar.byecarplus"));
						rp.topMargin = ResizeUtils.getSpecificLength(24);
					}
					break;
				case 0:
					rp.addRule(RelativeLayout.ALIGN_TOP, 
							getResources().getIdentifier("carRegistrationPage_optionView" + index,				//i - 1 + 1. 왼쪽 아이콘.
									"id", "com.byecar.byecarplus"));
					rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
					break;
				case 1:
					rp.addRule(RelativeLayout.ALIGN_TOP, 
							getResources().getIdentifier("carRegistrationPage_optionView" + index,				//i - 1 + 1. 왼쪽 아이콘.
									"id", "com.byecar.byecarplus"));
					rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
					rp.rightMargin = ResizeUtils.getSpecificLength(35);
					break;
				}
			}
			
			view.setLayoutParams(rp);
			view.setId(getResources().getIdentifier("carRegistrationPage_optionView" + (index + 1), 
							"id", "com.byecar.byecarplus"));
			
			if(checked[index]) {
				view.setBackgroundResource(
						getResources().getIdentifier("detail_option" + (index + 1) + "_btn_b", 
								"drawable", "com.byecar.byecarplus"));
			} else {
				view.setBackgroundResource(
						getResources().getIdentifier("detail_option" + (index + 1) + "_btn_a", 
								"drawable", "com.byecar.byecarplus"));
			}
			
			
			return view;
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		return null;
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
			
		case Car.TYPE_DIRECT:
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
				
				if(trim_id != 0) {
					url = BCPAPIs.SEARCH_CAR_DETAIL_INFO
							+ "?trim_id=" + trim_id;
				} else {
					url = BCPAPIs.SEARCH_CAR_DETAIL_INFO
							+ "?car_id=" + car.getCar_id();
				}
			}
		//이전 작성 불러오기.
		} else if(trim_id != 0) {
			url = BCPAPIs.SEARCH_CAR_DETAIL_INFO
					+ "?trim_id=" + trim_id;
		} else if(mActivity.bundle != null
				&& mActivity.bundle.containsKey("trim_id")) {
			url = BCPAPIs.SEARCH_CAR_DETAIL_INFO
					+ "?trim_id=" + mActivity.bundle.getInt("trim_id");
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
						
						//차량 수정인 경우 car에서 정보 받아서 세팅.
						if(!isSetInfo) {
							isSetInfo = true;
							
							if(car != null) {
								
								//연식.
								carInfoStrings[0] = "" + car.getYear() + "년 " + car.getMonth() + "월";
								year = car.getYear();
								month = car.getMonth();

								//사고유무.
								carInfoStrings[1] = Car.getAccidentTypeString(mContext, car.getHad_accident());
								history = car.getAccident_desc();
								
								//연료.
								carInfoStrings[2] = Car.getFuelTypeString(mContext, car.getFuel_type()); 

								//변속기.
								carInfoStrings[3] = Car.getTransmissionTypeString(mContext, car.getTransmission_type());
								
								//1인신조.
								carInfoStrings[4] = Car.getOneManOwnedTypeString(mContext, car.getIs_oneman_owned());
								
								//판매지역.
								carInfoStrings[5] = car.getArea();
								dong_id = car.getDong_id();
								
							//이전 작성을 불러오는 경우 정보 그대로 유지. 
							} else {
								//Do nothing.
							}
							
						//차량 트림을 선택한 경우 판매지역을 제외한 나머지 정보 다 날리기.
						} else {
							//연식.
							carInfoStrings[0] = null;
							year = 0;
							month = 0;

							//사고유무.
							carInfoStrings[1] = null;
							history = null;
							
							//연료.
							carInfoStrings[2] = null; 

							//변속기.
							carInfoStrings[3] = null;
							
							//1인신조.
							carInfoStrings[4] = null;
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
							"drawable", "com.byecar.byecarplus"));
		} else {
			optionViews[index].setBackgroundResource(
					getResources().getIdentifier("detail_option" + (index + 1) + "_btn_a", 
							"drawable", "com.byecar.byecarplus"));
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
				
				case BCPConstants.REQUEST_SEARCH_AREA:

					carInfoStrings[5] = mActivity.bundle.getString("address");
					dong_id = mActivity.bundle.getInt("dong_id");
					break;
					
				case BCPConstants.REQUEST_SEARCH_MONTH:
					year = Integer.parseInt(mActivity.bundle.getString("year"));
					month = Integer.parseInt(mActivity.bundle.getString("month"));
					carInfoStrings[0] = year + "년 " + month + "월";
					break;
				}
				
				setBtnCarInfos();
				
			//전화번호 인증 후.
			} else if(mActivity.bundle.containsKey("requestedPhoneNumber")) {
				//Bundle[{requestedPhoneNumber=01098138005, phone_auth_key=zOIyDFxNbQgXQQNcYufQwvtitoHItsUysLKcmqzSIEViBWLLumZfqDuFBcgmicgw}]
				ToastUtils.showToast(R.string.registringPhoneNumber);
				updatePhoneNumber(mActivity.bundle.getString("requestedPhoneNumber"), 
						mActivity.bundle.getString("phone_auth_key"));
				
			} else {
				LogUtils.log("###CarRegistrationPage.checkBundle.  "
						+ "\n bundle : " + mActivity.bundle.toString());
				
				int type = mActivity.bundle.getInt("type");
				
				if(type == TypeSearchCarPage.TYPE_TRIM) {
					trim_id = mActivity.bundle.getInt("trim_id");
					carModelDetailInfo = null;
					carInfoStrings[0] = null;
					btnCarInfos[0].setText(null);
					downloadCarDetailModelInfo();
				} else {
					
					try {
						if(type == TypeSearchCarPage.TYPE_ACCIDENT
								&& mActivity.bundle.containsKey("history")) {
							history = mActivity.bundle.getString("history");
						}
						
						carInfoStrings[type - 5] = mActivity.bundle.getString("text");
						setBtnCarInfos();
					} catch (Exception e) {
						LogUtils.trace(e);
					}
				}
			}
			
			mActivity.bundle = null;
		}
	}

	public void setBtnCarInfos() {
		
		if(carModelDetailInfo != null) {
			btnCarInfos[0].setBackgroundResource(R.drawable.registration_car_info_box);
			btnCarInfos[0].setText(carModelDetailInfo.getFull_name());

			//마지막 버튼(판매지역)은 따로 설정.
			for(int i=0; i<carInfoStrings.length-1; i++) {
				
				if(!StringUtils.isEmpty(carInfoStrings[i])) {
					btnCarInfos[i+1].setText(carInfoStrings[i]);
					btnCarInfos[i+1].setBackgroundResource(R.drawable.registration_car_info_box);
				} else {
					int resId = getResources().getIdentifier("registration_car_info" + (i + 2) + "_btn", "drawable", "com.byecar.byecarplus");
					btnCarInfos[i+1].setBackgroundResource(resId);
					btnCarInfos[i+1].setText(null);
				}
			}
		} else {
			btnCarInfos[0].setBackgroundResource(R.drawable.registration_car_info1_btn);
			btnCarInfos[0].setText(null);
		}
		
		if(!StringUtils.isEmpty(carInfoStrings[5])) {
			btnCarInfos[6].setText(carInfoStrings[5]);
			btnCarInfos[6].setBackgroundResource(R.drawable.registration_car_info_box);
		} else {
			int resId = getResources().getIdentifier("registration_car_info7_btn", "drawable", "com.byecar.byecarplus");
			btnCarInfos[6].setBackgroundResource(resId);
			btnCarInfos[6].setText(null);
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
				sb.append("&onsalecar[had_accident]=").append(Car.getAccidentOriginString(mContext, carInfoStrings[1]));
				
				if(!StringUtils.isEmpty(history)) {
					sb.append("&onsalecar[accident_desc]=").append(StringUtils.getUrlEncodedString(history));
				}
				
				//onsalecar[fuel_type] : 연료 타입 (gasoline/diesel/lpg)
				sb.append("&onsalecar[fuel_type]=").append(Car.getFuelOriginString(mContext, carInfoStrings[2]));
				
				//onsalecar[transmission_type] : 기어 타입 (auto/manual)
				sb.append("&onsalecar[transmission_type]=").append(Car.getTransmissionOriginString(mContext, carInfoStrings[3]));
				
				//onsalecar[is_oneman_owned] : 1인신조(1: 1인신조차량 / 0: 1인신조 아님)
				sb.append("&onsalecar[is_oneman_owned]=").append(Car.getOneManOwnedOriginString(mContext, carInfoStrings[4]));
				
				//onsalecar[dong_id] : 판매지역 (동 검색 결과 id)
				sb.append("&onsalecar[dong_id]=").append(dong_id);
				
				//onsalecar[car_number] : 차량넘버
				sb.append("&onsalecar[car_number]=").append(StringUtils.getUrlEncodedString(etDetailCarInfos[0].getEditText()));
				
				//onsalecar[mileage] : 주행거리
				sb.append("&onsalecar[mileage]=").append(StringUtils.getUrlEncodedString(etDetailCarInfos[1].getEditText()));
				
				//onsalecar[displacement] : 배기량
				sb.append("&onsalecar[displacement]=").append(StringUtils.getUrlEncodedString(etDetailCarInfos[2].getEditText()));
				
				//onsalecar[price] : 가격
				sb.append("&onsalecar[price]=").append(StringUtils.getUrlEncodedString(etDetailCarInfos[3].getEditText()));
				
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
										SharedPrefsUtils.clearPrefs(BCPConstants.PREFS_REG);
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
		
		//차량 사진 개당 5, 총 20.
		for(int i=0; i<4; i++) {
			if(selectedImageSdCardPaths[i] != null) {
				progress += 5;
			}
		}
		
		//차량검색 7, 총 35.
		for(int i=0; i<btnCarInfos.length; i++) {
			
			if(btnCarInfos[i].length() > 0) {
				progress += 5;
			}
		}
		
		//세부차량 정보 개당 10, 총 30, 가격은 제외.
		for(int i=0; i<3; i++) {
			if(etDetailCarInfos[i].getEditText().length() > 0) {
				progress += 10;
			}
		}
		
		//약관, 15.
		if(isTermOfUseClicked) {
			progress += 15;
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

	public void showPopup() {
		
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