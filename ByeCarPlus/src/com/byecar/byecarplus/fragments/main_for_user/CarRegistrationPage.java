package com.byecar.byecarplus.fragments.main_for_user;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
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

import com.byecar.byecarplus.MainForUserActivity;
import com.byecar.byecarplus.R;
import com.byecar.byecarplus.classes.BCPAPIs;
import com.byecar.byecarplus.classes.BCPConstants;
import com.byecar.byecarplus.classes.BCPFragment;
import com.byecar.byecarplus.models.CarModelDetailInfo;
import com.byecar.byecarplus.models.User;
import com.byecar.byecarplus.views.TitleBar;
import com.outspoken_kid.activities.BaseFragmentActivity;
import com.outspoken_kid.activities.MultiSelectGalleryActivity.OnAfterPickImageListener;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo.holo_light.HoloStyleEditText;

public class CarRegistrationPage extends BCPFragment {

	private static final int MIN_DESC_COUNT = 0;
	private static final int MAX_DESC_COUNT = 200;
	
	public static final int TYPE_REGISTRATION = 0;
	public static final int TYPE_REQUEST_CERTIFICATION = 1;
	public static final int TYPE_EDIT = 2;
	
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
	private TextView tvCarInfoText;
	private Button btnCarInfos[];
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
	
	private CarModelDetailInfo carModelDetailInfo;
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
			
			relativeForOption.setVisibility(View.GONE);
			lineAfterCarOption.setVisibility(View.GONE);
			
			tvCarDescriptionFromDealer.setVisibility(View.GONE);
			etCarDescriptionFromDealer.setVisibility(View.GONE);
			tvTextCount.setVisibility(View.GONE);
			
