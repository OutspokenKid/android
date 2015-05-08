package com.byecar.fragments.user;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.GuideActivity;
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
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo.holo_light.HoloStyleEditText;

public class CarRegistrationPage extends BCPFragment {

	private static final int OPTION_VIEW_SIZE = 31;
	private static final int MAX_DESC_COUNT = 200;
	
	private static final int MAX_PRICE = 50000;
	
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
	private TextView[] tvDetailCarInfos;
	
	private TextView tvCarPhotoTitle;
	private TextView tvRightPhoto;
	private TextView tvAddedPhotoTitle;
	private Button[] btnPhotos;
	private ImageView[] ivPhotos;
	private TextView[] tvCarPhotos;
	
	private TextView tvOptionTitle;
	private RelativeLayout relativeForOption;
	private View[] optionViews;
	
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
	private Button btnGuide;
	
	private String[] carInfoStrings = new String[8];
	private boolean[] checked;
	private int type;
	private boolean isTermOfUseClicked;
	private boolean isImmediatlySaleClicked;
	
	private int selectedImageIndex;
	private String[] selectedImageSdCardPaths = new String[8];
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
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
	};
	
	@Override
 	public void bindViews() {
		
		titleBar = (TitleBar) mThisView.findViewById(R.id.carRegistrationPage_titleBar);
		
		progressBar = (ProgressBar) mThisView.findViewById(R.id.carRegistrationPage_progressBar);
		tvPercentage = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvPercentage);
		tvPercentage2 = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvPercentage2);
		
		tvWriteAllContents = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvWriteAllContents);
		
		tvCarInfoTitle = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvCarInfoTitle);

		btnCarInfos = new Button[8];
		btnCarInfos[0] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarInfo1);
		btnCarInfos[1] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarInfo2);
		btnCarInfos[2] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarInfo3);
		btnCarInfos[3] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarInfo4);
		btnCarInfos[4] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarInfo5);
		btnCarInfos[5] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarInfo6);
		btnCarInfos[6] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarInfo7);
		btnCarInfos[7] = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnCarInfo8);
		
		tvDetailCarInfoTitle = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvDetailCarInfoTitle);
		etDetailCarInfos = new HoloStyleEditText[3];
		etDetailCarInfos[0] = (HoloStyleEditText) mThisView.findViewById(R.id.carRegistrationPage_etDetailCarInfo1);
		etDetailCarInfos[1] = (HoloStyleEditText) mThisView.findViewById(R.id.carRegistrationPage_etDetailCarInfo2);
		etDetailCarInfos[2] = (HoloStyleEditText) mThisView.findViewById(R.id.carRegistrationPage_etDetailCarInfo3);
		tvDetailCarInfos = new TextView[3];
		tvDetailCarInfos[0] = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvDetailCarInfo1);
		tvDetailCarInfos[1] = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvDetailCarInfo2);
		tvDetailCarInfos[2] = (TextView) mThisView.findViewById(R.id.carRegistrationPage_tvDetailCarInfo3);
		
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
		
		btnGuide = (Button) mThisView.findViewById(R.id.carRegistrationPage_btnGuide);
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

		titleBar.hideBottomLine();
		
		int size = etDetailCarInfos.length;
		for(int i=0; i<size; i++) {
			etDetailCarInfos[i].getEditText().setTextColor(getResources().getColor(R.color.color_orange));
			etDetailCarInfos[i].getEditText().setHintTextColor(getResources().getColor(R.color.color_orange));
		}
		
		etDetailCarInfos[0].setHint(R.string.hintForDetailCarInfo12);
		etDetailCarInfos[1].setHint(R.string.hintForDetailCarInfo22);
		etDetailCarInfos[2].setHint(R.string.hintForDetailCarInfo32);
		
		etCarDescription.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_DESC_COUNT)});
		tvTextCount.setText("0 / " + MAX_DESC_COUNT + "자");
	}

	@Override
	public void setListeners() {

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
					case 6:
						bundle.putInt("type", TypeSearchCarPage.TYPE_WD);
						break;
					case 7:
						bundle.putInt("type", TypeSearchCarPage.TYPE_AREA);
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
		
		btnGuide.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Intent intent = new Intent(mActivity, GuideActivity.class);
				intent.putExtra("type", GuideActivity.TYPE_USER_UPLOAD);
				mActivity.startActivity(intent);
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
		int editTextPadding = ResizeUtils.getSpecificLength(260);
		for(int i=0; i<etDetailCarInfos.length; i++) {
			rp = (RelativeLayout.LayoutParams) etDetailCarInfos[i].getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(586);
			rp.height = ResizeUtils.getSpecificLength(60);
			rp.topMargin = ResizeUtils.getSpecificLength(20);
			etDetailCarInfos[i].setPadding(editTextPadding, 0, 0, 0);
		}
		
		//tvDetailCarInfos
		for(int i=0; i<tvDetailCarInfos.length; i++) {
			rp = (RelativeLayout.LayoutParams) tvDetailCarInfos[i].getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(260);
			rp.height = ResizeUtils.getSpecificLength(60);
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
		
		//tvOptionTitle.
		rp = (RelativeLayout.LayoutParams) tvOptionTitle.getLayoutParams();
		rp.width = LayoutParams.MATCH_PARENT;
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.topMargin = ResizeUtils.getSpecificLength(80);
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
		
		//btnGuide.
		rp = (RelativeLayout.LayoutParams) btnGuide.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(60);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.topMargin = ResizeUtils.getSpecificLength(14);
		rp.rightMargin = ResizeUtils.getSpecificLength(10);

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
		
		for(int i=0; i<tvDetailCarInfos.length; i++) {
			FontUtils.setFontSize(tvDetailCarInfos[i], 20);
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
		
		FontUtils.setFontSize(tvOptionTitle, 24);
		FontUtils.setFontStyle(tvOptionTitle, FontUtils.BOLD);
		
		FontUtils.setFontSize(tvCarDescriptionTitle, 24);
		FontUtils.setFontStyle(tvCarDescriptionTitle, FontUtils.BOLD);
		FontUtils.setFontAndHintSize(etCarDescription, 20, 20);
		FontUtils.setFontSize(tvTextCount, 20);
		
		FontUtils.setFontSize(tvTermOfUseTitle, 24);
		FontUtils.setFontStyle(tvTermOfUseTitle, FontUtils.BOLD);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_car_registration;
	}

	@Override
	public int getPageTitleTextResId() {

		if(type == TYPE_EDIT) {
			return R.string.pageTitle_carRegistration_edit;
		} else {
			return R.string.pageTitle_carRegistration_registration;
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onResume() {
		super.onResume();
		
		if(relativeForOption.getChildCount() == 0) {
			addOptionViews();
		}
		
		if(type == TYPE_EDIT) {

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
	public int getRootViewResId() {

		return R.id.carRegistrationPage_mainLayout;
	}
	
//////////////////// Custom method.

	public void addOptionViews() {
		
		relativeForOption.removeAllViews();
		
		optionViews = new View[OPTION_VIEW_SIZE];
		checked = new boolean[OPTION_VIEW_SIZE];
		
		RelativeLayout.LayoutParams rp = null;
		

		for(int i=0; i<OPTION_VIEW_SIZE; i++) {

		//Texts.
			if(i == 0) {
				for(int j=0; j<3; j++) {
					relativeForOption.addView(getTextViewForOption(j));
				}
				
			} else if(i == 25) {
				View line = new View(mContext);
				rp = new RelativeLayout.LayoutParams(
						LayoutParams.MATCH_PARENT, 1);
				rp.leftMargin = ResizeUtils.getSpecificLength(26);
				rp.rightMargin = ResizeUtils.getSpecificLength(26);
				rp.addRule(RelativeLayout.BELOW, 
						getResources().getIdentifier("carRegistrationPage_optionTextView25", 
								"id", "com.byecar.byecarplus"));
				line.setLayoutParams(rp);
				line.setBackgroundColor(Color.rgb(232, 232, 232));
				relativeForOption.addView(line);
				
				relativeForOption.addView(getTextViewForOption(3));
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
	
	public TextView getTextViewForOption(int index) {
		
		try {
			TextView textView = new TextView(mContext);
			RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(
					index == 3? LayoutParams.WRAP_CONTENT : ResizeUtils.getSpecificLength(200), 
					ResizeUtils.getSpecificLength(80));
			
			switch(index) {
			
			case 0:
				rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				rp.leftMargin = ResizeUtils.getSpecificLength(25);
				textView.setId(getResources().getIdentifier(
						"carRegistrationPage_optionTextView1", "id", "com.byecar.byecarplus"));
				break;
				
			case 1:
				rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
				rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				break;
				
			case 2:
				rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				rp.rightMargin = ResizeUtils.getSpecificLength(25);
				break;
				
			case 3:
				rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				rp.addRule(RelativeLayout.BELOW, 
						getResources().getIdentifier("carRegistrationPage_optionView25", 
								"id", "com.byecar.byecarplus"));
				rp.leftMargin = ResizeUtils.getSpecificLength(35);
				textView.setId(getResources().getIdentifier(
						"carRegistrationPage_optionTextView2", "id", "com.byecar.byecarplus"));
				break;
			}
			
			rp.topMargin = ResizeUtils.getSpecificLength(20);
			textView.setLayoutParams(rp);
			textView.setText(getResources().getIdentifier(
					"detailOptionText" + (index + 1), "string", "com.byecar.byecarplus"));
			textView.setGravity(Gravity.CENTER);
			textView.setTextColor(getResources().getColor(R.color.holo_text));
			return textView;
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
								getResources().getIdentifier("carRegistrationPage_optionTextView1", 
										"id", "com.byecar.byecarplus"));
					} else {
						rp.addRule(RelativeLayout.BELOW, 
								getResources().getIdentifier("carRegistrationPage_optionView" + (index - 2),	//i - 3 + 1, 윗줄 아이콘. 
										"id", "com.byecar.byecarplus"));
					}
					
					rp.topMargin = ResizeUtils.getSpecificLength(24);
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
				
			} else if(index == 30) {
				rp.addRule(RelativeLayout.ALIGN_TOP, 
						getResources().getIdentifier("carRegistrationPage_optionView25",
								"id", "com.byecar.byecarplus"));
				rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				rp.rightMargin = ResizeUtils.getSpecificLength(35);
				
			} else {
				switch(index % 3) {
				
				case 1:
					rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
					rp.leftMargin = ResizeUtils.getSpecificLength(35);
					
					if(index == 25) {
						rp.addRule(RelativeLayout.BELOW, 
								getResources().getIdentifier("carRegistrationPage_optionTextView2", 
										"id", "com.byecar.byecarplus"));
					} else {
						rp.addRule(RelativeLayout.BELOW, 
								getResources().getIdentifier("carRegistrationPage_optionView" + (index - 2),	//i - 3 + 1, 윗줄 아이콘. 
										"id", "com.byecar.byecarplus"));
					}
					break;
				case 2:
					rp.addRule(RelativeLayout.ALIGN_TOP, 
							getResources().getIdentifier("carRegistrationPage_optionView" + index,				//i - 1 + 1. 왼쪽 아이콘.
									"id", "com.byecar.byecarplus"));
					rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
					break;
				case 0:
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
			view.setBackgroundResource(
					getResources().getIdentifier("detail_option" + (index + 1) + "_btn_a", 
							"drawable", "com.byecar.byecarplus"));
			
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

							//사고유무.
							carInfoStrings[1] = Car.getAccidentTypeString(mContext, car.getHad_accident());
							
							//연료.
							carInfoStrings[2] = Car.getFuelTypeString(mContext, car.getFuel_type()); 

							//변속기.
							carInfoStrings[3] = Car.getTransmissionTypeString(mContext, car.getTransmission_type());
							
							//1인신조.
							carInfoStrings[5] = Car.getOneManOwnedTypeString(mContext, car.getIs_oneman_owned());
							
							//2/4륜 구동.
							carInfoStrings[6] = car.getCar_wd();
							
							//판매지역.
							carInfoStrings[7] = car.getArea();
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
					optionViews[car.getOptions()[i] - 1].setBackgroundResource(
							getResources().getIdentifier("detail_option" + car.getOptions()[i] + "_btn_b", 
									"drawable", "com.byecar.byecarplus"));
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

			for(int i=0; i<carInfoStrings.length; i++) {
				
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
				
				//onsalecar[year] : 연식
				sb.append("&onsalecar[year]=").append(carInfoStrings[0]);
				
				//onsalecar[had_accident] : 사고유무 (0: 사고여부 모름 / 1: 유사고 / 2: 무사고)
				sb.append("&onsalecar[had_accident]=").append(Car.getAccidentOriginString(mContext, carInfoStrings[1]));
				
				//onsalecar[fuel_type] : 연료 타입 (gasoline/diesel/lpg)
				sb.append("&onsalecar[fuel_type]=").append(Car.getFuelOriginString(mContext, carInfoStrings[2]));
				
				//onsalecar[transmission_type] : 기어 타입 (auto/manual)
				sb.append("&onsalecar[transmission_type]=").append(Car.getTransmissionOriginString(mContext, carInfoStrings[3]));
				
				//onsalecar[is_oneman_owned] : 1인신조(1: 1인신조차량 / 0: 1인신조 아님)
				sb.append("&onsalecar[is_oneman_owned]=").append(Car.getOneManOwnedOriginString(mContext, carInfoStrings[4]));
				
				//2/4륜 구동.
//				sb.append("&onsalecar[is_oneman_owned]=").append(Car.getOneManOwnedOriginString(mContext, carInfoStrings[5]));
				
				//onsalecar[dong_id] : 판매지역 (동 검색 결과 id)
				sb.append("&onsalecar[dong_id]=").append(Car.getOneManOwnedOriginString(mContext, carInfoStrings[6]));
				
				//onsalecar[car_number] : 차량넘버
				sb.append("&onsalecar[car_number]=").append(StringUtils.getUrlEncodedString(etDetailCarInfos[0].getEditText()));
				
				//onsalecar[mileage] : 주행거리
				sb.append("&onsalecar[mileage]=").append(StringUtils.getUrlEncodedString(etDetailCarInfos[1].getEditText()));
				
				//onsalecar[displacement] : 배기량
				sb.append("&onsalecar[displacement]=").append(StringUtils.getUrlEncodedString(etDetailCarInfos[2].getEditText()));
				
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
						}, mActivity.getLoadingView());
			}
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
		}
	}

	public void uploadImages() {

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
		
		mActivity.hideLoadingView();
		
		//모든 이미지 업로드 완료.
		register();
	}
	
	public void checkProgress() {
		
		int progress = 0;
		
		LogUtils.log("###CarRegistrationPage.checkProgress.  ########################################");
		
		if(type == TYPE_EDIT|| type == TYPE_REGISTRATION) {

			//차량 사진 개당 5, 총 20.
			for(int i=0; i<4; i++) {
				if(selectedImageSdCardPaths[i] != null) {
					progress += 5;
					LogUtils.log("###CarRegistrationPage.checkProgress.  add 5 from photo.");
				}
			}
			
			//차량검색 5, 총 40.
			for(int i=0; i<btnCarInfos.length; i++) {
				
				if(btnCarInfos[i].length() > 0) {
					progress += 5;
					LogUtils.log("###CarRegistrationPage.checkProgress.  add 5 from search.");
				}
			}
			
			//세부차량 정보 개당 10, 총 30.
			for(int i=0; i<etDetailCarInfos.length; i++) {
				if(etDetailCarInfos[i].getEditText().length() > 0) {
					progress += 10;
					LogUtils.log("###CarRegistrationPage.checkProgress.  add 4 from detailInfo.");
				}
			}
			
//			//판매자 차량설명 10.
//			if(etCarDescriptionFromDealer.length() > MIN_DESC_COUNT) {
//				progress += 10;
//				LogUtils.log("###CarRegistrationPage.checkProgress.  add 10 from desc.");
//			}
		} else {
			
			//차량검색 개당 25, 총 50.
			if(btnCarInfos[0].length() > 0) {
				progress += 25;
			}
			
			if(btnCarInfos[1].length() > 0) {
				progress += 25;
			}
			
			//세부차량 정보 개당 25, 총 50.
			if(etDetailCarInfos[0].getEditText().length() > 0) {
				progress += 25;
			}
			
			if(etDetailCarInfos[2].getEditText().length() > 0) {
				progress += 25;
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
}