			tvWriteAllContents.setText(R.string.writeAllContentForRequestCertification);
			btnRequest.setVisibility(View.VISIBLE);
			btnComplete.setVisibility(View.INVISIBLE);
		} else {
			tvWriteAllContents.setText(R.string.writeAllContentForRegistration);
			btnRequest.setVisibility(View.INVISIBLE);
			btnComplete.setVisibility(View.VISIBLE);
		}
		
		if(type == TYPE_REQUEST_CERTIFICATION
				|| type == TYPE_EDIT) {
			immediatlySale.setVisibility(View.GONE);
			btnImmediatlySale.setVisibility(View.GONE);
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
					register();
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
		
		//btnCarPhoto1.
		rp = (RelativeLayout.LayoutParams) btnPhotos[0].getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(144);
		rp.height = ResizeUtils.getSpecificLength(110);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		rp.topMargin = ResizeUtils.getSpecificLength(6);
		
		//ivPhotos.
		rp = (RelativeLayout.LayoutParams) ivPhotos[0].getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(144);
		rp.height = ResizeUtils.getSpecificLength(110);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		rp.topMargin = ResizeUtils.getSpecificLength(6);
		
		//btnCarPhoto2.
		rp = (RelativeLayout.LayoutParams) btnPhotos[1].getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(144);
		rp.height = ResizeUtils.getSpecificLength(110);
		rp.leftMargin = ResizeUtils.getSpecificLength(10);
		
		//ivPhotos.
		rp = (RelativeLayout.LayoutParams) ivPhotos[1].getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(144);
		rp.height = ResizeUtils.getSpecificLength(110);
		rp.leftMargin = ResizeUtils.getSpecificLength(10);
		
		//btnCarPhoto3.
		rp = (RelativeLayout.LayoutParams) btnPhotos[2].getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(144);
		rp.height = ResizeUtils.getSpecificLength(110);
		rp.leftMargin = ResizeUtils.getSpecificLength(10);
		
		//ivPhotos.
		rp = (RelativeLayout.LayoutParams) ivPhotos[2].getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(144);
		rp.height = ResizeUtils.getSpecificLength(110);
		rp.leftMargin = ResizeUtils.getSpecificLength(10);
		
		//btnCarPhoto4.
		rp = (RelativeLayout.LayoutParams) btnPhotos[3].getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(144);
		rp.height = ResizeUtils.getSpecificLength(110);
		rp.leftMargin = ResizeUtils.getSpecificLength(10);
		
		//ivPhotos.
		rp = (RelativeLayout.LayoutParams) ivPhotos[3].getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(144);
		rp.height = ResizeUtils.getSpecificLength(110);
		rp.leftMargin = ResizeUtils.getSpecificLength(10);
		
		//tvMainImageText.
		rp = (RelativeLayout.LayoutParams) tvMainImageText.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(144);
		
		//tvAddedPhotoText.
		rp = (RelativeLayout.LayoutParams) tvAddedPhotoText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		
		//btnAddedPhoto1.
		rp = (RelativeLayout.LayoutParams) btnPhotos[4].getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(144);
		rp.height = ResizeUtils.getSpecificLength(110);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		rp.topMargin = ResizeUtils.getSpecificLength(6);
		
		//ivAddedPhoto1.
		rp = (RelativeLayout.LayoutParams) ivPhotos[4].getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(144);
		rp.height = ResizeUtils.getSpecificLength(110);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		rp.topMargin = ResizeUtils.getSpecificLength(6);
		
		//btnAddedPhoto2.
		rp = (RelativeLayout.LayoutParams) btnPhotos[5].getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(144);
		rp.height = ResizeUtils.getSpecificLength(110);
		rp.leftMargin = ResizeUtils.getSpecificLength(10);
		
		//ivAddedPhoto2.
		rp = (RelativeLayout.LayoutParams) ivPhotos[5].getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(144);
		rp.height = ResizeUtils.getSpecificLength(110);
		rp.leftMargin = ResizeUtils.getSpecificLength(10);
		
		//btnAddedPhoto3.
		rp = (RelativeLayout.LayoutParams) btnPhotos[6].getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(144);
		rp.height = ResizeUtils.getSpecificLength(110);
		rp.leftMargin = ResizeUtils.getSpecificLength(10);
		
		//ivAddedPhoto3.
		rp = (RelativeLayout.LayoutParams) ivPhotos[6].getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(144);
		rp.height = ResizeUtils.getSpecificLength(110);
		rp.leftMargin = ResizeUtils.getSpecificLength(10);
		
		//btnAddedPhoto4.
		rp = (RelativeLayout.LayoutParams) btnPhotos[7].getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(144);
		rp.height = ResizeUtils.getSpecificLength(110);
		rp.leftMargin = ResizeUtils.getSpecificLength(10);
		
		//ivAddedPhoto4.
		rp = (RelativeLayout.LayoutParams) ivPhotos[7].getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(144);
		rp.height = ResizeUtils.getSpecificLength(110);
		rp.leftMargin = ResizeUtils.getSpecificLength(10);

		//tvCarInfo.
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
		
		setDealerInfo();
		checkCarInfos();
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

		User user = ((MainForUserActivity) mActivity).getUser();
		
		if(!StringUtils.isEmpty(user.getPhone_number())) {
			tvDealerInfo.setTextColor(getResources().getColor(R.color.color_green));
			tvDealerInfo.setText(user.getName()
					+ "\n" + user.getPhone_number()
					+ "\n" + user.getAddress());
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
			
			switch(type) {
			
			case TypeSearchCarPage.TYPE_TRIM:
				String url = BCPAPIs.SEARCH_CAR_DETAIL_INFO
				+ "?trim_id=" + mActivity.bundle.getInt("trim_id");
				DownloadUtils.downloadJSONString(url,
					new OnJSONDownloadListener() {
	
						@Override
						public void onError(String url) {
	
							LogUtils.log("AuctionRegistrationPage.onError." + "\nurl : " + url);
	
						}
	
						@Override
						public void onCompleted(String url,
								JSONObject objJSON) {
	
							try {
								LogUtils.log("AuctionRegistrationPage.onCompleted."
										+ "\nurl : " + url
										+ "\nresult : " + objJSON);

								carModelDetailInfo = new CarModelDetailInfo(objJSON.getJSONObject("car"));
								checkCarInfos();
							} catch (Exception e) {
								LogUtils.trace(e);
							} catch (OutOfMemoryError oom) {
								LogUtils.trace(oom);
							}
						}
					});
				break;
				
			case TypeSearchCarPage.TYPE_YEAR:
			case TypeSearchCarPage.TYPE_FUEL:
			case TypeSearchCarPage.TYPE_TRANSMISSION:
			case TypeSearchCarPage.TYPE_ACCIDENT:
			case TypeSearchCarPage.TYPE_ONEMANOWNED:
				carInfoStrings[type - 5] = mActivity.bundle.getString("text");
				checkCarInfos();
				break;
			}
			
			mActivity.bundle = null;
		}
	}

	public void checkCarInfos() {
		
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

		((MainForUserActivity) mActivity).showPopup(MainForUserActivity.POPUP_REGISTRATION);
	}

	public void requestCertification() {
		
		((MainForUserActivity) mActivity).showPopup(MainForUserActivity.POPUP_REQUEST_CERTIFICATION);
	}

	public void checkProgress() {
		
		int progress = 0;
		
		if(type == TYPE_EDIT|| type == TYPE_REGISTRATION) {
			
			//판매자 정보 인증 20.
			if(tvDealerInfoCertified.getVisibility() == View.VISIBLE) {
				progress += 20;
			}
			
			//차량 사진 개당 5, 총 20.
			for(int i=0; i<4; i++) {
				if(selectedImageSdCardPaths[i] != null) {
					progress += 5;
				}
			}
			
			//차량검색 5, 나머지 검색 개당 3, 총 20.
			for(int i=0; i<6; i++) {
				
				if(btnCarInfos[i].length() > 0) {
					progress += i==0? 5 : 3;
				}
			}
			
			//세부차량 정보 개당 4, 총 20.
			for(int i=0; i<5; i++) {
				if(etDetailCarInfos[i].getEditText().length() > 0) {
					progress += 4;
				}
			}
			
			//판매자 차량설명 20.
			if(etCarDescriptionFromDealer.length() > MIN_DESC_COUNT) {
				progress += 20;
			}
		} else {

			//판매자 정보 인증 30.
			if(tvDealerInfoCertified.getVisibility() == View.VISIBLE) {
				progress += 30;
			}
			
			//차량검색 10, 나머지 검색 개당 6, 총 40.
			for(int i=0; i<6; i++) {
				
				if(btnCarInfos[i].length() > 0) {
					progress += i==0? 10 : 6;
				}
			}
			
			//세부차량 정보 개당 6, 총 30.
			for(int i=0; i<5; i++) {
				if(etDetailCarInfos[i].getEditText().length() > 0) {
					progress += 6;
				}
			}
		}
		
		setProgress(progress);
	}
}

